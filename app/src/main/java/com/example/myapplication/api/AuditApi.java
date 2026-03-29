package com.example.myapplication.api;

import static com.example.myapplication.config.ApiEndpoints.BASE_URL_API;

import android.util.Log;

import com.example.myapplication.model.AuditItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AuditApi {

    private static final String TAG = "AuditApi";
    private static final int CONNECT_TIMEOUT = 30000;
    private static final int READ_TIMEOUT = 30000;

    public static class AuditPageResponse {
        private final boolean success;
        private final String message;
        private final int page;
        private final int limit;
        private final int total;
        private final int totalPages;
        private final List<AuditItem> data;

        public AuditPageResponse(
                boolean success,
                String message,
                int page,
                int limit,
                int total,
                int totalPages,
                List<AuditItem> data
        ) {
            this.success = success;
            this.message = message;
            this.page = page;
            this.limit = limit;
            this.total = total;
            this.totalPages = totalPages;
            this.data = data;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public int getPage() {
            return page;
        }

        public int getLimit() {
            return limit;
        }

        public int getTotal() {
            return total;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public List<AuditItem> getData() {
            return data;
        }
    }

    public static AuditPageResponse getAuditList(
            String token,
            int page,
            int limit,
            String q,
            String tableName,
            String action,
            String actor,
            String requestId,
            String dateFrom,
            String dateTo
    ) {
        try {
            StringBuilder urlBuilder = new StringBuilder(BASE_URL_API)
                    .append("/api/audit")
                    .append("?page=").append(page)
                    .append("&limit=").append(limit);

            appendQueryParam(urlBuilder, "q", q);
            appendQueryParam(urlBuilder, "tableName", tableName);
            appendQueryParam(urlBuilder, "action", action);
            appendQueryParam(urlBuilder, "actor", actor);
            appendQueryParam(urlBuilder, "requestId", requestId);
            appendQueryParam(urlBuilder, "dateFrom", dateFrom);
            appendQueryParam(urlBuilder, "dateTo", dateTo);

            return requestAuditPage(token, urlBuilder.toString(), page, limit);
        } catch (Exception e) {
            Log.e(TAG, "Error getAuditList: " + e.getMessage(), e);
            return emptyErrorResponse(page, limit, "Gagal mengambil data audit: " + e.getMessage());
        }
    }

    public static AuditPageResponse getAuditByPk(
            String token,
            String pkValue,
            int page,
            int limit,
            String tableName,
            String action
    ) {
        try {
            String encodedPk = encodePathSegment(pkValue);
            StringBuilder urlBuilder = new StringBuilder(BASE_URL_API)
                    .append("/api/audit/pk/")
                    .append(encodedPk)
                    .append("?page=").append(page)
                    .append("&limit=").append(limit);

            appendQueryParam(urlBuilder, "tableName", tableName);
            appendQueryParam(urlBuilder, "action", action);

            return requestAuditPage(token, urlBuilder.toString(), page, limit);
        } catch (Exception e) {
            Log.e(TAG, "Error getAuditByPk: " + e.getMessage(), e);
            return emptyErrorResponse(page, limit, "Gagal mencari audit berdasarkan PK: " + e.getMessage());
        }
    }

    private static AuditPageResponse requestAuditPage(String token, String urlString, int defaultPage, int defaultLimit) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);

            int responseCode = connection.getResponseCode();
            String contentType = connection.getHeaderField("Content-Type");
            InputStream inputStream = (responseCode >= 200 && responseCode < 300)
                    ? connection.getInputStream()
                    : connection.getErrorStream();

            String responseText = readStream(inputStream);
            String responsePreview = safePreview(responseText, 220);
            Log.d(TAG, "requestAuditPage url=" + urlString
                    + " code=" + responseCode
                    + " contentType=" + (contentType == null ? "-" : contentType)
                    + " preview=" + responsePreview);

            String trimmed = responseText == null ? "" : responseText.trim();
            if (!trimmed.startsWith("{")) {
                return emptyErrorResponse(
                        defaultPage,
                        defaultLimit,
                        "Response server bukan JSON (HTTP " + responseCode + "). Cek endpoint/token."
                );
            }

            JSONObject json = new JSONObject(responseText);
            boolean success = json.optBoolean("success", false);
            String message = json.optString("message", success ? "OK" : "Request gagal");

            List<AuditItem> items = new ArrayList<>();
            JSONArray dataArr = json.optJSONArray("data");
            if (dataArr != null) {
                for (int i = 0; i < dataArr.length(); i++) {
                    JSONObject itemObj = dataArr.optJSONObject(i);
                    if (itemObj != null) {
                        items.add(AuditItem.fromJson(itemObj));
                    }
                }
            }

            return new AuditPageResponse(
                    success,
                    message,
                    json.optInt("page", defaultPage),
                    json.optInt("limit", defaultLimit),
                    json.optInt("total", items.size()),
                    json.optInt("totalPages", 1),
                    items
            );
        } catch (Exception e) {
            Log.e(TAG, "Error requestAuditPage: " + e.getMessage(), e);
            return emptyErrorResponse(defaultPage, defaultLimit, "Terjadi kesalahan koneksi: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static String safePreview(String text, int maxLen) {
        if (text == null) return "";
        String oneLine = text.replace("\r", " ").replace("\n", " ").trim();
        if (oneLine.length() <= maxLen) return oneLine;
        return oneLine.substring(0, maxLen) + "...";
    }

    private static void appendQueryParam(StringBuilder builder, String key, String value) throws Exception {
        if (value == null) return;
        String trimmed = value.trim();
        if (trimmed.isEmpty()) return;
        builder.append("&")
                .append(URLEncoder.encode(key, "UTF-8"))
                .append("=")
                .append(URLEncoder.encode(trimmed, "UTF-8"));
    }

    private static String encodePathSegment(String value) throws Exception {
        return URLEncoder.encode(value, "UTF-8").replace("+", "%20");
    }

    private static String readStream(InputStream inputStream) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        return sb.toString();
    }

    private static AuditPageResponse emptyErrorResponse(int page, int limit, String message) {
        return new AuditPageResponse(
                false,
                message,
                page,
                limit,
                0,
                1,
                new ArrayList<>()
        );
    }
}
