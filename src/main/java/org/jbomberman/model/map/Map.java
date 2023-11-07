package org.jbomberman.model.map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.geometry.Rectangle2D;
import lombok.Getter;
import lombok.Setter;
import org.jbomberman.controller.KeyHandler;
import org.jbomberman.model.entita.*;
import org.jbomberman.model.listener.*;
import org.jbomberman.model.tiles.GrassTile;
import org.jbomberman.model.tiles.ImmovableTile;
import org.jbomberman.model.tiles.Tile;
import org.jbomberman.view.Tiles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;

@Getter
@Setter
public class Map extends Observable
{


    private ArrayList<Tiles> numTileMap;

    private static Map instance;

    private List<Entity> entities;

    private Player player;

    private boolean mobsMovement;

    private List<Rectangle2D> tileHitBoxes;


    private Map()
    {
    }

    public void loadMap(String path)
    {
        ArrayList<Tiles> matrix = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Map.class.getResourceAsStream(path))))
        {
            for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine())
            {
                Arrays.stream(line.split(" "))
                        .mapToInt(Integer::parseInt)
                        .forEach(value -> matrix.add(Tiles.values()[value]));
            }
        } catch (IOException e)
        {
            throw new LoadMapException("Error while loading map!", e);
        }

        this.numTileMap = matrix;

        tileHitBoxes = new ArrayList<>();

        // Iterate through the Tile objects and add hitboxes of collidable tiles to the list
        for (int y = 0; y < 11; y++)
        {
            for (int x = 0; x < 13; x++)
            {
                Tiles tileType = numTileMap.get(y * 13 + x);
                Tile tile = createTile(x, y, tileType);

                tileHitBoxes.add(tile.getHitBox());

            }
        }


        LoadMapData packageData = new LoadMapData(PackageType.LOADMAP, matrix);
        sendUpdate(packageData);
    }

    private Tile createTile(int x, int y, Tiles tileType)
    {
        switch (tileType)
        {
            case GRASS:
                return new GrassTile(x, y); // Create and return a GrassTile instance
            case IMMOVABLE:
                return new ImmovableTile(x, y); // Create and return an ImmovableTile instance
            // Handle other tile types as needed
            default:
                return new GrassTile(x, y); // Return null or handle the case appropriately
        }
    }

    // ao bella
    public void sendUpdate(PackageData packageData)
    {
        setChanged();
        notifyObservers(packageData);
        clearChanged();
    }

    public void loadEntities(String filePath)
    {
        try (InputStreamReader fileReader = new InputStreamReader(Map.class.getResourceAsStream(filePath)))
        {
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

            JsonArray mobs = jsonObject.get("mob").getAsJsonArray();

            entities = new ArrayList<>();
            for (JsonElement mob : mobs)
            {
                Type type = Type.valueOf(mob.getAsJsonObject().get("type").getAsString());
                posx = mob.getAsJsonObject().get("posx").getAsInt();
                posy = mob.getAsJsonObject().get("posy").getAsInt();
                direction = Direction.valueOf(mob.getAsJsonObject().get("direction").getAsString());

                width = 0;
                height = 0;


                entities.add(new Mob(posx, posy, width, height, type, direction));

            }


        } catch (IOException e)
        {
            throw new LoadMapException("error while reading json file: " + e.getMessage(), e);
        }
    }

    public void loadLevel(String level)
    {
        // Load the map from the file
        loadMap(String.format("level/level%s/mappa_lvl%s.txt", level, level));

        // Read the json file
        loadEntities((String.format("level/level%s/level%s.json", level, level)));

        // thread per far muovere le entita' secondo il metodo move() all'interno di mob
        moveEntities();


    }

    public static Map getInstance()
    {
        if (instance == null)
        {
            instance = new Map(); // Create a new instance
        }
        return instance;
    }

    public void moveEntities()
    {

        mobsMovement = true;
        // Start the player's movement using AnimationTimer
        KeyHandler.getInstance().startMovement();

        // Create a thread to move mobs continuously using while loop
        Thread mobMoveThread = new Thread(() ->
        {
            Mob[] mobs = getMobs();
            while (mobsMovement)
            {
                for (Mob mob : mobs)
                {
                    mob.move(); // Call the move method of Mob
                }
                try
                {
                    Thread.sleep(100); // Adjust the sleep duration as needed
                } catch (InterruptedException e)
                {
                    Thread.currentThread().interrupt();
                }
            }
        });
        mobMoveThread.start();
    }

    public void movePlayer(int xStep, int yStep, double delta)
    {
        int oldX = player.getX();
        int oldY = player.getY();

        Rectangle2D newHitBox = new Rectangle2D(player.getX() + xStep, player.getY() + yStep, 32, 32);

        if (collidesWithSolid(newHitBox)) return;
        //TODO: fixa il fatto che quando il player collide con un blocco, si appiccia al blocco


        player.move(xStep, yStep);

        sendUpdate(new PlayerMovementData(PackageType.MOVE_PLAYER, player.getX() - 8, player.getY() - 64, delta, oldX - 8, oldY - 64));
    }

    public void spawnBomb()
    {
        int tileRow = player.getY() / 48;
        int tileColumn = player.getX() / 48;

        // Calculate the position of the bomb at the center of the tile
        int bombX = tileColumn * 48;
        int bombY = tileRow * 48;

        if (player.getBombCount() > 0)
        {
            boolean canSpawn = true;

            for (Entity entity : entities)
            {
                if (entity instanceof Bomb && entity.getX() == bombX && entity.getY() == bombY)
                {
                    canSpawn = false;
                    break;
                }
            }

            if (canSpawn)
            {

                Bomb bomb = new Bomb(bombX, bombY);
                entities.add(bomb);
                bomb.spawn();
                player.decrementBombCount(); // Decrement the player's bomb count

                BombSpawnData packageData = new BombSpawnData(PackageType.SPAWN_BOMB, bombX, bombY);
                sendUpdate(packageData);


            }
        }
    }

    public boolean collidesWithSolid(Rectangle2D newHitBox)
    {

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

    private boolean isCollidableTile(int leftX, int topY)
    {
        int tileX = leftX / Tiles.GRASS.size();
        int tileY = topY / Tiles.GRASS.size();

        int index = tileY * 13 + tileX;

        if (index >= 0 && index < numTileMap.size())
        {
            return numTileMap.get(index).isCollidable();
        }

        return false;
    }

    public void explodeBomb(Bomb bomb)
    {
        int explosionX = bomb.getX();
        int explosionY = bomb.getY();
        int range = bomb.getRange();

        bomb.setExploded(true);

        // Get the explosion ranges for each direction
        int[] ranges = explodeAdjacentTiles(explosionX, explosionY, range);
        System.out.println("right" + ranges[0] + " " + "left" + ranges[1] + " " + "up" + ranges[2] + " " + "down" + ranges[3]);

        BombExplosionData packageData = new BombExplosionData(PackageType.BOMB_EXPLOSION, explosionX, explosionY, ranges);
        sendUpdate(packageData);
    }

    // fai vedere a fede
    private int[] explodeAdjacentTiles(int x, int y, int playerRadius)
    {
        int[] distances = new int[]{playerRadius, playerRadius, playerRadius, playerRadius}; // Initialize with player range

        for (int i = 1; i <= playerRadius; i++)
        {
            // Right direction
            int rightX = x + i * 48;
            if (!isCollidableTile(rightX, y))
            {
                if (isDestroyableTile(rightX, y))
                {
                    handleTileDestruction(rightX / 48, y / 48);
                }
            } else
            {
                distances[0] = i - 1;
            }

            // Left direction
            int leftX = x - i * 48;
            if (!isCollidableTile(leftX, y))
            {
                if (isDestroyableTile(leftX, y))
                {
                    handleTileDestruction(leftX / 48, y / 48);
                }
            } else
            {
                distances[1] = i - 1;
            }

            // Up direction
            int upY = y - i * 48;
            if (!isCollidableTile(x, upY))
            {
                if (isDestroyableTile(x, upY))
                {
                    handleTileDestruction(x / 48, upY / 48);
                }
            } else
            {
                distances[2] = i - 1;
            }

            // Down direction
            int downY = y + i * 48;
            if (!isCollidableTile(x, downY))
            {
                if (isDestroyableTile(x, downY))
                {
                    handleTileDestruction(x / 48, downY / 48);
                }
            } else
            {
                distances[3] = i - 1;
            }
        }

        return distances;
    }

    private boolean isDestroyableTile(int x, int y)
    {
        if (x < 0 || x >= 13 || y < 0 || y >= 11)
        {
            return false; // Out-of-bounds coordinates, not destroyable
        }

        int index = y * 13 + x;
        Tiles tileType = numTileMap.get(index);

        return tileType.isDestroyable();
    }


    private boolean handleTileDestruction(int x, int y)
    {
        if (x < 0 || x >= 13 || y < 0 || y >= 11)
        {
            return true; // Out-of-bounds coordinates, stop the explosion
        }

        int index = y * 13 + x;
        Tiles tileType = numTileMap.get(index);

        if (tileType.isDestroyable())
        {
            // Create a TileDestructionData instance
            TileDestructionData destructionData = new TileDestructionData(PackageType.TILE_DESTRUCTION, index);

            // Notify GameView about the destruction of this tile
            sendUpdate(destructionData);

            System.out.println("distruggo il tile");
            // Replace destroyable tile with a GRASS tile
            numTileMap.set(index, Tiles.GRASS);

            return false; // Continue the explosion
        } else if (tileType.isCollidable())
        {
            return true; // Stop the explosion at a collidable but non-destroyable tile
        }
        return false; // Continue the explosion for other cases
    }


    public Mob[] getMobs()
    {
        List<Mob> mobList = new ArrayList<>();

        for (Entity entity : entities)
        {
            if (entity instanceof Mob mob)
            {
                mobList.add(mob);
            }
        }
        return mobList.toArray(new Mob[0]);

    }


}


