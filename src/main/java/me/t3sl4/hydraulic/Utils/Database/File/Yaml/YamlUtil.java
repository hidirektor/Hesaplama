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

    public YamlUtil(String classicComboPath, String powerPackComboPath, String classicPartPath, String powerpackPartPath) {
        loadClassicCombo(classicComboPath);
        loadPowerPackCombo(powerPackComboPath);
        loadClassicPartList(classicPartPath);
        loadPowerPackHidrosPartList(powerpackPartPath);
        loadPowerPackIthalPartList(powerpackPartPath);
    }

    public void loadClassicCombo(String filePath) {
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

    public void loadPowerPackCombo(String filePath) {
        try {
            loadMotorVoltaj(filePath);
            loadUniteTipi(filePath);
            loadMotorGucu(filePath);
            loadPowerPackPompa(filePath);
            loadTankTipi(filePath);
            loadTankKapasitesi(filePath);
            loadPlatformTipi(filePath);
            loadPowerPackValfTipi(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadClassicPartList(String filePath) {
        try {
            loadClassicParcaMotor(filePath);
            loadClassicParcaKampana(filePath);
            loadClassicParcaPompa(filePath);
            loadClassicParcaKaplin(filePath);
            loadClassicParcaValfBloklari(filePath);
            loadClassicParcaSogutma(filePath);
            loadClassicParcaBasincSalteri(filePath);
            loadClassicParcaDefault(filePath);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void loadPowerPackHidrosPartList(String filePath) {
        try {
            loadHidrosParcaMotor380(filePath);
            loadHidrosParcaMotor220(filePath);
            loadHidrosParcaPompa(filePath);
            loadHidrosParcaTankDikey(filePath);
            loadHidrosParcaTankYatay(filePath);
            loadHidrosParcaESPGenel(filePath);
            loadHidrosParcaESPDikeyCift(filePath);
            loadHidrosParcaDevirmeli(filePath);
            loadHidrosParcaStandart(filePath);
            loadHidrosParcaOzelYatay(filePath);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void loadPowerPackIthalPartList(String filePath) {
        //TODO
        //Power pack parçaları eklenecek
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

    public void loadMotorVoltaj(String filePath) throws FileNotFoundException {
        InputStream input = new FileInputStream(filePath);
        Yaml yaml = new Yaml();
        Map<String, Object> yamlData = yaml.load(input);

        Map<String, Object> motorVoltaj = (Map<String, Object>) yamlData.get("motor");
        if (motorVoltaj != null) {
            for (String key : motorVoltaj.keySet()) {
                Map<String, Object> options = (Map<String, Object>) ((Map<String, Object>) motorVoltaj.get(key)).get("options");
                LinkedList<String> valuesList = new LinkedList<>();
                for (String optionKey : options.keySet()) {
                    Object valueMap = options.get(optionKey);
                    if (valueMap instanceof Map) {
                        valuesList.add(((Map<String, String>) valueMap).get("value"));
                    } else if (valueMap instanceof String) {
                        valuesList.add((String) valueMap);
                    }
                }
                Launcher.getDataManipulator().motorVoltajMap.put(key, valuesList);
            }
        }
    }

    public void loadUniteTipi(String filePath) throws FileNotFoundException {
        InputStream input = new FileInputStream(filePath);
        Yaml yaml = new Yaml();
        Map<String, Object> yamlData = yaml.load(input);

        Map<String, Object> uniteTipi = (Map<String, Object>) yamlData.get("unite_tipi");
        if (uniteTipi != null) {
            for (String key : uniteTipi.keySet()) {
                Map<String, Object> options = (Map<String, Object>) ((Map<String, Object>) uniteTipi.get(key)).get("options");
                LinkedList<String> valuesList = new LinkedList<>();
                for (String optionKey : options.keySet()) {
                    Object valueMap = options.get(optionKey);
                    if (valueMap instanceof Map) {
                        valuesList.add(((Map<String, String>) valueMap).get("value"));
                    } else if (valueMap instanceof String) {
                        valuesList.add((String) valueMap);
                    }
                }
                Launcher.getDataManipulator().uniteTipiMap.put(key, valuesList);
            }
        }
    }

    public void loadMotorGucu(String filePath) throws FileNotFoundException {
        InputStream input = new FileInputStream(filePath);
        Yaml yaml = new Yaml();
        Map<String, Object> yamlData = yaml.load(input);

        Map<String, Map<String, Object>> motorGucuData = (Map<String, Map<String, Object>>) yamlData.get("motor_gucu");

        motorGucuData.forEach((key, value) -> {
            LinkedList<String> motorGucuList = new LinkedList<>();

            Map<String, Map<String, String>> options = (Map<String, Map<String, String>>) value.get("options");

            options.forEach((innerKey, pompaDetails) -> {
                String pompaName = pompaDetails.get("name");
                motorGucuList.add(pompaName);
            });

            Launcher.getDataManipulator().motorGucuMap.put(key, motorGucuList);
        });
    }

    public void loadPowerPackPompa(String filePath) throws FileNotFoundException {
        InputStream input = new FileInputStream(filePath);
        Yaml yaml = new Yaml();
        Map<String, Object> yamlData = yaml.load(input);

        Map<String, Map<String, Object>> pompaData = (Map<String, Map<String, Object>>) yamlData.get("pompa");

        pompaData.forEach((key, value) -> {
            LinkedList<String> pompaList = new LinkedList<>();

            Map<String, Map<String, String>> options = (Map<String, Map<String, String>>) value.get("options");

            options.forEach((innerKey, pompaDetails) -> {
                String pompaName = pompaDetails.get("value");
                pompaList.add(pompaName);
            });

            Launcher.getDataManipulator().pompaPowerPackMap.put(key, pompaList);
        });
    }

    public void loadTankTipi(String filePath) throws FileNotFoundException {
        InputStream input = new FileInputStream(filePath);
        Yaml yaml = new Yaml();
        Map<String, Object> yamlData = yaml.load(input);

        Map<String, Object> tankTipi = (Map<String, Object>) yamlData.get("tank_tipi");
        if (tankTipi != null) {
            for (String key : tankTipi.keySet()) {
                Map<String, Object> options = (Map<String, Object>) ((Map<String, Object>) tankTipi.get(key)).get("options");
                LinkedList<String> valuesList = new LinkedList<>();
                for (String optionKey : options.keySet()) {
                    Object valueMap = options.get(optionKey);
                    if (valueMap instanceof Map) {
                        valuesList.add(((Map<String, String>) valueMap).get("value"));
                    } else if (valueMap instanceof String) {
                        valuesList.add((String) valueMap);
                    }
                }
                Launcher.getDataManipulator().tankTipiMap.put(key, valuesList);
            }
        }
    }

    public void loadTankKapasitesi(String filePath) throws FileNotFoundException {
        InputStream input = new FileInputStream(filePath);
        Yaml yaml = new Yaml();
        Map<String, Object> yamlData = yaml.load(input);

        Map<String, Map<String, Object>> tankKapasitesiData = (Map<String, Map<String, Object>>) yamlData.get("tank_kapasitesi");

        tankKapasitesiData.forEach((key, value) -> {
            LinkedList<String> tankKapasitesiList = new LinkedList<>();

            Map<String, Map<String, String>> options = (Map<String, Map<String, String>>) value.get("options");

            options.forEach((innerKey, tankKapasitesiDetails) -> {
                String tankKapasitesiName = tankKapasitesiDetails.get("value");
                tankKapasitesiList.add(tankKapasitesiName);
            });

            Launcher.getDataManipulator().tankKapasitesiMap.put(key, tankKapasitesiList);
        });
    }

    public void loadPlatformTipi(String filePath) throws FileNotFoundException {
        InputStream input = new FileInputStream(filePath);
        Yaml yaml = new Yaml();
        Map<String, Object> yamlData = yaml.load(input);

        Map<String, Object> platformTipi = (Map<String, Object>) yamlData.get("platform_tipi");
        if (platformTipi != null) {
            for (String key : platformTipi.keySet()) {
                Map<String, Object> options = (Map<String, Object>) ((Map<String, Object>) platformTipi.get(key)).get("options");
                LinkedList<String> valuesList = new LinkedList<>();
                for (String optionKey : options.keySet()) {
                    Object valueMap = options.get(optionKey);
                    if (valueMap instanceof Map) {
                        valuesList.add(((Map<String, String>) valueMap).get("value"));
                    } else if (valueMap instanceof String) {
                        valuesList.add((String) valueMap);
                    }
                }
                Launcher.getDataManipulator().platformTipiMap.put(key, valuesList);
            }
        }
    }

    public void loadPowerPackValfTipi(String filePath) throws FileNotFoundException {
        InputStream input = new FileInputStream(filePath);
        Yaml yaml = new Yaml();
        Map<String, Object> yamlData = yaml.load(input);

        Map<String, Object> valfTipi = (Map<String, Object>) yamlData.get("valf_tipleri");
        if (valfTipi != null) {
            for (String key : valfTipi.keySet()) {
                Map<String, Object> options = (Map<String, Object>) ((Map<String, Object>) valfTipi.get(key)).get("options");
                LinkedList<String> valuesList = new LinkedList<>();
                for (String optionKey : options.keySet()) {
                    Object valueMap = options.get(optionKey);
                    if (valueMap instanceof Map) {
                        valuesList.add(((Map<String, String>) valueMap).get("value"));
                    } else if (valueMap instanceof String) {
                        valuesList.add((String) valueMap);
                    }
                }
                Launcher.getDataManipulator().valfTipiMap.put(key, valuesList);
            }
        }
    }

    public void loadClassicParcaMotor(String filePath) throws FileNotFoundException {
        InputStream input = new FileInputStream(filePath);
        Yaml yaml = new Yaml();
        Map<String, Object> yamlData = yaml.load(input);

        if (yamlData != null) {
            Map<String, Object> motorData = (Map<String, Object>) yamlData.get("motor");

            for (Map.Entry<String, Object> entry : motorData.entrySet()) {
                String key = entry.getKey();
                Map<String, String> motorDetails = (Map<String, String>) entry.getValue();

                String malzemeKodu = motorDetails.get("malzemeKodu");
                String malzemeAdi = motorDetails.get("malzemeAdi");
                String malzemeAdet = motorDetails.get("malzemeAdet");

                String combinedDetails = malzemeKodu + ";" + malzemeAdi + ";" + malzemeAdet;

                LinkedList<String> motorList = new LinkedList<>();
                motorList.add(combinedDetails);

                Launcher.getDataManipulator().classicParcaMotor.put(key, motorList);
            }
        }
    }

    public void loadClassicParcaKampana(String filePath) throws FileNotFoundException {
        InputStream input = new FileInputStream(filePath);
        Yaml yaml = new Yaml();
        Map<String, Object> yamlData = yaml.load(input);

        if (yamlData != null) {
            Map<String, Object> kampanaData = (Map<String, Object>) yamlData.get("kampana");

            for (Map.Entry<String, Object> kampanaEntry : kampanaData.entrySet()) {
                String kampanaKey = kampanaEntry.getKey();
                Map<String, Object> partsData = (Map<String, Object>) kampanaEntry.getValue();
                Map<String, Object> parts = (Map<String, Object>) partsData.get("parts");

                LinkedList<String> partDetailsList = new LinkedList<>();

                for (Map.Entry<String, Object> partEntry : parts.entrySet()) {
                    Map<String, String> partDetails = (Map<String, String>) partEntry.getValue();

                    String malzemeKodu = partDetails.get("malzemeKodu");
                    String malzemeAdi = partDetails.get("malzemeAdi");
                    String malzemeAdet = partDetails.get("malzemeAdet");

                    String combinedDetails = malzemeKodu + ";" + malzemeAdi + ";" + malzemeAdet;

                    partDetailsList.add(combinedDetails);
                }

                Launcher.getDataManipulator().classicParcaKampana.put(kampanaKey, partDetailsList);
            }
        }
    }

    public void loadClassicParcaPompa(String filePath) throws FileNotFoundException {
        InputStream input = new FileInputStream(filePath);
        Yaml yaml = new Yaml();
        Map<String, Object> yamlData = yaml.load(input);

        if (yamlData != null) {
            Map<String, Object> pompaData = (Map<String, Object>) yamlData.get("pompa");

            for (Map.Entry<String, Object> pompaEntry : pompaData.entrySet()) {
                String pompaKey = pompaEntry.getKey();
                Map<String, Object> partsData = (Map<String, Object>) pompaEntry.getValue();
                Map<String, Object> parts = (Map<String, Object>) partsData.get("parts");

                LinkedList<String> partDetailsList = new LinkedList<>();

                for (Map.Entry<String, Object> partEntry : parts.entrySet()) {
                    Map<String, String> partDetails = (Map<String, String>) partEntry.getValue();

                    String malzemeKodu = partDetails.get("malzemeKodu");
                    String malzemeAdi = partDetails.get("malzemeAdi");
                    String malzemeAdet = partDetails.get("malzemeAdet");

                    String combinedDetails = malzemeKodu + ";" + malzemeAdi + ";" + malzemeAdet;

                    partDetailsList.add(combinedDetails);
                }

                Launcher.getDataManipulator().classicParcaPompa.put(pompaKey, partDetailsList);
            }
        }
    }

    public void loadClassicParcaKaplin(String filePath) throws FileNotFoundException {
        InputStream input = new FileInputStream(filePath);
        Yaml yaml = new Yaml();
        Map<String, Object> yamlData = yaml.load(input);

        if (yamlData != null) {
            Map<String, Object> kaplinData = (Map<String, Object>) yamlData.get("kaplin");

            for (Map.Entry<String, Object> kaplinEntry : kaplinData.entrySet()) {
                String kaplinKey = kaplinEntry.getKey();
                Map<String, Object> partsData = (Map<String, Object>) kaplinEntry.getValue();
                Map<String, Object> parts = (Map<String, Object>) partsData.get("parts");

                LinkedList<String> partDetailsList = new LinkedList<>();

                for (Map.Entry<String, Object> partEntry : parts.entrySet()) {
                    Map<String, String> partDetails = (Map<String, String>) partEntry.getValue();

                    String malzemeKodu = partDetails.get("malzemeKodu");
                    String malzemeAdi = partDetails.get("malzemeAdi");
                    String malzemeAdet = partDetails.get("malzemeAdet");

                    String combinedDetails = malzemeKodu + ";" + malzemeAdi + ";" + malzemeAdet;

                    partDetailsList.add(combinedDetails);
                }

                Launcher.getDataManipulator().classicParcaKaplin.put(kaplinKey, partDetailsList);
            }
        }
    }

    public void loadClassicParcaValfBloklari(String filePath) throws FileNotFoundException {
        InputStream input = new FileInputStream(filePath);
        Yaml yaml = new Yaml();
        Map<String, Object> yamlData = yaml.load(input);

        if (yamlData != null) {
            Map<String, Object> valfData = (Map<String, Object>) yamlData.get("valf_bloklari");

            for (Map.Entry<String, Object> kaplinEntry : valfData.entrySet()) {
                String kaplinKey = kaplinEntry.getKey();
                Map<String, Object> partsData = (Map<String, Object>) kaplinEntry.getValue();
                Map<String, Object> parts = (Map<String, Object>) partsData.get("parts");

                LinkedList<String> partDetailsList = new LinkedList<>();

                for (Map.Entry<String, Object> partEntry : parts.entrySet()) {
                    Map<String, String> partDetails = (Map<String, String>) partEntry.getValue();

                    String malzemeKodu = partDetails.get("malzemeKodu");
                    String malzemeAdi = partDetails.get("malzemeAdi");
                    String malzemeAdet = partDetails.get("malzemeAdet");

                    String combinedDetails = malzemeKodu + ";" + malzemeAdi + ";" + malzemeAdet;

                    partDetailsList.add(combinedDetails);
                }

                Launcher.getDataManipulator().classicParcaValfBloklari.put(kaplinKey, partDetailsList);
            }
        }
    }

    public void loadClassicParcaSogutma(String filePath) throws FileNotFoundException {
        InputStream input = new FileInputStream(filePath);
        Yaml yaml = new Yaml();
        Map<String, Object> yamlData = yaml.load(input);

        if (yamlData != null) {
            Map<String, Object> valfData = (Map<String, Object>) yamlData.get("sogutma");

            for (Map.Entry<String, Object> kaplinEntry : valfData.entrySet()) {
                String kaplinKey = kaplinEntry.getKey();
                Map<String, Object> partsData = (Map<String, Object>) kaplinEntry.getValue();
                Map<String, Object> parts = (Map<String, Object>) partsData.get("parts");

                LinkedList<String> partDetailsList = new LinkedList<>();

                for (Map.Entry<String, Object> partEntry : parts.entrySet()) {
                    Map<String, String> partDetails = (Map<String, String>) partEntry.getValue();

                    String malzemeKodu = partDetails.get("malzemeKodu");
                    String malzemeAdi = partDetails.get("malzemeAdi");
                    String malzemeAdet = partDetails.get("malzemeAdet");

                    String combinedDetails = malzemeKodu + ";" + malzemeAdi + ";" + malzemeAdet;

                    partDetailsList.add(combinedDetails);
                }

                Launcher.getDataManipulator().classicParcaSogutma.put(kaplinKey, partDetailsList);
            }
        }
    }

    public void loadClassicParcaBasincSalteri(String filePath) throws FileNotFoundException {
        InputStream input = new FileInputStream(filePath);
        Yaml yaml = new Yaml();
        Map<String, Object> yamlData = yaml.load(input);

        if (yamlData != null) {
            Map<String, Object> valfData = (Map<String, Object>) yamlData.get("basinc_salteri");

            for (Map.Entry<String, Object> kaplinEntry : valfData.entrySet()) {
                String kaplinKey = kaplinEntry.getKey();
                Map<String, Object> partsData = (Map<String, Object>) kaplinEntry.getValue();
                Map<String, Object> parts = (Map<String, Object>) partsData.get("parts");

                LinkedList<String> partDetailsList = new LinkedList<>();

                for (Map.Entry<String, Object> partEntry : parts.entrySet()) {
                    Map<String, String> partDetails = (Map<String, String>) partEntry.getValue();

                    String malzemeKodu = partDetails.get("malzemeKodu");
                    String malzemeAdi = partDetails.get("malzemeAdi");
                    String malzemeAdet = partDetails.get("malzemeAdet");

                    String combinedDetails = malzemeKodu + ";" + malzemeAdi + ";" + malzemeAdet;

                    partDetailsList.add(combinedDetails);
                }

                Launcher.getDataManipulator().classicParcaBasincSalteri.put(kaplinKey, partDetailsList);
            }
        }
    }

    public void loadClassicParcaDefault(String filePath) throws FileNotFoundException {
        InputStream input = new FileInputStream(filePath);
        Yaml yaml = new Yaml();
        Map<String, Object> yamlData = yaml.load(input);

        if (yamlData != null) {
            Map<String, Object> valfData = (Map<String, Object>) yamlData.get("standart_malzemeler");

            for (Map.Entry<String, Object> kaplinEntry : valfData.entrySet()) {
                String kaplinKey = kaplinEntry.getKey();
                Map<String, Object> partsData = (Map<String, Object>) kaplinEntry.getValue();
                Map<String, Object> parts = (Map<String, Object>) partsData.get("parts");

                LinkedList<String> partDetailsList = new LinkedList<>();

                for (Map.Entry<String, Object> partEntry : parts.entrySet()) {
                    Map<String, String> partDetails = (Map<String, String>) partEntry.getValue();

                    String malzemeKodu = partDetails.get("malzemeKodu");
                    String malzemeAdi = partDetails.get("malzemeAdi");
                    String malzemeAdet = partDetails.get("malzemeAdet");

                    String combinedDetails = malzemeKodu + ";" + malzemeAdi + ";" + malzemeAdet;

                    partDetailsList.add(combinedDetails);
                }

                Launcher.getDataManipulator().classicParcaDefault.put(kaplinKey, partDetailsList);
            }
        }
    }

    public void loadHidrosParcaMotor380(String filePath) throws FileNotFoundException {
        InputStream input = new FileInputStream(filePath);
        Yaml yaml = new Yaml();
        Map<String, Object> yamlData = yaml.load(input);

        if (yamlData != null) {
            Map<String, Object> valfData = (Map<String, Object>) yamlData.get("motor_380");

            for (Map.Entry<String, Object> kaplinEntry : valfData.entrySet()) {
                String kaplinKey = kaplinEntry.getKey();
                Map<String, Object> partsData = (Map<String, Object>) kaplinEntry.getValue();
                Map<String, Object> parts = (Map<String, Object>) partsData.get("parts");

                LinkedList<String> partDetailsList = new LinkedList<>();

                for (Map.Entry<String, Object> partEntry : parts.entrySet()) {
                    Map<String, String> partDetails = (Map<String, String>) partEntry.getValue();

                    String malzemeKodu = partDetails.get("malzemeKodu");
                    String malzemeAdi = partDetails.get("malzemeAdi");
                    String malzemeAdet = partDetails.get("malzemeAdet");

                    String combinedDetails = malzemeKodu + ";" + malzemeAdi + ";" + malzemeAdet;

                    partDetailsList.add(combinedDetails);
                }

                Launcher.getDataManipulator().powerPackHidrosParcaMotor380.put(kaplinKey, partDetailsList);
            }
        }
    }

    public void loadHidrosParcaMotor220(String filePath) throws FileNotFoundException {
        InputStream input = new FileInputStream(filePath);
        Yaml yaml = new Yaml();
        Map<String, Object> yamlData = yaml.load(input);

        if (yamlData != null) {
            Map<String, Object> valfData = (Map<String, Object>) yamlData.get("motor_220");

            for (Map.Entry<String, Object> kaplinEntry : valfData.entrySet()) {
                String kaplinKey = kaplinEntry.getKey();
                Map<String, Object> partsData = (Map<String, Object>) kaplinEntry.getValue();
                Map<String, Object> parts = (Map<String, Object>) partsData.get("parts");

                LinkedList<String> partDetailsList = new LinkedList<>();

                for (Map.Entry<String, Object> partEntry : parts.entrySet()) {
                    Map<String, String> partDetails = (Map<String, String>) partEntry.getValue();

                    String malzemeKodu = partDetails.get("malzemeKodu");
                    String malzemeAdi = partDetails.get("malzemeAdi");
                    String malzemeAdet = partDetails.get("malzemeAdet");

                    String combinedDetails = malzemeKodu + ";" + malzemeAdi + ";" + malzemeAdet;

                    partDetailsList.add(combinedDetails);
                }

                Launcher.getDataManipulator().powerPackHidrosParcaMotor220.put(kaplinKey, partDetailsList);
            }
        }
    }

    public void loadHidrosParcaPompa(String filePath) throws FileNotFoundException {
        InputStream input = new FileInputStream(filePath);
        Yaml yaml = new Yaml();
        Map<String, Object> yamlData = yaml.load(input);

        if (yamlData != null) {
            Map<String, Object> valfData = (Map<String, Object>) yamlData.get("pompa");

            for (Map.Entry<String, Object> kaplinEntry : valfData.entrySet()) {
                String kaplinKey = kaplinEntry.getKey();
                Map<String, Object> partsData = (Map<String, Object>) kaplinEntry.getValue();
                Map<String, Object> parts = (Map<String, Object>) partsData.get("parts");

                LinkedList<String> partDetailsList = new LinkedList<>();

                for (Map.Entry<String, Object> partEntry : parts.entrySet()) {
                    Map<String, String> partDetails = (Map<String, String>) partEntry.getValue();

                    String malzemeKodu = partDetails.get("malzemeKodu");
                    String malzemeAdi = partDetails.get("malzemeAdi");
                    String malzemeAdet = partDetails.get("malzemeAdet");

                    String combinedDetails = malzemeKodu + ";" + malzemeAdi + ";" + malzemeAdet;

                    partDetailsList.add(combinedDetails);
                }

                Launcher.getDataManipulator().powerPackHidrosParcaPompa.put(kaplinKey, partDetailsList);
            }
        }
    }

    public void loadHidrosParcaTankDikey(String filePath) throws FileNotFoundException {
        InputStream input = new FileInputStream(filePath);
        Yaml yaml = new Yaml();
        Map<String, Object> yamlData = yaml.load(input);

        if (yamlData != null) {
            Map<String, Object> valfData = (Map<String, Object>) yamlData.get("tank_dikey");

            for (Map.Entry<String, Object> kaplinEntry : valfData.entrySet()) {
                String kaplinKey = kaplinEntry.getKey();
                Map<String, Object> partsData = (Map<String, Object>) kaplinEntry.getValue();
                Map<String, Object> parts = (Map<String, Object>) partsData.get("parts");

                LinkedList<String> partDetailsList = new LinkedList<>();

                for (Map.Entry<String, Object> partEntry : parts.entrySet()) {
                    Map<String, String> partDetails = (Map<String, String>) partEntry.getValue();

                    String malzemeKodu = partDetails.get("malzemeKodu");
                    String malzemeAdi = partDetails.get("malzemeAdi");
                    String malzemeAdet = partDetails.get("malzemeAdet");

                    String combinedDetails = malzemeKodu + ";" + malzemeAdi + ";" + malzemeAdet;

                    partDetailsList.add(combinedDetails);
                }

                Launcher.getDataManipulator().powerPackHidrosParcaTankDikey.put(kaplinKey, partDetailsList);
            }
        }
    }

    public void loadHidrosParcaTankYatay(String filePath) throws FileNotFoundException {
        InputStream input = new FileInputStream(filePath);
        Yaml yaml = new Yaml();
        Map<String, Object> yamlData = yaml.load(input);

        if (yamlData != null) {
            Map<String, Object> valfData = (Map<String, Object>) yamlData.get("tank_yatay");

            for (Map.Entry<String, Object> kaplinEntry : valfData.entrySet()) {
                String kaplinKey = kaplinEntry.getKey();
                Map<String, Object> partsData = (Map<String, Object>) kaplinEntry.getValue();
                Map<String, Object> parts = (Map<String, Object>) partsData.get("parts");

                LinkedList<String> partDetailsList = new LinkedList<>();

                for (Map.Entry<String, Object> partEntry : parts.entrySet()) {
                    Map<String, String> partDetails = (Map<String, String>) partEntry.getValue();

                    String malzemeKodu = partDetails.get("malzemeKodu");
                    String malzemeAdi = partDetails.get("malzemeAdi");
                    String malzemeAdet = partDetails.get("malzemeAdet");

                    String combinedDetails = malzemeKodu + ";" + malzemeAdi + ";" + malzemeAdet;

                    partDetailsList.add(combinedDetails);
                }

                Launcher.getDataManipulator().powerPackHidrosParcaTankYatay.put(kaplinKey, partDetailsList);
            }
        }
    }

    public void loadHidrosParcaESPGenel(String filePath) throws FileNotFoundException {
        InputStream input = new FileInputStream(filePath);
        Yaml yaml = new Yaml();
        Map<String, Object> yamlData = yaml.load(input);

        if (yamlData != null) {
            Map<String, Object> valfData = (Map<String, Object>) yamlData.get("esp_default");

            for (Map.Entry<String, Object> kaplinEntry : valfData.entrySet()) {
                String kaplinKey = kaplinEntry.getKey();
                Map<String, Object> partsData = (Map<String, Object>) kaplinEntry.getValue();
                Map<String, Object> parts = (Map<String, Object>) partsData.get("parts");

                LinkedList<String> partDetailsList = new LinkedList<>();

                for (Map.Entry<String, Object> partEntry : parts.entrySet()) {
                    Map<String, String> partDetails = (Map<String, String>) partEntry.getValue();

                    String malzemeKodu = partDetails.get("malzemeKodu");
                    String malzemeAdi = partDetails.get("malzemeAdi");
                    String malzemeAdet = partDetails.get("malzemeAdet");

                    String combinedDetails = malzemeKodu + ";" + malzemeAdi + ";" + malzemeAdet;

                    partDetailsList.add(combinedDetails);
                }

                Launcher.getDataManipulator().powerPackHidrosParcaESPGenel.put(kaplinKey, partDetailsList);
            }
        }
    }

    public void loadHidrosParcaESPDikeyCift(String filePath) throws FileNotFoundException {
        InputStream input = new FileInputStream(filePath);
        Yaml yaml = new Yaml();
        Map<String, Object> yamlData = yaml.load(input);

        if (yamlData != null) {
            Map<String, Object> valfData = (Map<String, Object>) yamlData.get("esp_custom");

            for (Map.Entry<String, Object> kaplinEntry : valfData.entrySet()) {
                String kaplinKey = kaplinEntry.getKey();
                Map<String, Object> partsData = (Map<String, Object>) kaplinEntry.getValue();
                Map<String, Object> parts = (Map<String, Object>) partsData.get("parts");

                LinkedList<String> partDetailsList = new LinkedList<>();

                for (Map.Entry<String, Object> partEntry : parts.entrySet()) {
                    Map<String, String> partDetails = (Map<String, String>) partEntry.getValue();

                    String malzemeKodu = partDetails.get("malzemeKodu");
                    String malzemeAdi = partDetails.get("malzemeAdi");
                    String malzemeAdet = partDetails.get("malzemeAdet");

                    String combinedDetails = malzemeKodu + ";" + malzemeAdi + ";" + malzemeAdet;

                    partDetailsList.add(combinedDetails);
                }

                Launcher.getDataManipulator().powerPackHidrosParcaESPCiftHiz.put(kaplinKey, partDetailsList);
            }
        }
    }

    public void loadHidrosParcaDevirmeli(String filePath) throws FileNotFoundException {
        InputStream input = new FileInputStream(filePath);
        Yaml yaml = new Yaml();
        Map<String, Object> yamlData = yaml.load(input);

        if (yamlData != null) {
            Map<String, Object> valfData = (Map<String, Object>) yamlData.get("devirmeli_yuruyus");

            for (Map.Entry<String, Object> kaplinEntry : valfData.entrySet()) {
                String kaplinKey = kaplinEntry.getKey();
                Map<String, Object> partsData = (Map<String, Object>) kaplinEntry.getValue();
                Map<String, Object> parts = (Map<String, Object>) partsData.get("parts");

                LinkedList<String> partDetailsList = new LinkedList<>();

                for (Map.Entry<String, Object> partEntry : parts.entrySet()) {
                    Map<String, String> partDetails = (Map<String, String>) partEntry.getValue();

                    String malzemeKodu = partDetails.get("malzemeKodu");
                    String malzemeAdi = partDetails.get("malzemeAdi");
                    String malzemeAdet = partDetails.get("malzemeAdet");

                    String combinedDetails = malzemeKodu + ";" + malzemeAdi + ";" + malzemeAdet;

                    partDetailsList.add(combinedDetails);
                }

                Launcher.getDataManipulator().powerPackHidrosParcaDevirmeli.put(kaplinKey, partDetailsList);
            }
        }
    }

    public void loadHidrosParcaStandart(String filePath) throws FileNotFoundException {
        InputStream input = new FileInputStream(filePath);
        Yaml yaml = new Yaml();
        Map<String, Object> yamlData = yaml.load(input);

        if (yamlData != null) {
            Map<String, Object> valfData = (Map<String, Object>) yamlData.get("standart_malzemeler");

            for (Map.Entry<String, Object> kaplinEntry : valfData.entrySet()) {
                String kaplinKey = kaplinEntry.getKey();
                Map<String, Object> partsData = (Map<String, Object>) kaplinEntry.getValue();
                Map<String, Object> parts = (Map<String, Object>) partsData.get("parts");

                LinkedList<String> partDetailsList = new LinkedList<>();

                for (Map.Entry<String, Object> partEntry : parts.entrySet()) {
                    Map<String, String> partDetails = (Map<String, String>) partEntry.getValue();

                    String malzemeKodu = partDetails.get("malzemeKodu");
                    String malzemeAdi = partDetails.get("malzemeAdi");
                    String malzemeAdet = partDetails.get("malzemeAdet");

                    String combinedDetails = malzemeKodu + ";" + malzemeAdi + ";" + malzemeAdet;

                    partDetailsList.add(combinedDetails);
                }

                Launcher.getDataManipulator().powerPackHidrosParcaDefault.put(kaplinKey, partDetailsList);
            }
        }
    }

    public void loadHidrosParcaOzelYatay(String filePath) throws FileNotFoundException {
        InputStream input = new FileInputStream(filePath);
        Yaml yaml = new Yaml();
        Map<String, Object> yamlData = yaml.load(input);

        if (yamlData != null) {
            Map<String, Object> valfData = (Map<String, Object>) yamlData.get("ozel_yatay");

            for (Map.Entry<String, Object> kaplinEntry : valfData.entrySet()) {
                String kaplinKey = kaplinEntry.getKey();
                Map<String, Object> partsData = (Map<String, Object>) kaplinEntry.getValue();
                Map<String, Object> parts = (Map<String, Object>) partsData.get("parts");

                LinkedList<String> partDetailsList = new LinkedList<>();

                for (Map.Entry<String, Object> partEntry : parts.entrySet()) {
                    Map<String, String> partDetails = (Map<String, String>) partEntry.getValue();

                    String malzemeKodu = partDetails.get("malzemeKodu");
                    String malzemeAdi = partDetails.get("malzemeAdi");
                    String malzemeAdet = partDetails.get("malzemeAdet");

                    String combinedDetails = malzemeKodu + ";" + malzemeAdi + ";" + malzemeAdet;

                    partDetailsList.add(combinedDetails);
                }

                Launcher.getDataManipulator().powerPackHidrosParcaOzelYatayGenel.put(kaplinKey, partDetailsList);
            }
        }
    }
}