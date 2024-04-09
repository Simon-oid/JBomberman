package org.jbomberman.model.powerups;

import javafx.geometry.Rectangle2D;
import lombok.Getter;
import lombok.Setter;
import org.jbomberman.model.entita.Player;

@Getter
@Setter
public class SkatePowerUp extends PowerUp {

  /** The skate power up constructor */
  public SkatePowerUp() {
    super(PowerUpType.SKATE);
  }

  /**
   * Apply the power up to the player
   *
   * @param player The player to apply the power up to
   */
  @Override
  public void applyPowerUp(Player player) {
    // Implement logic to increase the player's movement speed
    player.increaseMovementSpeed();
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
