package org.jbomberman.view;

import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Sprite {

  /** The image view of the sprite */
  private ImageView imageView;

  /** The id of the sprite */
  private int id;

  /** The initial x position of the sprite */
  private int initialX;

  /** The initial y position of the sprite */
  private int initialY;

  /**
   * The sprite constructor
   *
   * @param imageView The image view of the sprite
   * @param id The id of the sprite
   * @param initialX The initial x position of the sprite
   * @param initialY The initial y position of the sprite
   */
  public Sprite(ImageView imageView, int id, int initialX, int initialY) {
    this.imageView = imageView;
    this.id = id;
    this.initialX = initialX;
    this.initialY = initialY;
  }
}
