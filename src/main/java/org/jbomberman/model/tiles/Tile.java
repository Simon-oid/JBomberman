package org.jbomberman.model.tiles;

import javafx.geometry.Rectangle2D;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Tile
{
    public static final int TILE_SIZE = 48;
    private TileType type;
    private boolean isCollidable;
    private int x;
    private int y;
    private Rectangle2D tileHitBox;

    protected Tile(TileType type, boolean isCollidable, int x, int y)
    {
        this.type = type;
        this.isCollidable = isCollidable;
        this.x = x;
        this.y = y;
        tileHitBox = new Rectangle2D(x * 48.0, y * 48.0, 48, 48);
    }


    public static int[] getPositionFromIndex(int index)
    {

        int y = index / 13; // Calculate y-coordinate
        int x = index % 13; // Calculate x-coordinate

        return new int[]{x, y};
    }

    public Rectangle2D getHitBox()
    {
        return tileHitBox;
    }

}