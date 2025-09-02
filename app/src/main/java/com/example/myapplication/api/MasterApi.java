package com.example.myapplication.api;

import com.example.myapplication.config.DatabaseConfig;
import com.example.myapplication.model.MstFisikData;
import com.example.myapplication.model.MstGradeData;
import com.example.myapplication.model.MstJenisKayuData;
import com.example.myapplication.model.LokasiData;
import com.example.myapplication.model.MstBjData;
import com.example.myapplication.model.MstProfileData;
import com.example.myapplication.model.MstSpkData;
import com.example.myapplication.model.MstSusunData;
import com.example.myapplication.model.TellyData;
import com.example.myapplication.model.MstWarnaData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MasterApi {

    public static List<MstWarnaData> getWarnaList() {
        List<MstWarnaData> warnaList = new ArrayList<>();
        String query = "SELECT IdWarna, NamaWarna FROM MstWarna";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("IdWarna");
                String nama = rs.getString("NamaWarna");

                warnaList.add(new MstWarnaData(id, nama));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return warnaList;
    }


    public static List<LokasiData> getLokasiList() {
        List<LokasiData> lokasiList = new ArrayList<>();
        String query = "SELECT IdLokasi, Description, Enable, Blok FROM MstLokasi";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String id = rs.getString("IdLokasi"); // Ubah dari getInt ke getString
                String description = rs.getString("Description");
                boolean enable = rs.getBoolean("Enable");
                String blok = rs.getString("Blok");

                lokasiList.add(new LokasiData(id, description, enable, blok));
            }

        } catch (SQLException e) {
            System.err.println("[DB_ERROR] Terjadi kesalahan saat mengambil data lokasi:");
            e.printStackTrace();
        }

        return lokasiList;
    }


    public static List<MstGradeData> getGradeList(int idJenisKayu, String category) {
        List<MstGradeData> gradeList = new ArrayList<>();
        String query = "SELECT DISTINCT a.IdGrade, a.NamaGrade " +
                "FROM MstGrade a " +
                "INNER JOIN MstGrade_d b ON a.IdGrade = b.IdGrade " +
                "WHERE a.Enable = 1 AND b.IdJenisKayu = ? AND b.Category = ? " +
                "ORDER BY a.NamaGrade ASC";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, idJenisKayu);
            ps.setString(2, category);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int idGrade = rs.getInt("IdGrade");
                    String namaGrade = rs.getString("NamaGrade");
                    gradeList.add(new MstGradeData(idGrade, namaGrade));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return gradeList;
    }


    // Di class MasterApi atau helper model terpisah
    public static List<MstJenisKayuData> getJenisKayuList() {
        List<MstJenisKayuData> jenisKayuList = new ArrayList<>();
        String query = "SELECT IdJenisKayu, Jenis, IsUpah FROM dbo.MstJenisKayu " +
                "WHERE Enable = 1 AND IsInternal = 1 AND IsNonST = 1";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int idJenisKayu = rs.getInt("IdJenisKayu");
                String namaJenisKayu = rs.getString("Jenis");
                int isUpah = rs.getInt("IsUpah");

                jenisKayuList.add(new MstJenisKayuData(idJenisKayu, namaJenisKayu, isUpah));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return jenisKayuList;
    }


    public static List<MstJenisKayuData> getJenisKayuSTList() {
        List<MstJenisKayuData> jenisKayuList = new ArrayList<>();
        String query = "SELECT IdJenisKayu, Jenis, IsUpah FROM dbo.MstJenisKayu WHERE IsST = 1";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int idJenisKayu = rs.getInt("IdJenisKayu");
                String namaJenisKayu = rs.getString("Jenis");
                int isUpah = rs.getInt("IsUpah");

                jenisKayuList.add(new MstJenisKayuData(idJenisKayu, namaJenisKayu, isUpah));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return jenisKayuList;
    }


    public static String getIdLokasi(int idJenisKayu, int idGrade, int idWarna) {
        String idLokasi = "";
        String query = "SELECT IdLokasi FROM MstBagianJenisKayuGradeWarnaToIDLocation " +
                "WHERE IdJenisKayu = ? AND IdGrade = ? AND (CASE WHEN ? = 0 THEN 0 ELSE 1 END) = CASE WHEN Warna = 0 THEN 0 ELSE 1 END";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, idJenisKayu);
            ps.setInt(2, idGrade);
            ps.setInt(3, idWarna);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    idLokasi = rs.getString("IdLokasi");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idLokasi;
    }



    public static List<MstSpkData> getSPKList() {
        List<MstSpkData> spkList = new ArrayList<>();
        String query = "SELECT s.NoSPK, b.Buyer " +
                "FROM MstSPK_h s " +
                "INNER JOIN MstBuyer b ON s.IdBuyer = b.IdBuyer " +
                "WHERE s.enable = 1";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String noSPK = rs.getString("NoSPK");
                String buyer = rs.getString("Buyer");

                spkList.add(new MstSpkData(noSPK, buyer));
            }

        } catch (SQLException e) {
            System.err.println("[DB_ERROR] Gagal mengambil data SPK:");
            e.printStackTrace();
        }

        return spkList;
    }


    public static List<MstSusunData> getSusunList(String selectedDate) {
        List<MstSusunData> susunList = new ArrayList<>();

        String query = "SELECT NoBongkarSusun " +
                "FROM dbo.BongkarSusun_h " +
                "WHERE CONVERT(date, Tanggal) = CONVERT(date, ?)";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, selectedDate);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String noBongkarSusun = rs.getString("NoBongkarSusun");
                    susunList.add(new MstSusunData(noBongkarSusun));
                }
            }

        } catch (SQLException e) {
            System.err.println("[DB_ERROR] Gagal ambil data Susun:");
            e.printStackTrace();
        }

        return susunList;
    }


    public static boolean isPeriodValid(String dateToCheck) {
        String query1 = "SELECT MAX(Period) as max_period FROM MstTutupTransaksi";
        String query2 = "SELECT MAX(PeriodHarian) as max_period FROM MstTutupTransaksiHarian";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl())) {
            Date inputDate = sdf.parse(dateToCheck);

            // Cek bulanan
            try (PreparedStatement ps1 = con.prepareStatement(query1);
                 ResultSet rs1 = ps1.executeQuery()) {
                if (rs1.next()) {
                    String maxPeriodStr = rs1.getString("max_period");
                    if (maxPeriodStr != null) {
                        Date maxPeriod = sdf.parse(maxPeriodStr);
                        if (inputDate.before(maxPeriod) || inputDate.equals(maxPeriod)) {
                            return false; // ditutup
                        }
                    }
                }
            }

            // Cek harian
            try (PreparedStatement ps2 = con.prepareStatement(query2);
                 ResultSet rs2 = ps2.executeQuery()) {
                if (rs2.next()) {
                    String maxPeriodStr = rs2.getString("max_period");
                    if (maxPeriodStr != null) {
                        Date maxPeriodHarian = sdf.parse(maxPeriodStr);
                        if (inputDate.before(maxPeriodHarian) || inputDate.equals(maxPeriodHarian)) {
                            return false; // ditutup
                        }
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("[DB_ERROR] Gagal cek periode transaksi:");
            e.printStackTrace();
            return false; // kalau error dianggap tidak valid
        }

        return true; // kalau lolos semua -> masih valid
    }

    public static List<TellyData> getTellyList(String username) {
        List<TellyData> tellyList = new ArrayList<>();

        String query = "SELECT A.IdOrgTelly, A.NamaOrgTelly " +
                "FROM MstOrgTelly A " +
                "INNER JOIN ( " +
                "    SELECT Username, FName + ' ' + LName AS NamaTelly " +
                "    FROM MstUsername " +
                "    WHERE Username = ? " +
                ") B ON B.NamaTelly = A.NamaOrgTelly " +
                "WHERE A.Enable = 1";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String idOrgTelly = rs.getString("IdOrgTelly");
                    String namaOrgTelly = rs.getString("NamaOrgTelly");

                    tellyList.add(new TellyData(idOrgTelly, namaOrgTelly));
                }
            }

        } catch (SQLException e) {
            System.err.println("[DB_ERROR] Gagal ambil data Telly:");
            e.printStackTrace();
        }

        return tellyList;
    }

    public static List<MstFisikData> getFisikList(int IdWarehouse) {
        List<MstFisikData> fisikList = new ArrayList<>();

        String query = "SELECT Singkatan FROM dbo.MstWarehouse WHERE IdWarehouse = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, IdWarehouse); // <-- ini wajib

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String singkatan = rs.getString("Singkatan");
                    fisikList.add(new MstFisikData(singkatan));
                }
            }

        } catch (SQLException e) {
            System.err.println("[DB_ERROR] Gagal ambil data fisik:");
            e.printStackTrace();
        }

        return fisikList;
    }


    public static List<MstProfileData> getProfileList() {
        List<MstProfileData> profileList = new ArrayList<>();

        String query = "SELECT Profile, IdFJProfile FROM dbo.MstFJProfile WHERE IdFJProfile != 0";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String namaProfile = rs.getString("Profile");
                String idFJProfile = rs.getString("IdFJProfile");
                profileList.add(new MstProfileData(namaProfile, idFJProfile));
            }

        } catch (SQLException e) {
            System.err.println("[DB_ERROR] Gagal ambil data profile:");
            e.printStackTrace();
        }

        return profileList;
    }

    public static List<MstBjData> getBarangJadiList() {
        List<MstBjData> bjList = new ArrayList<>();

        String query = "SELECT IdBarangJadi, NamaBarangJadi " +
                "FROM dbo.MstBarangJadi " +
                "WHERE Enable = 1 " +
                "ORDER BY NamaBarangJadi ASC";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int idBarangJadi = rs.getInt("IdBarangJadi");
                String namaBarangJadi = rs.getString("NamaBarangJadi");
                bjList.add(new MstBjData(idBarangJadi, namaBarangJadi));
            }

        } catch (SQLException e) {
            System.err.println("[DB_ERROR] Gagal ambil data barang jadi:");
            e.printStackTrace();
        }

        return bjList;
    }




}
