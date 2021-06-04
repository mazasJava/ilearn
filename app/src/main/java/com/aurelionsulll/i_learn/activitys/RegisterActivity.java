package com.aurelionsulll.i_learn.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aurelionsulll.i_learn.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {


    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private Button register;
    private TextView login;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        progressBar = findViewById(R.id.register_progressBar);

        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        register = findViewById(R.id.register);
        login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();

            }
        });
        register.setOnClickListener(v -> {
            String registerEmail = email.getText().toString();
            String registerPassword = password.getText().toString();
            String registerConfirmPassword = confirmPassword.getText().toString();

            if (!TextUtils.isEmpty(registerEmail) && !TextUtils.isEmpty(registerPassword) && !TextUtils.isEmpty(registerConfirmPassword)) {
                if (registerPassword.equals(registerConfirmPassword)) {
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.createUserWithEmailAndPassword(registerEmail, registerPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent setupIntent = new Intent(RegisterActivity.this, SetupActivity.class);
                                progressBar.setVisibility(View.GONE);
                                startActivity(setupIntent);
                                finish();
                            }
                        }
                    });
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RegisterActivity.this, "Password and confirm password not matching ", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(RegisterActivity.this, "One of the required fields is missing", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            sendToMain();
        }
    }

    private void sendToMain() {
        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}