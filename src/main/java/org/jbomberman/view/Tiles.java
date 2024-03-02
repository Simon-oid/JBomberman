package org.jbomberman.view;

import java.io.InputStream;
import javafx.scene.image.Image;
import lombok.Getter;

public enum Tiles {
  GRASS(Tiles.class.getResourceAsStream("tiles/tile_livello1_0_16.png"), false, false),
  GRASS_SHADOW(Tiles.class.getResourceAsStream("tiles/tile_livello1_1_16.png"), false, false),
  IMMOVABLE(Tiles.class.getResourceAsStream("tiles/tile_livello1_2_16.png"), true, false),
  DESTROYABLE(Tiles.class.getResourceAsStream("tiles/tile_distruttibile_0.png"), true, true),
  GRASS_SHADOW_DESTROYABLE(
      Tiles.class.getResourceAsStream("tiles/tile_livello1_4_16.png"), false, false),
  EXIT(Tiles.class.getResourceAsStream("tiles/tile_distruttibile_3.png"), false, false),
  BORDER_HORIZONTAL(
      Tiles.class.getResourceAsStream("tiles/border_tile_horizontal.png"), true, false),
  BORDER_VERTICAL(Tiles.class.getResourceAsStream("tiles/border_tile_vertical.png"), true, false),
  BORDER_VERTICAL_RIGHT(
      Tiles.class.getResourceAsStream("tiles/border_tile_vertical_right.png"), true, false),
  BORDER_HORIZONTAL_DOWN(
      Tiles.class.getResourceAsStream("tiles/border_tile_horizontal_down.png"), true, false),
  BOMB_UP_POWERUP(Tiles.class.getResourceAsStream("tiles/border_bottomleft1.png"), true, false),
  DESTROYABLE1(Tiles.class.getResourceAsStream("tiles/tile_distruttibile_1.png"), true, true),
  DESTROYABLE2(Tiles.class.getResourceAsStream("tiles/tile_distruttibile_2.png"), true, true),
  DESTROYABLE3(Tiles.class.getResourceAsStream("tiles/tile_distruttibile_3.png"), true, true),
  DESTROYABLE_UNDER_NON_DESTRUCTIBLE(
      Tiles.class.getResourceAsStream("tiles/tile_distruttibile_0_immovable.png"), true, true),
  DESTROYABLE_UNDER_NON_DESTRUCTIBLE1(
      Tiles.class.getResourceAsStream("tiles/tile_distruttibile_1_immovable.png"), true, true),
  DESTROYABLE_UNDER_NON_DESTRUCTIBLE2(
      Tiles.class.getResourceAsStream("tiles/tile_distruttibile_2_immovable.png"), true, true),
  DESTROYABLE_UNDER_NON_DESTRUCTIBLE3(
      Tiles.class.getResourceAsStream("tiles/tile_distruttibile_3_immovable.png"), true, true),
  DESTROYED_TILE_0(Tiles.class.getResourceAsStream("tiles/destroyed_tile_0.png"), true, true),
  DESTROYED_TILE_1(Tiles.class.getResourceAsStream("tiles/destroyed_tile_1.png"), true, true),
  DESTROYED_TILE_2(Tiles.class.getResourceAsStream("tiles/destroyed_tile_2.png"), true, true),
  DESTROYED_TILE_3(Tiles.class.getResourceAsStream("tiles/destroyed_tile_3.png"), true, true),
  DESTROYED_TILE_4(Tiles.class.getResourceAsStream("tiles/destroyed_tile_4.png"), true, true),
  DESTROYED_TILE_5(Tiles.class.getResourceAsStream("tiles/destroyed_tile_5.png"), true, true),
  POWERUP_BOMBUP_0(Tiles.class.getResourceAsStream("tiles/powerUp_BombUp_0.png"), true, true),
  POWERUP_BOMBUP_1(Tiles.class.getResourceAsStream("tiles/powerUp_BombUp_1.png"), true, true),
  POWERUP_SKATE_0(Tiles.class.getResourceAsStream("tiles/powerUp_Skate_0.png"), true, true),
  POWERUP_SKATE_1(Tiles.class.getResourceAsStream("tiles/powerUp_Skate_1.png"), true, true);

  @Getter private final Image image;
  @Getter private final boolean isCollidable;
  @Getter private final boolean isDestroyable;

  Tiles(InputStream image, boolean isCollidable, boolean isDestroyable) {
    this.image = new Image(image, 16, 16, true, false);

    this.isCollidable = isCollidable;

    this.isDestroyable = isDestroyable;
  }

  public int size() {
    return 48;
  }
}
