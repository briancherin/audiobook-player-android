package com.corson.audiobookplayer.api;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

class OfflineDataStore implements DataStore {

    Context context;
    SharedPreferences sharedPreferences;

    public OfflineDataStore(Context context) {
        this.context = context;
        sharedPreferences = ((Activity)context).getPreferences(Context.MODE_PRIVATE);
    }

    @Override
    public void saveKeyValue(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    @Override
    public void saveKeyValue(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    @Override
    public String getKeyValueString(String key) {
        if (!sharedPreferences.contains(key)) return null;

        String defaultValue = "";
        return sharedPreferences.getString(key, defaultValue);
    }

    @Override
    public int getKeyValueInt(String key) {
        if (!sharedPreferences.contains(key)) return -1;

        int defaultValue = -1;
        return sharedPreferences.getInt(key, defaultValue);
    }


}
