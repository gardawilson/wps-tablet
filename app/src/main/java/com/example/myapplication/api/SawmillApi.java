package com.example.myapplication.api;

import static com.example.myapplication.utils.DateTimeUtils.formatToDatabaseDate;

import android.util.Log;

import androidx.annotation.Nullable;

import com.example.myapplication.config.DatabaseConfig;
import com.example.myapplication.model.JenisKayuData;
import com.example.myapplication.model.KayuBulatData;
import com.example.myapplication.model.MejaData;
import com.example.myapplication.model.OperatorData;
import com.example.myapplication.model.PenerimaanSTSawmillData;
import com.example.myapplication.model.QcSawmillData;
import com.example.myapplication.model.QcSawmillDetailData;
import com.example.myapplication.model.SawmillData;
import com.example.myapplication.model.SawmillDetailData;
import com.example.myapplication.model.SpecialConditionData;
import com.example.myapplication.model.GradeKBData;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;


public class SawmillApi {

    public static String getTanggalTutupTransaksi() {
        String query = "SELECT TOP 1 PeriodHarian FROM MstTutupTransaksiHarian ORDER BY PeriodHarian DESC";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                // Ubah ke format yyyy-MM-dd (atau yang kamu butuhkan)
                Date date = rs.getDate("PeriodHarian");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                return sdf.format(date);
            }

        } catch (SQLException e) {
            Log.e("DB_ERROR", "Gagal mengambil tanggal tutup transaksi: " + e.getMessage());
        }

        return null; // return null jika gagal atau tidak ada data
    }


    // GETTER DAN SETTER DATA SAWMILL
    public static List<SawmillData> getSawmillData(int page, int pageSize, @Nullable String keyword) {
        List<SawmillData> sawmillDataList = new ArrayList<>();
        int offset = (page - 1) * pageSize;

        // Ambil data stok berdasarkan jenis kayu
        Map<String, Integer> stokMapUmum = getTotalStokTersediaPerKayuBulat();
        Map<String, Integer> stokMapRambung = getTotalStokTersediaPerKayuBulatRambung();

        // Query yang diperbaiki - pastikan WHERE clause di tempat yang benar
        String query =
                "WITH Filtered AS (" +
                        "  SELECT h.NoSTSawmill, h.Shift, h.TglSawmill, h.NoKayuBulat, h.NoMeja, ms.NamaMeja, " +
                        "         CASE WHEN op2.NamaOperator IS NULL THEN op1.NamaOperator ELSE op1.NamaOperator + '/' + op2.NamaOperator END AS Operator, " +
                        "         h.IdSawmillSpecialCondition, h.BalokTerpakai, h.JamKerja, h.JlhBatangRajang, " +
                        "         h.HourMeter, h.Remark, jk.Jenis AS NamaJenisKayu, " +
                        "         h.HourStart, h.HourEnd, h.IdOperator1, h.IdOperator2, " +
                        "         ISNULL((SELECT SUM(Berat) FROM STSawmill_dBalokTim d WHERE d.NoSTSawmill = h.NoSTSawmill), 0) AS BeratBalokTim " +
                        "  FROM STSawmill_h h " +
                        "  LEFT JOIN MstOperator op1 ON h.IdOperator1 = op1.IdOperator " +
                        "  LEFT JOIN MstOperator op2 ON h.IdOperator2 = op2.IdOperator " +
                        "  LEFT JOIN KayuBulat_h kb ON h.NoKayuBulat = kb.NoKayuBulat " +
                        "  LEFT JOIN MstJenisKayu jk ON kb.IdJenisKayu = jk.IdJenisKayu " +
                        "  LEFT JOIN MstMesinSawmill ms ON h.NoMeja = ms.NoMeja " +
                        // PERBAIKAN: Pastikan WHERE clause benar
                        (keyword != null && !keyword.isEmpty() ? "  WHERE LOWER(h.NoKayuBulat) LIKE LOWER(?) " : "") +
                        "), " +
                        "Ordered AS ( " +
                        "  SELECT ROW_NUMBER() OVER (ORDER BY NoSTSawmill DESC) AS RowNum, * FROM Filtered " +
                        ") " +
                        "SELECT * FROM Ordered WHERE RowNum > ? AND RowNum <= ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            int paramIndex = 1;

            if (keyword != null && !keyword.isEmpty()) {
                // PERBAIKAN: Gunakan % di kedua sisi untuk contains, atau hanya prefix
                stmt.setString(paramIndex++, "%" + keyword.toLowerCase() + "%"); // Contains search
                // ATAU untuk prefix search:
                // stmt.setString(paramIndex++, keyword.toLowerCase() + "%");
            }

            stmt.setInt(paramIndex++, offset);
            stmt.setInt(paramIndex, offset + pageSize);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // ... rest of your code remains the same
                String noSTSawmill = rs.getString("NoSTSawmill");
                String shift = rs.getString("Shift");
                String tglSawmill = rs.getString("TglSawmill");
                String noKayuBulat = rs.getString("NoKayuBulat");
                String noMeja = rs.getString("NoMeja");
                String operator = rs.getString("Operator");
                int idSpecial = rs.getInt("IdSawmillSpecialCondition");
                String balokTerpakai = rs.getString("BalokTerpakai");
                String jamKerja = rs.getString("JamKerja");
                int jlhBatangRajang = rs.getInt("JlhBatangRajang");
                String hourMeter = rs.getString("HourMeter");
                String remark = rs.getString("Remark");
                String namaJenisKayu = rs.getString("NamaJenisKayu");
                String hourStart = rs.getString("HourStart");
                String hourEnd = rs.getString("HourEnd");
                int idOperator1 = rs.getInt("IdOperator1");
                int idOperator2 = rs.getInt("IdOperator2");
                double beratBalokTim = rs.getDouble("BeratBalokTim");
                String namaMeja = rs.getString("NamaMeja");

                int stokTersedia;
                if (namaJenisKayu != null && namaJenisKayu.toLowerCase().contains("rambung")) {
                    stokTersedia = stokMapRambung.getOrDefault(noKayuBulat, -1);
                } else {
                    stokTersedia = stokMapUmum.getOrDefault(noKayuBulat, -1);
                }

                SawmillData data = new SawmillData(
                        noSTSawmill, shift, tglSawmill, noKayuBulat, noMeja, operator,
                        idSpecial, balokTerpakai, jamKerja, jlhBatangRajang,
                        hourMeter, remark, namaJenisKayu, stokTersedia,
                        beratBalokTim, hourStart, hourEnd, idOperator1, idOperator2, namaMeja
                );

                sawmillDataList.add(data);
            }

        } catch (SQLException e) {
            Log.e("Database Fetch Error", "Error fetching sawmill data: " + e.getMessage());
            // TAMBAHAN: Print stack trace untuk debugging
            e.printStackTrace();
        }

        return sawmillDataList;
    }


    public static Map<String, Integer> getTotalStokTersediaPerKayuBulat() {
        Map<String, Integer> stokMap = new HashMap<>();

        String query = "SELECT " +
                "    A.NoKayuBulat, " +
                "    SUM(A.StokTersedia - COALESCE(B.Pcs, 0)) AS TotalStokTersedia " +
                "FROM ( " +
                "    SELECT A.NoKayuBulat, " +
                "           COUNT(A.NoKayuBulat) AS StokTersedia " +
                "    FROM ( " +
                "        SELECT A.NoKayuBulat " +
                "        FROM KayuBulat_h A " +
                "        INNER JOIN KayuBulat_d C ON C.NoKayuBulat = A.NoKayuBulat " +
                "    ) A " +
                "    GROUP BY A.NoKayuBulat " +
                ") A " +
                "LEFT JOIN ( " +
                "    SELECT B.NoKayuBulat, " +
                "           SUM(A.Pcs) AS Pcs " +
                "    FROM STSawmill_dBalokGantung A " +
                "    INNER JOIN STSawmill_h B ON B.NoSTSawmill = A.NoSTSawmill " +
                "    GROUP BY B.NoKayuBulat " +
                ") B ON B.NoKayuBulat = A.NoKayuBulat " +
                "GROUP BY A.NoKayuBulat";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String noKayuBulat = rs.getString("NoKayuBulat");
                int totalStokTersedia = rs.getInt("TotalStokTersedia");
                stokMap.put(noKayuBulat, totalStokTersedia);
            }

        } catch (SQLException e) {
            Log.e("DB Error", "Error in getTotalStokTersediaPerKayuBulatRambung: " + e.getMessage());
        }

        return stokMap;
    }


    public static Map<String, Integer> getTotalStokTersediaPerKayuBulatRambung() {
        Map<String, Integer> stokMap = new HashMap<>();

        String query = "SELECT A.NoKayuBulat, " +
                "SUM(A.StokTersedia - COALESCE(B.Pcs, 0)) AS TotalStokTersedia " +
                "FROM (" +
                "    SELECT A.NoKayuBulat, A.IdGradeKB, SUM(JmlhBatang) AS StokTersedia " +
                "    FROM KayuBulatKG_d A " +
                "    GROUP BY A.NoKayuBulat, A.IdGradeKB" +
                ") A " +
                "LEFT JOIN (" +
                "    SELECT B.NoKayuBulat, A.IdGradeKB, SUM(A.Pcs) AS Pcs " +
                "    FROM STSawmill_dBalokGantungKG A " +
                "    INNER JOIN STSawmill_h B ON B.NoSTSawmill = A.NoSTSawmill " +
                "    GROUP BY B.NoKayuBulat, A.IdGradeKB" +
                ") B ON B.NoKayuBulat = A.NoKayuBulat AND B.IdGradeKB = A.IdGradeKB " +
                "GROUP BY A.NoKayuBulat";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String noKayuBulat = rs.getString("NoKayuBulat");
                int stokTersedia = rs.getInt("TotalStokTersedia");
                stokMap.put(noKayuBulat, stokTersedia);
            }

        } catch (SQLException e) {
            Log.e("DB Error", "Gagal ambil stok tersedia: " + e.getMessage());
        }

        return stokMap;
    }


    public static List<SpecialConditionData> getSpecialConditionList() {
        List<SpecialConditionData> conditionList = new ArrayList<>();
        String query = "SELECT IdSawmillSpecialCondition, Condition FROM MstSawmillSpecialCondition WHERE enable = 1";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            // Proses hasil query
            while (rs.next()) {
                int id = rs.getInt("IdSawmillSpecialCondition");
                String condition = rs.getString("Condition");

                // Membuat objek SpecialConditionData dan menambahkannya ke dalam list
                conditionList.add(new SpecialConditionData(id, condition));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conditionList;
    }


    public static List<OperatorData> getOperatorList() {
        List<OperatorData> operatorList = new ArrayList<>();
        String query = "SELECT IdOperator, NamaOperator FROM MstOperator WHERE (IdBagian = 8 OR IdBagian = 9) AND ENABLE = 1";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("IdOperator");
                String nama = rs.getString("NamaOperator");

                operatorList.add(new OperatorData(id, nama));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return operatorList;
    }


    public static List<MejaData> getAllMeja() {
        List<MejaData> list = new ArrayList<>();
        String query = "SELECT NoMeja, NamaMeja FROM MstMesinSawmill ORDER BY NoMeja ASC";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new MejaData(
                        rs.getString("NoMeja"),
                        rs.getString("NamaMeja")
                ));
            }
        } catch (SQLException e) {
            Log.e("SawmillApi", "Gagal mengambil data Meja: " + e.getMessage());
        }

        return list;
    }



    public static SawmillData getOperatorByNoMeja(String noMeja) {
        String query = "SELECT NoMeja, IdOperator1, IdOperator2 FROM MstMesinSawmill WHERE NoMeja = ?";


        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, noMeja);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int idOp1 = rs.getInt("IdOperator1");
                    int idOp2 = rs.getInt("IdOperator2");

                    return new SawmillData(
                            null, null, null,      // noSTSawmill, shift, tglSawmill
                            null,                  // noKayuBulat
                            rs.getString("NoMeja"),// noMeja
                            null,                  // operator
                            0, null, null, 0, null, null,
                            null, 0, 0.0,
                            null, null,
                            idOp1,
                            idOp2,
                            null
                    );
                } else {
                    Log.w("SawmillApi", "Tidak ditemukan data operator untuk NoMeja: " + noMeja);
                }
            }

        } catch (SQLException e) {
            Log.e("SawmillApi", "Error fetching operator by NoMeja: " + e.getMessage(), e);
        }

        return null;
    }



    public static List<String> getNoKayuBulatSuggestions(String keyword) {
        List<String> suggestions = new ArrayList<>();

        String query = "SELECT TOP 10 NoKayuBulat " +
                "FROM KayuBulat_h " +
                "WHERE NoKayuBulat LIKE ? " +
                "AND DateUsage IS NULL " +
                "AND NoKayuBulat NOT IN (SELECT NoKayuBulat FROM PenerimaanSTSawmill_h) " +
                "ORDER BY NoKayuBulat ASC";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, keyword + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    suggestions.add(rs.getString("NoKayuBulat"));
                }
            }

        } catch (SQLException e) {
            Log.e("SawmillApi", "Error fetching NoKayuBulat suggestions: " + e.getMessage());
        }

        return suggestions;
    }


    public static KayuBulatData getKayuBulatDetail(String noKayuBulat) {
        String query = "SELECT kb.NoPlat, jk.Jenis, sp.NmSupplier, kb.NoTruk, kb.Suket " +
                "FROM KayuBulat_h kb " +
                "LEFT JOIN MstJenisKayu jk ON kb.IdJenisKayu = jk.IdJenisKayu " +
                "LEFT JOIN MstSupplier sp ON kb.IdSupplier = sp.IdSupplier " +
                "WHERE kb.NoKayuBulat = ? " +
                "AND kb.DateUsage IS NULL " +
                "AND kb.NoKayuBulat NOT IN (SELECT NoKayuBulat FROM PenerimaanSTSawmill_h)";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, noKayuBulat);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new KayuBulatData(
                            rs.getString("NoPlat"),
                            rs.getString("Jenis"),       // nama jenis kayu
                            rs.getString("NmSupplier"),  // nama supplier
                            rs.getString("NoTruk"),
                            rs.getString("Suket")
                    );
                }
            }

        } catch (SQLException e) {
            Log.e("SawmillApi", "Error fetching KayuBulat detail: " + e.getMessage());
        }

        return null;
    }

    public static String getNextNoTellySawmill() {
        String query = "SELECT TOP 1 NoSTSawmill FROM STSawmill_h ORDER BY NoSTSawmill DESC";
        String lastNoTellySawmill = null;

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                lastNoTellySawmill = rs.getString("NoSTSawmill");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (lastNoTellySawmill != null && lastNoTellySawmill.startsWith("D.")) {
            String numericPart = lastNoTellySawmill.substring(2);  // Mengambil bagian angka setelah "D."
            int nextNumber = Integer.parseInt(numericPart) + 1;  // Menambah 1 pada angka terakhir
            return "D." + String.format("%06d", nextNumber);  // Format dengan 6 digit angka
        } else {
            // Jika NoPenerimaanST tidak ditemukan atau formatnya tidak sesuai, kembalikan nomor default
            return null;
        }
    }


    //BUNGKUS SEMUA INSERT KE DALAM 1 METHOD AGAR KETIKA SALAH SATUNYA FAIL DAPAT DIROLLBACK!!
    public static void insertSawmillData(
            final String noSTSawmill,
            final int shift,
            final String tglSawmill,
            final String noKayuBulat,
            final String noMeja,
            final int idSawmillSpecialCondition,
            final int balokTerpakai,
            final String jlhBatangRajang,
            final String hourMeter,
            final int idOperator1,
            final int idOperator2,
            final String remark,
            final String beratBalokTim,
            final String jenisKayu,
            final String jamMulai,
            final String jamBerhenti,
            final String totalJamKerja
    ) throws SQLException {
        Connection con = null;
        try {
            con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
            con.setAutoCommit(false);  // mulai transaksi

            insertSTSawmillHeader(con, noSTSawmill, shift, tglSawmill, noKayuBulat, noMeja,
                    idSawmillSpecialCondition, balokTerpakai, jlhBatangRajang,
                    hourMeter, idOperator1, idOperator2, remark, jamMulai, jamBerhenti, totalJamKerja);

            insertBalokTimDetail(con, noSTSawmill, beratBalokTim);

            if (jenisKayu != null && jenisKayu.toLowerCase().contains("rambung")) {
                insertBalokTerpakaiKG(con, noSTSawmill, noKayuBulat, balokTerpakai);
            } else {
                insertBalokTerpakai(con, noSTSawmill, noKayuBulat, balokTerpakai);
            }

            con.commit();  // jika semua berhasil

        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException rollbackEx) {
                    // Tambahkan ke log jika perlu
                    rollbackEx.printStackTrace();
                }
            }
            throw e; // lempar ulang error ke pemanggil
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    // Tambahkan ke log jika perlu
                    e.printStackTrace();
                }
            }
        }
    }


    public static void insertSTSawmillHeader(Connection con,
                                             String noSTSawmill, int shift, String tglSawmill,
                                             String noKayuBulat, String noMeja, int idSawmillSpecialCondition,
                                             int balokTerpakai, String jlhBatangRajang, String hourMeter,
                                             int idOperator1, int idOperator2, String remark, String jamMulai, String jamBerhenti, String totalJamKerja) throws SQLException {
        String query = "INSERT INTO STSawmill_h " +
                "(NoSTSawmill, Shift, TglSawmill, NoKayuBulat, NoMeja, IdSawmillSpecialCondition, BalokTerpakai, JlhBatangRajang, HourMeter, IdOperator1, IdOperator2, Remark, HourStart, HourEnd, JamKerja) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Log.d("DEBUG_SAWMILL", "jamBerhenti yang dikirim: " + jamBerhenti);


        try (PreparedStatement stmt = con.prepareStatement(query)) {
            String formattedTgl = formatToDatabaseDate(tglSawmill);

            stmt.setString(1, noSTSawmill);
            stmt.setInt(2, shift);
            stmt.setString(3, formattedTgl);
            stmt.setString(4, noKayuBulat);
            stmt.setString(5, noMeja);
            stmt.setInt(6, idSawmillSpecialCondition);
            stmt.setInt(7, balokTerpakai);
            stmt.setString(8, jlhBatangRajang);
            stmt.setString(9, hourMeter);
            stmt.setInt(10, idOperator1);

            if (idOperator2 == -1) {
                stmt.setNull(11, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(11, idOperator2);
            }

            stmt.setString(12, remark);

            stmt.setString(13, jamMulai);
            if (jamBerhenti == null || jamBerhenti.isEmpty()) {
                stmt.setNull(14, java.sql.Types.TIME);
            } else {
                stmt.setString(14, jamBerhenti); // atau setTime dengan Time.valueOf(...)
            }
            stmt.setString(15, totalJamKerja);

            stmt.executeUpdate();
        }
    }

    public static void insertBalokTimDetail(Connection con, String noSTSawmill, String berat) throws SQLException {
        String query = "INSERT INTO STSawmill_dBalokTim (NoSTSawmill, Berat) VALUES (?, ?)";

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, noSTSawmill);
            stmt.setString(2, berat);
            stmt.executeUpdate();
        }
    }


    public static void insertBalokTerpakai(Connection con, String noSTSawmill, String noKayuBulat, int jlhBalokTerpakai) throws SQLException {
        String queryPriority = "SELECT A.NoKayuBulat, A.Jenis AS Grade, A.StokALL, " +
                "A.StokTersedia - (CASE WHEN B.Pcs IS NULL THEN 0 ELSE B.Pcs END) AS StokTersedia, " +
                "B.Pcs, " +
                "((CAST((A.StokTersedia) AS DECIMAL(10,2)) / A.StokALL) * 100) AS Priority " +
                "FROM ( " +
                "  SELECT A.NoKayuBulat, SUM(COUNT(A.NoKayuBulat)) OVER() AS StokALL, " +
                "  COUNT(A.NoKayuBulat) AS StokTersedia, Jenis " +
                "  FROM ( " +
                "    SELECT A.NoKayuBulat, " +
                "    CASE " +
                "      WHEN A.IdPengukuran = '2' THEN " +
                "        CASE " +
                "          WHEN C.IsAfkir = 0 AND C.IsMC = 0 AND C.IsMCMata = 0 AND C.IsBangkang = 1 THEN 'STD BKG' " +
                "          WHEN C.IsAfkir = 0 AND C.IsMC = 1 AND C.IsMCMata = 0 AND C.IsBangkang = 1 THEN 'MC BKG' " +
                "          WHEN C.IsAfkir = 0 AND C.IsMC = 0 AND C.IsMCMata = 1 AND C.IsBangkang = 0 THEN 'MC MATA' " +
                "          WHEN C.IsAfkir = 0 AND C.IsMC = 1 AND C.IsMCMata = 0 AND C.IsBangkang = 0 THEN 'MC' " +
                "          WHEN C.IsAfkir = 1 AND C.IsMC = 0 AND C.IsMCMata = 0 AND C.IsBangkang = 0 THEN 'AFKIR' " +
                "          ELSE CASE WHEN C.Tebal * C.Lebar >= 9 THEN 'STD' ELSE 'AFKIR' END " +
                "        END " +
                "      WHEN A.IdPengukuran = '10' THEN " +
                "        CASE " +
                "          WHEN C.IsAfkir = 0 AND C.IsMC = 0 AND C.IsMCMata = 1 AND C.IsBangkang = 0 THEN 'MC MATA' " +
                "          WHEN C.IsAfkir = 0 AND C.IsMC = 1 AND C.IsMCMata = 0 AND C.IsBangkang = 0 THEN 'MC' " +
                "          WHEN C.IsAfkir = 1 AND C.IsMC = 0 AND C.IsMCMata = 0 AND C.IsBangkang = 0 THEN 'AFKIR' " +
                "          ELSE CASE WHEN C.Tebal * C.Lebar >= 25 THEN 'STD' WHEN C.Tebal * C.Lebar >= 9 THEN 'MC' ELSE 'AFKIR' END " +
                "        END " +
                "      WHEN A.IdPengukuran = '11' THEN " +
                "        CASE " +
                "          WHEN C.IsAfkir = 0 AND C.IsMC = 0 AND C.IsMCMata = 1 AND C.IsBangkang = 0 THEN 'MC MATA' " +
                "          WHEN C.IsAfkir = 0 AND C.IsMC = 1 AND C.IsMCMata = 0 AND C.IsBangkang = 0 THEN 'MC' " +
                "          WHEN C.IsAfkir = 0 AND C.IsMC = 0 AND C.IsMCMata = 0 AND C.IsBangkang = 1 THEN 'BKG' " +
                "          WHEN C.IsAfkir = 1 AND C.IsMC = 0 AND C.IsMCMata = 0 AND C.IsBangkang = 0 THEN 'AFKIR' " +
                "          ELSE CASE WHEN C.Tebal * C.Lebar >= 16 THEN 'STD' ELSE 'AFKIR' END " +
                "        END " +
                "    END AS Jenis " +
                "    FROM KayuBulat_h A " +
                "    INNER JOIN KayuBulat_d C ON C.NoKayuBulat = A.NoKayuBulat " +
                "    WHERE A.NoKayuBulat = '" + noKayuBulat + "' " +
                "  ) A " +
                "  GROUP BY A.NoKayuBulat, A.Jenis " +
                ") A " +
                "LEFT JOIN ( " +
                "  SELECT B.NoKayuBulat, Grade, SUM(Pcs) AS Pcs " +
                "  FROM STSawmill_dBalokGantung A " +
                "  INNER JOIN STSawmill_h B ON B.NoSTSawmill = A.NoSTSawmill " +
                "  GROUP BY B.NoKayuBulat, Grade " +
                ") B ON B.NoKayuBulat = A.NoKayuBulat AND B.Grade = A.Jenis " +
                "ORDER BY Priority DESC";

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(queryPriority)) {

            int sisaBalok = jlhBalokTerpakai;

            int sumStokTersedia = 0;

            while (rs.next() && sisaBalok > 0) {
                String grade = rs.getString("Grade");

                // cek apakah stokTersedia null, default jadi 0 jika null
                int stokTersedia = rs.getObject("StokTersedia") != null ? rs.getInt("StokTersedia") : 0;
                sumStokTersedia += stokTersedia;

                // skip jika stok tidak tersedia
                if (stokTersedia <= 0) continue;

                int pcsInsert = Math.min(sisaBalok, stokTersedia);

                Log.d("KAYU_INSERT", "Insert NoSTSawmill: " + noSTSawmill + ", Grade: " + grade + ", Pcs: " + pcsInsert);

                String insertQuery = "INSERT INTO STSawmill_dBalokGantung (NoSTSawmill, Grade, Pcs) VALUES (?, ?, ?)";
                try (PreparedStatement ps = con.prepareStatement(insertQuery)) {
                    ps.setString(1, noSTSawmill);
                    ps.setString(2, grade);
                    ps.setInt(3, pcsInsert);
                    ps.executeUpdate();
                }

                sisaBalok -= pcsInsert;
            }

            if (sisaBalok > 0) {
                throw new SQLException(" Sisa balok: " + sumStokTersedia + "!");
            }
        }
    }


    public static void insertBalokTerpakaiKG(Connection con, String noSTSawmill, String noKayuBulat, int jlhBalokTerpakai) throws SQLException {
        String queryPriority = "SELECT A.NoKayuBulat, A.IdGradeKB, A.StokALL, " +
                "A.StokTersedia - (CASE WHEN B.Pcs IS NULL THEN 0 ELSE B.Pcs END) AS StokTersedia, " +
                "B.Pcs, " +
                "((CAST((A.StokTersedia) AS DECIMAL(10,2)) / A.StokALL) * 100) AS Priority " +
                "FROM ( " +
                "  SELECT A.NoKayuBulat, IdGradeKB, " +
                "  SUM(SUM(JmlhBatang)) OVER() AS StokALL, " +
                "  SUM(JmlhBatang) AS StokTersedia " +
                "  FROM KayuBulatKG_d A " +
                "  WHERE A.NoKayuBulat = '" + noKayuBulat + "' " +
                "  GROUP BY A.NoKayuBulat, IdGradeKB " +
                ") A " +
                "LEFT JOIN ( " +
                "  SELECT B.NoKayuBulat, IdGradeKB, SUM(Pcs) AS Pcs " +
                "  FROM STSawmill_dBalokGantungKG A " +
                "  INNER JOIN STSawmill_h B ON B.NoSTSawmill = A.NoSTSawmill " +
                "  GROUP BY B.NoKayuBulat, IdGradeKB " +
                ") B ON B.NoKayuBulat = A.NoKayuBulat AND B.IdGradeKB = A.IdGradeKB " +
                "ORDER BY Priority DESC";

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(queryPriority)) {

            int sisaBalok = jlhBalokTerpakai;
            int sumStokTersedia = 0;

            while (rs.next() && sisaBalok > 0) {
                String idGradeKB = rs.getString("IdGradeKB");

                int stokTersedia = rs.getObject("StokTersedia") != null ? rs.getInt("StokTersedia") : 0;
                sumStokTersedia += stokTersedia;

                if (stokTersedia <= 0) continue;

                int pcsInsert = Math.min(sisaBalok, stokTersedia);

                Log.d("KAYU_INSERT_KG", "Insert NoSTSawmill: " + noSTSawmill + ", IdGradeKB: " + idGradeKB + ", Pcs: " + pcsInsert);

                String insertQuery = "INSERT INTO STSawmill_dBalokGantungKG (NoSTSawmill, IdGradeKB, Pcs) VALUES (?, ?, ?)";
                try (PreparedStatement ps = con.prepareStatement(insertQuery)) {
                    ps.setString(1, noSTSawmill);
                    ps.setString(2, idGradeKB);
                    ps.setInt(3, pcsInsert);
                    ps.executeUpdate();
                }

                sisaBalok -= pcsInsert;
            }

            if (sisaBalok > 0) {
                throw new SQLException(" Sisa balok (Rambung) : " + sumStokTersedia + "!");
            }
        }
    }

    // UPDATE SAWMILL DATA - BUNGKUS SEMUA UPDATE KE DALAM 1 METHOD DENGAN TRANSACTION
    public static void updateSawmillData(
            final String noSTSawmill, // Primary key untuk update
            final int shift,
            final String tglSawmill,
            final String noKayuBulat,
            final String noMeja,
            final int idSawmillSpecialCondition,
            final int balokTerpakai,
            final String jlhBatangRajang,
            final String hourMeter,
            final int idOperator1,
            final int idOperator2,
            final String remark,
            final String beratBalokTim,
            final String jenisKayu,
            final String jamMulai,
            final String jamBerhenti,
            final String totalJamKerja
    ) throws SQLException {
        Connection con = null;
        try {
            con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
            con.setAutoCommit(false);  // mulai transaksi

            // 1. Update header table
            updateSTSawmillHeader(con, noSTSawmill, shift, tglSawmill, noKayuBulat, noMeja,
                    idSawmillSpecialCondition, balokTerpakai, jlhBatangRajang,
                    hourMeter, idOperator1, idOperator2, remark, jamMulai, jamBerhenti, totalJamKerja);

            // 2. Update/Replace BalokTim detail - hapus yang lama, insert yang baru
            deleteBalokTimDetail(con, noSTSawmill);
            insertBalokTimDetail(con, noSTSawmill, beratBalokTim);

            // 3. Update/Replace BalokTerpakai detail - hapus yang lama, insert yang baru
            deleteBalokTerpakaiDetail(con, noSTSawmill);
            if (jenisKayu != null && jenisKayu.toLowerCase().contains("rambung")) {
                insertBalokTerpakaiKG(con, noSTSawmill, noKayuBulat, balokTerpakai);
            } else {
                insertBalokTerpakai(con, noSTSawmill, noKayuBulat, balokTerpakai);
            }

            con.commit();  // jika semua berhasil

        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            throw e; // lempar ulang error ke pemanggil
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // UPDATE STSawmill_h (Header)
    public static void updateSTSawmillHeader(Connection con,
                                             String noSTSawmill, int shift, String tglSawmill,
                                             String noKayuBulat, String noMeja, int idSawmillSpecialCondition,
                                             int balokTerpakai, String jlhBatangRajang, String hourMeter,
                                             int idOperator1, int idOperator2, String remark,
                                             String jamMulai, String jamBerhenti, String totalJamKerja) throws SQLException {

        Log.d("DEBUG_SAWMILL", "jamBerhenti yang dikirim: " + jamBerhenti);


        String query = "UPDATE STSawmill_h SET " +
                "Shift = ?, TglSawmill = ?, NoKayuBulat = ?, NoMeja = ?, " +
                "IdSawmillSpecialCondition = ?, BalokTerpakai = ?, JlhBatangRajang = ?, " +
                "HourMeter = ?, IdOperator1 = ?, IdOperator2 = ?, Remark = ?, " +
                "HourStart = ?, HourEnd = ?, JamKerja = ? " +
                "WHERE NoSTSawmill = ?";

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            String formattedTgl = formatToDatabaseDate(tglSawmill);

            stmt.setInt(1, shift);
            stmt.setString(2, formattedTgl);
            stmt.setString(3, noKayuBulat);
            stmt.setString(4, noMeja);
            stmt.setInt(5, idSawmillSpecialCondition);
            stmt.setInt(6, balokTerpakai);
            stmt.setString(7, jlhBatangRajang);
            stmt.setString(8, hourMeter);
            stmt.setInt(9, idOperator1);

            if (idOperator2 == -1) {
                stmt.setNull(10, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(10, idOperator2);
            }

            stmt.setString(11, remark);
            stmt.setString(12, jamMulai);
            stmt.setString(13, jamBerhenti);
            stmt.setString(14, totalJamKerja);
            stmt.setString(15, noSTSawmill); // WHERE clause

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Update gagal, data dengan NoSTSawmill " + noSTSawmill + " tidak ditemukan");
            }
        }
    }

    // DELETE BalokTim Detail sebelum insert ulang
    public static void deleteBalokTimDetail(Connection con, String noSTSawmill) throws SQLException {
        String query = "DELETE FROM STSawmill_dBalokTim WHERE NoSTSawmill = ?";

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, noSTSawmill);
            stmt.executeUpdate();
        }
    }

    // DELETE BalokTerpakai Detail sebelum insert ulang
    public static void deleteBalokTerpakaiDetail(Connection con, String noSTSawmill) throws SQLException {
        // Delete dari kedua tabel detail
        String query1 = "DELETE FROM STSawmill_dBalokGantung WHERE NoSTSawmill = ?";
        String query2 = "DELETE FROM STSawmill_dBalokGantungKG WHERE NoSTSawmill = ?";

        try (PreparedStatement stmt1 = con.prepareStatement(query1);
             PreparedStatement stmt2 = con.prepareStatement(query2)) {

            stmt1.setString(1, noSTSawmill);
            stmt1.executeUpdate();

            stmt2.setString(1, noSTSawmill);
            stmt2.executeUpdate();
        }
    }


    // DELETE SAWMILL DATA - BUNGKUS SEMUA DELETE KE DALAM 1 METHOD DENGAN TRANSACTION
    public static void deleteSawmillData(String noSTSawmill, String jenisKayu) throws SQLException {
        Connection con = null;

        try {
            Log.d("deleteSawmillData", "Mulai menghapus data: " + noSTSawmill + ", jenis kayu: " + jenisKayu);

            con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
            con.setAutoCommit(false); // mulai transaksi

            if (jenisKayu != null && jenisKayu.toLowerCase().contains("rambung")) {
                Log.d("deleteSawmillData", "Menghapus dari STSawmill_dBalokGantungKG");
                deleteGantungKG(con, noSTSawmill);
            } else {
                Log.d("deleteSawmillData", "Menghapus dari STSawmill_dBalokGantung");
                deleteGantung(con, noSTSawmill);
            }

            Log.d("deleteSawmillData", "Menghapus dari STSawmill_dBalokTim");
            deleteBalokTim(con, noSTSawmill);

            // 🆕 Tambahkan ini untuk hapus detail dan grade
            Log.d("deleteSawmillData", "Menghapus dari STSawmill_d dan STSawmillKG_d");
            deleteAllDetail(con, noSTSawmill, jenisKayu != null && jenisKayu.toLowerCase().contains("rambung"));

            Log.d("deleteSawmillData", "Menghapus dari STSawmill_h");
            deleteHeader(con, noSTSawmill);

            con.commit();
            Log.d("deleteSawmillData", "Transaksi berhasil di-commit");

        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback();
                    Log.e("deleteSawmillData", "Rollback transaksi karena error: " + e.getMessage(), e);
                } catch (SQLException rollbackEx) {
                    Log.e("deleteSawmillData", "Gagal rollback: " + rollbackEx.getMessage(), rollbackEx);
                }
            }
            throw e;
        } finally {
            if (con != null) {
                try {
                    con.close();
                    Log.d("deleteSawmillData", "Koneksi ditutup");
                } catch (SQLException e) {
                    Log.e("deleteSawmillData", "Gagal menutup koneksi: " + e.getMessage(), e);
                }
            }
        }
    }

    private static void deleteGantung(Connection con, String noSTSawmill) throws SQLException {
        String query = "DELETE FROM STSawmill_dBalokGantung WHERE NoSTSawmill = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, noSTSawmill);
            stmt.executeUpdate();
        }
    }

    private static void deleteGantungKG(Connection con, String noSTSawmill) throws SQLException {
        String query = "DELETE FROM STSawmill_dBalokGantungKG WHERE NoSTSawmill = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, noSTSawmill);
            stmt.executeUpdate();
        }
    }

    private static void deleteBalokTim(Connection con, String noSTSawmill) throws SQLException {
        String query = "DELETE FROM STSawmill_dBalokTim WHERE NoSTSawmill = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, noSTSawmill);
            stmt.executeUpdate();
        }
    }

    private static void deleteHeader(Connection con, String noSTSawmill) throws SQLException {
        String query = "DELETE FROM STSawmill_h WHERE NoSTSawmill = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, noSTSawmill);
            stmt.executeUpdate();
        }
    }

    private static void deleteAllDetail(Connection con, String noSTSawmill, boolean isRambung) throws SQLException {
        if (isRambung) {
            String deleteGradeQuery = "DELETE FROM STSawmillKG_d WHERE NoSTSawmill = ?";
            try (PreparedStatement stmt = con.prepareStatement(deleteGradeQuery)) {
                stmt.setString(1, noSTSawmill);
                int rows = stmt.executeUpdate();
                Log.d("deleteSawmillData", "Deleted " + rows + " rows from STSawmillKG_d");
            }
        }

        String deleteDetailQuery = "DELETE FROM STSawmill_d WHERE NoSTSawmill = ?";
        try (PreparedStatement stmt = con.prepareStatement(deleteDetailQuery)) {
            stmt.setString(1, noSTSawmill);
            int rows = stmt.executeUpdate();
            Log.d("deleteSawmillData", "Deleted " + rows + " rows from STSawmill_d");
        }
    }


    public static boolean isHourRangeOverlapping(String tglSawmill, String noMeja,
                                                 String newHourStart, String newHourEnd,
                                                 String noSTSawmill) {
        final String TAG = "CheckOverlap";
        String formattedTgl = formatToDatabaseDate(tglSawmill);

        // Modifikasi query untuk mengecualikan record dengan ID tertentu
        String query = "SELECT NoSTSawmill, HourStart, HourEnd FROM STSawmill_h WHERE TglSawmill = ? AND NoMeja = ?";
        boolean isOverlap = false;

        Log.d(TAG, "Mulai cek overlap untuk tanggal: " + formattedTgl + ", meja: " + noMeja +
                ", mulai: " + newHourStart + ", selesai: " + newHourEnd + ", kecuali ID: " + noSTSawmill);

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, formattedTgl);
            stmt.setString(2, noMeja);

            try (ResultSet rs = stmt.executeQuery()) {
                LocalDate tgl = LocalDate.parse(formattedTgl);
                LocalTime startTime = LocalTime.parse(newHourStart);
                LocalTime endTime = LocalTime.parse(newHourEnd);

                // Penyesuaian shift lintas hari
                LocalDateTime newStart = LocalDateTime.of(tgl, startTime);
                LocalDateTime newEnd = endTime.isBefore(startTime)
                        ? LocalDateTime.of(tgl.plusDays(1), endTime)
                        : LocalDateTime.of(tgl, endTime);

                while (rs.next()) {
                    String recordId = rs.getString("NoSTSawmill");

                    // Lewati record yang sedang diedit
                    if (recordId != null && recordId.equals(noSTSawmill)) {
                        Log.d(TAG, "Melewati record dengan ID: " + recordId);
                        continue;
                    }

                    String dbStartStr = rs.getString("HourStart");
                    String dbEndStr = rs.getString("HourEnd");

                    if (dbStartStr == null || dbEndStr == null) {
                        Log.d(TAG, "Ditemukan record dengan nilai jam kosong, dilewati.");
                        continue;
                    }

                    LocalTime dbStartTime = LocalTime.parse(dbStartStr);
                    LocalTime dbEndTime = LocalTime.parse(dbEndStr);

                    LocalDateTime dbStart = LocalDateTime.of(tgl, dbStartTime);
                    LocalDateTime dbEnd = dbEndTime.isBefore(dbStartTime)
                            ? LocalDateTime.of(tgl.plusDays(1), dbEndTime)
                            : LocalDateTime.of(tgl, dbEndTime);

                    Log.d(TAG, "Membandingkan dengan record: ID=" + recordId + ", Start=" + dbStart + ", End=" + dbEnd);

                    // Cek overlap
                    if (dbStart.isBefore(newEnd) && newStart.isBefore(dbEnd)) {
                        Log.d(TAG, "Overlap ditemukan!");
                        isOverlap = true;
                        break;
                    }
                }
            }
        } catch (SQLException e) {
            Log.e(TAG, "Error saat cek overlap", e);
        }

        Log.d(TAG, "Hasil cek overlap: " + isOverlap);
        return isOverlap;
    }


    public static List<SawmillDetailData> fetchSawmillDetailData(String noSTSawmill) {
        List<SawmillDetailData> itemList = new ArrayList<>();

        String query =
                "SELECT d.NoUrut, d.Tebal, d.Lebar, d.Panjang, d.JmlhBatang, " +
                        "d.IsLocal, d.IdUOMTblLebar, d.IdUOMPanjang, d.IsBagusKulit, " +
                        "kg.IdGradeKB, g.NamaGrade " +
                        "FROM STSawmill_d d " +
                        "LEFT JOIN STSawmillKG_d kg ON d.NoSTSawmill = kg.NoSTSawmill AND d.NoUrut = kg.NoUrut " +
                        "LEFT JOIN MstGradeKB g ON kg.IdGradeKB = g.IdGradeKB " +
                        "WHERE d.NoSTSawmill = ?" +
                        "ORDER BY d.NoUrut ASC";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, noSTSawmill);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    SawmillDetailData item = new SawmillDetailData(
                            rs.getInt("NoUrut"),
                            rs.getFloat("Tebal"),
                            rs.getFloat("Lebar"),
                            rs.getFloat("Panjang"),
                            rs.getInt("JmlhBatang"),
                            rs.getBoolean("IsLocal"),
                            rs.getInt("IdUOMTblLebar"),
                            rs.getInt("IdUOMPanjang"),
                            rs.getInt("IsBagusKulit"),
                            rs.getInt("IdGradeKB"),
                            rs.getString("NamaGrade") // Ambil nama grade dari hasil join
                    );
                    itemList.add(item);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return itemList;
    }


    public static List<GradeKBData> getGradeKBList() {
        List<GradeKBData> gradeKBList = new ArrayList<>();
        String query = "SELECT IdGradeKB, NamaGrade FROM MstGradeKB WHERE isst = 1 AND enable = 1";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("IdGradeKB");
                String nama = rs.getString("NamaGrade");

                gradeKBList.add(new GradeKBData(id, nama));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return gradeKBList;
    }


    public static boolean replaceAllSawmillDetailData(String noSTSawmill, List<SawmillDetailData> detailDataList, String jenisKayu) {
        final String TAG = "SawmillDB";
        boolean isRambung = jenisKayu.toLowerCase().contains("rambung");

        // Sort the data before inserting
        Collections.sort(detailDataList, new Comparator<SawmillDetailData>() {
            @Override
            public int compare(SawmillDetailData a, SawmillDetailData b) {
                // Priority 0: Sort by isBagusKulit
                int kulitOrderA = getIsBagusKulitPriority(a.getIsBagusKulit());
                int kulitOrderB = getIsBagusKulitPriority(b.getIsBagusKulit());
                if (kulitOrderA != kulitOrderB) {
                    return Integer.compare(kulitOrderA, kulitOrderB);
                }

                // Priority 1: Sort by grade
                int gradeOrderA = getGradePriority(a.getNamaGrade(), a.getIsBagusKulit());
                int gradeOrderB = getGradePriority(b.getNamaGrade(), b.getIsBagusKulit());
                if (gradeOrderA != gradeOrderB) {
                    return Integer.compare(gradeOrderA, gradeOrderB);
                }

                // Priority 2: Sort by tebal (descending)
                int compareTebal = Float.compare(b.getTebal(), a.getTebal());
                if (compareTebal != 0) {
                    return compareTebal;
                }

                // Priority 3: Sort by lebar (ascending)
                int compareLebar = Float.compare(a.getLebar(), b.getLebar());
                if (compareLebar != 0) {
                    return compareLebar;
                }

                // Priority 4: Sort by panjang (ascending)
                return Float.compare(a.getPanjang(), b.getPanjang());
            }


            private int getIsBagusKulitPriority(int isBagusKulit) {
                // 0 = Kulit → 0 (paling atas)
                // 1 = Bagus → 1
                // Other → 2 (paling bawah)
                if (isBagusKulit == 0) return 0;
                if (isBagusKulit == 1) return 1;
                return 2;
            }

            private int getGradePriority(String namaGrade, int isBagusKulit) {
                if (namaGrade == null) return 999;

                switch (namaGrade.toUpperCase()) {
                    case "STD": return 1;
                    case "MC 1": return 2;
                    case "MC 2": return 3;
                    case "KAYU LAT": return 4;
                    default: return 999;
                }
            }
        });


        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl())) {
            Log.d(TAG, "Koneksi database berhasil");
            con.setAutoCommit(false);

            // 1. DELETE data lama
            String deleteGradeQuery = "DELETE FROM STSawmillKG_d WHERE NoSTSawmill = ?";
            String deleteDetailQuery = "DELETE FROM STSawmill_d WHERE NoSTSawmill = ?";

            if (isRambung) {
                try (PreparedStatement deleteGradeStmt = con.prepareStatement(deleteGradeQuery)) {
                    deleteGradeStmt.setString(1, noSTSawmill);
                    int deletedGradeRows = deleteGradeStmt.executeUpdate();
                    Log.d(TAG, "Deleted " + deletedGradeRows + " rows from STSawmillKG_d");
                }
            }

            try (PreparedStatement deleteDetailStmt = con.prepareStatement(deleteDetailQuery)) {
                deleteDetailStmt.setString(1, noSTSawmill);
                int deletedDetailRows = deleteDetailStmt.executeUpdate();
                Log.d(TAG, "Deleted " + deletedDetailRows + " rows from STSawmill_d");
            }

            // 2. INSERT data baru (already sorted)
            String insertDetailQuery = "INSERT INTO STSawmill_d (" +
                    "NoSTSawmill, NoUrut, Tebal, Lebar, Panjang, JmlhBatang, " +
                    "IsLocal, IdUOMTblLebar, IdUOMPanjang, IsBagusKulit" +
                    ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            String insertGradeQuery = "INSERT INTO STSawmillKG_d (" +
                    "NoSTSawmill, NoUrut, IdGradeKB" +
                    ") VALUES (?, ?, ?)";

            try (PreparedStatement insertDetailStmt = con.prepareStatement(insertDetailQuery);
                 PreparedStatement insertGradeStmt = isRambung ? con.prepareStatement(insertGradeQuery) : null) {

                // Update NoUrut based on sorted order
                int sequence = 1;
                for (SawmillDetailData data : detailDataList) {
                    // Insert ke STSawmill_d
                    insertDetailStmt.setString(1, noSTSawmill);
                    insertDetailStmt.setInt(2, sequence); // Use the new sequence number
                    insertDetailStmt.setFloat(3, data.getTebal());
                    insertDetailStmt.setFloat(4, data.getLebar());
                    insertDetailStmt.setFloat(5, data.getPanjang());
                    insertDetailStmt.setInt(6, data.getPcs());
                    insertDetailStmt.setBoolean(7, data.getIsBagusKulit() != 0);
                    insertDetailStmt.setInt(8, data.getIdUOMTblLebar());
                    insertDetailStmt.setInt(9, data.getIdUOMPanjang());
                    insertDetailStmt.setInt(10, data.getIsBagusKulit());
                    insertDetailStmt.addBatch();

                    if (isRambung && insertGradeStmt != null) {
                        insertGradeStmt.setString(1, noSTSawmill);
                        insertGradeStmt.setInt(2, sequence); // Use the new sequence number
                        insertGradeStmt.setInt(3, data.getIdGradeKB());
                        insertGradeStmt.addBatch();
                    }

                    Log.v(TAG, "Memproses data NoUrut=" + sequence +
                            ", Grade=" + data.getNamaGrade() +
                            ", BagusKulit=" + data.getIsBagusKulitLabel() +
                            ", Tebal=" + data.getTebal());

                    sequence++;
                }

                int[] detailResults = insertDetailStmt.executeBatch();
                Log.d(TAG, "Berhasil insert " + detailResults.length + " records ke STSawmill_d");

                if (isRambung && insertGradeStmt != null) {
                    int[] gradeResults = insertGradeStmt.executeBatch();
                    Log.d(TAG, "Berhasil insert " + gradeResults.length + " records ke STSawmillKG_d");
                }
            }

            con.commit();
            Log.i(TAG, "Transaksi sukses untuk NoSTSawmill: " + noSTSawmill);
            return true;

        } catch (SQLException e) {
            Log.e(TAG, "SQL Error:", e);
            return false;
        }
    }


    public static String insertPenerimaanSTSawmillWithDetail(String noKayuBulat, String tglLaporan) {
        String checkExisting = "SELECT TglLaporan FROM PenerimaanSTSawmill_h WHERE NoKayuBulat = ?";
        String nextNoPenerimaan = generateNextNoPenerimaanST();
        if (nextNoPenerimaan == null) return null;

        String insertHeader = "INSERT INTO PenerimaanSTSawmill_h (NoPenerimaanST, TglLaporan, NoKayuBulat) VALUES (?, ?, ?)";
        String selectDetails = "SELECT NoSTSawmill FROM STSawmill_h WHERE NoKayuBulat = ?";
        String insertDetail = "INSERT INTO PenerimaanSTSawmill_d (NoPenerimaanST, NoSTSawmill) VALUES (?, ?)";
        String updateDateUsage = "UPDATE KayuBulat_h SET DateUsage = ? WHERE NoKayuBulat = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl())) {

            // Cek apakah data sudah ada
            try (PreparedStatement checkStmt = con.prepareStatement(checkExisting)) {
                checkStmt.setString(1, noKayuBulat);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    return rs.getString("TglLaporan"); // Sudah ada, kembalikan TglLaporan
                }
            }

            con.setAutoCommit(false); // Mulai transaksi

            // Insert ke header
            try (PreparedStatement stmtHeader = con.prepareStatement(insertHeader)) {
                stmtHeader.setString(1, nextNoPenerimaan);
                stmtHeader.setString(2, tglLaporan);
                stmtHeader.setString(3, noKayuBulat);
                stmtHeader.executeUpdate();
            }

            // Ambil data detail STSawmill
            List<String> stsawmillList = new ArrayList<>();
            try (PreparedStatement stmtSelect = con.prepareStatement(selectDetails)) {
                stmtSelect.setString(1, noKayuBulat);
                ResultSet rs = stmtSelect.executeQuery();
                while (rs.next()) {
                    stsawmillList.add(rs.getString("NoSTSawmill"));
                }
            }

            // Insert ke detail
            try (PreparedStatement stmtDetail = con.prepareStatement(insertDetail)) {
                for (String noSTS : stsawmillList) {
                    stmtDetail.setString(1, nextNoPenerimaan);
                    stmtDetail.setString(2, noSTS);
                    stmtDetail.addBatch();
                }
                stmtDetail.executeBatch();
            }

            // Update DateUsage di KayuBulat_h
            try (PreparedStatement stmtUpdateUsage = con.prepareStatement(updateDateUsage)) {
                stmtUpdateUsage.setString(1, tglLaporan); // atau gunakan Timestamp sekarang
                stmtUpdateUsage.setString(2, noKayuBulat);
                stmtUpdateUsage.executeUpdate();
            }

            con.commit(); // Sukses, simpan transaksi
            return null;  // null artinya berhasil insert baru

        } catch (SQLException e) {
            Log.e("Insert Error", "Gagal simpan: " + e.getMessage());
            // Pastikan rollback jika gagal
            try (Connection conRollback = DriverManager.getConnection(DatabaseConfig.getConnectionUrl())) {
                conRollback.rollback();
            } catch (SQLException ex) {
                Log.e("Rollback Error", "Gagal rollback: " + ex.getMessage());
            }
            return null;
        }
    }




    public static String generateNextNoPenerimaanST() {
        String query = "SELECT MAX(NoPenerimaanST) AS LastNo FROM PenerimaanSTSawmill_h";
        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                String lastNo = rs.getString("LastNo"); // contoh: "B.000025"
                if (lastNo != null) {
                    int lastNumber = Integer.parseInt(lastNo.substring(2)); // ambil "000025" -> 25
                    int nextNumber = lastNumber + 1;
                    return String.format("B.%05d", nextNumber); // hasil: B.000026
                }
            }
        } catch (SQLException e) {
            Log.e("Generate No Error", "Gagal generate NoPenerimaanST: " + e.getMessage());
        }

        // Jika tidak ada data sebelumnya
        return "B.000001";
    }


    public static boolean deleteSawmillDetailItem(String noSTSawmill, int noUrut, boolean isRambung) {
        final String TAG = "SawmillDB-DeleteDetail";
        String deleteDetailQuery = "DELETE FROM STSawmill_d WHERE NoSTSawmill = ? AND NoUrut = ?";
        String deleteGradeQuery = "DELETE FROM STSawmillKG_d WHERE NoSTSawmill = ? AND NoUrut = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl())) {
            con.setAutoCommit(false);

            if (isRambung) {
                try (PreparedStatement stmt = con.prepareStatement(deleteGradeQuery)) {
                    stmt.setString(1, noSTSawmill);
                    stmt.setInt(2, noUrut);
                    int rowsDeleted = stmt.executeUpdate();
                    Log.d(TAG, "Deleted from STSawmillKG_d: " + rowsDeleted);
                }
            }

            try (PreparedStatement stmt = con.prepareStatement(deleteDetailQuery)) {
                stmt.setString(1, noSTSawmill);
                stmt.setInt(2, noUrut);
                int rowsDeleted = stmt.executeUpdate();
                Log.d(TAG, "Deleted from STSawmill_d: " + rowsDeleted);
            }

            con.commit();
            Log.i(TAG, "Berhasil hapus detail NoUrut=" + noUrut + " dari NoSTSawmill: " + noSTSawmill);
            return true;

        } catch (SQLException e) {
            Log.e(TAG, "SQL Error saat menghapus detail:", e);
            return false;
        }
    }


    public static void reorderNoUrut(String noSTSawmill, boolean isRambung) {
        final String TAG = "SawmillDB";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl())) {
            con.setAutoCommit(false);

            // Ambil semua data detail berdasarkan NoSTSawmill, urut sesuai ID atau timestamp insert
            String selectQuery = "SELECT * FROM STSawmill_d WHERE NoSTSawmill = ? ORDER BY NoUrut ASC";

            List<Integer> rowIds = new ArrayList<>();
            try (PreparedStatement selectStmt = con.prepareStatement(selectQuery)) {
                selectStmt.setString(1, noSTSawmill);
                ResultSet rs = selectStmt.executeQuery();
                while (rs.next()) {
                    rowIds.add(rs.getInt("NoUrut")); // bisa juga ID internal jika ada
                }
            }

            // Update ulang NoUrut agar berurutan mulai dari 1
            int newUrut = 1;
            for (int oldUrut : rowIds) {

                if (isRambung) {
                    try (PreparedStatement updateGradeStmt = con.prepareStatement(
                            "UPDATE STSawmillKG_d SET NoUrut = ? WHERE NoSTSawmill = ? AND NoUrut = ?")) {
                        updateGradeStmt.setInt(1, newUrut);
                        updateGradeStmt.setString(2, noSTSawmill);
                        updateGradeStmt.setInt(3, oldUrut);
                        updateGradeStmt.executeUpdate();
                    }
                }

                try (PreparedStatement updateStmt = con.prepareStatement(
                        "UPDATE STSawmill_d SET NoUrut = ? WHERE NoSTSawmill = ? AND NoUrut = ?")) {
                    updateStmt.setInt(1, newUrut);
                    updateStmt.setString(2, noSTSawmill);
                    updateStmt.setInt(3, oldUrut);
                    updateStmt.executeUpdate();
                }

                newUrut++;
            }

            con.commit();
            Log.d(TAG, "Berhasil reorder NoUrut untuk NoSTSawmill: " + noSTSawmill);

        } catch (SQLException e) {
            Log.e(TAG, "Gagal reorder NoUrut:", e);
        }
    }


    public static String getHourEndByTanggalShiftMeja(String tgl, String noMeja) {
        String hourEnd = "-"; // default jika HourStart null/kosong
        String query = "SELECT TOP 1 HourStart, HourEnd FROM STSawmill_h " +
                "WHERE TglSawmill = ? AND NoMeja = ? " +
                "ORDER BY NoSTSawmill DESC"; // ambil data terbaru jika ada duplikat

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, formatToDatabaseDate(tgl));
            stmt.setString(2, noMeja);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String hourStart = rs.getString("HourStart");
                String hourEndRaw = rs.getString("HourEnd");

                // Cek apakah HourStart tidak null dan tidak kosong
                if (hourStart != null && !hourStart.trim().isEmpty()) {
                    hourEnd = hourEndRaw; // gunakan HourEnd dari database
                }
            }
        } catch (SQLException e) {
            Log.e("SawmillApi", "Gagal mengambil HourEnd: " + e.getMessage());
        }
        Log.d("SawmillApi", "Returned HourEnd = " + hourEnd);

        return hourEnd;
    }


    public static boolean updateSawmillDetailItem(String noSTSawmill, int noUrut, SawmillDetailData data, boolean isRambung) {
        final String TAG = "SawmillDB-UpdateDetail";

        // Log semua parameter yang masuk
        Log.d(TAG, "PARAMETER MASUK:");
        Log.d(TAG, "noSTSawmill: " + noSTSawmill);
        Log.d(TAG, "noUrut: " + noUrut);
        Log.d(TAG, "isRambung: " + isRambung);

        if (data != null) {
            Log.d(TAG, "data.tebal: " + data.getTebal());
            Log.d(TAG, "data.lebar: " + data.getLebar());
            Log.d(TAG, "data.panjang: " + data.getPanjang());
            Log.d(TAG, "data.pcs: " + data.getPcs());
            Log.d(TAG, "data.isLocal: " + data.isLocal());
            Log.d(TAG, "data.idUOMTblLebar: " + data.getIdUOMTblLebar());
            Log.d(TAG, "data.idUOMPanjang: " + data.getIdUOMPanjang());
            Log.d(TAG, "data.isBagusKulit: " + data.getIsBagusKulit());
            Log.d(TAG, "data.idGradeKB: " + data.getIdGradeKB());
        } else {
            Log.e(TAG, "data object is NULL");
        }

        String updateDetailQuery = "UPDATE STSawmill_d SET Tebal = ?, Lebar = ?, Panjang = ?, JmlhBatang = ?, IsLocal = ?, IdUOMTblLebar = ?, IdUOMPanjang = ?, IsBagusKulit = ? WHERE NoSTSawmill = ? AND NoUrut = ?";
        String updateGradeQuery = "UPDATE STSawmillKG_d SET IdGradeKB = ? WHERE NoSTSawmill = ? AND NoUrut = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl())) {
            con.setAutoCommit(false);

            if (isRambung) {
                try (PreparedStatement stmt = con.prepareStatement(updateGradeQuery)) {
                    stmt.setInt(1, data.getIdGradeKB());
                    stmt.setString(2, noSTSawmill);
                    stmt.setInt(3, noUrut);
                    stmt.executeUpdate();
                }
            }

            try (PreparedStatement stmt = con.prepareStatement(updateDetailQuery)) {
                stmt.setFloat(1, data.getTebal());
                stmt.setFloat(2, data.getLebar());
                stmt.setFloat(3, data.getPanjang());
                stmt.setInt(4, data.getPcs());
                stmt.setBoolean(5, data.isLocal());
                stmt.setInt(6, data.getIdUOMTblLebar());
                stmt.setInt(7, data.getIdUOMPanjang());
                stmt.setInt(8, data.getIsBagusKulit());
                stmt.setString(9, noSTSawmill);
                stmt.setInt(10, noUrut);
                stmt.executeUpdate();
            }

            con.commit();
            Log.i(TAG, "Berhasil update detail NoUrut=" + noUrut + " untuk NoSTSawmill: " + noSTSawmill);
            return true;

        } catch (SQLException e) {
            Log.e(TAG, "SQL Error saat update detail:", e);
            return false;
        }
    }



    // GETTER DATA QC SAWMILL
    public static List<QcSawmillData> getQcSawmillData() {
        List<QcSawmillData> qcSawmillDataList = new ArrayList<>();

        String query = "SELECT TOP 50 h.NoQc, h.Tgl, h.IdJenisKayu, k.Jenis, h.Meja " +
                "FROM QcSawmill_h h " +
                "LEFT JOIN MstJenisKayu k ON h.IdJenisKayu = k.IdJenisKayu " +
                "ORDER BY h.NoQc DESC";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String noQc = rs.getString("NoQc");
                String tgl = rs.getString("Tgl");
                int idJenisKayu = rs.getInt("IdJenisKayu");
                String namaJenisKayu = rs.getString("Jenis");
                String meja = rs.getString("Meja");

                qcSawmillDataList.add(new QcSawmillData(noQc, tgl, idJenisKayu, namaJenisKayu, meja));
            }
        } catch (SQLException e) {
            Log.e("Database Fetch Error", "Error fetching QC Sawmill data: " + e.getMessage());
        }

        return qcSawmillDataList;
    }



    public static List<QcSawmillDetailData> fetchQcSawmillDetailByNoQc(String noQc) {
        List<QcSawmillDetailData> detailList = new ArrayList<>();

        String query = "SELECT NoQc, NoUrut, NoST, CuttingTebal, CuttingLebar, ActualTebal, ActualLebar, SusutTebal, SusutLebar " +
                "FROM QcSawmill_d WHERE NoQc = ? ORDER BY NoUrut";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, noQc);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    QcSawmillDetailData detail = new QcSawmillDetailData(
                            rs.getString("NoQc"),
                            rs.getInt("NoUrut"),
                            rs.getString("NoST"),
                            rs.getFloat("CuttingTebal"),
                            rs.getFloat("CuttingLebar"),
                            rs.getFloat("ActualTebal"),
                            rs.getFloat("ActualLebar"),
                            rs.getFloat("SusutTebal"),
                            rs.getFloat("SusutLebar")
                    );
                    detailList.add(detail);
                }
            }
        } catch (SQLException e) {
            Log.e("DB Fetch Error", "Error fetching QC Sawmill detail: " + e.getMessage());
        }

        return detailList;
    }

    public static void insertQcSawmillHeader(String noQc, String tgl, int idJenisKayu, String meja) throws SQLException {
        String query = "INSERT INTO QcSawmill_h (NoQc, Tgl, IdJenisKayu, Meja) VALUES (?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, noQc);
            stmt.setString(2, tgl);  // Pastikan format tgl sesuai database, misalnya yyyy-MM-dd
            stmt.setInt(3, idJenisKayu);
            stmt.setString(4, meja);

            stmt.executeUpdate();
        }
    }


    public static String getNextNoQc() {
        String query = "SELECT TOP 1 NoQc FROM QcSawmill_h WHERE NoQc LIKE 'M.%' ORDER BY NoQc DESC";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                String lastNo = rs.getString("NoQc");  // Misal: M.123456
                String numericPart = lastNo.substring(2);  // Ambil setelah "M." → "123456"
                int nextNumber = Integer.parseInt(numericPart) + 1;
                return String.format("M.%06d", nextNumber);  // Format: M.000001
            } else {
                return "M.000001";  // No pertama jika belum ada data
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static List<JenisKayuData> getJenisKayuList() {
        List<JenisKayuData> jenisKayuList = new ArrayList<>();

        String query = "SELECT IdJenisKayu, Jenis FROM MstJenisKayu WHERE Enable = 1 ORDER BY Jenis";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int idJenisKayu = rs.getInt("IdJenisKayu");
                String jenis = rs.getString("Jenis");

                JenisKayuData jenisKayuData = new JenisKayuData(idJenisKayu, jenis);
                jenisKayuList.add(jenisKayuData);
            }

        } catch (SQLException e) {
            Log.e("DB Fetch Error", "Error fetching Jenis Kayu: " + e.getMessage());
        }

        return jenisKayuList;
    }


    public static void insertQcSawmillDetailAutoNoUrut(
            String noQc,
            float cuttingTebal, float cuttingLebar,
            float actualTebal, float actualLebar,
            float susutTebal, float susutLebar
    ) throws SQLException {

        String selectQuery = "SELECT ISNULL(MAX(CAST(NoUrut AS INT)), 0) AS MaxNoUrut FROM QcSawmill_d WHERE NoQc = ?";
        String insertQuery = "INSERT INTO QcSawmill_d (NoQc, NoUrut, NoST, CuttingTebal, CuttingLebar, ActualTebal, ActualLebar, SusutTebal, SusutLebar) " +
                "VALUES (?, ?, NULL, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement selectStmt = con.prepareStatement(selectQuery)) {

            selectStmt.setString(1, noQc);
            ResultSet rs = selectStmt.executeQuery();

            int nextNoUrut = 1;
            if (rs.next()) {
                nextNoUrut = rs.getInt("MaxNoUrut") + 1;
            }

            try (PreparedStatement insertStmt = con.prepareStatement(insertQuery)) {
                insertStmt.setString(1, noQc);
                insertStmt.setInt(2, nextNoUrut);
                insertStmt.setFloat(3, cuttingTebal);
                insertStmt.setFloat(4, cuttingLebar);
                insertStmt.setFloat(5, actualTebal);
                insertStmt.setFloat(6, actualLebar);
                insertStmt.setFloat(7, susutTebal);
                insertStmt.setFloat(8, susutLebar);
                insertStmt.executeUpdate();
            }
        }
    }


    public static void deleteQcSawmillDetail(String noQc, int noUrut) throws SQLException {
        String deleteQuery = "DELETE FROM QcSawmill_d WHERE NoQc = ? AND NoUrut = ?";
        String reorderQuery = "WITH Ordered AS (" +
                "  SELECT NoQc, NoUrut, ROW_NUMBER() OVER (ORDER BY NoUrut ASC) AS NewNoUrut " +
                "  FROM QcSawmill_d WHERE NoQc = ?" +
                ") " +
                "UPDATE QcSawmill_d " +
                "SET NoUrut = Ordered.NewNoUrut " +
                "FROM QcSawmill_d INNER JOIN Ordered ON QcSawmill_d.NoQc = Ordered.NoQc AND QcSawmill_d.NoUrut = Ordered.NoUrut";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement deleteStmt = con.prepareStatement(deleteQuery);
             PreparedStatement reorderStmt = con.prepareStatement(reorderQuery)) {

            // Eksekusi DELETE
            deleteStmt.setString(1, noQc);
            deleteStmt.setInt(2, noUrut);
            deleteStmt.executeUpdate();

            // Eksekusi REORDER NoUrut
            reorderStmt.setString(1, noQc);
            reorderStmt.executeUpdate();
        }
    }

    public static void updateQcSawmillDetail(String noQc, int noUrut, float cuttingTebal, float cuttingLebar,
                                             float actualTebal, float actualLebar, float susutTebal, float susutLebar) throws SQLException {
        String query = "UPDATE QcSawmill_d SET " +
                "CuttingTebal = ?, CuttingLebar = ?, " +
                "ActualTebal = ?, ActualLebar = ?, " +
                "SusutTebal = ?, SusutLebar = ? " +
                "WHERE NoQc = ? AND NoUrut = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setFloat(1, cuttingTebal);
            ps.setFloat(2, cuttingLebar);
            ps.setFloat(3, actualTebal);
            ps.setFloat(4, actualLebar);
            ps.setFloat(5, susutTebal);
            ps.setFloat(6, susutLebar);
            ps.setString(7, noQc);
            ps.setInt(8, noUrut);

            ps.executeUpdate();
        }
    }

    public static void updateQcSawmillHeader(String noQc, String tgl, int idJenisKayu, String meja) throws SQLException {
        String query = "UPDATE QcSawmill_h SET Tgl = ?, IdJenisKayu = ?, Meja = ? WHERE NoQc = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, tgl);  // Pastikan format tgl sesuai dengan database
            stmt.setInt(2, idJenisKayu);
            stmt.setString(3, meja);
            stmt.setString(4, noQc); // Gunakan NoQc sebagai key untuk update

            stmt.executeUpdate();
        }
    }


    public static boolean deleteQcSawmillHeader(String noQc) throws SQLException {
        String deleteDetailQuery = "DELETE FROM QcSawmill_d WHERE NoQc = ?";
        String deleteHeaderQuery = "DELETE FROM QcSawmill_h WHERE NoQc = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl())) {
            con.setAutoCommit(false); // Mulai transaksi

            try (
                    PreparedStatement stmtDetail = con.prepareStatement(deleteDetailQuery);
                    PreparedStatement stmtHeader = con.prepareStatement(deleteHeaderQuery)
            ) {
                // Hapus detail dulu
                stmtDetail.setString(1, noQc);
                stmtDetail.executeUpdate();

                // Baru hapus header
                stmtHeader.setString(1, noQc);
                int rowsAffected = stmtHeader.executeUpdate();

                con.commit();
                return rowsAffected > 0;
            } catch (SQLException ex) {
                con.rollback(); // Rollback kalau gagal
                throw ex;
            }
        }
    }


    public static List<PenerimaanSTSawmillData> getPenerimaanSTSawmillData() {
        List<PenerimaanSTSawmillData> dataList = new ArrayList<>();

        String query = "SELECT TOP 100 NoPenerimaanST, TglLaporan, NoKayuBulat FROM PenerimaanSTSawmill_h ORDER BY NoPenerimaanST DESC";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String noPenerimaanST = rs.getString("NoPenerimaanST");
                String tglLaporan = rs.getString("TglLaporan");
                String noKayuBulat = rs.getString("NoKayuBulat");

                dataList.add(new PenerimaanSTSawmillData(noPenerimaanST, tglLaporan, noKayuBulat));
            }

        } catch (SQLException e) {
            Log.e("Database Error", "Error fetching ST Sawmill data: " + e.getMessage());
        }

        return dataList;
    }

    public static List<String> getSTSawmillListByNoPenerimaanST(String noPenerimaanST) {
        List<String> noSTSawmillList = new ArrayList<>();

        String query = "SELECT NoSTSawmill FROM PenerimaanSTSawmill_d WHERE NoPenerimaanST = ?";
        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, noPenerimaanST); // Set parameter

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String noSTSawmill = rs.getString("NoSTSawmill");
                    noSTSawmillList.add(noSTSawmill);

                    Log.d("Database Data", "NoSTSawmill: " + noSTSawmill);
                }
            }

        } catch (SQLException e) {
            Log.e("Database Fetch Error", "Error fetching NoSTSawmill: " + e.getMessage());
        }

        return noSTSawmillList;
    }








}
