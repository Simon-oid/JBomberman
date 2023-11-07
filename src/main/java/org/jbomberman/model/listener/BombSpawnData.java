package org.jbomberman.model.listener;

public record BombSpawnData(PackageType type, int bombX, int bombY) implements PackageData
{
}
