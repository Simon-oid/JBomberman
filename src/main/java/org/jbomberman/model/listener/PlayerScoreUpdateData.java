package org.jbomberman.model.listener;

/**
 * The player score update data class
 *
 * @param type The type of the package
 * @param score The score of the player
 */
public record PlayerScoreUpdateData(PackageType type, int score) implements PackageData {}
