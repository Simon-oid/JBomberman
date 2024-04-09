package org.jbomberman.controller;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.jbomberman.model.entita.Entity;
import org.jbomberman.model.entita.Player;
import org.jbomberman.model.powerups.PowerUp;

@Getter
public class PowerUpManager {

  /** The instance variable is used to keep track of the PowerUpManager instance. */
  private static PowerUpManager instance;

  /** The powerUps variable is used to keep track of the powerUps. */
  private List<PowerUp> powerUps;

  /** The following constructor is used to create a new instance of the PowerUpManager class. */
  private PowerUpManager() {
    powerUps = new ArrayList<>();
  }

  /**
   * The getInstance method is used to get the instance of the PowerUpManager class. This method is
   * used to ensure that only one instance of the PowerUpManager class is created.
   *
   * @return The instance of the PowerUpManager class.
   */
  public static PowerUpManager getInstance() {
    if (instance == null) {
      instance = new PowerUpManager();
    }
    return instance;
  }

  /**
   * The spawnPowerUp method is used to spawn a power up. This method is called when a power up is
   * spawned.
   *
   * @param powerUp The PowerUp object that contains the power up.
   */
  public void spawnPowerUp(PowerUp powerUp) {
    powerUps.add(powerUp);
  }

  /**
   * The removePowerUp method is used to remove a power up. This method is called when a power up is
   * removed.
   *
   * @param powerUp The PowerUp object that contains the power up.
   */
  public void removePowerUp(PowerUp powerUp) {
    powerUps.remove(powerUp);
  }

  /**
   * The applyPowerUpEffect method is used to apply the power up effect. This method is called when
   * the power up effect is applied.
   *
   * @param entity The Entity object that contains the entity.
   * @param powerUp The PowerUp object that contains the power up.
   */
  public static void applyPowerUpEffect(Entity entity, PowerUp powerUp) {
    if (entity instanceof Player player) {
      powerUp.applyPowerUp(player);
    }
  }
}
