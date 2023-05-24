package com.example.babyapp.views;

import static com.example.babyapp.adapters.MainAdapter.getPost;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.babyapp.R;
import com.example.babyapp.adapters.MainAdapter;
import com.example.babyapp.repositories.db.BabyAppDatabase;
import com.example.babyapp.repositories.db.daos.PostDao;
import com.example.babyapp.repositories.db.entities.Post;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


public class HomeFragment extends Fragment {

    private RecyclerView rv;
    private FloatingActionButton fab;
    private BottomNavigationView navigationView;

    private MainActivity mainActivity;

    public HomeFragment(MainActivity mainActivity) {
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initComponents(view);
        loadData();
        swipeAction();

        return view;
    }

    private void initComponents(View view){
        rv = view.findViewById(R.id.recyclerViewMain);
        BabyAppDatabase db = BabyAppDatabase.getDatabase(getContext()); // context
        dao = db.postDao();
    }

    private void loadData(){
        List<Post> posts = dao.loadAllPosts();
        MainAdapter adapter = new MainAdapter(posts, this.mainActivity,this.mainActivity); // context
        rv.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(this.mainActivity); // context
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        rv.addItemDecoration(new DividerItemDecoration(this.mainActivity,LinearLayoutManager.VERTICAL)); //context
    }

    private void swipeAction(){
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callBackMethod);
        itemTouchHelper.attachToRecyclerView(rv);
    }


    ItemTouchHelper.SimpleCallback callBackMethod = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();

            switch (direction){
                case ItemTouchHelper.LEFT:
                    deletePost(getPost(position));
                    rv.getAdapter().notifyItemRemoved(position);
                    break;
                case ItemTouchHelper.RIGHT:
                    //check or uncheck note

                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c,recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive )
                    .addSwipeLeftLabel("Delete")
                    .setSwipeLeftLabelColor(getResources().getColor(R.color.white))
                    .addSwipeLeftActionIcon(R.drawable.baseline_delete_24)
                    .setSwipeLeftActionIconTint(getResources().getColor(R.color.white))
                    .addSwipeLeftBackgroundColor(getResources().getColor(com.google.android.material.R.color.design_default_color_error))
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    private void deletePost(final Post post){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mainActivity); //context
        builder.setTitle("Emin misiniz?");
        builder.setMessage(post.title + " başlıklı post silinecektir.");
        builder.setIcon(R.drawable.baseline_warning_24);
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == DialogInterface.BUTTON_POSITIVE){
                    dao.delete(post);
                    reloadData();
                    Snackbar snackbar = Snackbar.make(rv, post.title + "başlıklı post veritabanından silindi", Snackbar.LENGTH_SHORT);
                    snackbar.setAction("Geri Al", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dao.insert(post);
                            reloadData();
                        }
                    });
                    //snackbar.show();
                } else {
                    // hiç bir şey yapma
                }
            }
        };
        builder.setPositiveButton("Evet", listener);
        builder.setNegativeButton("Hayır",listener);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void reloadData(){
        List<Post> posts = dao.loadAllPosts();
        MainAdapter adapter = new MainAdapter(posts, this.mainActivity,this.mainActivity); //context
        rv.setAdapter(adapter);
    }


    private void registerEventHandlers(){


    }

}