package org.jbomberman.model.listener;

import org.jbomberman.model.powerups.PowerUpType;

public record PowerUpSpawnData(PackageType type, PowerUpType powerUpType, int x, int y)
    implements PackageData {}
