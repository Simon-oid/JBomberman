package org.jbomberman.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class Menu implements Initializable
{
    @FXML
    public Button exit;

    @FXML
    public Button play;

    @FXML
    public Button settings;

    @FXML
    public Button chiLoSa;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        exit.setOnAction(event -> SceneController.getInstance().exit());
        play.setOnAction(event -> SceneController.getInstance().switchTo(Roots.LEVEL_SELECTION));
        settings.setOnAction(event -> SceneController.getInstance().switchTo(Roots.SETTINGS));
        chiLoSa.setOnAction(event -> SceneController.getInstance().switchTo(Roots.CHI_LO_SA));
    }
}