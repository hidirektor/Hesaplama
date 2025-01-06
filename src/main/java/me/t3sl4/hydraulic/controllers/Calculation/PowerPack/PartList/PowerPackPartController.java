package me.t3sl4.hydraulic.controllers.Calculation.PowerPack.PartList;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
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
import me.t3sl4.hydraulic.controllers.Calculation.PowerPack.PowerPackController;
import me.t3sl4.hydraulic.utils.Utils;
import me.t3sl4.hydraulic.utils.database.Model.Kabin.Kabin;
import me.t3sl4.hydraulic.utils.database.Model.Table.PartList.ParcaTableData;
import me.t3sl4.hydraulic.utils.general.SystemVariables;
import me.t3sl4.util.os.desktop.DesktopUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import static me.t3sl4.hydraulic.controllers.Calculation.PowerPack.PowerPackController.secilenTankTipi;

public class PowerPackPartController {
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
        manometreComboBox.getItems().clear();
        manometreComboBox.getItems().addAll("Var", "Yok");

        malzemeKodu.setCellValueFactory(new PropertyValueFactory<>("malzemeKoduProperty"));
        secilenMalzeme.setCellValueFactory(new PropertyValueFactory<>("malzemeAdiProperty"));
        adet.setCellValueFactory(new PropertyValueFactory<>("malzemeAdetProperty"));

