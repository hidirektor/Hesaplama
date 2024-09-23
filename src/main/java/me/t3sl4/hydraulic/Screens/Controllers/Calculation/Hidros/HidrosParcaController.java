package me.t3sl4.hydraulic.Screens.Controllers.Calculation.Hidros;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;
import me.t3sl4.hydraulic.Utils.Model.Table.ParcaTableData;
import me.t3sl4.hydraulic.Utils.File.ExcelDataReadUtil;
import me.t3sl4.hydraulic.Utils.Utils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HidrosParcaController {
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
        String secilenPlatform = HidrosController.secilenPlatformTipi.trim();
        if(HidrosController.uniteTipiDurumu.equals("Hidros")) {
            loadKabinKodu();
            loadMotorParca();
            loadPompaParca();
            loadPompaCivataParca();
            loadTankTipi();
            loadPlatformTipi();
            loadYagMiktari();
            loadManometre();
            loadBasincSalteri();
            loadElPompasiParca();
            loadGenelParcalar();
            if(Objects.equals(HidrosController.secilenTankTipi, "Yatay")) {
                loadGenelParcalarYatay();
            } else {
                loadGenelParcalarDikey();
            }
            loadESPHaricTam();
            if(Objects.equals(secilenPlatform, "Özel")) {
                loadOzelTekValf();
            }
        } else {
            //İthal Parçalar buraya
        }
    }

    private void loadMotorParca() {
        String voltajDegeri = HidrosController.secilenMotorTipi.replaceAll(" V$", "");
        String motorGucu = HidrosController.secilenMotorGucu.trim();

        if (Objects.equals(voltajDegeri, "380")) {
            String malzemeKodu = Utils.getStockCodeFromDoubleHashMap(ExcelDataReadUtil.dataManipulator.hidros380Parca, motorGucu);
            String secilenMalzeme = Utils.getMaterialFromDoubleHashMap(ExcelDataReadUtil.dataManipulator.hidros380Parca, motorGucu) + " Motor";

            String adet = Utils.float2String(Utils.getAmountFromDoubleHashMap(ExcelDataReadUtil.dataManipulator.hidros380Parca, motorGucu));

            ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
            parcaListesiTablo.getItems().add(data);
        } else if (Objects.equals(voltajDegeri, "220")) {
            String malzemeKodu = Utils.getStockCodeFromDoubleHashMap(ExcelDataReadUtil.dataManipulator.hidros220Parca, motorGucu);
            String secilenMalzeme = Utils.getMaterialFromDoubleHashMap(ExcelDataReadUtil.dataManipulator.hidros220Parca, motorGucu) + " Motor";

            String adet = Utils.float2String(Utils.getAmountFromDoubleHashMap(ExcelDataReadUtil.dataManipulator.hidros220Parca, motorGucu));

            ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
            parcaListesiTablo.getItems().add(data);
        }
    }

    private void loadPompaParca() {
        String pompaDegeri = HidrosController.secilenPompa.trim();

        String malzemeKodu = Utils.getStockCodeFromDoubleHashMap(ExcelDataReadUtil.dataManipulator.hidrosPompaParca, pompaDegeri);
        String secilenMalzeme = Utils.getMaterialFromDoubleHashMap(ExcelDataReadUtil.dataManipulator.hidrosPompaParca, pompaDegeri) + " Pompa";
        String adet = Utils.float2String(Utils.getAmountFromDoubleHashMap(ExcelDataReadUtil.dataManipulator.hidrosPompaParca, pompaDegeri));

        ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
        parcaListesiTablo.getItems().add(data);
    }

    private void loadPompaCivataParca() {
        String pompaDegeri = HidrosController.secilenPompa.trim();

        String malzemeKodu = Utils.getStockCodeFromDoubleHashMap(ExcelDataReadUtil.dataManipulator.hidrosPompaCivataParca, pompaDegeri);
        String secilenMalzeme = Utils.getMaterialFromDoubleHashMap(ExcelDataReadUtil.dataManipulator.hidrosPompaCivataParca, pompaDegeri) + " Pompa Civata";
        String adet = Utils.float2String(Utils.getAmountFromDoubleHashMap(ExcelDataReadUtil.dataManipulator.hidrosPompaCivataParca, pompaDegeri));

        ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
        parcaListesiTablo.getItems().add(data);
    }

    private void loadTankTipi() {
        String kontrolTankTipi = HidrosController.secilenTankTipi.trim();
        String kontrolTankKapasitesi = HidrosController.secilenTankKapasitesi.trim();

        if(Objects.equals(kontrolTankTipi, "Yatay")) {
            //ExcelUtil.dataManipulator.hidrosYatayTankParca
            String malzemeKodu = Utils.getStockCodeFromDoubleHashMap(ExcelDataReadUtil.dataManipulator.hidrosYatayTankParca, kontrolTankKapasitesi);
            String secilenMalzeme = Utils.getMaterialFromDoubleHashMap(ExcelDataReadUtil.dataManipulator.hidrosYatayTankParca, kontrolTankKapasitesi) + " Tank";
            String adet = Utils.float2String(Utils.getAmountFromDoubleHashMap(ExcelDataReadUtil.dataManipulator.hidrosYatayTankParca, kontrolTankKapasitesi));

            ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
            parcaListesiTablo.getItems().add(data);
        } else if(Objects.equals(kontrolTankTipi, "Dikey")) {
            //ExcelUtil.dataManipulator.hidrosDikeyTankParca
            String malzemeKodu = Utils.getStockCodeFromDoubleHashMap(ExcelDataReadUtil.dataManipulator.hidrosDikeyTankParca, kontrolTankKapasitesi);
            String secilenMalzeme = Utils.getMaterialFromDoubleHashMap(ExcelDataReadUtil.dataManipulator.hidrosDikeyTankParca, kontrolTankKapasitesi) + " Tank";
            String adet = Utils.float2String(Utils.getAmountFromDoubleHashMap(ExcelDataReadUtil.dataManipulator.hidrosDikeyTankParca, kontrolTankKapasitesi));

            ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
            parcaListesiTablo.getItems().add(data);
        }
    }

    private void loadPlatformTipi() {
        String secilenPlatform = HidrosController.secilenPlatformTipi.trim();

        if(Objects.equals(secilenPlatform, "ESP")) {
            String secilenTank = HidrosController.secilenTankTipi.trim();

            if(Objects.equals(secilenTank, "Dikey")) {
                String secilenInis = HidrosController.secilenInisTipi.trim();

                if(Objects.equals(secilenInis, "İnişte Tek Hız")) {
                    for (Map.Entry<String, HashMap<String, String>> entry : ExcelDataReadUtil.dataManipulator.hidrosDikeyTekHizParca.entrySet()) {
                        HashMap<String, String> innerMap = entry.getValue();

                        String malzemeKodu = innerMap.get("B");
                        String secilenMalzeme = entry.getKey();
                        String adet = Utils.float2String(innerMap.get("C"));

                        ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                        parcaListesiTablo.getItems().add(data);
                    }
                } else if(Objects.equals(secilenInis, "İnişte Çift Hız")) {
                    for (Map.Entry<String, HashMap<String, String>> entry : ExcelDataReadUtil.dataManipulator.hidrosDikeyCiftHizParcaESP.entrySet()) {
                        HashMap<String, String> innerMap = entry.getValue();

                        String malzemeKodu = innerMap.get("B");
                        String secilenMalzeme = entry.getKey();
                        String adet = Utils.float2String(innerMap.get("C"));

                        ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                        parcaListesiTablo.getItems().add(data);
                    }
                }
            } else if(Objects.equals(secilenTank, "Yatay")) {
                String secilenInis = HidrosController.secilenInisTipi.trim();

                if(Objects.equals(secilenInis, "İnişte Tek Hız")) {
                    for (Map.Entry<String, HashMap<String, String>> entry : ExcelDataReadUtil.dataManipulator.hidrosYatayTekHizParca.entrySet()) {
                        HashMap<String, String> innerMap = entry.getValue();

                        String malzemeKodu = innerMap.get("B");
                        String secilenMalzeme = entry.getKey();
                        String adet = Utils.float2String(innerMap.get("C"));

                        ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                        parcaListesiTablo.getItems().add(data);
                    }
                } else if(Objects.equals(secilenInis, "İnişte Çift Hız")) {
                    for (Map.Entry<String, HashMap<String, String>> entry : ExcelDataReadUtil.dataManipulator.hidrosYatayCiftHizParcaESP.entrySet()) {
                        HashMap<String, String> innerMap = entry.getValue();

                        String malzemeKodu = innerMap.get("B");
                        String secilenMalzeme = entry.getKey();
                        String adet = Utils.float2String(innerMap.get("C"));

                        ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                        parcaListesiTablo.getItems().add(data);
                    }
                }
            }
        } else if(Objects.equals(secilenPlatform, "Devirmeli")) {
            for(Map.Entry<String, HashMap<String, String>> entry : ExcelDataReadUtil.dataManipulator.hidrosDevirmeliParca.entrySet()) {
                HashMap<String, String> innerMap = entry.getValue();

                String malzemeKodu = innerMap.get("B");
                String secilenMalzeme = entry.getKey();
                String adet = Utils.float2String(innerMap.get("C"));

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(secilenPlatform, "Yürüyüş")) {
            //TODO
            //Yürüyüş için parça listesi eklenecek
        } else if(Objects.equals(secilenPlatform, "Özel")) {
            ArrayList<String> eklenecekParcaListesi = new ArrayList<>();
            if(!Objects.equals(HidrosController.secilenIkinciValf, "Yok")) {
                String eklenecekBirinciValfKodu = "";
                String eklenecekBirinciValfIsim = "";
                String eklenecekBirinciValfAdet = "";
                String eklenecekIkinciValfKodu = "";
                String eklenecekIkinciValfIsim = "";
                String eklenecekIkinciValfAdet = "";
                if(Objects.equals(HidrosController.secilenBirinciValf, "Açık Merkez")) {
                    eklenecekBirinciValfKodu = "150-51-04-025";
                    eklenecekBirinciValfIsim = "VALF AÇIK MERKEZ NG6 24V RH06021-24V";
                    eklenecekBirinciValfAdet = "1";
                } else if(Objects.equals(HidrosController.secilenBirinciValf, "Kapalı Merkez")) {
                    eklenecekBirinciValfKodu = "150-51-04-024";
                    eklenecekBirinciValfIsim = "VALF KAPALI MERKEZ NG6 24V";
                    eklenecekBirinciValfAdet = "1";
                } else if(Objects.equals(HidrosController.secilenBirinciValf, "H Merkez")) {
                    eklenecekBirinciValfKodu = "150-51-04-005";
                    eklenecekBirinciValfIsim = "VALF H MERKEZ NG6 24V RH06001-24V";
                    eklenecekBirinciValfAdet = "1";
                } else if(Objects.equals(HidrosController.secilenBirinciValf, "J Merkez")) {
                    eklenecekBirinciValfKodu = "150-51-04-004";
                    eklenecekBirinciValfIsim = "SELENOİD VALF J MERKEZ Z RH06041-24V";
                    eklenecekBirinciValfAdet = "1";
                }

                ParcaTableData birinciValfData = new ParcaTableData(eklenecekBirinciValfKodu, eklenecekBirinciValfIsim, eklenecekBirinciValfAdet);
                parcaListesiTablo.getItems().add(birinciValfData);

                if(Objects.equals(HidrosController.secilenIkinciValf, "Açık Merkez")) {
                    eklenecekIkinciValfKodu = "150-51-04-025";
                    eklenecekIkinciValfIsim = "VALF AÇIK MERKEZ NG6 24V RH06021-24V";
                    eklenecekIkinciValfAdet = "1";
                } else if(Objects.equals(HidrosController.secilenIkinciValf, "Kapalı Merkez")) {
                    eklenecekIkinciValfKodu = "150-51-04-024";
                    eklenecekIkinciValfIsim = "VALF KAPALI MERKEZ NG6 24V";
                    eklenecekIkinciValfAdet = "1";
                } else if(Objects.equals(HidrosController.secilenIkinciValf, "H Merkez")) {
                    eklenecekIkinciValfKodu = "150-51-04-005";
                    eklenecekIkinciValfIsim = "VALF H MERKEZ NG6 24V RH06001-24V";
                    eklenecekIkinciValfAdet = "1";
                } else if(Objects.equals(HidrosController.secilenIkinciValf, "J Merkez")) {
                    eklenecekIkinciValfKodu = "150-51-04-004";
                    eklenecekIkinciValfIsim = "SELENOİD VALF J MERKEZ Z RH06041-24V";
                    eklenecekIkinciValfAdet = "1";
                }

                ParcaTableData ikinciValfData = new ParcaTableData(eklenecekIkinciValfKodu, eklenecekIkinciValfIsim, eklenecekIkinciValfAdet);
                parcaListesiTablo.getItems().add(ikinciValfData);

                String ilkValf = "150-51-05-059;A01 BLOK;1";
                String ikinciValf = "150-51-05-060;A03 BLOK;1";

                eklenecekParcaListesi.add(0, ilkValf);
                eklenecekParcaListesi.add(1, ikinciValf);
                eklenecekParcaListesi.add(2, ikinciValf);

                for (String veri : eklenecekParcaListesi) {
                    String[] veriParcalari = veri.split(";");

                    String malzemeKodu = veriParcalari[0];
                    String secilenMalzeme = veriParcalari[1];
                    String adet = veriParcalari[2];

                    ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                    parcaListesiTablo.getItems().add(data);
                }
            } else {
                String eklenecekBirinciValfKodu = "";
                String eklenecekBirinciValfIsim = "";
                String eklenecekBirinciValfAdet = "";
                if(Objects.equals(HidrosController.secilenBirinciValf, "Açık Merkez")) {
                    eklenecekBirinciValfKodu = "150-51-04-025";
                    eklenecekBirinciValfIsim = "VALF AÇIK MERKEZ NG6 24V RH06021-24V";
                    eklenecekBirinciValfAdet = "1";
                } else if(Objects.equals(HidrosController.secilenBirinciValf, "Kapalı Merkez")) {
                    eklenecekBirinciValfKodu = "150-51-04-024";
                    eklenecekBirinciValfIsim = "VALF KAPALI MERKEZ NG6 24V";
                    eklenecekBirinciValfAdet = "1";
                } else if(Objects.equals(HidrosController.secilenBirinciValf, "H Merkez")) {
                    eklenecekBirinciValfKodu = "150-51-04-005";
                    eklenecekBirinciValfIsim = "VALF H MERKEZ NG6 24V RH06001-24V";
                    eklenecekBirinciValfAdet = "1";
                } else if(Objects.equals(HidrosController.secilenBirinciValf, "J Merkez")) {
                    eklenecekBirinciValfKodu = "150-51-04-004";
                    eklenecekBirinciValfIsim = "SELENOİD VALF J MERKEZ Z RH06041-24V";
                    eklenecekBirinciValfAdet = "1";
                }

                ParcaTableData birinciValfData = new ParcaTableData(eklenecekBirinciValfKodu, eklenecekBirinciValfIsim, eklenecekBirinciValfAdet);
                parcaListesiTablo.getItems().add(birinciValfData);

                String ilkValf = "150-51-05-059;A01 BLOK;1";
                String ikinciValf = "150-51-05-060;A03 BLOK;1";

                eklenecekParcaListesi.add(0, ilkValf);
                eklenecekParcaListesi.add(1, ikinciValf);

                for (String veri : eklenecekParcaListesi) {
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

    private void loadManometre() {
        if(Objects.equals(manometreDurumu, "Var")) {
            String malzemeKodu = "150-51-10-802";
            String secilenMalzeme = "Manometre";
            String adet = "1";

            ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
            parcaListesiTablo.getItems().add(data);
        }
    }

    private void loadBasincSalteri() {
        if(Objects.equals(basincSalteriDurumu, "Var")) {
            String malzemeKodu = "150-51-10-457";
            String secilenMalzeme = "Basınç Şalteri";
            String adet = "1";

            ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
            parcaListesiTablo.getItems().add(data);
        }
    }

    private void loadElPompasiParca() {
        if(Objects.equals(elPompasiDurumu, "Var")) {
            String malzemeKodu = "150-51-05-007";
            String secilenMalzeme = "A11 EL POMPALI BLOK V BLOK";
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
        for (Map.Entry<String, HashMap<String, String>> entry : ExcelDataReadUtil.dataManipulator.hidrosTamParca.entrySet()) {
            HashMap<String, String> innerMap = entry.getValue();

            String malzemeKodu = innerMap.get("B");
            String secilenMalzeme = entry.getKey();
            String adet = Utils.float2String(innerMap.get("C"));

            ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
            parcaListesiTablo.getItems().add(data);
        }
    }

    private void loadGenelParcalarYatay() {
        for (Map.Entry<String, HashMap<String, String>> entry : ExcelDataReadUtil.dataManipulator.hidrosTamParcaYatay.entrySet()) {
            HashMap<String, String> innerMap = entry.getValue();

            String malzemeKodu = innerMap.get("B");
            String secilenMalzeme = entry.getKey();
            String adet = Utils.float2String(innerMap.get("C"));

            ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
            parcaListesiTablo.getItems().add(data);
        }
    }

    private void loadGenelParcalarDikey() {
        for (Map.Entry<String, HashMap<String, String>> entry : ExcelDataReadUtil.dataManipulator.hidrosTamParcaDikey.entrySet()) {
            HashMap<String, String> innerMap = entry.getValue();

            String malzemeKodu = innerMap.get("B");
            String secilenMalzeme = entry.getKey();
            String adet = Utils.float2String(innerMap.get("C"));

            ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
            parcaListesiTablo.getItems().add(data);
        }
    }

    private void loadESPHaricTam() {
        String secilenPlatform = HidrosController.secilenPlatformTipi.trim();

        if(!Objects.equals(secilenPlatform, "ESP")) {
            for (Map.Entry<String, HashMap<String, String>> entry : ExcelDataReadUtil.dataManipulator.hidrosTamParcaESPHaric.entrySet()) {
                HashMap<String, String> innerMap = entry.getValue();

                String malzemeKodu = innerMap.get("B");
                String secilenMalzeme = entry.getKey();
                String adet = Utils.float2String(innerMap.get("C"));

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        }
    }

    private void loadOzelTekValf() {
        for (Map.Entry<String, HashMap<String, String>> entry : ExcelDataReadUtil.dataManipulator.hidrosTamParcaOzelTekValf.entrySet()) {
            HashMap<String, String> innerMap = entry.getValue();

            String malzemeKodu = innerMap.get("B");
            String secilenMalzeme = entry.getKey();
            String adet = Utils.float2String(innerMap.get("C"));

            ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
            parcaListesiTablo.getItems().add(data);
        }
    }

    private void loadYagMiktari() {
        String tankKapasite = HidrosController.secilenTankKapasitesi.trim();

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

    private void loadKabinKodu() {
        String malzemeKodu = null;
        String malzemeAdi = null;
        String adet = null;
        if(Objects.equals(HidrosController.kabinKodu, "KD-8 Engelli")) {
            malzemeKodu = "151-06-05-061";
            malzemeAdi = "KD-8 Engelli Kabin";
            adet = "1";
        } else if(Objects.equals(HidrosController.kabinKodu, "KD-10 (CARREFOUR)")) {
            malzemeKodu = "151-06-05-103";
            malzemeAdi = "KD-10 (CARREFOUR) Kabin";
            adet = "1";
        } else if(Objects.equals(HidrosController.kabinKodu, "KDB-20 (BALİNA)")) {
            malzemeKodu = "150-52-19-011";
            malzemeAdi = "KDB-20 (BALİNA) Kabin";
            adet = "1";
        }
        ParcaTableData data = new ParcaTableData(malzemeKodu, malzemeAdi, adet);
        parcaListesiTablo.getItems().add(data);
    }
}