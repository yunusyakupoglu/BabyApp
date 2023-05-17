package com.example.babyapp.views;

import static com.example.babyapp.adapters.MainAdapter.getPost;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.babyapp.R;
import com.example.babyapp.adapters.MainAdapter;
import com.example.babyapp.enums.TEXT_TYPE;
import com.example.babyapp.repositories.db.BabyAppDatabase;
import com.example.babyapp.repositories.db.daos.PostDao;
import com.example.babyapp.repositories.db.daos.ReplyDao;
import com.example.babyapp.repositories.db.daos.TextDao;
import com.example.babyapp.repositories.db.entities.Post;
import com.example.babyapp.repositories.db.entities.Text;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


public class MainActivity extends AppCompatActivity {

    private RecyclerView rv;
    private FloatingActionButton fab;


    private PostDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        registerEventHandlers();
        swipeAction();
        loadData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

      //  if (requestCode == REQUEST_CODE_YENI_GONDERI && resultCode == RESULT_OK) {
            // Yeni bir gönderi eklendi, yapılacak işlemler
            // ...
        //}
    }

    private void initComponents(){
        rv = findViewById(R.id.recyclerViewMain);
        fab = findViewById(R.id.floatingActionButton2);

        BabyAppDatabase db = BabyAppDatabase.getDatabase(MainActivity.this);
        dao = db.postDao();
    }

    private void loadData(){
        List<Post> posts = dao.loadAllPosts();
        MainAdapter adapter = new MainAdapter(posts, getApplicationContext(),MainActivity.this);
        rv.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        rv.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
        MainAdapter adapter = new MainAdapter(posts, getApplicationContext(),MainActivity.this);
        rv.setAdapter(adapter);
    }


    private void registerEventHandlers(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewPostActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up, R.anim.stay);
            }
        });

    }


}