        parcaListesiTablo.getItems().clear();
        comboBoxListener();
    }

    @FXML
    public void panoyaKopyala() {
        StringBuilder clipboardString = new StringBuilder();

        ObservableList<ParcaTableData> veriler = parcaListesiTablo.getItems();

        for (ParcaTableData veri : veriler) {
            clipboardString.append(veri.getMalzemeKoduProperty()).append(" ");
            clipboardString.append(veri.getMalzemeAdiProperty()).append(" ");
            clipboardString.append(veri.getMalzemeAdetProperty()).append("\n");
        }

        ClipboardContent content = new ClipboardContent();
        content.putString(clipboardString.toString());
        Clipboard.getSystemClipboard().setContent(content);
    }

    @FXML
    public void exportExcelProcess() {
        ObservableList<ParcaTableData> veriler = parcaListesiTablo.getItems();
        String excelFileName = SystemVariables.excelFileLocalPath + PowerPackController.girilenSiparisNumarasi + ".xlsx";

        Multimap<String, ParcaTableData> malzemeMultimap = LinkedListMultimap.create();
        Multimap<String, ParcaTableData> filteredMultimap = LinkedListMultimap.create();
        Multimap<String, ParcaTableData> duplicateMultimap = LinkedListMultimap.create();

        for (ParcaTableData rowData : veriler) {
            if (!(rowData.getMalzemeKoduProperty().equals("----") && rowData.getMalzemeAdetProperty().equals("----"))) {
                String malzemeKey = rowData.getMalzemeKoduProperty();

                filteredMultimap.put(malzemeKey, new ParcaTableData(
                        rowData.getMalzemeKoduProperty(),
                        rowData.getMalzemeAdiProperty(),
                        rowData.getMalzemeAdetProperty()
                ));
            }
        }

        List<String> keysToRemove = new ArrayList<>();
        Iterator<String> iterator = filteredMultimap.keySet().iterator();

        while (iterator.hasNext()) {
            String key = iterator.next();

            // "000-00-00-000" ve "Veri Yok" olmayan key'ler için işlem yap
            if (!key.equals("000-00-00-000") && !key.equals("Veri Yok")) {
                int toplamAdet = 0;
                String currentAdet = "";
                String malzemeKodu = null;
                String malzemeAdi = null;

                // Aynı key'e sahip öğeleri topla
                for (ParcaTableData data : filteredMultimap.get(key)) {
                    if (data.getMalzemeAdetProperty() != null && !data.getMalzemeAdetProperty().isEmpty() && !data.getMalzemeAdetProperty().contains("Lt")) {
                        toplamAdet += Integer.parseInt(data.getMalzemeAdetProperty());
                    } else {
                        currentAdet = data.getMalzemeAdetProperty();
                    }
                    if (malzemeKodu == null) {
                        malzemeKodu = data.getMalzemeKoduProperty();
                    }
                    if (malzemeAdi == null) {
                        malzemeAdi = data.getMalzemeAdiProperty();
                    }
                }

                // duplicateMultimap'e, key ve toplam adet bilgisi ile veri ekle
                ParcaTableData duplicateData;
                if(toplamAdet > 0) {
                    duplicateData = new ParcaTableData(malzemeKodu, malzemeAdi, String.valueOf(toplamAdet));
                } else {
                    duplicateData = new ParcaTableData(malzemeKodu, malzemeAdi, currentAdet);
                }
                duplicateMultimap.put(key, duplicateData);

                // Konsola duplicate verileri yazdır
                System.out.println("Key: " + key + ", Malzeme Kodu: " + malzemeKodu +
                        ", Malzeme Adı: " + malzemeAdi + ", Toplam Adet: " + toplamAdet);

                // Silinecek anahtarı işaretle
                keysToRemove.add(key);
            }
        }

        for (String key : keysToRemove) {
            filteredMultimap.removeAll(key);
        }

        // filteredMultimap'teki elemanları sırayla ekle
        for (String key : filteredMultimap.keySet()) {
            for (ParcaTableData data : filteredMultimap.get(key)) {
                malzemeMultimap.put(key, data);
            }
        }

        // duplicateMultimap'teki elemanları sırayla ekle
        for (String key : duplicateMultimap.keySet()) {
            for (ParcaTableData data : duplicateMultimap.get(key)) {
                malzemeMultimap.put(key, data);
            }
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Malzeme Listesi");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Malzeme Kodu");
            headerRow.createCell(1).setCellValue("Seçilen Malzeme");
            headerRow.createCell(2).setCellValue("Adet");

            int excelRowIndex = 1;
            for (ParcaTableData rowData : malzemeMultimap.values()) {
                Row row = sheet.createRow(excelRowIndex++);
                row.createCell(0).setCellValue(rowData.getMalzemeKoduProperty());
                row.createCell(1).setCellValue(rowData.getMalzemeAdiProperty());
                row.createCell(2).setCellValue(rowData.getMalzemeAdetProperty());
            }

            try (FileOutputStream fileOut = new FileOutputStream(excelFileName)) {
                workbook.write(fileOut);
                System.out.println("Excel dosyası başarıyla oluşturuldu: " + excelFileName);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Ünite Tipi", PowerPackController.secilenUniteTipi);
                jsonObject.put("Sipariş Numarası", PowerPackController.girilenSiparisNumarasi);
                jsonObject.put("Motor Voltaj", PowerPackController.secilenMotorTipi);
                jsonObject.put("Ünite Durumu", PowerPackController.uniteTipiDurumu);
                jsonObject.put("Motor Gücü", PowerPackController.secilenMotorGucu);
                jsonObject.put("Pompa", PowerPackController.secilenPompa);
                jsonObject.put("Tank Tipi", PowerPackController.secilenTankTipi);
                jsonObject.put("Tank Kapasitesi", PowerPackController.secilenTankKapasitesi);
                jsonObject.put("Özel Tank Ölçüleri (GxDxY)", PowerPackController.secilenOzelTankGenislik + "x" + PowerPackController.secilenOzelTankDerinlik + "x" + PowerPackController.secilenOzelTankYukseklik);
                jsonObject.put("Platform Tipi", PowerPackController.secilenPlatformTipi);
                jsonObject.put("1. Valf Tipi", PowerPackController.secilenBirinciValf);
                jsonObject.put("İniş Metodu", PowerPackController.secilenInisTipi);
                jsonObject.put("2. Valf Tipi", PowerPackController.secilenIkinciValf);

                if (SystemVariables.loggedInUser != null) {
                    Utils.createLocalUnitData(SystemVariables.localHydraulicStatsPath,
                            PowerPackController.girilenSiparisNumarasi,
                            Utils.getCurrentUnixTime(),
                            PowerPackController.secilenUniteTipi,
                            null,
                            excelFileName,
                            "no",
                            SystemVariables.loggedInUser.getUserID(),
                            jsonObject);
                } else {
                    Utils.createLocalUnitData(SystemVariables.localHydraulicStatsPath,
                            PowerPackController.girilenSiparisNumarasi,
                            Utils.getCurrentUnixTime(),
                            PowerPackController.secilenUniteTipi,
                            null,
                            excelFileName,
                            "yes",
                            System.getProperty("user.name"),
                            jsonObject);
                }

                DesktopUtil.startExternalApplicationAsync(excelFileName);
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
        String secilenPlatform = PowerPackController.secilenPlatformTipi.trim();

        loadStockCodes();
        loadMotorParca();
        loadPompaParca();
        loadTankTipi();
        loadPlatformTipi();
        //loadGenelParcalar();
        if(PowerPackController.secilenBirinciValf != null) {
            loadValfParcalar();
        }

        if(secilenPlatform.equals("Özel - Yatay")) {
            loadOzelYatayGenel();
        }

        loadManometre();
        loadBasincSalteri();
        loadElPompasiParca();
        if(PowerPackController.secilenTankKapasitesi != null) {
            loadYagMiktari();
        }
    }

    private void loadMotorParca() {
        String motorGucu = PowerPackController.secilenMotorGucu.trim();

        if(PowerPackController.uniteTipiDurumu.equals("Hidros")) {
            //Hidros Malzeme Listesi
            if (Objects.equals(PowerPackController.secilenMotorTipi, "380 V (AC)")) {
                if(motorGucu.equals("0.37 kW")) {
                    generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaMotor380.get("0"), "Motor Parçaları");
                } else if(motorGucu.equals("0.55 kW")) {
                    generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaMotor380.get("1"), "Motor Parçaları");
                } else if(motorGucu.equals("0.75 kW")) {
                    generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaMotor380.get("2"), "Motor Parçaları");
                } else if(motorGucu.equals("1.1 kW")) {
                    generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaMotor380.get("3"), "Motor Parçaları");
                } else if(motorGucu.equals("1.5 kW")) {
                    generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaMotor380.get("4"), "Motor Parçaları");
                } else if(motorGucu.equals("2.2 kW")) {
                    generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaMotor380.get("5"), "Motor Parçaları");
                } else if(motorGucu.equals("3 kW")) {
                    generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaMotor380.get("6"), "Motor Parçaları");
                } else if(motorGucu.equals("4 kW")) {
                    generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaMotor380.get("7"), "Motor Parçaları");
                }
            } else if (Objects.equals(PowerPackController.secilenMotorTipi, "220 V (AC)")) {
                if(motorGucu.equals("0.37 kW")) {
                    generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaMotor220.get("0"), "Motor Parçaları");
                } else if(motorGucu.equals("0.55 kW")) {
                    generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaMotor220.get("1"), "Motor Parçaları");
                } else if(motorGucu.equals("0.75 kW")) {
                    generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaMotor220.get("2"), "Motor Parçaları");
                } else if(motorGucu.equals("1.1 kW")) {
                    generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaMotor220.get("3"), "Motor Parçaları");
                } else if(motorGucu.equals("1.5 kW")) {
                    generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaMotor220.get("4"), "Motor Parçaları");
                } else if(motorGucu.equals("2.2 kW")) {
                    generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaMotor220.get("5"), "Motor Parçaları");
                } else if(motorGucu.equals("3 kW")) {
                    generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaMotor220.get("6"), "Motor Parçaları");
                }
            }
        } else {
            //İthal Malzeme Listesi
            if (Objects.equals(PowerPackController.secilenMotorTipi, "380 V (AC)")) {
                if(motorGucu.equals("0.55 kW")) {
                    generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaMotor380.get("1"), "Motor Parçaları");
                } else if(motorGucu.equals("0.75 kW")) {
                    generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaMotor380.get("2"), "Motor Parçaları");
                } else if(motorGucu.equals("1.1 kW")) {
                    generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaMotor380.get("3"), "Motor Parçaları");
                } else if(motorGucu.equals("1.5 kW")) {
                    generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaMotor380.get("4"), "Motor Parçaları");
                } else if(motorGucu.equals("2.2 kW")) {
                    generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaMotor380.get("5"), "Motor Parçaları");
                } else if(motorGucu.equals("3 kW")) {
                    generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaMotor380.get("6"), "Motor Parçaları");
                } else if(motorGucu.equals("4 kW")) {
                    generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaMotor380.get("7"), "Motor Parçaları");
                }
            } else if (Objects.equals(PowerPackController.secilenMotorTipi, "220 V (AC)")) {
                if(motorGucu.equals("0.37 kW")) {
                    generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaMotor220.get("0"), "Motor Parçaları");
                } else if(motorGucu.equals("0.55 kW")) {
                    generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaMotor220.get("1"), "Motor Parçaları");
                } else if(motorGucu.equals("0.75 kW")) {
                    generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaMotor220.get("2"), "Motor Parçaları");
                } else if(motorGucu.equals("1.1 kW")) {
                    generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaMotor220.get("3"), "Motor Parçaları");
                } else if(motorGucu.equals("1.5 kW")) {
                    generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaMotor220.get("4"), "Motor Parçaları");
                } else if(motorGucu.equals("2.2 kW")) {
                    generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaMotor220.get("5"), "Motor Parçaları");
                } else if(motorGucu.equals("3 kW")) {
                    generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaMotor220.get("6"), "Motor Parçaları");
                }
            }
        }
    }

    private void loadPompaParca() {
        String pompaDegeri = PowerPackController.secilenPompa.trim();

        if(PowerPackController.uniteTipiDurumu.equals("Hidros")) {
            //Hidros Malzeme Listesi
            if(pompaDegeri.equals("0.8 cc")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaPompa.get("0"), "Pompa Parçaları");
            } else if(pompaDegeri.equals("1.1 cc")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaPompa.get("1"), "Pompa Parçaları");
            } else if(pompaDegeri.equals("1.3 cc")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaPompa.get("2"), "Pompa Parçaları");
            } else if(pompaDegeri.equals("1.8 cc")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaPompa.get("3"), "Pompa Parçaları");
            } else if(pompaDegeri.equals("2.1 cc")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaPompa.get("4"), "Pompa Parçaları");
            } else if(pompaDegeri.equals("2.7 cc")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaPompa.get("5"), "Pompa Parçaları");
            } else if(pompaDegeri.equals("3.2 cc")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaPompa.get("6"), "Pompa Parçaları");
            } else if(pompaDegeri.equals("3.7 cc")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaPompa.get("7"), "Pompa Parçaları");
            } else if(pompaDegeri.equals("4.2 cc")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaPompa.get("8"), "Pompa Parçaları");
            } else if(pompaDegeri.equals("4.8 cc")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaPompa.get("9"), "Pompa Parçaları");
            } else if(pompaDegeri.equals("5.8 cc")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaPompa.get("10"), "Pompa Parçaları");
            } else if(pompaDegeri.equals("7 cc")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaPompa.get("11"), "Pompa Parçaları");
            } else if(pompaDegeri.equals("8 cc")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaPompa.get("12"), "Pompa Parçaları");
            } else if(pompaDegeri.equals("9 cc")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaPompa.get("13"), "Pompa Parçaları");
            }
        } else {
            //İthal Malzeme Listesi
            if(pompaDegeri.equals("0.8 cc")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaPompa.get("0"), "Pompa Parçaları");
            } else if(pompaDegeri.equals("1.1 cc")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaPompa.get("1"), "Pompa Parçaları");
            } else if(pompaDegeri.equals("1.3 cc")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaPompa.get("2"), "Pompa Parçaları");
            } else if(pompaDegeri.equals("1.8 cc")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaPompa.get("3"), "Pompa Parçaları");
            } else if(pompaDegeri.equals("2.1 cc")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaPompa.get("4"), "Pompa Parçaları");
            } else if(pompaDegeri.equals("2.7 cc")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaPompa.get("5"), "Pompa Parçaları");
            } else if(pompaDegeri.equals("3.2 cc")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaPompa.get("6"), "Pompa Parçaları");
            } else if(pompaDegeri.equals("3.7 cc")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaPompa.get("7"), "Pompa Parçaları");
            } else if(pompaDegeri.equals("4.2 cc")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaPompa.get("8"), "Pompa Parçaları");
            } else if(pompaDegeri.equals("4.8 cc")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaPompa.get("9"), "Pompa Parçaları");
            } else if(pompaDegeri.equals("5.8 cc")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaPompa.get("10"), "Pompa Parçaları");
            } else if(pompaDegeri.equals("7 cc")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaPompa.get("11"), "Pompa Parçaları");
            } else if(pompaDegeri.equals("8 cc")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaPompa.get("12"), "Pompa Parçaları");
            } else if(pompaDegeri.equals("9 cc")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaPompa.get("13"), "Pompa Parçaları");
            } else if(pompaDegeri.equals("9.8 cc")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaPompa.get("14"), "Pompa Parçaları");
            }
        }
    }

    private void loadTankTipi() {
        String kontrolTankTipi = secilenTankTipi.trim();

        if(secilenTankTipi.contains("Özel")) {
            ParcaTableData separatorData = new ParcaTableData("----", "Tank Parçaları", "----");
            parcaListesiTablo.getItems().add(separatorData);

            String malzemeKodu = "Özel Tank";
            String malzemeAdi = "Genişlik: " + PowerPackController.secilenOzelTankGenislik + "mm" + " Yükseklik: " + PowerPackController.secilenOzelTankYukseklik + "mm" + " Derinlik: " + PowerPackController.secilenOzelTankDerinlik + "mm";
            String adet = "1";

            ParcaTableData data = new ParcaTableData(malzemeKodu, malzemeAdi, adet);
            parcaListesiTablo.getItems().add(data);
        } else {
            String kontrolTankKapasitesi = PowerPackController.secilenTankKapasitesi.trim();

            if(PowerPackController.uniteTipiDurumu.equals("Hidros")) {
                //Hidros Malzeme Listesi
                if(Objects.equals(kontrolTankTipi, "Yatay")) {
                    if(kontrolTankKapasitesi.equals("2 Lt")) {
                        generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaTankYatay.get("0"), "Tank Parçaları");
                    } else if(kontrolTankKapasitesi.equals("4 Lt")) {
                        generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaTankYatay.get("1"), "Tank Parçaları");
                    } else if(kontrolTankKapasitesi.equals("6 Lt")) {
                        generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaTankYatay.get("2"), "Tank Parçaları");
                    } else if(kontrolTankKapasitesi.equals("8 Lt")) {
                        generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaTankYatay.get("3"), "Tank Parçaları");
                    } else if(kontrolTankKapasitesi.equals("10 Lt")) {
                        generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaTankYatay.get("4"), "Tank Parçaları");
                    } else if(kontrolTankKapasitesi.equals("12 Lt")) {
                        generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaTankYatay.get("5"), "Tank Parçaları");
                    } else if(kontrolTankKapasitesi.equals("20 Lt")) {
                        generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaTankYatay.get("6"), "Tank Parçaları");
                    }
                } else if(Objects.equals(kontrolTankTipi, "Dikey")) {
                    if(kontrolTankKapasitesi.equals("4 Lt")) {
                        generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaTankDikey.get("0"), "Tank Parçaları");
                    } else if(kontrolTankKapasitesi.equals("6 Lt")) {
                        generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaTankDikey.get("1"), "Tank Parçaları");
                    } else if(kontrolTankKapasitesi.equals("8 Lt")) {
                        generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaTankDikey.get("2"), "Tank Parçaları");
                    } else if(kontrolTankKapasitesi.equals("10 Lt")) {
                        generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaTankDikey.get("3"), "Tank Parçaları");
                    } else if(kontrolTankKapasitesi.equals("12 Lt")) {
                        generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaTankDikey.get("4"), "Tank Parçaları");
                    } else if(kontrolTankKapasitesi.equals("20 Lt")) {
                        generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaTankDikey.get("5"), "Tank Parçaları");
                    }
                }
            } else {
                //İthal Malzeme Listesi
                if(Objects.equals(kontrolTankTipi, "Yatay")) {
                    if(kontrolTankKapasitesi.equals("2.5 Lt")) {
                        generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaTankYatay.get("0"), "Tank Parçaları");
                    } else if(kontrolTankKapasitesi.equals("3.8 Lt")) {
                        generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaTankYatay.get("1"), "Tank Parçaları");
                    } else if(kontrolTankKapasitesi.equals("7 Lt")) {
                        generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaTankYatay.get("2"), "Tank Parçaları");
                    } else if(kontrolTankKapasitesi.equals("10 Lt")) {
                        generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaTankYatay.get("3"), "Tank Parçaları");
                    }
                } else if(Objects.equals(kontrolTankTipi, "Dikey")) {
                    if(kontrolTankKapasitesi.equals("2.5 Lt")) {
                        generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaTankDikey.get("0"), "Tank Parçaları");
                    } else if(kontrolTankKapasitesi.equals("3.8 Lt")) {
                        generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaTankDikey.get("1"), "Tank Parçaları");
                    } else if(kontrolTankKapasitesi.equals("7 Lt")) {
                        generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaTankDikey.get("2"), "Tank Parçaları");
                    } else if(kontrolTankKapasitesi.equals("10 Lt")) {
                        generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaTankDikey.get("3"), "Tank Parçaları");
                    } else if(kontrolTankKapasitesi.equals("12 Lt")) {
                        generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaTankDikey.get("4"), "Tank Parçaları");
                    } else if(kontrolTankKapasitesi.equals("15 Lt")) {
                        generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaTankDikey.get("5"), "Tank Parçaları");
                    } else if(kontrolTankKapasitesi.equals("20 Lt")) {
                        generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaTankDikey.get("6"), "Tank Parçaları");
                    } else if(kontrolTankKapasitesi.equals("30 Lt")) {
                        generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaTankDikey.get("7"), "Tank Parçaları");
                    }
                }
            }
        }
    }

    private void loadPlatformTipi() {
        String secilenPlatform = PowerPackController.secilenPlatformTipi.trim();

        if(PowerPackController.uniteTipiDurumu.equals("Hidros")) {
            //Hidros Malzeme Listesi
            if(Objects.equals(secilenPlatform, "ESP")) {
                String secilenTank = secilenTankTipi.trim();

                if(secilenTank.contains("Dikey")) {
                    String secilenInis = PowerPackController.secilenInisTipi.trim();

                    if(Objects.equals(secilenInis, "İnişte Tek Hız")) {
                        generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaESPGenel.get("0"), "Platform Parçaları");
                    } else if(Objects.equals(secilenInis, "İnişte Çift Hız")) {
                        generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaESPGenel.get("0"), "Platform Parçaları");
                        generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaESPCiftHiz.get("0"), "Platform Parçaları");
                    }
                } else if(secilenTank.contains("Yatay")) {
                    String secilenInis = PowerPackController.secilenInisTipi.trim();

                    if(Objects.equals(secilenInis, "İnişte Tek Hız")) {
                        generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaESPGenel.get("0"), "Platform Parçaları");
                    } else if(Objects.equals(secilenInis, "İnişte Çift Hız")) {
                        generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaESPGenel.get("0"), "Platform Parçaları");
                        generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaESPCiftHiz.get("0"), "Platform Parçaları");
                    }
                }
            } else if(Objects.equals(secilenPlatform, "Devirmeli + Yürüyüş")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaDevirmeli.get("0"), "Platform Parçaları");
            }
        } else {
            //İthal Malzeme Listesi
            if(Objects.equals(secilenPlatform, "ESP")) {
                String secilenTank = secilenTankTipi.trim();

                if(secilenTank.contains("Dikey")) {
                    String secilenInis = PowerPackController.secilenInisTipi.trim();

                    if(Objects.equals(secilenInis, "İnişte Tek Hız")) {
                        generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaESPGenel.get("0"), "Platform Parçaları");
                    } else if(Objects.equals(secilenInis, "İnişte Çift Hız")) {
                        generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaESPGenel.get("0"), "Platform Parçaları");
                        generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaESPCiftHiz.get("0"), "Platform Parçaları");
                    }
                } else if(secilenTank.contains("Yatay")) {
                    String secilenInis = PowerPackController.secilenInisTipi.trim();

                    if(Objects.equals(secilenInis, "İnişte Tek Hız")) {
                        generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaESPGenel.get("0"), "Platform Parçaları");
                    } else if(Objects.equals(secilenInis, "İnişte Çift Hız")) {
                        generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaESPGenel.get("0"), "Platform Parçaları");
                        generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaESPCiftHiz.get("0"), "Platform Parçaları");
                    }
                }
            } else if(Objects.equals(secilenPlatform, "Devirmeli + Yürüyüş")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaDevirmeli.get("0"), "Platform Parçaları");
            }
        }
    }

    private void loadManometre() {
        ParcaTableData separatorData = new ParcaTableData("----", "Manometre Parçaları", "----");
        parcaListesiTablo.getItems().add(separatorData);

        if(Objects.equals(manometreDurumu, "Var")) {
            String malzemeKodu = "150-51-10-802";
            String secilenMalzeme = "Manometre";
            String adet = "1";

            ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
            parcaListesiTablo.getItems().add(data);
        }
    }

    private void loadBasincSalteri() {
        ParcaTableData separatorData = new ParcaTableData("----", "Basınç Şalteri Parçaları", "----");
        parcaListesiTablo.getItems().add(separatorData);

        if(Objects.equals(basincSalteriDurumu, "Var")) {
            String malzemeKodu = "150-51-10-457";
            String secilenMalzeme = "Basınç Şalteri";
            String adet = "1";

            ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
            parcaListesiTablo.getItems().add(data);
        }
    }

    private void loadElPompasiParca() {
        ParcaTableData separatorData = new ParcaTableData("----", "El Pompası Parçaları", "----");
        parcaListesiTablo.getItems().add(separatorData);

        if(Objects.equals(elPompasiDurumu, "Var")) {
            String malzemeKodu = "150-51-05-007";
            String secilenMalzeme = "A11 EL POMPALI BLOK V BLOK";
            String adet = "1";

            String malzemeKodu2 = "150-51-05-059";
            String secilenMalzeme2 = "A01 BLOK";
            String adet2 = "1";

            ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
            ParcaTableData data2 = new ParcaTableData(malzemeKodu2, secilenMalzeme2, adet2);
            parcaListesiTablo.getItems().add(data);
            parcaListesiTablo.getItems().add(data2);
        }
    }

    private void loadGenelParcalar() {
        if(PowerPackController.uniteTipiDurumu.equals("Hidros")) {
            //Hidros Malzeme Listesi
            generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaDefault.get("0"), "Standart Parçalar");
        } else {
            //İthal Malzeme Listesi
            generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaDefault.get("0"), "Standart Parçalar");
        }
    }

    private void loadOzelYatayGenel() {
        if(PowerPackController.uniteTipiDurumu.equals("Hidros")) {
            //Hidros Malzeme Listesi
            generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaOzelYatayGenel.get("0"), "Özel Yatay Genel Parçalar");
        } else {
            //İthal Malzeme Listesi
            generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaOzelYatayGenel.get("0"), "Özel Yatay Genel Parçalar");
        }
    }

    private void loadYagMiktari() {
        ParcaTableData separatorData = new ParcaTableData("----", "Hidrolik Yağ Parçaları", "----");
        parcaListesiTablo.getItems().add(separatorData);

        String tankKapasite = PowerPackController.secilenTankKapasitesi.trim();

        String malzemeKodu = "150-53-04-002";
        String malzemeAdi = "HİDROLİK YAĞ SHELL TELLUS S2 M46";
        String adet = "";

        if(tankKapasite.equals("2 Lt")) {
            adet = "2 Lt";
        } else if(tankKapasite.equals("4 Lt")) {
            adet = "4 Lt";
        } else if(tankKapasite.equals("6 Lt")) {
            adet = "6 Lt";
        } else if(tankKapasite.equals("8 Lt")) {
            adet = "8 Lt";
        } else if(tankKapasite.equals("10 Lt")) {
            adet = "10 Lt";
        } else if(tankKapasite.equals("12 Lt")) {
            adet = "12 Lt";
        } else if(tankKapasite.equals("20 Lt")) {
            adet = "20 Lt";
        }

        ParcaTableData data = new ParcaTableData(malzemeKodu, malzemeAdi, adet);
        parcaListesiTablo.getItems().add(data);
    }

    private void loadStockCodes() {
        String adet = "1";
        List<ParcaTableData> dataList;

        if(PowerPackController.atananKabin.equals("Özel Kabin")) {
            dataList = Arrays.asList(
                    new ParcaTableData("----", "Kabin Genel Bilgisi", "----"),
                    new ParcaTableData("----", PowerPackController.atananKabin, adet)
            );
        } else {
            Kabin foundedTank = Utils.findPowerPackTankByKabinName(PowerPackController.atananKabin);
             dataList = Arrays.asList(
                    new ParcaTableData("----", "Kabin Genel Bilgisi", "----"),
                    new ParcaTableData(foundedTank.getKabinKodu(), foundedTank.getMalzemeAdi(), adet)
            );
        }

        parcaListesiTablo.getItems().addAll(dataList);
    }

    private void loadValfParcalar() {
        if(PowerPackController.uniteTipiDurumu.equals("Hidros")) {
            //Hidros Malzeme Listesi
            if(PowerPackController.secilenPlatformTipi.contains("Özel")) {
                if(PowerPackController.secilenBirinciValf.equals("1")) {
                    if(PowerPackController.secilenIkinciValf.equals("Açık Merkez")) {
                        if(!PowerPackController.secilenMotorTipi.contains("12 V")) {
                            generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaValf.get("0"), "Valf Parçaları");
                        } else {
                            generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaValf.get("1"), "Valf Parçaları");
                        }
                    } else if(PowerPackController.secilenIkinciValf.equals("J Merkez")) {
                        if(!PowerPackController.secilenMotorTipi.contains("12 V")) {
                            generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaValf.get("2"), "Valf Parçaları");
                        } else {
                            generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaValf.get("3"), "Valf Parçaları");
                        }
                    } else if(PowerPackController.secilenIkinciValf.equals("H Merkez")) {
                        if(!PowerPackController.secilenMotorTipi.contains("12 V")) {
                            generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaValf.get("4"), "Valf Parçaları");
                        } else {
                            generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaValf.get("5"), "Valf Parçaları");
                        }
                    }
                } else {
                    loadOzelCiftValf();
                }
            } else {
                if(PowerPackController.secilenBirinciValf.equals("Açık Merkez")) {
                    if(!PowerPackController.secilenMotorTipi.contains("12 V")) {
                        generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaValf.get("0"), "Valf Parçaları");
                    } else {
                        generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaValf.get("1"), "Valf Parçaları");
                    }
                } else if(PowerPackController.secilenBirinciValf.equals("J Merkez")) {
                    if(!PowerPackController.secilenMotorTipi.contains("12 V")) {
                        generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaValf.get("2"), "Valf Parçaları");
                    } else {
                        generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaValf.get("3"), "Valf Parçaları");
                    }
                } else if(PowerPackController.secilenBirinciValf.equals("H Merkez")) {
                    if(!PowerPackController.secilenMotorTipi.contains("12 V")) {
                        generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaValf.get("4"), "Valf Parçaları");
                    } else {
                        generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaValf.get("5"), "Valf Parçaları");
                    }
                }

                if(PowerPackController.secilenIkinciValf != null) {
                    if(PowerPackController.secilenIkinciValf.equals("Açık Merkez")) {
                        if(!PowerPackController.secilenMotorTipi.contains("12 V")) {
                            generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaValf.get("0"), "Valf Parçaları");
                        } else {
                            generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaValf.get("1"), "Valf Parçaları");
                        }
                    } else if(PowerPackController.secilenIkinciValf.equals("J Merkez")) {
                        if(!PowerPackController.secilenMotorTipi.contains("12 V")) {
                            generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaValf.get("2"), "Valf Parçaları");
                        } else {
                            generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaValf.get("3"), "Valf Parçaları");
                        }
                    } else if(PowerPackController.secilenIkinciValf.equals("H Merkez")) {
                        if(!PowerPackController.secilenMotorTipi.contains("12 V")) {
                            generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaValf.get("4"), "Valf Parçaları");
                        } else {
                            generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaValf.get("5"), "Valf Parçaları");
                        }
                    }
                }
            }
        } else {
            //İthal Malzeme Listesi
            if(PowerPackController.secilenPlatformTipi.contains("Özel")) {
                if(PowerPackController.secilenBirinciValf.equals("1")) {
                    if(PowerPackController.secilenIkinciValf.equals("Açık Merkez")) {
                        if(!PowerPackController.secilenMotorTipi.contains("12 V")) {
                            generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaValf.get("0"), "Valf Parçaları");
                        } else {
                            generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaValf.get("1"), "Valf Parçaları");
                        }
                    } else if(PowerPackController.secilenIkinciValf.equals("J Merkez")) {
                        if(!PowerPackController.secilenMotorTipi.contains("12 V")) {
                            generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaValf.get("2"), "Valf Parçaları");
                        } else {
                            generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaValf.get("3"), "Valf Parçaları");
                        }
                    } else if(PowerPackController.secilenIkinciValf.equals("H Merkez")) {
                        if(!PowerPackController.secilenMotorTipi.contains("12 V")) {
                            generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaValf.get("4"), "Valf Parçaları");
                        } else {
                            generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaValf.get("5"), "Valf Parçaları");
                        }
                    }
                } else {
                    loadOzelCiftValf();
                }
            } else {
                if(PowerPackController.secilenBirinciValf.equals("Açık Merkez")) {
                    if(!PowerPackController.secilenMotorTipi.contains("12 V")) {
                        generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaValf.get("0"), "Valf Parçaları");
                    } else {
                        generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaValf.get("1"), "Valf Parçaları");
                    }
                } else if(PowerPackController.secilenBirinciValf.equals("J Merkez")) {
                    if(!PowerPackController.secilenMotorTipi.contains("12 V")) {
                        generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaValf.get("2"), "Valf Parçaları");
                    } else {
                        generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaValf.get("3"), "Valf Parçaları");
                    }
                } else if(PowerPackController.secilenBirinciValf.equals("H Merkez")) {
                    if(!PowerPackController.secilenMotorTipi.contains("12 V")) {
                        generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaValf.get("4"), "Valf Parçaları");
                    } else {
                        generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaValf.get("5"), "Valf Parçaları");
                    }
                }

                if(PowerPackController.secilenIkinciValf != null) {
                    if(PowerPackController.secilenIkinciValf.equals("Açık Merkez")) {
                        if(!PowerPackController.secilenMotorTipi.contains("12 V")) {
                            generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaValf.get("0"), "Valf Parçaları");
                        } else {
                            generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaValf.get("1"), "Valf Parçaları");
                        }
                    } else if(PowerPackController.secilenIkinciValf.equals("J Merkez")) {
                        if(!PowerPackController.secilenMotorTipi.contains("12 V")) {
                            generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaValf.get("2"), "Valf Parçaları");
                        } else {
                            generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaValf.get("3"), "Valf Parçaları");
                        }
                    } else if(PowerPackController.secilenIkinciValf.equals("H Merkez")) {
                        if(!PowerPackController.secilenMotorTipi.contains("12 V")) {
                            generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaValf.get("4"), "Valf Parçaları");
                        } else {
                            generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaValf.get("5"), "Valf Parçaları");
                        }
                    }
                }
            }
        }
    }

    private void loadOzelCiftValf() {
        if(PowerPackController.uniteTipiDurumu.equals("Hidros")) {
            //Hidros Malzeme Listesi
            generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackHidrosParcaOzelCiftValf.get("0"), "Özel Çift Valf Parçaları");
        } else {
            //İthal Malzeme Listesi
            generalLoadFunc(SystemVariables.getLocalHydraulicData().powerPackIthalParcaOzelCiftValf.get("0"), "Özel Çift Valf Parçaları");
        }
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

        parcaListesiTablo.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(ParcaTableData item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else {
                    if (item.getMalzemeKoduProperty().equals("----") && item.getMalzemeAdetProperty().equals("----")) {
                        setStyle("-fx-background-color: #F9F871; -fx-text-fill: black;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });
    }
}