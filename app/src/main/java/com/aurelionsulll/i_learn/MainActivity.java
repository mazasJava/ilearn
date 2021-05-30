package com.aurelionsulll.i_learn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    private MaterialToolbar mainToolBar;
    private FirebaseAuth mAuth;
    Button btnToFeed;
    Button btnToJoinedPost;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        mainToolBar = findViewById(R.id.main_tool_bar);

        setSupportActionBar(mainToolBar);
        getSupportActionBar().setTitle("I learn");
        btnToFeed = findViewById(R.id.btnToFeed);
        btnToJoinedPost = findViewById(R.id.btn_joined_post);
        button = findViewById(R.id.button);
        btnToFeed.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Feed.class);
            startActivity(intent);
        });
        btnToJoinedPost.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, UserPostActivity.class);
            startActivity(intent);
        });
        button.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, JoinedPostActivity.class);
            startActivity(intent);
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

//        System.out.println(currentUser);
//        if (currentUser == null) {
//            sendToLogin();
//        }

    }

    //call custom menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    // menu actions
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //get clicked item
        switch (item.getItemId()) {
            case R.id.logout_btn:
                logOut();
                return true;
            case R.id.account_btn:
                sendToSetup();
                return true;
            case R.id.post_btn:
                post();
                return true;
            default:
                return false;
        }
    }

    // --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- //
    private void logOut() {
        mAuth.signOut();
        sendToLogin();
    }

    private void sendToLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void sendToSetup() {
        Intent setupIntent = new Intent(MainActivity.this, SetupActivity.class);
        startActivity(setupIntent);
    }

    private void post() {
        Intent postIntent = new Intent(MainActivity.this, PostController.class);
        startActivity(postIntent);
    }

}