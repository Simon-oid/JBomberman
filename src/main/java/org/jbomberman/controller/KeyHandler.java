package org.jbomberman.controller;

import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyEvent;
import lombok.Getter;
import lombok.Setter;
import org.jbomberman.model.map.Map;

@Getter
@Setter
public class KeyHandler {

  public boolean upPressed, downPressed, leftPressed, rightPressed;

  private boolean isPlayerMoving = false; // Track player movement status

  private static KeyHandler instance;

  private AnimationTimer animationTimer;

  private boolean paused = false;

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

  // Update player movement status based on key presses
  private void updateMovementStatus() {
    isPlayerMoving = upPressed || downPressed || leftPressed || rightPressed;
  }

  public static KeyHandler getInstance() {
    if (instance == null) {
      instance = new KeyHandler();
    }
    return instance;
  }

  public void startMovement() {
    if (animationTimer == null) createTimer();
    animationTimer.start();
  }

  // TODO: fixare la roba che riguarda il last frame come per il mob handler
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

  public void pauseKeyHandler() {
    if (!paused && animationTimer != null) {
      animationTimer.stop();
      paused = true;
    }
  }

  public void resumeKeyHandler() {
    if (paused && animationTimer != null) {
      animationTimer.start();
      paused = false;
    }
  }
}
