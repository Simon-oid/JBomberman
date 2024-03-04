package org.jbomberman.view;

import java.io.InputStream;
import javafx.scene.image.Image;
import lombok.Getter;

public enum FontMobKill {
  SPRITE_0_MOBKILL(FontMobKill.class.getResourceAsStream("font/0_font_mobkill.png")),
  SPRITE_1_MOBKILL(FontMobKill.class.getResourceAsStream("font/1_font_mobkill.png")),
  SPRITE_2_MOBKILL(FontMobKill.class.getResourceAsStream("font/2_font_mobkill.png")),
  SPRITE_3_MOBKILL(FontMobKill.class.getResourceAsStream("font/3_font_mobkill.png")),
  SPRITE_4_MOBKILL(FontMobKill.class.getResourceAsStream("font/4_font_mobkill.png")),
  SPRITE_5_MOBKILL(FontMobKill.class.getResourceAsStream("font/5_font_mobkill.png"));

  @Getter private final Image image;

  FontMobKill(InputStream image) {
    this.image = new Image(image, 8, 14, true, false);
  }
}
