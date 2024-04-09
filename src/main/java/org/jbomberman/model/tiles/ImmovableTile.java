package org.jbomberman.model.tiles;

/** The immovable tile class */
public class ImmovableTile extends Tile {

  /**
   * The immovable tile constructor
   *
   * @param x The X-coordinate of the tile
   * @param y The Y-coordinate of the tile
   */
  public ImmovableTile(int x, int y) {
    super(TileType.IMMOVABLE, true, x, y);
  }
}
