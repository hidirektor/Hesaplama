package me.t3sl4.hydraulic.Controllers;

import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import me.t3sl4.hydraulic.Launcher;
import me.t3sl4.hydraulic.Util.Data.Table.TableData;
import me.t3sl4.hydraulic.Util.Util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.Objects;

public class HidrosController {
    @FXML
    private VBox klasikVBox;

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
    private ComboBox birinciValfComboBox;

    @FXML
    private ComboBox inisTipiComboBox;

    @FXML
    private ComboBox platformTipiComboBox;

    @FXML
    private ComboBox ikinciValfComboBox;

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


    public String girilenSiparisNumarasi;
    public String secilenMotorTipi = null;
    public String secilenMotorGucu = null;
    public String secilenPompa = null;
    public String secilenTankTipi = null;
    public String secilenTankKapasitesi = null;
    public String secilenBirinciValf = null;
    public String secilenInisTipi = null;
    public String secilenPlatformTipi = null;
    public String secilenIkinciValf = null;

    public boolean hesaplamaBitti = false;

    public void initialize() {
        comboBoxListener();
        sonucTabloSatir1.setCellValueFactory(new PropertyValueFactory<>("satir1Property"));
        sonucTabloSatir2.setCellValueFactory(new PropertyValueFactory<>("satir2Property"));
    }

    @FXML
    public void hesaplaFunc() {
        if(checkComboBox()) {
            Util.showErrorMessage("Lütfen tüm girdileri kontrol edin.");
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
        tankTipiComboBox.getSelectionModel().clearSelection();
        tankTipiComboBox.setPromptText("Tank Tipi");
        tankKapasitesiComboBox.getSelectionModel().clearSelection();
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

        data = new TableData("Tank Tipi:", secilenTankTipi);
        sonucTablo.getItems().add(data);

        data = new TableData("Tank Kapasitesi:", secilenTankKapasitesi);
        sonucTablo.getItems().add(data);

        data = new TableData("Platform Tipi:", secilenPlatformTipi);
        sonucTablo.getItems().add(data);

        data = new TableData("Birinci Valf:", secilenBirinciValf);
        sonucTablo.getItems().add(data);

        data = new TableData("İkinci Valf:", secilenIkinciValf);
        sonucTablo.getItems().add(data);

        data = new TableData("İniş Metodu:", secilenInisTipi);
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

    private void initPlatformTipi() {
        platformTipiComboBox.getItems().clear();
        platformTipiComboBox.getItems().addAll(Util.dataManipulator.platformDegerleriHidros);
    }

    private void initValfTipi() {
        birinciValfComboBox.getItems().clear();
        birinciValfComboBox.getItems().addAll(Util.dataManipulator.valfDegerleriHidros);
    }

    private void initInisMetodu() {
        inisTipiComboBox.getItems().clear();
        inisTipiComboBox.getItems().addAll("İnişte Tek Hız", "İnişte Çift Hız");
    }

    private void initIkinciValf() {
        ikinciValfComboBox.getItems().clear();
        ikinciValfComboBox.getItems().addAll(Util.dataManipulator.valfDegerleriHidros);
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
            platformTipiComboBox.setDisable(false);
        }
    }

    @FXML
    public void platformTipiPressed() {
        if(platformTipiComboBox.getValue() != null) {
            secilenPlatformTipi = platformTipiComboBox.getValue().toString();

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
            secilenBirinciValf = birinciValfComboBox.getValue().toString();
            ikinciValfComboBox.setDisable(false);
        }
    }

    @FXML
    public void inisTipiPressed() {
        if(inisTipiComboBox.getValue() != null) {
            secilenInisTipi = inisTipiComboBox.getValue().toString();
        }
    }

    @FXML
    public void ikinciValfPressed() {
        if(ikinciValfComboBox.getValue() != null) {
            secilenIkinciValf = ikinciValfComboBox.getValue().toString();
            enableSonucSection();
        }
    }

    private boolean checkComboBox() {
        if(Objects.equals(secilenPlatformTipi, "ESP")) {
            if(siparisNumarasi.getText().isEmpty() ||
                    motorComboBox.getSelectionModel().isEmpty() || motorGucuComboBox.getSelectionModel().isEmpty() ||
                    pompaComboBox.getSelectionModel().isEmpty() || tankTipiComboBox.getSelectionModel().isEmpty() ||
                    tankKapasitesiComboBox.getSelectionModel().isEmpty() || platformTipiComboBox.getSelectionModel().isEmpty() ||
            inisTipiComboBox.getSelectionModel().isEmpty()) {
                return true;
            }
        } else if(Objects.equals(secilenPlatformTipi, "Devirmeli") || Objects.equals(secilenPlatformTipi, "Yürüyüş")) {
            if(siparisNumarasi.getText().isEmpty() ||
                    motorComboBox.getSelectionModel().isEmpty() || motorGucuComboBox.getSelectionModel().isEmpty() ||
                    pompaComboBox.getSelectionModel().isEmpty() || tankTipiComboBox.getSelectionModel().isEmpty() ||
                    tankKapasitesiComboBox.getSelectionModel().isEmpty() || platformTipiComboBox.getSelectionModel().isEmpty()) {
                return true;
            }
        } else if(Objects.equals(secilenPlatformTipi, "Özel")) {
            if(siparisNumarasi.getText().isEmpty() ||
                    motorComboBox.getSelectionModel().isEmpty() || motorGucuComboBox.getSelectionModel().isEmpty() ||
                    pompaComboBox.getSelectionModel().isEmpty() || tankTipiComboBox.getSelectionModel().isEmpty() ||
                    tankKapasitesiComboBox.getSelectionModel().isEmpty() || platformTipiComboBox.getSelectionModel().isEmpty() ||
                    birinciValfComboBox.getSelectionModel().isEmpty() || ikinciValfComboBox.getSelectionModel().isEmpty()) {
                return true;
            }
        }

        return false;
    }

    private void enableSonucSection() {
        Image image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("icons/tanklar/hidros/ornek.png")));
        sonucTankGorsel.setImage(image);

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
            coords2Png(startX, startY, width, height);
            pdfShaper(1);
            cropImage(680, startY, 370, height);

