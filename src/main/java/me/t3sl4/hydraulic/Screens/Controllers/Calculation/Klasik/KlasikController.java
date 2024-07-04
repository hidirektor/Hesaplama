package me.t3sl4.hydraulic.Screens.Controllers.Calculation.Klasik;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import me.t3sl4.hydraulic.Launcher;
import me.t3sl4.hydraulic.Screens.Main;
import me.t3sl4.hydraulic.Utility.Calculation.Calculator;
import me.t3sl4.hydraulic.Utility.Data.Table.TableData;
import me.t3sl4.hydraulic.Utility.File.ExcelUtil;
import me.t3sl4.hydraulic.Utility.File.PDFFileUtil;
import me.t3sl4.hydraulic.Utility.File.SystemUtil;
import me.t3sl4.hydraulic.Utility.HTTP.HTTPRequest;
import me.t3sl4.hydraulic.Utility.ReqUtil;
import me.t3sl4.hydraulic.Utility.Utils;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

import static me.t3sl4.hydraulic.Launcher.BASE_URL;
import static me.t3sl4.hydraulic.Launcher.insertHydraulicURLPrefix;

public class KlasikController {
    @FXML
    private ComboBox<String> motorComboBox;

    @FXML
    private ComboBox<String> pompaComboBox;

    @FXML
    private ComboBox<String> valfTipiComboBox;

    @FXML
    private ComboBox<String> hidrolikKilitComboBox;

    @FXML
    private ComboBox<String> sogutmaComboBox;

    @FXML
    private TextField tankKapasitesiTextField;

    @FXML
    private Label genislikSonucText;

    @FXML
    private Label yukseklikSonucText;

    @FXML
    private Label derinlikSonucText;

    @FXML
    private Label hacimText;

    @FXML
    private Text kilitMotorText;

    @FXML
    private Text kilitPompaText;

    @FXML
    private ComboBox<String> kilitMotorComboBox;

    @FXML
    private ComboBox<String> kilitPompaComboBox;

    @FXML
    private Text kullanilacakKabin;

    @FXML
    private TableView<TableData> sonucTablo;

    @FXML
    private TableColumn<TableData, String> sonucTabloSatir1;

    @FXML
    private TableColumn<TableData, String> sonucTabloSatir2;

    @FXML
    private TextField siparisNumarasi;

    @FXML
    private Text sonucAnaLabelTxt;

    @FXML
    private ImageView sonucKapakImage;

    @FXML
    private Text kampanaOlcuText;

    @FXML
    private Text kampanaOlcuText2;

    @FXML
    private Text kilitliBlokOlcuText;

    @FXML
    private Text kilitliBlokOlcuText2;

    @FXML
    private Text tahliyeOlcuText;

    @FXML
    private Text dolumOlcuText;

    @FXML
    private Text dolumOlcuText2;

    @FXML
    private Text kampana2OlcuText;
    @FXML
    private Text kampana2OlcuText2;

    @FXML
    private Text kilitMotorOlcuText;
    @FXML
    private Text kilitMotorOlcuText2;

    @FXML
    private Text kilitliBlok2OlcuText;
    @FXML
    private Text kilitliBlok2OlcuText2;


    @FXML
    private Text kucukHalkaCap2Text;
    @FXML
    private Text buyukHalkaCap2Text;
    @FXML
    private Text kucukHalkaCapText;
    @FXML
    private Text buyukHalkaCapText;
    @FXML
    private Text dolum2OlcuText;
    @FXML
    private Text dolum2OlcuText2;

    @FXML
    private Text tahliye2OlcuText;

    @FXML
    private Text kampanaVeriText;
    @FXML
    private Text kampanaVeri2Text;

    @FXML
    private Text kilitliBlokVeriText;

    @FXML
    private Text kilitliBlokVeri2Text;

    @FXML
    private Text kilitMotorVeriText;

    @FXML
    private Button exportButton;

    @FXML
    private Button parcaListesiButton;

    @FXML
    private Text kilitMotorIcOlcuText;

    @FXML
    private ImageView sonucTankGorsel;

    @FXML
    private Text sonucUstText;
    @FXML
    private Text sonucSagText;

    @FXML
    private VBox klasikVBox;

    /*
    Seçilen Değerler:
     */
    public static String girilenSiparisNumarasi = null;
    public String secilenUniteTipi = "Klasik";
    public static String secilenMotor = null;
    public static int secilenKampana = 0;
    public static String secilenPompa = null;
    public static double secilenPompaVal;
    public static int girilenTankKapasitesiMiktari = 0;
    public String secilenHidrolikKilitDurumu = null;
    public static String secilenValfTipi = null;
    public String secilenKilitMotor = null;
    public String secilenKilitPompa = null;
    public static String secilenSogutmaDurumu = null;

    public boolean hidrolikKilitStat = false;
    public boolean sogutmaStat = false;
    public boolean hesaplamaBitti = false;

    public static String atananHT;

    private double x, y;

    public static String atananKabinFinal = "";
    public static String gecisOlculeriFinal = "";

    public void initialize() {
        Utils.textFilter(tankKapasitesiTextField);
        defineKabinOlcu();
        comboBoxListener();
        sonucTabloSatir1.setCellValueFactory(new PropertyValueFactory<>("satir1Property"));
        sonucTabloSatir2.setCellValueFactory(new PropertyValueFactory<>("satir2Property"));
    }

