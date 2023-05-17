package com.example.babyapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.babyapp.R;
import com.example.babyapp.repositories.db.BabyAppDatabase;
import com.example.babyapp.repositories.db.daos.ReplyDao;
import com.example.babyapp.repositories.db.entities.Post;
import com.example.babyapp.repositories.db.entities.Reply;
import com.example.babyapp.repositories.db.entities.Text;
import com.example.babyapp.views.DetailActivity;
import com.example.babyapp.views.MainActivity;
import com.example.babyapp.views.ReplyActivity;

import java.io.Serializable;
import java.util.List;

public class TextAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static List<Text> texts;

    private Context context;

    private MainActivity mainActivity;

    public TextAdapter(List<Text> texts, Context context, MainActivity mainActivity) {
        this.texts = texts;
        this.context = context;
        this.mainActivity = mainActivity;
    }

    public class TextCardViewDesignHolder extends RecyclerView.ViewHolder{

        private TextView txtDescription, txtReplyCount;
        private CardView cardView;
        private ReplyDao replyDao;

        public TextCardViewDesignHolder(@NonNull View itemView) {
            super(itemView);
            txtDescription = itemView.findViewById(R.id.txtTextDescription);
            txtReplyCount = itemView.findViewById(R.id.txtReplyCount);
            cardView = itemView.findViewById(R.id.cardViewText);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.text_card_design,parent,false);
        return new TextCardViewDesignHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Text text = texts.get(position);
        TextCardViewDesignHolder vh = (TextCardViewDesignHolder) holder;
        BabyAppDatabase db = BabyAppDatabase.getDatabase(context);
        vh.replyDao = db.replyDao();
        vh.txtDescription.setText(text.getDescription());
        vh.txtReplyCount.setText((vh.replyDao.countRepliesByTextId(text.getId())+" Yanıt"));
        vh.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReplyActivity.class);
                intent.putExtra("text", (Serializable) text);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return texts.size();
    }

    @Override
    public long getItemId(int position) {
        Text text = texts.get(position);
        return text.getId();
    }

    public static Text getText(int position){
        Text text = texts.get(position);
        return text;
    }
}
