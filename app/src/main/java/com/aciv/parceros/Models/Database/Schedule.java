package com.aciv.parceros.Models.Database;

import java.util.ArrayList;
import java.util.List;

public class Schedule {
    private String start_hour, end_hour;
    private List<ScheduleDay> days;

    public Schedule(String start_hour, String end_hour, List<ScheduleDay> days) {
        this.start_hour = start_hour;
        this.end_hour = end_hour;
        this.days = days;
    }

    public String getStart_hour() {
        return start_hour;
    }

    public void setStart_hour(String start_hour) {
        this.start_hour = start_hour;
    }

    public String getEnd_hour() {
        return end_hour;
    }

    public void setEnd_hour(String end_hour) {
        this.end_hour = end_hour;
    }

    public List<ScheduleDay> getDays() {
        return days;
    }

    public void setDays(List<ScheduleDay> days) {
        this.days = days;
    }
}
