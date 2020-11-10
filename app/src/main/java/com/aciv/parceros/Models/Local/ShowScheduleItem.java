package com.aciv.parceros.Models.Local;

import java.util.List;

public class ShowScheduleItem {
    private int day;
    private List<HoursRange> hours;

    public ShowScheduleItem(int day, List<HoursRange> hours) {
        this.day = day;
        this.hours = hours;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public List<HoursRange> getHours() {
        return hours;
    }

    public void setHours(List<HoursRange> hours) {
        this.hours = hours;
    }
}
