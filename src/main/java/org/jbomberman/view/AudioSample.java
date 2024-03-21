package org.jbomberman.view;

import lombok.Getter;

@Getter
public enum AudioSample {
  SOUNDTRACK(
      "Z:/projects/Intellij/JBomberman/src/main/resources/org/jbomberman/view/audioSamples/soundtrack.wav"),
  STAGE_INTRO(
      "Z:/projects/Intellij/JBomberman/src/main/resources/org/jbomberman/view/audioSamples/stage_intro_long.wav"),
  STAGE_CLEAR(
      "Z:/projects/Intellij/JBomberman/src/main/resources/org/jbomberman/view/audioSamples/stage_clear.wav"),
  WALKING_1(
      "Z:/projects/Intellij/JBomberman/src/main/resources/org/jbomberman/view/audioSamples/walking_1.wav"),
  BOMB_PLACEMENT(
      "Z:/projects/Intellij/JBomberman/src/main/resources/org/jbomberman/view/audioSamples/place_bomb.wav"),
  BOMB_EXPLOSION(
      "Z:/projects/Intellij/JBomberman/src/main/resources/org/jbomberman/view/audioSamples/bomb_explosion.wav"),
  MOB_DEATH(
      "Z:/projects/Intellij/JBomberman/src/main/resources/org/jbomberman/view/audioSamples/enemy_dies.wav"),
  BOMBERMAN_DEATH(
      "Z:/projects/Intellij/JBomberman/src/main/resources/org/jbomberman/view/audioSamples/bomberman_dies.wav"),
  POWER_UP_PICKUP(
      "Z:/projects/Intellij/JBomberman/src/main/resources/org/jbomberman/view/audioSamples/item_get.wav"),
  ENDING(
      "Z:/projects/Intellij/JBomberman/src/main/resources/org/jbomberman/view/audioSamples/ending.wav");

  private final String filename;

  AudioSample(String filename) {
    this.filename = filename;
  }
}
