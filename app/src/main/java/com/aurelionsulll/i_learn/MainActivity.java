package com.aurelionsulll.i_learn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         firebaseFirestore = FirebaseFirestore.getInstance();
         firebaseAuth = FirebaseAuth.getInstance();


        /*
        Auth middleware
         */
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            sendToLoginActivity();
        }
        else{
            user_id = firebaseAuth.getCurrentUser().getUid();
            firebaseFirestore.collection("users").document(user_id).get().addOnCompleteListener(task -> {
                if (!task.getResult().exists()) {
                    sendToProfileActivity();
                }
            });
        }
        BottomNavigationView navView = findViewById(R.id.bottomNav_view);
        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(navView, navController);

    }

    private void sendToLoginActivity() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void sendToProfileActivity() {
        Intent intent = new Intent(MainActivity.this,SetupActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onStart() {
        super.onStart();

    }
}