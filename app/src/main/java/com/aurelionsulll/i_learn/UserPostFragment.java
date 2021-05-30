package com.aurelionsulll.i_learn;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserPostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserPostFragment extends Fragment {

    private RecyclerView recyclerView;
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

    public UserPostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserPostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserPostFragment newInstance(String param1, String param2) {
        UserPostFragment fragment = new UserPostFragment();
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
        View view = inflater.inflate(R.layout.fragment_user_post, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycleViewContainer);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        database = FirebaseFirestore.getInstance();
        getPostDataCreatedByUser();

        return view;
    }

    private void getPostDataCreatedByUser() {
        database.collection("posts").whereEqualTo("user_id", FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(task -> {
            postList = new ArrayList<Post>();
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Post post = document.toObject(Post.class);
                    post.setId(document.getId());
                    database.collection("users").document(post.getUser_id()).get().addOnSuccessListener(documentSnapshot -> {
                        User user = getUserData(documentSnapshot);
                        post.setUser(user);
                        postList.add(post);
                        mAdapter = new CustomRecyclerAdapterUserPosts(getContext(), postList);
                        recyclerView.setAdapter(mAdapter);
                    });
                }

            } else {
                Toast.makeText(getContext(), "Fail to get the data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private User getUserData(DocumentSnapshot documentSnapshot) {
        return new User(documentSnapshot.getString("name"), documentSnapshot.getString("image"));
    }
}