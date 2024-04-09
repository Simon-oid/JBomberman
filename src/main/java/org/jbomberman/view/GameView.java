package org.jbomberman.view;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;
import org.jbomberman.model.entita.Direction;
import org.jbomberman.model.entita.Type;
import org.jbomberman.model.listener.*;
import org.jbomberman.model.powerups.PowerUpType;

@Getter
@Setter
public class GameView {
  /** The map width */
  int MAP_WIDTH = 15;

  /** The map height */
  int MAP_HEIGHT = 13;

  /** The adjusted y offset */
  int adjustedYOffset = 64;

  /** The current level */
  private int currentLevel;

  /** The player score */
  private int playerScore;

  /** The TilePane */
  private TilePane tilePane;

  /** The AnchorPane */
  private AnchorPane anchorPane;

  /** The player imageview */
  private ImageView player;

  /** The mob sprites */
  private HashMap<Type, Map<Integer, Sprite>> mobSprites;

  /** The sprite id counter */
  private int spriteIdCounter = 0;

  /** The scoreboard */
  private ImageView scoreboard;

  /** The Y offset for the player and mobs. */
  private static final int Y_OFFSET = 100;

  /** The font sprites */
  private ImageView[] fontSprites;

  /** The font flicker sprites */
  private ImageView[] scoreFontSprites;

  /** The font flicker sprites */
  private ImageView[] scoreFontFlickerSprites;

  /** The player movement animation */
  private Timeline playerMovementAnimation;

  /** The player hit animation */
  private Timeline playerHitAnimation;

  /** The last animation direction */
  private Direction lastAnimationDirection = Direction.NONE;

  /** The mob movement animations */
  private Map<Sprite, Timeline> mobMovementAnimations;

  /** The last puropen direction */
  private Direction lastPuropenDirection = Direction.NONE;

  /** The destructible tile sprites */
  private Image[] destructibleTileSprites;

  /** The clock animation sprites */
  private ImageView[] clockSprites;

  /** The clock animation timeline */
  private Timeline clockAnimation;

  /** The clock imageview */
  private ImageView clockImageView;

  /** The exit tile imageview */
  private ImageView exitTileImageView;

  /** The first time loading */
  private boolean firstTimeLoading = true;

  /** The animation duration */
  private static final double ANIMATION_DURATION = 0.5; // Duration for animations in seconds

  /** The HUD appearance delay */
  private static final double HUD_APPEARANCE_DELAY = 0; // Delay before HUD appears

  /** The AudioManager instance */
  private AudioManager audioManager;

  /**
   * The sound timer. This timer is used to play the walking sound effect at regular intervals when
   * the player is moving.
   */
  private AnimationTimer soundTimer;

  /** The last play time. */
  private long lastPlayTime;

  /** The sound timer running. */
  private boolean soundTimerRunning = false;

  /** The soundtrack timer. */
  private AnimationTimer soundtrackTimer;

  /** The last play time soundtrack. */
  private long lastPlayTimeSoundtrack;

  /** The mob transitions */
  private HashMap<ImageView, TranslateTransition> mobTransitions = new HashMap<>();

  /** Instantiates a new Game view. */
  public GameView() {

    setupSoundTimer();

    audioManager = AudioManager.getInstance(); // Get AudioManager instance

    anchorPane = new AnchorPane();

    setupSoundTrackTimer();

    initializePlayer();

    // Initialize scoreboard
    initializeScoreboard();

    // Load clock sprites
    loadClockSprites();

    loadDestructibleTileAnimation();

    // Load font sprites
    loadFontSprites();

    // Load font sprites for numbers 0-9
    loadScoreFontSprites();

    loadScoreFlickerFontSprites();

    // Add player and scoreboard to anchorPane
    anchorPane = new AnchorPane(player, scoreboard); // Exclude tilePane initially

    currentLevel = 1;

    initGameView();
  }

  /**
   * Sets up the soundtrack timer. This timer is used to play the soundtrack at regular intervals
   * after the initial playback.
   */
  private void setupSoundTrackTimer() {
    soundtrackTimer =
        new AnimationTimer() {
          @Override
          public void handle(long now) {
            // Calculate the elapsed time since the last playback in seconds
            long elapsedTimeSeconds = (now - lastPlayTimeSoundtrack) / 1_000_000_000;

            // Check if the elapsed time exceeds the duration of the soundtrack (2 minutes and 42
            // seconds)
            if (elapsedTimeSeconds >= (2 * 60 + 42)) {
              audioManager.play(AudioSample.SOUNDTRACK);
              lastPlayTimeSoundtrack = now; // Reset the last playback time
            }
          }
        };
  }

  /**
   * Stops the soundtrack timer. This method is called when the game view is reset or when the game
   * view is closed.
   */
  private void stopSoundtrackTimer() {
    if (soundtrackTimer != null) {
      soundtrackTimer.stop();
      soundtrackTimer = null;
    }
  }

  /**
   * Stops the sound timer. This method is called when the game view is reset or when the game view
   * is closed.
   */
  private void stopSoundtrack() {
    AudioManager.getInstance().stopAll();
  }

