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
                             "LEFT JOIN MstOperator o ON p.IdOperator = o.IdOperator " + // tambahkan spasi
                             "ORDER BY p.NoProduksi DESC"
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


    public static void saveNoS4S(String noProduksi, String tglProduksi, List<String> noS4SList) {
        if (noS4SList == null || noS4SList.isEmpty()) {
            Log.e("SaveError", "List NoS4S kosong, tidak ada data untuk disimpan.");
            return;
        }

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement insertStmt = con.prepareStatement("INSERT INTO S4SProduksiInputS4S (NoProduksi, NoS4S) VALUES (?, ?)");
             PreparedStatement updateStmt = con.prepareStatement("UPDATE S4S_h SET DateUsage = ? WHERE NoS4S = ?")) {

            // Insert Data ke S4SProduksiInputS4S
            for (String noS4S : noS4SList) {
                insertStmt.setString(1, noProduksi);
                insertStmt.setString(2, noS4S);
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



    public static void saveNoST(String noProduksi, String tglProduksi, List<String> noSTList) {
        if (noSTList == null || noSTList.isEmpty()) {
            Log.e("SaveError", "List NoST kosong, tidak ada data untuk disimpan.");
            return;
        }

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement insertStmt = con.prepareStatement("INSERT INTO S4SProduksiInputST (NoProduksi, NoST) VALUES (?, ?)");
             PreparedStatement updateStmt = con.prepareStatement("UPDATE ST_h SET DateUsage = ? WHERE NoST = ?")) {

            // Insert Data ke S4SProduksiInputST
            for (String noST : noSTList) {
                insertStmt.setString(1, noProduksi);
                insertStmt.setString(2, noST);
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


    public static void saveNoMoulding(String noProduksi, String tglProduksi, List<String> noMouldingList) {
        if (noMouldingList == null || noMouldingList.isEmpty()) {
            Log.e("SaveError", "List NoMoulding kosong, tidak ada data untuk disimpan.");
            return;
        }

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement insertStmt = con.prepareStatement("INSERT INTO S4SProduksiInputMoulding (NoProduksi, NoMoulding) VALUES (?, ?)");
             PreparedStatement updateStmt = con.prepareStatement("UPDATE Moulding_h SET DateUsage = ? WHERE NoMoulding = ?")) {

            // Insert Data ke S4SProduksiInputMoulding
            for (String noMoulding : noMouldingList) {
                insertStmt.setString(1, noProduksi);
                insertStmt.setString(2, noMoulding);
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


    public static void saveNoFJ(String noProduksi, String tglProduksi, List<String> noFJList) {
        if (noFJList == null || noFJList.isEmpty()) {
            Log.e("SaveError", "List NoFJ kosong, tidak ada data untuk disimpan.");
            return;
        }

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement insertStmt = con.prepareStatement("INSERT INTO S4SProduksiInputFJ (NoProduksi, NoFJ) VALUES (?, ?)");
             PreparedStatement updateStmt = con.prepareStatement("UPDATE FJ_h SET DateUsage = ? WHERE NoFJ = ?")) {

            // Insert Data ke S4SProduksiInputFJ
            for (String noFJ : noFJList) {
                insertStmt.setString(1, noProduksi);
                insertStmt.setString(2, noFJ);
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


    public static void saveNoCC(String noProduksi, String tglProduksi, List<String> noCCList) {
        if (noCCList == null || noCCList.isEmpty()) {
            Log.e("SaveError", "List NoCCAkhir kosong, tidak ada data untuk disimpan.");
            return;
        }

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement insertStmt = con.prepareStatement("INSERT INTO S4SProduksiInputCCAkhir (NoProduksi, NoCCAkhir) VALUES (?, ?)");
             PreparedStatement updateStmt = con.prepareStatement("UPDATE CCAkhir_h SET DateUsage = ? WHERE NoCCAkhir = ?")) {

            // Insert Data ke S4SProduksiInputCCAkhir
            for (String noCCAkhir : noCCList) {
                insertStmt.setString(1, noProduksi);
                insertStmt.setString(2, noCCAkhir);
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


    public static void saveNoReproses(String noProduksi, String tglProduksi, List<String> noReprosesList) {
        if (noReprosesList == null || noReprosesList.isEmpty()) {
            Log.e("SaveError", "List NoReproses kosong, tidak ada data untuk disimpan.");
            return;
        }

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement insertStmt = con.prepareStatement("INSERT INTO S4SProduksiInputReproses (NoProduksi, NoReproses) VALUES (?, ?)");
             PreparedStatement updateStmt = con.prepareStatement("UPDATE Reproses_h SET DateUsage = ? WHERE NoReproses = ?")) {

            // Insert Data ke S4SProduksiInputReproses
            for (String noReproses : noReprosesList) {
                insertStmt.setString(1, noProduksi);
                insertStmt.setString(2, noReproses);
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

        return "-"; // Return a default message if result is not found
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

        return "-"; // Return a default message if result is not found
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

        return "-"; // Return a default message if result is not found
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

        return "-"; // Return a default message if result is not found
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
                                    return "Proses Produksi Sanding - CC Akhir";
                                case "S4SProduksiInputCCAkhir":
                                    return "Proses Produksi S4S - CC Akhir";
                                case "MouldingProduksiInputCCAkhir":
                                    return "Proses Produksi Moulding - CC Akhir";
                                case "LaminatingProduksiInputCCAkhir":
                                    return "Proses Produksi Laminating - CC Akhir";
                                case "FJProduksiInputCCAkhir":
                                    return "Proses Produksi Finger Joint - CC Akhir";
                                case "AdjustmentInputCCAkhir":
                                    return "Adjustment Cross Cut - CC Akhir";
                                case "BongkarSusunInputCCAkhir":
                                    return "Bongkar Susun Cross Cut - CC Akhir";
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

        return "-"; // Return a default message if result is not found
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

        return "-"; // Return a default message if result is not found
    }














}