package me.t3sl4.hydraulic.controllers.Calculation.Classic;

import javafx.fxml.FXML;
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
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import me.t3sl4.hydraulic.Launcher;
import me.t3sl4.hydraulic.utils.Utils;
import me.t3sl4.hydraulic.utils.database.File.PDF.PDFUtil;
import me.t3sl4.hydraulic.utils.database.Model.Kabin.Kabin;
import me.t3sl4.hydraulic.utils.database.Model.Table.PartList.TableData;
import me.t3sl4.hydraulic.utils.general.SceneUtil;
import me.t3sl4.hydraulic.utils.general.SystemVariables;
import me.t3sl4.hydraulic.utils.service.HTTP.HTTPMethod;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;

import static me.t3sl4.hydraulic.utils.general.SystemVariables.*;

public class ClassicController {

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
    private ComboBox<String> kilitMotorComboBox;

    @FXML
    private ComboBox<String> kilitPompaComboBox;

    @FXML
    private Text kullanilacakKabin;

    @FXML
    private Text kullanilacakKabinMainText;

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
    private Button kaydetButton;

    @FXML
    private Button parcaListesiButton;

    @FXML
    private ImageView sonucTankGorsel;

    @FXML
    private VBox klasikVBox;

    @FXML
    private Label screenDetectorLabel;

    /*
    Seçilen Değerler:
     */
    public static String secilenUniteTipi = "Klasik";
    public static String girilenSiparisNumarasi = null;
    public static String secilenMotor = null;
    public static String kompanzasyonDurumu = null;
    public static int secilenKampana = 0;
    public static String secilenPompa = null;
    public static double secilenPompaVal;
    public static int girilenTankKapasitesiMiktari = 0;
    public static String secilenHidrolikKilitDurumu = null;
    public static String secilenValfTipi = null;
    public static String secilenKilitMotor = null;
    public static String secilenKilitPompa = null;
    public static String secilenSogutmaDurumu = null;

    public boolean hidrolikKilitStat = false;
    public boolean hesaplamaBitti = false;

    public static String atananHT;

    private ArrayList<Text> sonucTexts = new ArrayList<>();

    public static String atananKabinFinal = "";
    public static String gecisOlculeriFinal = "";
    private String imagePath = "";
    private String reverseImagePath = "";

    int motorYukseklik = 0;

    int calculationResultX, calculationResultY, calculationResultH;

    public void initialize() {
        Utils.textFilter(tankKapasitesiTextField);
        comboBoxListener();
        sonucTabloSatir1.setCellValueFactory(new PropertyValueFactory<>("satir1Property"));
        sonucTabloSatir2.setCellValueFactory(new PropertyValueFactory<>("satir2Property"));
        if(SystemVariables.loggedInUser == null) {
            kaydetButton.setDisable(true);
        }
    }

    @FXML
    public void hesaplaFunc() {
        ArrayList<Integer> results;
        int calculatedX, calculatedY, calculatedH, calculatedHacim;

        if (checkComboBox()) {
            Utils.showErrorMessage("Lütfen tüm girdileri kontrol edin.", SceneUtil.getScreenOfNode(screenDetectorLabel), (Stage)screenDetectorLabel.getScene().getWindow());
        } else {
            sonucEkraniTemizle();
            imageTextDisable();

            enableSonucSection();
            results = calcDimensions();

            if (results.size() == 4) {
                calculatedX = results.get(0);
                calculatedY = results.get(1);
                calculatedH = results.get(2);
                calculatedHacim = results.get(3);

                genislikSonucText.setText("X: " + calculatedX + " mm");
                derinlikSonucText.setText("Y: " + calculatedY + " mm");
                yukseklikSonucText.setText("h: " + calculatedH + " mm");
                hacimText.setText("Tank : " + calculatedHacim + "L" + " (" + atananHT + ")");

                tabloGuncelle();
                Image image = null;

                if(secilenSogutmaDurumu.equals("Var")) {
                    if (secilenHidrolikKilitDurumu.equals("Var")) {
                        //Hidrolik Kilit Var
                        if(secilenValfTipi.equals("İnişte Tek Hız") || secilenValfTipi.equals("Kompanzasyon || İnişte Tek Hız")) {
                            image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/data/hydraulicUnitData/schematicImages/sogutma_kilitli_tek_hiz_white.png")));
                            imagePath = "/assets/data/hydraulicUnitData/schematicImages/sogutma_kilitli_tek_hiz_white.png";
                            reverseImagePath = "/assets/data/hydraulicUnitData/schematicImages/sogutma_kilitli_tek_hiz_black.png";
                            imageTextEnable(calculatedX, calculatedY, "sogutma_kilitli_tek_hiz");
                        } else if(secilenValfTipi.equals("İnişte Çift Hız") || secilenValfTipi.equals("Kilitli Blok")) {
                            image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/data/hydraulicUnitData/schematicImages/sogutma_kilitli_cift_hiz_white.png")));
                            imagePath = "/assets/data/hydraulicUnitData/schematicImages/sogutma_kilitli_cift_hiz_white.png";
                            reverseImagePath = "/assets/data/hydraulicUnitData/schematicImages/sogutma_kilitli_cift_hiz_black.png";
                            imageTextEnable(calculatedX, calculatedY, "sogutma_kilitli_cift_hiz");
                        }
                    } else {
                        //Hidrolik Kilit Yok
                        if(secilenValfTipi.equals("İnişte Tek Hız") || secilenValfTipi.equals("Kompanzasyon || İnişte Tek Hız")) {
                            image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/data/hydraulicUnitData/schematicImages/sogutma_kilitsiz_tek_hiz_white.png")));
                            imagePath = "/assets/data/hydraulicUnitData/schematicImages/sogutma_kilitsiz_tek_hiz_white.png";
                            reverseImagePath = "/assets/data/hydraulicUnitData/schematicImages/sogutma_kilitsiz_tek_hiz_black.png";
                            imageTextEnable(calculatedX, calculatedY, "sogutma_kilitsiz_tek_hiz");
                        } else if(secilenValfTipi.equals("İnişte Çift Hız") || secilenValfTipi.equals("Kilitli Blok")) {
                            image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/data/hydraulicUnitData/schematicImages/sogutma_kilitsiz_cift_hiz_white.png")));
                            imagePath = "/assets/data/hydraulicUnitData/schematicImages/sogutma_kilitsiz_cift_hiz_white.png";
                            reverseImagePath = "/assets/data/hydraulicUnitData/schematicImages/sogutma_kilitsiz_cift_hiz_black.png";
                            imageTextEnable(calculatedX, calculatedY, "sogutma_kilitsiz_cift_hiz");
                        }
                    }
                } else {
                    if(secilenHidrolikKilitDurumu.equals("Var")) {
                        //Hidrolik Kilit Var
                        if(secilenValfTipi.equals("Kilitli Blok")) {
                            image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/data/hydraulicUnitData/schematicImages/kilitli_blok_white.png")));
                            imagePath = "/assets/data/hydraulicUnitData/schematicImages/kilitli_blok_white.png";
                            reverseImagePath = "/assets/data/hydraulicUnitData/schematicImages/kilitli_blok_black.png";
                            imageTextEnable(calculatedX, calculatedY, "kilitli_blok");
                        } else {
                            if(secilenKilitMotor != null) {
                                if(secilenValfTipi.equals("İnişte Tek Hız") || secilenValfTipi.equals("Kompanzasyon || İnişte Tek Hız")) {
                                    image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/data/hydraulicUnitData/schematicImages/kilit_ayri_tek_hiz_white.png")));
                                    imagePath = "/assets/data/hydraulicUnitData/schematicImages/kilit_ayri_tek_hiz_white.png";
                                    reverseImagePath = "/assets/data/hydraulicUnitData/schematicImages/kilit_ayri_tek_hiz_black.png";
                                    imageTextEnable(calculatedX, calculatedY, "kilit_ayri_tek_hiz");
                                } else if(secilenValfTipi.equals("İnişte Çift Hız")) {
                                    image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/data/hydraulicUnitData/schematicImages/kilit_ayri_cift_hiz_white.png")));
                                    imagePath = "/assets/data/hydraulicUnitData/schematicImages/kilit_ayri_cift_hiz_white.png";
                                    reverseImagePath = "/assets/data/hydraulicUnitData/schematicImages/kilit_ayri_cift_hiz_black.png";
                                    imageTextEnable(calculatedX, calculatedY, "kilit_ayri_cift_hiz");
                                }
                            }
                        }
                    } else {
                        //Hidrolik Kilit Yok
                        if(kompanzasyonDurumu.equals("Var")) {
                            //Kompanzasyon Var
                            image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/data/hydraulicUnitData/schematicImages/tek_hiz_kompanzasyon_arti_tek_hiz_white.png")));
                            imagePath = "/assets/data/hydraulicUnitData/schematicImages/tek_hiz_kompanzasyon_arti_tek_hiz_white.png";
                            reverseImagePath = "/assets/data/hydraulicUnitData/schematicImages/tek_hiz_kompanzasyon_arti_tek_hiz_black.png";
                            imageTextEnable(calculatedX, calculatedY, "tek_hiz_kompanzasyon_arti_tek_hiz");
                        } else {
                            if(secilenValfTipi.equals("İnişte Çift Hız")) {
                                image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/data/hydraulicUnitData/schematicImages/cift_hiz_white.png")));
                                imagePath = "/assets/data/hydraulicUnitData/schematicImages/cift_hiz_white.png";
                                reverseImagePath = "/assets/data/hydraulicUnitData/schematicImages/cift_hiz_black.png";
                                imageTextEnable(calculatedX, calculatedY, "cift_hiz");
                            } else if(secilenValfTipi.equals("İnişte Tek Hız")) {
                                image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/data/hydraulicUnitData/schematicImages/tek_hiz_kompanzasyon_arti_tek_hiz_white.png")));
                                imagePath = "/assets/data/hydraulicUnitData/schematicImages/tek_hiz_kompanzasyon_arti_tek_hiz_white.png";
                                reverseImagePath = "/assets/data/hydraulicUnitData/schematicImages/tek_hiz_kompanzasyon_arti_tek_hiz_black.png";
                                imageTextEnable(calculatedX, calculatedY, "tek_hiz_kompanzasyon_arti_tek_hiz");
                            }
                        }
                    }
                }

                tankGorselLoad();

                sonucKapakImage.setImage(image);
                parcaListesiButton.setDisable(false);
                exportButton.setDisable(false);
                if(SystemVariables.loggedInUser != null) {
                    kaydetButton.setDisable(false);
                }

                hesaplamaBitti = true;
            } else {
                Utils.showErrorMessage("Hesaplama sonucu beklenmeyen bir hata oluştu.", SceneUtil.getScreenOfNode(screenDetectorLabel), (Stage)screenDetectorLabel.getScene().getWindow());
            }
        }
    }

