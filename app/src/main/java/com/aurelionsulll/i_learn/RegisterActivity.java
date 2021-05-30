package com.aurelionsulll.i_learn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        register = findViewById(R.id.register);
        login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent loginIntent = new Intent(RegisterActivity.this,LoginActivity.class);
//                finish();
                finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String registerEmail = email.getText().toString();
                String registerPassword = password.getText().toString();
                String registerConfirmPassword = confirmPassword.getText().toString();

                if (!TextUtils.isEmpty(registerEmail) && !TextUtils.isEmpty(registerPassword) && !TextUtils.isEmpty(registerConfirmPassword)) {

                    if (registerPassword.equals(registerConfirmPassword)) {
                        mAuth.createUserWithEmailAndPassword(registerEmail, registerPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Intent setupIntent = new Intent(RegisterActivity.this,SetupActivity.class);
                                    startActivity(setupIntent);
                                    finish();
                                } else {
                                    // error toast
                                }
                            }
                        });
                    } else {
                        // password != cPassword
                    }

                }
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