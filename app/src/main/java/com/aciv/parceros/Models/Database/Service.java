package com.aciv.parceros.Models.Database;

import java.util.ArrayList;
import java.util.List;

public class Service {
    private long id;
    private String name, description, duration;
    private int cost;
    private List<Reaction> reactions;

    public Service() {
        id = 0;
        name = "";
        description = "";
        duration = "";
        cost = -1;
        reactions = new ArrayList<>();
    }

    public Service(long id, String name, String description, String duration, int cost, List<Reaction> reactions) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.cost = cost;
        this.reactions = reactions;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public List<Reaction> getReactions() {
        return reactions;
    }

    public void setReactions(List<Reaction> reactions) {
        this.reactions = reactions;
    }
}
