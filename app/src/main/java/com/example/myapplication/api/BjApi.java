package com.example.myapplication.api;

import android.util.Log;

import com.example.myapplication.config.DatabaseConfig;
import com.example.myapplication.model.BjData;
import com.example.myapplication.model.LabelDetailData;
import com.example.myapplication.model.MstMesinData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BjApi {


    //METHOD MENAMPILKAN DATA
    public static List<BjData> getBjData(int page, int pageSize, String searchKeyword) {
        List<BjData> BjDataList = new ArrayList<>();

        int offset = (page - 1) * pageSize;

        // Query lengkap dengan semua JOIN yang diperlukan
        String query = "SELECT " +
                "    o.NoProduksi, " +
                "    h.DateCreate, " +
                "    h.Jam, " +
                "    h.IdOrgTelly, " +
                "    t.NamaOrgTelly, " +
                "    h.NoSPK, " +
                "    h.NoSPKAsal, " +
                "    h.IdBarangJadi, " +
                "    g.NamaBarangJadi, " +
                "    h.IdFJProfile, " +
                "    h.NoBJ, " +
                "    p.IdMesin, " +
                "    m.NamaMesin, " +
                "    s.NoBongkarSusun, " +
                "    f.Profile, " +
                "    w.NamaWarehouse, " +
                "    h.IdJenisKayu, " +
                "    k.Jenis, " +
                "    h.IsLembur, " +
                "    h.IsReject, " +
                "    h.Remark, " +
                "    h.IdLokasi, " +
                "    h.DateUsage, " +
                "    h.IdWarehouse, " +
                "    h.HasBeenPrinted, " +
                "    h.LastPrintDate " +
                "FROM " +
                "    BarangJadi_h h " +
                "INNER JOIN BarangJadi_d d ON h.NoBJ = d.NoBJ " +  // Hanya ambil yang punya data detail
                "LEFT JOIN " +
                "    ( " +
                "        SELECT NoProduksi, NoBJ " +
                "        FROM PackingProduksiOutput " +
                "    ) o ON h.NoBJ = o.NoBJ " +
                "LEFT JOIN " +
                "    MstBarangJadi g ON h.IdBarangJadi = g.IdBarangJadi " +
                "LEFT JOIN " +
                "    ( " +
                "        SELECT NoProduksi, IdMesin " +
                "        FROM PackingProduksi_h " +
                "    ) p ON o.NoProduksi = p.NoProduksi " +
                "LEFT JOIN " +
                "    BongkarSusunOutputBarangJadi s ON h.NoBJ = s.NoBJ " +
                "LEFT JOIN " +
                "    MstMesin m ON p.IdMesin = m.IdMesin " +
                "LEFT JOIN " +
                "    MstOrgTelly t ON h.IdOrgTelly = t.IdOrgTelly " +
                "LEFT JOIN " +
                "    MstFJProfile f ON h.IdFJProfile = f.IdFJProfile " +
                "LEFT JOIN " +
                "    MstWarehouse w ON h.IdWarehouse = w.IdWarehouse " +
                "LEFT JOIN " +
                "    MstJenisKayu k ON h.IdJenisKayu = k.IdJenisKayu " +
                "WHERE " +
                "    h.NoBJ LIKE ? AND h.DateUsage IS NULL " +
                "ORDER BY h.NoBJ DESC " +
                "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            String likeKeyword = "%" + searchKeyword + "%";
            stmt.setString(1, likeKeyword);
            stmt.setInt(2, offset);
            stmt.setInt(3, pageSize);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    BjDataList.add(new BjData(
                            // Field yang sudah ada (sesuai urutan constructor)
                            rs.getString("NoBJ"),
                            rs.getString("IdJenisKayu"),
                            rs.getString("Jenis"),  // NamaJenisKayu dari alias k.Jenis
                            rs.getInt("IdBarangJadi"),
                            rs.getString("NamaBarangJadi"),
                            rs.getString("IdOrgTelly"),
                            rs.getString("NamaOrgTelly"),
                            rs.getString("DateCreate"),
                            rs.getString("DateUsage"),
                            rs.getString("NoSPK"),
                            rs.getString("Jam"),
                            rs.getString("IsReject"),
                            rs.getString("IdWarehouse"),
                            rs.getString("IdFJProfile"),
                            rs.getString("IdLokasi"),
                            rs.getString("IsLembur"),
                            rs.getString("HasBeenPrinted"),
                            rs.getString("NoSPKAsal"),
                            rs.getString("Remark"),
                            rs.getString("LastPrintDate"),
                            // Field tambahan (sesuai urutan constructor)
                            rs.getString("NoProduksi"),
                            rs.getString("IdMesin"),
                            rs.getString("NamaMesin"),
                            rs.getString("NoBongkarSusun"),
                            rs.getString("Profile"),
                            rs.getString("NamaWarehouse")
                    ));
                }
            }

        } catch (SQLException e) {
            Log.e("Database Fetch Error", "Error fetching Snd data: " + e.getMessage());
        }

        return BjDataList;
    }



    public static BjData getSndHeader(String noBarangJadi) {
        String queryHeader = "SELECT " +
                "    o.NoProduksi, " +
                "    h.DateCreate, " +
                "    h.Jam, " +
                "    h.IdOrgTelly, " +
                "    t.NamaOrgTelly, " +
                "    h.NoSPK, " +
                "    h.NoSPKAsal, " +
                "    h.IdBarangJadi, " +
                "    g.NamaBarangJadi, " +
                "    h.IdFJProfile, " +
                "    h.IdWarehouse, " +
                "    o.NoBJ, " +
                "    p.IdMesin, " +
                "    m.NamaMesin, " +
                "    s.NoBongkarSusun, " +
                "    f.Profile, " +
                "    w.NamaWarehouse, " +
                "    h.IdJenisKayu, " +
                "    k.Jenis, " +
                "    h.IsLembur, " +
                "    h.IsReject, " +
                "    h.Remark, " +
                "    h.IdLokasi, " +
                "    h.DateUsage, " +
                "    h.IdWarehouse, " +
                "    h.HasBeenPrinted, " +
                "    h.LastPrintDate " +
                "FROM " +
                "    BarangJadi_h h " +
                "LEFT JOIN " +
                "    ( " +
                "        SELECT NoProduksi, NoBJ " +
                "        FROM PackingProduksiOutput " +
                "    ) o ON h.NoBJ = o.NoBJ " +
                "LEFT JOIN " +
                "    MstBarangJadi g ON h.IdBarangJadi = g.IdBarangJadi " +
                "LEFT JOIN " +
                "    ( " +
                "        SELECT NoProduksi, IdMesin " +
                "        FROM PackingProduksi_h " +
                "    ) p ON o.NoProduksi = p.NoProduksi " +
                "LEFT JOIN " +
                "    BongkarSusunOutputBarangJadi s ON h.NoBJ = s.NoBJ " +
                "LEFT JOIN " +
                "    MstMesin m ON p.IdMesin = m.IdMesin " +
                "LEFT JOIN " +
                "    MstOrgTelly t ON h.IdOrgTelly = t.IdOrgTelly " +
                "LEFT JOIN " +
                "    MstFJProfile f ON h.IdFJProfile = f.IdFJProfile " +
                "LEFT JOIN " +
                "    MstWarehouse w ON h.IdWarehouse = w.IdWarehouse " +
                "LEFT JOIN " +
                "    MstJenisKayu k ON h.IdJenisKayu = k.IdJenisKayu " +
                "WHERE " +
                "    h.NoBJ = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(queryHeader)) {

            stmt.setString(1, noBarangJadi);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new BjData(
                            // Field yang sudah ada (sesuai urutan constructor)
                            rs.getString("NoBJ"),
                            rs.getString("IdJenisKayu"),
                            rs.getString("Jenis"),
                            rs.getInt("IdBarangJadi"),
                            rs.getString("NamaBarangJadi"),
                            rs.getString("IdOrgTelly"),
                            rs.getString("NamaOrgTelly"),
                            rs.getString("DateCreate"),
                            rs.getString("DateUsage"),
                            rs.getString("NoSPK"),
                            rs.getString("Jam"),
                            rs.getString("IsReject"),
                            rs.getString("IdWarehouse"),
                            rs.getString("IdFJProfile"),
                            rs.getString("IdLokasi"),
                            rs.getString("IsLembur"),
                            rs.getString("HasBeenPrinted"),
                            rs.getString("NoSPKAsal"),
                            rs.getString("Remark"),
                            rs.getString("LastPrintDate"),
                            // Field tambahan
                            rs.getString("NoProduksi"),
                            rs.getString("IdMesin"),
                            rs.getString("NamaMesin"),
                            rs.getString("NoBongkarSusun"),
                            rs.getString("Profile"),
                            rs.getString("NamaWarehouse")
                    );
                }
            }

        } catch (SQLException e) {
            Log.e("Database Error", "Error fetching Laminating header: " + e.getMessage());
        }

        return null;
    }

    public static List<LabelDetailData> getSndDetail(String noBarangJadi) {
        List<LabelDetailData> detailList = new ArrayList<>();
        String queryDetail = "SELECT Tebal, Lebar, Panjang, JmlhBatang " +
                "FROM BarangJadi_d " +
                "WHERE NoBJ = ? " +
                "ORDER BY NoUrut";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(queryDetail)) {

            stmt.setString(1, noBarangJadi);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String tebal = rs.getString("Tebal");
                    String lebar = rs.getString("Lebar");
                    String panjang = rs.getString("Panjang");
                    String pcs = rs.getString("JmlhBatang");

                    LabelDetailData detailData = new LabelDetailData(tebal, lebar, panjang, pcs);
                    detailList.add(detailData);
                }
            }

        } catch (SQLException e) {
            Log.e("Database Error", "Error fetching Lmt detail: " + e.getMessage());
        }

        return detailList;
    }


    public static int getTotalLabelCount(String searchKeyword) {
        int totalLabel = 0;

        String query =
                "SELECT COUNT(*) AS TotalLabel " +
                        "FROM BarangJadi_h h " +
                        "WHERE h.NoBJ LIKE ? " +
                        "AND h.DateUsage IS NULL " +
                        "AND EXISTS ( " +
                        "   SELECT 1 FROM BarangJadi_d d " +
                        "   WHERE d.NoBJ = h.NoBJ " +
                        ")";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, "%" + searchKeyword + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    totalLabel = rs.getInt("TotalLabel");
                }
            }

        } catch (SQLException e) {
            Log.e("Database Fetch Error", "Error fetching total label count: " + e.getMessage());
        }

        return totalLabel;
    }


    //METHOD UNTUK MENAMPILKAN NAMA MESIN
    public static List<MstMesinData> getMesinList(String selectedDate) {
        List<MstMesinData> mesinList = new ArrayList<>();

        String query = "SELECT a.IdMesin, CONCAT(b.NamaMesin, ' - (SHIFT ', a.Shift, ')') AS NamaMesin, a.NoProduksi " +
                "FROM dbo.PackingProduksi_h a " +
                "INNER JOIN dbo.MstMesin b ON a.IdMesin = b.IdMesin " +
                "WHERE CONVERT(date, a.Tanggal) = CONVERT(date, ?) ";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, selectedDate);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String nomorProduksi = rs.getString("NoProduksi");
                    String namaMesin = rs.getString("NamaMesin");

                    mesinList.add(new MstMesinData(nomorProduksi, namaMesin));
                }
            }

        } catch (SQLException e) {
            System.err.println("[DB_ERROR] Gagal ambil data Mesin:");
            e.printStackTrace();
        }

        return mesinList;
    }



    //METHOD SAVE DATA
    /**
     * Main method untuk menyimpan data Lmt dengan transaction management
     */

    public static String generateNewNumber() {
        String query = "SELECT MAX(NoBJ) FROM dbo.BarangJadi_h";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String lastNumber = rs.getString(1);

                if (lastNumber != null && lastNumber.startsWith("I.")) {
                    String numericPart = lastNumber.substring(2);
                    int numericValue = Integer.parseInt(numericPart);
                    int newNumericValue = numericValue + 1;

                    // Format baru, misalnya R.000123
                    return "I." + String.format("%06d", newNumericValue);
                }
            }

        } catch (SQLException e) {
            System.err.println("[DB_ERROR] Gagal generate NoMoulding:");
            e.printStackTrace();
        }

        return null; // gagal atau tidak ada data
    }

    public static boolean saveData(
            String noBarangJadi, String dateCreate, String time, String idTelly,
            String noSPK, String noSPKasal, int idBarangJadi, int idJenisKayu,
            String idFJProfile, int isReject, int isLembur,
            int idUOMTblLebar, int idUOMPanjang, String remark, String idLokasi,
            boolean isProduksiOutput, String noProduksi,
            boolean isBongkarSusun, String noBongkarSusun,
            List<LabelDetailData> dataList) {

        final String TAG = "SaveDataLMT";
        Connection con = null;

        try {
            con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
            con.setAutoCommit(false); // Start transaction

            // 1. Insert Header
            if (!insertHeader(con, noBarangJadi, dateCreate, time, idTelly, noSPK, noSPKasal,
                    idBarangJadi, idJenisKayu, idFJProfile, isReject, isLembur,
                    idUOMTblLebar, idUOMPanjang, remark, idLokasi)) {
                con.rollback();
                Log.e(TAG, "Gagal insert Lmt Header untuk noBarangJadi=" + noBarangJadi);
                return false;
            }

            // 2. Insert Detail (Batch)
            if (!insertDetail(con, noBarangJadi, dataList)) {
                con.rollback();
                Log.e(TAG, "Gagal insert Lmt Detail untuk noBarangJadi=" + noBarangJadi);
                return false;
            }

            // 3. Insert ProduksiOutput (conditional)
            if (isProduksiOutput && noProduksi != null && !noProduksi.trim().isEmpty()) {
                if (!insertProduksiOutput(con, noProduksi, noBarangJadi)) {
                    Log.w(TAG, "Insert ProduksiOutput gagal untuk noBarangJadi=" + noBarangJadi + ", NoProduksi=" + noProduksi);
                    // lanjut tanpa rollback
                }
            }

            // 4. Insert BongkarSusun (conditional)
            if (isBongkarSusun && noBongkarSusun != null && !noBongkarSusun.trim().isEmpty()) {
                if (!insertBongkarSusun(con, noBongkarSusun, noBarangJadi)) {
                    Log.w(TAG, "Insert BongkarSusun gagal untuk noBarangJadi=" + noBarangJadi + ", NoBongkarSusun=" + noBongkarSusun);
                    // lanjut tanpa rollback
                }
            }

            con.commit(); // Commit all changes
            Log.i(TAG, "Data Lmt berhasil disimpan untuk noBarangJadi=" + noBarangJadi);
            return true;

        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException rollbackEx) {
                    Log.e(TAG, "Gagal rollback transaction", rollbackEx);
                }
            }
            Log.e(TAG, "Transaction gagal untuk noBarangJadi=" + noBarangJadi, e);
            return false;
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    Log.e(TAG, "Gagal close connection", e);
                }
            }
        }
    }

    /**
     * Insert Lmt Header data
     */
    private static boolean insertHeader(Connection con,
                                        String noBarangJadi, String dateCreate, String time, String idTelly,
                                        String noSPK, String noSPKasal, int idBarangJadi, int idJenisKayu,
                                        String idFJProfile, int isReject, int isLembur,
                                        int idUOMTblLebar, int idUOMPanjang, String remark, String idLokasi) throws SQLException {

        String query = "INSERT INTO dbo.BarangJadi_h (NoBJ, DateCreate, Jam, IdOrgTelly, NoSPK, NoSPKAsal, IdBarangJadi, " +
                "IdFJProfile, IdJenisKayu, IdWarehouse, IsReject, IsLembur, Remark, IdLokasi) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 11, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, noBarangJadi);
            ps.setString(2, dateCreate);
            ps.setString(3, time);
            ps.setString(4, idTelly);
            ps.setString(5, noSPK);
            ps.setString(6, noSPKasal);
            ps.setInt(7, idBarangJadi);
            ps.setString(8, idFJProfile);
            ps.setInt(9, idJenisKayu);
            ps.setInt(10, isReject);
            ps.setInt(11, isLembur);
            ps.setString(12, remark);

            // Handle null values for idLokasi
            if ("PILIH".equals(idLokasi) || idLokasi == null || idLokasi.trim().isEmpty()) {
                ps.setNull(13, java.sql.Types.VARCHAR);
            } else {
                ps.setString(13, idLokasi);
            }

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Insert BJ Detail data using batch processing
     */
    private static boolean insertDetail(Connection con, String noBarangJadi, List<LabelDetailData> dataList) throws SQLException {
        if (dataList == null || dataList.isEmpty()) {
            return true; // Consider empty list as success
        }

        String query = "INSERT INTO dbo.BarangJadi_d " +
                "(NoBJ, NoUrut, Tebal, Lebar, Panjang, JmlhBatang) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            for (int i = 0; i < dataList.size(); i++) {
                LabelDetailData data = dataList.get(i);

                ps.setString(1, noBarangJadi);
                ps.setInt(2, i + 1); // NoUrut dimulai dari 1
                ps.setDouble(3, Double.parseDouble(data.getTebal()));
                ps.setDouble(4, Double.parseDouble(data.getLebar()));
                ps.setDouble(5, Double.parseDouble(data.getPanjang()));
                ps.setInt(6, Integer.parseInt(data.getPcs()));

                ps.addBatch();
            }

            int[] results = ps.executeBatch();

            // Verify all batch executions were successful
            for (int result : results) {
                if (result == PreparedStatement.EXECUTE_FAILED) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Insert ProduksiOutput berdasarkan prefix NoProduksi
     */
    private static boolean insertProduksiOutput(Connection con, String noProduksi, String noBarangJadi) throws SQLException {
        String query;

        // Determine query based on NoProduksi prefix
        if (noProduksi.startsWith("X")) {
            query = "INSERT INTO dbo.PackingProduksiOutput (NoProduksi, NoBJ) VALUES (?, ?)";
        } else {
            System.err.println("[DB_ERROR] Prefix NoProduksi tidak valid: " + noProduksi);
            return false;
        }

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, noProduksi);
            ps.setString(2, noBarangJadi);

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Insert BongkarSusun data
     */
    private static boolean insertBongkarSusun(Connection con, String noBongkarSusun, String noBarangJadi) throws SQLException {
        String query = "INSERT INTO dbo.BongkarSusunOutputBarangJadi (NoBJ, NoBongkarSusun) VALUES (?, ?)";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, noBarangJadi);
            ps.setString(2, noBongkarSusun);

            return ps.executeUpdate() > 0;
        }
    }


    //METHOD UPDATE DATA
    public static boolean updateData(
            String noBarangJadi, String dateCreate, String time, String idTelly,
            String noSPK, String noSPKasal, int idBarangJadi, int idJenisKayu,
            String idFJProfile, int isReject, int isLembur,
            int idUOMTblLebar, int idUOMPanjang, String remark, String idLokasi,
            List<LabelDetailData> dataList) {

        final String TAG = "UpdateDataBJ";
        Connection con = null;

        try {
            con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
            con.setAutoCommit(false); // Start transaction

            // 1. Update Header
            if (!updateHeader(con, noBarangJadi, dateCreate, time, idTelly, noSPK, noSPKasal,
                    idBarangJadi, idJenisKayu, idFJProfile, isReject, isLembur,
                    idUOMTblLebar, idUOMPanjang, remark, idLokasi)) {
                con.rollback();
                Log.e(TAG, "Gagal update BJ Header untuk noBarangJadi=" + noBarangJadi);
                return false;
            }

            // 2. Delete existing detail dan Insert ulang (Replace strategy)
            if (!replaceDetail(con, noBarangJadi, dataList)) {
                con.rollback();
                Log.e(TAG, "Gagal update BJ Detail untuk noBarangJadi=" + noBarangJadi);
                return false;
            }

            con.commit(); // Commit all changes
            Log.i(TAG, "Data BJ berhasil diupdate untuk noBarangJadi=" + noBarangJadi);
            return true;

        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException rollbackEx) {
                    Log.e(TAG, "Gagal rollback transaction", rollbackEx);
                }
            }
            Log.e(TAG, "Update transaction gagal untuk noBarangJadi=" + noBarangJadi, e);
            return false;
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    Log.e(TAG, "Gagal close connection", e);
                }
            }
        }
    }

    /**
     * Update BJ Header data
     */
    private static boolean updateHeader(Connection con,
                                        String noBarangJadi, String dateCreate, String time, String idTelly,
                                        String noSPK, String noSPKasal, int idBarangJadi, int idJenisKayu,
                                        String idFJProfile, int isReject, int isLembur,
                                        int idUOMTblLebar, int idUOMPanjang, String remark, String idLokasi) throws SQLException {

        String query = "UPDATE dbo.BarangJadi_h SET " +
                "DateCreate=?, Jam=?, IdOrgTelly=?, NoSPK=?, NoSPKAsal=?, IdBarangJadi=?, " +
                "IdFJProfile=?, IdJenisKayu=?, IsReject=?, IsLembur=?, " +
                "Remark=?, IdLokasi=? " +
                "WHERE NoBJ=?";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, dateCreate);
            ps.setString(2, time);
            ps.setString(3, idTelly);
            ps.setString(4, noSPK);
            ps.setString(5, noSPKasal);
            ps.setInt(6, idBarangJadi);
            ps.setString(7, idFJProfile);
            ps.setInt(8, idJenisKayu);
            ps.setInt(9, isReject);
            ps.setInt(10, isLembur);
            ps.setString(11, remark);

            // Handle null values for idLokasi
            if (idLokasi.equals("PILIH") || idLokasi == null || idLokasi.trim().isEmpty()) {
                ps.setNull(12, java.sql.Types.VARCHAR);
            } else {
                ps.setString(12, idLokasi);
            }

            ps.setString(13, noBarangJadi); // WHERE condition

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                System.err.println("[DB_ERROR] No rows updated for noBarangJadi=" + noBarangJadi + ". Data mungkin tidak exist.");
                return false;
            }
            return true;
        }
    }

    /**
     * Replace BJ Detail data (Delete existing + Insert new)
     */
    private static boolean replaceDetail(Connection con, String noBarangJadi, List<LabelDetailData> dataList) throws SQLException {
        // 1. Delete existing detail
        String deleteQuery = "DELETE FROM dbo.BarangJadi_d WHERE NoBJ = ?";
        try (PreparedStatement ps = con.prepareStatement(deleteQuery)) {
            ps.setString(1, noBarangJadi);
            ps.executeUpdate(); // Don't check result, might be 0 if no existing detail
        }

        // 2. Insert new detail if provided
        if (dataList != null && !dataList.isEmpty()) {
            return insertDetail(con, noBarangJadi, dataList);
        }

        return true; // Success even if no new detail to insert
    }


    //METHOD DELETE DATA
    /**
     * Delete BJ data beserta semua relasinya
     * @param noBarangJadi Nomor BJ yang akan dihapus
     * @return true jika berhasil, false jika gagal
     */
    public static boolean deleteData(String noBarangJadi) {
        final String TAG = "DeleteData";
        Connection con = null;
        try {
            con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
            con.setAutoCommit(false);

            // Delete ProduksiOutput
            if (!deleteProduksiOutput(con, noBarangJadi)) {
                Log.w(TAG, "Gagal delete ProduksiOutput untuk noBarangJadi=" + noBarangJadi);
            }

            // Delete BongkarSusun
            if (!deleteBongkarSusun(con, noBarangJadi)) {
                Log.w(TAG, "Gagal delete BongkarSusun untuk noBarangJadi=" + noBarangJadi);
            }

            // Delete BJ Detail
            if (!deleteDetail(con, noBarangJadi)) {
                con.rollback();
                Log.e(TAG, "Gagal delete BJ Detail untuk noBarangJadi=" + noBarangJadi);
                return false;
            }

            // Delete BJ Header
            if (!deleteHeader(con, noBarangJadi)) {
                con.rollback();
                Log.e(TAG, "Gagal delete BJ Header untuk noBarangJadi=" + noBarangJadi);
                return false;
            }

            con.commit();
            Log.i(TAG, "Data BJ berhasil dihapus untuk noBarangJadi=" + noBarangJadi);
            return true;

        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException rollbackEx) {
                    Log.e(TAG, "Gagal rollback transaction", rollbackEx);
                }
            }
            Log.e(TAG, "Transaction gagal untuk delete noBarangJadi=" + noBarangJadi, e);
            return false;
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    Log.e(TAG, "Gagal close connection", e);
                }
            }
        }
    }

    /**
     * Delete BJ Header
     */
    private static boolean deleteHeader(Connection con, String noBarangJadi) throws SQLException {
        String query = "DELETE FROM dbo.BarangJadi_h WHERE NoBJ = ?";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, noBarangJadi);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                System.err.println("[DB_ERROR] Data BJ Header tidak ditemukan untuk NoBJ=" + noBarangJadi);
                return false;
            }
            return true;
        }
    }

    /**
     * Delete BJ Detail
     */
    private static boolean deleteDetail(Connection con, String noBarangJadi) throws SQLException {
        String query = "DELETE FROM dbo.BarangJadi_d WHERE NoBJ = ?";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, noBarangJadi);

            int rowsAffected = ps.executeUpdate();
            System.out.println("[INFO] " + rowsAffected + " detail records dihapus untuk noBarangJadi=" + noBarangJadi);
            return true; // Return true even if no detail records (bisa saja memang kosong)
        }
    }

    /**
     * Delete ProduksiOutput dari kedua tabel
     */
    private static boolean deleteProduksiOutput(Connection con, String noBarangJadi) throws SQLException {
        boolean deletedFromFJ = false;

        // Delete dari FJProduksiOutput
        String query1 = "DELETE FROM dbo.PackingProduksiOutput WHERE NoBJ = ?";
        try (PreparedStatement ps = con.prepareStatement(query1)) {
            ps.setString(1, noBarangJadi);
            int rows1 = ps.executeUpdate();
            if (rows1 > 0) {
                deletedFromFJ = true;
            }
        } catch (SQLException e) {
            System.err.println("[WARN] Error delete dari ProduksiOutput: " + e.getMessage());
        }

        // return true kalau minimal 1 query berhasil
        return deletedFromFJ;
    }


    /**
     * Delete BongkarSusun
     */
    private static boolean deleteBongkarSusun(Connection con, String noBarangJadi) throws SQLException {
        String query = "DELETE FROM dbo.BongkarSusunOutputBarangJadi WHERE NoBJ = ?";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, noBarangJadi);

            int rowsAffected = ps.executeUpdate();
            System.out.println("[INFO] " + rowsAffected + " BongkarSusun records dihapus untuk noBarangJadi=" + noBarangJadi);
            return true; // Return true even if no records found
        }
    }

    /**
     * Check apakah data BJ exists sebelum delete
     */
    public static boolean isDataExists(String noBarangJadi) {
        Connection con = null;
        try {
            con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());

            String query = "SELECT COUNT(*) FROM dbo.BarangJadi_h WHERE NoBJ = ?";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setString(1, noBarangJadi);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1) > 0;
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("[DB_ERROR] Error checking data existence for noBarangJadi=" + noBarangJadi + ":");
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    System.err.println("[DB_ERROR] Gagal close connection:");
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}
