package me.t3sl4.hydraulic.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import me.t3sl4.hydraulic.Launcher;
import me.t3sl4.hydraulic.Util.Data.Table.TableData;
import me.t3sl4.hydraulic.Util.Util;

import java.util.Objects;

public class HidrosController {
    @FXML
    private TextField siparisNumarasi;

    @FXML
    private ComboBox motorComboBox;

    @FXML
    private ComboBox motorGucuComboBox;

    @FXML
    private ComboBox pompaComboBox;

    @FXML
    private ComboBox tankTipiComboBox;

    @FXML
    private ComboBox tankKapasitesiComboBox;

    @FXML
    private ComboBox valfTipiComboBox;

    @FXML
    private ComboBox valfSekliComboBox;

    @FXML
    private ComboBox platformTipiComboBox;

    @FXML
    private ComboBox selenoidComboBox;

    @FXML
    private TableView<TableData> sonucTablo;

    @FXML
    private TableColumn<TableData, String> sonucTabloSatir1;

    @FXML
    private TableColumn<TableData, String> sonucTabloSatir2;

    @FXML
    private Text sonucAnaLabelTxt;

    @FXML
    private ImageView sonucTankGorsel;


    public String girilenSiparisNumarasi;
    public String secilenMotorTipi = null;
    public String secilenMotorGucu = null;
    public String secilenPompa = null;
    public String secilenTankTipi = null;
    public String secilenTankKapasitesi = null;
    public String secilenValfTipi = null;
    public String secilenValfSekli = null;
    public String secilenPlatformTipi = null;
    public String secilenSelenoidDurumu = null;

    public void initialize() {
        comboBoxListener();
        sonucTabloSatir1.setCellValueFactory(new PropertyValueFactory<>("satir1Property"));
        sonucTabloSatir2.setCellValueFactory(new PropertyValueFactory<>("satir2Property"));
    }

