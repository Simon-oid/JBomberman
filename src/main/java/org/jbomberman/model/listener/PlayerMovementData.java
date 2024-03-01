package org.jbomberman.model.listener;

import org.jbomberman.model.entita.Direction;

public record PlayerMovementData(
    PackageType type, int xStep, int yStep, double delta, int oldX, int oldY, Direction direction)
    implements PackageData {}
