package org.jbomberman.model.listener;

public record GameOverUpdateData(PackageType type, int score, int lives) implements PackageData {}
