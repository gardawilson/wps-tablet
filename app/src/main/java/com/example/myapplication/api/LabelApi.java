package com.example.myapplication.api;

import android.util.Log;

import com.example.myapplication.DatabaseConfig;
import com.example.myapplication.model.STPembelianData;
import com.example.myapplication.model.STPembelianDataReject;
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

    // Metode baru untuk mengambil NoS4S berdasarkan NoProduksi
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

        if (lastNoPenerimaanST != null && lastNoPenerimaanST.startsWith("B.")) {
            String numericPart = lastNoPenerimaanST.substring(2);  // Mengambil bagian angka setelah "B."
            int nextNumber = Integer.parseInt(numericPart) + 1;  // Menambah 1 pada angka terakhir
            return "B." + String.format("%06d", nextNumber);  // Format dengan 6 digit angka
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



}
