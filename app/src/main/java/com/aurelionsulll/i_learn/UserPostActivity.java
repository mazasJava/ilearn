//package com.aurelionsulll.i_learn;
//
//import android.os.Bundle;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.aurelionsulll.i_learn.models.Post;
//import com.aurelionsulll.i_learn.models.User;
//import com.aurelionsulll.i_learn.recyclerAdapter.CustomRecyclerAdapterUserPosts;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class UserPostActivity extends AppCompatActivity {
//    private RecyclerView recyclerView;
//    public static RecyclerView.Adapter mAdapter;
//    RecyclerView.LayoutManager layoutManager;
//    private FirebaseFirestore database;
//    List<Post> postList;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_feed);
//        recyclerView = (RecyclerView) findViewById(R.id.recycleViewContainer);
//        recyclerView.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//        database = FirebaseFirestore.getInstance();
//        getPostDataCreatedByUser();
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//    }
//
//    private void getPostDataCreatedByUser() {
//        database.collection("posts").whereEqualTo("user_id", FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(task -> {
//            postList = new ArrayList<Post>();
//            if (task.isSuccessful()) {
//                for (QueryDocumentSnapshot document : task.getResult()) {
//                    Post post = document.toObject(Post.class);
//                    post.setId(document.getId());
//                    database.collection("users").document(post.getUser_id()).get().addOnSuccessListener(documentSnapshot -> {
//                        User user = getUserData(documentSnapshot);
//                        post.setUser(user);
//                        postList.add(post);
//                        mAdapter = new CustomRecyclerAdapterUserPosts(UserPostActivity.this, postList);
//                        recyclerView.setAdapter(mAdapter);
//                    });
//                }
//
//            } else {
//                Toast.makeText(UserPostActivity.this, "Fail to get the data.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private User getUserData(DocumentSnapshot documentSnapshot) {
//        return new User(documentSnapshot.getString("name"), documentSnapshot.getString("image"));
//    }
//
//}
