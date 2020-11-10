package com.aciv.parceros.Models.Database;

public class Profile {
    private Profession profession;
    private int rest_break, cancellation;
    private boolean work_24_7;
    private String description;

    public Profile(){
        profession = new Profession(-1, "");
        rest_break = -1;
        cancellation = -1;
        work_24_7 = false;
        description = "";
    }

    public Profile(Profession profession, int rest_break, int cancellation, boolean work_24_7, String description) {
        this.profession = profession;
        this.rest_break = rest_break;
        this.cancellation = cancellation;
        this.work_24_7 = work_24_7;
        this.description = description;
    }

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public int getRest_break() {
        return rest_break;
    }

    public void setRest_break(int rest_break) {
        this.rest_break = rest_break;
    }

    public int getCancellation() {
        return cancellation;
    }

    public void setCancellation(int cancellation) {
        this.cancellation = cancellation;
    }

    public boolean isWork_24_7() {
        return work_24_7;
    }

    public void setWork_24_7(boolean work_24_7) {
        this.work_24_7 = work_24_7;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
