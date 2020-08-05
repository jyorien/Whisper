package sg.edu.tp.whisper;


import android.app.Service;
import android.content.Intent;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;

import android.os.IBinder;
import android.widget.Toast;



import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class MusicService extends Service {

    int img = 0;
    private String url = "";
    private String artisteName = "";
    private String songId = "";
    private String songTitle = "";
    private String fileLink = "";
    ArrayList<Song> songList = new ArrayList<>();

    int musicPosition = 0;

    private final IBinder binder = new LocalBinder();

    private static MediaPlayer player;

    public MusicService() {
    }

    @Override
    public void onCreate() {
        player = new MediaPlayer();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(this, "Service started", Toast.LENGTH_LONG).show();
        //return super.onStartCommand(intent, flags, startId);
        fileLink = intent.getStringExtra("fileLink");
        url = "https://p.scdn.co/mp3-preview/" + fileLink;
        img = intent.getIntExtra("coverArt",0);
        songList = (ArrayList<Song>) intent.getSerializableExtra("songList");
        songTitle = intent.getStringExtra("songName");
        songId = intent.getStringExtra("songId");
        artisteName = intent.getStringExtra("artisteID");
        preparePlayer();
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer player) {
                player.start();
            }
        });

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (!player.isLooping())
                    playNext();
            }
        });
        Toast.makeText(this, "Now playing: " + songTitle + " by " + artisteName, Toast.LENGTH_SHORT).show();
        return Service.START_NOT_STICKY;
    }
    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service destroyed", Toast.LENGTH_LONG).show();
        player.stop();
        player.release();
        player = null;
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Toast.makeText(this, "bound", Toast.LENGTH_SHORT).show();
        return binder;
    }

    public class LocalBinder extends Binder {
        MusicService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MusicService.this;
        }
    }

    public void preparePlayer() {
            player.reset();

        try {
            player.setAudioAttributes(
                    new AudioAttributes
                            .Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build());
            player.setDataSource(this, Uri.parse(url));
            player.prepareAsync();
        } catch (IllegalArgumentException | IOException e) {
            e.printStackTrace();
        }
    }

    public void playNext() {
        Song nextSong = getNextSong(songId);
        if (nextSong != null) {
            songId = nextSong.getId();
            songTitle = nextSong.getTitle();
            artisteName = nextSong.getArtiste();
            url = "https://p.scdn.co/mp3-preview/" + nextSong.getFileLink();
            img = nextSong.getImageIcon();
        }
        preparePlayer();
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer player) {
                player.start();
            }
        });
    }

    public Song getNextSong(String currentSongId) {
        Song song = null;
        if (songList == null) {
            return song;
        }

        for(int i = 0; i < songList.size(); i++) {
            if (songList.get(songList.size() - 1).getId().equals(currentSongId)) {
                song = songList.get(0);
                break;
            }
            String tempSongId = songList.get(i).getId();
            if (tempSongId.equals(currentSongId)) {
                song = songList.get(i+1);
                break;
            }
        }
        return song;
    }

    public void playPrev() {
        Song prevSong = getPrevSong(songId);
        if (prevSong != null) {
            songId = prevSong.getId();
            songTitle = prevSong.getTitle();
            artisteName = prevSong.getArtiste();
            url = "https://p.scdn.co/mp3-preview/" + prevSong.getFileLink();
            img = prevSong.getImageIcon();
        }
        preparePlayer();
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer player) {
                player.start();
            }
        });
    }

    public Song getPrevSong(String currentSongId) {
        Song song = null;
        if (songList == null) {
            return song;
        }

        for(int i = 0; i < songList.size(); i++) {
            if (songList.get(0).getId().equals(currentSongId)) {
                song = songList.get(songList.size() - 1);
                break;
            }
            String tempSongId = songList.get(i).getId();
            if (tempSongId.equals(currentSongId)) {
                song = songList.get(i-1);
                break;
            }
        }
        return song;
    }

    public void shuffleSong() {
        Song shuffledSong = getShuffleNextSong();
        if (shuffledSong != null) {
            songId = shuffledSong.getId();
            songTitle = shuffledSong.getTitle();
            artisteName = shuffledSong.getArtiste();
            url = "https://p.scdn.co/mp3-preview/" + shuffledSong.getFileLink();
            img = shuffledSong.getImageIcon();
        }
        preparePlayer();
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer player) {
                player.start();
            }
        });
    }

    public Song getShuffleNextSong() {
        int randInt;
        Random random = new Random();
        Song song = null;
        if (songList == null) {
            return song;
        }
        else {
            randInt = random.nextInt(songList.size());
            song = songList.get(randInt);
            return song;
        }
    }

    public void loopSong(boolean bool) {
        if (bool == true)
            player.setLooping(true);
        else
            player.setLooping(false);
    }
    public boolean getLoopState() {
        return player.isLooping();
    }

    public void playMusic(){
        player.start();
    }
    public void pauseMusic(){
        musicPosition = player.getCurrentPosition();
        player.pause();
    }

    public boolean isMusicPlaying() {
        return player.isPlaying();
    }
    public int getMusicPosition(){
        return player.getCurrentPosition();
    }
    public void seekToPos (int pos){
        player.seekTo(pos);
    }
    public int getMusicDuration() {
        return player.getDuration();
    }

    int getCoverArt() {
        return img;
    }
    public String getArtiste() {
        return artisteName;
    }
    public String getSongTitle() {
        return songTitle;
    }
    public String getSongId() {
        return songId;
    }
    public String getFileLink() {
        return fileLink;
    }
    public ArrayList<Song> getSongList() {
        return songList;
    }
}
