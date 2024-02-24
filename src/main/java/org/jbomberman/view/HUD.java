package org.jbomberman.view;

import java.io.InputStream;
import javafx.scene.image.Image;
import lombok.Getter;

public enum HUD {
  HUD_SPRITE(HUD.class.getResourceAsStream("hud/hud_prova.png"));

  @Getter private final Image image;

  HUD(InputStream image) {
    this.image = new Image(image, 256, 32, true, false);
  }
}
