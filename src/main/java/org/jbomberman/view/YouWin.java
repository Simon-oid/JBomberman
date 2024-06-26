package org.jbomberman.view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class YouWin implements Initializable {

  /** The FXML quit button element */
  @FXML public Button quit;

  /** The FXML scrolling background pane element */
  @FXML private Pane scrollingBackgroundPane;

  /**
   * Initialize the you win screen
   *
   * @param location The location
   * @param resources The resources
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    CustomFontButton quitButton = new CustomFontButton("QUIT");
    quitButton.setLayoutX(quit.getLayoutX()); // Set the X position of the button
    quitButton.setLayoutY(quit.getLayoutY()); // Set the Y position of the button

    quit.setGraphic(quitButton);
    quit.setOnAction(event -> SceneController.getInstance().exit());

    ScrollingBackground scrollingBackground = new ScrollingBackground();
    scrollingBackgroundPane.getChildren().add(scrollingBackground);
  }
}
