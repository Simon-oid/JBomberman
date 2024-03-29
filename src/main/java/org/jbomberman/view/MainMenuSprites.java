package org.jbomberman.view;

import java.io.InputStream;
import javafx.scene.image.Image;

public enum MainMenuSprites {
  BG(MainMenuSprites.class.getResourceAsStream("scenesAssets/SuperBombMainMenu.png")),
  FRAME(MainMenuSprites.class.getResourceAsStream("scenesAssets/gameOver_frame.png"));
  private final Image image;

  MainMenuSprites(InputStream imageStream) {
    this.image = new Image(imageStream);
  }

  public Image getImage() {
    return image;
  }
}
