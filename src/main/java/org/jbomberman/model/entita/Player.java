package org.jbomberman.model.entita;


import javafx.geometry.Rectangle2D;
import lombok.Getter;
import lombok.Setter;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class Player extends Entity
{
    private int lives;

    private int speed;

    private int bombCount;

    private int score;

    private int explosionRadius;

    private int invincibilityDuration = 3; // In seconds

    public Player(int x, int y, int width, int height, int lives, Direction direction, int score)
    {
        super(x, y, width, height, direction);
        this.lives = lives;
        this.score = score;
        this.speed = 100;
        this.bombCount = 5;
        explosionRadius = 2;
        initHitBox();
    }

    @Override
    protected void initHitBox()
    {
        hitBox = new Rectangle2D(x, y, width, height); //8, 16, 32, 32
    }

    @Override
    public void updateHitBox(int newX, int newY)
    {
        hitBox = new Rectangle2D(newX, newY, width, height);
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
        return hitBox;
    }


    public void move(int xStep, int yStep)
    {
        int newX = getX() + xStep;
        int newY = getY() + yStep;


        setX(newX);
        setY(newY);
        updateHitBox(newX, newY);

    }

    public void increaseMovementSpeed()
    {
        speed += 100;
    }

    public void incrementBombCount()
    {
        bombCount += 1;
    }

    public void incrementIceCream()
    {
        lives += 4;
        score += 50000;
    }

    public void decreaseLives()
    {
        lives -= 1;
    }

    // Method to handle player taking damage from a mob
    public void takeDamage()
    {
        if (isVulnerable)
        {
            isVulnerable = false; // Player is no longer vulnerable
            decreaseLives();
            // Set a timer to make the player vulnerable again after invincibilityDuration
            Timer timer = new Timer();
            timer.schedule(new TimerTask()
            {
                @Override
                public void run()
                {
                    isVulnerable = true; // Make the player vulnerable again
                }
            }, invincibilityDuration);
        }
    }


    public void decrementBombCount()
    {
        bombCount -= 1;
    }
}
