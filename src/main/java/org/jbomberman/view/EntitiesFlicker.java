package org.jbomberman.view;

import java.io.InputStream;
import javafx.scene.image.Image;
import lombok.Getter;

public enum EntitiesFlicker {
  PLAYER_FLICKER(
      Entities.class.getResourceAsStream("entities/flicker/giocatore_down_flicker_0.png")),
  PLAYER_DOWN1_FLICKER(
      Entities.class.getResourceAsStream("entities/flicker/giocatore_down_flicker_1.png")),
  PLAYER_DOWN2_FLICKER(
      Entities.class.getResourceAsStream("entities/flicker/giocatore_down_flicker_2.png")),
  PLAYER_LEFT_FLICKER(
      Entities.class.getResourceAsStream("entities/flicker/giocatore_left_flicker_0.png")),
  PLAYER_LEFT1_FLICKER(
      Entities.class.getResourceAsStream("entities/flicker/giocatore_left_flicker_1.png")),
  PLAYER_LEFT2_FLICKER(
      Entities.class.getResourceAsStream("entities/flicker/giocatore_left_flicker_2.png")),
  PLAYER_RIGHT_FLICKER(
      Entities.class.getResourceAsStream("entities/flicker/giocatore_right_flicker_0.png")),
  PLAYER_RIGHT1_FLICKER(
      Entities.class.getResourceAsStream("entities/flicker/giocatore_right_flicker_1.png")),
  PLAYER_RIGHT2_FLICKER(
      Entities.class.getResourceAsStream("entities/flicker/giocatore_right_flicker_2.png")),
  PLAYER_UP_FLICKER(
      Entities.class.getResourceAsStream("entities/flicker/giocatore_up_flicker_0.png")),
  PLAYER_UP1_FLICKER(
      Entities.class.getResourceAsStream("entities/flicker/giocatore_up_flicker_1.png")),
  PLAYER_UP2_FLICKER(
      Entities.class.getResourceAsStream("entities/flicker/giocatore_up_flicker_2.png")),
  PUROPEN_FLICKER(
      Entities.class.getResourceAsStream("entities/flicker/puropen_down_flicker_0.png")),
  PUROPEN_1_FLICKER(
      Entities.class.getResourceAsStream("entities/flicker/puropen_down_flicker_1.png")),
  PUROPEN_2_FLICKER(
      Entities.class.getResourceAsStream("entities/flicker/puropen_down_flicker_2.png")),
  PUROPEN_3_FLICKER(
      Entities.class.getResourceAsStream("entities/flicker/puropen_down_flicker_3.png")),
  PUROPEN_UP_0_FLICKER(
      Entities.class.getResourceAsStream("entities/flicker/puropen_up_flicker_0.png")),
  PUROPEN_UP_1_FLICKER(
      Entities.class.getResourceAsStream("entities/flicker/puropen_up_flicker_1.png")),
  PUROPEN_UP_2_FLICKER(
      Entities.class.getResourceAsStream("entities/flicker/puropen_up_flicker_2.png")),
  PUROPEN_UP_3_FLICKER(
      Entities.class.getResourceAsStream("entities/flicker/puropen_up_flicker_3.png")),
  PUROPEN_LEFT_0_FLICKER(
      Entities.class.getResourceAsStream("entities/flicker/puropen_left_flicker_0.png")),
  PUROPEN_LEFT_1_FLICKER(
      Entities.class.getResourceAsStream("entities/flicker/puropen_left_flicker_1.png")),
  PUROPEN_LEFT_2_FLICKER(
      Entities.class.getResourceAsStream("entities/flicker/puropen_left_flicker_2.png")),
  PUROPEN_LEFT_3_FLICKER(
      Entities.class.getResourceAsStream("entities/flicker/puropen_left_flicker_3.png")),
  PUROPEN_RIGHT_0_FLICKER(
      Entities.class.getResourceAsStream("entities/flicker/puropen_right_flicker_0.png")),
  PUROPEN_RIGHT_1_FLICKER(
      Entities.class.getResourceAsStream("entities/flicker/puropen_right_flicker_1.png")),
  PUROPEN_RIGHT_2_FLICKER(
      Entities.class.getResourceAsStream("entities/flicker/puropen_right_flicker_2.png")),
  PUROPEN_RIGHT_3_FLICKER(
      Entities.class.getResourceAsStream("entities/flicker/puropen_right_flicker_3.png")),
  DENKYUN_0_FLICKER(Entities.class.getResourceAsStream("entities/flicker/denkyun_0_flicker.png")),
  DENKYUN_1_FLICKER(Entities.class.getResourceAsStream("entities/flicker/denkyun_1_flicker.png")),
  DENKYUN_2_FLICKER(Entities.class.getResourceAsStream("entities/flicker/denkyun_2_flicker.png")),
  DENKYUN_3_FLICKER(Entities.class.getResourceAsStream("entities/flicker/denkyun_3_flicker.png")),
  DENKYUN_4_FLICKER(Entities.class.getResourceAsStream("entities/flicker/denkyun_4_flicker.png")),
  DENKYUN_5_FLICKER(Entities.class.getResourceAsStream("entities/flicker/denkyun_5_flicker.png"));
  @Getter private final Image image;

  EntitiesFlicker(InputStream image) {
    this.image = new Image(image);
  }
}
