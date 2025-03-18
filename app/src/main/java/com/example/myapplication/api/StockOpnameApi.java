package com.example.myapplication.api;

import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.DatabaseConfig;
import com.example.myapplication.model.LokasiBlok;
import com.example.myapplication.model.StockOpnameData;
import com.example.myapplication.model.StockOpnameDataByNoSO;
import com.example.myapplication.model.StockOpnameDataInputByNoSO;
import com.example.myapplication.model.UserIDSO;

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

    public static List<StockOpnameData> getStockOpnameData() {
        List<StockOpnameData> stockOpnames = new ArrayList<>();

        // Query untuk mengambil seluruh data tanpa OFFSET dan LIMIT
        String query = "SELECT [NoSO], [Tgl] " +
                        "FROM [dbo].[StockOpname_h] " +
                        "WHERE [Tgl] > (SELECT MAX(PeriodHarian) FROM [dbo].[MstTutupTransaksiHarian]) " +
                        "ORDER BY [NoSO] DESC";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

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


    public static List<StockOpnameDataByNoSO> getStockOpnameDataByNoSO(
            String noSO, String tglSO, String selectedBlok, String selectedLokasi,
            Set<String> selectedLabels, int offset, int limit) {

        List<StockOpnameDataByNoSO> stockOpnameDataByNoSOList = new ArrayList<>();
        Log.d("DataByNoso", "fetchDataByNoSO with offset: " + offset + " and limit: " + limit);

        // Membangun query secara dinamis
        StringBuilder query = new StringBuilder("SELECT c.NoLabel, c.IdLokasi FROM ( ");
        int noSOParamCount = 0; // Menghitung jumlah parameter NoSO
        boolean isFirstQuery = true; // Flag untuk memastikan UNION ALL tidak ditambahkan di awal

        // Tambahkan subquery berdasarkan selectedLabels
        for (String label : selectedLabels) {
            if (!label.equals("all")) { // Skip jika label adalah "all"
                if (!isFirstQuery) query.append("UNION ALL ");
                switch (label) {
                    case "ST":
                        query.append("SELECT sost.[NoST] AS NoLabel, sost.IdLokasi "
                                + "FROM [dbo].[StockOpnameST] sost "
                                + "INNER JOIN [dbo].[ST_h] sth ON sost.[NoST] = sth.[NoST] "
                                + "WHERE sost.[NoSO] = ? "
                                + "AND sost.[NoST] NOT IN ( "
                                + "    SELECT [NoST] FROM [dbo].[StockOpname_Hasil_d_ST] WHERE [NoSO] = ? "
                                + ") "
                                + "AND (sth.[DateUsage] > ? OR sth.[DateUsage] IS NULL)");
                        break;
                    case "S4S":
                        query.append("SELECT sos4s.[NoS4S] AS NoLabel, sos4s.IdLokasi "
                                + "FROM [dbo].[StockOpnameS4S] sos4s "
                                + "INNER JOIN [dbo].[S4S_h] s4h ON sos4s.[NoS4S] = s4h.[NoS4S] "
                                + "INNER JOIN [dbo].[S4S_d] s4d ON sos4s.[NoS4S] = s4d.NoS4S " // INNER JOIN dengan S4S_d
                                + "WHERE sos4s.[NoSO] = ? "
                                + "AND sos4s.[NoS4S] NOT IN ( "
                                + "    SELECT [NoS4S] FROM [dbo].[StockOpname_Hasil_d_S4S] WHERE [NoSO] = ? "
                                + ") "
                                + "AND (s4h.[DateUsage] > ? OR s4h.[DateUsage] IS NULL)");
                        break;
                    case "FJ":
                        query.append( "SELECT sofj.[NoFJ] AS NoLabel, sofj.IdLokasi "
                                + "FROM [dbo].[StockOpnameFJ] sofj "
                                + "INNER JOIN [dbo].[FJ_h] fjh ON sofj.[NoFJ] = fjh.[NoFJ] "
                                + "INNER JOIN [dbo].[FJ_d] fjd ON sofj.[NoFJ] = fjd.NoFJ " // INNER JOIN dengan FJ_d
                                + "WHERE sofj.[NoSO] = ? "
                                + "AND sofj.[NoFJ] NOT IN ( "
                                + "    SELECT [NoFJ] FROM [dbo].[StockOpname_Hasil_d_FJ] WHERE [NoSO] = ? "
                                + ") "
                                + "AND (fjh.[DateUsage] > ? OR fjh.[DateUsage] IS NULL)");
                        break;
                    case "MLD":
                        query.append("SELECT som.[NoMoulding] AS NoLabel, som.IdLokasi "
                                + "FROM [dbo].[StockOpnameMoulding] som "
                                + "INNER JOIN [dbo].[Moulding_h] mh ON som.[NoMoulding] = mh.[NoMoulding] "
                                + "INNER JOIN [dbo].[Moulding_d] mould ON som.[NoMoulding] = mould.NoMoulding "
                                + "WHERE som.[NoSO] = ? "
                                + "AND som.[NoMoulding] NOT IN ( "
                                + "    SELECT [NoMoulding] FROM [dbo].[StockOpname_Hasil_d_Moulding] WHERE [NoSO] = ? "
                                + ") "
                                + "AND (mh.[DateUsage] > ? OR mh.[DateUsage] IS NULL)");
                        break;
                    case "LMT":
                        query.append( "SELECT sol.[NoLaminating] AS NoLabel, sol.IdLokasi "
                                + "FROM [dbo].[StockOpnameLaminating] sol "
                                + "INNER JOIN [dbo].[Laminating_h] lh ON sol.[NoLaminating] = lh.[NoLaminating] "
                                + "INNER JOIN [dbo].[Laminating_d] ld ON sol.[NoLaminating] = ld.NoLaminating " // INNER JOIN dengan Laminating_d
                                + "WHERE sol.[NoSO] = ? "
                                + "AND sol.[NoLaminating] NOT IN ( "
                                + "    SELECT [NoLaminating] FROM [dbo].[StockOpname_Hasil_d_Laminating] WHERE [NoSO] = ? "
                                + ") "
                                + "AND (lh.[DateUsage] > ? OR lh.[DateUsage] IS NULL)");
                        break;
                    case "CC":
                        query.append( "SELECT soc.[NoCCAkhir] AS NoLabel, soc.IdLokasi "
                                + "FROM [dbo].[StockOpnameCCAkhir] soc "
                                + "INNER JOIN [dbo].[CCAkhir_h] ccah ON soc.[NoCCAkhir] = ccah.[NoCCAkhir] "
                                + "INNER JOIN [dbo].[CCAkhir_d] ccad ON soc.[NoCCAkhir] = ccad.NoCCAkhir " // INNER JOIN dengan CCAkhir_d
                                + "WHERE soc.[NoSO] = ? "
                                + "AND soc.[NoCCAkhir] NOT IN ( "
                                + "    SELECT [NoCCAkhir] FROM [dbo].[StockOpname_Hasil_d_CCAkhir] WHERE [NoSO] = ? "
                                + ") "
                                + "AND (ccah.[DateUsage] > ? OR ccah.[DateUsage] IS NULL)");
                        break;
                    case "SND":
                        query.append( "SELECT sosand.[NoSanding] AS NoLabel, sosand.IdLokasi "
                                + "FROM [dbo].[StockOpnameSanding] sosand "
                                + "INNER JOIN [dbo].[Sanding_h] sh ON sosand.[NoSanding] = sh.[NoSanding] "
                                + "INNER JOIN [dbo].[Sanding_d] sandd ON sosand.[NoSanding] = sandd.NoSanding " // INNER JOIN dengan Sanding_d
                                + "WHERE sosand.[NoSO] = ? "
                                + "AND sosand.[NoSanding] NOT IN ( "
                                + "    SELECT [NoSanding] FROM [dbo].[StockOpname_Hasil_d_Sanding] WHERE [NoSO] = ? "
                                + ") "
                                + "AND (sh.[DateUsage] > ? OR sh.[DateUsage] IS NULL)");
                        break;
                    case "BJ":
                        query.append("SELECT sobj.[NoBJ] AS NoLabel, sobj.IdLokasi "
                                + "FROM [dbo].[StockOpnameBJ] sobj "
                                + "INNER JOIN [dbo].[BarangJadi_h] bjh ON sobj.[NoBJ] = bjh.[NoBJ] "
                                + "INNER JOIN [dbo].[BarangJadi_d] bjd ON sobj.[NoBJ] = bjd.NoBJ " // INNER JOIN dengan BarangJadi_d
                                + "WHERE sobj.[NoSO] = ? "
                                + "AND sobj.[NoBJ] NOT IN ( "
                                + "    SELECT [NoBJ] FROM [dbo].[StockOpname_Hasil_d_BJ] WHERE [NoSO] = ? "
                                + ") "
                                + "AND (bjh.[DateUsage] > ? OR bjh.[DateUsage] IS NULL)");
                        break;
                }
                noSOParamCount += 3; // Setiap subquery membutuhkan 3 parameter: NoSO, NoSO, dan tglSO
                isFirstQuery = false;
            }
        }

        query.append(") AS c LEFT JOIN [dbo].[MstLokasi] AS m ON c.IdLokasi = m.IdLokasi WHERE 1=1 "); // Gabungkan dengan MstLokasi

        // Tambahkan filter untuk Blok jika tidak "Semua"
        if (selectedBlok != null && !selectedBlok.equals("Semua")) {
            query.append("AND m.Blok = ? ");
        }

        // Tambahkan filter untuk IdLokasi jika tidak "Semua"
        if (!selectedLokasi.equals("Semua")) {
            query.append("AND c.IdLokasi = ? ");
        }

        query.append("ORDER BY c.NoLabel OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        Log.d("SQL Query", query.toString()); // Log query untuk debugging

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query.toString())) {

            int currentParamIndex = 1;

            // Set parameter NoSO dan tglSO untuk setiap subquery
            for (int i = 0; i < noSOParamCount / 3; i++) {
                stmt.setString(currentParamIndex++, noSO);
                stmt.setString(currentParamIndex++, noSO);
                stmt.setString(currentParamIndex++, tglSO);
            }

            // Set parameter Blok jika ada
            if (selectedBlok != null && !selectedBlok.equals("Semua")) {
                stmt.setString(currentParamIndex++, selectedBlok);
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
                    stockOpnameDataByNoSOList.add(new StockOpnameDataByNoSO(noLabel, idLokasi));
                }
            }
        } catch (SQLException e) {
            Log.e("Database Fetch Error", "Error fetching data: " + e.getMessage());
        }

        return stockOpnameDataByNoSOList;
    }

    public static int getTotalStockOpnameDataCount(
            String noSO, String tglSO, String selectedBlok, String selectedLokasi,
            Set<String> selectedLabels) {

        int totalCount = 0;

        // Membangun query secara dinamis untuk menghitung jumlah data tanpa LIMIT dan OFFSET
        StringBuilder query = new StringBuilder("SELECT COUNT(*) AS totalCount FROM ( ");
        int noSOParamCount = 0;
        boolean isFirstQuery = true;

        // Tambahkan subquery berdasarkan selectedLabels
        for (String label : selectedLabels) {
            if (!label.equals("all")) { // Skip jika label adalah "all"
                if (!isFirstQuery) query.append("UNION ALL ");
                switch (label) {
                    case "ST":
                        query.append("SELECT sost.[NoST] AS NoLabel, sost.IdLokasi "
                                + "FROM [dbo].[StockOpnameST] sost "
                                + "INNER JOIN [dbo].[ST_h] sth ON sost.[NoST] = sth.[NoST] "
                                + "WHERE sost.[NoSO] = ? "
                                + "AND sost.[NoST] NOT IN ( "
                                + "    SELECT [NoST] FROM [dbo].[StockOpname_Hasil_d_ST] WHERE [NoSO] = ? "
                                + ") "
                                + "AND (sth.[DateUsage] > ? OR sth.[DateUsage] IS NULL)");
                        break;
                    case "S4S":
                        query.append("SELECT sos4s.[NoS4S] AS NoLabel, sos4s.IdLokasi "
                                + "FROM [dbo].[StockOpnameS4S] sos4s "
                                + "INNER JOIN [dbo].[S4S_h] s4h ON sos4s.[NoS4S] = s4h.[NoS4S] "
                                + "INNER JOIN [dbo].[S4S_d] s4d ON sos4s.[NoS4S] = s4d.NoS4S " // INNER JOIN dengan S4S_d
                                + "WHERE sos4s.[NoSO] = ? "
                                + "AND sos4s.[NoS4S] NOT IN ( "
                                + "    SELECT [NoS4S] FROM [dbo].[StockOpname_Hasil_d_S4S] WHERE [NoSO] = ? "
                                + ") "
                                + "AND (s4h.[DateUsage] > ? OR s4h.[DateUsage] IS NULL)");
                        break;
                    case "FJ":
                        query.append( "SELECT sofj.[NoFJ] AS NoLabel, sofj.IdLokasi "
                                + "FROM [dbo].[StockOpnameFJ] sofj "
                                + "INNER JOIN [dbo].[FJ_h] fjh ON sofj.[NoFJ] = fjh.[NoFJ] "
                                + "INNER JOIN [dbo].[FJ_d] fjd ON sofj.[NoFJ] = fjd.NoFJ " // INNER JOIN dengan FJ_d
                                + "WHERE sofj.[NoSO] = ? "
                                + "AND sofj.[NoFJ] NOT IN ( "
                                + "    SELECT [NoFJ] FROM [dbo].[StockOpname_Hasil_d_FJ] WHERE [NoSO] = ? "
                                + ") "
                                + "AND (fjh.[DateUsage] > ? OR fjh.[DateUsage] IS NULL)");
                        break;
                    case "MLD":
                        query.append( "SELECT som.[NoMoulding] AS NoLabel, som.IdLokasi "
                                + "FROM [dbo].[StockOpnameMoulding] som "
                                + "INNER JOIN [dbo].[Moulding_h] mh ON som.[NoMoulding] = mh.[NoMoulding] "
                                + "INNER JOIN [dbo].[Moulding_d] mould ON som.[NoMoulding] = mould.nomoulding "
                                + "WHERE som.[NoSO] = ? "
                                + "AND som.[NoMoulding] NOT IN ( "
                                + "    SELECT [NoMoulding] FROM [dbo].[StockOpname_Hasil_d_Moulding] WHERE [NoSO] = ? "
                                + ") "
                                + "AND (mh.[DateUsage] > ? OR mh.[DateUsage] IS NULL)");
                        break;
                    case "LMT":
                        query.append( "SELECT sol.[NoLaminating] AS NoLabel, sol.IdLokasi "
                                + "FROM [dbo].[StockOpnameLaminating] sol "
                                + "INNER JOIN [dbo].[Laminating_h] lh ON sol.[NoLaminating] = lh.[NoLaminating] "
                                + "INNER JOIN [dbo].[Laminating_d] ld ON sol.[NoLaminating] = ld.NoLaminating " // INNER JOIN dengan Laminating_d
                                + "WHERE sol.[NoSO] = ? "
                                + "AND sol.[NoLaminating] NOT IN ( "
                                + "    SELECT [NoLaminating] FROM [dbo].[StockOpname_Hasil_d_Laminating] WHERE [NoSO] = ? "
                                + ") "
                                + "AND (lh.[DateUsage] > ? OR lh.[DateUsage] IS NULL)");
                        break;
                    case "CC":
                        query.append("SELECT soc.[NoCCAkhir] AS NoLabel, soc.IdLokasi "
                                + "FROM [dbo].[StockOpnameCCAkhir] soc "
                                + "INNER JOIN [dbo].[CCAkhir_h] ccah ON soc.[NoCCAkhir] = ccah.[NoCCAkhir] "
                                + "INNER JOIN [dbo].[CCAkhir_d] ccad ON soc.[NoCCAkhir] = ccad.NoCCAkhir " // INNER JOIN dengan CCAkhir_d
                                + "WHERE soc.[NoSO] = ? "
                                + "AND soc.[NoCCAkhir] NOT IN ( "
                                + "    SELECT [NoCCAkhir] FROM [dbo].[StockOpname_Hasil_d_CCAkhir] WHERE [NoSO] = ? "
                                + ") "
                                + "AND (ccah.[DateUsage] > ? OR ccah.[DateUsage] IS NULL)");
                        break;
                    case "SND":
                        query.append("SELECT sosand.[NoSanding] AS NoLabel, sosand.IdLokasi "
                                + "FROM [dbo].[StockOpnameSanding] sosand "
                                + "INNER JOIN [dbo].[Sanding_h] sh ON sosand.[NoSanding] = sh.[NoSanding] "
                                + "INNER JOIN [dbo].[Sanding_d] sandd ON sosand.[NoSanding] = sandd.NoSanding " // INNER JOIN dengan Sanding_d
                                + "WHERE sosand.[NoSO] = ? "
                                + "AND sosand.[NoSanding] NOT IN ( "
                                + "    SELECT [NoSanding] FROM [dbo].[StockOpname_Hasil_d_Sanding] WHERE [NoSO] = ? "
                                + ") "
                                + "AND (sh.[DateUsage] > ? OR sh.[DateUsage] IS NULL)");
                        break;
                    case "BJ":
                        query.append("SELECT sobj.[NoBJ] AS NoLabel, sobj.IdLokasi "
                                + "FROM [dbo].[StockOpnameBJ] sobj "
                                + "INNER JOIN [dbo].[BarangJadi_h] bjh ON sobj.[NoBJ] = bjh.[NoBJ] "
                                + "INNER JOIN [dbo].[BarangJadi_d] bjd ON sobj.[NoBJ] = bjd.NoBJ " // INNER JOIN dengan BarangJadi_d
                                + "WHERE sobj.[NoSO] = ? "
                                + "AND sobj.[NoBJ] NOT IN ( "
                                + "    SELECT [NoBJ] FROM [dbo].[StockOpname_Hasil_d_BJ] WHERE [NoSO] = ? "
                                + ") "
                                + "AND (bjh.[DateUsage] > ? OR bjh.[DateUsage] IS NULL)");
                        break;
                }
                noSOParamCount += 3; // Setiap subquery membutuhkan 3 parameter: NoSO, NoSO, dan tglSO
                isFirstQuery = false;
            }
        }

        query.append(") AS c LEFT JOIN [dbo].[MstLokasi] AS m ON c.IdLokasi = m.IdLokasi WHERE 1=1 "); // Gabungkan dengan MstLokasi

        // Tambahkan filter untuk Blok jika tidak "Semua"
        if (selectedBlok != null && !selectedBlok.equals("Semua")) {
            query.append("AND m.Blok = ? ");
        }

        // Tambahkan filter untuk IdLokasi jika tidak "Semua"
        if (!selectedLokasi.equals("Semua")) {
            query.append("AND c.IdLokasi = ? ");
        }

        // Tidak ada OFFSET dan LIMIT karena hanya mengambil total count
        Log.d("SQL Query", query.toString()); // Log query untuk debugging

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query.toString())) {

            int currentParamIndex = 1;

            // Set parameter NoSO dan tglSO untuk setiap subquery
            for (int i = 0; i < noSOParamCount / 3; i++) {
                stmt.setString(currentParamIndex++, noSO);
                stmt.setString(currentParamIndex++, noSO);
                stmt.setString(currentParamIndex++, tglSO);
            }

            // Set parameter Blok jika ada
            if (selectedBlok != null && !selectedBlok.equals("Semua")) {
                stmt.setString(currentParamIndex++, selectedBlok);
            }

            // Set parameter IdLokasi jika ada
            if (!selectedLokasi.equals("Semua")) {
                stmt.setString(currentParamIndex++, selectedLokasi);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    totalCount = rs.getInt("totalCount"); // Ambil hasil COUNT dengan alias
                }
            }
        } catch (SQLException e) {
            Log.e("Database Count Error", "Error fetching count: " + e.getMessage());
        }

        return totalCount;
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

    public static List<StockOpnameDataByNoSO> searchStockOpnameDataByNoSO(
            String noSO, String tglSO, String searchTerm, String selectedBlok, String selectedIdLokasi,
            Set<String> selectedLabels, int offset, int limit) {

        List<StockOpnameDataByNoSO> stockOpnameDataByNoSOList = new ArrayList<>();
        Log.d("DataByNoso", "fetchDataBySearchTerm with searchTerm: " + searchTerm + ", offset: " + offset + " and limit: " + limit);

        // Start building the query dynamically
        StringBuilder queryBuilder = new StringBuilder("SELECT c.NoLabel, c.IdLokasi FROM ( ");
        boolean isFirstQuery = true;

        // Loop through selectedLabels to build the subqueries dynamically
        for (String label : selectedLabels) {
            if (!label.equals("all")) { // Skip if the label is "all"
                if (!isFirstQuery) {
                    queryBuilder.append("UNION ALL ");
                }
                switch (label) {
                    case "ST":
                        queryBuilder.append("SELECT sost.[NoST] AS NoLabel, sost.IdLokasi " +
                                "FROM [dbo].[StockOpnameST] sost " +
                                "INNER JOIN [dbo].[ST_h] sth ON sost.[NoST] = sth.[NoST] " +
                                "WHERE sost.[NoSO] = ? " +
                                "AND sost.[NoST] NOT IN (SELECT [NoST] FROM [dbo].[StockOpname_Hasil_d_ST] WHERE [NoSO] = ?) " +
                                "AND (sth.[DateUsage] > ? OR sth.[DateUsage] IS NULL) ");
                        break;
                    case "S4S":
                        queryBuilder.append("SELECT sos4s.[NoS4S] AS NoLabel, sos4s.IdLokasi " +
                                "FROM [dbo].[StockOpnameS4S] sos4s " +
                                "INNER JOIN [dbo].[S4S_h] s4h ON sos4s.[NoS4S] = s4h.[NoS4S] " +
                                "WHERE sos4s.[NoSO] = ? " +
                                "AND sos4s.[NoS4S] NOT IN (SELECT [NoS4S] FROM [dbo].[StockOpname_Hasil_d_S4S] WHERE [NoSO] = ?) " +
                                "AND (s4h.[DateUsage] > ? OR s4h.[DateUsage] IS NULL) ");
                        break;
                    case "FJ":
                        queryBuilder.append("SELECT sofj.[NoFJ] AS NoLabel, sofj.IdLokasi " +
                                "FROM [dbo].[StockOpnameFJ] sofj " +
                                "INNER JOIN [dbo].[FJ_h] fjh ON sofj.[NoFJ] = fjh.[NoFJ] " +
                                "WHERE sofj.[NoSO] = ? " +
                                "AND sofj.[NoFJ] NOT IN (SELECT [NoFJ] FROM [dbo].[StockOpname_Hasil_d_FJ] WHERE [NoSO] = ?) " +
                                "AND (fjh.[DateUsage] > ? OR fjh.[DateUsage] IS NULL) ");
                        break;
                    case "MLD":
                        queryBuilder.append("SELECT som.[NoMoulding] AS NoLabel, som.IdLokasi " +
                                "FROM [dbo].[StockOpnameMoulding] som " +
                                "INNER JOIN [dbo].[Moulding_h] mh ON som.[NoMoulding] = mh.[NoMoulding] " +
                                "WHERE som.[NoSO] = ? " +
                                "AND som.[NoMoulding] NOT IN (SELECT [NoMoulding] FROM [dbo].[StockOpname_Hasil_d_Moulding] WHERE [NoSO] = ?) " +
                                "AND (mh.[DateUsage] > ? OR mh.[DateUsage] IS NULL) ");
                        break;
                    case "LMT":
                        queryBuilder.append("SELECT sol.[NoLaminating] AS NoLabel, sol.IdLokasi " +
                                "FROM [dbo].[StockOpnameLaminating] sol " +
                                "INNER JOIN [dbo].[Laminating_h] lh ON sol.[NoLaminating] = lh.[NoLaminating] " +
                                "WHERE sol.[NoSO] = ? " +
                                "AND sol.[NoLaminating] NOT IN (SELECT [NoLaminating] FROM [dbo].[StockOpname_Hasil_d_Laminating] WHERE [NoSO] = ?) " +
                                "AND (lh.[DateUsage] > ? OR lh.[DateUsage] IS NULL) ");
                        break;
                    case "CC":
                        queryBuilder.append("SELECT soc.[NoCCAkhir] AS NoLabel, soc.IdLokasi " +
                                "FROM [dbo].[StockOpnameCCAkhir] soc " +
                                "INNER JOIN [dbo].[CCAkhir_h] ccah ON soc.[NoCCAkhir] = ccah.[NoCCAkhir] " +
                                "WHERE soc.[NoSO] = ? " +
                                "AND soc.[NoCCAkhir] NOT IN (SELECT [NoCCAkhir] FROM [dbo].[StockOpname_Hasil_d_CCAkhir] WHERE [NoSO] = ?) " +
                                "AND (ccah.[DateUsage] > ? OR ccah.[DateUsage] IS NULL) ");
                        break;
                    case "SND":
                        queryBuilder.append("SELECT sosand.[NoSanding] AS NoLabel, sosand.IdLokasi " +
                                "FROM [dbo].[StockOpnameSanding] sosand " +
                                "INNER JOIN [dbo].[Sanding_h] sh ON sosand.[NoSanding] = sh.[NoSanding] " +
                                "WHERE sosand.[NoSO] = ? " +
                                "AND sosand.[NoSanding] NOT IN (SELECT [NoSanding] FROM [dbo].[StockOpname_Hasil_d_Sanding] WHERE [NoSO] = ?) " +
                                "AND (sh.[DateUsage] > ? OR sh.[DateUsage] IS NULL) ");
                        break;
                    case "BJ":
                        queryBuilder.append("SELECT sobj.[NoBJ] AS NoLabel, sobj.IdLokasi " +
                                "FROM [dbo].[StockOpnameBJ] sobj " +
                                "INNER JOIN [dbo].[BarangJadi_h] bjh ON sobj.[NoBJ] = bjh.[NoBJ] " +
                                "WHERE sobj.[NoSO] = ? " +
                                "AND sobj.[NoBJ] NOT IN (SELECT [NoBJ] FROM [dbo].[StockOpname_Hasil_d_BJ] WHERE [NoSO] = ?) " +
                                "AND (bjh.[DateUsage] > ? OR bjh.[DateUsage] IS NULL) ");
                        break;
                }
                isFirstQuery = false;
            }
        }

        // Add WHERE clause after the UNION ALL
        queryBuilder.append(") AS c LEFT JOIN [dbo].[MstLokasi] AS m ON c.IdLokasi = m.IdLokasi WHERE 1=1 ");

        // Add filter for Blok if not "Semua"
        if (!selectedBlok.equals("Semua")) {
            queryBuilder.append("AND m.Blok = ? ");
        }

        // Add filter for IdLokasi if not "Semua"
        if (!selectedIdLokasi.equals("Semua")) {
            queryBuilder.append("AND c.IdLokasi = ? ");
        }

        // Add the condition to search by searchTerm (NoLabel or IdLokasi)
        queryBuilder.append("AND (c.NoLabel LIKE ? OR c.IdLokasi LIKE ?) ");

        // Add pagination (OFFSET and LIMIT)
        queryBuilder.append("ORDER BY c.NoLabel OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        Log.d("SQL Query", queryBuilder.toString()); // Log query for debugging

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(queryBuilder.toString())) {

            int parameterIndex = 1;

            // Set parameter NoSO and tglSO for each subquery
            for (int i = 0; i < selectedLabels.size(); i++) {
                stmt.setString(parameterIndex++, noSO);
                stmt.setString(parameterIndex++, noSO);
                stmt.setString(parameterIndex++, tglSO);
            }

            // Set parameter Blok if not "Semua"
            if (!selectedBlok.equals("Semua")) {
                stmt.setString(parameterIndex++, selectedBlok);
            }

            // Set parameter IdLokasi if not "Semua"
            if (!selectedIdLokasi.equals("Semua")) {
                stmt.setString(parameterIndex++, selectedIdLokasi);
            }

            // Set search term for NoLabel and IdLokasi
            stmt.setString(parameterIndex++, "%" + searchTerm + "%"); // Search for NoLabel
            stmt.setString(parameterIndex++, "%" + searchTerm + "%"); // Search for IdLokasi

            // Set OFFSET and LIMIT
            stmt.setInt(parameterIndex++, offset); // OFFSET
            stmt.setInt(parameterIndex++, limit); // LIMIT

            // Execute query and process results
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

    public static List<StockOpnameDataInputByNoSO> getStockOpnameDataInputBySearch(
            String noSO, String searchTerm, String selectedBlok, String selectedIdLokasi,
            Set<String> selectedLabels, int offset, int limit) {

        List<StockOpnameDataInputByNoSO> stockOpnameDataInputByNoSOList = new ArrayList<>();
        Log.d("Paging", "fetchDataInputBySearch with searchTerm: " + searchTerm + ", offset: " + offset + " and limit: " + limit);

        // Build query dynamically based on selectedLabels
        StringBuilder queryBuilder = new StringBuilder("SELECT c.NoLabel, c.IdLokasi, c.UserID FROM ( ");
        boolean isFirstQuery = true;

        // Loop through selectedLabels to dynamically build subqueries
        for (String label : selectedLabels) {
            if (!label.equals("all")) { // Skip if the label is "all"
                if (!isFirstQuery) {
                    queryBuilder.append("UNION ALL ");
                }
                switch (label) {
                    case "ST":
                        queryBuilder.append("SELECT [NoST] AS NoLabel, IdLokasi, UserID FROM [dbo].[StockOpname_Hasil_d_ST] WHERE [NoSO] = ? ");
                        break;
                    case "S4S":
                        queryBuilder.append("SELECT [NoS4S] AS NoLabel, IdLokasi, UserID FROM [dbo].[StockOpname_Hasil_d_S4S] WHERE [NoSO] = ? ");
                        break;
                    case "FJ":
                        queryBuilder.append("SELECT [NoFJ] AS NoLabel, IdLokasi, UserID FROM [dbo].[StockOpname_Hasil_d_FJ] WHERE [NoSO] = ? ");
                        break;
                    case "MLD":
                        queryBuilder.append("SELECT [NoMoulding] AS NoLabel, IdLokasi, UserID FROM [dbo].[StockOpname_Hasil_d_Moulding] WHERE [NoSO] = ? ");
                        break;
                    case "LMT":
                        queryBuilder.append("SELECT [NoLaminating] AS NoLabel, IdLokasi, UserID FROM [dbo].[StockOpname_Hasil_d_Laminating] WHERE [NoSO] = ? ");
                        break;
                    case "CC":
                        queryBuilder.append("SELECT [NoCCAkhir] AS NoLabel, IdLokasi, UserID FROM [dbo].[StockOpname_Hasil_d_CCAkhir] WHERE [NoSO] = ? ");
                        break;
                    case "SND":
                        queryBuilder.append("SELECT [NoSanding] AS NoLabel, IdLokasi, UserID FROM [dbo].[StockOpname_Hasil_d_Sanding] WHERE [NoSO] = ? ");
                        break;
                    case "BJ":
                        queryBuilder.append("SELECT [NoBJ] AS NoLabel, IdLokasi, UserID FROM [dbo].[StockOpname_Hasil_d_BJ] WHERE [NoSO] = ? ");
                        break;
                }
                isFirstQuery = false;
            }
        }

        // Complete the query by adding the WHERE clause
        queryBuilder.append(") AS c LEFT JOIN [dbo].[MstLokasi] AS m ON c.IdLokasi = m.IdLokasi WHERE 1=1 ");

        // Add filter for Blok if not "Semua"
        if (!selectedBlok.equals("Semua")) {
            queryBuilder.append("AND m.Blok = ? ");
        }

        // Add filter for IdLokasi if not "Semua"
        if (!selectedIdLokasi.equals("Semua")) {
            queryBuilder.append("AND c.IdLokasi = ? ");
        }

        // If searchTerm is provided, add it to the WHERE condition for NoLabel and IdLokasi
        if (searchTerm != null && !searchTerm.isEmpty()) {
            queryBuilder.append("AND (c.NoLabel LIKE ? OR c.IdLokasi LIKE ?) ");
        }

        // Add pagination (OFFSET and LIMIT)
        queryBuilder.append("ORDER BY c.NoLabel OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        Log.d("SQL Query", queryBuilder.toString()); // Log query for debugging

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(queryBuilder.toString())) {

            int parameterIndex = 1;

            // Set parameter NoSO for each query in the subqueries
            for (String label : selectedLabels) {
                if (!label.equals("all")) {
                    stmt.setString(parameterIndex++, noSO); // Set NoSO for each subquery
                }
            }

            // Set parameter Blok if not "Semua"
            if (!selectedBlok.equals("Semua")) {
                stmt.setString(parameterIndex++, selectedBlok);
            }

            // Set parameter IdLokasi if not "Semua"
            if (!selectedIdLokasi.equals("Semua")) {
                stmt.setString(parameterIndex++, selectedIdLokasi);
            }

            // If a search term is provided, set it for NoLabel and IdLokasi filters
            if (searchTerm != null && !searchTerm.isEmpty()) {
                stmt.setString(parameterIndex++, "%" + searchTerm + "%");  // Set search term for NoLabel
                stmt.setString(parameterIndex++, "%" + searchTerm + "%"); // Set search term for IdLokasi
            }

            // Set OFFSET and LIMIT for pagination
            stmt.setInt(parameterIndex++, offset); // OFFSET
            stmt.setInt(parameterIndex++, limit); // LIMIT

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

    public static List<UserIDSO> getUserIdsForNoSO(String noSO) {
        List<UserIDSO> userList = new ArrayList<>();

        // Query untuk mengambil data UserID dengan UNION
        String query = "SELECT sh.UserID " +
                "FROM [dbo].[StockOpname_Hasil_d_ST] sh " +
                "WHERE sh.NoSO = ? " +
                "UNION " +
                "SELECT s4s.UserID " +
                "FROM [dbo].[StockOpname_Hasil_d_S4S] s4s " +
                "WHERE s4s.NoSO = ? " +
                "UNION " +
                "SELECT fj.UserID " +
                "FROM [dbo].[StockOpname_Hasil_d_FJ] fj " +
                "WHERE fj.NoSO = ? " +
                "UNION " +
                "SELECT moulding.UserID " +
                "FROM [dbo].[StockOpname_Hasil_d_Moulding] moulding " +
                "WHERE moulding.NoSO = ? " +
                "UNION " +
                "SELECT laminating.UserID " +
                "FROM [dbo].[StockOpname_Hasil_d_Laminating] laminating " +
                "WHERE laminating.NoSO = ? " +
                "UNION " +
                "SELECT cca.UserID " +
                "FROM [dbo].[StockOpname_Hasil_d_CCAkhir] cca " +
                "WHERE cca.NoSO = ? " +
                "UNION " +
                "SELECT sanding.UserID " +
                "FROM [dbo].[StockOpname_Hasil_d_Sanding] sanding " +
                "WHERE sanding.NoSO = ? " +
                "UNION " +
                "SELECT bj.UserID " +
                "FROM [dbo].[StockOpname_Hasil_d_BJ] bj " +
                "WHERE bj.NoSO = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            // Set parameter NoSO untuk setiap query bagian UNION
            for (int i = 1; i <= 8; i++) {
                stmt.setString(i, noSO);  // Set parameter NoSO
            }
            Log.d("Database Query", "Executing query with NoSO: " + noSO);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String userIDSO = rs.getString("UserID");
                    if (userIDSO == null) {
                        userIDSO = "-";  // Jika UserID null, ganti dengan "-"
                    }

                    // Menambahkan UserIDSO ke list
                    userList.add(new UserIDSO(userIDSO));
                    Log.d("Database Result", "Fetched UserID: " + userIDSO);

                }
            }
        } catch (SQLException e) {
            Log.e("Database Fetch Error", "Error fetching UserID: " + e.getMessage());
        }

        return userList;
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


    public static List<StockOpnameDataInputByNoSO> getStockOpnameDataInputByFilter(
            String noSO, String selectedBlok, String selectedIdLokasi, Set<String> selectedLabels,
            String selectedUserID, int offset, int limit) {

        List<StockOpnameDataInputByNoSO> stockOpnameDataInputByNoSOList = new ArrayList<>();
        Log.d("valuefilter", "fetchDataInputByNoSO with offset: " + offset + " and limit: " + limit);

        // Menentukan query dasar
        StringBuilder query = new StringBuilder("SELECT s.NoLabel, s.IdLokasi, s.UserID, s.DateTimeScan FROM ( ");
        int noSOParamCount = 0; // Menghitung jumlah parameter NoSO
        boolean isFirstQuery = true;  // Flag untuk memastikan UNION ALL tidak ditambahkan di awal

        // Tambahkan kondisi berdasarkan selectedLabels (filter CheckBox)
        for (String label : selectedLabels) {
            if (!label.equals("all")) {  // Jangan menambahkan "all", karena akan mencakup semua
                if (!isFirstQuery) query.append("UNION ALL ");
                switch (label) {
                    case "ST":
                        query.append("SELECT [NoST] AS NoLabel, [IdLokasi], [UserID], [DateTimeScan] FROM [dbo].[StockOpname_Hasil_d_ST] WHERE [NoSO] = ? ");
                        break;
                    case "S4S":
                        query.append("SELECT [NoS4S] AS NoLabel, [IdLokasi], [UserID], [DateTimeScan] FROM [dbo].[StockOpname_Hasil_d_S4S] WHERE [NoSO] = ? ");
                        break;
                    case "FJ":
                        query.append("SELECT [NoFJ] AS NoLabel, [IdLokasi], [UserID], [DateTimeScan] FROM [dbo].[StockOpname_Hasil_d_FJ] WHERE [NoSO] = ? ");
                        break;
                    case "MLD":
                        query.append("SELECT [NoMoulding] AS NoLabel, [IdLokasi], [UserID], [DateTimeScan] FROM [dbo].[StockOpname_Hasil_d_Moulding] WHERE [NoSO] = ? ");
                        break;
                    case "LMT":
                        query.append("SELECT [NoLaminating] AS NoLabel, [IdLokasi], [UserID], [DateTimeScan] FROM [dbo].[StockOpname_Hasil_d_Laminating] WHERE [NoSO] = ? ");
                        break;
                    case "CC":
                        query.append("SELECT [NoCCAkhir] AS NoLabel, [IdLokasi], [UserID], [DateTimeScan] FROM [dbo].[StockOpname_Hasil_d_CCAkhir] WHERE [NoSO] = ? ");
                        break;
                    case "SND":
                        query.append("SELECT [NoSanding] AS NoLabel, [IdLokasi], [UserID], [DateTimeScan] FROM [dbo].[StockOpname_Hasil_d_Sanding] WHERE [NoSO] = ? ");
                        break;
                    case "BJ":
                        query.append("SELECT [NoBJ] AS NoLabel, [IdLokasi], [UserID], [DateTimeScan] FROM [dbo].[StockOpname_Hasil_d_BJ] WHERE [NoSO] = ? ");
                        break;
                }
                noSOParamCount++;
                isFirstQuery = false; // Set isFirstQuery to false after the first query
            }
        }

        query.append(") AS s LEFT JOIN [dbo].[MstLokasi] AS m ON s.IdLokasi = m.IdLokasi WHERE 1=1 "); // Gunakan LEFT JOIN

        // Filter berdasarkan Blok dan IdLokasi
        if (!selectedBlok.equals("Semua")) {
            if (selectedIdLokasi.equals("Semua")) {
                // Jika Blok = "A" dan IdLokasi = "Semua", filter berdasarkan Blok saja
                query.append("AND m.Blok = ? ");
            } else {
                // Jika Blok = "A" dan IdLokasi = "A1", filter berdasarkan Blok dan IdLokasi
                query.append("AND m.Blok = ? AND s.IdLokasi = ? ");
            }
        }

        // Tambahkan filter untuk UserID jika tidak "Semua"
        if (selectedUserID != null && !selectedUserID.equals("Semua")) {
            if (selectedUserID.equals("-")) {
                Log.d("UserID", selectedUserID); // Log the query for debugging purposes
                query.append("AND s.UserID IS NULL ");
            } else {
                query.append("AND s.UserID = ? ");
            }
        }

        query.append("ORDER BY s.DateTimeScan DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        Log.d("SQL Query", query.toString()); // Log the query for debugging purposes
        Log.d("Number of Parameters", String.valueOf(noSOParamCount)); // Log number of parameters

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query.toString())) {

            int currentParamIndex = 1;

            // Set parameter NoSO untuk setiap bagian query
            for (int i = 0; i < noSOParamCount; i++) {
                stmt.setString(currentParamIndex++, noSO);
            }

            // Set parameter Blok dan IdLokasi jika ada
            if (!selectedBlok.equals("Semua")) {
                stmt.setString(currentParamIndex++, selectedBlok); // Set Blok
                if (!selectedIdLokasi.equals("Semua")) {
                    stmt.setString(currentParamIndex++, selectedIdLokasi); // Set IdLokasi
                }
            }

            // Set parameter UserID jika ada
            if (selectedUserID != null && !selectedUserID.equals("Semua") && !selectedUserID.equals("-")) {
                stmt.setString(currentParamIndex++, selectedUserID);
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

    public static int getStockOpnameDataInputCountByFilter(String noSO, String selectedBlok, String selectedIdLokasi, Set<String> selectedLabels, String selectedUserID) {

        int count = 0;
        Log.d("Count", "Counting data with filter noSO: " + noSO + ", selectedBlok: " + selectedBlok +
                ", selectedIdLokasi: " + selectedIdLokasi + ", selectedUserID: " + selectedUserID + "selectedLabels: " + selectedLabels);

        // Menentukan query dasar
        StringBuilder query = new StringBuilder("SELECT COUNT(*) FROM ( ");
        int noSOParamCount = 0; // Menghitung jumlah parameter NoSO
        boolean isFirstQuery = true;  // Flag untuk memastikan UNION ALL tidak ditambahkan di awal

        // Tambahkan kondisi berdasarkan selectedLabels (filter CheckBox)
        for (String label : selectedLabels) {
            if (!label.equals("all")) {  // Jangan menambahkan "all", karena akan mencakup semua
                if (!isFirstQuery) query.append("UNION ALL ");
                switch (label) {
                    case "ST":
                        query.append("SELECT [NoST], [IdLokasi], [UserID] FROM [dbo].[StockOpname_Hasil_d_ST] WHERE [NoSO] = ? ");
                        break;
                    case "S4S":
                        query.append("SELECT [NoS4S], [IdLokasi], [UserID] FROM [dbo].[StockOpname_Hasil_d_S4S] WHERE [NoSO] = ? ");
                        break;
                    case "FJ":
                        query.append("SELECT [NoFJ], [IdLokasi], [UserID] FROM [dbo].[StockOpname_Hasil_d_FJ] WHERE [NoSO] = ? ");
                        break;
                    case "MLD":
                        query.append("SELECT [NoMoulding], [IdLokasi], [UserID] FROM [dbo].[StockOpname_Hasil_d_Moulding] WHERE [NoSO] = ? ");
                        break;
                    case "LMT":
                        query.append("SELECT [NoLaminating], [IdLokasi], [UserID] FROM [dbo].[StockOpname_Hasil_d_Laminating] WHERE [NoSO] = ? ");
                        break;
                    case "CC":
                        query.append("SELECT [NoCCAkhir], [IdLokasi], [UserID] FROM [dbo].[StockOpname_Hasil_d_CCAkhir] WHERE [NoSO] = ? ");
                        break;
                    case "SND":
                        query.append("SELECT [NoSanding], [IdLokasi], [UserID] FROM [dbo].[StockOpname_Hasil_d_Sanding] WHERE [NoSO] = ? ");
                        break;
                    case "BJ":
                        query.append("SELECT [NoBJ], [IdLokasi], [UserID] FROM [dbo].[StockOpname_Hasil_d_BJ] WHERE [NoSO] = ? ");
                        break;
                }
                noSOParamCount++;
                isFirstQuery = false; // Set isFirstQuery to false after the first query
            }
        }

        query.append(") AS s LEFT JOIN [dbo].[MstLokasi] AS m ON s.IdLokasi = m.IdLokasi WHERE 1=1 "); // Gunakan LEFT JOIN

        // Filter berdasarkan Blok dan IdLokasi
        if (selectedBlok != null && !selectedBlok.equals("Semua")) {
            if (selectedIdLokasi.equals("Semua")) {
                // Jika Blok = "A" dan IdLokasi = "Semua", filter berdasarkan Blok saja
                query.append("AND m.Blok = ? ");
            } else {
                // Jika Blok = "A" dan IdLokasi = "A1", filter berdasarkan Blok dan IdLokasi
                query.append("AND m.Blok = ? AND s.IdLokasi = ? ");
            }
        }

        // Tambahkan filter untuk UserID jika tidak "Semua"
        if (selectedUserID != null && !selectedUserID.equals("Semua")) {
            if (selectedUserID.equals("-")) {
                query.append("AND s.UserID IS NULL ");
            } else {
                query.append("AND s.UserID = ? ");
            }
        }

        // Menjalankan query COUNT
        Log.d("SQL Count Query", query.toString()); // Log the query for debugging purposes

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query.toString())) {

            int currentParamIndex = 1;

            // Set parameter NoSO untuk setiap bagian query
            for (int i = 0; i < noSOParamCount; i++) {
                stmt.setString(currentParamIndex++, noSO);
            }

            // Set parameter Blok dan IdLokasi jika ada
            if (selectedBlok != null && !selectedBlok.equals("Semua")) {
                stmt.setString(currentParamIndex++, selectedBlok); // Set Blok
                if (!selectedIdLokasi.equals("Semua")) {
                    stmt.setString(currentParamIndex++, selectedIdLokasi); // Set IdLokasi
                }
            }

            // Set parameter UserID jika ada
            if (selectedUserID != null && !selectedUserID.equals("Semua") && !selectedUserID.equals("-")) {
                stmt.setString(currentParamIndex++, selectedUserID);
            }

            // Menjalankan query dan mendapatkan hasil count
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);  // Mengambil nilai COUNT dari hasil query
                }
            }
        } catch (SQLException e) {
            Log.e("Database Count Error", "Error fetching count: " + e.getMessage());
        }

        return count;
    }






}
