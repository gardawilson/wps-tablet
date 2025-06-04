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


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


public class SawmillApi {

    // GETTER DAN SETTER DATA SAWMILL
    public static List<SawmillData> getSawmillData() {
        List<SawmillData> sawmillDataList = new ArrayList<>();

        String query = "SELECT TOP 50 " +
                "h.NoSTSawmill, " +
                "h.TglSawmill, " +
                "h.NoKayuBulat, " +
                "h.NoMeja, " +
                "CASE " +
                "WHEN op2.NamaOperator IS NULL THEN op1.NamaOperator " +
                "ELSE op1.NamaOperator + ' - ' + op2.NamaOperator " +
                "END AS Operator, " +
                "h.IdSawmillSpecialCondition, " +
                "h.BalokTerpakai, " +
                "h.JamKerja, " +
                "h.JlhBatangRajang, " +
                "h.HourMeter, " +
                "h.Remark " +
                "FROM STSawmill_h h " +
                "LEFT JOIN MstOperator op1 ON h.IdOperator1 = op1.IdOperator " +
                "LEFT JOIN MstOperator op2 ON h.IdOperator2 = op2.IdOperator " +
                "ORDER BY h.NoSTSawmill DESC";


        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String noSTSawmill = rs.getString("NoSTSawmill");
                String tglSawmill = rs.getString("TglSawmill");
                String noKayuBulat = rs.getString("NoKayuBulat");
                String noMeja = rs.getString("NoMeja");
                String operator = rs.getString("Operator"); // hasil dari Operator1 + ' - ' + Operator2
                int idSpecial = rs.getInt("IdSawmillSpecialCondition");
                String balokTerpakai = rs.getString("BalokTerpakai");
                String jamKerja = rs.getString("JamKerja");
                int jlhBatangRajang = rs.getInt("JlhBatangRajang");
                String hourMeter = rs.getString("HourMeter");
                String remark = rs.getString("Remark");

                sawmillDataList.add(new SawmillData(noSTSawmill, tglSawmill, noKayuBulat, noMeja, operator,
                        idSpecial, balokTerpakai, jamKerja, jlhBatangRajang, hourMeter, remark));
            }

        } catch (SQLException e) {
            Log.e("Database Fetch Error", "Error fetching sawmill data: " + e.getMessage());
        }

        return sawmillDataList;
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

        String query = "SELECT NoUrut, Tebal, Lebar, Panjang, JmlhBatang, " +
                "IsLocal, IdUOMTblLebar, IdUOMPanjang, IsBagusKulit " +
                "FROM STSawmill_d WHERE NoSTSawmill = ?";

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
                            rs.getInt("IsBagusKulit") // Pakai getInt, bukan getBoolean
                    );
                    itemList.add(item);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return itemList;
    }


    public static float fetchTotalTon(String noSTSawmill) {
        float totalTon = 0f;

        String query = "SELECT SUM(CASE " +
                "WHEN A.IdUOMTblLebar = '1' AND A.IdUOMPanjang = '4' THEN " +
                "    FLOOR(A.Tebal * A.Lebar * A.Panjang * 304.8 * A.JmlhBatang / 1000000000 / 1.416 * 10000) / 10000 " +
                "WHEN A.IdUOMTblLebar = '3' AND A.IdUOMPanjang = '4' THEN " +
                "    FLOOR(A.Tebal * A.Lebar * A.Panjang * A.JmlhBatang / 7200.8 * 10000) / 10000 " +
                "END) AS Ton " +
                "FROM STSawmill_d A " +
                "WHERE NoSTSawmill = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, noSTSawmill);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    totalTon = rs.getFloat("Ton");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalTon;
    }









}
