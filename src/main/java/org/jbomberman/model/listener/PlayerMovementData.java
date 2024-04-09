package org.jbomberman.model.listener;

import org.jbomberman.model.entita.Direction;

/**
 * The player movement data class
 *
 * @param type The type of the package
 * @param xStep The x step of the player
 * @param yStep The y step of the player
 * @param delta The delta of the player
 * @param oldX The old x-coordinate of the player
 * @param oldY The old y-coordinate of the player
 * @param direction The direction of the player
 */
public record PlayerMovementData(
    PackageType type, int xStep, int yStep, double delta, int oldX, int oldY, Direction direction)
    implements PackageData {}
