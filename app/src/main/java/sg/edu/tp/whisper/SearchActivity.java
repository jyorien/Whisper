package sg.edu.tp.whisper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    SongCollection songCollection = new SongCollection();
    ArrayList<Song> allSongs = songCollection.getAllSongs();
    ArrayList<Song> emptySongs = new ArrayList<>();
    ArrayList<Song> filteredList;


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
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.Library:
                        intent = new Intent(getApplicationContext(), LibraryActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
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
        filteredList = new ArrayList<>();

        if (text.isEmpty()) {
            adapter.filterList(emptySongs);
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
        adapter = new TracksAdapter(emptySongs, listener);
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
                intent.putExtra("songName", filteredList.get(position).getTitle());
                intent.putExtra("artisteID", filteredList.get(position).getArtiste());
                intent.putExtra("coverArt",filteredList.get(position).getImageIcon());
                intent.putExtra("fileLink", filteredList.get(position).getFileLink());
                intent.putExtra("songId",filteredList.get(position).getId());

                startActivity(intent);

            }
        };
    }

    // Make the Back button exit the entire app
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