            Util.pdfGenerator("icons/onderGrupMain.png", "cropped_screenshot.png", "/data/test.pdf", girilenSiparisNumarasi);
        } else {
            Util.showErrorMessage("Lütfen hesaplama işlemini tamamlayıp tekrar deneyin.");
        }
    }

    private void coords2Png(int startX, int startY, int width, int height) {
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setViewport(new javafx.geometry.Rectangle2D(startX, startY, width, height));

        WritableImage screenshot = exportButton.getScene().snapshot(null);

        File outputFile = new File("screenshot.png");

        BufferedImage bufferedImage = convertToBufferedImage(screenshot);

        try {
            ImageIO.write(bufferedImage, "png", outputFile);
            System.out.println("Ekran görüntüsü başarıyla kaydedildi: " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Ekran görüntüsü kaydedilirken bir hata oluştu: " + e.getMessage());
        }
    }

    private BufferedImage convertToBufferedImage(WritableImage writableImage) {
        int width = (int) writableImage.getWidth();
        int height = (int) writableImage.getHeight();
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);

        PixelReader pixelReader = writableImage.getPixelReader();
        WritablePixelFormat<IntBuffer> pixelFormat = WritablePixelFormat.getIntArgbInstance();

        int[] pixelData = new int[width * height];
        pixelReader.getPixels(0, 0, width, height, pixelFormat, pixelData, 0, width);

        bufferedImage.setRGB(0, 0, width, height, pixelData, 0, width);

        return bufferedImage;
    }

    private void cropImage(int startX, int startY, int width, int height) {
        try {
            BufferedImage originalImage = ImageIO.read(new File("screenshot.png"));

            BufferedImage croppedImage = originalImage.getSubimage(startX, startY, width, height);

            String croppedFilePath = "cropped_screenshot.png";
            ImageIO.write(croppedImage, "png", new File(croppedFilePath));
            System.out.println("Kırpılmış fotoğraf başarıyla kaydedildi: " + croppedFilePath);

            File originalFile = new File("screenshot.png");
            if (originalFile.delete()) {
                System.out.println("Eski fotoğraf başarıyla silindi: " + "screenshot.png");
            } else {
                System.out.println("Eski fotoğraf silinemedi: " + "screenshot.png");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void disableAllCombos() {
        motorComboBox.setDisable(true);
        motorGucuComboBox.setDisable(true);
        pompaComboBox.setDisable(true);
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
}
