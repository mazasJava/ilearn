package com.aurelionsulll.i_learn;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Objects;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddPostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddPostFragment extends Fragment {

    private static final String TAG = "";
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private Uri mainImageURI = null;

    private MaterialToolbar mainToolBar;
    private FirebaseAuth mAuth;


    private ImageView postImage;
    private EditText postTitle;
    private EditText postDescription;
    private Button createBtn;
    private String user_id;

    private String post_id = UUID.randomUUID().toString();

    private StorageReference storageReference;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddPostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JoinedPostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddPostFragment newInstance(String param1, String param2) {
        AddPostFragment fragment = new AddPostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_add_post, container, false);

        mAuth = FirebaseAuth.getInstance();

        mainToolBar = view.findViewById(R.id.main_tool_bar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mainToolBar);
        ((AppCompatActivity)getActivity()).setTitle("I learn");
        getActivity().setTitleColor(Color.parseColor("#ffffff"));

        setHasOptionsMenu(true);

        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        user_id = firebaseAuth.getCurrentUser().getUid();
        postImage = view.findViewById(R.id.post_image);
        postTitle = view.findViewById(R.id.post_title);
        postDescription = view.findViewById(R.id.post_description);
        createBtn = view.findViewById(R.id.post_btn_create);

        db = FirebaseFirestore.getInstance();

        postImage.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "permission denied", Toast.LENGTH_LONG).show();
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    System.out.println("You already have permision to this ");
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1, 1)
                            .start(getContext(),AddPostFragment.this);
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
                System.out.println(uri);
                System.out.println(downloadUrl);
                createPost(downloadUrl, title,description);
                Fragment fragment = new FragmentHome();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.navHostFragment, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }));
        });
        return  view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        System.out.println("onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            System.out.println(CropImage.getActivityResult(data));
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                assert result != null;
                mainImageURI = result.getUri();
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++" + mainImageURI);

            postImage.setImageURI(mainImageURI);
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++" + mainImageURI);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                assert result != null;
            }
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Logout:
                logOut();
                return true;
            default:
                return false;
        }
    }

    private void logOut() {
        mAuth.signOut();
        sendToLogin();
    }

    private void sendToLogin() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
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