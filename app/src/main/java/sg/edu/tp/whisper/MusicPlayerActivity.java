package sg.edu.tp.whisper;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;


public class MusicPlayerActivity extends AppCompatActivity {

    private boolean isShuffle = false;
    private boolean isLooping = false;
    ImageButton playPauseBtn = null;
    ImageButton repeatButton = null;
    ImageButton shuffleButton = null;
    TextView txtCurrentTime = null;

    private SeekBar seekBar = null;
    private Handler handler;

    int img = 0;

    private String artisteName = "";
    private String songId = "";
    private String fileLink = "";
    private String url = "";
    private String songTitle = "";
    private final String BASE_URL = "https://p.scdn.co/mp3-preview/";

    private MediaPlayer player = null;
    private int musicPosition = 0; // to store position of the song when paused

    private SongCollection songCollection = new SongCollection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        seekBar = findViewById(R.id.seekBar);
        handler = new Handler();
        txtCurrentTime = findViewById(R.id.txtCurrentTime);

        // On create, media player starts playing audio
        retrieveData();
        displaySong(songTitle, artisteName, img);
        playPauseBtn = findViewById(R.id.playPauseBtn);
        playPauseBtn.setBackgroundResource(R.drawable.fpause);
        player = new MediaPlayer();
        preparePlayer();

        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer player) {
                seekBar.setMax(player.getDuration());
                player.start();
                setTitle("Now playing: " + songTitle + " by " + artisteName);
                playPauseBtn.setBackgroundResource(R.drawable.fpause);
            }
        });
        // seekbar responds to user tap and progresses as the song plays
        updateSeekBar();
        seekBar.setOnSeekBarChangeListener(seekBarOnSeekChangeListener);

        /*if loop enabled, replay the same song.
        else if shuffle is enabled, play a random song next.
        else, auto play to the next song on the list. */
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if ( isLooping == true) {
                    preparePlayer();
                    player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer player) {
                            player.start();
                            setTitle("Now playing: " + songTitle + " by " + artisteName);
                            playPauseBtn.setBackgroundResource(R.drawable.fpause);

                        }
                    });
                }
                else if (isLooping == false && isShuffle == false)
                    playNext();
                else if (isShuffle == true) {
                    shuffleSong();

                }
            }
        });



    }

    public void playOrPause(View view) {
        playPauseBtn = findViewById(R.id.playPauseBtn);
        playPauseBtn.setBackgroundResource(R.drawable.fpause);
          /*if (player == null)
              preparePlayer();
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer player) {
                player.start();
                setTitle("Now playing: " + songTitle + " by " + artisteName);
                playPauseBtn.setBackgroundResource(R.drawable.fpause);

            }
        }); */
        if (!player.isPlaying()) {
            if (musicPosition > 0) {
                player.seekTo(musicPosition);
                player.start();
                updateSeekBar();
            }
        } else {
            pauseMusic();
        }
    }

    private void retrieveData() {

        artisteName = "Artiste not found";
        songTitle = "songTitle not found";
        img = R.drawable.home;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            artisteName = extras.getString("artisteID");
            songTitle = extras.getString("songName");
            img = extras.getInt("coverArt");
            songId = extras.getString("songId");
            fileLink = extras.getString("fileLink");
            url = BASE_URL + fileLink;
        }

    }

    private void displaySong(String title, String artisteName, int image) {
        TextView songName = findViewById(R.id.songName);
        TextView artiste = findViewById(R.id.artiste);
        ImageView coverArt = findViewById(R.id.imageView);
        artiste.setText(artisteName);
        songName.setText(title);
        coverArt.setImageResource(image);

    }
    // brings player to prepared state from idle > initialised > prepared
    public void preparePlayer() {
        player.reset();
        try {

            //player = MediaPlayer.create(this, Uri.parse(url))
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

    /*private void gracefullyStopWhenMusicEnds() {
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stopActivities();
                playNext();

            }
        });
    }*/

    private void stopActivities() {
        if (player != null) {
            playPauseBtn.setBackgroundResource(R.drawable.fplay);
            musicPosition = 0;
            player.stop();

            player.release();
            player = null;
            setTitle("");
        }

    }

    public void pauseMusic() {
        player.pause();
        musicPosition = player.getCurrentPosition();
        playPauseBtn.setBackgroundResource(R.drawable.fplay);
    }

    public void playNext() {
        Song nextSong = songCollection.getNextSong(songId);
        if (nextSong != null) {
            songId = nextSong.getId();
            songTitle = nextSong.getTitle();
            artisteName = nextSong.getArtiste();
            fileLink = nextSong.getFileLink();
            img = nextSong.getImageIcon();
            url = BASE_URL + fileLink;
            displaySong(songTitle, artisteName, img);
            //stopActivities();
            preparePlayer();
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer player) {
                    seekBar.setMax(player.getDuration());
                    player.start();
                    setTitle("Now playing: " + songTitle + " by " + artisteName);
                    playPauseBtn.setBackgroundResource(R.drawable.fpause);

                }
            });
        }
    }

    public void nextBtn(View view) {
        if (isShuffle == true) {
            shuffleSong();
        }
        else{
            playNext();
        }


    }

    public void playPrev() {
        Song prevSong = songCollection.getPrevSong(songId);
        if (prevSong != null) {
            songId = prevSong.getId();
            songTitle = prevSong.getTitle();
            artisteName = prevSong.getArtiste();
            fileLink = prevSong.getFileLink();
            img = prevSong.getImageIcon();
            url = BASE_URL + fileLink;
            displaySong(songTitle, artisteName, img);
            //stopActivities();
            preparePlayer();
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer player) {
                    player.start();
                    setTitle("Now playing: " + songTitle + " by " + artisteName);
                    playPauseBtn.setBackgroundResource(R.drawable.fpause);

                }
            });


        }
    }

    public void prevBtn(View view) {
        if (isShuffle == true) {
            shuffleSong();
        }
        else {
            playPrev();
        }

    }

    @Override
    protected void onDestroy() {
        stopActivities();
        super.onDestroy();
    }

    public void repeatSongBtn(View view) {
        repeatButton = findViewById(R.id.repeatButton);
        shuffleButton = findViewById(R.id.shuffleButton);

        if (isLooping == false) {
            isLooping = true;
            isShuffle = false;
            shuffleButton.setBackgroundResource(R.drawable.cshuffle);
            repeatButton.setBackgroundResource(R.drawable.crepeat1);
        }
        else if(isLooping == true){
            isLooping = false;
            repeatButton.setBackgroundResource(R.drawable.crepeat);
        }
    }

    public void shuffleSongBtn(View view) {
        repeatButton = findViewById(R.id.repeatButton);
        shuffleButton = findViewById(R.id.shuffleButton);
        if (isShuffle == false) {
            isShuffle = true;
            isLooping = false;
            shuffleButton.setBackgroundResource(R.drawable.cshuffle2);
            repeatButton.setBackgroundResource(R.drawable.crepeat);
            setTitle("Shuffle");
        }
        else if (isShuffle == true) {
            isShuffle = false;
            shuffleButton.setBackgroundResource(R.drawable.cshuffle);
        }
    }

    public void shuffleSong() {
        Song shuffledSong = songCollection.getShuffleNextSong();
        songId = shuffledSong.getId();
        songTitle = shuffledSong.getTitle();
        artisteName = shuffledSong.getArtiste();
        fileLink = shuffledSong.getFileLink();
        img = shuffledSong.getImageIcon();
        url = BASE_URL + fileLink;
        displaySong(songTitle, artisteName, img);
        //stopActivities();
        preparePlayer();
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer player) {
                player.start();
                setTitle("Now playing: " + songTitle + " by " + artisteName);
                playPauseBtn.setBackgroundResource(R.drawable.fpause);

            }
        });


    }

    private void updateSeekBar() {
        if (player == null) {
            seekBar.setProgress(0);

        }
        else {
            seekBar.setProgress(player.getCurrentPosition());
            txtCurrentTime.setText(milliSecondsToTimer(player.getCurrentPosition()));
            handler.postDelayed(runnable, 50);
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            updateSeekBar();
        }
    };

    private String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time

        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);


        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + "0" + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }
    SeekBar.OnSeekBarChangeListener seekBarOnSeekChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            // TODO Auto-generated method stub

            if (fromUser) {
                player.seekTo(progress);
                seekBar.setProgress(progress);
            }

        }
    };

}