    @FXML
    public void hesaplaFunc() {
        Image image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("icons/tanklar/hidros/ornek.png")));
        sonucTankGorsel.setImage(image);
    }

    public void comboBoxListener() {
        siparisNumarasi.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!siparisNumarasi.getText().isEmpty()) {
                girilenSiparisNumarasi = newValue;
            }
            sonucAnaLabelTxt.setText("Sipariş Numarası: " + girilenSiparisNumarasi);
            initMotorTipi();
            if(girilenSiparisNumarasi != null) {
                tabloGuncelle();
            }
        });

        motorComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if(!motorComboBox.getItems().isEmpty() && newValue != null) {
                secilenMotorTipi = newValue.toString();
                initMotorGucu();
                if(secilenMotorTipi != null) {
                    tabloGuncelle();
                }
            }
        });

        motorGucuComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if(!motorGucuComboBox.getItems().isEmpty() && newValue != null) {
                secilenMotorGucu = newValue.toString();
                initPompa();
                if(secilenMotorGucu != null) {
                    tabloGuncelle();
                }
            }
        });

        pompaComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if(!pompaComboBox.getItems().isEmpty() && newValue != null) {
                secilenPompa = newValue.toString();
                initTankTipi();
                if(secilenPompa != null) {
                    tabloGuncelle();
                }
            }
        });

        tankTipiComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if(!tankTipiComboBox.getItems().isEmpty() && newValue != null) {
                secilenTankTipi = newValue.toString();
                initTankKapasitesi();
                if(secilenTankTipi != null) {
                    tabloGuncelle();
                }
            }
        });

        tankKapasitesiComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if(!tankKapasitesiComboBox.getItems().isEmpty() && newValue != null) {
                secilenTankKapasitesi = newValue.toString();
                initValfTipi();
                if(secilenTankKapasitesi != null) {
                    tabloGuncelle();
                }
            }
        });

        valfTipiComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if(!valfTipiComboBox.getItems().isEmpty() && newValue != null) {
                secilenValfTipi = newValue.toString();
                initValfSekli();
                if(secilenValfTipi != null) {
                    tabloGuncelle();
                }
            }
        });

        valfSekliComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if(!valfSekliComboBox.getItems().isEmpty() && newValue != null) {
                secilenValfSekli = newValue.toString();
                initPlatformTipi();
                if(secilenValfSekli != null) {
                    tabloGuncelle();
                }
            }
        });

        platformTipiComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if(!platformTipiComboBox.getItems().isEmpty() && newValue != null) {
                secilenPlatformTipi = newValue.toString();
                initSelenoidValf();
                if(secilenPlatformTipi != null) {
                    tabloGuncelle();
                }
            }
        });

        selenoidComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if(!selenoidComboBox.getItems().isEmpty() && newValue != null) {
                secilenSelenoidDurumu = newValue.toString();
                if(secilenSelenoidDurumu != null) {
                    tabloGuncelle();
                }
            }
        });
    }

    private void tabloGuncelle() {
        sonucTablo.getItems().clear();
        TableData data = new TableData("Sipariş Numarası:", girilenSiparisNumarasi);
        sonucTablo.getItems().add(data);

        data = new TableData("Seçilen Motor Tipi:", secilenMotorTipi);
        sonucTablo.getItems().add(data);

        data = new TableData("Seçilen Motor:", secilenMotorGucu);
        sonucTablo.getItems().add(data);

        data = new TableData("Seçilen Pompa:", secilenPompa);
        sonucTablo.getItems().add(data);

        data = new TableData("Seçilen Tank Tipi:", secilenTankTipi);
        sonucTablo.getItems().add(data);

        data = new TableData("Seçilen Tank Kapasitesi:", secilenTankKapasitesi);
        sonucTablo.getItems().add(data);

        data = new TableData("Seçilen Valf Tipi:", secilenValfTipi);
        sonucTablo.getItems().add(data);

        data = new TableData("Seçilen Platform Tipi:", secilenPlatformTipi);
        sonucTablo.getItems().add(data);

        data = new TableData("Selenoid Valf Durumu:", secilenSelenoidDurumu);
        sonucTablo.getItems().add(data);
    }

    private void initMotorTipi() {
        motorComboBox.getItems().clear();
        motorComboBox.getItems().addAll("380 V", "220 V");
    }

    private void initMotorGucu() {
        motorGucuComboBox.getItems().clear();
        if(secilenMotorTipi.equals("380 V")) {
            motorGucuComboBox.getItems().addAll(Util.dataManipulator.motorDegerleriHidros380);
        } else {
            motorGucuComboBox.getItems().addAll(Util.dataManipulator.motorDegerleriHidros220);
        }
    }

    private void initPompa() {
        pompaComboBox.getItems().clear();
        pompaComboBox.getItems().addAll(Util.dataManipulator.pompaKapasiteDegerleriHidros);
    }

    private void initTankTipi() {
        tankTipiComboBox.getItems().clear();
        tankTipiComboBox.getItems().addAll("Dikey", "Yatay");
    }

    private void initTankKapasitesi() {
        tankKapasitesiComboBox.getItems().clear();
        if(secilenTankTipi.equals("Dikey")) {
            tankKapasitesiComboBox.getItems().addAll(Util.dataManipulator.tankKapasitesiDegerleriHidrosDikey);
        } else {
            tankKapasitesiComboBox.getItems().addAll(Util.dataManipulator.tankKapasitesiDegerleriHidrosYatay);
        }
    }

    private void initValfTipi() {
        valfTipiComboBox.getItems().clear();
        valfTipiComboBox.getItems().addAll("Dikey", "Yatay");
    }

    private void initValfSekli() {
        valfSekliComboBox.getItems().clear();
        valfSekliComboBox.getItems().addAll("İnişte Tek Hız", "İnişte Çift Hız");
    }

    private void initPlatformTipi() {
        platformTipiComboBox.getItems().clear();
        platformTipiComboBox.getItems().addAll(Util.dataManipulator.platformDegerleriHidros);
    }

    private void initSelenoidValf() {
        selenoidComboBox.getItems().clear();
        selenoidComboBox.getItems().addAll("Var", "Yok");
    }

    @FXML
    public void siparisNumarasiEntered() {
        if(siparisNumarasi.getText() != null) {
            motorComboBox.setDisable(false);
        }
    }

    @FXML
    public void motorTipiPressed() {
        if(motorComboBox.getValue() != null) {
            secilenMotorTipi = motorComboBox.getValue().toString();
            motorGucuComboBox.setDisable(false);
        }
    }

    @FXML
    public void motorGucuPressed() {
        if(motorGucuComboBox.getValue() != null) {
            secilenMotorGucu = motorGucuComboBox.getValue().toString();
            pompaComboBox.setDisable(false);
        }
    }

    @FXML
    public void pompaPressed() {
        if(pompaComboBox.getValue() != null) {
            secilenPompa = pompaComboBox.getValue().toString();
            tankTipiComboBox.setDisable(false);
        }
    }

    @FXML
    public void tankTipiPressed() {
        if(tankTipiComboBox.getValue() != null) {
            secilenTankTipi = tankTipiComboBox.getValue().toString();
            tankKapasitesiComboBox.setDisable(false);
        }
    }

    @FXML
    public void tankKapasitesiPressed() {
        if(tankKapasitesiComboBox.getValue() != null) {
            secilenTankKapasitesi = tankKapasitesiComboBox.getValue().toString();
            valfTipiComboBox.setDisable(false);
        }
    }

    @FXML
    public void valfTipiPressed() {
        if(valfTipiComboBox.getValue() != null) {
            secilenValfTipi = valfTipiComboBox.getValue().toString();
            valfSekliComboBox.setDisable(false);
        }
    }

    @FXML
    public void valfSekliPressed() {
        if(valfSekliComboBox.getValue() != null) {
            secilenValfSekli = valfSekliComboBox.getValue().toString();
            platformTipiComboBox.setDisable(false);
        }
    }

    @FXML
    public void platformTipiPressed() {
        if(platformTipiComboBox.getValue() != null) {
            secilenPlatformTipi = platformTipiComboBox.getValue().toString();
            selenoidComboBox.setDisable(false);
        }
    }

    @FXML
    public void selenoidPressed() {
        if(selenoidComboBox.getValue() != null) {
            secilenSelenoidDurumu = selenoidComboBox.getValue().toString();
        }
    }
}
