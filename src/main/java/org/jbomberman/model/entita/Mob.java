package org.jbomberman.model.entita;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.geometry.Rectangle2D;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Mob extends Entity {

  private Type type;

  private int speed;

  private int lives; // Number of lives for the mob

  private int invincibilityDuration = 3; // In seconds

  // private Rectangle2D mobHitBox = new Rectangle2D(8, 16, 32, 32);

  public Mob(int x, int y, int width, int height, Type type, Direction direction) {
    super(x, y, width, height, direction);
    this.type = type;
    this.speed = 50;
    this.lives = (type == Type.PUROPEN) ? 1 : 2;
    initHitBox();
  }

  @Override
  protected void initHitBox() {
    hitBox = new Rectangle2D(x, y, width, height);
    // System.out.println(hitBox); // 8, 16, 32, 32
  }

  @Override
  public void updateHitBox(int newX, int newY) {
    hitBox = new Rectangle2D(newX, newY, width, height);
  }

  @Override
  public void spawn() {
    setVulnerable(false); // Player becomes invincible
    // Schedule a task to make the player vulnerable again after the invincibility duration

    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    scheduler.schedule(() -> isVulnerable = true, invincibilityDuration, TimeUnit.SECONDS);
  }

  @Override
  public Rectangle2D getHitBox() {
    return hitBox;
  }

  public void move(int newX, int newY) {
    // Update the mob's position
    setX(newX);
    setY(newY);
    updateHitBox(newX, newY);
  }

  public void invincible() {
    setVulnerable(false);
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    scheduler.schedule(() -> isVulnerable = true, invincibilityDuration, TimeUnit.SECONDS);
  }
}
