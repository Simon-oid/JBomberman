package org.jbomberman.view;

import javafx.scene.image.Image;
import lombok.Getter;

import java.io.InputStream;

public enum FontMobKillFlicker {
  SPRITE_0_MOBKILL_FLICKER(
      FontMobKillFlicker.class.getResourceAsStream("font/0_font_mobkill_flicker.png")),
  SPRITE_1_MOBKILL_FLICKER(
      FontMobKillFlicker.class.getResourceAsStream("font/1_font_mobkill_flicker.png")),
  SPRITE_2_MOBKILL_FLICKER(
      FontMobKillFlicker.class.getResourceAsStream("font/2_font_mobkill_flicker.png")),
  SPRITE_3_MOBKILL_FLICKER(
      FontMobKillFlicker.class.getResourceAsStream("font/3_font_mobkill_flicker.png")),
  SPRITE_4_MOBKILL_FLICKER(
      FontMobKillFlicker.class.getResourceAsStream("font/4_font_mobkill_flicker.png")),
  SPRITE_5_MOBKILL_FLICKER(
      FontMobKillFlicker.class.getResourceAsStream("font/5_font_mobkill_flicker.png"));

  @Getter private final Image image;

  FontMobKillFlicker(InputStream image) {
    this.image = new Image(image, 8, 14, true, false);
  }
}
