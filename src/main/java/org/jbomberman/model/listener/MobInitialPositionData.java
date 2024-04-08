package org.jbomberman.model.listener;

import org.jbomberman.model.entita.Type;

public record MobInitialPositionData(
    PackageType type, Type mobType, int id, int initialX, int initialY) implements PackageData {}
