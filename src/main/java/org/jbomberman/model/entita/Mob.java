package org.jbomberman.model.entita;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.geometry.Rectangle2D;
import lombok.Getter;
import lombok.Setter;

public class Mob extends Entity {

  @Getter @Setter private Type type;

  @Getter @Setter private int speed;

  @Getter @Setter private int invincibilityDuration = 2; // In seconds

  // private Rectangle2D mobHitBox = new Rectangle2D(8, 16, 32, 32);

  public Mob(int x, int y, int width, int height, Type type, Direction direction) {
    super(x, y, width, height, direction);
    this.type = type;
    this.speed = 0;
    initHitBox();
  }

  @Override
  protected void initHitBox() {
    hitBox = new Rectangle2D(x, y, width, height);
    System.out.println(hitBox); // 8, 16, 32, 32
  }

  @Override
  public void updateHitBox(int newX, int newY) {
    hitBox = new Rectangle2D(newX, newY, width, height);
  }

  @Override
  public void spawn() {
    setVulnerable(false); // Player becomes invincible
    // Schedule a task to make the player vulnerable again after the invincibility duration

    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
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

  //  public void move(int xStep, int yStep) {
  //    int oldX = getX();
  //    int oldY = getY();
  //
  //    int newX = oldX + xStep;
  //    int newY = oldY + yStep;
  //
  //    setX(newX);
  //    setY(newY);
  //    updateHitBox(newX, newY);
  //
  //    if (!isValidMove(newX, newY)) {
  //      Direction newDirection = chooseRandomValidDirection();
  //      if (newDirection != null) {
  //        setDirection(newDirection);
  //      }
  //    }
  //  }

  private Direction chooseRandomValidDirection() {
    // TODO: rework metodo choose random direction
    return Direction.DOWN;
  }

  private boolean isValidMove(int newX, int newY) {
    // TODO: rework metodo
    return true;
  }
}
