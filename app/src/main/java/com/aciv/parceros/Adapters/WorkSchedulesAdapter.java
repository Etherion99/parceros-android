package com.aciv.parceros.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.aciv.parceros.Models.Database.Schedule;
import com.aciv.parceros.Models.Database.ScheduleDay;
import com.aciv.parceros.Models.Local.ScheduleItem;
import com.aciv.parceros.R;

import java.util.ArrayList;
import java.util.List;

public class WorkSchedulesAdapter extends ArrayAdapter<ScheduleItem> {
    private Context context;
    private List<ScheduleItem> schedules;

    public WorkSchedulesAdapter(Context context, List<ScheduleItem> schedules) {
        super(context, 0, schedules);
        this.context = context;
        this.schedules = schedules;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_work_schedule, parent, false);
        }

        ScheduleItem scheduleItem = schedules.get(position);

        TextView daysTV = convertView.findViewById(R.id.daysTV);
        TextView hoursTV = convertView.findViewById(R.id.hoursTV);

        String[] daysArray = context.getResources().getStringArray(R.array.days);
        StringBuilder days = new StringBuilder();

        for(ScheduleDay scheduleDay: scheduleItem.getSchedule().getDays()){
            days.append(daysArray[scheduleDay.getDay()].substring(0, 3));
            days.append(", ");
        }

        days.delete(days.length() - 2, days.length());

        daysTV.setText(days);
        hoursTV.setText(scheduleItem.getSchedule().getStart_hour() + " - " + scheduleItem.getSchedule().getEnd_hour());

        if(scheduleItem.isActivated()){
            daysTV.setTextColor(ContextCompat.getColor(context, R.color.accentGradient));
        }else{
            daysTV.setTextColor(ContextCompat.getColor(context, R.color.text));
        }

        return convertView;
    }
}
