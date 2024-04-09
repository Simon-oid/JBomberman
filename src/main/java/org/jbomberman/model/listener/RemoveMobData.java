package org.jbomberman.model.listener;

import org.jbomberman.model.entita.Type;

/**
 * The remove mob data class
 *
 * @param type The type of the package
 * @param mobType The type of the mob
 * @param id The id of the mob
 * @param lives The lives of the mob
 * @param score The score of the mob
 * @param x The x-coordinate of the mob
 * @param y The y-coordinate of the mob
 */
public record RemoveMobData(
    PackageType type, Type mobType, int id, int lives, int score, int x, int y)
    implements PackageData {}
