package org.jbomberman.model.powerups;

import java.util.concurrent.ScheduledFuture;
import javafx.geometry.Rectangle2D;
import lombok.Getter;
import lombok.Setter;
import org.jbomberman.model.entita.Player;

@Getter
@Setter
public abstract class PowerUp {

  /** The type of the power up */
  private PowerUpType type;

  /** The x coordinate of the power up */
  private Integer x; // X-coordinate

  /** The y coordinate of the power up */
  private Integer y; // Y-coordinate

  /** The width of the hit box */
  private static final int hitboxWidth = 48;

  /** The height of the hit box */
  private static final int hitboxHeight = 48;

  /** The despawn task scheduler */
  private ScheduledFuture<?> despawnTask;

  /**
   * The power up constructor
   *
   * @param type The type of the power up
   */
  protected PowerUp(PowerUpType type) {
    this.type = type;
  }

  /**
   * Applys the power up to the player
   *
   * @param player The player to apply the power up to
   */
  public abstract void applyPowerUp(Player player);

  /**
   * Get the hit box of the power up
   *
   * @return The hit box of the power up
   */
  public abstract Rectangle2D getHitBox();

  /**
   * Schedule the despawn task
   *
   * @param despawnTask The despawn task
   */
  public void setDespawnTask(ScheduledFuture<?> despawnTask) {
    this.despawnTask = despawnTask;
  }
}
