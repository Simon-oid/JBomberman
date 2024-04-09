package org.jbomberman.view;

import java.io.InputStream;
import javafx.scene.image.Image;

/** The main menu sprites enum */
public enum MainMenuSprites {
  BG(MainMenuSprites.class.getResourceAsStream("scenesAssets/SuperBombMainMenu.png")),
  FRAME(MainMenuSprites.class.getResourceAsStream("scenesAssets/gameOver_frame.png"));

  /** The image of the main menu sprites */
  private final Image image;

  /**
   * The main menu sprites constructor
   *
   * @param imageStream The image stream of the main menu sprites
   */
  MainMenuSprites(InputStream imageStream) {
    this.image = new Image(imageStream);
  }

  /**
   * Get the image of the main menu sprites
   *
   * @return The image of the main menu sprites
   */
  public Image getImage() {
    return image;
  }
}
