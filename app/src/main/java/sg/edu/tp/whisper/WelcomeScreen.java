package sg.edu.tp.whisper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeScreen extends AppCompatActivity {
    //FirebaseUser user;
    //String name;
    //TextView userEmail;
// this was made because the horizontal recyclerview wouldn't load
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        /*user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            if (user.getDisplayName() != null) {
                name = user.getDisplayName();
            }
            else {
                name = "user";
            }

        }
        userEmail = findViewById(R.id.userEmail);
        userEmail.append(name + "!");*/
        getSupportActionBar().hide();
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                Intent intent = new Intent(WelcomeScreen.this, MainActivity.class);
                startActivity(intent);
                finish(); } }, 50);
    }
}
