package com.aciv.parceros.Models.Database;

import java.util.List;

public class SignupUser {
    private User user;
    private Profile profile;
    private List<Schedule> schedules;
    private Location location;
    private List<Service> services;

    public SignupUser(User user, Profile profile, List<Schedule> schedules, Location location, List<Service> services) {
        this.user = user;
        this.profile = profile;
        this.schedules = schedules;
        this.location = location;
        this.services = services;
    }
}
