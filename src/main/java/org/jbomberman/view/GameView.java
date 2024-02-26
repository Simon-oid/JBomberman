package org.jbomberman.view;

import java.util.Arrays;
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
import org.jbomberman.model.powerups.PowerUpType;

public class GameView {

  private TilePane tilePane;
  private AnchorPane anchorPane;
  private ImageView player;
  private Map<Type, Image> mobSprites;
  private ImageView scoreboard;

  private ImageView[] fontSprites;

  private static final int Y_OFFSET = 100;

  public GameView() {
    player = new ImageView(Entities.PLAYER.getImage());
    player.setFitHeight(96);
    player.setFitWidth(48);

    tilePane = new TilePane();

    int MAP_WIDTH = 15;
    int MAP_HEIGHT = 13;

    tilePane.setPrefRows(MAP_HEIGHT);
    tilePane.setPrefColumns(MAP_WIDTH);

    for (int i = 1; i <= MAP_WIDTH * MAP_HEIGHT; i++) {
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

    scoreboard = new ImageView(HUD.HUD_SPRITE.getImage()); // Load custom scoreboard sprite
    scoreboard.setFitWidth(720);
    scoreboard.setFitHeight(100);

    // Add player and scoreboard to anchorPane
    anchorPane = new AnchorPane(tilePane, player, scoreboard);

    // Load font sprites
    loadFontSprites();

    // Positioning elements within the AnchorPane using layout constraints
    AnchorPane.setTopAnchor(tilePane, (double) Y_OFFSET); // Shift tilePane down by 256 pixels
    AnchorPane.setLeftAnchor(tilePane, 0.0); // Position tilePane at the left

    AnchorPane.setTopAnchor(player, (double) Y_OFFSET); // Shift player down by 256 pixels
    AnchorPane.setLeftAnchor(player, 0.0); // Position player at the left

    AnchorPane.setTopAnchor(scoreboard, 0.0); // Position scoreboard at the bottom
    AnchorPane.setLeftAnchor(scoreboard, 0.0); // Position scoreboard at the left
  }

  public void loadMap(LoadMapData data) {
    int i = 0;
    for (Tiles tiles : data.matrix()) {
      ((ImageView) tilePane.getChildren().get(i++)).setImage(tiles.getImage());
    }

    // Force layout update
    tilePane.getParent().layout();
  }

  public Parent getRoot() {

    return anchorPane;
  }

  public void spawnPlayer(PlayerInitialPositionData playerInitialPosition) {

    player.setTranslateX(48);
    player.setTranslateY(-4);
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
    bombImageView.setY(bombY + Y_OFFSET);

    // Add the bomb image view to the anchor pane
    anchorPane.getChildren().add(bombImageView);
  }

  public void explodeBomb(BombExplosionData data) {
    int explosionX = data.explosionX();
    int explosionY = data.explosionY();
    int[] ranges = data.ranges();

    // Draw the initial explosion sprite at the bomb's position
    drawExplosionSprite(explosionX, explosionY + Y_OFFSET);

    // Remove the bomb image
    removeBombImage(explosionX, explosionY + Y_OFFSET);

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

      drawExplosionSprite(x, y + Y_OFFSET);
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
    mobImageView.setFitWidth(48); // Set the initial fit width
    mobImageView.setFitHeight(48); // Set the initial fit height
    mobImageView.setX(mobInitialPositions.initialX());
    mobImageView.setY(mobInitialPositions.initialY());

    // Adjust the position of the mobImageView by adding 256 pixels to the y coordinate
    AnchorPane.setTopAnchor(mobImageView, mobInitialPositions.initialY() + (double) Y_OFFSET);

    //    // Adjust the position of the mobImageView to (8, 16)
    //    mobImageView.setX(8);
    //    mobImageView.setY(16);

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
    mobImageView.setFitWidth(48); // Set the initial fit width
    mobImageView.setFitHeight(48); // Set the initial fit height

    TranslateTransition transition = new TranslateTransition(Duration.seconds(delta), mobImageView);

    // Set the initial positions as the fromX and fromY properties
    transition.setFromX(initialX);
    transition.setFromY(initialY + Y_OFFSET);

    // Set the target positions using setToX and setToY
    transition.setToX(xStep);
    transition.setToY(yStep + Y_OFFSET);

    //    // Adjust the position of the mobImageView to (8, 16)
    //    mobImageView.setX(8);
    //    mobImageView.setY(8);

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

  public void spawnPowerUp(PowerUpSpawnData data) {
    PowerUpType powerUpType = data.powerUpType();
    ImageView powerUpImageView = new ImageView(getPowerUpImage(powerUpType));
    powerUpImageView.setFitWidth(48);
    powerUpImageView.setFitHeight(48);
    powerUpImageView.setX(data.x());
    powerUpImageView.setY(data.y() + Y_OFFSET);
    anchorPane.getChildren().add(powerUpImageView);
  }

  private Image getPowerUpImage(PowerUpType powerUpType) {
    // Add logic to return the appropriate image based on the power-up type
    switch (powerUpType) {
      case BOMB_UP:
        return Tiles.BOMB_UP_POWERUP.getImage();
        // Add cases for other power-up types if needed in the future
      default:
        // Return a default image if the power-up type is not recognized
        return getDefaultPowerUpImage();
    }
  }

  private Image getDefaultPowerUpImage() {
    // Return a default image for power-ups if needed
    return Tiles.EXIT.getImage();
  }

  public void applyPowerUp(PowerUpApplicationData data) {
    int x = data.x();
    int y = data.y();

    removePowerUp(x, y + Y_OFFSET);
  }

  private void removePowerUp(int x, int y) {
    // Iterate over the children of the anchorPane to find the ImageView representing the power-up
    anchorPane
        .getChildren()
        .removeIf(
            node ->
                node instanceof ImageView
                    && ((ImageView) node).getX() == x
                    && ((ImageView) node).getY() == y);
  }

  public void despawnPowerUp(PowerUpDespawnData data) {

    int x = data.x();
    int y = data.y();

    removePowerUp(x, y + Y_OFFSET);
  }

  private void loadFontSprites() {
    fontSprites = new ImageView[10]; // Assuming you have sprites for digits 0-9

    // Load font sprites for digits 0-9
    for (int i = 0; i < 10; i++) {
      fontSprites[i] = new ImageView(Font.values()[i].getImage());
      fontSprites[i].setFitWidth(8 * 2.8); // Adjust width based on sprite size
      fontSprites[i].setFitHeight(14 * 2.8); // Adjust height based on sprite size
    }
  }

  public void updateScore(PlayerScoreUpdateData data) {
    int score = data.score();

    // Calculate the number of digits in the score
    int numDigits = String.valueOf(score).length();

    // Define the starting X position for drawing sprites
    double startX = 291.0; // You may adjust this based on your layout

    // Define the spacing between digits
    double digitSpacing = 10.0 * 2.8; // Adjust this spacing as needed

    // Clear any existing score sprites before drawing new ones
    clearScoreSprites();

    // Calculate the total width of all digits
    double totalWidth = numDigits * digitSpacing;

    // Calculate the starting X position to align the rightmost digit
    double startXAligned = startX + (digitSpacing - totalWidth);

    // Iterate over each digit of the score from right to left
    for (int i = numDigits - 1; i >= 0; i--) {
      int digit = getDigitAt(score, i);
      if (digit >= 0 && digit <= 9) {
        ImageView digitView = new ImageView(fontSprites[digit].getImage());
        digitView.setFitWidth(8 * 2.8);
        digitView.setFitHeight(44);

        // Calculate the X position based on digit index and spacing
        double xPos =
            startXAligned + ((numDigits - 1 - i) * digitSpacing); // Adjust for right alignment
        digitView.setX(xPos);
        digitView.setY(28); // Adjust Y position as needed
        anchorPane.getChildren().add(digitView);
      }
    }
  }

  // Utility method to get the digit at a specific position from right to left
  private int getDigitAt(int number, int position) {
    return (int) (number / Math.pow(10, position)) % 10;
  }

  // Utility method to clear existing score sprites
  private void clearScoreSprites() {
    anchorPane
        .getChildren()
        .removeIf(node -> node instanceof ImageView && Arrays.asList(fontSprites).contains(node));
  }

  public void drawPlayerLives(PlayerLivesUpdateData data) {
    int lives = data.lives();

    // Define the starting X position for drawing lives sprites
    double startX = 67.6; // You may adjust this based on your layout

    // Clear any existing lives sprites before drawing new ones
    clearLivesSprites();

    // Draw the lives sprite at the appropriate position
    ImageView lifeView =
        new ImageView(
            fontSprites[lives]
                .getImage()); // Assuming font sprite for lives is at index corresponding to the
    // number of lives
    lifeView.setFitWidth(8 * 2.8);
    lifeView.setFitHeight(44);
    lifeView.setX(startX);
    lifeView.setY(28); // Adjust Y position as needed
    anchorPane.getChildren().add(lifeView);
  }

  // Utility method to clear existing lives sprites
  private void clearLivesSprites() {
    anchorPane
        .getChildren()
        .removeIf(node -> node instanceof ImageView && Arrays.asList(fontSprites).contains(node));
  }

  public void handleExitSpawn(ExitTileSpawnData data) {
    int exitX = data.exitTileX();
    int exitY = data.exitTileY();

    ImageView exitImageView = new ImageView(Tiles.EXIT.getImage());
    exitImageView.setFitWidth(48);
    exitImageView.setFitHeight(48);
    exitImageView.setX(exitX);
    exitImageView.setY(exitY + Y_OFFSET);

    anchorPane.getChildren().add(exitImageView);
  }
}
