package com.example.myapplication.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Satu label yang berada di sebuah lokasi mapping.
 * Sumber: GET /api/mapping/lokasi-labels?idlokasi=&lt;IdLokasi&gt;
 */
public class MappingLabel {

    /** Baris detail ukuran (tebal x lebar x panjang) dari sebuah label. */
    public static class Detail {
        private final int noUrut;
        private final double tebal;
        private final double lebar;
        private final double panjang;
        private final int jmlhBatang;

        public Detail(int noUrut, double tebal, double lebar, double panjang, int jmlhBatang) {
            this.noUrut = noUrut;
            this.tebal = tebal;
            this.lebar = lebar;
            this.panjang = panjang;
            this.jmlhBatang = jmlhBatang;
        }

        public int getNoUrut() {
            return noUrut;
        }

        public double getTebal() {
            return tebal;
        }

        public double getLebar() {
            return lebar;
        }

        public double getPanjang() {
            return panjang;
        }

        public int getJmlhBatang() {
            return jmlhBatang;
        }

        private static String num(double v) {
            return (v == Math.floor(v) && !Double.isInfinite(v))
                    ? String.valueOf((long) v)
                    : String.valueOf(v);
        }

        public String getTebalStr() {
            return num(tebal);
        }

        public String getLebarStr() {
            return num(lebar);
        }

        public String getPanjangStr() {
            return num(panjang);
        }

        /** Contoh: "16 x 29 x 4". */
        public String getUkuran() {
            return num(tebal) + " x " + num(lebar) + " x " + num(panjang);
        }
    }

    private final String labelNo;
    private final String labelType;
    private final String jenis;
    private final String singkatan;
    private final String dateCreate;
    private final int jumlah;
    private final List<Detail> details;

    public MappingLabel(String labelNo, String labelType, String jenis, String singkatan,
                        String dateCreate, int jumlah, List<Detail> details) {
        this.labelNo = labelNo;
        this.labelType = labelType;
        this.jenis = jenis;
        this.singkatan = singkatan;
        this.dateCreate = dateCreate;
        this.jumlah = jumlah;
        this.details = details != null ? details : new ArrayList<>();
    }

    /** Nomor label, mis. "E.515428". */
    public String getLabelNo() {
        return labelNo;
    }

    /** Kode modul label: ST / S4S / FJ / MLD / LMT / CCA / SND / BJ. */
    public String getLabelType() {
        return labelType;
    }

    /** Nama jenis kayu, mis. "RAMBUNG - STD". */
    public String getJenis() {
        return (jenis == null || jenis.trim().isEmpty()) ? "-" : jenis;
    }

    /** Singkatan jenis kayu, mis. "RB". */
    public String getSingkatan() {
        return (singkatan == null || singkatan.trim().isEmpty()) ? labelType : singkatan;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public int getJumlah() {
        return jumlah;
    }

    public List<Detail> getDetails() {
        return details;
    }
}
