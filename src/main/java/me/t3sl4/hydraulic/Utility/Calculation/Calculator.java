package me.t3sl4.hydraulic.Utility.Calculation;

import javafx.scene.text.Text;
import me.t3sl4.hydraulic.Utility.File.ExcelUtil;
import me.t3sl4.hydraulic.Utility.Utils;

import java.util.ArrayList;
import java.util.Objects;

public class Calculator {

    private static final int STANDARD_X = 1000;
    private static final int STANDARD_Y = 600;
    private static final int STANDARD_H = 350;
    private static final int MIN_Y = 350;
    private static final int MIN_X = 550;
    private static final int DEFAULT_H = 300;
    private static final String COOLING_PRESENT = "Var";
    private static final String COOLING_ABSENT = "Yok";
    private static final String SINGLE_SPEED = "İnişte Tek Hız";
    private static final String DOUBLE_SPEED = "İnişte Çift Hız";
    private static final String LOCKED_BLOCK = "Kilitli Blok || Çift Hız";
    private static final String COMPENSATION_SINGLE_SPEED = "Kompanzasyon + İnişte Tek Hız";

    public static ArrayList<Integer> calcDimensions(int x, int y, int secilenKampana, int secilenMotor, String sogutmaDurumu,
                                                    String hidrolikKilitDurumu, String valfTipi,
                                                    double secilenPompaVal, String secilenKilitMotor,
                                                    int girilenTankKapasitesiMiktari, Text sonucText) {

        String atananKabin = "", gecisOlculeri = "";

        ArrayList<Integer> finalValues = new ArrayList<>();
        int yV = 0;
        int yK = 0;
        int h = DEFAULT_H;

        if (COOLING_PRESENT.equals(sogutmaDurumu)) {
            handleCooling(x, y, h, finalValues);
            atananKabin = "KD SOĞUTMA";
            gecisOlculeri = "1000x600x350";
        } else {
            x += secilenKampana + ExcelUtil.dataManipulator.kampanaBoslukX;
            yK += secilenKampana + 2 * ExcelUtil.dataManipulator.kampanaBoslukY;

            System.out.println("Motor + Kampana için:");
            logValues(secilenKampana, ExcelUtil.dataManipulator.kampanaBoslukX, ExcelUtil.dataManipulator.kampanaBoslukY);

            yV = handleValveConfiguration(x, yV, hidrolikKilitDurumu, valfTipi, secilenPompaVal, secilenKilitMotor);
            y = Math.max(yV, yK);
            x = Math.max(x, MIN_X);
            y = Math.max(y, MIN_Y);

            h = calculateHeight(h, secilenMotor);
            int hesaplananHacim = calculateVolume(x, y, h);

            int[] bestDimensions = findBestDimensions(hesaplananHacim, girilenTankKapasitesiMiktari, x, y);
            if (bestDimensions != null) {
                x = bestDimensions[0];
                y = bestDimensions[1];
                h = bestDimensions[2];
            }

            finalValues.add(x);
            finalValues.add(y);
            finalValues.add(h);
            finalValues.add(hesaplananHacim);

            String atananHT = Objects.requireNonNull(Utils.getKeyByValue(ExcelUtil.dataManipulator.kabinOlculeri, bestDimensions)).toString();
            if(Objects.equals(atananHT, "HT 40")) {
                atananKabin = "KD 40";
                gecisOlculeri = "580x460x780";
            } else if(Objects.equals(atananHT, "HT 70")) {
                atananKabin = "KD 70";
                gecisOlculeri = "640x520x950";
            } else if(Objects.equals(atananHT, "HT 100")) {
                atananKabin = "KD 70";
                gecisOlculeri = "640x520x950";
            } else if(Objects.equals(atananHT, "HT 125")) {
                atananKabin = "KD 125";
                gecisOlculeri = "720x550x1000";
            } else if(Objects.equals(atananHT, "HT 160")) {
                atananKabin = "KD 1620";
                gecisOlculeri = "900x800x1100";
            } else if(Objects.equals(atananHT, "HT 200")) {
                atananKabin = "KD 1620";
                gecisOlculeri = "900x800x1100";
            } else if(Objects.equals(atananHT, "HT 250")) {
                atananKabin = "KD 2530";
                gecisOlculeri = "1100x900x1150";
            } else if(Objects.equals(atananHT, "HT 300")) {
                atananKabin = "KD 2530";
                gecisOlculeri = "1100x900x1150";
            } else if(Objects.equals(atananHT, "HT 350")) {
                atananKabin = "KD 3540";
                gecisOlculeri = "1100x900x1250";
            } else if(Objects.equals(atananHT, "HT 400")) {
                atananKabin = "KD 3540";
                gecisOlculeri = "1100x900x1250";
            }
        }

        printCalculationSummary(x, y, h, yV, yK, finalValues.get(0), finalValues.get(1), finalValues.get(2), finalValues.get(3), atananKabin, gecisOlculeri);
        sonucText.setText("Kullanmanız Gereken Kabin: \n\t\t\t\t\t\t" + atananKabin + "\n\t\t\tGeçiş Ölçüleri: " + gecisOlculeri + " (x, y, h)");

        return finalValues;
    }

    private static void handleCooling(int x, int y, int h, ArrayList<Integer> finalValues) {
        x = STANDARD_X;
        y = STANDARD_Y;
        h = STANDARD_H;
        double hesaplananHacim = ((x * h * y) / 1000000) - ExcelUtil.dataManipulator.kayipLitre;

        finalValues.add(x);
        finalValues.add(y);
        finalValues.add(h);
        finalValues.add((int) hesaplananHacim);
    }

