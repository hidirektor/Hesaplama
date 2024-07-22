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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import me.t3sl4.hydraulic.Launcher;
import me.t3sl4.hydraulic.Screens.Main;
import me.t3sl4.hydraulic.Utility.Data.Table.TableData;
import me.t3sl4.hydraulic.Utility.Data.Tank.Tank;
import me.t3sl4.hydraulic.Utility.File.ExcelUtil;
import me.t3sl4.hydraulic.Utility.File.PDFFileUtil;
import me.t3sl4.hydraulic.Utility.File.SystemUtil;
import me.t3sl4.hydraulic.Utility.ReqUtil;
import me.t3sl4.hydraulic.Utility.Utils;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class KlasikController {

    @FXML
    public AnchorPane hydraulicUnitBox;

    @FXML
    private ComboBox<String> motorComboBox;

    @FXML
    private ComboBox<String> pompaComboBox;

    @FXML
    private ComboBox<String> valfTipiComboBox;

    @FXML
    private ComboBox<String> hidrolikKilitComboBox;

    @FXML
    private ComboBox<String> kompanzasyonComboBox;

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
    private Button exportButton;

    @FXML
    private Button parcaListesiButton;

    @FXML
    private ImageView sonucTankGorsel;

    @FXML
    private VBox klasikVBox;

    /*
    Seçilen Değerler:
     */
    public static String girilenSiparisNumarasi = null;
    public String secilenUniteTipi = "Klasik";
    public static String secilenMotor = null;
    public static String kompanzasyonDurumu = null;
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

    private ArrayList<Text> sonucTexts = new ArrayList<>();

    double screenX, screenY;

    public static String atananKabinFinal = "";
    public static String gecisOlculeriFinal = "";

    public void initialize() {
        Utils.textFilter(tankKapasitesiTextField);
        comboBoxListener();
        sonucTabloSatir1.setCellValueFactory(new PropertyValueFactory<>("satir1Property"));
        sonucTabloSatir2.setCellValueFactory(new PropertyValueFactory<>("satir2Property"));
    }

    @FXML
    public void hesaplaFunc() {
        ArrayList<Integer> results;
        int calculatedX, calculatedY, calculatedH, calculatedHacim;

        if (checkComboBox()) {
            Utils.showErrorMessage("Lütfen tüm girdileri kontrol edin.");
        } else {
            sonucEkraniTemizle();
            imageTextDisable();

            enableSonucSection();
            results = calcDimensions(ExcelUtil.dataManipulator.kampanaDegerleri);

            if (results.size() == 4) {
                calculatedX = results.get(0);
                calculatedY = results.get(1);
                calculatedH = results.get(2);
                calculatedHacim = results.get(3);

                genislikSonucText.setText("X: " + calculatedX + " mm");
                derinlikSonucText.setText("Y: " + calculatedY + " mm");
                yukseklikSonucText.setText("h: " + calculatedH + " mm");
                hacimText.setText("Tank : " + calculatedHacim + "L");

                tabloGuncelle();
                Image image = null;
                if (Objects.equals(secilenSogutmaDurumu, "Var")) {
                    if(Objects.equals(secilenHidrolikKilitDurumu, "Var")) {
                        image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/icons/sogutmaKilit.png")));
                        imageTextEnable(calculatedX, calculatedY, "sogutmaKilit");
                    } else {
                        image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/icons/sogutmaKilitsiz.png")));
                        imageTextEnable(calculatedX, calculatedY, "sogutmaKilitsiz");
                    }
                } else {
                    if (Objects.equals(secilenHidrolikKilitDurumu, "Var")) {
                        if(secilenPompaVal <= 28.1) {
                            image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/icons/kilitMotor.png")));
                            imageTextEnable(calculatedX, calculatedY, "kilitMotor");
                        } else {
                            image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/icons/normal.png")));
                            imageTextEnable(calculatedX, calculatedY, "standartUnite");
                        }
                    } else {
                        if(secilenValfTipi.equals("Kompanzasyon + İnişte Tek Hız")) {
                            image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/icons/kompanzasyon.png")));
                            imageTextEnable(calculatedX, calculatedY, "kompanzasyon");
                        } else {
                            image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/icons/tekhizcifthiz.png")));
                            imageTextEnable(calculatedX, calculatedY, "tekhizcifthiz");
                        }
                    }
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
        String pdfPath = System.getProperty("user.home") + "/Desktop/" + girilenSiparisNumarasi + ".pdf";
        String excelPath = System.getProperty("user.home") + "/Desktop/" + girilenSiparisNumarasi + ".xlsx";

        if (SystemUtil.fileExists(pdfPath) && SystemUtil.fileExists(excelPath)) {
            ReqUtil.createHydraulicUnit(Main.loggedInUser.getUsername(), girilenSiparisNumarasi, secilenUniteTipi, excelPath, pdfPath);
        } else {
            Utils.showErrorMessage("Lütfen PDF ve parça listesi oluşturduktan sonra kaydedin");
        }
    }

    @FXML
    public void motorPressed() {
        if(motorComboBox.getValue() != null) {
            secilenMotor = motorComboBox.getValue();
            secilenKampana = ExcelUtil.dataManipulator.kampanaDegerleri.get(motorComboBox.getSelectionModel().getSelectedIndex());

            dataInit("sogutma", null);
            sogutmaComboBox.setDisable(false);
        }
    }

    @FXML
    public void sogutmaPressed() {
        if(sogutmaComboBox.getValue() != null) {
            secilenSogutmaDurumu = sogutmaComboBox.getValue();
            dataInit("hidrolikKilit", null);
            hidrolikKilitComboBox.setDisable(false);
        }
    }

    @FXML
    public void hidrolikKilitPressed() {
        if(hidrolikKilitComboBox.getValue() != null) {
            secilenHidrolikKilitDurumu = hidrolikKilitComboBox.getValue();
            hidrolikKilitStat = true;
            dataInit("pompa", null);
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
                dataInit("kompanzasyon", null);
                kompanzasyonComboBox.setDisable(false);
//                if(girilenTankKapasitesiMiktari < 1 || girilenTankKapasitesiMiktari > 500) {
//                    kompanzasyonComboBox.setDisable(true);
//                    disableKilitAndSogutma();
//                } else {
//                    kompanzasyonComboBox.setDisable(false);
//                    hidrolikKilitStat = true;
//                }
            } else {
                kompanzasyonComboBox.setDisable(true);
            }
        }
    }

    @FXML
    public void tankKapasitesiBackSpacePressed(KeyEvent event) {
        if(event.getCode() == KeyCode.BACK_SPACE || event.getCode() == KeyCode.DELETE) {
            tankKapasitesiTextField.clear();
            kompanzasyonComboBox.setDisable(true);
        }
    }

    @FXML
    public void kompanzasyonPressed() {
        if(kompanzasyonComboBox.getValue() != null) {
            kompanzasyonDurumu = kompanzasyonComboBox.getValue();
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
                    screenX = event.getSceneX();
                    screenY = event.getSceneY();
                });
                root.setOnMouseDragged(event -> {

                    popupStage.setX(event.getScreenX() - screenX);
                    popupStage.setY(event.getScreenY() - screenY);

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
        if(siparisNumarasi.getText().isEmpty() || motorComboBox.getSelectionModel().isEmpty() || kompanzasyonComboBox.getSelectionModel().isEmpty() || pompaComboBox.getSelectionModel().isEmpty() || valfTipiComboBox.getSelectionModel().isEmpty() || hidrolikKilitComboBox.getSelectionModel().isEmpty() || sogutmaComboBox.getSelectionModel().isEmpty()) {
            return true;
        }
        int girilenTankKapasitesi = 0;
        girilenTankKapasitesi = Integer.parseInt(tankKapasitesiTextField.getText());

        if(tankKapasitesiTextField.getText() == null || girilenTankKapasitesi == 0) {
            return true;
        } else return girilenTankKapasitesi < 1 || girilenTankKapasitesi > 500;
    }

    ArrayList<Integer> calcDimensions(ArrayList<Integer> kampanaDegerleri) {
        //Eklenecek Değerler
        int secilenMotorIndex = motorComboBox.getSelectionModel().getSelectedIndex();
        int kampanaDegeri = kampanaDegerleri.get(secilenMotorIndex);

        secilenKampana = kampanaDegerleri.get(secilenMotorIndex);
        secilenPompaVal = Utils.string2Double(secilenPompa);

        //Standart Boşluk Değerleri:
        int kampanaBoslukX = ExcelUtil.dataManipulator.kampanaBoslukX;
        int kampanaBoslukY = ExcelUtil.dataManipulator.kampanaBoslukY;
        int kilitAraBoslukX = ExcelUtil.dataManipulator.kilitliBlokAraBoslukX;
        int valfBoslukX = ExcelUtil.dataManipulator.valfBoslukX;
        int valfBoslukYArka = ExcelUtil.dataManipulator.valfBoslukYArka;
        int valfBoslukYOn = ExcelUtil.dataManipulator.valfBoslukYOn;
        int defaultHeight = 350; //standart yükseklik ölçüsü

        //Hesaplama Standartları
        ArrayList<Integer> finalValues = new ArrayList<>();
        int yV = 0;
        int yK = 0;
        int eskiX=0, eskiY=0, eskiH=0;
        int x=0, y=0, h=0;

        int hesaplananHacim=0, atananHacim=0;

        System.out.println("--------Hesaplama Başladı--------");
        if(Objects.equals(secilenSogutmaDurumu, "Var")) {
            /* TODO
            Standart üniteyi göster. Ölçüler:
            X: 1000
            Y: 600
            H: 350
            Soğutmanın standardı için bir kabin eklenecek :)
             */

            calculationSogutma(x, y, h, finalValues, hesaplananHacim, atananKabinFinal, gecisOlculeriFinal, atananHacim);

            return finalValues;
        } else {
            x +=  kampanaDegeri + kampanaBoslukX;
            yK += kampanaDegeri + kampanaBoslukY + kampanaBoslukY;
            System.out.println("Motor + Kampana için:");
            System.out.println("X += " + kampanaDegeri + " (Kampana) " + kampanaBoslukX + " (Kampana Boşluk)");

            //hidrolik kilit seçiliyse: valf tipi = kilitli blok olarak gelicek
            //kilitli blok ölçüsü olarak: X'e +100 olacak
            if(Objects.equals(secilenHidrolikKilitDurumu, "Var")) {
                if(Objects.equals(secilenValfTipi, "Kilitli Blok || Çift Hız")) {
                    x += 120 + kilitAraBoslukX + valfBoslukX;
                    yV += 190 + valfBoslukYArka + valfBoslukYOn;
                    System.out.println("Kilitli Blok için:");
                    System.out.println("X += " + kilitAraBoslukX + " (Ara Boşluk) + " + valfBoslukX + " (Valf Boşluk)");
                    System.out.println("yV += " + valfBoslukYArka + " (Valf Boşluk Arka) + " + valfBoslukYOn + " (Valf Boşluk Ön)");
                } else {
                    if(secilenPompaVal >= 28.1) {
                        float secilenKilitMotorVal = 0;
                        if(secilenKilitMotor != null) {
                            String[] secKilitMotor = secilenKilitMotor.split(" kW");
                            secilenKilitMotorVal = Float.parseFloat(secKilitMotor[0]);
                        }

                        if(Objects.equals(secilenValfTipi, "Kompanzasyon + İnişte Tek Hız")) {
                            yV += 180 + ExcelUtil.dataManipulator.valfBoslukYArka + ExcelUtil.dataManipulator.valfBoslukYOn;
                            System.out.println("Kompanzasyon + İnişte Tek Hız (Kilitli Blok) (Pompa > 28.1) için:");
                            System.out.println("yV += " + ExcelUtil.dataManipulator.valfBoslukYOn + " (Valf Boşluk Ön) + " + ExcelUtil.dataManipulator.valfBoslukYArka + " (Valf Boşluk Arka)");
                        } else if(Objects.equals(secilenValfTipi, "İnişte Çift Hız")) {
                            System.out.println("İnişte Çift Hız (Kilitli Blok) için:");
                            if(secilenPompaVal >= 28.1) {
                                yV += 90 + ExcelUtil.dataManipulator.valfBoslukYOn;
                                System.out.println("(Pompa > 28.1) için:");
                                System.out.println("yV += " + ExcelUtil.dataManipulator.valfBoslukYOn + " (Valf Boşluk Ön)");
                            } else {
                                yV += 90 + ExcelUtil.dataManipulator.valfBoslukYOn + ExcelUtil.dataManipulator.valfBoslukYArka;
                                System.out.println("(Pompa <= 28.1) için:");
                                System.out.println("yV += " + ExcelUtil.dataManipulator.valfBoslukYOn + " (Valf Boşluk Ön) + " + ExcelUtil.dataManipulator.valfBoslukYArka + " (Valf Boşluk Arka)");
                            }
                        } else if(Objects.equals(secilenValfTipi, "İnişte Tek Hız")) {
                            yV += 180 + ExcelUtil.dataManipulator.valfBoslukYOn + ExcelUtil.dataManipulator.valfBoslukYArka;
                            System.out.println("İnişte Tek Hız (Kilitli Blok) için:");
                            System.out.println("yV += " + ExcelUtil.dataManipulator.valfBoslukYOn + " (Valf Boşluk Ön) + " + ExcelUtil.dataManipulator.valfBoslukYArka + " (Valf Boşluk Arka)");
                        }

                        if(secilenKilitMotorVal != 0) {
                            x += 200 + ExcelUtil.dataManipulator.kilitMotorKampanaBosluk + ExcelUtil.dataManipulator.kilitMotorMotorBoslukX;
                            yV += 200 + ExcelUtil.dataManipulator.kilitMotorBoslukYOn + ExcelUtil.dataManipulator.kilitMotorBoslukYArka;
                            System.out.println("Kilit Motor için:");
                            System.out.println("X += " + ExcelUtil.dataManipulator.kilitMotorKampanaBosluk + " (Kampana Boşluk) + " + ExcelUtil.dataManipulator.kilitMotorMotorBoslukX + " (Kilit Motor Boşluk)");
                            System.out.println("yV += " + ExcelUtil.dataManipulator.kilitMotorBoslukYOn + " (Kilit Motor Ön) + " + ExcelUtil.dataManipulator.kilitMotorBoslukYArka + " (Kilit Motor Arka)");
                        }
                    }
                }
            } else { //hidrolik kilit olmadığı durumlarda valf tipleri için
                if(Objects.equals(secilenValfTipi, "İnişte Tek Hız")) {
                    // X yönünde +120 olacak Y yönünde 180 mm eklenecek
                    x += 70 + ExcelUtil.dataManipulator.valfBoslukX + ExcelUtil.dataManipulator.tekHizAraBoslukX;
                    yV += 180 + ExcelUtil.dataManipulator.valfBoslukYOn + ExcelUtil.dataManipulator.valfBoslukYArka;
                    System.out.println("İnişte Tek Hız İçin: (Hidrolik Kilit Yok)");
                    System.out.println("X += " + ExcelUtil.dataManipulator.valfBoslukX + " (Valf Boşluk) + " + ExcelUtil.dataManipulator.tekHizAraBoslukX + " (Tek Hız Boşluk)");
                    System.out.println("yV += " + ExcelUtil.dataManipulator.valfBoslukYOn + " (Valf Boşluk Ön) + " + ExcelUtil.dataManipulator.valfBoslukYArka + " (Valf Boşluk Arka)");
                } else if(Objects.equals(secilenValfTipi, "İnişte Çift Hız")) {
                    //X yönünde 190 Y yönünde 90
                    x += 140 + ExcelUtil.dataManipulator.ciftHizAraBoslukX + ExcelUtil.dataManipulator.valfBoslukX;
                    yV += 90 + ExcelUtil.dataManipulator.valfBoslukYOn + ExcelUtil.dataManipulator.valfBoslukYArka;
                    System.out.println("İnişte Çift Hız İçin: (Hidrolik Kilit Yok)");
                    System.out.println("X += " + ExcelUtil.dataManipulator.valfBoslukX + " (Valf Boşluk) + " + ExcelUtil.dataManipulator.ciftHizAraBoslukX + " (Tek Hız Boşluk)");
                    System.out.println("yV += " + ExcelUtil.dataManipulator.valfBoslukYOn + " (Valf Boşluk Ön) + " + ExcelUtil.dataManipulator.valfBoslukYArka + " (Valf Boşluk Arka)");
                } else {
                    //kompanzasyon seçilmişse:
                    //kilit yoksa: X'e 190 Y'ye 180
                    if(secilenHidrolikKilitDurumu.equals("Yok") && Objects.equals(secilenValfTipi, "Kompanzasyon + İnişte Tek Hız")) {
                        x += 140 + ExcelUtil.dataManipulator.kompanzasyonTekHizAraBoslukX;
                        yV += 180 + ExcelUtil.dataManipulator.valfBoslukYOn + ExcelUtil.dataManipulator.valfBoslukYArka;
                        System.out.println("Kompanzasyon + Tek Hız İçin: (Hidrolik Kilit Yok)");
                        System.out.println("X += " + ExcelUtil.dataManipulator.kompanzasyonTekHizAraBoslukX + " (Kompanzasyon Ara Boşluk)");
                        System.out.println("yV += " + ExcelUtil.dataManipulator.valfBoslukYOn + " (Valf Boşluk Ön) + " + ExcelUtil.dataManipulator.valfBoslukYArka + " (Valf Boşluk Arka)");
                    }
                }
            }

            y = Math.max(yV, yK);
            if(y <= 350) {
                y = 350;
            }
            if(x <= 550) {
                x = 550;
            }
            h = defaultHeight;

            String veri = ExcelUtil.dataManipulator.motorYukseklikVerileri.get(motorComboBox.getSelectionModel().getSelectedIndex());
            String sayiKismi = veri.replaceAll("[^0-9]", "");
            int yukseklik = Integer.parseInt(sayiKismi);

            if(h >= yukseklik) {
                h = defaultHeight;
            } else {
                h = yukseklik;
            }

            hesaplananHacim = ((x*h*y) / 1000000) - ExcelUtil.dataManipulator.kayipLitre;
            eskiX = x;
            eskiY = y;
            eskiH = h;
        }

        Tank finalTank = null;
        for(Tank selectedTank : ExcelUtil.dataManipulator.inputTanks) {
            int litre = selectedTank.getKabinHacim();
            int tempX = selectedTank.getKabinX();
            int tempY = selectedTank.getKabinY();

            if(hesaplananHacim > girilenTankKapasitesiMiktari) {
                if(x <= tempX && y <= tempY) {
                    finalTank = selectedTank;
                    break;
                }
            } else {
                if (litre >= girilenTankKapasitesiMiktari) {
                    if(hesaplananHacim != litre) {
                        if(x < tempX && y < tempY) {
                            finalTank = selectedTank;
                            break;
                        }
                    }
                }
            }
        }

        if (finalTank != null) {
            x = finalTank.getKabinX();
            y = finalTank.getKabinY();
            h = finalTank.getKabinH();
            atananHacim = finalTank.getKabinHacim();
        }

        String atananKabin = finalTank.getKabinName();
        String gecisOlculeri = finalTank.getGecisOlculeri();

        kullanilacakKabin.setText("Kullanmanız Gereken Kabin: \n\t\t\t\t\t\t" + atananKabin + "\n\t\t\tGeçiş Ölçüleri: " + gecisOlculeri + " (x, y, h)");
        atananKabinFinal = atananKabin;
        gecisOlculeriFinal = gecisOlculeri;
        //int secilenMotorIndeks = motorComboBox.getSelectionModel().getSelectedIndex();
        //int motorYukseklikDegeri = Integer.parseInt(motorYukseklikVerileri.get(secilenMotorIndeks));

        logCalculation(yV, yK, eskiX, eskiY, eskiH, hesaplananHacim, x, y, h, atananHacim, atananKabin, gecisOlculeri);

        finalValues.add(x);
        finalValues.add(y);
        finalValues.add(h);
        finalValues.add(atananHacim);
        return finalValues;
    }

    private void calculationSogutma(int x, int y, int h, ArrayList<Integer> finalValues, int hesaplananHacim, String atananKabinFinal, String gecisOlculeriFinal, int atananHacim) {
        x = 1000;
        y = 600;
        h = 350;
        hesaplananHacim = ((x*h*y) / 1000000) - ExcelUtil.dataManipulator.kayipLitre;

        atananHT = "HT SOĞUTMA";
        String atananKabin = "KD SOĞUTMA";
        String gecisOlculeri = "1000x600x350";
        kullanilacakKabin.setText("Kullanmanız Gereken Kabin: \n\t\t\t\t\t\t" + atananKabin + "\n\t\t\tGeçiş Ölçüleri: " + gecisOlculeri + " (x, y, h)");
        atananKabinFinal = atananKabin;
        gecisOlculeriFinal = gecisOlculeri;
        atananHacim = hesaplananHacim;

        System.out.println("--------Hesaplama Bitti--------");
        System.out.println("------------(Sonuç)------------");
        System.out.println("Atanan X: " + x);
        System.out.println("Atanan Y: " + y);
        System.out.println("Atanan h: " + h);
        System.out.println("Atanan Hacim: " + atananHacim);
        System.out.println("Kullanmanız Gereken Kabin: " + atananKabin);
        System.out.println("Geçiş Ölçüleri: " + gecisOlculeri);
        System.out.println("-------------------------------");

        finalValues.add(x);
        finalValues.add(y);
        finalValues.add(h);
        finalValues.add(atananHacim);
    }

    private void logCalculation(int yV, int yK, int eskiX, int eskiY, int eskiH, int hesaplananHacim, int x, int y, int h, int atananHacim, String atananKabin, String gecisOlculeri) {
        System.out.println("--------Hesaplama Bitti--------");
        System.out.println("------------(Sonuç)------------");
        System.out.println("yV: " + yV);
        System.out.println("yK: " + yK);
        System.out.println("Hesaplanan X: " + eskiX);
        System.out.println("Hesaplanan Y: " + eskiY);
        System.out.println("Hesaplanan h: " + eskiH);
        System.out.println("Hesaplanan Hacim: " + hesaplananHacim);
        System.out.println("Atanan X: " + x);
        System.out.println("Atanan Y: " + y);
        System.out.println("Atanan h: " + h);
        System.out.println("Atanan Hacim: " + atananHacim);
        System.out.println("Kullanmanız Gereken Kabin: " + atananKabin);
        System.out.println("Geçiş Ölçüleri: " + gecisOlculeri);
        System.out.println("-------------------------------");
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
        } else if(componentName.equals("kompanzasyon")) {
            kompanzasyonComboBox.getItems().clear();
            kompanzasyonComboBox.getItems().addAll("Var", "Yok");
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
                dataInit("sogutma", null);
                if(secilenMotor != null) {
                    tabloGuncelle();
                }
            }
        });

        sogutmaComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            secilenSogutmaDurumu = sogutmaComboBox.getValue();
            dataInit("hidrolikKilit", null);
            if(secilenSogutmaDurumu != null) {
                tabloGuncelle();
            }
        });

        hidrolikKilitComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            secilenHidrolikKilitDurumu = newValue;
            if(secilenPompa != null) {
                dataInit("pompa", null);
                tabloGuncelle();
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
                imageTextDisable();
            }
        });

        tankKapasitesiTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!tankKapasitesiTextField.getText().isEmpty()) {
                girilenTankKapasitesiMiktari = Integer.parseInt(newValue);
            }
            dataInit("kompanzasyon", null);
            if(girilenTankKapasitesiMiktari != 0) {
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

        imageTextDisable();
    }

    private void imageTextDisable() {
        for(Text text : sonucTexts) {
            hydraulicUnitBox.getChildren().remove(text);
        }
        sonucTexts.clear();
    }

    private void imageTextEnable(int x, int y, String calculatedImage) {
        if(calculatedImage.equals("sogutmaKilit")) {
            addTextToList("X: " + x + " mm", 580, 525, 0, 14, Color.WHITE);
            addTextToList("Y: " + y + " mm", 390, 400, 90, 14, Color.WHITE);

            addTextToList("40 mm", 420, 320, 90, 10, Color.WHITE);
            addTextToList("100 mm", 460, 310, 0, 10, Color.WHITE);
            addTextToList("230 mm", 450, 380, 0, 10, Color.WHITE);
            addTextToList("100 mm", 525, 310, 0, 9, Color.WHITE);
            addTextToList("100 mm", 525, 360, 0, 9, Color.WHITE);
            addTextToList("40 mm", 525, 402, 0, 9, Color.WHITE);
            addTextToList("50 mm", 435, 465, 0, 10, Color.WHITE);
            addTextToList("50 mm", 447, 505, 0, 10, Color.WHITE);
            addTextToList("100 mm", 740, 313, 90, 9, Color.WHITE);
            addTextToList("60 mm", 765, 355, 90, 10, Color.WHITE);
            addTextToList("70 mm", 573, 495, 90, 5, Color.WHITE);
            addTextToList("50 mm", 650, 500, 0, 9, Color.WHITE);
            addTextToList("230 mm", 635, 445, 0, 10, Color.WHITE);
            addTextToList("70 mm", 728, 500, 0, 9, Color.WHITE);
            addTextToList("70 mm", 763, 425, 90, 10, Color.WHITE);
            addTextToList(getKampanaText(), 688, 435, 0, 10, Color.WHITE);
            addTextToList("Kampana: " + 250 + "\nKesim: Ø" + 173, 555, 450, 0, 8, Color.WHITE);
        } else if(calculatedImage.equals("sogutmaKilitsiz")) {
            addTextToList("X: " + x + " mm", 590, 515, 0, 14, Color.WHITE);
            addTextToList("Y: " + y + " mm", 400, 400, 90, 14, Color.WHITE);

            addTextToList("230 mm", 465, 380, 0, 10, Color.WHITE);
            addTextToList("100 mm", 533, 325, 0, 9, Color.WHITE);
            addTextToList("100 mm", 533, 363, 0, 9, Color.WHITE);
            addTextToList("40 mm", 533, 402, 0, 9, Color.WHITE);
            addTextToList("50 mm", 455, 460, 0, 9, Color.WHITE);
            addTextToList("50 mm", 460, 495, 0, 10, Color.WHITE);
            addTextToList("100 mm", 722, 322, 90, 9, Color.WHITE);
            addTextToList("60 mm", 748, 363, 90, 10, Color.WHITE);
            addTextToList("70 mm", 575, 487, 90, 6, Color.WHITE);
            addTextToList("50 mm", 648, 490, 0, 9, Color.WHITE);
            addTextToList("230 mm", 630, 443, 0, 10, Color.WHITE);
            addTextToList("70 mm", 720, 490, 0, 9, Color.WHITE);
            addTextToList("70 mm", 748, 423, 90, 10, Color.WHITE);
            addTextToList(getKampanaText(), 680, 435, 0, 9, Color.WHITE);
            addTextToList("Kampana: " + 250 + "\nKesim: Ø" + 173, 557, 445, 0, 8, Color.WHITE);
        } else if(calculatedImage.equals("standartUnite")) {
            addTextToList("X: " + x + " mm", 590, 510, 0, 14, Color.WHITE);
            addTextToList("Y: " + y + " mm", 400, 400, 90, 14, Color.WHITE);

            addTextToList("50 mm", 488, 297, 0, 10, Color.WHITE);
            addTextToList("100 mm", 465, 320, 0, 9, Color.WHITE);
            addTextToList("Boğaz: Ø200\nKesim: Ø115", 505, 323, 0, 7.5, Color.WHITE);
            addTextToList("Kilit Motor: " + secilenKilitMotor + "\nKilit Pompa: " + secilenKilitPompa, 495, 355, 0, 10, Color.WHITE);
            addTextToList(secilenValfTipi, 535, 455, 0, 10, Color.WHITE);
            addTextToList("50 mm", 455, 475, 90, 8, Color.WHITE);
            addTextToList("50 mm", 477, 490, 0, 8, Color.WHITE);
            addTextToList(getKampanaText(), 630, 350, 0, 12, Color.WHITE);
            addTextToList("70 mm", 685, 302, 0, 10, Color.WHITE);
            addTextToList("70 mm", 735, 377, 90, 10, Color.WHITE);
            addTextToList("50 mm", 652, 484, 0, 10, Color.WHITE);
            addTextToList("Ø20", 677, 463, 0, 10, Color.WHITE);
            addTextToList("50 mm", 705, 484, 0, 8, Color.WHITE);
            addTextToList("Ø50", 723, 455, 0, 10, Color.WHITE);
            addTextToList("50 mm", 740, 457, 90, 8, Color.WHITE);
        } else if(calculatedImage.equals("kilitMotor")) {
            addTextToList("X: " + x + " mm", 590, 510, 0, 14, Color.WHITE);
            addTextToList("Y: " + y + " mm", 400, 400, 90, 14, Color.WHITE);

            addTextToList("50 mm", 480, 300, 0, 10, Color.WHITE);
            addTextToList("50 mm", 452, 348, 90, 10, Color.WHITE);
            addTextToList("50 mm", 450, 480, 0, 7, Color.WHITE);
            addTextToList("50 mm", 482, 490, 0, 10, Color.WHITE);
            addTextToList("50 mm", 555, 490, 0, 10, Color.WHITE);
            addTextToList("Ø50", 470, 460, 0, 10, Color.WHITE);
            addTextToList("Ø20", 550, 462, 0, 10, Color.WHITE);
            addTextToList(secilenValfTipi, 475, 430, 0, 12, Color.WHITE);
            addTextToList(getKampanaText(), 630, 365, 0, 12, Color.WHITE);
            addTextToList("70 mm", 682, 310, 0, 11, Color.WHITE);
            addTextToList("70 mm", 735, 390, 90, 11, Color.WHITE);
        } else if(calculatedImage.equals("kompanzasyon")) {
            addTextToList("X: " + x + " mm", 590, 510, 0, 14, Color.WHITE);
            addTextToList("Y: " + y + " mm", 400, 400, 90, 14, Color.WHITE);

            addTextToList("50 mm", 465, 300, 0, 10, Color.WHITE);
            addTextToList("50 mm", 450, 358, 90, 10, Color.WHITE);
            addTextToList("50 mm", 450, 460, 0, 8, Color.WHITE);
            addTextToList("50 mm", 497, 490, 0, 10, Color.WHITE);
            addTextToList("50 mm", 570, 490, 0, 10, Color.WHITE);
            addTextToList("Ø50", 485, 460, 0, 10, Color.WHITE);
            addTextToList("Ø20", 555, 455, 0, 10, Color.WHITE);
            addTextToList(secilenValfTipi, 460, 383, 0, 10, Color.WHITE);
            addTextToList(getKampanaText(), 630, 357, 0, 12, Color.WHITE);
            addTextToList("70 mm", 682, 310, 0, 11, Color.WHITE);
            addTextToList("70 mm", 735, 390, 90, 11, Color.WHITE);
        } else if(calculatedImage.equals("tekhizcifthiz")) {
            addTextToList("X: " + x + " mm", 590, 510, 0, 14, Color.WHITE);
            addTextToList("Y: " + y + " mm", 400, 400, 90, 14, Color.WHITE);

            addTextToList("50 mm", 465, 300, 0, 10, Color.WHITE);
            addTextToList("50 mm", 450, 358, 90, 10, Color.WHITE);
            addTextToList("50 mm", 450, 465, 0, 8, Color.WHITE);
            addTextToList("50 mm", 497, 490, 0, 10, Color.WHITE);
            addTextToList("50 mm", 570, 490, 0, 10, Color.WHITE);
            addTextToList("Ø50", 485, 460, 0, 10, Color.WHITE);
            addTextToList("Ø20", 555, 455, 0, 10, Color.WHITE);
            addTextToList(secilenValfTipi, 470, 383, 0, 10, Color.WHITE);
            addTextToList(getKampanaText(), 630, 357, 0, 12, Color.WHITE);
            addTextToList("70 mm", 682, 310, 0, 11, Color.WHITE);
            addTextToList("70 mm", 735, 390, 90, 11, Color.WHITE);
        }

        for (Text text : sonucTexts) {
            hydraulicUnitBox.getChildren().add(text);
        }
    }

    private void addTextToList(String content, int x, int y, int rotate, double fontSize, Color color) {
        Text text = new Text(content);
        text.setX(x);
        text.setY(y);
        text.setRotate(rotate);
        text.setFill(color);
        text.setFont(new Font(fontSize));
        sonucTexts.add(text);
    }

    private String getKampanaText() {
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

        return kampanaText;
    }

    private void sonucEkraniTemizle() {
        yukseklikSonucText.setVisible(false);
        genislikSonucText.setVisible(false);
        derinlikSonucText.setVisible(false);
        hacimText.setVisible(false);
    }

    private void tabloGuncelle() 1{
        sonucTablo.getItems().clear();
        TableData data = new TableData("Sipariş Numarası:", girilenSiparisNumarasi);
        sonucTablo.getItems().add(data);

        data = new TableData("Hidrolik Ünitesi Tipi:", secilenUniteTipi);
        sonucTablo.getItems().add(data);

        data = new TableData("Seçilen Motor:", secilenMotor);
        sonucTablo.getItems().add(data);

        data = new TableData("Kampana:", String.valueOf(secilenKampana));
        sonucTablo.getItems().add(data);

        data = new TableData("Soğutma Durumu:", secilenSogutmaDurumu);
        sonucTablo.getItems().add(data);

        data = new TableData("Hidrolik Kilit Durumu:", secilenHidrolikKilitDurumu);
        sonucTablo.getItems().add(data);

        data = new TableData("Seçilen Pompa:", secilenPompa);
        sonucTablo.getItems().add(data);

        data = new TableData("Tank Kapasitesi:", String.valueOf(girilenTankKapasitesiMiktari));
        sonucTablo.getItems().add(data);

        data = new TableData("Kompanzasyon:", String.valueOf(kompanzasyonDurumu));
        sonucTablo.getItems().add(data);

        data = new TableData("Seçilen Valf Tipi:", secilenValfTipi);
        sonucTablo.getItems().add(data);

        data = new TableData("Kilit Motoru:", Objects.requireNonNullElse(secilenKilitMotor, "Yok"));
        sonucTablo.getItems().add(data);

        data = new TableData("Kilit Pompa:", Objects.requireNonNullElse(secilenKilitPompa, "Yok"));
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
                    if(secilenValfTipi.contains("Kilitli Blok")) {
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
                    sonucTankGorsel.setImage(image);
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
        Color color = (type == 1) ? Color.WHITE : Color.BLACK;
        for (Text text : sonucTexts) {
            text.setFill(color);
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