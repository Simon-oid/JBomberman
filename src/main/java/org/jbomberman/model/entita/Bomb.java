package org.jbomberman.model.entita;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.geometry.Rectangle2D;
import lombok.Getter;
import lombok.Setter;
import org.jbomberman.model.map.Map;

@Setter
@Getter
public class Bomb extends Entity {

  /** The delay before the bomb explodes in seconds. */
  private static final double EXPLOSION_DELAY = 3.4;

  /**
   * Whether the bomb has exploded. This is used to prevent the bomb from exploding multiple times.
   */
  private boolean exploded = false;

  /** The range of the bomb. */
  private int range;

  /**
   * Instantiates a new Bomb.
   *
   * @param x the x coordinate
   * @param y the y coordinate
   */
  public Bomb(int x, int y) {
    super(x, y, 48, 48, null); // Direction parameter is not used for Bomb
    range = 2;
    initHitBox();
  }

  /** Initializes the hitbox of the bomb. */
  @Override
  protected void initHitBox() {
    // Initialize hitbox using x, y, width, and height
    hitBox = new Rectangle2D(x, y, 48, 48);
  }

  /**
   * Updates the hitbox of the bomb.
   *
   * @param newX new x coordinate
   * @param newY new y coordinate
   */
  @Override
  public void updateHitBox(int newX, int newY) {
    // Update hitbox using new x, y coordinates
    hitBox = new Rectangle2D(newX, newY, width, height);
  }

  /** Spawns the bomb and schedules the explosion. */
  @Override
  public void spawn() {
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    scheduler.schedule(
        this::triggerExplosion, (long) (EXPLOSION_DELAY * 1000), TimeUnit.MILLISECONDS);
  }

  /** Triggers the explosion of the bomb. */
  private void triggerExplosion() {
    if (!exploded) {
      System.out.println("the explosion has been triggered");
      Map.getInstance().explodeBomb(this);
      exploded = true;

      Map.getInstance().getEntities().remove(this);
    }
  }

  /**
   * Gets the hitbox of the bomb.
   *
   * @return the hitbox of the bomb
   */
  @Override
  public Rectangle2D getHitBox() {
    return hitBox;
  }
}