    @FXML
    public void transferCalculation() {
        String creationURL = BASE_URL + createHydraulicURLPrefix;

        String pdfPath = SystemVariables.pdfFileLocalPath + girilenSiparisNumarasi + ".pdf";
        String excelPath = excelFileLocalPath + girilenSiparisNumarasi + ".xlsx";

        if (new File(pdfPath).exists() && new File(excelPath).exists()) {
            File partListFile = new File(excelPath);
            File schematicFile = new File(pdfPath);

            Map<String, File> files = new HashMap<>();
            files.put("partListFile", partListFile);
            files.put("schematicFile", schematicFile);

            HTTPMethod.authorizedUploadMultipleFiles(creationURL, "POST", files, SystemVariables.loggedInUser.getAccessToken(), SystemVariables.loggedInUser.getUsername(), loggedInUser.getUserID(), girilenSiparisNumarasi, secilenUniteTipi, new HTTPMethod.RequestCallback() {
                @Override
                public void onSuccess(String response) {
                    Utils.showSuccessMessage("Hidrolik ünitesi başarılı bir şekilde kaydedildi.", SceneUtil.getScreenOfNode(screenDetectorLabel), (Stage)screenDetectorLabel.getScene().getWindow());
                }

                @Override
                public void onFailure() {
                    Utils.showErrorMessage("Hidrolik ünitesi kaydedilemedi !", SceneUtil.getScreenOfNode(screenDetectorLabel), (Stage)screenDetectorLabel.getScene().getWindow());
                }
            });
        } else {
            Utils.showErrorMessage("Lütfen PDF ve parça listesi oluşturduktan sonra kaydedin", SceneUtil.getScreenOfNode(screenDetectorLabel), (Stage)screenDetectorLabel.getScene().getWindow());
        }
    }

    @FXML
    public void motorPressed() {
        if(motorComboBox.getValue() != null) {
            secilenMotor = motorComboBox.getValue();
            secilenKampana = Integer.parseInt(SystemVariables.getLocalHydraulicData().motorKampanaMap.get(motorComboBox.getSelectionModel().getSelectedItem().toString()).replace(" mm", ""));

            if(secilenSogutmaDurumu == null) {
                initComboBoxes("motor", 0);
            }
        }
    }

    @FXML
    public void sogutmaPressed() {
        if(sogutmaComboBox.getValue() != null) {
            secilenSogutmaDurumu = sogutmaComboBox.getValue();

            if(secilenHidrolikKilitDurumu == null) {
                initComboBoxes("sogutma", 0);
            }
        }
    }

    @FXML
    public void hidrolikKilitPressed() {
        if(hidrolikKilitComboBox.getValue() != null) {
            secilenHidrolikKilitDurumu = hidrolikKilitComboBox.getValue();
            hidrolikKilitStat = true;

            if(secilenPompa == null) {
                initComboBoxes("hidrolikKilit", 0);
            }
        }
    }

    @FXML
    public void pompaPressed() {
        if(pompaComboBox.getValue() != null) {
            secilenPompa = pompaComboBox.getValue();
            secilenPompaVal = Utils.string2Double(pompaComboBox.getSelectionModel().getSelectedItem());

            if(girilenTankKapasitesiMiktari == 0) {
                initComboBoxes("pompa", 0);
            }
        }
    }

    @FXML
    public void tankKapasitesiEntered() {
        if(tankKapasitesiTextField.getText() != null) {
            if(!tankKapasitesiTextField.getText().isEmpty()) {
                girilenTankKapasitesiMiktari = Integer.parseInt(tankKapasitesiTextField.getText());

                if(kompanzasyonDurumu == null) {
                    initComboBoxes("tankKapasitesi", 0);
                }

                kompanzasyonComboBox.setDisable(false);
            }
        }
    }

    @FXML
    public void tankKapasitesiBackSpacePressed(KeyEvent event) {
        if(event.getCode() == KeyCode.BACK_SPACE || event.getCode() == KeyCode.DELETE) {
            tankKapasitesiTextField.clear();
            if(kompanzasyonDurumu != null) {
                kompanzasyonComboBox.setDisable(true);
            }
        }
    }

    @FXML
    public void kompanzasyonPressed() {
        if(kompanzasyonComboBox.getValue() != null) {
            kompanzasyonDurumu = kompanzasyonComboBox.getValue();

            if(secilenValfTipi == null) {
                initComboBoxes("kompanzasyon", 0);
            }
        }
    }

    @FXML
    public void valfTipiPressed() {
        if(valfTipiComboBox.getValue() != null) {
            secilenValfTipi = valfTipiComboBox.getValue();
            secilenPompaVal = Utils.string2Double(secilenPompa);

            if(secilenKilitMotor == null) {
                initComboBoxes("valfTipi", 0);
            }
        }
    }

    @FXML
    public void kilitMotorPressed() {
        if(kilitMotorComboBox.getValue() != null) {
            secilenKilitMotor = kilitMotorComboBox.getValue();

            if(secilenKilitPompa == null) {
                initComboBoxes("kilitMotor", 0);
            }
        }
    }

    @FXML
    public void kilitPompaPressed() {
        if(kilitPompaComboBox.getValue() != null) {
            secilenKilitPompa = kilitPompaComboBox.getValue();
        }
    }

    @FXML
    public void parcaListesiGoster() {
        Image icon = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/images/general/logo.png")));
        if(hesaplamaBitti) {
            Utils.showParcaListesiPopup(icon, SceneUtil.getScreenOfNode(screenDetectorLabel), "fxml/PartList.fxml");
        } else {
            Utils.showErrorMessage("Lütfen önce hesaplama işlemini bitirin !", SceneUtil.getScreenOfNode(screenDetectorLabel), (Stage)screenDetectorLabel.getScene().getWindow());
        }
    }

    @FXML
    public void temizlemeIslemi() {
        verileriSifirla();
        hesaplamaBitti = false;
    }

    @FXML
    public void exportProcess() {
        int tankImageStartX = 258;
        int tankImageStartY = 213;
        int tankImageWidth = 450;
        int tankImageHeight = 262;

        int schematicImageStartX = 740;
        int schematicImageStartY = 200;
        int schematicImageWidth = 400;
        int schematicImageHeight = 246;

        if(hesaplamaBitti) {
            String generalCyclinderString = Utils.showCyclinderPopup(SceneUtil.getScreenOfNode(derinlikSonucText), (Stage)screenDetectorLabel.getScene().getWindow(), null, null);
            String numberPart = "";
            String stringPart = "";
            if(generalCyclinderString != null) {
                numberPart = generalCyclinderString.replaceAll("[^0-9]", "");
                stringPart = generalCyclinderString.replaceAll("[0-9]", "");
            } else {
                Utils.showErrorMessage("Silindir sayısı seçilmedi. İşlem iptal edildi.", SceneUtil.getScreenOfNode(screenDetectorLabel), (Stage)screenDetectorLabel.getScene().getWindow());
                return;
            }

            int selectedCylinders = Integer.parseInt(numberPart);
            String isPressureValf = stringPart;
            if (selectedCylinders == -1 && isPressureValf.isEmpty()) {
                Utils.showErrorMessage("Silindir sayısı seçilmedi. İşlem iptal edildi.", SceneUtil.getScreenOfNode(screenDetectorLabel), (Stage)screenDetectorLabel.getScene().getWindow());
                return;
            }

            pdfShaper(0);
            PDFUtil.coords2Png(tankImageStartX, tankImageStartY, tankImageWidth, tankImageHeight, exportButton);
            pdfShaper(1);
            PDFUtil.cropImage(tankImageStartX, tankImageStartY, tankImageWidth, tankImageHeight, "tankImage.png");

            pdfShaper(0);
            PDFUtil.coords2Png(schematicImageStartX, schematicImageStartY, schematicImageWidth, schematicImageHeight, exportButton);
            pdfShaper(1);
            PDFUtil.cropImage(schematicImageStartX, schematicImageStartY, schematicImageWidth, schematicImageHeight, "schematicImage.png");

            String pdfPath = hydraulicSchemaSelection(selectedCylinders, isPressureValf);
            System.out.println("PDF Şema Yolu: " + pdfPath);

            PDFUtil.pdfGenerator("/assets/images/general/onder_grup_main.png", "tankImage.png", "schematicImage.png", "/assets/data/hydraulicUnitData/schematicPDF/classic/" + pdfPath, girilenSiparisNumarasi, kullanilacakKabin.getText().toString(), secilenMotor, secilenPompa, secilenUniteTipi, true);
        } else {
            Utils.showErrorMessage("Lütfen hesaplama işlemini tamamlayıp tekrar deneyin.", SceneUtil.getScreenOfNode(screenDetectorLabel), (Stage)screenDetectorLabel.getScene().getWindow());
        }
    }

