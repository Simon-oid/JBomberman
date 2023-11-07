package org.jbomberman.model.listener;

public record TileDestructionData(PackageType type, int tileIndex) implements PackageData
{
}
