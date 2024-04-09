package org.jbomberman.model.listener;

import org.jbomberman.model.powerups.PowerUpType;

/**
 * The power up despawn data class
 *
 * @param type The type of the package
 * @param powerUpType The type of the power up
 * @param x The x coordinate of the power up
 * @param y The y coordinate of the power up
 */
public record PowerUpDespawnData(PackageType type, PowerUpType powerUpType, int x, int y)
    implements PackageData {}
