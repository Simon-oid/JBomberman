package org.jbomberman.model.listener;

import org.jbomberman.model.powerups.PowerUpType;

public record PowerUpApplicationData(PackageType type, PowerUpType powerUpType, int x, int y)
    implements PackageData {
  // You can add additional methods or fields if needed
}
