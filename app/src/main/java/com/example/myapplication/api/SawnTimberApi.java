package com.example.myapplication.api;

import android.util.Log;

import com.example.myapplication.config.DatabaseConfig;
import com.example.myapplication.model.CustomerData;
import com.example.myapplication.model.GradeDetailData;
import com.example.myapplication.model.LabelDetailData;
import com.example.myapplication.model.MstGradeStickData;
import com.example.myapplication.model.MstStickData;
import com.example.myapplication.model.OutputDataST;
import com.example.myapplication.model.STPembelianData;
import com.example.myapplication.model.STPembelianDataReject;
import com.example.myapplication.model.STUpahData;
import com.example.myapplication.model.StData;
import com.example.myapplication.model.SupplierData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SawnTimberApi {

    // GETTER DAN SETTER DATA PEMBELIAN ST
    public static List<STPembelianData> getSTPembelianData() {
        List<STPembelianData> pembelianSTDataList = new ArrayList<>();

        // Query SQL dengan nama tabel dinamis
        String query = "SELECT TOP 50 " +
                "p.NoPenerimaanST, " +
                "p.TglLaporan, " +
                "p.TglMasuk, " +
                "s.NmSupplier, " +
                "p.NoTruk, " +
                "p.NoPlat, " +
                "p.Note, " +
                "p.TonSJ " +
                "FROM PenerimaanSTPembelian_h p " +
                "JOIN MstSupplier s ON p.IdSupplier = s.IdSupplier " +
                "ORDER BY p.NoPenerimaanST DESC";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                // Ambil data dari ResultSet
                String noSTPembelian = rs.getString("NoPenerimaanST");
                String tglLaporan = rs.getString("TglLaporan");
                String tglMasuk = rs.getString("TglMasuk");
                String supplier = rs.getString("NmSupplier");
                String noTruk = rs.getString("NoTruk");
                String noPlat = rs.getString("NoPlat");
                String keteranganPembelian = rs.getString("Note");
                String tonSJ = rs.getString("TonSJ");

                // Tambahkan ke list ProductionData
                pembelianSTDataList.add(new STPembelianData(noSTPembelian, tglLaporan, tglMasuk, supplier, noTruk, noPlat, keteranganPembelian, tonSJ));
            }
        } catch (SQLException e) {
            Log.e("Database Fetch Error", "Error fetching data: " + e.getMessage());
        }

        return pembelianSTDataList;
    }

    // GETTER DAN SETTER DATA PEMBELIAN ST
    public static List<STUpahData> getSTUpahData() {
        List<STUpahData> upahSTDataList = new ArrayList<>();

        // Query SQL dengan nama tabel dinamis
        String query = "SELECT TOP 50 " +
                "p.NoPenerimaanST, " +
                "p.TglMasuk, " +
                "s.NamaCustomer, " +
                "p.NoPlat, " +
                "p.NoTruk, " +
                "p.NoSJ, " +
                "p.Note " +
                "FROM PenerimaanSTUpah_h p " +
                "JOIN MstCustomerUpah s ON p.IdCustomer = s.IdCustomer " +
                "ORDER BY p.NoPenerimaanST DESC";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                // Ambil data dari ResultSet
                String noSTUpah = rs.getString("NoPenerimaanST");
                String tglMasuk = rs.getString("TglMasuk");
                String supplier = rs.getString("NamaCustomer");
                String noPlat = rs.getString("NoPlat");
                String noTruk = rs.getString("NoTruk");
                String NoSJ = rs.getString("NoSJ");
                String keteranganUpah = rs.getString("Note");

                // Tambahkan ke list ProductionData
                upahSTDataList.add(new STUpahData(noSTUpah, tglMasuk, supplier, noPlat, noTruk, NoSJ, keteranganUpah));
            }
        } catch (SQLException e) {
            Log.e("Database Fetch Error", "Error fetching data: " + e.getMessage());
        }

        return upahSTDataList;
    }


    public static List<String> getNonRejectListByNoPenST(String noPenerimaanST) {
        List<String> nonRejectList = new ArrayList<>();

        String query = "SELECT NoST FROM PenerimaanSTPembelian_d WHERE NoPenerimaanST = ?";
        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, noPenerimaanST); // Set nilai parameter
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String noST = rs.getString("NoST");
                    nonRejectList.add(noST); // Tambahkan NoS4S ke dalam daftar

                    Log.d("Database Data", "NoST: " + noST);
                }
            }
        } catch (SQLException e) {
            Log.e("Database Fetch Error", "Error fetching NoS4S: " + e.getMessage());
        }
        return nonRejectList;
    }

    public static List<String> getSTListBySTUpah(String noPenerimaanST) {
        List<String> noSTList = new ArrayList<>();

        String query = "SELECT NoST FROM PenerimaanSTUpah_d WHERE NoPenerimaanST = ?";
        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, noPenerimaanST); // Set nilai parameter
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String noST = rs.getString("NoST");
                    noSTList.add(noST);

                    Log.d("Database Data", "NoST: " + noST);
                }
            }
        } catch (SQLException e) {
            Log.e("Database Fetch Error", "Error fetching NoS4S: " + e.getMessage());
        }
        return noSTList;
    }


    public static List<STPembelianDataReject> fetchRejectDataByNoPenerimaanST(String noPenerimaanST) {
        List<STPembelianDataReject> rejectDataList = new ArrayList<>();
        // Query untuk mengambil data berdasarkan NoPenerimaanST
        String query = "SELECT NoUrut, Tebal, Lebar, Panjang, JmlhBatang, IdUOMTblLebar, IdUOMPanjang, Ton "
                + "FROM PenerimaanSTPembelian_Reject WHERE NoPenerimaanST = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            // Menetapkan parameter NoPenerimaanST pada query
            stmt.setString(1, noPenerimaanST);

            try (ResultSet rs = stmt.executeQuery()) {
                // Memproses hasil query
                while (rs.next()) {
                    // Membaca setiap kolom dan mengonversinya menjadi objek STPembelianDataReject
                    STPembelianDataReject data = new STPembelianDataReject(
                            rs.getString("NoUrut"),               // NoUrut
                            rs.getFloat("Tebal"),                 // Tebal
                            rs.getFloat("Lebar"),                 // Lebar
                            rs.getFloat("Panjang"),               // Panjang
                            rs.getInt("JmlhBatang"),              // JmlhBatang
                            rs.getInt("IdUOMTblLebar"),           // IdUOMTblLebar
                            rs.getInt("IdUOMPanjang"),            // IdUOMPanjang
                            rs.getFloat("Ton")                    // Ton
                    );
                    // Menambahkan data ke dalam daftar
                    rejectDataList.add(data);
                }
            }

        } catch (SQLException e) {
            // Menangani error
            e.printStackTrace();
        }

        // Mengembalikan daftar data
        return rejectDataList;
    }

    public static List<SupplierData> getSupplierList() {
        List<SupplierData> supplierList = new ArrayList<>();
        String query = "SELECT IdSupplier, NmSupplier FROM MstSupplier WHERE enable = 1";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            // Proses hasil query
            while (rs.next()) {
                int idSupplier = rs.getInt("IdSupplier");
                String nmSupplier = rs.getString("NmSupplier");

                // Membuat objek Supplier dan menambahkannya ke dalam list
                supplierList.add(new SupplierData(idSupplier, nmSupplier));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return supplierList;
    }

    public static List<CustomerData> getCustomerList() {
        List<CustomerData> customerList = new ArrayList<>();
        String query = "SELECT IdCustomer, NamaCustomer FROM MstCustomerUpah";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            // Proses hasil query
            while (rs.next()) {
                int idCustomer = rs.getInt("IdCustomer");
                String namaCustomer = rs.getString("NamaCustomer");

                // Membuat objek Supplier dan menambahkannya ke dalam list
                customerList.add(new CustomerData(idCustomer, namaCustomer));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customerList;
    }

    // Method untuk mendapatkan NoPenerimaanST terakhir dan menghasilkan yang baru
    public static String getNextNoPenerimaanST() {
        String query = "SELECT TOP 1 NoPenerimaanST FROM PenerimaanSTPembelian_h ORDER BY NoPenerimaanST DESC";
        String lastNoPenerimaanST = null;

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                lastNoPenerimaanST = rs.getString("NoPenerimaanST");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (lastNoPenerimaanST != null && lastNoPenerimaanST.startsWith("BA.")) {
            String numericPart = lastNoPenerimaanST.substring(3);  // Mengambil bagian angka setelah "B."
            int nextNumber = Integer.parseInt(numericPart) + 1;  // Menambah 1 pada angka terakhir
            return "BA." + String.format("%06d", nextNumber);  // Format dengan 6 digit angka
        } else {
            // Jika NoPenerimaanST tidak ditemukan atau formatnya tidak sesuai, kembalikan nomor default
            return null;
        }
    }

    public static String getNextNoPenerimaanSTUpah() {
        String query = "SELECT TOP 1 NoPenerimaanST FROM PenerimaanSTUpah_h ORDER BY NoPenerimaanST DESC";
        String lastNoPenerimaanST = null;

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                lastNoPenerimaanST = rs.getString("NoPenerimaanST");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (lastNoPenerimaanST != null && lastNoPenerimaanST.startsWith("O.")) {
            String numericPart = lastNoPenerimaanST.substring(2);  // Mengambil bagian angka setelah "O."
            int nextNumber = Integer.parseInt(numericPart) + 1;  // Menambah 1 pada angka terakhir
            return "O." + String.format("%06d", nextNumber);  // Format dengan 6 digit angka
        } else {
            // Jika NoPenerimaanST tidak ditemukan atau formatnya tidak sesuai, kembalikan nomor default
            return null;
        }
    }

    // Method untuk memastikan format tanggal yang benar
    private static String formatDate(String dateStr) {
        try {
            // Menentukan format input yang diterima (misalnya dd/MM/yyyy)
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = inputFormat.parse(dateStr);  // Parsing string tanggal yang diterima

            // Menentukan format output yang diinginkan untuk SQL (yyyy-MM-dd)
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
            return outputFormat.format(date);  // Mengembalikan tanggal dengan format yang sesuai
        } catch (Exception e) {
            e.printStackTrace();
            return null;  // Kembalikan null jika terjadi kesalahan format
        }
    }

    public static void insertDataToPembelian(final String noPenerimaanST, final String tglLaporan, final String tglMasuk, final int idSupplier,
                                            final String noTruk, final String noPlat, final String suket, final String tonSJ, final String note) {
        String query = "INSERT INTO PenerimaanSTPembelian_h " +
                "(NoPenerimaanST, TglLaporan, TglMasuk, IdSupplier, NoTruk, NoPlat, Suket, TonSJ, Note) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            // Mengonversi tanggal ke format yang sesuai
            String formattedTglLaporan = formatDate(tglLaporan);
            String formattedTglMasuk = formatDate(tglMasuk);

            // Menetapkan parameter query
            stmt.setString(1, noPenerimaanST);  // NoPenerimaanST
            stmt.setString(2, formattedTglLaporan);  // TglLaporan
            stmt.setString(3, formattedTglMasuk);   // TglMasuk
            stmt.setInt(4, idSupplier);         // IdSupplier
            stmt.setString(5, noTruk);          // NoTruk
            stmt.setString(6, noPlat);          // NoPlat
            stmt.setString(7, suket);           // Suket
            stmt.setString(8, tonSJ);           // TonSJ
            stmt.setString(9, note);            // Note

            stmt.executeUpdate();  // Eksekusi query untuk menyimpan data
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertDataToUpah(final String noPenerimaanST, final String tglLaporan, final int idCustomer,
                                            final String noPlat, final String noTruk, final String noSJ, final String note) {
        String query = "INSERT INTO PenerimaanSTUpah_h " +
                "(NoPenerimaanST, TglMasuk, IdCustomer, NoPlat, NoTruk, NoSJ, Note) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            // Mengonversi tanggal ke format yang sesuai
            String formattedTglLaporan = formatDate(tglLaporan);

            // Menetapkan parameter query
            stmt.setString(1, noPenerimaanST);  // NoPenerimaanST
            stmt.setString(2, formattedTglLaporan);  // TglLaporan
            stmt.setInt(3, idCustomer);         // IdSupplier
            stmt.setString(4, noPlat);          // NoTruk
            stmt.setString(5, noTruk);          // NoPlat
            stmt.setString(6, noSJ);           // Suket
            stmt.setString(7, note);            // Note

            stmt.executeUpdate();  // Eksekusi query untuk menyimpan data
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void insertDataRejectPembelian(final String noPenerimaanST,
                                                 final int noUrut,
                                                 final double tebal,
                                                 final double lebar,
                                                 final double panjang,
                                                 final int pcs,
                                                 final int idUOMTblLebar,
                                                 final int idUOMPanjang,
                                                 final double ton) {

        String query = "INSERT INTO PenerimaanSTPembelian_Reject " +
                "(NoPenerimaanST, NoUrut, Tebal, Lebar, Panjang, JmlhBatang, IdUOMTblLebar, IdUOMPanjang, Ton) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, noPenerimaanST);
            stmt.setInt(2, noUrut);
            stmt.setDouble(3, tebal);
            stmt.setDouble(4, lebar);
            stmt.setDouble(5, panjang);
            stmt.setInt(6, pcs);
            stmt.setInt(7, idUOMTblLebar);
            stmt.setInt(8, idUOMPanjang);
            stmt.setDouble(9, ton);

            int result = stmt.executeUpdate();  // Eksekusi query
            Log.d("DB_INSERT", "Insert result: " + result); // Biasanya 1 jika sukses

        } catch (SQLException e) {
            Log.e("DB_INSERT", "SQL Exception: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception ex) {
            Log.e("DB_INSERT", "General Exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }


    public static List<OutputDataST> getNoSTByDateCreate(String dateCreate) {
        List<OutputDataST> noSTList = new ArrayList<>();

        String query = "SELECT NoST, HasBeenPrinted FROM ST_h " +
                "WHERE CONVERT(date, DateCreate) = CONVERT(date, ?) " +
                "ORDER BY NoST DESC";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, dateCreate);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String noST = rs.getString("NoST");
                    boolean hasBeenPrinted = rs.getBoolean("HasBeenPrinted");

                    noSTList.add(new OutputDataST(noST, hasBeenPrinted));
                }
            }

        } catch (SQLException e) {
            Log.e("Database Fetch Error", "Error fetching NoST + status: " + e.getMessage());
        }

        return noSTList;
    }


    public static List<MstStickData> getStickByList() {
        List<MstStickData> stickByList = new ArrayList<>();

        String query = "SELECT IdStickBy, NamaStickBy " +
                "FROM dbo.MstStickBy " +
                "WHERE Enable = 1";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String idStickBy = rs.getString("IdStickBy");
                String namaStickBy = rs.getString("NamaStickBy");

                stickByList.add(new MstStickData(idStickBy, namaStickBy));
            }

            // Tambahkan dummy di awal list
            stickByList.add(0, new MstStickData("", "PILIH"));

        } catch (SQLException e) {
            System.err.println("[DB_ERROR] Gagal ambil data StickBy:");
            e.printStackTrace();
        }

        return stickByList;
    }


    public static List<MstGradeStickData> getGradeStickList() {
        List<MstGradeStickData> gradeStickList = new ArrayList<>();

        String query = "SELECT IdGradeStick, NamaGradeStick FROM dbo.MstGradeStick";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int idGradeStick = rs.getInt("IdGradeStick");
                String namaGradeStick = rs.getString("NamaGradeStick");

                gradeStickList.add(new MstGradeStickData(idGradeStick, namaGradeStick));
            }

            // tambahkan dummy default
            gradeStickList.add(0, new MstGradeStickData(0, "PILIH"));

        } catch (SQLException e) {
            System.err.println("[DB_ERROR] Gagal ambil data GradeStick:");
            e.printStackTrace();
        }

        return gradeStickList;
    }


    public static List<StData> getSawnTimberData(int page, int pageSize, String searchKeyword) {
        List<StData> sawnTimberDataList = new ArrayList<>();

        int offset = (page - 1) * pageSize;

        // Query lengkap dengan semua JOIN yang diperlukan, disesuaikan dengan model StData
        String query = "SELECT " +
                "h.NoST, " +
                "h.NoKayuBulat, " +
                "h.IdJenisKayu, " +
                "k.Jenis, " +
                "h.IdStickBy, " +
                "s.NamaStickBy, " +
                "h.NoSPK, " +
                "h.DateCreate, " +
                "h.IdOrgTelly, " +
                "t.NamaOrgTelly, " +
                "h.Remark, " +
                "p.NoPenerimaanST AS NoPenerimaanSTPembelian, " +
                "u.NoPenerimaanST AS NoPenerimaanSTUpah, " +
                "h.IsSLP, " +
                "h.VacuumDate, " +
                "b.NoBongkarSusun, " +
                "h.IdUOMTblLebar, " +
                "h.IdUOMPanjang " +
                "FROM ST_h h " +
                "LEFT JOIN MstJenisKayu k ON h.IdJenisKayu = k.IdJenisKayu " +
                "LEFT JOIN MstStickBy s ON h.IdStickBy = s.IdStickBy " +
                "LEFT JOIN MstOrgTelly t ON h.IdOrgTelly = t.IdOrgTelly " +
                "LEFT JOIN PenerimaanSTPembelian_d p ON h.NoST = p.NoST " +
                "LEFT JOIN PenerimaanSTUpah_d u ON h.NoST = u.NoST " +
                "LEFT JOIN BongkarSusunOutputST b ON h.NoST = b.NoST " +
                "WHERE " +
                "(h.NoST LIKE ? OR h.NoSPK LIKE ?) " +
                "AND h.DateUsage IS NULL " +
                "AND EXISTS (SELECT 1 FROM ST_d d WHERE d.NoST = h.NoST) " +
                "ORDER BY h.NoST DESC " +
                "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";


        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            String likeKeyword = "%" + searchKeyword + "%";
            stmt.setString(1, likeKeyword); // NoST
            stmt.setString(2, likeKeyword); // NoSPK
            stmt.setInt(3, offset);
            stmt.setInt(4, pageSize);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    sawnTimberDataList.add(new StData(
                            rs.getString("NoST"),
                            rs.getString("NoKayuBulat"),
                            rs.getInt("IdJenisKayu"),
                            rs.getString("Jenis"),
                            rs.getString("IdStickBy"),
                            rs.getString("NamaStickBy"),
                            rs.getString("NoSPK"),
                            rs.getString("DateCreate"),
                            rs.getString("IdOrgTelly"),
                            rs.getString("NamaOrgTelly"),
                            rs.getString("Remark"),
                            rs.getString("NoPenerimaanSTPembelian"),
                            rs.getString("NoPenerimaanSTUpah"),
                            rs.getInt("IsSLP"),
                            rs.getString("VacuumDate"),
                            rs.getString("NoBongkarSusun"),
                            rs.getInt("IdUOMTblLebar"),
                            rs.getInt("IdUOMPanjang")
                            ));
                }
            }

        } catch (SQLException e) {
            System.err.println("[DB_ERROR] Gagal ambil data sawn timber:");
            e.printStackTrace();
        }

        return sawnTimberDataList;
    }


    public static int getTotalLabelCount(String searchKeyword) {
        int totalLabel = 0;

        String query =
                "SELECT COUNT(*) AS TotalLabel " +
                        "FROM ST_h h " +
                        "WHERE h.NoST LIKE ? " +
                        "AND h.DateUsage IS NULL " +
                        "AND EXISTS ( " +
                        "   SELECT 1 FROM ST_d d " +
                        "   WHERE d.NoST = h.NoST " +
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



    public static StData getSawnTimberHeader(String noST) {
        String queryHeader = "SELECT " +
                "h.NoST, " +
                "h.NoKayuBulat, " +
                "h.IdJenisKayu, " +
                "k.Jenis, " +
                "h.IdStickBy, " +
                "s.NamaStickBy, " +
                "h.NoSPK, " +
                "h.DateCreate, " +
                "h.IdOrgTelly, " +
                "t.NamaOrgTelly, " +
                "h.Remark, " +
                "p.NoPenerimaanST AS NoPenerimaanSTPembelian, " +
                "u.NoPenerimaanST AS NoPenerimaanSTUpah, " +
                "h.IsSLP, " +
                "h.VacuumDate, " +
                "b.NoBongkarSusun, " +
                "h.IdUOMTblLebar, " +
                "h.IdUOMPanjang " +
                "FROM ST_h h " +
                "LEFT JOIN MstJenisKayu k ON h.IdJenisKayu = k.IdJenisKayu " +
                "LEFT JOIN MstStickBy s ON h.IdStickBy = s.IdStickBy " +
                "LEFT JOIN MstOrgTelly t ON h.IdOrgTelly = t.IdOrgTelly " +
                "LEFT JOIN PenerimaanSTPembelian_d p ON h.NoST = p.NoST " +
                "LEFT JOIN PenerimaanSTUpah_d u ON h.NoST = u.NoST " +
                "LEFT JOIN BongkarSusunOutputST b ON h.NoST = b.NoST " +
                "WHERE h.NoST = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement ps = con.prepareStatement(queryHeader)) {

            ps.setString(1, noST);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new StData(
                            rs.getString("NoST"),
                            rs.getString("NoKayuBulat"),
                            rs.getInt("IdJenisKayu"),
                            rs.getString("Jenis"),
                            rs.getString("IdStickBy"),
                            rs.getString("NamaStickBy"),
                            rs.getString("NoSPK"),
                            rs.getString("DateCreate"),
                            rs.getString("IdOrgTelly"),
                            rs.getString("NamaOrgTelly"),
                            rs.getString("Remark"),
                            rs.getString("NoPenerimaanSTPembelian"),
                            rs.getString("NoPenerimaanSTUpah"),
                            rs.getInt("IsSLP"),
                            rs.getString("VacuumDate"),
                            rs.getString("NoBongkarSusun"),
                            rs.getInt("IdUOMTblLebar"),
                            rs.getInt("IdUOMPanjang")
                            );
                }
            }

        } catch (SQLException e) {
            System.err.println("[DB_ERROR] Gagal ambil header sawn timber:");
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Get sawn timber detail data by NoST
     */
    public static List<LabelDetailData> getSawnTimberDetail(String noST) {
        List<LabelDetailData> detailList = new ArrayList<>();

        String queryDetail = "SELECT Tebal, Lebar, Panjang, JmlhBatang " +
                "FROM ST_d " +
                "WHERE NoST = ? " +
                "ORDER BY NoUrut";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement ps = con.prepareStatement(queryDetail)) {

            ps.setString(1, noST);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String tebal = rs.getString("Tebal");
                    String lebar = rs.getString("Lebar");
                    String panjang = rs.getString("Panjang");
                    String pcs = rs.getString("JmlhBatang");

                    detailList.add(new LabelDetailData(tebal, lebar, panjang, pcs));
                }
            }

        } catch (SQLException e) {
            System.err.println("[DB_ERROR] Gagal ambil detail sawn timber:");
            e.printStackTrace();
        }

        return detailList;
    }

    /**
     * Get sawn timber grade data by NoST
     */
    public static List<GradeDetailData> getSawnTimberGrade(String noST) {
        List<GradeDetailData> gradeList = new ArrayList<>();

        String queryGrade = "SELECT " +
                "s.IdGradeStick, " +
                "s.JumlahStick, " +
                "m.NamaGradeStick " +
                "FROM STStick s " +
                "INNER JOIN MstGradeStick m ON m.IdGradeStick = s.IdGradeStick " +
                "WHERE NoST = ? " +
                "ORDER BY IdGradeStick";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement ps = con.prepareStatement(queryGrade)) {

            ps.setString(1, noST);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int idGradeStick = rs.getInt("IdGradeStick");
                    String namaGradeStick = rs.getString("NamaGradeStick");
                    String jumlahGradeStick = rs.getString("JumlahStick");

                    gradeList.add(new GradeDetailData(idGradeStick, namaGradeStick, jumlahGradeStick));
                }
            }

        } catch (SQLException e) {
            System.err.println("[DB_ERROR] Gagal ambil grade sawn timber:");
            e.printStackTrace();
        }

        return gradeList;
    }



    // Di SawnTimberApi.java - Tambahkan method transaction
    public static String saveSawnTimberTransaction(String noKayuBulat, String jenisKayu,
                                                   String noSPK, String telly, String stickBy,
                                                   String dateCreate, String isVacuum, String remark,
                                                   int isSLP, int isSticked, int isKering,
                                                   int isBagusKulit, int isUpah, int idUOMTblLebar,
                                                   int idUOMPanjang, List<LabelDetailData> detailList,
                                                   List<GradeDetailData> gradeList, String noPenST,
                                                   int labelVersion, String noBongkarSusun, boolean cbBongkarSusunChecked) {

        Connection con = null;
        try {
            con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
            con.setAutoCommit(false); // Start transaction

            // 1. Generate new number
            String newNoST = generateNewNumberWithConnection(con);
            if (newNoST == null) {
                throw new SQLException("Gagal generate nomor ST baru");
            }

            // 2. Save header
            saveSawnTimberHeaderWithConnection(con, newNoST, noKayuBulat, jenisKayu, noSPK, telly, stickBy,
                    dateCreate, isVacuum, remark, isSLP, isSticked, isKering,
                    isBagusKulit, isUpah, idUOMTblLebar, idUOMPanjang);

            // 3. Save detail
            for (int i = 0; i < detailList.size(); i++) {
                LabelDetailData dataRow = detailList.get(i);
                saveSawnTimberDetailWithConnection(con, newNoST, i + 1,
                        Double.parseDouble(dataRow.getTebal()),
                        Double.parseDouble(dataRow.getLebar()),
                        Double.parseDouble(dataRow.getPanjang()),
                        Integer.parseInt(dataRow.getPcs()));
            }

            // 4. Save grade (jika bukan kayu lat)
            if (gradeList != null && !gradeList.isEmpty()) {
                for (GradeDetailData gradeDetailList : gradeList) {
                    saveSawnTimberGradeWithConnection(con, newNoST,
                            gradeDetailList.getGradeId(), gradeDetailList.getJumlah());
                }
            }

            // 5. Save penerimaan
            if (labelVersion == 1) {
                savePenerimaanSTPembelianWithConnection(con, noPenST, newNoST);
            } else if (labelVersion == 2) {
                savePenerimaanSTUpahWithConnection(con, noPenST, newNoST);
            }

            // 6. Save bongkar susun
            if (cbBongkarSusunChecked && noBongkarSusun != null) {
                saveBongkarSusunOutputSTWithConnection(con, newNoST, noBongkarSusun);
            }

            con.commit(); // Commit transaction
            return newNoST; // Return generated NoST instead of boolean

        } catch (Exception e) {
            System.err.println("[DB_ERROR] Transaction gagal: " + e.getMessage());
            e.printStackTrace();
            if (con != null) {
                try {
                    con.rollback(); // Rollback jika ada error
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            return null; // Return null jika gagal
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true); // Reset auto commit
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Helper method untuk generate number dengan connection yang sama
    private static String generateNewNumberWithConnection(Connection con) throws SQLException {
        String query = "SELECT MAX(NoST) FROM dbo.ST_h";

        try (PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String lastNoST = rs.getString(1);

                if (lastNoST != null && lastNoST.startsWith("E.")) {
                    String numericPart = lastNoST.substring(2);
                    int numericValue = Integer.parseInt(numericPart);
                    int newNumericValue = numericValue + 1;

                    return "E." + String.format("%06d", newNumericValue);
                }
            }
        }

        return "E.000001"; // Default jika belum ada data
    }

    // Helper methods untuk save dengan connection yang sama
    private static void saveSawnTimberHeaderWithConnection(Connection con, String noST, String noKayuBulat,
                                                           String jenisKayu, String noSPK, String telly,
                                                           String stickBy, String dateCreate, String isVacuum,
                                                           String remark, int isSLP, int isSticked, int isKering,
                                                           int isBagusKulit, int isUpah, int idUOMTblLebar,
                                                           int idUOMPanjang) throws SQLException {

        noKayuBulat = (noKayuBulat == null || noKayuBulat.trim().isEmpty()) ? null : noKayuBulat;

        String query = "INSERT INTO ST_h (NoST, NoKayuBulat, IdJenisKayu, NoSPK, IdOrgTelly, " +
                "IdStickBy, IsUpah, IdUOMTblLebar, IdUOMPanjang, DateCreate, VacuumDate, " +
                "Remark, IsSLP, IsSticked, StartKering, IsBagusKulit, IdLokasi) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, noST);
            ps.setString(2, noKayuBulat);
            ps.setString(3, jenisKayu);
            ps.setString(4, noSPK);
            ps.setString(5, telly);
            ps.setString(6, stickBy);
            ps.setInt(7, isUpah);
            ps.setInt(8, idUOMTblLebar);
            ps.setInt(9, idUOMPanjang);
            ps.setString(10, dateCreate);
            ps.setString(11, isVacuum);
            ps.setString(12, remark);
            ps.setInt(13, isSLP);
            ps.setInt(14, isSticked);
            ps.setInt(15, isKering);
            ps.setInt(16, isBagusKulit);

            if (isKering == 0) {
                ps.setString(17, "L01");
            } else {
                ps.setNull(17, java.sql.Types.VARCHAR);
            }

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Gagal insert header ST");
            }
        }
    }

    private static void saveSawnTimberDetailWithConnection(Connection con, String noST, int noUrut,
                                                           double tebal, double lebar, double panjang,
                                                           int pcs) throws SQLException {
        String query = "INSERT INTO dbo.ST_d (NoST, NoUrut, Tebal, Lebar, Panjang, JmlhBatang) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, noST);
            ps.setInt(2, noUrut);
            ps.setDouble(3, tebal);
            ps.setDouble(4, lebar);
            ps.setDouble(5, panjang);
            ps.setInt(6, pcs);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Gagal insert detail ST baris " + noUrut);
            }
        }
    }

    private static void saveSawnTimberGradeWithConnection(Connection con, String noST, int gradeId,
                                                          String jumlah) throws SQLException {
        String query = "INSERT INTO dbo.STStick (NoST, IdGradeStick, JumlahStick) VALUES (?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, noST);
            ps.setInt(2, gradeId);
            ps.setString(3, jumlah);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Gagal insert grade ST");
            }
        }
    }

    private static void savePenerimaanSTPembelianWithConnection(Connection con, String noPenerimaanST,
                                                                String noST) throws SQLException {
        String query = "INSERT INTO dbo.PenerimaanSTPembelian_d (NoPenerimaanST, NoST) VALUES (?, ?)";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, noPenerimaanST);
            ps.setString(2, noST);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Gagal insert penerimaan ST pembelian");
            }
        }
    }

    private static void savePenerimaanSTUpahWithConnection(Connection con, String noPenerimaanST,
                                                           String noST) throws SQLException {
        String query = "INSERT INTO dbo.PenerimaanSTUpah_d (NoPenerimaanST, NoST) VALUES (?, ?)";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, noPenerimaanST);
            ps.setString(2, noST);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Gagal insert penerimaan ST upah");
            }
        }
    }

    private static void saveBongkarSusunOutputSTWithConnection(Connection con, String noST,
                                                               String noBongkarSusun) throws SQLException {
        String query = "INSERT INTO dbo.BongkarSusunOutputST (NoST, NoBongkarSusun) VALUES (?, ?)";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, noST);
            ps.setString(2, noBongkarSusun);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Gagal insert bongkar susun output ST");
            }
        }
    }



    // Di SawnTimberApi.java - Method transaction untuk update
    public static boolean updateSawnTimberTransaction(String noST, String noKayuBulat, String jenisKayu,
                                                      String noSPK, String telly, String stickBy,
                                                      String dateCreate, String isVacuum, String remark,
                                                      int isSLP, int isSticked, int isKering,
                                                      int isBagusKulit, int isUpah, int idUOMTblLebar,
                                                      int idUOMPanjang, List<LabelDetailData> detailList,
                                                      List<GradeDetailData> gradeList, String noPenST,
                                                      int labelVersion, String noBongkarSusun, boolean cbBongkarSusunChecked) {

        Connection con = null;
        try {
            con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
            con.setAutoCommit(false); // Start transaction

            // 1. Update header
            updateSawnTimberHeaderWithConnection(con, noST, noKayuBulat, jenisKayu, noSPK, telly, stickBy,
                    dateCreate, isVacuum, remark, isSLP, isSticked, isKering,
                    isBagusKulit, isUpah, idUOMTblLebar, idUOMPanjang);

            // 2. Delete existing detail dan insert yang baru
            deleteSawnTimberDetailWithConnection(con, noST);
            for (int i = 0; i < detailList.size(); i++) {
                LabelDetailData dataRow = detailList.get(i);
                saveSawnTimberDetailWithConnection(con, noST, i + 1,
                        Double.parseDouble(dataRow.getTebal()),
                        Double.parseDouble(dataRow.getLebar()),
                        Double.parseDouble(dataRow.getPanjang()),
                        Integer.parseInt(dataRow.getPcs()));
            }

            // 3. Delete existing grade dan insert yang baru (jika bukan kayu lat)
            deleteSawnTimberGradeWithConnection(con, noST);
            if (gradeList != null && !gradeList.isEmpty()) {
                for (GradeDetailData gradeDetailList : gradeList) {
                    saveSawnTimberGradeWithConnection(con, noST,
                            gradeDetailList.getGradeId(), gradeDetailList.getJumlah());
                }
            }


            // 5. Update bongkar susun (delete dan insert baru)
            deleteBongkarSusunOutputSTWithConnection(con, noST);
            if (cbBongkarSusunChecked && noBongkarSusun != null) {
                saveBongkarSusunOutputSTWithConnection(con, noST, noBongkarSusun);
            }

            con.commit(); // Commit transaction
            return true;

        } catch (Exception e) {
            System.err.println("[DB_ERROR] Update transaction gagal: " + e.getMessage());
            e.printStackTrace();
            if (con != null) {
                try {
                    con.rollback(); // Rollback jika ada error
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            return false;
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true); // Reset auto commit
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Helper method untuk update header
    private static void updateSawnTimberHeaderWithConnection(Connection con, String noST, String noKayuBulat,
                                                             String jenisKayu, String noSPK, String telly,
                                                             String stickBy, String dateCreate, String isVacuum,
                                                             String remark, int isSLP, int isSticked, int isKering,
                                                             int isBagusKulit, int isUpah, int idUOMTblLebar,
                                                             int idUOMPanjang) throws SQLException {

        noKayuBulat = (noKayuBulat == null || noKayuBulat.trim().isEmpty()) ? null : noKayuBulat;

        String query = "UPDATE ST_h SET NoKayuBulat = ?, IdJenisKayu = ?, NoSPK = ?, IdOrgTelly = ?, " +
                "IdStickBy = ?, IsUpah = ?, IdUOMTblLebar = ?, IdUOMPanjang = ?, DateCreate = ?, " +
                "VacuumDate = ?, Remark = ?, IsSLP = ?, IsSticked = ?, StartKering = ?, " +
                "IsBagusKulit = ?, IdLokasi = ? WHERE NoST = ?";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            if (noKayuBulat == null || noKayuBulat.trim().isEmpty() || noKayuBulat.equals("-")) {
                ps.setNull(1, java.sql.Types.VARCHAR);
            } else {
                ps.setString(1, noKayuBulat);
            }

            ps.setString(2, jenisKayu);
            ps.setString(3, noSPK);
            ps.setString(4, telly);
            ps.setString(5, stickBy);
            ps.setInt(6, isUpah);
            ps.setInt(7, idUOMTblLebar);
            ps.setInt(8, idUOMPanjang);
            ps.setString(9, dateCreate);
            ps.setString(10, isVacuum);
            ps.setString(11, remark);
            ps.setInt(12, isSLP);
            ps.setInt(13, isSticked);
            ps.setInt(14, isKering);
            ps.setInt(15, isBagusKulit);

            if (isKering == 0) {
                ps.setString(16, "L01");
            } else {
                ps.setNull(16, java.sql.Types.VARCHAR);
            }

            ps.setString(17, noST); // WHERE clause


            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Gagal update header ST - No data found with NoST: " + noST);
            }
        }
    }

    // Helper methods untuk delete existing data
    private static void deleteSawnTimberDetailWithConnection(Connection con, String noST) throws SQLException {
        String query = "DELETE FROM dbo.ST_d WHERE NoST = ?";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, noST);
            ps.executeUpdate(); // Tidak perlu check rows affected karena bisa saja kosong
        }
    }

    private static void deleteSawnTimberGradeWithConnection(Connection con, String noST) throws SQLException {
        String query = "DELETE FROM dbo.STStick WHERE NoST = ?";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, noST);
            ps.executeUpdate();
        }
    }

    private static void deleteBongkarSusunOutputSTWithConnection(Connection con, String noST) throws SQLException {
        String query = "DELETE FROM dbo.BongkarSusunOutputST WHERE NoST = ?";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, noST);
            ps.executeUpdate();
        }
    }

    // 1) Hapus relasi penerimaan pembelian
    private static void deletePenerimaanSTPembelianWithConnection(Connection con, String noST) throws SQLException {
        String query = "DELETE FROM dbo.PenerimaanSTPembelian_d WHERE NoST = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, noST);
            ps.executeUpdate(); // boleh 0 row kalau memang tidak ada
        }
    }

    // 2) Hapus relasi penerimaan upah
    private static void deletePenerimaanSTUpahWithConnection(Connection con, String noST) throws SQLException {
        String query = "DELETE FROM dbo.PenerimaanSTUpah_d WHERE NoST = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, noST);
            ps.executeUpdate(); // boleh 0 row
        }
    }

    // 3) Hapus header ST (paling terakhir)
    private static int deleteSawnTimberHeaderWithConnection(Connection con, String noST) throws SQLException {
        String query = "DELETE FROM dbo.ST_h WHERE NoST = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, noST);
            return ps.executeUpdate(); // harusnya 1 kalau ada
        }
    }


    public static boolean deleteSawnTimberTransaction(String noST) {
        Connection con = null;
        try {
            con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
            con.setAutoCommit(false);

            // Urutan: child → parent
            deleteBongkarSusunOutputSTWithConnection(con, noST);
            deleteSawnTimberGradeWithConnection(con, noST);
            deleteSawnTimberDetailWithConnection(con, noST);
            deletePenerimaanSTPembelianWithConnection(con, noST);
            deletePenerimaanSTUpahWithConnection(con, noST);

            int headerDeleted = deleteSawnTimberHeaderWithConnection(con, noST);
            if (headerDeleted != 1) {
                throw new SQLException("Header ST tidak ditemukan / gagal dihapus: " + noST);
            }

            con.commit();
            return true;
        } catch (Exception e) {
            System.err.println("[DB_ERROR] Delete transaction gagal: " + e.getMessage());
            e.printStackTrace();
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            return false;
        } finally {
            if (con != null) {
                try { con.setAutoCommit(true); con.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }



}
