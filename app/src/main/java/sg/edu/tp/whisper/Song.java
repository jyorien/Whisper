package sg.edu.tp.whisper;

public class Song {
    private String id;
    private int imageIcon;
    private String title;
    private String artiste;
    private String fileLink;
    private double songLength;

    public Song(String id, String title, String artiste, String fileLink, double songLength, int imageIcon) {
        this.id = id;
        this.imageIcon = imageIcon;
        this.title = title;
        this.artiste = artiste;
        this.fileLink = fileLink;
        this.songLength = songLength;
    }

    String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFileLink() { return fileLink; }
    public void setFileLink(String fileLink) { this.fileLink = fileLink; }

    public double getSongLength() { return songLength; }
    public void setSongLength(double songLength) { this.songLength = songLength; }

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
}

