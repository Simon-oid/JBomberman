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
import org.jbomberman.model.powerups.PowerUp;
import org.jbomberman.model.powerups.PowerUpFactory;
import org.jbomberman.model.tiles.GrassTile;
import org.jbomberman.model.tiles.ImmovableTile;
import org.jbomberman.model.tiles.Tile;
import org.jbomberman.view.Tiles;

@Getter
@Setter
public class Map extends Observable {

  private static final int MAP_WIDTH = 15; // Adjust map width to include border
  private static final int MAP_HEIGHT = 13; // Adjust map height to include border

  // Getter for the level attribute
  @Getter private Integer level = 1;

  private ArrayList<Tiles> numTileMap;

  private static Map instance;

  private List<Entity> entities;

  private Player player;
  private int startingPosX;
  private int startingPosY;

  private boolean mobsMovement;

  private List<Rectangle2D> tileHitBoxes;

  private ScheduledExecutorService executorService;

  private Set<Mob> mobsAffectedByExplosion = new HashSet<>();

  private List<PowerUp> powerUps = new ArrayList<>();

  private ScheduledExecutorService despawnScheduler = Executors.newScheduledThreadPool(1);

  private Rectangle2D exitTileHitBox;

  private Direction puropenDirection = Direction.NONE;

  private boolean exitTileSpawned = false;

  private Map() {
    executorService = Executors.newSingleThreadScheduledExecutor();
  }

  public static Map getInstance() {
    if (instance == null) {
      instance = new Map(); // Create a new instance
    }
    return instance;
  }

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

  public void sendUpdate(PackageData packageData) {
    setChanged();
    notifyObservers(packageData);
    clearChanged();
  }

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

      int lives = player.get("lives").getAsInt();
      int score = player.get("score").getAsInt();

      int width = player.get("width").getAsInt();
      int height = player.get("height").getAsInt();

      this.player = new Player(adjustedX, adjustedY, width, height, lives, direction, score);

      JsonArray mobsJson = jsonObject.get("mob").getAsJsonArray();

      entities = new ArrayList<>();

