package sg.edu.tp.whisper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
    }

    private void userLogin() {

        String email = existingEmail.getText().toString().trim();
        String password = existingPssword.getText().toString().trim();
        if (email.isEmpty()) {
            existingEmail.setError("Email is required");
            existingEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            existingEmail.setError("Please enter a valid email address.");
            existingEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            existingPssword.setError("Password is required");
            existingPssword.requestFocus();
            return;
        }

        if(password.length() < 6 ) {
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
                        Intent intent = new Intent(LoginActivity.this,WelcomeScreen.class );
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }

                }
                else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
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


        }

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
