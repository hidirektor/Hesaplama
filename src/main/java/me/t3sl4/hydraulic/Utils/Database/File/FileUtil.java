package me.t3sl4.hydraulic.Utils.Database.File;

import javafx.concurrent.Task;
import me.t3sl4.hydraulic.Launcher;
import me.t3sl4.hydraulic.Utils.Database.File.JSON.JSONUtil;
import me.t3sl4.hydraulic.Utils.Database.File.Yaml.YamlUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

public class FileUtil {
    public static void setupFileSystemInBackground() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                setupFileSystem();
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
            }

            @Override
            protected void failed() {
                super.failed();

                Throwable exception = getException();
                exception.printStackTrace();
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true); // Uygulama kapandığında bu iş parçacığı da sonlandırılır
        thread.start();
    }

    public static void setupFileSystem() throws IOException {
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
        Launcher.mainPath = basePath + "OnderGrup/";
        Launcher.profilePhotoLocalPath = Launcher.mainPath + "userData/";
        Launcher.dataFileLocalPath = Launcher.mainPath + "data/";

        Launcher.tokenPath = Launcher.profilePhotoLocalPath + "auth.txt";

        Launcher.pdfFileLocalPath = Launcher.dataFileLocalPath + "schematicFiles/";
        Launcher.excelFileLocalPath = Launcher.dataFileLocalPath + "excelFiles/";

        Launcher.generalDBPath = Launcher.dataFileLocalPath + "general.json";
        Launcher.cabinetesDBPath = Launcher.dataFileLocalPath + "cabins.json";
        Launcher.classicComboDBPath = Launcher.dataFileLocalPath + "classic_combo.yml";
        Launcher.powerPackComboDBPath = Launcher.dataFileLocalPath + "powerpack_combo.yml";
        Launcher.classicPartsDBPath = Launcher.dataFileLocalPath + "classic_parts.yml";
        Launcher.powerPackPartsHidrosDBPath = Launcher.dataFileLocalPath + "powerpack_parts_hidros.yml";
        Launcher.powerPackPartsIthalDBPath = Launcher.dataFileLocalPath + "powerpack_parts_ithal.yml";

        // 1. OnderGrup klasörünü oluştur
        createDirectory(Launcher.mainPath);

        // 2. data ve userData klasörlerini oluştur
        createDirectory(Launcher.dataFileLocalPath);
        createDirectory(Launcher.profilePhotoLocalPath);

        // 3. userData içine boş auth.txt dosyasını oluştur
        createFile(Launcher.tokenPath);

        // 4. Belirtilen dosyaları data klasörüne kopyala
        fileCopy("/assets/data/programDatabase/general.json", Launcher.generalDBPath);
        fileCopy("/assets/data/programDatabase/cabins.json", Launcher.cabinetesDBPath);
        fileCopy("/assets/data/programDatabase/classic_combo.yml", Launcher.classicComboDBPath);
        fileCopy("/assets/data/programDatabase/powerpack_combo.yml", Launcher.powerPackComboDBPath);
        fileCopy("/assets/data/programDatabase/classic_parts.yml", Launcher.classicPartsDBPath);
        fileCopy("/assets/data/programDatabase/powerpack_parts_hidros.yml", Launcher.powerPackPartsHidrosDBPath);
        fileCopy("/assets/data/programDatabase/powerpack_parts_ithal.yml", Launcher.powerPackPartsIthalDBPath);

        // 5. data klasörünün içine excelFiles ve schematicFiles klasörlerini oluştur
        createDirectory(Launcher.excelFileLocalPath);
        createDirectory(Launcher.pdfFileLocalPath);

        // 6. Fazlalık dosyaları sil
        cleanDirectory(Launcher.excelFileLocalPath);
        cleanDirectory(Launcher.pdfFileLocalPath);

        // 7. Dataları yükle
        JSONUtil.loadJSONData();
        new YamlUtil(Launcher.classicComboDBPath, Launcher.powerPackComboDBPath, Launcher.classicPartsDBPath, Launcher.powerPackPartsHidrosDBPath, Launcher.powerPackPartsIthalDBPath);
    }

    // Yardımcı metotlar
    private static void createDirectory(String path) throws IOException {
        Path dirPath = Paths.get(path);
        if (Files.notExists(dirPath)) {
            Files.createDirectories(dirPath);
        }
    }

    private static void createFile(String path) throws IOException {
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