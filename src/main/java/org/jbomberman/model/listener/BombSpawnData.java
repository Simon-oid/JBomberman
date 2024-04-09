package org.jbomberman.model.listener;

/**
 * The bomb spawn data class
 *
 * @param type The type of the package
 * @param bombX The x-coordinate of the bomb
 * @param bombY The y-coordinate of the bomb
 */
public record BombSpawnData(PackageType type, int bombX, int bombY) implements PackageData {}
