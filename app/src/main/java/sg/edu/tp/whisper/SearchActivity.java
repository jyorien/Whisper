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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    SongCollection songCollection = new SongCollection();
    ArrayList<Song> allSongs = songCollection.getAllSongs();
    ArrayList<Song> filteredList = new ArrayList<>();

    private RecyclerView searchList;
    private TracksAdapter.RecyclerViewClickListener listener;
    TracksAdapter adapter;

    FloatingActionButton fab;
    boolean doublePress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setTitle("Search");
        setFab();
        setBottomNav();
        setAdapter();

        EditText searchInput = findViewById(R.id.searchInput);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }
    @Override
    public void onBackPressed() {
        // double tap to exit
        if (doublePress) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
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
                    Intent intent = new Intent(SearchActivity.this, MusicPlayerActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(SearchActivity.this, "Nothing is being played!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setBottomNav() {
        // initialise the bottom nav menu
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.Search);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.Home:
                        Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.Library:
                        intent = new Intent(SearchActivity.this, LibraryActivity.class);
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
        // set the recyclerview adapter

        LinearLayoutManager layoutManager;
        setOnClickListener();
        // initialise the recyclerview
        searchList = findViewById(R.id.searchList);

        // bind filteredList to recyclerview
        adapter = new TracksAdapter(filteredList, listener);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        searchList.setLayoutManager(layoutManager);
        searchList.setAdapter(adapter);
    }

    private void setOnClickListener() {
        //  On-click listener for the recyclerview
        listener = new TracksAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), MusicService.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("songList", filteredList);
                intent.putExtras(bundle);

                intent.putExtra("songName", filteredList.get(position).getTitle());
                intent.putExtra("artisteID", filteredList.get(position).getArtiste());
                intent.putExtra("coverArt",filteredList.get(position).getImageIcon());
                intent.putExtra("fileLink", filteredList.get(position).getFileLink());
                intent.putExtra("songId",filteredList.get(position).getId());
                startService(intent);

                if (!fab.isShown()) {
                    fab.show();
                }
            }
        };
    }

    private void filter(String text) {
        // filter songs based on text input
        filteredList.clear();

        if (text.isEmpty()) {
            adapter.filterList(filteredList);
        }
        else {
            for(Song song : allSongs) {
                if (song.getTitle().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(song);
                }
                else if (song.getArtiste().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(song);
                }
            }
            adapter.filterList(filteredList);
        }
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
