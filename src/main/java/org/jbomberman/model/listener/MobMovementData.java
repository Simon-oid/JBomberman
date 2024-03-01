package org.jbomberman.model.listener;

import org.jbomberman.model.entita.Direction;
import org.jbomberman.model.entita.Type;

public record MobMovementData(
    PackageType type,
    Type mobType,
    int xStep,
    int yStep,
    double delta,
    int oldX,
    int oldY,
    Direction puropenDirection)
    implements PackageData {}
