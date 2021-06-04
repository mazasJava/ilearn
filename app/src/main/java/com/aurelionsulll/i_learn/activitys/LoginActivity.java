package com.aurelionsulll.i_learn.activitys;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {


    private EditText email;
    private EditText password;
    private Button login;
    private TextView register;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressBar = findViewById(R.id.login_progressBar);

        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);


        register.setOnClickListener(v -> {
            Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(registerIntent);
            finish();
        });

        login.setOnClickListener(v -> {
            String loginEmail = email.getText().toString();
            String loginPassword = password.getText().toString();

            //if not empty
            if (!TextUtils.isEmpty(loginEmail) && !TextUtils.isEmpty(loginPassword)) {
                progressBar.setVisibility(View.VISIBLE);
                //login with email and password
                mAuth.signInWithEmailAndPassword(loginEmail, loginPassword).addOnCompleteListener(task -> {
                    //if success go to home page
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "LOGIN SUCCESS" , Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        sendToMain();
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        String error = task.getException().getMessage();
                        Toast.makeText(LoginActivity.this, "LOGIN ERROR : " + error, Toast.LENGTH_LONG).show();
                    }
                });
            }

        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        //if user auth go to home page
//        if (currentUser != null) {
//            sendToMain();
//        }
    }

    /**
     * send to home page
     */
    private void sendToMain() {
        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}