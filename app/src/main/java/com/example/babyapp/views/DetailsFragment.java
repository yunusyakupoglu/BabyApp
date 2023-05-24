package com.example.babyapp.views;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.babyapp.R;
import com.example.babyapp.repositories.db.BabyAppDatabase;
import com.example.babyapp.repositories.db.daos.PostDao;
import com.example.babyapp.repositories.db.entities.Post;


public class DetailsFragment extends Fragment {

private TextView txtDescription, txtTitle;
private ImageView imgPost;

    private Post post;
    public DetailsFragment() {
        // Required empty public constructor
    }

    public static DetailsFragment newInstance(Post post){
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable("data",post);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            post = (Post) getArguments().getSerializable("data");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        txtDescription = view.findViewById(R.id.txtDetailDescription);
        txtTitle = view.findViewById(R.id.txtDetailTitle);
        imgPost = view.findViewById(R.id.imgDetailImage);
        loadData();
        return view;
    }

    private void loadData(){
        imgPost.setImageURI(Uri.parse(post.getFileUri()));
        txtTitle.setText(post.getTitle());
        txtDescription.setText(post.getDescription());
    }
}