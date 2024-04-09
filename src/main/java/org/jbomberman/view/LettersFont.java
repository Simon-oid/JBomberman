package org.jbomberman.view;

import java.io.InputStream;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Getter;

public enum LettersFont {
  SPRITE_A(LettersFont.class.getResourceAsStream("font/A_bomberman_font.png")),
  SPRITE_B(LettersFont.class.getResourceAsStream("font/B_bomberman_font.png")),
  SPRITE_C(LettersFont.class.getResourceAsStream("font/C_bomberman_font.png")),
  SPRITE_D(LettersFont.class.getResourceAsStream("font/D_bomberman_font.png")),
  SPRITE_E(LettersFont.class.getResourceAsStream("font/E_bomberman_font.png")),
  SPRITE_F(LettersFont.class.getResourceAsStream("font/F_bomberman_font.png")),
  SPRITE_G(LettersFont.class.getResourceAsStream("font/G_bomberman_font.png")),
  SPRITE_H(LettersFont.class.getResourceAsStream("font/H_bomberman_font.png")),
  SPRITE_I(LettersFont.class.getResourceAsStream("font/I_bomberman_font.png")),
  SPRITE_J(LettersFont.class.getResourceAsStream("font/J_bomberman_font.png")),
  SPRITE_K(LettersFont.class.getResourceAsStream("font/K_bomberman_font.png")),
  SPRITE_L(LettersFont.class.getResourceAsStream("font/L_bomberman_font.png")),
  SPRITE_M(LettersFont.class.getResourceAsStream("font/M_bomberman_font.png")),
  SPRITE_N(LettersFont.class.getResourceAsStream("font/N_bomberman_font.png")),
  SPRITE_O(LettersFont.class.getResourceAsStream("font/O_bomberman_font.png")),
  SPRITE_P(LettersFont.class.getResourceAsStream("font/P_bomberman_font.png")),
  SPRITE_Q(LettersFont.class.getResourceAsStream("font/Q_bomberman_font.png")),
  SPRITE_R(LettersFont.class.getResourceAsStream("font/R_bomberman_font.png")),
  SPRITE_S(LettersFont.class.getResourceAsStream("font/S_bomberman_font.png")),
  SPRITE_T(LettersFont.class.getResourceAsStream("font/T_bomberman_font.png")),
  SPRITE_U(LettersFont.class.getResourceAsStream("font/U_bomberman_font.png")),
  SPRITE_V(LettersFont.class.getResourceAsStream("font/V_bomberman_font.png")),
  SPRITE_W(LettersFont.class.getResourceAsStream("font/W_bomberman_font.png")),
  SPRITE_X(LettersFont.class.getResourceAsStream("font/X_bomberman_font.png")),
  SPRITE_Y(LettersFont.class.getResourceAsStream("font/Y_bomberman_font.png")),
  SPRITE_Z(LettersFont.class.getResourceAsStream("font/Z_bomberman_font.png")),
  SPRITE_0(LettersFont.class.getResourceAsStream("font/0_bomberman_font.png")),
  SPRITE_1(LettersFont.class.getResourceAsStream("font/1_bomberman_font.png")),
  SPRITE_2(LettersFont.class.getResourceAsStream("font/2_bomberman_font.png")),
  SPRITE_3(LettersFont.class.getResourceAsStream("font/3_bomberman_font.png")),
  SPRITE_4(LettersFont.class.getResourceAsStream("font/4_bomberman_font.png")),
  SPRITE_5(LettersFont.class.getResourceAsStream("font/5_bomberman_font.png")),
  SPRITE_6(LettersFont.class.getResourceAsStream("font/6_bomberman_font.png")),
  SPRITE_7(LettersFont.class.getResourceAsStream("font/7_bomberman_font.png")),
  SPRITE_8(LettersFont.class.getResourceAsStream("font/8_bomberman_font.png")),
  SPRITE_9(LettersFont.class.getResourceAsStream("font/9_bomberman_font.png")),
  SPRITE_COLON(LettersFont.class.getResourceAsStream("font/colon_bomberman_font.png"));

  /** The image view of the letters font */
  @Getter private final ImageView imageView;

  /**
   * The letters font constructor
   *
   * @param imageStream The image stream of the letters font
   */
  LettersFont(InputStream imageStream) {
    Image image = new Image(imageStream);
    this.imageView = new ImageView(image);
    this.imageView.setFitWidth(11); // Set the width of the ImageView
    this.imageView.setFitHeight(16); // Set the height of the ImageView
  }
}
