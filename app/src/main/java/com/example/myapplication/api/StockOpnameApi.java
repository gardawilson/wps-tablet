package com.example.myapplication.api;

import android.util.Log;

import com.example.myapplication.DatabaseConfig;
import com.example.myapplication.model.LokasiBlok;
import com.example.myapplication.model.StockOpnameData;
import com.example.myapplication.model.StockOpnameDataByNoSO;
import com.example.myapplication.model.StockOpnameDataInputByNoSO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StockOpnameApi {

    public static List<StockOpnameData> getStockOpnameData(int offset, int limit) {
        List<StockOpnameData> stockOpnames = new ArrayList<>();

        // Menyesuaikan query dengan offset dan limit
        String query = "SELECT [NoSO], [Tgl] FROM [dbo].[StockOpname_h] ORDER BY NoSO DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            // Menetapkan parameter OFFSET dan LIMIT
            stmt.setInt(1, offset);  // OFFSET
            stmt.setInt(2, limit);    // LIMIT

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String noSO = rs.getString("NoSO");
                    String tgl = rs.getString("Tgl");

                    // Debugging
                    Log.d("Database Data", "NoSO: " + noSO + ", Tgl: " + tgl);

                    stockOpnames.add(new StockOpnameData(noSO, tgl));
                }
            }

        } catch (SQLException e) {
            Log.e("Database Fetch Error", "Error fetching data: " + e.getMessage());
        }

        return stockOpnames;
    }

    public static List<StockOpnameDataByNoSO> getStockOpnameDataByNoSO(String noSO, String tglSO, int offset, int limit) {
        List<StockOpnameDataByNoSO> stockOpnameDataByNoSOList = new ArrayList<>();
        Log.d("DataByNoso", "fetchDataByNoSO " + offset + " - " + limit);

        // Memodifikasi query untuk menambahkan OFFSET dan LIMIT
        String query =
                "SELECT [NoLabel], [IdLokasi] " +
                        "FROM ( " +
                        "    SELECT sost.[NoST] AS NoLabel, sost.IdLokasi " +
                        "    FROM [dbo].[StockOpnameST] sost " +
                        "    INNER JOIN [dbo].[ST_h] sth ON sost.[NoST] = sth.[NoST] " +
                        "    WHERE sost.[NoSO] = ? " +
                        "    AND sost.[NoST] NOT IN (SELECT [NoST] FROM [dbo].[StockOpname_Hasil_d_ST] WHERE [NoSO] = ?) " +
                        "    AND (sth.[DateUsage] > ? OR sth.[DateUsage] IS NULL) " +
                        "    UNION ALL " +
                        "    SELECT sos4s.[NoS4S] AS NoLabel, sos4s.IdLokasi " +
                        "    FROM [dbo].[StockOpnameS4S] sos4s " +
                        "    INNER JOIN [dbo].[S4S_h] s4h ON sos4s.[NoS4S] = s4h.[NoS4S] " +
                        "    WHERE sos4s.[NoSO] = ? " +
                        "    AND sos4s.[NoS4S] NOT IN (SELECT [NoS4S] FROM [dbo].[StockOpname_Hasil_d_S4S] WHERE [NoSO] = ?) " +
                        "    AND (s4h.[DateUsage] > ? OR s4h.[DateUsage] IS NULL) " +
                        "    UNION ALL " +
                        "    SELECT sofj.[NoFJ] AS NoLabel, sofj.IdLokasi " +
                        "    FROM [dbo].[StockOpnameFJ] sofj " +
                        "    INNER JOIN [dbo].[FJ_h] fjh ON sofj.[NoFJ] = fjh.[NoFJ] " +
                        "    WHERE sofj.[NoSO] = ? " +
                        "    AND sofj.[NoFJ] NOT IN (SELECT [NoFJ] FROM [dbo].[StockOpname_Hasil_d_FJ] WHERE [NoSO] = ?) " +
                        "    AND (fjh.[DateUsage] > ? OR fjh.[DateUsage] IS NULL) " +
                        "    UNION ALL " +
                        "    SELECT som.[NoMoulding] AS NoLabel, som.IdLokasi " +
                        "    FROM [dbo].[StockOpnameMoulding] som " +
                        "    INNER JOIN [dbo].[Moulding_h] mh ON som.[NoMoulding] = mh.[NoMoulding] " +
                        "    WHERE som.[NoSO] = ? " +
                        "    AND som.[NoMoulding] NOT IN (SELECT [NoMoulding] FROM [dbo].[StockOpname_Hasil_d_Moulding] WHERE [NoSO] = ?) " +
                        "    AND (mh.[DateUsage] > ? OR mh.[DateUsage] IS NULL) " +
                        "    UNION ALL " +
                        "    SELECT sol.[NoLaminating] AS NoLabel, sol.IdLokasi " +
                        "    FROM [dbo].[StockOpnameLaminating] sol " +
                        "    INNER JOIN [dbo].[Laminating_h] lh ON sol.[NoLaminating] = lh.[NoLaminating] " +
                        "    WHERE sol.[NoSO] = ? " +
                        "    AND sol.[NoLaminating] NOT IN (SELECT [NoLaminating] FROM [dbo].[StockOpname_Hasil_d_Laminating] WHERE [NoSO] = ?) " +
                        "    AND (lh.[DateUsage] > ? OR lh.[DateUsage] IS NULL) " +
                        "    UNION ALL " +
                        "    SELECT soc.[NoCCAkhir] AS NoLabel, soc.IdLokasi " +
                        "    FROM [dbo].[StockOpnameCCAkhir] soc " +
                        "    INNER JOIN [dbo].[CCAkhir_h] ccah ON soc.[NoCCAkhir] = ccah.[NoCCAkhir] " +
                        "    WHERE soc.[NoSO] = ? " +
                        "    AND soc.[NoCCAkhir] NOT IN (SELECT [NoCCAkhir] FROM [dbo].[StockOpname_Hasil_d_CCAkhir] WHERE [NoSO] = ?) " +
                        "    AND (ccah.[DateUsage] > ? OR ccah.[DateUsage] IS NULL) " +
                        "    UNION ALL " +
                        "    SELECT sosand.[NoSanding] AS NoLabel, sosand.IdLokasi " +
                        "    FROM [dbo].[StockOpnameSanding] sosand " +
                        "    INNER JOIN [dbo].[Sanding_h] sh ON sosand.[NoSanding] = sh.[NoSanding] " +
                        "    WHERE sosand.[NoSO] = ? " +
                        "    AND sosand.[NoSanding] NOT IN (SELECT [NoSanding] FROM [dbo].[StockOpname_Hasil_d_Sanding] WHERE [NoSO] = ?) " +
                        "    AND (sh.[DateUsage] > ? OR sh.[DateUsage] IS NULL) " +
                        "    UNION ALL " +
                        "    SELECT sobj.[NoBJ] AS NoLabel, sobj.IdLokasi " +
                        "    FROM [dbo].[StockOpnameBJ] sobj " +
                        "    INNER JOIN [dbo].[BarangJadi_h] bjh ON sobj.[NoBJ] = bjh.[NoBJ] " +
                        "    WHERE sobj.[NoSO] = ? " +
                        "    AND sobj.[NoBJ] NOT IN (SELECT [NoBJ] FROM [dbo].[StockOpname_Hasil_d_BJ] WHERE [NoSO] = ?) " +
                        "    AND (bjh.[DateUsage] > ? OR bjh.[DateUsage] IS NULL) " +
                        ") AS CombinedResults " +
                        "ORDER BY NoLabel " +
                        "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            int parameterIndex = 1;
            for (int i = 0; i < 8; i++) {
                stmt.setString(parameterIndex++, noSO);
                stmt.setString(parameterIndex++, noSO);
                stmt.setString(parameterIndex++, tglSO);
            }

            // Set OFFSET dan LIMIT
            stmt.setInt(parameterIndex++, offset); // OFFSET
            stmt.setInt(parameterIndex++, limit); // LIMIT

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String noLabel = rs.getString("NoLabel");
                    String idLokasi = rs.getString("IdLokasi");
                    stockOpnameDataByNoSOList.add(new StockOpnameDataByNoSO(noLabel, idLokasi));
                }
            }
        } catch (SQLException e) {
            Log.e("Database Fetch Error", "Error fetching " + e.getMessage());
        }

        return stockOpnameDataByNoSOList;
    }


    public static List<StockOpnameDataInputByNoSO> getStockOpnameDataInputByNoSO(String noSO, int offset, int limit) {
        List<StockOpnameDataInputByNoSO> stockOpnameDataInputByNoSOList = new ArrayList<>();
        Log.d("Paging", "fetchDataInputByNoSO with offset: " + offset + " and limit: " + limit);

        // Memodifikasi query untuk menambahkan OFFSET dan LIMIT
        String query =
                "SELECT NoLabel, IdLokasi, UserID FROM ( " +
                        "    SELECT [NoST] AS NoLabel, IdLokasi, UserID FROM [dbo].[StockOpname_Hasil_d_ST] WHERE [NoSO] = ? " +
                        "    UNION ALL " +
                        "    SELECT [NoS4S] AS NoLabel, IdLokasi, UserID FROM [dbo].[StockOpname_Hasil_d_S4S] WHERE [NoSO] = ? " +
                        "    UNION ALL " +
                        "    SELECT [NoFJ] AS NoLabel, IdLokasi, UserID FROM [dbo].[StockOpname_Hasil_d_FJ] WHERE [NoSO] = ? " +
                        "    UNION ALL " +
                        "    SELECT [NoMoulding] AS NoLabel, IdLokasi, UserID FROM [dbo].[StockOpname_Hasil_d_Moulding] WHERE [NoSO] = ? " +
                        "    UNION ALL " +
                        "    SELECT [NoLaminating] AS NoLabel, IdLokasi, UserID FROM [dbo].[StockOpname_Hasil_d_Laminating] WHERE [NoSO] = ? " +
                        "    UNION ALL " +
                        "    SELECT [NoCCAkhir] AS NoLabel, IdLokasi, UserID FROM [dbo].[StockOpname_Hasil_d_CCAkhir] WHERE [NoSO] = ? " +
                        "    UNION ALL " +
                        "    SELECT [NoSanding] AS NoLabel, IdLokasi, UserID FROM [dbo].[StockOpname_Hasil_d_Sanding] WHERE [NoSO] = ? " +
                        "    UNION ALL " +
                        "    SELECT [NoBJ] AS NoLabel, IdLokasi, UserID FROM [dbo].[StockOpname_Hasil_d_BJ] WHERE [NoSO] = ? " +
                        ") AS CombinedResults " +
                        "ORDER BY NoLabel " +
                        "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";  // Menambahkan OFFSET dan LIMIT

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            // Set parameter NoSO untuk setiap query
            for (int i = 1; i <= 8; i++) {
                stmt.setString(i, noSO);
            }

            // Set parameter OFFSET dan LIMIT
            stmt.setInt(9, offset); // OFFSET
            stmt.setInt(10, limit); // LIMIT

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String noLabel = rs.getString("NoLabel");
                    String idLokasi = rs.getString("IdLokasi");
                    String userId = rs.getString("UserID");

                    // Menambahkan data ke dalam list
                    stockOpnameDataInputByNoSOList.add(new StockOpnameDataInputByNoSO(noLabel, idLokasi, userId));
                }
            }
        } catch (SQLException e) {
            Log.e("Database Fetch Error", "Error fetching data: " + e.getMessage());
        }

        return stockOpnameDataInputByNoSOList;
    }


    public static List<StockOpnameDataInputByNoSO> getStockOpnameDataInputBySearch(String noSO, String searchTerm, int offset, int limit) {
        List<StockOpnameDataInputByNoSO> stockOpnameDataInputByNoSOList = new ArrayList<>();
        Log.d("Paging", "fetchDataInputBySearch with searchTerm: " + searchTerm + ", offset: " + offset + " and limit: " + limit);

        // Modify the query to add the search term condition
        String query =
                "SELECT NoLabel, IdLokasi, UserID FROM ( " +
                        "    SELECT [NoST] AS NoLabel, IdLokasi, UserID FROM [dbo].[StockOpname_Hasil_d_ST] WHERE [NoSO] = ? " +
                        "    UNION ALL " +
                        "    SELECT [NoS4S] AS NoLabel, IdLokasi, UserID FROM [dbo].[StockOpname_Hasil_d_S4S] WHERE [NoSO] = ? " +
                        "    UNION ALL " +
                        "    SELECT [NoFJ] AS NoLabel, IdLokasi, UserID FROM [dbo].[StockOpname_Hasil_d_FJ] WHERE [NoSO] = ? " +
                        "    UNION ALL " +
                        "    SELECT [NoMoulding] AS NoLabel, IdLokasi, UserID FROM [dbo].[StockOpname_Hasil_d_Moulding] WHERE [NoSO] = ? " +
                        "    UNION ALL " +
                        "    SELECT [NoLaminating] AS NoLabel, IdLokasi, UserID FROM [dbo].[StockOpname_Hasil_d_Laminating] WHERE [NoSO] = ? " +
                        "    UNION ALL " +
                        "    SELECT [NoCCAkhir] AS NoLabel, IdLokasi, UserID FROM [dbo].[StockOpname_Hasil_d_CCAkhir] WHERE [NoSO] = ? " +
                        "    UNION ALL " +
                        "    SELECT [NoSanding] AS NoLabel, IdLokasi, UserID FROM [dbo].[StockOpname_Hasil_d_Sanding] WHERE [NoSO] = ? " +
                        "    UNION ALL " +
                        "    SELECT [NoBJ] AS NoLabel, IdLokasi, UserID FROM [dbo].[StockOpname_Hasil_d_BJ] WHERE [NoSO] = ? " +
                        ") AS CombinedResults " +
                        "WHERE 1=1 " +
                        "AND (NoLabel LIKE ? OR IdLokasi LIKE ?)"  +
                        "ORDER BY NoLabel " +
                        "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";  // Added OFFSET and LIMIT

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            // Set parameter NoSO for each query
            for (int i = 1; i <= 8; i++) {
                stmt.setString(i, noSO);
            }

            // If a search term is provided, set it for NoLabel and IdLokasi filters
            if (searchTerm != null && !searchTerm.isEmpty()) {
                stmt.setString(9, "%" + searchTerm + "%");  // Set search term for NoLabel
                stmt.setString(10, "%" + searchTerm + "%"); // Set search term for IdLokasi
                stmt.setInt(11, offset); // OFFSET for pagination
                stmt.setInt(12, limit); // LIMIT for pagination
            } else {
                // No search term provided, only set the OFFSET and LIMIT
                stmt.setInt(9, offset); // OFFSET
                stmt.setInt(10, limit); // LIMIT
            }

            // Execute the query and process the result set
            try (ResultSet rs = stmt.executeQuery()) {
                // Log the number of rows fetched
                if (!rs.isBeforeFirst()) {
                    Log.d("Paging", "No results found for the query.");
                }

                // Process the result set
                while (rs.next()) {
                    String noLabel = rs.getString("NoLabel");
                    String idLokasi = rs.getString("IdLokasi");
                    String userId = rs.getString("UserID");

                    Log.d("Paging", "Result - NoLabel: " + noLabel + ", IdLokasi: " + idLokasi + ", UserID: " + userId);

                    // Add data to the list
                    stockOpnameDataInputByNoSOList.add(new StockOpnameDataInputByNoSO(noLabel, idLokasi, userId));
                }

            }

        } catch (SQLException e) {
            Log.e("Database Fetch Error", "Error fetching data: " + e.getMessage());
        }

        return stockOpnameDataInputByNoSOList;
    }


    public static boolean deleteStockOpnameDataInputByNoLabel(String noLabel) {
        boolean isDeleted = false;

        // Tentukan tabel dan kolom berdasarkan huruf awal NoLabel
        String tableName = getTableNameByNoLabel(noLabel);
        String columnName = getColumnNameByNoLabel(noLabel);

        if (tableName == null || columnName == null) {
            Log.e("Database Delete Error", "Invalid NoLabel: " + noLabel);
            return false;
        }

        // Query DELETE
        String deleteQuery = "DELETE FROM [dbo].[" + tableName + "] WHERE [" + columnName + "] = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(deleteQuery)) {

            stmt.setString(1, noLabel); // Set parameter NoLabel
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                isDeleted = true; // Data berhasil dihapus
                Log.d("Database Delete", "Deleted from " + tableName + " where " + columnName + " = " + noLabel);
            } else {
                Log.d("Database Delete", "No data found to delete for NoLabel: " + noLabel);
            }
        } catch (SQLException e) {
            Log.e("Database Delete Error", "Error deleting data: " + e.getMessage());
        }

        return isDeleted; // Mengembalikan status apakah data berhasil dihapus atau tidak
    }

    // Method untuk menentukan tabel berdasarkan huruf awal NoLabel
    private static String getTableNameByNoLabel(String noLabel) {
        if (noLabel == null || noLabel.isEmpty()) {
            return null;
        }

        char firstChar = noLabel.charAt(0);
        switch (firstChar) {
            case 'E':
                return "StockOpname_Hasil_d_ST";
            case 'R':
                return "StockOpname_Hasil_d_S4S";
            case 'S':
                return "StockOpname_Hasil_d_FJ";
            case 'T':
                return "StockOpname_Hasil_d_Moulding";
            case 'U':
                return "StockOpname_Hasil_d_Laminating";
            case 'V':
                return "StockOpname_Hasil_d_CCAkhir";
            case 'W':
                return "StockOpname_Hasil_d_Sanding";
            case 'I':
                return "StockOpname_Hasil_d_BJ";
            default:
                return null; // NoLabel tidak valid
        }
    }

    // Method untuk menentukan kolom berdasarkan huruf awal NoLabel
    private static String getColumnNameByNoLabel(String noLabel) {
        if (noLabel == null || noLabel.isEmpty()) {
            return null;
        }

        char firstChar = noLabel.charAt(0);
        switch (firstChar) {
            case 'E':
                return "NoST";
            case 'R':
                return "NoS4S";
            case 'S':
                return "NoFJ";
            case 'T':
                return "NoMoulding";
            case 'U':
                return "NoLaminating";
            case 'V':
                return "NoCCAkhir";
            case 'W':
                return "NoSanding";
            case 'I':
                return "NoBJ";
            default:
                return null; // NoLabel tidak valid
        }
    }

    public static List<LokasiBlok> getLokasiAndBlok() {
        List<LokasiBlok> lokasiBlokList = new ArrayList<>();

        // Query untuk mengambil data lokasi dan blok
        String query = "SELECT [IdLokasi], [Blok] FROM [dbo].[MstLokasi]";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String idLokasi = rs.getString("IdLokasi");
                    String blok = rs.getString("Blok");

                    lokasiBlokList.add(new LokasiBlok(idLokasi, blok));
                }
            }
        } catch (SQLException e) {
            Log.e("Database Fetch Error", "Error fetching data: " + e.getMessage());
        }

        return lokasiBlokList;
    }

