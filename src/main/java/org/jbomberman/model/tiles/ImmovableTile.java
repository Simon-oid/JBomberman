package org.jbomberman.model.tiles;

public class ImmovableTile extends Tile
{

    public ImmovableTile(int x, int y)
    {
        super(TileType.IMMOVABLE, true, x, y);
    }
}
