package com.example.myapplication.api;

import android.util.Log;

import com.example.myapplication.DatabaseConfig;
import com.example.myapplication.model.ProductionData;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.DriverManager;

public class ProductionApi {

    public static List<ProductionData> getProductionData() {
        List<ProductionData> productionDataList = new ArrayList<>();

        try (Connection con = DriverManager.getConnection(DatabaseConfig.getConnectionUrl());
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT TOP 30 NoProduksi, Shift, Tanggal, IdMesin, IdOperator FROM S4SProduksi_h")) {

            while (rs.next()) {
                String noProduksi = rs.getString("NoProduksi");
                String shift = rs.getString("Shift");
                String tanggal = rs.getString("Tanggal");
                String mesin = rs.getString("IdMesin");
                String operator = rs.getString("IdOperator");

                Log.d("Database Data", "NoProduksi: " + noProduksi +
                        ", Shift: " + shift +
                        ", Tanggal: " + tanggal +
                        ", Mesin: " + mesin +
                        ", Operator: " + operator);

                productionDataList.add(new ProductionData(noProduksi, shift, tanggal, mesin, operator));
            }
        } catch (SQLException e) {
            Log.e("Database Fetch Error", "Error fetching data: " + e.getMessage());
        }

        return productionDataList;
    }
}