package org.jbomberman.model.powerups;

import javafx.geometry.Rectangle2D;
import org.jbomberman.model.entita.Player;

public class IceCreamPowerUp extends PowerUp {
  /** The ice cream power up constructor */
  public IceCreamPowerUp() {
    super(PowerUpType.ICE_CREAM);
  }

  /**
   * Apply the power up to the player
   *
   * @param player The player to apply the power up to
   */
  @Override
  public void applyPowerUp(Player player) {
    player.incrementIceCream();
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
