package org.jbomberman.view;

import lombok.Getter;

@Getter
public enum Roots {
    LEVEL_SELECTION("scenes/LevelSelection.fxml"),
    SETTINGS("scenes/Settings.fxml"),
    MENU("scenes/Menu.fxml"),
    CHI_LO_SA("scenes/ChiLoSa.fxml");

    private final String resourcePath;

    Roots(String resourcePath) {
        this.resourcePath = resourcePath;
    }

}