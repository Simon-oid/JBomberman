package org.jbomberman.model.tiles;

/** The tile type enum */
public enum TileType {
  /** The grass tile */
  GRASS(0),
  /** The grass shadow tile */
  GRASS_SHADOW(1),
  /** The immovable tile */
  IMMOVABLE(2),
  /** The destroyable tile */
  DESTROYABLE(3),
  /** The exit tile */
  EXIT(4),
  /** The horizontal border tile */
  BORDER_HORIZONTAL(5),
  /** The vertical border tile */
  BORDER_VERTICAL(6),
  /** The top left corner border tile */
  TOP_LEFT_CORNER(7);

  /** The value of the tile type */
  private final int value;

  /**
   * The tile type constructor
   *
   * @param value The value of the tile type
   */
  TileType(int value) {
    this.value = value;
  }
}
