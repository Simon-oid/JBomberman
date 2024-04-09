package org.jbomberman.view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.jbomberman.model.map.Map;

/**
 * Defines: LevelSelection ,
 *
 * @author ForzaElettromotrice
 */
public class LevelSelection implements Initializable {
  /** The FXML level 1 button element */
  @FXML public Button livello1;

    /**
     * Initialize the level selection
     *
     * @param location The location
     * @param resources The resources
     */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    livello1.setOnAction(
        event -> {
          Map.getInstance().loadLevel("1");
          SceneController.getInstance().initialize();
        });
  }
}
