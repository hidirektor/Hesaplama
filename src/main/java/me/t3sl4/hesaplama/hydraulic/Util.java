package me.t3sl4.hesaplama.hydraulic;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Util {
    public static void pdfGenerator(String pngFilePath, String pdfFilePath, String girilenSiparisNumarasi) {
        try {
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(girilenSiparisNumarasi + ".pdf"));
            document.open();

            Image image = Image.getInstance(pngFilePath);
            document.add(image);

            PdfReader reader = new PdfReader(pdfFilePath);
            PdfImportedPage page = writer.getImportedPage(reader, 1);
            document.newPage();
            writer.getDirectContent().addTemplate(page, 0, 0);

            document.close();

            System.out.println("PDF oluşturuldu.");

            File pngFile = new File(pngFilePath);
            if (pngFile.delete()) {
                System.out.println("PNG dosyası silindi.");
            } else {
                System.out.println("PNG dosyası silinemedi.");
            }

            if (Desktop.isDesktopSupported()) {
                try {
                    File pdfFile = new File(girilenSiparisNumarasi + ".pdf");
                    Desktop.getDesktop().open(pdfFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }
}
