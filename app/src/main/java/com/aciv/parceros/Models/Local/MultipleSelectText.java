package com.aciv.parceros.Models.Local;

import java.util.ArrayList;
import java.util.List;

public class MultipleSelectText extends SelectText{

    public MultipleSelectText(String name) {
        super(name);
    }

    public static void selectAll(List<SelectText> list, boolean selected){
        for(SelectText item: list)
            item.setSelected(selected);
    }

    public static boolean isOneSelectedAtLeast(List<SelectText> list){
        boolean validator = false;

        for(SelectText item: list)
            if(item.isSelected()){
                validator = true;
                break;
            }

        return validator;
    }

    public static ArrayList<Integer> getSelecteds(List<SelectText> list){
        ArrayList<Integer> selecteds = new ArrayList<>();

        for(SelectText item: list)
            if(item.isSelected())
                selecteds.add(list.indexOf(item));

        return selecteds;
    }
}
