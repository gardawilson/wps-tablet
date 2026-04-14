package com.example.myapplication.api;

import android.content.Context;
import android.util.Log;

import com.example.myapplication.BuildConfig;
import com.example.myapplication.utils.SharedPrefUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class DeviceServiceApi {
    private static final String TAG = "DeviceServiceApi";
    private static final String BASE = BuildConfig.DEVICE_SERVICE_BASE;
    private static final String ENDPOINT_PRINTERS = BASE + "api/devices/printers";
    private static final String ENDPOINT_LOG = BASE + "api/devices/printers/log";
    private static final String SOURCE_APP = "WPS";
    private static final int TIMEOUT_MS = 10_000;

    private DeviceServiceApi() {
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Model
    // ─────────────────────────────────────────────────────────────────────────

    public static class PrinterData {
        public final String id;
        public final String identifier;  // MAC address (uppercase)
        public final String name;        // alias, nullable
        public final String printUsage;  // e.g. "1/1500"
        public final String lastUsedAt;
        public final String status;      // e.g. "NORMAL"

        public PrinterData(String id, String identifier, String name,
                           String printUsage, String lastUsedAt, String status) {
            this.id = id;
            this.identifier = identifier != null ? identifier.toUpperCase() : "";
            this.name = (name != null && !name.trim().isEmpty()) ? name : null;
            this.printUsage = printUsage != null ? printUsage : "-";
            this.lastUsedAt = lastUsedAt != null ? lastUsedAt : "-";
            this.status = status != null ? status : "-";
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Callbacks
    // ─────────────────────────────────────────────────────────────────────────

    public interface FetchPrintersCallback {
        /** Called on background thread — post to main thread before touching UI. */
        void onResult(List<PrinterData> printers);
        void onError(String message);
    }

    public interface RegisterPrinterCallback {
        /** Called on background thread — post to main thread before touching UI. */
        void onSuccess();
        void onError(String message);
    }

    public interface FetchPrinterByIdCallback {
        void onResult(PrinterData printer);
        void onNotFound();
        void onError(String message);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GET /api/devices/printers
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Fetches all registered printers from the device-service.
     * Callback is invoked on a background thread.
     */
    public static void fetchRegisteredPrinters(FetchPrintersCallback callback) {
        new Thread(() -> {
            try {
                URL url = new URL(ENDPOINT_PRINTERS);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(TIMEOUT_MS);
                conn.setReadTimeout(TIMEOUT_MS);

                int code = conn.getResponseCode();
                if (code == 200) {
                    String json = readResponse(conn);
                    JSONObject obj = new JSONObject(json);
                    JSONArray arr = obj.getJSONArray("printers");
                    List<PrinterData> list = new ArrayList<>();
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject p = arr.getJSONObject(i);
                        list.add(new PrinterData(
                                p.optString("id", ""),
                                p.optString("identifier", ""),
                                p.isNull("name") ? null : p.optString("name", null),
                                p.optString("printUsage", "-"),
                                p.optString("lastUsedAt", "-"),
                                p.optString("status", "-")
                        ));
                    }
                    Log.d(TAG, "fetchRegisteredPrinters: found=" + list.size());
                    callback.onResult(list);
                } else {
                    Log.w(TAG, "fetchRegisteredPrinters: HTTP " + code);
                    callback.onError("HTTP " + code);
                }
                conn.disconnect();
            } catch (Exception e) {
                Log.e(TAG, "fetchRegisteredPrinters: error", e);
                callback.onError(e.getMessage());
            }
        }).start();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GET /api/devices/printers/{id} — ambil data satu printer by ID
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Fetch data satu printer berdasarkan ID (bukan MAC).
     * Selalu mengambil data terbaru dari server (tidak ada cache).
     * Callback dipanggil di background thread.
     */
    public static void fetchPrinterById(String id, FetchPrinterByIdCallback callback) {
        new Thread(() -> {
            try {
                URL url = new URL(ENDPOINT_PRINTERS + "/" + id);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(TIMEOUT_MS);
                conn.setReadTimeout(TIMEOUT_MS);

                int code = conn.getResponseCode();
                if (code == 200) {
                    String json = readResponse(conn);
                    JSONObject p = new JSONObject(json);
                    PrinterData data = new PrinterData(
                            p.optString("id", ""),
                            p.optString("identifier", ""),
                            p.isNull("name") ? null : p.optString("name", null),
                            p.optString("printUsage", "-"),
                            p.optString("lastUsedAt", "-"),
                            p.optString("status", "-")
                    );
                    Log.d(TAG, "fetchPrinterById: id=" + id
                            + " usage=" + data.printUsage + " status=" + data.status);
                    callback.onResult(data);
                } else if (code == 404) {
                    Log.w(TAG, "fetchPrinterById: not found id=" + id);
                    callback.onNotFound();
                } else {
                    Log.w(TAG, "fetchPrinterById: HTTP " + code + " id=" + id);
                    callback.onError("HTTP " + code);
                }
                conn.disconnect();
            } catch (Exception e) {
                Log.e(TAG, "fetchPrinterById: error id=" + id, e);
                callback.onError(e.getMessage());
            }
        }).start();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // POST /api/devices/printers — register new printer
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Registers a new printer with the device-service.
     * Callback is invoked on a background thread.
     *
     * @param printerMac MAC address (e.g. "AA:BB:CC:DD:EE:FF")
     * @param name       Human-readable printer name (e.g. "Printer Lantai 1")
     */
    public static void registerPrinter(String printerMac, String name, RegisterPrinterCallback callback) {
        new Thread(() -> {
            try {
                JSONObject body = new JSONObject();
                body.put("mac", printerMac);
                body.put("name", name);
                byte[] payload = body.toString().getBytes(StandardCharsets.UTF_8);

                URL url = new URL(ENDPOINT_PRINTERS);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setConnectTimeout(TIMEOUT_MS);
                conn.setReadTimeout(TIMEOUT_MS);
                conn.setDoOutput(true);

                try (OutputStream os = conn.getOutputStream()) {
                    os.write(payload);
                }

                int code = conn.getResponseCode();
                if (code == 200 || code == 201) {
                    Log.d(TAG, "registerPrinter: success mac=" + printerMac);
                    callback.onSuccess();
                } else {
                    Log.w(TAG, "registerPrinter: HTTP " + code + " mac=" + printerMac);
                    callback.onError("HTTP " + code);
                }
                conn.disconnect();
            } catch (Exception e) {
                Log.e(TAG, "registerPrinter: error mac=" + printerMac, e);
                callback.onError(e.getMessage());
            }
        }).start();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // POST /api/devices/printers/log — fire-and-forget after successful print
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Fire-and-forget: logs a successful print job to the device-service API.
     * Runs on a new daemon thread — never blocks the caller.
     */
    public static void logPrinterUsageAsync(Context context, String printerMac) {
        if (printerMac == null || printerMac.isEmpty()) {
            Log.w(TAG, "logPrinterUsageAsync: skipped — printerMac is empty");
            return;
        }
        Thread t = new Thread(() -> {
            try {
                String username = SharedPrefUtils.getUsername(context);
                if (username == null) {
                    username = "";
                }

                JSONObject body = new JSONObject();
                body.put("printerId", printerMac);
                body.put("sourceApp", SOURCE_APP);
                body.put("user", username);
                byte[] payload = body.toString().getBytes(StandardCharsets.UTF_8);

                URL url = new URL(ENDPOINT_LOG);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setConnectTimeout(TIMEOUT_MS);
                conn.setReadTimeout(TIMEOUT_MS);
                conn.setDoOutput(true);

                try (OutputStream os = conn.getOutputStream()) {
                    os.write(payload);
                }

                int code = conn.getResponseCode();
                if (code == 201) {
                    Log.d(TAG, "logPrinterUsageAsync: success printer=" + printerMac);
                } else if (code == 404) {
                    Log.w(TAG, "logPrinterUsageAsync: printer not registered printer=" + printerMac);
                } else {
                    Log.w(TAG, "logPrinterUsageAsync: unexpected status=" + code + " printer=" + printerMac);
                }
                conn.disconnect();
            } catch (Exception e) {
                Log.e(TAG, "logPrinterUsageAsync: network failure printer=" + printerMac, e);
            }
        });
        t.setDaemon(true);
        t.start();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Internal helpers
    // ─────────────────────────────────────────────────────────────────────────

    private static String readResponse(HttpURLConnection conn) throws Exception {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        return sb.toString();
    }
}
