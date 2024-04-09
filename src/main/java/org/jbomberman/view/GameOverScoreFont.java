package org.jbomberman.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Getter;

/** The game over score font enum */
@Getter
public enum GameOverScoreFont {
  SPRITE_0(
      new Image(
          GameOverScoreFont.class
              .getResource("scenesAssets/gameOver_scoreFont_0.png")
              .toExternalForm())),
  SPRITE_1(
      new Image(
          GameOverScoreFont.class
              .getResource("scenesAssets/gameOver_scoreFont_1.png")
              .toExternalForm())),
  SPRITE_2(
      new Image(
          GameOverScoreFont.class
              .getResource("scenesAssets/gameOver_scoreFont_2.png")
              .toExternalForm())),
  SPRITE_3(
      new Image(
          GameOverScoreFont.class
              .getResource("scenesAssets/gameOver_scoreFont_3.png")
              .toExternalForm())),
  SPRITE_4(
      new Image(
          GameOverScoreFont.class
              .getResource("scenesAssets/gameOver_scoreFont_4.png")
              .toExternalForm())),
  SPRITE_5(
      new Image(
          GameOverScoreFont.class
              .getResource("scenesAssets/gameOver_scoreFont_5.png")
              .toExternalForm())),
  SPRITE_6(
      new Image(
          GameOverScoreFont.class
              .getResource("scenesAssets/gameOver_scoreFont_6.png")
              .toExternalForm())),
  SPRITE_7(
      new Image(
          GameOverScoreFont.class
              .getResource("scenesAssets/gameOver_scoreFont_7.png")
              .toExternalForm()));

  /** The image view of the game over score font */
  private final ImageView imageView;

  /**
   * The game over score font constructor
   *
   * @param image The image of the game over score font
   */
  GameOverScoreFont(Image image) {
    this.imageView = new ImageView(image);
    this.imageView.setFitWidth(30); // Set the width of the ImageView
    this.imageView.setFitHeight(30); // Set the height of the ImageView
  }
}
