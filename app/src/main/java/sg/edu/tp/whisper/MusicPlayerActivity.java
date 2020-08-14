package sg.edu.tp.whisper;

import androidx.appcompat.app.AppCompatActivity;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;






public class MusicPlayerActivity extends AppCompatActivity {

    private boolean isShuffle = false;
    private boolean isLooping = false;
    ImageButton playPauseBtn = null;
    ImageButton repeatButton = null;
    ImageButton shuffleButton = null;
    TextView txtCurrentTime = null;

    private SeekBar seekBar = null;
    private Handler handler = new Handler();

    ArrayList<Song> songList = new ArrayList<>();
    int img = 0;
    private String artisteName = "";
    private String songId = "";
    private String songTitle = "";
    private String fileLink = "";
    Boolean isLibraryActivity = false;

    TextView songName;
    TextView artiste;
    ImageView coverArt;

    private int musicPosition = 0; // to store position of the song when paused

    Boolean isAdded = false;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference songRef, dbRef, userRef;
    FirebaseUser user;
    ImageButton addToLibraryBtn;

    MusicService mService;
    boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        seekBar = findViewById(R.id.seekBar);
        txtCurrentTime = findViewById(R.id.txtCurrentTime);

        playPauseBtn = findViewById(R.id.playPauseButton);
        repeatButton = findViewById(R.id.repeatButton);
        shuffleButton = findViewById(R.id.shuffleButton);
        addToLibraryBtn  = findViewById(R.id.addToLibraryButton);
        playPauseBtn.setBackgroundResource(R.drawable.fpause);
        songName = findViewById(R.id.songName);
        artiste = findViewById(R.id.artiste);
        coverArt = findViewById(R.id.imageView);

