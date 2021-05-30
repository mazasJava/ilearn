package com.aurelionsulll.i_learn;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class JoinedPostActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    public static RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    private FirebaseFirestore database;
    List<Post> postList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        recyclerView = (RecyclerView) findViewById(R.id.recycleViewContainer);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        database = FirebaseFirestore.getInstance();

        database.collection("joined").whereEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    getPostDataCreatedByUser(queryDocumentSnapshot.get("postId").toString());
                }
            } else {
                Toast.makeText(JoinedPostActivity.this, "Fail to get the data.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void getPostDataCreatedByUser(String postId) {
        database.collection("posts").document(postId).get().addOnCompleteListener(task -> {
            postList = new ArrayList<Post>();
            if (task.isSuccessful()) {
                Post post = task.getResult().toObject(Post.class);
                post.setId(task.getResult().getId());
                database.collection("users").document(post.getUser_id()).get().addOnSuccessListener(documentSnapshot -> {
                    User user = getUserData(documentSnapshot);
                    post.setUser(user);
                    postList.add(post);
                    mAdapter = new CustomRecyclerAdapterJoinedPosts(JoinedPostActivity.this, postList);
                    recyclerView.setAdapter(mAdapter);
                });

            } else {
                Toast.makeText(JoinedPostActivity.this, "Fail to get the data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private User getUserData(DocumentSnapshot documentSnapshot) {
        return new User(documentSnapshot.getString("name"), documentSnapshot.getString("image"));
    }

}
