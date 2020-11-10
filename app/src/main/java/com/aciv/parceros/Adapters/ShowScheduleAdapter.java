package com.aciv.parceros.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aciv.parceros.Models.Database.Service;
import com.aciv.parceros.Models.Local.HoursRange;
import com.aciv.parceros.Models.Local.ShowScheduleItem;
import com.aciv.parceros.R;

import java.util.List;

public class ShowScheduleAdapter extends ArrayAdapter<ShowScheduleItem> {
    private Context context;
    private List<ShowScheduleItem> data;

    public ShowScheduleAdapter(Context context, List<ShowScheduleItem> data) {
        super(context, 0, data);

        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_show_schedule_day, parent, false);
        }

        ShowScheduleItem scheduleItem = data.get(position);

        TextView dayTV = convertView.findViewById(R.id.dayTV);
        TextView hoursTV = convertView.findViewById(R.id.hoursTV);

        String[] daysArray = context.getResources().getStringArray(R.array.days);

        if(scheduleItem != null){
            dayTV.setText(daysArray[scheduleItem.getDay()]);

            StringBuilder hoursText = new StringBuilder();

            for(HoursRange hours: scheduleItem.getHours()){
                hoursText.append(hours.getStart_hour()).append(" - ").append(hours.getEnd_hour()).append("\n");
            }

            hoursTV.setText(hoursText.toString());
        }
        return convertView;
    }
}
