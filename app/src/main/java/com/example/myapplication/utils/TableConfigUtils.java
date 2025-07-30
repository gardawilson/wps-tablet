package com.example.myapplication.utils;

import com.example.myapplication.R;
import com.example.myapplication.api.ProsesProduksiApi;
import com.example.myapplication.model.TableConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableConfigUtils {

    public static Map<String, TableConfig> getTableConfigMap(
            List<String> noS4SList,
            List<String> noSTList,
            List<String> noMouldingList,
            List<String> noFJList,
            List<String> noCCList,
            List<String> noReprosesList,
            List<String> noLaminatingList,
            List<String> noSandingList,
            List<String> noPackingList
    ) {
        Map<String, TableConfig> tableConfigMap = new HashMap<>();

        tableConfigMap.put("R", new TableConfig(
                "S4S_h", "S4S_d", "NoS4S", R.id.noS4STableLayout, noS4SList,
                ProsesProduksiApi::findS4SResultTable, R.id.sumS4SLabel
        ));

        tableConfigMap.put("E", new TableConfig(
                "ST_h", "ST_d", "NoST", R.id.noSTTableLayout, noSTList,
                ProsesProduksiApi::findSTResultTable, R.id.sumSTLabel
        ));

        tableConfigMap.put("T", new TableConfig(
                "Moulding_h", "Moulding_d", "NoMoulding", R.id.noMouldingTableLayout, noMouldingList,
                ProsesProduksiApi::findMouldingResultTable, R.id.sumMouldingLabel
        ));

        tableConfigMap.put("S", new TableConfig(
                "FJ_h", "FJ_d", "NoFJ", R.id.noFJTableLayout, noFJList,
                ProsesProduksiApi::findFJResultTable, R.id.sumFJLabel
        ));

        tableConfigMap.put("V", new TableConfig(
                "CCAkhir_h", "CCAkhir_d", "NoCCAkhir", R.id.noCCTableLayout, noCCList,
                ProsesProduksiApi::findCCAkhirResultTable, R.id.sumCCLabel
        ));

        tableConfigMap.put("Y", new TableConfig(
                "Reproses_h", "Reproses_d", "NoReproses", R.id.noReprosesTableLayout, noReprosesList,
                ProsesProduksiApi::findReprosesResultTable, R.id.sumReprosesLabel
        ));

        tableConfigMap.put("U", new TableConfig(
                "Laminating_h", "Laminating_d", "NoLaminating", R.id.noLaminatingTableLayout, noLaminatingList,
                ProsesProduksiApi::findLaminatingResultTable, R.id.sumLaminatingLabel
        ));

        tableConfigMap.put("W", new TableConfig(
                "Sanding_h", "Sanding_d", "NoSanding", R.id.noSandingTableLayout, noSandingList,
                ProsesProduksiApi::findSandingResultTable, R.id.sumSandingLabel
        ));

        tableConfigMap.put("I", new TableConfig(
                "BarangJadi_h", "BarangJadi_d", "NoBJ", R.id.noPackingTableLayout, noPackingList,
                ProsesProduksiApi::findPackingResultTable, R.id.sumPackingLabel
        ));

        return tableConfigMap;
    }
}
