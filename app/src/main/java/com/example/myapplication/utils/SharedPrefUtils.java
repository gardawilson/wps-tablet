package com.example.myapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefUtils {

    // Nama file SharedPreferences
    private static final String PREFS_NAME = "LoginPrefs";

    // Key untuk menyimpan username
    private static final String KEY_USERNAME = "username";

    // Method untuk menyimpan username
    public static void saveUsername(Context context, String username) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_USERNAME, username);
        editor.apply();
    }

    // Method untuk mengambil username
    public static String getUsername(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_USERNAME, ""); // "" adalah nilai default jika key tidak ditemukan
    }
}