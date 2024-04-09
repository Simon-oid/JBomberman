package org.jbomberman.model.tiles;

import org.jbomberman.model.map.Map;
import org.jbomberman.model.powerups.IceCreamPowerUp;
import org.jbomberman.model.powerups.PowerUp;

/**
 * The ExitTile class represents the exit tile in the game. When the exit tile is hit by a bomb
 * explosion, an IceCreamPowerUp is spawned on an adjacent tile.
 */
public class ExitTile extends Tile {

  /**
   * The exit tile constructor.
   *
   * @param x The X-coordinate of the tile
   * @param y The Y-coordinate of the tile
   */
  public ExitTile(int x, int y) {
    super(TileType.EXIT, false, x, y);
  }

  /**
   * Method to spawn an IceCreamPowerUp on an adjacent tile when the exit tile is hit by a bomb
   * explosion.
   */
  public void spawnIceCreamPowerUp() {
    // Check if the tile is hit by a bomb explosion
    if (isHitByBombExplosion()) {
      // Find an adjacent tile to spawn the IceCreamPowerUp
      Tile adjacentTile = findAdjacentTile();
      if (adjacentTile != null) {
        // Attempt to spawn an IceCreamPowerUp on the adjacent tile
        PowerUp spawnedPowerUp = new IceCreamPowerUp(); // coordinate sono sbagliate
        spawnedPowerUp.setX(adjacentTile.getX()); // Set the power-up's X-coordinate
        spawnedPowerUp.setY(adjacentTile.getY()); // Set the power-up's Y-coordinate
      }
    }
  }

  /**
   * Helper method to check if the tile is hit by a bomb explosion. You need to implement the logic
   * to detect bomb explosions and collisions with this tile.
   *
   * @return true if the tile is hit by a bomb explosion, false otherwise.
   */
  private boolean isHitByBombExplosion() {
    // Implement the logic to check if the tile is hit by a bomb explosion
    // For example, check if there is a bomb explosion at the tile's coordinates
    return false;
  }

  /**
   * Helper method to find an adjacent tile to spawn the IceCreamPowerUp.
   *
   * @return an adjacent valid tile or null if no valid tile is found.
   */
  private Tile findAdjacentTile() {
    int x = getX();
    int y = getY();

    // Get an instance of the Map class
    Map map = Map.getInstance();

    // Define the possible adjacent directions
    int[][] adjacentDirections = {
      {0, -1}, // Above
      {0, 1}, // Below
      {-1, 0}, // Left
      {1, 0} // Right
    };

    // Iterate over the adjacent directions
    for (int[] direction : adjacentDirections) {
      int newX = x + direction[0];
      int newY = y + direction[1];

      // Check if the tile is valid
      {
        return new GrassTile(newX, newY);
      }
    }

    return null; // No valid adjacent tile found
  }
}
