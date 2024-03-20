package org.jbomberman.model.listener;

public record LevelUpdateData(PackageType type, int currentLevel) implements PackageData {}
