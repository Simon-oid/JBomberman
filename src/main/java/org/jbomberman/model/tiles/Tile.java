package org.jbomberman.model.tiles;

import javafx.geometry.Rectangle2D;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Tile {
  /** The tile size */
  public static final int TILE_SIZE = 48;

  /** The type of the tile */
  private TileType type;

  /** Keeps track of whether the tile is collidable or not */
  private boolean isCollidable;

  /** The x coordinate of the tile */
  private int x;

  /** The y coordinate of the tile */
  private int y;

  /** The hit box of the tile */
  private Rectangle2D tileHitBox;

  /**
   * The tile constructor
   *
   * @param type The type of the tile
   * @param isCollidable Whether the tile is collidable or not
   * @param x The X-coordinate of the tile
   * @param y The Y-coordinate of the tile
   */
  protected Tile(TileType type, boolean isCollidable, int x, int y) {
    this.type = type;
    this.isCollidable = isCollidable;
    this.x = x;
    this.y = y;
    tileHitBox = new Rectangle2D(x * 48.0, y * 48.0, 48, 48);
  }

  /**
   * Get the index of the tile
   *
   * @return The index of the tile
   */
  public static int[] getPositionFromIndex(int index) {

    int y = index / 13; // Calculate y-coordinate
    int x = index % 13; // Calculate x-coordinate

    return new int[] {x, y};
  }

  /**
   * Gets the hitbox of the tile
   *
   * @return The hitbox of the tile
   */
  public Rectangle2D getHitBox() {
    return tileHitBox;
  }
}
