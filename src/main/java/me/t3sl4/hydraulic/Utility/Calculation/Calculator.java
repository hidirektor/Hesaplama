package me.t3sl4.hydraulic.Utility.Calculation;

import me.t3sl4.hydraulic.Utility.File.ExcelUtil;

public class Calculator {

    private static final int STANDARD_X = 1000;
    private static final int STANDARD_Y = 600;
    private static final int STANDARD_H = 350;
    private static final int MIN_Y = 350;
    private static final int MIN_X = 550;
    private static final int DEFAULT_H = 350;
    private static final String COOLING_PRESENT = "Var";
    private static final String COOLING_ABSENT = "Yok";
    private static final String SINGLE_SPEED = "İnişte Tek Hız";
    private static final String DOUBLE_SPEED = "İnişte Çift Hız";
    private static final String LOCKED_BLOCK = "Kilitli Blok || Çift Hız";
    private static final String COMPENSATION_SINGLE_SPEED = "Kompanzasyon + İnişte Tek Hız";

    //public int secilenMotorIndex;
    //public int secilenKampanaDegeri;

    //public int secilenKampanaVal;
    //public int secilenPompaVal;

    private static final int kampanaBoslukX = ExcelUtil.dataManipulator.kampanaBoslukX;
    private static final int kampanaBoslukY = ExcelUtil.dataManipulator.kampanaBoslukY;
    private static final int kilitAraBoslukX = ExcelUtil.dataManipulator.kilitliBlokAraBoslukX;
    private static final int valfBoslukX = ExcelUtil.dataManipulator.valfBoslukX;
    private static final int valfBoslukYArka = ExcelUtil.dataManipulator.valfBoslukYArka;
    private static final int getValfBoslukYOn = ExcelUtil.dataManipulator.valfBoslukYOn;

    //public static String atananKabinFinal = "";
    //public static String gecisOlculeriFinal = "";

    /*public Calculator(int secilenMotorIndex, int secilenKampanaDegeri, int secilenKampanaVal, int secilenPompaVal) {
        this.secilenMotorIndex = secilenMotorIndex;
        this.secilenKampanaDegeri = secilenKampanaDegeri;
        this.secilenKampanaVal = secilenKampanaVal;
        this.secilenPompaVal = secilenPompaVal;

    }*/


}