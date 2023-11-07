package org.jbomberman.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.jbomberman.model.map.Map;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Defines: LevelSelection ,
 *
 * @author ForzaElettromotrice
 */
public class LevelSelection implements Initializable
{
    @FXML
    public Button livello1;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        livello1.setOnAction(event ->
        {
            Map.getInstance().loadLevel("1");
            SceneController.getInstance().initialize();
        });
    }
}
