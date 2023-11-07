package org.jbomberman.model.powerups;

import javafx.geometry.Rectangle2D;
import lombok.Getter;
import lombok.Setter;
import org.jbomberman.model.entita.Player;

public abstract class PowerUp
{
    @Setter
    @Getter
    private PowerUpType type;

    @Getter
    @Setter
    private Integer x; // X-coordinate
    @Getter
    @Setter
    private Integer y; // Y-coordinate

    @Getter
    @Setter
    private Rectangle2D powerUpHitBox = new Rectangle2D(0, 0, 48, 48);

    protected PowerUp(PowerUpType type)
    {
        this.type = type;
    }

    public abstract void applyPowerUp(Player player); // You can adjust the method signature based on your Player class

    
}

