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
import javafx.geometry.Rectangle2D;
import lombok.Getter;
import lombok.Setter;
import org.jbomberman.controller.KeyHandler;
import org.jbomberman.controller.MobHandler;
import org.jbomberman.model.entita.*;
import org.jbomberman.model.listener.*;
import org.jbomberman.model.tiles.GrassTile;
import org.jbomberman.model.tiles.ImmovableTile;
import org.jbomberman.model.tiles.Tile;
import org.jbomberman.view.Tiles;

@Getter
@Setter
public class Map extends Observable {

  private ArrayList<Tiles> numTileMap;

  private static Map instance;

  private List<Entity> entities;

  private Player player;

  private boolean mobsMovement;

  private List<Rectangle2D> tileHitBoxes;

  private ScheduledExecutorService executorService;

  private Set<Mob> mobsAffectedByExplosion = new HashSet<>();

  private Map() {
    executorService = Executors.newSingleThreadScheduledExecutor();
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
    for (int y = 0; y < 11; y++) {
      for (int x = 0; x < 13; x++) {
        Tiles tileType = numTileMap.get(y * 13 + x);
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
        // Handle other tile types as needed
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

      Direction direction = Direction.valueOf(player.get("direction").getAsString());

      int lives = player.get("lives").getAsInt();
      int score = player.get("score").getAsInt();

      int width = player.get("width").getAsInt();
      int height = player.get("height").getAsInt();

      this.player = new Player(posx, posy, width, height, lives, direction, score);

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
    // Load the map from the file
    loadMap(String.format("level/level%s/mappa_lvl%s.txt", level, level));

    // Read the json file
    loadEntities((String.format("level/level%s/level%s.json", level, level)));

    // thread per far muovere le entita' secondo il metodo move() all'interno di mob
    moveEntities();
  }

  public static Map getInstance() {
    if (instance == null) {
      instance = new Map(); // Create a new instance
    }
    return instance;
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

    Rectangle2D newHitBox = new Rectangle2D(player.getX() + xStep, player.getY() + yStep, 32, 32);

    if (collidesWithSolid(newHitBox)) return;
    // TODO: fixa il fatto che quando il player collide con un blocco, si appiccia al blocco

    player.move(xStep, yStep);

    sendUpdate(
        new PlayerMovementData(
            PackageType.MOVE_PLAYER,
            player.getX() - 8,
            player.getY() - 64,
            delta,
            oldX - 8,
            oldY - 64));
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
    if (collidesWithSolid(newHitBox)) {
      // If collision occurs, change direction and update hitbox with old coordinates
      Direction newDirection = chooseRandomValidDirection(mob);
      if (newDirection != null) {
        mob.setDirection(newDirection);
      }
      mob.updateHitBox(oldX, oldY); // Update hitbox with old coordinates
      return;
    }

    // Update the mob's position
    mob.move(newX, newY);

    // Create package data and send movement information to GameView
    MobMovementData mobMovementData =
        new MobMovementData(PackageType.MOB_MOVEMENT, mob.getType(), newX, newY, delta, oldX, oldY);
    sendUpdate(mobMovementData);
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

    int index = tileY * 13 + tileX;

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
        2,
        TimeUnit.SECONDS);
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
    if (tileIndexX < 0 || tileIndexX >= 13 || tileIndexY < 0 || tileIndexY >= 11) {
      return; // Out-of-bounds coordinates, stop the destruction
    }

    // Calculate the tile index based on the coordinates
    int tileIndex = tileIndexY * 13 + tileIndexX;

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
    }
  }

  private boolean isExplosionCollision(int tileX, int tileY) {
    int index = tileY * 13 + tileX;

    if (index >= 0 && index < numTileMap.size()) {
      return numTileMap.get(index).isCollidable();
    }

    return false;
  }

  private boolean isDestroyableTile(int tileX, int tileY) {
    int index = tileY * 13 + tileX;
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
    System.out.println("Checking collision between mobs and explosion...");

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

    System.out.println("Collision detection complete.");
  }

  private void handleMobExplosion(Mob mob) {
    // Handle collision between mob and explosion
    // For example, remove the mob from the game, update scores, etc.
    entities.remove(mob);
    // Notify GameView to remove the mob from the map
    sendUpdate(new RemoveMobData(PackageType.REMOVE_MOB, mob.getType()));
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
