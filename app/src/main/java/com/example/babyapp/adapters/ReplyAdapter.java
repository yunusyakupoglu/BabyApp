package com.example.babyapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.babyapp.R;


import com.example.babyapp.repositories.db.entities.Reply;
import com.example.babyapp.repositories.db.entities.Text;
import com.example.babyapp.views.MainActivity;

import java.util.List;

public class ReplyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static List<Reply> replies;

    private Context context;
    public ReplyAdapter(List<Reply> replies ,Context context) {
        this.replies = replies;
        this.context = context;
    }

    public class ReplyCardViewDesignHolder extends RecyclerView.ViewHolder{

        private CardView cardView;
        private TextView txtReply;
        public ReplyCardViewDesignHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardViewReply);
            txtReply = itemView.findViewById(R.id.txtReplyCard);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reply_card_design,parent,false);
        return new ReplyAdapter.ReplyCardViewDesignHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Reply reply = replies.get(position);
        ReplyCardViewDesignHolder vh = (ReplyCardViewDesignHolder) holder;
        vh.txtReply.setText(reply.getReply());

    }

    @Override
    public int getItemCount() {
        return replies.size();
    }

    @Override
    public long getItemId(int position) {
        Reply reply = replies.get(position);
        return reply.getId();
    }
}
