package org.jbomberman.view;

import lombok.Getter;

/** The audio sample enum */
@Getter
public enum AudioSample {
  SOUNDTRACK("audioSamples/soundtrack.wav"),
  STAGE_INTRO("audioSamples/stage_intro_long.wav"),
  STAGE_CLEAR("audioSamples/stage_clear.wav"),
  WALKING_1("audioSamples/walking_1.wav"),
  BOMB_PLACEMENT("audioSamples/place_bomb.wav"),
  BOMB_EXPLOSION("audioSamples/bomb_explosion.wav"),
  MOB_DEATH("audioSamples/enemy_dies.wav"),
  BOMBERMAN_DEATH("audioSamples/bomberman_dies.wav"),
  POWER_UP_PICKUP("audioSamples/item_get.wav"),
  ENDING("audioSamples/ending.wav"),
  AUDIENCE("audioSamples/audience.wav"),
  GAME_OVER("audioSamples/gameOver.wav"),
  MAIN_MENU("audioSamples/mainMenu_theme.wav"),
  PLAYER_SELECTION("audioSamples/playerSelection_theme.wav"),
  LEADERBOARD("audioSamples/leaderboard_theme.wav");

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
