package org.jbomberman.view;

import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class PixelateEffect {

  public static Image pixelate(Image sourceImage, int pixelSize) {
    int width = (int) Math.ceil(sourceImage.getWidth() / pixelSize) * pixelSize;
    int height = (int) Math.ceil(sourceImage.getHeight() / pixelSize) * pixelSize;

    WritableImage outputImage = new WritableImage(width, height);
    double scaleX = (double) width / sourceImage.getWidth();
    double scaleY = (double) height / sourceImage.getHeight();

    for (int x = 0; x < width; x += pixelSize) {
      for (int y = 0; y < height; y += pixelSize) {
        double avgR = 0;
        double avgG = 0;
        double avgB = 0;

        int samples = 0;

        for (int sx = 0; sx < pixelSize; sx++) {
          for (int sy = 0; sy < pixelSize; sy++) {
            if (x + sx < sourceImage.getWidth() && y + sy < sourceImage.getHeight()) {
              Color color = sourceImage.getPixelReader().getColor(x + sx, y + sy);
              avgR += color.getRed();
              avgG += color.getGreen();
              avgB += color.getBlue();
              samples++;
            }
          }
        }

        avgR /= samples;
        avgG /= samples;
        avgB /= samples;

        Color averageColor = Color.color(avgR, avgG, avgB);
        outputImage.getPixelWriter().setColor(x, y, averageColor);
      }
    }

    // Apply the Gaussian blur effect to the output image
    WritableImage blurredImage = new WritableImage(width, height);
    PixelWriter blurPixelWriter = blurredImage.getPixelWriter();
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        Color color = outputImage.getPixelReader().getColor(x, y);
        double blurredR = 0;
        double blurredG = 0;
        double blurredB = 0;

        int blurSamples = 0;

        for (int k = -3; k <= 3; k++) {
          for (int j = -3; j <= 3; j++) {
            if (x + k >= 0 && x + k < width && y + j >= 0 && y + j < height) {
              Color sampleColor = outputImage.getPixelReader().getColor(x + k, y + j);
              blurredR += sampleColor.getRed();
              blurredG += sampleColor.getGreen();
              blurredB += sampleColor.getBlue();
              blurSamples++;
            }
          }
        }

        blurredR /= blurSamples;
        blurredG /= blurSamples;
        blurredB /= blurSamples;

        blurPixelWriter.setColor(x, y, new Color(blurredR, blurredG, blurredB, color.getOpacity()));
      }
    }
    return blurredImage;
  }

  public static Image pixelateImage(String imageUrl, int pixelSize) {
    Image image = new Image(imageUrl);
    return pixelate(image, pixelSize);
  }
}
