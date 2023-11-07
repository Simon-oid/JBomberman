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


//    protected boolean isValidMove(int newX, int newY)
//    {
//        // Check if new coordinates are within bounds of the game map
//        boolean withinBounds = newX >= 0 && newX < Map.WIDTH && newY >= 0 && newY < Map.HEIGHT;
//
//        if (withinBounds)
//        {
//            // Calculate the corners of the solid area at the new coordinates
//
//            int solidRight = (int) (newX + hitBox.getWidth());
//
//            int solidBottom = (int) (newY + hitBox.getHeight());
//
//            // Check if all corners of the solid area are inside a block of grass
//            boolean allCornersInGrass = Map.getInstance().isValidPosition(newX, newY)
//                    && Map.getInstance().isValidPosition(solidRight, newY)
//                    && Map.getInstance().isValidPosition(newX, solidBottom)
//                    && Map.getInstance().isValidPosition(solidRight, solidBottom);
//
//            // Check if there are no other entities at the new coordinates
//            // Check if the solid area collides with another entity
//            boolean noEntityCollision = true;
//            for (Entity entity : Map.getInstance().getEntities())
//            {
//                if (entity != this)
//                {
//                    entity.getHitBox().contains(hitBox);
//                    noEntityCollision = false;
//                }
//            }
//
//            return allCornersInGrass && noEntityCollision;
//        }
//
//        return false;
//
//
//    }

    public abstract Rectangle2D getHitBox();
}



