package com.example.myapplication.config;

import com.example.myapplication.BuildConfig;

public class ApiEndpoints {

    private static final String BASE_URL = "http://" + BuildConfig.DB_IP + ":3000/";
    public static final String BASE_REPORT_MICROSERVICE = BuildConfig.BASE_REPORT_MICROSERVICE;
    public static final String BASE_URL_API = BuildConfig.BASE_URL_API;
    public static final String CRYSTAL_REPORT_WPS_EXPORT_PDF =
            BASE_URL + "api/crystalreport/wps/export-pdf";
}
