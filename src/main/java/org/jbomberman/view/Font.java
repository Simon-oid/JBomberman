package org.jbomberman.view;

import java.io.InputStream;
import javafx.scene.image.Image;
import lombok.Getter;

public enum Font {
  SPRITE_0(Font.class.getResourceAsStream("font/0_font.png")),
  SPRITE_1(Font.class.getResourceAsStream("font/1_font.png")),
  SPRITE_2(Font.class.getResourceAsStream("font/2_font.png")),
  SPRITE_3(Font.class.getResourceAsStream("font/3_font.png")),
  SPRITE_4(Font.class.getResourceAsStream("font/4_font.png")),
  SPRITE_5(Font.class.getResourceAsStream("font/5_font.png")),
  SPRITE_6(Font.class.getResourceAsStream("font/6_font.png")),
  SPRITE_7(Font.class.getResourceAsStream("font/7_font.png")),
  SPRITE_8(Font.class.getResourceAsStream("font/8_font.png")),
  SPRITE_9(Font.class.getResourceAsStream("font/9_font.png"));

  @Getter private final Image image;

  Font(InputStream image) {
    this.image = new Image(image, 8, 14, true, false);
  }
}
