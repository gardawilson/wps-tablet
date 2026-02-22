package com.example.myapplication.model;

public class MstMejaData {

    private String noMeja;
    private boolean isSLP;
    private boolean isGroup;
    private String type;
    private boolean enable;
    private Integer idGroupMesinSawmill;
    private String namaMeja;

    public MstMejaData(
            String noMeja,
            boolean isSLP,
            boolean isGroup,
            String type,
            boolean enable,
            Integer idGroupMesinSawmill,
            String namaMeja
    ) {
        this.noMeja = noMeja;
        this.isSLP = isSLP;
        this.isGroup = isGroup;
        this.type = type;
        this.enable = enable;
        this.idGroupMesinSawmill = idGroupMesinSawmill;
        this.namaMeja = namaMeja;
    }

    public String getNoMeja() { return noMeja; }
    public boolean isSLP() { return isSLP; }
    public boolean isGroup() { return isGroup; }
    public String getType() { return type; }
    public boolean isEnable() { return enable; }
    public Integer getIdGroupMesinSawmill() { return idGroupMesinSawmill; }
    public String getNamaMeja() { return namaMeja; }

    @Override
    public String toString() {
        return namaMeja;
    }
}