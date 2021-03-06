package com.aurelionsulll.i_learn.fragments;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aurelionsulll.i_learn.R;
import com.aurelionsulll.i_learn.models.User;
import com.aurelionsulll.i_learn.models.Post;
import com.aurelionsulll.i_learn.recyclerAdapters.CustomRecyclerAdapterJoinedPosts;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link JoinedPostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JoinedPostFragment extends Fragment {


    private RecyclerView recyclerView;
    public static RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    private FirebaseFirestore database;
    List<Post> postList;
    private ProgressBar progressBar;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public JoinedPostFragment() {
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
    public static JoinedPostFragment newInstance(String param1, String param2) {
        JoinedPostFragment fragment = new JoinedPostFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_joined_post, container, false);
        progressBar = view.findViewById(R.id.joined_post_progressBar);
        postList = new ArrayList<Post>();

        Toolbar mainToolBar = view.findViewById(R.id.main_tool_bar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mainToolBar);
        mainToolBar.setTitleTextColor(Color.WHITE);
        ((AppCompatActivity) getActivity()).setTitle("Joined posts");
        mainToolBar.setBackground(Drawable.createFromPath("color/mainbgm"));
        setHasOptionsMenu(true);
        progressBar.setVisibility(View.VISIBLE);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycleViewContainer);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        database = FirebaseFirestore.getInstance();

        database.collection("joined").whereEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    getPostDataCreatedByUser(queryDocumentSnapshot.get("postId").toString());
                }
                progressBar.setVisibility(View.INVISIBLE);
            } else {
                Toast.makeText(getContext(), "Fail to get the data.", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

    private void getPostDataCreatedByUser(String postId) {
        database.collection("posts").document(postId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Post post = task.getResult().toObject(Post.class);
                post.setId(task.getResult().getId());
                database.collection("users").document(post.getUser_id()).get().addOnSuccessListener(documentSnapshot -> {
                    User user = getUserData(documentSnapshot);
                    post.setUser(user);
                    postList.add(post);
                    mAdapter = new CustomRecyclerAdapterJoinedPosts(getContext(), postList);
                    recyclerView.setAdapter(mAdapter);
                });

            } else {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), "Fail to get the data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private User getUserData(DocumentSnapshot documentSnapshot) {
        return new User(documentSnapshot.getString("name"), documentSnapshot.getString("image"));
    }
}