package org.jbomberman.view;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.*;

public class AudioManager {

  private static AudioManager instance;
  private Clip soundtrackClip; // Separate clip for the soundtrack

  public static AudioManager getInstance() {
    if (instance == null) instance = new AudioManager();
    return instance;
  }

  private AudioManager() {}

  public void play(AudioSample audioSample) {
    try {
      System.out.println(audioSample);
      String filename = audioSample.getFilename();
      InputStream in = new BufferedInputStream(new FileInputStream(filename));
      AudioInputStream audioIn = AudioSystem.getAudioInputStream(in);

      // Specify a standard audio format
      AudioFormat baseFormat = audioIn.getFormat();
      AudioFormat targetFormat =
          new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 1, 2, 44100, false);
      AudioInputStream audioStream = AudioSystem.getAudioInputStream(targetFormat, audioIn);

      DataLine.Info info = new DataLine.Info(Clip.class, targetFormat);
      Clip clip = (Clip) AudioSystem.getLine(info);
      clip.open(audioStream);
      clip.start();

      if (audioSample == AudioSample.SOUNDTRACK) {
        resetSoundtrackClip();
        soundtrackClip = clip;
      }

    } catch (FileNotFoundException e1) {
      e1.printStackTrace();
    } catch (IOException e1) {
      e1.printStackTrace();
    } catch (UnsupportedAudioFileException e1) {
      e1.printStackTrace();
    } catch (LineUnavailableException e1) {
      e1.printStackTrace();
    }
  }

  public void stop(AudioSample audioSample) {
    // Stop the specific clip corresponding to the given audio sample
    if (audioSample == AudioSample.SOUNDTRACK
        && soundtrackClip != null
        && soundtrackClip.isRunning()) {
      soundtrackClip.stop();
      reinitializeSoundtrackClip();
    }
    // You can add more conditions to handle stopping other specific clips if needed
  }

  private void resetSoundtrackClip() {
    if (soundtrackClip != null) {
      soundtrackClip.flush(); // Flush any pending data to reset the clip
      soundtrackClip.setMicrosecondPosition(0); // Rewind the clip to the beginning
    }
  }

  public void reinitializeSoundtrackClip() {
    // Reinitialize the soundtrackClip with the audio data
    try {
      String filename = AudioSample.SOUNDTRACK.getFilename();
      InputStream in = new BufferedInputStream(new FileInputStream(filename));
      AudioInputStream audioIn = AudioSystem.getAudioInputStream(in);

      // Specify a standard audio format
      AudioFormat baseFormat = audioIn.getFormat();
      AudioFormat targetFormat =
          new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 1, 2, 44100, false);
      AudioInputStream audioStream = AudioSystem.getAudioInputStream(targetFormat, audioIn);

      DataLine.Info info = new DataLine.Info(Clip.class, targetFormat);
      soundtrackClip = (Clip) AudioSystem.getLine(info);
      soundtrackClip.open(audioStream);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (UnsupportedAudioFileException e) {
      e.printStackTrace();
    } catch (LineUnavailableException e) {
      e.printStackTrace();
    }
  }
}