    @FXML
    public void hesaplaFunc() {
        int h = 0; // Yükseklik
        int y = 0; // Derinlik
        int x = 0; // Genişlik
        int hacim = 0; // Hacim
        ArrayList<Integer> results;

        if (checkComboBox()) {
            Utils.showErrorMessage("Lütfen tüm girdileri kontrol edin.");
        } else {
            enableSonucSection();
            results = Calculator.calcDimensions(x, y, secilenKampana,
                    motorComboBox.getSelectionModel().getSelectedIndex(), secilenSogutmaDurumu, secilenHidrolikKilitDurumu,
                    secilenValfTipi, secilenPompaVal, secilenKilitMotor, girilenTankKapasitesiMiktari, kullanilacakKabin);

            if (results.size() == 4) {
                x = results.get(0);
                y = results.get(1);
                h = results.get(2);
                hacim = results.get(3);

                genislikSonucText.setText("X: " + x + " mm");
                derinlikSonucText.setText("Y: " + y + " mm");
                yukseklikSonucText.setText("h: " + h + " mm");
                hacimText.setText("Tank : " + hacim + "L");

                tabloGuncelle();
                Image image;
                if (Objects.equals(secilenSogutmaDurumu, "Var") && Objects.equals(secilenHidrolikKilitDurumu, "Var")) {
                    image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/icons/sogutmaKilit.png")));
                    imageTextEnable(x, y);
                } else {
                    if (secilenKilitMotor != null) {
                        image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/icons/normal.png")));
                    } else {
                        image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/icons/kilitMotor.png")));
                    }
                    imageTextEnable(x, y);
                }
                tankGorselLoad();

                sonucKapakImage.setImage(image);
                parcaListesiButton.setDisable(false);
                exportButton.setDisable(false);

                hesaplamaBitti = true;
            } else {
                Utils.showErrorMessage("Hesaplama sonucu beklenmeyen bir hata oluştu.");
            }
        }
    }


    @FXML
    public void transferCalculation() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        String pdfPath = System.getProperty("user.home") + "/Desktop/" + girilenSiparisNumarasi + ".pdf";
        String excelPath = System.getProperty("user.home") + "/Desktop/" + girilenSiparisNumarasi + ".xlsx";