  /**
   * Initializes the GameView. This method is called when the game view is first loaded or when the
   * game view is reset.
   */
  private void initGameView() {

    playerMovementAnimation = new Timeline();

    audioManager.play(AudioSample.STAGE_INTRO);

    // Schedule both soundtrack playback and timer setup inside a timeline
    Timeline timelineSoundTrack =
        new Timeline(
            new KeyFrame(
                Duration.seconds(3.5),
                event -> {
                  audioManager.play(AudioSample.SOUNDTRACK);
                }));
    timelineSoundTrack.setCycleCount(1); // Ensure timeline executes only once
    timelineSoundTrack.play();

    ImageView levelSwapFontImageView;

    // Load and display the appropriate LevelSwapFont image based on the current level
    if (currentLevel == 1) {
      levelSwapFontImageView = new ImageView(LevelSwapFont.STAGE1_LVL_1.getImage());
    } else if (currentLevel == 2) {
      levelSwapFontImageView = new ImageView(LevelSwapFont.STAGE1_LVL_2.getImage());
    } else {
      // Handle unsupported levels or default case
      return;
    }

    // Add the LevelSwapFont image to the anchorPane
    anchorPane.getChildren().add(levelSwapFontImageView);

    // Position the LevelSwapFont image within the AnchorPane using layout constraints
    // Position the LevelSwapFont image below the tilePane
    AnchorPane.setTopAnchor(
        levelSwapFontImageView, (double) Y_OFFSET + 150); // Initially position below the screen
    AnchorPane.setLeftAnchor(levelSwapFontImageView, (-700.0)); // Center horizontally

    // Slide the LevelSwapFont image from below to Y_OFFSET + 150
    Timeline slideInTimeline1 =
        new Timeline(
            new KeyFrame(
                Duration.seconds(0.5),
                new KeyValue(
                    levelSwapFontImageView.translateXProperty(), (860)))); // Center horizontally

    // Create a pause transition with a duration of 1 second
    PauseTransition pauseTransition = new PauseTransition(Duration.seconds(3));

    // Slide the LevelSwapFont image from the center to the right
    Timeline slideInTimeline2 =
        new Timeline(
            new KeyFrame(
                Duration.seconds(0.5),
                new KeyValue(
                    levelSwapFontImageView.translateXProperty(),
                    (710 + 999)))); // Center horizontally

    // Chain the timelines
    slideInTimeline1.setOnFinished(event -> pauseTransition.play());
    pauseTransition.setOnFinished(event -> slideInTimeline2.play());

    slideInTimeline1.play();

    initializeTilePane();

    tilePane.setVisible(false);

    // Positioning elements within the AnchorPane using layout constraints
    AnchorPane.setTopAnchor(player, (double) Y_OFFSET); // Shift player down by 256 pixels
    AnchorPane.setLeftAnchor(player, 0.0); // Position player at the left

    AnchorPane.setTopAnchor(scoreboard, 0.0); // Position scoreboard at the bottom
    AnchorPane.setLeftAnchor(scoreboard, 0.0); // Position scoreboard at the left

    // Initialize scoreboard
    initializeScoreboard();

    scoreboard.setVisible(false);

    initializeMobSprites();

    for (Map<Integer, Sprite> mobSpriteMap : mobSprites.values()) {
      for (Sprite mobSprite : mobSpriteMap.values()) {
        mobSprite.getImageView().setVisible(false);
      }
    }
    // Initialize clock animation
    initializeClockAnimation();

    // Draw clock on HUD
    drawClockOnHUD();

    // Slowly appear the HUD elements
    slowlyAppearHUD();

    // Schedule the addition of tilePane with a delay
    Timeline timeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(2.3),
                event -> {
                  // Slide in the tilemap from below
                  slideInTilemapFromBelow();
                }));
    timeline.setCycleCount(1); // Ensure timeline executes only once
    timeline.play();

    Timeline timeline1 =
        new Timeline(
            new KeyFrame(
                Duration.seconds(3.5),
                event -> {
                  tilePane.setVisible(true);
                  player.setVisible(true);
                  scoreboard.setVisible(true);
                  // Hide the mobs
                  // Show the mobs
                  for (Map<Integer, Sprite> mobSpriteMap : mobSprites.values()) {
                    for (Sprite mobSprite : mobSpriteMap.values()) {
                      mobSprite.getImageView().setVisible(true);
                    }
                  }
                }));

    Timeline timeline2 =
        new Timeline(
            new KeyFrame(
                Duration.seconds(5),
                event -> {
                  anchorPane.getChildren().remove(levelSwapFontImageView);
                }));
    timeline2.setCycleCount(1);
    timeline1.setCycleCount(1);
    timeline1.play();
    timeline2.play();
  }

  /**
   * Resets the game view. This method is called when the player completes a level or when the game
   * view is closed.
   */
  public void resetGameView() {
    if (firstTimeLoading) {

      currentLevel = 1;

      firstTimeLoading = false;
    } else {

      currentLevel++;

      resetView();

      initGameView();
    }
  }

  /**
   * Resets the view. This method is called when the player completes a level or when the game view
   * is closed.
   */
  public void resetView() {

    anchorPane.getChildren().remove(exitTileImageView);

    anchorPane.getChildren().remove(tilePane);

    player.setVisible(false);

    scoreboard.setVisible(false);

    clockAnimation.stop();
    clockAnimation.getKeyFrames().clear();

    removeAllPowerUps();

    // Hide the mobs

    // Hide the mobs
    for (Map<Integer, Sprite> mobSpriteMap : mobSprites.values()) {
      for (Sprite mobSprite : mobSpriteMap.values()) {
        mobSprite.getImageView().setVisible(false);
      }
    }

    stopSoundtrackTimer();

    setupSoundTrackTimer();
  }

  /**
   * clears the HUD. This method is called when the player completes a level or when the game view
   * is closed.
   */
  public void clearHUD() {
    anchorPane.getChildren().removeAll(scoreboard, clockImageView);
  }

  /**
   * Removes all power ups. This method is called when the player completes a level or when the game
   * view is closed.
   */
  private void removeAllPowerUps() {
    List<Image> powerUpImages =
        Arrays.asList(
            Tiles.POWERUP_BOMBUP_0.getImage(),
            Tiles.POWERUP_BOMBUP_1.getImage(),
            Tiles.POWERUP_SKATE_0.getImage(),
            Tiles.POWERUP_SKATE_1.getImage(),
            Tiles.POWERUP_ICESCREAM_0.getImage(),
            Tiles.POWERUP_ICESCREAM_1.getImage());

    List<Node> toRemove = new ArrayList<>();
    for (Node node : anchorPane.getChildren()) {
      if (node instanceof ImageView && powerUpImages.contains(((ImageView) node).getImage())) {
        toRemove.add(node);
      }
    }
    anchorPane.getChildren().removeAll(toRemove);
  }

  /** Initializes the player. */
  private void initializePlayer() {
    player = new ImageView(Entities.VOID.getImage());
    player.setFitHeight(96);
    player.setFitWidth(48);
  }

  /** Initializes the TilePane. */
  private void initializeTilePane() {
    tilePane = new TilePane();
    tilePane.setPrefRows(MAP_HEIGHT);
    tilePane.setPrefColumns(MAP_WIDTH);

    // Add tile images to tilePane
    for (int i = 1; i <= MAP_WIDTH * MAP_HEIGHT; i++) {
      ImageView imageView = new ImageView();
      imageView.setFitHeight(48);
      imageView.setFitWidth(48);
      imageView.setSmooth(false);
      tilePane.getChildren().add(imageView);
    }

    anchorPane.getChildren().add(tilePane);
    // Position tilePane within the AnchorPane using layout constraints
    AnchorPane.setTopAnchor(tilePane, (double) Y_OFFSET); // Shift tilepane down by 256 pixels
    AnchorPane.setLeftAnchor(tilePane, 0.0); // Position tilepane at the left
  }

  /** Initializes the scoreboard. */
  private void initializeScoreboard() {
    scoreboard = new ImageView(HUD.HUD_SPRITE.getImage()); // Load custom scoreboard sprite
    scoreboard.setFitWidth(720);
    scoreboard.setFitHeight(100);
  }

  /**
   * Method that make the HUD slowly appear. This method is called when the game view is first
   * loaded or when the game view is reset.
   */
  public void slowlyAppearHUD() {
    List<Node> hudNodes = new ArrayList<>(anchorPane.getChildren());
    hudNodes.remove(tilePane); // Exclude tilePane from fading animation
    fadeIn(hudNodes, ANIMATION_DURATION, HUD_APPEARANCE_DELAY);

    // Fade in the score numbers
    fadeInScoreNumbers();

    // Fade in the clock
    fadeInClock();
  }

  /** Method that makes the score numbers slowly appear. */
  private void fadeInScoreNumbers() {
    fadeIn(Arrays.asList(scoreFontSprites), ANIMATION_DURATION, HUD_APPEARANCE_DELAY);
    fadeIn(Arrays.asList(scoreFontFlickerSprites), ANIMATION_DURATION, HUD_APPEARANCE_DELAY);
  }

  /** Method that makes the clock slowly appear. */
  private void fadeInClock() {
    clockImageView.setOpacity(0.0);

    FadeTransition fadeTransition =
        new FadeTransition(Duration.seconds(ANIMATION_DURATION), clockImageView);
    fadeTransition.setFromValue(0.0);
    fadeTransition.setToValue(1.0);
    fadeTransition.setDelay(Duration.seconds(HUD_APPEARANCE_DELAY)); // Add the delay
    fadeTransition.play();
  }

  /** method that makes the TileMap slide in from below the screen. */
  public void slideInTilemapFromBelow() {

    // Schedule the slide-in animation with the specified delay
    Timeline timeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(1),
                event -> {
                  double finalX = 0; // Final X position
                  double finalY = 0; // Final Y position
                  double initialY = 900; // Start from below the screen

                  slideIn(tilePane, ANIMATION_DURATION, finalX, finalX, initialY, finalY);
                }));
    timeline.play();
  }

  /**
   * the SlideIn method that slides in a node from a specified position to another position.
   *
   * @param node the node
   * @param durationSeconds the duration seconds
   * @param fromX the position x where the animation starts
   * @param toX the position x where the animation ends
   * @param fromY the position y where the animation starts
   * @param toY the position y where the animation ends
   */
  private void slideIn(
      Node node, double durationSeconds, double fromX, double toX, double fromY, double toY) {
    node.setTranslateX(fromX);
    node.setTranslateY(fromY);
    TranslateTransition translateTransition =
        new TranslateTransition(Duration.seconds(durationSeconds), node);
    translateTransition.setFromX(fromX);
    translateTransition.setToX(toX);
    translateTransition.setFromY(fromY);
    translateTransition.setToY(toY);
    translateTransition.play();
  }

  /**
   * Utility method to fade in a list of nodes with a specified duration and delay.
   *
   * @param nodes List of nodes to fade in
   * @param durationSeconds Duration of the fade in animation
   * @param delaySeconds Delay before the fade in animation starts
   */
  private void fadeIn(List<Node> nodes, double durationSeconds, double delaySeconds) {
    for (Node node : nodes) {
      if (node != tilePane) { // Exclude tilePane from fading animation
        node.setOpacity(0.0);
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(durationSeconds), node);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.setDelay(Duration.seconds(delaySeconds));
        fadeTransition.play();
      }
    }
  }

  /** Method that initializes the mob sprites. */
  private void initializeMobSprites() {
    mobSprites = new HashMap<>();
    mobMovementAnimations = new HashMap<>(); // New HashMap to store timelines

    int numberOfPuropens = 0;
    int numberOfDenkyuns = 0;

    if (currentLevel == 1) {
      numberOfPuropens = 3;
    } else if (currentLevel == 2) {
      numberOfPuropens = 3;
      numberOfDenkyuns = 1;
    }

    int puropenIdCounter = 0; // Separate counter for PUROPEN
    int denkyunIdCounter = 0; // Separate counter for DENKYUN

    for (Type mobType : Type.values()) {
      List<ImageView> mobImageViewList = new ArrayList<>();
      Map<Integer, Sprite> mobSpriteMap = new HashMap<>(); // Create a new map for Sprites
      int numberOfMobs = (mobType == Type.PUROPEN) ? numberOfPuropens : numberOfDenkyuns;

      for (int i = 0; i < numberOfMobs; i++) {
        // Use the Sprites instance to get the image for the ImageView
        ImageView mobImageView = new ImageView(Entities.VOID.getImage());
        mobImageView.setFitWidth(48);
        mobImageView.setFitHeight(80);
        mobImageViewList.add(mobImageView);

        // Create a new Sprite with the unique ID and add it to the map
        int spriteId = (mobType == Type.PUROPEN) ? puropenIdCounter++ : denkyunIdCounter++;
        Sprite mobSprite = new Sprite(mobImageView, spriteId, 0, 0);
        mobSpriteMap.put(spriteId, mobSprite); // Add the Sprite to the map

        // Create a new Timeline for each mob and store it in the map
        Timeline mobTimeline = new Timeline();
        mobMovementAnimations.put(mobSprite, mobTimeline); // Store the Timeline in the map
      }
      mobSprites.put(mobType, mobSpriteMap);
    }
  }

  /**
   * Method that loads the map tiles using a matrix from the LoadMapData data object.
   *
   * @param data the LoadMapData object
   */
  public void loadMap(LoadMapData data) {

    int i = 0;
    for (Tiles tiles : data.matrix()) {
      ImageView tileImageView = (ImageView) tilePane.getChildren().get(i++);
      // Set the tile image
      tileImageView.setImage(tiles.getImage());

      // Check if the tile is a destructible tile
      if (tiles == Tiles.DESTROYABLE) {
        // Check if there's a non-destructible tile above it
        if (isTileAboveDestructible(i - 1, data)) {
          // Play a different animation for the destructible tile
          playDestructibleUnderNonDestructibleAnimation(tileImageView);
        } else {
          // Play the regular animation for the destructible tile
          playDestructibleTileAnimation(tileImageView);
        }
      }

      // Force layout update
      tilePane.getParent().layout();
    }
  }

  /**
   * gett the root of the game view.
   *
   * @return the root
   */
  public Parent getRoot() {

    return anchorPane;
  }

  /**
   * Method that spawns the player at the initial position.
   *
   * @param playerInitialPosition the player initial position
   */
  public void spawnPlayer(PlayerInitialPositionData playerInitialPosition) {

    player.setTranslateX(48);
    player.setTranslateY(-4);
  }

  /** Method that starts the sound timer. */
  private void setupSoundTimer() {
    soundTimer =
        new AnimationTimer() {
          @Override
          public void handle(long now) {
            // Check if at least 500 milliseconds (half a second) have passed since the last play
            // time
            if (now - lastPlayTime >= 320_000_000) {
              audioManager.play(AudioSample.WALKING_1);
              lastPlayTime = now;
            }
          }
        };
  }

  /**
   * Method that moves the player based on the PlayerMovementData object.
   *
   * @param data the PlayerMovementData object
   */
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

    // Determine the direction of movement from the packet data
    Direction direction = data.direction();

    // Start the transition if direction is not NONE
    if (direction != Direction.NONE) {
      if (direction != lastAnimationDirection) {
        animatePlayer(player, direction);
        // Start the sound timer if not already running
        if (!soundTimerRunning) {
          soundTimer.start();
          soundTimerRunning = true;
        }
      }
      transition.play();
    } else {
      player.setImage(Entities.PLAYER.getImage());
      playerMovementAnimation.pause();

      // Stop the sound timer when movement stops
      if (soundTimerRunning) {
        soundTimer.stop();
        soundTimerRunning = false;
      }
    }
  }

  /**
   * Method that animates the player based on the direction.
   *
   * @param playerImageView The player image view
   * @param direction The direction
   */
  private void animatePlayer(ImageView playerImageView, Direction direction) {
    switch (direction) {
      case UP:
        playerMovementAnimation =
            playPlayerMovementAnimation(playerImageView, getPlayerSpriteImages(Direction.UP));
        break;
      case DOWN:
        playerMovementAnimation =
            playPlayerMovementAnimation(playerImageView, getPlayerSpriteImages(Direction.DOWN));
        break;
      case LEFT:
        playerMovementAnimation =
            playPlayerMovementAnimation(playerImageView, getPlayerSpriteImages(Direction.LEFT));
        break;
      case RIGHT:
        playerMovementAnimation =
            playPlayerMovementAnimation(playerImageView, getPlayerSpriteImages(Direction.RIGHT));
        break;
      case NONE:
        player.setImage(Entities.PLAYER.getImage());
        break;
    }
  }

  /**
   * Method that plays the player movement animation based on the direction and sprite images.
   *
   * @param imageView The image view
   * @param sprites The sprite images
   * @return the timeline
   */
  private Timeline playPlayerMovementAnimation(ImageView imageView, Image[] sprites) {

    // Create keyframes for each sprite in the sequence
    for (int i = 0; i < sprites.length; i++) {
      final int index = i;
      KeyFrame keyFrame =
          new KeyFrame(
              Duration.seconds(i * 0.2), // Change duration as needed for faster animation
              event -> imageView.setImage(sprites[index]));
      playerMovementAnimation.getKeyFrames().add(keyFrame);
    }

    // Set the cycle count to indefinite to  keep the animation playing
    playerMovementAnimation.setCycleCount(Animation.INDEFINITE);

    // Play the animation
    playerMovementAnimation.play();

    // Return the timeline object
    return playerMovementAnimation;
  }

  /**
   * Method that gets the player sprite images based on the direction.
   *
   * @param direction The direction
   * @return the player sprite images
   */
  private Image[] getPlayerSpriteImages(Direction direction) {
    // Return player sprite images based on direction
    switch (direction) {
      case UP:
        return new Image[] {
          Entities.PLAYER_UP1.getImage(),
          Entities.PLAYER_UP.getImage(),
          Entities.PLAYER_UP2.getImage(),
          Entities.PLAYER_UP.getImage(),
          Entities.PLAYER_UP.getImage()
        };
      case DOWN:
        return new Image[] {
          Entities.PLAYER_DOWN1.getImage(),
          Entities.PLAYER.getImage(),
          Entities.PLAYER_DOWN2.getImage(),
          Entities.PLAYER.getImage(),
          Entities.PLAYER.getImage()
        };
      case LEFT:
        return new Image[] {
          Entities.PLAYER_LEFT1.getImage(),
          Entities.PLAYER_LEFT.getImage(),
          Entities.PLAYER_LEFT2.getImage(),
          Entities.PLAYER_LEFT.getImage(),
          Entities.PLAYER_LEFT.getImage()
        };
      case RIGHT:
        return new Image[] {
          Entities.PLAYER_RIGHT1.getImage(),
          Entities.PLAYER_RIGHT.getImage(),
          Entities.PLAYER_RIGHT2.getImage(),
          Entities.PLAYER_RIGHT.getImage(),
          Entities.PLAYER_RIGHT.getImage()
        };
      case NONE:
        return new Image[] {
          Entities.PLAYER.getImage(),
          Entities.PLAYER.getImage(),
          Entities.PLAYER.getImage(),
          Entities.PLAYER.getImage(),
        };
      default:
        return new Image[] {};
    }
  }

  /**
   * Method that spawns the bomb at the specified position.
   *
   * @param data the bomb spawn data
   */
  public void spawnBomb(BombSpawnData data) {
    int bombX = data.bombX();
    int bombY = data.bombY();
    double bombTimer = 4.0; // Bomb timer duration is 2 seconds

    ImageView bombImageView = new ImageView(Entities.BOMB_1.getImage());
    bombImageView.setFitWidth(48);
    bombImageView.setFitHeight(48);
    bombImageView.setX(bombX);
    bombImageView.setY(bombY + Y_OFFSET);

    // Add the bomb image view to the anchor pane
    anchorPane.getChildren().add(bombImageView);

    // Ensure bomb is drawn under the player
    anchorPane.getChildren().remove(player);
    anchorPane.getChildren().add(player);

    // Play bomb explosion animation with bomb timer duration
    playBombExplosionAnimation(bombImageView, bombTimer);

    // Play bomb explosion sound
    audioManager.play(AudioSample.BOMB_PLACEMENT);
  }

  /**
   * method that plays the bomb explosion animation.
   *
   * @param bombImageView the bomb image view
   * @param bombTimer the bomb timer
   */
  private void playBombExplosionAnimation(ImageView bombImageView, double bombTimer) {
    Image[] bombExplosionSprites = {
      Entities.BOMB_1.getImage(), Entities.BOMB_2.getImage(), Entities.BOMB_3.getImage()
    };

    Timeline timeline = new Timeline();

    // Calculate the duration of each frame based on the number of sprites and the bomb timer
    Duration frameDuration = Duration.seconds(bombTimer / (bombExplosionSprites.length * 4));

    // Add key frames to change the image at specific intervals for forward animation
    for (int i = 0; i < bombExplosionSprites.length; i++) {
      int finalI = i;
      KeyFrame keyFrame =
          new KeyFrame(
              frameDuration.multiply(i),
              event -> bombImageView.setImage(bombExplosionSprites[finalI]));
      timeline.getKeyFrames().add(keyFrame);
    }

    // Add key frames to change the image at specific intervals for reverse animation
    for (int i = 0; i < bombExplosionSprites.length; i++) {
      int finalI = i;
      KeyFrame keyFrame =
          new KeyFrame(
              frameDuration.multiply(i + bombExplosionSprites.length),
              event ->
                  bombImageView.setImage(
                      bombExplosionSprites[bombExplosionSprites.length - finalI - 1]));
      timeline.getKeyFrames().add(keyFrame);
    }

    // Set the cycle count to 2 to play the animation twice
    timeline.setCycleCount(2);

    // Remove the bomb image view from the anchor pane after the animation finishes
    timeline.setOnFinished(
        event -> {
          audioManager.play(AudioSample.BOMB_EXPLOSION);
          anchorPane.getChildren().remove(bombImageView);
        });

    // Play the timeline
    timeline.play();
  }

  /**
   * Method that handles the bomb explosion animation.
   *
   * @param data the bomb explosion data
   */
  public void explodeBomb(BombExplosionData data) {
    int explosionX = data.explosionX();
    int explosionY = data.explosionY();
    int[] ranges = data.ranges();

    // Draw the initial explosion sprite at the bomb's position
    animateExplosionSprite(explosionX, explosionY + Y_OFFSET, Entities.BOMB_EXPLOSION_CENTER_0);

    // Remove the bomb image
    removeBombImage(explosionX, explosionY + Y_OFFSET);

    // Define explosion sprites for each direction
    Entities[] explosionSpritesRight = {
      Entities.BOMB_EXPLOSION_RIGHT_0,
      Entities.BOMB_EXPLOSION_RIGHT_1,
      Entities.BOMB_EXPLOSION_RIGHT_2,
      Entities.BOMB_EXPLOSION_RIGHT_3,
      Entities.BOMB_EXPLOSION_RIGHT_4
    };

    Entities[] explosionSpritesLeft = {
      Entities.BOMB_EXPLOSION_LEFT_0,
      Entities.BOMB_EXPLOSION_LEFT_1,
      Entities.BOMB_EXPLOSION_LEFT_2,
      Entities.BOMB_EXPLOSION_LEFT_3,
      Entities.BOMB_EXPLOSION_LEFT_4
    };

    Entities[] explosionSpritesUp = {
      Entities.BOMB_EXPLOSION_UP_0,
      Entities.BOMB_EXPLOSION_UP_1,
      Entities.BOMB_EXPLOSION_UP_2,
      Entities.BOMB_EXPLOSION_UP_3,
      Entities.BOMB_EXPLOSION_UP_4
    };

    Entities[] explosionSpritesDown = {
      Entities.BOMB_EXPLOSION_DOWN_0,
      Entities.BOMB_EXPLOSION_DOWN_1,
      Entities.BOMB_EXPLOSION_DOWN_2,
      Entities.BOMB_EXPLOSION_DOWN_3,
      Entities.BOMB_EXPLOSION_DOWN_4
    };

    // Draw the explosions based on the ranges array
    drawExplosionSpritesInDirection(
        explosionX,
        explosionY,
        ranges[0],
        1,
        0,
        Entities.BOMB_EXPLOSION_RIGHT_0,
        Entities.BOMB_EXPLOSION_RIGHT_LAST_0,
        explosionSpritesRight); // Right
    drawExplosionSpritesInDirection(
        explosionX,
        explosionY,
        ranges[1],
        -1,
        0,
        Entities.BOMB_EXPLOSION_LEFT_0,
        Entities.BOMB_EXPLOSION_LEFT_LAST_0,
        explosionSpritesLeft); // Left
    drawExplosionSpritesInDirection(
        explosionX,
        explosionY,
        ranges[2],
        0,
        -1,
        Entities.BOMB_EXPLOSION_UP_0,
        Entities.BOMB_EXPLOSION_UP_LAST_0,
        explosionSpritesUp); // Up
    drawExplosionSpritesInDirection(
        explosionX,
        explosionY,
        ranges[3],
        0,
        1,
        Entities.BOMB_EXPLOSION_DOWN_0,
        Entities.BOMB_EXPLOSION_DOWN_LAST_0,
        explosionSpritesDown); // Down

    // Ensure bomb is drawn under the player
    anchorPane.getChildren().remove(player);
    anchorPane.getChildren().add(player);

    // Schedule a task to remove the remaining explosion images after 0.6 seconds
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    scheduler.schedule(
        () -> {
          Platform.runLater(
              () -> {
                anchorPane
                    .getChildren()
                    .removeIf(
                        node -> {
                          if (node instanceof ImageView) {
                            Image image = ((ImageView) node).getImage();
                            return image == Entities.BOMB_EXPLOSION_CENTER_0.getImage()
                                || image == Entities.BOMB_EXPLOSION_DOWN_LAST_0.getImage()
                                || image == Entities.BOMB_EXPLOSION_UP_LAST_0.getImage()
                                || image == Entities.BOMB_EXPLOSION_LEFT_LAST_0.getImage()
                                || image == Entities.BOMB_EXPLOSION_RIGHT_LAST_0.getImage()
                                || image == Entities.BOMB_EXPLOSION_DOWN_0.getImage()
                                || image == Entities.BOMB_EXPLOSION_UP_0.getImage()
                                || image == Entities.BOMB_EXPLOSION_LEFT_0.getImage()
                                || image == Entities.BOMB_EXPLOSION_RIGHT_0.getImage();
                          }
                          return false;
                        });
              });
        },
        600,
        TimeUnit.MILLISECONDS);
  }

  /**
   * Method that draws the explosion sprites in a specific direction.
   *
   * @param startX the start x
   * @param startY the start y
   * @param range the range
   * @param deltaX the delta x
   * @param deltaY the delta y
   * @param directionEntity the direction entity
   * @param lastDirectionEntity the last direction entity
   * @param explosionSprites the explosion sprites
   */
  private void drawExplosionSpritesInDirection(
      int startX,
      int startY,
      int range,
      int deltaX,
      int deltaY,
      Entities directionEntity,
      Entities lastDirectionEntity,
      Entities[] explosionSprites) {
    for (int i = 1; i <= range; i++) {
      int x = startX + i * deltaX * 48;
      int y = startY + i * deltaY * 48;

      if (i < range) {
        animateExplosionDirectionSprite(x, y + Y_OFFSET, directionEntity, explosionSprites);
      } else {
        animateExplosionDirectionSprite(x, y + Y_OFFSET, lastDirectionEntity, explosionSprites);
      }
    }
  }

  /**
   * Method that animates the explosion sprite in a specific direction.
   *
   * @param x the x
   * @param y the y
   * @param entity the entity
   * @param explosionSprites the explosion sprites
   */
  private void animateExplosionDirectionSprite(
      int x, int y, Entities entity, Entities[] explosionSprites) {
    ImageView explosionImageView = new ImageView(entity.getImage());
    explosionImageView.setFitWidth(48);
    explosionImageView.setFitHeight(48);
    explosionImageView.setX(x);
    explosionImageView.setY(y);

    anchorPane.getChildren().add(explosionImageView);

    Timeline timeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(0.15),
                new KeyValue(explosionImageView.imageProperty(), entity.getImage())),
            new KeyFrame(
                Duration.seconds(0.2),
                new KeyValue(explosionImageView.imageProperty(), explosionSprites[1].getImage())),
            new KeyFrame(
                Duration.seconds(0.25),
                new KeyValue(explosionImageView.imageProperty(), explosionSprites[2].getImage())),
            new KeyFrame(
                Duration.seconds(0.3),
                new KeyValue(explosionImageView.imageProperty(), explosionSprites[3].getImage())),
            new KeyFrame(
                Duration.seconds(0.35),
                new KeyValue(explosionImageView.imageProperty(), explosionSprites[4].getImage())),
            new KeyFrame(
                Duration.seconds(0.4),
                new KeyValue(explosionImageView.imageProperty(), explosionSprites[3].getImage())),
            new KeyFrame(
                Duration.seconds(0.45),
                new KeyValue(explosionImageView.imageProperty(), explosionSprites[2].getImage())),
            new KeyFrame(
                Duration.seconds(0.5),
                new KeyValue(explosionImageView.imageProperty(), explosionSprites[1].getImage())),
            new KeyFrame(
                Duration.seconds(0.55),
                new KeyValue(explosionImageView.imageProperty(), entity.getImage())));
    timeline.setCycleCount(1);
    timeline.play();
  }

  /**
   * Method that animates the explosion sprite at the specified position.
   *
   * @param x the x
   * @param y the y
   * @param entity the entity
   */
  private void animateExplosionSprite(int x, int y, Entities entity) {
    // Create ImageView with initial sprite
    ImageView explosionImageView = new ImageView(entity.getImage());
    explosionImageView.setFitWidth(48);
    explosionImageView.setFitHeight(48);
    explosionImageView.setX(x);
    explosionImageView.setY(y);

    // Add ImageView to anchorPane
    anchorPane.getChildren().add(explosionImageView);

    // Animate ImageView
    Timeline timeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(0.15),
                new KeyValue(explosionImageView.imageProperty(), entity.getImage())),
            new KeyFrame(
                Duration.seconds(0.2),
                new KeyValue(
                    explosionImageView.imageProperty(),
                    Entities.BOMB_EXPLOSION_CENTER_1.getImage())),
            new KeyFrame(
                Duration.seconds(0.25),
                new KeyValue(
                    explosionImageView.imageProperty(),
                    Entities.BOMB_EXPLOSION_CENTER_2.getImage())),
            new KeyFrame(
                Duration.seconds(0.3),
                new KeyValue(
                    explosionImageView.imageProperty(),
                    Entities.BOMB_EXPLOSION_CENTER_3.getImage())),
            new KeyFrame(
                Duration.seconds(0.35),
                new KeyValue(
                    explosionImageView.imageProperty(),
                    Entities.BOMB_EXPLOSION_CENTER_4.getImage())),
            new KeyFrame(
                Duration.seconds(0.4),
                new KeyValue(
                    explosionImageView.imageProperty(),
                    Entities.BOMB_EXPLOSION_CENTER_3.getImage())),
            new KeyFrame(
                Duration.seconds(0.45),
                new KeyValue(
                    explosionImageView.imageProperty(),
                    Entities.BOMB_EXPLOSION_CENTER_2.getImage())),
            new KeyFrame(
                Duration.seconds(0.5),
                new KeyValue(
                    explosionImageView.imageProperty(),
                    Entities.BOMB_EXPLOSION_CENTER_1.getImage())),
            new KeyFrame(
                Duration.seconds(0.55),
                new KeyValue(explosionImageView.imageProperty(), entity.getImage())));
    timeline.setCycleCount(1);
    timeline.play();
  }

  /**
   * Method that removes the bomb image at the specified position.
   *
   * @param explosionX the explosion x
   * @param explosionY the explosion y
   */
  private void removeBombImage(int explosionX, int explosionY) {
    for (Node node : anchorPane.getChildren()) {
      if (node instanceof ImageView
          && ((ImageView) node).getImage() == Entities.BOMB_1.getImage()
          && (Math.abs(((ImageView) node).getX() - explosionX) < 1
              && Math.abs(((ImageView) node).getY() - explosionY) < 1)) {
        anchorPane.getChildren().remove(node);
        break;
      }
    }
  }

  /**
   * Method that handles the tile destruction animation.
   *
   * @param data the tile destruction data
   */
  public void handleTileDestruction(TileDestructionData data) {
    int tileIndex = data.tileIndex();

    if (tileIndex >= 0 && tileIndex < tilePane.getChildren().size()) {
      // Replace the destroyed tile with a GRASS tile
      ImageView imageView = new ImageView(Tiles.GRASS.getImage());
      imageView.setFitHeight(48);
      imageView.setFitWidth(48);
      imageView.setSmooth(false);

      tilePane.getChildren().set(tileIndex, imageView);

      // Check if there's an IMMOVABLE tile above the destroyed tile
      if (tileIndex - 15 >= 0 && tileIndex - 15 < tilePane.getChildren().size()) {
        Node tileAbove = tilePane.getChildren().get(tileIndex - 15);
        if (tileAbove instanceof ImageView) {
          Image tileAboveImage = ((ImageView) tileAbove).getImage();
          if (tileAboveImage == Tiles.IMMOVABLE.getImage()) {
            animateTileDestructionUnderImmovable(imageView);
            return;
          }
        }
      }

      // If there's no IMMOVABLE tile above, proceed with regular animation
      animateTileDestruction(imageView);

      // Check if there's a GRASS_SHADOW_DESTROYABLE tile below
      if (tileIndex + 15 < tilePane.getChildren().size()) {
        Node tileBelow = tilePane.getChildren().get(tileIndex + 15);
        if (tileBelow instanceof ImageView) {
          Image tileBelowImage = ((ImageView) tileBelow).getImage();
          if (tileBelowImage == Tiles.GRASS_SHADOW_DESTROYABLE.getImage()) {
            // Replace the tile below with GRASS
            ImageView grassImageView = new ImageView(Tiles.GRASS.getImage());
            grassImageView.setFitHeight(48);
            grassImageView.setFitWidth(48);
            grassImageView.setSmooth(false);

            tilePane.getChildren().set(tileIndex + 15, grassImageView);
          }
        }
      }
    }
  }

  /**
   * Method that plays the destructible tile animation.
   *
   * @param imageView the image view
   */
  private void animateTileDestruction(ImageView imageView) {
    Timeline timeline = new Timeline();
    timeline
        .getKeyFrames()
        .addAll(
            new KeyFrame(
                Duration.ZERO,
                new KeyValue(imageView.imageProperty(), Tiles.DESTROYED_TILE_0.getImage())),
            new KeyFrame(
                Duration.seconds(0.2),
                new KeyValue(imageView.imageProperty(), Tiles.DESTROYED_TILE_1.getImage())),
            new KeyFrame(
                Duration.seconds(0.4),
                new KeyValue(imageView.imageProperty(), Tiles.DESTROYED_TILE_2.getImage())),
            new KeyFrame(
                Duration.seconds(0.6),
                new KeyValue(imageView.imageProperty(), Tiles.DESTROYED_TILE_3.getImage())),
            new KeyFrame(
                Duration.seconds(0.8),
                new KeyValue(imageView.imageProperty(), Tiles.DESTROYED_TILE_4.getImage())),
            new KeyFrame(
                Duration.seconds(1),
                new KeyValue(imageView.imageProperty(), Tiles.DESTROYED_TILE_5.getImage())),
            new KeyFrame(
                Duration.seconds(1.2),
                new KeyValue(imageView.imageProperty(), Tiles.GRASS.getImage())));

    timeline.play();
  }

  /**
   * Method that plays the destructible tile animation under an immovable tile.
   *
   * @param imageView the image view
   */
  private void animateTileDestructionUnderImmovable(ImageView imageView) {
    Timeline timeline = new Timeline();
    timeline
        .getKeyFrames()
        .addAll(
            new KeyFrame(
                Duration.ZERO,
                new KeyValue(imageView.imageProperty(), Tiles.DESTROYED_TILE_0.getImage())),
            new KeyFrame(
                Duration.seconds(0.2),
                new KeyValue(imageView.imageProperty(), Tiles.DESTROYED_TILE_1.getImage())),
            new KeyFrame(
                Duration.seconds(0.4),
                new KeyValue(imageView.imageProperty(), Tiles.DESTROYED_TILE_2.getImage())),
            new KeyFrame(
                Duration.seconds(0.6),
                new KeyValue(imageView.imageProperty(), Tiles.DESTROYED_TILE_3.getImage())),
            new KeyFrame(
                Duration.seconds(0.8),
                new KeyValue(imageView.imageProperty(), Tiles.DESTROYED_TILE_4.getImage())),
            new KeyFrame(
                Duration.seconds(1),
                new KeyValue(imageView.imageProperty(), Tiles.DESTROYED_TILE_5.getImage())),
            new KeyFrame(
                Duration.seconds(1.2),
                new KeyValue(imageView.imageProperty(), Tiles.GRASS_SHADOW.getImage())));

    timeline.play();
  }

  /**
   * Method that spawns the mobs at the initial positions.
   *
   * @param mobInitialPositions the mob initial positions
   */
  public void spawnMob(MobInitialPositionData mobInitialPositions) {
    Type mobType = mobInitialPositions.mobType();
    int mobId = mobInitialPositions.id();

    // Get the list of sprites for the given mob type
    Map<Integer, Sprite> mobSpriteMap = mobSprites.get(mobType);
    // Check if the list is not empty and the mobId is valid
    // Check if the map is not null and contains the mobId
    if (mobSpriteMap != null && mobSpriteMap.containsKey(mobId)) {
      // Get the sprite that corresponds to the mobId from the map
      Sprite mobSprite = mobSpriteMap.get(mobId);

      ImageView mobImageView = mobSprite.getImageView();
      mobImageView.setFitWidth(48); // Set the initial fit width
      mobImageView.setFitHeight(80); // Set the initial fit height

      // Set the initial position of each mob
      mobSprite.setInitialX(mobInitialPositions.initialX()); // Add an offset based on the index
      mobSprite.setInitialY(mobInitialPositions.initialY());

      if (!anchorPane.getChildren().contains(mobImageView)) {
        anchorPane.getChildren().add(mobImageView); // add the mobImageView to the anchorPane
      }

      int adjustment = 0;
      if (mobType == Type.DENKYUN) {
        adjustment = 96;
      } else if (mobType == Type.PUROPEN && currentLevel == 1) {
        adjustment = 386;
      } else if (mobType == Type.PUROPEN && currentLevel == 2) {
        adjustment = 288;
      }

      if (mobId == 0 && currentLevel == 1) {
        mobImageView.setX(mobInitialPositions.initialX() - adjustment - 48);
      }

      double adjustedY = mobInitialPositions.initialY() - mobImageView.getFitHeight();
      mobImageView.setY(adjustedY);

      // Ensure bomb is drawn under the player
      anchorPane.getChildren().remove(player);
      anchorPane.getChildren().add(player);
    }
  }

  /**
   * Method that moves the mob based on the MobMovementData object.
   *
   * @param data the MobMovementData object
   */
  public void moveMob(MobMovementData data) {
    int xStep = data.xStep();
    int yStep = data.yStep();
    double delta = data.delta();

    int initialX = data.oldX();
    int initialY = data.oldY();

    Type mobType = data.mobType();

    int mobId = data.id(); // Get the ID from the data

    // Get the specific mob using the ID
    Sprite mobSprite = mobSprites.get(mobType).get(mobId);
    ImageView mobImageView = mobSprite.getImageView();

    mobImageView.setY(adjustedYOffset);

    // Create a new TranslateTransition for the specific mob
    TranslateTransition transition = new TranslateTransition(Duration.seconds(delta));

    // Set the ImageView as the node to be translated
    transition.setNode(mobImageView);

    transition.setFromX(initialX);
    transition.setFromY(initialY);
    transition.setToX(xStep);
    transition.setToY(yStep);

    transition.setCycleCount(1);
    transition.play();

    // Ensure bomb is drawn under the player
    anchorPane.getChildren().remove(player);
    anchorPane.getChildren().add(player);

    // Animation is played for every mob, regardless of its type
    Direction direction = data.puropenDirection();
    if (mobType == Type.PUROPEN) {
      if (direction != lastPuropenDirection) {
        animatePuropen(mobSprite, direction);
      }
    } else if (mobType == Type.DENKYUN) {
      playDenkyunAnimation(mobSprite, mobImageView);
    }
  }

  /**
   * Method that animates the Puropen mob based on the direction.
   *
   * @param mobSprite The mob sprite
   * @param direction The direction
   */
  private void animatePuropen(Sprite mobSprite, Direction direction) {

    ImageView mobImageView = mobSprite.getImageView();

    Timeline puropenMovementAnimation = mobMovementAnimations.get(mobSprite);
    switch (direction) {
      case UP:
        puropenMovementAnimation =
            playMobMovementAnimation(mobSprite, mobImageView, getPuropenSpriteImages(Direction.UP));
        break;
      case DOWN:
        playMobMovementAnimation(mobSprite, mobImageView, getPuropenSpriteImages(Direction.DOWN));
        break;
      case LEFT:
        playMobMovementAnimation(mobSprite, mobImageView, getPuropenSpriteImages(Direction.LEFT));
        break;
      case RIGHT:
        playMobMovementAnimation(mobSprite, mobImageView, getPuropenSpriteImages(Direction.RIGHT));
        break;
      case NONE:
        mobImageView.setImage(Entities.PUROPEN.getImage());
        break;
    }
    // Update the timeline stored in the map
    mobMovementAnimations.put(mobSprite, puropenMovementAnimation);
  }

  /**
   * Method that plays the mob movement animation based on the sprite images.
   *
   * @param mobSprite The mob sprite
   * @param imageView The image view
   * @param sprites The sprite images
   * @return the timeline
   */
  private Timeline playMobMovementAnimation(
      Sprite mobSprite, ImageView imageView, Image[] sprites) {
    Timeline mobMovementAnimation =
        mobMovementAnimations.get(mobSprite); // Get the existing Timeline

    // Clear the old keyframes if necessary
    mobMovementAnimation.getKeyFrames().clear();

    // Create keyframes for each sprite in the sequence
    for (int i = 0; i < sprites.length; i++) {
      final int index = i;
      KeyFrame keyFrame =
          new KeyFrame(
              Duration.seconds(i * 0.2 / 4), // Change duration as needed for faster animation
              event -> imageView.setImage(sprites[index]));
      mobMovementAnimation.getKeyFrames().add(keyFrame);
    }

    // Set the cycle count to indefinite to keep the animation playing
    mobMovementAnimation.setCycleCount(Animation.INDEFINITE);

    // Play the animation
    mobMovementAnimation.play();

    // Update the timeline stored in the map
    mobMovementAnimations.put(mobSprite, mobMovementAnimation);

    // Return the timeline object
    return mobMovementAnimation;
  }

  /**
   * Method that plays the Denkyun mob animation.
   *
   * @param mobSprite The mob sprite
   * @param imageView The image view
   */
  private Timeline playDenkyunAnimation(Sprite mobSprite, ImageView imageView) {

    Timeline mobMovementAnimation =
        mobMovementAnimations.get(mobSprite); // Get the existing Timeline

    Image[] sprites = getDenkyunSpriteImages();

    Duration frameDuration = Duration.seconds(0.1);

    // Add key frames to change the image at specific intervals for forward animation
    for (int i = 0; i < sprites.length; i++) {
      int finalI = i;
      KeyFrame keyFrame =
          new KeyFrame(frameDuration.multiply(i), event -> imageView.setImage(sprites[finalI]));
      mobMovementAnimation.getKeyFrames().add(keyFrame);
    }

    // Add key frames to change the image at specific intervals for reverse animation
    for (int i = 0; i < sprites.length; i++) {
      int finalI = i;
      KeyFrame keyFrame =
          new KeyFrame(
              frameDuration.multiply(i + sprites.length),
              event -> imageView.setImage(sprites[sprites.length - finalI - 1]));
      mobMovementAnimation.getKeyFrames().add(keyFrame);
    }

    // Set the cycle count to indefinite to keep the animation playing
    mobMovementAnimation.setCycleCount(Timeline.INDEFINITE);

    // Play the animation
    mobMovementAnimation.play();

    // Update the timeline stored in the map
    mobMovementAnimations.put(mobSprite, mobMovementAnimation);

    // Return the timeline object
    return mobMovementAnimation;
  }

  /**
   * Method that handles the removal of a mob based on the RemoveMobData object.
   *
   * @param data the RemoveMobData object
   */
  public void removeMob(RemoveMobData data) {
    Type mobType = data.mobType();
    int score = data.score();
    int x = data.x();
    int y = data.y();
    int lives = data.lives();
    int mobId = data.id(); // Get the ID from the data

    // Get the map of sprites for the mob type
    Map<Integer, Sprite> mobSpriteMap = mobSprites.get(mobType);

    if (mobSpriteMap != null) {
      // Get the sprite with the matching ID
      Sprite mobSprite = mobSpriteMap.get(mobId);
      if (mobSprite != null) { // Check if the sprite exists
        if (lives > 0) {
          // Play the flickering animation between the normal sprite and the flickering sprite
          playFlickeringAnimation(mobType, mobId);
        } else {
          playFlickeringAnimation(mobType, mobId);
          // Stop the existing movement animation of the mob
          stopMobMovementAnimation(mobSprite);

          Timeline delayTimeline1 =
              new Timeline(
                  new KeyFrame(
                      Duration.seconds(1),
                      event -> {
                        audioManager.play(AudioSample.MOB_DEATH);
                      }));
          delayTimeline1.play();

          // Delay the removal of the mob sprite by 2 seconds and play spawnScoreNumbers
          Timeline delayTimeline =
              new Timeline(
                  new KeyFrame(
                      Duration.seconds(2),
                      event -> {
                        spawnScoreNumbers(score, x, y + 50);
                        removeOldMobSprite(mobType, mobId); // Pass the ID to the remove method
                      }));
          delayTimeline.play();
        }
      }
    }
  }

  /**
   * Method that stops the movement animation of the mob sprite.
   *
   * @param mobSprite the mob sprite
   */
  private void stopMobMovementAnimation(Sprite mobSprite) {
    Timeline mobMovementAnimation = mobMovementAnimations.get(mobSprite);
    if (mobMovementAnimation != null) {
      mobMovementAnimation.stop();
      mobMovementAnimations.remove(mobSprite);
    }
  }

  /**
   * Method that plays the flickering animation for the mob sprite.
   *
   * @param mobType the mob type
   * @param mobId the mob ID
   */
  private void playFlickeringAnimation(Type mobType, int mobId) {
    Map<Integer, Sprite> mobSpriteMap = mobSprites.get(mobType);
    if (mobSpriteMap != null) {
      // Get the specific sprite with the matching ID
      Sprite mobSprite = mobSpriteMap.get(mobId);
      if (mobSprite != null) { // Check if the sprite exists
        ImageView mobImageView = mobSprite.getImageView();
        Timeline flickeringAnimation =
            new Timeline(
                new KeyFrame(Duration.seconds(0.050), event -> mobImageView.setVisible(false)),
                new KeyFrame(Duration.seconds(0.1), event -> mobImageView.setVisible(true)));
        flickeringAnimation.setCycleCount(30); // Repeat indefinitely
        flickeringAnimation.play();
      }
    }
  }

  /**
   * method that removes the old mob sprite based on the mob type and mob ID.
   *
   * @param mobType the mob type
   * @param mobId the mob ID
   */
  private void removeOldMobSprite(Type mobType, int mobId) {
    Map<Integer, Sprite> mobSpriteMap = mobSprites.get(mobType);
    if (mobSpriteMap != null) {
      // Get the specific sprite with the matching ID
      Sprite mobSprite = mobSpriteMap.get(mobId);
      if (mobSprite != null) { // Check if the sprite exists
        ImageView mobImageView = mobSprite.getImageView();
        anchorPane.getChildren().remove(mobImageView);

        // Remove the Timeline associated with the Sprite from the map
        mobMovementAnimations.remove(mobSprite);

        // Remove the Sprite from the map
        mobSpriteMap.remove(mobId);
      }
    }
  }

  /**
   * Method that updates the player lives based on the PlayerLivesUpdateData object.
   *
   * @param data the PlayerLivesUpdateData object
   */
  public void updatePlayerLives(PlayerLivesUpdateData data) {
    int lives = data.lives();

    if (lives == 0) {
      playPlayerDeathAnimation(player, getPlayerHitSprites());
      Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3.5), e -> removePlayer()));
      timeline.play();
      soundTimer.stop();
      audioManager.play(AudioSample.BOMBERMAN_DEATH);
    } else {
      playPlayerHitAnimation(player, getPlayerHitSprites());
      soundTimer.stop();
      audioManager.play(AudioSample.BOMBERMAN_DEATH);
    }
  }

  /**
   * Method that plays the player hit animation.
   *
   * @param imageView the image view
   * @param sprites the sprites
   */
  public void playPlayerHitAnimation(ImageView imageView, Image[] sprites) {

    // Clear existing key frames from playerMovementAnimation
    playerMovementAnimation.getKeyFrames().clear();

    playerHitAnimation = new Timeline();

    // Create keyframes for each sprite in the sequence
    for (int i = 0; i < sprites.length; i++) {
      final int index = i;
      KeyFrame keyFrame =
          new KeyFrame(
              Duration.seconds(i * 0.6), // Change duration as needed for faster animation
              event -> imageView.setImage(sprites[index]));
      playerHitAnimation.getKeyFrames().add(keyFrame);
    }

    // Set the cycle count to indefinite to  keep the animation playing
    playerHitAnimation.setCycleCount(1);

    // Play the hit animation
    playerHitAnimation.setOnFinished(event -> playFlickerAnimation(imageView));
    playerHitAnimation.play();
  }

  /**
   * Method that plays the player death animation.
   *
   * @param imageView the image view
   * @param sprites the sprites
   */
  private void playPlayerDeathAnimation(ImageView imageView, Image[] sprites) {
    // Clear existing key frames from playerMovementAnimation
    playerMovementAnimation.getKeyFrames().clear();

    playerHitAnimation = new Timeline();

    // Create keyframes for each sprite in the sequence
    for (int i = 0; i < sprites.length; i++) {
      final int index = i;
      KeyFrame keyFrame =
          new KeyFrame(
              Duration.seconds(i * 0.6), // Change duration as needed for faster animation
              event -> imageView.setImage(sprites[index]));
      playerHitAnimation.getKeyFrames().add(keyFrame);
    }

    // Set the cycle count to indefinite to  keep the animation playing
    playerHitAnimation.setCycleCount(1);

    playerHitAnimation.play();
  }

  /**
   * Method that plays the flicker animation for an imageview sprite.
   *
   * @param imageView the image view
   */
  private void playFlickerAnimation(ImageView imageView) {
    // Create a Timeline for flickering
    Timeline flickerAnimation =
        new Timeline(
            new KeyFrame(Duration.seconds(0.1), event -> imageView.setVisible(false)),
            new KeyFrame(Duration.seconds(0.2), event -> imageView.setVisible(true)));

    // Set the cycle count to play the flicker animation for 2 seconds
    flickerAnimation.setCycleCount(20); // Adjust duration and cycle count as needed

    // Play the flicker animation
    flickerAnimation.setOnFinished(finishedEvent -> imageView.setVisible(true));
    flickerAnimation.play();
  }

  /**
   * Gets the sprites for the player hit animation.
   *
   * @return the player hit sprites
   */
  private static Image[] getPlayerHitSprites() {
    return new Image[] {
      Entities.PLAYER_HIT_0.getImage(),
      Entities.PLAYER_HIT_1.getImage(),
      Entities.PLAYER_HIT_2.getImage(),
      Entities.PLAYER_HIT_3.getImage(),
    };
  }

  /** removes the player from the anchorPane. */
  private void removePlayer() {
    anchorPane.getChildren().remove(player);
  }

  /**
   * Method that spawns the power-up at the specified position.
   *
   * @param data the PowerUpSpawnData object
   */
  public void spawnPowerUp(PowerUpSpawnData data) {
    PowerUpType powerUpType = data.powerUpType();
    ImageView powerUpImageView = new ImageView();
    Image[] powerUpFrames;

    if (powerUpType == PowerUpType.BOMB_UP) {
      powerUpFrames =
          new Image[] {Tiles.POWERUP_BOMBUP_0.getImage(), Tiles.POWERUP_BOMBUP_1.getImage()};
    } else if (powerUpType == PowerUpType.SKATE) {
      powerUpFrames =
          new Image[] {Tiles.POWERUP_SKATE_0.getImage(), Tiles.POWERUP_SKATE_1.getImage()};
    } else {
      powerUpFrames =
          new Image[] {Tiles.POWERUP_ICESCREAM_0.getImage(), Tiles.POWERUP_ICESCREAM_1.getImage()};
    }

    powerUpImageView.setFitWidth(48);
    powerUpImageView.setFitHeight(48);
    powerUpImageView.setX(data.x());
    powerUpImageView.setY(data.y() + Y_OFFSET);

    animatePowerUp(powerUpImageView, powerUpFrames[0], powerUpFrames[1]);

    // Ensure bomb is drawn under the player
    anchorPane.getChildren().remove(player);
    anchorPane.getChildren().add(player);

    // Add the ImageView to the anchorPane after animating it
    anchorPane.getChildren().add(powerUpImageView);
  }

  /**
   * Method that animates the power-up sprite.
   *
   * @param imageView the image view
   * @param frame0 the frame 0
   * @param frame1 the frame 1
   */
  private void animatePowerUp(ImageView imageView, Image frame0, Image frame1) {
    Timeline timeline =
        new Timeline(
            new KeyFrame(Duration.seconds(0), e -> imageView.setImage(frame0)),
            new KeyFrame(Duration.seconds(0.1), e -> imageView.setImage(frame1)),
            new KeyFrame(Duration.seconds(0.2), e -> imageView.setImage(frame0)),
            new KeyFrame(Duration.seconds(0.3), e -> imageView.setImage(frame1)));
    timeline.setCycleCount(Timeline.INDEFINITE); // Repeat the animation indefinitely
    timeline.play();
  }

  /**
   * Method that applies the power-up at the specified position.
   *
   * @param data the PowerUpApplicationData object
   */
  public void applyPowerUp(PowerUpApplicationData data) {
    int x = data.x();
    int y = data.y();

    audioManager.play(AudioSample.POWER_UP_PICKUP);

    removePowerUp(x, y + Y_OFFSET);
  }

  /**
   * Method that removes the power-up at the specified position.
   *
   * @param x the x coordinate
   * @param y the y coordinate
   */
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

  /**
   * Method that despawns the power-up at the specified position.
   *
   * @param data the PowerUpDespawnData object
   */
  public void despawnPowerUp(PowerUpDespawnData data) {

    int x = data.x();
    int y = data.y();

    removePowerUp(x, y + Y_OFFSET);
  }

  /** Method that loads the font sprites for the score and lives. */
  private void loadFontSprites() {
    fontSprites = new ImageView[10]; // Assuming you have sprites for digits 0-9

    // Load font sprites for digits 0-9
    for (int i = 0; i < 10; i++) {
      fontSprites[i] = new ImageView(Font.values()[i].getImage());
      fontSprites[i].setFitWidth(8 * 2.8); // Adjust width based on sprite size
      fontSprites[i].setFitHeight(14 * 2.8); // Adjust height based on sprite size
    }
  }

  /**
   * Method that updates the player score based on the PlayerScoreUpdateData object.
   *
   * @param data the PlayerScoreUpdateData object
   */
  public void updateScore(PlayerScoreUpdateData data) {

    int score = data.score();

    this.playerScore = score;

    // Calculate the number of digits in the score
    int numDigits = String.valueOf(score).length();

    // Define the starting X position for drawing sprites
    double startX = 293.0; // You may adjust this based on your layout

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

  /**
   * Utility method to get the digit at a specific position in a number
   *
   * @param number the number
   * @param position the position
   */
  private int getDigitAt(int number, int position) {
    return (int) (number / Math.pow(10, position)) % 10;
  }

  /** Method that clears the existing score sprites. */
  private void clearScoreSprites() {
    anchorPane
        .getChildren()
        .removeIf(node -> node instanceof ImageView && Arrays.asList(fontSprites).contains(node));
  }

  /**
   * Method that draws the player score based on the PlayerScoreUpdateData object.
   *
   * @param data the PlayerScoreUpdateData object
   */
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

  /**
   * Method that handles the exit spawn at the specified position.
   *
   * @param data the ExitTileSpawnData object
   */
  public void handleExitSpawn(ExitTileSpawnData data) {

    int x = data.exitTileX();
    int y = data.exitTileY();

    Image exitTileImage0 = Tiles.EXIT_TILE_0.getImage();
    Image exitTileImage1 = Tiles.EXIT_TILE_1.getImage();

    exitTileImageView = new ImageView(exitTileImage0);
    exitTileImageView.setFitWidth(48);
    exitTileImageView.setFitHeight(48);
    exitTileImageView.setX(x);
    exitTileImageView.setY(y + Y_OFFSET);

    List<Node> mobSpritesTemp = new ArrayList<>();
    for (Map<Integer, Sprite> mobSpriteMap : mobSprites.values()) {
      for (Sprite mobSprite : mobSpriteMap.values()) {
        mobSpritesTemp.add(mobSprite.getImageView());
      }
    }
    anchorPane.getChildren().removeAll(mobSpritesTemp);

    // Add the ImageView to the anchor pane
    anchorPane.getChildren().add(exitTileImageView);

    // Ensure bomb is drawn under the player
    anchorPane.getChildren().remove(player);
    anchorPane.getChildren().add(player);

    anchorPane.getChildren().addAll(mobSpritesTemp);

    // Create the animation for the exit tile
    Timeline exitTileAnimation =
        new Timeline(
            new KeyFrame(
                Duration.seconds(0.1),
                event -> {
                  exitTileImageView.setImage(exitTileImage0);
                }),
            new KeyFrame(
                Duration.seconds(0.2),
                event -> {
                  exitTileImageView.setImage(exitTileImage1);
                }));

    // Set the cycle count to 2 to repeat the animation twice
    exitTileAnimation.setCycleCount(900);

    // Start the animation
    exitTileAnimation.play();
  }

  /** Method that loads the font sprites for the score on mob kill. */
  private void loadScoreFontSprites() {
    scoreFontSprites = new ImageView[5];

    // Load font sprites for digits 0-9
    for (int i = 0; i < 5; i++) {
      scoreFontSprites[i] = new ImageView(FontMobKill.values()[i].getImage());
      scoreFontSprites[i].setFitWidth(8 * 2.8); // Adjust width based on sprite size
      scoreFontSprites[i].setFitHeight(14 * 2.8); // Adjust height based on sprite size
    }
  }

  /** Method that loads the font sprites for the score on mob kill. */
  private void loadScoreFlickerFontSprites() {
    scoreFontFlickerSprites = new ImageView[5]; // Assuming you have sprites for digits 0-9

    // Load font sprites for digits 0-9
    for (int i = 0; i < 5; i++) {
      scoreFontFlickerSprites[i] = new ImageView(FontMobKillFlicker.values()[i].getImage());
      scoreFontFlickerSprites[i].setFitWidth(8 * 2.8); // Adjust width based on sprite size
      scoreFontFlickerSprites[i].setFitHeight(14 * 2.8); // Adjust height based on sprite size
    }
  }

  /**
   * Method that spawns the score numbers at the specified position.
   *
   * @param score the score
   * @param x the x coordinate
   * @param y the y coordinate
   */
  public void spawnScoreNumbers(int score, double x, double y) {
    String scoreString = String.valueOf(score);
    double startX = x;

    // Draw each digit of the score
    for (int i = 0; i < scoreString.length(); i++) {
      int digit = Character.getNumericValue(scoreString.charAt(i));
      if (digit >= 0 && digit <= 9) {
        ImageView digitView = new ImageView(scoreFontSprites[digit].getImage());
        digitView.setFitWidth(8 * 2.8);
        digitView.setFitHeight(44);
        digitView.setX(startX + (i * 10 * 2.8));
        digitView.setY(y);

        // Create a translation animation to move the digit up
        Timeline moveUpAnimation =
            new Timeline(
                new KeyFrame(Duration.ZERO, e -> digitView.setTranslateY(0)),
                new KeyFrame(Duration.seconds(1), e -> digitView.setTranslateY(-20)),
                new KeyFrame(Duration.seconds(2), e -> digitView.setTranslateY(-40)));

        // Create a timeline to flicker the digit between normal and flicker sprites
        Timeline flickerAnimation =
            new Timeline(
                new KeyFrame(
                    Duration.ZERO, e -> digitView.setImage(scoreFontSprites[digit].getImage())),
                new KeyFrame(
                    Duration.seconds(0.2),
                    e -> digitView.setImage(scoreFontFlickerSprites[digit].getImage())),
                new KeyFrame(
                    Duration.seconds(0.4),
                    e -> digitView.setImage(scoreFontSprites[digit].getImage())));
        flickerAnimation.setCycleCount(Timeline.INDEFINITE);

        // Play both animations together
        moveUpAnimation.play();
        flickerAnimation.play();

        anchorPane.getChildren().add(digitView);

        // Remove the digit from the anchorPane after 2 seconds
        Timeline removeAnimation =
            new Timeline(
                new KeyFrame(Duration.seconds(3), e -> anchorPane.getChildren().remove(digitView)));
        removeAnimation.play();
      }
    }
  }

  /** Method that loads the sprites for the destructible tiles animation. */
  private void loadDestructibleTileAnimation() {
    destructibleTileSprites =
        new Image[] {
          Tiles.DESTROYABLE.getImage(),
          Tiles.DESTROYABLE1.getImage(),
          Tiles.DESTROYABLE2.getImage(),
          Tiles.DESTROYABLE3.getImage()
        };
  }

  /**
   * Method tha plays the destructible tile animation for the specified imageview.
   *
   * @param imageView the image view
   */
  private void playDestructibleTileAnimation(ImageView imageView) {
    // Create a timeline to play the animation
    Timeline timeline = new Timeline();

    // Define the duration of each frame (adjust as needed)
    Duration frameDuration = Duration.seconds(0.2); // Set a shorter duration for faster animation

    // Add key frames to change the image at specific intervals
    for (int i = 0; i < destructibleTileSprites.length; i++) {
      int finalI = i;
      KeyFrame keyFrame =
          new KeyFrame(
              frameDuration.multiply(i), // Multiply frame duration by index for progressive timing
              event -> imageView.setImage(destructibleTileSprites[finalI]));
      timeline.getKeyFrames().add(keyFrame);
    }

    // Set cycle count to indefinite for continuous looping
    timeline.setCycleCount(Animation.INDEFINITE);

    // Play the timeline
    timeline.play();
  }

  /**
   * Method that plays the destructible tile animation for the specified imageview if under a not
   * destroyable tile.
   *
   * @param imageView the image view
   */
  private void playDestructibleUnderNonDestructibleAnimation(ImageView imageView) {
    Image[] destructibleUnderNonDestructibleSprites =
        new Image[] {
          Tiles.DESTROYABLE_UNDER_NON_DESTRUCTIBLE.getImage(),
          Tiles.DESTROYABLE_UNDER_NON_DESTRUCTIBLE1.getImage(),
          Tiles.DESTROYABLE_UNDER_NON_DESTRUCTIBLE2.getImage(),
          Tiles.DESTROYABLE_UNDER_NON_DESTRUCTIBLE3.getImage()
        };

    // Create a timeline for the animation
    Timeline timeline = new Timeline();

    // Create keyframes for each sprite in the sequence
    for (int i = 0; i < destructibleUnderNonDestructibleSprites.length; i++) {
      final int index = i;
      KeyFrame keyFrame =
          new KeyFrame(
              Duration.seconds(i * 0.2), // Change duration as needed for faster animation
              event -> imageView.setImage(destructibleUnderNonDestructibleSprites[index]));
      timeline.getKeyFrames().add(keyFrame);
    }

    // Set the cycle count to indefinite to keep the animation playing
    timeline.setCycleCount(Timeline.INDEFINITE);

    // Play the animation
    timeline.play();
  }

  /**
   * Method that checks if the tile above the current tile is destructible.
   *
   * @param currentIndex the current index
   * @param data the LoadMapData object
   * @return true if the tile above is destructible, false otherwise
   */
  private boolean isTileAboveDestructible(int currentIndex, LoadMapData data) {
    // Calculate the index of the tile above the current tile
    int aboveIndex = currentIndex - MAP_WIDTH;

    // Check if the calculated index is within the valid range
    if (aboveIndex >= 0) {
      // Get the tile at the calculated index
      Tiles tileAbove = data.matrix().get(aboveIndex);

      // Return true if the tile above is of type IMMOVABLE
      return tileAbove == Tiles.IMMOVABLE || tileAbove == Tiles.BORDER_HORIZONTAL;
    }
    return false;
  }

  /**
   * Method that gets the sprites for the puropen animation based on direction
   *
   * @param direction the direction
   * @return the puropen sprite images
   */
  private static Image[] getPuropenSpriteImages(Direction direction) {
    switch (direction) {
      case UP:
        return new Image[] {
          Entities.PUROPEN_UP_0.getImage(),
          Entities.PUROPEN_UP_1.getImage(),
          Entities.PUROPEN_UP_2.getImage(),
          Entities.PUROPEN_UP_3.getImage()
        };
      case DOWN:
        return new Image[] {
          Entities.PUROPEN.getImage(),
          Entities.PUROPEN_1.getImage(),
          Entities.PUROPEN_2.getImage(),
          Entities.PUROPEN_3.getImage()
        };
      case LEFT:
        return new Image[] {
          Entities.PUROPEN_LEFT_0.getImage(),
          Entities.PUROPEN_LEFT_1.getImage(),
          Entities.PUROPEN_LEFT_2.getImage(),
          Entities.PUROPEN_LEFT_3.getImage()
        };
      case RIGHT:
        return new Image[] {
          Entities.PUROPEN_RIGHT_0.getImage(),
          Entities.PUROPEN_RIGHT_1.getImage(),
          Entities.PUROPEN_RIGHT_2.getImage(),
          Entities.PUROPEN_RIGHT_3.getImage()
        };
      default:
        return new Image[] {};
    }
  }

  /**
   * Method that gets the sprites for the denkyun animation.
   *
   * @return the denkyun sprite images
   */
  private static Image[] getDenkyunSpriteImages() {
    return new Image[] {
      Entities.DENKYUN_0.getImage(),
      Entities.DENKYUN_1.getImage(),
      Entities.DENKYUN_2.getImage(),
      Entities.DENKYUN_3.getImage(),
      Entities.DENKYUN_4.getImage(),
      Entities.DENKYUN_5.getImage()
    };
  }

  /** Method that loads the clock sprites and animation. */
  private void loadClockSprites() {
    clockSprites = new ImageView[8];
    for (int i = 0; i < 8; i++) {
      clockSprites[i] = new ImageView(Clock.values()[i].getImage());
    }
  }

  /** Method that initializes the clock animation. */
  private void initializeClockAnimation() {
    clockAnimation = new Timeline();
    // Add keyframes to switch between clock sprites
    for (int i = 0; i < 8; i++) {
      final int index = i;
      KeyFrame keyFrame =
          new KeyFrame(
              Duration.seconds(i * 1),
              event -> clockImageView.setImage(clockSprites[index].getImage()));
      clockAnimation.getKeyFrames().add(keyFrame);
    }
    clockAnimation.setCycleCount(Timeline.INDEFINITE);
  }

  /** Method that draws the clock on the HUD. */
  private void drawClockOnHUD() {
    clockImageView = new ImageView();
    clockImageView.setFitWidth(16 * 2.8); // Adjust width based on sprite size
    clockImageView.setFitHeight(25 * 2.8); // Adjust height based on sprite size
    // Set clock position to center of scoreboard
    double x = (720 - 382);
    double y = 24;
    clockImageView.setX(x);
    clockImageView.setY(y);
    // Start clock animation
    clockAnimation.play();
    // Add clock to HUD AnchorPane
    anchorPane.getChildren().add(clockImageView);
  }

  /**
   * Method that respawns the Denkyun mob at the specified coordinates.
   *
   * @param data the DenkyunRespawnData object
   */
  public void spawnDenkyunAtCoordinates(DenkyunRespawnData data) {
    double x = data.x();
    double y = data.y();

    initializeDenkyun(x, y, data.id());
  }

  /**
   * Method that initializes the Denkyun mob at the specified coordinates.
   *
   * @param x the x coordinate
   * @param y the y coordinate
   * @param denkyunId the denkyun ID
   */
  private void initializeDenkyun(double x, double y, int denkyunId) {
    ImageView denkyunImageView = new ImageView(Entities.VOID.getImage());
    denkyunImageView.setFitWidth(48);
    denkyunImageView.setFitHeight(80);

    AnchorPane.setTopAnchor(denkyunImageView, (double) Y_OFFSET - 35);
    AnchorPane.setLeftAnchor(denkyunImageView, 0.0);

    anchorPane.getChildren().add(denkyunImageView); // Add Denkyun ImageView to the anchorPane

    denkyunImageView.setY(y - 96);

    // Create a new Sprite for the Denkyun with the passed ID
    Sprite denkyunSprite = new Sprite(denkyunImageView, denkyunId, 0, 0);

    Map<Integer, Sprite> denkyunMap = mobSprites.get(Type.DENKYUN);
    if (denkyunMap == null) {
      denkyunMap = new HashMap<>();
      mobSprites.put(Type.DENKYUN, denkyunMap);
    }
    denkyunMap.put(denkyunId, denkyunSprite);

    // Create a new timeline for the Denkyun mob
    Timeline mobMovementAnimation = new Timeline();

    // Add the timeline to the map using the Sprite as the key
    mobMovementAnimations.put(denkyunSprite, mobMovementAnimation);

    playFlickeringAnimation(Type.DENKYUN, denkyunId); // Start flickering animation after pause
  }

  /**
   * Method that handles the level clear event.
   *
   * @param data the LevelUpdateData object
   */
  public void levelClear(LevelUpdateData data) {

    spriteIdCounter = 0;

    playerMovementAnimation.stop(); //
    playerMovementAnimation.getKeyFrames().clear(); // clear animation
    playerMovementAnimation = null; //

    playSpinAnimation();

    stopSoundtrack();

    soundTimer.stop();

    audioManager.play(AudioSample.STAGE_CLEAR);
  }

  /**
   * Method that gets the sprites for the player spin animation.
   *
   * @return the player spin sprites
   */
  private Image[] getSpinSprites() {
    return new Image[] {
      Entities.PLAYER_SPIN_0.getImage(),
      Entities.PLAYER_SPIN_0.getImage(),
      Entities.PLAYER_SPIN_0.getImage(),
      Entities.PLAYER_SPIN_1.getImage(),
      Entities.PLAYER_SPIN_2.getImage(),
      Entities.PLAYER_SPIN_3.getImage(),
      Entities.PLAYER_SPIN_4.getImage(),
      Entities.PLAYER_SPIN_5.getImage(),
      Entities.PLAYER_SPIN_6.getImage(),
      Entities.PLAYER_SPIN_7.getImage(),
      Entities.PLAYER_SPIN_8.getImage(),
      Entities.VOID.getImage()
    };
  }

  /** Method that plays the spin animation for the player. */
  private void playSpinAnimation() {

    ImageView playerImageView = player;

    Image[] spinSprites = getSpinSprites();
    Duration frameDuration = Duration.seconds(0.2);

    Timeline spinAnimation = new Timeline();

    // Add key frames to change the image at specific intervals for spin animation
    for (int i = 0; i < spinSprites.length; i++) {
      int finalI = i;
      KeyFrame keyFrame =
          new KeyFrame(
              frameDuration.multiply(i), event -> playerImageView.setImage(spinSprites[finalI]));
      spinAnimation.getKeyFrames().add(keyFrame);
    }

    // Set the cycle count to play the animation once
    spinAnimation.setCycleCount(1);

    // Play the spin animation
    spinAnimation.play();
  }
}
