package com.example.myapplication.api;

import android.util.Log;

import com.example.myapplication.DatabaseConfig;
import com.example.myapplication.model.HistoryItem;
import com.example.myapplication.model.ProductionData;
import com.example.myapplication.model.TooltipData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.sql.DriverManager;
import java.util.Locale;
import java.util.Map;

public class ProductionApi {

    public static List<ProductionData> getProductionData(String tableName) {
        List<ProductionData> productionDataList = new ArrayList<>();

        // Query SQL dengan nama tabel dinamis
        String query = "SELECT TOP 50 " +
                "p.NoProduksi, " +
                "p.Shift, " +
                "p.Tanggal, " +
                "p.IdMesin, " +
                "p.IdOperator, " +
                "m.NamaMesin, " +
                "o.NamaOperator " +
                "FROM " + tableName + " p " +
                "LEFT JOIN MstMesin m ON p.IdMesin = m.IdMesin " +
                "LEFT JOIN MstOperator o ON p.IdOperator = o.IdOperator " +
                "ORDER BY p.NoProduksi DESC";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                // Ambil data dari ResultSet
                String noProduksi = rs.getString("NoProduksi");
                String shift = rs.getString("Shift");
                String tanggal = rs.getString("Tanggal");
                String mesin = rs.getString("NamaMesin");
                String operator = rs.getString("NamaOperator");

                // Debugging untuk memverifikasi data
                Log.d("Database Data", "NoProduksi: " + noProduksi +
                        ", Shift: " + shift +
                        ", Tanggal: " + tanggal +
                        ", Mesin: " + mesin +
                        ", Operator: " + operator);

                // Tambahkan ke list ProductionData
                productionDataList.add(new ProductionData(noProduksi, shift, tanggal, mesin, operator));
            }
        } catch (SQLException e) {
            Log.e("Database Fetch Error", "Error fetching data: " + e.getMessage());
        }

        return productionDataList;
    }


    // Metode baru untuk mengambil NoS4S berdasarkan NoProduksi
    public static List<String> getNoS4SByNoProduksi(String noProduksi, String tableName) {
        List<String> noS4SList = new ArrayList<>();

        // Query untuk mengambil NoS4S berdasarkan NoProduksi
        String query = "SELECT NoS4S FROM " + tableName + " WHERE NoProduksi = ?";
        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, noProduksi); // Set nilai parameter
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String noS4S = rs.getString("NoS4S");
                    noS4SList.add(noS4S); // Tambahkan NoS4S ke dalam daftar

                    Log.d("Database Data", "NoS4S: " + noS4S);
                }
            }
        } catch (SQLException e) {
            Log.e("Database Fetch Error", "Error fetching NoS4S: " + e.getMessage());
        }
        return noS4SList;
    }

    public static List<String> getNoSTByNoProduksi(String noProduksi) {
        List<String> noSTList = new ArrayList<>();

        String query = "SELECT NoST FROM S4SProduksiInputST WHERE NoProduksi = '" + noProduksi + "'";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String noST = rs.getString("NoST");
                noSTList.add(noST);
                Log.d("Database Data", "NoST: " + noST);
            }
        } catch (SQLException e) {
            Log.e("Database Fetch Error", "Error fetching NoST: " + e.getMessage());
        }

        return noSTList;
    }

    public static List<String> getNoMouldingByNoProduksi(String noProduksi) {
        List<String> noMouldingList = new ArrayList<>();

        String query = "SELECT NoMoulding FROM S4SProduksiInputMoulding WHERE NoProduksi = '" + noProduksi + "'";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String noST = rs.getString("NoMoulding");
                noMouldingList.add(noST);
                Log.d("Database Data", "NoMoulding: " + noST);
            }
        } catch (SQLException e) {
            Log.e("Database Fetch Error", "Error fetching NoMoulding: " + e.getMessage());
        }

        return noMouldingList;
    }

    public static List<String> getNoFJByNoProduksi(String noProduksi) {
        List<String> noFJList = new ArrayList<>();

        String query = "SELECT NoFJ FROM S4SProduksiInputFJ WHERE NoProduksi = '" + noProduksi + "'";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String noFJ = rs.getString("NoFJ");
                noFJList.add(noFJ);
                Log.d("Database Data", "NoFJ: " + noFJ);
            }
        } catch (SQLException e) {
            Log.e("Database Fetch Error", "Error fetching NoFJ: " + e.getMessage());
        }

        return noFJList;
    }

    public static List<String> getNoCCByNoProduksi(String noProduksi, String tableName) {
        List<String> noCCList = new ArrayList<>();

        // Query SQL dinamis berdasarkan nama tabel
        String query = "SELECT NoCCAkhir FROM " + tableName + " WHERE NoProduksi = ?";

        Log.d("SQL Query", "Executing query: SELECT NoCCAkhir FROM " + tableName + " WHERE NoProduksi = '" + noProduksi + "'");

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement pstmt = con.prepareStatement(query)) {

            // Set parameter untuk query
            pstmt.setString(1, noProduksi);

            // Eksekusi query
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String noCC = rs.getString("NoCCAkhir");
                    noCCList.add(noCC);
                    Log.d("Database Data", "NoCC: " + noCC);
                }
            }
        } catch (SQLException e) {
            Log.e("Database Fetch Error", "Error fetching NoCC: " + e.getMessage());
        }

        // Debugging jumlah data yang ditemukan
        Log.d("NoCC List Size", "Number of NoCC fetched: " + noCCList.size());

        return noCCList;
    }


    public static List<String> getNoReprosesByNoProduksi(String noProduksi) {
        List<String> noReprosesList = new ArrayList<>();

        String query = "SELECT NoReproses FROM S4SProduksiInputReproses WHERE NoProduksi = '" + noProduksi + "'";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String noReproses = rs.getString("NoReproses");
                noReprosesList.add(noReproses);
                Log.d("Database Data", "NoReproses: " + noReproses);
            }
        } catch (SQLException e) {
            Log.e("Database Fetch Error", "Error fetching NoReproses: " + e.getMessage());
        }

        return noReprosesList;
    }


    public static void saveNoS4S(String noProduksi, String tglProduksi, List<String> noS4SList, String dateTimeSaved, String tableName) {
        if (noS4SList == null || noS4SList.isEmpty()) {
            Log.e("SaveError", "List NoS4S kosong, tidak ada data untuk disimpan.");
            return;
        }

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement insertStmt = con.prepareStatement("INSERT INTO " + tableName + " (NoProduksi, NoS4S, DateTimeSaved) VALUES (?, ?, ?)");
             PreparedStatement updateStmt = con.prepareStatement("UPDATE S4S_h SET DateUsage = ? WHERE NoS4S = ?")) {

            // Insert Data ke S4SProduksiInputS4S
            for (String noS4S : noS4SList) {
                insertStmt.setString(1, noProduksi);
                insertStmt.setString(2, noS4S);
                insertStmt.setString(3, dateTimeSaved);
                insertStmt.addBatch();
            }
            insertStmt.executeBatch();

            // Update DataUsage di S4S_h
            for (String noS4S : noS4SList) {
                updateStmt.setString(1, tglProduksi);
                updateStmt.setString(2, noS4S);
                updateStmt.addBatch();
            }
            updateStmt.executeBatch();

            Log.d("SaveSuccess", "Data berhasil disimpan dan DateUsage diperbarui.");

        } catch (SQLException e) {
            Log.e("SaveError", "Error saat menyimpan data: " + e.getMessage());
        }
    }



    public static void saveNoST(String noProduksi, String tglProduksi, List<String> noSTList, String dateTimeSaved) {
        if (noSTList == null || noSTList.isEmpty()) {
            Log.e("SaveError", "List NoST kosong, tidak ada data untuk disimpan.");
            return;
        }

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement insertStmt = con.prepareStatement("INSERT INTO S4SProduksiInputST (NoProduksi, NoST, DateTimeSaved) VALUES (?, ?, ?)");
             PreparedStatement updateStmt = con.prepareStatement("UPDATE ST_h SET DateUsage = ? WHERE NoST = ?")) {

            // Insert Data ke S4SProduksiInputST
            for (String noST : noSTList) {
                insertStmt.setString(1, noProduksi);
                insertStmt.setString(2, noST);
                insertStmt.setString(3, dateTimeSaved);
                insertStmt.addBatch();
            }
            insertStmt.executeBatch();

            // Update DataUsage di S4S_h
            for (String noST : noSTList) {
                updateStmt.setString(1, tglProduksi);
                updateStmt.setString(2, noST);
                updateStmt.addBatch();
            }
            updateStmt.executeBatch();

            Log.d("SaveSuccess", "Data NoST berhasil disimpan dan DateUsage diperbarui.");

        } catch (SQLException e) {
            Log.e("SaveError", "Error saat menyimpan data NoST: " + e.getMessage());
        }
    }


    public static void saveNoMoulding(String noProduksi, String tglProduksi, List<String> noMouldingList, String dateTimeSaved) {
        if (noMouldingList == null || noMouldingList.isEmpty()) {
            Log.e("SaveError", "List NoMoulding kosong, tidak ada data untuk disimpan.");
            return;
        }

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement insertStmt = con.prepareStatement("INSERT INTO S4SProduksiInputMoulding (NoProduksi, NoMoulding, DateTimeSaved) VALUES (?, ?, ?)");
             PreparedStatement updateStmt = con.prepareStatement("UPDATE Moulding_h SET DateUsage = ? WHERE NoMoulding = ?")) {

            // Insert Data ke S4SProduksiInputMoulding
            for (String noMoulding : noMouldingList) {
                insertStmt.setString(1, noProduksi);
                insertStmt.setString(2, noMoulding);
                insertStmt.setString(3, dateTimeSaved);
                insertStmt.addBatch();
            }
            insertStmt.executeBatch();

            // Update DataUsage di Moulding_h
            for (String noMoulding : noMouldingList) {
                updateStmt.setString(1, tglProduksi);
                updateStmt.setString(2, noMoulding);
                updateStmt.addBatch();
            }
            updateStmt.executeBatch();

            Log.d("SaveSuccess", "Data NoMoulding berhasil disimpan dan DateUsage diperbarui.");

        } catch (SQLException e) {
            Log.e("SaveError", "Error saat menyimpan data NoMoulding: " + e.getMessage());
        }
    }


    public static void saveNoFJ(String noProduksi, String tglProduksi, List<String> noFJList, String dateTimeSaved) {
        if (noFJList == null || noFJList.isEmpty()) {
            Log.e("SaveError", "List NoFJ kosong, tidak ada data untuk disimpan.");
            return;
        }

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement insertStmt = con.prepareStatement("INSERT INTO S4SProduksiInputFJ (NoProduksi, NoFJ, DateTimeSaved) VALUES (?, ?, ?)");
             PreparedStatement updateStmt = con.prepareStatement("UPDATE FJ_h SET DateUsage = ? WHERE NoFJ = ?")) {

            // Insert Data ke S4SProduksiInputFJ
            for (String noFJ : noFJList) {
                insertStmt.setString(1, noProduksi);
                insertStmt.setString(2, noFJ);
                insertStmt.setString(3, dateTimeSaved);
                insertStmt.addBatch();
            }
            insertStmt.executeBatch();

            // Update DataUsage di FJ_h
            for (String noFJ : noFJList) {
                updateStmt.setString(1, tglProduksi);
                updateStmt.setString(2, noFJ);
                updateStmt.addBatch();
            }
            updateStmt.executeBatch();

            Log.d("SaveSuccess", "Data NoFJ berhasil disimpan dan DateUsage diperbarui.");

        } catch (SQLException e) {
            Log.e("SaveError", "Error saat menyimpan data NoFJ: " + e.getMessage());
        }
    }


    public static void saveNoCC(String noProduksi, String tglProduksi, List<String> noCCList, String dateTimeSaved, String tableName) {
        if (noCCList == null || noCCList.isEmpty()) {
            Log.e("SaveError", "List NoCCAkhir kosong, tidak ada data untuk disimpan.");
            return;
        }

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement insertStmt = con.prepareStatement("INSERT INTO " + tableName + " (NoProduksi, NoCCAkhir, DateTimeSaved) VALUES (?, ?, ?)");
             PreparedStatement updateStmt = con.prepareStatement("UPDATE CCAkhir_h SET DateUsage = ? WHERE NoCCAkhir = ?")) {

            // Insert Data ke S4SProduksiInputCCAkhir
            for (String noCCAkhir : noCCList) {
                insertStmt.setString(1, noProduksi);
                insertStmt.setString(2, noCCAkhir);
                insertStmt.setString(3, dateTimeSaved);
                insertStmt.addBatch();
            }
            insertStmt.executeBatch();

            // Update DataUsage di CCAkhir_h
            for (String noCCAkhir : noCCList) {
                updateStmt.setString(1, tglProduksi);
                updateStmt.setString(2, noCCAkhir);
                updateStmt.addBatch();
            }
            updateStmt.executeBatch();

            Log.d("SaveSuccess", "Data NoCCAkhir berhasil disimpan dan DateUsage diperbarui.");

        } catch (SQLException e) {
            Log.e("SaveError", "Error saat menyimpan data NoCCAkhir: " + e.getMessage());
        }
    }


    public static void saveNoReproses(String noProduksi, String tglProduksi, List<String> noReprosesList, String dateTimeSaved) {
        if (noReprosesList == null || noReprosesList.isEmpty()) {
            Log.e("SaveError", "List NoReproses kosong, tidak ada data untuk disimpan.");
            return;
        }

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement insertStmt = con.prepareStatement("INSERT INTO S4SProduksiInputReproses (NoProduksi, NoReproses, DateTimeSaved) VALUES (?, ?, ?)");
             PreparedStatement updateStmt = con.prepareStatement("UPDATE Reproses_h SET DateUsage = ? WHERE NoReproses = ?")) {

            // Insert Data ke S4SProduksiInputReproses
            for (String noReproses : noReprosesList) {
                insertStmt.setString(1, noProduksi);
                insertStmt.setString(2, noReproses);
                insertStmt.setString(3, dateTimeSaved);
                insertStmt.addBatch();
            }
            insertStmt.executeBatch();

            // Update DataUsage di Reproses_h
            for (String noReproses : noReprosesList) {
                updateStmt.setString(1, tglProduksi);
                updateStmt.setString(2, noReproses);
                updateStmt.addBatch();
            }
            updateStmt.executeBatch();

            Log.d("SaveSuccess", "Data NoReproses berhasil disimpan dan DateUsage diperbarui.");

        } catch (SQLException e) {
            Log.e("SaveError", "Error saat menyimpan data NoReproses: " + e.getMessage());
        }
    }


    public static boolean isDataExists(String result, String tableName_h, String tableName_d, String columnName) {
        boolean existsInTableH = false;
        boolean existsInTableD = false;

        Log.d("isDataExists", "Checking data existence for result: " + result);

        // Buat query dinamis untuk kedua tabel
        String queryH = "SELECT 1 FROM " + tableName_h + " WHERE " + columnName + " = ?";
        String queryD = "SELECT 1 FROM " + tableName_d + " WHERE " + columnName + " = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl())) {
            if (con == null) {
                Log.e("DatabaseConnection", "Failed to connect to database.");
                return false;
            }

            // Cek di tabel H
            try (PreparedStatement pstmtH = con.prepareStatement(queryH)) {
                pstmtH.setString(1, result);
                Log.d("QueryExecution", "Executing query on " + tableName_h + " with parameter: " + result);

                try (ResultSet rsH = pstmtH.executeQuery()) {
                    existsInTableH = rsH.next();
                    if (existsInTableH) {
                        Log.d("isDataExists", "Data found in " + tableName_h + ": " + result);
                    } else {
                        Log.d("isDataExists", "Data not found in " + tableName_h + ": " + result);
                    }
                }
            }

            // Cek di tabel D
            try (PreparedStatement pstmtD = con.prepareStatement(queryD)) {
                pstmtD.setString(1, result);
                Log.d("QueryExecution", "Executing query on " + tableName_d + " with parameter: " + result);

                try (ResultSet rsD = pstmtD.executeQuery()) {
                    existsInTableD = rsD.next();
                    if (existsInTableD) {
                        Log.d("isDataExists", "Data found in " + tableName_d + ": " + result);
                    } else {
                        Log.d("isDataExists", "Data not found in " + tableName_d + ": " + result);
                    }
                }
            }
        } catch (SQLException e) {
            Log.e("Database Check Error", "Error checking data existence: " + e.getMessage());
            e.printStackTrace();
            return false; // Jika terjadi error, kembalikan false
        }

        // Return true jika data ditemukan di kedua tabel
        boolean exists = existsInTableH && existsInTableD;
        Log.d("isDataExists", "Final result: Data exists in both tables: " + exists);
        return exists;
    }

    public static boolean isDateUsageNull(String result, String tableNameH, String columnName) {
        String query = "SELECT DateUsage FROM " + tableNameH + " WHERE " + columnName + " = ?";
        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, result);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getObject("DateUsage") == null; // True jika DateUsage null
                }
            }
        } catch (SQLException e) {
            Log.e("Database Check Error", "Error checking DateUsage: " + e.getMessage());
        }
        return false; // Default jika tidak valid
    }

    public static boolean isDateValid(String noProduksi, String tableProduksi, String result, String tableNameH, String columnName) {
        String queryH = "SELECT DateCreate FROM " + tableNameH + " WHERE " + columnName + " = ?";
        String queryProduksi = "SELECT Tanggal FROM " + tableProduksi + " WHERE NoProduksi = ?";
        java.sql.Date dateCreated = null;
        java.sql.Date produksiInputDate = null;

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl())) {
            // Ambil DateCreated dari tableNameH
            try (PreparedStatement pstmtH = con.prepareStatement(queryH)) {
                pstmtH.setString(1, result);
                try (ResultSet rsH = pstmtH.executeQuery()) {
                    if (rsH.next()) {
                        dateCreated = rsH.getDate("DateCreate");
                    }
                }
            }

            // Ambil Tanggal dari ProduksiInput
            try (PreparedStatement pstmtProduksi = con.prepareStatement(queryProduksi)) {
                pstmtProduksi.setString(1, noProduksi);
                try (ResultSet rsProduksi = pstmtProduksi.executeQuery()) {
                    if (rsProduksi.next()) {
                        produksiInputDate = rsProduksi.getDate("Tanggal");
                    }
                }
            }

            // Bandingkan tanggal
            if (dateCreated != null && produksiInputDate != null) {
                return produksiInputDate.compareTo(dateCreated) >= 0;
            }
        } catch (SQLException e) {
            Log.e("Database Check Error", "Error comparing dates: " + e.getMessage());
        }
        return false; // Default jika tidak valid
    }

    public static String findS4SResultTable(String result) {
        String queryTemplate = "SELECT ? AS TableName, NoS4S FROM %s WHERE NoS4S = ?";
        String[] tables = {
                "S4SProduksiInputS4S",
                "FJProduksiInputS4S",
                "MouldingProduksiInputS4S",
                "AdjustmentInputS4S",
                "BongkarSusunInputS4S"
        };

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl())) {
            for (String table : tables) {
                String query = String.format(queryTemplate, table);
                try (PreparedStatement pstmt = con.prepareStatement(query)) {
                    pstmt.setString(1, table); // Set table name as parameter
                    pstmt.setString(2, result); // Set NoS4S value

                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            // Return a message based on the table name
                            switch (table) {
                                case "S4SProduksiInputS4S":
                                    return "Proses Produksi S4S";
                                case "FJProduksiInputS4S":
                                    return "Proses Produksi FJ";
                                case "MouldingProduksiInputS4S":
                                    return "Proses Produksi Moulding";
                                case "AdjustmentInputS4S":
                                    return "Adjustment S4S";
                                case "BongkarSusunInputS4S":
                                    return "Bongkar Susun";
                                default:
                                    return " " + table;
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            Log.e("Database Check Error", "Error finding result table: " + e.getMessage());
            e.printStackTrace();
        }

        return null; // Return a default message if result is not found
    }


    public static String findSTResultTable(String result) {
        String queryTemplate = "SELECT ? AS TableName, NoST FROM %s WHERE NoST = ?";
        String[] tables = {
                "S4SProduksiInputST",
                "AdjustmentInputST",
                "BongkarSusunInputST"
        };

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl())) {
            for (String table : tables) {
                String query = String.format(queryTemplate, table);
                try (PreparedStatement pstmt = con.prepareStatement(query)) {
                    pstmt.setString(1, table); // Set table name as parameter
                    pstmt.setString(2, result); // Set NoST value

                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            // Return a message based on the table name
                            switch (table) {
                                case "S4SProduksiInputST":
                                    return "Proses Produksi S4S";
                                case "AdjustmentInputST":
                                    return "Adjustment ST";
                                case "BongkarSusunInputST":
                                    return "Bongkar Susun";
                                default:
                                    return " " + table;
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            Log.e("Database Check Error", "Error finding result table: " + e.getMessage());
            e.printStackTrace();
        }

        return null; // Return a default message if result is not found
    }

    public static String findMouldingResultTable(String result) {
        String queryTemplate = "SELECT ? AS TableName, NoMoulding FROM %s WHERE NoMoulding = ?";
        String[] tables = {
                "S4SProduksiInputMoulding",
                "SandingProduksiInputMoulding",
                "PackingProduksiInputMoulding",
                "MouldingProduksiInputMoulding",
                "LaminatingProduksiInputMoulding",
                "CCAkhirProduksiInputMoulding",
                "AdjustmentInputMoulding",
                "BongkarSusunInputMoulding"
        };

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl())) {
            for (String table : tables) {
                String query = String.format(queryTemplate, table);
                try (PreparedStatement pstmt = con.prepareStatement(query)) {
                    pstmt.setString(1, table); // Set table name as parameter
                    pstmt.setString(2, result); // Set NoMoulding value

                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            // Return a message based on the table name
                            switch (table) {
                                case "S4SProduksiInputMoulding":
                                    return "Proses Produksi S4S";
                                case "SandingProduksiInputMoulding":
                                    return "Proses Produksi Sanding";
                                case "PackingProduksiInputMoulding":
                                    return "Proses Packing Packing";
                                case "MouldingProduksiInputMoulding":
                                    return "Proses Produksi Moulding";
                                case "LaminatingProduksiInputMoulding":
                                    return "Proses Produksi Laminating";
                                case "CCAkhirProduksiInputMoulding":
                                    return "Proses Produksi Cross Cut";
                                case "AdjustmentInputMoulding":
                                    return "Adjustment Moulding";
                                case "BongkarSusunInputMoulding":
                                    return "Bongkar Susun Moulding";
                                default:
                                    return " " + table;
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            Log.e("Database Check Error", "Error finding result table: " + e.getMessage());
            e.printStackTrace();
        }

        return null; // Return a default message if result is not found
    }


    public static String findFJResultTable(String result) {
        String queryTemplate = "SELECT ? AS TableName, NoFJ FROM %s WHERE NoFJ = ?";
        String[] tables = {
                "SandingProduksiInputFJ",
                "S4SProduksiInputFJ",
                "MouldingProduksiInputFJ",
                "CCAkhirProduksiInputFJ",
                "AdjustmentInputFJ",
                "BongkarSusunInputFJ"
        };

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl())) {
            for (String table : tables) {
                String query = String.format(queryTemplate, table);
                try (PreparedStatement pstmt = con.prepareStatement(query)) {
                    pstmt.setString(1, table); // Set table name as parameter
                    pstmt.setString(2, result); // Set NoFJ value

                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            // Return a message based on the table name
                            switch (table) {
                                case "SandingProduksiInputFJ":
                                    return "Proses Produksi Sanding";
                                case "S4SProduksiInputFJ":
                                    return "Proses Produksi S4S";
                                case "MouldingProduksiInputFJ":
                                    return "Proses Produksi Moulding";
                                case "CCAkhirProduksiInputFJ":
                                    return "Proses Produksi Cross Cut";
                                case "AdjustmentInputFJ":
                                    return "Adjustment FJ";
                                case "BongkarSusunInputFJ":
                                    return "Bongkar Susun FJ";
                                default:
                                    return " " + table;
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            Log.e("Database Check Error", "Error finding result table: " + e.getMessage());
            e.printStackTrace();
        }

        return null; // Return a default message if result is not found
    }


    public static String findCCAkhirResultTable(String result) {
        String queryTemplate = "SELECT ? AS TableName, NoCCAkhir FROM %s WHERE NoCCAkhir = ?";
        String[] tables = {
                "SandingProduksiInputCCAkhir",
                "S4SProduksiInputCCAkhir",
                "MouldingProduksiInputCCAkhir",
                "LaminatingProduksiInputCCAkhir",
                "FJProduksiInputCCAkhir",
                "AdjustmentInputCCAkhir",
                "BongkarSusunInputCCAkhir"
        };

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl())) {
            for (String table : tables) {
                String query = String.format(queryTemplate, table);
                try (PreparedStatement pstmt = con.prepareStatement(query)) {
                    pstmt.setString(1, table); // Set table name as parameter
                    pstmt.setString(2, result); // Set NoCCAkhir value

                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            // Return a message based on the table name
                            switch (table) {
                                case "SandingProduksiInputCCAkhir":
                                    return "Proses Produksi Sanding";
                                case "S4SProduksiInputCCAkhir":
                                    return "Proses Produksi S4S";
                                case "MouldingProduksiInputCCAkhir":
                                    return "Proses Produksi Moulding";
                                case "LaminatingProduksiInputCCAkhir":
                                    return "Proses Produksi Laminating";
                                case "FJProduksiInputCCAkhir":
                                    return "Proses Produksi Finger Joint";
                                case "AdjustmentInputCCAkhir":
                                    return "Adjustment Cross Cut";
                                case "BongkarSusunInputCCAkhir":
                                    return "Bongkar Susun Cross Cut";
                                default:
                                    return " " + table;
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            Log.e("Database Check Error", "Error finding result table: " + e.getMessage());
            e.printStackTrace();
        }

        return null; // Return a default message if result is not found
    }



    public static String findReprosesResultTable(String result) {
        String queryTemplate = "SELECT ? AS TableName, NoReproses FROM %s WHERE NoReproses = ?";
        String[] tables = {
                "SandingProduksiInputReproses",
                "S4SProduksiInputReproses",
                "MouldingProduksiInputReproses",
                "LaminatingProduksiInputReproses",
                "FJProduksiInputReproses",
                "AdjustmentInputReproses",
                "BongkarSusunInputReproses"
        };

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl())) {
            for (String table : tables) {
                String query = String.format(queryTemplate, table);
                try (PreparedStatement pstmt = con.prepareStatement(query)) {
                    pstmt.setString(1, table); // Set table name as parameter
                    pstmt.setString(2, result); // Set NoReproses value

                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            // Return a message based on the table name
                            switch (table) {
                                case "SandingProduksiInputReproses":
                                    return "Proses Produksi Sanding";
                                case "S4SProduksiInputReproses":
                                    return "Proses Produksi S4S";
                                case "MouldingProduksiInputReproses":
                                    return "Proses Produksi Moulding";
                                case "LaminatingProduksiInputReproses":
                                    return "Proses Produksi Laminating";
                                case "FJProduksiInputReproses":
                                    return "Proses Produksi Finger Joint";
                                case "AdjustmentInputReproses":
                                    return "Adjustment Reproses";
                                case "BongkarSusunInputReproses":
                                    return "Bongkar Susun Reproses";
                                default:
                                    return " " + table;
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            Log.e("Database Check Error", "Error finding result table: " + e.getMessage());
            e.printStackTrace();
        }

        return null; // Return a default message if result is not found
    }


    public static List<HistoryItem> getHistoryItems(String noProduksi) {
        List<HistoryItem> historyGroups = new ArrayList<>();
        String query = "SELECT DateTimeSaved, Label, COUNT(KodeLabel) AS Total, " +
                "SUM(CASE WHEN Label = 'S4S' THEN 1 ELSE 0 END) AS TotalS4S, " +
                "SUM(CASE WHEN Label = 'ST' THEN 1 ELSE 0 END) AS TotalST, " +
                "SUM(CASE WHEN Label = 'Moulding' THEN 1 ELSE 0 END) AS TotalMoulding, " +
                "SUM(CASE WHEN Label = 'FJ' THEN 1 ELSE 0 END) AS TotalFJ, " +
                "SUM(CASE WHEN Label = 'Cross Cut' THEN 1 ELSE 0 END) AS TotalCrossCut, " +
                "SUM(CASE WHEN Label = 'Reproses' THEN 1 ELSE 0 END) AS TotalReproses " +
                "FROM ( " +
                "    SELECT 'S4S' AS Label, NoS4S AS KodeLabel, DateTimeSaved FROM S4SProduksiInputS4S WHERE NoProduksi = ? " +
                "    UNION ALL " +
                "    SELECT 'ST' AS Label, NoST AS KodeLabel, DateTimeSaved FROM S4SProduksiInputST WHERE NoProduksi = ? " +
                "    UNION ALL " +
                "    SELECT 'Moulding' AS Label, NoMoulding AS KodeLabel, DateTimeSaved FROM S4SProduksiInputMoulding WHERE NoProduksi = ? " +
                "    UNION ALL " +
                "    SELECT 'FJ' AS Label, NoFJ AS KodeLabel, DateTimeSaved FROM S4SProduksiInputFJ WHERE NoProduksi = ? " +
                "    UNION ALL " +
                "    SELECT 'Cross Cut' AS Label, NoCCAkhir AS KodeLabel, DateTimeSaved FROM S4SProduksiInputCCAkhir WHERE NoProduksi = ? " +
                "    UNION ALL " +
                "    SELECT 'Reproses' AS Label, NoReproses AS KodeLabel, DateTimeSaved FROM S4SProduksiInputReproses WHERE NoProduksi = ? " +
                ") AS CombinedData " +
                "GROUP BY DateTimeSaved, Label " +
                "ORDER BY DateTimeSaved DESC";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement pstmt = con.prepareStatement(query)) {
            for (int i = 1; i <= 6; i++) {
                pstmt.setString(i, noProduksi);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                Map<String, HistoryItem> groupedHistory = new LinkedHashMap<>();

                while (rs.next()) {
                    String dateTimeSaved = rs.getString("DateTimeSaved");
                    String label = rs.getString("Label");
                    int total = rs.getInt("Total");

                    int totalS4S = rs.getInt("TotalS4S");
                    int totalST = rs.getInt("TotalST");
                    int totalMoulding = rs.getInt("TotalMoulding");
                    int totalFJ = rs.getInt("TotalFJ");
                    int totalCrossCut = rs.getInt("TotalCrossCut");
                    int totalReproses = rs.getInt("TotalReproses");
                    int totalAllLabels = totalS4S + totalST + totalMoulding + totalFJ + totalCrossCut + totalReproses;

                    Log.d("TotalS4S", "Total S4S: " + totalS4S);
                    Log.d("TotalST", "Total ST: " + totalST);
                    Log.d("TotalMoulding", "Total Moulding: " + totalMoulding);
                    Log.d("TotalFJ", "Total FJ: " + totalFJ);
                    Log.d("TotalCrossCut", "Total Cross Cut: " + totalCrossCut);
                    Log.d("TotalReproses", "Total Reproses: " + totalReproses);
                    Log.d("TotalAllLabels", "Total All Labels: " + totalAllLabels);


                    groupedHistory.putIfAbsent(dateTimeSaved, new HistoryItem(dateTimeSaved));
                    groupedHistory.get(dateTimeSaved).addItem(new HistoryItem(label, String.valueOf(total), dateTimeSaved));

                    HistoryItem history = groupedHistory.get(dateTimeSaved);
                    history.setTotalCrossCut(history.getTotalCrossCut() + totalCrossCut);
                    history.setTotalS4S(history.getTotalS4S() + totalS4S);
                    history.setTotalST(history.getTotalST() + totalST);
                    history.setTotalMoulding(history.getTotalMoulding() + totalMoulding);
                    history.setTotalFJ(history.getTotalFJ() + totalFJ);
                    history.setTotalReproses(history.getTotalReproses() + totalReproses);
                    history.setTotalAllLabels(history.getTotalAllLabels() + totalAllLabels);
                }

                historyGroups.addAll(groupedHistory.values());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return historyGroups;
    }



    public static boolean isTransactionPeriodClosed(String tglProduksi) {
        String queryBulanan = "SELECT TOP 1 Period FROM MstTutupTransaksi WHERE Lock = 1 ORDER BY Period DESC";
        String queryHarian = "SELECT TOP 1 PeriodHarian FROM MstTutupTransaksiHarian WHERE Lock = 1 ORDER BY PeriodHarian DESC";

        java.sql.Date periodBulanan = null;
        java.sql.Date periodHarian = null;
        java.sql.Date produksiDate = java.sql.Date.valueOf(tglProduksi);

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl())) {
            // Ambil Period dari queryBulanan
            try (PreparedStatement pstmtBulanan = con.prepareStatement(queryBulanan);
                 ResultSet rsBulanan = pstmtBulanan.executeQuery()) {
                if (rsBulanan.next()) {
                    periodBulanan = rsBulanan.getDate("Period");
                }
            }

            if (periodBulanan != null && produksiDate.before(periodBulanan)) {
                return false;
            }

            // Ambil Period dari queryHarian
            try (PreparedStatement pstmtHarian = con.prepareStatement(queryHarian);
                 ResultSet rsHarian = pstmtHarian.executeQuery()) {
                if (rsHarian.next()) {
                    periodHarian = rsHarian.getDate("PeriodHarian");
                }
            }

            if (periodHarian != null && produksiDate.before(periodHarian)) {
                return false;
            }

            // Jika tglProduksi lebih kecil atau sama dengan kedua period, return true
            return true;
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            return false; // Return default jika terjadi kesalahan
        }
    }

    public static TooltipData getTooltipData(String noKey, String tableH, String tableD, String mainColumn) {
        TooltipData tooltipData = new TooltipData();

        // Query untuk tabel utama (detail tooltip)
        String detailQuery = "SELECT h." + mainColumn + " AS KeyColumn, h.DateCreate, h.Jam, k.Jenis, h.NoSPK, " +
                "b1.Buyer AS BuyerNoSPK, h.NoSPKAsal, b2.Buyer AS BuyerNoSPKAsal, g.NamaGrade, h.IsLembur " +
                "FROM " + tableH + " h " +
                "LEFT JOIN MstGrade g ON h.IdGrade = g.IdGrade " +
                "LEFT JOIN MstJenisKayu k ON h.IdJenisKayu = k.IdJenisKayu " +
                "LEFT JOIN MstSPK_h s1 ON h.NoSPK = s1.NoSPK " +
                "LEFT JOIN MstBuyer b1 ON s1.IdBuyer = b1.IdBuyer " +
                "LEFT JOIN MstSPK_h s2 ON h.NoSPKAsal = s2.NoSPK " +
                "LEFT JOIN MstBuyer b2 ON s2.IdBuyer = b2.IdBuyer " +
                "WHERE h." + mainColumn + " = ?";

        // Query untuk tabel detail
        String tableQuery = "SELECT Tebal, Lebar, Panjang, JmlhBatang FROM " + tableD + " WHERE " + mainColumn + " = ? ORDER BY NoUrut";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement detailStmt = con.prepareStatement(detailQuery);
             PreparedStatement tableStmt = con.prepareStatement(tableQuery)) {

            // Set parameter untuk detailQuery
            detailStmt.setString(1, noKey);

            try (ResultSet detailRs = detailStmt.executeQuery()) {
                if (detailRs.next()) {
                    tooltipData.setNoS4S(detailRs.getString("KeyColumn")); // Ambil kolom kunci (NoS4S, NoFJ, dll.)
                    String dateCreate = detailRs.getString("DateCreate");
                    String jam = detailRs.getString("Jam");
                    tooltipData.setFormattedDateTime(combineDateTime(dateCreate, jam));
                    tooltipData.setJenis(detailRs.getString("Jenis"));
                    tooltipData.setSpkDetail(formatSpk(detailRs.getString("NoSPK"), detailRs.getString("BuyerNoSPK")));
                    tooltipData.setSpkAsalDetail(formatSpk(detailRs.getString("NoSPKAsal"), detailRs.getString("BuyerNoSPKAsal")));
                    tooltipData.setNamaGrade(detailRs.getString("NamaGrade"));
                    tooltipData.setLembur(detailRs.getBoolean("IsLembur"));
                }
            }

            // Set parameter untuk tableQuery
            tableStmt.setString(1, noKey);

            try (ResultSet tableRs = tableStmt.executeQuery()) {
                List<String[]> tableData = new ArrayList<>();
                int totalPcs = 0;
                double totalM3 = 0.0;

                while (tableRs.next()) {
                    double tebal = tableRs.getDouble("Tebal");
                    double lebar = tableRs.getDouble("Lebar");
                    double panjang = tableRs.getDouble("Panjang");
                    int pcs = tableRs.getInt("JmlhBatang");

                    totalPcs += pcs;
                    double rowM3 = (tebal * lebar * panjang * pcs) / 1000000000.0;
                    rowM3 = Math.floor(rowM3 * 10000) / 10000; // Bulatkan ke 4 desimal
                    totalM3 += rowM3;

                    tableData.add(new String[]{
                            String.valueOf((int) tebal),
                            String.valueOf((int) lebar),
                            String.valueOf((int) panjang),
                            String.valueOf(pcs)
                    });
                }

                tooltipData.setTableData(tableData);
                tooltipData.setTotalPcs(totalPcs);
                tooltipData.setTotalM3(totalM3);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tooltipData;
    }

    // Helper untuk menggabungkan Date dan Time
    private static String combineDateTime(String date, String time) {
        try {
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date parsedDate = inputDateFormat.parse(date);
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            return outputFormat.format(parsedDate) + " (" + time + ")";
        } catch (Exception e) {
            e.printStackTrace();
            return date + " " + time;
        }
    }

    // Helper untuk memformat SPK
    private static String formatSpk(String noSPK, String buyer) {
        return (noSPK != null && buyer != null) ? noSPK + " - " + buyer : "-";
    }




















}