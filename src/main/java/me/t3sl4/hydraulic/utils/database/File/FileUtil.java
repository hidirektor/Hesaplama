package me.t3sl4.hydraulic.utils.database.File;

import me.t3sl4.hydraulic.utils.database.File.JSON.JSONUtil;
import me.t3sl4.hydraulic.utils.database.File.Yaml.YamlUtil;
import me.t3sl4.hydraulic.utils.general.SystemVariables;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

public class FileUtil {
    public static void setupFileSystem() {
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

            createDirectory(SystemVariables.excelFileLocalPath);
            createDirectory(SystemVariables.pdfFileLocalPath);
            createFile(SystemVariables.localHydraulicStatsPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONUtil.loadJSONData();
        new YamlUtil(SystemVariables.classicComboDBPath, SystemVariables.powerPackComboDBPath, SystemVariables.classicPartsDBPath, SystemVariables.powerPackPartsHidrosDBPath, SystemVariables.powerPackPartsIthalDBPath, SystemVariables.schematicTextsDBPath);
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
        InputStream resourceAsStream = FileUtil.class.getResourceAsStream(sourcePath);
        if (resourceAsStream == null) {
            throw new FileNotFoundException("Kaynak bulunamadı: " + sourcePath);
        }

        Path destination = Paths.get(destPath);
        Files.copy(resourceAsStream, destination, StandardCopyOption.REPLACE_EXISTING);
        resourceAsStream.close();
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
}