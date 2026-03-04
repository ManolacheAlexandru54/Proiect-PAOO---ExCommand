package PaooGame.Audio;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

/// Clasa facuta pentru a reda muzica de fundal și efecte sonore
/// AudioPlayer este responsabilă pentru gestionarea redării sunetelor în joc.
/// Aceasta include redarea muzicii de fundal în buclă și redarea efectelor sonore.
/// De asemenea, permite controlul volumului atât pentru muzica de fundal, cât și pentru efectele sonore.
/// Această clasă utilizează API-ul Java Sound pentru a încărca și reda fișiere audio.

public class AudioPlayer {
    private Clip clip;
    private float soundVolume = 1.0f;

    // Metodă pentru redarea buclată a muzicii de fundal
    public void playBackgroundMusic(String soundPath) {
        try {
            // Încarcă sunetul din resurse
            URL soundURL = getClass().getResource(soundPath);
            if (soundURL == null) {
                throw new IllegalArgumentException("File not found: " + soundPath);
            }
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundURL);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            // Muzica se redă în buclă continuu
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

   public void playSoundEffect(String soundPath) {
       if (soundVolume == 0.0f) {
           return; // Ignoră sunetul dacă volumul este 0
       }

       try {
           URL soundURL = getClass().getResource(soundPath);
           if (soundURL == null) {
               throw new IllegalArgumentException("File not found: " + soundPath);
           }
           AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundURL);
           Clip effectClip = AudioSystem.getClip();
           effectClip.open(audioInputStream);

           // Setează volumul efectului sonor
           setSoundEffectVolume(soundVolume, effectClip);

           effectClip.start();
           effectClip.addLineListener(event -> {
               if (event.getType() == LineEvent.Type.STOP) {
                   effectClip.close();
               }
           });
       } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
           e.printStackTrace();
       }
   }

    // Metodă pentru oprirea muzicii de fundal
    public void stopBackgroundMusic() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    // Reia muzica de fundal de unde a rămas
    public void resumeBackgroundMusic() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
   public void setVolume(float volume) {
    if (clip != null && clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

        float min = gainControl.getMinimum();
        float max = gainControl.getMaximum();

        float gain;
        if (volume == 0.0f) {
            gain = min;
        } else {
            gain = min + (max - min) * volume;
        }

        gainControl.setValue(gain);

        // Verifică dacă muzica se redă; dacă nu, reia redarea
        if (!clip.isRunning()) {
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY); // Reia redarea în buclă
        }
    }
}

   public void setSoundEffectVolume(float volume, Clip effectClip) {
       if (effectClip != null && effectClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
           FloatControl gainControl = (FloatControl) effectClip.getControl(FloatControl.Type.MASTER_GAIN);
           float min = gainControl.getMinimum();
           float max = gainControl.getMaximum();
           float gain = min + (max - min) * volume;
           gainControl.setValue(gain);
       }
   }
}