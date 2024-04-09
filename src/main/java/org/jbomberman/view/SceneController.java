package org.jbomberman.view;

import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Observable;
import java.util.Observer;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;
import org.jbomberman.controller.KeyHandler;
import org.jbomberman.model.listener.*;

@Getter
@Setter
public class SceneController implements Observer {

  /** The instance of the SceneController */
  private static SceneController instance;

  /** The window of the SceneController */
  private Stage window;

  /** The roots of the SceneController */
  private final Parent[] roots;

  /** The game root of the SceneController */
  private GameView gameRoot;

  /** Initialize the SceneController */
  public void initialize() {

    KeyHandler keyHandler = KeyHandler.getInstance();

    if (gameRoot == null) gameRoot = new GameView();

    window.getScene().setRoot(gameRoot.getRoot());

    window.getScene().setOnKeyPressed(keyHandler::onkeyPressed);
    window.getScene().setOnKeyReleased(keyHandler::onkeyReleased);

    adjustWindowSize();

    window.getScene().getRoot().setStyle("-fx-background-color: black;");

    window.setOnCloseRequest(
        event -> {
          // Prevent the window from closing
          event.consume();

          // Call the exitConfirm method
          exitConfirm();
        });
  }

  /**
   * Sence controller constructor
   *
   * <p>Initialize the roots
   */
  private SceneController() {
    roots = new Parent[Roots.values().length];
  }

  /**
   * Get the instance of the SceneController
   *
   * @return The instance
   */
  public static SceneController getInstance() {
    if (instance == null) instance = new SceneController();
    return instance;
  }

  /**
   * Switch to the root
   *
   * @param root The root
   */
  public void switchTo(Roots root) {
    if (roots[root.ordinal()] == null) {
      load(root);
    }
    window.getScene().setRoot(roots[root.ordinal()]);
    adjustWindowSize();

    // Play the mainTheme when the MENU root is selected
    if (root == Roots.MENU) {
      AudioManager.getInstance().stopAll();

      AudioManager.getInstance().play(AudioSample.MAIN_MENU);
    } else if (root == Roots.PLAYER_SELECTION) {
      AudioManager.getInstance().stopAll();

      AudioManager.getInstance().play(AudioSample.PLAYER_SELECTION);
    } else if (root == Roots.LEADERBOARD) {
      AudioManager.getInstance().stopAll();

      AudioManager.getInstance().play(AudioSample.LEADERBOARD);
    }
  }

  /**
   * Adjust the window size
   *
   * <p>Set the width and height of the window
   */
  private void adjustWindowSize() {
    int tileWidth = 48;
    int tileHeight = 48;
    int hudHeight = 139; // Height of the HUD

    int mapWidth = 15;
    int mapHeight = 13;

    int windowWidth = (mapWidth * tileWidth) + 16; // 15 columns * tile width + padding
    int windowHeight = (mapHeight * tileHeight) + hudHeight; // 13 rows * tile height + HUD height

    // Set the width and height of the window
    window.setWidth(windowWidth);
    window.setHeight(windowHeight);
  }

  /** Closes the window */
  public void exitConfirm() {

    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("exit?");
    alert.setHeaderText("stai per uscire dal gioco!");
    alert.setContentText("i progressi non salvati, non verranno salvati: ");

    if (alert.showAndWait().get() == ButtonType.OK) {
      revertUsernameChanges();
      window.close();
      System.exit(0);
    }
  }

  /** Exits the window */
  public void exit() {
    window.close();
    System.exit(0);
  }

