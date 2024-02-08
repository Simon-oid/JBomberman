package org.jbomberman.model.entita;

import javafx.geometry.Rectangle2D;
import lombok.Getter;
import lombok.Setter;
import org.jbomberman.model.map.Map;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Setter
@Getter
public class Bomb extends Entity {

  private static final long EXPLOSION_DELAY = 2; // 2 seconds

  private boolean exploded = false;

  private int range;

  public Bomb(int x, int y) {
    super(x, y, 48, 48, null); // Direction parameter is not used for Bomb
    range = 2;
    initHitBox();
  }

  @Override
  protected void initHitBox() {
    // Initialize hitbox using x, y, width, and height
    hitBox = new Rectangle2D(x, y, 48, 48);
  }

  @Override
  public void updateHitBox(int newX, int newY) {
    // Update hitbox using new x, y coordinates
    hitBox = new Rectangle2D(newX, newY, width, height);
  }

  @Override
  public void spawn() {
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    scheduler.schedule(this::triggerExplosion, EXPLOSION_DELAY, TimeUnit.SECONDS);
    System.out.println("the bomb timer has been initialized");
  }

  private void triggerExplosion() {
    if (!exploded) {
      System.out.println("the explosion has been triggered");
      Map.getInstance().explodeBomb(this);
      exploded = true;

      Map.getInstance().getEntities().remove(this);
    }
  }

  @Override
  public Rectangle2D getHitBox() {
    return hitBox;
  }
}