    private static void logValues(int secilenKampana, int kampanaBoslukX, int kampanaBoslukY) {
        System.out.println("X += " + secilenKampana + " (Kampana) + " + kampanaBoslukX + " (Kampana Boşluk)");
        System.out.println("yK += " + secilenKampana + " (Kampana) + " + kampanaBoslukY + " (Kampana Boşluk) + " + kampanaBoslukY + " (Kampana Boşluk)");
    }

    private static int handleValveConfiguration(int x, int yV, String hidrolikKilitDurumu, String valfTipi, double secilenPompaVal, String secilenKilitMotor) {
        if (COOLING_PRESENT.equals(hidrolikKilitDurumu) && LOCKED_BLOCK.equals(valfTipi)) {
            x += 120 + ExcelUtil.dataManipulator.kilitliBlokAraBoslukX + ExcelUtil.dataManipulator.valfBoslukX;
            yV += 190 + ExcelUtil.dataManipulator.valfBoslukYArka + ExcelUtil.dataManipulator.valfBoslukYOn;
        } else if (COOLING_ABSENT.equals(hidrolikKilitDurumu)) {
            if (SINGLE_SPEED.equals(valfTipi)) {
                x += 70 + ExcelUtil.dataManipulator.valfBoslukX + ExcelUtil.dataManipulator.tekHizAraBoslukX;
                yV += 180 + ExcelUtil.dataManipulator.valfBoslukYOn + ExcelUtil.dataManipulator.valfBoslukYArka;
            } else if (DOUBLE_SPEED.equals(valfTipi)) {
                x += 140 + ExcelUtil.dataManipulator.ciftHizAraBoslukX + ExcelUtil.dataManipulator.valfBoslukX;
                yV += 90 + ExcelUtil.dataManipulator.valfBoslukYOn + ExcelUtil.dataManipulator.valfBoslukYArka;
            } else if (COMPENSATION_SINGLE_SPEED.equals(valfTipi)) {
                x += 140 + ExcelUtil.dataManipulator.kompanzasyonTekHizAraBoslukX;
                yV += 180 + ExcelUtil.dataManipulator.valfBoslukYOn + ExcelUtil.dataManipulator.valfBoslukYArka;
            }
        }

        if (secilenPompaVal >= 28.1 && secilenKilitMotor != null) {
            x += 200 + ExcelUtil.dataManipulator.kilitMotorKampanaBosluk + ExcelUtil.dataManipulator.kilitMotorMotorBoslukX;
            yV += 200 + ExcelUtil.dataManipulator.kilitMotorBoslukYOn + ExcelUtil.dataManipulator.kilitMotorBoslukYArka;
        }

        return yV;
    }

    private static int calculateHeight(int h, int secilenMotor) {
        String veri = ExcelUtil.dataManipulator.motorYukseklikVerileri.get(secilenMotor);
        String sayiKismi = veri.replaceAll("[^0-9]", "");
        int yukseklik = Integer.parseInt(sayiKismi);
        return Math.min(h, yukseklik);
    }

    private static int calculateVolume(int x, int y, int h) {
        return ((x * h * y) / 1000000) - ExcelUtil.dataManipulator.kayipLitre;
    }

    private static int[] findBestDimensions(double hesaplananHacim, int girilenTankKapasitesiMiktari, int x, int y) {
        int enKucukLitreFarki = Integer.MAX_VALUE;
        int[] enKucukLitreOlculer = null;

        for (int[] olculer : ExcelUtil.dataManipulator.kabinOlculeri.values()) {
            int litre = olculer[3];
            int tempX = olculer[0];
            int tempY = olculer[1];

            if (hesaplananHacim > girilenTankKapasitesiMiktari) {
                if (x <= tempX && y <= tempY) {
                    enKucukLitreOlculer = olculer;
                    break;
                }
            } else {
                if (litre >= girilenTankKapasitesiMiktari && litre - girilenTankKapasitesiMiktari <= enKucukLitreFarki) {
                    if (hesaplananHacim != litre && hesaplananHacim < litre) {
                        if (x < tempX && y < tempY) {
                            enKucukLitreFarki = litre - girilenTankKapasitesiMiktari;
                            enKucukLitreOlculer = olculer;
                        }
                    }
                }
            }
        }

        return enKucukLitreOlculer;
    }

    private static void printCalculationSummary(int x, int y, int h, int yV, int yK, int eskiX, int eskiY, int eskiH, int hesaplananHacim, String atananKabinFinal, String gecisOlculeriFinal) {
        System.out.println("--------Hesaplama Bitti--------");
        System.out.println("------------(Sonuç)------------");
        System.out.println("yV: " + yV);
        System.out.println("yK: " + yK);
        System.out.println("Hesaplanan X: " + eskiX);
        System.out.println("Hesaplanan Y: " + eskiY);
        System.out.println("Hesaplanan h: " + eskiH);
        System.out.println("Hesaplanan Hacim: " + hesaplananHacim);
        System.out.println("Atanan X: " + x);
        System.out.println("Atanan Y: " + y);
        System.out.println("Atanan h: " + h);
        System.out.println("Atanan Hacim: " + hesaplananHacim);
        System.out.println("Kullanmanız Gereken Kabin: " + atananKabinFinal);
        System.out.println("Geçiş Ölçüleri: " + gecisOlculeriFinal);
        System.out.println("-------------------------------");
    }
}