  /**
   * Revert the username changes
   *
   * <p>Revert the changes made to the player data
   */
  private void revertUsernameChanges() {
    try {
      RandomAccessFile file =
          new RandomAccessFile("src/main/resources/playerData/playerData.csv", "rw");
      long length = file.length() - 1;
      byte b;
      do {
        length -= 1;
        file.seek(length);
        b = file.readByte();
      } while (b != 10 && length > 0);
      file.setLength(length + 1);
      file.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Load the root
   *
   * @param root The root
   */
  private void load(Roots root) {
    try {
      roots[root.ordinal()] = FXMLLoader.load(getClass().getResource(root.getResourcePath()));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Switch to the GameOver scene
   *
   * @param data The GameOverUpdateData
   */
  private void switchToGameOverScene(GameOverUpdateData data) {
    int playerScore = data.score();

    AudioManager.getInstance().stopAll();

    // Load the GameOver scene and get the controller
    FXMLLoader loader = new FXMLLoader(getClass().getResource(Roots.GAME_OVER.getResourcePath()));
    try {
      roots[Roots.GAME_OVER.ordinal()] = loader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    GameOver gameOverScene = loader.getController();

    // Pass the score to the GameOver scene
    gameOverScene.setPlayerScore(playerScore);

    try (FileWriter writer = new FileWriter("src/main/resources/playerData/playerData.csv", true)) {
      writer.write(Integer.toString(playerScore) + "\n"); // Write the player's score and a newline
      writer.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }

    PauseTransition delay = new PauseTransition(Duration.seconds(3.5));
    delay.setOnFinished(
        event -> {
          gameRoot.resetView();

          gameRoot.clearHUD();

          gameRoot.setCurrentLevel(0);

          AudioManager.getInstance().stop(AudioSample.SOUNDTRACK);

          switchTo(Roots.GAME_OVER);

          AudioManager.getInstance().play(AudioSample.GAME_OVER);
        });
    delay.play();
  }

  /** Switch to the YouWin scene */
  private void switchToYouWonScene() {

    AudioManager.getInstance().stopAll();

    PauseTransition delay = new PauseTransition(Duration.seconds(3.5));
    delay.setOnFinished(
        event -> {
          gameRoot.resetView();

          gameRoot.clearHUD();

          switchTo(Roots.YOU_WIN);

          int playerScore = gameRoot.getPlayerScore();

          try (FileWriter writer =
              new FileWriter("src/main/resources/playerData/playerData.csv", true)) {
            writer.write(
                Integer.toString(playerScore) + "\n"); // Write the player's score and a newline
            writer.flush();
          } catch (IOException e) {
            e.printStackTrace();
          }

          AudioManager.getInstance().play(AudioSample.AUDIENCE);
          AudioManager.getInstance().play(AudioSample.ENDING);
        });
    delay.play();
  }

  /**
   * Update the observer
   *
   * @param o the observable object.
   * @param arg an argument passed to the {@code notifyObservers} method.
   */
  @Override
  public void update(Observable o, Object arg) {
    if (arg instanceof PackageData data) {
      switch (data.type()) {
        case LOADMAP ->
            Platform.runLater(
                () -> {
                  gameRoot.resetGameView();
                  gameRoot.loadMap((LoadMapData) data);
                });
        case MOVE_PLAYER -> Platform.runLater(() -> gameRoot.movePlayer((PlayerMovementData) data));
        case SPAWN_BOMB -> Platform.runLater(() -> gameRoot.spawnBomb((BombSpawnData) data));
        case BOMB_EXPLOSION ->
            Platform.runLater(() -> gameRoot.explodeBomb((BombExplosionData) data));
        case TILE_DESTRUCTION ->
            Platform.runLater(() -> gameRoot.handleTileDestruction((TileDestructionData) data));
        case MOB_MOVEMENT -> Platform.runLater(() -> gameRoot.moveMob((MobMovementData) data));
        case REMOVE_MOB -> Platform.runLater(() -> gameRoot.removeMob((RemoveMobData) data));
        case PLAYER_LIVES_UPDATE ->
            Platform.runLater(() -> gameRoot.updatePlayerLives((PlayerLivesUpdateData) data));
        case MOB_INITIAL_POSITION ->
            Platform.runLater(() -> gameRoot.spawnMob((MobInitialPositionData) data));
        case PLAYER_INITIAL_POSITION ->
            Platform.runLater(() -> gameRoot.spawnPlayer((PlayerInitialPositionData) data));
        case SPAWN_POWERUP ->
            Platform.runLater(() -> gameRoot.spawnPowerUp((PowerUpSpawnData) data));
        case POWERUP_APPLICATION ->
            Platform.runLater(() -> gameRoot.applyPowerUp((PowerUpApplicationData) data));
        case POWERUP_DESPAWN ->
            Platform.runLater(() -> gameRoot.despawnPowerUp((PowerUpDespawnData) data));
        case PLAYER_SCORE_UPDATE ->
            Platform.runLater(() -> gameRoot.updateScore((PlayerScoreUpdateData) data));
        case DRAW_PLAYER_LIVES_UPDATE ->
            Platform.runLater(() -> gameRoot.drawPlayerLives((PlayerLivesUpdateData) data));
        case SPAWN_EXIT_TILE ->
            Platform.runLater(() -> gameRoot.handleExitSpawn((ExitTileSpawnData) data));
        case GAME_OVER ->
            Platform.runLater(
                () -> {
                  switchToGameOverScene((GameOverUpdateData) data);
                });
        case DENKYUN_RESPAWN ->
            Platform.runLater(() -> gameRoot.spawnDenkyunAtCoordinates((DenkyunRespawnData) data));
        case LEVEL_UPDATE -> Platform.runLater(() -> gameRoot.levelClear((LevelUpdateData) data));
        case YOU_WIN -> Platform.runLater(this::switchToYouWonScene);
      }
    }
  }
}
