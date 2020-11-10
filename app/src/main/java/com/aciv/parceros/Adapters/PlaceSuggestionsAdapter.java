package com.aciv.parceros.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aciv.parceros.Models.Local.PlaceSuggestion;
import com.aciv.parceros.R;

import java.util.List;

public class PlaceSuggestionsAdapter extends ArrayAdapter<PlaceSuggestion> {
    private Context context;
    private List<PlaceSuggestion> data;

    public PlaceSuggestionsAdapter(Context context, List<PlaceSuggestion> data) {
        super(context, 0, data);

        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_place_suggestion, parent, false);
        }

        PlaceSuggestion suggestion = data.get(position);

        TextView suggestionTV = convertView.findViewById(R.id.suggestionTV);

        suggestionTV.setText(suggestion.getName());

        return convertView;
    }
}
