package me.t3sl4.hydraulic.Utility.File;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import me.t3sl4.hydraulic.Launcher;
import me.t3sl4.hydraulic.Utility.Utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PDFFileUtil {

    private static final Logger logger = Logger.getLogger(Utils.class.getName());

    public static void pdfGenerator(String pngFilePath1, String pngFilePath2, String pdfFilePath, String girilenSiparisNumarasi) {
        try {
            String userHome = System.getProperty("user.home");
            String ExPDFFilePath = userHome + File.separator + "Desktop" + File.separator + girilenSiparisNumarasi + ".pdf";

            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(ExPDFFilePath));
            document.open();

            PdfContentByte contentByte = writer.getDirectContentUnder();
            BaseColor backgroundColor = new BaseColor(255, 255, 255);
            contentByte.setColorFill(backgroundColor);
            contentByte.rectangle(0, 0, document.getPageSize().getWidth(), document.getPageSize().getHeight());
            contentByte.fill();

            Image image1 = Image.getInstance(Objects.requireNonNull(Launcher.class.getResource(pngFilePath1)));
            float targetWidth1 = document.getPageSize().getWidth() * 0.5f;
            float targetHeight1 = (image1.getHeight() / (float) image1.getWidth()) * targetWidth1;
            image1.scaleToFit(targetWidth1, targetHeight1);
            image1.setAlignment(Image.ALIGN_CENTER);
            document.add(image1);

            Image image2 = Image.getInstance(pngFilePath2);

            float documentWidth = document.getPageSize().getWidth();
            float documentHeight = document.getPageSize().getHeight();
            float imageWidth = image2.getWidth();
            float imageHeight = image2.getHeight();

            float x = (documentWidth - imageWidth) / 2;
            float y = (documentHeight - imageHeight) / 2;

            image2.setAbsolutePosition(x, y);
            document.add(image2);

            if(pdfFilePath != null) {
                PdfReader reader = new PdfReader(Objects.requireNonNull(Launcher.class.getResource(pdfFilePath)));
                PdfImportedPage page = writer.getImportedPage(reader, 1);
                document.newPage();

                float targetWidth = document.getPageSize().getWidth();
                float targetHeight = document.getPageSize().getHeight();

                float originalWidth = page.getWidth();
                float originalHeight = page.getHeight();

                float widthScale = targetWidth / originalWidth;
                float heightScale = targetHeight / originalHeight;

                writer.getDirectContent().addTemplate(page, widthScale, 0, 0, heightScale, 0, 0);
                reader.close();
            }

            document.close();
            writer.close();

            System.out.println("PDF oluşturuldu.");

            File pngFile2 = new File(pngFilePath2);
            if (pngFile2.exists()) {
                if (pngFile2.delete()) {
                    System.out.println("İkinci PNG dosyası silindi.");
                } else {
                    System.out.println("İkinci PNG dosyası silinemedi.");
                }
            } else {
                System.out.println("İkinci PNG dosyası bulunamadı.");
            }

            if (Desktop.isDesktopSupported()) {
                try {
                    File pdfFile = new File(ExPDFFilePath);
                    Desktop.getDesktop().open(pdfFile);
                } catch (IOException e) {
                    logger.log(Level.SEVERE, e.getMessage(), e);
                }
            }
        } catch (DocumentException | IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void cropImage(int startX, int startY, int width, int height) {
        try {
            BufferedImage originalImage = ImageIO.read(new File("screenshot.png"));

            BufferedImage croppedImage = originalImage.getSubimage(startX, startY, width, height);

            String croppedFilePath = "cropped_screenshot.png";
            ImageIO.write(croppedImage, "png", new File(croppedFilePath));
            System.out.println("Kırpılmış fotoğraf başarıyla kaydedildi: " + croppedFilePath);

            File originalFile = new File("screenshot.png");
            if (originalFile.delete()) {
                System.out.println("Eski fotoğraf başarıyla silindi: " + "screenshot.png");
            } else {
                System.out.println("Eski fotoğraf silinemedi: " + "screenshot.png");
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void coords2Png(int startX, int startY, int width, int height, javafx.scene.control.Button exportButton) {
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setViewport(new javafx.geometry.Rectangle2D(startX, startY, width, height));

        WritableImage screenshot = exportButton.getScene().snapshot(null);

        File outputFile = new File("screenshot.png");

        BufferedImage bufferedImage = convertToBufferedImage(screenshot);

        try {
            ImageIO.write(bufferedImage, "png", outputFile);
            System.out.println("Ekran görüntüsü başarıyla kaydedildi: " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Ekran görüntüsü kaydedilirken bir hata oluştu: " + e.getMessage());
        }
    }

    private static BufferedImage convertToBufferedImage(WritableImage writableImage) {
        int width = (int) writableImage.getWidth();
        int height = (int) writableImage.getHeight();
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);

        PixelReader pixelReader = writableImage.getPixelReader();
        WritablePixelFormat<IntBuffer> pixelFormat = WritablePixelFormat.getIntArgbInstance();

        int[] pixelData = new int[width * height];
        pixelReader.getPixels(0, 0, width, height, pixelFormat, pixelData, 0, width);

        bufferedImage.setRGB(0, 0, width, height, pixelData, 0, width);

        return bufferedImage;
    }
}
