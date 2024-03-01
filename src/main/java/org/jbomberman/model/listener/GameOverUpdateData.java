package org.jbomberman.model.listener;

public record GameOverUpdateData(PackageType type, int score) implements PackageData {}
