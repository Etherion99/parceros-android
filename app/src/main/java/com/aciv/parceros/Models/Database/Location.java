package com.aciv.parceros.Models.Database;

public class Location {
    private double lat, lng;
    private boolean work_at_loc, travel_to_work;
    private int max_distance;

    public Location() {
        lat = -1;
        lng = -1;
        work_at_loc = false;
        travel_to_work = false;
        max_distance = 0;
    }

    public Location(double lat, double lng, boolean work_at_loc, boolean travel_to_work, int max_distance) {
        this.lat = lat;
        this.lng = lng;
        this.work_at_loc = work_at_loc;
        this.travel_to_work = travel_to_work;
        this.max_distance = max_distance;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public boolean isWork_at_loc() {
        return work_at_loc;
    }

    public void setWork_at_loc(boolean work_at_loc) {
        this.work_at_loc = work_at_loc;
    }

    public boolean isTravel_to_work() {
        return travel_to_work;
    }

    public void setTravel_to_work(boolean travel_to_work) {
        this.travel_to_work = travel_to_work;
    }

    public int getMax_distance() {
        return max_distance;
    }

    public void setMax_distance(int max_distance) {
        this.max_distance = max_distance;
    }
}
