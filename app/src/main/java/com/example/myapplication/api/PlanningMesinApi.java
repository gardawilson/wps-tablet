package com.example.myapplication.api;

import android.util.Log;

import com.example.myapplication.config.DatabaseConfig;
import com.example.myapplication.model.PlanningMesinData;
import com.example.myapplication.utils.DateTimeUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PlanningMesinApi {

    public static List<PlanningMesinData> getPlanningMesinS4SData() {
        List<PlanningMesinData> dataList = new ArrayList<>();

        String query = "SELECT TOP 50 p.IdMesin, m.NamaMesin, p.Tanggal, p.PlanningJamKerja " +
                "FROM dbo.MstPlanningMesinS4S p " +
                "LEFT JOIN dbo.MstMesin m ON p.IdMesin = m.IdMesin " +
                "ORDER BY p.Tanggal DESC";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int idMesin = rs.getInt("IdMesin");
                String namaMesin = rs.getString("NamaMesin"); // 🔹 ambil nama mesin
                String tanggal = rs.getString("Tanggal");
                int planningJamKerja = rs.getInt("PlanningJamKerja");

                dataList.add(new PlanningMesinData(idMesin, namaMesin, tanggal, planningJamKerja));
            }

        } catch (SQLException e) {
            Log.e("Database Error", "Error fetching PlanningMesinS4S data: " + e.getMessage());
        }

        return dataList;
    }


    public static List<PlanningMesinData> getPlanningMesinFJData() {
        List<PlanningMesinData> dataList = new ArrayList<>();

        String query = "SELECT TOP 50 p.IdMesin, m.NamaMesin, p.Tanggal, p.PlanningJamKerja " +
                "FROM dbo.MstPlanningMesinFJ p " +
                "LEFT JOIN dbo.MstMesin m ON p.IdMesin = m.IdMesin " +
                "ORDER BY p.Tanggal DESC";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int idMesin = rs.getInt("IdMesin");
                String namaMesin = rs.getString("NamaMesin");
                String tanggal = rs.getString("Tanggal");
                int planningJamKerja = rs.getInt("PlanningJamKerja");

                dataList.add(new PlanningMesinData(idMesin, namaMesin, tanggal, planningJamKerja));
            }

        } catch (SQLException e) {
            Log.e("Database Error", "Error fetching PlanningMesinFJ data: " + e.getMessage());
        }

        return dataList;
    }

    public static List<PlanningMesinData> getPlanningMesinMLDData() {
        List<PlanningMesinData> dataList = new ArrayList<>();

        String query = "SELECT TOP 50 p.IdMesin, m.NamaMesin, p.Tanggal, p.PlanningJamKerja " +
                "FROM dbo.MstPlanningMesinMoulding p " +
                "LEFT JOIN dbo.MstMesin m ON p.IdMesin = m.IdMesin " +
                "ORDER BY p.Tanggal DESC";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int idMesin = rs.getInt("IdMesin");
                String namaMesin = rs.getString("NamaMesin");
                String tanggal = rs.getString("Tanggal");
                int planningJamKerja = rs.getInt("PlanningJamKerja");

                dataList.add(new PlanningMesinData(idMesin, namaMesin, tanggal, planningJamKerja));
            }

        } catch (SQLException e) {
            Log.e("Database Error", "Error fetching PlanningMesinMoulding data: " + e.getMessage());
        }

        return dataList;
    }

    public static List<PlanningMesinData> getPlanningMesinLMTData() {
        List<PlanningMesinData> dataList = new ArrayList<>();

        String query = "SELECT TOP 50 p.IdMesin, m.NamaMesin, p.Tanggal, p.PlanningJamKerja " +
                "FROM dbo.MstPlanningMesinLaminating p " +
                "LEFT JOIN dbo.MstMesin m ON p.IdMesin = m.IdMesin " +
                "ORDER BY p.Tanggal DESC";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int idMesin = rs.getInt("IdMesin");
                String namaMesin = rs.getString("NamaMesin");
                String tanggal = rs.getString("Tanggal");
                int planningJamKerja = rs.getInt("PlanningJamKerja");

                dataList.add(new PlanningMesinData(idMesin, namaMesin, tanggal, planningJamKerja));
            }

        } catch (SQLException e) {
            Log.e("Database Error", "Error fetching PlanningMesinLaminating data: " + e.getMessage());
        }

        return dataList;
    }


    public static List<PlanningMesinData> getPlanningMesinCCAData() {
        List<PlanningMesinData> dataList = new ArrayList<>();

        String query = "SELECT TOP 50 p.IdMesin, m.NamaMesin, p.Tanggal, p.PlanningJamKerja " +
                "FROM dbo.MstPlanningMesinCCA p " +
                "LEFT JOIN dbo.MstMesin m ON p.IdMesin = m.IdMesin " +
                "ORDER BY p.Tanggal DESC";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int idMesin = rs.getInt("IdMesin");
                String namaMesin = rs.getString("NamaMesin");
                String tanggal = rs.getString("Tanggal");
                int planningJamKerja = rs.getInt("PlanningJamKerja");

                dataList.add(new PlanningMesinData(idMesin, namaMesin, tanggal, planningJamKerja));
            }

        } catch (SQLException e) {
            Log.e("Database Error", "Error fetching PlanningMesinCCA data: " + e.getMessage());
        }

        return dataList;
    }

    public static List<PlanningMesinData> getPlanningMesinSNDData() {
        List<PlanningMesinData> dataList = new ArrayList<>();

        String query = "SELECT TOP 50 p.IdMesin, m.NamaMesin, p.Tanggal, p.PlanningJamKerja " +
                "FROM dbo.MstPlanningMesinSanding p " +
                "LEFT JOIN dbo.MstMesin m ON p.IdMesin = m.IdMesin " +
                "ORDER BY p.Tanggal DESC";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int idMesin = rs.getInt("IdMesin");
                String namaMesin = rs.getString("NamaMesin");
                String tanggal = rs.getString("Tanggal");
                int planningJamKerja = rs.getInt("PlanningJamKerja");

                dataList.add(new PlanningMesinData(idMesin, namaMesin, tanggal, planningJamKerja));
            }

        } catch (SQLException e) {
            Log.e("Database Error", "Error fetching PlanningMesinSanding data: " + e.getMessage());
        }

        return dataList;
    }


    public static List<PlanningMesinData> getPlanningMesinBJData() {
        List<PlanningMesinData> dataList = new ArrayList<>();

        String query = "SELECT TOP 50 p.IdMesin, m.NamaMesin, p.Tanggal, p.PlanningJamKerja " +
                "FROM dbo.MstPlanningMesinPacking p " +
                "LEFT JOIN dbo.MstMesin m ON p.IdMesin = m.IdMesin " +
                "ORDER BY p.Tanggal DESC";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int idMesin = rs.getInt("IdMesin");
                String namaMesin = rs.getString("NamaMesin");
                String tanggal = rs.getString("Tanggal");
                int planningJamKerja = rs.getInt("PlanningJamKerja");

                dataList.add(new PlanningMesinData(idMesin, namaMesin, tanggal, planningJamKerja));
            }

        } catch (SQLException e) {
            Log.e("Database Error", "Error fetching PlanningMesinPacking data: " + e.getMessage());
        }

        return dataList;
    }

    public static List<PlanningMesinData> getPlanningMesinSLPData() {
        List<PlanningMesinData> dataList = new ArrayList<>();

        String query = "SELECT TOP 50 p.IdMesin, s.NamaMeja AS NamaMesin, p.Tanggal, p.PlanningJamKerja " +
                "FROM dbo.MstPlanningMesinSLP p " +
                "LEFT JOIN dbo.MstMesinSawmill s ON p.IdMesin = s.NoMeja " +
                "ORDER BY p.Tanggal DESC";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int idMesin = rs.getInt("IdMesin");
                String namaMesin = rs.getString("NamaMesin"); // diambil dari NamaMeja
                String tanggal = rs.getString("Tanggal");
                int planningJamKerja = rs.getInt("PlanningJamKerja");

                dataList.add(new PlanningMesinData(idMesin, namaMesin, tanggal, planningJamKerja));
            }

        } catch (SQLException e) {
            Log.e("Database Error", "Error fetching PlanningMesinSLP data: " + e.getMessage());
        }

        return dataList;
    }


    public static boolean insertPlanningMesinS4S(String idMesin, String tanggal, int planningJamKerja) {
        String query = "INSERT INTO dbo.MstPlanningMesinS4S (IdMesin, Tanggal, PlanningJamKerja) VALUES (?, ?, ?)";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, idMesin);
            stmt.setString(2, DateTimeUtils.formatToDatabaseDate(tanggal));
            stmt.setInt(3, planningJamKerja);

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            Log.e("Database Error", "Error inserting PlanningMesinS4S: " + e.getMessage());
            return false;
        }
    }

    public static boolean insertPlanningMesinFJ(String idMesin, String tanggal, int planningJamKerja) {
        String query = "INSERT INTO dbo.MstPlanningMesinFJ (IdMesin, Tanggal, PlanningJamKerja) VALUES (?, ?, ?)";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, idMesin);
            stmt.setString(2, DateTimeUtils.formatToDatabaseDate(tanggal));
            stmt.setInt(3, planningJamKerja);

            int rows = stmt.executeUpdate();
            return rows > 0; // true kalau berhasil insert

        } catch (SQLException e) {
            Log.e("Database Error", "Error inserting PlanningMesinFJ: " + e.getMessage());
            return false;
        }
    }

    public static boolean insertPlanningMesinMoulding(String idMesin, String tanggal, int planningJamKerja) {
        String query = "INSERT INTO dbo.MstPlanningMesinMoulding (IdMesin, Tanggal, PlanningJamKerja) VALUES (?, ?, ?)";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, idMesin);
            stmt.setString(2, DateTimeUtils.formatToDatabaseDate(tanggal));
            stmt.setInt(3, planningJamKerja);

            int rows = stmt.executeUpdate();
            return rows > 0; // true kalau ada row yang berhasil masuk

        } catch (SQLException e) {
            Log.e("Database Error", "Error inserting PlanningMesinMoulding: " + e.getMessage());
            return false;
        }
    }


    public static boolean insertPlanningMesinLaminating(String idMesin, String tanggal, int planningJamKerja) {
        String query = "INSERT INTO dbo.MstPlanningMesinLaminating (IdMesin, Tanggal, PlanningJamKerja) VALUES (?, ?, ?)";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, idMesin);
            stmt.setString(2, DateTimeUtils.formatToDatabaseDate(tanggal));
            stmt.setInt(3, planningJamKerja);

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            Log.e("Database Error", "Error inserting PlanningMesinLaminating: " + e.getMessage());
            return false;
        }
    }


    public static boolean insertPlanningMesinCCA(String idMesin, String tanggal, int planningJamKerja) {
        String query = "INSERT INTO dbo.MstPlanningMesinCCA (IdMesin, Tanggal, PlanningJamKerja) VALUES (?, ?, ?)";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, idMesin);
            stmt.setString(2, DateTimeUtils.formatToDatabaseDate(tanggal));
            stmt.setInt(3, planningJamKerja);

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            Log.e("Database Error", "Error inserting PlanningMesinCCA: " + e.getMessage());
            return false;
        }
    }

    public static boolean insertPlanningMesinSanding(String idMesin, String tanggal, int planningJamKerja) {
        String sql = "INSERT INTO dbo.MstPlanningMesinSanding (IdMesin, Tanggal, PlanningJamKerja) VALUES (?, ?, ?)";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, idMesin);
            ps.setString(2, DateTimeUtils.formatToDatabaseDate(tanggal));          // format yyyy-MM-dd atau sesuai kolom (DATE/DATETIME)
            ps.setInt(3, planningJamKerja);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Log.e("DB", "insertPlanningMesinSanding error: " + e.getMessage());
            return false;
        }
    }

    public static boolean insertPlanningMesinPacking(String idMesin, String tanggal, int planningJamKerja) {
        String sql = "INSERT INTO dbo.MstPlanningMesinPacking (IdMesin, Tanggal, PlanningJamKerja) VALUES (?, ?, ?)";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, idMesin);
            ps.setString(2, DateTimeUtils.formatToDatabaseDate(tanggal));
            ps.setInt(3, planningJamKerja);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Log.e("DB", "insertPlanningMesinPacking error: " + e.getMessage());
            return false;
        }
    }

    public static boolean insertPlanningMesinSLP(String idMesinNoMeja, String tanggal, int planningJamKerja) {
        // idMesinNoMeja = NoMeja dari MstMesinSawmill (String)
        String sql = "INSERT INTO dbo.MstPlanningMesinSLP (IdMesin, Tanggal, PlanningJamKerja) VALUES (?, ?, ?)";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, idMesinNoMeja);
            ps.setString(2, DateTimeUtils.formatToDatabaseDate(tanggal));
            ps.setInt(3, planningJamKerja);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Log.e("DB", "insertPlanningMesinSLP error: " + e.getMessage());
            return false;
        }
    }



    public static boolean updatePlanningMesinS4S(
            String oldIdMesin, String oldTanggal,   // kunci lama
            String newIdMesin, String newTanggal, int newPlanningJamKerja) {

        String query = "UPDATE dbo.MstPlanningMesinS4S " +
                "SET IdMesin = ?, Tanggal = ?, PlanningJamKerja = ? " +
                "WHERE IdMesin = ? AND Tanggal = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            // nilai baru
            stmt.setString(1, newIdMesin);
            stmt.setString(2, DateTimeUtils.formatToDatabaseDate(newTanggal));
            stmt.setInt(3, newPlanningJamKerja);

            // nilai lama (untuk WHERE)
            stmt.setString(4, oldIdMesin);
            stmt.setString(5, DateTimeUtils.formatToDatabaseDate(oldTanggal));

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Log.e("DB", "updatePlanningMesinS4S error: " + e.getMessage());
            return false;
        }
    }


    public static boolean updatePlanningMesinFJ(
            String oldIdMesin, String oldTanggal,
            String newIdMesin, String newTanggal, int newPlanningJamKerja) {

        String query = "UPDATE dbo.MstPlanningMesinFJ " +
                "SET IdMesin = ?, Tanggal = ?, PlanningJamKerja = ? " +
                "WHERE IdMesin = ? AND Tanggal = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, newIdMesin);
            stmt.setString(2, DateTimeUtils.formatToDatabaseDate(newTanggal));
            stmt.setInt(3, newPlanningJamKerja);

            stmt.setString(4, oldIdMesin);
            stmt.setString(5, DateTimeUtils.formatToDatabaseDate(oldTanggal));

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Log.e("DB", "updatePlanningMesinFJ error: " + e.getMessage());
            return false;
        }
    }


    public static boolean updatePlanningMesinMoulding(
            String oldIdMesin, String oldTanggal,
            String newIdMesin, String newTanggal, int newPlanningJamKerja) {

        String query = "UPDATE dbo.MstPlanningMesinMoulding " +
                "SET IdMesin = ?, Tanggal = ?, PlanningJamKerja = ? " +
                "WHERE IdMesin = ? AND Tanggal = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, newIdMesin);
            stmt.setString(2, DateTimeUtils.formatToDatabaseDate(newTanggal));
            stmt.setInt(3, newPlanningJamKerja);

            stmt.setString(4, oldIdMesin);
            stmt.setString(5, DateTimeUtils.formatToDatabaseDate(oldTanggal));

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Log.e("DB", "updatePlanningMesinMoulding error: " + e.getMessage());
            return false;
        }
    }


    public static boolean updatePlanningMesinLaminating(
            String oldIdMesin, String oldTanggal,
            String newIdMesin, String newTanggal, int newPlanningJamKerja) {

        String query = "UPDATE dbo.MstPlanningMesinLaminating " +
                "SET IdMesin = ?, Tanggal = ?, PlanningJamKerja = ? " +
                "WHERE IdMesin = ? AND Tanggal = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, newIdMesin);
            stmt.setString(2, DateTimeUtils.formatToDatabaseDate(newTanggal));
            stmt.setInt(3, newPlanningJamKerja);

            stmt.setString(4, oldIdMesin);
            stmt.setString(5, DateTimeUtils.formatToDatabaseDate(oldTanggal));

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Log.e("DB", "updatePlanningMesinLaminating error: " + e.getMessage());
            return false;
        }
    }


    public static boolean updatePlanningMesinCCA(
            String oldIdMesin, String oldTanggal,
            String newIdMesin, String newTanggal, int newPlanningJamKerja) {

        String query = "UPDATE dbo.MstPlanningMesinCCA " +
                "SET IdMesin = ?, Tanggal = ?, PlanningJamKerja = ? " +
                "WHERE IdMesin = ? AND Tanggal = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, newIdMesin);
            stmt.setString(2, DateTimeUtils.formatToDatabaseDate(newTanggal));
            stmt.setInt(3, newPlanningJamKerja);

            stmt.setString(4, oldIdMesin);
            stmt.setString(5, DateTimeUtils.formatToDatabaseDate(oldTanggal));

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Log.e("DB", "updatePlanningMesinCCA error: " + e.getMessage());
            return false;
        }
    }


    public static boolean updatePlanningMesinSanding(
            String oldIdMesin, String oldTanggal,
            String newIdMesin, String newTanggal, int newPlanningJamKerja) {

        String query = "UPDATE dbo.MstPlanningMesinSanding " +
                "SET IdMesin = ?, Tanggal = ?, PlanningJamKerja = ? " +
                "WHERE IdMesin = ? AND Tanggal = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, newIdMesin);
            stmt.setString(2, DateTimeUtils.formatToDatabaseDate(newTanggal));
            stmt.setInt(3, newPlanningJamKerja);

            stmt.setString(4, oldIdMesin);
            stmt.setString(5, DateTimeUtils.formatToDatabaseDate(oldTanggal));

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Log.e("DB", "updatePlanningMesinSanding error: " + e.getMessage());
            return false;
        }
    }


    public static boolean updatePlanningMesinPacking(
            String oldIdMesin, String oldTanggal,
            String newIdMesin, String newTanggal, int newPlanningJamKerja) {

        String query = "UPDATE dbo.MstPlanningMesinPacking " +
                "SET IdMesin = ?, Tanggal = ?, PlanningJamKerja = ? " +
                "WHERE IdMesin = ? AND Tanggal = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, newIdMesin);
            stmt.setString(2, DateTimeUtils.formatToDatabaseDate(newTanggal));
            stmt.setInt(3, newPlanningJamKerja);

            stmt.setString(4, oldIdMesin);
            stmt.setString(5, DateTimeUtils.formatToDatabaseDate(oldTanggal));

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Log.e("DB", "updatePlanningMesinPacking error: " + e.getMessage());
            return false;
        }
    }

    public static boolean updatePlanningMesinSLP(
            String oldNoMeja, String oldTanggal,
            String newNoMeja, String newTanggal, int newPlanningJamKerja) {

        String query = "UPDATE dbo.MstPlanningMesinSLP " +
                "SET IdMesin = ?, Tanggal = ?, PlanningJamKerja = ? " +
                "WHERE IdMesin = ? AND Tanggal = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, newNoMeja); // di SLP pakai NoMeja
            stmt.setString(2, DateTimeUtils.formatToDatabaseDate(newTanggal));
            stmt.setInt(3, newPlanningJamKerja);

            stmt.setString(4, oldNoMeja);
            stmt.setString(5, DateTimeUtils.formatToDatabaseDate(oldTanggal));

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Log.e("DB", "updatePlanningMesinSLP error: " + e.getMessage());
            return false;
        }
    }


    public static boolean deletePlanningMesinS4S(String idMesin, String tanggal) {
        String query = "DELETE FROM dbo.MstPlanningMesinS4S WHERE IdMesin = ? AND Tanggal = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, idMesin);
            stmt.setString(2, DateTimeUtils.formatToDatabaseDate(tanggal));

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Log.e("DB", "deletePlanningMesinS4S error: " + e.getMessage());
            return false;
        }
    }

    public static boolean deletePlanningMesinFJ(String idMesin, String tanggal) {
        String query = "DELETE FROM dbo.MstPlanningMesinFJ WHERE IdMesin = ? AND Tanggal = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, idMesin);
            stmt.setString(2, DateTimeUtils.formatToDatabaseDate(tanggal));

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Log.e("DB", "deletePlanningMesinFJ error: " + e.getMessage());
            return false;
        }
    }


    public static boolean deletePlanningMesinMoulding(String idMesin, String tanggal) {
        String query = "DELETE FROM dbo.MstPlanningMesinMoulding WHERE IdMesin = ? AND Tanggal = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, idMesin);
            stmt.setString(2, DateTimeUtils.formatToDatabaseDate(tanggal));

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Log.e("DB", "deletePlanningMesinMoulding error: " + e.getMessage());
            return false;
        }
    }

    public static boolean deletePlanningMesinLaminating(String idMesin, String tanggal) {
        String query = "DELETE FROM dbo.MstPlanningMesinLaminating WHERE IdMesin = ? AND Tanggal = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, idMesin);
            stmt.setString(2, DateTimeUtils.formatToDatabaseDate(tanggal));

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Log.e("DB", "deletePlanningMesinLaminating error: " + e.getMessage());
            return false;
        }
    }

    public static boolean deletePlanningMesinCCA(String idMesin, String tanggal) {
        String query = "DELETE FROM dbo.MstPlanningMesinCCA WHERE IdMesin = ? AND Tanggal = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, idMesin);
            stmt.setString(2, DateTimeUtils.formatToDatabaseDate(tanggal));

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Log.e("DB", "deletePlanningMesinCCA error: " + e.getMessage());
            return false;
        }
    }

    public static boolean deletePlanningMesinSanding(String idMesin, String tanggal) {
        String query = "DELETE FROM dbo.MstPlanningMesinSanding WHERE IdMesin = ? AND Tanggal = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, idMesin);
            stmt.setString(2, DateTimeUtils.formatToDatabaseDate(tanggal));

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Log.e("DB", "deletePlanningMesinSanding error: " + e.getMessage());
            return false;
        }
    }

    public static boolean deletePlanningMesinPacking(String idMesin, String tanggal) {
        String query = "DELETE FROM dbo.MstPlanningMesinPacking WHERE IdMesin = ? AND Tanggal = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, idMesin);
            stmt.setString(2, DateTimeUtils.formatToDatabaseDate(tanggal));

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Log.e("DB", "deletePlanningMesinPacking error: " + e.getMessage());
            return false;
        }
    }

    public static boolean deletePlanningMesinSLP(String noMeja, String tanggal) {
        String query = "DELETE FROM dbo.MstPlanningMesinSLP WHERE IdMesin = ? AND Tanggal = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, noMeja); // di SLP IdMesin = NoMeja
            stmt.setString(2, DateTimeUtils.formatToDatabaseDate(tanggal));

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Log.e("DB", "deletePlanningMesinSLP error: " + e.getMessage());
            return false;
        }
    }







}
