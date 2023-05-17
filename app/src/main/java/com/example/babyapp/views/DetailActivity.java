package com.example.babyapp.views;

import androidx.appcompat.app.AppCompatActivity;
import com.example.babyapp.R;
import com.example.babyapp.repositories.db.entities.Post;
import com.example.babyapp.repositories.db.entities.Text;

import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    private TextView txtComment;
    private Post post;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initComponents();
        loadData();
    }

    private void initComponents(){
        txtComment = findViewById(R.id.txtCommentDetails);

    }

    private void loadData(){
        post = (Post) getIntent().getSerializableExtra("nesne");
        txtComment.setText("id: " + post.getId()
                + "\n"
                + "title: " + post.getTitle()
                + "\n"
                + "description: " + post.getDescription()
                + "\n"
                + "fileUri: " + post.getFileUri());

    }
}