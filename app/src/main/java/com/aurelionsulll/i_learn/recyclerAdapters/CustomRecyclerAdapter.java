package com.aurelionsulll.i_learn.recyclerAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aurelionsulll.i_learn.R;
import com.aurelionsulll.i_learn.models.Joined;
import com.aurelionsulll.i_learn.models.Post;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class CustomRecyclerAdapter extends RecyclerView.Adapter<CustomRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Post> posts;
    private FirebaseFirestore database;


    public CustomRecyclerAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
        database = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomRecyclerAdapter.ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.itemView.setTag(posts.get(position));
        holder.title_post.setText(post.getTitle());
//        holder.txtDate.setText(post.getDescription());
        holder.user_name.setText(String.valueOf(post.getUser().getName()));
        holder.postId = post.getId();
        holder.postUserID = post.getUser_id();
        Glide.with(context).load(post.getUser().getImage()).into(holder.image_profile);
        Glide.with(context).load(post.getImage()).into(holder.image_post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title_post;
        public TextView txtDate;
        public TextView user_name;
        public ImageView interested;
        public ImageView image_profile;
        public ImageView image_post;
        public String postId;
        public String postUserID;
        Boolean checked = false;


        public Boolean getChecked() {
            return checked;
        }

        public void setChecked(Boolean checked) {
            this.checked = checked;
        }

        public ViewHolder(View itemView) {
            super(itemView);
            title_post = (TextView) itemView.findViewById(R.id.title_post);
//            txtDate = (TextView) itemView.findViewById(R.id.txtDate);
            user_name = (TextView) itemView.findViewById(R.id.user_name);
            interested = (ImageView) itemView.findViewById(R.id.interested);
            image_profile = (ImageView) itemView.findViewById(R.id.image_profile);
            image_post = (ImageView) itemView.findViewById(R.id.image_post);
            interested.setImageResource(R.drawable.ic_add);

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference collection = db.collection("joined");
            CollectionReference postCollection = db.collection("posts");


            DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference("joined");
            scoresRef.keepSynced(true);

            //get the joined post by the current user
            Query getUserId = collection.whereEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());


            getUserId.get().addOnCompleteListener(task -> {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    if (document.get("userId").equals(postUserID)) {
                        interested.setVisibility(View.GONE);
                    }
                    task.getResult().getQuery().whereEqualTo("postId", postId).addSnapshotListener((value, error) -> {
                        if (document.get("postId").equals(postId)) {
                            interested.setImageResource(R.drawable.ic_check_circle_green);
                            interested.setClickable(false);
                        }
                    });
                }
            });

            interested.setOnClickListener(v -> {
                    System.out.println("true");
                    database.collection("joined").add(new Joined(this.postId, FirebaseAuth.getInstance().getCurrentUser().getUid()));
                    interested.setImageResource(R.drawable.ic_check_circle_green);
                    Toast.makeText(context, "ADDED SUCCESSFULLY TO CLIPBOARD", Toast.LENGTH_LONG).show();
                    interested.setClickable(false);
            });
        }
    }
}
