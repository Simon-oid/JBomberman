package org.jbomberman.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Getter;

/** The game over fonts enum */
@Getter
public enum GameOverFonts {
  SPRITE_C_OVER(
      new Image(
          GameOverFonts.class.getResource("scenesAssets/gameOver_font_C.png").toExternalForm())),
  SPRITE_E_OVER(
      new Image(
          GameOverFonts.class.getResource("scenesAssets/gameOver_font_E.png").toExternalForm())),
  SPRITE_I_OVER(
      new Image(
          GameOverFonts.class.getResource("scenesAssets/gameOver_font_I.png").toExternalForm())),
  SPRITE_N_OVER(
      new Image(
          GameOverFonts.class.getResource("scenesAssets/gameOver_font_N.png").toExternalForm())),
  SPRITE_O_OVER(
      new Image(
          GameOverFonts.class.getResource("scenesAssets/gameOver_font_O.png").toExternalForm())),
  SPRITE_S_OVER(
      new Image(
          GameOverFonts.class.getResource("scenesAssets/gameOver_font_S.png").toExternalForm())),
  SPRITE_T_OVER(
      new Image(
          GameOverFonts.class.getResource("scenesAssets/gameOver_font_T.png").toExternalForm())),
  SPRITE_U_OVER(
      new Image(
          GameOverFonts.class.getResource("scenesAssets/gameOver_font_U.png").toExternalForm())),
  SPRITE_Y_OVER(
      new Image(
          GameOverFonts.class.getResource("scenesAssets/gameOver_font_Y.png").toExternalForm())),
  SPRITE_Z_OVER(
      new Image(
          GameOverFonts.class
              .getResource("scenesAssets/gameOver_font_questionMark.png")
              .toExternalForm()));

  /** The image view of the game over fonts */
  private final ImageView imageView;

  /**
   * The game over fonts constructor
   *
   * @param image The image of the game over fonts
   */
  GameOverFonts(Image image) {
    this.imageView = new ImageView(image);
    this.imageView.setFitWidth(10); // Set the width of the ImageView
    this.imageView.setFitHeight(10); // Set the height of the ImageView
  }
}
