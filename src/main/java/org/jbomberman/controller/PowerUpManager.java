package org.jbomberman.controller;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.jbomberman.model.entita.Entity;
import org.jbomberman.model.entita.Player;
import org.jbomberman.model.powerups.PowerUp;

@Getter
public class PowerUpManager
{

    private static PowerUpManager instance;

    private List<PowerUp> powerUps;

    private PowerUpManager()
    {
        powerUps = new ArrayList<>();
    }

    public static PowerUpManager getInstance()
    {
        if (instance == null)
        {
            instance = new PowerUpManager();
        }
        return instance;
    }

    public void spawnPowerUp(PowerUp powerUp)
    {
        powerUps.add(powerUp);
    }

    public void removePowerUp(PowerUp powerUp)
    {
        powerUps.remove(powerUp);
    }

    public static void applyPowerUpEffect(Entity entity, PowerUp powerUp)
    {
        if (entity instanceof Player)
        {
            Player player = (Player) entity;
            powerUp.applyPowerUp(player);
        }
    }
}
