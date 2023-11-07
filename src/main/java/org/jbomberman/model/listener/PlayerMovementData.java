package org.jbomberman.model.listener;

public record PlayerMovementData(PackageType type, int xStep, int yStep, double delta, int oldX,
                                 int oldY) implements PackageData
{
}
