package org.jbomberman;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.jbomberman.model.map.Map;
import org.jbomberman.view.AudioManager;
import org.jbomberman.view.AudioSample;
import org.jbomberman.view.SceneController;

public class Main extends Application {

  @Override
  public void start(Stage stage) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("view/scenes/Menu.fxml"));

    Parent root = loader.load();
    AudioManager.getInstance().play(AudioSample.MAIN_MENU);

    Scene scene = new Scene(root);

    scene
        .getStylesheets()
        .add(getClass().getResource("view/scenesAssets/styles.css").toExternalForm());

    SceneController.getInstance().setWindow(stage);

    stage.setTitle("JBomberman");
    Image icon = new Image(getClass().getResource("view/icon/Bomberman-icon.png").toExternalForm());
    stage.getIcons().add(icon);

    stage.setScene(scene);

    stage.setResizable(false);

    stage.setWidth(736);
    stage.setHeight(763);

    stage.setOnCloseRequest(
        event -> {
          event.consume();
          SceneController.getInstance().exit();
        });

    Map.getInstance().addObserver(SceneController.getInstance());

    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
