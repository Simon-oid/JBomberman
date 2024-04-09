package org.jbomberman.controller;

import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyEvent;
import lombok.Getter;
import lombok.Setter;
import org.jbomberman.model.map.Map;

@Getter
@Setter
public class KeyHandler {

  /** The following variables are used to keep track of the key presses for the player movement. */
  public boolean upPressed, downPressed, leftPressed, rightPressed;

  /**
   * isPlayerMoving is a boolean variable that is used to keep track of the player movement status.
   */
  private boolean isPlayerMoving = false;

  /**
   * The instance variable is used to keep track of the KeyHandler instance. This is used to ensure
   * that only one instance of the KeyHandler is created.
   */
  private static KeyHandler instance;

  /**
   * The animationTimer variable is used to keep track of the AnimationTimer instance. This is used
   * to ensure that only one instance of the AnimationTimer is created.
   */
  private AnimationTimer animationTimer;

  /** The paused variable is used to keep track of the paused status of the KeyHandler. */
  private boolean paused = false;

  /** The lastFrameTime variable is used to keep track of the last frame time. */
  private long lastFrameTime = -1;

  /**
   * The onkeyPressed method is used to handle the key pressed event. This method is called when a
   * key is pressed.
   *
   * @param e The KeyEvent object that contains the key pressed event.
   */
  public void onkeyPressed(KeyEvent e) {
    switch (e.getCode()) {
      case W -> upPressed = true;
      case S -> downPressed = true;
      case A -> leftPressed = true;
      case D -> rightPressed = true;
      case SPACE -> Map.getInstance().spawnBomb();

      default -> {
        /*ignored*/
      }
    }
    // Player pressed a movement key, so update movement status
    updateMovementStatus();
  }

  /**
   * The onkeyReleased method is used to handle the key released event. This method is called when a
   * key is released.
   *
   * @param e The KeyEvent object that contains the key released event.
   */
  public void onkeyReleased(KeyEvent e) {
    switch (e.getCode()) {
      case W -> upPressed = false;
      case S -> downPressed = false;
      case A -> leftPressed = false;
      case D -> rightPressed = false;
      default -> {
        /*ignored*/
      }
    }
    // Player pressed a movement key, so update movement status
    updateMovementStatus();
  }

  /**
   * The updateMovementStatus method is used to update the movement status of the player. This
   * method is called when a key is pressed or released.
   */
  private void updateMovementStatus() {
    isPlayerMoving = upPressed || downPressed || leftPressed || rightPressed;
  }

  /**
   * The getInstance method is used to get the KeyHandler instance. This method is used to ensure
   * that only one instance of the KeyHandler is created.
   *
   * @return The KeyHandler instance.
   */
  public static KeyHandler getInstance() {
    if (instance == null) {
      instance = new KeyHandler();
    }
    return instance;
  }

  /**
   * The startMovement method is used to start the player movement. This method is called when the
   * player starts moving.
   */
  public void startMovement() {
    if (animationTimer == null) createTimer();
    animationTimer.start();
  }

  /**
   * The createTimer method is used to create the AnimationTimer instance. This method is called
   * when the AnimationTimer instance is not created.
   */
  private void createTimer() {
    animationTimer =
        new AnimationTimer() {
          private long lastFrame = -1;

          @Override
          public void handle(long now) {

            if (lastFrame == -1) {
              lastFrame = now;
            }

            double delta = (now - lastFrame) / 1_000_000_000D;
            if (delta < 1.0 / 30) return;

            lastFrame = now;

            if (!isPlayerMoving) {
              // Player is not moving, send update to GameView through Map
              Map.getInstance().playerNotMovingUpdate(0, 0, delta);
            } else {
              int yStep =
                  (int)
                      (delta
                          * Map.getInstance().getPlayer().getSpeed()
                          * ((upPressed ? -1 : 0) + (downPressed ? 1 : 0)));
              int xStep =
                  (int)
                      (delta
                          * Map.getInstance().getPlayer().getSpeed()
                          * ((leftPressed ? -1 : 0) + (rightPressed ? 1 : 0)));
              if (xStep == 0 && yStep == 0) return;
              Map.getInstance().movePlayer(xStep, yStep, delta);
            }
          }
        };
  }

  /**
   * The getDelta method is used to get the delta time. This method is called to get the time
   * difference between the current frame and the last frame.
   *
   * @return The delta time.
   */
  public double getDelta() {
    if (lastFrameTime == -1) {
      lastFrameTime = System.nanoTime();
      return 0; // No delta for the first frame
    }

    long currentTime = System.nanoTime();
    double deltaSeconds = (currentTime - lastFrameTime) / 1_000_000_000.0;
    lastFrameTime = currentTime;

    return deltaSeconds;
  }

  /** The stopMovement method is used to stop the player movement. */
  public void stopKeyHandler() {
    if (animationTimer != null) {
      animationTimer.stop();
      animationTimer = null;
      paused = true;
    }
  }
}
