package org.jbomberman.model.listener;

/**
 * The game over update data class
 *
 * @param type The type of the package
 * @param score The score of the player
 * @param lives The lives of the player
 */
public record GameOverUpdateData(PackageType type, int score, int lives) implements PackageData {}
