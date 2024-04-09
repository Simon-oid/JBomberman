package org.jbomberman.model.tiles;

import org.jbomberman.controller.PowerUpManager;
import org.jbomberman.model.powerups.PowerUp;
import org.jbomberman.model.powerups.PowerUpFactory;

/** The destroyable tile class */
public class DestroyableTile extends Tile {

  /**
   * The destroyable tile constructor
   *
   * @param x The X-coordinate of the tile
   * @param y The Y-coordinate of the tile
   */
  public DestroyableTile(int x, int y) {
    super(TileType.DESTROYABLE, true, x, y);
  }

  /**
   * Method to destroy the tile and spawn a power-up. You can implement the logic to spawn a
   * power-up when the tile is destroyed here.
   */
  public void destroy() {

    PowerUp spawnedPowerUp = PowerUpFactory.createRandomPowerUp();
    if (spawnedPowerUp != null) {
      spawnedPowerUp.setX(getX()); // Set the power-up's X-coordinate
      spawnedPowerUp.setY(getY()); // Set the power-up's Y-coordinate
      PowerUpManager.getInstance().spawnPowerUp(spawnedPowerUp); // Add to PowerUpManager
    }
  }
}
