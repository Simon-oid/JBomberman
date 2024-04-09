package org.jbomberman.model.powerups;

import javafx.geometry.Rectangle2D;
import org.jbomberman.model.entita.Player;

public class BombUpPowerUp extends PowerUp {

  /** The bombup power up constructor */
  public BombUpPowerUp() {
    super(PowerUpType.BOMB_UP);
  }

  /**
   * Apply the power up to the player
   *
   * @param player The player to apply the power up to
   */
  @Override
  public void applyPowerUp(Player player) {
    // implement logic to increase the player's bomb count
    player.incrementBombCount();
  }

  /**
   * Get the hit box of the power up
   *
   * @return The hit box of the power up
   */
  @Override
  public Rectangle2D getHitBox() {
    return new Rectangle2D(getX(), getY(), 48, 48);
  }
}
