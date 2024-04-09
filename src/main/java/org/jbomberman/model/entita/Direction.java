package org.jbomberman.model.entita;

import lombok.Getter;

@Getter
public enum Direction {
  /** Enumerates the possible directions of movement. */
  UP(0, -1),
  DOWN(0, 1),
  LEFT(-1, 0),
  RIGHT(1, 0),
  NONE(0, 0);

  /** The x coordinate of the direction. */
  private final int X;

  /** The y coordinate of the direction. */
  private final int Y;

  /**
   * Constructor of the enum.
   *
   * @param deltaX the x coordinate of the direction.
   * @param deltaY the y coordinate of the direction.
   */
  Direction(int deltaX, int deltaY) {
    this.X = deltaX;
    this.Y = deltaY;
  }
}
