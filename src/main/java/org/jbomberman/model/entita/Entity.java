package org.jbomberman.model.entita;

import javafx.geometry.Rectangle2D;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Entity {

  /** x coordinates of the entity */
  protected int x;

  /** y coordinates of the entity */
  protected int y;

  /** The amount of pixels the entity moves in the x direction */
  private int deltaX;

  /** The amount of pixels the entity moves in the y direction */
  private int deltaY;

  /** The speed of the entity */
  private int speed;

  /** The direction the entity is facing */
  Direction direction;

  /** The width of the entity */
  protected int width;

  /** The height of the entity */
  protected int height;

  /** The hitbox of the entity */
  protected Rectangle2D hitBox;

  /** The state of the entity */
  protected boolean isVulnerable;

  /**
   * Constructor for the entity
   *
   * @param x x coordinates of the entity
   * @param y y coordinates of the entity
   * @param width width of the entity
   * @param height height of the entity
   * @param direction direction the entity is facing
   */
  protected Entity(int x, int y, int width, int height, Direction direction) {

    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.direction = direction;
    speed = 400;
    this.isVulnerable = true;
    initHitBox();
  }

  /** abstract method to initialize the hitbox of the entity */
  protected abstract void initHitBox();

  /**
   * Method to update the hitbox of the entity
   *
   * @param newX new x coordinates
   * @param newY new y coordinates
   */
  public abstract void updateHitBox(int newX, int newY);

  /** method to spawn the entity */
  public abstract void spawn();

  /**
   * method to get the hitbox of the entity
   *
   * @return the hitbox of the entity
   */
  public abstract Rectangle2D getHitBox();
}
