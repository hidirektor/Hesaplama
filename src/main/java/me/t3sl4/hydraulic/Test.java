package me.t3sl4.hydraulic;

import me.t3sl4.hydraulic.Util.Gen.FileUtil;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class Test {
    public static void main(String[] args) {
        String pdfBase64String = FileUtil.encodeFileToBase64(Launcher.class.getResource("/data/45.pdf"));
        String xlsxBase64String = FileUtil.encodeFileToBase64(Launcher.class.getResource("/data/45.xlsx"));

        System.out.println("Base64 PDF: " + pdfBase64String);
        System.out.println("Base64 XLSX: " + xlsxBase64String);

        String desktopPath = System.getProperty("user.home") + "/Desktop/";

        FileUtil.decodeBase64AndSaveToFile(pdfBase64String, desktopPath, "sample_decoded.pdf");
        FileUtil.decodeBase64AndSaveToFile(xlsxBase64String, desktopPath, "sample_decoded.xlsx");
    }
}
