package com.aciv.parceros.Models.Database;

public class Phone {
    private String number, indicative;

    public Phone(){
        number = "";
        indicative = "";
    }

    public Phone(String number, String indicative) {
        this.number = number;
        this.indicative = indicative;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getIndicative() {
        return indicative;
    }

    public void setIndicative(String indicative) {
        this.indicative = indicative;
    }

    //custom
    public String getFullNumber(){
        return indicative + number;
    }
}
