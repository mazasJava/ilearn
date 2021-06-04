package com.aurelionsulll.i_learn.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aurelionsulll.i_learn.activitys.LoginActivity;
import com.aurelionsulll.i_learn.R;
import com.aurelionsulll.i_learn.models.User;
import com.aurelionsulll.i_learn.models.Post;
import com.aurelionsulll.i_learn.recyclerAdapters.CustomRecyclerAdapter;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHome extends Fragment {
    private RecyclerView recyclerView;
    private MaterialToolbar mainToolBar;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    public static RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    private FirebaseFirestore database;
    List<Post> postList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragmentHome.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentHome newInstance(String param1, String param2) {
        FragmentHome fragment = new FragmentHome();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentHome() {
        // Required empty public constructor
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
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        mAuth = FirebaseAuth.getInstance();
        progressBar = v.findViewById(R.id.home_progressBar);

        mainToolBar = v.findViewById(R.id.main_tool_bar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mainToolBar);
        ((AppCompatActivity) getActivity()).setTitle("I learn");
        mainToolBar.setTitleTextColor(Color.WHITE);
        mainToolBar.setBackground(Drawable.createFromPath("color/mainbgm"));
        setHasOptionsMenu(true);
        progressBar.setVisibility(View.VISIBLE);

        recyclerView = (RecyclerView) v.findViewById(R.id.recycleViewContainer);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        database = FirebaseFirestore.getInstance();
        getPostData();
        return v;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
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

    private void getPostData() {
        database.collection("posts").get().addOnCompleteListener(task -> {
            postList = new ArrayList<Post>();
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Post post = document.toObject(Post.class);
                    post.setId(document.getId());
                    database.collection("users").document(post.getUser_id()).get().addOnSuccessListener(documentSnapshot -> {
                        User user = getUserData(documentSnapshot);
                        post.setUser(user);
                        postList.add(post);
                        mAdapter = new CustomRecyclerAdapter(getContext(), postList);
                        recyclerView.setAdapter(mAdapter);
                    });
                    progressBar.setVisibility(View.INVISIBLE);
                }

            } else {
                Toast.makeText(getContext(), "Fail to get the data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendToLogin() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
    }

    private User getUserData(DocumentSnapshot documentSnapshot) {
        return new User(documentSnapshot.getString("name"), documentSnapshot.getString("image"));
    }
}