    private String hydraulicSchemaSelection(int selectedCylinders, String isPressureValf) {
        boolean isSogutmaVar = secilenSogutmaDurumu.equals("Var");
        boolean isKilitVar = secilenHidrolikKilitDurumu != null;
        boolean isKompanzasyonVar = kompanzasyonDurumu.equals("Var");
        boolean isKilitMotorVar = secilenKilitMotor != null;

        if(isSogutmaVar) {
            if(isKilitVar && isKilitMotorVar) {
                if(isKompanzasyonVar) {
                    if(secilenValfTipi.equals("Kompanzasyon || İnişte Tek Hız")) {
                        return getCylinderImage(selectedCylinders, isPressureValf, 13, 14, 15, 16);
                    }
                } else {
                    if(secilenValfTipi.equals("İnişte Çift Hız")) {
                        return getCylinderImage(selectedCylinders, isPressureValf, 5, 6, 7, 8);
                    }
                }
            } else {
                if(isKompanzasyonVar) {
                    if(secilenValfTipi.equals("Kompanzasyon || İnişte Tek Hız")) {
                        return getCylinderImage(selectedCylinders, isPressureValf, 9, 10, 11, 12);
                    }
                } else {
                    if(secilenValfTipi.equals("İnişte Çift Hız")) {
                        return getCylinderImage(selectedCylinders, isPressureValf, 1, 2, 3, 4);
                    }
                }
            }
        } else {
            if(isKilitVar && isKilitMotorVar) {
                if(isKompanzasyonVar) {
                    if(secilenValfTipi.equals("Kompanzasyon || İnişte Tek Hız")) {
                        return getCylinderImage(selectedCylinders, isPressureValf, 33, 34, 35, 36);
                    }
                } else {
                    if(secilenValfTipi.equals("İnişte Tek Hız")) {
                        return getCylinderImage(selectedCylinders, isPressureValf, 17, 18, 19, 20);
                    } else if(secilenValfTipi.equals("İnişte Çift Hız")) {
                        return getCylinderImage(selectedCylinders, isPressureValf, 21, 22, 23, 24);
                    }
                }
            } else {
                if(isKompanzasyonVar) {
                    if(secilenValfTipi.equals("Kompanzasyon || İnişte Tek Hız")) {
                        return getCylinderImage(selectedCylinders, isPressureValf, 29, 30, 31, 32);
                    }
                } else {
                    if(secilenValfTipi.equals("İnişte Tek Hız")) {
                        return getCylinderImage(selectedCylinders, isPressureValf, 41, 42, 43, 44);
                    } else if(secilenValfTipi.equals("İnişte Çift Hız")) {
                        return getCylinderImage(selectedCylinders, isPressureValf, 37, 38, 39, 40);
                    } else if(secilenValfTipi.equals("Kilitli Blok")) {
                        return getCylinderImage(selectedCylinders, isPressureValf, 25, 26, 27, 28);
                    }
                }
            }
        }
        return null;
    }

