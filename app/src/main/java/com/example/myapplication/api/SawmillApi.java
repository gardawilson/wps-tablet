package com.example.myapplication.api;

import static com.example.myapplication.utils.DateTimeUtils.formatToDatabaseDate;

import android.util.Log;

import com.example.myapplication.DatabaseConfig;
import com.example.myapplication.model.KayuBulatData;
import com.example.myapplication.model.OperatorData;
import com.example.myapplication.model.SawmillData;
import com.example.myapplication.model.SawmillDetailData;
import com.example.myapplication.model.SpecialConditionData;
import com.example.myapplication.model.SupplierData;
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
        String query = "SELECT TOP 1 [PeriodHarian] FROM [WPS_Test].[dbo].[MstTutupTransaksiHarian] ORDER BY PeriodHarian DESC";

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
    public static List<SawmillData> getSawmillData() {
        List<SawmillData> sawmillDataList = new ArrayList<>();

        // Ambil data stok berdasarkan jenis kayu
        Map<String, Integer> stokMapUmum = getTotalStokTersediaPerKayuBulat();
        Map<String, Integer> stokMapRambung = getTotalStokTersediaPerKayuBulatRambung();

        String query = "SELECT TOP 20 " +
                "h.NoSTSawmill, h.Shift, h.TglSawmill, h.NoKayuBulat, h.NoMeja, " +
                "CASE " +
                "WHEN op2.NamaOperator IS NULL THEN op1.NamaOperator " +
                "ELSE op1.NamaOperator + '/' + op2.NamaOperator " +
                "END AS Operator, " +
                "h.IdSawmillSpecialCondition, h.BalokTerpakai, h.JamKerja, h.JlhBatangRajang, " +
                "h.HourMeter, h.Remark, jk.Jenis AS NamaJenisKayu, " +
                "h.HourStart, h.HourEnd, h.IdOperator1, h.IdOperator2, " +
                "ISNULL((SELECT SUM(Berat) FROM STSawmill_dBalokTim d WHERE d.NoSTSawmill = h.NoSTSawmill), 0) AS BeratBalokTim " +
                "FROM STSawmill_h h " +
                "LEFT JOIN MstOperator op1 ON h.IdOperator1 = op1.IdOperator " +
                "LEFT JOIN MstOperator op2 ON h.IdOperator2 = op2.IdOperator " +
                "LEFT JOIN KayuBulat_h kb ON h.NoKayuBulat = kb.NoKayuBulat " +
                "LEFT JOIN MstJenisKayu jk ON kb.IdJenisKayu = jk.IdJenisKayu " +
                "ORDER BY h.NoSTSawmill DESC";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
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
                        beratBalokTim, hourStart, hourEnd, idOperator1, idOperator2
                );

                sawmillDataList.add(data);
            }

        } catch (SQLException e) {
            Log.e("Database Fetch Error", "Error fetching sawmill data: " + e.getMessage());
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


    public static List<String> getNoKayuBulatSuggestions(String keyword) {
        List<String> suggestions = new ArrayList<>();

        String query = "SELECT TOP 10 NoKayuBulat FROM KayuBulat_h WHERE NoKayuBulat LIKE ? AND DateUsage IS NULL ORDER BY NoKayuBulat ASC";

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
                "WHERE kb.NoKayuBulat = ? AND kb.DateUsage IS NULL";

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
            stmt.setString(14, jamBerhenti);
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


    public static boolean isHourRangeOverlapping(String tglSawmill, String noMeja, String newHourStart, String newHourEnd) {
        final String TAG = "CheckOverlap";
        String formattedTgl = formatToDatabaseDate(tglSawmill);

        String query = "SELECT HourStart, HourEnd FROM STSawmill_h WHERE TglSawmill = ? AND NoMeja = ?";
        boolean isOverlap = false;

        Log.d(TAG, "Mulai cek overlap untuk tanggal: " + formattedTgl + ", meja: " + noMeja + ", mulai: " + newHourStart + ", selesai: " + newHourEnd);

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

                    Log.d(TAG, "Membandingkan dengan record: Start=" + dbStart + ", End=" + dbEnd);

                    // Cek overlap
                    if (!(newEnd.isBefore(dbStart) || newStart.isAfter(dbEnd))) {
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
                        "WHERE d.NoSTSawmill = ?";

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
                // Priority 0: Sort by isBagusKulit with custom order
                int kulitOrderA = getIsBagusKulitPriority(a.getIsBagusKulit());
                int kulitOrderB = getIsBagusKulitPriority(b.getIsBagusKulit());

                if (kulitOrderA != kulitOrderB) {
                    return Integer.compare(kulitOrderA, kulitOrderB);
                }

                // Priority 1: Sort by grade name
                int gradeOrderA = getGradePriority(a.getNamaGrade(), a.getIsBagusKulit());
                int gradeOrderB = getGradePriority(b.getNamaGrade(), b.getIsBagusKulit());

                if (gradeOrderA != gradeOrderB) {
                    return Integer.compare(gradeOrderA, gradeOrderB);
                }

                // Priority 2: Sort by thickness (descending)
                return Float.compare(b.getTebal(), a.getTebal());
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

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl())) {

            // Cek apakah sudah ada sebelumnya
            try (PreparedStatement checkStmt = con.prepareStatement(checkExisting)) {
                checkStmt.setString(1, noKayuBulat);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    return rs.getString("TglLaporan"); // Sudah ada → return tanggal
                }
            }

            con.setAutoCommit(false);

            // Insert ke header
            try (PreparedStatement stmtHeader = con.prepareStatement(insertHeader)) {
                stmtHeader.setString(1, nextNoPenerimaan);
                stmtHeader.setString(2, tglLaporan);
                stmtHeader.setString(3, noKayuBulat);
                stmtHeader.executeUpdate();
            }

            // Ambil NoSTSawmill untuk detail
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

            con.commit();
            return null; // null artinya insert baru berhasil

        } catch (SQLException e) {
            Log.e("Insert Error", "Gagal simpan: " + e.getMessage());
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
                try (PreparedStatement updateStmt = con.prepareStatement(
                        "UPDATE STSawmill_d SET NoUrut = ? WHERE NoSTSawmill = ? AND NoUrut = ?")) {
                    updateStmt.setInt(1, newUrut);
                    updateStmt.setString(2, noSTSawmill);
                    updateStmt.setInt(3, oldUrut);
                    updateStmt.executeUpdate();
                }

                if (isRambung) {
                    try (PreparedStatement updateGradeStmt = con.prepareStatement(
                            "UPDATE STSawmillKG_d SET NoUrut = ? WHERE NoSTSawmill = ? AND NoUrut = ?")) {
                        updateGradeStmt.setInt(1, newUrut);
                        updateGradeStmt.setString(2, noSTSawmill);
                        updateGradeStmt.setInt(3, oldUrut);
                        updateGradeStmt.executeUpdate();
                    }
                }

                newUrut++;
            }

            con.commit();
            Log.d(TAG, "Berhasil reorder NoUrut untuk NoSTSawmill: " + noSTSawmill);

        } catch (SQLException e) {
            Log.e(TAG, "Gagal reorder NoUrut:", e);
        }
    }







}
