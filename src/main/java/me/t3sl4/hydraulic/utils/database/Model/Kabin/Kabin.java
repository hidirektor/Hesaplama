package me.t3sl4.hydraulic.utils.database.Model.Kabin;

public class Kabin {

    public String tankName;
    public String kabinName;

    public String gecisOlculeri;
    public String kabinOlculeri;

    public int kabinHacim;

    public int kabinX;
    public int kabinY;
    public int kabinH;

    public int tankX;
    public int tankY;
    public int tankH;

    public String kabinKodu;
    public String yagTankiKodu;
    public String malzemeAdi;

    public Kabin(String tankName, String kabinName, int kabinHacim, int kabinX, int kabinY, int kabinH, int tankX, int tankY, int tankH, String kabinKodu, String yagTankiKodu, String malzemeAdi) {
        this.tankName = tankName;
        this.kabinName = kabinName;
        this.kabinHacim = kabinHacim;
        this.kabinX = kabinX;
        this.kabinY = kabinY;
        this.kabinH = kabinH;
        this.tankX = tankX;
        this.tankY = tankY;
        this.tankH = tankH;
        this.kabinKodu = kabinKodu;
        this.yagTankiKodu = yagTankiKodu;
        this.malzemeAdi = malzemeAdi;

        this.gecisOlculeri = kabinX + "x" + kabinY + "x" + kabinH;
        this.kabinOlculeri = tankX + "x" + tankY + "x" + tankH;
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

    public int getKabinX() {
        return kabinX;
    }

    public void setKabinX(int kabinX) {
        this.kabinX = kabinX;
    }

    public int getKabinY() {
        return kabinY;
    }

    public void setKabinY(int gecisY) {
        this.kabinY = kabinY;
    }

    public int getKabinH() {
        return kabinH;
    }

    public void setKabinH(int kabinH) {
        this.kabinH = kabinH;
    }

    public int getTankX() {
        return tankX;
    }

    public void setTankX(int tankX) {
        this.tankX = tankX;
    }

    public int getTankY() {
        return tankY;
    }

    public void setTankY(int tankY) {
        this.tankY = tankY;
    }

    public int getTankH() {
        return tankH;
    }

    public void setTankH(int tankH) {
        this.tankH = tankH;
    }

    public String getKabinKodu() {
        return kabinKodu;
    }

    public void setKabinKodu(String kabinKodu) {
        this.kabinKodu = kabinKodu;
    }

    public String getYagTankiKodu() {
        return yagTankiKodu;
    }

    public void setYagTankiKodu(String yagTankiKodu) {
        this.yagTankiKodu = yagTankiKodu;
    }

    public String getMalzemeAdi() {
        return malzemeAdi;
    }

    public void setMalzemeAdi(String malzemeAdi) {
        this.malzemeAdi = malzemeAdi;
    }
}
