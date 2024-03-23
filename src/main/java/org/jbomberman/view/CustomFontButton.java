package org.jbomberman.view;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class CustomFontButton extends Button {
  public CustomFontButton(String text) {
    HBox hbox = new HBox();
    for (char c : text.toCharArray()) {
      hbox.getChildren()
          .add(LettersFont.valueOf("SPRITE_" + Character.toUpperCase(c)).getImageView());
    }
    this.setGraphic(hbox);
    this.setStyle("-fx-background-color: transparent;");
  }
}
