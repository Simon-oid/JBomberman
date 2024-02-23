package org.jbomberman.model.powerups;

import javafx.geometry.Rectangle2D;
import org.jbomberman.model.entita.Player;

public class IceCreamPowerUp extends PowerUp {
  public IceCreamPowerUp() {
    super(PowerUpType.ICE_CREAM);
  }

  @Override
  public void applyPowerUp(Player player) {
    player.incrementIceCream();
  }

  @Override
  public Rectangle2D getHitBox() {
    return new Rectangle2D(getX(), getY(), getHitboxWidth(), getHitboxHeight());
  }
}