        if (SystemUtil.fileExists(pdfPath) && SystemUtil.fileExists(excelPath)) {
            String pdfURL = girilenSiparisNumarasi + ".pdf";
            String excelURL = girilenSiparisNumarasi + ".xlsx";
            String url = BASE_URL + insertHydraulicURLPrefix;
            String jsonBody = "{\n" +
                    "  \"OrderNumber\": \"" + girilenSiparisNumarasi + "\",\n" +
                    "  \"OrderDate\": \"" + dtf.format(now) + "\",\n" +
                    "  \"Type\": \"" + secilenUniteTipi + "\",\n" +
                    "  \"InCharge\": \"" + Main.loggedInUser.getUsername() + "\",\n" +
                    "  \"PDF\": \"" + pdfURL + "\",\n" +
                    "  \"PartList\": \"" + excelURL + "\",\n" +
                    "  \"InChargeName\": \"" + Main.loggedInUser.getFullName() + "\"\n" +
                    "}";

            HTTPRequest.sendRequest(url, jsonBody, new HTTPRequest.RequestCallback() {
                @Override
                public void onSuccess(String response) throws IOException {
                    if(ReqUtil.uploadPDFFile2Server(pdfPath, girilenSiparisNumarasi) && ReqUtil.uploadExcelFile2Server(excelPath, girilenSiparisNumarasi)) {
                        Utils.showSuccessMessage("Oluşturulan ünite başarıyla kaydedildi.");
                    }
                }

                @Override
                public void onFailure() {
                    Utils.showErrorMessage("Oluşturulan hidrolik ünitesi kaydedilemedi !");
                }
            });
        } else {
            Utils.showErrorMessage("Lütfen PDF ve parça listesi oluşturduktan sonra kaydedin");
        }
    }

    @FXML
    public void motorPressed() {
        if(motorComboBox.getValue() != null) {
            secilenMotor = motorComboBox.getValue();
            secilenKampana = ExcelUtil.dataManipulator.kampanaDegerleri.get(motorComboBox.getSelectionModel().getSelectedIndex());
            pompaComboBox.setDisable(false);
        }
    }

    @FXML
    public void pompaPressed() {
        if(pompaComboBox.getValue() != null) {
            secilenPompa = pompaComboBox.getValue();
            tankKapasitesiTextField.setDisable(false);
        }
    }

    @FXML
    public void tankKapasitesiEntered() {
        if(tankKapasitesiTextField.getText() != null) {
            if(!tankKapasitesiTextField.getText().isEmpty()) {
                girilenTankKapasitesiMiktari = Integer.parseInt(tankKapasitesiTextField.getText());
                if(girilenTankKapasitesiMiktari < 1 || girilenTankKapasitesiMiktari > 500) {
                    hidrolikKilitComboBox.setDisable(true);
                    disableKilitAndSogutma();
                } else {
                    hidrolikKilitComboBox.setDisable(false);
                    hidrolikKilitStat = true;
                }
            } else {
                hidrolikKilitComboBox.setDisable(true);
                disableKilitAndSogutma();
            }
        }
    }

    @FXML
    public void tankKapasitesiBackSpacePressed(KeyEvent event) {
        if(event.getCode() == KeyCode.BACK_SPACE || event.getCode() == KeyCode.DELETE) {
            tankKapasitesiTextField.clear();
            hidrolikKilitComboBox.setDisable(false);
            hidrolikKilitStat = true;

        }
    }

    @FXML
    public void hidrolikKilitPressed() {
        if(hidrolikKilitComboBox.getValue() != null) {
            secilenHidrolikKilitDurumu = hidrolikKilitComboBox.getValue();
            hidrolikKilitStat = true;
            if(Objects.equals(secilenHidrolikKilitDurumu, "Yok")) {
                dataInit("valfTipi", 0);
            } else {
                dataInit("valfTipi", 1);
            }
        }
    }

    @FXML
    public void valfTipiPressed() {
        if(valfTipiComboBox.getValue() != null) {
            secilenValfTipi = valfTipiComboBox.getValue();
            secilenPompaVal = Utils.string2Double(secilenPompa);
            System.out.println("Seçilen Pompa Değeri: " + secilenPompaVal);

            if(Objects.equals(secilenHidrolikKilitDurumu, "Var") && secilenPompaVal > 28.1) {
                dataInit("kilitMotor", null);
            } else {
                sogutmaComboBox.setDisable(false);
                dataInit("sogutma", null);
                sogutmaStat = true;
            }
        }
    }

    @FXML
    public void kilitMotorPressed() {
        if(kilitMotorComboBox.getValue() != null) {
            kilitPompaComboBox.setDisable(false);
            kilitPompaComboBox.getItems().addAll(ExcelUtil.dataManipulator.kilitPompaDegerleri);
            //kilitPompaComboBox.getItems().addAll("4.2 cc", "4.8 cc", "5.8 cc");
            secilenKilitMotor = kilitMotorComboBox.getValue();
        }
    }

    @FXML
    public void kilitPompaPressed() {
        if(kilitPompaComboBox.getValue() != null) {
            sogutmaComboBox.setDisable(false);
            dataInit("sogutma", null);
            secilenKilitPompa = kilitPompaComboBox.getValue();
        }
    }

    @FXML
    public void sogutmaPressed() {
        if(sogutmaComboBox.getValue() != null) {
            secilenSogutmaDurumu = sogutmaComboBox.getValue();
        }
    }

    @FXML
    public void parcaListesiGoster() {
        Image icon = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/icons/logo.png")));
        if(hesaplamaBitti) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("fxml/ParcaListesi.fxml"));
                VBox root = fxmlLoader.load();
                ParcaController parcaController = fxmlLoader.getController();
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
    public void temizlemeIslemi() {
        verileriSifirla();
        hesaplamaBitti = false;
    }

    @FXML
    public void exportProcess() {
        int startX = 500;
        int startY = 10;
        int width = 800;
        int height = 565;

        if(hesaplamaBitti) {
            pdfShaper(0);
            PDFFileUtil.coords2Png(startX, startY, width, height, exportButton);
            pdfShaper(1);
            PDFFileUtil.cropImage(680, startY, 370, height);

            String pdfPath = "";
            if(Objects.equals(secilenValfTipi, "İnişte Tek Hız")) {
                pdfPath = "/assets/data/pdf/klasikinistetek.pdf";
            } else if(Objects.equals(secilenValfTipi, "İnişte Çift Hız")) {
                pdfPath = "/assets/data/pdf/klasikinistecift.pdf";
            } else if(Objects.equals(secilenValfTipi, "Kilitli Blok || Çift Hız")) {
                pdfPath = "/assets/data/pdf/klasikkilitliblokcift.pdf";
            }
            PDFFileUtil.pdfGenerator("/assets/icons/onderGrupMain.png", "cropped_screenshot.png", pdfPath, girilenSiparisNumarasi);
        } else {
            Utils.showErrorMessage("Lütfen hesaplama işlemini tamamlayıp tekrar deneyin.");
        }
    }

    private boolean checkComboBox() {
        if(siparisNumarasi.getText().isEmpty() || motorComboBox.getSelectionModel().isEmpty() || pompaComboBox.getSelectionModel().isEmpty() || valfTipiComboBox.getSelectionModel().isEmpty() || hidrolikKilitComboBox.getSelectionModel().isEmpty() || sogutmaComboBox.getSelectionModel().isEmpty()) {
            return true;
        }
        int girilenTankKapasitesi = 0;
        girilenTankKapasitesi = Integer.parseInt(tankKapasitesiTextField.getText());

        if(tankKapasitesiTextField.getText() == null || girilenTankKapasitesi == 0) {
            return true;
        } else return girilenTankKapasitesi < 1 || girilenTankKapasitesi > 500;
    }

    private void initKabinOlculeri(int x, int y, int h, int litre, String key) {
        int[] kabinOlcu = new int[4];
        kabinOlcu[0] = x;
        kabinOlcu[1] = y;
        kabinOlcu[2] = h;
        kabinOlcu[3] = litre;
        ExcelUtil.dataManipulator.kabinOlculeri.put(key, kabinOlcu);
    }

    private void defineKabinOlcu() {
        initKabinOlculeri(550, 350, 300, 40, "HT 40");
        initKabinOlculeri(600, 370, 300, 70, "HT 70");
        initKabinOlculeri(600, 470, 400, 100, "HT 100");
        initKabinOlculeri(650, 500, 400, 125, "HT 125");
        initKabinOlculeri(700, 600, 400, 160, "HT 160");
        initKabinOlculeri(800, 650, 400, 200, "HT 200");
        initKabinOlculeri(900, 700, 400, 250, "HT 250");
        initKabinOlculeri(1000, 800, 400, 300, "HT 300");
        initKabinOlculeri(1000, 800, 450, 350, "HT 350");
        initKabinOlculeri(1000, 800, 500, 400, "HT 400");
    }

    private void dataInit(String componentName, @Nullable Integer valfTipiStat) {
        if(componentName.equals("motor")) {
            motorComboBox.setDisable(false);
            motorComboBox.getItems().clear();
            motorComboBox.getItems().addAll(ExcelUtil.dataManipulator.motorDegerleri);
            //motorComboBox.getItems().addAll("4 kW", "5.5 kW", "5.5 kW (Kompakt)", "7.5 kW (Kompakt)", "11 kW", "11 kW (Kompakt)", "15 kW", "18.5 kW", "22 kW", "37 kW");
        } else if(componentName.equals("pompa")) {
            pompaComboBox.getItems().clear();
            if(Objects.equals(secilenUniteTipi, "Hidros")) {
                pompaComboBox.getItems().addAll(ExcelUtil.dataManipulator.pompaDegerleriHidros);
                //pompaComboBox.getItems().addAll("1.1 cc", "1.6 cc", "2.1 cc", "2.7 cc", "3.2 cc", "3.7 cc", "4.2 cc", "4.8 cc", "5.8 cc", "7 cc", "8 cc", "9 cc");
            } else if(Objects.equals(secilenUniteTipi, "Klasik")) {
                pompaComboBox.getItems().addAll(ExcelUtil.dataManipulator.pompaDegerleriKlasik);
                //pompaComboBox.getItems().addAll("9.5 cc", "11.9 cc", "14 cc", "14.6 cc", "16.8 cc", "19.2 cc", "22.9 cc", "28.1 cc", "28.8 cc", "33.3 cc", "37.9 cc", "42.6 cc", "45.5 cc", "49.4 cc", "56.1 cc");
            } else {
                pompaComboBox.getItems().addAll(ExcelUtil.dataManipulator.pompaDegerleriTumu);
                //pompaComboBox.getItems().addAll("1.1 cc", "1.6 cc", "2.1 cc", "2.7 cc", "3.2 cc", "3.7 cc", "4.2 cc", "4.8 cc", "5.8 cc", "7 cc", "8 cc", "9 cc", "9.5 cc", "11.9 cc", "14 cc", "14.6 cc", "16.8 cc", "19.2 cc", "22.9 cc", "28.1 cc", "28.8 cc", "33.3 cc", "37.9 cc", "42.6 cc", "45.5 cc", "49.4 cc", "56.1 cc");
            }
        } else if(componentName.equals("valfTipi")) {
            valfTipiComboBox.getItems().clear();
            valfTipiComboBox.setDisable(false);
            if(valfTipiStat == 1) {
                valfTipiComboBox.getItems().addAll(ExcelUtil.dataManipulator.valfTipiDegerleri1);
                //valfTipiComboBox.getItems().addAll("Kilitli Blok || Çift Hız", "Kilitli Blok || Kompanzasyon");
            } else {
                valfTipiComboBox.getItems().addAll(ExcelUtil.dataManipulator.valfTipiDegerleri2);
                //valfTipiComboBox.getItems().addAll("İnişte Tek Hız", "İnişte Çift Hız", "Kompanzasyon + İnişte Tek Hız");
            }
        } else if(componentName.equals("hidrolikKilit")) {
            hidrolikKilitComboBox.getItems().clear();
            hidrolikKilitComboBox.getItems().addAll("Var", "Yok");
        } else if(componentName.equals("sogutma")) {
            sogutmaComboBox.getItems().clear();
            sogutmaComboBox.getItems().addAll("Var", "Yok");
        } else if(componentName.equals("kilitMotor")) {
            kilitMotorComboBox.setDisable(false);
            kilitMotorComboBox.getItems().clear();
            kilitMotorText.setVisible(true);
            kilitMotorComboBox.setVisible(true);
            kilitMotorComboBox.getItems().addAll(ExcelUtil.dataManipulator.kilitMotorDegerleri);
            //kilitMotorComboBox.getItems().addAll("1.5 kW", "2.2 kW");
        } else if(componentName.equals("kilitPompa")) {
            kilitPompaComboBox.setDisable(false);
            kilitPompaComboBox.getItems().clear();
            kilitPompaText.setVisible(true);
            kilitPompaComboBox.setVisible(true);
            kilitPompaComboBox.getItems().addAll(ExcelUtil.dataManipulator.kilitPompaDegerleri);
            //kilitPompaComboBox.getItems().addAll("4.2 cc", "4.8 cc", "5.8 cc");
        }
    }

    private void disableMotorPompa(int stat) {
        if(stat == 1) {
            secilenKilitMotor = null;
            secilenKilitPompa = null;
            kilitMotorComboBox.setDisable(true);
            kilitPompaComboBox.setDisable(true);
        } else {
            secilenKilitMotor = null;
            kilitMotorComboBox.setDisable(false);
        }
    }

    private void enableSonucSection() {
        genislikSonucText.setVisible(true);
        yukseklikSonucText.setVisible(true);
        derinlikSonucText.setVisible(true);
        hacimText.setVisible(true);
        kullanilacakKabin.setVisible(true);
    }

    private void disableKilitAndSogutma() {
        if(hidrolikKilitStat) {
            hidrolikKilitComboBox.setDisable(true);
            hidrolikKilitStat = false;
        }
        if(sogutmaStat) {
            sogutmaComboBox.setDisable(true);
            sogutmaStat = false;
        }
    }

    private void comboBoxListener() {
        siparisNumarasi.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!siparisNumarasi.getText().isEmpty()) {
                girilenSiparisNumarasi = newValue;
            }
            sonucAnaLabelTxt.setText("Sipariş Numarası: " + girilenSiparisNumarasi);
            dataInit("motor", null);
            if(girilenSiparisNumarasi != null) {
                tabloGuncelle();
            }
        });

        motorComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if(!motorComboBox.getItems().isEmpty() && newValue != null) {
                secilenMotor = newValue;
                secilenKampana = ExcelUtil.dataManipulator.kampanaDegerleri.get(motorComboBox.getSelectionModel().getSelectedIndex());
                dataInit("pompa", null);
                if(secilenMotor != null) {
                    tabloGuncelle();
                }
            }
        });

        pompaComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            secilenPompa = newValue;
            if(oldValue != null && secilenPompa != null) {
                double oldSecilenPompaVal = Utils.string2Double(oldValue);
                secilenPompaVal = Utils.string2Double(secilenPompa);
                if(oldSecilenPompaVal >= 28.1 && secilenPompaVal < 28.1) {
                    disableMotorPompa(1);
                } else if(oldSecilenPompaVal < 28.1 && secilenPompaVal >= 28.1) {
                    disableMotorPompa(2);
                }
            }
            if(secilenPompa != null) {
                tabloGuncelle();
                imageTextDisable(0);
            }
        });

        tankKapasitesiTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!tankKapasitesiTextField.getText().isEmpty()) {
                girilenTankKapasitesiMiktari = Integer.parseInt(newValue);
            }
            dataInit("hidrolikKilit", null);
            if(girilenTankKapasitesiMiktari != 0) {
                tabloGuncelle();
            }
        });

        hidrolikKilitComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            secilenHidrolikKilitDurumu = newValue;
            if(secilenPompa != null) {
                if(Objects.equals(secilenHidrolikKilitDurumu, "Var")) {
                    System.out.println("Secilen Pompa: " + secilenPompaVal);
                    if(secilenPompaVal > 28.1) {
                        dataInit("valfTipi", 0);
                    } else {
                        dataInit("valfTipi", 1);
                    }
                } else {
                    dataInit("valfTipi", 0);
                    kilitPompaComboBox.setVisible(true);
                    kilitMotorComboBox.setVisible(true);
                    kilitMotorComboBox.setDisable(true);
                    kilitPompaComboBox.setDisable(true);
                }
                tabloGuncelle();
            }
        });

        valfTipiComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            secilenValfTipi = newValue;
            if(secilenPompa != null) {
                if(Objects.equals(secilenHidrolikKilitDurumu, "Var")) {
                    if(secilenPompaVal >= 28.1) {
                        dataInit("kilitMotor", null);
                    } else {
                        sogutmaComboBox.setDisable(false);
                        dataInit("sogutma", null);
                    }
                } else {
                    dataInit("sogutma", null);
                }
            }
            if(secilenValfTipi != null) {
                tabloGuncelle();
            }
        });

        kilitMotorComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            secilenKilitMotor = kilitMotorComboBox.getValue();
            dataInit("kilitPompa", null);
            if(secilenKilitMotor != null) {
                tabloGuncelle();
            }
        });

        kilitPompaComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            secilenKilitPompa = kilitPompaComboBox.getValue();
            dataInit("sogutma", null);
            if(secilenKilitPompa != null) {
                tabloGuncelle();
            }
        });

        sogutmaComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            secilenSogutmaDurumu = sogutmaComboBox.getValue();
            if(secilenSogutmaDurumu != null) {
                tabloGuncelle();
            }
        });
    }

    private void verileriSifirla() {
        siparisNumarasi.clear();
        secilenMotor = null;
        secilenPompa = null;
        girilenTankKapasitesiMiktari = 0;
        secilenHidrolikKilitDurumu = null;
        secilenValfTipi = null;
        secilenSogutmaDurumu = null;
        if(secilenKilitMotor != null) {
            secilenKilitMotor = null;
        }
        if(secilenKilitPompa != null) {
            secilenKilitPompa = null;
        }

        tankKapasitesiTextField.clear();
        motorComboBox.getSelectionModel().clearSelection();
        motorComboBox.setPromptText("Motor");
        pompaComboBox.getSelectionModel().clearSelection();
        pompaComboBox.setPromptText("Pompa");
        hidrolikKilitComboBox.getSelectionModel().clearSelection();
        hidrolikKilitComboBox.setPromptText("Hidrolik Kilit");
        valfTipiComboBox.getSelectionModel().clearSelection();
        valfTipiComboBox.setPromptText("Valf Tipi");
        kilitMotorComboBox.getSelectionModel().clearSelection();
        kilitMotorComboBox.setPromptText("Kilit Motor");
        kilitPompaComboBox.getSelectionModel().clearSelection();
        kilitPompaComboBox.setPromptText("Kilit Pompa");
        sogutmaComboBox.getSelectionModel().clearSelection();
        sogutmaComboBox.setPromptText("Soğutma");

        motorComboBox.setDisable(true);
        pompaComboBox.setDisable(true);
        tankKapasitesiTextField.setDisable(true);
        hidrolikKilitComboBox.setDisable(true);
        valfTipiComboBox.setDisable(true);
        kilitMotorComboBox.setDisable(true);
        kilitPompaComboBox.setDisable(true);
        sogutmaComboBox.setDisable(true);

        sonucEkraniTemizle();
        sonucTablo.getItems().clear();

        sonucKapakImage.setImage(null);
        parcaListesiButton.setDisable(true);
        exportButton.setDisable(true);
        kullanilacakKabin.setVisible(false);
        sonucAnaLabelTxt.setText("Sipariş Numarası: ");
        sonucTankGorsel.setImage(null);

        imageTextDisable(0);
    }

    private void imageTextDisable(int stat) {
        sonucSagText.setVisible(false);
        sonucUstText.setVisible(false);
        if(stat == 1) {
            if(secilenKilitMotor != null) {
                kampana2OlcuText.setVisible(false);
                kampana2OlcuText2.setVisible(false);
                kilitMotorOlcuText.setVisible(false);
                kilitMotorOlcuText2.setVisible(false);
                kilitMotorIcOlcuText.setVisible(false);
                kilitliBlok2OlcuText.setVisible(false);
                kilitliBlok2OlcuText2.setVisible(false);
                dolum2OlcuText.setVisible(false);
                dolum2OlcuText2.setVisible(false);
                tahliye2OlcuText.setVisible(false);
                kampanaVeri2Text.setVisible(false);
                kilitMotorVeriText.setVisible(false);
                kilitliBlokVeri2Text.setVisible(false);
                kucukHalkaCap2Text.setVisible(false);
                buyukHalkaCap2Text.setVisible(false);
            } else {
                kampanaOlcuText.setVisible(false);
                kampanaOlcuText2.setVisible(false);
                kilitliBlokOlcuText.setVisible(false);
                kilitliBlokOlcuText2.setVisible(false);
                dolumOlcuText.setVisible(false);
                dolumOlcuText2.setVisible(false);
                tahliyeOlcuText.setVisible(false);
                kampanaVeriText.setVisible(false);
                kilitliBlokVeriText.setVisible(false);
                kucukHalkaCapText.setVisible(false);
                buyukHalkaCapText.setVisible(false);
            }
        } else {
            sonucKapakImage.setImage(null);
            kampana2OlcuText.setVisible(false);
            kampana2OlcuText2.setVisible(false);
            kilitMotorIcOlcuText.setVisible(false);
            kilitMotorOlcuText.setVisible(false);
            kilitMotorOlcuText2.setVisible(false);
            kilitliBlok2OlcuText.setVisible(false);
            kilitliBlok2OlcuText2.setVisible(false);
            dolum2OlcuText.setVisible(false);
            dolum2OlcuText2.setVisible(false);
            tahliye2OlcuText.setVisible(false);
            kampanaVeri2Text.setVisible(false);
            kampanaOlcuText.setVisible(false);
            kampanaOlcuText2.setVisible(false);
            kilitliBlokOlcuText.setVisible(false);
            kilitliBlokOlcuText2.setVisible(false);
            dolumOlcuText.setVisible(false);
            dolumOlcuText2.setVisible(false);
            tahliyeOlcuText.setVisible(false);
            kampanaVeriText.setVisible(false);
            kilitMotorVeriText.setVisible(false);
            kilitliBlokVeriText.setVisible(false);
            kilitliBlokVeri2Text.setVisible(false);
            kucukHalkaCap2Text.setVisible(false);
            buyukHalkaCap2Text.setVisible(false);
            kucukHalkaCapText.setVisible(false);
            buyukHalkaCapText.setVisible(false);
        }
    }

    private void imageTextEnable(int x, int y) {
        sonucUstText.setVisible(true);
        sonucSagText.setVisible(true);
        sonucUstText.setText("X: " + x + " mm");
        sonucSagText.setText("Y: " + y + " mm");
        if(secilenKilitMotor != null) {
            imageTextDisable(1);
            kampana2OlcuText.setVisible(true);
            kampana2OlcuText2.setVisible(true);
            kilitMotorOlcuText.setVisible(true);
            kilitMotorOlcuText2.setVisible(true);
            kilitliBlok2OlcuText.setVisible(true);
            kilitliBlok2OlcuText2.setVisible(true);
            dolum2OlcuText.setVisible(true);
            dolum2OlcuText2.setVisible(true);
            tahliye2OlcuText.setVisible(true);
            kampanaVeri2Text.setVisible(true);
            kucukHalkaCap2Text.setVisible(true);
            buyukHalkaCap2Text.setVisible(true);

           showKampanaText(2);

            kilitMotorIcOlcuText.setVisible(true);
            kilitMotorIcOlcuText.setText("Boğaz: Ø200\nKesim: Ø115");
            kilitMotorVeriText.setVisible(true);
            kilitMotorVeriText.setText("Kilit Motor: " + secilenKilitMotor + "\nKilit Pompa: " + secilenKilitPompa);
            kilitliBlokVeri2Text.setVisible(true);
            kilitliBlokVeri2Text.setText(secilenValfTipi);
        } else {
            imageTextDisable(1);
            kampanaOlcuText.setVisible(true);
            kampanaOlcuText2.setVisible(true);
            kilitliBlokOlcuText.setVisible(true);
            kilitliBlokOlcuText2.setVisible(true);
            dolumOlcuText.setVisible(true);
            dolumOlcuText2.setVisible(true);
            tahliyeOlcuText.setVisible(true);
            kampanaVeriText.setVisible(true);
            kucukHalkaCapText.setVisible(true);
            buyukHalkaCapText.setVisible(true);
            showKampanaText(1);
            kilitliBlokVeriText.setVisible(true);
            kilitliBlokVeriText.setText(secilenValfTipi);
        }
    }

    private void showKampanaText(int type) {
        String kampanaText = "";

        if(secilenPompaVal >= 33.3) {
            if(secilenKampana == 250) {
                kampanaText = "Kampana: 2K-" + secilenKampana + "\nKesim Çapı: Ø" + 173;
            } else if(secilenKampana == 300) {
                kampanaText = "Kampana: 2K-" + secilenKampana + "\nKesim Çapı: Ø" + 236;
            } else if(secilenKampana == 350) {
                kampanaText = "Kampana: 2K-" + secilenKampana + "\nKesim Çapı: Ø" + 263;
            } else if(secilenKampana == 400) {
                kampanaText = "Kampana: 2K-" + secilenKampana + "\nKesim Çapı: Ø" + " NaN";
            }
        } else {
            if(secilenKampana == 250) {
                kampanaText = "Kampana: " + secilenKampana + "\nKesim Çapı: Ø" + 173;
            } else if(secilenKampana == 300) {
                kampanaText = "Kampana: " + secilenKampana + "\nKesim Çapı: Ø" + 236;
            } else if(secilenKampana == 350) {
                kampanaText = "Kampana: " + secilenKampana + "\nKesim Çapı: Ø" + 263;
            } else if(secilenKampana == 400) {
                kampanaText = "Kampana: " + secilenKampana + "\nKesim Çapı: Ø" + " NaN";
            }
        }

        if(type == 1) {
            kampanaVeriText.setText(kampanaText);
        } else {
            kampanaVeri2Text.setText(kampanaText);
        }
    }

    private void sonucEkraniTemizle() {
        yukseklikSonucText.setVisible(false);
        genislikSonucText.setVisible(false);
        derinlikSonucText.setVisible(false);
        hacimText.setVisible(false);
    }

    private void tabloGuncelle() {
        sonucTablo.getItems().clear();
        TableData data = new TableData("Sipariş Numarası:", girilenSiparisNumarasi);
        sonucTablo.getItems().add(data);

        data = new TableData("Hidrolik Ünitesi Tipi:", secilenUniteTipi);
        sonucTablo.getItems().add(data);

        data = new TableData("Seçilen Motor:", secilenMotor);
        sonucTablo.getItems().add(data);

        data = new TableData("Kampana:", String.valueOf(secilenKampana));
        sonucTablo.getItems().add(data);

        data = new TableData("Seçilen Pompa:", secilenPompa);
        sonucTablo.getItems().add(data);

        data = new TableData("Tank Kapasitesi:", String.valueOf(girilenTankKapasitesiMiktari));
        sonucTablo.getItems().add(data);

        data = new TableData("Hidrolik Kilit Durumu:", secilenHidrolikKilitDurumu);
        sonucTablo.getItems().add(data);

        data = new TableData("Seçilen Valf Tipi:", secilenValfTipi);
        sonucTablo.getItems().add(data);

        data = new TableData("Kilit Motoru:", Objects.requireNonNullElse(secilenKilitMotor, "Yok"));
        sonucTablo.getItems().add(data);

        data = new TableData("Kilit Pompa:", Objects.requireNonNullElse(secilenKilitPompa, "Yok"));
        sonucTablo.getItems().add(data);

        data = new TableData("Soğutma Durumu:", secilenSogutmaDurumu);
        sonucTablo.getItems().add(data);
    }

    private void tankGorselLoad() {
        Image image;

        if(secilenSogutmaDurumu.contains("Var")) {
            image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/icons/tanklar/ingilteresogutuculu.png")));
            sonucTankGorsel.setImage(image);
            genislikSonucText.setLayoutY(216.0);
            genislikSonucText.setRotate(33.5);
            derinlikSonucText.setRotate(-30.0);
            derinlikSonucText.setLayoutX(638.0);

        } else {
            if(secilenHidrolikKilitDurumu.contains("Var")) {
                if(secilenPompaVal >= 33.3) {
                    image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/icons/tanklar/sogutuculuotuzuc.png")));
                    sonucTankGorsel.setImage(image);
                    genislikSonucText.setRotate(27.5);
                    derinlikSonucText.setRotate(-27.5);
                    derinlikSonucText.setLayoutX(635.0);
                } else {
                    if(Objects.equals(secilenValfTipi, "Kilitli Blok || Çift Hız")) {
                        image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/icons/tanklar/kilitliblok.png")));
                        sonucTankGorsel.setImage(image);
                        genislikSonucText.setRotate(27.5);
                        derinlikSonucText.setRotate(-27.5);
                        derinlikSonucText.setLayoutX(635.0);
                    }
                }
            } else {
                if(Objects.equals(secilenValfTipi, "Kilitli Blok || Çift Hız")) {
                    image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/icons/tanklar/cifthiz.png")));
                } else if(Objects.equals(secilenValfTipi, "İnişte Tek Hız")) {
                    image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/icons/tanklar/tekhiz.png")));
                    sonucTankGorsel.setImage(image);
                } else if(Objects.equals(secilenValfTipi, "İnişte Çift Hız")) {
                    image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/icons/tanklar/cifthiz.png")));
                    sonucTankGorsel.setImage(image);
                }
            }
        }
    }

    private void textColorChange(int type) {
        if(type == 1) {
            kampana2OlcuText.setFill(Color.WHITE);
            kampana2OlcuText2.setFill(Color.WHITE);
            kilitMotorOlcuText.setFill(Color.WHITE);
            kilitMotorOlcuText2.setFill(Color.WHITE);
            kilitliBlok2OlcuText.setFill(Color.WHITE);
            kilitliBlok2OlcuText2.setFill(Color.WHITE);
            dolum2OlcuText.setFill(Color.WHITE);
            dolum2OlcuText2.setFill(Color.WHITE);
            tahliye2OlcuText.setFill(Color.WHITE);
            kampanaVeri2Text.setFill(Color.WHITE);
            kilitMotorIcOlcuText.setFill(Color.WHITE);
            kilitMotorVeriText.setFill(Color.WHITE);
            kilitliBlokVeri2Text.setFill(Color.WHITE);

            kampanaOlcuText.setFill(Color.WHITE);
            kampanaOlcuText2.setFill(Color.WHITE);
            kilitliBlokOlcuText.setFill(Color.WHITE);
            kilitliBlokOlcuText2.setFill(Color.WHITE);
            dolumOlcuText.setFill(Color.WHITE);
            dolumOlcuText2.setFill(Color.WHITE);
            tahliyeOlcuText.setFill(Color.WHITE);
            kampanaVeriText.setFill(Color.WHITE);
            kilitliBlokVeriText.setFill(Color.WHITE);

            sonucUstText.setFill(Color.WHITE);
            sonucSagText.setFill(Color.WHITE);
        }  else {
            kampana2OlcuText.setFill(Color.BLACK);
            kampana2OlcuText2.setFill(Color.BLACK);
            kilitMotorOlcuText.setFill(Color.BLACK);
            kilitMotorOlcuText2.setFill(Color.BLACK);
            kilitliBlok2OlcuText.setFill(Color.BLACK);
            kilitliBlok2OlcuText2.setFill(Color.BLACK);
            dolum2OlcuText.setFill(Color.BLACK);
            dolum2OlcuText2.setFill(Color.BLACK);
            tahliye2OlcuText.setFill(Color.BLACK);
            kampanaVeri2Text.setFill(Color.BLACK);
            kilitMotorIcOlcuText.setFill(Color.BLACK);
            kilitMotorVeriText.setFill(Color.BLACK);
            kilitliBlokVeri2Text.setFill(Color.BLACK);

            kampanaOlcuText.setFill(Color.BLACK);
            kampanaOlcuText2.setFill(Color.BLACK);
            kilitliBlokOlcuText.setFill(Color.BLACK);
            kilitliBlokOlcuText2.setFill(Color.BLACK);
            dolumOlcuText.setFill(Color.BLACK);
            dolumOlcuText2.setFill(Color.BLACK);
            tahliyeOlcuText.setFill(Color.BLACK);
            kampanaVeriText.setFill(Color.BLACK);
            kilitliBlokVeriText.setFill(Color.BLACK);

            sonucUstText.setFill(Color.BLACK);
            sonucSagText.setFill(Color.BLACK);
        }
    }

    private void pdfShaper(int type) {
        if(type == 0) {
            //pdf oluşturma öncesi
            klasikVBox.setStyle("-fx-background-color: #FFFFFF;"); //sarı: #F9F871
            sonucAnaLabelTxt.setFill(Color.BLACK);
            genislikSonucText.setTextFill(Color.BLACK);
            derinlikSonucText.setTextFill(Color.BLACK);
            yukseklikSonucText.setTextFill(Color.BLACK);
            hacimText.setTextFill(Color.BLACK);
            kullanilacakKabin.setFill(Color.BLACK);
            textColorChange(0);
        } else {
            //pdf oluşturma sonrası
            klasikVBox.setStyle("-fx-background-color: #353a46;");
            genislikSonucText.setTextFill(Color.WHITE);
            derinlikSonucText.setTextFill(Color.WHITE);
            yukseklikSonucText.setTextFill(Color.WHITE);
            hacimText.setTextFill(Color.WHITE);
            sonucAnaLabelTxt.setFill(Color.web("#B7C3D7"));
            kullanilacakKabin.setFill(Color.web("#B7C3D7"));
            textColorChange(1);
        }
    }
}