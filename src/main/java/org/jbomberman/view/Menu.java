package org.jbomberman.view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class Menu implements Initializable {
  @FXML public Button exit;

  @FXML public Button play;

  @FXML public Button leaderboard;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    exit.setOnAction(event -> SceneController.getInstance().exit());
    play.setOnAction(event -> SceneController.getInstance().switchTo(Roots.PLAYER_SELECTION));
    leaderboard.setOnAction(event -> SceneController.getInstance().switchTo(Roots.LEADERBOARD));
  }
}
