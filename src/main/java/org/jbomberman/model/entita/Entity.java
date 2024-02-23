package org.jbomberman.model.entita;

import javafx.geometry.Rectangle2D;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Entity
{
    // variabili utilizzate nel player, monstri ecc.

    protected int x;
    protected int y;

    private int deltaX;
    private int deltaY;

    private int speed;

    Direction direction;

    protected int width;
    protected int height;

    protected Rectangle2D hitBox;

    protected boolean isVulnerable;


    protected Entity(int x, int y, int width, int height, Direction direction)
    {

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.direction = direction;
        speed = 400;
        this.isVulnerable = true;
        initHitBox();

    }

    protected abstract void initHitBox();

    public abstract void updateHitBox(int newX, int newY);

    public abstract void spawn();


    public abstract Rectangle2D getHitBox();
}



