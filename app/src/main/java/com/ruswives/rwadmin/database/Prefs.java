package com.ruswives.rwadmin.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.ruswives.rwadmin.Consts;

public class Prefs {

    private SharedPreferences prefs;
    private Context context;

    public Prefs(Context context) {
        this.context = context;
        String PREFS_FILE_NAME = "russian_wives_app";
        prefs = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
    }


    public String getValue(String key) {
        return prefs.getString(key, Consts.DEFAULT);
    }

    public void setValue(String key, String value) {
        prefs.edit().putString(key, value).apply();
    }

}
