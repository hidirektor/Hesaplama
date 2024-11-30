package me.t3sl4.hydraulic.utils.database.File;

import javafx.application.Platform;
import me.t3sl4.hydraulic.utils.database.File.JSON.JSONUtil;
import me.t3sl4.hydraulic.utils.database.File.Yaml.YamlUtil;
import me.t3sl4.hydraulic.utils.general.SystemVariables;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class FileUtil {
    public static void criticalFileSystem() {
        // İşletim sistemine göre dosya yollarını ayarla
        String userHome = System.getProperty("user.name");
        String os = System.getProperty("os.name").toLowerCase();
        String basePath;

        if (os.contains("win")) {
            basePath = "C:/Users/" + userHome + "/";
        } else {
            basePath = "/Users/" + userHome + "/";
        }

        // Dosya yollarını belirle
        SystemVariables.mainPath = basePath + "OnderGrup/";
        SystemVariables.profilePhotoLocalPath = SystemVariables.mainPath + "userData/";
        SystemVariables.dataFileLocalPath = SystemVariables.mainPath + "data/";

        SystemVariables.tokenPath = SystemVariables.profilePhotoLocalPath + "auth.txt";
        SystemVariables.licensePath = SystemVariables.profilePhotoLocalPath + "license.txt";

        SystemVariables.pdfFileLocalPath = SystemVariables.profilePhotoLocalPath + "HydraulicUnits/schematicFiles/";
        SystemVariables.excelFileLocalPath = SystemVariables.profilePhotoLocalPath + "HydraulicUnits/excelFiles/";
        SystemVariables.localHydraulicStatsPath = SystemVariables.profilePhotoLocalPath + "HydraulicUnits/local_units.yml";

        SystemVariables.generalDBPath = SystemVariables.dataFileLocalPath + "general.json";
        SystemVariables.cabinsDBPath = SystemVariables.dataFileLocalPath + "cabins.json";
        SystemVariables.classicComboDBPath = SystemVariables.dataFileLocalPath + "classic_combo.yml";
        SystemVariables.powerPackComboDBPath = SystemVariables.dataFileLocalPath + "powerpack_combo.yml";
        SystemVariables.classicPartsDBPath = SystemVariables.dataFileLocalPath + "classic_parts.yml";
        SystemVariables.powerPackPartsHidrosDBPath = SystemVariables.dataFileLocalPath + "powerpack_parts_hidros.yml";
        SystemVariables.powerPackPartsIthalDBPath = SystemVariables.dataFileLocalPath + "powerpack_parts_ithal.yml";
        SystemVariables.schematicTextsDBPath = SystemVariables.dataFileLocalPath + "schematic_texts.yml";
        SystemVariables.partOriginsClassicDBPath = SystemVariables.dataFileLocalPath + "part_origins_classic.yml";
        SystemVariables.partOriginsPowerPackDBPath = SystemVariables.dataFileLocalPath + "part_origins_powerpack.yml";

        try {
            createDirectory(SystemVariables.mainPath);

            createDirectory(SystemVariables.dataFileLocalPath);
            createDirectory(SystemVariables.profilePhotoLocalPath);

            createFile(SystemVariables.tokenPath);
            createFile(SystemVariables.licensePath);

            fileCopy("/assets/data/programDatabase/general.json", SystemVariables.generalDBPath);
            fileCopy("/assets/data/programDatabase/cabins.json", SystemVariables.cabinsDBPath);
            fileCopy("/assets/data/programDatabase/classic_combo.yml", SystemVariables.classicComboDBPath);
            fileCopy("/assets/data/programDatabase/powerpack_combo.yml", SystemVariables.powerPackComboDBPath);
            fileCopy("/assets/data/programDatabase/classic_parts.yml", SystemVariables.classicPartsDBPath);
            fileCopy("/assets/data/programDatabase/powerpack_parts_hidros.yml", SystemVariables.powerPackPartsHidrosDBPath);
            fileCopy("/assets/data/programDatabase/powerpack_parts_ithal.yml", SystemVariables.powerPackPartsIthalDBPath);
            fileCopy("/assets/data/programDatabase/schematic_texts.yml", SystemVariables.schematicTextsDBPath);
            fileCopy("/assets/data/programDatabase/part_origins_classic.yml", SystemVariables.partOriginsClassicDBPath);
            fileCopy("/assets/data/programDatabase/part_origins_powerpack.yml", SystemVariables.partOriginsPowerPackDBPath);

            createDirectory(SystemVariables.excelFileLocalPath);
            createDirectory(SystemVariables.pdfFileLocalPath);
            createFile(SystemVariables.localHydraulicStatsPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setupLocalData() {
        JSONUtil.loadJSONData();
        new YamlUtil(SystemVariables.classicComboDBPath, SystemVariables.powerPackComboDBPath, SystemVariables.classicPartsDBPath, SystemVariables.powerPackPartsHidrosDBPath, SystemVariables.powerPackPartsIthalDBPath, SystemVariables.schematicTextsDBPath);
        new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> FileUtil.partRenameAutomatically("klasik"));
            Platform.runLater(() -> FileUtil.partRenameAutomatically("powerpack"));
        }).start();
    }

    public static void partRenameAutomatically(String partType) {
        Set<String> modifiedFiles = new HashSet<>();

        try {
            Map<String, Object> partOrigins;

            String[] TARGET_FILES;

            if(partType.equals("klasik")) {
                partOrigins = loadYamlFile(SystemVariables.partOriginsClassicDBPath);

                TARGET_FILES = new String[]{
                        SystemVariables.classicPartsDBPath
                };
            } else {
                partOrigins = loadYamlFile(SystemVariables.partOriginsPowerPackDBPath);

                TARGET_FILES = new String[]{
                        SystemVariables.powerPackPartsHidrosDBPath,
                        SystemVariables.powerPackPartsIthalDBPath
                };
            }

            for (String targetFilePath : TARGET_FILES) {
                Map<String, Object> targetData = loadYamlFile(targetFilePath);

                boolean isModifiedByName = updateParts(targetData, partOrigins, true);

                boolean isModifiedByCode = updateParts(targetData, partOrigins, false);

                if (isModifiedByName || isModifiedByCode) {
                    saveYamlFile(targetFilePath, targetData);
                    modifiedFiles.add(targetFilePath);
                }
            }

            System.out.println("Güncelleme tamamlandı.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Dosya işlemleri sırasında bir hata oluştu.");
        }
    }

    private static void createDirectory(String path) throws IOException {
        Path dirPath = Paths.get(path);
        if (Files.notExists(dirPath)) {
            Files.createDirectories(dirPath);
        }
    }

    public static void createFile(String path) throws IOException {
        Path filePath = Paths.get(path);
        if (Files.notExists(filePath)) {
            Files.createFile(filePath);
        }
    }

    private static void fileCopy(String sourcePath, String destPath) throws IOException {
        File destinationFile = new File(destPath);

        if (!destinationFile.exists()) {
            InputStream resourceAsStream = FileUtil.class.getResourceAsStream(sourcePath);

            if (resourceAsStream == null) {
                throw new FileNotFoundException("Kaynak bulunamadı: " + sourcePath);
            }

            Path destination = Paths.get(destPath);
            Files.copy(resourceAsStream, destination, StandardCopyOption.REPLACE_EXISTING);
            resourceAsStream.close();
        } else {
            System.out.println("File already exists: " + destPath);
        }
    }

    private static void cleanDirectory(String path) throws IOException {
        Path dirPath = Paths.get(path);
        if (Files.exists(dirPath)) {
            try (Stream<Path> files = Files.list(dirPath)) {
                files.forEach(file -> {
                    try {
                        Files.deleteIfExists(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static boolean updateParts(Map<String, Object> targetData, Map<String, Object> partOrigins, boolean matchByName) {
        boolean isModified = false;

        // part_origins verisini al
        Map<String, Object> originsParts = (Map<String, Object>) partOrigins.get("part_origins");

        if (originsParts != null) {
            for (Object originPartKey : originsParts.keySet()) {
                Map<String, Object> originPart = (Map<String, Object>) originsParts.get(originPartKey);

                if (originPart != null) {
                    // parts içindeki malzemeKodu ve malzemeAdi'ni almak için iç içe yapıyı kontrol et
                    Map<String, Object> parts = (Map<String, Object>) originPart.get("parts");
                    if (parts != null && parts.containsKey("0")) {
                        Map<String, Object> part = (Map<String, Object>) parts.get("0");
                        String originCode = (String) part.get("malzemeKodu");
                        String originName = (String) part.get("malzemeAdi");

                        if (originName == null || originCode == null) {
                            System.err.println("Hata: part_origins içerisinde eksik veri var. originName veya originCode null.");
                            continue; // Eksik veri varsa bu kaydı atla
                        }

                        // TARGET dosyasındaki motor veya ozel_cift_valf yapılarını kontrol et
                        for (Object targetKey : targetData.keySet()) {
                            Map<String, Object> targetPart = (Map<String, Object>) targetData.get(targetKey);

                            if (targetPart != null) {
                                // motor formatındaki dosyalar için
                                System.out.println(targetPart);
                                for (Object outerKey : targetPart.keySet()) {
                                    Map<String, Object> outerPart = (Map<String, Object>) targetPart.get(outerKey);
                                    System.out.println("Dış Anahtar: " + outerKey + ", Değer: " + outerPart);

                                    if (outerPart != null && outerPart.containsKey("parts")) {
                                        Map<String, Object> targetParts = (Map<String, Object>) outerPart.get("parts");

                                        // "parts" içindeki her bir sayı key'ini kontrol et
                                        for (Object innerKey : targetParts.keySet()) {
                                            Map<String, Object> currentPart = (Map<String, Object>) targetParts.get(innerKey);
                                            System.out.println("parts içindeki Anahtar: " + innerKey + ", Değer: " + currentPart);

                                            if (currentPart != null && currentPart.containsKey("malzemeKodu") && currentPart.containsKey("malzemeAdi")) {
                                                String targetName = (String) currentPart.get("malzemeAdi");
                                                String targetCode = (String) currentPart.get("malzemeKodu");

                                                System.out.println("Hedef İsim: " + targetName);
                                                System.out.println("Hedef Kod: " + targetCode);

                                                if (targetName == null || targetCode == null) {
                                                    System.err.println("Hata: Hedef dosyada eksik veri var. targetName veya targetCode null.");
                                                    continue; // Eksik veri varsa bu kaydı atla
                                                }

                                                // Güncelleme mantığı
                                                if (matchByName && originName.equals(targetName) && !originCode.equals(targetCode)) {
                                                    currentPart.put("malzemeKodu", originCode); // malzemeKodu'nu güncelle
                                                    isModified = true;
                                                } else if (!matchByName && originCode.equals(targetCode) && !originName.equals(targetName)) {
                                                    currentPart.put("malzemeAdi", originName); // malzemeAdi'ni güncelle
                                                    isModified = true;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return isModified;
    }

    private static Map<String, Object> loadYamlFile(String filePath) throws IOException {
        Yaml yaml = new Yaml();
        try (InputStream inputStream = new FileInputStream(filePath)) {
            Map<String, Object> yamlData = yaml.load(inputStream);
            System.out.println("YAML Dosyası İçeriği:");
            System.out.println(yamlData);  // Bu şekilde dosya içeriğini yazdırabilirsiniz
            return yamlData;
        }
    }

    private static void saveYamlFile(String filePath, Map<String, Object> data) throws IOException {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setDefaultScalarStyle(DumperOptions.ScalarStyle.DOUBLE_QUOTED);
        options.setPrettyFlow(true);
        Yaml yaml = new Yaml(options);

        try (FileWriter writer = new FileWriter(filePath)) {
            yaml.dump(data, writer);
        }
    }
}