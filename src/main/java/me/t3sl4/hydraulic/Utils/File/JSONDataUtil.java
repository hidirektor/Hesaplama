package me.t3sl4.hydraulic.Utils.File;

import me.t3sl4.hydraulic.Launcher;
import me.t3sl4.hydraulic.Utils.Model.Excel.DataManipulator;
import me.t3sl4.hydraulic.Utils.Model.Tank.Tank;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JSONDataUtil {
    private static final Logger logger = Logger.getLogger(JSONDataUtil.class.getName());

    public static void loadJSONData() {
        readJson4DefinedTanks(Launcher.cabinetesDBPath, Launcher.getDataManipulator());
        readJson4Bosluk(Launcher.generalDBPath, Launcher.getDataManipulator());
        readJson4UniteType(Launcher.generalDBPath, Launcher.getDataManipulator());
    }
    private static void readJson4DefinedTanks(String filePath, DataManipulator dataManipulator) {
        try {
            FileReader reader = new FileReader(filePath);
            StringBuilder sb = new StringBuilder();
            int i;
            while ((i = reader.read()) != -1) {
                sb.append((char) i);
            }
            reader.close();

            JSONObject jsonObject = new JSONObject(sb.toString());
            JSONArray classicCabinetes = jsonObject.getJSONArray("classic_cabinetes");

            for (int j = 0; j < classicCabinetes.length(); j++) {
                JSONObject cabinet = classicCabinetes.getJSONObject(j);

                String tankName = cabinet.getString("tankName");
                String kabinName = cabinet.getString("kabinName");
                int kabinHacim = cabinet.getInt("kabinHacim");
                int gecisX = cabinet.getInt("gecisX (G)");
                int gecisY = cabinet.getInt("gecisY (D)");
                int gecisH = cabinet.getInt("gecisH (Y)");
                int kabinX = cabinet.getInt("kabinX (G)");
                int kabinY = cabinet.getInt("kabinY (D)");
                int kabinH = cabinet.getInt("kabinH (Y)");
                String malzemeKodu = cabinet.getString("malzemeKodu");
                String malzemeAdi = cabinet.getString("malzemeAdi");

                Tank tank = new Tank(tankName, kabinName, kabinHacim, gecisX, gecisY, gecisH, kabinX, kabinY, kabinH, malzemeKodu, malzemeAdi);
                dataManipulator.inputTanks.add(tank);
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private static void readJson4Bosluk(String filePath, DataManipulator dataManipulator) {
        try {
            FileReader reader = new FileReader(filePath);
            StringBuilder sb = new StringBuilder();
            int i;
            while ((i = reader.read()) != -1) {
                sb.append((char) i);
            }
            reader.close();

            JSONObject jsonObject = new JSONObject(sb.toString());
            JSONObject voidValues = jsonObject.getJSONObject("void_values");

            dataManipulator.kampanaBoslukX = Integer.parseInt(voidValues.getString("kampanaBoslukX"));
            dataManipulator.kampanaBoslukY = Integer.parseInt(voidValues.getString("kampanaBoslukY"));
            dataManipulator.valfBoslukX = Integer.parseInt(voidValues.getString("valfBoslukX"));
            dataManipulator.valfBoslukYArka = Integer.parseInt(voidValues.getString("valfBoslukYArka"));
            dataManipulator.valfBoslukYOn = Integer.parseInt(voidValues.getString("valfBoslukYOn"));
            dataManipulator.kilitliBlokAraBoslukX = Integer.parseInt(voidValues.getString("kilitliBlokAraBoslukX"));
            dataManipulator.tekHizAraBoslukX = Integer.parseInt(voidValues.getString("tekHizAraBoslukX"));
            dataManipulator.ciftHizAraBoslukX = Integer.parseInt(voidValues.getString("ciftHizAraBoslukX"));
            dataManipulator.kompanzasyonTekHizAraBoslukX = Integer.parseInt(voidValues.getString("kompanzasyonTekHizAraBoslukX"));
            dataManipulator.sogutmaAraBoslukX = Integer.parseInt(voidValues.getString("sogutmaAraBoslukX"));
            dataManipulator.sogutmaAraBoslukYkOn = Integer.parseInt(voidValues.getString("sogutmaAraBoslukYkOn"));
            dataManipulator.sogutmaAraBoslukYkArka = Integer.parseInt(voidValues.getString("sogutmaAraBoslukYkArka"));
            dataManipulator.kilitMotorKampanaBosluk = Integer.parseInt(voidValues.getString("kilitMotorKampanaBosluk"));
            dataManipulator.kilitMotorMotorBoslukX = Integer.parseInt(voidValues.getString("kilitMotorMotorBoslukX"));
            dataManipulator.kilitMotorBoslukYOn = Integer.parseInt(voidValues.getString("kilitMotorBoslukYOn"));
            dataManipulator.kilitMotorBoslukYArka = Integer.parseInt(voidValues.getString("kilitMotorBoslukYArka"));
            dataManipulator.kayipLitre = Integer.parseInt(voidValues.getString("kayipLitre"));
            dataManipulator.kilitPlatformMotorBosluk = Integer.parseInt(voidValues.getString("kilitPlatformMotorBosluk"));
            dataManipulator.valfXBoslukSogutma = Integer.parseInt(voidValues.getString("valfXBoslukSogutma"));

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private static void readJson4UniteType(String filePath, DataManipulator dataManipulator) {
        try {
            FileReader reader = new FileReader(filePath);
            StringBuilder sb = new StringBuilder();
            int i;
            while ((i = reader.read()) != -1) {
                sb.append((char) i);
            }
            reader.close();

            JSONObject jsonObject = new JSONObject(sb.toString());
            JSONObject unitTypeValues = jsonObject.getJSONObject("unit_types");

            dataManipulator.uniteTipiDegerleri.add(unitTypeValues.getString("classic"));
            dataManipulator.uniteTipiDegerleri.add(unitTypeValues.getString("power_pack"));
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
