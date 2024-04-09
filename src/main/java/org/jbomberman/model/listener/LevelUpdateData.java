package org.jbomberman.model.listener;

/**
 * The level update data class
 *
 * @param type The type of the package
 * @param currentLevel The current level
 */
public record LevelUpdateData(PackageType type, int currentLevel) implements PackageData {}