//    public static List<StockOpnameDataInputByNoSO> getStockOpnameDataInputByFilter(String noSO, String idLokasi, int offset, int limit) {
//        List<StockOpnameDataInputByNoSO> stockOpnameDataInputByNoSOList = new ArrayList<>();
//        Log.d("Paging", "fetchDataInputByNoSO with offset: " + offset + " and limit: " + limit);
//
//        // Memodifikasi query untuk menambahkan filter berdasarkan NoSO dan IdLokasi
//        String query =
//                "SELECT NoLabel, IdLokasi, UserID FROM ( " +
//                        "    SELECT [NoST] AS NoLabel, IdLokasi, UserID FROM [dbo].[StockOpname_Hasil_d_ST] WHERE [NoSO] = ? AND IdLokasi = ? " +
//                        "    UNION ALL " +
//                        "    SELECT [NoS4S] AS NoLabel, IdLokasi, UserID FROM [dbo].[StockOpname_Hasil_d_S4S] WHERE [NoSO] = ? AND IdLokasi = ? " +
//                        "    UNION ALL " +
//                        "    SELECT [NoFJ] AS NoLabel, IdLokasi, UserID FROM [dbo].[StockOpname_Hasil_d_FJ] WHERE [NoSO] = ? AND IdLokasi = ? " +
//                        "    UNION ALL " +
//                        "    SELECT [NoMoulding] AS NoLabel, IdLokasi, UserID FROM [dbo].[StockOpname_Hasil_d_Moulding] WHERE [NoSO] = ? AND IdLokasi = ? " +
//                        "    UNION ALL " +
//                        "    SELECT [NoLaminating] AS NoLabel, IdLokasi, UserID FROM [dbo].[StockOpname_Hasil_d_Laminating] WHERE [NoSO] = ? AND IdLokasi = ? " +
//                        "    UNION ALL " +
//                        "    SELECT [NoCCAkhir] AS NoLabel, IdLokasi, UserID FROM [dbo].[StockOpname_Hasil_d_CCAkhir] WHERE [NoSO] = ? AND IdLokasi = ? " +
//                        "    UNION ALL " +
//                        "    SELECT [NoSanding] AS NoLabel, IdLokasi, UserID FROM [dbo].[StockOpname_Hasil_d_Sanding] WHERE [NoSO] = ? AND IdLokasi = ? " +
//                        "    UNION ALL " +
//                        "    SELECT [NoBJ] AS NoLabel, IdLokasi, UserID FROM [dbo].[StockOpname_Hasil_d_BJ] WHERE [NoSO] = ? AND IdLokasi = ? " +
//                        ") AS CombinedResults " +
//                        "ORDER BY NoLabel " +
//                        "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";  // Menambahkan OFFSET dan LIMIT
//
//        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
//             PreparedStatement stmt = con.prepareStatement(query)) {
//
//            // Set parameter NoSO dan IdLokasi untuk setiap query
//            for (int i = 1; i <= 8; i++) {
//                stmt.setString(i * 2 - 1, noSO);   // Set NoSO
//                stmt.setString(i * 2, idLokasi);  // Set IdLokasi
//            }
//
//            // Set parameter OFFSET dan LIMIT
//            stmt.setInt(17, offset); // OFFSET
//            stmt.setInt(18, limit); // LIMIT
//
//            try (ResultSet rs = stmt.executeQuery()) {
//                while (rs.next()) {
//                    String noLabel = rs.getString("NoLabel");
//                    String idLokasiResult = rs.getString("IdLokasi");
//                    String userId = rs.getString("UserID");
//
//                    // Menambahkan data ke dalam list
//                    stockOpnameDataInputByNoSOList.add(new StockOpnameDataInputByNoSO(noLabel, idLokasiResult, userId));
//                }
//            }
//        } catch (SQLException e) {
//            Log.e("Database Fetch Error", "Error fetching data: " + e.getMessage());
//        }
//
//        return stockOpnameDataInputByNoSOList;
//    }


    public static List<StockOpnameDataInputByNoSO> getStockOpnameDataInputByFilter(String noSO, String selectedLokasi, Set<String> selectedLabels, int offset, int limit) {
        List<StockOpnameDataInputByNoSO> stockOpnameDataInputByNoSOList = new ArrayList<>();
        Log.d("valuefilter", "fetchDataInputByNoSO with offset: " + offset + " and limit: " + limit);

        // Menentukan query dasar
        StringBuilder query = new StringBuilder("SELECT NoLabel, IdLokasi, UserID FROM ( ");
        int noSOParamCount = 0; // Menghitung jumlah parameter NoSO
        boolean isFirstQuery = true;  // Flag untuk memastikan UNION ALL tidak ditambahkan di awal

        // Tambahkan kondisi berdasarkan selectedLabels (filter CheckBox)
        for (String label : selectedLabels) {
            if (!label.equals("all")) {  // Jangan menambahkan "all", karena akan mencakup semua
                if (!isFirstQuery) query.append("UNION ALL ");
                switch (label) {
                    case "ST":
                        query.append("SELECT [NoST] AS NoLabel, IdLokasi, UserID FROM [dbo].[StockOpname_Hasil_d_ST] WHERE [NoSO] = ? ");
                        break;
                    case "S4S":
                        query.append("SELECT [NoS4S] AS NoLabel, IdLokasi, UserID FROM [dbo].[StockOpname_Hasil_d_S4S] WHERE [NoSO] = ? ");
                        break;
                    case "FJ":
                        query.append("SELECT [NoFJ] AS NoLabel, IdLokasi, UserID FROM [dbo].[StockOpname_Hasil_d_FJ] WHERE [NoSO] = ? ");
                        break;
                    case "Moulding":
                        query.append("SELECT [NoMoulding] AS NoLabel, IdLokasi, UserID FROM [dbo].[StockOpname_Hasil_d_Moulding] WHERE [NoSO] = ? ");
                        break;
                    case "Laminating":
                        query.append("SELECT [NoLaminating] AS NoLabel, IdLokasi, UserID FROM [dbo].[StockOpname_Hasil_d_Laminating] WHERE [NoSO] = ? ");
                        break;
                    case "CCAkhir":
                        query.append("SELECT [NoCCAkhir] AS NoLabel, IdLokasi, UserID FROM [dbo].[StockOpname_Hasil_d_CCAkhir] WHERE [NoSO] = ? ");
                        break;
                    case "Sanding":
                        query.append("SELECT [NoSanding] AS NoLabel, IdLokasi, UserID FROM [dbo].[StockOpname_Hasil_d_Sanding] WHERE [NoSO] = ? ");
                        break;
                    case "BJ":
                        query.append("SELECT [NoBJ] AS NoLabel, IdLokasi, UserID FROM [dbo].[StockOpname_Hasil_d_BJ] WHERE [NoSO] = ? ");
                        break;
                }
                noSOParamCount++;
                isFirstQuery = false; // Set isFirstQuery to false after the first query
            }
        }

        query.append(") AS CombinedResults WHERE 1=1 "); // Menambahkan WHERE clause dasar

        // Tambahkan filter untuk IdLokasi jika tidak "all"
        if (!selectedLokasi.equals("Semua")) {
            query.append("AND IdLokasi = ? ");
        }

        query.append("ORDER BY NoLabel OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        Log.d("SQL Query", query.toString()); // Log the query for debugging purposes
        Log.d("Number of Parameters", String.valueOf(noSOParamCount)); // Log number of parameters

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query.toString())) {

            int currentParamIndex = 1;

            // Set parameter NoSO untuk setiap bagian query
            for (int i = 0; i < noSOParamCount; i++) {
                stmt.setString(currentParamIndex++, noSO);
            }

            // Set parameter IdLokasi jika ada
            if (!selectedLokasi.equals("Semua")) {
                stmt.setString(currentParamIndex++, selectedLokasi);
            }

            // Set parameter OFFSET dan LIMIT
            stmt.setInt(currentParamIndex++, offset); // OFFSET
            stmt.setInt(currentParamIndex, limit); // LIMIT

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String noLabel = rs.getString("NoLabel");
                    String idLokasi = rs.getString("IdLokasi");
                    String userId = rs.getString("UserID");

                    // Menambahkan data ke dalam list
                    stockOpnameDataInputByNoSOList.add(new StockOpnameDataInputByNoSO(noLabel, idLokasi, userId));
                }
            }
        } catch (SQLException e) {
            Log.e("Database Fetch Error", "Error fetching data: " + e.getMessage());
        }

        return stockOpnameDataInputByNoSOList;
    }






}
