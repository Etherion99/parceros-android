package com.aciv.parceros.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aciv.parceros.Models.Database.Profession;
import com.aciv.parceros.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class SimpleRVAdapter extends RecyclerView.Adapter<SimpleRVAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Profession> professions;
    private int selected;
    private ItemClickListener clickListener;

    public SimpleRVAdapter(Context context, ArrayList<Profession> professions, int selected, ItemClickListener clickListener){
        this.context = context;
        this.professions = professions;
        this.selected = selected;
        this.clickListener = clickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView nameTV;
        ItemClickListener clickListener;

        ViewHolder(@NonNull View itemView, ItemClickListener clickListener) {
            super(itemView);
            this.nameTV = itemView.findViewById(R.id.nameTV);

            this.clickListener = clickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onRVItemClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.list_item_simple_gradient, parent, false);

        return new ViewHolder(itemView, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameTV.setText(professions.get(position).getName());

        if(selected == professions.get(position).getId()){
            holder.nameTV.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.nameTV.setBackground(ContextCompat.getDrawable(context, R.drawable.gradient));
        }
    }

    @Override
    public int getItemCount() {
        return professions.size();
    }

    public interface ItemClickListener{
        void onRVItemClick(int position);
    }
}
