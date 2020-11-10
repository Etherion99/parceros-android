package com.aciv.parceros.Tools;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SessionTools {
    public static SharedPreferences.Editor getEditor(Activity activity){
        return activity.getPreferences(Context.MODE_PRIVATE).edit();
    }
}
