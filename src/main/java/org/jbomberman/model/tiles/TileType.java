package org.jbomberman.model.tiles;

public enum TileType
{
    GRASS(0),
    GRASS_SHADOW(1),
    IMMOVABLE(2),
    DESTROYABLE(3),
    EXIT(4);

    private final int value;

    TileType(int value)
    {
        this.value = value;
    }
}
