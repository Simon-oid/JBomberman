package org.jbomberman.model.listener;

/**
 * The tile destruction data class
 *
 * @param type The type of the package
 * @param tileIndex The index of the tile
 */
public record TileDestructionData(PackageType type, int tileIndex) implements PackageData {}
