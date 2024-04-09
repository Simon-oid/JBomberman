package org.jbomberman.model.listener;

import org.jbomberman.model.entita.Direction;
import org.jbomberman.model.entita.Type;

/**
 * The mob movement data class
 *
 * @param type
 * @param mobType
 * @param id
 * @param xStep
 * @param yStep
 * @param delta
 * @param oldX
 * @param oldY
 * @param puropenDirection
 */
public record MobMovementData(
    PackageType type,
    Type mobType,
    int id,
    int xStep,
    int yStep,
    double delta,
    int oldX,
    int oldY,
    Direction puropenDirection)
    implements PackageData {}
