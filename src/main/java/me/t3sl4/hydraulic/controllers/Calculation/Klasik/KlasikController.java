package me.t3sl4.hydraulic.controllers.Calculation.Klasik;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
import me.t3sl4.hydraulic.app.Main;
import me.t3sl4.hydraulic.controllers.Calculation.Klasik.PartList.KlasikParcaController;
import me.t3sl4.hydraulic.controllers.Popup.CylinderController;
import me.t3sl4.hydraulic.utils.Utils;
import me.t3sl4.hydraulic.utils.database.File.PDF.PDFUtil;
import me.t3sl4.hydraulic.utils.database.Model.Kabin.Kabin;
import me.t3sl4.hydraulic.utils.database.Model.Table.PartList.TableData;
import me.t3sl4.hydraulic.utils.general.SystemVariables;
import me.t3sl4.hydraulic.utils.service.HTTPRequest;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static me.t3sl4.hydraulic.utils.general.SystemVariables.*;

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

    private static final int DEFAULT_HEIGHT = 350;
    private static final int MAX_TANK_CAPACITY = 500;

    /*
    Seçilen Değerler:
     */
    public String secilenUniteTipi = "Klasik";
    public static String girilenSiparisNumarasi = null;
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
    public boolean hesaplamaBitti = false;

    public static String atananHT;

    private ArrayList<Text> sonucTexts = new ArrayList<>();

    double screenX, screenY;

    public static String atananKabinFinal = "";
    public static String gecisOlculeriFinal = "";
    private String imagePath = "";
    private String reverseImagePath = "";

    int motorYukseklik = 0;

    public void initialize() {
        Utils.textFilter(tankKapasitesiTextField);
        comboBoxListener();
        sonucTabloSatir1.setCellValueFactory(new PropertyValueFactory<>("satir1Property"));
        sonucTabloSatir2.setCellValueFactory(new PropertyValueFactory<>("satir2Property"));
        if(Main.loggedInUser == null) {
            kaydetButton.setDisable(true);
        }
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
            results = calcDimensions();

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
                    //Yeni Sistem:
                    if(secilenHidrolikKilitDurumu.equals("Var")) {
                        //Hidrolik Kilit Var
                        if(kompanzasyonDurumu.equals("Yok")) {
                            if(secilenValfTipi.equals("İnişte Çift Hız") || secilenValfTipi.equals("Kilitli Blok")) {
                                if(secilenPompaVal > 28.1) {
                                    //Kilit Ayrı Çift Hız.pdf
                                    image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/data/hydraulicUnitData/schematicImages/kilit_ayri_cift_hiz_white.png")));
                                    imagePath = "/assets/data/hydraulicUnitData/schematicImages/kilit_ayri_cift_hiz_white.png";
                                    reverseImagePath = "/assets/data/hydraulicUnitData/schematicImages/kilit_ayri_cift_hiz_black.png";
                                    imageTextEnable(calculatedX, calculatedY, "kilit_ayri_cift_hiz");
                                } else {
                                    //Kilitli Blok.pdf
                                    image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/data/hydraulicUnitData/schematicImages/kilitli_blok_white.png")));
                                    imagePath = "/assets/data/hydraulicUnitData/schematicImages/kilitli_blok_white.png";
                                    reverseImagePath = "/assets/data/hydraulicUnitData/schematicImages/kilitli_blok_black.png";
                                    imageTextEnable(calculatedX, calculatedY, "kilitli_blok");
                                }
                            } else if(secilenValfTipi.equals("Kompanzasyon || İnişte Tek Hız")) {
                                //Kilit Ayrı Tek Hız.pdf
                                image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/data/hydraulicUnitData/schematicImages/kilit_ayri_tek_hiz_white.png")));
                                imagePath = "/assets/data/hydraulicUnitData/schematicImages/kilit_ayri_tek_hiz_white.png";
                                reverseImagePath = "/assets/data/hydraulicUnitData/schematicImages/kilit_ayri_tek_hiz_black.png";
                                imageTextEnable(calculatedX, calculatedY, "kilit_ayri_tek_hiz");
                            }
                        } else {
                            //Kompanzasyon Farketmez
                            if(secilenValfTipi.equals("Kompanzasyon || İnişte Tek Hız")) {
                                //Kilit Ayrı Tek Hız.pdf
                                image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/data/hydraulicUnitData/schematicImages/kilit_ayri_tek_hiz_white.png")));
                                imagePath = "/assets/data/hydraulicUnitData/schematicImages/kilit_ayri_tek_hiz_white.png";
                                reverseImagePath = "/assets/data/hydraulicUnitData/schematicImages/kilit_ayri_tek_hiz_black.png";
                                imageTextEnable(calculatedX, calculatedY, "kilit_ayri_tek_hiz");
                            }
                        }
                    } else {
                        //Hidrolik Kilit Yok
                        if(kompanzasyonDurumu.equals("Yok")) {
                            if(secilenValfTipi.equals("İnişte Çift Hız")) {
                                //Çift Hız.pdf
                                image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/data/hydraulicUnitData/schematicImages/cift_hiz_white.png")));
                                imagePath = "/assets/data/hydraulicUnitData/schematicImages/cift_hiz_white.png";
                                reverseImagePath = "/assets/data/hydraulicUnitData/schematicImages/cift_hiz_black.png";
                                imageTextEnable(calculatedX, calculatedY, "cift_hiz");
                            } else if(secilenValfTipi.equals("Kompanzasyon || İnişte Tek Hız") || secilenValfTipi.equals("İnişte Tek Hız")) {
                                //Tek Hız_Kompanzasyon + Tek Hız.pdf
                                image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/data/hydraulicUnitData/schematicImages/tek_hiz_kompanzasyon_arti_tek_hiz_white.png")));
                                imagePath = "/assets/data/hydraulicUnitData/schematicImages/tek_hiz_kompanzasyon_arti_tek_hiz_white.png";
                                reverseImagePath = "/assets/data/hydraulicUnitData/schematicImages/tek_hiz_kompanzasyon_arti_tek_hiz_black.png";
                                imageTextEnable(calculatedX, calculatedY, "tek_hiz_kompanzasyon_arti_tek_hiz");
                            }
                        } else {
                            if(secilenValfTipi.equals("Kompanzasyon || İnişte Tek Hız") || secilenValfTipi.equals("İnişte Tek Hız")) {
                                //Tek Hız_Kompanzasyon + Tek Hız.pdf
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
                if(Main.loggedInUser != null) {
                    kaydetButton.setDisable(false);
                }

                hesaplamaBitti = true;
            } else {
                Utils.showErrorMessage("Hesaplama sonucu beklenmeyen bir hata oluştu.");
            }
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

            HTTPRequest.authorizedUploadMultipleFiles(creationURL, "POST", files, SystemVariables.getAccessToken(), Main.loggedInUser.getUsername(), girilenSiparisNumarasi, secilenUniteTipi, new HTTPRequest.RequestCallback() {
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
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("fxml/ParcaListesi.fxml"));
                VBox root = fxmlLoader.load();
                KlasikParcaController parcaController = fxmlLoader.getController();
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
        int tankImageStartX = 258;
        int tankImageStartY = 213;
        int tankImageWidth = 340;
        int tankImageHeight = 262;

        int schematicImageStartX = 740;
        int schematicImageStartY = 200;
        int schematicImageWidth = 400;
        int schematicImageHeight = 246;

        if(hesaplamaBitti) {
            String generalCyclinderString = showCyclinderPopup();
            String numberPart = generalCyclinderString.replaceAll("[^0-9]", "");
            String stringPart = generalCyclinderString.replaceAll("[0-9]", "");

            int selectedCylinders = Integer.parseInt(numberPart);
            String isPressureValf = stringPart;
            if (selectedCylinders == -1 && isPressureValf.isEmpty()) {
                Utils.showErrorMessage("Silindir sayısı seçilmedi. İşlem iptal edildi.");
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

            PDFUtil.pdfGenerator("/assets/images/general/onder_grup_main.png", "tankImage.png", "schematicImage.png", "/assets/data/hydraulicUnitData/schematicPDF/" + pdfPath, girilenSiparisNumarasi, kullanilacakKabin.getText().toString(), secilenMotor, secilenPompa);
        } else {
            Utils.showErrorMessage("Lütfen hesaplama işlemini tamamlayıp tekrar deneyin.");
        }
    }

    private String hydraulicSchemaSelection(int selectedCylinders, String isPressureValf) {
        boolean isSogutmaVar = secilenSogutmaDurumu.equals("Var");
        boolean isKilitMotorVar = secilenKilitMotor != null;
        boolean isKompanzasyonVar = kompanzasyonDurumu.equals("Var");
        boolean isInisteCiftHiz = secilenValfTipi.equals("İnişte Çift Hız");
        boolean isInisteTekHiz = secilenValfTipi.equals("İnişte Tek Hız");
        boolean isKilitliBlok = secilenValfTipi.equals("Kilitli Blok");

        if (isSogutmaVar) {
            if (isKilitMotorVar) {
                if (isKompanzasyonVar) {
                    return getCylinderImage(selectedCylinders, isPressureValf, 13, 14, 15, 16);
                } else {
                    return getCylinderImage(selectedCylinders, isPressureValf, 5, 6, 7, 8);
                }
            } else {
                if (isKompanzasyonVar) {
                    return getCylinderImage(selectedCylinders, isPressureValf, 9, 10, 11, 12);
                } else {
                    return getCylinderImage(selectedCylinders, isPressureValf, 1, 2, 3, 4);
                }
            }
        } else {
            if (isKilitMotorVar) {
                if (isKompanzasyonVar) {
                    return getCylinderImage(selectedCylinders, isPressureValf, 33, 34, 35, 36);
                } else if (isInisteTekHiz) {
                    return getCylinderImage(selectedCylinders, isPressureValf, 17, 18, 19, 20);
                } else {
                    return getCylinderImage(selectedCylinders, isPressureValf, 21, 22, 23, 24);
                }
            } else {
                if (isKompanzasyonVar) {
                    return getCylinderImage(selectedCylinders, isPressureValf, 29, 30, 31, 32);
                } else if (isKilitliBlok) {
                    return getCylinderImage(selectedCylinders, isPressureValf, 25, 26, 27, 28);
                } else if (isInisteCiftHiz) {
                    return getCylinderImage(selectedCylinders, isPressureValf, 37, 38, 39, 40);
                } else {
                    return getCylinderImage(selectedCylinders, isPressureValf, 41, 42, 43, 44);
                }
            }
        }
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
            x +=  kampanaDegeri;
            yK += kampanaDegeri;
            System.out.println("Başlangıç:");
            System.out.println("X: " + x + " yK: " + yK + " yV: " + yV);
            System.out.println("Motor + Kampana için:");
            System.out.println("X += " + kampanaDegeri + " (Kampana)");
            System.out.println("yK += " + kampanaDegeri + " (Kampana)");

            //TODO
            //Yeni verilere göre ekleme yapılacak
            if(secilenKilitMotor != null) {
                //Kilit Motor Var
                if(kompanzasyonDurumu.equals("Yok")) {
                    //Kompanzasyon Yok
                    if(secilenHidrolikKilitDurumu.equals("Yok")) {
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
                    //Kompanzasyon Var
                    if(secilenValfTipi.equals("Kompanzasyon || İnişte Tek Hız")) {
                        x += getLocalHydraulicData().kilitMotorTankArasiBoslukX + getLocalHydraulicData().kilitMotorX + getLocalHydraulicData().kilitMotorAraBoslukX + getLocalHydraulicData().kampanaTankArasiBoslukX;
                        yV += getLocalHydraulicData().kilitMotorTankArasiBoslukY + getLocalHydraulicData().kilitMotorY + getLocalHydraulicData().kilitMotorYOn + getLocalHydraulicData().tekHizKilitAyriY + getLocalHydraulicData().tekHizBlokY + getLocalHydraulicData().tekHizKilitAyriYOn;
                        yK += getLocalHydraulicData().kampanaTankArasiBoslukY + getLocalHydraulicData().kampanaBoslukYOn;
                    }
                }
            } else {
                //Kilit Motor Yok
                if(kompanzasyonDurumu.equals("Var")) {
                    //Kompanzasyon Var
                    if(secilenHidrolikKilitDurumu.equals("Yok")) {
                        if(secilenValfTipi.equals("Kompanzasyon || İnişte Tek Hız")) {
                            x += getLocalHydraulicData().tekHizTankArasiBoslukX + getLocalHydraulicData().tekHizBlokX + getLocalHydraulicData().tekHizAraBoslukX + getLocalHydraulicData().kampanaTankArasiBoslukX;
                            yV += getLocalHydraulicData().tekHizTankArasiBoslukY + getLocalHydraulicData().tekHizBlokY + getLocalHydraulicData().tekHizYOn;
                            yK += getLocalHydraulicData().kampanaTankArasiBoslukY + getLocalHydraulicData().kampanaBoslukYOn;
                        }
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
                    } else if(secilenValfTipi.equals("Kilitli Blok")) {
                        x += getLocalHydraulicData().kilitliBlokTankArasiBoslukX + getLocalHydraulicData().kilitliBlokX + getLocalHydraulicData().kilitliBlokAraBoslukX + getLocalHydraulicData().kampanaTankArasiBoslukX;
                        yV += getLocalHydraulicData().kilitliBlokTankArasiBoslukY + getLocalHydraulicData().kilitliBlokY + getLocalHydraulicData().kilitliBlokYOn;
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

            hesaplananHacim = ((x*h*y) / 1000000) - SystemVariables.getLocalHydraulicData().kayipLitre;
            eskiX = x;
            eskiY = y;
            eskiH = h;
        }

        Kabin finalTank = null;
        for(Kabin selectedTank : SystemVariables.getLocalHydraulicData().inputTanks) {
            int litre = selectedTank.getKabinHacim();
            int tempX = selectedTank.getKabinX();
            int tempY = selectedTank.getKabinY();
            int kabinYukseklik = selectedTank.getKabinH();

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
            atananHT = finalTank.getTankName();
            if(finalTank.getGecisH() < (motorYukseklik + h)) {
                for(Kabin selectedTank : SystemVariables.getLocalHydraulicData().inputTanks) {
                    int kabinYukseklik = selectedTank.getGecisH();
                    //System.out.println("Kabin Yükseklik: " + kabinYukseklik + "\nÖnceden Seçilen Kabin Yükseklik: " + finalTank.getKabinH());

                    int tempYukseklik = finalTank.getKabinH() + motorYukseklik;
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
        String gecisOlculeri = finalTank.getGecisOlculeri();

        //kullanilacakKabin.setText("Kullanmanız Gereken Kabin: \n\t\t\t\t\t\t" + atananKabin + "\n\t\t\tGeçiş Ölçüleri: " + gecisOlculeri + " (x, y, h)");
        kullanilacakKabin.setText("\t\t\t\t\t\t" + atananKabin + "\n\t\t\tGeçiş Ölçüleri: " + gecisOlculeri + " (x, y, h)");
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
        hesaplananHacim = ((x*h*y) / 1000000) - SystemVariables.getLocalHydraulicData().kayipLitre;

        atananHT = "HT SOĞUTMA";
        String atananKabin = "KD SOĞUTMA";
        String gecisOlculeri = "1000x600x350";
        //kullanilacakKabin.setText("Kullanmanız Gereken Kabin: \n\t\t\t\t\t\t" + atananKabin + "\n\t\t\tGeçiş Ölçüleri: " + gecisOlculeri + " (x, y, h)");
        kullanilacakKabin.setText("\t\t\t\t\t\t" + atananKabin + "\n\t\t\tGeçiş Ölçüleri: " + gecisOlculeri + " (x, y, h)");
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
            } else {
                valfTipiComboBox.getItems().addAll(SystemVariables.getLocalHydraulicData().valveTypeMap.get("1")); //Kompanzasyon || İnişte Tek Hız
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
        if(calculatedImage.equals("cift_hiz")) {
            addTextToList("X: " + x + " mm", 672, 445, 0, 14, Color.WHITE);
            addTextToList("Y: " + y + " mm", 475, 318, 90, 14, Color.WHITE);

            addTextToList("50 mm", 560, 225, 0, 10, Color.WHITE);
            addTextToList("50 mm", 520, 290, 90, 10, Color.WHITE);
            addTextToList("30 mm", 510, 405, 90, 10, Color.WHITE);
            addTextToList("65 mm", 550, 420, 0, 10, Color.WHITE);
            addTextToList("125 mm", 575, 395, 0, 11, Color.WHITE);
            addTextToList("Ø25", 550, 370, -45, 10, Color.WHITE);
            addTextToList("Ø30", 650, 370, -45, 10, Color.WHITE);
            addTextToList(secilenValfTipi, 570, 312, 0, 10, Color.WHITE);
            addTextToList(getKampanaText(), 720, 300, 0, 12, Color.WHITE);
            addTextToList("70 mm", 775, 228, 0, 11, Color.WHITE);
            addTextToList("70 mm", 830, 290, 90, 11, Color.WHITE);
            addTextToList("Ø30", 810, 390, 30, 10, Color.WHITE);
            addTextToList("35 mm", 795, 420, 0, 11, Color.WHITE);
            addTextToList("35 mm", 840, 383, 90, 11, Color.WHITE);
        } else if (calculatedImage.equals("kilit_ayri_cift_hiz")) {
            addTextToList("X: " + x + " mm", 672, 448, 0, 14, Color.WHITE);
            addTextToList("Y: " + y + " mm", 475, 318, 90, 14, Color.WHITE);

            addTextToList("40 mm", 565, 215, 0, 10, Color.WHITE);
            addTextToList("100 mm", 530, 255, 0, 10, Color.WHITE);
            addTextToList("Ø110", 595, 245, 0, 10, Color.WHITE);
            addTextToList(getKampanaText(), 745, 260, 0, 10.5, Color.WHITE);
            addTextToList("70 mm", 785, 220, 0, 10, Color.WHITE);
            addTextToList("70 mm", 830, 263, 0, 10, Color.WHITE);
            addTextToList("210 mm", 595, 315, -90, 10, Color.WHITE);
            addTextToList(secilenValfTipi, 570, 395, 0, 10, Color.WHITE);
            addTextToList("50 mm", 520, 415, 90, 10, Color.WHITE);
            addTextToList("60 mm", 640, 415, 90, 10, Color.WHITE);
            addTextToList("Ø30", 675, 385, -30, 10, Color.WHITE);
            addTextToList("Ø25", 740, 385, -30, 10, Color.WHITE);
            addTextToList("50 mm", 595, 427, 0, 10, Color.WHITE);
            addTextToList("70 mm", 675, 427, 0, 10, Color.WHITE);
            addTextToList("100 mm", 685, 408, 0, 10, Color.WHITE);
            addTextToList("Ø52", 795, 390, 30, 10, Color.WHITE);
            addTextToList("50 mm", 785, 425, 0, 11, Color.WHITE);
            addTextToList("50 mm", 835, 383, 90, 11, Color.WHITE);
        } else if(calculatedImage.equals("kilit_ayri_tek_hiz")) {
            addTextToList("X: " + x + " mm", 672, 448, 0, 14, Color.WHITE);
            addTextToList("Y: " + y + " mm", 475, 318, 90, 14, Color.WHITE);

            addTextToList("40 mm", 565, 215, 0, 10, Color.WHITE);
            addTextToList("100 mm", 530, 255, 0, 10, Color.WHITE);
            addTextToList("Ø110", 595, 245, 0, 10, Color.WHITE);
            addTextToList(getKampanaText(), 745, 275, 0, 10.5, Color.WHITE);
            addTextToList("70 mm", 785, 220, 0, 10, Color.WHITE);
            addTextToList("70 mm", 830, 263, 0, 10, Color.WHITE);
            addTextToList("180 mm", 580, 315, -90, 10, Color.WHITE);
            addTextToList("150 mm", 525, 390, 0, 10, Color.WHITE);
            addTextToList(secilenValfTipi, 615, 365, 0, 10, Color.WHITE);
            addTextToList("50 mm", 555, 427, 0, 10, Color.WHITE);
            addTextToList("70 mm", 675, 427, 0, 10, Color.WHITE);
            addTextToList("100 mm", 620, 410, 0, 10, Color.WHITE);
            addTextToList("100 mm", 690, 410, 0, 10, Color.WHITE);
            addTextToList("Ø30", 675, 385, -30, 10, Color.WHITE);
            addTextToList("Ø25", 740, 385, -30, 10, Color.WHITE);
            addTextToList("Ø52", 795, 390, 30, 10, Color.WHITE);
            addTextToList("50 mm", 785, 425, 0, 11, Color.WHITE);
            addTextToList("50 mm", 835, 383, 90, 11, Color.WHITE);
        } else if(calculatedImage.equals("kilitli_blok")) {
            addTextToList("X: " + x + " mm", 672, 445, 0, 14, Color.WHITE);
            addTextToList("Y: " + y + " mm", 475, 318, 90, 14, Color.WHITE);

            addTextToList("50 mm", 550, 225, 0, 10, Color.WHITE);
            addTextToList("50 mm", 520, 285, 90, 10, Color.WHITE);
            addTextToList("200 mm", 560, 403, 0, 11, Color.WHITE);
            addTextToList("50 mm", 655, 420, 0, 11, Color.WHITE);
            addTextToList("Ø25", 661, 385, -45, 10, Color.WHITE);
            addTextToList(secilenValfTipi, 565, 305, 0, 10, Color.WHITE);
            addTextToList(getKampanaText(), 725, 300, 0, 12, Color.WHITE);
            addTextToList("70 mm", 775, 228, 0, 11, Color.WHITE);
            addTextToList("70 mm", 830, 290, 90, 11, Color.WHITE);
            addTextToList("Ø30", 810, 390, 30, 10, Color.WHITE);
            addTextToList("35 mm", 795, 420, 0, 11, Color.WHITE);
            addTextToList("35 mm", 840, 383, 90, 11, Color.WHITE);
        } else if(calculatedImage.equals("tek_hiz_kompanzasyon_arti_tek_hiz")) {
            addTextToList("X: " + x + " mm", 672, 445, 0, 14, Color.WHITE);
            addTextToList("Y: " + y + " mm", 475, 318, 90, 14, Color.WHITE);

            addTextToList("70 mm", 565, 225, 0, 11, Color.WHITE);
            addTextToList("100 mm", 520, 300, 0, 11, Color.WHITE);
            addTextToList("35 mm", 510, 405, 90, 10, Color.WHITE);
            addTextToList("150 mm", 580, 395, 0, 11, Color.WHITE);
            addTextToList("65 mm", 545, 415, 0, 11, Color.WHITE);
            addTextToList("Ø25", 550, 370, -45, 10, Color.WHITE);
            addTextToList("Ø30", 660, 366, -45, 10, Color.WHITE);
            addTextToList(secilenValfTipi, 570, 340, 0, 10, Color.WHITE);
            addTextToList(getKampanaText(), 720, 300, 0, 12, Color.WHITE);
            addTextToList("70 mm", 775, 228, 0, 11, Color.WHITE);
            addTextToList("70 mm", 830, 290, 90, 11, Color.WHITE);
            addTextToList("Ø30", 810, 390, 30, 10, Color.WHITE);
            addTextToList("35 mm", 795, 420, 0, 11, Color.WHITE);
            addTextToList("35 mm", 840, 383, 90, 11, Color.WHITE);
        } else if(calculatedImage.equals("sogutma_kilitsiz_cift_hiz")) {
            addTextToList("X: " + x + " mm", 672, 442, 0, 14, Color.WHITE);
            addTextToList("Y: " + y + " mm", 470, 318, 90, 14, Color.WHITE);
        } else if(calculatedImage.equals("sogutma_kilitli_cift_hiz")) {
            addTextToList("X: " + x + " mm", 672, 442, 0, 14, Color.WHITE);
            addTextToList("Y: " + y + " mm", 470, 318, 90, 14, Color.WHITE);
        } else if(calculatedImage.equals("sogutma_kilitsiz_tek_hiz")) {
            addTextToList("X: " + x + " mm", 672, 442, 0, 14, Color.WHITE);
            addTextToList("Y: " + y + " mm", 470, 318, 90, 14, Color.WHITE);
        } else if (calculatedImage.equals("sogutma_kilitli_tek_hiz")) {
            addTextToList("X: " + x + " mm", 672, 442, 0, 14, Color.WHITE);
            addTextToList("Y: " + y + " mm", 470, 318, 90, 14, Color.WHITE);
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
    }

    private void tankGorselLoad() {
        Image image;

        if(secilenSogutmaDurumu.equals("Var")) {
            if(secilenHidrolikKilitDurumu.equals("Var")) {
                image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/data/hydraulicUnitData/cabins/classic/ingilteresogutuculu.png")));
                sonucTankGorsel.setImage(image);
                genislikSonucText.setLayoutY(412.0);
                genislikSonucText.setRotate(33.5);
                derinlikSonucText.setRotate(-30.0);
                derinlikSonucText.setLayoutX(193.0);
            } else {
                image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/data/hydraulicUnitData/cabins/classic/sogutuculukilitsiz.png")));
                sonucTankGorsel.setImage(image);
                genislikSonucText.setLayoutY(412.0);
                genislikSonucText.setRotate(33.5);
                derinlikSonucText.setRotate(-30.0);
                derinlikSonucText.setLayoutX(193.0);
            }
        } else {
            if(secilenHidrolikKilitDurumu.equals("Var")) {
                if(kompanzasyonDurumu.equals("Var")) {
                    image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/data/hydraulicUnitData/cabins/classic/sogutuculuotuzuc.png")));
                    sonucTankGorsel.setImage(image);
                    genislikSonucText.setRotate(27.5);
                    derinlikSonucText.setRotate(-27.5);
                    derinlikSonucText.setLayoutX(190.0);
                } else {
                    if(secilenPompaVal > 28.1) {
                        image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/data/hydraulicUnitData/cabins/classic/sogutuculuotuzuc.png")));
                        sonucTankGorsel.setImage(image);
                        genislikSonucText.setRotate(27.5);
                        derinlikSonucText.setRotate(-27.5);
                        derinlikSonucText.setLayoutX(190.0);
                    } else {
                        image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/data/hydraulicUnitData/cabins/classic/kilitliblok.png")));
                        sonucTankGorsel.setImage(image);
                        genislikSonucText.setRotate(27.5);
                        derinlikSonucText.setRotate(-27.5);
                        derinlikSonucText.setLayoutX(193.0);
                    }
                }
            } else {
                if(kompanzasyonDurumu.equals("Var")) {
                    image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/data/hydraulicUnitData/cabins/classic/tekhiz.png")));
                    sonucTankGorsel.setImage(image);
                } else {
                    if(secilenValfTipi.contains("Tek Hız")) {
                        image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/data/hydraulicUnitData/cabins/classic/tekhiz.png")));
                        sonucTankGorsel.setImage(image);
                    } else {
                        image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/data/hydraulicUnitData/cabins/classic/cifthiz.png")));
                        sonucTankGorsel.setImage(image);
                    }
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
                    dataInit("valfTipi", 1); //Tek + Çift
                }
            } else {
                if(kompanzasyonDurumu.equals("Var")) {
                    dataInit("valfTipi", 0); //Komp + Tek
                } else {
                    dataInit("valfTipi", 1); //Tek + Çift
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

    private String showCyclinderPopup() {
        Image icon = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/images/general/logo.png")));
        try {
            FXMLLoader loader = new FXMLLoader(Launcher.class.getResource("fxml/CylinderCount.fxml"));
            Parent root = loader.load();

            CylinderController controller = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Silindir Seçimi");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.getIcons().add(icon);
            stage.showAndWait();

            if (controller.isConfirmed()) {
                return controller.getFinalResult();
            } else {
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
            Utils.showErrorMessage("Silindir seçimi sırasında bir hata oluştu.");
            return null;
        }
    }
}
