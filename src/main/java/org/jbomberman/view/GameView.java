package org.jbomberman.view;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.util.Duration;
import org.jbomberman.model.entita.Type;
import org.jbomberman.model.listener.*;

public class GameView {

  private TilePane tilePane;
  private AnchorPane anchorPane;
  private ImageView player;
  private Map<Type, Image> mobSprites;

  public GameView() {
    player = new ImageView(Entities.PLAYER.getImage());
    player.setFitHeight(96);
    player.setFitWidth(48);

    tilePane = new TilePane();

    tilePane.setPrefRows(17);
    tilePane.setPrefColumns(13);

    for (int i = 1; i < 221; i++) {
      ImageView imageView = new ImageView();
      imageView.setFitHeight(48);
      imageView.setFitWidth(48);
      imageView.setSmooth(false);
      tilePane.getChildren().add(imageView);
    }
    anchorPane = new AnchorPane(tilePane, player);

    mobSprites = new HashMap<>();
    mobSprites.put(Type.PUROPEN, Entities.PUROPEN.getImage());
    mobSprites.put(Type.DENKYUN, Entities.DENKYUN.getImage());
  }

  public void loadMap(LoadMapData data) {
    int i = 0;
    for (Tiles tiles : data.matrix()) {
      ((ImageView) tilePane.getChildren().get(i++)).setImage(tiles.getImage());
    }
  }

  public Parent getRoot() {
    return anchorPane;
  }

  public void spawnPlayer(PlayerInitialPositionData playerInitialPosition) {

    player.setX(playerInitialPosition.initialX());
    player.setY(playerInitialPosition.initialY());
  }

  public void movePlayer(PlayerMovementData data) {
    int xStep = data.xStep();
    int yStep = data.yStep();
    double delta = data.delta();

    int initialX = data.oldX();
    int initialY = data.oldY();

    TranslateTransition transition = new TranslateTransition(Duration.seconds(delta), player);

    // Set the initial positions as the fromX and fromY properties
    transition.setFromX(initialX);
    transition.setFromY(initialY);

    // Set the target positions using setToX and setToY
    transition.setToX(xStep);
    transition.setToY(yStep);

    transition.play();
  }

  public void spawnBomb(BombSpawnData data) {
    int bombX = data.bombX();
    int bombY = data.bombY();

    ImageView bombImageView = new ImageView(Entities.BOMB.getImage());

    bombImageView.setFitWidth(48);
    bombImageView.setFitHeight(48);

    bombImageView.setX(bombX);
    bombImageView.setY(bombY);

    // Add the bomb image view to the anchor pane
    anchorPane.getChildren().add(bombImageView);
  }

  public void explodeBomb(BombExplosionData data) {
    int explosionX = data.explosionX();
    int explosionY = data.explosionY();
    int[] ranges = data.ranges();

    // Draw the initial explosion sprite at the bomb's position
    drawExplosionSprite(explosionX, explosionY);

    // Remove the bomb image
    removeBombImage(explosionX, explosionY);

    // Draw the explosions based on the ranges array
    drawExplosionSpritesInDirection(explosionX, explosionY, ranges[0], 1, 0); // Right
    drawExplosionSpritesInDirection(explosionX, explosionY, ranges[1], -1, 0); // Left
    drawExplosionSpritesInDirection(explosionX, explosionY, ranges[2], 0, -1); // Up
    drawExplosionSpritesInDirection(explosionX, explosionY, ranges[3], 0, 1); // Down

    // Schedule a task to remove the explosion images after 2 seconds
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    scheduler.schedule(
        () ->
            Platform.runLater(
                () ->
                    anchorPane
                        .getChildren()
                        .removeIf(
                            node ->
                                node instanceof ImageView
                                    && ((ImageView) node).getImage()
                                        == Entities.EXPLOSION.getImage())),
        2,
        TimeUnit.SECONDS);
  }

