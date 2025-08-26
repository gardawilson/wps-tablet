package com.example.myapplication.api;

import android.util.Log;

import com.example.myapplication.config.DatabaseConfig;
import com.example.myapplication.model.LabelDetailData;
import com.example.myapplication.model.MesinData;
import com.example.myapplication.model.S4sData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class S4sApi {

    public static List<S4sData> getS4SData(int page, int pageSize, String searchKeyword) {
        List<S4sData> s4sDataList = new ArrayList<>();

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
                "    h.NoS4S, " +
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
                "    h.IdWarna, " +
                "    h.IdLokasi, " +
                "    h.DateUsage, " +
                "    h.NoSTAsal, " +
                "    h.IdUOMTblLebar, " +
                "    h.IdUOMPanjang, " +
                "    h.IdWarehouse, " +
                "    h.HasBeenPrinted, " +
                "    h.LastPrintDate " +
                "FROM " +
                "    S4S_h h " +
                "INNER JOIN S4S_d d ON h.NoS4S = d.NoS4S " +  // Hanya ambil yang punya data detail
                "LEFT JOIN " +
                "    ( " +
                "        SELECT NoProduksi, NoS4S " +
                "        FROM S4SProduksiOutput " +
                "        UNION " +
                "        SELECT NoProduksi, NoS4S " +
                "        FROM CCAkhirProduksiOutputS4S " +
                "    ) o ON h.NoS4S = o.NoS4S " +
                "LEFT JOIN " +
                "    MstGrade g ON h.IdGrade = g.IdGrade " +
                "LEFT JOIN " +
                "    ( " +
                "        SELECT NoProduksi, IdMesin " +
                "        FROM S4SProduksi_h " +
                "        UNION " +
                "        SELECT NoProduksi, IdMesin " +
                "        FROM CCAkhirProduksi_h " +
                "    ) p ON o.NoProduksi = p.NoProduksi " +
                "LEFT JOIN " +
                "    BongkarSusunOutputS4S s ON h.NoS4S = s.NoS4S " +
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
                "    h.NoS4S LIKE ? AND h.DateUsage IS NULL " +
                "ORDER BY h.NoS4S DESC " +
                "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            String likeKeyword = "%" + searchKeyword + "%";
            stmt.setString(1, likeKeyword); // NoS4S
            stmt.setInt(2, offset);
            stmt.setInt(3, pageSize);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    s4sDataList.add(new S4sData(
                            // Field yang sudah ada (sesuai urutan constructor)
                            rs.getString("NoS4S"),
                            rs.getString("IdJenisKayu"),
                            rs.getString("Jenis"),  // NamaJenisKayu dari alias k.Jenis
                            rs.getString("IdGrade"),
                            rs.getString("NamaGrade"),
                            rs.getString("IdOrgTelly"),
                            rs.getString("NamaOrgTelly"),
                            rs.getString("DateCreate"),
                            rs.getString("DateUsage"),
                            rs.getString("NoSTAsal"),
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
                            rs.getString("NamaWarehouse"),
                            rs.getString("IdWarna")
                    ));
                }
            }

        } catch (SQLException e) {
            Log.e("Database Fetch Error", "Error fetching S4S data: " + e.getMessage());
        }

        return s4sDataList;
    }

    public static int getTotalLabelCount(String searchKeyword) {
        int totalLabel = 0;

        String query =
                "SELECT COUNT(*) AS TotalLabel " +
                        "FROM S4S_h h " +
                        "WHERE h.NoS4S LIKE ? " +
                        "AND h.DateUsage IS NULL " +
                        "AND EXISTS ( " +
                        "   SELECT 1 FROM S4S_d d " +
                        "   WHERE d.NoS4S = h.NoS4S " +
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


    // Tambahkan method ini ke class S4sApi yang sudah ada

    public static S4sData getS4SHeaderByNoS4S(String noS4S) {
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
                "    o.NoS4S, " +
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
                "    h.IdWarna, " +
                "    h.IdLokasi, " +
                "    h.DateUsage, " +
                "    h.NoSTAsal, " +
                "    h.IdUOMTblLebar, " +
                "    h.IdUOMPanjang, " +
                "    h.IdWarehouse, " +
                "    h.HasBeenPrinted, " +
                "    h.LastPrintDate " +
                "FROM " +
                "    S4S_h h " +
                "LEFT JOIN " +
                "    ( " +
                "        SELECT NoProduksi, NoS4S " +
                "        FROM S4SProduksiOutput " +
                "        UNION " +
                "        SELECT NoProduksi, NoS4S " +
                "        FROM CCAkhirProduksiOutputS4S " +
                "    ) o ON h.NoS4S = o.NoS4S " +
                "LEFT JOIN " +
                "    MstGrade g ON h.IdGrade = g.IdGrade " +
                "LEFT JOIN " +
                "    ( " +
                "        SELECT NoProduksi, IdMesin " +
                "        FROM S4SProduksi_h " +
                "        UNION " +
                "        SELECT NoProduksi, IdMesin " +
                "        FROM CCAkhirProduksi_h " +
                "    ) p ON o.NoProduksi = p.NoProduksi " +
                "LEFT JOIN " +
                "    BongkarSusunOutputS4S s ON h.NoS4S = s.NoS4S " +
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
                "    h.NoS4S = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(queryHeader)) {

            stmt.setString(1, noS4S);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new S4sData(
                            // Field yang sudah ada (sesuai urutan constructor)
                            rs.getString("NoS4S"),
                            rs.getString("IdJenisKayu"),
                            rs.getString("Jenis"),  // NamaJenisKayu
                            rs.getString("IdGrade"),
                            rs.getString("NamaGrade"),
                            rs.getString("IdOrgTelly"),
                            rs.getString("NamaOrgTelly"),
                            rs.getString("DateCreate"),
                            rs.getString("DateUsage"),
                            rs.getString("NoSTAsal"),
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
                            rs.getString("NamaWarehouse"),
                            rs.getString("IdWarna")
                    );
                }
            }

        } catch (SQLException e) {
            Log.e("Database Error", "Error fetching S4S header: " + e.getMessage());
        }

        return null;
    }

    public static List<LabelDetailData> getS4SDetailByNoS4S(String noS4S) {
        List<LabelDetailData> detailList = new ArrayList<>();
        String queryDetail = "SELECT Tebal, Lebar, Panjang, JmlhBatang " +
                "FROM S4S_d " +
                "WHERE NoS4S = ? " +
                "ORDER BY NoUrut";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(queryDetail)) {

            stmt.setString(1, noS4S);

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
            Log.e("Database Error", "Error fetching S4S detail: " + e.getMessage());
        }

        return detailList;
    }





    public static List<MesinData> getMesinList(String selectedDate) {
        List<MesinData> mesinList = new ArrayList<>();

        String query = "SELECT a.IdMesin, CONCAT(b.NamaMesin, ' - (SHIFT ', a.Shift, ')') AS NamaMesin, a.NoProduksi " +
                "FROM dbo.S4SProduksi_h a " +
                "INNER JOIN dbo.MstMesin b ON a.IdMesin = b.IdMesin " +
                "WHERE CONVERT(date, a.Tanggal) = CONVERT(date, ?) " +
                "UNION ALL " +
                "SELECT a.IdMesin, CONCAT(b.NamaMesin, ' - (SHIFT ', a.Shift, ')') AS NamaMesin, a.NoProduksi " +
                "FROM dbo.CCAkhirProduksi_h a " +
                "INNER JOIN dbo.MstMesin b ON a.IdMesin = b.IdMesin " +
                "WHERE a.Tanggal = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, selectedDate);
            ps.setString(2, selectedDate);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String nomorProduksi = rs.getString("NoProduksi");
                    String namaMesin = rs.getString("NamaMesin");

                    mesinList.add(new MesinData(nomorProduksi, namaMesin));
                }
            }

        } catch (SQLException e) {
            System.err.println("[DB_ERROR] Gagal ambil data Mesin:");
            e.printStackTrace();
        }

        return mesinList;
    }

    public static String generateNewNoS4S() {
        String query = "SELECT MAX(NoS4S) FROM dbo.S4S_h";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String lastNoS4S = rs.getString(1);

                if (lastNoS4S != null && lastNoS4S.startsWith("R.")) {
                    String numericPart = lastNoS4S.substring(2);
                    int numericValue = Integer.parseInt(numericPart);
                    int newNumericValue = numericValue + 1;

                    // Format baru, misalnya R.000123
                    return "R." + String.format("%06d", newNumericValue);
                }
            }

        } catch (SQLException e) {
            System.err.println("[DB_ERROR] Gagal generate NoS4S:");
            e.printStackTrace();
        }

        return null; // gagal atau tidak ada data
    }


    /**
     * Main method untuk menyimpan data S4S dengan transaction management
     */
    public static boolean saveData(
            String noS4S, String dateCreate, String time, String idTelly,
            String noSPK, String noSPKasal, int idGrade, int idJenisKayu,
            String idFJProfile, int isReject, int isLembur,
            int idUOMTblLebar, int idUOMPanjang, String remark,
            int idWarna, String idLokasi,
            boolean isProduksiOutput, String noProduksi,
            boolean isBongkarSusun, String noBongkarSusun,
            List<LabelDetailData> dataList) {

        Connection con = null;
        try {
            con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
            con.setAutoCommit(false); // Start transaction

            // 1. Insert Header
            if (!insertHeader(con, noS4S, dateCreate, time, idTelly, noSPK, noSPKasal,
                    idGrade, idJenisKayu, idFJProfile, isReject, isLembur,
                    idUOMTblLebar, idUOMPanjang, remark, idWarna, idLokasi)) {
                con.rollback();
                System.err.println("[DB_ERROR] Gagal insert S4S Header untuk NoS4S=" + noS4S);
                return false;
            }

            // 2. Insert Detail (Batch)
            if (!insertDetail(con, noS4S, dataList)) {
                con.rollback();
                System.err.println("[DB_ERROR] Gagal insert S4S Detail untuk NoS4S=" + noS4S);
                return false;
            }

            // 3. Insert ProduksiOutput (conditional) - tidak rollback jika gagal, hanya warning
            if (isProduksiOutput && noProduksi != null && !noProduksi.trim().isEmpty()) {
                if (!insertProduksiOutput(con, noProduksi, noS4S)) {
                    System.err.println("[WARN] Insert ProduksiOutput gagal untuk NoS4S=" + noS4S + ", NoProduksi=" + noProduksi);
                    // Continue without rollback - business decision
                }
            }

            // 4. Insert BongkarSusun (conditional) - tidak rollback jika gagal, hanya warning
            if (isBongkarSusun && noBongkarSusun != null && !noBongkarSusun.trim().isEmpty()) {
                if (!insertBongkarSusun(con, noBongkarSusun, noS4S)) {
                    System.err.println("[WARN] Insert BongkarSusun gagal untuk NoS4S=" + noS4S + ", NoBongkarSusun=" + noBongkarSusun);
                    // Continue without rollback - business decision
                }
            }

            con.commit(); // Commit all changes
            System.out.println("[SUCCESS] Data S4S berhasil disimpan untuk NoS4S=" + noS4S);
            return true;

        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException rollbackEx) {
                    System.err.println("[DB_ERROR] Gagal rollback transaction:");
                    rollbackEx.printStackTrace();
                }
            }
            System.err.println("[DB_ERROR] Transaction gagal untuk NoS4S=" + noS4S + ":");
            e.printStackTrace();
            return false;
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
    }

    /**
     * Insert S4S Header data
     */
    private static boolean insertHeader(Connection con,
                                        String noS4S, String dateCreate, String time, String idTelly,
                                        String noSPK, String noSPKasal, int idGrade, int idJenisKayu,
                                        String idFJProfile, int isReject, int isLembur,
                                        int idUOMTblLebar, int idUOMPanjang, String remark,
                                        int idWarna, String idLokasi) throws SQLException {

        String query = "INSERT INTO dbo.S4S_h (NoS4S, DateCreate, Jam, IdOrgTelly, NoSPK, NoSPKAsal, IdGrade, " +
                "IdFJProfile, IdFisik, IdJenisKayu, IdWarehouse, IsReject, IsLembur, IdUOMTblLebar, IdUOMPanjang, Remark, IdWarna, IdLokasi) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 4, ?, 4, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, noS4S);
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

            // Handle null values for idWarna
            if (idWarna == 0) {
                ps.setNull(15, java.sql.Types.INTEGER);
            } else {
                ps.setInt(15, idWarna);
            }

            // Handle null values for idLokasi
            if ("PILIH".equals(idLokasi) || idLokasi == null || idLokasi.trim().isEmpty()) {
                ps.setNull(16, java.sql.Types.VARCHAR);
            } else {
                ps.setString(16, idLokasi);
            }

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Insert S4S Detail data using batch processing
     */
    private static boolean insertDetail(Connection con, String noS4S, List<LabelDetailData> dataList) throws SQLException {
        if (dataList == null || dataList.isEmpty()) {
            System.out.println("[INFO] Tidak ada detail data untuk diinsert, NoS4S=" + noS4S);
            return true; // Consider empty list as success
        }

        String query = "INSERT INTO dbo.S4S_d " +
                "(NoS4S, NoUrut, Tebal, Lebar, Panjang, JmlhBatang) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            for (int i = 0; i < dataList.size(); i++) {
                LabelDetailData data = dataList.get(i);

                ps.setString(1, noS4S);
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
    private static boolean insertProduksiOutput(Connection con, String noProduksi, String noS4S) throws SQLException {
        String query;

        // Determine query based on NoProduksi prefix
        if (noProduksi.startsWith("RA")) {
            query = "INSERT INTO dbo.S4SProduksiOutput (NoProduksi, NoS4S) VALUES (?, ?)";
        } else if (noProduksi.startsWith("VA")) {
            query = "INSERT INTO dbo.CCAkhirProduksiOutputS4S (NoProduksi, NoS4S) VALUES (?, ?)";
        } else {
            System.err.println("[DB_ERROR] Prefix NoProduksi tidak valid: " + noProduksi);
            return false;
        }

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, noProduksi);
            ps.setString(2, noS4S);

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Insert BongkarSusun data
     */
    private static boolean insertBongkarSusun(Connection con, String noBongkarSusun, String noS4S) throws SQLException {
        String query = "INSERT INTO dbo.BongkarSusunOutputS4S (NoS4S, NoBongkarSusun) VALUES (?, ?)";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, noS4S);
            ps.setString(2, noBongkarSusun);

            return ps.executeUpdate() > 0;
        }
    }


    public static boolean updateData(
            String noS4S, String dateCreate, String time, String idTelly,
            String noSPK, String noSPKasal, int idGrade, int idJenisKayu,
            String idFJProfile, int isReject, int isLembur,
            int idUOMTblLebar, int idUOMPanjang, String remark,
            int idWarna, String idLokasi,
            List<LabelDetailData> dataList) {

        Connection con = null;
        try {
            con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
            con.setAutoCommit(false); // Start transaction

            // 1. Update Header
            if (!updateHeader(con, noS4S, dateCreate, time, idTelly, noSPK, noSPKasal,
                    idGrade, idJenisKayu, idFJProfile, isReject, isLembur,
                    idUOMTblLebar, idUOMPanjang, remark, idWarna, idLokasi)) {
                con.rollback();
                System.err.println("[DB_ERROR] Gagal update S4S Header untuk NoS4S=" + noS4S);
                return false;
            }

            // 2. Delete existing detail dan Insert ulang (Replace strategy)
            if (!replaceDetail(con, noS4S, dataList)) {
                con.rollback();
                System.err.println("[DB_ERROR] Gagal update S4S Detail untuk NoS4S=" + noS4S);
                return false;
            }

            con.commit(); // Commit all changes
            System.out.println("[SUCCESS] Data S4S berhasil diupdate untuk NoS4S=" + noS4S);
            return true;

        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException rollbackEx) {
                    System.err.println("[DB_ERROR] Gagal rollback transaction:");
                    rollbackEx.printStackTrace();
                }
            }
            System.err.println("[DB_ERROR] Update transaction gagal untuk NoS4S=" + noS4S + ":");
            e.printStackTrace();
            return false;
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
    }

    /**
     * Update S4S Header data
     */
    private static boolean updateHeader(Connection con,
                                        String noS4S, String dateCreate, String time, String idTelly,
                                        String noSPK, String noSPKasal, int idGrade, int idJenisKayu,
                                        String idFJProfile, int isReject, int isLembur,
                                        int idUOMTblLebar, int idUOMPanjang, String remark,
                                        int idWarna, String idLokasi) throws SQLException {

        String query = "UPDATE dbo.S4S_h SET " +
                "DateCreate=?, Jam=?, IdOrgTelly=?, NoSPK=?, NoSPKAsal=?, IdGrade=?, " +
                "IdFJProfile=?, IdJenisKayu=?, IsReject=?, IsLembur=?, IdUOMTblLebar=?, IdUOMPanjang=?, " +
                "Remark=?, IdWarna=?, IdLokasi=? " +
                "WHERE NoS4S=?";

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

            // Handle null values for idWarna
            if (idWarna == 0) {
                ps.setNull(14, java.sql.Types.INTEGER);
            } else {
                ps.setInt(14, idWarna);
            }

            // Handle null values for idLokasi
            if (idLokasi.equals("PILIH") || idLokasi == null || idLokasi.trim().isEmpty()) {
                ps.setNull(15, java.sql.Types.VARCHAR);
            } else {
                ps.setString(15, idLokasi);
            }

            ps.setString(16, noS4S); // WHERE condition

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                System.err.println("[DB_ERROR] No rows updated for NoS4S=" + noS4S + ". Data mungkin tidak exist.");
                return false;
            }
            return true;
        }
    }

    /**
     * Replace S4S Detail data (Delete existing + Insert new)
     */
    private static boolean replaceDetail(Connection con, String noS4S, List<LabelDetailData> dataList) throws SQLException {
        // 1. Delete existing detail
        String deleteQuery = "DELETE FROM dbo.S4S_d WHERE NoS4S = ?";
        try (PreparedStatement ps = con.prepareStatement(deleteQuery)) {
            ps.setString(1, noS4S);
            ps.executeUpdate(); // Don't check result, might be 0 if no existing detail
        }

        // 2. Insert new detail if provided
        if (dataList != null && !dataList.isEmpty()) {
            return insertDetail(con, noS4S, dataList);
        }

        return true; // Success even if no new detail to insert
    }

    /**
     * Upsert ProduksiOutput (Update if exists, Insert if not)
     */
    private static boolean upsertProduksiOutput(Connection con, String noProduksi, String noS4S) throws SQLException {
        String table;

        // Determine table based on NoProduksi prefix
        if (noProduksi.startsWith("RA")) {
            table = "dbo.S4SProduksiOutput";
        } else if (noProduksi.startsWith("VA")) {
            table = "dbo.CCAkhirProduksiOutputS4S";
        } else {
            System.err.println("[DB_ERROR] Prefix NoProduksi tidak valid: " + noProduksi);
            return false;
        }

        // Try update first
        String updateQuery = "UPDATE " + table + " SET NoProduksi=? WHERE NoS4S=?";
        try (PreparedStatement ps = con.prepareStatement(updateQuery)) {
            ps.setString(1, noProduksi);
            ps.setString(2, noS4S);

            if (ps.executeUpdate() > 0) {
                return true; // Update successful
            }
        }

        // If update failed (no existing row), do insert
        String insertQuery = "INSERT INTO " + table + " (NoProduksi, NoS4S) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(insertQuery)) {
            ps.setString(1, noProduksi);
            ps.setString(2, noS4S);

            return ps.executeUpdate() > 0;
        }
    }



    /**
     * Delete S4S data beserta semua relasinya
     * @param noS4S Nomor S4S yang akan dihapus
     * @return true jika berhasil, false jika gagal
     */
    public static boolean deleteData(String noS4S) {
        final String TAG = "DeleteData";
        Connection con = null;
        try {
            con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
            con.setAutoCommit(false);

            // Delete ProduksiOutput
            if (!deleteProduksiOutput(con, noS4S)) {
                Log.w(TAG, "Gagal delete ProduksiOutput untuk NoS4S=" + noS4S);
            }

            // Delete BongkarSusun
            if (!deleteBongkarSusun(con, noS4S)) {
                Log.w(TAG, "Gagal delete BongkarSusun untuk NoS4S=" + noS4S);
            }

            // Delete S4S Detail
            if (!deleteDetail(con, noS4S)) {
                con.rollback();
                Log.e(TAG, "Gagal delete S4S Detail untuk NoS4S=" + noS4S);
                return false;
            }

            // Delete S4S Header
            if (!deleteHeader(con, noS4S)) {
                con.rollback();
                Log.e(TAG, "Gagal delete S4S Header untuk NoS4S=" + noS4S);
                return false;
            }

            con.commit();
            Log.i(TAG, "Data S4S berhasil dihapus untuk NoS4S=" + noS4S);
            return true;

        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException rollbackEx) {
                    Log.e(TAG, "Gagal rollback transaction", rollbackEx);
                }
            }
            Log.e(TAG, "Transaction gagal untuk delete NoS4S=" + noS4S, e);
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
     * Delete S4S Header
     */
    private static boolean deleteHeader(Connection con, String noS4S) throws SQLException {
        String query = "DELETE FROM dbo.S4S_h WHERE NoS4S = ?";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, noS4S);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                System.err.println("[DB_ERROR] Data S4S Header tidak ditemukan untuk NoS4S=" + noS4S);
                return false;
            }
            return true;
        }
    }

    /**
     * Delete S4S Detail
     */
    private static boolean deleteDetail(Connection con, String noS4S) throws SQLException {
        String query = "DELETE FROM dbo.S4S_d WHERE NoS4S = ?";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, noS4S);

            int rowsAffected = ps.executeUpdate();
            System.out.println("[INFO] " + rowsAffected + " detail records dihapus untuk NoS4S=" + noS4S);
            return true; // Return true even if no detail records (bisa saja memang kosong)
        }
    }

    /**
     * Delete ProduksiOutput dari kedua tabel
     */
    private static boolean deleteProduksiOutput(Connection con, String noS4S) throws SQLException {
        boolean deletedFromS4S = false;
        boolean deletedFromCCAkhir = false;

        // Delete dari S4SProduksiOutput
        String query1 = "DELETE FROM dbo.S4SProduksiOutput WHERE NoS4S = ?";
        try (PreparedStatement ps = con.prepareStatement(query1)) {
            ps.setString(1, noS4S);
            int rows1 = ps.executeUpdate();
            if (rows1 > 0) {
                deletedFromS4S = true;
            }
            System.out.println("[INFO] " + rows1 + " records dihapus dari S4SProduksiOutput untuk NoS4S=" + noS4S);
        } catch (SQLException e) {
            System.err.println("[WARN] Error delete dari S4SProduksiOutput: " + e.getMessage());
        }

        // Delete dari CCAkhirProduksiOutputS4S
        String query2 = "DELETE FROM dbo.CCAkhirProduksiOutputS4S WHERE NoS4S = ?";
        try (PreparedStatement ps = con.prepareStatement(query2)) {
            ps.setString(1, noS4S);
            int rows2 = ps.executeUpdate();
            if (rows2 > 0) {
                deletedFromCCAkhir = true;
            }
            System.out.println("[INFO] " + rows2 + " records dihapus dari CCAkhirProduksiOutputS4S untuk NoS4S=" + noS4S);
        } catch (SQLException e) {
            System.err.println("[WARN] Error delete dari CCAkhirProduksiOutputS4S: " + e.getMessage());
        }

        // return true kalau minimal 1 query berhasil
        return deletedFromS4S || deletedFromCCAkhir;
    }


    /**
     * Delete BongkarSusun
     */
    private static boolean deleteBongkarSusun(Connection con, String noS4S) throws SQLException {
        String query = "DELETE FROM dbo.BongkarSusunOutputS4S WHERE NoS4S = ?";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, noS4S);

            int rowsAffected = ps.executeUpdate();
            System.out.println("[INFO] " + rowsAffected + " BongkarSusun records dihapus untuk NoS4S=" + noS4S);
            return true; // Return true even if no records found
        }
    }

    /**
     * Check apakah data S4S exists sebelum delete
     */
    public static boolean isDataExists(String noS4S) {
        Connection con = null;
        try {
            con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());

            String query = "SELECT COUNT(*) FROM dbo.S4S_h WHERE NoS4S = ?";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setString(1, noS4S);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1) > 0;
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("[DB_ERROR] Error checking data existence for NoS4S=" + noS4S + ":");
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
