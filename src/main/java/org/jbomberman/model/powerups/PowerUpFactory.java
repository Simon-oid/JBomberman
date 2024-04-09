package org.jbomberman.model.powerups;

import java.util.Random;

public class PowerUpFactory {

  /** The spawn chance of the bomb up power-up */
  private static double bombUpSpawnChance = 0.3; // 30% chance

  /** The spawn chance of the skate power-up */
  private static double skateSpawnChance = 0.7; // 70% chance

  /**
   * Set the spawn chance of the power-ups
   *
   * @param bombUpSpawnChance The spawn chance of the bomb up power-up
   * @param skateSpawnChance The spawn chance of the skate power-up
   */
  public static void setPowerUpSpawnChance(double bombUpSpawnChance, double skateSpawnChance) {
    PowerUpFactory.bombUpSpawnChance = bombUpSpawnChance;
    PowerUpFactory.skateSpawnChance = skateSpawnChance;
  }

  /**
   * Create a random power-up
   *
   * @return The random power-up
   */
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
