package sg.edu.tp.whisper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();

        //setContentView(R.layout.activity_splash_screen);
        //getSupportActionBar().hide();
        /*new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(intent);
                finish(); } }, 1000);*/
    }
    }

