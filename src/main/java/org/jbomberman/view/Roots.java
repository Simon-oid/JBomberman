package org.jbomberman.view;

import lombok.Getter;

@Getter
public enum Roots {
  MENU("scenes/Menu.fxml"),
  PLAYER_SELECTION("scenes/PlayerSelection.fxml"),
  LEVEL_SELECTION("scenes/LevelSelection.fxml"),
  GAME_OVER("scenes/GameOver.fxml"),
  YOU_WIN("scenes/YouWin.fxml"),
  LEADERBOARD("scenes/Leaderboard.fxml");

  private final String resourcePath;

  Roots(String resourcePath) {
    this.resourcePath = resourcePath;
  }
}
