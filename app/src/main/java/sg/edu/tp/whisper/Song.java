package sg.edu.tp.whisper;

import java.io.Serializable;
import java.util.ArrayList;

public class Song implements Serializable {
    private String id;
    private int imageIcon;
    private String title;
    private String artiste;
    private String fileLink;
    private ArrayList<Song> songList;

    public Song(String id, String title, String artiste, String fileLink, int imageIcon) {
        this.id = id;
        this.imageIcon = imageIcon;
        this.title = title;
        this.artiste = artiste;
        this.fileLink = fileLink;
    }

    String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFileLink() { return fileLink; }
    public void setFileLink(String fileLink) { this.fileLink = fileLink; }


    int getImageIcon() {
        return imageIcon;
    }
    public void setImageIcon(int imageIcon) { this.imageIcon = imageIcon; }

    String getTitle() {
        return title;
    }
    public void setTitle(String title) { this.title = title; }

    String getArtiste() {
        return artiste;
    }
    public void setArtiste(String artiste) { this.artiste = artiste; }

    public ArrayList<Song> getSongList() {
        return songList;
    }

    public void setSongList(ArrayList<Song> songList) {
        this.songList = songList;
    }
}

