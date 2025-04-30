package com.example.myapplication.api;

import android.util.Log;

import com.example.myapplication.DatabaseConfig;
import com.example.myapplication.model.CustomerData;
import com.example.myapplication.model.STPembelianData;
import com.example.myapplication.model.STPembelianDataReject;
import com.example.myapplication.model.STUpahData;
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

public class LabelApi {

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


//                // Debugging untuk memverifikasi data
//                Log.d("Database Data", "NoProduksi: " + noProduksi +
//                        ", Shift: " + shift +
//                        ", Tanggal: " + tanggal +
//                        ", Mesin: " + mesin +
//                        ", Operator: " + operator);

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

    public static void insertDataToDatabase(final String noPenerimaanST, final String tglLaporan, final String tglMasuk, final int idSupplier,
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





}
