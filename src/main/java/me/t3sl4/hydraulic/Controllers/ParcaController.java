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
import java.util.Objects;

public class ParcaController {
    @FXML
    private ComboBox<String> basincSalteriComboBox;

    @FXML
    private ComboBox<String> manometreComboBox;

    @FXML
    private ComboBox<String> elPompasiComboBox;

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
    private String elPompasiDurumu = null;

    public void initialize() {
        if(KlasikController.secilenSogutmaDurumu.equals("Var")) {
            manometreComboBox.setDisable(true);
            basincSalteriComboBox.setDisable(false);
            basincSalteriComboBox.getItems().clear();
            basincSalteriComboBox.getItems().addAll("Var", "Yok");
        } else {
            manometreComboBox.setDisable(false);
            manometreComboBox.getItems().clear();
            manometreComboBox.getItems().addAll("Var", "Yok");
        }

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
        basincSalteriComboBox.setDisable(false);
    }

    @FXML
    public void basincSalteriPressed() {
        elPompasiComboBox.getItems().clear();
        elPompasiComboBox.getItems().addAll("Var", "Yok");

        if(basincSalteriComboBox.getValue() != null) {
            basincSalteriDurumu = basincSalteriComboBox.getValue();
        }
        elPompasiComboBox.setDisable(false);
    }

    @FXML
    public void elPompasiPressed() {
        elPompasiDurumu = String.valueOf(elPompasiComboBox.getValue());
        tabloGuncelle();
    }

