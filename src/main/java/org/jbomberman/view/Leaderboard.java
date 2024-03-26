package org.jbomberman.view;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class Leaderboard {

  @FXML private VBox scoresBox;
  @FXML private Pane scrollingBackgroundPane;
  @FXML private Button exit;

  public void initialize() {

    // Create an instance of ScrollingBackground and add it to the scrollingBackgroundPane
    ScrollingBackground scrollingBackground = new ScrollingBackground();
    scrollingBackgroundPane.getChildren().add(scrollingBackground);

    List<String> playerData = getPlayerData();
    for (String data : playerData) {
      String[] splitData = data.split(",");
      String playerName = splitData[0];
      String playerScore = splitData[1];
      drawPlayerData(playerName, playerScore);
    }
    // Center the sprites and add some space between them
    scoresBox.setAlignment(Pos.CENTER);
    scoresBox.setSpacing(10);

    scoresBox.setPadding(new Insets(0, 0, 0, 250));

    // Set the exit button to be transparent and to have the "Menu" sign as graphics
    CustomFontButton menuSign = new CustomFontButton("Menu");
    exit.setGraphic(menuSign);
    exit.setStyle("-fx-background-color: transparent;");

    exit.setOnAction(event -> SceneController.getInstance().switchTo(Roots.MENU));
  }

  private List<String> getPlayerData() {
    List<String> playerData = new ArrayList<>();
    try (BufferedReader br =
        new BufferedReader(new FileReader("src/main/resources/playerData/playerData.csv"))) {
      String line;
      while ((line = br.readLine()) != null) {
        playerData.add(line);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Sort the player data from highest to lowest score
    playerData.sort(
        Comparator.comparing(
            (String line) -> {
              String[] splitData = line.split(",");
              return -Integer.parseInt(splitData[1]); // Use negative to sort in descending order
            }));

    // Limit the player data to the top 10 scores
    playerData = playerData.stream().limit(10).collect(Collectors.toList());

    return playerData;
  }

  private void drawPlayerData(String playerName, String playerScore) {
    String playerData = playerName + ":" + playerScore;
    HBox playerScoreBox = new HBox();
    for (char c : playerData.toCharArray()) {
      ImageView sprite = getSprite(c);
      playerScoreBox.getChildren().add(sprite);
    }
    scoresBox.getChildren().add(playerScoreBox);
  }

  private ImageView getSprite(char c) {
    ImageView original;
    if (c == ':') {
      original = LettersFont.SPRITE_COLON.getImageView();
    } else if (Character.isDigit(c)) {
      original = LettersFont.valueOf("SPRITE_" + c).getImageView();
    } else {
      original = LettersFont.valueOf("SPRITE_" + Character.toUpperCase(c)).getImageView();
    }
    ImageView copy = new ImageView(original.getImage());
    copy.setFitWidth(original.getFitWidth());
    copy.setFitHeight(original.getFitHeight());
    return copy;
  }
}
