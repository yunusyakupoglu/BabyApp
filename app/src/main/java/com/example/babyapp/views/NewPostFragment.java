package com.example.babyapp.views;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.babyapp.R;
import com.example.babyapp.repositories.db.BabyAppDatabase;
import com.example.babyapp.repositories.db.daos.PostDao;
import com.example.babyapp.repositories.db.entities.Post;
import com.google.android.material.appbar.MaterialToolbar;

import java.io.File;

public class NewPostFragment extends Fragment {
    private String selectedImageData;
    private MaterialToolbar toolbar;
    private EditText txtDescription, txtTitle;
    private ImageButton imgButton;

    private BabyAppDatabase db;

    private MainActivity mainActivity;
    private HomeFragment homeFragment;
    private PostDao dao;

    private static final int IMAGE_PICK_MODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    public NewPostFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_post, container, false);
        initComponents(view);
        registerEventHandlers();

        return view;
    }

    private void initComponents(View view){
        db = BabyAppDatabase.getDatabase(mainActivity);
        dao = db.postDao();
        //  rv = findViewById(R.id.recyclerView_main);
        toolbar = view.findViewById(R.id.materialToolbar);
        txtDescription = view.findViewById(R.id.txtDescription);
        txtTitle = view.findViewById(R.id.txtTitle);
        imgButton = view.findViewById(R.id.imageButton);
    }

    private void registerEventHandlers(){
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (mainActivity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                        //permission not granted, request it.
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        //show popup for runtime permission
                        requestPermissions(permissions,PERMISSION_CODE);
                    }
                    else{
                        //permission already granted
                        pickImageFromGallery();
                    }
                }
                else{
                    // system os is less then marsmallow
                    pickImageFromGallery();
                }
            }
        });
    }

    private void pickImageFromGallery() {
        //intent to pick image
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_MODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    //permission was granted
                    pickImageFromGallery();
                } else {
                    //permission was denied
                    Toast.makeText(mainActivity, "Permission denied...!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == mainActivity.RESULT_OK && requestCode == IMAGE_PICK_MODE) {
            //imageViewFile.setImageURI(data.getData());
            Uri selectedImageUri = data.getData();
            // Get the path from the Uri
            final String path = getPathFromURI(selectedImageUri);
            if (path != null) {
                File f = new File(path);
                selectedImageUri = Uri.fromFile(f);
            }
            imgButton.setImageURI(selectedImageUri);
            selectedImageData = selectedImageUri.toString();
        }
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = mainActivity.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }
    public void onImageButtonClick(View view) {
        Intent imageIntent = new Intent(Intent.ACTION_GET_CONTENT);
        imageIntent.setType("image/*");
        startActivityForResult(imageIntent, 1);
    }

    private void setToolbar(){
        mainActivity.setSupportActionBar(toolbar);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        mainActivity.getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                mainActivity.finish();
                mainActivity.overridePendingTransition(R.anim.stay, R.anim.slide_down);
                return true;
            case R.id.insert:
                Post post = new Post();
                post.description = txtDescription.getText().toString();
                post.title = txtTitle.getText().toString();
                post.fileUri = selectedImageData;
                dao.insert(post);
                Toast.makeText(mainActivity, "Post başarıyla kaydedildi.", Toast.LENGTH_SHORT).show();
                homeFragment.reloadData();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}