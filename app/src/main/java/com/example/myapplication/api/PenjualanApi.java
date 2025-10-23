package com.example.myapplication.api;

import android.util.Log;

import com.example.myapplication.config.DatabaseConfig;
import com.example.myapplication.model.BJJualData;
import com.example.myapplication.model.PenjualanData;
import com.example.myapplication.utils.DateTimeUtils;

import net.sourceforge.jtds.jdbc.DateTime;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class PenjualanApi {

    public static List<PenjualanData> getPenjualanData(String tableName) {
        List<PenjualanData> penjualanDataList = new ArrayList<>();

        String query =
                "SELECT TOP 50 h.NoJual, h.TglJual, h.IdBuyer, b.Buyer, " +
                        "h.Keterangan, h.NoSJ, h.NoPlat, h.IdJenisKendaraan, jk.Model " +
                        "FROM " + tableName + " h " +
                        "LEFT JOIN dbo.MstBuyer b ON h.IdBuyer = b.IdBuyer " +
                        "LEFT JOIN dbo.MstJenisKendaraan jk ON h.IdJenisKendaraan = jk.IdJenisKendaraan " +
                        "ORDER BY h.NoJual DESC";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String keterangan = rs.getString("Keterangan");
                if (rs.wasNull()) keterangan = null;

                String noSJ = rs.getString("NoSJ");
                if (rs.wasNull()) noSJ = null;

                String noPlat = rs.getString("NoPlat");
                if (rs.wasNull()) noPlat = null;

                PenjualanData data = new PenjualanData(
                        rs.getString("NoJual"),
                        rs.getString("TglJual"),
                        rs.getInt("IdBuyer"),
                        rs.getString("Buyer"),    // ambil nama Buyer
                        keterangan,
                        noSJ,
                        noPlat,
                        rs.getInt("IdJenisKendaraan"),
                        rs.getString("Model")     // ambil Model kendaraan
                );
                penjualanDataList.add(data);
            }
        } catch (SQLException e) {
            Log.e("DB_FETCH", "Error fetching Penjualan data: " + e.getMessage(), e);
        }

        return penjualanDataList;
    }



    public static boolean isDateValidInPenjualan(String noJual, String mainTable, String result, String tableNameH, String columnName) {
        String queryH = "SELECT DateCreate FROM " + tableNameH + " WHERE " + columnName + " = ?";
        String queryProduksi = "SELECT TglJual FROM " + mainTable + " WHERE NoJual = ?";
        java.sql.Date dateCreated = null;
        java.sql.Date produksiInputDate = null;

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl())) {
            // Ambil DateCreated dari tableNameH
            try (PreparedStatement pstmtH = con.prepareStatement(queryH)) {
                pstmtH.setString(1, result);
                try (ResultSet rsH = pstmtH.executeQuery()) {
                    if (rsH.next()) {
                        dateCreated = rsH.getDate("DateCreate");
                    }
                }
            }

            // Ambil Tanggal dari ProduksiInput
            try (PreparedStatement pstmtProduksi = con.prepareStatement(queryProduksi)) {
                pstmtProduksi.setString(1, noJual);
                try (ResultSet rsProduksi = pstmtProduksi.executeQuery()) {
                    if (rsProduksi.next()) {
                        produksiInputDate = rsProduksi.getDate("TglJual");
                    }
                }
            }

            // Bandingkan tanggal
            if (dateCreated != null && produksiInputDate != null) {
                return produksiInputDate.compareTo(dateCreated) >= 0;
            }
        } catch (SQLException e) {
            Log.e("Database Check Error", "Error comparing dates: " + e.getMessage());
        }
        return false; // Default jika tidak valid
    }


    public static List<String> getNoSTByNoJual(String noJual, String tableName) {
        List<String> noSTList = new ArrayList<>();

        String query = "SELECT NoST FROM " + tableName + " WHERE NoJual = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, noJual);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String noST = rs.getString("NoST");
                    noSTList.add(noST);
                    Log.d("Database Data", "NoST: " + noST);
                }
            }

        } catch (SQLException e) {
            Log.e("Database Fetch Error", "Error fetching NoST: " + e.getMessage());
        }

        return noSTList;
    }


    public static List<String> getNoS4SByNoJual(String noJual, String tableName) {
        List<String> noS4SList = new ArrayList<>();

        String query = "SELECT NoS4S FROM " + tableName + " WHERE NoJual = ?";
        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, noJual); // Set nilai parameter
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String noS4S = rs.getString("NoS4S");
                    noS4SList.add(noS4S);

                    Log.d("Database Data", "NoS4S: " + noS4S);
                }
            }
        } catch (SQLException e) {
            Log.e("Database Fetch Error", "Error fetching NoS4S: " + e.getMessage());
        }
        return noS4SList;
    }


    public static List<String> getNoFJByNoJual(String noJual, String tableName) {
        List<String> noFJList = new ArrayList<>();

        String query = "SELECT NoFJ FROM " + tableName + " WHERE NoJual = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, noJual);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String noFJ = rs.getString("NoFJ");
                    noFJList.add(noFJ);
                    Log.d("Database Data", "NoFJ: " + noFJ);
                }
            }

        } catch (SQLException e) {
            Log.e("Database Fetch Error", "Error fetching NoFJ: " + e.getMessage());
        }

        return noFJList;
    }


    public static List<String> getNoMouldingByNoJual(String noJual, String tableName) {
        List<String> noMouldingList = new ArrayList<>();

        // Menggunakan parameterized query
        String query = "SELECT NoMoulding FROM " + tableName + " WHERE NoJual = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, noJual);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String noMoulding = rs.getString("NoMoulding");
                    noMouldingList.add(noMoulding);
                    Log.d("Database Data", "NoMoulding: " + noMoulding);
                }
            }

        } catch (SQLException e) {
            Log.e("Database Fetch Error", "Error fetching NoMoulding: " + e.getMessage());
        }

        return noMouldingList;
    }


    public static List<String> getNoLaminatingByNoJual(String noJual, String tableName) {
        List<String> noLaminatingList = new ArrayList<>();

        // Menggunakan parameterized query
        String query = "SELECT NoLaminating FROM " + tableName + " WHERE NoJual = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement pstmt = con.prepareStatement(query)) {

            // Set nilai untuk parameter "?"
            pstmt.setString(1, noJual);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String noLaminating = rs.getString("NoLaminating");
                    noLaminatingList.add(noLaminating);
                    Log.d("Database Data", "NoLaminating: " + noLaminating);
                }
            }

        } catch (SQLException e) {
            Log.e("Database Fetch Error", "Error fetching NoLaminating: " + e.getMessage());
        }

        return noLaminatingList;
    }


    public static List<String> getNoCCByNoJual(String noJual, String tableName) {
        List<String> noCCList = new ArrayList<>();

        // Query SQL dinamis berdasarkan nama tabel
        String query = "SELECT NoCCAkhir FROM " + tableName + " WHERE NoJual = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement pstmt = con.prepareStatement(query)) {

            // Set parameter untuk query
            pstmt.setString(1, noJual);

            // Eksekusi query
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String noCC = rs.getString("NoCCAkhir");
                    noCCList.add(noCC);
                    Log.d("Database Data", "NoCC: " + noCC);
                }
            }
        } catch (SQLException e) {
            Log.e("Database Fetch Error", "Error fetching NoCC: " + e.getMessage());
        }

        // Debugging jumlah data yang ditemukan
        Log.d("NoCC List Size", "Number of NoCC fetched: " + noCCList.size());

        return noCCList;
    }


    public static List<String> getNoSandingByNoJual(String noJual, String tableName) {
        List<String> noSandingList = new ArrayList<>();

        // Menggunakan parameterized query
        String query = "SELECT NoSanding FROM " + tableName + " WHERE NoJual = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement pstmt = con.prepareStatement(query)) {

            // Set nilai untuk parameter "?"
            pstmt.setString(1, noJual);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String noSanding = rs.getString("NoSanding");
                    noSandingList.add(noSanding);
                    Log.d("Database Data", "NoSanding: " + noSanding);
                }
            }

        } catch (SQLException e) {
            Log.e("Database Fetch Error", "Error fetching NoSanding: " + e.getMessage());
        }

        return noSandingList;
    }


    //SAVE IN PENJUALAN
    public static void saveNoSTInPenjualan(String noJual, String tgl, List<String> noSTList, String tableName) {
        if (noSTList == null || noSTList.isEmpty()) {
            Log.e("SaveError", "List NoST kosong, tidak ada data untuk disimpan.");
            return;
        }

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement insertStmt = con.prepareStatement("INSERT INTO " + tableName + " (NoJual, NoST) VALUES (?, ?)");
             PreparedStatement updateStmt = con.prepareStatement("UPDATE ST_h SET DateUsage = ? WHERE NoST = ?")) {

            // Insert Data ke PenjualanST
            for (String noST : noSTList) {
                insertStmt.setString(1, noJual);
                insertStmt.setString(2, noST);
                insertStmt.addBatch();
            }
            insertStmt.executeBatch();

            // Update DataUsage di ST_h
            for (String noST : noSTList) {
                updateStmt.setString(1, tgl);
                updateStmt.setString(2, noST);
                updateStmt.addBatch();
            }
            updateStmt.executeBatch();

            Log.d("SaveSuccess", "Data NoST berhasil disimpan dan DateUsage diperbarui.");

        } catch (SQLException e) {
            Log.e("SaveError", "Error saat menyimpan data NoST: " + e.getMessage());
        }
    }


    public static void saveNoS4SInPenjualan(String noJual, String tgl, List<String> noS4SList, String tableName) {
        if (noS4SList == null || noS4SList.isEmpty()) {
            Log.e("SaveError", "List NoS4S kosong, tidak ada data untuk disimpan.");
            return;
        }

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement insertStmt = con.prepareStatement("INSERT INTO " + tableName + " (NoJual, NoS4S) VALUES (?, ?)");
             PreparedStatement updateStmt = con.prepareStatement("UPDATE S4S_h SET DateUsage = ? WHERE NoS4S = ?")) {

            // Insert Data ke S4SProduksiInputST
            for (String noS4S : noS4SList) {
                insertStmt.setString(1, noJual);
                insertStmt.setString(2, noS4S);
                insertStmt.addBatch();
            }
            insertStmt.executeBatch();

            // Update DataUsage di S4S_h
            for (String noS4S : noS4SList) {
                updateStmt.setString(1, tgl);
                updateStmt.setString(2, noS4S);
                updateStmt.addBatch();
            }
            updateStmt.executeBatch();

            Log.d("SaveSuccess", "Data NoS4S berhasil disimpan dan DateUsage diperbarui.");

        } catch (SQLException e) {
            Log.e("SaveError", "Error saat menyimpan data NoS4S: " + e.getMessage());
        }
    }


    public static void saveNoFJInPenjualan(String noJual, String tgl, List<String> noFJList, String tableName) {
        if (noFJList == null || noFJList.isEmpty()) {
            Log.e("SaveError", "List NoFJ kosong, tidak ada data untuk disimpan.");
            return;
        }

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement insertStmt = con.prepareStatement("INSERT INTO " + tableName + " (NoJual, NoFJ) VALUES (?, ?)");
             PreparedStatement updateStmt = con.prepareStatement("UPDATE FJ_h SET DateUsage = ? WHERE NoFJ = ?")) {

            // Insert Data ke S4SProduksiInputFJ
            for (String noFJ : noFJList) {
                insertStmt.setString(1, noJual);
                insertStmt.setString(2, noFJ);
                insertStmt.addBatch();
            }
            insertStmt.executeBatch();

            // Update DataUsage di FJ_h
            for (String noFJ : noFJList) {
                updateStmt.setString(1, tgl);
                updateStmt.setString(2, noFJ);
                updateStmt.addBatch();
            }
            updateStmt.executeBatch();

            Log.d("SaveSuccess", "Data NoFJ berhasil disimpan dan DateUsage diperbarui.");

        } catch (SQLException e) {
            Log.e("SaveError", "Error saat menyimpan data NoFJ: " + e.getMessage());
        }
    }

    public static void saveNoMouldingInPenjualan(String noJual, String tgl, List<String> noMouldingList, String tableName) {
        if (noMouldingList == null || noMouldingList.isEmpty()) {
            Log.e("SaveError", "List NoMoulding kosong, tidak ada data untuk disimpan.");
            return;
        }

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement insertStmt = con.prepareStatement("INSERT INTO " + tableName + " (NoJual, NoMoulding) VALUES (?, ?)");
             PreparedStatement updateStmt = con.prepareStatement("UPDATE Moulding_h SET DateUsage = ? WHERE NoMoulding = ?")) {

            // Insert Data ke S4SProduksiInputMoulding
            for (String noMoulding : noMouldingList) {
                insertStmt.setString(1, noJual);
                insertStmt.setString(2, noMoulding);
                insertStmt.addBatch();
            }
            insertStmt.executeBatch();

            // Update DataUsage di Moulding_h
            for (String noMoulding : noMouldingList) {
                updateStmt.setString(1, tgl);
                updateStmt.setString(2, noMoulding);
                updateStmt.addBatch();
            }
            updateStmt.executeBatch();

            Log.d("SaveSuccess", "Data NoMoulding berhasil disimpan dan DateUsage diperbarui.");

        } catch (SQLException e) {
            Log.e("SaveError", "Error saat menyimpan data NoMoulding: " + e.getMessage());
        }
    }


    public static void saveNoLaminatingInPenjualan(String noJual, String tgl, List<String> noLaminatingList, String tableName) {
        if (noLaminatingList == null || noLaminatingList.isEmpty()) {
            Log.e("SaveError", "List NoLaminating kosong, tidak ada data untuk disimpan.");
            return;
        }

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement insertStmt = con.prepareStatement("INSERT INTO " + tableName + " (NoJual, NoLaminating) VALUES (?, ?)");
             PreparedStatement updateStmt = con.prepareStatement("UPDATE Laminating_h SET DateUsage = ? WHERE NoLaminating = ?")) {

            // Insert Data ke S4SProduksiInputCCAkhir
            for (String noLaminating : noLaminatingList) {
                insertStmt.setString(1, noJual);
                insertStmt.setString(2, noLaminating);
                insertStmt.addBatch();
            }
            insertStmt.executeBatch();

            // Update DataUsage di CCAkhir_h
            for (String noLaminating : noLaminatingList) {
                updateStmt.setString(1, tgl);
                updateStmt.setString(2, noLaminating);
                updateStmt.addBatch();
            }
            updateStmt.executeBatch();

            Log.d("SaveSuccess", "Data NoLaminating berhasil disimpan dan DateUsage diperbarui.");

        } catch (SQLException e) {
            Log.e("SaveError", "Error saat menyimpan data NoLaminating: " + e.getMessage());
        }
    }


    public static void saveNoCCInPenjualan(String noJual, String tgl, List<String> noCCList, String tableName) {
        if (noCCList == null || noCCList.isEmpty()) {
            Log.e("SaveError", "List NoCCAkhir kosong, tidak ada data untuk disimpan.");
            return;
        }

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement insertStmt = con.prepareStatement("INSERT INTO " + tableName + " (NoJual, NoCCAkhir) VALUES (?, ?)");
             PreparedStatement updateStmt = con.prepareStatement("UPDATE CCAkhir_h SET DateUsage = ? WHERE NoCCAkhir = ?")) {

            // Insert Data ke S4SProduksiInputCCAkhir
            for (String noCCAkhir : noCCList) {
                insertStmt.setString(1, noJual);
                insertStmt.setString(2, noCCAkhir);
                insertStmt.addBatch();
            }
            insertStmt.executeBatch();

            // Update DataUsage di CCAkhir_h
            for (String noCCAkhir : noCCList) {
                updateStmt.setString(1, tgl);
                updateStmt.setString(2, noCCAkhir);
                updateStmt.addBatch();
            }
            updateStmt.executeBatch();

            Log.d("SaveSuccess", "Data NoCCAkhir berhasil disimpan dan DateUsage diperbarui.");

        } catch (SQLException e) {
            Log.e("SaveError", "Error saat menyimpan data NoCCAkhir: " + e.getMessage());
        }
    }


    public static void saveNoSandingInPenjualan(String noJual, String tgl, List<String> noSandingList, String tableName) {
        if (noSandingList == null || noSandingList.isEmpty()) {
            Log.e("SaveError", "List NoSanding kosong, tidak ada data untuk disimpan.");
            return;
        }

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement insertStmt = con.prepareStatement("INSERT INTO " + tableName + " (NoJual, NoSanding) VALUES (?, ?)");
             PreparedStatement updateStmt = con.prepareStatement("UPDATE Sanding_h SET DateUsage = ? WHERE NoSanding = ?")) {

            for (String noSanding : noSandingList) {
                insertStmt.setString(1, noJual);
                insertStmt.setString(2, noSanding);
                insertStmt.addBatch();
            }
            insertStmt.executeBatch();

            for (String noSanding : noSandingList) {
                updateStmt.setString(1, tgl);
                updateStmt.setString(2, noSanding);
                updateStmt.addBatch();
            }
            updateStmt.executeBatch();

            Log.d("SaveSuccess", "Data NoSanding berhasil disimpan dan DateUsage diperbarui.");

        } catch (SQLException e) {
            Log.e("SaveError", "Error saat menyimpan data NoSanding: " + e.getMessage());
        }
    }


    private static String generateNewNoJual(Connection con, String tableName) throws SQLException {
        // Cari angka maksimum dari 6 digit terakhir utk prefix G.
        // Filter panjang = 8 char (G. + 6 digit) dan konversi aman pakai TRY_CONVERT.
        String sql =
                "SELECT ISNULL(MAX(TRY_CONVERT(INT, RIGHT(NoJual, 6))), 0) AS MaxNum " +
                        "FROM " + tableName + " WITH (UPDLOCK, HOLDLOCK) " +
                        "WHERE NoJual LIKE 'G.%' AND LEN(NoJual) = 8";

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            int last = 0;
            if (rs.next()) last = rs.getInt("MaxNum");

            if (last >= 999_999) {
                throw new SQLException("NoJual overflow (>= G.999999)");
            }
            return "G." + String.format("%06d", last + 1);
        }
    }


    public static String insertPenjualanHeaderWithGeneratedNoJual(String tglJualISO,
                                                                  int idBuyer,
                                                                  String keterangan,
                                                                  String noSJ,
                                                                  String noPlat,
                                                                  int idJenisKendaraan) {
        if (idBuyer <= 0 || idJenisKendaraan <= 0) {
            System.err.println("[VALIDATION] idBuyer & idJenisKendaraan harus > 0");
            return null;
        }

        final String tableName = "dbo.Penjualan_h";
        final String insertSql =
                "INSERT INTO " + tableName + " (NoJual, TglJual, IdBuyer, Keterangan, NoSJ, NoPlat, IdJenisKendaraan) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";

        // (Opsional) jaga panjang string sesuai schema kolom
        if (keterangan != null && keterangan.length() > 255) keterangan = keterangan.substring(0, 255);
        if (noSJ != null && noSJ.length() > 50)            noSJ       = noSJ.substring(0, 50);
        if (noPlat != null && noPlat.length() > 20)        noPlat     = noPlat.substring(0, 20);

        int attempts = 0;
        while (attempts < 3) {
            attempts++;
            Connection con = null;
            try {
                con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
                con.setAutoCommit(false);
                con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

                // 1) Generate nomor dalam transaksi (terkunci)
                String newNoJual = generateNewNoJual(con, tableName);

                // 2) Insert
                try (PreparedStatement ps = con.prepareStatement(insertSql)) {
                    ps.setString(1, newNoJual);
                    ps.setString(2, DateTimeUtils.formatToDatabaseDate(tglJualISO));
                    ps.setInt(3, idBuyer);

                    if (keterangan == null || keterangan.trim().isEmpty()) ps.setNull(4, Types.VARCHAR);
                    else ps.setString(4, keterangan.trim());

                    if (noSJ == null || noSJ.trim().isEmpty()) ps.setNull(5, Types.VARCHAR);
                    else ps.setString(5, noSJ.trim());

                    if (noPlat == null || noPlat.trim().isEmpty()) ps.setNull(6, Types.VARCHAR);
                    else ps.setString(6, noPlat.trim());

                    ps.setInt(7, idJenisKendaraan);

                    ps.executeUpdate();
                }

                con.commit();
                return newNoJual;

            } catch (SQLException e) {
                System.err.println("[DB_ERROR] Insert Penjualan_h attempt " + attempts + " gagal: " +
                        e.getMessage() + " (SQLState=" + e.getSQLState() +
                        ", Code=" + e.getErrorCode() + ")");
                try { if (con != null) con.rollback(); } catch (SQLException ignore) {}

                return null;

            } catch (Exception e) {
                System.err.println("[APP_ERROR] Insert Penjualan_h fatal: " + e.getMessage());
                try { if (con != null) con.rollback(); } catch (SQLException ignore) {}
                return null;

            } finally {
                if (con != null) try { con.close(); } catch (SQLException ignore) {}
            }
        }

        System.err.println("[DB_ERROR] Gagal insert setelah 3x retry (duplikasi terus)");
        return null;
    }


    public static boolean updatePenjualanHeader(String noJual, String tglJualISO, int idBuyer,
                                                String keterangan, String noSJ, String noPlat,
                                                int idJenisKendaraan) {
        String sql = "UPDATE dbo.Penjualan_h " +
                "SET TglJual=?, IdBuyer=?, Keterangan=?, NoSJ=?, NoPlat=?, IdJenisKendaraan=? " +
                "WHERE NoJual=?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, DateTimeUtils.formatToDatabaseDate(tglJualISO));
            ps.setInt(2, idBuyer);
            if (keterangan == null) ps.setNull(3, Types.VARCHAR); else ps.setString(3, keterangan);
            if (noSJ == null) ps.setNull(4, Types.VARCHAR); else ps.setString(4, noSJ);
            if (noPlat == null) ps.setNull(5, Types.VARCHAR); else ps.setString(5, noPlat);
            ps.setInt(6, idJenisKendaraan);
            ps.setString(7, noJual);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[DB_ERROR] Update gagal: " + e.getMessage());
            return false;
        }
    }


    public static List<BJJualData> getBJJualData() {
        List<BJJualData> bjJualDataList = new ArrayList<>();

        String query = "SELECT TOP 50 NoBJJual, TglJual, NoSPK, Keterangan " +
                "FROM dbo.BJJual_h " +
                "ORDER BY NoBJJual DESC";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String keterangan = rs.getString("Keterangan");
                if (rs.wasNull()) keterangan = null;

                BJJualData data = new BJJualData(
                        rs.getString("NoBJJual"),
                        rs.getString("TglJual"),
                        rs.getString("NoSPK"),
                        keterangan
                );
                bjJualDataList.add(data);
            }
        } catch (SQLException e) {
            Log.e("DB_FETCH", "Error fetching BJJual data: " + e.getMessage(), e);
        }

        return bjJualDataList;
    }


    public static List<String> getNoPackingByBJJual(String noBjJual) {
        List<String> noPackingList = new ArrayList<>();

        // Menggunakan parameterized query
        String query = "SELECT NoBJ FROM BJJual_d WHERE NoBJJual = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement pstmt = con.prepareStatement(query)) {

            // Set nilai untuk parameter "?"
            pstmt.setString(1, noBjJual);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String noPacking = rs.getString("NoBJ");
                    noPackingList.add(noPacking);
                    Log.d("Database Data", "NoPacking: " + noPacking);
                }
            }

        } catch (SQLException e) {
            Log.e("Database Fetch Error", "Error fetching NoPacking: " + e.getMessage());
        }

        return noPackingList;
    }


    // -- Helper: generate nomor J.xxxxxx yang aman di dalam transaksi
    private static String generateNewNoBJJual(Connection con) throws SQLException {
        final String sql =
                "SELECT ISNULL(MAX(CASE WHEN ISNUMERIC(RIGHT(NoBJJual, 6)) = 1 " +
                        "THEN CONVERT(INT, RIGHT(NoBJJual, 6)) ELSE 0 END), 0) AS MaxNum " +
                        "FROM dbo.BJJual_h WITH (UPDLOCK, HOLDLOCK) " +
                        "WHERE NoBJJual LIKE 'J.%' AND LEN(NoBJJual) = 8";

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            int last = 0;
            if (rs.next()) last = rs.getInt("MaxNum");

            if (last >= 999_999) {
                throw new SQLException("NoBJJual overflow (>= J.999999)");
            }
            return "J." + String.format("%06d", last + 1);
        }
    }

    /**
     * Insert header BJ dengan auto-generate NoBJJual.
     * @param tglJualISO   format "yyyy-MM-dd" dari UI (DateTimeUtils.getCurrentDate())
     * @param noSPK        dari spinner; kalau null/""/"-" akan diset NULL di DB
     * @param keterangan   optional; null/"" -> NULL
     * @return String NoBJJual baru (mis. "J.000123") jika sukses, atau null jika gagal
     */
    public static String insertBJJualHeaderWithGeneratedNo(String tglJualISO,
                                                           String noSPK,
                                                           String keterangan) {
        final String tableName = "dbo.BJJual_h";
        final String insertSql =
                "INSERT INTO " + tableName + " (NoBJJual, TglJual, NoSPK, Keterangan) " +
                        "VALUES (?, ?, ?, ?)";

        // (Opsional) batasi panjang teks jika kolom punya limit (ubah sesuai schema kamu)
        if (noSPK != null && noSPK.length() > 50)       noSPK = noSPK.substring(0, 50);
        if (keterangan != null && keterangan.length() > 255) keterangan = keterangan.substring(0, 255);

        int attempts = 0;
        while (attempts < 3) {
            attempts++;
            Connection con = null;
            try {
                con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
                con.setAutoCommit(false);
                con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

                // 1) Generate nomor dalam transaksi
                String newNo = generateNewNoBJJual(con);

                // 2) Insert
                try (PreparedStatement ps = con.prepareStatement(insertSql)) {
                    ps.setString(1, newNo);
                    ps.setString(2, DateTimeUtils.formatToDatabaseDate(tglJualISO));

                    // NoSPK: treat "-", "", null as NULL
                    if (noSPK == null || noSPK.trim().isEmpty() || "-".equals(noSPK.trim())) {
                        ps.setNull(3, Types.VARCHAR);
                    } else {
                        ps.setString(3, noSPK.trim());
                    }

                    // Keterangan: optional
                    if (keterangan == null || keterangan.trim().isEmpty()) {
                        ps.setNull(4, Types.VARCHAR);
                    } else {
                        ps.setString(4, keterangan.trim());
                    }

                    ps.executeUpdate();
                }

                con.commit();
                return newNo;

            } catch (SQLException e) {
                System.err.println("[DB_ERROR] Insert BJJual_h attempt " + attempts + " gagal: " +
                        e.getMessage() + " (SQLState=" + e.getSQLState() +
                        ", Code=" + e.getErrorCode() + ")");
                try { if (con != null) con.rollback(); } catch (SQLException ignore) {}

                // Kalau error karena unique/dupe key saat race, coba retry
                // selain itu biasanya langsung return null juga oke
                if (!"23000".equals(e.getSQLState())) { // SQLState 23000 = integrity constraint violation
                    return null;
                }

            } catch (Exception e) {
                System.err.println("[APP_ERROR] Insert BJJual_h fatal: " + e.getMessage());
                try { if (con != null) con.rollback(); } catch (SQLException ignore) {}
                return null;

            } finally {
                if (con != null) try { con.close(); } catch (SQLException ignore) {}
            }
        }

        System.err.println("[DB_ERROR] Gagal insert BJJual_h setelah 3x retry");
        return null;
    }


    /**
     * Update header BJ berdasarkan NoBJJual (primary key).
     * @param noBJJual   wajib (mis. "J.000123")
     * @param tglJualISO format "yyyy-MM-dd" dari UI
     * @param noSPK      optional; null / "" / "-" -> set NULL
     * @param keterangan optional; null / ""       -> set NULL
     * @return true jika tepat 1 baris ter-update, selain itu false
     */
    public static boolean updateBJJualHeader(String noBJJual,
                                             String tglJualISO,
                                             String noSPK,
                                             String keterangan) {
        if (noBJJual == null || noBJJual.trim().isEmpty()) {
            System.err.println("[VALIDATION] noBJJual wajib diisi");
            return false;
        }

        final String tableName = "dbo.BJJual_h";
        final String sql =
                "UPDATE " + tableName + " " +
                        "SET TglJual = ?, NoSPK = ?, Keterangan = ? " +
                        "WHERE NoBJJual = ?";

        // (opsional) jaga panjang string jika kolom ada limit
        if (noSPK != null && noSPK.length() > 50)             noSPK      = noSPK.substring(0, 50);
        if (keterangan != null && keterangan.length() > 255)  keterangan = keterangan.substring(0, 255);

        Connection con = null;
        try {
            con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
            con.setAutoCommit(false);
            con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            int rows;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                // TglJual (wajib)
                ps.setString(1, DateTimeUtils.formatToDatabaseDate(tglJualISO));

                // NoSPK (optional)
                if (noSPK == null || noSPK.trim().isEmpty() || "-".equals(noSPK.trim())) {
                    ps.setNull(2, Types.VARCHAR);
                } else {
                    ps.setString(2, noSPK.trim());
                }

                // Keterangan (optional)
                if (keterangan == null || keterangan.trim().isEmpty()) {
                    ps.setNull(3, Types.VARCHAR);
                } else {
                    ps.setString(3, keterangan.trim());
                }

                // WHERE
                ps.setString(4, noBJJual.trim());

                rows = ps.executeUpdate();
            }

            con.commit();
            return rows == 1;

        } catch (SQLException e) {
            System.err.println("[DB_ERROR] Update BJJual_h gagal: " +
                    e.getMessage() + " (SQLState=" + e.getSQLState() +
                    ", Code=" + e.getErrorCode() + ")");
            try { if (con != null) con.rollback(); } catch (SQLException ignore) {}
            return false;

        } catch (Exception e) {
            System.err.println("[APP_ERROR] Update BJJual_h fatal: " + e.getMessage());
            try { if (con != null) con.rollback(); } catch (SQLException ignore) {}
            return false;

        } finally {
            if (con != null) try { con.close(); } catch (SQLException ignore) {}
        }
    }




}
