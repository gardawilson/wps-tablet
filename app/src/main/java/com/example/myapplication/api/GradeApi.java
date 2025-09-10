package com.example.myapplication.api;

import android.util.Log;

import com.example.myapplication.config.DatabaseConfig;
import com.example.myapplication.model.GradeABCData;
import com.example.myapplication.model.GradeABCDetailData;
import com.example.myapplication.utils.DateTimeUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GradeApi {

    public static List<GradeABCData> getGradeABCData() {
        List<GradeABCData> dataList = new ArrayList<>();

        String query = "SELECT TOP 500 NoGradeABC, Tanggal, Keterangan FROM GradeABC_h ORDER BY NoGradeABC DESC";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String noGradeABC = rs.getString("NoGradeABC");
                String tanggal = rs.getString("Tanggal");
                String keterangan = rs.getString("Keterangan");

                dataList.add(new GradeABCData(noGradeABC, tanggal, keterangan));
            }

        } catch (SQLException e) {
            Log.e("Database Error", "Error fetching GradeABC data: " + e.getMessage());
        }

        return dataList;
    }


    public static List<GradeABCDetailData> getGradeABCDetailData(String noGradeABC) {
        List<GradeABCDetailData> detailList = new ArrayList<>();

        String query =
                "SELECT d.NoGradeABC, d.IdGradeABC, ISNULL(m.NamaGrade,'') AS NamaGrade, d.JmlhBatang " +
                        "FROM GradeABC_d d " +
                        "LEFT JOIN MstGradeABC m ON d.IdGradeABC = m.IdGradeABC " +
                        "WHERE d.NoGradeABC = ? " +
                        "ORDER BY d.IdGradeABC ASC";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, noGradeABC);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String noGrade = rs.getString("NoGradeABC");
                    int idGrade = rs.getInt("IdGradeABC");
                    String namaGrade = rs.getString("NamaGrade");       // bisa kosong kalau tak ada di master
                    int jmlhBatang = rs.getInt("JmlhBatang");

                    detailList.add(new GradeABCDetailData(noGrade, idGrade, namaGrade, jmlhBatang));
                }
            }
        } catch (SQLException e) {
            Log.e("Database Error", "Error fetching GradeABC detail: " + e.getMessage());
        }

        return detailList;
    }


    // -- Helper: generate nomor GG.xxxxxx yang aman di dalam transaksi
    private static String generateNewNoGradeABC(Connection con) throws SQLException {
        final String sql =
                "SELECT ISNULL(MAX(TRY_CONVERT(INT, RIGHT(NoGradeABC, 6))), 0) AS MaxNum " +
                        "FROM dbo.GradeABC_h WITH (UPDLOCK, HOLDLOCK) " +
                        "WHERE NoGradeABC LIKE 'GG.%' AND LEN(NoGradeABC) = 9"; // "GG." + 6 digit

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            int last = 0;
            if (rs.next()) last = rs.getInt("MaxNum");

            if (last >= 999_999) {
                throw new SQLException("NoGradeABC overflow (>= GG.999999)");
            }
            return "GG." + String.format("%06d", last + 1);
        }
    }



    /**
     * Insert header GradeABC dengan auto-generate NoGradeABC.
     * @param tanggalISO  format "yyyy-MM-dd" dari UI (DateTimeUtils.getCurrentDate())
     * @param keterangan  optional; null/"" -> NULL
     * @return String NoGradeABC baru (mis. "GG.000123") jika sukses, atau null jika gagal
     */
    public static String insertGradeABCHeaderWithGeneratedNo(String tanggalISO,
                                                             String keterangan) {
        final String tableName = "dbo.GradeABC_h";
        final String insertSql =
                "INSERT INTO " + tableName + " (NoGradeABC, Tanggal, Keterangan) " +
                        "VALUES (?, ?, ?)";

        // (Opsional) batasi panjang teks jika ada limit skema
        if (keterangan != null && keterangan.length() > 255) {
            keterangan = keterangan.substring(0, 255);
        }

        int attempts = 0;
        while (attempts < 3) {
            attempts++;
            Connection con = null;
            try {
                con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
                con.setAutoCommit(false);
                con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

                // 1) Generate nomor di dalam transaksi (lock baris agregasi)
                String newNo = generateNewNoGradeABC(con);

                // 2) Insert header
                try (PreparedStatement ps = con.prepareStatement(insertSql)) {
                    ps.setString(1, newNo);
                    ps.setString(2, DateTimeUtils.formatToDatabaseDate(tanggalISO)); // pastikan return "yyyy-MM-dd"
                    if (keterangan == null || keterangan.trim().isEmpty()) {
                        ps.setNull(3, java.sql.Types.VARCHAR);
                    } else {
                        ps.setString(3, keterangan.trim());
                    }
                    ps.executeUpdate();
                }

                con.commit();
                return newNo;

            } catch (SQLException e) {
                System.err.println("[DB_ERROR] Insert GradeABC_h attempt " + attempts + " gagal: " +
                        e.getMessage() + " (SQLState=" + e.getSQLState() +
                        ", Code=" + e.getErrorCode() + ")");
                try { if (con != null) con.rollback(); } catch (SQLException ignore) {}

                // 23000 = integrity constraint violation (dupe key, dll) → retry
                if (!"23000".equals(e.getSQLState())) {
                    return null;
                }

            } catch (Exception e) {
                System.err.println("[APP_ERROR] Insert GradeABC_h fatal: " + e.getMessage());
                try { if (con != null) con.rollback(); } catch (SQLException ignore) {}
                return null;

            } finally {
                if (con != null) try { con.close(); } catch (SQLException ignore) {}
            }
        }

        System.err.println("[DB_ERROR] Gagal insert GradeABC_h setelah 3x retry");
        return null;
    }


    public static boolean updateGradeABCHeader(String noGradeABC,
                                               String tanggalISO,
                                               String keterangan) {
        final String tableName = "dbo.GradeABC_h";
        final String sql =
                "UPDATE " + tableName + " " +
                        "SET Tanggal = ?, Keterangan = ? " +
                        "WHERE NoGradeABC = ?";

        if (keterangan != null && keterangan.length() > 255) {
            keterangan = keterangan.substring(0, 255);
        }

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, DateTimeUtils.formatToDatabaseDate(tanggalISO)); // "yyyy-MM-dd"
            if (keterangan == null || keterangan.trim().isEmpty()) {
                ps.setNull(2, java.sql.Types.VARCHAR);
            } else {
                ps.setString(2, keterangan.trim());
            }
            ps.setString(3, noGradeABC);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("[DB_ERROR] Update GradeABC_h gagal: " + e.getMessage()
                    + " (SQLState=" + e.getSQLState() + ", Code=" + e.getErrorCode() + ")");
            return false;
        } catch (Exception e) {
            System.err.println("[APP_ERROR] Update GradeABC_h fatal: " + e.getMessage());
            return false;
        }
    }


    public static boolean deleteGradeABCHeaderCascade(String noGradeABC) {
        final String tableH = "dbo.GradeABC_h";
        final String tableD = "dbo.GradeABC_d";

        final String delD = "DELETE FROM " + tableD + " WHERE NoGradeABC = ?";
        final String delH = "DELETE FROM " + tableH + " WHERE NoGradeABC = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl())) {
            con.setAutoCommit(false);
            con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            try (PreparedStatement psD = con.prepareStatement(delD);
                 PreparedStatement psH = con.prepareStatement(delH)) {

                // 1) Hapus detail
                psD.setString(1, noGradeABC);
                psD.executeUpdate();

                // 2) Hapus header
                psH.setString(1, noGradeABC);
                int rows = psH.executeUpdate();

                con.commit();
                return rows > 0; // true kalau header-nya benar-benar terhapus
            } catch (SQLException e) {
                try { con.rollback(); } catch (SQLException ignore) {}
                System.err.println("[DB_ERROR] Delete cascade gagal: " + e.getMessage()
                        + " (SQLState=" + e.getSQLState() + ", Code=" + e.getErrorCode() + ")");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("[DB_ERROR] Koneksi delete cascade gagal: " + e.getMessage());
            return false;
        }
    }


    public static boolean existsGradeABCDetail(String noGradeABC, int idGradeABC) {
        final String sql = "SELECT COUNT(*) FROM dbo.GradeABC_d WHERE NoGradeABC = ? AND IdGradeABC = ?";
        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, noGradeABC);
            ps.setInt(2, idGradeABC);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("[DB_ERROR] existsGradeABCDetail: " + e.getMessage());
        }
        return false;
    }


    public static boolean insertGradeABCDetail(String noGradeABC, int idGradeABC, int pcs) {
        final String sql = "INSERT INTO dbo.GradeABC_d (NoGradeABC, IdGradeABC, JmlhBatang) VALUES (?, ?, ?)";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, noGradeABC);
            ps.setInt(2, idGradeABC);
            ps.setInt(3, pcs);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("[DB_ERROR] insertGradeABCDetail: " + e.getMessage()
                    + " (SQLState=" + e.getSQLState() + ", Code=" + e.getErrorCode() + ")");
            return false;
        }
    }


    public static boolean updateGradeABCDetail(String noGradeABC, int oldIdGradeABC, int newIdGradeABC, int pcs) {
        // jika grade tidak berubah: update jumlah
        if (oldIdGradeABC == newIdGradeABC) {
            final String sql = "UPDATE dbo.GradeABC_d " +
                    "SET JmlhBatang = ? " +
                    "WHERE NoGradeABC = ? AND IdGradeABC = ?";
            try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, pcs);
                ps.setString(2, noGradeABC);
                ps.setInt(3, oldIdGradeABC);
                int rows = ps.executeUpdate();
                return rows > 0;

            } catch (SQLException e) {
                System.err.println("[DB_ERROR] updateGradeABCDetail(keep-id): " + e.getMessage()
                        + " (SQLState=" + e.getSQLState() + ", Code=" + e.getErrorCode() + ")");
                return false;
            }
        }

        // jika grade berubah: update id + jumlah
        final String sql2 = "UPDATE dbo.GradeABC_d " +
                "SET IdGradeABC = ?, JmlhBatang = ? " +
                "WHERE NoGradeABC = ? AND IdGradeABC = ?";
        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement ps = con.prepareStatement(sql2)) {

            ps.setInt(1, newIdGradeABC);
            ps.setInt(2, pcs);
            ps.setString(3, noGradeABC);
            ps.setInt(4, oldIdGradeABC);
            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("[DB_ERROR] updateGradeABCDetail(change-id): " + e.getMessage()
                    + " (SQLState=" + e.getSQLState() + ", Code=" + e.getErrorCode() + ")");
            return false;
        }
    }


    public static boolean deleteGradeABCDetail(String noGradeABC, int idGradeABC) {
        final String sql = "DELETE FROM dbo.GradeABC_d WHERE NoGradeABC = ? AND IdGradeABC = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, noGradeABC);
            ps.setInt(2, idGradeABC);

            int rows = ps.executeUpdate();
            return rows > 0; // true kalau ada baris terhapus

        } catch (SQLException e) {
            System.err.println("[DB_ERROR] deleteGradeABCDetail: " + e.getMessage()
                    + " (SQLState=" + e.getSQLState() + ", Code=" + e.getErrorCode() + ")");
            return false;
        }
    }



}
