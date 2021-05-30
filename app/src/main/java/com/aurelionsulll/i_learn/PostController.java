package com.aurelionsulll.i_learn;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;


public class PostController extends AppCompatActivity {

    private static final String TAG = "";
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private Uri mainImageURI = null;


    private ImageView postImage;
    private EditText postTitle;
    private EditText postDescription;
    private Button createBtn;
    private String user_id;

    private String post_id = UUID.randomUUID().toString();

    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add);

        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        user_id = firebaseAuth.getCurrentUser().getUid();
        postImage = findViewById(R.id.post_image);
        postTitle = findViewById(R.id.post_title);
        postDescription = findViewById(R.id.post_description);
        createBtn = findViewById(R.id.post_btn_create);

        db = FirebaseFirestore.getInstance();

        postImage.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(PostController.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(PostController.this, "permission denied", Toast.LENGTH_LONG).show();
                    ActivityCompat.requestPermissions(PostController.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    System.out.println("You already have permision to this ");
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1, 1)
                            .start(PostController.this);
                }
            }
        });

        createBtn.setOnClickListener(v -> {
            String title = postTitle.getText().toString();
            String description = postDescription.getText().toString();
                    user_id = firebaseAuth.getCurrentUser().getUid();
                    StorageReference image_path = storageReference.child("post_image").child(post_id + ".jpg");
                    image_path.putFile(mainImageURI).addOnSuccessListener(taskSnapshot -> image_path.getDownloadUrl().addOnSuccessListener(uri -> {
                        final Uri downloadUrl = uri;
                        createPost(downloadUrl, title,description);
                    }));
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                assert result != null;
                mainImageURI = result.getUri();
                postImage.setImageURI(mainImageURI);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                assert result != null;
                Exception error = result.getError();
            }
        }
    }

    private void createPost(Uri uri, String title, String description) {
        Uri download_uri;
        if (uri != null) {
            System.out.println("create profile with image");
            download_uri = uri;
        } else {
            download_uri = mainImageURI;
        }
        Post newPost = new Post(title,
                description,
                Objects.requireNonNull(user_id),
                download_uri.toString());
        db.collection("posts").add(newPost);
    }

}
