package com.example.myapplication.config; // ✅ Harus cocok dengan folder

import com.example.myapplication.BuildConfig; // ✅ Ini wajib

public class DatabaseConfig {

    public static String getIp() {
        return BuildConfig.DB_IP;
    }

    public static String getPort() {
        return BuildConfig.DB_PORT;
    }

    public static String getUsername() {
        return BuildConfig.DB_USER;
    }

    public static String getPassword() {
        return BuildConfig.DB_PASS;
    }

    public static String getDatabaseName() {
        return BuildConfig.DB_NAME;
    }

    public static String getConnectionUrl() {
        return String.format(
                "jdbc:jtds:sqlserver://%s:%s;databasename=%s;user=%s;password=%s;",
                getIp(), getPort(), getDatabaseName(), getUsername(), getPassword()
        );
    }
}
