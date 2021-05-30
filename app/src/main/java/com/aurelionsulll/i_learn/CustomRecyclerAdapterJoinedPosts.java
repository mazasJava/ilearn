package com.aurelionsulll.i_learn;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class CustomRecyclerAdapterJoinedPosts  extends RecyclerView.Adapter<CustomRecyclerAdapterJoinedPosts.ViewHolder>{

    private Context context;
    private List<Post> posts;
    private FirebaseFirestore database;


    public CustomRecyclerAdapterJoinedPosts(Context context, List<Post> posts) {
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
    public void onBindViewHolder(@NonNull CustomRecyclerAdapterJoinedPosts.ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.itemView.setTag(posts.get(position));
        holder.title_post.setText(post.getTitle());
        holder.user_name.setText(String.valueOf(post.getUser().getName()));
        holder.postId = post.getId();
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

        public ViewHolder(View itemView) {
            super(itemView);
            title_post = (TextView) itemView.findViewById(R.id.title_post);
            user_name = (TextView) itemView.findViewById(R.id.user_name);
            interested = (ImageView) itemView.findViewById(R.id.interested);
            image_profile = (ImageView) itemView.findViewById(R.id.image_profile);
            image_post = (ImageView) itemView.findViewById(R.id.image_post);
            interested.setImageResource(R.drawable.ic_check_circle_green);
//            interested.setVisibility(View.GONE);
            interested.setOnClickListener(v->{
//                itemView.setActivated(false);
                interested.setImageResource(R.drawable.ic_add);
            });

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, UpdateUser.class);
                intent.putExtra("id", postId);
                intent.putExtra("show", "1");
                context.startActivity(intent);
            });
        }
    }
}
