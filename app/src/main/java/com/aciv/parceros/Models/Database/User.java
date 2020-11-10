package com.aciv.parceros.Models.Database;

import java.io.Serializable;

public class User implements Serializable {
    private long id;
    private String name, email;
    private Phone phone;
    private Profile profile;

    public User() {
        this.id = 0;
        this.name = "";
        this.email = "";
        this.phone = new Phone();
        this.profile = new Profile();
    }

    public User(long id, String name, String email, Phone phone, Profile profile) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.profile = profile;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
