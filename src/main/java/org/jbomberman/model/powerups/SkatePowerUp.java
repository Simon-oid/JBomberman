package org.jbomberman.model.powerups;

import javafx.geometry.Rectangle2D;
import org.jbomberman.model.entita.Player;

public class SkatePowerUp extends PowerUp {

  public SkatePowerUp() {
    super(PowerUpType.SKATE);
  }

  @Override
  public void applyPowerUp(Player player) {
    // Implement logic to increase the player's movement speed
    player.increaseMovementSpeed();
  }

  @Override
  public Rectangle2D getHitBox() {
    return new Rectangle2D(getX(), getY(), getHitboxWidth(), getHitboxHeight());
  }
}
