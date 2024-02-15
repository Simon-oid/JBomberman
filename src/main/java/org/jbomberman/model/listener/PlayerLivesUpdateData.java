package org.jbomberman.model.listener;

public record PlayerLivesUpdateData(PackageType type, int lives) implements PackageData {}
