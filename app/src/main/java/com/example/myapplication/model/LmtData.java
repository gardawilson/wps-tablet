package com.example.myapplication.model;

public class LmtData {
    // Fields yang sudah ada
    private String noLaminating;
    private String idJenisKayu;
    private String namaJenisKayu; // sama dengan 'jenis' dari query
    private String idGrade;
    private String namaGrade;
    private String idOrgTelly;
    private String namaOrgTelly;
    private String dateCreate;
    private String dateUsage;
    private String idUOMTblLebar;
    private String idUOMPanjang;
    private String noSPK;
    private String jam;
    private String isReject;
    private String idWarehouse;
    private String idFJProfile;
    private String idLokasi;
    private String isLembur;
    private String hasBeenPrinted;
    private String idFisik;
    private String noSPKAsal;
    private String remark;
    private String lastPrintDate;

    // Fields tambahan dari query baru
    private String noProduksi;
    private String idMesin;
    private String namaMesin;
    private String noBongkarSusun;
    private String profile;
    private String namaWarehouse;

    public LmtData(
            String noLaminating,
            String idJenisKayu,
            String namaJenisKayu,
            String idGrade,
            String namaGrade,
            String idOrgTelly,
            String namaOrgTelly,
            String dateCreate,
            String dateUsage,
            String idUOMTblLebar,
            String idUOMPanjang,
            String noSPK,
            String jam,
            String isReject,
            String idWarehouse,
            String idFJProfile,
            String idLokasi,
            String isLembur,
            String hasBeenPrinted,
            String idFisik,
            String noSPKAsal,
            String remark,
            String lastPrintDate,
            // Parameter tambahan
            String noProduksi,
            String idMesin,
            String namaMesin,
            String noBongkarSusun,
            String profile,
            String namaWarehouse
    ) {
        this.noLaminating = noLaminating;
        this.idJenisKayu = idJenisKayu;
        this.namaJenisKayu = namaJenisKayu;
        this.idGrade = idGrade;
        this.namaGrade = namaGrade;
        this.idOrgTelly = idOrgTelly;
        this.namaOrgTelly = namaOrgTelly;
        this.dateCreate = dateCreate;
        this.dateUsage = dateUsage;
        this.idUOMTblLebar = idUOMTblLebar;
        this.idUOMPanjang = idUOMPanjang;
        this.noSPK = noSPK;
        this.jam = jam;
        this.isReject = isReject;
        this.idWarehouse = idWarehouse;
        this.idFJProfile = idFJProfile;
        this.idLokasi = idLokasi;
        this.isLembur = isLembur;
        this.hasBeenPrinted = hasBeenPrinted;
        this.idFisik = idFisik;
        this.noSPKAsal = noSPKAsal;
        this.remark = remark;
        this.lastPrintDate = lastPrintDate;
        // Set nilai tambahan
        this.noProduksi = noProduksi;
        this.idMesin = idMesin;
        this.namaMesin = namaMesin;
        this.noBongkarSusun = noBongkarSusun;
        this.profile = profile;
        this.namaWarehouse = namaWarehouse;
    }

    // Getter methods yang sudah ada
    public String getNoLaminating() { return noLaminating; }
    public String getIdJenisKayu() { return idJenisKayu; }
    public String getNamaJenisKayu() { return namaJenisKayu; }
    public String getIdGrade() { return idGrade; }
    public String getNamaGrade() { return namaGrade; }
    public String getIdOrgTelly() { return idOrgTelly; }
    public String getNamaOrgTelly() { return namaOrgTelly; }
    public String getDateCreate() { return dateCreate; }
    public String getDateUsage() { return dateUsage; }
    public String getIdUOMTblLebar() { return idUOMTblLebar; }
    public String getIdUOMPanjang() { return idUOMPanjang; }
    public String getNoSPK() { return noSPK; }
    public String getJam() { return jam; }
    public String getIsReject() { return isReject; }
    public String getIdWarehouse() { return idWarehouse; }
    public String getIdFJProfile() { return idFJProfile; }
    public String getIdLokasi() { return idLokasi; }
    public String getIsLembur() { return isLembur; }
    public String getHasBeenPrinted() { return hasBeenPrinted; }
    public String getIdFisik() { return idFisik; }
    public String getNoSPKAsal() { return noSPKAsal; }
    public String getRemark() { return remark; }
    public String getLastPrintDate() { return lastPrintDate; }

    // Getter methods untuk field tambahan
    public String getNoProduksi() { return noProduksi; }
    public String getIdMesin() { return idMesin; }
    public String getNamaMesin() { return namaMesin; }
    public String getNoBongkarSusun() { return noBongkarSusun; }
    public String getProfile() { return profile; }
    public String getNamaWarehouse() { return namaWarehouse; }

    // Setter methods (opsional, jika diperlukan)
    public void setNoLaminating(String noLaminating) { this.noLaminating = noLaminating; }
    public void setIdJenisKayu(String idJenisKayu) { this.idJenisKayu = idJenisKayu; }
    public void setNamaJenisKayu(String namaJenisKayu) { this.namaJenisKayu = namaJenisKayu; }
    public void setIdGrade(String idGrade) { this.idGrade = idGrade; }
    public void setNamaGrade(String namaGrade) { this.namaGrade = namaGrade; }
    public void setIdOrgTelly(String idOrgTelly) { this.idOrgTelly = idOrgTelly; }
    public void setNamaOrgTelly(String namaOrgTelly) { this.namaOrgTelly = namaOrgTelly; }
    public void setDateCreate(String dateCreate) { this.dateCreate = dateCreate; }
    public void setDateUsage(String dateUsage) { this.dateUsage = dateUsage; }
    public void setIdUOMTblLebar(String idUOMTblLebar) { this.idUOMTblLebar = idUOMTblLebar; }
    public void setIdUOMPanjang(String idUOMPanjang) { this.idUOMPanjang = idUOMPanjang; }
    public void setNoSPK(String noSPK) { this.noSPK = noSPK; }
    public void setJam(String jam) { this.jam = jam; }
    public void setIsReject(String isReject) { this.isReject = isReject; }
    public void setIdWarehouse(String idWarehouse) { this.idWarehouse = idWarehouse; }
    public void setIdFJProfile(String idFJProfile) { this.idFJProfile = idFJProfile; }
    public void setIdLokasi(String idLokasi) { this.idLokasi = idLokasi; }
    public void setIsLembur(String isLembur) { this.isLembur = isLembur; }
    public void setHasBeenPrinted(String hasBeenPrinted) { this.hasBeenPrinted = hasBeenPrinted; }
    public void setIdFisik(String idFisik) { this.idFisik = idFisik; }
    public void setNoSPKAsal(String noSPKAsal) { this.noSPKAsal = noSPKAsal; }
    public void setRemark(String remark) { this.remark = remark; }
    public void setLastPrintDate(String lastPrintDate) { this.lastPrintDate = lastPrintDate; }
    public void setNoProduksi(String noProduksi) { this.noProduksi = noProduksi; }
    public void setIdMesin(String idMesin) { this.idMesin = idMesin; }
    public void setNamaMesin(String namaMesin) { this.namaMesin = namaMesin; }
    public void setNoBongkarSusun(String noBongkarSusun) { this.noBongkarSusun = noBongkarSusun; }
    public void setProfile(String profile) { this.profile = profile; }
    public void setNamaWarehouse(String namaWarehouse) { this.namaWarehouse = namaWarehouse; }
}