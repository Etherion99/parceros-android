package com.aciv.parceros.Adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.aciv.parceros.Models.Local.SelectText;
import com.aciv.parceros.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class SimpleListAdapter extends ArrayAdapter<SelectText> {
    private Context context;
    private List<SelectText> data;
    private int gravity;

    public SimpleListAdapter(Context context, List<SelectText> data, int gravity) {
        super(context, 0, data);

        this.context = context;
        this.data = data;
        this.gravity = gravity;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_simple, parent, false);
        }

        SelectText item = data.get(position);

        TextView nameTV = convertView.findViewById(R.id.nameTV);

        nameTV.setText(item.getName());
        nameTV.setGravity(gravity);

        if(item.isSelected()) {
            nameTV.setBackgroundColor(ContextCompat.getColor(context, R.color.accentGradient));
            nameTV.setTextColor(ContextCompat.getColor(context, R.color.white));
        }else {
            nameTV.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent));
            nameTV.setTextColor(ContextCompat.getColor(context, R.color.text));
        }

        return convertView;
    }
}
