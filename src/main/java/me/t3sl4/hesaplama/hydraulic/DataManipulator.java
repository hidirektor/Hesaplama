package me.t3sl4.hesaplama.hydraulic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataManipulator {
    public int kampanaBoslukX;
    public int kampanaBoslukY;
    public int valfBoslukX;
    public int valfBoslukYArka;
    public int valfBoslukYOn;
    public int kilitliBlokAraBoslukX;
    public int tekHizAraBoslukX;
    public int ciftHizAraBoslukX;
    public int kompanzasyonTekHizAraBoslukX;
    public int sogutmaAraBoslukX;
    public int sogutmaAraBoslukYkOn;
    public int sogutmaAraBoslukYkArka;
    public int kilitMotorKampanaBosluk;
    public int kilitMotorMotorBoslukX; //hidros kilit motor kampana ile tank dış ölçüsü ara boşluğu
    public int kilitMotorBoslukYOn;
    public int kilitMotorBoslukYArka;
    public int kayipLitre;
    public ArrayList<Integer> kampanaDegerleri = new ArrayList<>();

    public HashMap<Object, int[]> kabinOlculeri = new HashMap<Object, int[]>();

    public List<String> motorYukseklikVerileri = new ArrayList<>();
    public DataManipulator() {
        //TODO
        //data update
    }
}
