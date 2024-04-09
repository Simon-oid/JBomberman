package org.jbomberman.model.listener;

import org.jbomberman.model.entita.Type;

/**
 * The mob initial position data class
 *
 * @param type The type of the package
 * @param mobType The type of the mob
 * @param id The id of the mob
 * @param initialX The initial x-coordinate of the mob
 * @param initialY The initial y-coordinate of the mob
 */
public record MobInitialPositionData(
    PackageType type, Type mobType, int id, int initialX, int initialY) implements PackageData {}
