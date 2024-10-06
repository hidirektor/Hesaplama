package me.t3sl4.hydraulic.utils.database.File.PDF;

import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import me.t3sl4.hydraulic.Launcher;
import me.t3sl4.hydraulic.utils.Utils;

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

public class PDFUtil {

    private static final Logger logger = Logger.getLogger(Utils.class.getName());

    public static void pdfGenerator(String pngFilePath1, String pngFilePath2, String pngFilePath3, String pdfFilePath, String girilenSiparisNumarasi, String kullanilacakKabin) {
        try {
            String userHome = System.getProperty("user.home");
            String ExPDFFilePath = userHome + File.separator + "Desktop" + File.separator + girilenSiparisNumarasi + ".pdf";

            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(ExPDFFilePath));
            document.open();

            PdfContentByte contentByte = writer.getDirectContentUnder();
            BaseColor backgroundColor = new BaseColor(153, 153, 153);
            contentByte.setColorFill(backgroundColor);
            contentByte.rectangle(0, 0, document.getPageSize().getWidth(), document.getPageSize().getHeight());
            contentByte.fill();

            // İlk resmi ekle ve boyutunu ayarla (yükseklik küçültüldü)
            Image image1 = Image.getInstance(Objects.requireNonNull(Launcher.class.getResource(pngFilePath1)));
            float targetWidth1 = document.getPageSize().getWidth() * 0.8f;  // Genişliği %80'e ayarla
            float targetHeight1 = (image1.getHeight() / (float) image1.getWidth()) * targetWidth1 * 0.7f; // Yüksekliği %70'e küçült
            image1.scaleToFit(targetWidth1, targetHeight1);
            image1.setAlignment(Image.ALIGN_CENTER);
            document.add(image1);

            // Türkçe karakter destekleyen fontu yükle
            BaseFont baseFont = BaseFont.createFont(String.valueOf(Launcher.class.getResource("/assets/fonts/Roboto/Roboto-Bold.ttf")), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font unicodeFont = new Font(baseFont, 22, Font.BOLD);

            // Girilen Sipariş Numarasını ve metni ekle
            Paragraph paragraph = new Paragraph(girilenSiparisNumarasi + " Numaralı Sipariş", unicodeFont);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            paragraph.setSpacingBefore(15);  // 15dp üst boşluk
            document.add(paragraph);

            // İkinci resmi ekle ve boyutunu ayarla (yükseklik küçültüldü)
            Image image2 = Image.getInstance(pngFilePath2);
            float targetWidth2 = document.getPageSize().getWidth() * 0.8f;  // Genişliği %80'e ayarla
            float targetHeight2 = (image2.getHeight() / (float) image2.getWidth()) * targetWidth2 * 0.7f; // Yüksekliği %70'e küçült
            image2.scaleToFit(targetWidth2, targetHeight2);
            image2.setAlignment(Image.ALIGN_CENTER);
            image2.setSpacingBefore(10);  // 10dp üst boşluk
            document.add(image2);

            if(pngFilePath3 != null) {
                // Üçüncü resmi yükleyip beyaz alanlarını siyaha çevirelim
                BufferedImage originalImage = ImageIO.read(new File(pngFilePath3));
                BufferedImage processedImage = convertWhiteToBlack(originalImage);

                // İşlenmiş görüntüyü geçici bir dosyaya kaydet
                File tempFile = new File("processed_image.png");
                ImageIO.write(processedImage, "png", tempFile);

                // Üçüncü resmi ekle ve boyutunu ayarla (yükseklik küçültüldü)
                Image image3 = Image.getInstance(tempFile.getAbsolutePath());
                float targetWidth3 = document.getPageSize().getWidth() * 0.8f;  // Genişliği %80'e ayarla
                float targetHeight3 = (image3.getHeight() / (float) image3.getWidth()) * targetWidth3 * 0.7f; // Yüksekliği %70'e küçült
                image3.scaleToFit(targetWidth3, targetHeight3);
                image3.setAlignment(Image.ALIGN_CENTER);
                image3.setSpacingBefore(10);  // 10dp üst boşluk
                document.add(image3);
            }

            // "HALİL" metnini sayfanın en altına yerleştir
            Paragraph halilParagraph = new Paragraph(kullanilacakKabin, unicodeFont);
            halilParagraph.setAlignment(Element.ALIGN_CENTER);
            halilParagraph.setSpacingBefore(20);  // 20dp boşluk
            document.add(halilParagraph);

            if (pdfFilePath != null) {
                // PdfReader ile PDF'yi yükle
                PdfReader reader = new PdfReader(Launcher.class.getResource(pdfFilePath));

                // Sayfa eklemek için yeni bir sayfa açın
                document.newPage();

                // PdfWriter üzerinden sayfayı al ve yeni PDF'ye ekle
                PdfImportedPage importedPage = writer.getImportedPage(reader, 1);
                PdfContentByte cb = writer.getDirectContent();

                // Sayfayı ekle (orijinal PDF'den alınan sayfa boyutlarını kullanarak)
                cb.addTemplate(importedPage, 0, 0);

                reader.close();  // PdfReader'ı kapatmayı unutmayın
            }

            document.close();
            writer.close();

            System.out.println("PDF oluşturuldu.");

            File pngFile2 = new File(pngFilePath2);
            if(pngFilePath3 != null) {
                File pngFile3 = new File(pngFilePath3);
                pngFile3.delete();
            }
            pngFile2.delete();
            new File("processed_image.png").delete();

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

    public static void cropImage(int startX, int startY, int width, int height, String fileName) {
        try {
            BufferedImage originalImage = ImageIO.read(new File("screenshot.png"));

            BufferedImage croppedImage = originalImage.getSubimage(startX, startY, width, height);

            ImageIO.write(croppedImage, "png", new File(fileName));
            System.out.println("Kırpılmış fotoğraf başarıyla kaydedildi: " + fileName);

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

    public static void coords2Png(int startX, int startY, int width, int height, Button exportButton) {
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setViewport(new Rectangle2D(startX, startY, width, height));

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

    public static BufferedImage convertWhiteToBlack(BufferedImage originalImage) {
        BufferedImage newImage = new BufferedImage(
                originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < originalImage.getWidth(); x++) {
            for (int y = 0; y < originalImage.getHeight(); y++) {
                int rgb = originalImage.getRGB(x, y);
                Color color = new Color(rgb, true);

                if (color.getRed() > 200 && color.getGreen() > 200 && color.getBlue() > 200) {
                    newImage.setRGB(x, y, Color.BLACK.getRGB());
                } else {
                    newImage.setRGB(x, y, rgb);
                }
            }
        }
        return newImage;
    }
}
