package me.t3sl4.hydraulic.Utils.File;

import me.t3sl4.hydraulic.Launcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class GeneralFileSystem {

    public static void firstLaunch() {
        changeDataStoragePath();
        createDirectories();
        initializeTokens();
    }

    public static void systemSetup() {
        fileCopy("/assets/data/Hidrolik.xlsx", Launcher.excelDBPath);
        fileCopy("/assets/data/general.json", Launcher.generalDBPath);
        fileCopy("/assets/data/kabinler.json", Launcher.cabinetesDBPath);
        fileCopy("/assets/data/klasik.json", Launcher.classicDBPath);
        fileCopy("/assets/data/powerpack.json", Launcher.powerPackDBPath);
        JSONDataUtil.loadJSONData();
        ExcelDataReadUtil.excelDataRead();
    }

    public static String getOperatingSystem() {
        return System.getProperty("os.name");
    }

    public static void createDirectories() {
        List<String> directories = Arrays.asList(
                Launcher.mainPath,
                Launcher.profilePhotoLocalPath,
                Launcher.pdfFileLocalPath,
                Launcher.excelFileLocalPath,
                Launcher.dataFileLocalPath
        );

        directories.forEach(GeneralFileSystem::createDirectory);
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
        Launcher.tokenPath = Launcher.mainPath + "auth.txt";
        Launcher.profilePhotoLocalPath = Launcher.mainPath + "profilePhoto/";
        Launcher.pdfFileLocalPath = Launcher.mainPath + "hydraulicUnits/";
        Launcher.excelFileLocalPath = Launcher.mainPath + "partList/";
        Launcher.dataFileLocalPath = Launcher.mainPath + "assets/data/";
        Launcher.excelDBPath = Launcher.mainPath + "assets/data/Hidrolik.xlsx";
        Launcher.generalDBPath = Launcher.mainPath + "assets/data/general.json";
        Launcher.cabinetesDBPath = Launcher.mainPath + "assets/data/kabinler.json";
        Launcher.classicDBPath = Launcher.mainPath + "assets/data/klasik.json";
        Launcher.powerPackDBPath = Launcher.mainPath + "assets/data/powerpack.json";
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