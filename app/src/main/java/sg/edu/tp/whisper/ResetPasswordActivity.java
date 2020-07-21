package sg.edu.tp.whisper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResetPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;
    EditText resetEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        getSupportActionBar().setTitle("Reset Password");

        resetEmail = findViewById(R.id.resetEmail);
        progressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.btnResetPassword).setOnClickListener(this);
        findViewById(R.id.btnLoginPage).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnLoginPage:
                startActivity(new Intent(this, LoginActivity.class));
                break;

            case R.id.btnResetPassword:
                resetPassword();
                break;
        }
    }

    private void resetPassword() {
        String email = resetEmail.getText().toString().trim();
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(), "Please check your email!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ResetPasswordActivity.this,LoginActivity.class );
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
    }
}
