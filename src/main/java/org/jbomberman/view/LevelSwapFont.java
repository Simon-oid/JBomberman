package org.jbomberman.view;

import java.io.InputStream;
import javafx.scene.image.Image;
import lombok.Getter;

/** The level swap font enum */
public enum LevelSwapFont {
  STAGE1_LVL_1(LevelSwapFont.class.getResourceAsStream("levelswap_font/stage1_lvl_1.png")),
  STAGE1_LVL_2(LevelSwapFont.class.getResourceAsStream("levelswap_font/stage1_lvl_2.png"));

  /** The image of the level swap font */
  @Getter private final Image image;

  /**
   * The level swap font constructor
   *
   * @param image The image of the level swap font
   */
  LevelSwapFont(InputStream image) {
    this.image = new Image(image, 384, 192, true, false);
  }
}
