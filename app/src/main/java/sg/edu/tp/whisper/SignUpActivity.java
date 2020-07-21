package sg.edu.tp.whisper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;
    EditText newEmail, newPassword, newPasswordConfirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().setTitle("Sign Up");

        newEmail = findViewById(R.id.newEmail);
        newPassword = findViewById(R.id.newPassword);
        newPasswordConfirm = findViewById(R.id.newPasswordConfirm);
        progressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.btnSignUp).setOnClickListener(this);
        findViewById(R.id.btnLoginPage).setOnClickListener(this);

    }

    private void registerUser() {
        String email = newEmail.getText().toString().trim();
        String password = newPassword.getText().toString().trim();
        String passwordConfirm = newPasswordConfirm.getText().toString().trim();

        if (email.isEmpty()) {
            newEmail.setError("Email is required");
            newEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            newEmail.setError("Please enter a valid email address.");
            newEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            newPassword.setError("Password is required");
            newPassword.requestFocus();
            return;
        }

        if(password.length() < 6 ) {
            newPassword.setError("Minimum length of password is 6.");
            newPassword.requestFocus();
            return;
        }

        if(!password.equals(passwordConfirm)) {
            newPassword.getText().clear();
            newPasswordConfirm.getText().clear();
            newPasswordConfirm.setError("Passwords do not match!");
            newPassword.setError("Passwords do not match!");


        }

        else {

            progressBar.setVisibility(View.VISIBLE);

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        user = mAuth.getCurrentUser();
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getApplicationContext(), "Please check your email!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(SignUpActivity.this,LoginActivity.class );
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);

                            }
                        });

                    }
                    else if (task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(), "You are already registered!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            });

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLoginPage:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.btnSignUp:
                registerUser();

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
