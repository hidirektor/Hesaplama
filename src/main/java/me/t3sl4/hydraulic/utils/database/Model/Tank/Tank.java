package me.t3sl4.hydraulic.utils.database.Model.Tank;

public class Tank {

    public String tankName;
    public String kabinName;

    public String gecisOlculeri;
    public String kabinOlculeri;

    public int kabinHacim;

    public int gecisX;
    public int gecisY;
    public int gecisH;

    public int kabinX;
    public int kabinY;
    public int kabinH;

    public String malzemeKodu;
    public String malzemeAdi;

    public Tank(String tankName, String kabinName, int kabinHacim, int gecisX, int gecisY, int gecisH, int kabinX, int kabinY, int kabinH, String malzemeKodu, String malzemeAdi) {
        this.tankName = tankName;
        this.kabinName = kabinName;
        this.kabinHacim = kabinHacim;
        this.gecisX = gecisX;
        this.gecisY = gecisY;
        this.gecisH = gecisH;
        this.kabinX = kabinX;
        this.kabinY = kabinY;
        this.kabinH = kabinH;
        this.malzemeKodu = malzemeKodu;
        this.malzemeAdi = malzemeAdi;

        this.gecisOlculeri = gecisX + "x" + gecisY + "x" + gecisH;
        this.kabinOlculeri = kabinX + "x" + kabinY + "x" + kabinH;
    }

    public String getTankName() {
        return tankName;
    }

    public void setTankName(String tankName) {
        this.tankName = tankName;
    }

    public String getKabinName() {
        return kabinName;
    }

    public void setKabinName(String kabinName) {
        this.kabinName = kabinName;
    }

    public String getGecisOlculeri() {
        return gecisOlculeri;
    }

    public void setGecisOlculeri(String gecisOlculeri) {
        this.gecisOlculeri = gecisOlculeri;
    }

    public String getKabinOlculeri() {
        return kabinOlculeri;
    }

    public void setKabinOlculeri(String kabinOlculeri) {
        this.kabinOlculeri = kabinOlculeri;
    }

    public int getKabinHacim() {
        return kabinHacim;
    }

    public void setKabinHacim(int kabinHacim) {
        this.kabinHacim = kabinHacim;
    }

    public int getGecisX() {
        return gecisX;
    }

    public void setGecisX(int gecisX) {
        this.gecisX = gecisX;
    }

    public int getGecisY() {
        return gecisY;
    }

    public void setGecisY(int gecisY) {
        this.gecisY = gecisY;
    }

    public int getGecisH() {
        return gecisH;
    }

    public void setGecisH(int gecisH) {
        this.gecisH = gecisH;
    }

    public int getKabinX() {
        return kabinX;
    }

    public void setKabinX(int kabinX) {
        this.kabinX = kabinX;
    }

    public int getKabinY() {
        return kabinY;
    }

    public void setKabinY(int kabinY) {
        this.kabinY = kabinY;
    }

    public int getKabinH() {
        return kabinH;
    }

    public void setKabinH(int kabinH) {
        this.kabinH = kabinH;
    }

    public String getMalzemeKodu() {
        return malzemeKodu;
    }

    public void setMalzemeKodu(String malzemeKodu) {
        this.malzemeKodu = malzemeKodu;
    }

    public String getMalzemeAdi() {
        return malzemeAdi;
    }

    public void setMalzemeAdi(String malzemeAdi) {
        this.malzemeAdi = malzemeAdi;
    }
}
