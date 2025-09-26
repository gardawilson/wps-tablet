package com.example.myapplication.api;

import android.util.Log;

import com.example.myapplication.config.DatabaseConfig;
import com.example.myapplication.model.BongkarSusunData;
import com.example.myapplication.model.NyangkutData;
import com.example.myapplication.utils.DateTimeUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NyangkutApi {

    public static List<NyangkutData> getNyangkutData() {
        List<NyangkutData> nyangkutList = new ArrayList<>();

        String query = "SELECT TOP 50 " +
                "NoNyangkut, " +
                "Tgl " +
                "FROM Nyangkut_h " +
                "ORDER BY NoNyangkut DESC";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                NyangkutData data = new NyangkutData(
                        rs.getString("NoNyangkut"),
                        rs.getString("Tgl")   // bisa juga pakai rs.getDate("Tgl") kalau mau Date
                );
                nyangkutList.add(data);
            }
        } catch (SQLException e) {
            Log.e("DB_FETCH", "Error fetching Nyangkut data: " + e.getMessage(), e);
        }

        return nyangkutList;
    }


    public static List<String> getNoSTByNoNyangkut(String noNyangkut) {
        List<String> noSTList = new ArrayList<>();

        String query = "SELECT NoST FROM Nyangkut_ST WHERE NoNyangkut = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, noNyangkut);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String noST = rs.getString("NoST");
                    noSTList.add(noST);
                    Log.d("DB_FETCH", "NoST: " + noST);
                }
            }

        } catch (SQLException e) {
            Log.e("DB_FETCH_ERROR", "Error fetching NoST for Nyangkut: " + e.getMessage(), e);
        }

        return noSTList;
    }


    public static List<String> getNoS4SByNoNyangkut(String noNyangkut) {
        List<String> noS4SList = new ArrayList<>();

        String query = "SELECT NoS4S FROM Nyangkut_S4S WHERE NoNyangkut = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, noNyangkut);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String noS4S = rs.getString("NoS4S");
                    noS4SList.add(noS4S);
                    Log.d("DB_FETCH", "NoS4S: " + noS4S);
                }
            }

        } catch (SQLException e) {
            Log.e("DB_FETCH_ERROR", "Error fetching NoS4S for Nyangkut: " + e.getMessage(), e);
        }

        return noS4SList;
    }


    public static List<String> getNoFJByNoNyangkut(String noNyangkut) {
        List<String> noFJList = new ArrayList<>();

        String query = "SELECT NoFJ FROM Nyangkut_FJ WHERE NoNyangkut = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, noNyangkut);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String noFJ = rs.getString("NoFJ");
                    noFJList.add(noFJ);
                    Log.d("DB_FETCH", "NoFJ: " + noFJ);
                }
            }

        } catch (SQLException e) {
            Log.e("DB_FETCH_ERROR", "Error fetching NoFJ for Nyangkut: " + e.getMessage(), e);
        }

        return noFJList;
    }


    public static List<String> getNoMouldingByNoNyangkut(String noNyangkut) {
        List<String> noMouldingList = new ArrayList<>();

        String query = "SELECT NoMoulding FROM Nyangkut_Moulding WHERE NoNyangkut = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, noNyangkut);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String noMoulding = rs.getString("NoMoulding");
                    noMouldingList.add(noMoulding);
                    Log.d("DB_FETCH", "NoMoulding: " + noMoulding);
                }
            }

        } catch (SQLException e) {
            Log.e("DB_FETCH_ERROR", "Error fetching NoMoulding for Nyangkut: " + e.getMessage(), e);
        }

        return noMouldingList;
    }


    public static List<String> getNoLaminatingByNoNyangkut(String noNyangkut) {
        List<String> noLaminatingList = new ArrayList<>();

        String query = "SELECT NoLaminating FROM Nyangkut_Laminating WHERE NoNyangkut = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, noNyangkut);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String noLaminating = rs.getString("NoLaminating");
                    noLaminatingList.add(noLaminating);
                    Log.d("DB_FETCH", "NoLaminating: " + noLaminating);
                }
            }

        } catch (SQLException e) {
            Log.e("DB_FETCH_ERROR", "Error fetching NoLaminating for Nyangkut: " + e.getMessage(), e);
        }

        return noLaminatingList;
    }


    public static List<String> getNoCCAByNoNyangkut(String noNyangkut) {
        List<String> noCCAList = new ArrayList<>();

        String query = "SELECT NoCCAkhir FROM Nyangkut_CCA WHERE NoNyangkut = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, noNyangkut);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String noCCA = rs.getString("NoCCAkhir");
                    noCCAList.add(noCCA);
                    Log.d("DB_FETCH", "NoCCAkhir: " + noCCA);
                }
            }

        } catch (SQLException e) {
            Log.e("DB_FETCH_ERROR", "Error fetching NoCCAkhir for Nyangkut: " + e.getMessage(), e);
        }

        return noCCAList;
    }


    public static List<String> getNoSandingByNoNyangkut(String noNyangkut) {
        List<String> noSandingList = new ArrayList<>();

        String query = "SELECT NoSanding FROM Nyangkut_Sanding WHERE NoNyangkut = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, noNyangkut);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String noSanding = rs.getString("NoSanding");
                    noSandingList.add(noSanding);
                    Log.d("DB_FETCH", "NoSanding: " + noSanding);
                }
            }

        } catch (SQLException e) {
            Log.e("DB_FETCH_ERROR", "Error fetching NoSanding for Nyangkut: " + e.getMessage(), e);
        }

        return noSandingList;
    }


    public static List<String> getNoBJByNoNyangkut(String noNyangkut) {
        List<String> noBJList = new ArrayList<>();

        String query = "SELECT NoBJ FROM Nyangkut_BarangJadi WHERE NoNyangkut = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, noNyangkut);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String noBJ = rs.getString("NoBJ");
                    noBJList.add(noBJ);
                    Log.d("DB_FETCH", "NoBJ: " + noBJ);
                }
            }

        } catch (SQLException e) {
            Log.e("DB_FETCH_ERROR", "Error fetching NoBJ for Nyangkut: " + e.getMessage(), e);
        }

        return noBJList;
    }


    public static boolean createNewNyangkut(String tanggal) {
        final String TAG = "NyangkutDB";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl())) {
            con.setAutoCommit(false); // Mulai transaction

            // 1. Generate nomor baru (misal format Z.XXXXXX)
            String newNo = generateNewNoNyangkut(con);
            Log.d(TAG, "Generated new NoNyangkut: " + newNo);

            // 2. Simpan data ke tabel Nyangkut_h
            String query = "INSERT INTO Nyangkut_h (NoNyangkut, Tgl) VALUES (?, ?)";

            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setString(1, newNo);
                stmt.setString(2, DateTimeUtils.formatToDatabaseDate(tanggal));

                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    con.commit();
                    Log.i(TAG, "Successfully created record: " + newNo);
                    return true;
                } else {
                    con.rollback();
                    return false;
                }
            }
        } catch (SQLException e) {
            Log.e(TAG, "Error creating Nyangkut: " + e.getMessage(), e);
            return false;
        }
    }

    private static String generateNewNoNyangkut(Connection con) throws SQLException {
        String prefix = "GC"; // Bisa kamu ganti sesuai aturan
        String query = "SELECT TOP 1 NoNyangkut FROM Nyangkut_h ORDER BY NoNyangkut DESC";

        try (PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                String lastNo = rs.getString("NoNyangkut");
                // Ambil angka dari belakang, increment +1
                int lastNumber = Integer.parseInt(lastNo.replace(prefix + ".", ""));
                int newNumber = lastNumber + 1;
                return String.format("%s.%06d", prefix, newNumber);
            } else {
                // Kalau belum ada data
                return prefix + ".000001";
            }
        }
    }


    public static boolean updateNyangkut(String noNyangkut, String tanggalBaru) {
        final String TAG = "NyangkutDB-UPDATE";

        String query = "UPDATE Nyangkut_h SET Tgl = ? WHERE NoNyangkut = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, DateTimeUtils.formatToDatabaseDate(tanggalBaru));
            stmt.setString(2, noNyangkut);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                Log.i(TAG, "Successfully updated NoNyangkut: " + noNyangkut);
                return true;
            } else {
                Log.w(TAG, "No record updated for NoNyangkut: " + noNyangkut);
                return false;
            }

        } catch (SQLException e) {
            Log.e(TAG, "Error updating Nyangkut: " + e.getMessage(), e);
            return false;
        }
    }



    public static boolean deleteNyangkutHeader(String noNyangkut) {
        final String TAG = "NyangkutDB-DELETE-CASCADE";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl())) {
            con.setAutoCommit(false); // Mulai transaction

            // 1. Hapus semua detail di tabel terkait
            String[] detailQueries = new String[]{
                    "DELETE FROM Nyangkut_ST WHERE NoNyangkut = ?",
                    "DELETE FROM Nyangkut_S4S WHERE NoNyangkut = ?",
                    "DELETE FROM Nyangkut_FJ WHERE NoNyangkut = ?",
                    "DELETE FROM Nyangkut_Moulding WHERE NoNyangkut = ?",
                    "DELETE FROM Nyangkut_Laminating WHERE NoNyangkut = ?",
                    "DELETE FROM Nyangkut_CCA WHERE NoNyangkut = ?",
                    "DELETE FROM Nyangkut_Sanding WHERE NoNyangkut = ?",
                    "DELETE FROM Nyangkut_BarangJadi WHERE NoNyangkut = ?"
            };

            for (String query : detailQueries) {
                try (PreparedStatement stmt = con.prepareStatement(query)) {
                    stmt.setString(1, noNyangkut);
                    stmt.executeUpdate(); // tidak masalah kalau 0 rows
                }
            }

            // 2. Hapus header
            String deleteHeader = "DELETE FROM Nyangkut_h WHERE NoNyangkut = ?";
            try (PreparedStatement stmt = con.prepareStatement(deleteHeader)) {
                stmt.setString(1, noNyangkut);
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    con.commit();
                    Log.i(TAG, "Cascade delete success for NoNyangkut: " + noNyangkut);
                    return true;
                } else {
                    con.rollback();
                    Log.w(TAG, "No header deleted for: " + noNyangkut);
                    return false;
                }
            }

        } catch (SQLException e) {
            Log.e(TAG, "Error cascade deleting Nyangkut: " + e.getMessage(), e);
            return false;
        }
    }




    public static boolean deleteNyangkutST(String noNyangkut, String noST) {
        final String TAG = "NyangkutDB-DELETE";

        String query = "DELETE FROM Nyangkut_ST WHERE NoNyangkut = ? AND NoST = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, noNyangkut);
            stmt.setString(2, noST);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                Log.i(TAG, "Deleted Nyangkut_ST: " + noNyangkut + " - " + noST);
                return true;
            } else {
                Log.w(TAG, "No record deleted for: " + noNyangkut + " - " + noST);
                return false;
            }
        } catch (SQLException e) {
            Log.e(TAG, "Error deleting Nyangkut_ST: " + e.getMessage(), e);
            return false;
        }
    }


    public static boolean deleteNyangkutS4S(String noNyangkut, String noS4S) {
        final String TAG = "NyangkutDB-DELETE-S4S";

        String query = "DELETE FROM Nyangkut_S4S WHERE NoNyangkut = ? AND NoS4S = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, noNyangkut);
            stmt.setString(2, noS4S);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                Log.i(TAG, "Deleted Nyangkut_S4S: " + noNyangkut + " - " + noS4S);
                return true;
            } else {
                Log.w(TAG, "No record deleted for: " + noNyangkut + " - " + noS4S);
                return false;
            }
        } catch (SQLException e) {
            Log.e(TAG, "Error deleting Nyangkut_S4S: " + e.getMessage(), e);
            return false;
        }
    }


    public static boolean deleteNyangkutFJ(String noNyangkut, String noFJ) {
        final String TAG = "NyangkutDB-DELETE-FJ";

        String query = "DELETE FROM Nyangkut_FJ WHERE NoNyangkut = ? AND NoFJ = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, noNyangkut);
            stmt.setString(2, noFJ);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                Log.i(TAG, "Deleted Nyangkut_FJ: " + noNyangkut + " - " + noFJ);
                return true;
            } else {
                Log.w(TAG, "No record deleted for: " + noNyangkut + " - " + noFJ);
                return false;
            }
        } catch (SQLException e) {
            Log.e(TAG, "Error deleting Nyangkut_FJ: " + e.getMessage(), e);
            return false;
        }
    }


    public static boolean deleteNyangkutMoulding(String noNyangkut, String noMoulding) {
        final String TAG = "NyangkutDB-DELETE-Moulding";

        String query = "DELETE FROM Nyangkut_Moulding WHERE NoNyangkut = ? AND NoMoulding = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, noNyangkut);
            stmt.setString(2, noMoulding);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                Log.i(TAG, "Deleted Nyangkut_Moulding: " + noNyangkut + " - " + noMoulding);
                return true;
            } else {
                Log.w(TAG, "No record deleted for: " + noNyangkut + " - " + noMoulding);
                return false;
            }
        } catch (SQLException e) {
            Log.e(TAG, "Error deleting Nyangkut_Moulding: " + e.getMessage(), e);
            return false;
        }
    }

    public static boolean deleteNyangkutLaminating(String noNyangkut, String noLaminating) {
        final String TAG = "NyangkutDB-DELETE-Laminating";

        String query = "DELETE FROM Nyangkut_Laminating WHERE NoNyangkut = ? AND NoLaminating = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, noNyangkut);
            stmt.setString(2, noLaminating);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                Log.i(TAG, "Deleted Nyangkut_Laminating: " + noNyangkut + " - " + noLaminating);
                return true;
            } else {
                Log.w(TAG, "No record deleted for: " + noNyangkut + " - " + noLaminating);
                return false;
            }
        } catch (SQLException e) {
            Log.e(TAG, "Error deleting Nyangkut_Laminating: " + e.getMessage(), e);
            return false;
        }
    }

    public static boolean deleteNyangkutCCA(String noNyangkut, String noCCAkhir) {
        final String TAG = "NyangkutDB-DELETE-CCA";

        String query = "DELETE FROM Nyangkut_CCA WHERE NoNyangkut = ? AND NoCCAkhir = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, noNyangkut);
            stmt.setString(2, noCCAkhir);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                Log.i(TAG, "Deleted Nyangkut_CCA: " + noNyangkut + " - " + noCCAkhir);
                return true;
            } else {
                Log.w(TAG, "No record deleted for: " + noNyangkut + " - " + noCCAkhir);
                return false;
            }
        } catch (SQLException e) {
            Log.e(TAG, "Error deleting Nyangkut_CCA: " + e.getMessage(), e);
            return false;
        }
    }

    public static boolean deleteNyangkutSanding(String noNyangkut, String noSanding) {
        final String TAG = "NyangkutDB-DELETE-Sanding";

        String query = "DELETE FROM Nyangkut_Sanding WHERE NoNyangkut = ? AND NoSanding = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, noNyangkut);
            stmt.setString(2, noSanding);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                Log.i(TAG, "Deleted Nyangkut_Sanding: " + noNyangkut + " - " + noSanding);
                return true;
            } else {
                Log.w(TAG, "No record deleted for: " + noNyangkut + " - " + noSanding);
                return false;
            }
        } catch (SQLException e) {
            Log.e(TAG, "Error deleting Nyangkut_Sanding: " + e.getMessage(), e);
            return false;
        }
    }

    public static boolean deleteNyangkutBJ(String noNyangkut, String noBJ) {
        final String TAG = "NyangkutDB-DELETE-BJ";

        String query = "DELETE FROM Nyangkut_BarangJadi WHERE NoNyangkut = ? AND NoBJ = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, noNyangkut);
            stmt.setString(2, noBJ);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                Log.i(TAG, "Deleted Nyangkut_BarangJadi: " + noNyangkut + " - " + noBJ);
                return true;
            } else {
                Log.w(TAG, "No record deleted for: " + noNyangkut + " - " + noBJ);
                return false;
            }
        } catch (SQLException e) {
            Log.e(TAG, "Error deleting Nyangkut_BarangJadi: " + e.getMessage(), e);
            return false;
        }
    }




}
