package org.jbomberman.model.entita;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.geometry.Rectangle2D;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Player extends Entity {

  /** The number of lives of the player */
  private int lives;

  /** The speed of the player */
  private int speed;

  /** The number of bombs the player can place */
  private int bombCount;

  /** The score of the player */
  private int score;

  /** The radius of the explosion of the player's bombs */
  private int explosionRadius;

  /** The duration of invincibility of the player after spawning/getting hit */
  private int invincibilityDuration = 2; // In seconds

  /**
   * Constructor for the Player class
   *
   * @param x The x-coordinate of the player
   * @param y The y-coordinate of the player
   * @param width The width of the player
   * @param height The height of the player
   * @param lives The number of lives of the player
   * @param direction The direction of the player
   * @param score The score of the player
   */
  public Player(int x, int y, int width, int height, int lives, Direction direction, int score) {
    super(x, y, width, height, direction);
    this.lives = lives;
    this.score = score;
    this.speed = 200;
    this.bombCount = 5;
    explosionRadius = 2;
    initHitBox();
  }

  /** Initializes the hit box of the player */
  @Override
  protected void initHitBox() {
    hitBox = new Rectangle2D(x, y, width, height); // 8, 16, 32, 32
  }

  /**
   * Updates the hit box of the player
   *
   * @param newX new x coordinates
   * @param newY new y coordinates
   */
  @Override
  public void updateHitBox(int newX, int newY) {
    hitBox = new Rectangle2D(newX, newY, width, height);
  }

  /**
   * Spawns the player
   *
   * <p>When the player spawns, they become invincible for a short duration
   */
  @Override
  public void spawn() {
    setVulnerable(false); // Player becomes invincible
    // Schedule a task to make the player vulnerable again after the invincibility duration

    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    scheduler.schedule(() -> isVulnerable = true, invincibilityDuration, TimeUnit.SECONDS);
  }

  /**
   * Gets the hit box of the player
   *
   * @return The hit box of the player
   */
  @Override
  public Rectangle2D getHitBox() {
    return hitBox;
  }

  /**
   * Moves the player to the new coordinates
   *
   * @param xStep The number of pixels to move the player in the x-direction
   * @param yStep The number of pixels to move the player in the y-direction
   */
  public void move(int xStep, int yStep) {
    int newX = getX() + xStep;
    int newY = getY() + yStep;

    setX(newX);
    setY(newY);
    updateHitBox(newX, newY);
  }

  /** Increases the movement speed of the player */
  public void increaseMovementSpeed() {
    speed += 20;
    setScore(score += 10);
  }

  /** Increases the number of bombs the player can place */
  public void incrementBombCount() {
    bombCount += 1;
    setScore(score += 10);
  }

  /** regenerates the number of bombs the player can place */
  public void regenBombCount() {
    bombCount += 1;
  }

  /** Increases the score of the player */
  public void incrementIceCream() {
    score += 50000;
  }

  /** Decreases the number of lives of the player */
  public void decreaseLives() {
    lives -= 1;
  }

  /**
   * Makes the player take damage
   *
   * <p>When the player takes damage, they lose a life and become invincible for a short duration
   */
  public void takeDamage() {
    if (isVulnerable) {
      setVulnerable(false); // Player is no longer vulnerable
      decreaseLives();
      // Set a timer to make the player vulnerable again after invincibilityDuration
      Timer timer = new Timer();
      timer.schedule(
          new TimerTask() {
            @Override
            public void run() {
              setVulnerable(true); // Make the player vulnerable again
            }
          },
          invincibilityDuration * 3000L);
    }
  }

  /**
   * Checks if the player collides with an exit tile
   *
   * @param exitTileHitBox The hit box of the exit tile
   * @return True if the player collides with the exit tile, false otherwise
   */
  public boolean collidesWithExitTile(Rectangle2D exitTileHitBox) {
    Rectangle2D playerHitBox = getHitBox(); // Assuming you have a method to get the player's hitbox
    return playerHitBox.intersects(exitTileHitBox);
  }

  /** Decreases the number of bombs the player can place */
  public void decrementBombCount() {
    bombCount -= 1;
  }
}
