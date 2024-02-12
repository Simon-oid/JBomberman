package org.jbomberman.model.listener;

import org.jbomberman.model.entita.Type;

public record RemoveMobData(PackageType type, Type mobType) implements PackageData {}
