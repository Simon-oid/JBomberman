package org.jbomberman.model.listener;

/**
 * The player lives update data class
 *
 * @param type The type of the package
 * @param lives The lives of the player
 */
public record PlayerLivesUpdateData(PackageType type, int lives) implements PackageData {}
