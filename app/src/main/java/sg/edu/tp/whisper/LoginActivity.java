package sg.edu.tp.whisper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    FirebaseUser user;
    FirebaseAuth mAuth;
    EditText existingEmail, existingPssword;
    ProgressBar progressBar;
    boolean doublePress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Login");

        mAuth = FirebaseAuth.getInstance();
        existingEmail = findViewById(R.id.existingEmail);
        existingPssword = findViewById(R.id.existingPassword);
        progressBar = findViewById(R.id.progressBar);

        findViewById(R.id.btnSignUpPage).setOnClickListener(this);
        findViewById(R.id.btnLogin).setOnClickListener(this);
        findViewById(R.id.btnResetPasswordPage).setOnClickListener(this);
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
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                userLogin();
                break;

            case R.id.btnSignUpPage:
                startActivity(new Intent(this, SignUpActivity.class));
                break;

            case R.id.btnResetPasswordPage:
                startActivity(new Intent(this, ResetPasswordActivity.class));
                break;
        }
    }

    private void userLogin() {
        String email = existingEmail.getText().toString().trim();
        String password = existingPssword.getText().toString().trim();

        if (email.isEmpty()) {
            existingEmail.setError("Email is required");
            existingEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            existingEmail.setError("Please enter a valid email address.");
            existingEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            existingPssword.setError("Password is required");
            existingPssword.requestFocus();
            return;
        }

        if (password.length() < 6 ) {
            existingPssword.setError("Minimum length of password is 6.");
            existingPssword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    user = mAuth.getCurrentUser();

                    if (!user.isEmailVerified()) {
                        Toast.makeText(getApplicationContext(), "Please verify your email!",Toast.LENGTH_LONG).show();
                        return;
                    }

                    else {
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class );
                        startActivity(intent);
                        finish();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
