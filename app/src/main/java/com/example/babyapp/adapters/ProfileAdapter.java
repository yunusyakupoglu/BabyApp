package com.example.babyapp.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.babyapp.R;
import com.example.babyapp.repositories.db.entities.Post;
import com.example.babyapp.views.ProfileFragment;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static List<Post> posts;

    public ProfileAdapter(List<Post> posts) {
        this.posts = posts;
    }

    public class ProfileCardViewDesignHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;

        public ProfileCardViewDesignHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imgProfilePost);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_card_design,parent,false);
        return new ProfileAdapter.ProfileCardViewDesignHolder(itemView);    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Post post = posts.get(position);

        ProfileAdapter.ProfileCardViewDesignHolder vh = (ProfileCardViewDesignHolder) holder;
        vh.imageView.setImageURI(Uri.parse(post.getFileUri()));
    }



    @Override
    public int getItemCount() {
        return posts.size();
    }

    @Override
    public long getItemId(int position) {
        Post post = posts.get(position);
        return post.getId();
    }
    public static Post getPost(int position){
        Post post = posts.get(position);
        return post;
    }
}
