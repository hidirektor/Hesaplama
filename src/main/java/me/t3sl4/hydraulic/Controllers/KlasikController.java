package me.t3sl4.hydraulic.Controllers;

import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Box;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import me.t3sl4.hydraulic.Launcher;
import me.t3sl4.hydraulic.Util.Table.TableData;
import me.t3sl4.hydraulic.Util.Util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.UnaryOperator;

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
    private Text genislikSonucText;

    @FXML
    private Text yukseklikSonucText;

    @FXML
    private Text derinlikSonucText;

    @FXML
    private Text hacimText;

    @FXML
    private Box hydraulicUnitShape;

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

    /*
    Seçilen Değerler:
     */
    public String girilenSiparisNumarasi = null;
    public String secilenUniteTipi = "Klasik";
    public static String secilenMotor = null;
    public static int secilenKampana = 0;
    public static String secilenPompa = null;
    public int girilenTankKapasitesiMiktari = 0;
    public String secilenHidrolikKilitDurumu = null;
    public static String secilenValfTipi = null;
    public String secilenKilitMotor = null;
    public String secilenKilitPompa = null;
    public String secilenSogutmaDurumu = null;

    public boolean hidrolikKilitStat = false;
    public boolean sogutmaStat = false;
    public boolean hesaplamaBitti = false;

    public static String atananHT;

    int atananHacim = 0;
    int hesaplananHacim = 0;

    private HostServices hostServices;
    private static final String GITHUB_URL = "https://github.com/";
    private static final String LINKEDIN_URL = "https://www.linkedin.com/in/";

    private double x, y;

    public void initialize() {
        textFilter();
        defineKabinOlcu();
        comboBoxListener();
        sonucTabloSatir1.setCellValueFactory(new PropertyValueFactory<>("satir1Property"));
        sonucTabloSatir2.setCellValueFactory(new PropertyValueFactory<>("satir2Property"));
    }

    @FXML
    public void hesaplaFunc() {
        int h = 0; //Yükseklik
        int y = 0; //Derinlik
        int x = 0; //Genişlik
        int hacim = 0; //Hacim
        ArrayList<Integer> results;
        if (checkComboBox()) {
            Util.showErrorMessage("Lütfen tüm girdileri kontrol edin.");
        } else {
            enableSonucSection();
            results = calcDimensions(x, y, h, Util.dataManipulator.kampanaDegerleri);
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
            if(secilenKilitMotor != null) {
                image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("icons/normal.png")));

            } else {
                image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("icons/kilitMotor.png")));
            }
            //tankGorselLoad();
            sonucKapakImage.setImage(image);
            parcaListesiButton.setDisable(false);
            exportButton.setDisable(false);
            imageTextEnable();
            hesaplamaBitti = true;
        }
    }

    ArrayList<Integer> calcDimensions(int x, int y, int h, ArrayList<Integer> kampanaDegerleri) {
        int eskiX=0, eskiY=0, eskiH=0;

        //hesaplama kısmı:
        ArrayList<Integer> finalValues = new ArrayList<>();
        int yV = 0;
        int yK = 0;
        System.out.println("--------Hesaplama Başladı--------Ø");
        secilenKampana = kampanaDegerleri.get(motorComboBox.getSelectionModel().getSelectedIndex());
        String[] secPmp = secilenPompa.split(" cc");
        x += kampanaDegerleri.get(motorComboBox.getSelectionModel().getSelectedIndex()) + Util.dataManipulator.kampanaBoslukX;
        yK += kampanaDegerleri.get(motorComboBox.getSelectionModel().getSelectedIndex()) + Util.dataManipulator.kampanaBoslukY + Util.dataManipulator.kampanaBoslukY;
        System.out.println("Motor + Kampana için:");
        System.out.println("X += " + kampanaDegerleri.get(motorComboBox.getSelectionModel().getSelectedIndex()) + " (Kampana) " + Util.dataManipulator.kampanaBoslukX + " (Kampana Boşluk)");
        System.out.println("yK += " + kampanaDegerleri.get(motorComboBox.getSelectionModel().getSelectedIndex()) + " (Kampana) + " + Util.dataManipulator.kampanaBoslukY + " (Kampana Boşluk) + " + Util.dataManipulator.kampanaBoslukY + " (Kampana Boşluk)");

        float secilenPompaVal = Float.parseFloat(secPmp[0]);
        //hidrolik kilit seçiliyse: valf tipi = kilitli blok olarak gelicek
        //kilitli blok ölçüsü olarak: X'e +100 olacak
        if(Objects.equals(secilenHidrolikKilitDurumu, "Var") && Objects.equals(secilenValfTipi, "Kilitli Blok || Çift Hız")) {
            x += 120 + Util.dataManipulator.kilitliBlokAraBoslukX + Util.dataManipulator.valfBoslukX;
            yV += 190 + Util.dataManipulator.valfBoslukYArka + Util.dataManipulator.valfBoslukYOn;
            System.out.println("Kilitli Blok için:");
            System.out.println("X += " + Util.dataManipulator.kilitliBlokAraBoslukX + " (Ara Boşluk) + " + Util.dataManipulator.valfBoslukX + " (Valf Boşluk)");
            System.out.println("yV += " + Util.dataManipulator.valfBoslukYArka + " (Valf Boşluk Arka) + " + Util.dataManipulator.valfBoslukYOn + " (Valf Boşluk Ön)");
        }
        //hidrolik kilit olmadığı durumlarda valf tipleri için
        if(Objects.equals(secilenHidrolikKilitDurumu, "Yok")) {
            if(Objects.equals(secilenValfTipi, "İnişte Tek Hız")) {
                // X yönünde +120 olacak Y yönünde 180 mm eklenecek
                x += 70 + Util.dataManipulator.valfBoslukX + Util.dataManipulator.tekHizAraBoslukX;
                yV += 180 + Util.dataManipulator.valfBoslukYOn + Util.dataManipulator.valfBoslukYArka;
                System.out.println("İnişte Tek Hız İçin: (Hidrolik Kilit Yok)");
                System.out.println("X += " + Util.dataManipulator.valfBoslukX + " (Valf Boşluk) + " + Util.dataManipulator.tekHizAraBoslukX + " (Tek Hız Boşluk)");
                System.out.println("yV += " + Util.dataManipulator.valfBoslukYOn + " (Valf Boşluk Ön) + " + Util.dataManipulator.valfBoslukYArka + " (Valf Boşluk Arka)");
            } else if(Objects.equals(secilenValfTipi, "İnişte Çift Hız")) {
                //X yönünde 190 Y yönünde 90
                x += 140 + Util.dataManipulator.ciftHizAraBoslukX + Util.dataManipulator.valfBoslukX;
                yV += 90 + Util.dataManipulator.valfBoslukYOn + Util.dataManipulator.valfBoslukYArka;
                System.out.println("İnişte Çift Hız İçin: (Hidrolik Kilit Yok)");
                System.out.println("X += " + Util.dataManipulator.valfBoslukX + " (Valf Boşluk) + " + Util.dataManipulator.ciftHizAraBoslukX + " (Tek Hız Boşluk)");
                System.out.println("yV += " + Util.dataManipulator.valfBoslukYOn + " (Valf Boşluk Ön) + " + Util.dataManipulator.valfBoslukYArka + " (Valf Boşluk Arka)");
            } else {
                //kompanzasyon seçilmişse:
                //kilit yoksa: X'e 190 Y'ye 180
                if(secilenHidrolikKilitDurumu.equals("Yok") && Objects.equals(secilenValfTipi, "Kompanzasyon + İnişte Tek Hız")) {
                    x += 140 + Util.dataManipulator.kompanzasyonTekHizAraBoslukX;
                    yV += 180 + Util.dataManipulator.valfBoslukYOn + Util.dataManipulator.valfBoslukYArka;
                    System.out.println("Kompanzasyon + Tek Hız İçin: (Hidrolik Kilit Yok)");
                    System.out.println("X += " + Util.dataManipulator.kompanzasyonTekHizAraBoslukX + " (Kompanzasyon Ara Boşluk)");
                    System.out.println("yV += " + Util.dataManipulator.valfBoslukYOn + " (Valf Boşluk Ön) + " + Util.dataManipulator.valfBoslukYArka + " (Valf Boşluk Arka)");
                }
            }
        } else {
            if(secilenPompaVal > 28.1) {
                String[] secKilitMotor = secilenKilitMotor.split(" kW");
                float secilenKilitMotorVal = Float.parseFloat(secKilitMotor[0]);
                //String[] secKilitPompa = secilenKilitPompa.split(" cc");
                //float secilenKilitPompaVal = Float.parseFloat(secKilitPompa[0]);

                if(Objects.equals(secilenValfTipi, "Kompanzasyon + İnişte Tek Hız")) {
                    yV += 180 + Util.dataManipulator.valfBoslukYArka + Util.dataManipulator.valfBoslukYOn;
                    System.out.println("Kompanzasyon + İnişte Tek Hız (Kilitli Blok) (Pompa > 28.1) için:");
                    System.out.println("yV += " + Util.dataManipulator.valfBoslukYOn + " (Valf Boşluk Ön) + " + Util.dataManipulator.valfBoslukYArka + " (Valf Boşluk Arka)");
                } else if(Objects.equals(secilenValfTipi, "İnişte Çift Hız")) {
                    System.out.println("İnişte Çift Hız (Kilitli Blok) için:");
                    if(secilenPompaVal > 28.1) {
                        yV += 90 + Util.dataManipulator.valfBoslukYOn;
                        System.out.println("(Pompa > 28.1) için:");
                        System.out.println("yV += " + Util.dataManipulator.valfBoslukYOn + " (Valf Boşluk Ön)");
                    } else {
                        yV += 90 + Util.dataManipulator.valfBoslukYOn + Util.dataManipulator.valfBoslukYArka;
                        System.out.println("(Pompa <= 28.1) için:");
                        System.out.println("yV += " + Util.dataManipulator.valfBoslukYOn + " (Valf Boşluk Ön) + " + Util.dataManipulator.valfBoslukYArka + " (Valf Boşluk Arka)");
                    }
                } else if(Objects.equals(secilenValfTipi, "İnişte Tek Hız")) {
                    yV += 180 + Util.dataManipulator.valfBoslukYOn + Util.dataManipulator.valfBoslukYArka;
                    System.out.println("İnişte Tek Hız (Kilitli Blok) için:");
                    System.out.println("yV += " + Util.dataManipulator.valfBoslukYOn + " (Valf Boşluk Ön) + " + Util.dataManipulator.valfBoslukYArka + " (Valf Boşluk Arka)");
                }

                if(secilenKilitMotorVal != 0) {
                    x += 200 + Util.dataManipulator.kilitMotorKampanaBosluk + Util.dataManipulator.kilitMotorMotorBoslukX;
                    yV += 200 + Util.dataManipulator.kilitMotorBoslukYOn + Util.dataManipulator.kilitMotorBoslukYArka;
                    System.out.println("Kilit Motor için:");
                    System.out.println("X += " + Util.dataManipulator.kilitMotorKampanaBosluk + " (Kampana Boşluk) + " + Util.dataManipulator.kilitMotorMotorBoslukX + " (Kilit Mootr Boşluk)");
                    System.out.println("yV += " + Util.dataManipulator.kilitMotorBoslukYOn + " (Kilit Motor Ön) + " + Util.dataManipulator.kilitMotorBoslukYArka + " (Kilit Motor Arka)");
                }
            }
        }
        if(Objects.equals(secilenSogutmaDurumu, "Var")) {
            x += 350 + Util.dataManipulator.sogutmaAraBoslukX;
            yK += 152 + Util.dataManipulator.sogutmaAraBoslukYkOn + Util.dataManipulator.sogutmaAraBoslukYkArka;
            System.out.println("Soğutma için:");
            System.out.println("X += " + Util.dataManipulator.sogutmaAraBoslukX + " (Ara Boşluk)");
            System.out.println("yK += " + Util.dataManipulator.sogutmaAraBoslukYkOn + " (Ara Boşluk Ön) + " + Util.dataManipulator.sogutmaAraBoslukYkArka + " (Ara Boşluk Arka)");
        }
        y = Math.max(yV, yK);
        if(y <= 350) {
            y = 350;
        }
        if(x <= 550) {
            x = 550;
        }
        h = 300;

        hesaplananHacim = ((x*h*y) / 1000000) - Util.dataManipulator.kayipLitre;
        eskiX = x;
        eskiY = y;
        eskiH = h;

        int enKucukLitreFarki = Integer.MAX_VALUE;
        int[] enKucukLitreOlculer = null;
        for (int[] olculer : Util.dataManipulator.kabinOlculeri.values()) {
            int litre = olculer[3];
            int tempX = olculer[0];
            int tempY = olculer[1];

            if(hesaplananHacim > girilenTankKapasitesiMiktari) {
                if(x <= tempX && y <= tempY) {
                    enKucukLitreOlculer = olculer;
                    break;
                }
            } else {
                if (litre >= girilenTankKapasitesiMiktari && litre - girilenTankKapasitesiMiktari <= enKucukLitreFarki) {
                    if(hesaplananHacim != litre && hesaplananHacim < litre) {
                        if(x < tempX && y < tempY) {
                            enKucukLitreFarki = litre - girilenTankKapasitesiMiktari;
                            enKucukLitreOlculer = olculer;
                            break;
                        }
                    }
                }
            }
        }

        if (enKucukLitreOlculer != null) {
            x = enKucukLitreOlculer[0];
            y = enKucukLitreOlculer[1];
            h = enKucukLitreOlculer[2];
            atananHacim = enKucukLitreOlculer[3];
        }

        atananHT = Objects.requireNonNull(Util.getKeyByValue(Util.dataManipulator.kabinOlculeri, enKucukLitreOlculer)).toString();
        String atananKabin = "";
        String gecisOlculeri = "";
        if(Objects.equals(atananHT, "HT 40")) {
            atananKabin = "KD 40";
            gecisOlculeri = "540x460x780";
        } else if(Objects.equals(atananHT, "HT 70")) {
            atananKabin = "KD 70";
            gecisOlculeri = "640x520x950";
        } else if(Objects.equals(atananHT, "HT 100")) {
            atananKabin = "KD 70";
            gecisOlculeri = "640x520x950";
        } else if(Objects.equals(atananHT, "HT 125")) {
            atananKabin = "KD 125";
            gecisOlculeri = "720x550x1000";
        } else if(Objects.equals(atananHT, "HT 160")) {
            atananKabin = "KD 1620";
            gecisOlculeri = "900x800x1100";
        } else if(Objects.equals(atananHT, "HT 200")) {
            atananKabin = "KD 1620";
            gecisOlculeri = "900x800x1100";
        } else if(Objects.equals(atananHT, "HT 250")) {
            atananKabin = "KD 2530";
            gecisOlculeri = "1100x900x1150";
        } else if(Objects.equals(atananHT, "HT 300")) {
            atananKabin = "KD 2530";
            gecisOlculeri = "1100x900x1150";
        } else if(Objects.equals(atananHT, "HT 350")) {
            atananKabin = "KD 3540";
            gecisOlculeri = "1100x900x1250";
        } else if(Objects.equals(atananHT, "HT 400")) {
            atananKabin = "KD 3540";
            gecisOlculeri = "1100x900x1250";
        }

        kullanilacakKabin.setText("Kullanmanız Gereken Kabin: \n\t\t\t\t\t\t" + atananKabin + "\n\t\t\tGeçiş Ölçüleri: " + gecisOlculeri + " (x, y, h)");
        //int secilenMotorIndeks = motorComboBox.getSelectionModel().getSelectedIndex();
        //int motorYukseklikDegeri = Integer.parseInt(motorYukseklikVerileri.get(secilenMotorIndeks));


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

        finalValues.add(x);
        finalValues.add(y);
        finalValues.add(h);
        finalValues.add(atananHacim);
        return finalValues;
    }

    @FXML
    public void redirectGithub() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Yardım");
        alert.setHeaderText("Yardım İçin GitHub Sayfasına Yönlendiriliyorsunuz");

        Text text = new Text("Yardım dokümantasyonuna ulaşmak için aşağıdaki linke tıklayınız:");
        Hyperlink link = new Hyperlink("GitHub Repo Sayfası");
        link.setOnAction(this::openGitHubDocumentation);

        VBox vbox = new VBox(text, link);
        alert.getDialogPane().setContent(vbox);
        alert.showAndWait();
    }

    @FXML
    public void motorPressed() {
        if(motorComboBox.getValue() != null) {
            secilenMotor = motorComboBox.getValue();
            secilenKampana = Util.dataManipulator.kampanaDegerleri.get(motorComboBox.getSelectionModel().getSelectedIndex());
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

    private void textFilter() {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        };
        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        tankKapasitesiTextField.setTextFormatter(textFormatter);
    }

    @FXML
    public void hidrolikKilitPressed() {
        if(hidrolikKilitComboBox.getValue() != null) {
            secilenHidrolikKilitDurumu = hidrolikKilitComboBox.getValue();
            hidrolikKilitStat = true;
            if(Objects.equals(secilenHidrolikKilitDurumu, "Yok")) {
                initValf(0);
            } else {
                initValf(1);
            }
        }
    }

    @FXML
    public void valfTipiPressed() {
        if(valfTipiComboBox.getValue() != null) {
            secilenValfTipi = valfTipiComboBox.getValue();
            System.out.println("Seçilen Valf Tipi: " + secilenValfTipi);
            String[] secPmp = secilenPompa.split(" cc");
            float secilenPompaVal = Float.parseFloat(secPmp[0]);
            System.out.println("Seçilen Pompa Değeri: " + secilenPompaVal);

            if(Objects.equals(secilenHidrolikKilitDurumu, "Var") && secilenPompaVal > 28.1) {
                initKilitMotor();
            } else {
                sogutmaComboBox.setDisable(false);
                initSogutma();
                sogutmaStat = true;
            }
        }
    }

    @FXML
    public void kilitMotorPressed() {
        if(kilitMotorComboBox.getValue() != null) {
            kilitPompaComboBox.setDisable(false);
            kilitPompaComboBox.getItems().addAll(Util.dataManipulator.kilitPompaDegerleri);
            //kilitPompaComboBox.getItems().addAll("4.2 cc", "4.8 cc", "5.8 cc");
            secilenKilitMotor = kilitMotorComboBox.getValue();
        }
    }

    @FXML
    public void kilitPompaPressed() {
        if(kilitPompaComboBox.getValue() != null) {
            sogutmaComboBox.setDisable(false);
            initSogutma();
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
    public void parametrePressed() {
        Image icon = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("icons/logo.png")));
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("popup.fxml"));
            VBox root = fxmlLoader.load();
            PopupController popupController = fxmlLoader.getController();
            popupController.setValues(Util.dataManipulator.kampanaBoslukX, Util.dataManipulator.kampanaBoslukY,
                    Util.dataManipulator.valfBoslukX, Util.dataManipulator.valfBoslukYArka, Util.dataManipulator.valfBoslukYOn,
                    Util.dataManipulator.kilitliBlokAraBoslukX, Util.dataManipulator.tekHizAraBoslukX, Util.dataManipulator.ciftHizAraBoslukX,
                    Util.dataManipulator.kompanzasyonTekHizAraBoslukX, Util.dataManipulator.sogutmaAraBoslukX, Util.dataManipulator.sogutmaAraBoslukYkOn,
                    Util.dataManipulator.sogutmaAraBoslukYkArka, Util.dataManipulator.kilitMotorKampanaBosluk, Util.dataManipulator.kilitMotorMotorBoslukX,
                    Util.dataManipulator.kilitMotorBoslukYOn, Util.dataManipulator.kilitMotorBoslukYArka, Util.dataManipulator.kayipLitre);
            popupController.showValues();
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setScene(new Scene(root));
            popupStage.getIcons().add(icon);
            popupStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void parcaListesiGoster() {
        Image icon = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("icons/logo.png")));
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
            Util.showErrorMessage("Lütfen önce hesaplama işlemini bitirin !");
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
        int height = 550;

        if(hesaplamaBitti) {
            coords2Png(startX, startY, width, height);
            cropImage(680, startY, 370, height);

            //PDF kısmı:
            Util.pdfGenerator("icons/onderGrupMainBeyaz.png", "cropped_screenshot.png", "/data/test.pdf", girilenSiparisNumarasi);
        } else {
            Util.showErrorMessage("Lütfen hesaplama işlemini tamamlayıp tekrar deneyin.");
        }
    }

    private void coords2Png(int startX, int startY, int width, int height) {
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setViewport(new javafx.geometry.Rectangle2D(startX, startY, width, height));

        WritableImage screenshot = exportButton.getScene().snapshot(null);

        File outputFile = new File("screenshot.png");

        //Alan Testi:
        /*PixelWriter pixelWriter = screenshot.getPixelWriter();

        for (int y = startY; y < height; y++) {
            for (int x = startX; x < width; x++) {
                pixelWriter.setColor(x, y, javafx.scene.paint.Color.RED);
            }
        }*/

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

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
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

    private void openGitHubDocumentation(ActionEvent event) {
        String url = "https://github.com/hidirektor/OnderGrup-Hydraulic-Tool";
        hostServices.showDocument(url);
    }

    @FXML
    public void onderGrupSiteOpen() {
        Util.openURL("https://ondergrup.com");
    }

    @FXML
    public void openRecep() {
        Util.openURL(LINKEDIN_URL + "recep-can-ba%C5%9Fkurt-1a9889174/");
    }

    @FXML
    public void openHid() {
        Util.openURL(GITHUB_URL + "hidirektor");
    }

    @FXML
    public void openIpek() {
        Util.openURL(LINKEDIN_URL + "ipekszr/");
    }

    private void initMotor() {
        motorComboBox.setDisable(false);
        motorComboBox.getItems().clear();
        motorComboBox.getItems().addAll(Util.dataManipulator.motorDegerleri);
        //motorComboBox.getItems().addAll("4 kW", "5.5 kW", "5.5 kW (Kompakt)", "7.5 kW (Kompakt)", "11 kW", "11 kW (Kompakt)", "15 kW", "18.5 kW", "22 kW", "37 kW");
    }

    /*private void initMotorYukseklik() {
        Util.dataManipulator.motorYukseklikVerileri.add("345 mm");
        Util.dataManipulator.motorYukseklikVerileri.add("375 mm");
        Util.dataManipulator.motorYukseklikVerileri.add("365 mm");
        Util.dataManipulator.motorYukseklikVerileri.add("410 mm");
        Util.dataManipulator.motorYukseklikVerileri.add("500 mm");
        Util.dataManipulator.motorYukseklikVerileri.add("470 mm");
        Util.dataManipulator.motorYukseklikVerileri.add("540 mm");
        Util.dataManipulator.motorYukseklikVerileri.add("565 mm");
        Util.dataManipulator.motorYukseklikVerileri.add("565 mm");
        Util.dataManipulator.motorYukseklikVerileri.add("600 mm");
    }*/

    private void initKabinOlculeri(int x, int y, int h, int litre, String key) {
        int[] kabinOlcu = new int[4];
        kabinOlcu[0] = x;
        kabinOlcu[1] = y;
        kabinOlcu[2] = h;
        kabinOlcu[3] = litre;
        Util.dataManipulator.kabinOlculeri.put(key, kabinOlcu);
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

    private void initPompa() {
        pompaComboBox.getItems().clear();
        if(Objects.equals(secilenUniteTipi, "Hidros")) {
            pompaComboBox.getItems().addAll(Util.dataManipulator.pompaDegerleriHidros);
            //pompaComboBox.getItems().addAll("1.1 cc", "1.6 cc", "2.1 cc", "2.7 cc", "3.2 cc", "3.7 cc", "4.2 cc", "4.8 cc", "5.8 cc", "7 cc", "8 cc", "9 cc");
        } else if(Objects.equals(secilenUniteTipi, "Klasik")) {
            pompaComboBox.getItems().addAll(Util.dataManipulator.pompaDegerleriKlasik);
            //pompaComboBox.getItems().addAll("9.5 cc", "11.9 cc", "14 cc", "14.6 cc", "16.8 cc", "19.2 cc", "22.9 cc", "28.1 cc", "28.8 cc", "33.3 cc", "37.9 cc", "42.6 cc", "45.5 cc", "49.4 cc", "56.1 cc");
        } else {
            pompaComboBox.getItems().addAll(Util.dataManipulator.pompaDegerleriTumu);
            //pompaComboBox.getItems().addAll("1.1 cc", "1.6 cc", "2.1 cc", "2.7 cc", "3.2 cc", "3.7 cc", "4.2 cc", "4.8 cc", "5.8 cc", "7 cc", "8 cc", "9 cc", "9.5 cc", "11.9 cc", "14 cc", "14.6 cc", "16.8 cc", "19.2 cc", "22.9 cc", "28.1 cc", "28.8 cc", "33.3 cc", "37.9 cc", "42.6 cc", "45.5 cc", "49.4 cc", "56.1 cc");
        }
    }

    private void initValf(int stat) {
        valfTipiComboBox.getItems().clear();
        valfTipiComboBox.setDisable(false);
        if(stat == 1) {
            valfTipiComboBox.getItems().addAll(Util.dataManipulator.valfTipiDegerleri1);
            //valfTipiComboBox.getItems().addAll("Kilitli Blok || Çift Hız");
        } else {
            valfTipiComboBox.getItems().addAll(Util.dataManipulator.valfTipiDegerleri2);
            //valfTipiComboBox.getItems().addAll("İnişte Tek Hız", "İnişte Çift Hız", "Kompanzasyon + İnişte Tek Hız");
        }
    }

    private void initHidrolikKilit() {
        hidrolikKilitComboBox.getItems().clear();
        hidrolikKilitComboBox.getItems().addAll("Var", "Yok");
    }

    private void initSogutma() {
        sogutmaComboBox.getItems().clear();
        sogutmaComboBox.getItems().addAll("Var", "Yok");
    }

    private void initKilitMotor() {
        kilitMotorComboBox.setDisable(false);
        kilitMotorComboBox.getItems().clear();
        kilitMotorText.setVisible(true);
        kilitMotorComboBox.setVisible(true);
        kilitMotorComboBox.getItems().addAll(Util.dataManipulator.kilitMotorDegerleri);
        //kilitMotorComboBox.getItems().addAll("1.5 kW", "2.2 kW");
    }

    private void initKilitPompa() {
        kilitPompaComboBox.setDisable(false);
        kilitPompaComboBox.getItems().clear();
        kilitPompaText.setVisible(true);
        kilitPompaComboBox.setVisible(true);
        kilitPompaComboBox.getItems().addAll(Util.dataManipulator.kilitPompaDegerleri);
        //kilitPompaComboBox.getItems().addAll("4.2 cc", "4.8 cc", "5.8 cc");
    }

    private void disableAllSections() {
        motorComboBox.setDisable(true);
        pompaComboBox.setDisable(true);
        tankKapasitesiTextField.setDisable(true);
        valfTipiComboBox.setDisable(true);
        hidrolikKilitComboBox.setDisable(true);
        sogutmaComboBox.setDisable(true);
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
        hydraulicUnitShape.setVisible(true);
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
            initMotor();
            if(girilenSiparisNumarasi != null) {
                tabloGuncelle();
            }
        });

        motorComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if(!motorComboBox.getItems().isEmpty() && newValue != null) {
                secilenMotor = newValue;
                secilenKampana = Util.dataManipulator.kampanaDegerleri.get(motorComboBox.getSelectionModel().getSelectedIndex());
                initPompa();
                if(secilenMotor != null) {
                    tabloGuncelle();
                }
            }
        });

        pompaComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            secilenPompa = newValue;
            if(oldValue != null && secilenPompa != null) {
                String[] oldSecPmp = oldValue.split(" cc");
                float oldSecilenPompaVal = Float.parseFloat(oldSecPmp[0]);
                String[] secPmp = secilenPompa.split(" cc");
                float secilenPompaVal = Float.parseFloat(secPmp[0]);
                if(oldSecilenPompaVal >= 28.1 && secilenPompaVal < 28.1) {
                    disableMotorPompa(1);
                    imageTextDisable(0);
                } else if(oldSecilenPompaVal < 28.1 && secilenPompaVal >= 28.1) {
                    disableMotorPompa(2);
                    imageTextDisable(0);
                }
            }
            if(secilenPompa != null) {
                tabloGuncelle();
            }
        });

        tankKapasitesiTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!tankKapasitesiTextField.getText().isEmpty()) {
                girilenTankKapasitesiMiktari = Integer.parseInt(newValue);
            }
            initHidrolikKilit();
            if(girilenTankKapasitesiMiktari != 0) {
                tabloGuncelle();
            }
        });

        hidrolikKilitComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            secilenHidrolikKilitDurumu = newValue;
            if(secilenPompa != null) {
                String[] secPmp = secilenPompa.split(" cc");
                float secilenPompaVal = Float.parseFloat(secPmp[0]);
                if(Objects.equals(secilenHidrolikKilitDurumu, "Var")) {
                    System.out.println("Secilen Pompa: " + secilenPompaVal);
                    if(secilenPompaVal > 28.1) {
                        initValf(0);
                    } else {
                        initValf(1);
                    }
                } else {
                    initValf(0);
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
                String[] secPmp = secilenPompa.split(" cc");
                float secilenPompaVal = Float.parseFloat(secPmp[0]);
                if(Objects.equals(secilenHidrolikKilitDurumu, "Var")) {
                    if(secilenPompaVal > 28.1) {
                        initKilitMotor();
                    } else {
                        sogutmaComboBox.setDisable(false);
                        initSogutma();
                    }
                } else {
                    initSogutma();
                }
            }
            if(secilenValfTipi != null) {
                tabloGuncelle();
            }
        });

        kilitMotorComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            secilenKilitMotor = kilitMotorComboBox.getValue();
            initKilitPompa();
            if(secilenKilitMotor != null) {
                tabloGuncelle();
            }
        });

        kilitPompaComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            secilenKilitPompa = kilitPompaComboBox.getValue();
            initSogutma();
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
        pompaComboBox.getSelectionModel().clearSelection();
        hidrolikKilitComboBox.getSelectionModel().clearSelection();
        valfTipiComboBox.getSelectionModel().clearSelection();
        kilitMotorComboBox.getSelectionModel().clearSelection();
        kilitPompaComboBox.getSelectionModel().clearSelection();
        sogutmaComboBox.getSelectionModel().clearSelection();

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

        imageTextDisable(0);
    }

    private void imageTextDisable(int stat) {
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
        }
    }

    private void imageTextEnable() {
        if(secilenKilitMotor != null) {
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
            if(secilenKampana == 250) {
                kampanaVeri2Text.setText("Kampana: " + secilenKampana + "\nKesim Çapı: Ø" + 173);
            } else if(secilenKampana == 300) {
                kampanaVeri2Text.setText("Kampana: " + secilenKampana + "\nKesim Çapı: Ø" + 236);
            } else if(secilenKampana == 350) {
                kampanaVeri2Text.setText("Kampana: " + secilenKampana + "\nKesim Çapı: Ø" + 263);
            } else if(secilenKampana == 400) {
                kampanaVeri2Text.setText("Kampana: " + secilenKampana + "\nKesim Çapı: Ø" + " NaN");
            }
            kilitMotorIcOlcuText.setVisible(true);
            kilitMotorIcOlcuText.setText("Boğaz: Ø200\nKesim: Ø??");
            kilitMotorVeriText.setVisible(true);
            kilitMotorVeriText.setText("Kilit Motor: " + secilenKilitMotor + "\nKilit Pompa: " + secilenKilitPompa);
            kilitliBlokVeri2Text.setVisible(true);
            kilitliBlokVeri2Text.setText("Kilitli Blok: \n" + secilenValfTipi);
        } else {
            kampanaOlcuText.setVisible(true);
            kampanaOlcuText2.setVisible(true);
            kilitliBlokOlcuText.setVisible(true);
            kilitliBlokOlcuText2.setVisible(true);
            dolumOlcuText.setVisible(true);
            dolumOlcuText2.setVisible(true);
            tahliyeOlcuText.setVisible(true);
            kampanaVeriText.setVisible(true);
            if(secilenKampana == 250) {
                kampanaVeriText.setText("Kampana: " + secilenKampana + "\nKesim Çapı: Ø" + 173);
            } else if(secilenKampana == 300) {
                kampanaVeriText.setText("Kampana: " + secilenKampana + "\nKesim Çapı: Ø" + 236);
            } else if(secilenKampana == 350) {
                kampanaVeriText.setText("Kampana: " + secilenKampana + "\nKesim Çapı: Ø" + 263);
            } else if(secilenKampana == 400) {
                kampanaVeriText.setText("Kampana: " + secilenKampana + "\nKesim Çapı: Ø" + " NaN");
            }
            kilitliBlokVeriText.setVisible(true);
            kilitliBlokVeriText.setText("Kilitli Blok: \n" + secilenValfTipi);
        }
    }

    private void sonucEkraniTemizle() {
        yukseklikSonucText.setVisible(false);
        genislikSonucText.setVisible(false);
        derinlikSonucText.setVisible(false);
        hacimText.setVisible(false);
        hydraulicUnitShape.setVisible(false);
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

        image = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("icons/tanklar/kilitsiz.png")));
        sonucTankGorsel.setImage(image);

        derinlikSonucText.setVisible(false);
        genislikSonucText.setVisible(false);
        hacimText.setVisible(false);
        yukseklikSonucText.setVisible(false);
        hydraulicUnitShape.setVisible(false);
    }
}