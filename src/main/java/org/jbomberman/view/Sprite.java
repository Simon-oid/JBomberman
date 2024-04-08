package org.jbomberman.view;

import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Sprite {
  private ImageView imageView;
  private int id;
  private int initialX;
  private int initialY;

  public Sprite(ImageView imageView, int id, int initialX, int initialY) {
    this.imageView = imageView;
    this.id = id;
    this.initialX = initialX;
    this.initialY = initialY;
  }
}
