package org.jbomberman.model.listener;

import org.jbomberman.model.entita.Type;

public record RemoveMobData(PackageType type, Type mobType, int score, int x, int y)
    implements PackageData {}
