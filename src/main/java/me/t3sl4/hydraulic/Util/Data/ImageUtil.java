package me.t3sl4.hydraulic.Util.Data;

import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

public class ImageUtil {
    public static String image2Base64(String inputFilePath) throws IOException {
        File f = new File(inputFilePath);
        FileInputStream fin = new FileInputStream(f);
        byte imageByteArray[] = new byte[(int) f.length()];
        fin.read(imageByteArray);
        String base64String = Base64.getEncoder().encodeToString(imageByteArray);
        fin.close();

        return base64String;
    }

    public static Image base64ToImage(String base64String) {
        byte[] imageByteArray = Base64.getDecoder().decode(base64String);
        Image resultImage = new Image(new ByteArrayInputStream(imageByteArray));
        return resultImage;
    }
}
