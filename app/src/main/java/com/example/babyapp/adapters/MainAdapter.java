package com.example.babyapp.adapters;

import static com.example.babyapp.adapters.TextAdapter.getText;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.babyapp.R;
import com.example.babyapp.enums.TEXT_TYPE;
import com.example.babyapp.repositories.db.BabyAppDatabase;
import com.example.babyapp.repositories.db.daos.TextDao;
import com.example.babyapp.repositories.db.entities.Post;
import com.example.babyapp.repositories.db.entities.Text;
import com.example.babyapp.views.DetailActivity;
import com.example.babyapp.views.DetailsFragment;
import com.example.babyapp.views.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static List<Post> posts;

    private Context context;

    private MainActivity mainActivity;



    public MainAdapter(List<Post> posts, Context context, MainActivity mainActivity) {
        this.posts = posts;
        this.context = context;
        this.mainActivity = mainActivity;
    }


    public class CardViewDesignHolder extends RecyclerView.ViewHolder{
        private TextView txtContent, txtPersonName;
        private CardView cardView;
        private ImageView imgMain, imgAvatar;

        private Button btnComment, btnQuestion;

        private Button btnSendComment, btnSendQuestion;

        private EditText txtCommentInput, txtQuestionInput;
        private TextDao textDao;
        private FirebaseFirestore db;



        public CardViewDesignHolder(@NonNull View itemView) {
            super(itemView);
            txtContent = itemView.findViewById(R.id.txtMainContent);
            cardView = itemView.findViewById(R.id.cardViewMain);
            imgMain = itemView.findViewById(R.id.imgMainImage);
            imgAvatar = itemView.findViewById(R.id.imgPersonAvatar);
            btnComment = itemView.findViewById(R.id.btnMainComment);
            btnQuestion = itemView.findViewById(R.id.btnMainQuestion);
            btnSendComment = itemView.findViewById(R.id.btnSendComment);
            txtCommentInput = itemView.findViewById(R.id.txtCommentInput);
            txtPersonName = itemView.findViewById(R.id.txtPersonName);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_card_design,parent,false);
        return new CardViewDesignHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Post post = posts.get(position);
        MainAdapter.CardViewDesignHolder vh = (MainAdapter.CardViewDesignHolder) holder;

        vh.db = FirebaseFirestore.getInstance();
        DocumentReference userRef = vh.db.collection("user").document(post.getUserId());
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Retrieve user information
                        String userName = document.getString("username");
                        String profilePhoto = document.getString("profilephoto");
                        // Display user information
                        vh.txtPersonName.setText(userName);
                        vh.imgAvatar.setImageURI(Uri.parse(profilePhoto));
                    } else {
                        // Document doesn't exist
                    }
                } else {
                    // An error occurred
                }
            }
        });


        vh.txtContent.setText(post.getTitle());
        vh.imgMain.setImageURI(Uri.parse(post.getFileUri()));
        vh.imgAvatar.setImageURI(Uri.parse(post.getFileUri()));
        BabyAppDatabase db = BabyAppDatabase.getDatabase(context);
        vh.textDao = db.textDao();
        vh.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailsFragment fragment = DetailsFragment.newInstance(post);
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_container, fragment)
                        .commit();
            }
        });

        vh.btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mainActivity, R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(mainActivity).inflate(R.layout.layout_bottom_sheet_comment, null);
                EditText editText = bottomSheetView.findViewById(R.id.txtCommentInput);
                Button button = bottomSheetView.findViewById(R.id.btnSendComment);
                RecyclerView rv = bottomSheetView.findViewById(R.id.rvCommentText);
                loadTexts(post.getId(),TEXT_TYPE.COMMENT.getKey(),rv,vh);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                          Text text = new Text();
                         text.description = editText.getText().toString();
                         text.type = TEXT_TYPE.COMMENT.getKey();
                         text.postId = post.getId();
                         vh.textDao.insert(text);
                        loadTexts(post.getId(),TEXT_TYPE.COMMENT.getKey(),rv,vh);
                        Toast.makeText(mainActivity, "Yorum başarıyla kaydedildi.", Toast.LENGTH_SHORT).show();

                    }
                });
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });



        vh.btnQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog dialog = new BottomSheetDialog(mainActivity, R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(mainActivity).inflate(R.layout.layout_bottom_sheet_question, null);
                EditText editText = bottomSheetView.findViewById(R.id.txtQuestionInput);
                Button button = bottomSheetView.findViewById(R.id.btnSendQuestion);
                RecyclerView rv = bottomSheetView.findViewById(R.id.rvQuestionText);
                loadTexts(post.getId(),TEXT_TYPE.QUESTION.getKey(),rv,vh);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Text text = new Text();
                        text.description = editText.getText().toString();
                        text.type = TEXT_TYPE.QUESTION.getKey();
                        text.postId = post.getId();
                        vh.textDao.insert(text);
                        loadTexts(post.getId(),TEXT_TYPE.QUESTION.getKey(),rv,vh);
                        Toast.makeText(mainActivity,"Soru başarıyla kaydedildi.",Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.setContentView(bottomSheetView);
                dialog.show();
            }
        });
    }

    private void swipeAction(long postId, int TEXT_TYPE, RecyclerView rv, MainAdapter.CardViewDesignHolder vh) {
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
                        vh.textDao.delete(getText(position));
                        rv.getAdapter().notifyItemRemoved(position);
                        loadTexts(postId,TEXT_TYPE,rv,vh);
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
                        .setSwipeLeftLabelColor(context.getResources().getColor(R.color.white))
                        .addSwipeLeftActionIcon(R.drawable.baseline_delete_24)
                        .setSwipeLeftActionIconTint(context.getResources().getColor(R.color.white))
                        .addSwipeLeftBackgroundColor(context.getResources().getColor(com.google.android.material.R.color.design_default_color_error))
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callBackMethod);
        itemTouchHelper.attachToRecyclerView(rv);
    }


    public void loadTexts(long postId,int TEXT_TYPE, RecyclerView rv,MainAdapter.CardViewDesignHolder vh){
        List<Text> texts = vh.textDao.loadTextsByTextTypeAndPostId(TEXT_TYPE,postId);
        TextAdapter adapter = new TextAdapter(texts,context,mainActivity);
        swipeAction(postId,TEXT_TYPE,rv,vh);
        rv.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        rv.addItemDecoration(new DividerItemDecoration(context,LinearLayoutManager.VERTICAL));
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
