package com.aciv.parceros.Models.Local;

import com.aciv.parceros.Models.Database.Schedule;

public class ScheduleItem {
    private Schedule schedule;
    private boolean activated;

    public ScheduleItem(Schedule schedule, boolean activated) {
        this.schedule = schedule;
        this.activated = activated;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }
}
