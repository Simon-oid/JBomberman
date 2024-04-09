package org.jbomberman.controller;

import javafx.animation.AnimationTimer;
import org.jbomberman.model.entita.Mob;
import org.jbomberman.model.map.Map;

public class MobHandler {
  /** */
  private AnimationTimer animationTimer;

  /** The instance variable is used to keep track of the MobHandler instance. */
  private static MobHandler instance;

  /** The following constructor is used to create a new instance of the MobHandler class. */
  private MobHandler() {}

  /**
   * The getInstance method is used to get the instance of the MobHandler class. This method is used
   * to ensure that only one instance of the MobHandler class is created.
   *
   * @return The instance of the MobHandler class.
   */
  public static MobHandler getInstance() {
    if (instance == null) {
      instance = new MobHandler();
    }
    return instance;
  }

  /**
   * The startMobMovement method is used to start the mob movement. This method is called when the
   * mob movement is started.
   */
  public void startMobMovement() {
    if (animationTimer == null) createMobTimer();
    animationTimer.start();
  }

  /**
   * The createMobTimer method is used to create the mob timer. This method is called when the mob
   * timer is created.
   */
  private void createMobTimer() {
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

            // Iterate through the mobs and move them
            for (Mob mob : Map.getInstance().getMobs()) {
              // Calculate xStep and yStep based on mob's direction and speed
              int yStep = (int) (delta * mob.getSpeed() * mob.getDirection().getY());
              int xStep = (int) (delta * mob.getSpeed() * mob.getDirection().getX());

              Map.getInstance().moveMob(mob, xStep, yStep, delta);
            }
          }
        };
  }

  /**
   * The stopMobTimer method is used to stop the mob timer. This method is called when the mob timer
   * is stopped.
   */
  public void stopMobTimer() {
    animationTimer.stop();
  }
}
