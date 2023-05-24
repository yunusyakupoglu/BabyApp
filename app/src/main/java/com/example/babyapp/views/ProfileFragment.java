package com.example.babyapp.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.babyapp.R;
import com.example.babyapp.adapters.ProfileAdapter;
import com.example.babyapp.repositories.db.BabyAppDatabase;
import com.example.babyapp.repositories.db.daos.PostDao;
import com.example.babyapp.repositories.db.entities.Post;

import java.util.List;


public class ProfileFragment extends Fragment {

    private RecyclerView rv;
    private MainActivity mainActivity;


    public ProfileFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    private PostDao dao;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        rv = view.findViewById(R.id.rvProfile);
        BabyAppDatabase db = BabyAppDatabase.getDatabase(getContext()); // context
        dao = db.postDao();
        List<Post> posts = dao.loadAllPosts();
        ProfileAdapter adapter = new ProfileAdapter(posts);
        rv.setAdapter(adapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mainActivity, 3);
        rv.setLayoutManager(gridLayoutManager);
        rv.setHasFixedSize(true);

        return view;
    }
}