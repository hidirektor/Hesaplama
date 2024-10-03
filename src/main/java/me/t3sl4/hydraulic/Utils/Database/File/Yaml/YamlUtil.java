package me.t3sl4.hydraulic.Utils.Database.File.Yaml;

import me.t3sl4.hydraulic.Launcher;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class YamlUtil {

    public YamlUtil(String filePath) {
        loadYaml(filePath);
    }

    @SuppressWarnings("unchecked")
    private void loadYaml(String filePath) {
        Yaml yaml = new Yaml();
        InputStream inputStream = null;
        try {
            // Resource dosyasını yükleme
            inputStream = new FileInputStream(filePath);

            // YAML dosyasını yükle ve haritalara ekle
            Map<String, Object> data = yaml.load(inputStream);

            // Motor map
            LinkedHashMap<String, Object> motorData = (LinkedHashMap<String, Object>) data.get("motor");
            for (Map.Entry<String, Object> entry : motorData.entrySet()) {
                LinkedHashMap<String, String> motorDetails = (LinkedHashMap<String, String>) entry.getValue();
                Launcher.getDataManipulator().motorMap.put(Integer.parseInt(entry.getKey()), motorDetails.get("name"));
            }

            // Sogutma map
            LinkedHashMap<String, Object> sogutmaData = (LinkedHashMap<String, Object>) data.get("sogutma");
            for (Map.Entry<String, Object> entry : sogutmaData.entrySet()) {
                Launcher.getDataManipulator().sogutmaMap.put(Integer.parseInt(entry.getKey()), ((LinkedHashMap<String, String>) entry.getValue()).get("value"));
            }

            // Hidrolik kilit map
            LinkedHashMap<String, Object> hidrolikKilitData = (LinkedHashMap<String, Object>) data.get("hidrolik_kilit");
            for (Map.Entry<String, Object> entry : hidrolikKilitData.entrySet()) {
                Launcher.getDataManipulator().hidrolikKilitMap.put(Integer.parseInt(entry.getKey()), ((LinkedHashMap<String, String>) entry.getValue()).get("value"));
            }

            // PowerPack Pompa map
            LinkedHashMap<String, Object> powerPackPompaData = (LinkedHashMap<String, Object>) ((LinkedHashMap<String, Object>) data.get("pompa")).get("0");
            LinkedHashMap<String, Object> powerPackPompaValues = (LinkedHashMap<String, Object>) powerPackPompaData.get("values");
            for (Map.Entry<String, Object> entry : powerPackPompaValues.entrySet()) {
                LinkedHashMap<String, String> pompaDetails = (LinkedHashMap<String, String>) entry.getValue();
                Launcher.getDataManipulator().powerPackPompaMap.put(Integer.parseInt(entry.getKey()), pompaDetails.get("name"));
            }

            // Klasik Pompa map
            LinkedHashMap<String, Object> klasikPompaData = (LinkedHashMap<String, Object>) ((LinkedHashMap<String, Object>) data.get("pompa")).get("1");
            LinkedHashMap<String, Object> klasikPompaValues = (LinkedHashMap<String, Object>) klasikPompaData.get("values");
            for (Map.Entry<String, Object> entry : klasikPompaValues.entrySet()) {
                LinkedHashMap<String, String> pompaDetails = (LinkedHashMap<String, String>) entry.getValue();
                Launcher.getDataManipulator().klasikPompaMap.put(Integer.parseInt(entry.getKey()), pompaDetails.get("name"));
            }

            // Kompanzasyon map
            LinkedHashMap<String, Object> kompanzasyonData = (LinkedHashMap<String, Object>) data.get("kompanzasyon");
            for (Map.Entry<String, Object> entry : kompanzasyonData.entrySet()) {
                Launcher.getDataManipulator().kompanzasyonMap.put(Integer.parseInt(entry.getKey()), ((LinkedHashMap<String, String>) entry.getValue()).get("value"));
            }

            // Valf Tipi map
            LinkedHashMap<String, Object> valfTipiData = (LinkedHashMap<String, Object>) data.get("valf_tipi");
            for (Map.Entry<String, Object> entry : valfTipiData.entrySet()) {
                LinkedHashMap<String, String> valfOptions = (LinkedHashMap<String, String>) ((LinkedHashMap<String, Object>) entry.getValue()).get("options");
                Launcher.getDataManipulator().valfTipiMap.put(Integer.parseInt(entry.getKey()), valfOptions.get("value"));
            }

            // Kilit Motor map
            LinkedHashMap<String, Object> kilitMotorData = (LinkedHashMap<String, Object>) data.get("kilit_motor");
            for (Map.Entry<String, Object> entry : kilitMotorData.entrySet()) {
                Launcher.getDataManipulator().kilitMotorMap.put(Integer.parseInt(entry.getKey()), ((LinkedHashMap<String, String>) entry.getValue()).get("value"));
            }

            // Kilit Pompa map
            LinkedHashMap<String, Object> kilitPompaData = (LinkedHashMap<String, Object>) data.get("kilit_pompa");
            for (Map.Entry<String, Object> entry : kilitPompaData.entrySet()) {
                Launcher.getDataManipulator().kilitPompaMap.put(Integer.parseInt(entry.getKey()), ((LinkedHashMap<String, String>) entry.getValue()).get("value"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // InputStream'in kapatılması
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}