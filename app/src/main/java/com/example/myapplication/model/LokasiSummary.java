package com.example.myapplication.model;

/**
 * Ringkasan satu blok lokasi gudang: id, blok, dan jumlah label yang masih
 * berada di lokasi tersebut. Sumber: GET /api/mapping/lokasi-summary.
 */
public class LokasiSummary {
    private final String idLokasi;
    private final String blok;
    private final String description;
    private final int jumlahLabel;

    public LokasiSummary(String idLokasi, String blok, String description, int jumlahLabel) {
        this.idLokasi = idLokasi;
        this.blok = blok;
        this.description = description;
        this.jumlahLabel = jumlahLabel;
    }

    public String getIdLokasi() {
        return idLokasi;
    }

    public String getBlok() {
        return blok;
    }

    /** Deskripsi lokasi dari MstLokasi.Description; fallback ke judul gabungan. */
    public String getDescription() {
        return (description == null || description.trim().isEmpty()) ? getTitle() : description.trim();
    }

    public int getJumlahLabel() {
        return jumlahLabel;
    }

    /**
     * Judul gabungan: Blok + IdLokasi, mis. Blok "A" + IdLokasi "7" -> "A7".
     * Bila IdLokasi sudah diawali kode Blok (mis. "A00"), Blok tidak digandakan.
     */
    public String getTitle() {
        String b = blok == null ? "" : blok.trim();
        String id = idLokasi == null ? "" : idLokasi.trim();
        if (b.isEmpty()) {
            return id;
        }
        if (id.isEmpty()) {
            return b;
        }
        if (id.toUpperCase().startsWith(b.toUpperCase())) {
            return id;
        }
        return b + id;
    }
}
