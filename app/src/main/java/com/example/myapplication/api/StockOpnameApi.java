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
                "SELECT [NoLabel] " +
                        "FROM ( " +
                        "    SELECT sost.[NoST] AS NoLabel FROM [dbo].[StockOpnameST] sost " +
                        "    INNER JOIN [dbo].[ST_h] sth ON sost.[NoST] = sth.[NoST] " +
                        "    WHERE sost.[NoSO] = ? " +
                        "    AND sost.[NoST] NOT IN (SELECT [NoST] FROM [dbo].[StockOpname_Hasil_d_ST] WHERE [NoSO] = ?) " +
                        "    AND (sth.[DateUsage] > ? OR sth.[DateUsage] IS NULL) " +
                        "    UNION ALL " +
                        "    SELECT sos4s.[NoS4S] AS NoLabel FROM [dbo].[StockOpnameS4S] sos4s " +
                        "    INNER JOIN [dbo].[S4S_h] s4h ON sos4s.[NoS4S] = s4h.[NoS4S] " +
                        "    WHERE sos4s.[NoSO] = ? " +
                        "    AND sos4s.[NoS4S] NOT IN (SELECT [NoS4S] FROM [dbo].[StockOpname_Hasil_d_S4S] WHERE [NoSO] = ?) " +
                        "    AND (s4h.[DateUsage] > ? OR s4h.[DateUsage] IS NULL) " +
                        "    UNION ALL " +
                        "    SELECT sofj.[NoFJ] AS NoLabel FROM [dbo].[StockOpnameFJ] sofj " +
                        "    INNER JOIN [dbo].[FJ_h] fjh ON sofj.[NoFJ] = fjh.[NoFJ] " +
                        "    WHERE sofj.[NoSO] = ? " +
                        "    AND sofj.[NoFJ] NOT IN (SELECT [NoFJ] FROM [dbo].[StockOpname_Hasil_d_FJ] WHERE [NoSO] = ?) " +
                        "    AND (fjh.[DateUsage] > ? OR fjh.[DateUsage] IS NULL) " +
                        "    UNION ALL " +
                        "    SELECT som.[NoMoulding] AS NoLabel FROM [dbo].[StockOpnameMoulding] som " +
                        "    INNER JOIN [dbo].[Moulding_h] mh ON som.[NoMoulding] = mh.[NoMoulding] " +
                        "    WHERE som.[NoSO] = ? " +
                        "    AND som.[NoMoulding] NOT IN (SELECT [NoMoulding] FROM [dbo].[StockOpname_Hasil_d_Moulding] WHERE [NoSO] = ?) " +
                        "    AND (mh.[DateUsage] > ? OR mh.[DateUsage] IS NULL) " +
                        "    UNION ALL " +
                        "    SELECT sol.[NoLaminating] AS NoLabel FROM [dbo].[StockOpnameLaminating] sol " +
                        "    INNER JOIN [dbo].[Laminating_h] lh ON sol.[NoLaminating] = lh.[NoLaminating] " +
                        "    WHERE sol.[NoSO] = ? " +
                        "    AND sol.[NoLaminating] NOT IN (SELECT [NoLaminating] FROM [dbo].[StockOpname_Hasil_d_Laminating] WHERE [NoSO] = ?) " +
                        "    AND (lh.[DateUsage] > ? OR lh.[DateUsage] IS NULL) " +
                        "    UNION ALL " +
                        "    SELECT soc.[NoCCAkhir] AS NoLabel FROM [dbo].[StockOpnameCCAkhir] soc " +
                        "    INNER JOIN [dbo].[CCAkhir_h] ccah ON soc.[NoCCAkhir] = ccah.[NoCCAkhir] " +
                        "    WHERE soc.[NoSO] = ? " +
                        "    AND soc.[NoCCAkhir] NOT IN (SELECT [NoCCAkhir] FROM [dbo].[StockOpname_Hasil_d_CCAkhir] WHERE [NoSO] = ?) " +
                        "    AND (ccah.[DateUsage] > ? OR ccah.[DateUsage] IS NULL) " +
                        "    UNION ALL " +
                        "    SELECT sosand.[NoSanding] AS NoLabel FROM [dbo].[StockOpnameSanding] sosand " +
                        "    INNER JOIN [dbo].[Sanding_h] sh ON sosand.[NoSanding] = sh.[NoSanding] " +
                        "    WHERE sosand.[NoSO] = ? " +
                        "    AND sosand.[NoSanding] NOT IN (SELECT [NoSanding] FROM [dbo].[StockOpname_Hasil_d_Sanding] WHERE [NoSO] = ?) " +
                        "    AND (sh.[DateUsage] > ? OR sh.[DateUsage] IS NULL) " +
                        "    UNION ALL " +
                        "    SELECT sobj.[NoBJ] AS NoLabel FROM [dbo].[StockOpnameBJ] sobj " +
                        "    INNER JOIN [dbo].[BarangJadi_h] bjh ON sobj.[NoBJ] = bjh.[NoBJ] " +
                        "    WHERE sobj.[NoSO] = ? " +
                        "    AND sobj.[NoBJ] NOT IN (SELECT [NoBJ] FROM [dbo].[StockOpname_Hasil_d_BJ] WHERE [NoSO] = ?) " +
                        "    AND (bjh.[DateUsage] > ? OR bjh.[DateUsage] IS NULL) " +
                        ") AS CombinedResults " +
                        "ORDER BY NoLabel " +
                        "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";  // Menambahkan OFFSET dan FETCH NEXT

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
                    stockOpnameDataByNoSOList.add(new StockOpnameDataByNoSO(noLabel));
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
                "SELECT [NoST] AS NoLabel FROM [dbo].[StockOpname_Hasil_d_ST] WHERE [NoSO] = ? " +
                        "UNION ALL " +
                        "SELECT [NoS4S] AS NoLabel FROM [dbo].[StockOpname_Hasil_d_S4S] WHERE [NoSO] = ? " +
                        "UNION ALL " +
                        "SELECT [NoFJ] AS NoLabel FROM [dbo].[StockOpname_Hasil_d_FJ] WHERE [NoSO] = ? " +
                        "UNION ALL " +
                        "SELECT [NoMoulding] AS NoLabel FROM [dbo].[StockOpname_Hasil_d_Moulding] WHERE [NoSO] = ? " +
                        "UNION ALL " +
                        "SELECT [NoLaminating] AS NoLabel FROM [dbo].[StockOpname_Hasil_d_Laminating] WHERE [NoSO] = ? " +
                        "UNION ALL " +
                        "SELECT [NoCCAkhir] AS NoLabel FROM [dbo].[StockOpname_Hasil_d_CCAkhir] WHERE [NoSO] = ? " +
                        "UNION ALL " +
                        "SELECT [NoSanding] AS NoLabel FROM [dbo].[StockOpname_Hasil_d_Sanding] WHERE [NoSO] = ? " +
                        "UNION ALL " +
                        "SELECT [NoBJ] AS NoLabel FROM [dbo].[StockOpname_Hasil_d_BJ] WHERE [NoSO] = ? " +
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

                    // Menambahkan data ke dalam list
                    stockOpnameDataInputByNoSOList.add(new StockOpnameDataInputByNoSO(noLabel));
                }
            }
        } catch (SQLException e) {
            Log.e("Database Fetch Error", "Error fetching data: " + e.getMessage());
        }

        return stockOpnameDataInputByNoSOList;
    }




}
