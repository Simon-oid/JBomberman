package org.jbomberman.view;

import lombok.Getter;

/** The audio sample enum */
@Getter
public enum AudioSample {
  SOUNDTRACK("src/main/resources/org/jbomberman/view/audioSamples/soundtrack.wav"),
  STAGE_INTRO("src/main/resources/org/jbomberman/view/audioSamples/stage_intro_long.wav"),
  STAGE_CLEAR("src/main/resources/org/jbomberman/view/audioSamples/stage_clear.wav"),
  WALKING_1("src/main/resources/org/jbomberman/view/audioSamples/walking_1.wav"),
  BOMB_PLACEMENT("src/main/resources/org/jbomberman/view/audioSamples/place_bomb.wav"),
  BOMB_EXPLOSION("src/main/resources/org/jbomberman/view/audioSamples/bomb_explosion.wav"),
  MOB_DEATH("src/main/resources/org/jbomberman/view/audioSamples/enemy_dies.wav"),
  BOMBERMAN_DEATH("src/main/resources/org/jbomberman/view/audioSamples/bomberman_dies.wav"),
  POWER_UP_PICKUP("src/main/resources/org/jbomberman/view/audioSamples/item_get.wav"),
  ENDING("src/main/resources/org/jbomberman/view/audioSamples/ending.wav"),
  AUDIENCE("src/main/resources/org/jbomberman/view/audioSamples/audience.wav"),
  GAME_OVER("src/main/resources/org/jbomberman/view/audioSamples/gameOver.wav"),
  MAIN_MENU("src/main/resources/org/jbomberman/view/audioSamples/mainMenu_theme.wav"),
  PLAYER_SELECTION("src/main/resources/org/jbomberman/view/audioSamples/playerSelection_theme.wav"),
  LEADERBOARD("src/main/resources/org/jbomberman/view/audioSamples/leaderboard_theme.wav");

  /** The filename of the audio sample */
  private final String filename;

  /**
   * The audio sample constructor
   *
   * @param filename The filename of the audio sample
   */
  AudioSample(String filename) {
    this.filename = filename;
  }
}
