package com.example.babyapp.views;

import static com.example.babyapp.adapters.MainAdapter.getPost;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


public class MainActivity extends AppCompatActivity {

    private RecyclerView rv;
    private BottomNavigationView navigationView;


    private PostDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigationView = findViewById(R.id.bottomNavigationView);


        HomeFragment homeFragment = new HomeFragment(this);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_container, homeFragment)
                .commit();
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_home:
                        loadFragment(new HomeFragment(MainActivity.this));
                        return true;
                    case R.id.menu_new:
                        loadFragment(new NewPostFragment(MainActivity.this));
                        return true;
                    case R.id.menu_profile:
                        loadFragment(new ProfileFragment(MainActivity.this));
                        return true;
                    case R.id.menu_settings:
                        loadFragment(new SettingsFragment());
                        return true;

                }
                return false;
            }
        });

    }

    public void loadFragment(Fragment fragment) {
        switch (fragment.getClass().getSimpleName()) {
            case "HomeFragment":
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new HomeFragment(this)).commit();
                break;
            case "ProfileFragment":
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new ProfileFragment(this)).commit();
                break;
            case "SettingsFragment":
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new SettingsFragment()).commit();
                break;
            case "NewPostFragment":
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new NewPostFragment(this)).commit();
                break;
            default:
                Log.e("Fragment", "Invalid Fragment type");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

      //  if (requestCode == REQUEST_CODE_YENI_GONDERI && resultCode == RESULT_OK) {
            // Yeni bir gönderi eklendi, yapılacak işlemler
            // ...
        //}
    }



}