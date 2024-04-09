package org.jbomberman.view;

import lombok.Getter;

/** The roots enum */
@Getter
public enum Roots {
  MENU("scenes/Menu.fxml"),
  PLAYER_SELECTION("scenes/PlayerSelection.fxml"),
  LEVEL_SELECTION("scenes/LevelSelection.fxml"),
  GAME_OVER("scenes/GameOver.fxml"),
  YOU_WIN("scenes/YouWin.fxml"),
  LEADERBOARD("scenes/Leaderboard.fxml");

  /** The resource path */
  private final String resourcePath;

  /**
   * The roots constructor
   *
   * @param resourcePath The resource path
   */
  Roots(String resourcePath) {
    this.resourcePath = resourcePath;
  }
}
