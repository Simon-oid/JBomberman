package org.jbomberman.model.powerups;

import java.util.concurrent.ScheduledFuture;
import javafx.geometry.Rectangle2D;
import lombok.Getter;
import lombok.Setter;
import org.jbomberman.model.entita.Player;

@Getter
@Setter
public abstract class PowerUp {
  private PowerUpType type;

  private Integer x; // X-coordinate
  private Integer y; // Y-coordinate

  // Define hitbox dimensions
  private final int hitboxWidth = 48;
  private final int hitboxHeight = 48;

  private ScheduledFuture<?> despawnTask;

  protected PowerUp(PowerUpType type) {
    this.type = type;
  }

  public abstract void applyPowerUp(Player player);

  public abstract Rectangle2D getHitBox();

  public void setDespawnTask(ScheduledFuture<?> despawnTask) {
    this.despawnTask = despawnTask;
  }
}
