package org.jbomberman.model.powerups;

import javafx.geometry.Rectangle2D;
import org.jbomberman.model.entita.Player;

public class BombUpPowerUp extends PowerUp {

  public BombUpPowerUp() {
    super(PowerUpType.BOMB_UP);
  }

  @Override
  public void applyPowerUp(Player player) {
    // implement logic to increase the player's bomb count
    player.incrementBombCount();
  }

  @Override
  public Rectangle2D getHitBox() {
    return new Rectangle2D(getX(), getY(), getHitboxWidth(), getHitboxHeight());
  }
}
