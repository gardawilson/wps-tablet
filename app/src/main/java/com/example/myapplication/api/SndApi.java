package com.example.myapplication.api;

import android.util.Log;

import com.example.myapplication.config.DatabaseConfig;
import com.example.myapplication.model.SndData;
import com.example.myapplication.model.LabelDetailData;
import com.example.myapplication.model.MstMesinData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SndApi {


    //METHOD MENAMPILKAN DATA
    public static List<SndData> getSndData(int page, int pageSize, String searchKeyword) {
        List<SndData> LmtDataList = new ArrayList<>();

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
                "    h.IdGrade, " +
                "    g.NamaGrade, " +
                "    h.IdFJProfile, " +
                "    h.IdFisik, " +
                "    h.NoSanding, " +
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
                "    h.IdUOMTblLebar, " +
                "    h.IdUOMPanjang, " +
                "    h.IdWarehouse, " +
                "    h.HasBeenPrinted, " +
                "    h.LastPrintDate " +
                "FROM " +
                "    Sanding_h h " +
                "INNER JOIN Sanding_d d ON h.NoSanding = d.NoSanding " +  // Hanya ambil yang punya data detail
                "LEFT JOIN " +
                "    ( " +
                "        SELECT NoProduksi, NoSanding " +
                "        FROM SandingProduksiOutput " +
                "    ) o ON h.NoSanding = o.NoSanding " +
                "LEFT JOIN " +
                "    MstGrade g ON h.IdGrade = g.IdGrade " +
                "LEFT JOIN " +
                "    ( " +
                "        SELECT NoProduksi, IdMesin " +
                "        FROM FJProduksi_h " +
                "    ) p ON o.NoProduksi = p.NoProduksi " +
                "LEFT JOIN " +
                "    BongkarSusunOutputSanding s ON h.NoSanding = s.NoSanding " +
                "LEFT JOIN " +
                "    MstMesin m ON p.IdMesin = m.IdMesin " +
                "LEFT JOIN " +
                "    MstOrgTelly t ON h.IdOrgTelly = t.IdOrgTelly " +
                "LEFT JOIN " +
                "    MstFJProfile f ON h.IdFJProfile = f.IdFJProfile " +
                "LEFT JOIN " +
                "    MstWarehouse w ON h.IdFisik = w.IdWarehouse " +
                "LEFT JOIN " +
                "    MstJenisKayu k ON h.IdJenisKayu = k.IdJenisKayu " +
                "WHERE " +
                "    h.NoSanding LIKE ? AND h.DateUsage IS NULL " +
                "ORDER BY h.NoSanding DESC " +
                "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            String likeKeyword = "%" + searchKeyword + "%";
            stmt.setString(1, likeKeyword);
            stmt.setInt(2, offset);
            stmt.setInt(3, pageSize);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    LmtDataList.add(new SndData(
                            // Field yang sudah ada (sesuai urutan constructor)
                            rs.getString("NoSanding"),
                            rs.getString("IdJenisKayu"),
                            rs.getString("Jenis"),  // NamaJenisKayu dari alias k.Jenis
                            rs.getString("IdGrade"),
                            rs.getString("NamaGrade"),
                            rs.getString("IdOrgTelly"),
                            rs.getString("NamaOrgTelly"),
                            rs.getString("DateCreate"),
                            rs.getString("DateUsage"),
                            rs.getString("IdUOMTblLebar"),
                            rs.getString("IdUOMPanjang"),
                            rs.getString("NoSPK"),
                            rs.getString("Jam"),
                            rs.getString("IsReject"),
                            rs.getString("IdWarehouse"),
                            rs.getString("IdFJProfile"),
                            rs.getString("IdLokasi"),
                            rs.getString("IsLembur"),
                            rs.getString("HasBeenPrinted"),
                            rs.getString("IdFisik"),
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
            Log.e("Database Fetch Error", "Error fetching Lmt data: " + e.getMessage());
        }

        return LmtDataList;
    }



    public static SndData getSndHeader(String noSanding) {
        String queryHeader = "SELECT " +
                "    o.NoProduksi, " +
                "    h.DateCreate, " +
                "    h.Jam, " +
                "    h.IdOrgTelly, " +
                "    t.NamaOrgTelly, " +
                "    h.NoSPK, " +
                "    h.NoSPKAsal, " +
                "    h.IdGrade, " +
                "    g.NamaGrade, " +
                "    h.IdFJProfile, " +
                "    h.IdFisik, " +
                "    o.NoSanding, " +
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
                "    h.IdUOMTblLebar, " +
                "    h.IdUOMPanjang, " +
                "    h.IdWarehouse, " +
                "    h.HasBeenPrinted, " +
                "    h.LastPrintDate " +
                "FROM " +
                "    Sanding_h h " +
                "LEFT JOIN " +
                "    ( " +
                "        SELECT NoProduksi, NoSanding " +
                "        FROM SandingProduksiOutput " +
                "    ) o ON h.NoSanding = o.NoSanding " +
                "LEFT JOIN " +
                "    MstGrade g ON h.IdGrade = g.IdGrade " +
                "LEFT JOIN " +
                "    ( " +
                "        SELECT NoProduksi, IdMesin " +
                "        FROM SandingProduksi_h " +
                "    ) p ON o.NoProduksi = p.NoProduksi " +
                "LEFT JOIN " +
                "    BongkarSusunOutputSanding s ON h.NoSanding = s.NoSanding " +
                "LEFT JOIN " +
                "    MstMesin m ON p.IdMesin = m.IdMesin " +
                "LEFT JOIN " +
                "    MstOrgTelly t ON h.IdOrgTelly = t.IdOrgTelly " +
                "LEFT JOIN " +
                "    MstFJProfile f ON h.IdFJProfile = f.IdFJProfile " +
                "LEFT JOIN " +
                "    MstWarehouse w ON h.IdFisik = w.IdWarehouse " +
                "LEFT JOIN " +
                "    MstJenisKayu k ON h.IdJenisKayu = k.IdJenisKayu " +
                "WHERE " +
                "    h.NoSanding = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(queryHeader)) {

            stmt.setString(1, noSanding);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new SndData(
                            // Field yang sudah ada (sesuai urutan constructor)
                            rs.getString("NoSanding"),
                            rs.getString("IdJenisKayu"),
                            rs.getString("Jenis"),
                            rs.getString("IdGrade"),
                            rs.getString("NamaGrade"),
                            rs.getString("IdOrgTelly"),
                            rs.getString("NamaOrgTelly"),
                            rs.getString("DateCreate"),
                            rs.getString("DateUsage"),
                            rs.getString("IdUOMTblLebar"),
                            rs.getString("IdUOMPanjang"),
                            rs.getString("NoSPK"),
                            rs.getString("Jam"),
                            rs.getString("IsReject"),
                            rs.getString("IdWarehouse"),
                            rs.getString("IdFJProfile"),
                            rs.getString("IdLokasi"),
                            rs.getString("IsLembur"),
                            rs.getString("HasBeenPrinted"),
                            rs.getString("IdFisik"),
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

    public static List<LabelDetailData> getSndDetail(String noSanding) {
        List<LabelDetailData> detailList = new ArrayList<>();
        String queryDetail = "SELECT Tebal, Lebar, Panjang, JmlhBatang " +
                "FROM Sanding_d " +
                "WHERE NoSanding = ? " +
                "ORDER BY NoUrut";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(queryDetail)) {

            stmt.setString(1, noSanding);

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
                        "FROM Sanding_h h " +
                        "WHERE h.NoSanding LIKE ? " +
                        "AND h.DateUsage IS NULL " +
                        "AND EXISTS ( " +
                        "   SELECT 1 FROM Sanding_d d " +
                        "   WHERE d.NoSanding = h.NoSanding " +
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
                "FROM dbo.SandingProduksi_h a " +
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
        String query = "SELECT MAX(NoSanding) FROM dbo.Sanding_h";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String lastNumber = rs.getString(1);

                if (lastNumber != null && lastNumber.startsWith("W.")) {
                    String numericPart = lastNumber.substring(2);
                    int numericValue = Integer.parseInt(numericPart);
                    int newNumericValue = numericValue + 1;

                    // Format baru, misalnya R.000123
                    return "W." + String.format("%06d", newNumericValue);
                }
            }

        } catch (SQLException e) {
            System.err.println("[DB_ERROR] Gagal generate NoMoulding:");
            e.printStackTrace();
        }

        return null; // gagal atau tidak ada data
    }

    public static boolean saveData(
            String noSanding, String dateCreate, String time, String idTelly,
            String noSPK, String noSPKasal, int idGrade, int idJenisKayu,
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
            if (!insertHeader(con, noSanding, dateCreate, time, idTelly, noSPK, noSPKasal,
                    idGrade, idJenisKayu, idFJProfile, isReject, isLembur,
                    idUOMTblLebar, idUOMPanjang, remark, idLokasi)) {
                con.rollback();
                Log.e(TAG, "Gagal insert Lmt Header untuk noSanding=" + noSanding);
                return false;
            }

            // 2. Insert Detail (Batch)
            if (!insertDetail(con, noSanding, dataList)) {
                con.rollback();
                Log.e(TAG, "Gagal insert Lmt Detail untuk noSanding=" + noSanding);
                return false;
            }

            // 3. Insert ProduksiOutput (conditional)
            if (isProduksiOutput && noProduksi != null && !noProduksi.trim().isEmpty()) {
                if (!insertProduksiOutput(con, noProduksi, noSanding)) {
                    Log.w(TAG, "Insert ProduksiOutput gagal untuk noSanding=" + noSanding + ", NoProduksi=" + noProduksi);
                    // lanjut tanpa rollback
                }
            }

            // 4. Insert BongkarSusun (conditional)
            if (isBongkarSusun && noBongkarSusun != null && !noBongkarSusun.trim().isEmpty()) {
                if (!insertBongkarSusun(con, noBongkarSusun, noSanding)) {
                    Log.w(TAG, "Insert BongkarSusun gagal untuk noSanding=" + noSanding + ", NoBongkarSusun=" + noBongkarSusun);
                    // lanjut tanpa rollback
                }
            }

            con.commit(); // Commit all changes
            Log.i(TAG, "Data Lmt berhasil disimpan untuk noSanding=" + noSanding);
            return true;

        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException rollbackEx) {
                    Log.e(TAG, "Gagal rollback transaction", rollbackEx);
                }
            }
            Log.e(TAG, "Transaction gagal untuk noSanding=" + noSanding, e);
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
                                        String noSanding, String dateCreate, String time, String idTelly,
                                        String noSPK, String noSPKasal, int idGrade, int idJenisKayu,
                                        String idFJProfile, int isReject, int isLembur,
                                        int idUOMTblLebar, int idUOMPanjang, String remark, String idLokasi) throws SQLException {

        String query = "INSERT INTO dbo.Sanding_h (NoSanding, DateCreate, Jam, IdOrgTelly, NoSPK, NoSPKAsal, IdGrade, " +
                "IdFJProfile, IdFisik, IdJenisKayu, IdWarehouse, IsReject, IsLembur, IdUOMTblLebar, IdUOMPanjang, Remark, IdLokasi) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 9, ?, 9, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, noSanding);
            ps.setString(2, dateCreate);
            ps.setString(3, time);
            ps.setString(4, idTelly);
            ps.setString(5, noSPK);
            ps.setString(6, noSPKasal);
            ps.setInt(7, idGrade);
            ps.setString(8, idFJProfile);
            ps.setInt(9, idJenisKayu);
            ps.setInt(10, isReject);
            ps.setInt(11, isLembur);
            ps.setInt(12, idUOMTblLebar);
            ps.setInt(13, idUOMPanjang);
            ps.setString(14, remark);

            // Handle null values for idLokasi
            if ("PILIH".equals(idLokasi) || idLokasi == null || idLokasi.trim().isEmpty()) {
                ps.setNull(15, java.sql.Types.VARCHAR);
            } else {
                ps.setString(15, idLokasi);
            }

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Insert LMT Detail data using batch processing
     */
    private static boolean insertDetail(Connection con, String noSanding, List<LabelDetailData> dataList) throws SQLException {
        if (dataList == null || dataList.isEmpty()) {
            return true; // Consider empty list as success
        }

        String query = "INSERT INTO dbo.Sanding_d " +
                "(NoSanding, NoUrut, Tebal, Lebar, Panjang, JmlhBatang) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            for (int i = 0; i < dataList.size(); i++) {
                LabelDetailData data = dataList.get(i);

                ps.setString(1, noSanding);
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
    private static boolean insertProduksiOutput(Connection con, String noProduksi, String noSanding) throws SQLException {
        String query;

        // Determine query based on NoProduksi prefix
        if (noProduksi.startsWith("WA")) {
            query = "INSERT INTO dbo.SandingProduksiOutput (NoProduksi, NoSanding) VALUES (?, ?)";
        } else {
            System.err.println("[DB_ERROR] Prefix NoProduksi tidak valid: " + noProduksi);
            return false;
        }

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, noProduksi);
            ps.setString(2, noSanding);

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Insert BongkarSusun data
     */
    private static boolean insertBongkarSusun(Connection con, String noBongkarSusun, String noSanding) throws SQLException {
        String query = "INSERT INTO dbo.BongkarSusunOutputSanding (NoSanding, NoBongkarSusun) VALUES (?, ?)";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, noSanding);
            ps.setString(2, noBongkarSusun);

            return ps.executeUpdate() > 0;
        }
    }


    //METHOD UPDATE DATA
    public static boolean updateData(
            String noSanding, String dateCreate, String time, String idTelly,
            String noSPK, String noSPKasal, int idGrade, int idJenisKayu,
            String idFJProfile, int isReject, int isLembur,
            int idUOMTblLebar, int idUOMPanjang, String remark, String idLokasi,
            List<LabelDetailData> dataList) {

        final String TAG = "UpdateDataLMT";
        Connection con = null;

        try {
            con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
            con.setAutoCommit(false); // Start transaction

            // 1. Update Header
            if (!updateHeader(con, noSanding, dateCreate, time, idTelly, noSPK, noSPKasal,
                    idGrade, idJenisKayu, idFJProfile, isReject, isLembur,
                    idUOMTblLebar, idUOMPanjang, remark, idLokasi)) {
                con.rollback();
                Log.e(TAG, "Gagal update LMT Header untuk noSanding=" + noSanding);
                return false;
            }

            // 2. Delete existing detail dan Insert ulang (Replace strategy)
            if (!replaceDetail(con, noSanding, dataList)) {
                con.rollback();
                Log.e(TAG, "Gagal update LMT Detail untuk noSanding=" + noSanding);
                return false;
            }

            con.commit(); // Commit all changes
            Log.i(TAG, "Data LMT berhasil diupdate untuk noSanding=" + noSanding);
            return true;

        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException rollbackEx) {
                    Log.e(TAG, "Gagal rollback transaction", rollbackEx);
                }
            }
            Log.e(TAG, "Update transaction gagal untuk noSanding=" + noSanding, e);
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
     * Update LMT Header data
     */
    private static boolean updateHeader(Connection con,
                                        String noSanding, String dateCreate, String time, String idTelly,
                                        String noSPK, String noSPKasal, int idGrade, int idJenisKayu,
                                        String idFJProfile, int isReject, int isLembur,
                                        int idUOMTblLebar, int idUOMPanjang, String remark, String idLokasi) throws SQLException {

        String query = "UPDATE dbo.Sanding_h SET " +
                "DateCreate=?, Jam=?, IdOrgTelly=?, NoSPK=?, NoSPKAsal=?, IdGrade=?, " +
                "IdFJProfile=?, IdJenisKayu=?, IsReject=?, IsLembur=?, IdUOMTblLebar=?, IdUOMPanjang=?, " +
                "Remark=?, IdLokasi=? " +
                "WHERE NoSanding=?";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, dateCreate);
            ps.setString(2, time);
            ps.setString(3, idTelly);
            ps.setString(4, noSPK);
            ps.setString(5, noSPKasal);
            ps.setInt(6, idGrade);
            ps.setString(7, idFJProfile);
            ps.setInt(8, idJenisKayu);
            ps.setInt(9, isReject);
            ps.setInt(10, isLembur);
            ps.setInt(11, idUOMTblLebar);
            ps.setInt(12, idUOMPanjang);
            ps.setString(13, remark);

            // Handle null values for idLokasi
            if (idLokasi.equals("PILIH") || idLokasi == null || idLokasi.trim().isEmpty()) {
                ps.setNull(14, java.sql.Types.VARCHAR);
            } else {
                ps.setString(14, idLokasi);
            }

            ps.setString(15, noSanding); // WHERE condition

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                System.err.println("[DB_ERROR] No rows updated for noSanding=" + noSanding + ". Data mungkin tidak exist.");
                return false;
            }
            return true;
        }
    }

    /**
     * Replace LMT Detail data (Delete existing + Insert new)
     */
    private static boolean replaceDetail(Connection con, String noSanding, List<LabelDetailData> dataList) throws SQLException {
        // 1. Delete existing detail
        String deleteQuery = "DELETE FROM dbo.Sanding_d WHERE NoSanding = ?";
        try (PreparedStatement ps = con.prepareStatement(deleteQuery)) {
            ps.setString(1, noSanding);
            ps.executeUpdate(); // Don't check result, might be 0 if no existing detail
        }

        // 2. Insert new detail if provided
        if (dataList != null && !dataList.isEmpty()) {
            return insertDetail(con, noSanding, dataList);
        }

        return true; // Success even if no new detail to insert
    }


    //METHOD DELETE DATA
    /**
     * Delete LMT data beserta semua relasinya
     * @param noSanding Nomor LMT yang akan dihapus
     * @return true jika berhasil, false jika gagal
     */
    public static boolean deleteData(String noSanding) {
        final String TAG = "DeleteData";
        Connection con = null;
        try {
            con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
            con.setAutoCommit(false);

            // Delete ProduksiOutput
            if (!deleteProduksiOutput(con, noSanding)) {
                Log.w(TAG, "Gagal delete ProduksiOutput untuk noSanding=" + noSanding);
            }

            // Delete BongkarSusun
            if (!deleteBongkarSusun(con, noSanding)) {
                Log.w(TAG, "Gagal delete BongkarSusun untuk noSanding=" + noSanding);
            }

            // Delete LMT Detail
            if (!deleteDetail(con, noSanding)) {
                con.rollback();
                Log.e(TAG, "Gagal delete LMT Detail untuk noSanding=" + noSanding);
                return false;
            }

            // Delete LMT Header
            if (!deleteHeader(con, noSanding)) {
                con.rollback();
                Log.e(TAG, "Gagal delete LMT Header untuk noSanding=" + noSanding);
                return false;
            }

            con.commit();
            Log.i(TAG, "Data LMT berhasil dihapus untuk noSanding=" + noSanding);
            return true;

        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException rollbackEx) {
                    Log.e(TAG, "Gagal rollback transaction", rollbackEx);
                }
            }
            Log.e(TAG, "Transaction gagal untuk delete noSanding=" + noSanding, e);
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
     * Delete LMT Header
     */
    private static boolean deleteHeader(Connection con, String noSanding) throws SQLException {
        String query = "DELETE FROM dbo.Sanding_h WHERE NoSanding = ?";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, noSanding);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                System.err.println("[DB_ERROR] Data LMT Header tidak ditemukan untuk NoSanding=" + noSanding);
                return false;
            }
            return true;
        }
    }

    /**
     * Delete LMT Detail
     */
    private static boolean deleteDetail(Connection con, String noSanding) throws SQLException {
        String query = "DELETE FROM dbo.Sanding_d WHERE NoSanding = ?";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, noSanding);

            int rowsAffected = ps.executeUpdate();
            System.out.println("[INFO] " + rowsAffected + " detail records dihapus untuk noSanding=" + noSanding);
            return true; // Return true even if no detail records (bisa saja memang kosong)
        }
    }

    /**
     * Delete ProduksiOutput dari kedua tabel
     */
    private static boolean deleteProduksiOutput(Connection con, String noSanding) throws SQLException {
        boolean deletedFromFJ = false;

        // Delete dari FJProduksiOutput
        String query1 = "DELETE FROM dbo.SandingProduksiOutput WHERE NoSanding = ?";
        try (PreparedStatement ps = con.prepareStatement(query1)) {
            ps.setString(1, noSanding);
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
    private static boolean deleteBongkarSusun(Connection con, String noSanding) throws SQLException {
        String query = "DELETE FROM dbo.BongkarSusunOutputSanding WHERE NoSanding = ?";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, noSanding);

            int rowsAffected = ps.executeUpdate();
            System.out.println("[INFO] " + rowsAffected + " BongkarSusun records dihapus untuk noSanding=" + noSanding);
            return true; // Return true even if no records found
        }
    }

    /**
     * Check apakah data LMT exists sebelum delete
     */
    public static boolean isDataExists(String noSanding) {
        Connection con = null;
        try {
            con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());

            String query = "SELECT COUNT(*) FROM dbo.Sanding_h WHERE NoSanding = ?";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setString(1, noSanding);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1) > 0;
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("[DB_ERROR] Error checking data existence for noSanding=" + noSanding + ":");
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
