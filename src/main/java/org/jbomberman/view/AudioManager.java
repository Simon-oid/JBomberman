package org.jbomberman.view;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.*;

public class AudioManager {

  /** The AudioManager instance */
  private static AudioManager instance;

  /** The soundtrack clip */
  private Clip soundtrackClip; // Separate clip for the soundtrack

  /** The collection of all Clip instances */
  private List<Clip> clips; // Collection of all Clip instances

  /**
   * Get the AudioManager instance
   *
   * @return The AudioManager instance
   */
  public static AudioManager getInstance() {
    if (instance == null) instance = new AudioManager();
    return instance;
  }

  /** The AudioManager constructor */
  private AudioManager() {
    clips = new ArrayList<>();
  }

  /** Stop all audio clips */
  public void stopAll() {
    for (Clip clip : clips) {
      if (clip != null && clip.isRunning()) {
        clip.stop();
      }
    }
  }

  /**
   * Play the audio sample
   *
   * @param audioSample The audio sample to play
   */
  public void play(AudioSample audioSample) {
    try {
      String filename = audioSample.getFilename();
      InputStream in = new BufferedInputStream(getClass().getResourceAsStream(filename));
      AudioInputStream audioIn = AudioSystem.getAudioInputStream(in);

      // Specify a standard audio format
      AudioFormat baseFormat = audioIn.getFormat();
      AudioFormat targetFormat =
          new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 1, 2, 44100, false);
      AudioInputStream audioStream = AudioSystem.getAudioInputStream(targetFormat, audioIn);

      DataLine.Info info = new DataLine.Info(Clip.class, targetFormat);
      Clip clip = (Clip) AudioSystem.getLine(info);
      clip.open(audioStream);

      // Check if the audio sample is one of the soundtracks that should be looped
      if (audioSample == AudioSample.MAIN_MENU
          || audioSample == AudioSample.PLAYER_SELECTION
          || audioSample == AudioSample.LEADERBOARD) {
        clip.loop(Clip.LOOP_CONTINUOUSLY); // Loop the soundtrack continuously
      } else {
        clip.start(); // Start the clip normally for other audio samples
      }

      // Add the clip to the collection of clips
      clips.add(clip);

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

  /**
   * Stop the audio sample
   *
   * @param audioSample The audio sample to stop
   */
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

  /** Resets the soundtrack clip to the beginning */
  private void resetSoundtrackClip() {
    if (soundtrackClip != null) {
      soundtrackClip.flush(); // Flush any pending data to reset the clip
      soundtrackClip.setMicrosecondPosition(0); // Rewind the clip to the beginning
    }
  }

  /** Reinitialize the soundtrack clip with the audio data */
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
