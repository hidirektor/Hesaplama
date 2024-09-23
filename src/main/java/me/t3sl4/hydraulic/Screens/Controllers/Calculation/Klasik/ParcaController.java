package me.t3sl4.hydraulic.Screens.Controllers.Calculation.Klasik;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;
import me.t3sl4.hydraulic.Utils.Data.Table.ParcaTableData;
import me.t3sl4.hydraulic.Utils.Data.Tank.Tank;
import me.t3sl4.hydraulic.Utils.File.ExcelUtil;
import me.t3sl4.hydraulic.Utils.Utils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
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
        String excelFileName = KlasikController.girilenSiparisNumarasi + ".xlsx";

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
        loadKabinKodu();
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
        loadYagMiktari();
    }

    private void loadKabinKodu() {
        String malzemeKodu = null;
        String malzemeAdi = null;
        String adet = "1";

        Tank foundedTank = Utils.findTankByKabinName(KlasikController.atananKabinFinal);
        malzemeKodu = foundedTank.getMalzemeKodu();
        malzemeAdi = foundedTank.getMalzemeAdi();

        ParcaTableData data = new ParcaTableData(malzemeKodu, malzemeAdi, adet);
        parcaListesiTablo.getItems().add(data);
    }

    private void loadKampanaParca() {
        ArrayList<String> loadingList = new ArrayList<>();

        if(KlasikController.secilenPompaVal >= 33.3) {
            if(KlasikController.secilenKampana == 250) {
                loadingList = ExcelUtil.dataManipulator.parcaListesiKampana2502k;
            } else if(KlasikController.secilenKampana == 300) {
                loadingList = ExcelUtil.dataManipulator.parcaListesiKampana3002k;
            } else if(KlasikController.secilenKampana == 350) {
                loadingList = ExcelUtil.dataManipulator.parcaListesiKampana3502k;
            } else if(KlasikController.secilenKampana == 400) {
                loadingList = ExcelUtil.dataManipulator.parcaListesiKampana4002k;
            }
        } else {
            if(KlasikController.secilenKampana == 250) {
                loadingList = ExcelUtil.dataManipulator.parcaListesiKampana2501k;
            } else if(KlasikController.secilenKampana == 300) {
                loadingList = ExcelUtil.dataManipulator.parcaListesiKampana3001k;
            } else if(KlasikController.secilenKampana == 350) {
                loadingList = ExcelUtil.dataManipulator.parcaListesiKampana3501k;
            } else if(KlasikController.secilenKampana == 400) {
                loadingList = ExcelUtil.dataManipulator.parcaListesiKampana4001k;
            }
        }

        for (String veri : loadingList) {
            String[] veriParcalari = veri.split(";");

            String malzemeKodu = veriParcalari[0];
            String secilenMalzeme = veriParcalari[1];
            String adet = veriParcalari[2];

            ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
            parcaListesiTablo.getItems().add(data);
        }
    }

    private void loadPompaParca() {
        if(Objects.equals(KlasikController.secilenPompa, "9.5 cc")) {
            for (String veri : ExcelUtil.dataManipulator.parcaListesiPompa95) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenPompa, "11.9 cc")) {
            for (String veri : ExcelUtil.dataManipulator.parcaListesiPompa119) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenPompa, "14 cc")) {
            for (String veri : ExcelUtil.dataManipulator.parcaListesiPompa14) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenPompa, "14.6 cc")) {
            for (String veri : ExcelUtil.dataManipulator.parcaListesiPompa146) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenPompa, "16.8 cc")) {
            for (String veri : ExcelUtil.dataManipulator.parcaListesiPompa168) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenPompa, "19.2 cc")) {
            for (String veri : ExcelUtil.dataManipulator.parcaListesiPompa192) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenPompa, "22.9 cc")) {
            for (String veri : ExcelUtil.dataManipulator.parcaListesiPompa229) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenPompa, "28.1 cc")) {
            for (String veri : ExcelUtil.dataManipulator.parcaListesiPompa281) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenPompa, "28.8 cc")) {
            for (String veri : ExcelUtil.dataManipulator.parcaListesiPompa288) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenPompa, "33.3 cc")) {
            for (String veri : ExcelUtil.dataManipulator.parcaListesiPompa333) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenPompa, "37.9 cc")) {
            for (String veri : ExcelUtil.dataManipulator.parcaListesiPompa379) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenPompa, "42.6 cc")) {
            for (String veri : ExcelUtil.dataManipulator.parcaListesiPompa426) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenPompa, "45.5 cc")) {
            for (String veri : ExcelUtil.dataManipulator.parcaListesiPompa455) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenPompa, "49.4 cc")) {
            for (String veri : ExcelUtil.dataManipulator.parcaListesiPompa494) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenPompa, "56.1 cc")) {
            for (String veri : ExcelUtil.dataManipulator.parcaListesiPompa561) {
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
            for (String veri : ExcelUtil.dataManipulator.parcaListesiMotor202) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenMotor, "3 kW")) {
            for (String veri : ExcelUtil.dataManipulator.parcaListesiMotor3) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenMotor, "4 kW")) {
            for (String veri : ExcelUtil.dataManipulator.parcaListesiMotor4) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenMotor, "5.5 kW")) {
            for (String veri : ExcelUtil.dataManipulator.parcaListesiMotor55) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenMotor, "5.5 kW (Kompakt)")) {
            for (String veri : ExcelUtil.dataManipulator.parcaListesiMotor55Kompakt) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenMotor, "7.5 kW (Kompakt)")) {
            for (String veri : ExcelUtil.dataManipulator.parcaListesiMotor75Kompakt) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenMotor, "11 kW")) {
            for (String veri : ExcelUtil.dataManipulator.parcaListesiMotor11) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenMotor, "11 kW (Kompakt)")) {
            for (String veri : ExcelUtil.dataManipulator.parcaListesiMotor11Kompakt) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenMotor, "15 kW")) {
            for (String veri : ExcelUtil.dataManipulator.parcaListesiMotor15) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenMotor, "18.5 kW")) {
            for (String veri : ExcelUtil.dataManipulator.parcaListesiMotor185) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenMotor, "22 kW")) {
            for (String veri : ExcelUtil.dataManipulator.parcaListesiMotor22) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenMotor, "37 kW")) {
            for (String veri : ExcelUtil.dataManipulator.parcaListesiMotor37) {
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
                for (String veri : ExcelUtil.dataManipulator.parcaListesiKaplin1PN28) {
                    String[] veriParcalari = veri.split(";");

                    String malzemeKodu = veriParcalari[0];
                    String secilenMalzeme = veriParcalari[1];
                    String adet = veriParcalari[2];

                    ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                    parcaListesiTablo.getItems().add(data);
                }
            } else if(Objects.equals(KlasikController.secilenMotor, "7.5 kW (Kompakt)") || Objects.equals(KlasikController.secilenMotor, "11 kW") || Objects.equals(KlasikController.secilenMotor, "11 kW (Kompakt)")) {
                for (String veri : ExcelUtil.dataManipulator.parcaListesiKaplin1PN38) {
                    String[] veriParcalari = veri.split(";");

                    String malzemeKodu = veriParcalari[0];
                    String secilenMalzeme = veriParcalari[1];
                    String adet = veriParcalari[2];

                    ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                    parcaListesiTablo.getItems().add(data);
                }
            } else if(Objects.equals(KlasikController.secilenMotor, "15 kW") || Objects.equals(KlasikController.secilenMotor, "18.5 kW") || Objects.equals(KlasikController.secilenMotor, "22 kW") || Objects.equals(KlasikController.secilenMotor, "37 kW")) {
                for (String veri : ExcelUtil.dataManipulator.parcaListesiKaplin1PN42) {
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
                for (String veri : ExcelUtil.dataManipulator.parcaListesiKaplin2PN28) {
                    String[] veriParcalari = veri.split(";");

                    String malzemeKodu = veriParcalari[0];
                    String secilenMalzeme = veriParcalari[1];
                    String adet = veriParcalari[2];

                    ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                    parcaListesiTablo.getItems().add(data);
                }
            } else if(Objects.equals(KlasikController.secilenMotor, "7.5 kW (Kompakt)") || Objects.equals(KlasikController.secilenMotor, "11 kW") || Objects.equals(KlasikController.secilenMotor, "11 kW (Kompakt)")) {
                for (String veri : ExcelUtil.dataManipulator.parcaListesiKaplin2PN38) {
                    String[] veriParcalari = veri.split(";");

                    String malzemeKodu = veriParcalari[0];
                    String secilenMalzeme = veriParcalari[1];
                    String adet = veriParcalari[2];

                    ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                    parcaListesiTablo.getItems().add(data);
                }
            } else if(Objects.equals(KlasikController.secilenMotor, "15 kW") || Objects.equals(KlasikController.secilenMotor, "18.5 kW") || Objects.equals(KlasikController.secilenMotor, "22 kW") || Objects.equals(KlasikController.secilenMotor, "37 kW")) {
                for (String veri : ExcelUtil.dataManipulator.parcaListesiKaplin2PN42) {
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
            for (String veri : ExcelUtil.dataManipulator.parcaListesiValfBloklariTekHiz) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenValfTipi, "İnişte Çift Hız")) {
            for (String veri : ExcelUtil.dataManipulator.parcaListesiValfBloklariCiftHiz) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenValfTipi, "Kilitli Blok")) {
            for (String veri : ExcelUtil.dataManipulator.parcaListesiValfBloklariKilitliBlok) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(KlasikController.secilenValfTipi, "Kompanzasyon || İnişte Tek Hız")) {
            for (String veri : ExcelUtil.dataManipulator.parcaListesiValfBloklariKompanzasyon) {
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
            for (String veri : ExcelUtil.dataManipulator.parcaListesiBasincSalteri) {
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
        int totalElements = ExcelUtil.dataManipulator.parcaListesiStandart.size();

        for (String veri : ExcelUtil.dataManipulator.parcaListesiStandart) {
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
        for (String veri : ExcelUtil.dataManipulator.parcaListesiSogutucu) {
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

    private void loadYagMiktari() {
        String malzemeKodu = "150-53-04-002";
        String malzemeAdi = "HİDROLİK YAĞ SHELL TELLUS S2 M46";
        String adet = KlasikController.girilenTankKapasitesiMiktari + " Lt";

        ParcaTableData data = new ParcaTableData(malzemeKodu, malzemeAdi, adet);
        parcaListesiTablo.getItems().add(data);
    }
}