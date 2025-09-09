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

    public static List<MstSPKData> getMstSPKData() {
        List<MstSPKData> dataList = new ArrayList<>();

        String query = "SELECT TOP 50 h.NoSPK, h.Tanggal, h.NoContract, h.IdBuyer, b.Buyer, h.Tujuan, h.Enable, " +
                "h.LockDimensionS4S, h.LockDimensionFJ, h.LockDimensionMLD, h.LockDimensionLMT, " +
                "h.LockDimensionCCA, h.LockDimensionSAND, h.LockDimensionBJ, h.LockDimensionST, " +
                "h.UnlockGradeS4S, h.UnlockGradeFJ, h.UnlockGradeMLD, h.UnlockGradeLMT, h.UnlockGradeCCA, " +
                "h.UnlockGradeSAND, h.UnlockGradeBJ, h.UnlockGradeST " +
                "FROM dbo.MstSPK_h h " +
                "LEFT JOIN dbo.MstBuyer b ON h.IdBuyer = b.IdBuyer " +
                "ORDER BY h.NoSPK DESC";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String noSPK = rs.getString("NoSPK");
                String tanggal = rs.getString("Tanggal");
                String noContract = rs.getString("NoContract");
                int idBuyer = rs.getInt("IdBuyer");
                String buyerName = rs.getString("Buyer");
                String tujuan = rs.getString("Tujuan");
                int enable = rs.getInt("Enable");
                int lockDimensionS4S = rs.getInt("LockDimensionS4S");
                int lockDimensionFJ = rs.getInt("LockDimensionFJ");
                int lockDimensionMLD = rs.getInt("LockDimensionMLD");
                int lockDimensionLMT = rs.getInt("LockDimensionLMT");
                int lockDimensionCCA = rs.getInt("LockDimensionCCA");
                int lockDimensionSAND = rs.getInt("LockDimensionSAND");
                int lockDimensionBJ = rs.getInt("LockDimensionBJ");
                int lockDimensionST = rs.getInt("LockDimensionST");
                int unlockGradeS4S = rs.getInt("UnlockGradeS4S");
                int unlockGradeFJ = rs.getInt("UnlockGradeFJ");
                int unlockGradeMLD = rs.getInt("UnlockGradeMLD");
                int unlockGradeLMT = rs.getInt("UnlockGradeLMT");
                int unlockGradeCCA = rs.getInt("UnlockGradeCCA");
                int unlockGradeSAND = rs.getInt("UnlockGradeSAND");
                int unlockGradeBJ = rs.getInt("UnlockGradeBJ");
                int unlockGradeST = rs.getInt("UnlockGradeST");

                dataList.add(new MstSPKData(noSPK, tanggal, noContract, idBuyer, buyerName, tujuan,
                        enable, lockDimensionS4S, lockDimensionFJ, lockDimensionMLD,
                        lockDimensionLMT, lockDimensionCCA, lockDimensionSAND,
                        lockDimensionBJ, lockDimensionST, unlockGradeS4S, unlockGradeFJ,
                        unlockGradeMLD, unlockGradeLMT, unlockGradeCCA, unlockGradeSAND,
                        unlockGradeBJ, unlockGradeST));
            }

        } catch (SQLException e) {
            Log.e("Database Error", "Error fetching MstSPK data: " + e.getMessage());
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
