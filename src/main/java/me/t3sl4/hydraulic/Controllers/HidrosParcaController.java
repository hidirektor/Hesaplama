package me.t3sl4.hydraulic.Controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;
import me.t3sl4.hydraulic.Util.Data.Table.ParcaTableData;
import me.t3sl4.hydraulic.Util.Util;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Objects;

public class HidrosParcaController {
    @FXML
    private ComboBox<String> basincSalteriComboBox;

    @FXML
    private ComboBox<String> manometreComboBox;

    @FXML
    private TableView<ParcaTableData> parcaListesiTablo;

    @FXML
    private TableColumn<ParcaTableData, String> malzemeKodu;

    @FXML
    private TableColumn<ParcaTableData, String> secilenMalzeme;

    @FXML
    private TableColumn<ParcaTableData, String> adet;

    private String basincSalteriDurumu = null;
    private String manometreDurumu = null;

    public void initialize() {
        manometreComboBox.getItems().clear();
        manometreComboBox.getItems().addAll("Var", "Yok");

        malzemeKodu.setCellValueFactory(new PropertyValueFactory<>("satir1Property"));
        secilenMalzeme.setCellValueFactory(new PropertyValueFactory<>("satir2Property"));
        adet.setCellValueFactory(new PropertyValueFactory<>("satir3Property"));

        parcaListesiTablo.getItems().clear();
        comboBoxListener();
    }

    @FXML
    public void panoyaKopyala() {
        StringBuilder clipboardString = new StringBuilder();

        ObservableList<ParcaTableData> veriler = parcaListesiTablo.getItems();

        for (ParcaTableData veri : veriler) {
            clipboardString.append(veri.getSatir1Property()).append(" ");
            clipboardString.append(veri.getSatir2Property()).append(" ");
            clipboardString.append(veri.getSatir3Property()).append("\n");
        }

        ClipboardContent content = new ClipboardContent();
        content.putString(clipboardString.toString());
        Clipboard.getSystemClipboard().setContent(content);
    }

    @FXML
    public void exportExcelProcess() {
        ObservableList<ParcaTableData> veriler = parcaListesiTablo.getItems();
        String excelFileName = HidrosController.girilenSiparisNumarasi + ".xlsx";

        String desktopPath = Paths.get(System.getProperty("user.home"), "Desktop").toString();
        excelFileName = Paths.get(desktopPath, excelFileName).toString();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Malzeme Listesi");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Malzeme Kodu");
            headerRow.createCell(1).setCellValue("Seçilen Malzeme");
            headerRow.createCell(2).setCellValue("Adet");

            for (int i = 0; i < veriler.size(); i++) {
                ParcaTableData rowData = veriler.get(i);
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(rowData.getSatir1Property());
                row.createCell(1).setCellValue(rowData.getSatir2Property());
                row.createCell(2).setCellValue(rowData.getSatir3Property());
            }

            try (FileOutputStream fileOut = new FileOutputStream(excelFileName)) {
                workbook.write(fileOut);
                System.out.println("Excel dosyası başarıyla oluşturuldu: " + excelFileName);
            }

        } catch (IOException e) {
            System.err.println("Excel dosyası oluşturulurken bir hata oluştu: " + e.getMessage());
        }
    }

    @FXML
    public void popupKapat() {
        Stage stage = (Stage) basincSalteriComboBox.getScene().getWindow();

        stage.close();
    }

    @FXML
    public void manometrePressed() {
        basincSalteriComboBox.getItems().clear();
        basincSalteriComboBox.getItems().addAll("Var", "Yok");

        if(manometreComboBox.getValue() != null) {
            manometreDurumu = manometreComboBox.getValue();
        }
    }

    @FXML
    public void basincSalteriPressed() {
        basincSalteriDurumu = String.valueOf(basincSalteriComboBox.getValue());
        tabloGuncelle();
    }

    private void comboBoxListener() {
        basincSalteriComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            parcaListesiTablo.getItems().clear();
            tabloGuncelle();
            basincSalteriDurumu = String.valueOf(newValue);
        });
    }

    private void tabloGuncelle() {
        loadMotorParca();
        loadPompaParca();
    }

    private void loadMotorParca() {
        String voltajDegeri = HidrosController.secilenMotorTipi.replaceAll(" V$", "");
        String motorGucu = HidrosController.secilenMotorGucu.trim();

        if (Objects.equals(voltajDegeri, "380")) {
            String malzemeKodu = Util.getStockCodeFromDoubleHashMap(Util.dataManipulator.hidros380Parca, motorGucu);
            String secilenMalzeme = Util.getMaterialFromDoubleHashMap(Util.dataManipulator.hidros380Parca, motorGucu);

            String adet = Util.getAmountFromDoubleHashMap(Util.dataManipulator.hidros380Parca, motorGucu);
            float floatAdet = Float.parseFloat(adet);
            int tamSayi = (int) floatAdet;
            String adetFinal = String.valueOf(tamSayi);

            ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adetFinal);
            parcaListesiTablo.getItems().add(data);
        } else if (Objects.equals(voltajDegeri, "220")) {
            String malzemeKodu = Util.getStockCodeFromDoubleHashMap(Util.dataManipulator.hidros220Parca, motorGucu);
            String secilenMalzeme = Util.getMaterialFromDoubleHashMap(Util.dataManipulator.hidros220Parca, motorGucu);
            String adet = Util.getAmountFromDoubleHashMap(Util.dataManipulator.hidros220Parca, motorGucu);
            float floatAdet = Float.parseFloat(adet);
            int tamSayi = (int) floatAdet;
            String adetFinal = String.valueOf(tamSayi);

            ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adetFinal);
            parcaListesiTablo.getItems().add(data);
        }
    }

    private void loadPompaParca() {
        String pompaDegeri = HidrosController.secilenPompa;

        String malzemeKodu = Util.getStockCodeFromDoubleHashMap(Util.dataManipulator.hidrosPompaParca, pompaDegeri);
        String secilenMalzeme = Util.getMaterialFromDoubleHashMap(Util.dataManipulator.hidrosPompaParca, pompaDegeri);

        String adet = Util.getAmountFromDoubleHashMap(Util.dataManipulator.hidrosPompaParca, pompaDegeri);

        ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
        parcaListesiTablo.getItems().add(data);
    }
}