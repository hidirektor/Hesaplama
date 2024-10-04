package me.t3sl4.hydraulic.Utils.Database.File;

import me.t3sl4.hydraulic.Launcher;
import me.t3sl4.hydraulic.Utils.Database.File.Excel.ExcelUtil;
import me.t3sl4.hydraulic.Utils.Database.File.JSON.JSONUtil;
import me.t3sl4.hydraulic.Utils.Database.File.Yaml.YamlUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class FileUtil {

    public static void firstLaunch() {
        changeDataStoragePath();
        createDirectories();
        initializeTokens();
    }

    public static void systemSetup() {
        fileCopy("/assets/data/programDatabase/Hidrolik.xlsx", Launcher.excelDBPath);
        fileCopy("/assets/data/programDatabase/general.json", Launcher.generalDBPath);
        fileCopy("/assets/data/programDatabase/cabins.json", Launcher.cabinetesDBPath);
        fileCopy("/assets/data/programDatabase/classic_combo.yml", Launcher.classicComboDBPath);
        fileCopy("/assets/data/programDatabase/powerpack_combo.yml", Launcher.powerPackComboDBPath);
        fileCopy("/assets/data/programDatabase/classic_parts.yml", Launcher.classicPartsDBPath);
        fileCopy("/assets/data/programDatabase/powerpack_parts.yml", Launcher.powerPackPartsDBPath);
        JSONUtil.loadJSONData();
        ExcelUtil.excelDataRead();
        new YamlUtil(Launcher.classicComboDBPath, Launcher.powerPackComboDBPath, Launcher.classicPartsDBPath, Launcher.powerPackPartsDBPath);
    }

    public static String getOperatingSystem() {
        return System.getProperty("os.name");
    }

    public static void createDirectories() {
        List<String> directories = Arrays.asList(
                Launcher.mainPath,
                Launcher.dataFileLocalPath,
                Launcher.profilePhotoLocalPath,
                Launcher.pdfFileLocalPath,
                Launcher.excelFileLocalPath
        );

        directories.forEach(FileUtil::createDirectory);
    }

    private static void createDirectory(String path) {
        File file = new File(path);
        if (!file.exists() && file.mkdirs()) {
            System.out.println("Directory created: " + path);
        }
    }

    public static void changeDataStoragePath() {
        String userHome = System.getProperty("user.name");
        String basePath = getOperatingSystem().contains("Windows") ? "C:/Users/" + userHome + "/" : "/Users/" + userHome + "/";
        Launcher.mainPath = basePath + "OnderGrup/";
        Launcher.profilePhotoLocalPath = Launcher.mainPath + "userData/";
        Launcher.dataFileLocalPath = Launcher.mainPath + "data/";

        Launcher.tokenPath = Launcher.profilePhotoLocalPath + "auth.txt";

        Launcher.pdfFileLocalPath = Launcher.dataFileLocalPath + "schematicFiles/";
        Launcher.excelFileLocalPath = Launcher.dataFileLocalPath + "excelFiles/";

        Launcher.excelDBPath = Launcher.dataFileLocalPath + "Hidrolik.xlsx";
        Launcher.generalDBPath = Launcher.dataFileLocalPath + "general.json";
        Launcher.cabinetesDBPath = Launcher.dataFileLocalPath + "cabins.json";
        Launcher.classicComboDBPath = Launcher.dataFileLocalPath + "classic_combo.yml";
        Launcher.powerPackComboDBPath = Launcher.dataFileLocalPath + "powerpack_combo.yml";
        Launcher.classicPartsDBPath = Launcher.dataFileLocalPath + "classic_parts.yml";
        Launcher.powerPackPartsDBPath = Launcher.dataFileLocalPath + "powerpack_parts.yml";
    }

    public static void fileCopy(String resourcePath, String targetPath) {
        try (InputStream inputStream = Launcher.class.getResourceAsStream(resourcePath)) {
            if (inputStream != null) {
                Files.copy(inputStream, Paths.get(targetPath), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("File copied to: " + targetPath);
            } else {
                System.err.println("Resource not found: " + resourcePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void initializeTokens() {
        File authFile = new File(Launcher.tokenPath);
        if (!authFile.exists()) return;

        try (Scanner scanner = new Scanner(authFile)) {
            Map<String, String> tokenMap = new HashMap<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.contains(": ")) {
                    String[] parts = line.split(": ");
                    tokenMap.put(parts[0].trim(), parts[1].trim());
                }
            }
            Launcher.userName = tokenMap.getOrDefault("userName", "");
            Launcher.userID = tokenMap.getOrDefault("userID", "");
            Launcher.accessToken = tokenMap.getOrDefault("AccessToken", "");
            Launcher.refreshToken = tokenMap.getOrDefault("RefreshToken", "");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}