package com.example.myapplication.utils;

import com.example.myapplication.R;
import com.example.myapplication.api.ProductionApi;
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
            List<String> noReprosesList
    ) {
        Map<String, TableConfig> tableConfigMap = new HashMap<>();

        tableConfigMap.put("R", new TableConfig(
                "S4S_h", "S4S_d", "NoS4S", R.id.noS4STableLayout, noS4SList,
                ProductionApi::findS4SResultTable, R.id.sumS4SLabel
        ));

        tableConfigMap.put("E", new TableConfig(
                "ST_h", "ST_d", "NoST", R.id.noSTTableLayout, noSTList,
                ProductionApi::findSTResultTable, R.id.sumSTLabel
        ));

        tableConfigMap.put("T", new TableConfig(
                "Moulding_h", "Moulding_d", "NoMoulding", R.id.noMouldingTableLayout, noMouldingList,
                ProductionApi::findMouldingResultTable, R.id.sumMouldingLabel
        ));

        tableConfigMap.put("S", new TableConfig(
                "FJ_h", "FJ_d", "NoFJ", R.id.noFJTableLayout, noFJList,
                ProductionApi::findFJResultTable, R.id.sumFJLabel
        ));

        tableConfigMap.put("V", new TableConfig(
                "CCAkhir_h", "CCAkhir_d", "NoCCAkhir", R.id.noCCTableLayout, noCCList,
                ProductionApi::findCCAkhirResultTable, R.id.sumCCLabel
        ));

        tableConfigMap.put("Y", new TableConfig(
                "Reproses_h", "Reproses_d", "NoReproses", R.id.noReprosesTableLayout, noReprosesList,
                ProductionApi::findReprosesResultTable, R.id.sumReprosesLabel
        ));

        return tableConfigMap;
    }
}
