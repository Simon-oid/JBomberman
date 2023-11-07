package org.jbomberman.view;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.util.Duration;
import org.jbomberman.model.listener.*;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameView
{

    private TilePane tilePane;
    private AnchorPane anchorPane;
    private ImageView player;

    public GameView()
    {
        player = new ImageView(Entities.PLAYER.getImage());
        player.setFitHeight(96);
        player.setFitWidth(48);

        tilePane = new TilePane();

        tilePane.setPrefRows(17);
        tilePane.setPrefColumns(13);


        for (int i = 1; i < 221; i++)
        {
            ImageView imageView = new ImageView();
            imageView.setFitHeight(48);
            imageView.setFitWidth(48);
            imageView.setSmooth(false);
            tilePane.getChildren().add(imageView);
        }
        anchorPane = new AnchorPane(tilePane, player);
    }

    public void loadMap(LoadMapData data)
    {
        int i = 0;
        for (Tiles tiles : data.matrix())
        {
            ((ImageView) tilePane.getChildren().get(i++)).setImage(tiles.getImage());
        }

    }

    public Parent getRoot()
    {
        return anchorPane;
    }

    public void movePlayer(PlayerMovementData data)
    {
        int xStep = data.xStep();
        int yStep = data.yStep();
        double delta = data.delta();

        int initialX = data.oldX();
        int initialY = data.oldY();

        TranslateTransition transition = new TranslateTransition(Duration.seconds(delta), player);

        // Set the initial positions as the fromX and fromY properties
        transition.setFromX(initialX);
        transition.setFromY(initialY);

        // Set the target positions using setToX and setToY
        transition.setToX(xStep);
        transition.setToY(yStep);

        transition.play();
    }

    public void spawnBomb(BombSpawnData data)
    {
        int bombX = data.bombX();
        int bombY = data.bombY();

        ImageView bombImageView = new ImageView(Entities.BOMB.getImage());

        bombImageView.setFitWidth(48);
        bombImageView.setFitHeight(48);

        bombImageView.setX(bombX);
        bombImageView.setY(bombY);

        // Add the bomb image view to the anchor pane
        anchorPane.getChildren().add(bombImageView);

    }

    public void explodeBomb(BombExplosionData data)
    {
        int explosionX = data.explosionX();
        int explosionY = data.explosionY();
        int[] ranges = data.ranges();

        // Remove the bomb image
        removeBombImage(explosionX, explosionY);

        // Iterate over the ranges and draw the explosions in each direction
        for (int r : ranges)
        {
            for (int i = 0; i <= r; i++)
            {
                // Draw the explosion sprites in the horizontal and vertical directions
                drawExplosionSprite(explosionX + i * 48, explosionY);
                drawExplosionSprite(explosionX - i * 48, explosionY);
                drawExplosionSprite(explosionX, explosionY + i * 48);
                drawExplosionSprite(explosionX, explosionY - i * 48);
            }
        }

        // Schedule a task to remove the explosion images after 2 seconds
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.schedule(() ->
                Platform.runLater(() ->
                        anchorPane.getChildren().removeIf(node -> node instanceof ImageView && ((ImageView) node).getImage() == Entities.EXPLOSION.getImage())), 2, TimeUnit.SECONDS);
    }


    private void drawExplosionSprite(int x, int y)
    {
        ImageView explosionImageView = new ImageView(Entities.EXPLOSION.getImage());
        explosionImageView.setFitWidth(48);
        explosionImageView.setFitHeight(48);
        explosionImageView.setX(x);
        explosionImageView.setY(y);

        anchorPane.getChildren().add(explosionImageView);
    }

    private void removeBombImage(int explosionX, int explosionY)
    {
        for (Node node : anchorPane.getChildren())
        {
            if (node instanceof ImageView && ((ImageView) node).getImage() == Entities.BOMB.getImage() && (Math.abs(((ImageView) node).getX() - explosionX) < 1 && Math.abs(((ImageView) node).getY() - explosionY) < 1))
            {
                anchorPane.getChildren().remove(node);
                break;

            }
        }

    }

    public void handleTileDestruction(TileDestructionData data)
    {
        int tile_index = data.tileIndex();

        if (tile_index >= 0 && tile_index < tilePane.getChildren().size())
        {
            // Replace the destroyed tile with a GRASS tile
            ImageView imageView = new ImageView(Tiles.GRASS.getImage());
            imageView.setFitHeight(48);
            imageView.setFitWidth(48);
            imageView.setSmooth(false);

            tilePane.getChildren().set(data.tileIndex(), imageView);
        }
    }
}
