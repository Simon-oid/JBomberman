package org.jbomberman.view;

import java.io.InputStream;
import javafx.scene.image.Image;
import lombok.Getter;

public enum Entities {
  PLAYER(Entities.class.getResourceAsStream("entities/giocatore_down_000_16x32.png")),
  PLAYER_DOWN1(Entities.class.getResourceAsStream("entities/giocatore_down_001_16x32.png")),
  PLAYER_DOWN2(Entities.class.getResourceAsStream("entities/giocatore_down_002_16x32.png")),
  PLAYER_LEFT(Entities.class.getResourceAsStream("entities/giocatore_left_000_16x32.png")),
  PLAYER_LEFT1(Entities.class.getResourceAsStream("entities/giocatore_left_001_16x32.png")),
  PLAYER_LEFT2(Entities.class.getResourceAsStream("entities/giocatore_left_002_16x32.png")),
  PLAYER_RIGHT(Entities.class.getResourceAsStream("entities/giocatore_right_000_16x32.png")),
  PLAYER_RIGHT1(Entities.class.getResourceAsStream("entities/giocatore_right_001_16x32.png")),
  PLAYER_RIGHT2(Entities.class.getResourceAsStream("entities/giocatore_right_002_16x32.png")),
  PLAYER_UP(Entities.class.getResourceAsStream("entities/giocatore_up_000_16x32.png")),
  PLAYER_UP1(Entities.class.getResourceAsStream("entities/giocatore_up_001_16x32.png")),
  PLAYER_UP2(Entities.class.getResourceAsStream("entities/giocatore_up_002_16x32.png")),
  PUROPEN(Entities.class.getResourceAsStream("entities/puropen_down_0.png")),
  PUROPEN_1(Entities.class.getResourceAsStream("entities/puropen_down_1.png")),
  PUROPEN_2(Entities.class.getResourceAsStream("entities/puropen_down_2.png")),
  PUROPEN_3(Entities.class.getResourceAsStream("entities/puropen_down_3.png")),
  PUROPEN_UP_0(Entities.class.getResourceAsStream("entities/puropen_up_0.png")),
  PUROPEN_UP_1(Entities.class.getResourceAsStream("entities/puropen_up_1.png")),
  PUROPEN_UP_2(Entities.class.getResourceAsStream("entities/puropen_up_2.png")),
  PUROPEN_UP_3(Entities.class.getResourceAsStream("entities/puropen_up_3.png")),
  PUROPEN_LEFT_0(Entities.class.getResourceAsStream("entities/puropen_left_0.png")),
  PUROPEN_LEFT_1(Entities.class.getResourceAsStream("entities/puropen_left_1.png")),
  PUROPEN_LEFT_2(Entities.class.getResourceAsStream("entities/puropen_left_2.png")),
  PUROPEN_LEFT_3(Entities.class.getResourceAsStream("entities/puropen_left_3.png")),
  PUROPEN_RIGHT_0(Entities.class.getResourceAsStream("entities/puropen_right_0.png")),
  PUROPEN_RIGHT_1(Entities.class.getResourceAsStream("entities/puropen_right_1.png")),
  PUROPEN_RIGHT_2(Entities.class.getResourceAsStream("entities/puropen_right_2.png")),
  PUROPEN_RIGHT_3(Entities.class.getResourceAsStream("entities/puropen_right_3.png")),
  DENKYUN_0(Entities.class.getResourceAsStream("entities/denkyun_0.png")),
  DENKYUN_1(Entities.class.getResourceAsStream("entities/denkyun_1.png")),
  DENKYUN_2(Entities.class.getResourceAsStream("entities/denkyun_2.png")),
  DENKYUN_3(Entities.class.getResourceAsStream("entities/denkyun_3.png")),
  DENKYUN_4(Entities.class.getResourceAsStream("entities/denkyun_4.png")),
  DENKYUN_5(Entities.class.getResourceAsStream("entities/denkyun_5.png")),
  BOMB_1(Entities.class.getResourceAsStream("entities/bomb_1.png")),
  BOMB_2(Entities.class.getResourceAsStream("entities/bomb_2.png")),
  BOMB_3(Entities.class.getResourceAsStream("entities/bomb_3.png")),
  // bomb explosion sprites 0
  BOMB_EXPLOSION_CENTER_0(
      Entities.class.getResourceAsStream("entities/bomb_explosion_center_0.png")),
  BOMB_EXPLOSION_DOWN_0(Entities.class.getResourceAsStream("entities/bomb_explosion_down_0.png")),
  BOMB_EXPLOSION_DOWN_LAST_0(
      Entities.class.getResourceAsStream("entities/bomb_explosion_down_last_0.png")),
  BOMB_EXPLOSION_UP_0(Entities.class.getResourceAsStream("entities/bomb_explosion_up_0.png")),
  BOMB_EXPLOSION_UP_LAST_0(
      Entities.class.getResourceAsStream("entities/bomb_explosion_up_last_0.png")),
  BOMB_EXPLOSION_LEFT_0(Entities.class.getResourceAsStream("entities/bomb_explosion_left_0.png")),
  BOMB_EXPLOSION_LEFT_LAST_0(
      Entities.class.getResourceAsStream("entities/bomb_explosion_left_last_0.png")),
  BOMB_EXPLOSION_RIGHT_0(Entities.class.getResourceAsStream("entities/bomb_explosion_right_0.png")),
  BOMB_EXPLOSION_RIGHT_LAST_0(
      Entities.class.getResourceAsStream("entities/bomb_explosion_right_last_0.png")),
  // bomb explosion sprites 1
  BOMB_EXPLOSION_CENTER_1(
      Entities.class.getResourceAsStream("entities/bomb_explosion_center_1.png")),
  BOMB_EXPLOSION_DOWN_1(Entities.class.getResourceAsStream("entities/bomb_explosion_down_1.png")),
  BOMB_EXPLOSION_DOWN_LAST_1(
      Entities.class.getResourceAsStream("entities/bomb_explosion_down_last_1.png")),
  BOMB_EXPLOSION_UP_1(Entities.class.getResourceAsStream("entities/bomb_explosion_up_1.png")),
  BOMB_EXPLOSION_UP_LAST_1(
      Entities.class.getResourceAsStream("entities/bomb_explosion_up_last_1.png")),
  BOMB_EXPLOSION_LEFT_1(Entities.class.getResourceAsStream("entities/bomb_explosion_left_1.png")),
  BOMB_EXPLOSION_LEFT_LAST_1(
      Entities.class.getResourceAsStream("entities/bomb_explosion_left_last_1.png")),
  BOMB_EXPLOSION_RIGHT_1(Entities.class.getResourceAsStream("entities/bomb_explosion_right_1.png")),
  BOMB_EXPLOSION_RIGHT_LAST_1(
      Entities.class.getResourceAsStream("entities/bomb_explosion_right_last_1.png")),
  // bomb explosion sprites 2
  BOMB_EXPLOSION_CENTER_2(
      Entities.class.getResourceAsStream("entities/bomb_explosion_center_2.png")),
  BOMB_EXPLOSION_DOWN_2(Entities.class.getResourceAsStream("entities/bomb_explosion_down_2.png")),
  BOMB_EXPLOSION_DOWN_LAST_2(
      Entities.class.getResourceAsStream("entities/bomb_explosion_down_last_2.png")),
  BOMB_EXPLOSION_UP_2(Entities.class.getResourceAsStream("entities/bomb_explosion_up_2.png")),
  BOMB_EXPLOSION_UP_LAST_2(
      Entities.class.getResourceAsStream("entities/bomb_explosion_up_last_2.png")),
  BOMB_EXPLOSION_LEFT_2(Entities.class.getResourceAsStream("entities/bomb_explosion_left_2.png")),
  BOMB_EXPLOSION_LEFT_LAST_2(
      Entities.class.getResourceAsStream("entities/bomb_explosion_left_last_2.png")),
  BOMB_EXPLOSION_RIGHT_2(Entities.class.getResourceAsStream("entities/bomb_explosion_right_2.png")),
  BOMB_EXPLOSION_RIGHT_LAST_2(
      Entities.class.getResourceAsStream("entities/bomb_explosion_right_last_2.png")),
  // bomb explosion sprites 3
  BOMB_EXPLOSION_CENTER_3(
      Entities.class.getResourceAsStream("entities/bomb_explosion_center_3.png")),
  BOMB_EXPLOSION_DOWN_3(Entities.class.getResourceAsStream("entities/bomb_explosion_down_3.png")),
  BOMB_EXPLOSION_DOWN_LAST_3(
      Entities.class.getResourceAsStream("entities/bomb_explosion_down_last_3.png")),
  BOMB_EXPLOSION_UP_3(Entities.class.getResourceAsStream("entities/bomb_explosion_up_3.png")),
  BOMB_EXPLOSION_UP_LAST_3(
      Entities.class.getResourceAsStream("entities/bomb_explosion_up_last_3.png")),
  BOMB_EXPLOSION_LEFT_3(Entities.class.getResourceAsStream("entities/bomb_explosion_left_3.png")),
  BOMB_EXPLOSION_LEFT_LAST_3(
      Entities.class.getResourceAsStream("entities/bomb_explosion_left_last_3.png")),
  BOMB_EXPLOSION_RIGHT_3(Entities.class.getResourceAsStream("entities/bomb_explosion_right_3.png")),
  BOMB_EXPLOSION_RIGHT_LAST_3(
      Entities.class.getResourceAsStream("entities/bomb_explosion_right_last_3.png")),
  // bomb explosion sprites 4
  BOMB_EXPLOSION_CENTER_4(
      Entities.class.getResourceAsStream("entities/bomb_explosion_center_4.png")),
  BOMB_EXPLOSION_DOWN_4(Entities.class.getResourceAsStream("entities/bomb_explosion_down_4.png")),
  BOMB_EXPLOSION_DOWN_LAST_4(
      Entities.class.getResourceAsStream("entities/bomb_explosion_down_last_4.png")),
  BOMB_EXPLOSION_UP_4(Entities.class.getResourceAsStream("entities/bomb_explosion_up_4.png")),
  BOMB_EXPLOSION_UP_LAST_4(
      Entities.class.getResourceAsStream("entities/bomb_explosion_up_last_4.png")),
  BOMB_EXPLOSION_LEFT_4(Entities.class.getResourceAsStream("entities/bomb_explosion_left_4.png")),
  BOMB_EXPLOSION_LEFT_LAST_4(
      Entities.class.getResourceAsStream("entities/bomb_explosion_left_last_4.png")),
  BOMB_EXPLOSION_RIGHT_4(Entities.class.getResourceAsStream("entities/bomb_explosion_right_4.png")),
  BOMB_EXPLOSION_RIGHT_LAST_4(
      Entities.class.getResourceAsStream("entities/bomb_explosion_right_last_4.png")),
  VOID(Entities.class.getResourceAsStream("entities/VOID.png")),
  PLAYER_HIT_0(Entities.class.getResourceAsStream("entities/player_hit_0.png")),
  PLAYER_HIT_1(Entities.class.getResourceAsStream("entities/player_hit_1.png")),
  PLAYER_HIT_2(Entities.class.getResourceAsStream("entities/player_hit_2.png")),
  PLAYER_HIT_3(Entities.class.getResourceAsStream("entities/player_hit_3.png"));

  @Getter private final Image image;

  Entities(InputStream image) {
    this.image = new Image(image);
  }
}
