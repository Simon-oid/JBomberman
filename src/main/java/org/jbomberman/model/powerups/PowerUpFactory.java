package org.jbomberman.model.powerups;

import java.util.Random;

public class PowerUpFactory {

  private static final double DEFAULT_SPAWN_CHANCE = 0.3; // Default spawn chance

  // Different spawn chances for each power-up type
  private static double bombUpSpawnChance = 0.3; // 30% chance
  private static double skateSpawnChance = 0.7; // 70% chance

  public static void setPowerUpSpawnChance(double bombUpSpawnChance, double skateSpawnChance) {
    PowerUpFactory.bombUpSpawnChance = bombUpSpawnChance;
    PowerUpFactory.skateSpawnChance = skateSpawnChance;
  }

  public static PowerUp createRandomPowerUp() {
    Random random = new Random();
    double totalChance = bombUpSpawnChance + skateSpawnChance; // Total spawn chances

    // Generate a random value between 0 (inclusive) and the total spawn chance (exclusive)
    double randomValue = random.nextDouble() * totalChance;

    if (randomValue < bombUpSpawnChance) {
      return new BombUpPowerUp();
    } else if (randomValue < (bombUpSpawnChance + skateSpawnChance)) {
      return new SkatePowerUp(); // Spawn SkatePowerUp
    } else {
      return null; // No power-up spawned
    }
  }
}
