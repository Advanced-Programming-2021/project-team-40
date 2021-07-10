package GUI;

import javafx.scene.media.MediaPlayer;

import java.util.ArrayList;

public interface Music {
    ArrayList<MediaPlayer> music = new ArrayList<>();
    void setMusic(String songName);
}
