package com.example.myapplication.api;

import static com.example.myapplication.config.ApiEndpoints.BASE_URL_API;

import android.util.Log;

import com.example.myapplication.model.LokasiSummary;
import com.example.myapplication.model.MappingLabel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Panggilan API untuk modul Mapping (backend baru, bertahap menggantikan JDBC).
 */
public class MappingApi {

    private static final String TAG = "MappingApi";
    private static final int TIMEOUT_MS = 30000;

    /** Hasil pemanggilan: daftar lokasi + info error yang bisa ditampilkan ke user. */
    public static class Result {
        public final boolean ok;
        public final int httpCode;
        public final String message;
        public final List<LokasiSummary> data;

        Result(boolean ok, int httpCode, String message, List<LokasiSummary> data) {
            this.ok = ok;
            this.httpCode = httpCode;
            this.message = message;
            this.data = data;
        }
    }

    /** GET /api/mapping/lokasi-summary -> blok, idLokasi, jumlah label. */
    public static Result getLokasiSummary(String token) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(BASE_URL_API + "/api/mapping/lokasi-summary");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + (token == null ? "" : token));
            connection.setConnectTimeout(TIMEOUT_MS);
            connection.setReadTimeout(TIMEOUT_MS);

            int code = connection.getResponseCode();
            InputStream stream = (code >= 200 && code < 300)
                    ? connection.getInputStream()
                    : connection.getErrorStream();
            String body = readStream(stream);

            String trimmed = body == null ? "" : body.trim();
            if (!trimmed.startsWith("{")) {
                return new Result(false, code, "Respons server tidak valid (HTTP " + code + ").",
                        new ArrayList<>());
            }

            JSONObject json = new JSONObject(trimmed);

            if (code == 401 || code == 403) {
                return new Result(false, code, "Sesi berakhir. Silakan login ulang.", new ArrayList<>());
            }
            if (!json.optBoolean("success", false)) {
                return new Result(false, code, json.optString("message", "Gagal mengambil data lokasi."),
                        new ArrayList<>());
            }

            List<LokasiSummary> list = new ArrayList<>();
            JSONArray arr = json.optJSONArray("data");
            if (arr != null) {
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject o = arr.optJSONObject(i);
                    if (o == null) {
                        continue;
                    }
                    list.add(new LokasiSummary(
                            o.optString("IdLokasi", ""),
                            o.optString("Blok", ""),
                            o.isNull("Description") ? null : o.optString("Description", null),
                            o.optInt("JumlahLabel", 0)
                    ));
                }
            }
            return new Result(true, code, json.optString("message", "OK"), list);

        } catch (Exception e) {
            Log.e(TAG, "getLokasiSummary error: " + e.getMessage(), e);
            return new Result(false, 0, "Server tidak dapat dihubungi. Cek koneksi.", new ArrayList<>());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /** Hasil pengambilan daftar label pada satu lokasi. */
    public static class LabelResult {
        public final boolean ok;
        public final int httpCode;
        public final String message;
        public final List<MappingLabel> labels;
        public final int totalData;
        public final int totalJumlah;

        LabelResult(boolean ok, int httpCode, String message, List<MappingLabel> labels,
                    int totalData, int totalJumlah) {
            this.ok = ok;
            this.httpCode = httpCode;
            this.message = message;
            this.labels = labels;
            this.totalData = totalData;
            this.totalJumlah = totalJumlah;
        }
    }

    /** GET /api/mapping/lokasi-labels?idlokasi=&lt;idLokasi&gt; -> label + jenis kayu + detail ukuran. */
    public static LabelResult getLabelsByLokasi(String token, String idLokasi) {
        HttpURLConnection connection = null;
        try {
            String url = BASE_URL_API + "/api/mapping/lokasi-labels?idlokasi="
                    + URLEncoder.encode(idLokasi == null ? "" : idLokasi, "UTF-8");
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + (token == null ? "" : token));
            connection.setConnectTimeout(TIMEOUT_MS);
            connection.setReadTimeout(TIMEOUT_MS);

            int code = connection.getResponseCode();
            InputStream stream = (code >= 200 && code < 300)
                    ? connection.getInputStream()
                    : connection.getErrorStream();
            String body = readStream(stream);
            String trimmed = body == null ? "" : body.trim();

            if (code == 401 || code == 403) {
                return new LabelResult(false, code, "Sesi berakhir. Silakan login ulang.",
                        new ArrayList<>(), 0, 0);
            }
            if (!trimmed.startsWith("{")) {
                return new LabelResult(false, code, "Respons server tidak valid (HTTP " + code + ").",
                        new ArrayList<>(), 0, 0);
            }

            JSONObject json = new JSONObject(trimmed);
            if (!json.optBoolean("success", false)) {
                return new LabelResult(false, code, json.optString("message", "Gagal mengambil data label."),
                        new ArrayList<>(), 0, 0);
            }

            List<MappingLabel> list = new ArrayList<>();
            JSONArray arr = json.optJSONArray("data");
            if (arr != null) {
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject o = arr.optJSONObject(i);
                    if (o == null) {
                        continue;
                    }
                    List<MappingLabel.Detail> details = new ArrayList<>();
                    JSONArray dArr = o.optJSONArray("Details");
                    if (dArr != null) {
                        for (int j = 0; j < dArr.length(); j++) {
                            JSONObject d = dArr.optJSONObject(j);
                            if (d == null) {
                                continue;
                            }
                            details.add(new MappingLabel.Detail(
                                    d.optInt("NoUrut", j + 1),
                                    d.optDouble("Tebal", 0),
                                    d.optDouble("Lebar", 0),
                                    d.optDouble("Panjang", 0),
                                    d.optInt("JmlhBatang", 0)
                            ));
                        }
                    }
                    list.add(new MappingLabel(
                            o.optString("LabelNo", "-"),
                            o.optString("LabelType", ""),
                            o.isNull("Jenis") ? null : o.optString("Jenis", null),
                            o.isNull("Singkatan") ? null : o.optString("Singkatan", null),
                            o.optString("DateCreate", ""),
                            o.optInt("Jumlah", 0),
                            details
                    ));
                }
            }

            int totalData = json.optInt("totalData", list.size());
            JSONObject summary = json.optJSONObject("summary");
            int totalJumlah = summary != null ? summary.optInt("totalJumlah", 0) : 0;

            return new LabelResult(true, code, "OK", list, totalData, totalJumlah);

        } catch (Exception e) {
            Log.e(TAG, "getLabelsByLokasi error: " + e.getMessage(), e);
            return new LabelResult(false, 0, "Server tidak dapat dihubungi. Cek koneksi.",
                    new ArrayList<>(), 0, 0);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static String readStream(InputStream in) throws Exception {
        if (in == null) {
            return "";
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        return sb.toString();
    }
}
