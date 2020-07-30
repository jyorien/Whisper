package sg.edu.tp.whisper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
//import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.google.gson.Gson;




import java.util.ArrayList;

public class LibraryActivity extends AppCompatActivity {


    private RecyclerView trackList;
    //private SongCollection songCollection = new SongCollection();
    private ArrayList<Song> songList = new ArrayList<>();
    private Boolean isLibraryActivity = true;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    //SharedPreferences mPrefs = getPreferences(Context.MODE_PRIVATE);

    TracksAdapter adapter;
    private TracksAdapter.RecyclerViewClickListener listener;

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        getSupportActionBar().setTitle("Library");
        prepareLibrarySongs();
        fab = findViewById(R.id.fab);
        if (isServiceRunning(MusicService.class)) {
            fab.show();
        }
        else {
            fab.hide();
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(LibraryActivity.this, MusicPlayerActivity.class);
                    intent.putExtra("isLibraryActivity",isLibraryActivity);
                    startActivity(intent);
                    finish();

            }
        });

        //songList = LibraryList.getLibraryList().songList;
        //songList = SongCollection.librarySongs;
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
                        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.Home:
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
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
        adapter = new TracksAdapter(songList, listener);
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
    }
    // Make the Back button exit the entire app
    /*@Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }*/

    private void prepareLibrarySongs() {

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //String value = dataSnapshot.getValue().toString();
                //String value = dataSnapshot.getKey();
                //Toast.makeText(getApplicationContext(), value, Toast.LENGTH_LONG).show();
                String[] valueArray = new String[5];
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //String value = ds.getValue().toString();
                    Song song;
                    String value = ds.getValue().toString();

                    if (value != null){
                        valueArray = value.split(","); }
                    String songId = valueArray[0];
                    String title = valueArray[1];
                    String artiste = valueArray[2];
                    String fileLink = valueArray[3];
                    String imageIcon = valueArray[4];
                    int image = Integer.parseInt(imageIcon);
                    song = new Song(songId, title, artiste, fileLink, image);
                    boolean isAdded = false;

                    for (int i = 0; i < songList.size(); i++){
                        if (song.getId().equals(songList.get(i).getId())) {
                            isAdded = true;

                            //SongCollection.librarySongs.remove(i);
                            //songList.remove(song);
                            break;
                        }
                    }
                    if(isAdded == false)
                        songList.add(song);

                }
                adapter.filterList(songList);

                /*Song song;
                String[] valueArray = new String[5];
                String value = dataSnapshot.getValue(String.class);
                if (value != null){
                    valueArray = value.split(","); }
                String songId = valueArray[0];
                String title = valueArray[1];
                String artiste = valueArray[2];
                String fileLink = valueArray[3];
                String imageIcon = valueArray[4];
                int image = Integer.parseInt(imageIcon);
                song = new Song(songId, title, artiste, fileLink, image);
                boolean isAdded = false;
                for (int i = 0; i < songList.size(); i++){
                    if (song.getId().equals(songList.get(i).getId())) {
                        isAdded = true;

                        //SongCollection.librarySongs.remove(i);
                        break;
                    }
                }
                if(isAdded == false)
                    songList.add(song);
                adapter.filterList(songList);
                Toast.makeText(getApplicationContext(), "CAN YOU PLEASE WORK", Toast.LENGTH_SHORT).show();
                //Log.d(TAG, "Value is: " + value);*/
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
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
    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
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
