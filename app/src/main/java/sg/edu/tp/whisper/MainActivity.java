package sg.edu.tp.whisper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView topTracks, topArtistes;
    private SongCollection songCollection = new SongCollection();
    ArrayList<Song> songList = songCollection.getSongs();
    private HorizontalAdapter.RecyclerViewClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Home");
        setContentView(R.layout.activity_main);


        setAdapter();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);

        bottomNavigationView.setSelectedItemId(R.id.Home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.Search:
                        startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.Library:
                        startActivity(new Intent(getApplicationContext(), LibraryActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });
    }

    private void setAdapter() {
        setOnClickListener();
        topArtistes = findViewById(R.id.topArtistes);
        topTracks = findViewById(R.id.topTracks);
        HorizontalAdapter adapter1 = new HorizontalAdapter(this, songList, true, listener);
        HorizontalAdapter adapter2 = new HorizontalAdapter(this, songList, false, listener);
        LinearLayoutManager layoutManager1, layoutManager2;

        layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        topArtistes.setLayoutManager(layoutManager1);
        topArtistes.setAdapter(adapter1);

        layoutManager2 = new LinearLayoutManager(this);
        layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        topTracks.setLayoutManager(layoutManager2);
        topTracks.setAdapter(adapter2);

    }

    private void setOnClickListener() {
        listener = new HorizontalAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), MusicPlayerActivity.class);
                intent.putExtra("songName", songList.get(position).getTitle());
                intent.putExtra("artisteID", songList.get(position).getArtiste());
                intent.putExtra("coverArt",songList.get(position).getImageIcon());
                intent.putExtra("fileLink", songList.get(position).getFileLink());
                intent.putExtra("songLength",songList.get(position).getSongLength());
                intent.putExtra("songId",songList.get(position).getId());

                startActivity(intent);

            }
        };
    }

    // This is to display the settings button in the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // On click listener for the settings button
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){
            case R.id.settings:   //this item has your app icon
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default: return super.onOptionsItemSelected(item);
        }
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
