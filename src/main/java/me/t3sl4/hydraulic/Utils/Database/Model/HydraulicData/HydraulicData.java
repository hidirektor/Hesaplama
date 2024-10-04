package me.t3sl4.hydraulic.Utils.Database.Model.HydraulicData;

import me.t3sl4.hydraulic.Utils.Database.Model.Tank.Tank;

import java.util.*;

public class HydraulicData {
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

    //TODO
    //Yeni sistem değişkenleri

    public ArrayList<String> uniteTipiDegerleri = new ArrayList<>(); //Jsondan gelen değerler
    public List<Tank> inputTanks = new ArrayList<>(); //Jsondan gelen tank verileri

    /*
    Classic Side
     */
    public Map<String, LinkedList<String>> motorMap = new HashMap<>();
    public Map<String, String> motorKampanaMap = new HashMap<>();
    public Map<String, String> motorYukseklikMap = new HashMap<>();

    public Map<String, LinkedList<String>> coolingMap = new HashMap<>();
    public Map<String, LinkedList<String>> hydraulicLockMap = new HashMap<>();
    public Map<String, LinkedList<String>> pumpMap = new HashMap<>();
    public Map<String, LinkedList<String>> compensationMap = new HashMap<>();
    public Map<String, LinkedList<String>> valveTypeMap = new HashMap<>();
    public Map<String, LinkedList<String>> lockMotorMap = new HashMap<>();
    public Map<String, LinkedList<String>> lockPumpMap = new HashMap<>();

    /*
    Power Pack Side
     */
    public Map<String, LinkedList<String>> motorVoltajMap = new HashMap<>();
    public Map<String, LinkedList<String>> uniteTipiMap = new HashMap<>();
    public Map<String, LinkedList<String>> motorGucuMap = new HashMap<>();
    public Map<String, LinkedList<String>> pompaPowerPackMap = new HashMap<>();
    public Map<String, LinkedList<String>> tankTipiMap = new HashMap<>();
    public Map<String, LinkedList<String>> tankKapasitesiMap = new HashMap<>();
    public Map<String, LinkedList<String>> platformTipiMap = new HashMap<>();
    public Map<String, LinkedList<String>> valfTipiMap = new HashMap<>();

    /*
    Parça Listesi
     */

    /*
    Classic Parça Listesi
     */
    public Map<String, LinkedList<String>> classicParcaMotor = new HashMap<>();
    public Map<String, LinkedList<String>> classicParcaKampana = new HashMap<>();
    public Map<String, LinkedList<String>> classicParcaPompa = new HashMap<>();
    public Map<String, LinkedList<String>> classicParcaKaplin = new HashMap<>();
    public Map<String, LinkedList<String>> classicParcaValfBloklari = new HashMap<>();
    public Map<String, LinkedList<String>> classicParcaSogutma = new HashMap<>();
    public Map<String, LinkedList<String>> classicParcaBasincSalteri = new HashMap<>();
    public Map<String, LinkedList<String>> classicParcaDefault = new HashMap<>();

    /*
    PowerPack Parça Listesi
     */


    //Bitişi

    //PowerPack - Hidros Parça Listesi:
    public HashMap<String, HashMap<String, String>> hidros380Parca = new HashMap<>();
    public HashMap<String, HashMap<String, String>> hidros220Parca = new HashMap<>();
    public HashMap<String, HashMap<String, String>> hidrosPompaParca = new HashMap<>();
    public HashMap<String, HashMap<String, String>> hidrosPompaCivataParca = new HashMap<>();
    public HashMap<String, HashMap<String, String>> hidrosDikeyTankParca = new HashMap<>();
    public HashMap<String, HashMap<String, String>> hidrosYatayTankParca = new HashMap<>();
    public HashMap<String, HashMap<String, String>> hidrosDikeyCiftHizParca = new HashMap<>();
    public HashMap<String, HashMap<String, String>> hidrosDikeyTekHizParca = new HashMap<>();
    public HashMap<String, HashMap<String, String>> hidrosYatayCiftHizParca = new HashMap<>();
    public HashMap<String, HashMap<String, String>> hidrosYatayTekHizParca = new HashMap<>();
    public HashMap<String, HashMap<String, String>> hidrosDikeyCiftHizParcaESP = new HashMap<>();
    public HashMap<String, HashMap<String, String>> hidrosDikeyTekHizParcaESP = new HashMap<>();
    public HashMap<String, HashMap<String, String>> hidrosYatayCiftHizParcaESP = new HashMap<>();
    public HashMap<String, HashMap<String, String>> hidrosYatayTekHizParcaESP = new HashMap<>();
    public HashMap<String, HashMap<String, String>> hidrosDevirmeliParca = new HashMap<>();
    public HashMap<String, HashMap<String, String>> hidrosGenelParca = new HashMap<>();
    public HashMap<String, HashMap<String, String>> hidrosTamParca = new HashMap<>();
    public HashMap<String, HashMap<String, String>> hidrosTamParcaYatay = new HashMap<>();
    public HashMap<String, HashMap<String, String>> hidrosTamParcaDikey = new HashMap<>();
    public HashMap<String, HashMap<String, String>> hidrosTamParcaESPHaric = new HashMap<>();
    public HashMap<String, HashMap<String, String>> hidrosTamParcaOzelTekValf = new HashMap<>();

    //PowerPack - İthal Parçalar
    public HashMap<String, HashMap<String, String>> hidrosIthalParcaMotor380 = new HashMap<>();
    public HashMap<String, HashMap<String, String>> hidrosIthalParcaMotor220 = new HashMap<>();
    public HashMap<String, HashMap<String, String>> hidrosIthalParcaPompa = new HashMap<>();
    public HashMap<String, HashMap<String, String>> hidrosIthalParcaPompaCivata = new HashMap<>();
    public HashMap<String, HashMap<String, String>> hidrosIthalParcaTankDikey = new HashMap<>();
    public HashMap<String, HashMap<String, String>> hidrosIthalParcaTankYatay = new HashMap<>();

    public HydraulicData() {
        //TODO
        //data update
    }
}