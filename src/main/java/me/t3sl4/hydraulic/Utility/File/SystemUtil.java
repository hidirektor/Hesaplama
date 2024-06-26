package me.t3sl4.hydraulic.Utility.File;

import me.t3sl4.hydraulic.Launcher;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class SystemUtil {

    public static void systemSetup() {
        changeDataStoragePath();
        createDirectories();
        fileCopy();

        ExcelUtil.excelDataRead();
    }

    public static String getOperatingSystem() {
        return System.getProperty("os.name");
    }

    public static boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    public static void createDirectories() {
        createMainDirectory();
        createDirectory(Launcher.profilePhotoLocalPath);
        createDirectory(Launcher.pdfFileLocalPath);
        createDirectory(Launcher.excelFileLocalPath);
        createDirectory(Launcher.dataFileLocalPath);
        createDirectory(Launcher.loginFilePath);
    }

    private static void createDirectory(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static void createMainDirectory() {
        File mainDir = new File(Launcher.mainPath);
        if (!mainDir.exists()) {
            if (mainDir.mkdirs()) {
                System.out.println("Main directory created: " + Launcher.mainPath);
            } else {
                System.err.println("Failed to create main directory: " + Launcher.mainPath);
                System.err.println("Absolute path: " + mainDir.getAbsolutePath());
                System.err.println("Writable: " + mainDir.canWrite());
                System.err.println("Readable: " + mainDir.canRead());
                System.err.println("Executable: " + mainDir.canExecute());
            }
        }
    }

    public static void changeDataStoragePath() {
        if (getOperatingSystem().contains("Windows")) {
            Launcher.mainPath = "C:/Users/" + System.getProperty("user.name") + "/OnderGrup/";
        } else {
            Launcher.mainPath = "/Users/" + System.getProperty("user.name") + "/OnderGrup/";
        }

        Launcher.profilePhotoLocalPath = Launcher.mainPath + "profilePhoto/";
        Launcher.pdfFileLocalPath = Launcher.mainPath + "hydraulicUnits/";
        Launcher.excelFileLocalPath = Launcher.mainPath + "partList/";
        Launcher.dataFileLocalPath = Launcher.mainPath + "assets/data/";
        Launcher.loginFilePath = Launcher.mainPath + "login/loginInfo.txt";
        Launcher.excelDBPath = Launcher.mainPath + "assets/data/Hidrolik.xlsx";
    }

    public static void fileCopy() {
        Path targetPath = Paths.get(Launcher.excelDBPath);
        System.out.println("Target path: " + targetPath.toString());

        try (InputStream inputStream = Launcher.class.getResourceAsStream("/assets/data/Hidrolik.xlsx")) {
            if (inputStream != null) {
                Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Hidrolik.xlsx kopyalandı: " + targetPath);
            } else {
                System.err.println("Hidrolik.xlsx dosyası bulunamadı.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteRememberedFile() {
        File file = new File(Launcher.loginFilePath);
        if (file.exists()) {
            if(file.delete()) {
                System.out.println(file.getName() + " deleted :))");
            } else {
                System.out.println(file.getName() + " not deleted :((");
            }
        }
    }
}
