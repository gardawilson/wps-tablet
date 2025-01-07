package com.example.myapplication.api;

import android.util.Log;

import com.example.myapplication.DatabaseConfig;
import com.example.myapplication.model.ProductionData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.sql.DriverManager;
import java.util.Locale;

public class ProductionApi {

    public static List<ProductionData> getProductionData() {
        List<ProductionData> productionDataList = new ArrayList<>();

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT TOP 200 " +
                             "p.NoProduksi, " +
                             "p.Shift, " +
                             "p.Tanggal, " +
                             "p.IdMesin, " +
                             "p.IdOperator, " +
                             "m.NamaMesin, " +
                             "o.NamaOperator " +
                             "FROM S4SProduksi_h p " +
                             "LEFT JOIN MstMesin m ON p.IdMesin = m.IdMesin " +
                             "LEFT JOIN MstOperator o ON p.IdOperator = o.IdOperator"
             );
        ) {

            while (rs.next()) {
                String noProduksi = rs.getString("NoProduksi");
                String shift = rs.getString("Shift");
                String tanggal = rs.getString("Tanggal");
                String mesin = rs.getString("NamaMesin");
                String operator = rs.getString("NamaOperator");

                Log.d("Database Data", "NoProduksi: " + noProduksi +
                        ", Shift: " + shift +
                        ", Tanggal: " + tanggal +
                        ", Mesin: " + mesin +
                        ", Operator: " + operator);

                productionDataList.add(new ProductionData(noProduksi, shift, tanggal, mesin, operator));
            }
        } catch (SQLException e) {
            Log.e("Database Fetch Error", "Error fetching data: " + e.getMessage());
        }

        return productionDataList;
    }

    // Metode baru untuk mengambil NoS4S berdasarkan NoProduksi
    public static List<String> getNoS4SByNoProduksi(String noProduksi) {
        List<String> noS4SList = new ArrayList<>();

        // Query untuk mengambil NoS4S berdasarkan NoProduksi
        String query = "SELECT NoS4S FROM S4SProduksiInputS4S WHERE NoProduksi = '" + noProduksi + "'";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Iterasi hasil query
            while (rs.next()) {
                String noS4S = rs.getString("NoS4S");
                noS4SList.add(noS4S); // Tambahkan NoS4S ke dalam daftar

                Log.d("Database Data", "NoS4S: " + noS4S);
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

    public static List<String> getNoCCByNoProduksi(String noProduksi) {
        List<String> noCCList = new ArrayList<>();

        String query = "SELECT NoCCAkhir FROM S4SProduksiInputCCAkhir WHERE NoProduksi = '" + noProduksi + "'";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String noCC = rs.getString("NoCCAkhir");
                noCCList.add(noCC);
                Log.d("Database Data", "NoCC: " + noCC);
            }
        } catch (SQLException e) {
            Log.e("Database Fetch Error", "Error fetching NoCC: " + e.getMessage());
        }

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

//    public static void saveNoS4S(String noProduksi, List<String> noS4SList) {
//        if (noS4SList == null || noS4SList.isEmpty()) {
//            Log.e("SaveError", "List NoS4S kosong");
//            return;
//        }
//
//        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
//             Statement stmt = con.createStatement()) {
//
//            for (String noS4S : noS4SList) {
//                String query = "INSERT INTO S4SProduksiInputS4S (NoProduksi, NoS4S) VALUES ('" + noProduksi + "', '" + noS4S + "')";
//                int rowsAffected = stmt.executeUpdate(query);
//
//                if (rowsAffected > 0) {
//                    Log.d("SaveSuccess", "NoS4S berhasil disimpan: " + noS4S);
//                } else {
//                    Log.e("SaveError", "Gagal menyimpan NoS4S: " + noS4S);
//                }
//            }
//        } catch (SQLException e) {
//            Log.e("SaveError", "Error menyimpan NoS4S: " + e.getMessage());
//        }
//    }
//
//    public static void saveNoST(String noProduksi, List<String> noSTList) {
//        if (noSTList == null || noSTList.isEmpty()) {
//            Log.e("SaveError", "List NoST kosong");
//            return;
//        }
//
//        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
//             Statement stmt = con.createStatement()) {
//
//            for (String noST : noSTList) {
//                String query = "INSERT INTO S4SProduksiInputST (NoProduksi, NoST) VALUES ('" + noProduksi + "', '" + noST + "')";
//                int rowsAffected = stmt.executeUpdate(query);
//
//                if (rowsAffected > 0) {
//                    Log.d("SaveSuccess", "NoST berhasil disimpan: " + noST);
//                } else {
//                    Log.e("SaveError", "Gagal menyimpan NoST: " + noST);
//                }
//            }
//        } catch (SQLException e) {
//            Log.e("SaveError", "Error menyimpan NoST: " + e.getMessage());
//        }
//    }


    public static void saveNoS4S(String noProduksi, List<String> noS4SList) {
        saveDataToDatabase(noProduksi, noS4SList, "INSERT INTO S4SProduksiInputS4S (NoProduksi, NoS4S) VALUES (?, ?)");
    }

    public static void saveNoST(String noProduksi, List<String> noSTList) {
        saveDataToDatabase(noProduksi, noSTList, "INSERT INTO S4SProduksiInputST (NoProduksi, NoST) VALUES (?, ?)");
    }

    public static void saveNoMoulding(String noProduksi, List<String> noMouldingList) {
        saveDataToDatabase(noProduksi, noMouldingList, "INSERT INTO S4SProduksiInputMoulding (NoProduksi, NoMoulding) VALUES (?, ?)");
    }

    public static void saveNoFJ(String noProduksi, List<String> noFJList) {
        saveDataToDatabase(noProduksi, noFJList, "INSERT INTO S4SProduksiInputFJ (NoProduksi, NoFJ) VALUES (?, ?)");
    }

    public static void saveNoCC(String noProduksi, List<String> noCCList) {
        saveDataToDatabase(noProduksi, noCCList, "INSERT INTO S4SProduksiInputCCAkhir (NoProduksi, NoCCAkhir) VALUES (?, ?)");
    }

    public static void saveNoReproses(String noProduksi, List<String> noReprosesList) {
        saveDataToDatabase(noProduksi, noReprosesList, "INSERT INTO S4SProduksiInputReproses (NoProduksi, NoReproses) VALUES (?, ?)");
    }

    private static void saveDataToDatabase(String noProduksi, List<String> dataList, String query) {
        if (dataList == null || dataList.isEmpty()) {
            Log.e("SaveError", "List data kosong");
            return;
        }

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement pstmt = con.prepareStatement(query)) {

            for (String data : dataList) {
                pstmt.setString(1, noProduksi);
                pstmt.setString(2, data);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } catch (SQLException e) {
            Log.e("SaveError", "Error menyimpan data: " + e.getMessage());
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

    public static boolean isResultExists(String result, String tableS4S, String tableFJ, String tableMoulding, String tableAdjustment, String tableBongkarSusun, String columnName) {
        String query =         "SELECT 1 " +
                "FROM (" +
                "    SELECT " + columnName + " FROM " + tableS4S + " WHERE " + columnName + " = ? " +
                "    UNION " +
                "    SELECT " + columnName + " FROM " + tableFJ + " WHERE " + columnName + " = ? " +
                "    UNION " +
                "    SELECT " + columnName + " FROM " + tableMoulding + " WHERE " + columnName + " = ? " +
                "    UNION " +
                "    SELECT " + columnName + " FROM " + tableAdjustment + " WHERE " + columnName + " = ? " +
                "    UNION " +
                "    SELECT " + columnName + " FROM " + tableBongkarSusun + " WHERE " + columnName + " = ? " +
                ") AS Combined";
        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement pstmt = con.prepareStatement(query)) {

            // Set the parameter for all placeholders (?)
            for (int i = 1; i <= 5; i++) {
                pstmt.setString(i, result);
            }

            // Execute the query
            try (ResultSet rs = pstmt.executeQuery()) {
                return !rs.next(); // Return true if no rows are found
            }
        } catch (SQLException e) {
            Log.e("Database Check Error", "Error checking result: " + e.getMessage());
            e.printStackTrace();
        }
        return false; // Return false in case of an exception
    }










}