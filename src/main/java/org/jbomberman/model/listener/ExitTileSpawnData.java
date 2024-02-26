package org.jbomberman.model.listener;

public record ExitTileSpawnData(PackageType type, int exitTileX, int exitTileY)
    implements PackageData {}
