package me.t3sl4.hydraulic.Utility.File;

import me.t3sl4.hydraulic.Launcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SystemUtil {

    public static void firstLaunch() {
        changeDataStoragePath();
        createDirectories();
        initializeTokens();
    }

    public static void systemSetup() {
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

        Launcher.tokenPath = Launcher.mainPath + "auth.txt";
        Launcher.profilePhotoLocalPath = Launcher.mainPath + "profilePhoto/";
        Launcher.pdfFileLocalPath = Launcher.mainPath + "hydraulicUnits/";
        Launcher.excelFileLocalPath = Launcher.mainPath + "partList/";
        Launcher.dataFileLocalPath = Launcher.mainPath + "assets/data/";
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

    public static void initializeTokens() {
        try {
            File authFile = new File(Launcher.tokenPath);
            if(authFile.exists()) {
                Scanner myReader = new Scanner(authFile);

                List<String> lines = new ArrayList<String>();

                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    lines.add(data);
                    System.out.println(data);
                }
                myReader.close();

                for(String line : lines) {
                    if(line.contains("userName: ")) {
                        Launcher.userName = line;
                    } else if(line.contains("userID: ")) {
                        Launcher.userID = line;
                    } else if(line.contains("AccessToken: ")) {
                        Launcher.accessToken = line;
                    } else if(line.contains("RefreshToken: ")) {
                        Launcher.refreshToken = line;
                    }
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
