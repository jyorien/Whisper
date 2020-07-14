package sg.edu.tp.whisper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
//import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
//import com.google.gson.Gson;




import java.util.ArrayList;

public class LibraryActivity extends AppCompatActivity {

    private RecyclerView trackList;
    private SongCollection songCollection = new SongCollection();
    public static ArrayList<Song> songList = new ArrayList<>();
    private Boolean isLibraryActivity = true;
    //SharedPreferences mPrefs = getPreferences(Context.MODE_PRIVATE);






    private TracksAdapter.RecyclerViewClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        getSupportActionBar().setTitle("Library");
        //songList = LibraryList.getLibraryList().songList;
        songList = songCollection.librarySongs;
        //prepareLibraryList();
        /*Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Song>>(){}.getType();
        String json = mPrefs.getString("songList", "");
        songList = gson.fromJson(json, type);*/
        setAdapter();


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);

        bottomNavigationView.setSelectedItemId(R.id.Library);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.Search:
                        Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.Home:
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
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
                intent.putExtra("isLibraryActivity", isLibraryActivity);


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
    /*private void prepareLibraryList() {
        Song theWayYouLookTonight = new Song("S1001", "The Way You Look Tonight", "Michael Buble",
                "a5b8972e764025020625bbf9c1c2bbb06e394a60?cid=2afe87a64b0042dabf51f37318616965",
                R.drawable.michael_buble_collection);
        songList.add(theWayYouLookTonight);
    }*/


    /*@Override
    protected void onDestroy() {
        songList.clear();
        super.onDestroy();
    }*/
}
