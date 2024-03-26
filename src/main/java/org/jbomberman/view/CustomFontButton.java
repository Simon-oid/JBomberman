package org.jbomberman.view;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class CustomFontButton extends Button {
  public CustomFontButton(String text) {
    HBox hbox = new HBox();
    for (char c : text.toCharArray()) {
      ImageView sprite = LettersFont.valueOf("SPRITE_" + Character.toUpperCase(c)).getImageView();
      ImageView copy = new ImageView(sprite.getImage());
      copy.setFitWidth(18); // Set the width of the ImageView
      copy.setFitHeight(24); // Set the height of the ImageView
      hbox.getChildren().add(copy);
    }
    this.setGraphic(hbox);
    this.setStyle("-fx-background-color: transparent;");
  }
}
