package org.jbomberman.view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class Menu implements Initializable {

  /** The FXML exit button element */
  @FXML public Button exit;

  /** The FXML play button element */
  @FXML public Button play;

  /** The FXML leaderboard button element */
  @FXML public Button leaderboard;

  /**
   * Initialize the menu
   *
   * @param location The location
   * @param resources The resources
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    exit.setOnAction(event -> SceneController.getInstance().exit());
    play.setOnAction(event -> SceneController.getInstance().switchTo(Roots.PLAYER_SELECTION));
    leaderboard.setOnAction(event -> SceneController.getInstance().switchTo(Roots.LEADERBOARD));

    Pane pane = new Pane();

    // Load the image from the enum
    Image image = MainMenuSprites.BG.getImage();

    // Create a BackgroundSize object
    BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);

    // Create a BackgroundImage object
    BackgroundImage backgroundImage =
        new BackgroundImage(
            image,
            BackgroundRepeat.REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            backgroundSize);

    // Create a Background object
    Background background = new Background(backgroundImage);

    // Set the background of the Pane
    pane.setBackground(background);

    // Create CustomFontButton and set it as the graphic of the Button
    exit.setGraphic(new CustomFontButton("QUIT"));
    play.setGraphic(new CustomFontButton("PLAY"));
    leaderboard.setGraphic(new CustomFontButton("LEADERBOARD"));

    // Set the style of the Button to be transparent
    exit.setStyle("-fx-background-color: transparent;");
    play.setStyle("-fx-background-color: transparent;");
    leaderboard.setStyle("-fx-background-color: transparent;");
  }
}
