package me.t3sl4.hydraulic.Utils.Database.File.Yaml;

import me.t3sl4.hydraulic.Launcher;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Map;

public class YamlUtil {

    public YamlUtil(String filePath) {
        loadYaml(filePath);
    }

    public void loadYaml(String filePath) {
        try {
            loadMotor(filePath);
            loadSogutma(filePath);
            loadHydraulicLock(filePath);
            loadPompa(filePath);
            loadKompanzasyon(filePath);
            loadValfTipi(filePath);
            loadKilitMotor(filePath);
            loadKilitPompa(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMotor(String filePath) throws FileNotFoundException {
        InputStream input = new FileInputStream(filePath);
        Yaml yaml = new Yaml();
        Map<String, Object> yamlData = yaml.load(input);
        Map<String, Map<String, Map<String, String>>> motorData = (Map<String, Map<String, Map<String, String>>>) yamlData.get("motor");

        motorData.forEach((key, value) -> {
            LinkedList<String> motorList = new LinkedList<>();
            value.forEach((innerKey, motorDetails) -> {
                String motorName = motorDetails.get("name");
                motorList.add(motorName);

                // Kampana ve yükseklik map'lerine atama
                Launcher.getDataManipulator().motorKampanaMap.put(motorName, motorDetails.get("kampana"));
                Launcher.getDataManipulator().motorYukseklikMap.put(motorName, motorDetails.get("yukseklik"));
            });
            Launcher.getDataManipulator().motorMap.put(key, motorList);
        });
    }

    public void loadSogutma(String filePath) throws FileNotFoundException {
        InputStream input = new FileInputStream(filePath);
        Yaml yaml = new Yaml();
        Map<String, Object> yamlData = yaml.load(input);

        Map<String, Object> sogutma = (Map<String, Object>) yamlData.get("sogutma");
        if (sogutma != null) {
            for (String key : sogutma.keySet()) {
                Map<String, Object> options = (Map<String, Object>) ((Map<String, Object>) sogutma.get(key)).get("options");
                LinkedList<String> valuesList = new LinkedList<>();
                for (String optionKey : options.keySet()) {
                    Object valueMap = options.get(optionKey);
                    if (valueMap instanceof Map) {
                        valuesList.add(((Map<String, String>) valueMap).get("value"));
                    } else if (valueMap instanceof String) {
                        valuesList.add((String) valueMap);
                    }
                }
                Launcher.getDataManipulator().coolingMap.put(key, valuesList);
            }
        }
    }

    public void loadHydraulicLock(String filePath) throws FileNotFoundException {
        InputStream input = new FileInputStream(filePath);
        Yaml yaml = new Yaml();
        Map<String, Object> yamlData = yaml.load(input);

        Map<String, Object> hydraulicLock = (Map<String, Object>) yamlData.get("hidrolik_kilit");
        if (hydraulicLock != null) {
            for (String key : hydraulicLock.keySet()) {
                Map<String, Object> options = (Map<String, Object>) ((Map<String, Object>) hydraulicLock.get(key)).get("options");
                LinkedList<String> valuesList = new LinkedList<>();
                for (String optionKey : options.keySet()) {
                    Object valueMap = options.get(optionKey);
                    if (valueMap instanceof Map) {
                        valuesList.add(((Map<String, String>) valueMap).get("value"));
                    } else if (valueMap instanceof String) {
                        valuesList.add((String) valueMap);
                    }
                }
                Launcher.getDataManipulator().hydraulicLockMap.put(key, valuesList);
            }
        }
    }

    public void loadPompa(String filePath) throws FileNotFoundException {
        InputStream input = new FileInputStream(filePath);
        Yaml yaml = new Yaml();
        Map<String, Object> yamlData = yaml.load(input);

        Map<String, Map<String, Object>> pompaData = (Map<String, Map<String, Object>>) yamlData.get("pompa");

        pompaData.forEach((key, value) -> {
            LinkedList<String> pompaList = new LinkedList<>();

            Map<String, Map<String, String>> options = (Map<String, Map<String, String>>) value.get("options");

            options.forEach((innerKey, pompaDetails) -> {
                String pompaName = pompaDetails.get("name");
                pompaList.add(pompaName);
            });

            Launcher.getDataManipulator().pumpMap.put(key, pompaList);
        });
    }

    public void loadKompanzasyon(String filePath) throws FileNotFoundException {
        InputStream input = new FileInputStream(filePath);
        Yaml yaml = new Yaml();
        Map<String, Object> yamlData = yaml.load(input);

        Map<String, Object> kompanzasyon = (Map<String, Object>) yamlData.get("kompanzasyon");
        if (kompanzasyon != null) {
            for (String key : kompanzasyon.keySet()) {
                Map<String, Object> options = (Map<String, Object>) ((Map<String, Object>) kompanzasyon.get(key)).get("options");
                LinkedList<String> valuesList = new LinkedList<>();
                for (String optionKey : options.keySet()) {
                    Object valueMap = options.get(optionKey);
                    if (valueMap instanceof Map) {
                        valuesList.add(((Map<String, String>) valueMap).get("value"));
                    } else if (valueMap instanceof String) {
                        valuesList.add((String) valueMap);
                    }
                }
                Launcher.getDataManipulator().compensationMap.put(key, valuesList);
            }
        }
    }

    public void loadValfTipi(String filePath) throws FileNotFoundException {
        InputStream input = new FileInputStream(filePath);
        Yaml yaml = new Yaml();
        Map<String, Object> yamlData = yaml.load(input);

        Map<String, Map<String, Object>> valfTipiData = (Map<String, Map<String, Object>>) yamlData.get("valf_tipi");

        valfTipiData.forEach((key, value) -> {
            LinkedList<String> valfTipiList = new LinkedList<>();

            Map<String, Map<String, String>> options = (Map<String, Map<String, String>>) value.get("options");

            options.forEach((innerKey, valfTipiDetails) -> {
                String valfTipiName = valfTipiDetails.get("value");
                valfTipiList.add(valfTipiName);
            });

            Launcher.getDataManipulator().valveTypeMap.put(key, valfTipiList);
        });
    }

    public void loadKilitMotor(String filePath) throws FileNotFoundException {
        InputStream input = new FileInputStream(filePath);
        Yaml yaml = new Yaml();
        Map<String, Object> yamlData = yaml.load(input);

        Map<String, Object> kilitMotor = (Map<String, Object>) yamlData.get("kilit_motor");
        if (kilitMotor != null) {
            for (String key : kilitMotor.keySet()) {
                Map<String, Object> options = (Map<String, Object>) ((Map<String, Object>) kilitMotor.get(key)).get("options");
                LinkedList<String> valuesList = new LinkedList<>();
                for (String optionKey : options.keySet()) {
                    Object valueMap = options.get(optionKey);
                    if (valueMap instanceof Map) {
                        valuesList.add(((Map<String, String>) valueMap).get("value"));
                    } else if (valueMap instanceof String) {
                        valuesList.add((String) valueMap);
                    }
                }
                Launcher.getDataManipulator().lockMotorMap.put(key, valuesList);
            }
        }
    }

    public void loadKilitPompa(String filePath) throws FileNotFoundException {
        InputStream input = new FileInputStream(filePath);
        Yaml yaml = new Yaml();
        Map<String, Object> yamlData = yaml.load(input);

        Map<String, Object> kilitPompa = (Map<String, Object>) yamlData.get("kilit_pompa");
        if (kilitPompa != null) {
            for (String key : kilitPompa.keySet()) {
                Map<String, Object> options = (Map<String, Object>) ((Map<String, Object>) kilitPompa.get(key)).get("options");
                LinkedList<String> valuesList = new LinkedList<>();
                for (String optionKey : options.keySet()) {
                    Object valueMap = options.get(optionKey);
                    if (valueMap instanceof Map) {
                        valuesList.add(((Map<String, String>) valueMap).get("value"));
                    } else if (valueMap instanceof String) {
                        valuesList.add((String) valueMap);
                    }
                }
                Launcher.getDataManipulator().lockPumpMap.put(key, valuesList);
            }
        }
    }
}