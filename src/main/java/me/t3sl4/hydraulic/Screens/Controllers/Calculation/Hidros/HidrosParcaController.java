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
import me.t3sl4.hydraulic.Launcher;
import me.t3sl4.hydraulic.Utils.Database.Model.Table.PartList.ParcaTableData;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.LinkedList;
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
            loadTankTipi();
            loadPlatformTipi();
            loadGenelParcalar();
            loadValfParcalar();

            if(secilenPlatform.equals("Özel - Yatay")) {
                loadOzelYatayGenel();
            }

            loadManometre();
            loadBasincSalteri();
            loadElPompasiParca();
            if(!secilenPlatform.contains("Özel")) {
                loadYagMiktari();
            }
        } else {
            //İthal Parçalar buraya
        }
    }

    private void loadMotorParca() {
        String motorGucu = HidrosController.secilenMotorGucu.trim();

        if (Objects.equals(HidrosController.secilenMotorTipi, "380 V (AC)")) {
            if(motorGucu.equals("0.37 kW")) {
                generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaMotor380.get("0"));
            } else if(motorGucu.equals("0.55 kW")) {
                generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaMotor380.get("1"));
            } else if(motorGucu.equals("0.75 kW")) {
                generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaMotor380.get("2"));
            } else if(motorGucu.equals("1.1 kW")) {
                generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaMotor380.get("3"));
            } else if(motorGucu.equals("1.5 kW")) {
                generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaMotor380.get("4"));
            } else if(motorGucu.equals("2.2 kW")) {
                generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaMotor380.get("5"));
            } else if(motorGucu.equals("3 kW")) {
                generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaMotor380.get("6"));
            } else if(motorGucu.equals("4 kW")) {
                generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaMotor380.get("7"));
            }
        } else if (Objects.equals(HidrosController.secilenMotorTipi, "220 V (AC)")) {
            if(motorGucu.equals("0.37 kW")) {
                generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaMotor220.get("0"));
            } else if(motorGucu.equals("0.55 kW")) {
                generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaMotor220.get("1"));
            } else if(motorGucu.equals("0.75 kW")) {
                generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaMotor220.get("2"));
            } else if(motorGucu.equals("1.1 kW")) {
                generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaMotor220.get("3"));
            } else if(motorGucu.equals("1.5 kW")) {
                generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaMotor220.get("4"));
            } else if(motorGucu.equals("2.2 kW")) {
                generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaMotor220.get("5"));
            } else if(motorGucu.equals("3 kW")) {
                generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaMotor220.get("6"));
            }
        }
    }

    private void loadPompaParca() {
        String pompaDegeri = HidrosController.secilenPompa.trim();

        if(pompaDegeri.equals("0.8 cc")) {
            generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaPompa.get("0"));
        } else if(pompaDegeri.equals("1.1 cc")) {
            generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaPompa.get("1"));
        } else if(pompaDegeri.equals("1.3 cc")) {
            generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaPompa.get("2"));
        } else if(pompaDegeri.equals("1.8 cc")) {
            generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaPompa.get("3"));
        } else if(pompaDegeri.equals("2.1 cc")) {
            generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaPompa.get("4"));
        } else if(pompaDegeri.equals("2.7 cc")) {
            generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaPompa.get("5"));
        } else if(pompaDegeri.equals("3.2 cc")) {
            generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaPompa.get("6"));
        } else if(pompaDegeri.equals("3.7 cc")) {
            generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaPompa.get("7"));
        } else if(pompaDegeri.equals("4.2 cc")) {
            generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaPompa.get("8"));
        } else if(pompaDegeri.equals("4.8 cc")) {
            generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaPompa.get("9"));
        } else if(pompaDegeri.equals("5.8 cc")) {
            generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaPompa.get("10"));
        } else if(pompaDegeri.equals("7 cc")) {
            generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaPompa.get("11"));
        } else if(pompaDegeri.equals("8 cc")) {
            generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaPompa.get("12"));
        } else if(pompaDegeri.equals("9 cc")) {
            generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaPompa.get("13"));
        }
    }

    private void loadTankTipi() {
        String kontrolTankTipi = HidrosController.secilenTankTipi.trim();

        if(HidrosController.secilenTankTipi.contains("Özel")) {
            String malzemeKodu = "Özel Tank";
            String malzemeAdi = "Genişlik: " + HidrosController.secilenOzelTankGenislik + "mm" + " Yükseklik: " + HidrosController.secilenOzelTankYukseklik + "mm" + " Derinlik: " + HidrosController.secilenOzelTankDerinlik + "mm";
            String adet = "1";

            ParcaTableData data = new ParcaTableData(malzemeKodu, malzemeAdi, adet);
            parcaListesiTablo.getItems().add(data);
        } else {
            String kontrolTankKapasitesi = HidrosController.secilenTankKapasitesi.trim();

            if(Objects.equals(kontrolTankTipi, "Yatay")) {
                if(kontrolTankKapasitesi.equals("2 Lt")) {
                    generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaTankYatay.get("0"));
                } else if(kontrolTankKapasitesi.equals("4 Lt")) {
                    generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaTankYatay.get("1"));
                } else if(kontrolTankKapasitesi.equals("6 Lt")) {
                    generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaTankYatay.get("2"));
                } else if(kontrolTankKapasitesi.equals("8 Lt")) {
                    generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaTankYatay.get("3"));
                } else if(kontrolTankKapasitesi.equals("10 Lt")) {
                    generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaTankYatay.get("4"));
                } else if(kontrolTankKapasitesi.equals("12 Lt")) {
                    generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaTankYatay.get("5"));
                } else if(kontrolTankKapasitesi.equals("20 Lt")) {
                    generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaTankYatay.get("6"));
                }
            } else if(Objects.equals(kontrolTankTipi, "Dikey")) {
                if(kontrolTankKapasitesi.equals("4 Lt")) {
                    generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaTankDikey.get("0"));
                } else if(kontrolTankKapasitesi.equals("6 Lt")) {
                    generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaTankDikey.get("1"));
                } else if(kontrolTankKapasitesi.equals("8 Lt")) {
                    generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaTankDikey.get("2"));
                } else if(kontrolTankKapasitesi.equals("10 Lt")) {
                    generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaTankDikey.get("3"));
                } else if(kontrolTankKapasitesi.equals("12 Lt")) {
                    generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaTankDikey.get("4"));
                } else if(kontrolTankKapasitesi.equals("20 Lt")) {
                    generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaTankDikey.get("5"));
                }
            }
        }
    }

    private void loadPlatformTipi() {
        String secilenPlatform = HidrosController.secilenPlatformTipi.trim();

        if(Objects.equals(secilenPlatform, "ESP")) {
            String secilenTank = HidrosController.secilenTankTipi.trim();

            if(Objects.equals(secilenTank, "Dikey")) {
                String secilenInis = HidrosController.secilenInisTipi.trim();

                if(Objects.equals(secilenInis, "İnişte Tek Hız")) {
                    generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaESPGenel.get("0"));
                } else if(Objects.equals(secilenInis, "İnişte Çift Hız")) {
                    generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaESPGenel.get("0"));
                    generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaESPCiftHiz.get("0"));
                }
            } else if(Objects.equals(secilenTank, "Yatay")) {
                String secilenInis = HidrosController.secilenInisTipi.trim();

                if(Objects.equals(secilenInis, "İnişte Tek Hız")) {
                    generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaESPGenel.get("0"));
                } else if(Objects.equals(secilenInis, "İnişte Çift Hız")) {
                    generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaESPGenel.get("0"));
                    generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaESPCiftHiz.get("0"));
                }
            }
        } else if(Objects.equals(secilenPlatform, "Devirmeli + Yürüyüş")) {
            generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaDevirmeli.get("0"));
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
        generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaDefault.get("0"));
    }

    private void loadOzelYatayGenel() {
        generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaOzelYatayGenel.get("0"));
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

        if(malzemeKodu != null) {
            ParcaTableData data = new ParcaTableData(malzemeKodu, malzemeAdi, adet);
            parcaListesiTablo.getItems().add(data);
        }
    }

    private void loadValfParcalar() {
        if(HidrosController.secilenPlatformTipi.contains("Özel")) {
            if(HidrosController.secilenBirinciValf.equals("1")) {
                if(HidrosController.secilenIkinciValf.equals("Açık Merkez")) {
                    generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaValf.get("0"));
                } else if(HidrosController.secilenIkinciValf.equals("J Merkez")) {
                    generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaValf.get("1"));
                } else if(HidrosController.secilenIkinciValf.equals("H Merkez")) {
                    generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaValf.get("2"));
                }
            } else {
                loadOzelCiftValf();
            }
        } else {
            if(HidrosController.secilenBirinciValf.equals("Açık Merkez")) {
                generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaValf.get("0"));
            } else if(HidrosController.secilenBirinciValf.equals("J Merkez")) {
                generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaValf.get("1"));
            } else if(HidrosController.secilenBirinciValf.equals("H Merkez")) {
                generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaValf.get("2"));
            }

            if(HidrosController.secilenIkinciValf != null) {
                if(HidrosController.secilenIkinciValf.equals("Açık Merkez")) {
                    generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaValf.get("0"));
                } else if(HidrosController.secilenIkinciValf.equals("J Merkez")) {
                    generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaValf.get("1"));
                } else if(HidrosController.secilenIkinciValf.equals("H Merkez")) {
                    generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaValf.get("2"));
                }
            }
        }
    }

    private void loadOzelCiftValf() {
        generalLoadFunc(Launcher.getDataManipulator().powerPackHidrosParcaOzelCiftValf.get("0"));
    }

    private void generalLoadFunc(LinkedList<String> parcaListesi) {
        for (String veri : parcaListesi) {
            String[] veriParcalari = veri.split(";");

            String malzemeKodu = veriParcalari[0];
            String secilenMalzeme = veriParcalari[1];
            String adet = veriParcalari[2];

            ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
            parcaListesiTablo.getItems().add(data);
        }
    }
}