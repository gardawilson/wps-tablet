package com.example.myapplication.config;

import com.example.myapplication.BuildConfig;

public class ApiEndpoints {

    private static final String BASE_URL = "http://" + BuildConfig.DB_IP + "/CrystalAPI/";

    public static final String CRYSTAL_REPORT_WPS_EXPORT_PDF =
            BASE_URL + "api/crystalreport/wps/export-pdf";

}
