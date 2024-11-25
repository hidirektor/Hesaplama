package me.t3sl4.hydraulic.controllers.Calculation.Klasik.PartList;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;
import me.t3sl4.hydraulic.controllers.Calculation.Klasik.KlasikController;
import me.t3sl4.hydraulic.utils.Utils;
import me.t3sl4.hydraulic.utils.database.Model.Kabin.Kabin;
import me.t3sl4.hydraulic.utils.database.Model.Table.PartList.ParcaTableData;
import me.t3sl4.hydraulic.utils.general.SystemVariables;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class KlasikParcaController {
    @FXML
    private ComboBox<String> basincSalteriComboBox;

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
    private String elPompasiDurumu = null;

    public void initialize() {
        basincSalteriComboBox.setDisable(false);
        basincSalteriComboBox.getItems().clear();
        basincSalteriComboBox.getItems().addAll("Var", "Yok");

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
        String excelFileName = SystemVariables.excelFileLocalPath + KlasikController.girilenSiparisNumarasi + ".xlsx";

        Map<String, ParcaTableData> malzemeMap = new HashMap<>();

        for (ParcaTableData rowData : veriler) {
            // "----" separator satırlarını atla
            if (!(rowData.getSatir1Property().equals("----") && rowData.getSatir3Property().equals("----"))) {
                String malzemeKodu = rowData.getSatir1Property();

                // Eğer malzeme zaten haritada varsa, adetini güncelle
                if (malzemeMap.containsKey(malzemeKodu)) {
                    ParcaTableData existingData = malzemeMap.get(malzemeKodu);
                    int existingAdet = Integer.parseInt(existingData.getSatir3Property());
                    int yeniAdet = Integer.parseInt(rowData.getSatir3Property());
                    // Adetleri topluyoruz
                    existingData.setSatir3Property(String.valueOf(existingAdet + yeniAdet));
                } else {
                    malzemeMap.put(malzemeKodu, new ParcaTableData(
                            rowData.getSatir1Property(),
                            rowData.getSatir2Property(),
                            rowData.getSatir3Property()
                    ));
                }
            }
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Malzeme Listesi");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Malzeme Kodu");
            headerRow.createCell(1).setCellValue("Seçilen Malzeme");
            headerRow.createCell(2).setCellValue("Adet");

            int excelRowIndex = 1;
            for (ParcaTableData rowData : malzemeMap.values()) {
                // "----" separator satırlarını atla
                if (!(rowData.getSatir1Property()
                        .equals("----")
                        && rowData.getSatir3Property().equals("----"))) {

                    Row row = sheet.createRow(excelRowIndex++);
                    row.createCell(0).setCellValue(rowData.getSatir1Property());
                    row.createCell(1).setCellValue(rowData.getSatir2Property());
                    row.createCell(2).setCellValue(rowData.getSatir3Property());
                }
            }

            try (FileOutputStream fileOut = new FileOutputStream(excelFileName)) {
                workbook.write(fileOut);
                System.out.println("Excel dosyası başarıyla oluşturuldu: " + excelFileName);
                if(SystemVariables.loggedInUser != null) {
                    Utils.createLocalUnitData(SystemVariables.localHydraulicStatsPath,
                            KlasikController.girilenSiparisNumarasi,
                            Utils.getCurrentUnixTime(),
                            KlasikController.secilenUniteTipi,
                            null,
                            excelFileName,
                            "no",
                            SystemVariables.loggedInUser.getUserID());
                } else {
                    Utils.createLocalUnitData(SystemVariables.localHydraulicStatsPath,
                            KlasikController.girilenSiparisNumarasi,
                            Utils.getCurrentUnixTime(),
                            KlasikController.secilenUniteTipi,
                            null,
                            excelFileName,
                            "yes",
                            System.getProperty("user.name"));
                }
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
        if(KlasikController.secilenSogutmaDurumu.equals("Yok")) {
            loadStockCodes();
        }

        loadMotorParca();
        loadKampanaParca();
        loadPompaParca();
        loadKaplinParca();
        loadValfBlokParca();

        if(KlasikController.secilenKilitMotor != null) {
            loadKilitMotorParca();
        }

        loadStandartParca();
        if(KlasikController.secilenSogutmaDurumu.contains("Var")) {
            loadSogutucuParca();
        }

        if(basincSalteriDurumu.equals("Var")) {
            loadBasincSalteriParca();
        }

        if(elPompasiDurumu.equals("Var")) {
            loadElPompasiParca();
        }

        loadYagMiktari();
    }

    private void loadStockCodes() {
        String adet = "1";

        Kabin foundedTank = Utils.findClassicTankByKabinName(KlasikController.atananKabinFinal);
        List<ParcaTableData> dataList = Arrays.asList(
                new ParcaTableData("----", "Kabin Genel Bilgisi", "----"),
                new ParcaTableData(foundedTank.getKabinKodu(), foundedTank.getMalzemeAdi(), adet),
                new ParcaTableData(foundedTank.getYagTankiKodu(), foundedTank.getTankName(), adet)
        );

        parcaListesiTablo.getItems().addAll(dataList);
    }

    private void loadKampanaParca() {
        if(KlasikController.secilenPompaVal >= 33.3) {
            if(KlasikController.secilenKampana == 250) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaKampana.get("4"), "Kampana Parçaları");
            } else if(KlasikController.secilenKampana == 300) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaKampana.get("5"), "Kampana Parçaları");
            } else if(KlasikController.secilenKampana == 350) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaKampana.get("6"), "Kampana Parçaları");
            } else if(KlasikController.secilenKampana == 400) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaKampana.get("7"), "Kampana Parçaları");
            }
        } else {
            if(KlasikController.secilenKampana == 250) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaKampana.get("0"), "Kampana Parçaları");
            } else if(KlasikController.secilenKampana == 300) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaKampana.get("1"), "Kampana Parçaları");
            } else if(KlasikController.secilenKampana == 350) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaKampana.get("2"), "Kampana Parçaları");
            } else if(KlasikController.secilenKampana == 400) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaKampana.get("3"), "Kampana Parçaları");
            }
        }
    }

    private void loadPompaParca() {
        if(Objects.equals(KlasikController.secilenPompa, "9.5 cc")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaPompa.get("0"), "Pompa Parçaları");
        } else if(Objects.equals(KlasikController.secilenPompa, "11.9 cc")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaPompa.get("1"), "Pompa Parçaları");
        } else if(Objects.equals(KlasikController.secilenPompa, "14 cc")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaPompa.get("2"), "Pompa Parçaları");
        } else if(Objects.equals(KlasikController.secilenPompa, "14.6 cc")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaPompa.get("3"), "Pompa Parçaları");
        } else if(Objects.equals(KlasikController.secilenPompa, "16.8 cc")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaPompa.get("4"), "Pompa Parçaları");
        } else if(Objects.equals(KlasikController.secilenPompa, "19.2 cc")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaPompa.get("5"), "Pompa Parçaları");
        } else if(Objects.equals(KlasikController.secilenPompa, "22.9 cc")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaPompa.get("6"), "Pompa Parçaları");
        } else if(Objects.equals(KlasikController.secilenPompa, "28.1 cc")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaPompa.get("7"), "Pompa Parçaları");
        } else if(Objects.equals(KlasikController.secilenPompa, "28.8 cc")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaPompa.get("8"), "Pompa Parçaları");
        } else if(Objects.equals(KlasikController.secilenPompa, "33.3 cc")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaPompa.get("9"), "Pompa Parçaları");
        } else if(Objects.equals(KlasikController.secilenPompa, "37.9 cc")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaPompa.get("10"), "Pompa Parçaları");
        } else if(Objects.equals(KlasikController.secilenPompa, "42.6 cc")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaPompa.get("11"), "Pompa Parçaları");
        } else if(Objects.equals(KlasikController.secilenPompa, "45.5 cc")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaPompa.get("12"), "Pompa Parçaları");
        } else if(Objects.equals(KlasikController.secilenPompa, "49.4 cc")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaPompa.get("13"), "Pompa Parçaları");
        } else if(Objects.equals(KlasikController.secilenPompa, "56.1 cc")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaPompa.get("14"), "Pompa Parçaları");
        }
    }

    private void loadMotorParca() {
        if(Objects.equals(KlasikController.secilenMotor, "2.2 kW")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaMotor.get("0"), "Motor Parçaları");
        } else if(Objects.equals(KlasikController.secilenMotor, "3 kW")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaMotor.get("1"), "Motor Parçaları");
        } else if(Objects.equals(KlasikController.secilenMotor, "4 kW")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaMotor.get("2"), "Motor Parçaları");
        } else if(Objects.equals(KlasikController.secilenMotor, "5.5 kW")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaMotor.get("3"), "Motor Parçaları");
        } else if(Objects.equals(KlasikController.secilenMotor, "5.5 kW (Kompakt)")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaMotor.get("4"), "Motor Parçaları");
        } else if(Objects.equals(KlasikController.secilenMotor, "7.5 kW (Kompakt)")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaMotor.get("5"), "Motor Parçaları");
        } else if(Objects.equals(KlasikController.secilenMotor, "11 kW")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaMotor.get("6"), "Motor Parçaları");
        } else if(Objects.equals(KlasikController.secilenMotor, "11 kW (Kompakt)")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaMotor.get("7"), "Motor Parçaları");
        } else if(Objects.equals(KlasikController.secilenMotor, "15 kW")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaMotor.get("8"), "Motor Parçaları");
        } else if(Objects.equals(KlasikController.secilenMotor, "18.5 kW")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaMotor.get("9"), "Motor Parçaları");
        } else if(Objects.equals(KlasikController.secilenMotor, "22 kW")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaMotor.get("10"), "Motor Parçaları");
        } else if(Objects.equals(KlasikController.secilenMotor, "37 kW")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaMotor.get("11"), "Motor Parçaları");
        }
    }

    private void loadKaplinParca() {
        String[] secPmp = KlasikController.secilenPompa.split(" cc");
        float secilenPompaVal = Float.parseFloat(secPmp[0]);

        if(secilenPompaVal < 33.3) {
            if(Objects.equals(KlasikController.secilenMotor, "2.2 kW") || Objects.equals(KlasikController.secilenMotor, "3 kW") || Objects.equals(KlasikController.secilenMotor, "4 kW") || Objects.equals(KlasikController.secilenMotor, "5.5 kW") || Objects.equals(KlasikController.secilenMotor, "5.5 kW (Kompakt)")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaKaplin.get("0"), "Kaplin Parçaları");
            } else if(Objects.equals(KlasikController.secilenMotor, "7.5 kW (Kompakt)") || Objects.equals(KlasikController.secilenMotor, "11 kW") || Objects.equals(KlasikController.secilenMotor, "11 kW (Kompakt)")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaKaplin.get("1"), "Kaplin Parçaları");
            } else if(Objects.equals(KlasikController.secilenMotor, "15 kW") || Objects.equals(KlasikController.secilenMotor, "18.5 kW") || Objects.equals(KlasikController.secilenMotor, "22 kW") || Objects.equals(KlasikController.secilenMotor, "37 kW")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaKaplin.get("2"), "Kaplin Parçaları");
            }
        } else {
            if(Objects.equals(KlasikController.secilenMotor, "2.2 kW") || Objects.equals(KlasikController.secilenMotor, "3 kW") || Objects.equals(KlasikController.secilenMotor, "4 kW") || Objects.equals(KlasikController.secilenMotor, "5.5 kW") || Objects.equals(KlasikController.secilenMotor, "5.5 kW (Kompakt)")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaKaplin.get("3"), "Kaplin Parçaları");
            } else if(Objects.equals(KlasikController.secilenMotor, "7.5 kW (Kompakt)") || Objects.equals(KlasikController.secilenMotor, "11 kW") || Objects.equals(KlasikController.secilenMotor, "11 kW (Kompakt)")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaKaplin.get("4"), "Kaplin Parçaları");
            } else if(Objects.equals(KlasikController.secilenMotor, "15 kW") || Objects.equals(KlasikController.secilenMotor, "18.5 kW") || Objects.equals(KlasikController.secilenMotor, "22 kW") || Objects.equals(KlasikController.secilenMotor, "37 kW")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaKaplin.get("5"), "Kaplin Parçaları");
            }
        }
    }

    private void loadValfBlokParca() {
        if(KlasikController.secilenPompaVal < 33.3) {
            //1 Grubu
            if(Objects.equals(KlasikController.secilenValfTipi, "İnişte Tek Hız")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaValfBloklari.get("0"), "Valf Blok Parçaları");
            } else if(Objects.equals(KlasikController.secilenValfTipi, "İnişte Çift Hız")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaValfBloklari.get("1"), "Valf Blok Parçaları");
            } else if(Objects.equals(KlasikController.secilenValfTipi, "Kilitli Blok")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaValfBloklari.get("2"), "Valf Blok Parçaları");
            } else if(Objects.equals(KlasikController.secilenValfTipi, "Kompanzasyon || İnişte Tek Hız")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaValfBloklari.get("3"), "Valf Blok Parçaları");
            }
        } else {
            //2 Grubu
            if(Objects.equals(KlasikController.secilenValfTipi, "İnişte Tek Hız")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaValfBloklari.get("4"), "Valf Blok Parçaları");
            } else if(Objects.equals(KlasikController.secilenValfTipi, "İnişte Çift Hız")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaValfBloklari.get("5"), "Valf Blok Parçaları");
            } else if(Objects.equals(KlasikController.secilenValfTipi, "Kilitli Blok")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaValfBloklari.get("6"), "Valf Blok Parçaları");
            } else if(Objects.equals(KlasikController.secilenValfTipi, "Kompanzasyon || İnişte Tek Hız")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaValfBloklari.get("7"), "Valf Blok Parçaları");
            }
        }
    }

    private void loadBasincSalteriParca() {
        if(Objects.equals(basincSalteriDurumu, "Var")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaBasincSalteri.get("0"), "Basınç Şalteri Parçaları");
        }
    }

    private void loadElPompasiParca() {
        ParcaTableData separatorData = new ParcaTableData("----", "El Pompası Parçaları", "----");
        parcaListesiTablo.getItems().add(separatorData);

        if(Objects.equals(elPompasiDurumu, "Var")) {
            String malzemeKodu = "150-51-10-086";
            String secilenMalzeme = "Oleocon Hidrolik El Pompası OHP Serisi 501-t";
            String adet = "1";

            ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
            parcaListesiTablo.getItems().add(data);
        }
    }

    private void loadStandartParca() {
        if(Objects.equals(KlasikController.atananHT, "HT 40")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaDefault.get("0"), "Standart Parçalar");
        } else if(Objects.equals(KlasikController.atananHT, "HT 70")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaDefault.get("1"), "Standart Parçalar");
        } else if(Objects.equals(KlasikController.atananHT, "HT 100")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaDefault.get("2"), "Standart Parçalar");
        } else if(Objects.equals(KlasikController.atananHT, "HT 125")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaDefault.get("3"), "Standart Parçalar");
        } else if(Objects.equals(KlasikController.atananHT, "HT 160")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaDefault.get("4"), "Standart Parçalar");
        } else if(Objects.equals(KlasikController.atananHT, "HT 200")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaDefault.get("5"), "Standart Parçalar");
        } else if(Objects.equals(KlasikController.atananHT, "HT 250")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaDefault.get("6"), "Standart Parçalar");
        } else if(Objects.equals(KlasikController.atananHT, "HT 300")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaDefault.get("7"), "Standart Parçalar");
        } else if(Objects.equals(KlasikController.atananHT, "HT 350")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaDefault.get("8"), "Standart Parçalar");
        } else if(Objects.equals(KlasikController.atananHT, "HT 400")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaDefault.get("9"), "Standart Parçalar");
        }
    }

    private void loadSogutucuParca() {
        if(KlasikController.secilenHidrolikKilitDurumu.equals("Var")) {
            //Hidrolik Kilit Var
            if(KlasikController.secilenValfTipi.equals("İnişte Tek Hız") || KlasikController.secilenValfTipi.equals("Kompanzasyon || İnişte Tek Hız")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaSogutma.get("2"), "Soğutucu Parçaları");
            } else if(KlasikController.secilenValfTipi.equals("İnişte Çift Hız") || KlasikController.secilenValfTipi.equals("Kilitli Blok")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaSogutma.get("3"), "Soğutucu Parçaları");
            }
        } else {
            //Hidrolik Kilit Yok
            if(KlasikController.secilenValfTipi.equals("İnişte Tek Hız") || KlasikController.secilenValfTipi.equals("Kompanzasyon || İnişte Tek Hız")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaSogutma.get("0"), "Soğutucu Parçaları");
            } else if(KlasikController.secilenValfTipi.equals("İnişte Çift Hız") || KlasikController.secilenValfTipi.equals("Kilitli Blok")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaSogutma.get("1"), "Soğutucu Parçaları");
            }
        }
    }

    private void loadKilitMotorParca() {
        String kilitMotorVal = KlasikController.secilenKilitMotor;
        String kilitPompaVal = KlasikController.secilenKilitPompa;
        generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaKilitMotor.get("0"), "Kilit Motor Parçaları");

        if(kilitMotorVal.equals("1.5 kW")) {
            ParcaTableData data1 = new ParcaTableData("Veri Yok", kilitMotorVal + " Kilit Motor", "1");
            parcaListesiTablo.getItems().add(data1);
        } else if(kilitMotorVal.equals("2.2 kW")) {
            ParcaTableData data1 = new ParcaTableData("Veri Yok", kilitMotorVal + " Kilit Motor", "1");
            parcaListesiTablo.getItems().add(data1);
        }

        if(kilitPompaVal.equals("4.2 cc")) {
            ParcaTableData data1 = new ParcaTableData("Veri Yok", kilitPompaVal + " Pompa", "1");
            parcaListesiTablo.getItems().add(data1);

            ParcaTableData data = new ParcaTableData("150-50-21-107", "CİVATA İMBUS M8 90 MM BEYAZ (DIN 912)", "2");
            parcaListesiTablo.getItems().add(data);
        } else if(kilitPompaVal.equals("4.8 cc")) {
            ParcaTableData data1 = new ParcaTableData("Veri Yok", kilitPompaVal + " Pompa", "1");
            parcaListesiTablo.getItems().add(data1);

            ParcaTableData data = new ParcaTableData("150-50-21-107", "CİVATA İMBUS M8 90 MM BEYAZ (DIN 912)", "2");
            parcaListesiTablo.getItems().add(data);
        } else if(kilitPompaVal.equals("5.8 cc")) {
            ParcaTableData data1 = new ParcaTableData("Veri Yok", kilitPompaVal + " Pompa", "1");
            parcaListesiTablo.getItems().add(data1);

            ParcaTableData data = new ParcaTableData("150-50-21-109", "CİVATA İMBUS M8 100 MM BEYAZ (DIN 912)", "2");
            parcaListesiTablo.getItems().add(data);
        }
    }

    private void loadYagMiktari() {
        ParcaTableData separatorData = new ParcaTableData("----", "Hidrolik Yağ Parçaları", "----");
        parcaListesiTablo.getItems().add(separatorData);

        String malzemeKodu = "150-53-04-002";
        String malzemeAdi = "HİDROLİK YAĞ SHELL TELLUS S2 M46";
        String adet = KlasikController.girilenTankKapasitesiMiktari + " Lt";

        ParcaTableData data = new ParcaTableData(malzemeKodu, malzemeAdi, adet);
        parcaListesiTablo.getItems().add(data);
    }

    private void generalLoadFunc(LinkedList<String> parcaListesi, String seperatorText) {
        ParcaTableData separatorData = new ParcaTableData("----", seperatorText, "----");
        parcaListesiTablo.getItems().add(separatorData);

        for (String veri : parcaListesi) {
            String[] veriParcalari = veri.split(";");

            String malzemeKodu = veriParcalari[0];
            String secilenMalzeme = veriParcalari[1];
            String adet = veriParcalari[2];

            ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
            parcaListesiTablo.getItems().add(data);
        }

        parcaListesiTablo.setRowFactory(tv -> new TableRow<ParcaTableData>() {
            @Override
            protected void updateItem(ParcaTableData item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else {
                    if (item.getSatir1Property().equals("----") && item.getSatir3Property().equals("----")) {
                        setStyle("-fx-background-color: #F9F871; -fx-text-fill: black;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });
    }
}