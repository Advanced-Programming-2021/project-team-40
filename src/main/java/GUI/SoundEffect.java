package GUI;

import javafx.scene.media.MediaPlayer;

import java.util.ArrayList;

public interface SoundEffect {
    ArrayList<MediaPlayer> soundEffects = new ArrayList<>();
    void playSoundEffect(String effectName);
}
