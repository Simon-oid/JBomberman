package org.jbomberman.model.listener;

public record BombExplosionData(PackageType type, int explosionX, int explosionY, int[] ranges) implements PackageData
{
}
