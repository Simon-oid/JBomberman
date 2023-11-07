package org.jbomberman.view;

import javafx.scene.image.Image;
import lombok.Getter;

import java.io.InputStream;

public enum Entities
{
    PLAYER(Entities.class.getResourceAsStream("entities/giocatore_down_000_16x32.png")),
    PLAYER_STRONZO(Entities.class.getResourceAsStream("entities/giocatore_up_000_16x32.png")),
    PUROPEN(Entities.class.getResourceAsStream("entities/Omo.jpeg")),
    DENKYUN(Entities.class.getResourceAsStream("entities/Omo.jpeg")),
    BOMB(Entities.class.getResourceAsStream("entities/Omo.jpeg")),
    EXPLOSION(Entities.class.getResourceAsStream("tiles/tile_distruttibile_0.png"));

    @Getter
    private final Image image;

    Entities(InputStream image)
    {
        this.image = new Image(image);
    }

}