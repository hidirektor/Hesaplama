package me.t3sl4.hesaplama.ui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import me.t3sl4.hesaplama.hydraulic.ParcaTableData;

import java.util.Objects;

public class ParcaController {
    @FXML
    private ComboBox basincSalteriComboBox;

    @FXML
    private TableView<ParcaTableData> parcaListesiTablo;

    @FXML
    private TableColumn<ParcaTableData, String> malzemeKodu;

    @FXML
    private TableColumn<ParcaTableData, String> secilenMalzeme;

    @FXML
    private TableColumn<ParcaTableData, String> adet;

    private String basincSalteriDurumu = null;

    public void initialize() {
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
        loadKampanaParca();
        loadPompaParca();
        loadKaplinParca();
        loadValfBlokParca();
        loadBasincSalteriParca();
        loadBasincStandart();
    }

    private void loadKampanaParca() {
        if(MainController.secilenKampana == 250) {
            for (String veri : MainController.dataManipulator.parcaListesiKampana250) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(MainController.secilenKampana == 300) {
            for (String veri : MainController.dataManipulator.parcaListesiKampana300) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(MainController.secilenKampana == 350) {
            for (String veri : MainController.dataManipulator.parcaListesiKampana350) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(MainController.secilenKampana == 400) {
            for (String veri : MainController.dataManipulator.parcaListesiKampana400) {
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
        if(Objects.equals(MainController.secilenPompa, "9.5 cc")) {
            for (String veri : MainController.dataManipulator.parcaListesiPompa95) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(MainController.secilenPompa, "11.9 cc")) {
            for (String veri : MainController.dataManipulator.parcaListesiPompa119) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(MainController.secilenPompa, "14 cc")) {
            for (String veri : MainController.dataManipulator.parcaListesiPompa14) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(MainController.secilenPompa, "14.6 cc")) {
            for (String veri : MainController.dataManipulator.parcaListesiPompa146) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(MainController.secilenPompa, "16.8 cc")) {
            for (String veri : MainController.dataManipulator.parcaListesiPompa168) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(MainController.secilenPompa, "19.2 cc")) {
            for (String veri : MainController.dataManipulator.parcaListesiPompa192) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(MainController.secilenPompa, "22.9 cc")) {
            for (String veri : MainController.dataManipulator.parcaListesiPompa229) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(MainController.secilenPompa, "28.1 cc")) {
            for (String veri : MainController.dataManipulator.parcaListesiPompa281) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(MainController.secilenPompa, "28.8 cc")) {
            for (String veri : MainController.dataManipulator.parcaListesiPompa288) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(MainController.secilenPompa, "33.3 cc")) {
            for (String veri : MainController.dataManipulator.parcaListesiPompa333) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(MainController.secilenPompa, "37.9 cc")) {
            for (String veri : MainController.dataManipulator.parcaListesiPompa379) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(MainController.secilenPompa, "42.6 cc")) {
            for (String veri : MainController.dataManipulator.parcaListesiPompa426) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(MainController.secilenPompa, "45.5 cc")) {
            for (String veri : MainController.dataManipulator.parcaListesiPompa455) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(MainController.secilenPompa, "49.4 cc")) {
            for (String veri : MainController.dataManipulator.parcaListesiPompa494) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(MainController.secilenPompa, "56.1 cc")) {
            for (String veri : MainController.dataManipulator.parcaListesiPompa561) {
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
        if(Objects.equals(MainController.secilenMotor, "4 kW")) {
            for (String veri : MainController.dataManipulator.parcaListesiMotor4) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(MainController.secilenMotor, "5.5 kW")) {
            for (String veri : MainController.dataManipulator.parcaListesiMotor55) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(MainController.secilenMotor, "5.5 kW (Kompakt)")) {
            for (String veri : MainController.dataManipulator.parcaListesiMotor55Kompakt) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(MainController.secilenMotor, "7.5 kW (Kompakt)")) {
            for (String veri : MainController.dataManipulator.parcaListesiMotor75Kompakt) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(MainController.secilenMotor, "11 kW")) {
            for (String veri : MainController.dataManipulator.parcaListesiMotor11) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(MainController.secilenMotor, "11 kW (Kompakt)")) {
            for (String veri : MainController.dataManipulator.parcaListesiMotor11Kompakt) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(MainController.secilenMotor, "15 kW")) {
            for (String veri : MainController.dataManipulator.parcaListesiMotor15) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(MainController.secilenMotor, "18.5 kW")) {
            for (String veri : MainController.dataManipulator.parcaListesiMotor185) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(MainController.secilenMotor, "22 kW")) {
            for (String veri : MainController.dataManipulator.parcaListesiMotor22) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(MainController.secilenMotor, "37 kW")) {
            for (String veri : MainController.dataManipulator.parcaListesiMotor37) {
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
        if(Objects.equals(MainController.secilenMotor, "4 kW") || Objects.equals(MainController.secilenMotor, "5.5 kW (Kompakt)")) {
            for (String veri : MainController.dataManipulator.parcaListesiKaplin1PN28) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(MainController.secilenMotor, "5.5 kW") || Objects.equals(MainController.secilenMotor, "7.5 kW (Kompakt)") || Objects.equals(MainController.secilenMotor, "11 kW")) {
            for (String veri : MainController.dataManipulator.parcaListesiKaplin1PN38) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(MainController.secilenMotor, "15 kW") || Objects.equals(MainController.secilenMotor, "18.5 kW") || Objects.equals(MainController.secilenMotor, "22 kW") || Objects.equals(MainController.secilenMotor, "37 kW")) {
            for (String veri : MainController.dataManipulator.parcaListesiKaplin1PN42) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(MainController.secilenMotor, "4 kW") || Objects.equals(MainController.secilenMotor, "5.5 kW (Kompakt)")) {
            for (String veri : MainController.dataManipulator.parcaListesiKaplin2PN28) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(MainController.secilenMotor, "5.5 kW") || Objects.equals(MainController.secilenMotor, "7.5 kW (Kompakt)") || Objects.equals(MainController.secilenMotor, "11 kW")) {
            for (String veri : MainController.dataManipulator.parcaListesiKaplin2PN38) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(MainController.secilenMotor, "15 kW") || Objects.equals(MainController.secilenMotor, "18.5 kW") || Objects.equals(MainController.secilenMotor, "22 kW") || Objects.equals(MainController.secilenMotor, "37 kW")) {
            for (String veri : MainController.dataManipulator.parcaListesiKaplin2PN42) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        }
    }

    private void loadValfBlokParca() {
        if(Objects.equals(MainController.secilenValfTipi, "İnişte Tek Hız")) {
            for (String veri : MainController.dataManipulator.parcaListesiValfBloklariTekHiz) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(MainController.secilenValfTipi, "İnişte Çift Hız")) {
            for (String veri : MainController.dataManipulator.parcaListesiValfBloklariCiftHiz) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(MainController.secilenValfTipi, "Kilitli Blok || Çift Hız")) {
            for (String veri : MainController.dataManipulator.parcaListesiValfBloklariKilitliBlok) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        } else if(Objects.equals(MainController.secilenValfTipi, "Kompanzasyon + İnişte Tek Hız")) {
            for (String veri : MainController.dataManipulator.parcaListesiValfBloklariKompanzasyon) {
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
            for (String veri : MainController.dataManipulator.parcaListesiBasincSalteri) {
                String[] veriParcalari = veri.split(";");

                String malzemeKodu = veriParcalari[0];
                String secilenMalzeme = veriParcalari[1];
                String adet = veriParcalari[2];

                ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
                parcaListesiTablo.getItems().add(data);
            }
        }
    }

    private void loadBasincStandart() {
        int index = 0;
        int totalElements = MainController.dataManipulator.parcaListesiStandart.size();

        for (String veri : MainController.dataManipulator.parcaListesiStandart) {
            String[] veriParcalari = veri.split(";");

            String malzemeKodu = veriParcalari[0];
            String secilenMalzeme = veriParcalari[1];
            String adet = veriParcalari[2];
            index++;
            if(index == totalElements) {
                if(Objects.equals(MainController.atananHT, "HT 40")) {
                    adet = String.valueOf(10);
                } else if(Objects.equals(MainController.atananHT, "HT 70")) {
                    adet = String.valueOf(14);
                } else if(Objects.equals(MainController.atananHT, "HT 100")) {
                    adet = String.valueOf(14);
                } else if(Objects.equals(MainController.atananHT, "HT 125")) {
                    adet = String.valueOf(14);
                } else if(Objects.equals(MainController.atananHT, "HT 160")) {
                    adet = String.valueOf(16);
                } else if(Objects.equals(MainController.atananHT, "HT 200")) {
                    adet = String.valueOf(18);
                } else if(Objects.equals(MainController.atananHT, "HT 250")) {
                    adet = String.valueOf(18);
                } else if(Objects.equals(MainController.atananHT, "HT 300")) {
                    adet = String.valueOf(22);
                } else if(Objects.equals(MainController.atananHT, "HT 350")) {
                    adet = String.valueOf(22);
                } else if(Objects.equals(MainController.atananHT, "HT 400")) {
                    adet = String.valueOf(22);
                }
            }
            ParcaTableData data = new ParcaTableData(malzemeKodu, secilenMalzeme, adet);
            parcaListesiTablo.getItems().add(data);
        }
    }
}