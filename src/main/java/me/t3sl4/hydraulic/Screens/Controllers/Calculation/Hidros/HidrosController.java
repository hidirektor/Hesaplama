package me.t3sl4.hydraulic.Screens.Controllers.Calculation.Hidros;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import me.t3sl4.hydraulic.Launcher;
import me.t3sl4.hydraulic.Screens.Main;
import me.t3sl4.hydraulic.Utils.File.PDFUtil;
import me.t3sl4.hydraulic.Utils.HTTP.HTTPRequest;
import me.t3sl4.hydraulic.Utils.Model.Table.TableData;
import me.t3sl4.hydraulic.Utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static me.t3sl4.hydraulic.Launcher.BASE_URL;
import static me.t3sl4.hydraulic.Launcher.createHydraulicURLPrefix;

public class HidrosController {
    @FXML
    private VBox klasikVBox;

    @FXML
    private TextField siparisNumarasi;

    @FXML
    private ComboBox<String> motorComboBox;

    @FXML
    private ComboBox<String> motorGucuComboBox;

    @FXML
    private ComboBox<String> pompaComboBox;

    @FXML
    private ComboBox<String> uniteTipiComboBox;

    @FXML
    private ComboBox<String> tankTipiComboBox;

    @FXML
    private ComboBox<String> tankKapasitesiComboBox;

    @FXML
    private ComboBox<String> birinciValfComboBox;

    @FXML
    private ComboBox<String> inisTipiComboBox;

    @FXML
    private ComboBox<String> platformTipiComboBox;

    @FXML
    private ComboBox<String> ikinciValfComboBox;

    @FXML
    private Button exportButton;

    @FXML
    private Button parcaListesiButton;

    @FXML
    private Button kaydetButton;

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

    @FXML
    private Text kullanilacakKabinText;

    @FXML
    private Text tankOlculeriText;

    @FXML
    private Text firstValveText;

    @FXML
    private Text secondValveText;

    @FXML
    private TextField ozelTankGenislik;

    @FXML
    private TextField ozelTankYukseklik;

    @FXML
    private TextField ozelTankDerinlik;

    @FXML
    private Text tankKapasitesiMainText;


    public static String girilenSiparisNumarasi;
    public static String secilenMotorTipi = null;
    public static String secilenMotorGucu = null;
    public static String secilenPompa = null;
    public static String uniteTipiDurumu = null;
    public static String secilenTankTipi = null;
    public static String secilenTankKapasitesi = null;
    public static String secilenOzelTankGenislik = null;
    public static String secilenOzelTankYukseklik = null;
    public static String secilenOzelTankDerinlik = null;
    public static String secilenBirinciValf = null;
    public static String secilenInisTipi = null;
    public static String secilenPlatformTipi = null;
    public static String secilenIkinciValf = null;
    public static String kabinKodu = null;

    public boolean hesaplamaBitti = false;

    private double x, y;

    public String secilenUniteTipi = "Hidros";
    boolean pdfSucc = false;
    boolean excelSucc = false;

    public void initialize() {
        comboBoxListener();
        sonucTabloSatir1.setCellValueFactory(new PropertyValueFactory<>("satir1Property"));
        sonucTabloSatir2.setCellValueFactory(new PropertyValueFactory<>("satir2Property"));
        ozelTankStatus("null");
        if(Main.loggedInUser == null) {
            kaydetButton.setDisable(true);
        }
    }

    @FXML
    public void hesaplaFunc() {
        if(checkComboBox()) {
            Utils.showErrorMessage("Lütfen tüm girdileri kontrol edin.");
        } else {
            enableSonucSection();
            hesaplamaBitti = true;
        }
    }

    @FXML
    public void temizleFunc() {
        siparisNumarasi.setText("");
        sonucAnaLabelTxt.setText("Sipariş Numarası: ");
        motorComboBox.getSelectionModel().clearSelection();
        motorComboBox.setPromptText("Motor Voltajı");
        motorGucuComboBox.getSelectionModel().clearSelection();
        motorGucuComboBox.setPromptText("Motor Gücü");
        pompaComboBox.getSelectionModel().clearSelection();
        pompaComboBox.setPromptText("Pompa");
        uniteTipiComboBox.getSelectionModel().clearSelection();
        uniteTipiComboBox.setPromptText("Ünite Tipi");
        tankTipiComboBox.getSelectionModel().clearSelection();
        tankTipiComboBox.setPromptText("Tank Tipi");
        tankKapasitesiComboBox.setPromptText("Tank Kapasitesi");
        platformTipiComboBox.getSelectionModel().clearSelection();
        platformTipiComboBox.setPromptText("Platform Tipi");
        birinciValfComboBox.getSelectionModel().clearSelection();
        birinciValfComboBox.setPromptText("1. Valf Tipi");
        ikinciValfComboBox.getSelectionModel().clearSelection();
        ikinciValfComboBox.setPromptText("2. Valf Tipi");
        inisTipiComboBox.getSelectionModel().clearSelection();
        inisTipiComboBox.setPromptText("İnis Metodu");
        sonucTankGorsel.setImage(null);
        hesaplamaBitti = false;
        disableAllCombos();
        sonucButtonDisable();
        kullanilacakKabinText.setVisible(false);
        ozelTankStatus("null");
    }

