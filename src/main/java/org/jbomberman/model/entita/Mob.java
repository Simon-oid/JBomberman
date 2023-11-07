package org.jbomberman.model.entita;

import javafx.geometry.Rectangle2D;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Mob extends Entity
{

    @Getter
    @Setter
    private Type type;

    @Getter
    @Setter
    private int invincibilityDuration = 2; // In seconds

    private Rectangle2D mobHitBox = new Rectangle2D(8, 16, 32, 32);


    public Mob(int x, int y, int width, int height, Type type, Direction direction)
    {
        super(x, y, width, height, direction);
        this.type = type;

    }

    @Override
    protected void initHitBox()
    {

    }

    @Override
    public void updateHitBox(int newX, int newY)
    {

    }

    @Override
    public void spawn()
    {
        setVulnerable(false); // Player becomes invincible
        // Schedule a task to make the player vulnerable again after the invincibility duration

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.schedule(() -> isVulnerable = true, invincibilityDuration, TimeUnit.SECONDS);
    }

    @Override
    public Rectangle2D getHitBox()
    {
        return mobHitBox;
    }


    public void move()
    {
        int oldX = getX();
        int oldY = getY();

        int newX = oldX + getDeltaX();
        int newY = oldY + getDeltaY();

        // Calculate new position based on deltas
        int updatedX = newX + getDeltaX();
        int updatedY = newY + getDeltaY();

        setX(newX);
        setY(newY);
        hitBox = new Rectangle2D(newX, newY, 32, 32);

        if (!isValidMove(updatedX, updatedY))
        {
            Direction newDirection = chooseRandomValidDirection();
            if (newDirection != null)
            {
                setDirection(newDirection);
            }
        }

    }

    private Direction chooseRandomValidDirection()
    {
        //TODO: rework metodo choose random direction
        return Direction.DOWN;
    }

    private boolean isValidMove(int updatedX, int updatedY)
    {
        //TODO: rework metodo
        return true;
    }
}

