package org.jbomberman;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.jbomberman.model.map.Map;
import org.jbomberman.view.SceneController;

import java.io.IOException;


public class Main extends Application
{

    @Override
    public void start(Stage stage) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/scenes/Menu.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);

        SceneController.getInstance().setWindow(stage);

        stage.setTitle("JBomberman");
        Image icon = new Image(getClass().getResource("view/icon/Bomberman-icon.png").toExternalForm());
        stage.getIcons().add(icon);

        stage.setScene(scene);

        stage.setResizable(false);

        stage.setWidth((48.0 * 13) + 16);
        stage.setHeight((48.0 * 11) + 39);

        stage.setOnCloseRequest(event ->
        {
            event.consume();
            SceneController.getInstance().exit();
        });

        Map.getInstance().addObserver(SceneController.getInstance());

        stage.show();


    }


    public static void main(String[] args)
    {
        launch(args);
    }
}