    private String getCylinderImage(int selectedCylinders, String isPressureValf, int one, int two, int three, int other) {
        String suffix = isPressureValf.equals("Var") ? "B" : "";
        switch (selectedCylinders) {
            case 1:
                return one + suffix + ".pdf";
            case 2:
                return two + suffix + ".pdf";
            case 3:
                return three + suffix + ".pdf";
            default:
                return other + suffix + ".pdf";
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

    ArrayList<Integer> calcDimensions() {
        //Eklenecek Değerler
        int kampanaDegeri = Integer.parseInt(SystemVariables.getLocalHydraulicData().motorKampanaMap.get(motorComboBox.getSelectionModel().getSelectedItem().toString()).replace(" mm", ""));

        secilenKampana = Integer.parseInt(SystemVariables.getLocalHydraulicData().motorKampanaMap.get(motorComboBox.getSelectionModel().getSelectedItem().toString()).replace(" mm", ""));
        secilenPompaVal = Utils.string2Double(secilenPompa);

        //Hesaplama Standartları
        ArrayList<Integer> finalValues = new ArrayList<>();
        int yV = 0;
        int yK = 0;
        int eskiX=0, eskiY=0, eskiH=0;
        int x=0, y=0, h=0;

        int atananHacim=0;

        System.out.println("--------Hesaplama Başladı--------");
        if(Objects.equals(secilenSogutmaDurumu, "Var")) {
            /* TODO
            Standart üniteyi göster. Ölçüler:
            X: 1000
            Y: 600
            H: 350
            Soğutmanın standardı için bir kabin eklenecek :)
             */

            atananHacim = 200;
            calculationSogutma(x, y, h, finalValues, atananKabinFinal, gecisOlculeriFinal, atananHacim);

            return finalValues;
        } else {
            x +=  kampanaDegeri;
            yK += kampanaDegeri;
            System.out.println("Başlangıç:");
            System.out.println("X: " + x + " yK: " + yK + " yV: " + yV);
            System.out.println("Motor + Kampana için:");
            System.out.println("X += " + kampanaDegeri + " (Kampana)");
            System.out.println("yK += " + kampanaDegeri + " (Kampana)");

            if(secilenHidrolikKilitDurumu.equals("Var")) {
                //Hidrolik Kilit Var
                if(secilenKilitMotor != null) {
                    //Kilit Motor Var
                    if(kompanzasyonDurumu.equals("Var")) {
                        //Kompanzasyon Var
                        if(secilenValfTipi.equals("Kompanzasyon || İnişte Tek Hız")) {
                            x += getLocalHydraulicData().kilitMotorTankArasiBoslukX + getLocalHydraulicData().kilitMotorX + getLocalHydraulicData().kilitMotorAraBoslukX + getLocalHydraulicData().kampanaTankArasiBoslukX;
                            yV += getLocalHydraulicData().kilitMotorTankArasiBoslukY + getLocalHydraulicData().kilitMotorY + getLocalHydraulicData().kilitMotorYOn + getLocalHydraulicData().tekHizKilitAyriY + getLocalHydraulicData().tekHizBlokY + getLocalHydraulicData().tekHizKilitAyriYOn;
                            yK += getLocalHydraulicData().kampanaTankArasiBoslukY + getLocalHydraulicData().kampanaBoslukYOn;
                        }
                    } else {
                        //Kompanzasyon Yok
                        if(secilenValfTipi.equals("İnişte Tek Hız")) {
                            x += getLocalHydraulicData().kilitMotorTankArasiBoslukX + getLocalHydraulicData().kilitMotorX + getLocalHydraulicData().kilitMotorAraBoslukX + getLocalHydraulicData().kampanaTankArasiBoslukX;
                            yV += getLocalHydraulicData().kilitMotorTankArasiBoslukY + getLocalHydraulicData().kilitMotorY + getLocalHydraulicData().kilitMotorYOn + getLocalHydraulicData().tekHizKilitAyriY + getLocalHydraulicData().tekHizBlokY + getLocalHydraulicData().tekHizKilitAyriYOn;
                            yK += getLocalHydraulicData().kampanaTankArasiBoslukY + getLocalHydraulicData().kampanaBoslukYOn;
                        } else if(secilenValfTipi.equals("İnişte Çift Hız")) {
                            x += getLocalHydraulicData().kilitMotorTankArasiBoslukX + getLocalHydraulicData().kilitMotorX + getLocalHydraulicData().kilitMotorAraBoslukX + getLocalHydraulicData().kampanaTankArasiBoslukX;
                            yV += getLocalHydraulicData().kilitMotorTankArasiBoslukY + getLocalHydraulicData().kilitMotorY + getLocalHydraulicData().kilitMotorYOn + getLocalHydraulicData().ciftHizKilitAyriY + getLocalHydraulicData().ciftHizBlokY + getLocalHydraulicData().ciftHizKilitAyriYOn;
                            yK += getLocalHydraulicData().kampanaTankArasiBoslukY + getLocalHydraulicData().kampanaBoslukYOn;
                        }
                    }
                } else {
                    //Kilit Motor Yok
                    if(secilenValfTipi.equals("Kilitli Blok")) {
                        x += getLocalHydraulicData().kilitliBlokTankArasiBoslukX + getLocalHydraulicData().kilitliBlokX + getLocalHydraulicData().kilitliBlokAraBoslukX + getLocalHydraulicData().kampanaTankArasiBoslukX;
                        yV += getLocalHydraulicData().kilitliBlokTankArasiBoslukY + getLocalHydraulicData().kilitliBlokY + getLocalHydraulicData().kilitliBlokYOn;
                        yK += getLocalHydraulicData().kampanaTankArasiBoslukY + getLocalHydraulicData().kampanaBoslukYOn;
                    }
                }
            } else {
                //Hidrolik Kilit Yok
                if(kompanzasyonDurumu.equals("Var")) {
                    //kompanzasyon Var
                    if(secilenValfTipi.equals("Kompanzasyon || İnişte Tek Hız")) {
                        x += getLocalHydraulicData().tekHizTankArasiBoslukX + getLocalHydraulicData().tekHizBlokX + getLocalHydraulicData().tekHizAraBoslukX + getLocalHydraulicData().kampanaTankArasiBoslukX;
                        yV += getLocalHydraulicData().tekHizTankArasiBoslukY + getLocalHydraulicData().tekHizBlokY + getLocalHydraulicData().tekHizYOn;
                        yK += getLocalHydraulicData().kampanaTankArasiBoslukY + getLocalHydraulicData().kampanaBoslukYOn;
                    }
                } else {
                    //Kompanzasyon Yok
                    if(secilenValfTipi.equals("İnişte Tek Hız")) {
                        x += getLocalHydraulicData().tekHizTankArasiBoslukX + getLocalHydraulicData().tekHizBlokX + getLocalHydraulicData().tekHizAraBoslukX + getLocalHydraulicData().kampanaTankArasiBoslukX;
                        yV += getLocalHydraulicData().tekHizTankArasiBoslukY + getLocalHydraulicData().tekHizBlokY + getLocalHydraulicData().tekHizYOn;
                        yK += getLocalHydraulicData().kampanaTankArasiBoslukY + getLocalHydraulicData().kampanaBoslukYOn;
                    } else if(secilenValfTipi.equals("İnişte Çift Hız")) {
                        x += getLocalHydraulicData().ciftHizTankArasiBoslukX + getLocalHydraulicData().ciftHizBlokX + getLocalHydraulicData().ciftHizAraBoslukX + getLocalHydraulicData().kampanaTankArasiBoslukX;
                        yV += getLocalHydraulicData().ciftHizTankArasiBoslukY + getLocalHydraulicData().ciftHizBlokY + getLocalHydraulicData().ciftHizYOn;
                        yK += getLocalHydraulicData().kampanaTankArasiBoslukY + getLocalHydraulicData().kampanaBoslukYOn;
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
            h = getLocalHydraulicData().defaultHeight;

            String veri = SystemVariables.getLocalHydraulicData().motorYukseklikMap.get(motorComboBox.getSelectionModel().getSelectedItem());
            String sayiKismi = veri.replaceAll("[^0-9]", "");
            motorYukseklik = Integer.parseInt(sayiKismi);

            eskiX = x;
            eskiY = y;
            eskiH = h;
        }

        calculationResultX = x;
        calculationResultY = y;
        calculationResultH = h;
        tabloGuncelle();

        Kabin finalTank = null;
        for(Kabin selectedTank : SystemVariables.getLocalHydraulicData().classicCabins) {
            int selectedTankKabinHacim = selectedTank.getKabinHacim();
            int selectedTankKabinX = selectedTank.getKabinGecisX();
            int selectedTankKabinY = selectedTank.getKabinGecisY();
            int selectedTankKabinH = selectedTank.getKabinGecisH();

            if(selectedTankKabinX >= x && selectedTankKabinY >= y && selectedTankKabinH >= h) {
                int kayipLitre = getLocalHydraulicData().kayipLitre;
                int hacimMinVal = selectedTankKabinHacim - kayipLitre;
                int hacimMaxVal = selectedTankKabinHacim + kayipLitre;
                System.out.println("Girilen Deger: " + girilenTankKapasitesiMiktari
                        + "Min Val: " + hacimMinVal
                        + " Max Val: " + hacimMaxVal
                        + " Secilen Deger: " + selectedTankKabinHacim);
                if (girilenTankKapasitesiMiktari >= hacimMinVal && girilenTankKapasitesiMiktari <= hacimMaxVal) {
                    finalTank = selectedTank;
                    System.out.println("Tank seçildi: " + selectedTankKabinHacim);
                    break;
                } else if(selectedTankKabinHacim > girilenTankKapasitesiMiktari) {
                    finalTank = selectedTank;
                    System.out.println("Tank seçildi: " + selectedTankKabinHacim);
                    break;
                } else {
                    System.out.println("Girilen değer aralığa uymuyor.");
                }
            }
        }

        if (finalTank != null) {
            x = finalTank.getKabinGecisX();
            y = finalTank.getKabinGecisY();
            h = finalTank.getKabinGecisH();
            atananHacim = finalTank.getKabinHacim();
            atananHT = finalTank.getTankName();
            if(finalTank.getKabinDisH() < (motorYukseklik + h)) {
                for(Kabin selectedTank : getLocalHydraulicData().classicCabins) {
                    int kabinYukseklik = selectedTank.getKabinDisH();
                    //System.out.println("Kabin Yükseklik: " + kabinYukseklik + "\nÖnceden Seçilen Kabin Yükseklik: " + finalTank.getKabinH());

                    int tempYukseklik = finalTank.getKabinDisH() + motorYukseklik;
                    System.out.println("Yukseklik Hesaplama: " + tempYukseklik + "   Kabin Yükseklik: " + kabinYukseklik);

                    if(kabinYukseklik >= tempYukseklik) {
                        finalTank = selectedTank;
                        System.out.println("Yeni Kabin: " + finalTank.getKabinName());
                        break;
                    }
                }
            }
        }

        String atananKabin = finalTank.getKabinName();
        String disOlculer = finalTank.getKabinOlculeri();
        String gecisOlculeri = finalTank.getGecisOlculeri();
        String tankOlculeri = finalTank.getTankOlculeri();

        //kullanilacakKabin.setText("Kullanmanız Gereken Kabin: \n\t\t\t\t\t\t" + atananKabin + "\n\t\t\tGeçiş Ölçüleri: " + gecisOlculeri + " (x, y, h)");
        kullanilacakKabin.setText("\t\t\t\t\t\t" + atananKabin + "\n\t\t\tDış Ölçüler: " + disOlculer + " (x, y, h)" + "\n\t\t\tGeçiş Ölçüleri: " + gecisOlculeri + " (x, y, h)" + "\n\t\t\tTank Ölçüleri: " + tankOlculeri + " (x, y, h)");
        atananKabinFinal = atananKabin;
        gecisOlculeriFinal = gecisOlculeri;
        //int secilenMotorIndeks = motorComboBox.getSelectionModel().getSelectedIndex();
        //int motorYukseklikDegeri = Integer.parseInt(motorYukseklikVerileri.get(secilenMotorIndeks));

        logCalculation(yV, yK, eskiX, eskiY, eskiH, x, y, h, atananHacim, atananKabin, gecisOlculeri);

        finalValues.add(x);
        finalValues.add(y);
        finalValues.add(h);
        finalValues.add(atananHacim);
        return finalValues;
    }

    private void calculationSogutma(int x, int y, int h, ArrayList<Integer> finalValues, String atananKabinFinal, String gecisOlculeriFinal, int atananHacim) {
        x = 1000;
        y = 600;
        h = 350;

        atananHT = "HT SOĞUTMA";
        String atananKabin = "KD SOĞUTMA";
        String gecisOlculeri = "1000x600x350";
        //kullanilacakKabin.setText("Kullanmanız Gereken Kabin: \n\t\t\t\t\t\t" + atananKabin + "\n\t\t\tGeçiş Ölçüleri: " + gecisOlculeri + " (x, y, h)");
        kullanilacakKabin.setText("\t\t\t\t\t\t" + atananKabin + "\n\t\t\tGeçiş Ölçüleri: " + gecisOlculeri + " (x, y, h)");
        atananKabinFinal = atananKabin;
        gecisOlculeriFinal = gecisOlculeri;

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

    private void logCalculation(int yV, int yK, int eskiX, int eskiY, int eskiH, int x, int y, int h, int atananHacim, String atananKabin, String gecisOlculeri) {
        System.out.println("--------Hesaplama Bitti--------");
        System.out.println("------------(Sonuç)------------");
        System.out.println("yV: " + yV);
        System.out.println("yK: " + yK);
        System.out.println("Hesaplanan X: " + eskiX);
        System.out.println("Hesaplanan Y: " + eskiY);
        System.out.println("Hesaplanan h: " + eskiH);
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
            motorComboBox.getItems().addAll(SystemVariables.getLocalHydraulicData().motorMap.get("0"));
        } else if(componentName.equals("pompa")) {
            pompaComboBox.setDisable(false);
            pompaComboBox.getItems().clear();
            if(Objects.equals(secilenUniteTipi, "Hidros")) {
                pompaComboBox.getItems().addAll(SystemVariables.getLocalHydraulicData().pumpMap.get("0"));
            } else if(Objects.equals(secilenUniteTipi, "Klasik")) {
                pompaComboBox.getItems().addAll(SystemVariables.getLocalHydraulicData().pumpMap.get("1"));
           } else {
                LinkedList<String> list1 = SystemVariables.getLocalHydraulicData().pumpMap.get("0");
                LinkedList<String> list2 = SystemVariables.getLocalHydraulicData().pumpMap.get("1");
                LinkedList<String> combinedList = new LinkedList<>();
                combinedList.addAll(list1);
                combinedList.addAll(list2);

                pompaComboBox.getItems().addAll(combinedList);
            }
        } else if(componentName.equals("valfTipi")) {
            valfTipiComboBox.setDisable(false);
            valfTipiComboBox.getItems().clear();
            if(valfTipiStat == 1) {
                valfTipiComboBox.getItems().addAll(SystemVariables.getLocalHydraulicData().valveTypeMap.get("0")); //İnişte Tek Hız, İnişte Çift Hız
            } else if(valfTipiStat == 0) {
                valfTipiComboBox.getItems().addAll(SystemVariables.getLocalHydraulicData().valveTypeMap.get("1")); //Kompanzasyon || İnişte Tek Hız
            } else {
                valfTipiComboBox.getItems().addAll(SystemVariables.getLocalHydraulicData().valveTypeMap.get("2")); //İnişte Çift Hız
            }
        } else if(componentName.equals("hidrolikKilit")) {
            hidrolikKilitComboBox.setDisable(false);
            hidrolikKilitComboBox.getItems().clear();
            hidrolikKilitComboBox.getItems().addAll(SystemVariables.getLocalHydraulicData().hydraulicLockMap.get("0"));
        } else if(componentName.equals("sogutma")) {
            sogutmaComboBox.setDisable(false);
            sogutmaComboBox.getItems().clear();
            sogutmaComboBox.getItems().addAll(SystemVariables.getLocalHydraulicData().coolingMap.get("0"));
        } else if(componentName.equals("kilitMotor")) {
            kilitMotorComboBox.setDisable(false);
            kilitMotorComboBox.getItems().clear();
            kilitMotorComboBox.getItems().addAll(SystemVariables.getLocalHydraulicData().lockMotorMap.get("0"));
        } else if(componentName.equals("kilitPompa")) {
            kilitPompaComboBox.setDisable(false);
            kilitPompaComboBox.getItems().clear();
            kilitPompaComboBox.getItems().addAll(SystemVariables.getLocalHydraulicData().lockPumpMap.get("0"));
        } else if(componentName.equals("kompanzasyon")) {
            kompanzasyonComboBox.setDisable(false);
            kompanzasyonComboBox.getItems().clear();
            kompanzasyonComboBox.getItems().addAll(SystemVariables.getLocalHydraulicData().compensationMap.get("0"));
        }
    }

    private void enableSonucSection() {
        kullanilacakKabinMainText.setVisible(true);
        genislikSonucText.setVisible(true);
        yukseklikSonucText.setVisible(true);
        derinlikSonucText.setVisible(true);
        hacimText.setVisible(true);
        kullanilacakKabin.setVisible(true);
    }

    private void comboBoxListener() {
        siparisNumarasi.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!siparisNumarasi.getText().isEmpty()) {
                girilenSiparisNumarasi = newValue;
            }

            sonucAnaLabelTxt.setText(girilenSiparisNumarasi + " Numaralı Sipariş");
            dataInit("motor", null);

            if(girilenSiparisNumarasi != null) {
                tabloGuncelle();
            }
        });

        motorComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if(!motorComboBox.getItems().isEmpty() && newValue != null) {
                secilenMotor = newValue;
                secilenKampana = Integer.parseInt(SystemVariables.getLocalHydraulicData().motorKampanaMap.get(motorComboBox.getSelectionModel().getSelectedItem().toString()).replace(" mm", ""));

                dataInit("sogutma", null);

                if(secilenMotor != null) {
                    tabloGuncelle();
                }
            }
        });

        sogutmaComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            secilenSogutmaDurumu = sogutmaComboBox.getValue();

            if(secilenSogutmaDurumu != null && secilenHidrolikKilitDurumu != null && secilenPompa != null && kompanzasyonDurumu != null) {
                initValfValues();
            }

            if(secilenSogutmaDurumu != null) {
                tabloGuncelle();
            }
        });

        hidrolikKilitComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            secilenHidrolikKilitDurumu = newValue;

            if(secilenSogutmaDurumu != null && secilenHidrolikKilitDurumu != null && secilenPompa != null && kompanzasyonDurumu != null) {
                initValfValues();
            }

            if(secilenPompa != null) {
                tabloGuncelle();
            }
        });

        pompaComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            secilenPompa = newValue;
            if(oldValue != null && secilenPompa != null) {
                double oldSecilenPompaVal = Utils.string2Double(oldValue);
                secilenPompaVal = Utils.string2Double(secilenPompa);

                if(secilenSogutmaDurumu != null && secilenHidrolikKilitDurumu != null && secilenPompa != null && kompanzasyonDurumu != null) {
                    initValfValues();
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

            if(girilenTankKapasitesiMiktari != 0) {
                tabloGuncelle();
            }
        });

        kompanzasyonComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            kompanzasyonDurumu = newValue;

            if(secilenSogutmaDurumu != null && secilenHidrolikKilitDurumu != null && secilenPompa != null && kompanzasyonDurumu != null) {
                initValfValues();
            }

            if(secilenValfTipi != null) {
                tabloGuncelle();
            }
        });

        valfTipiComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            secilenValfTipi = newValue;

            if(secilenSogutmaDurumu.equals("Yok") && secilenHidrolikKilitDurumu.equals("Var") && kompanzasyonDurumu.equals("Var")) {
                dataInit("kilitMotor", 0);
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
            if(secilenKilitPompa != null) {
                tabloGuncelle();
            }
        });
    }

    private void verileriSifirla() {
        //Kullanıcı Input Side
        siparisNumarasi.clear();
        girilenSiparisNumarasi = null;
        siparisNumarasi.setPromptText("x Numaralı Sipariş");

        motorComboBox.setDisable(true);
        if(secilenMotor != null) {
            secilenMotor = null;
            secilenKampana = 0;
        }
        motorComboBox.setPromptText("Motor");

        sogutmaComboBox.setDisable(true);
        if(secilenSogutmaDurumu != null) {
            secilenSogutmaDurumu = null;
        }
        sogutmaComboBox.setPromptText("Soğutma");

        hidrolikKilitComboBox.setDisable(true);
        if(secilenHidrolikKilitDurumu != null) {
            secilenHidrolikKilitDurumu = null;
            hidrolikKilitStat = false;
        }
        hidrolikKilitComboBox.setPromptText("Hidrolik Kilit");

        pompaComboBox.setDisable(true);
        if(secilenPompa != null) {
            secilenPompa = null;
            secilenPompaVal = 0;
        }
        pompaComboBox.setPromptText("Pompa");

        tankKapasitesiTextField.clear();
        tankKapasitesiTextField.setDisable(true);
        if(girilenTankKapasitesiMiktari != 0) {
            girilenTankKapasitesiMiktari = 0;
        }
        tankKapasitesiTextField.setPromptText("Gerekli Yağ Miktarı (L)");

        kompanzasyonComboBox.setDisable(true);
        if(kompanzasyonDurumu != null) {
            kompanzasyonComboBox.getSelectionModel().clearSelection();
            kompanzasyonDurumu = null;
        }
        kompanzasyonComboBox.setPromptText("Kompanzasyon");

        valfTipiComboBox.setDisable(true);
        if(secilenValfTipi != null) {
            secilenValfTipi = null;
        }
        valfTipiComboBox.setPromptText("Valf Tipi");

        kilitMotorComboBox.setDisable(true);
        if(secilenKilitMotor != null) {
            secilenKilitMotor = null;
        }
        kilitMotorComboBox.setPromptText("Kilit Motor");

        kilitPompaComboBox.setDisable(true);
        if(secilenKilitPompa != null) {
            secilenKilitPompa = null;
        }
        kilitPompaComboBox.setPromptText("Kilit Pompa");

        sonucEkraniTemizle();
        sonucTablo.getItems().clear();

        sonucKapakImage.setImage(null);
        parcaListesiButton.setDisable(true);
        exportButton.setDisable(true);
        kullanilacakKabin.setVisible(false);
        sonucAnaLabelTxt.setText("");
        sonucTankGorsel.setImage(null);
        kullanilacakKabinMainText.setVisible(false);

        imageTextDisable();
    }

    private void imageTextDisable() {
        for(Text text : sonucTexts) {
            hydraulicUnitBox.getChildren().remove(text);
        }
        sonucTexts.clear();
    }

    private void imageTextEnable(int x, int y, String calculatedImage) {
        sonucTexts.clear();

        if(calculatedImage.equals("cift_hiz")) {
            addTextToList("X: " + x + " mm", 672, 445, 0, 14, Color.WHITE, null);
            addTextToList("Y: " + y + " mm", 475, 318, 90, 14, Color.WHITE, null);

            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicCiftHizTexts.get("0")), 560, 225, 0, 10, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicCiftHizTexts.get("0")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicCiftHizTexts.get("1")), 520, 290, 90, 10, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicCiftHizTexts.get("1")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicCiftHizTexts.get("2")), 510, 405, 90, 10, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicCiftHizTexts.get("2")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicCiftHizTexts.get("3")), 550, 420, 0, 10, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicCiftHizTexts.get("3")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicCiftHizTexts.get("4")), 575, 395, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicCiftHizTexts.get("4")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicCiftHizTexts.get("5")), 550, 370, -45, 10, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicCiftHizTexts.get("5")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicCiftHizTexts.get("6")), 650, 370, -45, 10, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicCiftHizTexts.get("6")));
            addTextToList(secilenValfTipi, 570, 312, 0, 10, Color.WHITE, null);
            addTextToList(getKampanaText(), 720, 300, 0, 12, Color.WHITE, null);
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicCiftHizTexts.get("7")), 775, 228, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicCiftHizTexts.get("7")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicCiftHizTexts.get("8")), 830, 290, 90, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicCiftHizTexts.get("8")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicCiftHizTexts.get("9")), 810, 390, 30, 10, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicCiftHizTexts.get("9")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicCiftHizTexts.get("10")), 795, 420, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicCiftHizTexts.get("10")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicCiftHizTexts.get("11")), 840, 383, 90, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicCiftHizTexts.get("11")));
        } else if (calculatedImage.equals("kilit_ayri_cift_hiz")) {
            addTextToList("X: " + x + " mm", 672, 448, 0, 14, Color.WHITE, null);
            addTextToList("Y: " + y + " mm", 475, 318, 90, 14, Color.WHITE, null);

            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicKilitAyriCiftHizTexts.get("0")), 565, 215, 0, 10, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicKilitAyriCiftHizTexts.get("0")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicKilitAyriCiftHizTexts.get("1")), 530, 255, 0, 10, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicKilitAyriCiftHizTexts.get("1")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicKilitAyriCiftHizTexts.get("2")), 595, 245, 0, 10, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicKilitAyriCiftHizTexts.get("2")));
            addTextToList(getKampanaText(), 745, 260, 0, 10.5, Color.WHITE, null);
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicKilitAyriCiftHizTexts.get("3")), 785, 220, 0, 10, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicKilitAyriCiftHizTexts.get("3")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicKilitAyriCiftHizTexts.get("4")), 830, 263, 0, 10, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicKilitAyriCiftHizTexts.get("4")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicKilitAyriCiftHizTexts.get("5")), 595, 315, -90, 10, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicKilitAyriCiftHizTexts.get("5")));
            addTextToList(secilenValfTipi, 570, 395, 0, 10, Color.WHITE, null);
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicKilitAyriCiftHizTexts.get("6")), 520, 415, 90, 10, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicKilitAyriCiftHizTexts.get("6")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicKilitAyriCiftHizTexts.get("7")), 640, 415, 90, 10, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicKilitAyriCiftHizTexts.get("7")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicKilitAyriCiftHizTexts.get("8")), 675, 385, -30, 10, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicKilitAyriCiftHizTexts.get("8")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicKilitAyriCiftHizTexts.get("9")), 740, 385, -30, 10, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicKilitAyriCiftHizTexts.get("9")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicKilitAyriCiftHizTexts.get("10")), 595, 427, 0, 10, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicKilitAyriCiftHizTexts.get("10")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicKilitAyriCiftHizTexts.get("11")), 675, 427, 0, 10, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicKilitAyriCiftHizTexts.get("11")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicKilitAyriCiftHizTexts.get("12")), 685, 408, 0, 10, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicKilitAyriCiftHizTexts.get("12")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicKilitAyriCiftHizTexts.get("13")), 795, 390, 30, 10, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicKilitAyriCiftHizTexts.get("13")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicKilitAyriCiftHizTexts.get("14")), 785, 425, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicKilitAyriCiftHizTexts.get("14")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicKilitAyriCiftHizTexts.get("15")), 835, 383, 90, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicKilitAyriCiftHizTexts.get("15")));
        } else if(calculatedImage.equals("kilit_ayri_tek_hiz")) {
            addTextToList("X: " + x + " mm", 672, 448, 0, 14, Color.WHITE, null);
            addTextToList("Y: " + y + " mm", 475, 318, 90, 14, Color.WHITE, null);

            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicKilitAyriTekHizTexts.get("0")), 565, 215, 0, 10, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicKilitAyriTekHizTexts.get("0")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicKilitAyriTekHizTexts.get("1")), 530, 255, 0, 10, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicKilitAyriTekHizTexts.get("1")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicKilitAyriTekHizTexts.get("2")), 595, 245, 0, 10, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicKilitAyriTekHizTexts.get("2")));
            addTextToList(getKampanaText(), 745, 275, 0, 10.5, Color.WHITE, null);
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicKilitAyriTekHizTexts.get("3")), 785, 220, 0, 10, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicKilitAyriTekHizTexts.get("3")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicKilitAyriTekHizTexts.get("4")), 830, 263, 0, 10, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicKilitAyriTekHizTexts.get("4")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicKilitAyriTekHizTexts.get("5")), 580, 315, -90, 10, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicKilitAyriTekHizTexts.get("5")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicKilitAyriTekHizTexts.get("6")), 525, 390, 0, 10, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicKilitAyriTekHizTexts.get("6")));
            addTextToList(secilenValfTipi, 615, 365, 0, 10, Color.WHITE, null);
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicKilitAyriTekHizTexts.get("7")), 555, 427, 0, 10, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicKilitAyriTekHizTexts.get("7")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicKilitAyriTekHizTexts.get("8")), 675, 427, 0, 10, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicKilitAyriTekHizTexts.get("8")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicKilitAyriTekHizTexts.get("9")), 620, 410, 0, 10, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicKilitAyriTekHizTexts.get("9")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicKilitAyriTekHizTexts.get("10")), 690, 410, 0, 10, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicKilitAyriTekHizTexts.get("10")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicKilitAyriTekHizTexts.get("11")), 675, 385, -30, 10, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicKilitAyriTekHizTexts.get("11")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicKilitAyriTekHizTexts.get("12")), 740, 385, -30, 10, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicKilitAyriTekHizTexts.get("12")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicKilitAyriTekHizTexts.get("13")), 795, 390, 30, 10, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicKilitAyriTekHizTexts.get("13")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicKilitAyriTekHizTexts.get("14")), 785, 425, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicKilitAyriTekHizTexts.get("14")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicKilitAyriTekHizTexts.get("15")), 835, 383, 90, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicKilitAyriTekHizTexts.get("15")));
        } else if(calculatedImage.equals("kilitli_blok")) {
            addTextToList("X: " + x + " mm", 672, 445, 0, 14, Color.WHITE, null);
            addTextToList("Y: " + y + " mm", 475, 318, 90, 14, Color.WHITE, null);

            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicKilitliBlokTexts.get("0")), 550, 225, 0, 10, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicKilitliBlokTexts.get("0")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicKilitliBlokTexts.get("1")), 520, 285, 90, 10, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicKilitliBlokTexts.get("1")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicKilitliBlokTexts.get("2")), 560, 403, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicKilitliBlokTexts.get("2")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicKilitliBlokTexts.get("3")), 655, 420, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicKilitliBlokTexts.get("3")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicKilitliBlokTexts.get("4")), 661, 385, -45, 10, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicKilitliBlokTexts.get("4")));
            addTextToList(secilenValfTipi, 565, 305, 0, 10, Color.WHITE, null);
            addTextToList(getKampanaText(), 725, 300, 0, 12, Color.WHITE, null);
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicKilitliBlokTexts.get("5")), 775, 228, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicKilitliBlokTexts.get("5")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicKilitliBlokTexts.get("6")), 830, 290, 90, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicKilitliBlokTexts.get("6")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicKilitliBlokTexts.get("7")), 810, 390, 30, 10, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicKilitliBlokTexts.get("7")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicKilitliBlokTexts.get("8")), 795, 420, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicKilitliBlokTexts.get("8")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicKilitliBlokTexts.get("9")), 840, 383, 90, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicKilitliBlokTexts.get("9")));
        } else if(calculatedImage.equals("tek_hiz_kompanzasyon_arti_tek_hiz")) {
            addTextToList("X: " + x + " mm", 672, 445, 0, 14, Color.WHITE, null);
            addTextToList("Y: " + y + " mm", 475, 318, 90, 14, Color.WHITE, null);

            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicTekHizKompanzasyonArtiTekHizTexts.get("0")), 565, 225, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicTekHizKompanzasyonArtiTekHizTexts.get("0")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicTekHizKompanzasyonArtiTekHizTexts.get("1")), 520, 300, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicTekHizKompanzasyonArtiTekHizTexts.get("1")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicTekHizKompanzasyonArtiTekHizTexts.get("2")), 510, 405, 90, 10, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicTekHizKompanzasyonArtiTekHizTexts.get("2")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicTekHizKompanzasyonArtiTekHizTexts.get("3")), 580, 395, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicTekHizKompanzasyonArtiTekHizTexts.get("3")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicTekHizKompanzasyonArtiTekHizTexts.get("4")), 545, 415, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicTekHizKompanzasyonArtiTekHizTexts.get("4")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicTekHizKompanzasyonArtiTekHizTexts.get("5")), 550, 370, -45, 10, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicTekHizKompanzasyonArtiTekHizTexts.get("5")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicTekHizKompanzasyonArtiTekHizTexts.get("6")), 660, 366, -45, 10, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicTekHizKompanzasyonArtiTekHizTexts.get("6")));
            addTextToList(secilenValfTipi, 570, 340, 0, 10, Color.WHITE, null);
            addTextToList(getKampanaText(), 720, 300, 0, 12, Color.WHITE, null);
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicTekHizKompanzasyonArtiTekHizTexts.get("7")), 775, 228, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicTekHizKompanzasyonArtiTekHizTexts.get("7")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicTekHizKompanzasyonArtiTekHizTexts.get("8")), 830, 290, 90, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicTekHizKompanzasyonArtiTekHizTexts.get("8")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicTekHizKompanzasyonArtiTekHizTexts.get("9")), 810, 390, 30, 10, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicTekHizKompanzasyonArtiTekHizTexts.get("9")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicTekHizKompanzasyonArtiTekHizTexts.get("10")), 795, 420, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicTekHizKompanzasyonArtiTekHizTexts.get("10")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicTekHizKompanzasyonArtiTekHizTexts.get("11")), 840, 383, 90, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicTekHizKompanzasyonArtiTekHizTexts.get("11")));
        } else if(calculatedImage.equals("sogutma_kilitsiz_cift_hiz")) {
            addTextToList("X: " + x + " mm", 672, 442, 0, 14, Color.WHITE, null);
            addTextToList("Y: " + y + " mm", 470, 318, 90, 14, Color.WHITE, null);

            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("0")), 530, 330, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("0")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("1")), 507, 372, 90, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("1")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("2")), 565, 420, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("2")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("3")), 590, 377, -90, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("3")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("4")), 605, 323, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("4")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("5")), 560, 280, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("5")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("6")), 612, 340, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("6")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("7")), 615, 305, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("7")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("8")), 615, 255, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("8")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("9")), 665, 420, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("9")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("10")), 730, 420, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("10")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("11")), 810, 420, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("11")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("12")), 843, 382, 90, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("12")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("13")), 720, 365, -90, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("13")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("14")), 736, 328, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("14")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("15")), 738, 400, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("15")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("16")), 780, 295, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("16")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("17")), 680, 250, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("17")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("18")), 710, 280, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("18")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("19")), 810, 280, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("19")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("20")), 785, 220, 0, 9, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("20")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("21")), 645, 226, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("21")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("22")), 815, 230, 90, 9, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("22")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("23")), 845, 240, 90, 9, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("23")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("24")), 815, 263, 0, 9, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("24")));
            addTextToList(secilenValfTipi, 535, 370, 0, 10, Color.WHITE, null);
            addTextToList(getKampanaText(), 770, 355, 0, 9, Color.WHITE, null);
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("25")), 630, 365, 0, 8, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizCiftHizTexts.get("25")));
        } else if(calculatedImage.equals("sogutma_kilitli_cift_hiz")) {
            addTextToList("X: " + x + " mm", 672, 442, 0, 14, Color.WHITE, null);
            addTextToList("Y: " + y + " mm", 470, 318, 90, 14, Color.WHITE, null);

            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("0")), 548, 230, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("0")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("1")), 507, 290, 90, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("1")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("2")), 530, 330, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("2")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("3")), 507, 372, 90, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("3")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("4")), 565, 420, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("4")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("5")), 590, 377, -90, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("5")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("6")), 605, 323, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("6")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("7")), 560, 280, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("7")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("8")), 612, 340, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("8")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("9")), 615, 305, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("9")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("10")), 615, 255, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("10")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("11")), 665, 420, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("11")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("12")), 730, 420, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("12")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("13")), 810, 420, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("13")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("14")), 843, 382, 90, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("14")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("15")), 720, 365, -90, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("15")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("16")), 736, 328, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("16")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("17")), 738, 400, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("17")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("18")), 780, 295, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("18")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("19")), 680, 250, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("19")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("20")), 710, 280, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("20")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("21")), 810, 280, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("21")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("22")), 785, 220, 0, 9, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("22")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("23")), 645, 226, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("23")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("24")), 815, 230, 90, 9, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("24")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("25")), 845, 240, 90, 9, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("25")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("26")), 815, 263, 0, 9, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("26")));
            addTextToList(secilenValfTipi, 535, 370, 0, 10, Color.WHITE, null);
            addTextToList(getKampanaText(), 770, 355, 0, 9, Color.WHITE, null);
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("27")), 630, 365, 0, 8, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliCiftHizTexts.get("27")));
        } else if(calculatedImage.equals("sogutma_kilitsiz_tek_hiz")) {
            addTextToList("X: " + x + " mm", 672, 442, 0, 14, Color.WHITE, null);
            addTextToList("Y: " + y + " mm", 470, 318, 90, 14, Color.WHITE, null);
            
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("0")), 530, 330, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("0")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("1")), 507, 372, 90, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("1")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("2")), 565, 420, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("2")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("3")), 590, 377, -90, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("3")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("4")), 605, 323, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("4")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("5")), 560, 280, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("5")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("6")), 612, 340, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("6")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("7")), 615, 305, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("7")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("8")), 615, 255, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("8")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("9")), 665, 420, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("9")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("10")), 730, 420, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("10")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("11")), 810, 420, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("11")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("12")), 843, 382, 90, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("12")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("13")), 720, 365, -90, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("13")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("14")), 736, 328, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("14")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("15")), 738, 400, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("15")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("16")), 780, 295, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("16")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("17")), 680, 250, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("17")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("18")), 710, 280, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("18")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("19")), 810, 280, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("19")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("20")), 785, 220, 0, 9, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("20")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("21")), 645, 226, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("21")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("22")), 815, 230, 90, 9, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("22")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("23")), 845, 240, 90, 9, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("23")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("24")), 815, 263, 0, 9, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("24")));
            addTextToList(secilenValfTipi, 535, 355, 0, 10, Color.WHITE, null);
            addTextToList(getKampanaText(), 770, 355, 0, 9, Color.WHITE, null);
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("25")), 630, 365, 0, 8, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitsizTekHizTexts.get("25")));
        } else if (calculatedImage.equals("sogutma_kilitli_tek_hiz")) {
            addTextToList("X: " + x + " mm", 672, 442, 0, 14, Color.WHITE, null);
            addTextToList("Y: " + y + " mm", 470, 318, 90, 14, Color.WHITE, null);

            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("0")), 548, 230, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("0")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("1")), 507, 290, 90, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("1")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("2")), 530, 330, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("2")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("3")), 507, 372, 90, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("3")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("4")), 565, 420, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("4")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("5")), 590, 377, -90, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("5")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("6")), 605, 323, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("6")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("7")), 560, 280, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("7")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("8")), 612, 340, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("8")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("9")), 615, 305, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("9")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("10")), 615, 255, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("10")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("11")), 665, 420, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("11")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("12")), 730, 420, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("12")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("13")), 810, 420, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("13")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("14")), 843, 382, 90, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("14")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("15")), 720, 365, -90, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("15")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("16")), 736, 328, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("16")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("17")), 738, 400, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("17")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("18")), 780, 295, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("18")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("19")), 680, 250, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("19")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("20")), 710, 280, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("20")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("21")), 810, 280, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("21")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("22")), 785, 220, 0, 9, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("22")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("23")), 645, 226, 0, 11, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("23")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("24")), 815, 230, 90, 9, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("24")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("25")), 845, 240, 90, 9, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("25")));
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("26")), 815, 263, 0, 9, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("26")));
            addTextToList(secilenValfTipi, 535, 355, 0, 10, Color.WHITE, null);
            addTextToList(getKampanaText(), 770, 355, 0, 9, Color.WHITE, null);
            addTextToList(getLocalHydraulicData().getValue(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("27")), 630, 365, 0, 8, Color.WHITE, getLocalHydraulicData().getKey(getLocalHydraulicData().schematicSogutmaKilitliTekHizTexts.get("27")));
        }

        for (Text text : sonucTexts) {
            hydraulicUnitBox.getChildren().add(text);
        }
    }

    private void addTextToList(String content, int x, int y, int rotate, double fontSize, Color color, String tooltipText) {
        Text text = new Text(content);
        text.setX(x);
        text.setY(y);
        text.setRotate(rotate);
        text.setFill(color);
        text.setFont(new Font(fontSize));
        text.setTextAlignment(TextAlignment.CENTER);

        if(tooltipText != null) {
            tooltipText = "Key: " + tooltipText + "\n \nÜstteki anahtarı schematic_texts.yml'de aratarak değeri güncelleyebilirsiniz.";
        } else {
            tooltipText = "Hesaplama sonucu üretilen değerdir.";
        }

        Tooltip tooltip = new Tooltip(tooltipText);
        tooltip.setShowDelay(Duration.seconds(0.1));
        tooltip.setStyle("-fx-font-size: 14px;");
        Tooltip.install(text, tooltip);

        text.setOnMouseEntered(e -> {
            text.setUnderline(true);
        });

        text.setOnMouseExited(e -> {
            text.setUnderline(false);
        });

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

        data = new TableData("Hesaplanan X", String.valueOf(calculationResultX));
        sonucTablo.getItems().add(data);

        data = new TableData("Hesaplanan Y", String.valueOf(calculationResultY));
        sonucTablo.getItems().add(data);

        data = new TableData("Hesaplanan H", String.valueOf(calculationResultH));
        sonucTablo.getItems().add(data);
    }

    private void tankGorselLoad() {
        Image image;

        if(secilenSogutmaDurumu.equals("Var")) {
            if(secilenHidrolikKilitDurumu.equals("Var")) {
                if(secilenValfTipi.equals("İnişte Çift Hız")) {
                    image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/data/hydraulicUnitData/cabins/classic/sogutma_kilit_cift_hiz.png")));
                    sonucTankGorsel.setImage(image);
                    yukseklikSonucText.setLayoutX(6.0);
                    genislikSonucText.setRotate(32.0);
                    genislikSonucText.setLayoutY(410.0);
                    derinlikSonucText.setRotate(-32.0);
                    derinlikSonucText.setLayoutX(197.0);
                    derinlikSonucText.setLayoutY(422.0);
                } else if(secilenValfTipi.contains("İnişte Tek Hız")) {
                    image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/data/hydraulicUnitData/cabins/classic/sogutma_kilit_tek_hiz.png")));
                    sonucTankGorsel.setImage(image);
                    yukseklikSonucText.setLayoutX(6.0);
                    genislikSonucText.setRotate(32.0);
                    genislikSonucText.setLayoutY(410.0);
                    derinlikSonucText.setRotate(-32.0);
                    derinlikSonucText.setLayoutX(197.0);
                    derinlikSonucText.setLayoutY(422.0);
                }
            } else {
                if(secilenValfTipi.equals("İnişte Çift Hız")) {
                    image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/data/hydraulicUnitData/cabins/classic/sogutma_cift_hiz.png")));
                    sonucTankGorsel.setImage(image);
                    yukseklikSonucText.setLayoutX(6.0);
                    genislikSonucText.setRotate(32.0);
                    genislikSonucText.setLayoutY(410.0);
                    derinlikSonucText.setRotate(-32.0);
                    derinlikSonucText.setLayoutX(197.0);
                    derinlikSonucText.setLayoutY(422.0);
                } else if(secilenValfTipi.contains("İnişte Tek Hız")) {
                    image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/data/hydraulicUnitData/cabins/classic/sogutma_tek_hiz.png")));
                    sonucTankGorsel.setImage(image);
                    yukseklikSonucText.setLayoutX(6.0);
                    genislikSonucText.setRotate(32.0);
                    genislikSonucText.setLayoutY(410.0);
                    derinlikSonucText.setRotate(-32.0);
                    derinlikSonucText.setLayoutX(197.0);
                    derinlikSonucText.setLayoutY(422.0);
                }
            }
        } else {
            if(secilenHidrolikKilitDurumu.equals("Var")) {
                if(secilenValfTipi.equals("İnişte Çift Hız")) {
                    image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/data/hydraulicUnitData/cabins/classic/kilit_ayri_cift_hiz.png")));
                    sonucTankGorsel.setImage(image);
                    yukseklikSonucText.setLayoutX(6.0);
                    genislikSonucText.setRotate(27.5);
                    genislikSonucText.setLayoutY(418.0);
                    derinlikSonucText.setRotate(-29.5);
                    derinlikSonucText.setLayoutX(199.0);
                    derinlikSonucText.setLayoutY(422.0);
                } else if(secilenValfTipi.equals("Kompanzasyon || İnişte Tek Hız")) {
                    image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/data/hydraulicUnitData/cabins/classic/kilit_ayri_kompanzasyon.png")));
                    sonucTankGorsel.setImage(image);
                    yukseklikSonucText.setLayoutX(6.0);
                    genislikSonucText.setRotate(27.5);
                    genislikSonucText.setLayoutY(418.0);
                    derinlikSonucText.setRotate(-29.5);
                    derinlikSonucText.setLayoutX(199.0);
                    derinlikSonucText.setLayoutY(422.0);
                } else if(secilenValfTipi.equals("İnişte Tek Hız")) {
                    image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/data/hydraulicUnitData/cabins/classic/kilit_ayri_tek_hiz.png")));
                    sonucTankGorsel.setImage(image);
                    yukseklikSonucText.setLayoutX(6.0);
                    genislikSonucText.setRotate(27.5);
                    genislikSonucText.setLayoutY(418.0);
                    derinlikSonucText.setRotate(-29.5);
                    derinlikSonucText.setLayoutX(199.0);
                    derinlikSonucText.setLayoutY(422.0);
                } else if(secilenValfTipi.equals("Kilitli Blok")) {
                    image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/data/hydraulicUnitData/cabins/classic/kilitli_blok.png")));
                    sonucTankGorsel.setImage(image);
                    yukseklikSonucText.setLayoutX(6.0);
                    genislikSonucText.setRotate(27.5);
                    genislikSonucText.setLayoutY(418.0);
                    derinlikSonucText.setRotate(-29.5);
                    derinlikSonucText.setLayoutX(199.0);
                    derinlikSonucText.setLayoutY(422.0);
                }
            } else {
                if(secilenValfTipi.equals("İnişte Çift Hız")) {
                    image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/data/hydraulicUnitData/cabins/classic/cift_hiz.png")));
                    sonucTankGorsel.setImage(image);
                    yukseklikSonucText.setLayoutX(6.0);
                    genislikSonucText.setRotate(27.5);
                    genislikSonucText.setLayoutY(418.0);
                    derinlikSonucText.setRotate(-29.5);
                    derinlikSonucText.setLayoutX(199.0);
                    derinlikSonucText.setLayoutY(422.0);
                } else if(secilenValfTipi.equals("İnişte Tek Hız")) {
                    image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/data/hydraulicUnitData/cabins/classic/tek_hiz.png")));
                    sonucTankGorsel.setImage(image);
                    yukseklikSonucText.setLayoutX(6.0);
                    genislikSonucText.setRotate(27.5);
                    genislikSonucText.setLayoutY(418.0);
                    derinlikSonucText.setRotate(-29.5);
                    derinlikSonucText.setLayoutX(199.0);
                    derinlikSonucText.setLayoutY(422.0);
                } else if(secilenValfTipi.equals("Kompanzasyon || İnişte Tek Hız")) {
                    image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/data/hydraulicUnitData/cabins/classic/tek_hiz_kompanzasyon.png")));
                    sonucTankGorsel.setImage(image);
                    yukseklikSonucText.setLayoutX(6.0);
                    genislikSonucText.setRotate(27.5);
                    genislikSonucText.setLayoutY(418.0);
                    derinlikSonucText.setRotate(-29.5);
                    derinlikSonucText.setLayoutX(199.0);
                    derinlikSonucText.setLayoutY(422.0);
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
            sonucKapakImage.setImage(new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream(reverseImagePath))));
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
            sonucKapakImage.setImage(new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream(imagePath))));
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

    private void initValfValues() {
        valfTipiComboBox.getItems().clear();

        if(secilenSogutmaDurumu.equals("Var")) {
            if(secilenHidrolikKilitDurumu.equals("Var")) {
                if(kompanzasyonDurumu.equals("Var")) {
                    dataInit("valfTipi", 0); //Komp + Tek
                } else {
                    dataInit("valfTipi", 2); //Sadece Çift
                }
            } else {
                if(kompanzasyonDurumu.equals("Var")) {
                    dataInit("valfTipi", 0); //Komp + Tek
                } else {
                    dataInit("valfTipi", 2); //Sadece Çift
                }
            }
        } else {
            if(secilenHidrolikKilitDurumu.equals("Var")) {
                if(kompanzasyonDurumu.equals("Var")) {
                    dataInit("valfTipi", 0);
                } else {
                    if(secilenPompaVal <= 28.1) {
                        valfTipiComboBox.getItems().clear();
                        valfTipiComboBox.getItems().addAll("Kilitli Blok");
                        valfTipiComboBox.setDisable(false);
                    } else {
                        dataInit("valfTipi", 1); //Tek + Çift
                    }
                }
            } else {
                if(kompanzasyonDurumu.equals("Var")) {
                    dataInit("valfTipi", 0); //Komp + Tek
                } else {
                    dataInit("valfTipi", 1); //Tek + Çift
                }
            }
        }

        disableKilitMotorAndPompa();
    }

    private void initComboBoxes(String currentComponent, int listenerStatus) {
        if(currentComponent.equals("motor")) {
            //motor seçildiğinde nereler etkilenecek
            if(listenerStatus == 0) {
                dataInit("sogutma", null);
                sogutmaComboBox.setDisable(false);
            }
        } else if(currentComponent.equals("sogutma")) {
            //sogutma seçildiğinde nereler etkilenecek
            if(listenerStatus == 0) {
                dataInit("hidrolikKilit", null);
                hidrolikKilitComboBox.setDisable(false);
            }
        } else if(currentComponent.equals("hidrolikKilit")) {
            //hidrolik kilit seçildiğinde nereler etkilenecek
            if(listenerStatus == 0) {
                dataInit("pompa", null);
                pompaComboBox.setDisable(false);
            }
        } else if(currentComponent.equals("pompa")) {
            //pompa seçildiğinde nereler etkilenecek
            if(listenerStatus == 0) {
                tankKapasitesiTextField.setDisable(false);
            }
        } else if(currentComponent.equals("tankKapasitesi")) {
            //tank kapasitesi değiştiğinde nereler etkilenecek
            if(listenerStatus == 0) {
                dataInit("kompanzasyon", null);
                kompanzasyonComboBox.setDisable(false);
            }
        } else if(currentComponent.equals("kompanzasyon")) {
            //kompanzasyon değiştiğinde nereler etkilenecek
            if(listenerStatus == 0) {
                initValfValues();
            }
        } else if(currentComponent.equals("valfTipi")) {
            //valf tipi değiştiğinde nereler etkilenecek
            if(listenerStatus == 0) {
                if(secilenSogutmaDurumu.equals("Yok")) {
                    if(Objects.equals(secilenHidrolikKilitDurumu, "Var") && secilenPompaVal > 28.1) {
                        dataInit("kilitMotor", null);
                    }
                }
            }
        } else if(currentComponent.equals("kilitMotor")) {
            //kilit motor değiştiğinde nereler etkilenecek
            if(listenerStatus == 0) {
                kilitPompaComboBox.setDisable(false);
                kilitPompaComboBox.getItems().addAll(SystemVariables.getLocalHydraulicData().lockPumpMap.get("0"));
            }
        } else if(currentComponent.equals("kilitPompa")) {
            //Bu kısımda sadece listener olacak

        }
    }

    private void disableKilitMotorAndPompa() {
        kilitMotorComboBox.setDisable(true);
        if(secilenKilitMotor != null) {
            kilitMotorComboBox.getSelectionModel().clearSelection();
            secilenKilitMotor = null;
        }

        kilitPompaComboBox.setDisable(true);
        if(secilenKilitPompa != null) {
            kilitPompaComboBox.getSelectionModel().clearSelection();
            secilenKilitPompa = null;
        }
    }

    public void minimizeProgram() {
        if (exportButton != null) {
            Stage stage = (Stage) exportButton.getScene().getWindow();
            stage.setIconified(true);
        }
    }
}