      for (JsonElement mob : mobsJson) {
        Type type = Type.valueOf(mob.getAsJsonObject().get("type").getAsString());

        posx = mob.getAsJsonObject().get("posx").getAsInt();
        posy = mob.getAsJsonObject().get("posy").getAsInt();

        direction = Direction.valueOf(mob.getAsJsonObject().get("direction").getAsString());

        int widthh = mob.getAsJsonObject().get("width").getAsInt();
        int heightt = mob.getAsJsonObject().get("height").getAsInt();

        entities.add(new Mob(posx, posy, widthh, heightt, type, direction));
      }

    } catch (IOException e) {
      throw new LoadMapException("error while reading json file: " + e.getMessage(), e);
    }
  }

  public void loadLevel(String level) {
    System.out.println(level);

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
                Duration.seconds(1.5),
                event -> {
                  // Call the method to update the player score after the delay
                  updatePlayerScore(player);

                  // Call the method to update the player lives after the delay
                  updatePlayerLives(player);
                }));
    timeline.play();
  }

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

  private PackageData createInitialPositionData(Entity entity) {
    int initialX = entity.getX(); // Get the initial X position of the entity
    int initialY = entity.getY(); // Get the initial Y position of the entity

    if (entity instanceof Mob) {
      // If the entity is a mob, create initial position data for mob
      Mob mob = (Mob) entity;
      return new MobInitialPositionData(
          PackageType.MOB_INITIAL_POSITION, mob.getType(), initialX, initialY);
    } else {
      // Handle other types of entities as needed
      return null;
    }
  }

  public void moveEntities() {
    mobsMovement = true;

    // Start the player's movement using AnimationTimer
    KeyHandler.getInstance().startMovement();

    // Start the mob's movement using AnimationTimer
    MobHandler.getInstance().startMobMovement();
  }

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

    // TODO: fixa il fatto che quando il player collide con un blocco, si appiccia al blocco
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

  private boolean playerWasInsideBomb() {
    // Check if the player's old position was inside a bomb
    Rectangle2D oldPlayerHitBox = new Rectangle2D(player.getX(), player.getY(), 32, 32);
    return checkPlayerBombCollision(oldPlayerHitBox);
  }

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

  public void moveMob(Mob mob, int xStep, int yStep, double delta) {
    int oldX = mob.getX();
    int oldY = mob.getY();

    // Calculate the new position based on xStep and yStep
    int newX = oldX + xStep;
    int newY = oldY + yStep;

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

    detectPlayerMobCollision(delta);

    // Update the mob's position
    mob.move(newX, newY);

    // Create package data and send movement information to GameView
    MobMovementData mobMovementData =
        new MobMovementData(
            PackageType.MOB_MOVEMENT,
            mob.getType(),
            newX,
            newY,
            delta,
            oldX,
            oldY,
            puropenDirection);
    sendUpdate(mobMovementData);
  }

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

  private Direction chooseRandomValidDirection(Mob mob) {
    // Get the current direction of the mob
    Direction currentDirection = mob.getDirection();

    // Create a list to store valid directions
    List<Direction> validDirections = new ArrayList<>();

    // Check each direction and add it to the list if it is valid
    for (Direction direction : Direction.values()) {
      if (direction != currentDirection && isValidDirection(mob, direction)) {
        validDirections.add(direction);
      }
    }

    // If there are valid directions available, choose a random one
    if (!validDirections.isEmpty()) {
      Random random = new Random();
      return validDirections.get(random.nextInt(validDirections.size()));
    }

    // If no valid directions are available, return null
    return null;
  }

  private boolean isValidDirection(Mob mob, Direction direction) {
    // Calculate the new position based on the direction
    int newX = mob.getX() + direction.getX();
    int newY = mob.getY() + direction.getY();

    // Create a hitbox for the new position
    Rectangle2D newHitBox = new Rectangle2D(newX, newY, mob.getWidth(), mob.getHeight());

    // Check if the new position collides with solid tiles
    return !collidesWithSolid(newHitBox);
  }

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

  private boolean isCollidableTile(int leftX, int topY) {
    int tileX = leftX / Tiles.GRASS.size();
    int tileY = topY / Tiles.GRASS.size();

    int index = tileY * MAP_WIDTH + tileX;

    if (index >= 0 && index < numTileMap.size()) {
      return numTileMap.get(index).isCollidable();
    }

    return false;
  }

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
  }

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

  private int[] explodeAdjacentTiles(int x, int y, int playerRadius) {
    int[] distances = new int[] {playerRadius, playerRadius, playerRadius, playerRadius};
    int[][] directions = new int[][] {{1, 0}, {-1, 0}, {0, -1}, {0, 1}}; // Right, Left, Up, Down

    for (int direction = 0; direction < directions.length; direction++) {
      for (int range = 1; range <= playerRadius; range++) {
        int deltaX = directions[direction][0] * range;
        int deltaY = directions[direction][1] * range;
        int tileX = x / 48 + deltaX;
        int tileY = y / 48 + deltaY;

        // System.out.println(tileX);
        // System.out.println(tileY);

        if (isExplosionCollision(tileX, tileY)) {
          // Check if the tile is destroyable
          if (isDestroyableTile(tileX, tileY)) {
            // Calculate the next tile based on the current direction
            int nextTileX = tileX + directions[direction][0];
            int nextTileY = tileY + directions[direction][1];

            // System.out.println("Current Tile: (" + tileX + ", " + tileY + ")");
            // System.out.println("Next Tile: (" + nextTileX + ", " + nextTileY + ")");

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

      // Notify GameView about the destruction of this tile
      sendUpdate(destructionData);

      // Replace destroyable tile with a GRASS tile
      numTileMap.set(tileIndex, Tiles.GRASS);
      tileHitBoxes.set(
          tileIndex, createTile(tileIndexX, tileIndexY, Tiles.GRASS).getHitBox()); // Update hitbox

      // Spawn an exit tile based on a certain percentile
      if (shouldSpawnExitTile() && !exitTileSpawned) {
        spawnExitTile(tileIndexX, tileIndexY, 1300);
        exitTileSpawned = true;
      } else {
        spawnRandomPowerUp(tileIndexX, tileIndexY, 1300);
      }
    }
  }

  private boolean isExplosionCollision(int tileX, int tileY) {
    int index = tileY * MAP_WIDTH + tileX;

    if (index >= 0 && index < numTileMap.size()) {
      return numTileMap.get(index).isCollidable();
    }

    return false;
  }

  private boolean isDestroyableTile(int tileX, int tileY) {
    int index = tileY * MAP_WIDTH + tileX;
    if (index >= 0 && index < numTileMap.size()) {
      return numTileMap.get(index).isDestroyable();
    }
    return false;
  }

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

  private void detectMobExplosionCollision(
      Rectangle2D horizontalExplosionHitbox, Rectangle2D verticalExplosionHitbox) {
    //    System.out.println("Checking collision between mobs and explosion...");

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
          System.out.println("Collision detected between mob and width explosion!");
          handleMobExplosion(mob);
          mobsAffectedByExplosion.add(mob);
        }

        // Check if mob's hitbox intersects with height explosion hitbox
        if (mobHitbox.intersects(verticalExplosionHitbox)) {
          System.out.println("Collision detected between mob and height explosion!");
          handleMobExplosion(mob);
          mobsAffectedByExplosion.add(mob);
        }
      }
    }

    // System.out.println("Collision detection complete.");
  }

  private void handleMobExplosion(Mob mob) {
    int score = calculateScoreForMob(mob);

    // Award the calculated score to the player
    player.setScore(player.getScore() + score);
    updatePlayerScore(player);

    // Remove the mob from the game
    entities.remove(mob);

    // Notify GameView to update display and remove the mob from the map
    sendUpdate(
        new RemoveMobData(PackageType.REMOVE_MOB, mob.getType(), score, mob.getX(), mob.getY()));
  }

  private int calculateScoreForMob(Mob mob) {
    Type mobType = mob.getType();
    int score = 0;
    // Determine score based on the type of mob
    switch (mobType) {
      case PUROPEN:
        score = 100; // Example score for a normal mob
        break;
      case DENKYUN:
        score = 400; // Example score for a special mob
        break;
      default:
        score = 0; // Default score if mob type is unknown
        break;
    }

    return score;
  }

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

  private void detectPlayerCollisionWithExplosion(
      Player player, Rectangle2D horizontalExplosionHitbox, Rectangle2D verticalExplosionHitbox) {
    Rectangle2D playerHitbox = player.getHitBox();

    // Check if player's hitbox intersects with width explosion hitbox
    if (playerHitbox.intersects(horizontalExplosionHitbox)) {
      handlePlayerHit(player, KeyHandler.getInstance().getDelta());
      updatePlayerLives(player);
    }

    // Check if player's hitbox intersects with height explosion hitbox
    if (playerHitbox.intersects(verticalExplosionHitbox)) {
      handlePlayerHit(player, KeyHandler.getInstance().getDelta());
      updatePlayerLives(player);
    }
  }

  private void handlePlayerHit(Player player, double delta) {
    if (player.isVulnerable()) {

      player.takeDamage();

      pauseKeyHandler();

      // Schedule the method call to resume key handler and set player position after a delay
      ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
      // Set player position if needed
      //            player.setX(startingPosX);
      //            player.setY(startingPosY);
      scheduler.schedule(
          this::resumeKeyHandler, 2000, TimeUnit.MILLISECONDS); // Adjust timing as needed

      // Player takes damage from the explosion

      System.out.println("Player Took Damage!!!");

      // Notify GameView to update display with new player lives
      sendUpdate(new PlayerLivesUpdateData(PackageType.PLAYER_LIVES_UPDATE, player.getLives()));
    }
  }

  private void pauseKeyHandler() {
    // Pause the KeyHandler
    KeyHandler.getInstance().pauseKeyHandler();
  }

  private void resumeKeyHandler() {
    // Resume the KeyHandler
    KeyHandler.getInstance().resumeKeyHandler();
  }

  public void detectPlayerMobCollision(double delta) {
    Rectangle2D playerHitbox = player.getHitBox();

    // Iterate over all mobs to check for collision with player
    for (Entity entity : entities) {
      if (entity instanceof Mob) {
        Mob mob = (Mob) entity;
        Rectangle2D mobHitbox = mob.getHitBox();

        // Check if mob's hitbox intersects with player's hitbox
        if (mobHitbox.intersects(playerHitbox)) {
          handlePlayerHit(player, delta);
          updatePlayerLives(player);
        }
      }
    }
  }

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
        System.out.println("powerup collision checked");
        // Send update
        sendUpdate(
            new PowerUpApplicationData(
                PackageType.POWERUP_APPLICATION, powerUp.getType(), powerUpX, powerUpY));
        break; // Exit loop after applying one power-up per move
      }
    }
  }

  private void handlePowerUpDespawn(PowerUp powerUp) {
    powerUps.remove(powerUp); // Remove the power-up from the list
    sendUpdate(
        new PowerUpDespawnData(
            PackageType.POWERUP_DESPAWN, powerUp.getType(), powerUp.getX(), powerUp.getY()));
    System.out.println("powerup despawned");
  }

  private void cancelPowerUpDespawn(PowerUp powerUp) {
    // Get the despawn task associated with the power-up
    ScheduledFuture<?> despawnTask = powerUp.getDespawnTask();
    if (despawnTask != null) {
      // Cancel the despawn task
      despawnTask.cancel(false);
    }
    System.out.println("powerup despawn canceled");
  }

  public void updatePlayerScore(Player player) {
    // get the score of the player
    int score = player.getScore();
    // Create package data containing the player's updated score
    PlayerScoreUpdateData packageData =
        new PlayerScoreUpdateData(PackageType.PLAYER_SCORE_UPDATE, score);

    // Send the update to the GameView
    sendUpdate(packageData);
  }

  public void updatePlayerLives(Player player) {
    int lives = player.getLives();

    // Check if player has no lives left
    if (lives <= 0) {
      // Player has no lives left, trigger game over
      gameOver();
    } else {
      // Player still has lives left, update the UI
      PlayerLivesUpdateData packageData =
          new PlayerLivesUpdateData(PackageType.DRAW_PLAYER_LIVES_UPDATE, lives);
      sendUpdate(packageData);
    }
  }

  private boolean shouldSpawnExitTile() {
    // Check if there's already an exit tile on the map
    for (Tiles tile : numTileMap) {
      if (tile == Tiles.EXIT) {
        return false; // Exit tile already exists, so don't spawn another one
      }
    }

    // Check if there's only one destructible tile left on the map
    int destructibleTileCount = 0;
    for (Tiles tile : numTileMap) {
      if (tile.isDestroyable()) {
        destructibleTileCount++;
      }
    }

    // If there's only one destructible tile left, always spawn an exit tile
    if (destructibleTileCount == 1) {
      return true;
    }

    // Calculate the probability of spawning an exit tile (e.g., 10% chance)
    double spawnChance = 0.1; // Adjust this value as needed

    // Generate a random number between 0 and 1
    double randomValue = Math.random();

    System.out.println(randomValue <= spawnChance);

    // Check if the random value falls within the spawn chance
    return randomValue <= spawnChance;
  }

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

            System.out.println("spawnato il tile exit");
            // Notify GameView to update display with the exit tile
            sendUpdate(new ExitTileSpawnData(PackageType.SPAWN_EXIT_TILE, x, y));
          } catch (Exception e) {
            // If an exception occurs during spawning, log it (optional)
            System.out.println("Failed to spawn exit tile: " + e.getMessage());
          }
        };

    // Schedule the spawning task with the specified delay
    executorService.schedule(spawnTask, delayInSeconds, TimeUnit.MILLISECONDS);
  }

  public void checkPlayerExitCollision() {
    if (player.collidesWithExitTile(exitTileHitBox)) {
      // Player collided with the exit tile, end the level
      endLevel();
    }
  }

  private void endLevel() {
    // Add logic to end the current level, e.g., load the next level or display victory screen
    System.out.println("Level ended!");
    level++;
    // Increment the level number
    int currentLevel = getLevel();
    System.out.println(getLevel());

    // Clear existing entities and power-ups
    entities.clear();
    powerUps.clear();

    loadLevel(Integer.toString(currentLevel));
  }

  private void gameOver() {
    int score = player.getScore();
    // Send an update to GameView indicating that the game is over
    GameOverUpdateData gameOverData = new GameOverUpdateData(PackageType.GAME_OVER, score);
    sendUpdate(gameOverData);
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
