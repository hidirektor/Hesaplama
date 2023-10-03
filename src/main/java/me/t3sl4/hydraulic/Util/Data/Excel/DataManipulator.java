package me.t3sl4.hydraulic.Util.Data.Excel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class DataManipulator {
    public int kampanaBoslukX;
    public int kilitPlatformMotorBosluk;
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
    public int valfXBoslukSogutma;
    public ArrayList<Integer> kampanaDegerleri = new ArrayList<>();

    public LinkedHashMap<Object, int[]> kabinOlculeri = new LinkedHashMap<Object, int[]>();

    public ArrayList<String> motorYukseklikVerileri = new ArrayList<>();

    public ArrayList<String> motorDegerleri = new ArrayList<>();

    public ArrayList<String> uniteTipiDegerleri = new ArrayList<>();
    public ArrayList<String> pompaDegerleriHidros = new ArrayList<>();
    public ArrayList<String> pompaDegerleriKlasik = new ArrayList<>();
    public ArrayList<String> pompaDegerleriTumu = new ArrayList<>();
    public ArrayList<String> kilitMotorDegerleri = new ArrayList<>();
    public ArrayList<String> kilitPompaDegerleri = new ArrayList<>();

    public ArrayList<String> valfTipiDegerleri1 = new ArrayList<>();
    public ArrayList<String> valfTipiDegerleri2 = new ArrayList<>();


    //Parça Listesi için değişkenler:
    public ArrayList<String> parcaListesiKampana250 = new ArrayList<>();
    public ArrayList<String> parcaListesiKampana300 = new ArrayList<>();
    public ArrayList<String> parcaListesiKampana350 = new ArrayList<>();
    public ArrayList<String> parcaListesiKampana400 = new ArrayList<>();

    public ArrayList<String> parcaListesiPompa95 = new ArrayList<>();
    public ArrayList<String> parcaListesiPompa119 = new ArrayList<>();
    public ArrayList<String> parcaListesiPompa14 = new ArrayList<>();
    public ArrayList<String> parcaListesiPompa146 = new ArrayList<>();
    public ArrayList<String> parcaListesiPompa168 = new ArrayList<>();
    public ArrayList<String> parcaListesiPompa192 = new ArrayList<>();
    public ArrayList<String> parcaListesiPompa229 = new ArrayList<>();
    public ArrayList<String> parcaListesiPompa281 = new ArrayList<>();
    public ArrayList<String> parcaListesiPompa288 = new ArrayList<>();
    public ArrayList<String> parcaListesiPompa333 = new ArrayList<>();
    public ArrayList<String> parcaListesiPompa379 = new ArrayList<>();
    public ArrayList<String> parcaListesiPompa426 = new ArrayList<>();
    public ArrayList<String> parcaListesiPompa455 = new ArrayList<>();
    public ArrayList<String> parcaListesiPompa494 = new ArrayList<>();
    public ArrayList<String> parcaListesiPompa561 = new ArrayList<>();

    public ArrayList<String> parcaListesiMotor4 = new ArrayList<>();
    public ArrayList<String> parcaListesiMotor55 = new ArrayList<>();
    public ArrayList<String> parcaListesiMotor55Kompakt = new ArrayList<>();
    public ArrayList<String> parcaListesiMotor75Kompakt = new ArrayList<>();
    public ArrayList<String> parcaListesiMotor11 = new ArrayList<>();
    public ArrayList<String> parcaListesiMotor11Kompakt = new ArrayList<>();
    public ArrayList<String> parcaListesiMotor15 = new ArrayList<>();
    public ArrayList<String> parcaListesiMotor185 = new ArrayList<>();
    public ArrayList<String> parcaListesiMotor22 = new ArrayList<>();
    public ArrayList<String> parcaListesiMotor37 = new ArrayList<>();

    public ArrayList<String> parcaListesiKaplin1PN28 = new ArrayList<>();
    public ArrayList<String> parcaListesiKaplin1PN38 = new ArrayList<>();
    public ArrayList<String> parcaListesiKaplin1PN42 = new ArrayList<>();
    public ArrayList<String> parcaListesiKaplin2PN28 = new ArrayList<>();
    public ArrayList<String> parcaListesiKaplin2PN38 = new ArrayList<>();
    public ArrayList<String> parcaListesiKaplin2PN42 = new ArrayList<>();

    public ArrayList<String> parcaListesiValfBloklariTekHiz = new ArrayList<>();
    public ArrayList<String> parcaListesiValfBloklariCiftHiz = new ArrayList<>();
    public ArrayList<String> parcaListesiValfBloklariKilitliBlok = new ArrayList<>();
    public ArrayList<String> parcaListesiValfBloklariKompanzasyon = new ArrayList<>();

    public ArrayList<String> parcaListesiBasincSalteri = new ArrayList<>();

    public ArrayList<String> parcaListesiStandart = new ArrayList<>();

    //Hidros kısmı için geçerli olanlar:
    public ArrayList<String> motorDegerleriHidros380 = new ArrayList<>();
    public ArrayList<String> motorDegerleriHidros220 = new ArrayList<>();
    public ArrayList<String> pompaKapasiteDegerleriHidros = new ArrayList<>();
    public ArrayList<String> tankKapasitesiDegerleriHidrosDikey = new ArrayList<>();
    public ArrayList<String> tankKapasitesiDegerleriHidrosYatay = new ArrayList<>();
    public ArrayList<String> platformDegerleriHidros = new ArrayList<>();
    public ArrayList<String> valfDegerleriHidros = new ArrayList<>();
    public DataManipulator() {
        //TODO
        //data update
    }
}