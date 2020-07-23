package sg.edu.tp.whisper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class ArtisteSongsActivity extends AppCompatActivity {
    ArrayList<Song> songList = new ArrayList<>();
    private RecyclerView trackList;
    private TracksAdapter.RecyclerViewClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artiste_songs);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        retrieveData();
        getSupportActionBar().setTitle(songList.get(0).getArtiste());
        setAdapter();
    }

    private void retrieveData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            songList = (ArrayList<Song>) extras.getSerializable("songList");
        }
    }

    private void setAdapter() {
        setOnClickListener();
        trackList = findViewById(R.id.trackList);
        TracksAdapter adapter = new TracksAdapter(songList, listener);
        LinearLayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        trackList.setLayoutManager(layoutManager);
        trackList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void setOnClickListener() {
        listener = new TracksAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), MusicPlayerActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("songList", songList);
                intent.putExtras(bundle);

                intent.putExtra("songName", songList.get(position).getTitle());
                intent.putExtra("artisteID", songList.get(position).getArtiste());
                intent.putExtra("coverArt",songList.get(position).getImageIcon());
                intent.putExtra("fileLink", songList.get(position).getFileLink());
                intent.putExtra("songId",songList.get(position).getId());

                startActivity(intent);

            }
        };
    }

    // for the back button in the title bar
    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
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
