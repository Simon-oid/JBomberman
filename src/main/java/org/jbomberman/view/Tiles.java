package org.jbomberman.view;

import javafx.scene.image.Image;
import lombok.Getter;

import java.io.InputStream;

public enum Tiles
{
    GRASS(Tiles.class.getResourceAsStream("tiles/tile_livello1_0_16.png"), false, false),
    GRASS_SHADOW(Tiles.class.getResourceAsStream("tiles/tile_livello1_1_16.png"), false, false),
    IMMOVABLE(Tiles.class.getResourceAsStream("tiles/tile_livello1_2_16.png"), true, false),
    DESTROYABLE(Tiles.class.getResourceAsStream("tiles/tile_distruttibile_0.png"), true, true),
    EXIT(Tiles.class.getResourceAsStream("tiles/tile_livello1_0_16.png"), false, false);

    @Getter
    private final Image image;
    @Getter
    private final boolean isCollidable;
    @Getter
    private final boolean isDestroyable;

    Tiles(InputStream image, boolean isCollidable, boolean isDestroyable)
    {
        this.image = new Image(image, 16, 16, true, false);

        this.isCollidable = isCollidable;

        this.isDestroyable = isDestroyable;
    }

    public int size()
    {
        return 48;
    }

}
