package me.t3sl4.hydraulic.utils.database.File;

import javafx.concurrent.Task;
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
        SystemVariables.mainPath = basePath + "OnderGrup/";
        SystemVariables.profilePhotoLocalPath = SystemVariables.mainPath + "userData/";
        SystemVariables.dataFileLocalPath = SystemVariables.mainPath + "data/";

        SystemVariables.tokenPath = SystemVariables.profilePhotoLocalPath + "auth.txt";

        SystemVariables.pdfFileLocalPath = SystemVariables.dataFileLocalPath + "schematicFiles/";
        SystemVariables.excelFileLocalPath = SystemVariables.dataFileLocalPath + "excelFiles/";

        SystemVariables.generalDBPath = SystemVariables.dataFileLocalPath + "general.json";
        SystemVariables.cabinsDBPath = SystemVariables.dataFileLocalPath + "cabins.json";
        SystemVariables.classicComboDBPath = SystemVariables.dataFileLocalPath + "classic_combo.yml";
        SystemVariables.powerPackComboDBPath = SystemVariables.dataFileLocalPath + "powerpack_combo.yml";
        SystemVariables.classicPartsDBPath = SystemVariables.dataFileLocalPath + "classic_parts.yml";
        SystemVariables.powerPackPartsHidrosDBPath = SystemVariables.dataFileLocalPath + "powerpack_parts_hidros.yml";
        SystemVariables.powerPackPartsIthalDBPath = SystemVariables.dataFileLocalPath + "powerpack_parts_ithal.yml";

        // 1. OnderGrup klasörünü oluştur
        createDirectory(SystemVariables.mainPath);

        // 2. data ve userData klasörlerini oluştur
        createDirectory(SystemVariables.dataFileLocalPath);
        createDirectory(SystemVariables.profilePhotoLocalPath);

        // 3. userData içine boş auth.txt dosyasını oluştur
        createFile(SystemVariables.tokenPath);

        // 4. Belirtilen dosyaları data klasörüne kopyala
        fileCopy("/assets/data/programDatabase/general.json", SystemVariables.generalDBPath);
        fileCopy("/assets/data/programDatabase/cabins.json", SystemVariables.cabinsDBPath);
        fileCopy("/assets/data/programDatabase/classic_combo.yml", SystemVariables.classicComboDBPath);
        fileCopy("/assets/data/programDatabase/powerpack_combo.yml", SystemVariables.powerPackComboDBPath);
        fileCopy("/assets/data/programDatabase/classic_parts.yml", SystemVariables.classicPartsDBPath);
        fileCopy("/assets/data/programDatabase/powerpack_parts_hidros.yml", SystemVariables.powerPackPartsHidrosDBPath);
        fileCopy("/assets/data/programDatabase/powerpack_parts_ithal.yml", SystemVariables.powerPackPartsIthalDBPath);

        // 5. data klasörünün içine excelFiles ve schematicFiles klasörlerini oluştur
        createDirectory(SystemVariables.excelFileLocalPath);
        createDirectory(SystemVariables.pdfFileLocalPath);

        // 6. Fazlalık dosyaları sil
        cleanDirectory(SystemVariables.excelFileLocalPath);
        cleanDirectory(SystemVariables.pdfFileLocalPath);

        // 7. Dataları yükle
        JSONUtil.loadJSONData();
        new YamlUtil(SystemVariables.classicComboDBPath, SystemVariables.powerPackComboDBPath, SystemVariables.classicPartsDBPath, SystemVariables.powerPackPartsHidrosDBPath, SystemVariables.powerPackPartsIthalDBPath);
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