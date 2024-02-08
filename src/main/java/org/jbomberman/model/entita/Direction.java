package org.jbomberman.model.entita;

import lombok.Getter;

@Getter
public enum Direction {
  UP(0, -1),
  DOWN(0, 1),
  LEFT(-1, 0),
  RIGHT(1, 0);

  private final int X;
  private final int Y;

  Direction(int deltaX, int deltaY) {
    this.X = deltaX;
    this.Y = deltaY;
  }
}
