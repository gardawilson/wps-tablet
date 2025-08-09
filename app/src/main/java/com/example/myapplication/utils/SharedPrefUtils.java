package com.example.myapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.List;

public class SharedPrefUtils {

    // Nama file SharedPreferences
    private static final String PREFS_NAME = "LoginPrefs";


    // Key untuk menyimpan username
    private static final String KEY_USERNAME = "username";

    private static final String KEY_ROLE_LIST = "user_roles";

    private static final String KEY_PERMISSION_LIST = "user_permissions";



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

    // Simpan list role sebagai string gabungan
    public static void saveRoles(Context context, List<String> roles) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String joinedRoles = TextUtils.join(",", roles); // pisahkan pakai koma
        editor.putString(KEY_ROLE_LIST, joinedRoles);
        editor.apply();
    }

    // Ambil list role dari string
    public static List<String> getRoles(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String joinedRoles = prefs.getString(KEY_ROLE_LIST, "");
        if (joinedRoles.isEmpty()) return Arrays.asList();
        return Arrays.asList(joinedRoles.split(","));
    }

    // Simpan list permission sebagai string gabungan
    public static void savePermissions(Context context, List<String> permissions) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String joinedPermissions = TextUtils.join(",", permissions); // pisahkan pakai koma
        editor.putString(KEY_PERMISSION_LIST, joinedPermissions);
        editor.apply();
    }

    // Ambil list permission dari string
    public static List<String> getPermissions(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String joinedPermissions = prefs.getString(KEY_PERMISSION_LIST, "");
        if (joinedPermissions.isEmpty()) return Arrays.asList();
        return Arrays.asList(joinedPermissions.split(","));
    }

}