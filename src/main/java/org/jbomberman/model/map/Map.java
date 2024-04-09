package org.jbomberman.model.map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Rectangle2D;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;
import org.jbomberman.controller.KeyHandler;
import org.jbomberman.controller.MobHandler;
import org.jbomberman.model.entita.*;
import org.jbomberman.model.listener.*;
import org.jbomberman.model.powerups.*;
import org.jbomberman.model.tiles.GrassTile;
import org.jbomberman.model.tiles.ImmovableTile;
import org.jbomberman.model.tiles.Tile;
import org.jbomberman.view.Tiles;

@Getter
@Setter
public class Map extends Observable {

  /** The width of the map in tiles */
  private static final int MAP_WIDTH = 15; // Adjust map width to include border

  /** The height of the map in tiles */
  private static final int MAP_HEIGHT = 13; // Adjust map height to include border

  /** The level of the map */
  private Integer level = 1;

  /** The list of tiles in the map */
  private ArrayList<Tiles> numTileMap;

  /**
   * Singleton pattern method to get the instance of the Map class.
   *
   * @return the single instance of Map
   */
  private static Map instance;

  /** The list of entities in the map */
  private List<Entity> entities;

  /** The player entity */
  private Player player;

  /** The starting position on the x-axis */
  private int startingPosX;

  /** The starting position on the y-axis */
  private int startingPosY;

  /** The flag to determine if mobs are moving */
  private boolean mobsMovement;

  /** The list of hitboxes for the tiles */
  private List<Rectangle2D> tileHitBoxes;

  /** The scheduled executor service for handling mob movement */
  private ScheduledExecutorService executorService;

  /** The set of mobs affected by the explosion */
  private Set<Mob> mobsAffectedByExplosion = new HashSet<>();

  /** The list of power-ups in the map */
  private List<PowerUp> powerUps = new ArrayList<>();

  /** The scheduled executor service for handling power-up despawning */
  private ScheduledExecutorService despawnScheduler = Executors.newScheduledThreadPool(1);

  /** The hitbox for the exit tile */
  private Rectangle2D exitTileHitBox;

  /** The standard direction of the puropen mob */
  private Direction puropenDirection = Direction.NONE;

  /** The flag to determine if the exit tile has been spawned */
  private boolean exitTileSpawned = false;

  private Map() {
    executorService = Executors.newSingleThreadScheduledExecutor();
  }

  /**
   * Singleton pattern method to get the instance of the Map class.
   *
   * @return the single instance of Map
   */
  public static Map getInstance() {
    if (instance == null) {
      instance = new Map(); // Create a new instance
    }
    return instance;
  }

  /**
   * Loads the map from a given path.
   *
   * @param path the path of the map file
   * @throws LoadMapException if an error occurs while loading the map
   */
  public void loadMap(String path) {
    ArrayList<Tiles> matrix = new ArrayList<>();

    try (BufferedReader bufferedReader =
        new BufferedReader(new InputStreamReader(Map.class.getResourceAsStream(path)))) {
      for (String line = bufferedReader.readLine();
          line != null;
          line = bufferedReader.readLine()) {
        Arrays.stream(line.split(" "))
            .mapToInt(Integer::parseInt)
            .forEach(value -> matrix.add(Tiles.values()[value]));
      }
    } catch (IOException e) {
      throw new LoadMapException("Error while loading map!", e);
    }

    this.numTileMap = matrix;

    tileHitBoxes = new ArrayList<>();

    // Iterate through the Tile objects and add hitboxes of collidable tiles to the list
    for (int y = 0; y < MAP_HEIGHT; y++) {
      for (int x = 0; x < MAP_WIDTH; x++) {
        Tiles tileType = numTileMap.get(y * MAP_WIDTH + x);
        Tile tile = createTile(x, y, tileType);

        tileHitBoxes.add(tile.getHitBox());
      }
    }

    LoadMapData packageData = new LoadMapData(PackageType.LOADMAP, matrix);
    sendUpdate(packageData);
  }

  /**
   * Creates a tile based on the given parameters.
   *
   * @param x the x-coordinate of the tile
   * @param y the y-coordinate of the tile
   * @param tileType the type of the tile
   * @return the created Tile
   */
  private Tile createTile(int x, int y, Tiles tileType) {
    switch (tileType) {
      case GRASS:
        return new GrassTile(x, y); // Create and return a GrassTile instance
      case IMMOVABLE:
        return new ImmovableTile(x, y); // Create and return an ImmovableTile instance
      default:
        return new GrassTile(x, y); // Return null or handle the case appropriately
    }
  }

  /**
   * Sends an update to the observers.
   *
   * @param packageData the data to be sent
   */
  public void sendUpdate(PackageData packageData) {
    setChanged();
    notifyObservers(packageData);
    clearChanged();
  }

  /**
   * Loads entities from a given file path.
   *
   * @param filePath the path of the file
   * @throws LoadMapException if an error occurs while loading entities
   */
  public void loadEntities(String filePath) {
    try (InputStreamReader fileReader =
        new InputStreamReader(Map.class.getResourceAsStream(filePath))) {
      JsonObject jsonObject = JsonParser.parseReader(fileReader).getAsJsonObject();

      JsonObject player = jsonObject.get("player").getAsJsonObject();

      int posx = player.get("posx").getAsInt();
      int posy = player.get("posy").getAsInt();

      startingPosX = posx;
      startingPosY = posy;

      // Calculate the adjusted position to center the player sprite in the first tile
      int adjustedX = posx + 8; // Adjusted X position
      int adjustedY = posy + 12; // Adjusted Y position

      Direction direction = Direction.valueOf(player.get("direction").getAsString());

      int lives = (this.player == null) ? player.get("lives").getAsInt() : this.player.getLives();

      int score = (this.player == null) ? player.get("score").getAsInt() : this.player.getScore();

      int width = player.get("width").getAsInt();
      int height = player.get("height").getAsInt();

      this.player = new Player(adjustedX, adjustedY, width, height, lives, direction, score);

      JsonArray mobsJson = jsonObject.get("mob").getAsJsonArray();

      entities = new ArrayList<>();

      for (JsonElement mob : mobsJson) {

        int id = mob.getAsJsonObject().get("id").getAsInt();

        Type type = Type.valueOf(mob.getAsJsonObject().get("type").getAsString());

        posx = mob.getAsJsonObject().get("posx").getAsInt();
        posy = mob.getAsJsonObject().get("posy").getAsInt();

        direction = Direction.valueOf(mob.getAsJsonObject().get("direction").getAsString());

        int widthh = mob.getAsJsonObject().get("width").getAsInt();
        int heightt = mob.getAsJsonObject().get("height").getAsInt();

        entities.add(new Mob(posx, posy, widthh, heightt, type, direction, id));
      }

    } catch (IOException e) {
      throw new LoadMapException("error while reading json file: " + e.getMessage(), e);
    }
  }

