package com.jawadkhansahil.grossmart;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreference {

    private static final String SHARED_PREF_NAME = "my_shared_pref";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public SharedPreference(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Method to save a string
    public void saveString(String key, String value) {
        editor.putString(key, value);
        editor.apply(); // Asynchronous save
    }

    // Method to retrieve the saved string
    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }
}
