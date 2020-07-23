package sg.edu.tp.whisper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    SongCollection songCollection = new SongCollection();
    ArrayList<Song> allSongs = songCollection.getAllSongs();
    ArrayList<Song> filteredList = new ArrayList<>();


    private RecyclerView allSongsList;
    private TracksAdapter.RecyclerViewClickListener listener;
    TracksAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setTitle("Search");
        setAdapter();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);

        bottomNavigationView.setSelectedItemId(R.id.Search);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.Home:
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.Library:
                        intent = new Intent(getApplicationContext(), LibraryActivity.class);
                        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        EditText searchInput = findViewById(R.id.searchInput);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }

    private void filter(String text) {
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

    private void setAdapter() {
        setOnClickListener();
        allSongsList = findViewById(R.id.allSongsList);
        adapter = new TracksAdapter(filteredList, listener);
        LinearLayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        allSongsList.setLayoutManager(layoutManager);
        allSongsList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void setOnClickListener() {
        listener = new TracksAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), MusicPlayerActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("songList", filteredList);
                intent.putExtras(bundle);

                intent.putExtra("songName", filteredList.get(position).getTitle());
                intent.putExtra("artisteID", filteredList.get(position).getArtiste());
                intent.putExtra("coverArt",filteredList.get(position).getImageIcon());
                intent.putExtra("fileLink", filteredList.get(position).getFileLink());
                intent.putExtra("songId",filteredList.get(position).getId());

                startActivity(intent);

            }
        };
    }

    boolean doublePress = false;
    // double tap to exit
    @Override
    public void onBackPressed() {
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
}
