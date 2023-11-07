package org.jbomberman.model.powerups;

import org.jbomberman.model.entita.Player;

public class IceCreamPowerUp extends PowerUp
{

    public IceCreamPowerUp()
    {
        super(PowerUpType.ICE_CREAM);
    }

    @Override
    public void applyPowerUp(Player player)
    {
        player.incrementIceCream();
    }
}
