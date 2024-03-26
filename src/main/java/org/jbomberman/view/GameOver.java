package org.jbomberman.view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class GameOver implements Initializable {

  @FXML public Button continueButton;
  @FXML public Button yes;
  @FXML public Button no;

  @FXML private HBox scoreHBox;

  @FXML private Pane scrollingBackgroundPane;
  private int playerScore;

  public void setPlayerScore(int playerScore) {
    this.playerScore = playerScore;

    // Clear the previous score
    scoreHBox.getChildren().clear();

    // Add padding to the HBox to move the score to the right
    scoreHBox.setPadding(new Insets(0, 0, 0, 80)); // Adjust the fourth parameter as needed

    // Add spacing between the children of the HBox
    scoreHBox.setSpacing(10); // Adjust as needed

    // Convert the player's score to a string and add the ImageView for each digit to the HBox
    String scoreStr = String.valueOf(playerScore);
    for (char digit : scoreStr.toCharArray()) {
      ImageView digitSprite = getDigitSprite(Character.getNumericValue(digit));
      scoreHBox.getChildren().add(digitSprite);
    }
  }

  private ImageView getDigitSprite(int digit) {
    // Get the sprite for the digit
    Image image = GameOverScoreFont.valueOf("SPRITE_" + digit).getImageView().getImage();
    ImageView imageView = new ImageView(image);
    imageView.setFitWidth(50); // Set the width of the ImageView
    imageView.setFitHeight(50); // Set the height of the ImageView
    return imageView;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    ScrollingBackground scrollingBackground = new ScrollingBackground();
    scrollingBackgroundPane.getChildren().add(scrollingBackground);

    GameOverFontButton continueFontButton = new GameOverFontButton("CONTINUE?");
    continueButton.setGraphic(continueFontButton.getGraphic());

    GameOverFontButton yesFontButton = new GameOverFontButton("YES");
    yes.setGraphic(yesFontButton.getGraphic());

    GameOverFontButton noFontButton = new GameOverFontButton("NO");
    no.setGraphic(noFontButton.getGraphic());

    yes.setOnAction(
        event -> {
          SceneController.getInstance().switchTo(Roots.PLAYER_SELECTION);
        });

    no.setOnAction(event -> SceneController.getInstance().switchTo(Roots.MENU));

    //    // Convert the player's score to a string and add the ImageView for each digit to the HBox
    //    String scoreStr = String.valueOf(playerScore);
    //    for (char digit : scoreStr.toCharArray()) {
    //      ImageView digitSprite = getDigitSprite(Character.getNumericValue(digit));
    //      scoreHBox.getChildren().add(digitSprite);
    //    }
  }
}
