package com.tsml.hkl.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class DataSave {

    private Context context;
    private SharedPreferences sp;

    public DataSave(Context context) {
        this.context = context;
        sp = getSp();
    }

    public SharedPreferences getSp() {
        if (sp == null) {
            sp = context.getSharedPreferences("gfuyewgufe", Context.MODE_PRIVATE);
        }
        return sp;
    }

    public void saveInt(String key, int value) {
        SharedPreferences.Editor editor = getSp().edit();
        editor.putInt(key, value);
        editor.apply();


    }

    public void saveString(String key, String value) {
        SharedPreferences.Editor editor = getSp().edit();
        editor.putString(key, value);
        editor.apply();

    }

    public void saveLong(String key, long value) {
        SharedPreferences.Editor editor = getSp().edit();
        editor.putLong(key, value);
        editor.apply();
    }


    public int getInt(String key) {
        return getSp().getInt(key, -1);
    }

    public String getString(String key) {
        return getSp().getString(key, "");
    }

    public long getLong(String key) {
        return getSp().getLong(key, -1);
    }
}
