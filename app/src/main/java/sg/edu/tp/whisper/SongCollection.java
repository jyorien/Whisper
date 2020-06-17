package sg.edu.tp.whisper;

import java.util.ArrayList;
import java.util.Random;

public class SongCollection {
    Random random = new Random();
    int randInt;
    SongCollection() {
        prepareTrackList();
        prepareTopTrackList();
    }

    public Song searchById(String id) {
        // temporary song object
        Song song = null;
        for (int index = 0; index < songs.size(); index++) {
            song = songs.get(index);
            if (song.getId().equals(id)) {
                return song;
            }
        }
        return song;
    }


    private ArrayList<Song> songs = new ArrayList<>();

    ArrayList<Song> getSongs() {
        return songs;
    }
    private void prepareTrackList() {
        Song theWayYouLookTonight = new Song("S1001", "The Way You Look Tonight", "Michael Buble",
                "a5b8972e764025020625bbf9c1c2bbb06e394a60?cid=2afe87a64b0042dabf51f37318616965",
                4.66, R.drawable.michael_buble_collection);

        Song billieJean = new Song("S1002", "Billie Jean", "Michael Jackson",
                "f504e6b8e037771318656394f532dede4f9bcaea?cid=2afe87a64b0042dabf51f37318616965",
                4.9, R.drawable.billie_jean);

        Song photograph = new Song("S1003", "Photograph", "Ed Sheeran",
                "097c7b735ceb410943cbd507a6e1dfda272fd8a8?cid=2afe87a64b0042dabf51f37318616965",
                4.32, R.drawable.photograph);

        Song endlessly = new Song("S1004", "Endlessly", "The Cab",
                "99e455921cf33b4242b463f778111cad251c1937?cid=2afe87a64b0042dabf51f37318616965",
                4.32, R.drawable.endlessly);

        Song wonderwall = new Song("S1005", "Wonderwall", "Oasis",
                "b4347e755d823bd300c7520a2ab7533a718a7c98?cid=2afe87a64b0042dabf51f37318616965",
                4.31, R.drawable.wonder_wall);

        Song lettingGo = new Song("S1006", "Letting Go", "One OK Rock",
                "1cf8d48884a0ce9cbf21bba281a0a8e2b43f0bb2?cid=2afe87a64b0042dabf51f37318616965",
                4.31, R.drawable.letting_go);

        Song theScientist = new Song("S1007", "The Scientist", "Coldplay",
                "95cb9df1b056d759920b5e85ad7f9aff0a390671?cid=2afe87a64b0042dabf51f37318616965",
                4.31, R.drawable.coldplay);

        Song boyWithLuv = new Song("S1008", "Boy With Luv", "BTS",
                "d16797fb391fb909f3c46454d7cf89a2718f8171?cid=2afe87a64b0042dabf51f37318616965", 3.83, R.drawable.persona);


        songs.add(theWayYouLookTonight);
        songs.add(billieJean);
        songs.add(photograph);
        songs.add(endlessly);
        songs.add(wonderwall);
        songs.add(lettingGo);
        songs.add(theScientist);
        songs.add(boyWithLuv);


    }


    public Song getNextSong(String currentSongId) {
        Song song = null;

        for(int i = 0; i < songs.size(); i++) {
            if (songs.get(songs.size() - 1).getId().equals(currentSongId)) {
                song = songs.get(0);
                break;
            }

            String tempSongId = songs.get(i).getId();
            if (tempSongId.equals(currentSongId)) {
                song = songs.get(i+1);
                break;
            }
        }
        return song;

    }

    public Song getPrevSong(String currentSongId) {
        Song song = null;

        for(int i = 0; i < songs.size(); i++) {
            if (songs.get(0).getId().equals(currentSongId)) {
               song = songs.get(songs.size() - 1);
                break;
            }
            String tempSongId = songs.get(i).getId();
            if (tempSongId.equals(currentSongId)) {
                song = songs.get(i-1);
                break;
            }
        }
        return song;

    }
    public Song getShuffleNextSong() {
        Song song = null;
        randInt = random.nextInt(songs.size());
        song = songs.get(randInt);
        return song;
    }


    private ArrayList<Song> topSongs = new ArrayList<>();

    ArrayList<Song> getTopSongs() {
        return topSongs;
    }
    private void prepareTopTrackList() {

        Song photograph = new Song("S1003", "Photograph", "Ed Sheeran",
                "097c7b735ceb410943cbd507a6e1dfda272fd8a8?cid=2afe87a64b0042dabf51f37318616965",
                4.32, R.drawable.photograph);

        Song endlessly = new Song("S1004", "Endlessly", "The Cab",
                "99e455921cf33b4242b463f778111cad251c1937?cid=2afe87a64b0042dabf51f37318616965",
                4.32, R.drawable.endlessly);

        Song lettingGo = new Song("S1006", "Letting Go", "One OK Rock",
                "1cf8d48884a0ce9cbf21bba281a0a8e2b43f0bb2?cid=2afe87a64b0042dabf51f37318616965",
                4.31, R.drawable.letting_go);

        Song theScientist = new Song("S1007", "The Scientist", "Coldplay",
                "95cb9df1b056d759920b5e85ad7f9aff0a390671?cid=2afe87a64b0042dabf51f37318616965",
                4.31, R.drawable.coldplay);

        topSongs.add(photograph);
        topSongs.add(endlessly);
        topSongs.add(lettingGo);
        topSongs.add(theScientist);

    }
}
