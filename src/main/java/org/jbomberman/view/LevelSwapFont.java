package org.jbomberman.view;

import java.io.InputStream;
import javafx.scene.image.Image;
import lombok.Getter;

public enum LevelSwapFont {
  STAGE1_LVL_1(LevelSwapFont.class.getResourceAsStream("levelswap_font/stage1_lvl_1.png")),
  STAGE1_LVL_2(LevelSwapFont.class.getResourceAsStream("levelswap_font/stage1_lvl_2.png"));

  @Getter private final Image image;

  LevelSwapFont(InputStream image) {
    this.image = new Image(image, 384, 192, true, false);
  }
}
