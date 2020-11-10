package com.aciv.parceros.Models.Database;

import com.google.gson.annotations.SerializedName;

public class Reaction {
    private long id;

    @SerializedName("user_id")
    private long userId;

    @SerializedName("service_id")
    private long serviceId;

    private User user;
    private Service service;
    private boolean like;

    public Reaction() {
        id = 0;
        userId = 0;
        user = null;
        serviceId = 0;
        service = null;
        like = false;
    }

    public Reaction(long userId, long serviceId, boolean like) {
        id = 0;
        user = null;
        service = null;

        this.userId = userId;
        this.serviceId = serviceId;
        this.like = like;
    }

    public Reaction(long id, User user, Service service, boolean like) {
        this.id = id;
        this.user = user;
        this.service = service;
        this.like = like;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }
}
