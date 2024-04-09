package org.jbomberman.model.tiles;

/** The grass tile class */
public class GrassTile extends Tile {

  /**
   * The grass tile constructor
   *
   * @param x The X-coordinate of the tile
   * @param y The Y-coordinate of the tile
   */
  public GrassTile(int x, int y) {
    super(TileType.GRASS, false, x, y);
  }
}
