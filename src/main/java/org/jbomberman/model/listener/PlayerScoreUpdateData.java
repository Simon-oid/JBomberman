package org.jbomberman.model.listener;

public record PlayerScoreUpdateData(PackageType type, int score) implements PackageData {}
