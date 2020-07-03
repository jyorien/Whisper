package sg.edu.tp.whisper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    EditText username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setTitle("Settings");

        username = findViewById(R.id.username);


        findViewById(R.id.btnLogOut).setOnClickListener(this);
        findViewById(R.id.btnUpdate).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogOut:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
                break;

            case R.id.btnUpdate:
                updateProfile();
        }

    }

    private void updateProfile() {
        String updatedName = username.getText().toString().trim();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(updatedName).build();
        user.updateProfile(profileUpdates);
        //username.getText().clear();
        Toast.makeText(getApplicationContext(), "Username updated to " + updatedName + "!",Toast.LENGTH_SHORT).show();

    }
}
