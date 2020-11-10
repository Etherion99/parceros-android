package com.aciv.parceros.Models.Local;

public class HoursRange {
    private String start_hour, end_hour;

    public HoursRange(String start_hour, String end_hour) {
        this.start_hour = start_hour;
        this.end_hour = end_hour;
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
}