  private void drawExplosionSpritesInDirection(
      int startX, int startY, int range, int deltaX, int deltaY) {
    for (int i = 1; i <= range; i++) {
      int x = startX + i * deltaX * 48;
      int y = startY + i * deltaY * 48;

      drawExplosionSprite(x, y);
    }
  }

  private void drawExplosionSprite(int x, int y) {
    ImageView explosionImageView = new ImageView(Entities.EXPLOSION.getImage());
    explosionImageView.setFitWidth(48);
    explosionImageView.setFitHeight(48);
    explosionImageView.setX(x);
    explosionImageView.setY(y);

    anchorPane.getChildren().add(explosionImageView);
  }

  private void removeBombImage(int explosionX, int explosionY) {
    for (Node node : anchorPane.getChildren()) {
      if (node instanceof ImageView
          && ((ImageView) node).getImage() == Entities.BOMB.getImage()
          && (Math.abs(((ImageView) node).getX() - explosionX) < 1
              && Math.abs(((ImageView) node).getY() - explosionY) < 1)) {
        anchorPane.getChildren().remove(node);
        break;
      }
    }
  }

  public void handleTileDestruction(TileDestructionData data) {
    int tile_index = data.tileIndex();

    if (tile_index >= 0 && tile_index < tilePane.getChildren().size()) {
      // Replace the destroyed tile with a GRASS tile
      ImageView imageView = new ImageView(Tiles.GRASS.getImage());
      imageView.setFitHeight(48);
      imageView.setFitWidth(48);
      imageView.setSmooth(false);

      tilePane.getChildren().set(data.tileIndex(), imageView);
    }
  }

  public void spawnMob(MobInitialPositionData mobInitialPositions) {
    Type mobType = mobInitialPositions.mobType();
    Image mobImage = mobSprites.get(mobType);

    ImageView mobImageView = new ImageView(mobImage);
    mobImageView.setFitWidth(32); // Set the initial fit width
    mobImageView.setFitHeight(32); // Set the initial fit height
    mobImageView.setX(mobInitialPositions.initialX());
    mobImageView.setY(mobInitialPositions.initialY());

    // Adjust the position of the mobImageView to (8, 16)
    mobImageView.setX(8);
    mobImageView.setY(16);

    anchorPane.getChildren().add(mobImageView);
  }

  public void moveMob(MobMovementData data) {
    int xStep = data.xStep();
    int yStep = data.yStep();
    double delta = data.delta();

    int initialX = data.oldX();
    int initialY = data.oldY();

    Type mobType = data.mobType();
    Image mobImage = mobSprites.get(mobType);

    ImageView mobImageView = new ImageView(mobImage);
    mobImageView.setFitWidth(32); // Set the initial fit width
    mobImageView.setFitHeight(32); // Set the initial fit height

    TranslateTransition transition = new TranslateTransition(Duration.seconds(delta), mobImageView);

    // Set the initial positions as the fromX and fromY properties
    transition.setFromX(initialX);
    transition.setFromY(initialY);

    // Set the target positions using setToX and setToY
    transition.setToX(xStep);
    transition.setToY(yStep);

    // Adjust the position of the mobImageView to (8, 16)
    mobImageView.setX(8);
    mobImageView.setY(8);

    // Remove the old sprite of the mob, if exists
    removeOldMobSprite(mobType);

    anchorPane.getChildren().add(mobImageView);

    transition.play();
  }

  public void removeMob(RemoveMobData data) {
    Type mobType = data.mobType();

    // Remove the sprite of the mob from the screen
    removeOldMobSprite(mobType);
  }

  private void removeOldMobSprite(Type mobType) {
    // Remove the old sprite of the mob
    anchorPane
        .getChildren()
        .removeIf(
            node ->
                node instanceof ImageView
                    && ((ImageView) node).getImage() == mobSprites.get(mobType));
  }

  public void updatePlayerLives(PlayerLivesUpdateData data) {
    int lives = data.lives();
    // Update the display to reflect the new player lives
    // You can implement this based on your game's UI

    if (lives == 0) {
      removePlayer();
    }
  }

  public void removePlayer() {
    anchorPane.getChildren().remove(player);
  }
}
