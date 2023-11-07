package org.jbomberman.model.powerups;

import org.jbomberman.model.entita.Player;

public class SkatePowerUp extends PowerUp
{

    public SkatePowerUp()
    {
        super(PowerUpType.SKATE);
    }

    @Override
    public void applyPowerUp(Player player)
    {
        // Implement logic to increase the player's movement speed
        player.increaseMovementSpeed();
    }
}