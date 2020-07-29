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
import android.widget.Adapter;
import android.widget.Toast;
import android.widget.Toolbar;

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

public class MainActivity extends AppCompatActivity {

    RecyclerView topTracks, topArtistes;
    private SongCollection songCollection = new SongCollection();
    ArrayList<Song> songList = songCollection.getTopSongs();
    ArrayList<Song> artisteList = songCollection.getTopArtistes();
    private HorizontalAdapter.RecyclerViewClickListener listener;
    private HorizontalAdapter.RecyclerViewClickListener listener2;
    FirebaseUser user;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = FirebaseAuth.getInstance().getCurrentUser();

        String name;
        if (user.getDisplayName() != null) {
            name = user.getDisplayName();
        }
        else {
            name = "user";
        }

        getSupportActionBar().setTitle("Welcome home, " + name + "!");
        setContentView(R.layout.activity_main);
        fab = findViewById(R.id.fab);


        setAdapter();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);

        bottomNavigationView.setSelectedItemId(R.id.Home);

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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isServiceRunning(MusicService.class))  {
                    Intent intent = new Intent(MainActivity.this, MusicPlayerActivity.class);
                    startActivity(intent); }
                else {
                    Toast.makeText(MainActivity.this, "Nothing is being played!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Song");
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                getSupportActionBar().setTitle(value);
                //Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });*/

    }

    // setting the recyclerview adapter
    private void setAdapter() {
        setOnClickListener();
        topArtistes = findViewById(R.id.topArtistes);
        topTracks = findViewById(R.id.topTracks);
        HorizontalAdapter adapter1 = new HorizontalAdapter(this, artisteList, true, listener2);
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

    // setting on click listeners for the two lists of items
    private void setOnClickListener() {
        listener = new HorizontalAdapter.RecyclerViewClickListener() {
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



                /*Intent intent = new Intent(getApplicationContext(), MusicPlayerActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("songList", songList);
                intent.putExtras(bundle);

                intent.putExtra("songName", songList.get(position).getTitle());
                intent.putExtra("artisteID", songList.get(position).getArtiste());
                intent.putExtra("coverArt",songList.get(position).getImageIcon());
                intent.putExtra("fileLink", songList.get(position).getFileLink());
                intent.putExtra("songId",songList.get(position).getId());

                startActivity(intent);*/


            }
        };

        listener2 = new HorizontalAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), ArtisteSongsActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("songList", artisteList.get(position).getSongList());
                intent.putExtras(bundle);

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
                finish();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
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

    // check if service is running
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
