package sg.edu.tp.whisper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView topTracks, topArtistes;
    HorizontalAdapter songAdapter, artisteAdapter;
    private HorizontalAdapter.RecyclerViewClickListener songListener;
    private HorizontalAdapter.RecyclerViewClickListener artisteListener;

    private SongCollection songCollection = new SongCollection();
    ArrayList<Song> songList = songCollection.getTopSongs();
    ArrayList<Song> artisteList = songCollection.getTopArtistes();

    FirebaseUser user;
    String name;
    FloatingActionButton fab;
    boolean doublePress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setFab();
        setBottomNav();
        setAdapter();

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user.getDisplayName() != null) {
            name = user.getDisplayName();
        } else {
            name = "user";
        }
        getSupportActionBar().setTitle("Welcome home, " + name + "!");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // Display the settings button in the toolbar
        getMenuInflater().inflate(R.menu.settings, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // On-click listener for settings button
        switch(item.getItemId()){
            case R.id.settings:   //this item has your app icon
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                finish();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
        // double tap to exit
        if (doublePress) {
            super.onBackPressed();
            return;
        }
        doublePress = true;
        Toast.makeText(this, "Tap again to EXIT", Toast.LENGTH_SHORT).show();
        // change back to false after 2 seconds
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doublePress=false;
            }
        }, 2000);
    }

    private void setFab() {
        // initialise the floating action button
        fab = findViewById(R.id.fab);
        if (isServiceRunning(MusicService.class)) {
            fab.show();
        } else {
            fab.hide();
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isServiceRunning(MusicService.class)) {
                    Intent intent = new Intent(MainActivity.this, MusicPlayerActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Nothing is being played!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setBottomNav() {
        // initialise the bottom nav menu
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.Home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.Search:
                        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.Library:
                        intent = new Intent(MainActivity.this, LibraryActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }

    private void setAdapter() {
        // set the recyclerview adapters

        LinearLayoutManager artisteLayoutManager, songLayoutManager;
        setOnClickListener();
        // initialise the recyclerview
        topArtistes = findViewById(R.id.topArtistes);
        topTracks = findViewById(R.id.topTracks);

        // bind songList to recyclerview
        songAdapter = new HorizontalAdapter(this, songList, true, songListener);
        songLayoutManager = new LinearLayoutManager(this);
        songLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        topTracks.setAdapter(songAdapter);
        topTracks.setLayoutManager(songLayoutManager);

        // bind artisteList to recyclerview
        artisteAdapter = new HorizontalAdapter(this, artisteList, false, artisteListener);
        artisteLayoutManager = new LinearLayoutManager(this);
        artisteLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        topArtistes.setAdapter(artisteAdapter);
        topArtistes.setLayoutManager(artisteLayoutManager);
    }

    private void setOnClickListener() {
        //  On-click listeners for the two recyclerviews
        songListener = new HorizontalAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), MusicService.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("songList", songList);
                intent.putExtras(bundle);

                intent.putExtra("songName", songList.get(position).getTitle());
                intent.putExtra("artisteID", songList.get(position).getArtiste());
                intent.putExtra("coverArt",songList.get(position).getImageIcon());
                intent.putExtra("fileLink", songList.get(position).getFileLink());
                intent.putExtra("songId",songList.get(position).getId());
                startService(intent);

                if (!fab.isShown()) {
                    fab.show();
                }
            }
        };
        artisteListener = new HorizontalAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(MainActivity.this, ArtisteSongsActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("songList", artisteList.get(position).getSongList());
                intent.putExtras(bundle);

                startActivity(intent);
                finish();
            }
        };
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        // check if service is running
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
