package me.t3sl4.hesaplama.hydraulic;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.*;
import java.io.*;
import java.util.Map;
import java.util.Objects;

import me.t3sl4.hesaplama.Launcher;
import org.apache.poi.ss.usermodel.*;

public class Util {

    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

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

    public static void readExcel4Bosluk(String filePath, DataManipulator dataManipulator) {
        String sheetName = "Boşluk Değerleri";

        try(InputStream file = Launcher.class.getResourceAsStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            Row variableNamesRow = sheet.getRow(0);
            Row variableValuesRow = sheet.getRow(1);

            dataManipulator.kampanaBoslukX = (int) variableValuesRow.getCell(0).getNumericCellValue();
            dataManipulator.kampanaBoslukY = (int) variableValuesRow.getCell(1).getNumericCellValue();
            dataManipulator.valfBoslukX = (int) variableValuesRow.getCell(2).getNumericCellValue();
            dataManipulator.valfBoslukYArka = (int) variableValuesRow.getCell(3).getNumericCellValue();
            dataManipulator.valfBoslukYOn = (int) variableValuesRow.getCell(4).getNumericCellValue();
            dataManipulator.kilitliBlokAraBoslukX = (int) variableValuesRow.getCell(5).getNumericCellValue();
            dataManipulator.tekHizAraBoslukX = (int) variableValuesRow.getCell(6).getNumericCellValue();
            dataManipulator.ciftHizAraBoslukX = (int) variableValuesRow.getCell(7).getNumericCellValue();
            dataManipulator.kompanzasyonTekHizAraBoslukX = (int) variableValuesRow.getCell(8).getNumericCellValue();
            dataManipulator.sogutmaAraBoslukX = (int) variableValuesRow.getCell(9).getNumericCellValue();
            dataManipulator.sogutmaAraBoslukYkOn = (int) variableValuesRow.getCell(10).getNumericCellValue();
            dataManipulator.sogutmaAraBoslukYkArka = (int) variableValuesRow.getCell(11).getNumericCellValue();
            dataManipulator.kilitMotorKampanaBosluk = (int) variableValuesRow.getCell(12).getNumericCellValue();
            dataManipulator.kilitMotorMotorBoslukX = (int) variableValuesRow.getCell(13).getNumericCellValue();
            dataManipulator.kilitMotorBoslukYOn = (int) variableValuesRow.getCell(14).getNumericCellValue();
            dataManipulator.kilitMotorBoslukYArka = (int) variableValuesRow.getCell(15).getNumericCellValue();
            dataManipulator.kayipLitre = (int) variableValuesRow.getCell(16).getNumericCellValue();

            workbook.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void readExcel4Kampana(String filePath, DataManipulator dataManipulator) {
        String sheetName = "Kampana";

        try(InputStream file = Launcher.class.getResourceAsStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for (int i = 1; i < rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(0);
                String data = cell.getStringCellValue();
                data = data.trim();
                data = data.replaceAll("[^0-9]", "");
                dataManipulator.kampanaDegerleri.add(Integer.parseInt(data));
                //dataManipulator.kampanaVerileri.add(data);
            }

            workbook.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
