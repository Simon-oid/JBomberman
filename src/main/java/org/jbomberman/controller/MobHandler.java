package org.jbomberman.controller;

import javafx.animation.AnimationTimer;
import org.jbomberman.model.entita.Mob;
import org.jbomberman.model.map.Map;

public class MobHandler {
  private AnimationTimer animationTimer;
  private static MobHandler instance;

  private MobHandler() {}

  public static MobHandler getInstance() {
    if (instance == null) {
      instance = new MobHandler();
    }
    return instance;
  }

  public void startMobMovement() {
    if (animationTimer == null) createMobTimer();
    animationTimer.start();
  }

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

            // System.out.println(delta);

            lastFrame = now;

            // Iterate through the mobs and move them
            for (Mob mob : Map.getInstance().getMobs()) {
              // Calculate xStep and yStep based on mob's direction and speed
              int yStep = (int) (delta * mob.getSpeed() * mob.getDirection().getY());
              int xStep = (int) (delta * mob.getSpeed() * mob.getDirection().getX());
              // System.out.println(yStep);
              // System.out.println(mob.getDirection().getY());
              // System.out.println(mob.getSpeed());

              // Call moveMob() in Map class to initiate mob movements
              Map.getInstance().moveMob(mob, xStep, yStep, delta);
            }
          }
        };
  }

  public void stopMobTimer() {
    animationTimer.stop();
  }
}