  /**
   * Loads a level from a given level string.
   *
   * @param level the level string
   */
  public void loadLevel(String level) {

    Timeline timeline0 =
        new Timeline(
            new KeyFrame(
                Duration.seconds(3.5),
                event -> {
                  // Load the map from the file
                  loadMap(String.format("level/level%s/mappa_lvl%s.txt", level, level));

                  // Read the json file
                  loadEntities((String.format("level/level%s/level%s.json", level, level)));

                  // Ensure entities are drawn at their initial positions
                  sendInitialEntityPositions();

                  // thread per far muovere le entita' secondo il metodo move() all'interno di mob
                  moveEntities();
                }));
    timeline0.play();

    Timeline timeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(3.5),
                event -> {
                  // Call the method to update the player score after the delay
                  updatePlayerScore(player);

                  // Call the method to update the player lives after the delay
                  updatePlayerLives(player);
                }));
    timeline.play();
  }

  /** Sends the initial positions of the entities to the observers. */
  private void sendInitialEntityPositions() {

    int initialX = player.getX();
    int initialY = player.getY();

    sendUpdate(
        new PlayerInitialPositionData(PackageType.PLAYER_INITIAL_POSITION, initialX, initialY));

    for (Entity entity : entities) {
      // Create package data for the initial position of the entity
      PackageData packageData = createInitialPositionData(entity);
      // Send the initial position update to the GameView
      sendUpdate(packageData);
    }
  }

  /**
   * Creates package data for the initial position of the entity.
   *
   * @param entity the entity for which to create the initial position data
   * @return the package data for the initial position of the entity
   */
  private PackageData createInitialPositionData(Entity entity) {
    int initialX = entity.getX(); // Get the initial X position of the entity
    int initialY = entity.getY(); // Get the initial Y position of the entity

    if (entity instanceof Mob) {
      // If the entity is a mob, create initial position data for mob
      Mob mob = (Mob) entity;
      return new MobInitialPositionData(
          PackageType.MOB_INITIAL_POSITION, mob.getType(), mob.getId(), initialX, initialY);
    } else {
      // Handle other types of entities as needed
      return null;
    }
  }

  /** Moves entities in the map. */
  public void moveEntities() {
    mobsMovement = true;

    // Start the player's movement using AnimationTimer
    KeyHandler.getInstance().startMovement();

    // Start the mob's movement using AnimationTimer
    MobHandler.getInstance().startMobMovement();
  }

  /**
   * Moves the player in the map.
   *
   * @param xStep the x-coordinate step
   * @param yStep the y-coordinate step
   * @param delta the delta time
   */
  public void movePlayer(int xStep, int yStep, double delta) {
    int oldX = player.getX();
    int oldY = player.getY();

    // Determine the direction of movement based on xStep and yStep
    Direction direction = calculateDirection(xStep, yStep);

    Rectangle2D newHitBox = new Rectangle2D(player.getX() + xStep, player.getY() + yStep, 32, 32);

    if (collidesWithSolid(newHitBox)) return;

    // Check collision with bomb
    if (checkPlayerBombCollision(newHitBox)) {
      // If the player is colliding with a bomb
      if (playerWasInsideBomb()) {
        // Allow movement
        player.move(xStep, yStep);

        sendUpdate(
            new PlayerMovementData(
                PackageType.MOVE_PLAYER,
                player.getX() - 8,
                player.getY() - 64,
                delta,
                oldX - 8,
                oldY - 64,
                direction)); // Update direction
      }
      return;
    }

    checkPlayerPowerUpCollision();

    checkPlayerExitCollision();

    player.move(xStep, yStep);

    sendUpdate(
        new PlayerMovementData(
            PackageType.MOVE_PLAYER,
            player.getX() - 8,
            player.getY() - 64,
            delta,
            oldX - 8,
            oldY - 64,
            direction)); // Update direction
  }

  /**
   * calculates the direction of the player
   *
   * @param xStep the value of the step that the player takes
   */
  private Direction calculateDirection(int xStep, int yStep) {
    if (xStep > 0) {
      return Direction.RIGHT;
    } else if (xStep < 0) {
      return Direction.LEFT;
    } else if (yStep > 0) {
      return Direction.DOWN;
    } else if (yStep < 0) {
      return Direction.UP;
    } else {
      return Direction.NONE; // Keep the current direction if no movement
    }
  }

  /** Checks if the player was inside a bomb */
  private boolean playerWasInsideBomb() {
    // Check if the player's old position was inside a bomb
    Rectangle2D oldPlayerHitBox = new Rectangle2D(player.getX(), player.getY(), 32, 32);
    return checkPlayerBombCollision(oldPlayerHitBox);
  }

  /**
   * Checks if the player collides with the Bomb
   *
   * @param playerHitBox the player hitbox
   */
  public boolean checkPlayerBombCollision(Rectangle2D playerHitBox) {
    for (Entity entity : entities) {
      if (entity instanceof Bomb) {
        Bomb bomb = (Bomb) entity;
        Rectangle2D bombHitBox = bomb.getHitBox();
        if (playerHitBox.intersects(bombHitBox)) {
          return true; // Collision detected
        }
      }
    }
    return false; // No collision detected
  }

  /**
   * Moves a mob in the map.
   *
   * @param mob the mob to be moved
   * @param xStep the x-coordinate step
   * @param yStep the y-coordinate step
   * @param delta the delta time
   */
  public void moveMob(Mob mob, int xStep, int yStep, double delta) {

    int oldX = mob.getX();
    int oldY = mob.getY();

    // Calculate the new position based on xStep and yStep
    int newX = oldX + xStep;
    int newY = oldY + yStep;

    int id = mob.getId();

    // Create a new hitbox for the new position
    Rectangle2D newHitBox = new Rectangle2D(newX, newY, mob.getWidth(), mob.getHeight());

    // Check if the new position collides with solid tiles
    if (collidesWithSolid(newHitBox)
        || collidesWithOtherMobs(mob, newX, newY)
        || checkMobBombCollision(newHitBox)) {
      // If collision occurs with solid tiles, handle collision or change direction as needed
      Direction newDirection = chooseRandomValidDirection(mob);
      if (newDirection != null) {
        mob.setDirection(newDirection);
      }
      mob.updateHitBox(oldX, oldY); // Update hitbox with old coordinates
      return;
    }

    if (mob.getType() == Type.PUROPEN) {
      puropenDirection = mob.getDirection();
      // Update puropenDirection when the direction of the mob changes
      mob.setDirection(puropenDirection);
    }

    detectPlayerMobCollision();

    // Update the mob's position
    mob.move(newX, newY);

    // Create package data and send movement information to GameView
    MobMovementData mobMovementData =
        new MobMovementData(
            PackageType.MOB_MOVEMENT,
            mob.getType(),
            id,
            newX,
            newY,
            delta,
            oldX,
            oldY,
            puropenDirection);
    sendUpdate(mobMovementData);
  }

  /**
   * Detects collision between the bomb and mobs.
   *
   * @param mobHitBox the hitbox of the mob
   * @return true if collision is detected, false otherwise
   */
  public boolean checkMobBombCollision(Rectangle2D mobHitBox) {
    for (Entity entity : entities) {
      if (entity instanceof Bomb) {
        Bomb bomb = (Bomb) entity;
        Rectangle2D bombHitBox = bomb.getHitBox();
        if (mobHitBox.intersects(bombHitBox)) {
          return true; // Collision detected
        }
      }
    }
    return false; // No collision detected
  }

  /**
   * Detects collision between the mobs.
   *
   * @param mob the mob to be checked
   * @param newX the new x-coordinate of the mob
   * @param newY the new y-coordinate of the mob
   */
  private boolean collidesWithOtherMobs(Mob mob, int newX, int newY) {
    // Create a hitbox for the new position
    Rectangle2D newHitBox = new Rectangle2D(newX, newY, mob.getWidth(), mob.getHeight());

    // Iterate over all mobs to check for collision with other mobs
    for (Entity entity : entities) {
      if (entity instanceof Mob && entity != mob) {
        Mob otherMob = (Mob) entity;
        Rectangle2D otherMobHitbox = otherMob.getHitBox();
        // Check if the hitbox of the other mob intersects with the new hitbox
        if (newHitBox.intersects(otherMobHitbox)) {
          // Collision detected with another mob
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Chooses a random valid direction for the mob.
   *
   * @param mob the mob for which to choose a direction
   * @return the chosen direction
   */
  private Direction chooseRandomValidDirection(Mob mob) {
    // Get the current direction of the mob
    Direction currentDirection = mob.getDirection();

    // Create a list to store valid directions
    List<Direction> validDirections = new ArrayList<>();

    // Check each direction and add it to the list if it is valid
    for (Direction direction : Direction.values()) {
      if (direction != currentDirection
          && direction != Direction.NONE
          && isValidDirection(mob, direction)) {
        validDirections.add(direction);
      }
    }

    // If there are valid directions available, choose a random one
    if (!validDirections.isEmpty()) {
      Random random = new Random();
      return validDirections.get(random.nextInt(validDirections.size()));
    }

    // If no valid directions are available, return null
    return currentDirection;
  }

  /**
   * calculates the valid directions for the mob
   *
   * @param mob the mob for which to calculate the valid directions
   * @param direction the direction to check
   * @return true if the direction is valid, false otherwise
   */
  private boolean isValidDirection(Mob mob, Direction direction) {
    // Calculate the new position based on the direction
    int newX = mob.getX() + direction.getX();
    int newY = mob.getY() + direction.getY();

    // Create a hitbox for the new position
    Rectangle2D newHitBox = new Rectangle2D(newX, newY, mob.getWidth(), mob.getHeight());

    // Check if the new position collides with solid tiles
    return !collidesWithSolid(newHitBox);
  }

  /** spawns a bomb in the map */
  public void spawnBomb() {
    int tileRow = player.getY() / 48;
    int tileColumn = player.getX() / 48;

    // Calculate the position of the bomb at the center of the tile
    int bombX = tileColumn * 48;
    int bombY = tileRow * 48;

    if (player.getBombCount() > 0) {
      boolean canSpawn = true;

      for (Entity entity : entities) {
        if (entity instanceof Bomb && entity.getX() == bombX && entity.getY() == bombY) {
          canSpawn = false;
          break;
        }
      }

      if (canSpawn) {

        Bomb bomb = new Bomb(bombX, bombY);
        entities.add(bomb);
        bomb.spawn();
        player.decrementBombCount(); // Decrement the player's bomb count

        BombSpawnData packageData = new BombSpawnData(PackageType.SPAWN_BOMB, bombX, bombY);
        sendUpdate(packageData);
      }
    }
  }

  /**
   * Detects collision between an entity and the solid tiles.
   *
   * @param newHitBox the hitbox of the entity
   * @return true if collision is detected, false otherwise
   */
  public boolean collidesWithSolid(Rectangle2D newHitBox) {

    int leftX = (int) newHitBox.getMinX();
    int rightX = (int) newHitBox.getMaxX();
    int topY = (int) newHitBox.getMinY();
    int bottomY = (int) newHitBox.getMaxY();

    boolean topLeftCollides = isCollidableTile(leftX, topY);
    boolean topRightCollides = isCollidableTile(rightX, topY);
    boolean bottomLeftCollides = isCollidableTile(leftX, bottomY);
    boolean bottomRightCollides = isCollidableTile(rightX, bottomY);

    return topLeftCollides || topRightCollides || bottomLeftCollides || bottomRightCollides;
  }

  /**
   * Checks if the tile at the given coordinates is collidable.
   *
   * @param leftX the left x-coordinate of the tile
   * @param topY the top y-coordinate of the tile
   * @return true if the tile is collidable, false otherwise
   */
  private boolean isCollidableTile(int leftX, int topY) {
    int tileX = leftX / Tiles.GRASS.size();
    int tileY = topY / Tiles.GRASS.size();

    int index = tileY * MAP_WIDTH + tileX;

    if (index >= 0 && index < numTileMap.size()) {
      return numTileMap.get(index).isCollidable();
    }

    return false;
  }

  /**
   * Explodes the bomb and handles the explosion.
   *
   * @param bomb the bomb to explode
   */
  public void explodeBomb(Bomb bomb) {
    int explosionX = bomb.getX();
    int explosionY = bomb.getY();
    int range = bomb.getRange();

    bomb.setExploded(true);

    // Get the explosion ranges for each direction
    int[] ranges = explodeAdjacentTiles(explosionX, explosionY, range);

    // Calculate the explosion hitboxes
    Rectangle2D[] explosionHitboxes = calculateExplosionHitboxes(explosionX, explosionY, ranges);

    Rectangle2D horizontalExplosionHitbox = explosionHitboxes[0];
    Rectangle2D verticalExplosionHitbox = explosionHitboxes[1];

    // Reset the set of mobs affected by the explosion
    mobsAffectedByExplosion.clear();

    // Initialize the collision detection scheduler
    initCollisionDetectionScheduler(horizontalExplosionHitbox, verticalExplosionHitbox);

    // Initialize the collision detection scheduler for the player
    initPlayerCollisionDetectionScheduler(horizontalExplosionHitbox, verticalExplosionHitbox);

    BombExplosionData packageData =
        new BombExplosionData(PackageType.BOMB_EXPLOSION, explosionX, explosionY, ranges);
    sendUpdate(packageData);

    // increment the player's bomb count after the explosion
    player.regenBombCount();

    // Check if the explosion hitboxes intersect with the exit tile hitbox
    if (exitTileHitBox != null
        && (exitTileHitBox.intersects(horizontalExplosionHitbox)
            || exitTileHitBox.intersects(verticalExplosionHitbox))) {
      // Get the exit tile coordinates
      int exitTileX = (int) exitTileHitBox.getMinX() / Tiles.GRASS.size();
      int exitTileY = (int) exitTileHitBox.getMinY() / Tiles.GRASS.size();

      // Define the coordinates of the adjacent tiles
      int[][] adjacentTiles = {
        {exitTileX - 1, exitTileY},
        {exitTileX + 1, exitTileY},
        {exitTileX, exitTileY - 1},
        {exitTileX, exitTileY + 1}
      };

      // Iterate over the adjacent tiles
      for (int[] tile : adjacentTiles) {
        int tileX = tile[0];
        int tileY = tile[1];

        // Check if the tile is within the map bounds and is not occupied by another tile
        if (tileX >= 0
            && tileX < MAP_WIDTH
            && tileY >= 0
            && tileY < MAP_HEIGHT
            && numTileMap.get(tileY * MAP_WIDTH + tileX) == Tiles.GRASS) {
          // Spawn an IceCreamPowerUp at the adjacent tile location
          spawnPowerUp(tileX, tileY, PowerUpType.ICE_CREAM, 0);
          break;
        }
      }
    }
  }

  /**
   * Spawns a power-up at the given tile coordinates.
   *
   * @param tileIndexX
   * @param tileIndexY
   * @param type
   * @param delayInSeconds
   */
  public void spawnPowerUp(int tileIndexX, int tileIndexY, PowerUpType type, int delayInSeconds) {
    Runnable spawnTask =
        () -> {
          try {
            // Calculate the coordinates based on the tile index
            int x = tileIndexX * Tiles.GRASS.size();
            int y = tileIndexY * Tiles.GRASS.size();

            PowerUp powerUp;
            switch (type) {
              case BOMB_UP:
                powerUp = new BombUpPowerUp();
                break;
              case SKATE:
                powerUp = new SkatePowerUp();
                break;
              case ICE_CREAM:
                powerUp = new IceCreamPowerUp();
                break;
              default:
                return;
            }

            powerUp.setX(x);
            powerUp.setY(y);
            powerUps.add(powerUp); // Add the power-up to the powerUps collection

            ScheduledFuture<?> despawnTask =
                executorService.schedule(() -> handlePowerUpDespawn(powerUp), 3, TimeUnit.SECONDS);

            powerUp.setDespawnTask(despawnTask); // Set the despawn task in the power-up

            PowerUpSpawnData packageData =
                new PowerUpSpawnData(PackageType.SPAWN_POWERUP, powerUp.getType(), x, y);
            sendUpdate(packageData);
          } catch (Exception e) {
            // If an exception occurs during power-up spawning, log it (optional)
            System.out.println("PowerUP spawn attempt did not spawn a powerup: " + e.getMessage());
          }
        };

    // Schedule the spawning task with the specified delay
    executorService.schedule(spawnTask, delayInSeconds, TimeUnit.MILLISECONDS);
  }

  /**
   * Initializes the collision detection scheduler for the mob.
   *
   * @param horizontalExplosionHitbox the horizontal explosion hitbox
   * @param verticalExplosionHitbox the vertical explosion hitbox
   */
  private void initCollisionDetectionScheduler(
      Rectangle2D horizontalExplosionHitbox, Rectangle2D verticalExplosionHitbox) {
    // Schedule the task for collision detection at a fixed rate
    ScheduledFuture<?> collisionDetectionTask =
        executorService.scheduleAtFixedRate(
            () -> detectMobExplosionCollision(horizontalExplosionHitbox, verticalExplosionHitbox),
            0,
            100,
            TimeUnit.MILLISECONDS);

    // Schedule a task to cancel collision detection after the two-second interval
    executorService.schedule(
        () -> {
          collisionDetectionTask.cancel(false);
          // Clear the set of mobs affected by the explosion after the interval
          mobsAffectedByExplosion.clear();
        },
        550,
        TimeUnit.MILLISECONDS);
  }

  /**
   * Handles the detection of collision between the bomb explosion and adjacent tiles.
   *
   * @param x the starting x-coordinate of the explosion
   * @param y the starting y-coordinate of the explosion
   * @param playerRadius the radius of the player
   * @return an array of distances for each direction
   */
  private int[] explodeAdjacentTiles(int x, int y, int playerRadius) {
    int[] distances = new int[] {playerRadius, playerRadius, playerRadius, playerRadius};
    int[][] directions = new int[][] {{1, 0}, {-1, 0}, {0, -1}, {0, 1}}; // Right, Left, Up, Down

    for (int direction = 0; direction < directions.length; direction++) {
      for (int range = 1; range <= playerRadius; range++) {
        int deltaX = directions[direction][0] * range;
        int deltaY = directions[direction][1] * range;
        int tileX = x / 48 + deltaX;
        int tileY = y / 48 + deltaY;

        if (isExplosionCollision(tileX, tileY)) {
          // Check if the tile is destroyable
          if (isDestroyableTile(tileX, tileY)) {

            handleTileDestruction(tileX, tileY);
            distances[direction] = range - 1;

            // Stop the explosion after destroying the first destroyable tile
            break;
          } else {
            // If the tile is not destroyable, stop the explosion
            distances[direction] = range - 1;
            break;
          }
        }
      }
    }

    return distances;
  }

  /**
   * Handles the destruction of a destroyable tile.
   *
   * @param tileIndexX the x-coordinate of the tile
   * @param tileIndexY the y-coordinate of the tile
   */
  private void handleTileDestruction(int tileIndexX, int tileIndexY) {
    if (tileIndexX < 0 || tileIndexX >= MAP_WIDTH || tileIndexY < 0 || tileIndexY >= MAP_HEIGHT) {
      return; // Out-of-bounds coordinates, stop the destruction
    }

    // Calculate the tile index based on the coordinates
    int tileIndex = tileIndexY * MAP_WIDTH + tileIndexX;

    Tiles tileType = numTileMap.get(tileIndex);

    if (tileType.isDestroyable()) {
      // Create a TileDestructionData instance
      TileDestructionData destructionData =
          new TileDestructionData(PackageType.TILE_DESTRUCTION, tileIndex);

      sendUpdate(destructionData);

      // Check if the tile below is a GRASS_SHADOW_DESTROYABLE tile
      if (tileIndexY + 1 < MAP_HEIGHT
          && numTileMap.get((tileIndexY + 1) * MAP_WIDTH + tileIndexX)
              == Tiles.GRASS_SHADOW_DESTROYABLE) {
        numTileMap.set((tileIndexY + 1) * MAP_WIDTH + tileIndexX, Tiles.GRASS);
        tileHitBoxes.set(
            (tileIndexY + 1) * MAP_WIDTH + tileIndexX,
            createTile(tileIndexX, tileIndexY + 1, Tiles.GRASS).getHitBox());
      }

      // Check if the tile above is an IMMOVABLE or BORDER_HORIZONTAL tile
      Tiles tileAbove =
          tileIndexY - 1 >= 0 ? numTileMap.get((tileIndexY - 1) * MAP_WIDTH + tileIndexX) : null;

      if (tileAbove == Tiles.IMMOVABLE || tileAbove == Tiles.BORDER_HORIZONTAL) {
        numTileMap.set(tileIndex, Tiles.GRASS_SHADOW);
        tileHitBoxes.set(
            tileIndex, createTile(tileIndexX, tileIndexY, Tiles.GRASS_SHADOW).getHitBox());
      } else {
        // Replace destroyable tile with a GRASS tile
        numTileMap.set(tileIndex, Tiles.GRASS);
        tileHitBoxes.set(tileIndex, createTile(tileIndexX, tileIndexY, Tiles.GRASS).getHitBox());
      }

      // Generate a random number between 0 and 1
      double randomValue = Math.random();
      // Check if the random value falls within the spawn chance
      if (randomValue <= 0.05
          || isLastDestroyableTile()) { // Adjust this value as needed for the desired spawn chance
        if (!exitTileSpawned) {
          spawnExitTile(tileIndexX, tileIndexY, 1300);
          exitTileSpawned = true;
        }
      } else {
        // Generate a random number between 0 and 1
        randomValue = Math.random();

        // Check if the random value falls within the spawn chance
        if (randomValue <= 0.2) { // Adjust this value as needed for the desired spawn chance
          spawnRandomPowerUp(tileIndexX, tileIndexY, 1300);
        }
      }
    }
  }

  /**
   * calculates if the current tile is the last destroyable tile present on the map
   *
   * @return true if the last destroyable tile is present, false otherwise
   */
  private boolean isLastDestroyableTile() {
    return numTileMap.stream().filter(Tiles::isDestroyable).count() == 0;
  }

  /**
   * calculates if the explosion of the bomb collides with a tile
   *
   * @param tileX the x-coordinate of the tile
   * @param tileY the y-coordinate of the tile
   * @return true if the explosion collides with a tile, false otherwise
   */
  private boolean isExplosionCollision(int tileX, int tileY) {
    int index = tileY * MAP_WIDTH + tileX;

    if (index >= 0 && index < numTileMap.size()) {
      return numTileMap.get(index).isCollidable();
    }

    return false;
  }

  /**
   * calculates if the tile is destroyable
   *
   * @param tileX the x-coordinate of the tile
   * @param tileY the y-coordinate of the tile
   * @return true if the tile is destroyable, false otherwise
   */
  private boolean isDestroyableTile(int tileX, int tileY) {
    int index = tileY * MAP_WIDTH + tileX;
    if (index >= 0 && index < numTileMap.size()) {
      return numTileMap.get(index).isDestroyable();
    }
    return false;
  }

  /**
   * Calculates the explosion hitboxes for the bomb explosion.
   *
   * @param explosionX the x-coordinate of the explosion
   * @param explosionY the y-coordinate of the explosion
   * @param ranges the ranges of the explosion in each direction
   * @return an array of the horizontal and vertical hitboxes
   */
  private Rectangle2D[] calculateExplosionHitboxes(int explosionX, int explosionY, int[] ranges) {
    // Calculate the dimensions of the horizontal hitbox
    int horizontalWidth = ((ranges[1] + ranges[0]) * 48) + 48;
    int horizontalHeight = 48;
    int horizontalX = explosionX - ranges[0] * 48;

    // Create the horizontal hitbox
    Rectangle2D horizontalHitbox =
        new Rectangle2D(horizontalX, explosionY, horizontalWidth, horizontalHeight);

    // Calculate the dimensions of the vertical hitbox
    int verticalWidth = 48;
    int verticalHeight = ((ranges[3] + ranges[2]) * 48) + 48;
    int verticalY = explosionY - ranges[2] * 48;

    // Create the vertical hitbox
    Rectangle2D verticalHitbox =
        new Rectangle2D(explosionX, verticalY, verticalWidth, verticalHeight);

    // Return an array containing the horizontal and vertical hitboxes
    return new Rectangle2D[] {horizontalHitbox, verticalHitbox};
  }

  /**
   * Detects collision between the mob and the explosion.
   *
   * @param horizontalExplosionHitbox the horizontal explosion hitbox
   * @param verticalExplosionHitbox the vertical explosion hitbox
   */
  private void detectMobExplosionCollision(
      Rectangle2D horizontalExplosionHitbox, Rectangle2D verticalExplosionHitbox) {

    // Iterate over all entities to check for collision with explosion
    for (Entity entity : entities) {
      if (entity instanceof Mob) {
        Mob mob = (Mob) entity;

        // Check if the mob has already been affected by the explosion
        if (mobsAffectedByExplosion.contains(mob)) {
          continue;
        }

        Rectangle2D mobHitbox = mob.getHitBox();

        // Check if mob's hitbox intersects with width explosion hitbox
        if (mobHitbox.intersects(horizontalExplosionHitbox)) {
          handleMobExplosion(mob);
          mobsAffectedByExplosion.add(mob);
        }

        // Check if mob's hitbox intersects with height explosion hitbox
        if (mobHitbox.intersects(verticalExplosionHitbox)) {
          handleMobExplosion(mob);
          mobsAffectedByExplosion.add(mob);
        }
      }
    }
  }

  /**
   * Handles the explosion of the mob.
   *
   * @param mob the mob to be exploded
   */
  private void handleMobExplosion(Mob mob) {
    int score = calculateScoreForMob(mob);

    if (mob.isVulnerable()) { // If the mob is vulnerable
      mob.setLives(mob.getLives() - 1);

      // Send the current number of lives along with the RemoveMobData package
      sendUpdate(
          new RemoveMobData(
              PackageType.REMOVE_MOB,
              mob.getType(),
              mob.getId(),
              mob.getLives(), // Send current number of lives
              score,
              mob.getX(),
              mob.getY()));

      mob.invincible();
    }

    // Remove the mob from the game if its lives equal 0
    if (mob.getLives() <= 0) {

      if (mob.getLives() == 0 && mob.getType() == Type.DENKYUN) { // PUROPEN mob reaches 0 lives
        // Check if the exit tile is present on the map
        if (isExitTilePresent()) {

          Timeline delayTimeline =
              new Timeline(
                  new KeyFrame(
                      Duration.seconds(5),
                      event -> {
                        // Respawn the PUROPEN mob at the exit tile
                        respawnMobOnExitTile();
                      }));
          delayTimeline.play();
        }
      }
      // Award the calculated score to the player
      player.setScore(player.getScore() + score);
      updatePlayerScore(player);

      // Schedule a powerup to spawn at the mob's location after a delay
      despawnScheduler.schedule(
          () ->
              spawnRandomPowerUp(
                  mob.getX() / Tiles.GRASS.size(), mob.getY() / Tiles.GRASS.size(), 0),
          2100,
          TimeUnit.MILLISECONDS);

      entities.remove(mob);
    }
  }

  /**
   * checks is if the TileExit is present on the map.
   *
   * @return true if the exit tile is present, false otherwise
   */
  private boolean isExitTilePresent() {
    // Check if there's already an exit tile on the map
    for (Tiles tile : numTileMap) {
      if (tile == Tiles.EXIT) {
        return true; // Exit tile already exists, so don't spawn another one
      }
    }
    return false;
  }

  /** Respawns the DENKYUN mob at the exit tile. */
  private void respawnMobOnExitTile() {
    // Find the position of the exit tile
    int[] exitTilePosition = findExitTilePosition(numTileMap);

    if (exitTilePosition != null) {
      // Calculate the position of the exit tile
      int exitTilePosX = exitTilePosition[0];
      int exitTilePosY = exitTilePosition[1];

      // Create a new DENKYUN mob at the exit tile position
      Mob denkyunMob =
          new Mob(exitTilePosX, exitTilePosY, 45, 45, Type.DENKYUN, Direction.RIGHT, 0);

      Direction newDirection = chooseRandomValidDirection(denkyunMob);

      denkyunMob.setDirection(newDirection);
      // Spawn the DENKYUN mob
      denkyunMob.spawn();
      entities.add(denkyunMob);

      sendUpdate(
          new DenkyunRespawnData(
              PackageType.DENKYUN_RESPAWN,
              denkyunMob.getType(),
              denkyunMob.getX(),
              denkyunMob.getY(),
              denkyunMob.getId()));
    }
  }

  /**
   * Finds the position of the exit tile on the map.
   *
   * @param numTileMap the list of tiles
   * @return an array containing the x and y coordinates of the exit tile
   */
  private int[] findExitTilePosition(ArrayList<Tiles> numTileMap) {

    int mapWidth = 15; // Width of the map in tile units
    int tileSize = 48; // Size of each tile in pixels

    int exitIndex = numTileMap.indexOf(Tiles.EXIT);
    if (exitIndex != -1) {
      int exitTileX = exitIndex % mapWidth; // X coordinate of the exit tile in tile units
      int exitTileY = exitIndex / mapWidth; // Y coordinate of the exit tile in tile units
      int exitTileTopLeftX =
          exitTileX * tileSize; // X coordinate of the top-left pixel of the exit tile
      int exitTileTopLeftY =
          exitTileY * tileSize; // Y coordinate of the top-left pixel of the exit tile
      return new int[] {
        exitTileTopLeftX, exitTileTopLeftY
      }; // Return exit tile top-left position (x, y)
    }
    return null; // Exit tile not found
  }

  /**
   * Calculates the score for the mob based on its type.
   *
   * @param mob the mob for which to calculate the score
   * @return the score for the mob
   */
  private int calculateScoreForMob(Mob mob) {
    Type mobType = mob.getType();
    int score = 0;
    // Determine score based on the type of mob
    switch (mobType) {
      case PUROPEN:
        score = 100; // Example score for a normal mob
        break;
      case DENKYUN:
        score = 300; // Example score for a special mob
        break;
      default:
        score = 0; // Default score if mob type is unknown
        break;
    }

    return score;
  }

  /**
   * Initializes the collision detection scheduler for the player.
   *
   * @param horizontalExplosionHitbox the horizontal explosion hitbox
   * @param verticalExplosionHitbox the vertical explosion hitbox
   */
  private void initPlayerCollisionDetectionScheduler(
      Rectangle2D horizontalExplosionHitbox, Rectangle2D verticalExplosionHitbox) {
    // Schedule the task for player collision detection at a fixed rate
    ScheduledFuture<?> playerCollisionDetectionTask =
        executorService.scheduleAtFixedRate(
            () ->
                detectPlayerCollisionWithExplosion(
                    player, horizontalExplosionHitbox, verticalExplosionHitbox),
            0,
            100,
            TimeUnit.MILLISECONDS);

    // Schedule a task to cancel player collision detection after the two-second interval
    executorService.schedule(
        () -> playerCollisionDetectionTask.cancel(false), 550, TimeUnit.MILLISECONDS);
  }

  /**
   * Detects collision between the player and the explosion.
   *
   * @param player the player
   * @param horizontalExplosionHitbox the horizontal explosion hitbox
   * @param verticalExplosionHitbox the vertical explosion hitbox
   */
  private void detectPlayerCollisionWithExplosion(
      Player player, Rectangle2D horizontalExplosionHitbox, Rectangle2D verticalExplosionHitbox) {
    Rectangle2D playerHitbox = player.getHitBox();

    // Check if player's hitbox intersects with width explosion hitbox
    if (playerHitbox.intersects(horizontalExplosionHitbox)) {
      handlePlayerHit(player);
      updatePlayerLives(player);
    }

    // Check if player's hitbox intersects with height explosion hitbox
    if (playerHitbox.intersects(verticalExplosionHitbox)) {
      handlePlayerHit(player);
      updatePlayerLives(player);
    }
  }

  /**
   * Updates the player's lives and sends the update to the GameView.
   *
   * @param player the player
   */
  private void handlePlayerHit(Player player) {
    if (player.isVulnerable()) {

      player.takeDamage();

      pauseKeyHandler();

      // Schedule the method call to resume key handler and set player position after a delay
      ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
      // Set player position if needed

      scheduler.schedule(
          this::resumeKeyHandler, 2500, TimeUnit.MILLISECONDS); // Adjust timing as needed

      // Notify GameView to update display with new player lives
      sendUpdate(new PlayerLivesUpdateData(PackageType.PLAYER_LIVES_UPDATE, player.getLives()));
    }
  }

  /** stops the key handler */
  private void pauseKeyHandler() {
    // Pause the KeyHandler
    KeyHandler.getInstance().stopKeyHandler();
  }

  /** resumes the key handler */
  private void resumeKeyHandler() {
    // Resume the KeyHandler
    KeyHandler.getInstance().startMovement();
  }

  /** Detects collision between the player and mobs. */
  public void detectPlayerMobCollision() {
    Rectangle2D playerHitbox = player.getHitBox();

    // Iterate over all mobs to check for collision with player
    for (Entity entity : entities) {
      if (entity instanceof Mob) {
        Mob mob = (Mob) entity;
        Rectangle2D mobHitbox = mob.getHitBox();

        // Check if mob's hitbox intersects with player's hitbox
        if (mobHitbox.intersects(playerHitbox)) {
          handlePlayerHit(player);
          updatePlayerLives(player);
        }
      }
    }
  }

  /**
   * Spawns a random power-up at the given tile coordinates after a delay.
   *
   * @param tileIndexX the x-coordinate of the tile
   * @param tileIndexY the y-coordinate of the tile
   * @param delayInSeconds the delay in seconds before spawning the power-up
   */
  public void spawnRandomPowerUp(int tileIndexX, int tileIndexY, int delayInSeconds) {
    try {
      // Check if the tile at the given coordinates is an exit tile
      if (numTileMap.get(tileIndexY * MAP_WIDTH + tileIndexX) == Tiles.EXIT) {
        // Exit tile exists at these coordinates, so skip power-up spawning
        return;
      }

      Runnable spawnTask =
          () -> {
            PowerUp powerUp = PowerUpFactory.createRandomPowerUp();

            // Calculate the coordinates based on the tile index
            int x = tileIndexX * Tiles.GRASS.size();
            int y = tileIndexY * Tiles.GRASS.size();

            powerUp.setX(x);
            powerUp.setY(y);
            powerUps.add(powerUp); // Add the power-up to the powerUps collection

            ScheduledFuture<?> despawnTask =
                executorService.schedule(() -> handlePowerUpDespawn(powerUp), 3, TimeUnit.SECONDS);

            powerUp.setDespawnTask(despawnTask); // Set the despawn task in the power-up

            PowerUpSpawnData packageData =
                new PowerUpSpawnData(PackageType.SPAWN_POWERUP, powerUp.getType(), x, y);
            sendUpdate(packageData);
          };

      // Schedule the spawning task with the specified delay
      executorService.schedule(spawnTask, delayInSeconds, TimeUnit.MILLISECONDS);

    } catch (Exception e) {
      // If an exception occurs during power-up spawning, log it (optional)
      System.out.println("PowerUP spawn attempt did not spawn a powerup: " + e.getMessage());
    }
  }

  /**
   * Checks collision between the player and power-ups.
   *
   * <p>When a collision is detected, the power-up is applied to the player.
   */
  public void checkPlayerPowerUpCollision() {
    Rectangle2D playerHitBox = player.getHitBox();

    // Check collision with power-ups
    Iterator<PowerUp> iterator = powerUps.iterator();
    while (iterator.hasNext()) {
      PowerUp powerUp = iterator.next();
      Rectangle2D powerUpHitBox = powerUp.getHitBox();
      if (playerHitBox.intersects(powerUpHitBox)) {
        // Apply power-up effect to the player
        powerUp.applyPowerUp(player);
        updatePlayerScore(player);
        // Cancel despawn task
        cancelPowerUpDespawn(powerUp);

        // Get power-up position
        int powerUpX = (int) powerUp.getX();
        int powerUpY = (int) powerUp.getY();
        // Remove the power-up from the list
        iterator.remove();
        // Send update
        sendUpdate(
            new PowerUpApplicationData(
                PackageType.POWERUP_APPLICATION, powerUp.getType(), powerUpX, powerUpY));
        break; // Exit loop after applying one power-up per move
      }
    }
  }

  /**
   * Handles the despawn of a power-up.
   *
   * @param powerUp the power-up to despawn
   */
  private void handlePowerUpDespawn(PowerUp powerUp) {
    powerUps.remove(powerUp); // Remove the power-up from the list
    sendUpdate(
        new PowerUpDespawnData(
            PackageType.POWERUP_DESPAWN, powerUp.getType(), powerUp.getX(), powerUp.getY()));
  }

  /**
   * Cancels the despawn task associated with the power-up.
   *
   * @param powerUp the power-up
   */
  private void cancelPowerUpDespawn(PowerUp powerUp) {
    // Get the despawn task associated with the power-up
    ScheduledFuture<?> despawnTask = powerUp.getDespawnTask();
    if (despawnTask != null) {
      // Cancel the despawn task
      despawnTask.cancel(false);
    }
  }

  /**
   * Updates the player's score and sends the update to the GameView.
   *
   * @param player the player
   */
  public void updatePlayerScore(Player player) {
    // get the score of the player

    int score = player.getScore();
    // Create package data containing the player's updated score
    PlayerScoreUpdateData packageData =
        new PlayerScoreUpdateData(PackageType.PLAYER_SCORE_UPDATE, score);

    // Send the update to the GameView
    sendUpdate(packageData);
  }

  /**
   * Updates the player's lives and sends the update to the GameView.
   *
   * @param player the player
   */
  public void updatePlayerLives(Player player) {
    int lives = player.getLives();

    // Check if player has no lives left
    if (lives <= 0) {

      // Player has no lives left, trigger game over
      gameOver();

      player.setLives(5);

    } else {
      // Player still has lives left, update the UI
      PlayerLivesUpdateData packageData =
          new PlayerLivesUpdateData(PackageType.DRAW_PLAYER_LIVES_UPDATE, lives);
      sendUpdate(packageData);
    }
  }

  /**
   * Spawns the exit tile at the given tile coordinates after a delay.
   *
   * @param tileIndexX the x-coordinate of the tile
   * @param tileIndexY the y-coordinate of the tile
   * @param delayInSeconds the delay in seconds before spawning the exit tile
   */
  private void spawnExitTile(int tileIndexX, int tileIndexY, int delayInSeconds) {
    Runnable spawnTask =
        () -> {
          try {
            // Calculate the coordinates based on the tile index
            int x = tileIndexX * Tiles.GRASS.size();
            int y = tileIndexY * Tiles.GRASS.size();

            // Replace the destroyable tile with an exit tile
            numTileMap.set(tileIndexY * MAP_WIDTH + tileIndexX, Tiles.EXIT);

            // Store the hitbox of the exit tile
            exitTileHitBox = new Rectangle2D(x, y, Tiles.GRASS.size(), Tiles.GRASS.size());

            // Notify GameView to update display with the exit tile
            sendUpdate(new ExitTileSpawnData(PackageType.SPAWN_EXIT_TILE, x, y));

            exitTileSpawned = true;
          } catch (Exception e) {
            // If an exception occurs during spawning, log it (optional)
          }
        };

    // Schedule the spawning task with the specified delay
    executorService.schedule(spawnTask, delayInSeconds, TimeUnit.MILLISECONDS);
  }

  /**
   * Checks if the player has collided with the exit tile.
   *
   * <p>If the player has collided with the exit tile, the level is ended.
   */
  public void checkPlayerExitCollision() {
    if (player.collidesWithExitTile(exitTileHitBox)) {
      // Player collided with the exit tile, end the level
      endLevel();
    }
  }

  /**
   * Ends the current level and loads the next level.
   *
   * <p>If the current level is the last level, the game is won.
   */
  private void endLevel() {
    level++;
    // Increment the level number
    int currentLevel = getLevel();

    // Clear existing entities and power-ups
    pauseKeyHandler();
    entities.clear();
    powerUps.clear();

    // Remove the exit tile hitbox
    removeExitTileHitbox();

    exitTileSpawned = false;

    if (currentLevel > 2) { // set to == 1 for testing purposes
      KeyHandler.getInstance().stopKeyHandler();
      displayYouWinScreen();
      sendUpdate(new LevelUpdateData(PackageType.LEVEL_UPDATE, currentLevel));
      exitTileSpawned = false;
      return;
    }

    loadLevel(Integer.toString(currentLevel));

    sendUpdate(new LevelUpdateData(PackageType.LEVEL_UPDATE, currentLevel));
  }

  /**
   * Displays the "You Win" screen.
   *
   * <p>This method is called when the player has completed all levels.
   */
  private void displayYouWinScreen() {
    // Create a new PackageData instance for the "You Won" event
    PackageData youWinData = new YouWinData(PackageType.YOU_WIN);

    // Send the update
    sendUpdate(youWinData);
  }

  /** Removes the exit tile hitbox. */
  private void removeExitTileHitbox() {
    exitTileHitBox = null;
  }

  /**
   * Ends the game and displays the "Game Over" screen.
   *
   * <p>This method is called when the player has no lives left.
   */
  private void gameOver() {
    int score = player.getScore();

    // Send an update to GameView indicating that the game is over
    GameOverUpdateData gameOverData =
        new GameOverUpdateData(PackageType.GAME_OVER, score, player.getLives());
    sendUpdate(gameOverData);

    // Clear existing entities and power-ups
    KeyHandler.getInstance().stopKeyHandler();
    entities.clear();
    powerUps.clear();
    setLevel(1);
  }

  // Method to handle update when player is not moving
  public void playerNotMovingUpdate(int xStep, int yStep, double delta) {
    Direction direction = calculateDirection(xStep, yStep);
    int oldY = player.getY();
    int oldX = player.getX();
    sendUpdate(
        new PlayerMovementData(
            PackageType.MOVE_PLAYER,
            player.getX() - 8,
            player.getY() - 64,
            delta,
            oldX - 8,
            oldY - 64,
            direction)); // Update direction
  }

  // gets an array of all the mobs in the game
  public Mob[] getMobs() {
    List<Mob> mobList = new ArrayList<>();

    for (Entity entity : entities) {
      if (entity instanceof Mob mob) {
        mobList.add(mob);
      }
    }
    return mobList.toArray(new Mob[0]);
  }
}
