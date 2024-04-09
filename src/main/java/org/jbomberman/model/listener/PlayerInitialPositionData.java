package org.jbomberman.model.listener;

/**
 * The player initial position data class
 *
 * @param type The type of the package
 * @param initialX The initial x-coordinate of the player
 * @param initialY The initial y-coordinate of the player
 */
public record PlayerInitialPositionData(PackageType type, int initialX, int initialY)
    implements PackageData {}
