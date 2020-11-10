package com.aciv.parceros.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.aciv.parceros.Models.Database.Comment;
import com.aciv.parceros.R;

import java.util.List;

public class ShowCommentAdapter extends RecyclerView.Adapter<ShowCommentAdapter.ViewHolder> {
    private Context context;
    private List<Comment> comments;

    public ShowCommentAdapter(Context context, List<Comment> comments){
        this.context = context;
        this.comments = comments;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView nameTV, contentTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.nameTV = itemView.findViewById(R.id.nameTV);
            this.contentTV = itemView.findViewById(R.id.contentTV);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.list_item_show_comment, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = comments.get(position);

        holder.nameTV.setText(comment.getCreated_by().getName());
        holder.contentTV.setText(comment.getContent());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
}
