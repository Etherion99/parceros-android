package com.aciv.parceros.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aciv.parceros.Models.Database.User;
import com.aciv.parceros.Models.Firebase.Chat;
import com.aciv.parceros.R;

import java.util.List;

public class ChatAdapter extends ArrayAdapter<Chat> {
    private Context context;
    private List<Chat> data;

    public ChatAdapter(Context context, List<Chat> data) {
        super(context, 0, data);

        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_chat, parent, false);
        }

        Chat chat = data.get(position);

        TextView lastMessageTV = convertView.findViewById(R.id.lastMessageTV);

        if(chat != null){
            lastMessageTV.setText(chat.getLast().getContent());
        }

        return convertView;
    }
}
