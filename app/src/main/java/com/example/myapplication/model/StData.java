package com.example.myapplication.model;

public class StData {
    private String noST;
    private String noKayuBulat;
    private int idJenisKayu;
    private String jenis;
    private String idStickBy;
    private String namaStickBy;
    private String noSPK;
    private String dateCreate;
    private String idOrgTelly;
    private String namaOrgTelly;
    private String remark;
    private String noPenerimaanSTPembelian;
    private String noPenerimaanSTUpah;
    private int isSLP;
    private String vacuumDate;
    private String noBongkarSusun;
    private int idUOMTblLbr;
    private int idUOMPanjang;

    // Constructor
    public StData(String noST, String noKayuBulat, int idJenisKayu, String jenis,
                                String idStickBy, String namaStickBy, String noSPK,
                                String dateCreate, String idOrgTelly, String namaOrgTelly,
                                String remark, String noPenerimaanSTPembelian,
                                String noPenerimaanSTUpah, int isSLP, String vacuumDate,
                                String noBongkarSusun, int idUOMTblLbr, int idUOMPanjang) {
        this.noST = noST != null ? noST : "-";
        this.noKayuBulat = noKayuBulat != null ? noKayuBulat : "-";
        this.idJenisKayu = idJenisKayu;
        this.jenis = jenis != null ? jenis : "-";
        this.idStickBy = idStickBy != null ? idStickBy : "-";
        this.namaStickBy = namaStickBy != null ? namaStickBy : "-";
        this.noSPK = noSPK != null ? noSPK : "-";
        this.dateCreate = dateCreate != null ? dateCreate : "-";
        this.idOrgTelly = idOrgTelly != null ? idOrgTelly : "-";
        this.namaOrgTelly = namaOrgTelly != null ? namaOrgTelly : "-";
        this.remark = remark != null ? remark : "-";
        this.noPenerimaanSTPembelian = noPenerimaanSTPembelian != null ? noPenerimaanSTPembelian : "-";
        this.noPenerimaanSTUpah = noPenerimaanSTUpah != null ? noPenerimaanSTUpah : "-";
        this.isSLP = isSLP;
        this.vacuumDate = vacuumDate != null ? vacuumDate : "-";
        this.noBongkarSusun = noBongkarSusun != null ? noBongkarSusun : "-";
        this.idUOMTblLbr = idUOMTblLbr;
        this.idUOMPanjang = idUOMPanjang;
    }

    // Getters
    public String getNoST() { return noST; }
    public String getNoKayuBulat() { return noKayuBulat; }
    public int getIdJenisKayu() { return idJenisKayu; }
    public String getJenis() { return jenis; }
    public String getIdStickBy() { return idStickBy; }
    public String getNamaStickBy() { return namaStickBy; }
    public String getNoSPK() { return noSPK; }
    public String getDateCreate() { return dateCreate; }
    public String getIdOrgTelly() { return idOrgTelly; }
    public String getNamaOrgTelly() { return namaOrgTelly; }
    public String getRemark() { return remark; }
    public String getNoPenerimaanSTPembelian() { return noPenerimaanSTPembelian; }
    public String getNoPenerimaanSTUpah() { return noPenerimaanSTUpah; }
    public int getIsSLP() { return isSLP; }
    public String getVacuumDate() { return vacuumDate; }
    public String getNoBongkarSusun() { return noBongkarSusun; }
    public int getIdUOMTblLbr() { return idUOMTblLbr; }
    public int getIdUOMPanjang() { return idUOMPanjang; }

    // Setters (jika diperlukan)
    public void setNoST(String noST) { this.noST = noST; }
    public void setNoKayuBulat(String noKayuBulat) { this.noKayuBulat = noKayuBulat; }
    public void setIdJenisKayu(int idJenisKayu) { this.idJenisKayu = idJenisKayu; }
    public void setJenis(String jenis) { this.jenis = jenis; }
    public void setIdStickBy(String idStickBy) { this.idStickBy = idStickBy; }
    public void setNamaStickBy(String namaStickBy) { this.namaStickBy = namaStickBy; }
    public void setNoSPK(String noSPK) { this.noSPK = noSPK; }
    public void setDateCreate(String dateCreate) { this.dateCreate = dateCreate; }
    public void setIdOrgTelly(String idOrgTelly) { this.idOrgTelly = idOrgTelly; }
    public void setNamaOrgTelly(String namaOrgTelly) { this.namaOrgTelly = namaOrgTelly; }
    public void setRemark(String remark) { this.remark = remark; }
    public void setNoPenerimaanSTPembelian(String noPenerimaanSTPembelian) { this.noPenerimaanSTPembelian = noPenerimaanSTPembelian; }
    public void setNoPenerimaanSTUpah(String noPenerimaanSTUpah) { this.noPenerimaanSTUpah = noPenerimaanSTUpah; }
    public void setIsSLP(int isSLP) { this.isSLP = isSLP; }
    public void setVacuumDate(String vacuumDate) { this.vacuumDate = vacuumDate; }
    public void setNoBongkarSusun(String noBongkarSusun) { this.noBongkarSusun = noBongkarSusun; }
    public void setIdUOMTblLbr(int idUOMTblLbr) { this.idUOMTblLbr = idUOMTblLbr; }
    public void setIdUOMPanjang(int idUOMPanjang) { this.idUOMPanjang = idUOMPanjang; }
}