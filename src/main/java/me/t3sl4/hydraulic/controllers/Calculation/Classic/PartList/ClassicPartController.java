package me.t3sl4.hydraulic.controllers.Calculation.Classic.PartList;

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
import me.t3sl4.hydraulic.controllers.Calculation.Classic.ClassicController;
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

public class ClassicPartController {
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
        String excelFileName = SystemVariables.excelFileLocalPath + ClassicController.girilenSiparisNumarasi + ".xlsx";

        Multimap<String, ParcaTableData> malzemeMultimap = LinkedListMultimap.create();
        Multimap<String, ParcaTableData> filteredMultimap = LinkedListMultimap.create();
        Multimap<String, ParcaTableData> duplicateMultimap = LinkedListMultimap.create();

        for (ParcaTableData rowData : veriler) {
            if (!(rowData.getMalzemeKoduProperty().equals("----") && rowData.getMalzemeAdetProperty().equals("----"))) {
                String malzemeKey = rowData.getMalzemeKoduProperty();
                String mapKey = rowData.getMalzemeKoduProperty() + " " + rowData.getMalzemeAdiProperty();

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
                jsonObject.put("Ünite Tipi", ClassicController.secilenUniteTipi);
                jsonObject.put("Sipariş Numarası", ClassicController.girilenSiparisNumarasi);
                jsonObject.put("Motor", ClassicController.secilenMotor);
                jsonObject.put("Soğutma", ClassicController.secilenSogutmaDurumu);
                jsonObject.put("Hidrolik Kilit", ClassicController.secilenHidrolikKilitDurumu);
                jsonObject.put("Pompa", ClassicController.secilenPompa);
                jsonObject.put("Gerekli Yağ Miktarı", ClassicController.girilenTankKapasitesiMiktari);
                jsonObject.put("Kompanzasyon", ClassicController.kompanzasyonDurumu);
                jsonObject.put("Valf Tipi", ClassicController.secilenValfTipi);
                jsonObject.put("Kilit Motor", ClassicController.secilenKilitMotor);
                jsonObject.put("Kilit Pompa", ClassicController.secilenKilitPompa);
                jsonObject.put("Seçilen Kampana", ClassicController.secilenKampana);
                jsonObject.put("Seçilen Pompa Val", ClassicController.secilenPompaVal);

                if(SystemVariables.loggedInUser != null) {
                    Utils.createLocalUnitData(SystemVariables.localHydraulicStatsPath,
                            ClassicController.girilenSiparisNumarasi,
                            Utils.getCurrentUnixTime(),
                            ClassicController.secilenUniteTipi,
                            null,
                            excelFileName,
                            "no",
                            SystemVariables.loggedInUser.getUserID(),
                            jsonObject);
                } else {
                    Utils.createLocalUnitData(SystemVariables.localHydraulicStatsPath,
                            ClassicController.girilenSiparisNumarasi,
                            Utils.getCurrentUnixTime(),
                            ClassicController.secilenUniteTipi,
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
        if(ClassicController.secilenSogutmaDurumu.equals("Yok")) {
            loadStockCodes();
        }

        loadMotorParca();
        loadKampanaParca();
        loadPompaParca();
        loadKaplinParca();
        loadValfBlokParca();

        if(ClassicController.secilenKilitMotor != null) {
            loadKilitMotorParca();
        }

        loadStandartParca();
        if(ClassicController.secilenSogutmaDurumu.contains("Var")) {
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

        Kabin foundedTank = Utils.findClassicTankByKabinName(ClassicController.atananKabinFinal);
        List<ParcaTableData> dataList = Arrays.asList(
                new ParcaTableData("----", "Kabin Genel Bilgisi", "----"),
                new ParcaTableData(foundedTank.getKabinKodu(), foundedTank.getMalzemeAdi(), adet),
                new ParcaTableData(foundedTank.getYagTankiKodu(), foundedTank.getTankName(), adet)
        );

        parcaListesiTablo.getItems().addAll(dataList);
    }

    private void loadKampanaParca() {
        if(ClassicController.secilenPompaVal >= 33.3) {
            if(ClassicController.secilenKampana == 250) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaKampana.get("4"), "Kampana Parçaları");
            } else if(ClassicController.secilenKampana == 300) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaKampana.get("5"), "Kampana Parçaları");
            } else if(ClassicController.secilenKampana == 350) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaKampana.get("6"), "Kampana Parçaları");
            } else if(ClassicController.secilenKampana == 400) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaKampana.get("7"), "Kampana Parçaları");
            }
        } else {
            if(ClassicController.secilenKampana == 250) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaKampana.get("0"), "Kampana Parçaları");
            } else if(ClassicController.secilenKampana == 300) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaKampana.get("1"), "Kampana Parçaları");
            } else if(ClassicController.secilenKampana == 350) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaKampana.get("2"), "Kampana Parçaları");
            } else if(ClassicController.secilenKampana == 400) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaKampana.get("3"), "Kampana Parçaları");
            }
        }
    }

    private void loadPompaParca() {
        if(Objects.equals(ClassicController.secilenPompa, "9.5 cc")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaPompa.get("0"), "Pompa Parçaları");
        } else if(Objects.equals(ClassicController.secilenPompa, "11.9 cc")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaPompa.get("1"), "Pompa Parçaları");
        } else if(Objects.equals(ClassicController.secilenPompa, "14 cc")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaPompa.get("2"), "Pompa Parçaları");
        } else if(Objects.equals(ClassicController.secilenPompa, "14.6 cc")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaPompa.get("3"), "Pompa Parçaları");
        } else if(Objects.equals(ClassicController.secilenPompa, "16.8 cc")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaPompa.get("4"), "Pompa Parçaları");
        } else if(Objects.equals(ClassicController.secilenPompa, "19.2 cc")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaPompa.get("5"), "Pompa Parçaları");
        } else if(Objects.equals(ClassicController.secilenPompa, "22.9 cc")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaPompa.get("6"), "Pompa Parçaları");
        } else if(Objects.equals(ClassicController.secilenPompa, "28.1 cc")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaPompa.get("7"), "Pompa Parçaları");
        } else if(Objects.equals(ClassicController.secilenPompa, "28.8 cc")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaPompa.get("8"), "Pompa Parçaları");
        } else if(Objects.equals(ClassicController.secilenPompa, "33.3 cc")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaPompa.get("9"), "Pompa Parçaları");
        } else if(Objects.equals(ClassicController.secilenPompa, "37.9 cc")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaPompa.get("10"), "Pompa Parçaları");
        } else if(Objects.equals(ClassicController.secilenPompa, "42.6 cc")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaPompa.get("11"), "Pompa Parçaları");
        } else if(Objects.equals(ClassicController.secilenPompa, "45.5 cc")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaPompa.get("12"), "Pompa Parçaları");
        } else if(Objects.equals(ClassicController.secilenPompa, "49.4 cc")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaPompa.get("13"), "Pompa Parçaları");
        } else if(Objects.equals(ClassicController.secilenPompa, "56.1 cc")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaPompa.get("14"), "Pompa Parçaları");
        }
    }

    private void loadMotorParca() {
        if(Objects.equals(ClassicController.secilenMotor, "2.2 kW")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaMotor.get("0"), "Motor Parçaları");
        } else if(Objects.equals(ClassicController.secilenMotor, "3 kW")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaMotor.get("1"), "Motor Parçaları");
        } else if(Objects.equals(ClassicController.secilenMotor, "4 kW")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaMotor.get("2"), "Motor Parçaları");
        } else if(Objects.equals(ClassicController.secilenMotor, "5.5 kW")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaMotor.get("3"), "Motor Parçaları");
        } else if(Objects.equals(ClassicController.secilenMotor, "5.5 kW (Kompakt)")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaMotor.get("4"), "Motor Parçaları");
        } else if(Objects.equals(ClassicController.secilenMotor, "7.5 kW (Kompakt)")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaMotor.get("5"), "Motor Parçaları");
        } else if(Objects.equals(ClassicController.secilenMotor, "11 kW")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaMotor.get("6"), "Motor Parçaları");
        } else if(Objects.equals(ClassicController.secilenMotor, "11 kW (Kompakt)")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaMotor.get("7"), "Motor Parçaları");
        } else if(Objects.equals(ClassicController.secilenMotor, "15 kW")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaMotor.get("8"), "Motor Parçaları");
        } else if(Objects.equals(ClassicController.secilenMotor, "18.5 kW")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaMotor.get("9"), "Motor Parçaları");
        } else if(Objects.equals(ClassicController.secilenMotor, "22 kW")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaMotor.get("10"), "Motor Parçaları");
        } else if(Objects.equals(ClassicController.secilenMotor, "37 kW")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaMotor.get("11"), "Motor Parçaları");
        }
    }

    private void loadKaplinParca() {
        String[] secPmp = ClassicController.secilenPompa.split(" cc");
        float secilenPompaVal = Float.parseFloat(secPmp[0]);

        if(secilenPompaVal < 33.3) {
            if(Objects.equals(ClassicController.secilenMotor, "2.2 kW") || Objects.equals(ClassicController.secilenMotor, "3 kW") || Objects.equals(ClassicController.secilenMotor, "4 kW") || Objects.equals(ClassicController.secilenMotor, "5.5 kW") || Objects.equals(ClassicController.secilenMotor, "5.5 kW (Kompakt)")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaKaplin.get("0"), "Kaplin Parçaları");
            } else if(Objects.equals(ClassicController.secilenMotor, "7.5 kW (Kompakt)") || Objects.equals(ClassicController.secilenMotor, "11 kW") || Objects.equals(ClassicController.secilenMotor, "11 kW (Kompakt)")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaKaplin.get("1"), "Kaplin Parçaları");
            } else if(Objects.equals(ClassicController.secilenMotor, "15 kW") || Objects.equals(ClassicController.secilenMotor, "18.5 kW") || Objects.equals(ClassicController.secilenMotor, "22 kW") || Objects.equals(ClassicController.secilenMotor, "37 kW")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaKaplin.get("2"), "Kaplin Parçaları");
            }
        } else {
            if(Objects.equals(ClassicController.secilenMotor, "2.2 kW") || Objects.equals(ClassicController.secilenMotor, "3 kW") || Objects.equals(ClassicController.secilenMotor, "4 kW") || Objects.equals(ClassicController.secilenMotor, "5.5 kW") || Objects.equals(ClassicController.secilenMotor, "5.5 kW (Kompakt)")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaKaplin.get("3"), "Kaplin Parçaları");
            } else if(Objects.equals(ClassicController.secilenMotor, "7.5 kW (Kompakt)") || Objects.equals(ClassicController.secilenMotor, "11 kW") || Objects.equals(ClassicController.secilenMotor, "11 kW (Kompakt)")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaKaplin.get("4"), "Kaplin Parçaları");
            } else if(Objects.equals(ClassicController.secilenMotor, "15 kW") || Objects.equals(ClassicController.secilenMotor, "18.5 kW") || Objects.equals(ClassicController.secilenMotor, "22 kW") || Objects.equals(ClassicController.secilenMotor, "37 kW")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaKaplin.get("5"), "Kaplin Parçaları");
            }
        }
    }

    private void loadValfBlokParca() {
        if(ClassicController.secilenPompaVal < 33.3) {
            //1 Grubu
            if(Objects.equals(ClassicController.secilenValfTipi, "İnişte Tek Hız")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaValfBloklari.get("0"), "Valf Blok Parçaları");
            } else if(Objects.equals(ClassicController.secilenValfTipi, "İnişte Çift Hız")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaValfBloklari.get("1"), "Valf Blok Parçaları");
            } else if(Objects.equals(ClassicController.secilenValfTipi, "Kilitli Blok")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaValfBloklari.get("2"), "Valf Blok Parçaları");
            } else if(Objects.equals(ClassicController.secilenValfTipi, "Kompanzasyon || İnişte Tek Hız")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaValfBloklari.get("3"), "Valf Blok Parçaları");
            }
        } else {
            //2 Grubu
            if(Objects.equals(ClassicController.secilenValfTipi, "İnişte Tek Hız")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaValfBloklari.get("4"), "Valf Blok Parçaları");
            } else if(Objects.equals(ClassicController.secilenValfTipi, "İnişte Çift Hız")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaValfBloklari.get("5"), "Valf Blok Parçaları");
            } else if(Objects.equals(ClassicController.secilenValfTipi, "Kilitli Blok")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaValfBloklari.get("6"), "Valf Blok Parçaları");
            } else if(Objects.equals(ClassicController.secilenValfTipi, "Kompanzasyon || İnişte Tek Hız")) {
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
        if(Objects.equals(ClassicController.atananHT, "HT 40")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaDefault.get("0"), "Standart Parçalar");
        } else if(Objects.equals(ClassicController.atananHT, "HT 70")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaDefault.get("1"), "Standart Parçalar");
        } else if(Objects.equals(ClassicController.atananHT, "HT 100")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaDefault.get("2"), "Standart Parçalar");
        } else if(Objects.equals(ClassicController.atananHT, "HT 125")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaDefault.get("3"), "Standart Parçalar");
        } else if(Objects.equals(ClassicController.atananHT, "HT 160")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaDefault.get("4"), "Standart Parçalar");
        } else if(Objects.equals(ClassicController.atananHT, "HT 200")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaDefault.get("5"), "Standart Parçalar");
        } else if(Objects.equals(ClassicController.atananHT, "HT 250")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaDefault.get("6"), "Standart Parçalar");
        } else if(Objects.equals(ClassicController.atananHT, "HT 300")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaDefault.get("7"), "Standart Parçalar");
        } else if(Objects.equals(ClassicController.atananHT, "HT 350")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaDefault.get("8"), "Standart Parçalar");
        } else if(Objects.equals(ClassicController.atananHT, "HT 400")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaDefault.get("9"), "Standart Parçalar");
        } else if(Objects.equals(ClassicController.atananHT, "HT SOĞUTMA")) {
            generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaDefault.get("10"), "Standart Parçalar");
        }
    }

    private void loadSogutucuParca() {
        if(ClassicController.secilenHidrolikKilitDurumu.equals("Var")) {
            //Hidrolik Kilit Var
            if(ClassicController.secilenValfTipi.equals("İnişte Tek Hız") || ClassicController.secilenValfTipi.equals("Kompanzasyon || İnişte Tek Hız")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaSogutma.get("2"), "Soğutucu Parçaları");
            } else if(ClassicController.secilenValfTipi.equals("İnişte Çift Hız") || ClassicController.secilenValfTipi.equals("Kilitli Blok")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaSogutma.get("3"), "Soğutucu Parçaları");
            }
        } else {
            //Hidrolik Kilit Yok
            if(ClassicController.secilenValfTipi.equals("İnişte Tek Hız") || ClassicController.secilenValfTipi.equals("Kompanzasyon || İnişte Tek Hız")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaSogutma.get("0"), "Soğutucu Parçaları");
            } else if(ClassicController.secilenValfTipi.equals("İnişte Çift Hız") || ClassicController.secilenValfTipi.equals("Kilitli Blok")) {
                generalLoadFunc(SystemVariables.getLocalHydraulicData().classicParcaSogutma.get("1"), "Soğutucu Parçaları");
            }
        }
    }

    private void loadKilitMotorParca() {
        String kilitMotorVal = ClassicController.secilenKilitMotor;
        String kilitPompaVal = ClassicController.secilenKilitPompa;
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
        String adet = ClassicController.girilenTankKapasitesiMiktari + " Lt";

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