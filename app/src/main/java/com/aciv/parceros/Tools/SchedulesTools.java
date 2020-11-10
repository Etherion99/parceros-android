package com.aciv.parceros.Tools;

import com.aciv.parceros.Models.Database.Schedule;
import com.aciv.parceros.Models.Database.ScheduleDay;
import com.aciv.parceros.Models.Local.HoursRange;
import com.aciv.parceros.Models.Local.ScheduleItem;
import com.aciv.parceros.Models.Local.ShowScheduleItem;

import java.util.ArrayList;
import java.util.List;

public class SchedulesTools {
    public static List<ShowScheduleItem> scheduleDBToItem(List<Schedule> schedules){
        List<ShowScheduleItem> items = new ArrayList<>();

        for(Schedule schedule: schedules){
            for(ScheduleDay day: schedule.getDays()){
                int index = checkDay(items, day.getDay());

                if(index != -1){
                    items.get(index).getHours().add(new HoursRange(schedule.getStart_hour(), schedule.getEnd_hour()));
                }else{
                    List<HoursRange> hours = new ArrayList<>();
                    hours.add(new HoursRange(schedule.getStart_hour(), schedule.getEnd_hour()));
                    items.add(new ShowScheduleItem(day.getDay(), hours));
                }
            }
        }

        ShowScheduleItem aux;

        for(int i = 0; i < items.size() - 1; i++){
            for(int j = 0; j < items.size() - i - 1; j++){
                if(items.get(j+1).getDay() < items.get(j).getDay()){
                    aux = items.get(j+1);
                    items.set(j+1, items.get(j));
                    items.set(j, aux);
                }
            }
        }

        return items;
    }

    private static int checkDay(List<ShowScheduleItem> items, int day){
        for(ShowScheduleItem item: items)
            if(item.getDay() == day)
                return items.indexOf(item);

        return -1;
    }
}
