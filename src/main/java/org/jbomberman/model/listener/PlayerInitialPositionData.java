package org.jbomberman.model.listener;

public record PlayerInitialPositionData(PackageType type, int initialX, int initialY)
    implements PackageData {}
