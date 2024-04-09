package org.jbomberman.model.listener;

import org.jbomberman.view.Tiles;

import java.util.List;

/**
 * The load map data class
 *
 * @param type The type of the package
 * @param matrix The matrix of the map
 */
public record LoadMapData(PackageType type, List<Tiles> matrix) implements PackageData {}
