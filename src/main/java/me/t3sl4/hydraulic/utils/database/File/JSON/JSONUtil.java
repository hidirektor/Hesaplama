package me.t3sl4.hydraulic.utils.database.File.JSON;

import me.t3sl4.hydraulic.utils.database.Model.HydraulicData.HydraulicData;
import me.t3sl4.hydraulic.utils.database.Model.Tank.Tank;
import me.t3sl4.hydraulic.utils.general.SystemVariables;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JSONUtil {
    private static final Logger logger = Logger.getLogger(JSONUtil.class.getName());

    public static void loadJSONData() {
        readJson4DefinedTanks(SystemVariables.cabinsDBPath, SystemVariables.getLocalHydraulicData());
        readJson4Bosluk(SystemVariables.generalDBPath, SystemVariables.getLocalHydraulicData());
        readJson4UniteType(SystemVariables.generalDBPath, SystemVariables.getLocalHydraulicData());
    }
    private static void readJson4DefinedTanks(String filePath, HydraulicData hydraulicData) {
        try {
            FileReader reader = new FileReader(filePath);
            StringBuilder sb = new StringBuilder();
            int i;
            while ((i = reader.read()) != -1) {
                sb.append((char) i);
            }
            reader.close();

            JSONObject jsonObject = new JSONObject(sb.toString());
            JSONArray classicCabinetes = jsonObject.getJSONArray("classic_cabins");

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
                hydraulicData.inputTanks.add(tank);
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private static void readJson4Bosluk(String filePath, HydraulicData hydraulicData) {
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

            hydraulicData.kampanaBoslukX = Integer.parseInt(voidValues.getString("kampanaBoslukX"));
            hydraulicData.kampanaBoslukY = Integer.parseInt(voidValues.getString("kampanaBoslukY"));
            hydraulicData.valfBoslukX = Integer.parseInt(voidValues.getString("valfBoslukX"));
            hydraulicData.valfBoslukYArka = Integer.parseInt(voidValues.getString("valfBoslukYArka"));
            hydraulicData.valfBoslukYOn = Integer.parseInt(voidValues.getString("valfBoslukYOn"));
            hydraulicData.kilitliBlokAraBoslukX = Integer.parseInt(voidValues.getString("kilitliBlokAraBoslukX"));
            hydraulicData.tekHizAraBoslukX = Integer.parseInt(voidValues.getString("tekHizAraBoslukX"));
            hydraulicData.ciftHizAraBoslukX = Integer.parseInt(voidValues.getString("ciftHizAraBoslukX"));
            hydraulicData.kompanzasyonTekHizAraBoslukX = Integer.parseInt(voidValues.getString("kompanzasyonTekHizAraBoslukX"));
            hydraulicData.sogutmaAraBoslukX = Integer.parseInt(voidValues.getString("sogutmaAraBoslukX"));
            hydraulicData.sogutmaAraBoslukYkOn = Integer.parseInt(voidValues.getString("sogutmaAraBoslukYkOn"));
            hydraulicData.sogutmaAraBoslukYkArka = Integer.parseInt(voidValues.getString("sogutmaAraBoslukYkArka"));
            hydraulicData.kilitMotorKampanaBosluk = Integer.parseInt(voidValues.getString("kilitMotorKampanaBosluk"));
            hydraulicData.kilitMotorMotorBoslukX = Integer.parseInt(voidValues.getString("kilitMotorMotorBoslukX"));
            hydraulicData.kilitMotorBoslukYOn = Integer.parseInt(voidValues.getString("kilitMotorBoslukYOn"));
            hydraulicData.kilitMotorBoslukYArka = Integer.parseInt(voidValues.getString("kilitMotorBoslukYArka"));
            hydraulicData.kayipLitre = Integer.parseInt(voidValues.getString("kayipLitre"));
            hydraulicData.kilitPlatformMotorBosluk = Integer.parseInt(voidValues.getString("kilitPlatformMotorBosluk"));
            hydraulicData.valfXBoslukSogutma = Integer.parseInt(voidValues.getString("valfXBoslukSogutma"));

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private static void readJson4UniteType(String filePath, HydraulicData hydraulicData) {
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

            hydraulicData.uniteTipiDegerleri.add(unitTypeValues.getString("classic"));
            hydraulicData.uniteTipiDegerleri.add(unitTypeValues.getString("power_pack"));
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
