package com.aciv.parceros.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aciv.parceros.Models.Database.User;
import com.aciv.parceros.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class SearchUserAdapter extends ArrayAdapter<User> {
    private Context context;
    private List<User> data;

    public SearchUserAdapter(Context context, List<User> data) {
        super(context, 0, data);

        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_search_user, parent, false);
        }

        User user = data.get(position);

        TextView nameTV = convertView.findViewById(R.id.nameTV);
        TextView professionTV = convertView.findViewById(R.id.professionTV);
        ImageView pictureIV = convertView.findViewById(R.id.pictureIV);

        if(user != null){
            nameTV.setText(user.getName());
            Glide.with(context)
                    .load("http://www.acivp.hol.es/backend/api/profile/picture/" + user.getId())
                    .placeholder(R.drawable.v_main_rounded)
                    .error(R.drawable.v_button_alt)
                    .centerCrop()
                    .into(pictureIV);

            if(user.getProfile() != null){

                if(user.getProfile().getProfession() != null){
                    professionTV.setText(user.getProfile().getProfession().getName());
                }
            }
        }
        return convertView;
    }
}
