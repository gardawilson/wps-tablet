package com.example.myapplication.model;

public class SawmillData {
    private String noSTSawmill;
    private String shift;
    private String tglSawmill;
    private String noKayuBulat;
    private String noMeja;
    private String operator;
    private int idSawmillSpecialCondition;
    private String balokTerpakai;
    private String jamKerja;
    private int jlhBatangRajang;
    private String hourMeter;
    private String remark;
    private String namaJenisKayu;
    private int stokTersedia;
    private double beratBalokTim;
    private double beratBalok;
    private String hourStart;
    private String hourEnd;
    private Integer idOperator1;
    private Integer idOperator2;
    private String namaMeja;


    public SawmillData(String noSTSawmill, String shift, String tglSawmill, String noKayuBulat, String noMeja, String operator,
                       int idSawmillSpecialCondition, String balokTerpakai, String jamKerja, int jlhBatangRajang,
                       String hourMeter, String remark, String namaJenisKayu, int stokTersedia, double beratBalokTim, double beratBalok,
                       String hourStart, String hourEnd, Integer idOperator1, Integer idOperator2, String namaMeja) {
        this.noSTSawmill = noSTSawmill;
        this.shift = shift;
        this.tglSawmill = tglSawmill;
        this.noKayuBulat = noKayuBulat;
        this.noMeja = noMeja;
        this.operator = operator;
        this.idSawmillSpecialCondition = idSawmillSpecialCondition;
        this.balokTerpakai = balokTerpakai;
        this.jamKerja = jamKerja;
        this.jlhBatangRajang = jlhBatangRajang;
        this.hourMeter = hourMeter;
        this.remark = remark;
        this.namaJenisKayu = namaJenisKayu;
        this.stokTersedia = stokTersedia;
        this.beratBalokTim = beratBalokTim;
        this.beratBalok = beratBalok;
        this.hourStart = hourStart;
        this.hourEnd = hourEnd;
        this.idOperator1 = idOperator1;
        this.idOperator2 = idOperator2;
        this.namaMeja = namaMeja;
    }

    // Getter dan Setter lengkap
    public String getNoSTSawmill() { return noSTSawmill; }
    public void setNoSTSawmill(String noSTSawmill) { this.noSTSawmill = noSTSawmill; }

    public String getShift() { return shift; }
    public void setShift(String shift) { this.shift = shift; }

    public String getTglSawmill() { return tglSawmill; }
    public void setTglSawmill(String tglSawmill) { this.tglSawmill = tglSawmill; }

    public String getNoKayuBulat() { return noKayuBulat; }
    public void setNoKayuBulat(String noKayuBulat) { this.noKayuBulat = noKayuBulat; }

    public String getNoMeja() { return noMeja; }
    public void setNoMeja(String noMeja) { this.noMeja = noMeja; }

    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }

    public int getIdSawmillSpecialCondition() { return idSawmillSpecialCondition; }
    public void setIdSawmillSpecialCondition(int idSawmillSpecialCondition) { this.idSawmillSpecialCondition = idSawmillSpecialCondition; }

    public String getBalokTerpakai() { return balokTerpakai; }
    public void setBalokTerpakai(String balokTerpakai) { this.balokTerpakai = balokTerpakai; }

    public String getJamKerja() { return jamKerja; }
    public void setJamKerja(String jamKerja) { this.jamKerja = jamKerja; }

    public int getJlhBatangRajang() { return jlhBatangRajang; }
    public void setJlhBatangRajang(int jlhBatangRajang) { this.jlhBatangRajang = jlhBatangRajang; }

    public String getHourMeter() { return hourMeter; }
    public void setHourMeter(String hourMeter) { this.hourMeter = hourMeter; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public String getNamaJenisKayu() { return namaJenisKayu; }
    public void setNamaJenisKayu(String namaJenisKayu) { this.namaJenisKayu = namaJenisKayu; }

    public int getStokTersedia() { return stokTersedia; }

    public double getBeratBalokTim() {
        return beratBalokTim;
    }
    public void setBeratBalokTim(double beratBalokTim) {
        this.beratBalokTim = beratBalokTim;
    }

    public double getBeratBalok() {
        return beratBalok;
    }
    public void setBeratBalok(double beratBalok) {
        this.beratBalok = beratBalok;
    }

    public String getHourStart() { return hourStart; }
    public void setHourStart(String hourStart) { this.hourStart = hourStart; }

    public String getHourEnd() { return hourEnd; }
    public void setHourEnd(String hourEnd) { this.hourEnd = hourEnd; }

    public Integer getIdOperator1() { return idOperator1; }
    public void setIdOperator1(Integer idOperator1) { this.idOperator1 = idOperator1; }

    public Integer getIdOperator2() { return idOperator2; }
    public void setIdOperator2(Integer idOperator2) { this.idOperator2 = idOperator2; }

    public String getNamaMeja() { return namaMeja; }
    public void setNamaMeja(String namaMeja) { this.namaMeja = namaMeja; }

}
