package com.aurelionsulll.i_learn.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.aurelionsulll.i_learn.R;
import com.aurelionsulll.i_learn.models.Post;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

public class UpdateUserActivity extends AppCompatActivity {
    private ImageView imagePost;
    private EditText titlePost, descriptionPost;
    private Button saveButton, cancelButton;
    private FirebaseFirestore database;
    private Intent intent;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);
        progressBar = findViewById(R.id.login_progressBar);
        imagePost = findViewById(R.id.imagePost);
        titlePost = findViewById(R.id.titlePost);
        descriptionPost = findViewById(R.id.descriptionPost);
        cancelButton = findViewById(R.id.cancelButton);
        saveButton = findViewById(R.id.saveButton);
        database = FirebaseFirestore.getInstance();
        intent = getIntent();
        getPostDataCreatedByUser(intent.getStringExtra("id"));
        if (intent.getStringExtra("show").equals("1")) {
            cancelButton.setVisibility(View.GONE);
            saveButton.setVisibility(View.GONE);
        } else {

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        saveButton.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            post.setTitle(titlePost.getText().toString());
            post.setDescription(descriptionPost.getText().toString());
            database.collection("posts").document(intent.getStringExtra("id")).set(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(UpdateUserActivity.this, "Updated Successfully",
                                Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    } else
                        System.out.println("not working");
                }
            });
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });

        cancelButton.setOnClickListener(v -> {
//            FragmentHome fragment = new FragmentHome();
//            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.replace(R.id.navHostFragment, fragment);
//            fragmentTransaction.addToBackStack(null);
//            fragmentTransaction.commit();t
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });
    }

    private Post post;

    private void getPostDataCreatedByUser(String postId) {
        database.collection("posts").document(postId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                post = task.getResult().toObject(Post.class);
                titlePost.setText(post.getTitle());
                descriptionPost.setText(post.getDescription());
                Glide.with(UpdateUserActivity.this).load(post.getImage()).into(imagePost);
            } else {
                Toast.makeText(getApplicationContext(), ".", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
