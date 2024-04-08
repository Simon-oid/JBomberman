package org.jbomberman.model.listener;

import org.jbomberman.model.entita.Type;

public record DenkyunRespawnData(PackageType type, Type mobType, int x, int y, int id)
    implements PackageData {}
