package AppPackage;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class MainClass {

    FileInputStream fileis;
    BufferedInputStream buffis;

    public Player player;
    public long pause_location;
    public long song_total_length;
    public String file_location;

    public void Stop() {
        if (player != null) {
            player.close();

            pause_location = 0;
            song_total_length = 0;
            //MP3PlayerGUI.Display.setText(" ");
        }
    }

    public void Pause() {
        if (player != null) {
            try {
                pause_location = fileis.available();
                player.close();
            } catch (IOException ex) {}
        }
    }

    public void Play(String path) {
        try {
            fileis = new FileInputStream(path);
            buffis = new BufferedInputStream(fileis);

            player = new Player(buffis);

            song_total_length = fileis.available();

            file_location = path + "";
        } catch (FileNotFoundException | JavaLayerException ex) {}
        catch (IOException ex) {}

        new Thread() {  //Seperate thread to play music

            public void run() {
                try {
                    player.play();
                } catch (JavaLayerException ex) {}
            }
        }.start();
    }

    public void Resume() {
        try {
            fileis = new FileInputStream(file_location);
            buffis = new BufferedInputStream(fileis);
            player = new Player(buffis);
            fileis.skip(song_total_length - pause_location);
            
        }
        catch (JavaLayerException ex) {}
        catch (FileNotFoundException ex) {}
        catch (IOException ex) {}
        new Thread() {  //Seperate thread to play music

            public void run() {
                try {
                    player.play();
                    if (player.isComplete() && MP3PlayerGUI.count == 1) {
                        Play(file_location);
                    }
                } catch (JavaLayerException ex) {}
            }
        }.start();
    }
}
