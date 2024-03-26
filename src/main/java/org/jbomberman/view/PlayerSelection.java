package org.jbomberman.view;

import java.io.FileWriter;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.jbomberman.model.map.Map;

public class PlayerSelection {

  @FXML private TextField playerName;

  @FXML private Button submit;

  private static PlayerSelection instance;

  private String currentName = ""; // Set a default value

  public static PlayerSelection getInstance() {
    if (instance == null) {
      instance = new PlayerSelection();
    }
    return instance;
  }

  public String getPlayerName() {
    return currentName; // Change this line
  }

  @FXML
  public void initialize() {
    submit.setOnAction(
        event -> {
          String name = playerName.getText();
          if (!name.isEmpty()) {
            currentName = name; // Only set currentName if name is not empty
            try (FileWriter writer =
                new FileWriter("src/main/resources/playerData/playerData.csv", true)) {
              writer.write(name + ","); // Write the player's name and a comma
              writer.flush();
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
          // Load level 1 and initialize the scene
          Map.getInstance().loadLevel("1");
          SceneController.getInstance().initialize();
        });
  }
}
