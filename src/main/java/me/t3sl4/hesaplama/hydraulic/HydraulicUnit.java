package me.t3sl4.hesaplama.hydraulic;

import java.util.ArrayList;
import java.util.List;

public class HydraulicUnit {
    private List<String> motorVerileri = new ArrayList<>();
    private List<String> kampanaVerileri = new ArrayList<>();
    private List<String> pompaVerileri = new ArrayList<>();
    private List<String> tankKapasitesiVerileri = new ArrayList<>();
    private List<String> valfVerileri = new ArrayList<>();
    private List<String> hidrolikKilitStatsVerileri = new ArrayList<>();
    private List<String> sogutmaStatsVerileri = new ArrayList<>();

    public HydraulicUnit(String motorVerisi, String kampanaVerisi, String pompaVerisi, String tankKapasitesi, String valfVerisi, String hidrolikKilitStatVerisi, String sogutmaStatVerisi) {
        this.motorVerileri.add(motorVerisi);
        this.kampanaVerileri.add(kampanaVerisi);
        this.pompaVerileri.add(pompaVerisi);
        this.tankKapasitesiVerileri.add(tankKapasitesi);
        this.valfVerileri.add(valfVerisi);
        this.hidrolikKilitStatsVerileri.add(hidrolikKilitStatVerisi);
        this.sogutmaStatsVerileri.add(sogutmaStatVerisi);
    }

    private void List2Org() {
        //TODO
        //Listteki verileri original formatlarına dönüştür.
    }

    private void Excel2List() {
        //TODO
        //Yüklenen excele göre verileri List'e aktar.
    }

    public List<String> getMotorVerileri() {
        return motorVerileri;
    }

    public void setMotorVerileri(List<String> motorVerileri) {
        this.motorVerileri = motorVerileri;
    }

    public List<String> getKampanaVerileri() {
        return kampanaVerileri;
    }

    public void setKampanaVerileri(List<String> kampanaVerileri) {
        this.kampanaVerileri = kampanaVerileri;
    }

    public List<String> getPompaVerileri() {
        return pompaVerileri;
    }

    public void setPompaVerileri(List<String> pompaVerileri) {
        this.pompaVerileri = pompaVerileri;
    }

    public List<String> getTankKapasitesiVerileri() {
        return tankKapasitesiVerileri;
    }

    public void setTankKapasitesiVerileri(List<String> tankKapasitesiVerileri) {
        this.tankKapasitesiVerileri = tankKapasitesiVerileri;
    }

    public List<String> getValfVerileri() {
        return valfVerileri;
    }

    public void setValfVerileri(List<String> valfVerileri) {
        this.valfVerileri = valfVerileri;
    }

    public List<String> getHidrolikKilitStatsVerileri() {
        return hidrolikKilitStatsVerileri;
    }

    public void setHidrolikKilitStatsVerileri(List<String> hidrolikKilitStatsVerileri) {
        this.hidrolikKilitStatsVerileri = hidrolikKilitStatsVerileri;
    }

    public List<String> getSogutmaStatsVerileri() {
        return sogutmaStatsVerileri;
    }

    public void setSogutmaStatsVerileri(List<String> sogutmaStatsVerileri) {
        this.sogutmaStatsVerileri = sogutmaStatsVerileri;
    }
}
