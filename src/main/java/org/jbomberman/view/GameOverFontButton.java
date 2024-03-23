package org.jbomberman.view;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

public class GameOverFontButton extends Button {
  public GameOverFontButton(String text) {
    HBox hbox = new HBox();
    hbox.setPadding(new Insets(10, 10, 10, 10)); // Add padding to the HBox

    Region spacer = new Region(); // Create a spacer
    spacer.setPrefWidth(35); // Set the width of the spacer to move the sprites to the right
    hbox.getChildren().add(spacer); // Add the spacer to the HBox

    for (char c : text.toCharArray()) {
      String spriteName = "SPRITE_" + Character.toUpperCase(c) + "_OVER";
      if (c == '?') {
        spriteName = "SPRITE_Z_OVER";
      }
      try {
        Image image = GameOverFonts.valueOf(spriteName).getImageView().getImage();
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(30); // Set the width of the ImageView
        imageView.setFitHeight(30); // Set the height of the ImageView
        hbox.getChildren().add(imageView);
      } catch (IllegalArgumentException e) {
        System.out.println("No sprite found for: " + spriteName);
      }
    }
    this.setGraphic(hbox);
    this.setStyle("-fx-background-color: transparent;");
  }
}