        user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        dbRef = database.getReference(); // reference the whole database
        userRef = database.getReference(userId);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isLibraryActivity = extras.getBoolean("isLibraryActivity");
        }

        startMusicService();
        displaySong();

        updateSeekBar();
        seekBar.setOnSeekBarChangeListener(seekBarOnSeekChangeListener);
    }

    private void displaySong() {
        if (mService != null) {
        artisteName = mService.getArtiste();
        songTitle = mService.getSongTitle();
        img = mService.getCoverArt();
        songList = mService.getSongList();
        songId = mService.getSongId();
        fileLink = mService.getFileLink();
        }

        artiste.setText(artisteName);
        songName.setText(songTitle);
        coverArt.setImageResource(img);
    }

    private void checkSongInDb() {
        // check if the current song is inside the database.
        // changes state of addToLibraryBtn
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if (dataSnapshot.hasChild(songId)) {
                    isAdded = true;
                    addToLibraryBtn.setBackgroundResource(R.drawable.remove_button);
                }
                else {
                    isAdded = false;
                    addToLibraryBtn.setBackgroundResource(R.drawable.add_button);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    private void updateSeekBar() {
        if (mBound == true) {
            txtCurrentTime.setText(milliSecondsToTimer(mService.getMusicPosition()));
            seekBar.setProgress(mService.getMusicPosition());
        }
        handler.postDelayed(runnable, 100); // updates seekbar pos every 0.1 sec
    }
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mBound == true) {
                txtCurrentTime.setText(milliSecondsToTimer(mService.getMusicPosition()));
                seekBar.setProgress(mService.getMusicPosition());
                handler.postDelayed(runnable,100);
            };
        }
    };

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
                mService.seekToPos(progress);
                seekBar.setProgress(progress);
            }
            if (progress == seekBar.getMax()) {

                if (isLooping == false) {
                    mService.playNext();
                }

            }
            if (progress == 0) {
                displaySong();
            }
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

    private void startMusicService() {
        Intent intent = new Intent(getApplicationContext(), MusicService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            // called when the connection to MusicService is established
            MusicService.LocalBinder binder = (MusicService.LocalBinder) iBinder;
            // get the service from the LocalBinder class
            mService = binder.getService();
            mBound = true;

            displaySong();
            seekBar.setMax(mService.getMusicDuration());
            updateSeekBar();
            checkSongInDb();

            if (mService.isMusicPlaying() == false) {
                seekBar.setProgress(mService.getMusicPosition());
                pauseMusic();
            }
            if (mService.getLoopState() == true) {
                isLooping = true;
                repeatButton.setBackgroundResource(R.drawable.crepeat1);
            }
            if (mService.getShuffleState() == true) {
                isShuffle = true;
                shuffleButton.setBackgroundResource(R.drawable.cshuffle2);
            }
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBound = false;
        }
    };

    public void pauseMusic() {
        mService.pauseMusic();
        musicPosition = mService.getMusicPosition();
        playPauseBtn.setBackgroundResource(R.drawable.fplay);
    }

    public void playOrPauseBtn(View view) {
        playPauseBtn = findViewById(R.id.playPauseButton);
        playPauseBtn.setBackgroundResource(R.drawable.fpause);

        if (!mService.isMusicPlaying()) {
            if (musicPosition > 0) {
                seekBar.setProgress(musicPosition);
                mService.playMusic();
            }
        }
        else {
            pauseMusic();
        }
    }

    public void nextBtn(View view) {
            mService.playNext();
            displaySong();
            checkSongInDb();
            playPauseBtn.setBackgroundResource(R.drawable.fpause);
            repeatButton.setBackgroundResource(R.drawable.crepeat);
            isLooping = false;
            mService.loopSong(false);
    }

    public void prevBtn(View view) {
            mService.playPrev();
            displaySong();
            checkSongInDb();
            playPauseBtn.setBackgroundResource(R.drawable.fpause);
            repeatButton.setBackgroundResource(R.drawable.crepeat);
            isLooping = false;
            mService.loopSong(false);
    }

    public void repeatSongBtn(View view) {
        if (isLooping == false) {
            isLooping = true;
            mService.loopSong(true);
            isShuffle = false;
            mService.unshuffleSongs();
            shuffleButton.setBackgroundResource(R.drawable.cshuffle);
            repeatButton.setBackgroundResource(R.drawable.crepeat1);
        }
        else {
            isLooping = false;
            mService.loopSong(false);
            repeatButton.setBackgroundResource(R.drawable.crepeat);
        }
    }

    public void shuffleSongBtn(View view) {
        repeatButton = findViewById(R.id.repeatButton);
        shuffleButton = findViewById(R.id.shuffleButton);
        if (isShuffle == false) {
            isShuffle = true;
            isLooping = false;
            mService.shuffleSongs();
            mService.loopSong(false);
            shuffleButton.setBackgroundResource(R.drawable.cshuffle2);
            repeatButton.setBackgroundResource(R.drawable.crepeat);
        }
        else {
            isShuffle = false;
            mService.unshuffleSongs();
            shuffleButton.setBackgroundResource(R.drawable.cshuffle);
        }
    }

    public void addToLibraryBtn(View view) {
        songRef = userRef.child(songId); // append child node in database

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if (dataSnapshot.hasChild(songId)) {
                    // remove the node with the song
                    songRef.removeValue();
                    Toast.makeText(getApplicationContext(), "Removed " + songTitle + " from Library", Toast.LENGTH_SHORT).show();
                    isAdded = false;
                }
                else {
                    // add a new child node to store song
                    String image = Integer.toString(img);
                    String addSong = songId + "," + songTitle + "," + artisteName + "," + fileLink + "," + image;
                    songRef.setValue(addSong);
                    Toast.makeText(getApplicationContext(), "Added " + songTitle + " to Library", Toast.LENGTH_SHORT).show();
                    isAdded = true;
                }

                if (isAdded == true) {
                    addToLibraryBtn.setBackgroundResource(R.drawable.remove_button);
                }
                else {
                    addToLibraryBtn.setBackgroundResource(R.drawable.add_button);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    @Override
    public void onBackPressed() {
        startPreviousActivity();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        // for the back button in the title bar
        startPreviousActivity();
        return true;
    }

    private void startPreviousActivity() {
        if (isLibraryActivity == true) {
            Intent intent = new Intent(MusicPlayerActivity.this, LibraryActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            finish();
        }
        unbindService(mConnection);
        mBound = false;
    }

}