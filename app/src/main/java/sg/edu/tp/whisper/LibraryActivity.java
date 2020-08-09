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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;





import java.util.ArrayList;

public class LibraryActivity extends AppCompatActivity {

    private RecyclerView trackList;

    private ArrayList<Song> songList = new ArrayList<>();
    private Boolean isLibraryActivity = true;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dbRef = database.getReference();
    DatabaseReference userRef = dbRef.child(user.getUid());

    TracksAdapter adapter;
    private TracksAdapter.RecyclerViewClickListener listener;

    FloatingActionButton fab;
    boolean doublePress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        getSupportActionBar().setTitle("Library");
        setFab();
        setBottomNav();
        setAdapter();
        prepareLibrarySongs();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // Display the settings button in the toolbar
        getMenuInflater().inflate(R.menu.delete, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // On-click listener for settings button
        switch(item.getItemId()){
            case R.id.delete:
                dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        songList.clear(); // clear the arraylist
                        userRef.removeValue(); // clear the node in the database
                        adapter.filterList(songList); // update the recyclerview

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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
                    Intent intent = new Intent(LibraryActivity.this, MusicPlayerActivity.class);
                    intent.putExtra("isLibraryActivity", isLibraryActivity);
                    startActivity(intent);
                } else {
                    Toast.makeText(LibraryActivity.this, "Nothing is being played!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setBottomNav() {
        // initialise the bottom nav menu
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.Library);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.Search:
                        Intent intent = new Intent(LibraryActivity.this, SearchActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.Home:
                        intent = new Intent(LibraryActivity.this, MainActivity.class);
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
        trackList = findViewById(R.id.trackList);

        // bind songList to recyclerview
        adapter = new TracksAdapter(songList, listener);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        trackList.setLayoutManager(layoutManager);
        trackList.setAdapter(adapter);
    }

    private void setOnClickListener() {
        //  On-click listener for the recyclerview
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

    private void prepareLibrarySongs() {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String[] valueArray = new String[5];
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Song song;
                    // store the child node value in 'value'
                    String value = ds.getValue().toString();
                    // the values are separated by commas
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
                            break;
                        }
                    }
                    if (isAdded == false)
                        songList.add(song);
                }
                adapter.filterList(songList);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
