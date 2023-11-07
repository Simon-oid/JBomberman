package org.jbomberman.model.tiles;

import org.jbomberman.controller.PowerUpManager;
import org.jbomberman.model.powerups.PowerUp;
import org.jbomberman.model.powerups.PowerUpFactory;

public class DestroyableTile extends Tile
{

    public DestroyableTile(int x, int y)
    {
        super(TileType.DESTROYABLE, true, x, y);
    }

    public void destroy()
    {
        // Add logic here to handle tile destruction

        // Attempt to spawn a power-up
        //Map.getInstance().updateMapAfterDestruction(getX(), getY());

        PowerUp spawnedPowerUp = PowerUpFactory.createRandomPowerUp();
        if (spawnedPowerUp != null)
        {
            spawnedPowerUp.setX(getX()); // Set the power-up's X-coordinate
            spawnedPowerUp.setY(getY()); // Set the power-up's Y-coordinate
            PowerUpManager.getInstance().spawnPowerUp(spawnedPowerUp); // Add to PowerUpManager
        }
    }


}