    private void comboBoxListener() {
        elPompasiComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            parcaListesiTablo.getItems().clear();
            tabloGuncelle();
            elPompasiDurumu = String.valueOf(newValue);
        });
    }

    private void tabloGuncelle() {
        loadMotorParca();
        loadKampanaParca();
        loadPompaParca();
        loadKaplinParca();
        loadValfBlokParca();
        if(basincSalteriDurumu.equals("Var")) {
            loadBasincSalteriParca();
        }
        if(KlasikController.secilenSogutmaDurumu.contains("Yok") && manometreDurumu.equals("Var")) {
            loadManometre();
        }

        if(elPompasiDurumu.equals("Var")) {
            loadElPompasiParca();
        }

        loadBasincStandart();
        if(KlasikController.secilenSogutmaDurumu.contains("Var")) {
            loadSogutucuParca();
        }
    }

    private void loadKampanaParca() {
        if(KlasikController.secilenKampana == 250) {
            for (String veri : Util.dataManipulator.parcaListesiKampana250) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(KlasikController.secilenKampana == 300) {
            for (String veri : Util.dataManipulator.parcaListesiKampana300) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(KlasikController.secilenKampana == 350) {
            for (String veri : Util.dataManipulator.parcaListesiKampana350) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(KlasikController.secilenKampana == 400) {
            for (String veri : Util.dataManipulator.parcaListesiKampana400) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        }
    }

    private void loadPompaParca() {
        if(Objects.equals(KlasikController.secilenPompa, "9.5 cc")) {
            for (String veri : Util.dataManipulator.parcaListesiPompa95) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenPompa, "11.9 cc")) {
            for (String veri : Util.dataManipulator.parcaListesiPompa119) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenPompa, "14 cc")) {
            for (String veri : Util.dataManipulator.parcaListesiPompa14) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenPompa, "14.6 cc")) {
            for (String veri : Util.dataManipulator.parcaListesiPompa146) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenPompa, "16.8 cc")) {
            for (String veri : Util.dataManipulator.parcaListesiPompa168) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenPompa, "19.2 cc")) {
            for (String veri : Util.dataManipulator.parcaListesiPompa192) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenPompa, "22.9 cc")) {
            for (String veri : Util.dataManipulator.parcaListesiPompa229) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenPompa, "28.1 cc")) {
            for (String veri : Util.dataManipulator.parcaListesiPompa281) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenPompa, "28.8 cc")) {
            for (String veri : Util.dataManipulator.parcaListesiPompa288) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenPompa, "33.3 cc")) {
            for (String veri : Util.dataManipulator.parcaListesiPompa333) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenPompa, "37.9 cc")) {
            for (String veri : Util.dataManipulator.parcaListesiPompa379) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenPompa, "42.6 cc")) {
            for (String veri : Util.dataManipulator.parcaListesiPompa426) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenPompa, "45.5 cc")) {
            for (String veri : Util.dataManipulator.parcaListesiPompa455) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenPompa, "49.4 cc")) {
            for (String veri : Util.dataManipulator.parcaListesiPompa494) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenPompa, "56.1 cc")) {
            for (String veri : Util.dataManipulator.parcaListesiPompa561) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        }
    }

    private void loadMotorParca() {
        if(Objects.equals(KlasikController.secilenMotor, "2.2 kW")) {
            for (String veri : Util.dataManipulator.parcaListesiMotor202) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenMotor, "3 kW")) {
            for (String veri : Util.dataManipulator.parcaListesiMotor3) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenMotor, "4 kW")) {
            for (String veri : Util.dataManipulator.parcaListesiMotor4) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenMotor, "5.5 kW")) {
            for (String veri : Util.dataManipulator.parcaListesiMotor55) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenMotor, "5.5 kW (Kompakt)")) {
            for (String veri : Util.dataManipulator.parcaListesiMotor55Kompakt) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenMotor, "7.5 kW (Kompakt)")) {
            for (String veri : Util.dataManipulator.parcaListesiMotor75Kompakt) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenMotor, "11 kW")) {
            for (String veri : Util.dataManipulator.parcaListesiMotor11) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenMotor, "11 kW (Kompakt)")) {
            for (String veri : Util.dataManipulator.parcaListesiMotor11Kompakt) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenMotor, "15 kW")) {
            for (String veri : Util.dataManipulator.parcaListesiMotor15) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenMotor, "18.5 kW")) {
            for (String veri : Util.dataManipulator.parcaListesiMotor185) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenMotor, "22 kW")) {
            for (String veri : Util.dataManipulator.parcaListesiMotor22) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenMotor, "37 kW")) {
            for (String veri : Util.dataManipulator.parcaListesiMotor37) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        }
    }

    private void loadKaplinParca() {
        String[] secPmp = KlasikController.secilenPompa.split(" cc");
        float secilenPompaVal = Float.parseFloat(secPmp[0]);

        if(secilenPompaVal < 33.3) {
            if(Objects.equals(KlasikController.secilenMotor, "2.2 kW") || Objects.equals(KlasikController.secilenMotor, "3 kW") || Objects.equals(KlasikController.secilenMotor, "4 kW") || Objects.equals(KlasikController.secilenMotor, "5.5 kW") || Objects.equals(KlasikController.secilenMotor, "5.5 kW (Kompakt)")) {
                for (String veri : Util.dataManipulator.parcaListesiKaplin1PN28) {
                    String[] veriParcalari = veri.split(";");

                    String malzemeKodu = veriParcalari[0];
                    String secilenMalzeme = veriParcalari[1];
                    String adet = veriParcalari[2];

                    ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                    parcaListesiTablo.getItems().add(data);
                }
            } else if(Objects.equals(KlasikController.secilenMotor, "7.5 kW") || Objects.equals(KlasikController.secilenMotor, "11 kW") || Objects.equals(KlasikController.secilenMotor, "11 kW (Kompakt)")) {
                for (String veri : Util.dataManipulator.parcaListesiKaplin1PN38) {
                    String[] veriParcalari = veri.split(";");

                    String malzemeKodu = veriParcalari[0];
                    String secilenMalzeme = veriParcalari[1];
                    String adet = veriParcalari[2];

                    ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                    parcaListesiTablo.getItems().add(data);
                }
            } else if(Objects.equals(KlasikController.secilenMotor, "15 kW") || Objects.equals(KlasikController.secilenMotor, "18.5 kW") || Objects.equals(KlasikController.secilenMotor, "22 kW") || Objects.equals(KlasikController.secilenMotor, "37 kW")) {
                for (String veri : Util.dataManipulator.parcaListesiKaplin1PN42) {
                    String[] veriParcalari = veri.split(";");

                    String malzemeKodu = veriParcalari[0];
                    String secilenMalzeme = veriParcalari[1];
                    String adet = veriParcalari[2];

                    ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                    parcaListesiTablo.getItems().add(data);
                }
            }
        } else {
            if(Objects.equals(KlasikController.secilenMotor, "2.2 kW") || Objects.equals(KlasikController.secilenMotor, "3 kW") || Objects.equals(KlasikController.secilenMotor, "4 kW") || Objects.equals(KlasikController.secilenMotor, "5.5 kW") || Objects.equals(KlasikController.secilenMotor, "5.5 kW (Kompakt)")) {
                for (String veri : Util.dataManipulator.parcaListesiKaplin2PN28) {
                    String[] veriParcalari = veri.split(";");

                    String malzemeKodu = veriParcalari[0];
                    String secilenMalzeme = veriParcalari[1];
                    String adet = veriParcalari[2];

                    ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                    parcaListesiTablo.getItems().add(data);
                }
            } else if(Objects.equals(KlasikController.secilenMotor, "7.5 kW") || Objects.equals(KlasikController.secilenMotor, "11 kW") || Objects.equals(KlasikController.secilenMotor, "11 kW (Kompakt)")) {
                for (String veri : Util.dataManipulator.parcaListesiKaplin2PN38) {
                    String[] veriParcalari = veri.split(";");

                    String malzemeKodu = veriParcalari[0];
                    String secilenMalzeme = veriParcalari[1];
                    String adet = veriParcalari[2];

                    ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                    parcaListesiTablo.getItems().add(data);
                }
            } else if(Objects.equals(KlasikController.secilenMotor, "15 kW") || Objects.equals(KlasikController.secilenMotor, "18.5 kW") || Objects.equals(KlasikController.secilenMotor, "22 kW") || Objects.equals(KlasikController.secilenMotor, "37 kW")) {
                for (String veri : Util.dataManipulator.parcaListesiKaplin2PN42) {
                    String[] veriParcalari = veri.split(";");

                    String malzemeKodu = veriParcalari[0];
                    String secilenMalzeme = veriParcalari[1];
                    String adet = veriParcalari[2];

                    ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                    parcaListesiTablo.getItems().add(data);
                }
            }
        }
    }

    private void loadValfBlokParca() {
        if(Objects.equals(KlasikController.secilenValfTipi, "İnişte Tek Hız")) {
            for (String veri : Util.dataManipulator.parcaListesiValfBloklariTekHiz) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenValfTipi, "İnişte Çift Hız")) {
            for (String veri : Util.dataManipulator.parcaListesiValfBloklariCiftHiz) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenValfTipi, "Kilitli Blok || Çift Hız")) {
            for (String veri : Util.dataManipulator.parcaListesiValfBloklariKilitliBlok) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenValfTipi, "Kompanzasyon + İnişte Tek Hız")) {
            for (String veri : Util.dataManipulator.parcaListesiValfBloklariKompanzasyon) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        }
    }

    private void loadBasincSalteriParca() {
        if(Objects.equals(basincSalteriDurumu, "Var")) {
            for (String veri : Util.dataManipulator.parcaListesiBasincSalteri) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        }
    }

    private void loadElPompasiParca() {
        if(Objects.equals(elPompasiDurumu, "Var")) {
            String malzemeKodu = "150-51-10-086";
            String secilenMalzeme = "Oleocon Hidrolik El Pompası OHP Serisi 501-t";
            String adet = "1";

            ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
            parcaListesiTablo.getItems().add(data);
        }
    }

    private void loadBasincStandart() {
        int index = 0;
        int totalElements = Util.dataManipulator.parcaListesiStandart.size();

        for (String veri : Util.dataManipulator.parcaListesiStandart) {
            String[] veriParcalari = veri.split(";");

            String malzemeKodu = veriParcalari[0];
            String secilenMalzeme = veriParcalari[1];
            String adet = veriParcalari[2];
            index++;
            if(index == totalElements) {
                if(Objects.equals(KlasikController.atananHT, "HT 40")) {
                    adet = String.valueOf(10);
                } else if(Objects.equals(KlasikController.atananHT, "HT 70")) {
                    adet = String.valueOf(14);
                } else if(Objects.equals(KlasikController.atananHT, "HT 100")) {
                    adet = String.valueOf(14);
                } else if(Objects.equals(KlasikController.atananHT, "HT 125")) {
                    adet = String.valueOf(14);
                } else if(Objects.equals(KlasikController.atananHT, "HT 160")) {
                    adet = String.valueOf(16);
                } else if(Objects.equals(KlasikController.atananHT, "HT 200")) {
                    adet = String.valueOf(18);
                } else if(Objects.equals(KlasikController.atananHT, "HT 250")) {
                    adet = String.valueOf(18);
                } else if(Objects.equals(KlasikController.atananHT, "HT 300")) {
                    adet = String.valueOf(22);
                } else if(Objects.equals(KlasikController.atananHT, "HT 350")) {
                    adet = String.valueOf(22);
                } else if(Objects.equals(KlasikController.atananHT, "HT 400")) {
                    adet = String.valueOf(22);
                }
            }
            ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
            parcaListesiTablo.getItems().add(data);
        }
    }

    private void loadSogutucuParca() {
        for (String veri : Util.dataManipulator.parcaListesiSogutucu) {
            String[] veriParcalari = veri.split(";");

            String malzemeKodu = veriParcalari[0];
            String secilenMalzeme = veriParcalari[1];
            String adet = veriParcalari[2];

            ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
            parcaListesiTablo.getItems().add(data);
        }
    }

    private void loadManometre() {
        if(Objects.equals(manometreDurumu, "Var")) {
            String malzemeKodu = "150-51-10-802";
            String secilenMalzeme = "Manometre";
            String adet = "1";

            ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
            parcaListesiTablo.getItems().add(data);
        }
    }
}