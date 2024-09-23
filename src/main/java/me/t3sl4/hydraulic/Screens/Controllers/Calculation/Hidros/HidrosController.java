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
import me.t3sl4.hydraulic.Utils.Model.Table.TableData;
import me.t3sl4.hydraulic.Utils.File.ExcelDataReadUtil;
import me.t3sl4.hydraulic.Utils.File.PDFUtil;
import me.t3sl4.hydraulic.Utils.File.GeneralFileSystem;
import me.t3sl4.hydraulic.Utils.HTTP.HTTPRequest;
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
    private TextField tankKapasitesiField;

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


    public static String girilenSiparisNumarasi;
    public static String secilenMotorTipi = null;
    public static String secilenMotorGucu = null;
    public static String secilenPompa = null;
    public static String uniteTipiDurumu = null;
    public static String secilenTankTipi = null;
    public static String secilenTankKapasitesi = null;
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
        tankKapasitesiField.setText("");
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
    }

    @FXML
    public void parcaListesiGoster() {
        Image icon = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/icons/logo.png")));
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

        if (GeneralFileSystem.fileExists(pdfPath) && GeneralFileSystem.fileExists(excelPath)) {
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
            sonucAnaLabelTxt.setText("Sipariş Numarası: " + girilenSiparisNumarasi);
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
                initTankKapasitesi();
                if(secilenTankTipi != null) {
                    tabloGuncelle();
                }
            }
        });

        tankKapasitesiField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!tankKapasitesiField.getText().isEmpty()) {
                secilenTankKapasitesi = newValue.toString();
                initPlatformTipi();
                if(secilenTankKapasitesi != null) {
                    tabloGuncelle();
                }
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

        data = new TableData("Platform Tipi:", secilenPlatformTipi);
        sonucTablo.getItems().add(data);

        data = new TableData("İniş Metodu:", secilenInisTipi);
        sonucTablo.getItems().add(data);

        data = new TableData("Birinci Valf:", secilenBirinciValf);
        sonucTablo.getItems().add(data);

        data = new TableData("İkinci Valf:", secilenIkinciValf);
        sonucTablo.getItems().add(data);
    }

    private void initMotorTipi() {
        motorComboBox.getItems().clear();
        motorComboBox.getItems().addAll("380 V", "220 V");
    }

    private void initMotorGucu() {
        motorGucuComboBox.getItems().clear();
        if(uniteTipiDurumu.equals("Hidros")) {
            if(secilenMotorTipi.equals("380 V")) {
                motorGucuComboBox.getItems().addAll(ExcelDataReadUtil.dataManipulator.motorDegerleriHidros380);
            } else if(secilenMotorTipi.equals("220 V")) {
                motorGucuComboBox.getItems().addAll(ExcelDataReadUtil.dataManipulator.motorDegerleriHidros220);
            }
        } else {
            if(secilenMotorTipi.equals("380 V")) {
                motorGucuComboBox.getItems().addAll(ExcelDataReadUtil.dataManipulator.motorDegerleriIthal380);
            } else if(secilenMotorTipi.equals("220 V")) {
                motorGucuComboBox.getItems().addAll(ExcelDataReadUtil.dataManipulator.motorDegerleriIthal220);
            }
        }
    }

    private void initPompa() {
        pompaComboBox.getItems().clear();
        if(uniteTipiDurumu.equals("Hidros")) {
            pompaComboBox.getItems().addAll(ExcelDataReadUtil.dataManipulator.pompaKapasiteDegerleriHidros);
        } else {
            pompaComboBox.getItems().addAll(ExcelDataReadUtil.dataManipulator.pompaKapasiteDegerleriIthal);
        }
    }

    private void initUniteTipi() {
        uniteTipiComboBox.getItems().clear();
        uniteTipiComboBox.getItems().addAll("Hidros", "İthal");
    }

    private void initTankTipi() {
        tankTipiComboBox.getItems().clear();
        tankTipiComboBox.getItems().addAll("Dikey", "Yatay", "Özel");
    }

    private void initTankKapasitesi() {
        //Eklenebilir ileride
    }

    private void initPlatformTipi() {
        platformTipiComboBox.getItems().clear();
        platformTipiComboBox.getItems().addAll(ExcelDataReadUtil.dataManipulator.platformDegerleriHidros);
    }

    private void initValfTipi() {
        birinciValfComboBox.getItems().clear();
        birinciValfComboBox.getItems().addAll(ExcelDataReadUtil.dataManipulator.valfDegerleriHidros);
    }

    private void initInisMetodu() {
        inisTipiComboBox.getItems().clear();
        inisTipiComboBox.getItems().addAll("İnişte Tek Hız", "İnişte Çift Hız");
    }

    private void initIkinciValf() {
        ikinciValfComboBox.getItems().clear();
        ikinciValfComboBox.getItems().addAll(ExcelDataReadUtil.dataManipulator.valfDegerleriHidros);
        ikinciValfComboBox.getItems().addAll("Yok");
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
            tankKapasitesiField.setDisable(false);
        }
    }

    @FXML
    public void tankKapasitesiEntered() {
        if (tankKapasitesiField.getText() != null) {
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
        if(Objects.equals(secilenPlatformTipi, "ESP")) {
            return siparisNumarasi.getText().isEmpty() ||
                    motorComboBox.getSelectionModel().isEmpty() || motorGucuComboBox.getSelectionModel().isEmpty() ||
                    pompaComboBox.getSelectionModel().isEmpty() || tankTipiComboBox.getSelectionModel().isEmpty() ||
                    secilenTankKapasitesi.isEmpty() || platformTipiComboBox.getSelectionModel().isEmpty() ||
                    inisTipiComboBox.getSelectionModel().isEmpty();
        } else if(Objects.equals(secilenPlatformTipi, "Devirmeli") || Objects.equals(secilenPlatformTipi, "Yürüyüş")) {
            return siparisNumarasi.getText().isEmpty() ||
                    motorComboBox.getSelectionModel().isEmpty() || motorGucuComboBox.getSelectionModel().isEmpty() ||
                    pompaComboBox.getSelectionModel().isEmpty() || tankTipiComboBox.getSelectionModel().isEmpty() ||
                    secilenTankKapasitesi.isEmpty() || platformTipiComboBox.getSelectionModel().isEmpty();
        } else if(Objects.equals(secilenPlatformTipi, "Özel")) {
            return siparisNumarasi.getText().isEmpty() ||
                    motorComboBox.getSelectionModel().isEmpty() || motorGucuComboBox.getSelectionModel().isEmpty() ||
                    pompaComboBox.getSelectionModel().isEmpty() || tankTipiComboBox.getSelectionModel().isEmpty() ||
                    secilenTankKapasitesi.isEmpty() || platformTipiComboBox.getSelectionModel().isEmpty() ||
                    birinciValfComboBox.getSelectionModel().isEmpty() || ikinciValfComboBox.getSelectionModel().isEmpty();
        }

        return false;
    }

    private void enableSonucSection() {
        kullanilacakKabinText.setVisible(true);
        Image image = null;

        if(secilenPlatformTipi != null) {
            if(Objects.equals(secilenPlatformTipi, "ESP")) {
                if(Objects.equals(secilenInisTipi, "İnişte Tek Hız")) {
                    image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/icons/tanklar/hidros/tekhiz.png")));
                } else if(Objects.equals(secilenInisTipi, "İnişte Çift Hız")) {
                    image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/icons/tanklar/hidros/cifthiz.png")));
                }
            } else if(Objects.equals(secilenPlatformTipi, "Devirmeli")) {
                image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/icons/tanklar/hidros/ozel.png")));
            } else if(Objects.equals(secilenPlatformTipi, "Yürüyüş")) {
                image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/icons/tanklar/hidros/ozel.png")));
            } else if(Objects.equals(secilenPlatformTipi, "Özel")) {
                if(secilenBirinciValf != null && Objects.equals(secilenIkinciValf, "Yok")) {
                    image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/icons/tanklar/hidros/tekvalf.png")));
                } else if(secilenIkinciValf != null) {
                    image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/icons/tanklar/hidros/ozel.png")));
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
        int startX = 500;
        int startY = 10;
        int width = 800;
        int height = 565;

        if(hesaplamaBitti) {
            pdfShaper(0);
            PDFUtil.coords2Png(startX, startY, width, height, exportButton);
            pdfShaper(1);
            PDFUtil.cropImage(680, startY, 370, height);

            String pdfPath = "";
            if(Objects.equals(secilenPlatformTipi, "Özel")) {
                pdfPath = "/assets/data/pdf/hidrosozel.pdf";
            } else if(Objects.equals(secilenPlatformTipi, "ESP")) {
                if(Objects.equals(secilenInisTipi, "İnişte Tek Hız")) {
                    pdfPath = "/assets/data/pdf/hidrosinistetek.pdf";
                } else if(Objects.equals(secilenInisTipi, "İnişte Çift Hız")) {
                    pdfPath = "/assets/data/hidrosinistecift.pdf";
                }
            } else if(Objects.equals(secilenPlatformTipi, "Devirmeli")) {
                pdfPath = "/assets/data/pdf/hidrosdevirmeli.pdf";
            } else if(Objects.equals(secilenPlatformTipi, "Yürüyüş")) {
                pdfPath = "/assets/data/pdf/hidrosdevirmeli.pdf";
            }
            PDFUtil.pdfGenerator("/assets/icons/onderGrupMain.png", "cropped_screenshot.png", pdfPath, girilenSiparisNumarasi);
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
        tankKapasitesiField.setDisable(true);
        birinciValfComboBox.setDisable(true);
        inisTipiComboBox.setDisable(true);
        platformTipiComboBox.setDisable(true);
        ikinciValfComboBox.setDisable(true);
    }

    private void pdfShaper(int type) {
        if(type == 0) {
            //pdf oluşturma öncesi
            klasikVBox.setStyle("-fx-background-color: #FFFFFF;"); //sarı: #F9F871
            sonucAnaLabelTxt.setFill(Color.BLACK);
        } else {
            //pdf oluşturma sonrası
            klasikVBox.setStyle("-fx-background-color: #353a46;");
            sonucAnaLabelTxt.setFill(Color.web("#B7C3D7"));
        }
    }

    private void sonucButtonEnable() {
        kaydetButton.setDisable(false);
        exportButton.setDisable(false);
        parcaListesiButton.setDisable(false);
    }

    private void sonucButtonDisable() {
        kaydetButton.setDisable(true);
        exportButton.setDisable(true);
        parcaListesiButton.setDisable(true);
    }

    private void calculateKabin() {
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
