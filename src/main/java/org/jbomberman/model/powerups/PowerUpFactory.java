package org.jbomberman.model.powerups;

import java.util.Random;

public class PowerUpFactory
{

    private static final double DEFAULT_SPAWN_CHANCE = 0.2; // Default spawn chance

    private static double bombUpSpawnChance = DEFAULT_SPAWN_CHANCE;
    private static double skateSpawnChance = DEFAULT_SPAWN_CHANCE;

    public static void setPowerUpSpawnChance(double spawnChance)
    {
        bombUpSpawnChance = spawnChance;
        skateSpawnChance = spawnChance;
    }

    public static PowerUp createRandomPowerUp()
    {
        Random random = new Random();
        double randomValue = random.nextDouble();

        if (randomValue <= bombUpSpawnChance)
        {
            return new BombUpPowerUp();
        } else if (randomValue <= skateSpawnChance)
        {
            return new SkatePowerUp();
        } else
        {
            return null; // No power-up spawned
        }
    }
}