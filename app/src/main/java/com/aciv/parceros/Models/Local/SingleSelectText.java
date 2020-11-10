package com.aciv.parceros.Models.Local;

import java.util.ArrayList;
import java.util.List;

public class SingleSelectText extends SelectText{

    public SingleSelectText(String name) {
        super(name);
    }

    public static void deselect(List<SelectText> list){
        for(SelectText item: list)
            if(item.isSelected()){
                item.setSelected(false);
                break;
            }
    }

    public static int getSelected(List<SelectText> list){
        for(SelectText item: list)
            if(item.isSelected())
                return list.indexOf(item);

        return -1;
    }

    public static String getSelectedName(List<SelectText> list){
        for(SelectText item: list)
            if(item.isSelected())
                return item.getName();

        return "";
    }

    public static int getByName(List<SelectText> list, String name){
        for(SelectText item: list)
            if(item.getName().equals(name))
                return list.indexOf(item);

        return -1;
    }
}
