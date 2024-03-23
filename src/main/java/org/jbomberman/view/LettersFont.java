package org.jbomberman.view;

import java.io.InputStream;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Getter;

public enum LettersFont {
  SPRITE_Q(LettersFont.class.getResourceAsStream("font/Q_bomberman_font.png")),
  SPRITE_U(LettersFont.class.getResourceAsStream("font/U_bomberman_font.png")),
  SPRITE_I(LettersFont.class.getResourceAsStream("font/I_bomberman_font.png")),
  SPRITE_T(LettersFont.class.getResourceAsStream("font/T_bomberman_font.png"));

  @Getter private final ImageView imageView;

  LettersFont(InputStream imageStream) {
    Image image = new Image(imageStream);
    this.imageView = new ImageView(image);
    this.imageView.setFitWidth(36); // Set the width of the ImageView
    this.imageView.setFitHeight(48); // Set the height of the ImageView
  }
}
