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

  /** The id of the mob */
  private int id;

  /** The type of the mob */
  private Type type;

  /** The speed of the mob */
  private int speed;

  /** The number of lives of the mob */
  private int lives;

  /** The duration of invincibility of the mob after spawning/getting hit */
  private int invincibilityDuration = 3; // In seconds

  /**
   * Constructor for the Mob class
   *
   * @param x The x-coordinate of the mob
   * @param y The y-coordinate of the mob
   * @param width The width of the mob
   * @param height The height of the mob
   * @param type The type of the mob
   * @param direction The direction of the mob
   * @param id The id of the mob
   */
  public Mob(int x, int y, int width, int height, Type type, Direction direction, int id) {
    super(x, y, width, height, direction);
    this.id = id;
    this.type = type;
    this.speed = 70;
    this.lives = (type == Type.PUROPEN) ? 1 : 2;
    initHitBox();
  }

  /** Initializes the hit box of the mob */
  @Override
  protected void initHitBox() {
    hitBox = new Rectangle2D(x + 2, y, width, height);
  }

  /**
   * Updates the hit box of the mob
   *
   * @param newX new x coordinates
   * @param newY new y coordinates
   */
  @Override
  public void updateHitBox(int newX, int newY) {
    hitBox = new Rectangle2D(newX, newY, width, height);
  }

  /** Spawns the mob on the map */
  @Override
  public void spawn() {
    setVulnerable(false); // Player becomes invincible
    // Schedule a task to make the player vulnerable again after the invincibility duration

    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    scheduler.schedule(() -> isVulnerable = true, invincibilityDuration, TimeUnit.SECONDS);
  }

  /**
   * Returns the hit box of the mob
   *
   * @return the hit box of the mob
   */
  @Override
  public Rectangle2D getHitBox() {
    return hitBox;
  }

  /**
   * Moves the mob to the new coordinates
   *
   * @param newX The new x-coordinate of the mob
   * @param newY The new y-coordinate of the mob
   */
  public void move(int newX, int newY) {
    // Update the mob's position
    setX(newX);
    setY(newY);
    updateHitBox(newX, newY);
  }

  /** Makes the mob invincible for a certain duration */
  public void invincible() {
    setVulnerable(false);
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    scheduler.schedule(() -> isVulnerable = true, invincibilityDuration, TimeUnit.SECONDS);
  }
}
