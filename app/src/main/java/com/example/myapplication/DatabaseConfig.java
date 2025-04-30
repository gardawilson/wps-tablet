package com.example.myapplication;

import android.annotation.SuppressLint;
import android.util.Log;
import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConfig {
    private static String ip = "192.168.10.100";
    private static String port = "1433";
    private static String username = "sa";
    private static String password = "Utama1234";
    private static String databasename = "WPS";

    // Getter methods
    public static String getIp() { return ip; }
    public static String getPort() { return port; }
    public static String getUsername() { return username; }
    public static String getPassword() { return password; }
    public static String getDatabasename() { return databasename; }

    // Method untuk mengupdate settings
    public static void updateSettings(String newIp, String newPort, String newUsername,
                                      String newPassword, String newDatabasename) {
        ip = newIp;
        port = newPort;
        username = newUsername;
        password = newPassword;
        databasename = newDatabasename;
    }

    // Method untuk mendapatkan connection string
    public static String getConnectionUrl() {
        return String.format("jdbc:jtds:sqlserver://%s:%s;databasename=%s;User=%s;password=%s;",
                ip, port, databasename, username, password);
    }
}