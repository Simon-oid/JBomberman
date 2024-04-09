package org.jbomberman.model.listener;

/**
 * The exit tile spawn data class
 *
 * @param type The type of the package
 * @param exitTileX The x-coordinate of the exit tile
 * @param exitTileY The y-coordinate of the exit tile
 */
public record ExitTileSpawnData(PackageType type, int exitTileX, int exitTileY)
    implements PackageData {}
