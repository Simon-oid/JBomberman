package org.jbomberman.view;

import java.io.FileWriter;
import java.io.IOException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import org.jbomberman.model.map.Map;

public class PlayerSelection {

  /** The FXML pane scrolling background */
  @FXML private Pane scrollingBackgroundPane;

  /** The FXML exit button element */
  @FXML private Button exit;

  /** The FXML insert name sign element */
  @FXML private HBox insertNameSign;

  @FXML private TextField playerName;
  private HBox playerNameSign;
  @FXML private Button submit;

  /** Initialize the player selection */
  public void initialize() {

    // Create an instance of ScrollingBackground and add it to the scrollingBackgroundPane
    ScrollingBackground scrollingBackground = new ScrollingBackground();
    scrollingBackgroundPane.getChildren().add(scrollingBackground);

    // Add the ImageView to the scrollingBackgroundPane
    ImageView gameOverFrame = new ImageView(MainMenuSprites.FRAME.getImage());
    gameOverFrame.setFitHeight(589.0);
    gameOverFrame.setFitWidth(616.0);
    gameOverFrame.setLayoutX(50.0);
    gameOverFrame.setLayoutY(80.0);
    scrollingBackgroundPane.getChildren().add(gameOverFrame);

    // Set the exit button to be transparent and to have the "Menu" sign as graphics
    CustomFontButton menuSign = new CustomFontButton("Menu");
    exit.setGraphic(menuSign);
    exit.setStyle("-fx-background-color: transparent;");

    exit.setOnAction(event -> SceneController.getInstance().switchTo(Roots.MENU));

    playerName.getStyleClass().add("custom-text-field");

    // Initialize the playerNameSign HBox and add it to the scene
    playerNameSign = new HBox();
    scrollingBackgroundPane.getChildren().add(playerNameSign);

    // Set the position of the playerNameSign HBox
    playerNameSign.setLayoutX(insertNameSign.getLayoutX());
    playerNameSign.setLayoutY(
        insertNameSign.getLayoutY() + insertNameSign.getHeight() + 70); // 10 is the padding

    playerNameSign.toFront();

    // Add a listener to the playerName TextField
    playerName
        .textProperty()
        .addListener(
            new ChangeListener<String>() {
              @Override
              public void changed(
                  ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // Clear the current sprites in the playerNameSign HBox
                playerNameSign.getChildren().clear();

                // Draw the new sprites in the playerNameSign HBox
                drawPlayerNameSign(newValue);
              }
            });

    // Draw the "INSERT NAME" sign
    drawInsertNameSign();

    // Set the submit button to be transparent and to have the "Submit" sign as graphics
    CustomFontButton submitSign = new CustomFontButton("Submit");
    submit.setGraphic(submitSign);
    submit.setStyle("-fx-background-color: transparent;");

    submit.setOnAction(event -> handleSubmit());
  }

  /** Draw the "INSERT NAME" sign */
  private void drawInsertNameSign() {
    String text = "INSERT NAME:";
    drawSign(insertNameSign, text, false);
  }

  /**
   * Draw the player name sign
   *
   * @param text The text to draw
   */
  private void drawPlayerNameSign(String text) {
    drawSign(playerNameSign, text, true);
  }

  /**
   * Draw the sign
   *
   * @param sign The sign
   * @param text The text
   * @param recenter Whether to recenter the sign
   */
  private void drawSign(HBox sign, String text, boolean recenter) {
    for (char c : text.toCharArray()) {
      if (c == ' ') {
        Pane spacer = new Pane();
        spacer.setMinWidth(15); // Adjust the width to control the spacing
        sign.getChildren().add(spacer);
        continue;
      }
      ImageView sprite;
      if (c == ':') {
        sprite = LettersFont.SPRITE_COLON.getImageView();
      } else {
        sprite = LettersFont.valueOf("SPRITE_" + Character.toUpperCase(c)).getImageView();
      }
      ImageView copy = new ImageView(sprite.getImage());
      copy.setFitWidth(18); // Set the width of the ImageView
      copy.setFitHeight(24); // Set the height of the ImageView
      sign.getChildren().add(copy);

      // Recenter the sign after each letter is drawn, if recenter is true
      if (recenter) {
        double totalWidth =
            sign.getChildren().stream()
                .filter(node -> node instanceof ImageView)
                .mapToDouble(node -> ((ImageView) node).getFitWidth())
                .sum();
        sign.setLayoutX((scrollingBackgroundPane.getWidth() - totalWidth - 20) / 2);
      }
    }
  }

  /** Handle the submit button */
  private void handleSubmit() {
    String playerNameText = playerName.getText();
    if (playerNameText.isEmpty()) {
      // Handle the case where the player name is empty
      System.out.println("Player name cannot be empty");
    } else {
      // Handle the case where the player name is not empty
      System.out.println("Player name is: " + playerNameText);

      // Handle the case where the player name is not empty
      System.out.println("Player name is: " + playerNameText);
      // Save the player's name to a file
      try (FileWriter writer =
          new FileWriter("src/main/resources/playerData/playerData.csv", true)) {
        writer.write(playerNameText + ",");
      } catch (IOException e) {
        e.printStackTrace();
      }

      // Load level one and initialize the scene
      AudioManager.getInstance().stopAll();
      Map.getInstance().loadLevel("1");
      SceneController.getInstance().initialize();
    }
  }
}
