package org.jbomberman.view;

import javafx.animation.AnimationTimer;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class ScrollingBackground extends Pane {
  private static final int TILE_SIZE = 48;
  private static final int WIDTH = 900; // replace with actual width
  private static final int HEIGHT = 900; // replace with actual height
  private static final int SPEED = 1;
  private ImageView[][] tiles;

  public ScrollingBackground() {
    tiles = new ImageView[WIDTH / TILE_SIZE + 2][HEIGHT / TILE_SIZE + 2];

    for (int i = 0; i < tiles.length; i++) {
      for (int j = 0; j < tiles[i].length; j++) {
        tiles[i][j] = new ImageView(Tiles.BG_TILE.getImage());
        tiles[i][j].setFitWidth(TILE_SIZE); // Set the width of the ImageView
        tiles[i][j].setFitHeight(TILE_SIZE); // Set the height of the ImageView
        tiles[i][j].setX(i * TILE_SIZE);
        tiles[i][j].setY(j * TILE_SIZE);
        getChildren().add(tiles[i][j]);
      }
    }

    AnimationTimer timer =
        new AnimationTimer() {
          @Override
          public void handle(long now) {
            for (ImageView[] tileRow : tiles) {
              for (ImageView tile : tileRow) {
                tile.setX(tile.getX() - SPEED);
                tile.setY(tile.getY() - SPEED);

                if (tile.getX() + TILE_SIZE < 0) {
                  tile.setX(WIDTH);
                }

                if (tile.getY() + TILE_SIZE < 0) {
                  tile.setY(HEIGHT);
                }
              }
            }
          }
        };

    timer.start();
  }
}
