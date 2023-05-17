package com.example.babyapp.views;

import static com.example.babyapp.adapters.MainAdapter.getPost;
import static com.example.babyapp.adapters.TextAdapter.getText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.babyapp.R;
import com.example.babyapp.adapters.MainAdapter;
import com.example.babyapp.adapters.ReplyAdapter;
import com.example.babyapp.repositories.db.BabyAppDatabase;
import com.example.babyapp.repositories.db.daos.PostDao;
import com.example.babyapp.repositories.db.daos.ReplyDao;
import com.example.babyapp.repositories.db.daos.TextDao;
import com.example.babyapp.repositories.db.entities.Post;
import com.example.babyapp.repositories.db.entities.Reply;
import com.example.babyapp.repositories.db.entities.Text;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class ReplyActivity extends AppCompatActivity {

    private RecyclerView rv;
    private MaterialToolbar toolbar;
    private EditText txtReply;
    private Button replyButton;
    private ReplyDao replyDao;

    private Text text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        setToolbar();
    }

    private void setToolbar(){
        text = (Text) getIntent().getSerializableExtra("text");
        toolbar = findViewById(R.id.toolbarReply);
        toolbar.setTitle("Yanıtlar");
        toolbar.setSubtitle(text.getDescription());
        setSupportActionBar(toolbar);
        initComponents();
        registerEventHandlers(text);
        loadData(text.getId());
    }

    private void initComponents(){
        rv = findViewById(R.id.rv_reply);
        txtReply = findViewById(R.id.txtReply);
        replyButton = findViewById(R.id.btnSendReply);

        BabyAppDatabase db = BabyAppDatabase.getDatabase(ReplyActivity.this);
        replyDao = db.replyDao();
    }

    private void registerEventHandlers(Text text){
        replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reply reply = new Reply();
                reply.reply = txtReply.getText().toString();
                reply.textId = text.getId();
                reply.textType = text.getType();
                replyDao.insert(reply);
                reloadData(text.getId());
                Toast.makeText(ReplyActivity.this,"Yanıt başarıyla kaydedildi.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadData(long textId){
        List<Reply> replies = replyDao.loadRepliesByTextId(textId);
        ReplyAdapter adapter = new ReplyAdapter(replies,ReplyActivity.this);
        swipeAction(replies, textId);
        rv.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        rv.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
    }

    private void swipeAction(List<Reply> replies, long textId){
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
                        replyDao.delete(getReply(position, replies));
                        rv.getAdapter().notifyItemRemoved(position);
                        reloadData(textId);
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


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callBackMethod);
        itemTouchHelper.attachToRecyclerView(rv);
    }

    public void reloadData(long textId){
        List<Reply> replies = replyDao.loadRepliesByTextId(textId);
        ReplyAdapter adapter = new ReplyAdapter(replies, ReplyActivity.this);
        rv.setAdapter(adapter);
    }

    public static Reply getReply(int position, List<Reply> replies){
        Reply reply = replies.get(position);
        return reply;
    }

}