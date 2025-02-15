package com.example.myapplication.api;

import android.util.Log;

import com.example.myapplication.DatabaseConfig;
import com.example.myapplication.model.StockOpnameData;
import com.example.myapplication.model.StockOpnameDataByNoSO;
import com.example.myapplication.model.StockOpnameDataInputByNoSO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
        Log.d("Paging", "fetchDataByNoSO " + offset + " - " + limit);

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
        Log.d("Paging", "fetchDataInputBySearch with noSO: " + noSO + ", searchTerm: " + searchTerm + ", offset: " + offset + " and limit: " + limit);

        // Memodifikasi query untuk menambahkan search term pada kondisi WHERE
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
                        "WHERE 1=1 " + // For easy addition of further conditions
                        (searchTerm != null && !searchTerm.isEmpty() ? "AND (NoLabel LIKE ? OR IdLokasi LIKE ?)" : "") +
                        "ORDER BY NoLabel " +
                        "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";  // Menambahkan OFFSET dan LIMIT

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            // Set parameter NoSO untuk setiap query
            for (int i = 1; i <= 8; i++) {
                stmt.setString(i, noSO);
            }

            // If a search term is provided, set it for NoLabel and IdLokasi filters
            if (searchTerm != null && !searchTerm.isEmpty()) {
                stmt.setString(9, "%" + searchTerm + "%");  // NoLabel search term
                stmt.setString(10, "%" + searchTerm + "%"); // IdLokasi search term
                stmt.setInt(11, offset); // OFFSET
                stmt.setInt(12, limit); // LIMIT
            } else {
                // No search term provided, only set the OFFSET and LIMIT
                stmt.setInt(9, offset); // OFFSET
                stmt.setInt(10, limit); // LIMIT
            }

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





}
