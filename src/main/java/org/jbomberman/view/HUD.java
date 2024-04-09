package org.jbomberman.view;

import java.io.InputStream;
import javafx.scene.image.Image;
import lombok.Getter;

/** The HUD enum */
public enum HUD {
  HUD_SPRITE(HUD.class.getResourceAsStream("hud/hud_prova.png"));

  /** The image of the HUD */
  @Getter private final Image image;

  /**
   * The HUD constructor
   *
   * @param image The image of the HUD
   */
  HUD(InputStream image) {
    this.image = new Image(image, 256, 32, true, false);
  }
}
