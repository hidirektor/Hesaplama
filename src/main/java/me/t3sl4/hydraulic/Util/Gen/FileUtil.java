package me.t3sl4.hydraulic.Util.Gen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class FileUtil {
    public static String encodeFileToBase64(URL fileURL) {
        try {
            byte[] fileContent = Files.readAllBytes(Paths.get(fileURL.toURI()));
            return Base64.getEncoder().encodeToString(fileContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encodeFileToBase64WString(String filePath) {
        try {
            byte[] fileContent = Files.readAllBytes(Paths.get(filePath));
            return Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void decodeBase64AndSaveToFile(String base64String, String savePath, String fileName) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(base64String);
            FileOutputStream fos = new FileOutputStream(savePath + fileName);
            fos.write(decodedBytes);
            fos.close();
            System.out.println("File saved: " + savePath + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }
}
