package com.aciv.parceros.Models.Local;

import java.util.ArrayList;
import java.util.List;

public class SelectText {
    private String name;
    private boolean selected;

    public SelectText(String name) {
        this.name = name;
        selected = false;
    }

    public static List<SelectText> generateList(String[] data){
        List<SelectText> list = new ArrayList<>();

        for(String item: data)
            list.add(new SelectText(item));

        return list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
