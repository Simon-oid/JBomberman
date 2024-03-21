package org.jbomberman.view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class YouWin implements Initializable {

  @FXML public Button menu;

  @FXML public Button quit;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    quit.setOnAction(event -> SceneController.getInstance().exit());
    menu.setOnAction(event -> SceneController.getInstance().switchTo(Roots.MENU));
  }
}
