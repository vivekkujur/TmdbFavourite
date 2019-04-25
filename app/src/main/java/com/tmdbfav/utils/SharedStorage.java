package com.tmdbfav.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class SharedStorage {

    private static final String MY_PREFERENCES_11234234 = "MY_PREFERENCES_11234234";
    private SharedPreferences sharedPreferences;

    public static String movieIdList = "movieIdList";



    public SharedStorage(Context context) {
        this.sharedPreferences = context.getSharedPreferences(MY_PREFERENCES_11234234,Context.MODE_PRIVATE);
    }

    public void setValue(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void putListString(String key, ArrayList<String> stringList) {
        if (key == null) return;
        if (stringList ==null)return;
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String[] myStringList = stringList.toArray(new String[stringList.size()]);
        editor.putString(key, TextUtils.join(",", myStringList)).apply();
    }

    public ArrayList<String> getListString(String key) {
        return new ArrayList<String>(Arrays.asList(TextUtils.split(sharedPreferences.
                getString(key, ""), ",")));
    }


    public String getValue(String key) {
        return sharedPreferences.getString(key, "");
    }

    public void showPreferences(String label){
        Log.d("PREFERENCES",label);

        Map<String, ?> allEntries = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.d("PREFERENCES map values", entry.getKey() + ": " + entry.getValue().toString());
        }
        Log.d("PREFERENCES",label +" end");
    }

}
