package com.example.myapplication.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class AuditDisplayFormatter {

    private static final Set<String> PRINT_FIELD_KEYS = new LinkedHashSet<>(Arrays.asList(
            "hasbeenprinted",
            "lastprintdate"
    ));
    private static final Map<String, String> TABLE_ALIAS = new LinkedHashMap<>();
    private static final Map<String, String> FIELD_ALIAS = new LinkedHashMap<>();
    private static final Map<String, String> FIELD_ALIAS_CANONICAL = new LinkedHashMap<>();
    private static final Map<String, String> PREFIX_ALIAS = new LinkedHashMap<>();

    static {
        TABLE_ALIAS.put("BarangJadi_h", "Barang Jadi (Header)");
        TABLE_ALIAS.put("BarangJadi_d", "Barang Jadi (Detail)");
        TABLE_ALIAS.put("PackingProduksiOutput", "Output Produksi Packing");

        TABLE_ALIAS.put("Sanding_h", "Sanding (Header)");
        TABLE_ALIAS.put("Sanding_d", "Sanding (Detail)");
        TABLE_ALIAS.put("SandingProduksiOutput", "Output Produksi Sanding");

        TABLE_ALIAS.put("CCAkhir_h", "CC Akhir (Header)");
        TABLE_ALIAS.put("CCAkhir_d", "CC Akhir (Detail)");
        TABLE_ALIAS.put("CCAkhirProduksiOutput", "Output Produksi CC Akhir");
        TABLE_ALIAS.put("BongkarSusunOutputCCAkhir", "Output Bongkar Susun CC Akhir");

        TABLE_ALIAS.put("Laminating_h", "Laminating (Header)");
        TABLE_ALIAS.put("Laminating_d", "Laminating (Detail)");
        TABLE_ALIAS.put("LaminatingProduksiOutput", "Output Produksi Laminating");

        TABLE_ALIAS.put("Moulding_h", "Moulding (Header)");
        TABLE_ALIAS.put("Moulding_d", "Moulding (Detail)");
        TABLE_ALIAS.put("MouldingProduksiOutput", "Output Produksi Moulding");

        TABLE_ALIAS.put("FJ_h", "Finger Joint (Header)");
        TABLE_ALIAS.put("FJ_d", "Finger Joint (Detail)");
        TABLE_ALIAS.put("FJProduksiOutput", "Output Produksi Finger Joint");
        TABLE_ALIAS.put("S4S_h", "S4S (Header)");
        TABLE_ALIAS.put("S4S_d", "S4S (Detail)");
        TABLE_ALIAS.put("S4SProduksiOutput", "Output Produksi S4S");

        FIELD_ALIAS.put("NoBJ", "No. Barang Jadi");
        FIELD_ALIAS.put("NoS4S", "No. S4S");
        FIELD_ALIAS.put("NoSanding", "No. Sanding");
        FIELD_ALIAS.put("NoCCAkhir", "No. CC Akhir");
        FIELD_ALIAS.put("NoLaminating", "No. Laminating");
        FIELD_ALIAS.put("NoMoulding", "No. Moulding");
        FIELD_ALIAS.put("NoFJ", "No. Finger Joint");
        FIELD_ALIAS.put("NoProduksi", "No. Produksi");
        FIELD_ALIAS.put("NoSPK", "No. SPK");
        FIELD_ALIAS.put("NoSPKAsal", "No. SPK Asal");
        FIELD_ALIAS.put("NoBongkarSusun", "No. Bongkar Susun");
        FIELD_ALIAS.put("NoUrut", "Urutan");

        FIELD_ALIAS.put("IdJenisKayu", "Jenis Kayu");
        FIELD_ALIAS.put("JenisKayu", "Jenis Kayu");
        FIELD_ALIAS.put("IdBarangJadi", "Kategori Barang Jadi");
        FIELD_ALIAS.put("NamaBarangJadi", "Barang Jadi");
        FIELD_ALIAS.put("IdGrade", "Grade");
        FIELD_ALIAS.put("NamaGrade", "Grade");
        FIELD_ALIAS.put("Grade", "Grade");
        FIELD_ALIAS.put("IdOrgTelly", "Telly");
        FIELD_ALIAS.put("NamaOrgTelly", "Telly");
        FIELD_ALIAS.put("OrgTelly", "Telly");
        FIELD_ALIAS.put("Telly", "Telly");
        FIELD_ALIAS.put("IdFJProfile", "Profil FJ");
        FIELD_ALIAS.put("Profile", "Profile FJ");
        FIELD_ALIAS.put("IdWarehouse", "Gudang");
        FIELD_ALIAS.put("NamaWarehouse", "Gudang");
        FIELD_ALIAS.put("IdLokasi", "Lokasi");
        FIELD_ALIAS.put("IdUOMTblLebar", "Satuan Lebar");
        FIELD_ALIAS.put("NamaUOMTblLebar", "Satuan Lebar");
        FIELD_ALIAS.put("IdUOMPanjang", "Satuan Panjang");
        FIELD_ALIAS.put("NamaUOMPanjang", "Satuan Panjang");
        FIELD_ALIAS.put("IdFisik", "Fisik");
        FIELD_ALIAS.put("SingkatanFisik", "Fisik");

        FIELD_ALIAS.put("DateCreate", "Tanggal");
        FIELD_ALIAS.put("Jam", "Jam");
        FIELD_ALIAS.put("LastPrintDate", "Tanggal Cetak Terakhir");
        FIELD_ALIAS.put("Remark", "Catatan");

        FIELD_ALIAS.put("Tebal", "Tebal");
        FIELD_ALIAS.put("Lebar", "Lebar");
        FIELD_ALIAS.put("Panjang", "Panjang");
        FIELD_ALIAS.put("JmlhBatang", "Jumlah Batang");

        FIELD_ALIAS.put("IsReject", "Reject");
        FIELD_ALIAS.put("IsLembur", "Lembur");
        FIELD_ALIAS.put("HasBeenPrinted", "Sudah Dicetak");

        FIELD_ALIAS.put("RequestId", "Request ID");
        FIELD_ALIAS.put("RequestID", "Request ID");
        FIELD_ALIAS.put("AuditId", "Audit ID");
        FIELD_ALIAS.put("EventTime", "Waktu");
        FIELD_ALIAS.put("Actor", "Actor");
        FIELD_ALIAS.put("Username", "Username");
        FIELD_ALIAS.put("TableName", "Tabel");
        FIELD_ALIAS.put("Action", "Aksi");

        PREFIX_ALIAS.put("I", "Barang Jadi");
        PREFIX_ALIAS.put("W", "Sanding");
        PREFIX_ALIAS.put("V", "CC Akhir");
        PREFIX_ALIAS.put("U", "Laminating");
        PREFIX_ALIAS.put("T", "Moulding");
        PREFIX_ALIAS.put("S", "Finger Joint");
        PREFIX_ALIAS.put("X", "Produksi Packing");
        PREFIX_ALIAS.put("WA", "Produksi Sanding");
        PREFIX_ALIAS.put("VA", "Produksi CC Akhir");
        PREFIX_ALIAS.put("UA", "Produksi Laminating");
        PREFIX_ALIAS.put("TA", "Produksi Moulding");
        PREFIX_ALIAS.put("SA", "Produksi Finger Joint");
        PREFIX_ALIAS.put("Z", "Bongkar Susun");
        PREFIX_ALIAS.put("RA", "Produksi S4S");
        PREFIX_ALIAS.put("RB", "Produksi Moulding");
        PREFIX_ALIAS.put("RC", "Produksi FJ");
        PREFIX_ALIAS.put("RD", "Produksi Laminating");
        PREFIX_ALIAS.put("RE", "Produksi CC Akhir");
        PREFIX_ALIAS.put("RF", "Produksi Sanding");
        PREFIX_ALIAS.put("RG", "Produksi Packing");

        for (Map.Entry<String, String> entry : FIELD_ALIAS.entrySet()) {
            FIELD_ALIAS_CANONICAL.put(canonicalKey(entry.getKey()), entry.getValue());
        }
    }

    public static String actionAlias(String action) {
        if (action == null) return "-";
        String upper = action.trim().toUpperCase(Locale.US);
        switch (upper) {
            case "INSERT":
                return "CREATE";
            case "UPDATE":
                return "EDIT";
            case "PRINT":
                return "PRINT";
            case "DELETE":
                return "DELETE";
            case "CONSUME":
                return "CONSUME";
            case "UNCONSUME":
                return "UNCONSUME";
            case "PRODUCE":
                return "PRODUCE";
            case "UNPRODUCE":
                return "UNPRODUCE";
            case "MAPPING":
                return "MAPPING";
            default:
                return upper;
        }
    }

    public static String tableAlias(String tableName) {
        if (tableName == null || tableName.trim().isEmpty()) return "-";
        String alias = TABLE_ALIAS.get(tableName);
        return alias == null ? tableName : alias;
    }

    public static String formatPk(String rawPk) {
        JSONObject pkObj = parseToObject(rawPk);
        if (pkObj == null) {
            return safe(rawPk);
        }

        List<String> lines = new ArrayList<>();
        Iterator<String> keys = pkObj.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            String value = stringifyValue(pkObj.opt(key));
            String prefixLabel = resolvePrefixAlias(value);
            if (prefixLabel == null) {
                lines.add("- " + aliasField(key) + ": " + value);
            } else {
                lines.add("- " + aliasField(key) + ": " + value + " (" + prefixLabel + ")");
            }
        }
        return joinLines(lines);
    }

    public static String formatData(String tableName, String rawData) {
        JSONObject obj = parseToObject(rawData);
        if (obj == null) return safe(rawData);

        List<String> orderedKeys = orderedKeys(obj);
        List<String> lines = new ArrayList<>();
        for (String key : orderedKeys) {
            lines.add("- " + aliasField(key) + ": " + stringifyValue(obj.opt(key)));
        }
        return joinLines(lines);
    }

    public static String formatUpdateDiff(String tableName, String oldRaw, String newRaw) {
        return formatDiff(oldRaw, newRaw, false);
    }

    public static String formatActionDiff(String tableName, String action, String oldRaw, String newRaw) {
        if ("PRINT".equalsIgnoreCase(action)) {
            return formatDiff(oldRaw, newRaw, true);
        }
        return formatUpdateDiff(tableName, oldRaw, newRaw);
    }

    public static boolean isPrintField(String key) {
        return PRINT_FIELD_KEYS.contains(canonicalKey(key));
    }

    private static String formatDiff(String oldRaw, String newRaw, boolean printOnlyFields) {
        JSONObject oldObj = parseToObject(oldRaw);
        JSONObject newObj = parseToObject(newRaw);
        if (oldObj == null || newObj == null) return "-";

        Set<String> keys = new LinkedHashSet<>();
        keys.addAll(orderedKeys(oldObj));
        keys.addAll(orderedKeys(newObj));

        List<String> changes = new ArrayList<>();
        for (String key : keys) {
            if (printOnlyFields && !isPrintField(key)) {
                continue;
            }
            String oldVal = stringifyValue(oldObj.opt(key));
            String newVal = stringifyValue(newObj.opt(key));
            if (!oldVal.equals(newVal)) {
                changes.add("- " + aliasField(key) + ": " + oldVal + " -> " + newVal);
            }
        }
        if (changes.isEmpty()) {
            return "- Tidak ada perubahan nilai";
        }
        return joinLines(changes);
    }

    public static LinkedHashMap<String, String> toFieldMap(String rawData) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        JSONObject obj = parseToObject(rawData);
        if (obj == null) return map;

        List<String> keys = orderedKeys(obj);
        for (String key : keys) {
            map.put(key, stringifyValue(obj.opt(key)));
        }
        return map;
    }

    public static String fieldAlias(String key) {
        return aliasField(key);
    }

    private static JSONObject parseToObject(String raw) {
        if (raw == null) return null;
        String text = raw.trim();
        if (text.isEmpty() || "-".equals(text) || "NULL".equalsIgnoreCase(text)) {
            return null;
        }
        try {
            if (text.startsWith("{")) {
                return new JSONObject(text);
            }
            if (text.startsWith("[")) {
                JSONArray arr = new JSONArray(text);
                if (arr.length() == 0) return null;
                Object first = arr.opt(0);
                if (first instanceof JSONObject) {
                    return (JSONObject) first;
                }
            }
        } catch (Exception ignored) {
            return null;
        }
        return null;
    }

    private static List<String> orderedKeys(JSONObject obj) {
        List<String> preferred = Arrays.asList(
                "NoProduksi", "NoBJ", "NoSanding", "NoCCAkhir", "NoLaminating", "NoMoulding", "NoFJ",
                "NoSPK", "NoSPKAsal", "NoBongkarSusun", "NoUrut",
                "IdJenisKayu", "IdBarangJadi", "IdGrade", "IdOrgTelly", "IdFJProfile",
                "IdWarehouse", "IdLokasi", "IdUOMTblLebar", "IdUOMPanjang", "IdFisik",
                "JenisKayu", "NamaGrade", "NamaOrgTelly", "Profile",
                "NamaWarehouse", "NamaUOMTblLebar", "NamaUOMPanjang", "SingkatanFisik",
                "DateCreate", "Jam",
                "Tebal", "Lebar", "Panjang", "JmlhBatang",
                "IsReject", "IsLembur", "HasBeenPrinted",
                "Remark", "LastPrintDate"
        );

        Set<String> seen = new LinkedHashSet<>();
        List<String> result = new ArrayList<>();

        for (String key : preferred) {
            if (obj.has(key) && seen.add(key)) {
                result.add(key);
            }
        }

        Iterator<String> it = obj.keys();
        while (it.hasNext()) {
            String key = it.next();
            if (seen.add(key)) {
                result.add(key);
            }
        }
        return result;
    }

    private static String aliasField(String key) {
        if (key == null) return "-";

        String trimmedKey = key.trim();
        String alias = FIELD_ALIAS.get(trimmedKey);
        if (alias != null) return alias;

        for (Map.Entry<String, String> entry : FIELD_ALIAS.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(trimmedKey)) {
                return entry.getValue();
            }
        }

        String canonicalAlias = FIELD_ALIAS_CANONICAL.get(canonicalKey(trimmedKey));
        if (canonicalAlias != null) return canonicalAlias;

        if (trimmedKey.toLowerCase(Locale.US).startsWith("nama") && trimmedKey.length() > 4) {
            String stripped = trimmedKey.substring(4); // contoh: NamaGrade -> Grade
            String strippedAlias = FIELD_ALIAS.get(stripped);
            if (strippedAlias != null) return strippedAlias;
            for (Map.Entry<String, String> entry : FIELD_ALIAS.entrySet()) {
                if (entry.getKey().equalsIgnoreCase(stripped)) {
                    return entry.getValue();
                }
            }
            String strippedCanonicalAlias = FIELD_ALIAS_CANONICAL.get(canonicalKey(stripped));
            if (strippedCanonicalAlias != null) return strippedCanonicalAlias;
        }

        return humanizeFieldName(key);
    }

    private static String canonicalKey(String key) {
        if (key == null) return "";
        return key.replaceAll("[^A-Za-z0-9]", "").toLowerCase(Locale.US);
    }

    private static String humanizeFieldName(String key) {
        if (key == null || key.trim().isEmpty()) return "-";
        String text = key.replace('_', ' ').replace('-', ' ');
        text = text.replaceAll("([a-z])([A-Z])", "$1 $2");
        text = text.replaceAll("\\s+", " ").trim();
        if (text.isEmpty()) return key;
        if (text.equals(text.toUpperCase(Locale.US))) {
            return text;
        }
        return Character.toUpperCase(text.charAt(0)) + text.substring(1);
    }

    private static String stringifyValue(Object value) {
        if (value == null || value == JSONObject.NULL) return "-";
        if (value instanceof Number) {
            return normalizeNumber((Number) value);
        }
        if (value instanceof Boolean) {
            return ((Boolean) value) ? "Ya" : "Tidak";
        }
        return String.valueOf(value);
    }

    private static String normalizeNumber(Number n) {
        double d = n.doubleValue();
        if (Math.floor(d) == d) {
            return String.valueOf((long) d);
        }
        return new DecimalFormat("0.###").format(d);
    }

    private static String resolvePrefixAlias(String text) {
        if (text == null) return null;
        int dotIdx = text.indexOf('.');
        if (dotIdx <= 0) return null;
        String prefix = text.substring(0, dotIdx).toUpperCase(Locale.US);
        String alias = PREFIX_ALIAS.get(prefix);
        if (alias != null) return alias;

        // fallback untuk prefix 2 huruf seperti WA/VA kalau ada
        if (prefix.length() >= 2) {
            alias = PREFIX_ALIAS.get(prefix.substring(0, 2));
            if (alias != null) return alias;
        }
        return null;
    }

    private static String safe(String text) {
        if (text == null || text.trim().isEmpty()) return "-";
        return text;
    }

    private static String joinLines(List<String> lines) {
        if (lines == null || lines.isEmpty()) return "-";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lines.size(); i++) {
            if (i > 0) sb.append("\n");
            sb.append(lines.get(i));
        }
        return sb.toString();
    }
}
