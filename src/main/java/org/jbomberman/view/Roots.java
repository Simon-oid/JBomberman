package org.jbomberman.view;

import lombok.Getter;

@Getter
public enum Roots {
  LEVEL_SELECTION("scenes/LevelSelection.fxml"),
  GAME_OVER("scenes/GameOver.fxml"),
  MENU("scenes/Menu.fxml"),
  YOU_WIN("scenes/YouWin.fxml");

  private final String resourcePath;

  Roots(String resourcePath) {
    this.resourcePath = resourcePath;
  }
}