    @FXML
    public void parcaListesiGoster() {
        Image icon = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/images/general/logo.png")));
        if(hesaplamaBitti) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("fxml/HidrosParcaListesi.fxml"));
                VBox root = fxmlLoader.load();
                HidrosParcaController hidrosParcaController = fxmlLoader.getController();
                Stage popupStage = new Stage();
                popupStage.initModality(Modality.APPLICATION_MODAL);
                popupStage.initStyle(StageStyle.UNDECORATED);
                popupStage.setScene(new Scene(root));
                popupStage.getIcons().add(icon);

                root.setOnMousePressed(event -> {
                    x = event.getSceneX();
                    y = event.getSceneY();
                });
                root.setOnMouseDragged(event -> {

                    popupStage.setX(event.getScreenX() - x);
                    popupStage.setY(event.getScreenY() - y);

                });
                popupStage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Utils.showErrorMessage("Lütfen önce hesaplama işlemini bitirin !");
        }
    }

    @FXML
    public void transferCalculation() {
        String creationURL = BASE_URL + createHydraulicURLPrefix;

        String pdfPath = System.getProperty("user.home") + "/Desktop/" + girilenSiparisNumarasi + ".pdf";
        String excelPath = System.getProperty("user.home") + "/Desktop/" + girilenSiparisNumarasi + ".xlsx";

        if (new File(pdfPath).exists() && new File(excelPath).exists()) {
            File partListFile = new File(excelPath);
            File schematicFile = new File(pdfPath);

            Map<String, File> files = new HashMap<>();
            files.put("partListFile", partListFile);
            files.put("schematicFile", schematicFile);

            HTTPRequest.authorizedUploadMultipleFiles(creationURL, "POST", files, Launcher.getAccessToken(), Main.loggedInUser.getUsername(), girilenSiparisNumarasi, secilenUniteTipi, new HTTPRequest.RequestCallback() {
                @Override
                public void onSuccess(String response) {
                    Utils.showSuccessMessage("Hidrolik ünitesi başarılı bir şekilde kaydedildi.");
                }

                @Override
                public void onFailure() {
                    Utils.showErrorMessage("Hidrolik ünitesi kaydedilemedi !");
                }
            });
        } else {
            Utils.showErrorMessage("Lütfen PDF ve parça listesi oluşturduktan sonra kaydedin");
        }
    }

    public void comboBoxListener() {
        siparisNumarasi.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!siparisNumarasi.getText().isEmpty()) {
                girilenSiparisNumarasi = newValue;
            }
            sonucAnaLabelTxt.setText(girilenSiparisNumarasi + " Numaralı Sipariş");
            initMotorTipi();
            if(girilenSiparisNumarasi != null) {
                tabloGuncelle();
            }
        });

        motorComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if(!motorComboBox.getItems().isEmpty() && newValue != null) {
                secilenMotorTipi = newValue.toString();
                initUniteTipi();
                if(secilenMotorTipi != null) {
                    tabloGuncelle();
                }
            }
        });

        uniteTipiComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if(!uniteTipiComboBox.getItems().isEmpty() && newValue != null) {
                uniteTipiDurumu = newValue.toString();
                initMotorGucu();
                if(secilenPompa != null) {
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
                if(secilenTankTipi.contains("Özel")) {
                    ozelTankStatus("Özel");
                } else {
                    ozelTankStatus("Normal");
                    initTankKapasitesi();
                }
                if(secilenTankTipi != null) {
                    tabloGuncelle();
                }
            }
        });

        tankKapasitesiComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if(!tankKapasitesiComboBox.getItems().isEmpty() && newValue != null) {
                secilenTankKapasitesi = newValue.toString();
                initPlatformTipi();
                if(secilenTankKapasitesi != null) {
                    tabloGuncelle();
                }
            }
        });

        ozelTankGenislik.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!ozelTankGenislik.getText().isEmpty()) {
                secilenOzelTankGenislik = newValue;
            }

            if(secilenOzelTankGenislik != null) {
                tabloGuncelle();
            }
        });

        ozelTankYukseklik.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!ozelTankYukseklik.getText().isEmpty()) {
                secilenOzelTankYukseklik = newValue;
            }

            if(secilenOzelTankYukseklik != null) {
                tabloGuncelle();
            }
        });

        ozelTankDerinlik.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!ozelTankDerinlik.getText().isEmpty()) {
                secilenOzelTankDerinlik = newValue;
            }
            initPlatformTipi();
            if(secilenOzelTankDerinlik != null) {
                tabloGuncelle();
            }
        });

        platformTipiComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if(!platformTipiComboBox.getItems().isEmpty() && newValue != null) {
                secilenPlatformTipi = newValue.toString();
                if(secilenPlatformTipi.equals("ESP")) {
                    initValfTipi();
                } else if(secilenPlatformTipi.equals("Devirmeli") || secilenPlatformTipi.equals("Yürüyüş")) {
                    hesaplamaBitti = true;
                    enableSonucSection();
                } else {
                    initValfTipi();
                }

                if(secilenPlatformTipi != null) {
                    tabloGuncelle();
                }
            }
        });

        birinciValfComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if(!birinciValfComboBox.getItems().isEmpty() && newValue != null) {
                secilenBirinciValf = newValue.toString();
                initIkinciValf();
                if(secilenBirinciValf != null) {
                    tabloGuncelle();
                }
            }
        });

        inisTipiComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if(!inisTipiComboBox.getItems().isEmpty() && newValue != null) {
                secilenInisTipi = newValue.toString();
                if(secilenInisTipi != null) {
                    tabloGuncelle();
                }
            }
        });

        ikinciValfComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if(!ikinciValfComboBox.getItems().isEmpty() && newValue != null) {
                secilenIkinciValf = newValue.toString();
                if(secilenIkinciValf != null) {
                    tabloGuncelle();
                }
            }
        });
    }

    private void ozelTankStatus(String tankType) {
        if(tankType.equals("Özel")) {
            tankKapasitesiMainText.setVisible(false);
            tankKapasitesiComboBox.setVisible(false);
            tankOlculeriText.setVisible(true);
            ozelTankGenislik.setVisible(true);
            ozelTankYukseklik.setVisible(true);
            ozelTankDerinlik.setVisible(true);
        } else if(tankType.equals("null")) {
            //Başlangıç durumu:
            tankOlculeriText.setVisible(false);
            ozelTankGenislik.setVisible(false);
            ozelTankYukseklik.setVisible(false);
            ozelTankDerinlik.setVisible(false);
        } else {
            tankKapasitesiMainText.setVisible(true);
            tankKapasitesiComboBox.setVisible(true);
            tankOlculeriText.setVisible(false);
            ozelTankGenislik.setVisible(false);
            ozelTankYukseklik.setVisible(false);
            ozelTankDerinlik.setVisible(false);
        }
    }

    private void tabloGuncelle() {
        sonucTablo.getItems().clear();
        TableData data = new TableData("Sipariş Numarası:", girilenSiparisNumarasi);
        sonucTablo.getItems().add(data);

        data = new TableData("Motor Voltajı:", secilenMotorTipi);
        sonucTablo.getItems().add(data);

        data = new TableData("Motor Gücü:", secilenMotorGucu);
        sonucTablo.getItems().add(data);

        data = new TableData("Pompa:", secilenPompa);
        sonucTablo.getItems().add(data);

        data = new TableData("Ünite Tipi:", uniteTipiDurumu);
        sonucTablo.getItems().add(data);

        data = new TableData("Tank Tipi:", secilenTankTipi);
        sonucTablo.getItems().add(data);

        data = new TableData("Tank Kapasitesi:", secilenTankKapasitesi);
        sonucTablo.getItems().add(data);

        data = new TableData("Özel Tank Ölçüleri: ", "G: " + secilenOzelTankGenislik + " Y: " + secilenOzelTankYukseklik + " D: " + secilenOzelTankDerinlik);
        sonucTablo.getItems().add(data);

        data = new TableData("Platform Tipi:", secilenPlatformTipi);
        sonucTablo.getItems().add(data);

        data = new TableData("İniş Metodu:", secilenInisTipi);
        sonucTablo.getItems().add(data);

        if(secilenPlatformTipi != null && secilenPlatformTipi.equals("Özel")) {
            data = new TableData("Valf Sayısı:", secilenBirinciValf);
            sonucTablo.getItems().add(data);

            data = new TableData("Valf Tipi:", secilenIkinciValf);
            sonucTablo.getItems().add(data);
        } else {
            data = new TableData("Birinci Valf:", secilenBirinciValf);
            sonucTablo.getItems().add(data);

            data = new TableData("İkinci Valf:", secilenIkinciValf);
            sonucTablo.getItems().add(data);
        }
    }

    private void initMotorTipi() {
        motorComboBox.getItems().clear();
        motorComboBox.getItems().addAll("380 V (AC)", "220 V (AC)", "12 V (DC)", "24 V (DC)");
    }

    private void initMotorGucu() {
        motorGucuComboBox.getItems().clear();
        if(uniteTipiDurumu.equals("Hidros")) {
            if(secilenMotorTipi.equals("380 V (AC)")) {
                motorGucuComboBox.getItems().addAll(Launcher.getDataManipulator().motorDegerleriHidros380);
            } else if(secilenMotorTipi.equals("220 V (AC)")) {
                motorGucuComboBox.getItems().addAll(Launcher.getDataManipulator().motorDegerleriHidros220);
            } else if(secilenMotorTipi.equals("24 V (DC)")) {
                motorGucuComboBox.getItems().addAll(Launcher.getDataManipulator().motorDegerleriHidros24);
            } else if(secilenMotorTipi.equals("12 V (DC)")) {
                motorGucuComboBox.getItems().addAll(Launcher.getDataManipulator().motorDegerleriHidros12);
            }
        } else {
            if(secilenMotorTipi.equals("380 V (AC)")) {
                motorGucuComboBox.getItems().addAll(Launcher.getDataManipulator().motorDegerleriIthal380);
            } else if(secilenMotorTipi.equals("220 V (AC)")) {
                motorGucuComboBox.getItems().addAll(Launcher.getDataManipulator().motorDegerleriIthal220);
            } else if(secilenMotorTipi.equals("24 V (DC)")) {
                motorGucuComboBox.getItems().addAll(Launcher.getDataManipulator().motorDegerleriIthal24);
            } else if(secilenMotorTipi.equals("12 V (DC)")) {
                motorGucuComboBox.getItems().addAll(Launcher.getDataManipulator().motorDegerleriIthal12);
            }
        }
    }

    private void initPompa() {
        pompaComboBox.getItems().clear();
        if(uniteTipiDurumu.equals("Hidros")) {
            pompaComboBox.getItems().addAll(Launcher.getDataManipulator().pompaKapasiteDegerleriHidros);
        } else {
            pompaComboBox.getItems().addAll(Launcher.getDataManipulator().pompaKapasiteDegerleriIthal);
        }
    }

    private void initUniteTipi() {
        uniteTipiComboBox.getItems().clear();
        uniteTipiComboBox.getItems().addAll("Hidros", "İthal");
    }

    private void initTankTipi() {
        tankTipiComboBox.getItems().clear();
        tankTipiComboBox.getItems().addAll("Dikey", "Yatay", "Özel - Dikey", "Özel - Yatay");
    }

    private void initTankKapasitesi() {
        if(secilenTankTipi.equals("Dikey")) {
            if(uniteTipiDurumu.equals("Hidros")) {
                tankKapasitesiComboBox.getItems().clear();
                tankKapasitesiComboBox.getItems().addAll(Launcher.getDataManipulator().tankKapasitesiDegerleriHidrosDikey);
            } else {
                tankKapasitesiComboBox.getItems().clear();
                tankKapasitesiComboBox.getItems().addAll(Launcher.getDataManipulator().tankKapasitesiDegerleriIthalDikey);
            }
        } else if(secilenTankTipi.equals("Yatay")) {
            if(uniteTipiDurumu.equals("Hidros")) {
                tankKapasitesiComboBox.getItems().clear();
                tankKapasitesiComboBox.getItems().addAll(Launcher.getDataManipulator().tankKapasitesiDegerleriHidrosYatay);
            } else {
                tankKapasitesiComboBox.getItems().clear();
                tankKapasitesiComboBox.getItems().addAll(Launcher.getDataManipulator().tankKapasitesiDegerleriIthalYatay);
            }
        }
    }

    private void initPlatformTipi() {
        platformTipiComboBox.getItems().clear();
        platformTipiComboBox.getItems().addAll(Launcher.getDataManipulator().platformDegerleriHidros);
    }

    private void initValfTipi() {
        if(secilenPlatformTipi.equals("Özel")) {
            //Platform özelse:
            initCustomPlatform(1);
            birinciValfComboBox.getItems().clear();
            birinciValfComboBox.getItems().addAll(
                    "1",
                    "2"
            );
        } else {
            initCustomPlatform(0);
            birinciValfComboBox.getItems().clear();
            birinciValfComboBox.getItems().addAll(Launcher.getDataManipulator().valfDegerleriHidros);
        }
    }

    private void initCustomPlatform(int platformStatus) {
        if(platformStatus == 1) {
            firstValveText.setText("Valf Sayısı");
            secondValveText.setText("Valf Tipi");
            birinciValfComboBox.setPromptText("Valf Sayısı");
            ikinciValfComboBox.setPromptText("Valf Tipi");
        } else {
            firstValveText.setText("1. Valf Tipi");
            secondValveText.setText("2. Valf Tipi");
            birinciValfComboBox.setPromptText("1. Valf Tipi");
            ikinciValfComboBox.setPromptText("2. Valf Tipi");
        }
    }

    private void initInisMetodu() {
        inisTipiComboBox.getItems().clear();
        inisTipiComboBox.getItems().addAll("İnişte Tek Hız", "İnişte Çift Hız");
    }

    private void initIkinciValf() {
        if(secilenPlatformTipi.equals("Özel")) {
            if(secilenBirinciValf.equals("1")) {
                //Tek Valf
                if(secilenMotorTipi.equals("12 V (DC)") || secilenMotorTipi.equals("24 V (DC)")) {
                    //DC Motor
                    ikinciValfComboBox.getItems().clear();
                    ikinciValfComboBox.getItems().addAll(
                            "J Merkez",
                            "H Merkez",
                            "Açık Merkez"
                    );
                } else {
                    //AC
                    ikinciValfComboBox.getItems().clear();
                    ikinciValfComboBox.getItems().addAll(
                            "J Merkez",
                            "H Merkez"
                    );
                }
            } else {
                //Çift Valf
                ikinciValfComboBox.getItems().clear();
                ikinciValfComboBox.getItems().addAll(
                        "1. Valf: J Merkez\n2. Valf: Kapalı Merkez"
                );
            }
        } else {
            ikinciValfComboBox.getItems().clear();
            ikinciValfComboBox.getItems().addAll(Launcher.getDataManipulator().valfDegerleriHidros);
            ikinciValfComboBox.getItems().addAll("Yok");
        }
    }

    @FXML
    public void siparisNumarasiEntered() {
        if(siparisNumarasi.getText() != null) {
            motorComboBox.setDisable(false);
        }
    }

    @FXML
    public void ozelTankGenislikEntered() {
        if(ozelTankGenislik.getText() != null) {
            ozelTankYukseklik.setDisable(false);
        }
    }

    @FXML
    public void ozelTankYukseklikEntered() {
        if(ozelTankYukseklik.getText() != null) {
            ozelTankDerinlik.setDisable(false);
        }
    }

    @FXML
    public void ozelTankDerinlikEntered() {
        if(ozelTankDerinlik.getText() != null) {
            platformTipiComboBox.setDisable(false);
        }
    }

    @FXML
    public void motorTipiPressed() {
        if(motorComboBox.getValue() != null) {
            secilenMotorTipi = motorComboBox.getValue();
            uniteTipiComboBox.setDisable(false);
        }
    }

    @FXML
    public void motorGucuPressed() {
        if(motorGucuComboBox.getValue() != null) {
            secilenMotorGucu = motorGucuComboBox.getValue();
            pompaComboBox.setDisable(false);
        }
    }

    @FXML
    public void pompaPressed() {
        if(pompaComboBox.getValue() != null) {
            secilenPompa = pompaComboBox.getValue();
            tankTipiComboBox.setDisable(false);
        }
    }

    @FXML
    public void uniteTipiPressed() {
        if(uniteTipiComboBox.getValue() != null) {
            uniteTipiDurumu = uniteTipiComboBox.getValue();
            motorGucuComboBox.setDisable(false);
        }
    }

    @FXML
    public void tankTipiPressed() {
        if(tankTipiComboBox.getValue() != null) {
            secilenTankTipi = tankTipiComboBox.getValue();
            tankKapasitesiComboBox.setDisable(false);
        }
    }

    @FXML
    public void tankKapasitesiPressed() {
        if(tankKapasitesiComboBox.getValue() != null) {
            secilenTankKapasitesi = tankKapasitesiComboBox.getValue();
            platformTipiComboBox.setDisable(false);
        }
    }

    @FXML
    public void platformTipiPressed() {
        if(platformTipiComboBox.getValue() != null) {
            secilenPlatformTipi = platformTipiComboBox.getValue();

            if(secilenPlatformTipi.equals("ESP")) {
                inisTipiComboBox.setDisable(false);
                birinciValfComboBox.setDisable(true);
                ikinciValfComboBox.setDisable(true);
                initInisMetodu();
            } else if(secilenPlatformTipi.equals("Devirmeli") || secilenPlatformTipi.equals("Yürüyüş")) {
                inisTipiComboBox.setDisable(true);
                birinciValfComboBox.setDisable(true);
                ikinciValfComboBox.setDisable(true);
            } else {
                inisTipiComboBox.setDisable(true);
                birinciValfComboBox.setDisable(false);
                initValfTipi();
            }
        }
    }

    @FXML
    public void birinciValfPressed() {
        if(birinciValfComboBox.getValue() != null) {
            secilenBirinciValf = birinciValfComboBox.getValue();
            ikinciValfComboBox.setDisable(false);
        }
    }

    @FXML
    public void inisTipiPressed() {
        if(inisTipiComboBox.getValue() != null) {
            secilenInisTipi = inisTipiComboBox.getValue();
        }
    }

    @FXML
    public void ikinciValfPressed() {
        if(ikinciValfComboBox.getValue() != null) {
            secilenIkinciValf = ikinciValfComboBox.getValue();
        }
    }

    private boolean checkComboBox() {
        boolean isSiparisNumarasiEmpty = isStringEmpty(siparisNumarasi.getText());
        boolean isMotorComboBoxEmpty = isComboBoxEmpty(motorComboBox);
        boolean isMotorGucuComboBoxEmpty = isComboBoxEmpty(motorGucuComboBox);
        boolean isPompaComboBoxEmpty = isComboBoxEmpty(pompaComboBox);
        boolean isTankTipiComboBoxEmpty = isComboBoxEmpty(tankTipiComboBox);
        boolean isPlatformTipiComboBoxEmpty = isComboBoxEmpty(platformTipiComboBox);

        boolean isTankKapasitesiEmpty = isStringEmpty(secilenTankKapasitesi);
        boolean isOzelTankGenislikEmpty = isStringEmpty(secilenOzelTankGenislik);
        boolean isOzelTankYukseklikEmpty = isStringEmpty(secilenOzelTankYukseklik);
        boolean isOzelTankDerinlikEmpty = isStringEmpty(secilenOzelTankDerinlik);

        boolean isTankCapacityInvalid = isTankKapasitesiEmpty &&
                (isOzelTankGenislikEmpty || isOzelTankYukseklikEmpty || isOzelTankDerinlikEmpty);

        if (Objects.equals(secilenPlatformTipi, "ESP")) {
            boolean isInisTipiComboBoxEmpty = isComboBoxEmpty(inisTipiComboBox);
            return isSiparisNumarasiEmpty ||
                    isMotorComboBoxEmpty ||
                    isMotorGucuComboBoxEmpty ||
                    isPompaComboBoxEmpty ||
                    isTankTipiComboBoxEmpty ||
                    isTankCapacityInvalid ||
                    isPlatformTipiComboBoxEmpty ||
                    isInisTipiComboBoxEmpty;
        } else if (Objects.equals(secilenPlatformTipi, "Devirmeli + Yürüyüş")) {
            return isSiparisNumarasiEmpty ||
                    isMotorComboBoxEmpty ||
                    isMotorGucuComboBoxEmpty ||
                    isPompaComboBoxEmpty ||
                    isTankTipiComboBoxEmpty ||
                    isTankCapacityInvalid ||
                    isPlatformTipiComboBoxEmpty;
        } else if (Objects.equals(secilenPlatformTipi, "Özel")) {
            boolean isBirinciValfComboBoxEmpty = isComboBoxEmpty(birinciValfComboBox);
            boolean isIkinciValfComboBoxEmpty = isComboBoxEmpty(ikinciValfComboBox);
            return isSiparisNumarasiEmpty ||
                    isMotorComboBoxEmpty ||
                    isMotorGucuComboBoxEmpty ||
                    isPompaComboBoxEmpty ||
                    isTankTipiComboBoxEmpty ||
                    isTankCapacityInvalid ||
                    isPlatformTipiComboBoxEmpty ||
                    isBirinciValfComboBoxEmpty ||
                    isIkinciValfComboBoxEmpty;
        }

        return false;
    }

    private void enableSonucSection() {
        kullanilacakKabinText.setVisible(true);
        Image image = null;

        if(secilenPlatformTipi != null) {
            if(Objects.equals(secilenPlatformTipi, "ESP")) {
                if(Objects.equals(secilenInisTipi, "İnişte Tek Hız")) {
                    image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/data/hydraulicUnitData/cabins/powerpack/tekhiz.png")));
                } else if(Objects.equals(secilenInisTipi, "İnişte Çift Hız")) {
                    image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/data/hydraulicUnitData/cabins/powerpack/cifthiz.png")));
                }
            } else if(Objects.equals(secilenPlatformTipi, "Devirmeli + Yürüyüş")) {
                image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/data/hydraulicUnitData/cabins/powerpack/ozel.png")));
            } else if(Objects.equals(secilenPlatformTipi, "Özel")) {
                if(secilenBirinciValf != null && Objects.equals(secilenIkinciValf, "Yok")) {
                    image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/data/hydraulicUnitData/cabins/powerpack/tekvalf.png")));
                } else if(secilenIkinciValf != null) {
                    image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/data/hydraulicUnitData/cabins/powerpack/ozel.png")));
                }
            }
        }

        sonucTankGorsel.setImage(image);

        calculateKabin();

        //Butonlar:
        sonucButtonEnable();
    }

    @FXML
    public void exportProcess() {
        int startX = 800;
        int startY = 270;
        int width = 370;
        int height = 430;

        if(hesaplamaBitti) {
            pdfShaper(0);
            PDFUtil.coords2Png(startX, startY, width, height, exportButton);
            pdfShaper(1);
            PDFUtil.cropImage(startX, startY, width, height, "cropped_screenshot.png");

            String pdfPath = "";
            if(Objects.equals(secilenPlatformTipi, "Özel")) {
                pdfPath = "/assets/data/hydraulicUnitData/pdf/hidrosozel.pdf";
            } else if(Objects.equals(secilenPlatformTipi, "ESP")) {
                if(Objects.equals(secilenInisTipi, "İnişte Tek Hız")) {
                    pdfPath = "/assets/data/hydraulicUnitData/pdf/hidrosinistetek.pdf";
                } else if(Objects.equals(secilenInisTipi, "İnişte Çift Hız")) {
                    pdfPath = "/assets/data/hidrosinistecift.pdf";
                }
            } else if(Objects.equals(secilenPlatformTipi, "Devirmeli + Yürüyüş")) {
                pdfPath = "/assets/data/hydraulicUnitData/pdf/hidrosdevirmeli.pdf";
            }
            pdfPath = null;
            PDFUtil.pdfGenerator("/assets/images/general/onder_grup_main.png", "cropped_screenshot.png", null, pdfPath, girilenSiparisNumarasi, kullanilacakKabinText.getText().toString());
        } else {
            Utils.showErrorMessage("Lütfen hesaplama işlemini tamamlayıp tekrar deneyin.");
        }
    }

    private void disableAllCombos() {
        motorComboBox.setDisable(true);
        motorGucuComboBox.setDisable(true);
        pompaComboBox.setDisable(true);
        uniteTipiComboBox.setDisable(true);
        tankTipiComboBox.setDisable(true);
        tankKapasitesiComboBox.setDisable(true);
        birinciValfComboBox.setDisable(true);
        inisTipiComboBox.setDisable(true);
        platformTipiComboBox.setDisable(true);
        ikinciValfComboBox.setDisable(true);
    }

    private void pdfShaper(int type) {
        if(type == 0) {
            //pdf oluşturma öncesi
            klasikVBox.setStyle("-fx-background-color: #999999;"); //sarı: #F9F871
            sonucAnaLabelTxt.setFill(Color.BLACK);
        } else {
            //pdf oluşturma sonrası
            klasikVBox.setStyle("-fx-background-color: #353a46;");
            sonucAnaLabelTxt.setFill(Color.web("#B7C3D7"));
        }
    }

    private void sonucButtonEnable() {
        kaydetButton.setDisable(false);
        if(Main.loggedInUser != null) {
            exportButton.setDisable(false);
        }
        parcaListesiButton.setDisable(false);
    }

    private void sonucButtonDisable() {
        kaydetButton.setDisable(true);
        exportButton.setDisable(true);
        parcaListesiButton.setDisable(true);
    }

    private void calculateKabin() {
        if(!secilenTankTipi.contains("Özel")) {
            String motorKW = secilenMotorGucu.trim();
            String tankKapasite = secilenTankKapasitesi.trim();

            if(Objects.equals(motorKW, "0.37 kW")) {
                if(Objects.equals(tankKapasite, "4 Lt") || Objects.equals(tankKapasite, "6 Lt") || Objects.equals(tankKapasite, "8 Lt") || Objects.equals(tankKapasite, "12 Lt")) {
                    kullanilacakKabinText.setText("Kullanılacak Kabin: KD-8 Engelli\nÖlçü: 330x370x640\nKabin Kodu: 151-06-05-061");
                    kabinKodu = "KD-8 Engelli";
                } else if(Objects.equals(tankKapasite, "10 Lt") || Objects.equals(tankKapasite, "20 Lt") || Objects.equals(tankKapasite, "30 Lt")) {
                    kullanilacakKabinText.setText("Kullanılacak Kabin: KD-10 (CARREFOUR)\nÖlçü: 390x400x840\nKabin Kodu: 151-06-05-103");
                    kabinKodu = "KD-10 (CARREFOUR)";
                }
            } else if(Objects.equals(motorKW, "0.55 kW")) {
                if(Objects.equals(tankKapasite, "4 Lt") || Objects.equals(tankKapasite, "6 Lt") || Objects.equals(tankKapasite, "8 Lt") || Objects.equals(tankKapasite, "12 Lt")) {
                    kullanilacakKabinText.setText("Kullanılacak Kabin: KD-8 Engelli\nÖlçü: 330x370x640\nKabin Kodu: 151-06-05-061");
                    kabinKodu = "KD-8 Engelli";
                } else if(Objects.equals(tankKapasite, "10 Lt") || Objects.equals(tankKapasite, "20 Lt") || Objects.equals(tankKapasite, "30 Lt")) {
                    kullanilacakKabinText.setText("Kullanılacak Kabin: KD-10 (CARREFOUR)\nÖlçü: 390x400x840\nKabin Kodu: 151-06-05-103");
                    kabinKodu = "KD-10 (CARREFOUR)";
                }
            } else if(Objects.equals(motorKW, "0.75 kW")) {
                if(Objects.equals(tankKapasite, "4 Lt") || Objects.equals(tankKapasite, "6 Lt") || Objects.equals(tankKapasite, "8 Lt") || Objects.equals(tankKapasite, "12 Lt")) {
                    kullanilacakKabinText.setText("Kullanılacak Kabin: KD-8 Engelli\nÖlçü: 330x370x640\nKabin Kodu: 151-06-05-061");
                    kabinKodu = "KD-8 Engelli";
                } else if(Objects.equals(tankKapasite, "10 Lt") || Objects.equals(tankKapasite, "20 Lt") || Objects.equals(tankKapasite, "30 Lt")) {
                    kullanilacakKabinText.setText("Kullanılacak Kabin: KD-10 (CARREFOUR)\nÖlçü: 390x400x840\nKabin Kodu: 151-06-05-103");
                    kabinKodu = "KD-10 (CARREFOUR)";
                }
            } else if(Objects.equals(motorKW, "1.1 kW")) {
                if(Objects.equals(tankKapasite, "4 Lt") || Objects.equals(tankKapasite, "6 Lt") || Objects.equals(tankKapasite, "8 Lt") || Objects.equals(tankKapasite, "12 Lt")) {
                    kullanilacakKabinText.setText("Kullanılacak Kabin: KD-8 Engelli\nÖlçü: 330x370x640\nKabin Kodu: 151-06-05-061");
                    kabinKodu = "KD-8 Engelli";
                } else if(Objects.equals(tankKapasite, "10 Lt") || Objects.equals(tankKapasite, "20 Lt") || Objects.equals(tankKapasite, "30 Lt")) {
                    kullanilacakKabinText.setText("Kullanılacak Kabin: KD-10 (CARREFOUR)\nÖlçü: 390x400x840\nKabin Kodu: 151-06-05-103");
                    kabinKodu = "KD-10 (CARREFOUR)";
                }
            } else if(Objects.equals(motorKW, "1.5 kW")) {
                if(Objects.equals(tankKapasite, "4 Lt") || Objects.equals(tankKapasite, "6 Lt") || Objects.equals(tankKapasite, "8 Lt") || Objects.equals(tankKapasite, "12 Lt")) {
                    kullanilacakKabinText.setText("Kullanılacak Kabin: KD-8 Engelli\nÖlçü: 330x370x640\nKabin Kodu: 151-06-05-061");
                    kabinKodu = "KD-8 Engelli";
                } else if(Objects.equals(tankKapasite, "10 Lt") || Objects.equals(tankKapasite, "20 Lt") || Objects.equals(tankKapasite, "30 Lt")) {
                    kullanilacakKabinText.setText("Kullanılacak Kabin: KD-10 (CARREFOUR)\nÖlçü: 390x400x840\nKabin Kodu: 151-06-05-103");
                    kabinKodu = "KD-10 (CARREFOUR)";
                }
            } else if(Objects.equals(motorKW, "2.2 kW")) {
                if(Objects.equals(tankKapasite, "4 Lt") || Objects.equals(tankKapasite, "6 Lt") || Objects.equals(tankKapasite, "12 Lt")) {
                    kullanilacakKabinText.setText("Kullanılacak Kabin: KD-8 Engelli\nÖlçü: 330x370x640\nKabin Kodu: 151-06-05-061");
                    kabinKodu = "KD-8 Engelli";
                } else if(Objects.equals(tankKapasite, "10 Lt") || Objects.equals(tankKapasite, "20 Lt") || Objects.equals(tankKapasite, "30 Lt")) {
                    kullanilacakKabinText.setText("Kullanılacak Kabin: KD-10 (CARREFOUR)\nÖlçü: 390x400x840\nKabin Kodu: 151-06-05-103");
                    kabinKodu = "KD-10 (CARREFOUR)";
                }
            } else if(Objects.equals(motorKW, "3 kW")) {
                if(Objects.equals(tankKapasite, "4 Lt") || Objects.equals(tankKapasite, "6 Lt") || Objects.equals(tankKapasite, "12 Lt")) {
                    kullanilacakKabinText.setText("Kullanılacak Kabin: KD-8 Engelli\nÖlçü: 330x370x640\nKabin Kodu: 151-06-05-061");
                    kabinKodu = "KD-8 Engelli";
                } else if(Objects.equals(tankKapasite, "10 Lt") || Objects.equals(tankKapasite, "20 Lt") || Objects.equals(tankKapasite, "30 Lt")) {
                    kullanilacakKabinText.setText("Kullanılacak Kabin: KD-10 (CARREFOUR)\nÖlçü: 390x400x840\nKabin Kodu: 151-06-05-103");
                    kabinKodu = "KD-10 (CARREFOUR)";
                }
            } else if(Objects.equals(motorKW, "4 kW")) {
                if(Objects.equals(tankKapasite, "4 Lt") || Objects.equals(tankKapasite, "6 Lt") || Objects.equals(tankKapasite, "12 Lt")) {
                    kullanilacakKabinText.setText("Kullanılacak Kabin: KD-8 Engelli\nÖlçü: 330x370x640\nKabin Kodu: 151-06-05-061");
                    kabinKodu = "KD-8 Engelli";
                } else if(Objects.equals(tankKapasite, "10 Lt") || Objects.equals(tankKapasite, "20 Lt") || Objects.equals(tankKapasite, "30 Lt")) {
                    kullanilacakKabinText.setText("Kullanılacak Kabin: KDB-20 (BALİNA)\nÖlçü: 390x400x840\nKabin Kodu: 151-06-05-103\nKabin Kodu: 150-52-19-011");
                    kabinKodu = "KDB-20 (BALİNA)";
                }
            }
        }
    }

    /**
     * Verilen bir String'in boş veya null olup olmadığını kontrol eder.
     * @param text Kontrol edilecek String
     * @return Eğer String boş veya null ise true, aksi halde false
     */
    private boolean isStringEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }

    /**
     * Verilen bir ComboBox'ın seçiminin boş olup olmadığını kontrol eder.
     * @param comboBox Kontrol edilecek ComboBox
     * @return Eğer ComboBox'ta seçim yapılmamışsa true, aksi halde false
     */
    private boolean isComboBoxEmpty(ComboBox<?> comboBox) {
        return comboBox.getSelectionModel() == null || comboBox.getSelectionModel().getSelectedItem() == null;
    }
}
