package sg.edu.tp.whisper;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.IOException;
import java.util.ArrayList;


public class MusicService extends Service {
    private String url = "";

    private final IBinder binder = new LocalBinder();

    public static MediaPlayer player = new MediaPlayer();

    ArrayList<Song> songList = new ArrayList<>();

    public MusicService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "Service created", Toast.LENGTH_LONG).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(this, "Service started", Toast.LENGTH_LONG).show();
        //return super.onStartCommand(intent, flags, startId);
        url = intent.getStringExtra("url");
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

            }
        });

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
        //throw new UnsupportedOperationException("Not yet implemented");
        url = intent.getStringExtra("url");
        //songList = (ArrayList<Song>) intent.getSerializableExtra("songList");
        preparePlayer();
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer player) {
                player.start();
            }
        });
        Toast.makeText(this, "bound", Toast.LENGTH_SHORT).show();
        return binder;
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

    public class LocalBinder extends Binder {
        MusicService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MusicService.this;
        }

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

    public void pauseMusic(){
        player.pause();
    }

    public void playMusic(){
        player.start();
    }

    public boolean isMusicPlaying() {
        return player.isPlaying();
    }
}
