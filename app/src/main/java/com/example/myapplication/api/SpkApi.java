package com.example.myapplication.api;

import android.util.Log;

import com.example.myapplication.config.DatabaseConfig;
import com.example.myapplication.model.MstSPKData;
import com.example.myapplication.model.PenerimaanSTSawmillData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SpkApi {

    public static List<MstSPKData> getMstSPKData(int page, int pageSize, String searchQuery) {
        List<MstSPKData> dataList = new ArrayList<>();

        int offset = (page - 1) * pageSize;

        // Base query
        String query =
                "SELECT * FROM (" +
                        " SELECT ROW_NUMBER() OVER (ORDER BY h.Tanggal DESC) AS RowNum, " +
                        " h.NoSPK, h.Tanggal, h.NoContract, h.IdBuyer, b.Buyer, h.Tujuan, h.Enable, " +
                        " h.LockDimensionS4S, h.LockDimensionFJ, h.LockDimensionMLD, h.LockDimensionLMT, " +
                        " h.LockDimensionCCA, h.LockDimensionSAND, h.LockDimensionBJ, h.LockDimensionST, " +
                        " h.UnlockGradeS4S, h.UnlockGradeFJ, h.UnlockGradeMLD, h.UnlockGradeLMT, " +
                        " h.UnlockGradeCCA, h.UnlockGradeSAND, h.UnlockGradeBJ, h.UnlockGradeST " +
                        " FROM dbo.MstSPK_h h " +
                        " LEFT JOIN dbo.MstBuyer b ON h.IdBuyer = b.IdBuyer " +
                        " WHERE 1=1 ";  // 👈 always true, so we can safely append optional filters

        // Add search filter
        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            query += " AND (h.NoSPK LIKE ? OR b.Buyer LIKE ? OR h.Tujuan LIKE ?) ";
        }

        // Pagination (ROW_NUMBER window)
        query += ") AS t WHERE RowNum BETWEEN ? AND ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            int paramIndex = 1;

            // Bind search params if provided
            if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                String likeValue = "%" + searchQuery.trim() + "%";
                stmt.setString(paramIndex++, likeValue);  // NoSPK
                stmt.setString(paramIndex++, likeValue);  // Buyer
                stmt.setString(paramIndex++, likeValue);  // Tujuan
            }

            // Bind paging params
            stmt.setInt(paramIndex++, offset + 1);
            stmt.setInt(paramIndex, offset + pageSize);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MstSPKData d = new MstSPKData(
                            rs.getString("NoSPK"),
                            rs.getString("Tanggal"),
                            rs.getString("NoContract"),
                            rs.getInt("IdBuyer"),
                            rs.getString("Buyer"),
                            rs.getString("Tujuan"),
                            rs.getInt("Enable"),
                            rs.getInt("LockDimensionS4S"),
                            rs.getInt("LockDimensionFJ"),
                            rs.getInt("LockDimensionMLD"),
                            rs.getInt("LockDimensionLMT"),
                            rs.getInt("LockDimensionCCA"),
                            rs.getInt("LockDimensionSAND"),
                            rs.getInt("LockDimensionBJ"),
                            rs.getInt("LockDimensionST"),
                            rs.getInt("UnlockGradeS4S"),
                            rs.getInt("UnlockGradeFJ"),
                            rs.getInt("UnlockGradeMLD"),
                            rs.getInt("UnlockGradeLMT"),
                            rs.getInt("UnlockGradeCCA"),
                            rs.getInt("UnlockGradeSAND"),
                            rs.getInt("UnlockGradeBJ"),
                            rs.getInt("UnlockGradeST")
                    );
                    dataList.add(d);
                }
            }

        } catch (SQLException e) {
            Log.e("Database Error", "Error fetching paged MstSPK data: " + e.getMessage());
        }

        return dataList;
    }



    // Set nilai Enable secara eksplisit (0/1)
    public static boolean setMstSPKEnable(String noSPK, int newEnable) {
        final String sql = "UPDATE dbo.MstSPK_h WITH (ROWLOCK) SET Enable = ? WHERE NoSPK = ?";
        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, newEnable);
            ps.setString(2, noSPK);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Log.e("DB", "setMstSPKEnable gagal: " + e.getMessage());
            return false;
        }
    }



}
