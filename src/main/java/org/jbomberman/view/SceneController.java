package org.jbomberman.view;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.jbomberman.controller.KeyHandler;
import org.jbomberman.model.listener.*;

@Getter
@Setter
public class SceneController implements Observer
{

    private static SceneController instance;

    private Stage window;

    private final Parent[] roots;

    private GameView gameRoot;


    public void initialize()
    {
        KeyHandler keyHandler = KeyHandler.getInstance();

        if (gameRoot == null) gameRoot = new GameView();
        window.getScene().setRoot(gameRoot.getRoot());

        window.getScene().setOnKeyPressed(keyHandler::onkeyPressed);
        window.getScene().setOnKeyReleased(keyHandler::onkeyReleased);
    }

    private SceneController()
    {
        roots = new Parent[Roots.values().length];
    }

    public static SceneController getInstance()
    {
        if (instance == null) instance = new SceneController();
        return instance;
    }


    public void switchTo(Roots root)
    {
        if (roots[root.ordinal()] == null)
        {
            load(root);
        }
        window.getScene().setRoot(roots[root.ordinal()]);
    }

    public void exit()
    {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("exit?");
        alert.setHeaderText("stai per uscire dal gioco!");
        alert.setContentText("i progressi non salvati, nun so salvati: ");

        if (alert.showAndWait().get() == ButtonType.OK)
        {
            System.out.println("daje sei uscito!");
            window.close();
            System.exit(0);
        }

    }

    private void load(Roots root)
    {
        try
        {
            roots[root.ordinal()] = FXMLLoader.load(getClass().getResource(root.getResourcePath()));
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void update(Observable o, Object arg)
    {
        if (arg instanceof PackageData data)
        {
            switch (data.type())
            {
                case LOADMAP -> Platform.runLater(() -> gameRoot.loadMap((LoadMapData) data));
                case MOVE_PLAYER -> Platform.runLater(() -> gameRoot.movePlayer((PlayerMovementData) data));
                case SPAWN_BOMB -> Platform.runLater(() -> gameRoot.spawnBomb((BombSpawnData) data));
                case BOMB_EXPLOSION -> Platform.runLater(() -> gameRoot.explodeBomb((BombExplosionData) data));
                case TILE_DESTRUCTION ->
                        Platform.runLater(() -> gameRoot.handleTileDestruction((TileDestructionData) data));
                case MOB_MOVEMENT -> Platform.runLater(() -> gameRoot.moveMob((MobMovementData) data));
            }
        }
    }

}
