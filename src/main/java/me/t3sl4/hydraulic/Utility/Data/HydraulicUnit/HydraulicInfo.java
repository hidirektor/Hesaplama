package me.t3sl4.hydraulic.Utility.Data.HydraulicUnit;

public class HydraulicInfo {
    private int unitID;
    private String userName;
    private String siparisNumarasi;
    private String siparisTarihi;
    private String uniteTipi;
    private String pdfFile;
    private String excelFile;
    private String createdBy;

    public int getUnitID() {
        return unitID;
    }

    public void setUnitID(int unitID) {
        this.unitID = unitID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSiparisNumarasi() {
        return siparisNumarasi;
    }

    public void setSiparisNumarasi(String siparisNumarasi) {
        this.siparisNumarasi = siparisNumarasi;
    }

    public String getSiparisTarihi() {
        return siparisTarihi;
    }

    public void setSiparisTarihi(String siparisTarihi) {
        this.siparisTarihi = siparisTarihi;
    }

    public String getUniteTipi() {
        return uniteTipi;
    }

    public void setUniteTipi(String uniteTipi) {
        this.uniteTipi = uniteTipi;
    }

    public String getPdfFile() {
        return pdfFile;
    }

    public void setPdfFile(String pdfFile) {
        this.pdfFile = pdfFile;
    }

    public String getExcelFile() {
        return excelFile;
    }

    public void setExcelFile(String excelFile) {
        this.excelFile = excelFile;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
