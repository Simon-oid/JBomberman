package org.jbomberman.model.listener;

import org.jbomberman.view.Tiles;

import java.util.List;

public record LoadMapData(PackageType type, List<Tiles> matrix) implements PackageData
{
}
