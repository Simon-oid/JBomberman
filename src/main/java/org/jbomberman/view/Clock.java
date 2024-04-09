package org.jbomberman.view;

import java.io.InputStream;
import javafx.scene.image.Image;
import lombok.Getter;

/** The clock enum */
public enum Clock {
  HUD_CLOCK_0(HUD.class.getResourceAsStream("hud/hud_clock_0.png")),
  HUD_CLOCK_1(HUD.class.getResourceAsStream("hud/hud_clock_1.png")),
  HUD_CLOCK_2(HUD.class.getResourceAsStream("hud/hud_clock_2.png")),
  HUD_CLOCK_3(HUD.class.getResourceAsStream("hud/hud_clock_3.png")),
  HUD_CLOCK_4(HUD.class.getResourceAsStream("hud/hud_clock_4.png")),
  HUD_CLOCK_5(HUD.class.getResourceAsStream("hud/hud_clock_5.png")),
  HUD_CLOCK_6(HUD.class.getResourceAsStream("hud/hud_clock_6.png")),
  HUD_CLOCK_7(HUD.class.getResourceAsStream("hud/hud_clock_7.png"));

  /** The image of the clock */
  @Getter private final Image image;

  /**
   * The clock constructor
   *
   * @param image The image of the clock
   */
  Clock(InputStream image) {
    this.image = new Image(image, 15, 22, true, false);
  }
}
