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
      "Z:/projects/Intellij/JBomberman/src/main/resources/org/jbomberman/view/audioSamples/ending.wav"),
  AUDIENCE(
      "Z:/projects/Intellij/JBomberman/src/main/resources/org/jbomberman/view/audioSamples/audience.wav"),
  GAME_OVER(
      "Z:/projects/Intellij/JBomberman/src/main/resources/org/jbomberman/view/audioSamples/gameOver.wav"),
  MAIN_MENU(
      "Z:/projects/Intellij/JBomberman/src/main/resources/org/jbomberman/view/audioSamples/mainMenu_theme.wav"),
  PLAYER_SELECTION(
      "Z:/projects/Intellij/JBomberman/src/main/resources/org/jbomberman/view/audioSamples/playerSelection_theme.wav"),
  LEADERBOARD(
      "Z:/projects/Intellij/JBomberman/src/main/resources/org/jbomberman/view/audioSamples/leaderboard_theme.wav");

  private final String filename;

  AudioSample(String filename) {
    this.filename = filename;
  }
}
