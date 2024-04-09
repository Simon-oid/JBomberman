package org.jbomberman.model.listener;

/**
 * The bomb explosion data class
 *
 * @param type
 * @param explosionX
 * @param explosionY
 * @param ranges
 */
public record BombExplosionData(PackageType type, int explosionX, int explosionY, int[] ranges)
    implements PackageData {}
