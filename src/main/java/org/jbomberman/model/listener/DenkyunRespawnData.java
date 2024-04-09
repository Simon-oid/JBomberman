package org.jbomberman.model.listener;

import org.jbomberman.model.entita.Type;

/**
 * The denkyun respawn data class
 *
 * @param type The type of the package
 * @param mobType The type of the mob
 * @param x The x-coordinate of the mob
 * @param y The y-coordinate of the mob
 * @param id The id of the mob
 */
public record DenkyunRespawnData(PackageType type, Type mobType, int x, int y, int id)
    implements PackageData {}
