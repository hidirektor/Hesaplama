package me.t3sl4.hesaplama.ui;

import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Box;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import me.t3sl4.hesaplama.Launcher;
import me.t3sl4.hesaplama.hydraulic.TableData;

import java.io.IOException;
import java.util.*;
import java.util.function.UnaryOperator;

public class MainController {
    @FXML
    private ComboBox<String> uniteTipiComboBox;

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
    private Button temizleButton;

    @FXML
    private Text kullanilacakKabin;

    @FXML
    private TableView<TableData> sonucTablo;

    @FXML
    private TableColumn<TableData, String> sonucTabloSatir1;

    @FXML
    private TableColumn<TableData, String> sonucTabloSatir2;

    @FXML
    private Button parametreKontrol;

    @FXML
    private TextField siparisNumarasi;

    @FXML
    private Text sonucAnaLabelTxt;

    @FXML
    private ImageView sonucKapakImage;

    @FXML
    private Text testOlcu;

    /*
    Seçilen Değerler:
     */
    private String girilenSiparisNumarasi = null;
    private String secilenUniteTipi = null;
    private String secilenMotor = null;
    private int secilenKampana = 0;
    private String secilenPompa = null;
    private int girilenTankKapasitesiMiktari = 0;
    private String secilenHidrolikKilitDurumu = null;
    private String secilenValfTipi = null;
    private String secilenKilitMotor = null;
    private String secilenKilitPompa = null;
    private String secilenSogutmaDurumu = null;

    boolean hidrolikKilitStat = false;
    boolean sogutmaStat = false;



    List<String> kampanaVerileri = new ArrayList<>();
    List<String> motorYukseklikVerileri = new ArrayList<>();
    HashMap<Object, int[]> kabinOlculeri = new HashMap<Object, int[]>();
    HashMap<Object, int[]> kabinListesi = new HashMap<Object, int[]>();
    int[] kampanaDegerleri = {250, 300, 250, 300, 350, 300, 350, 350, 350, 400};

    int hesaplananHacim = 0;

    //boşluklar:
    int kampanaBoslukX = 30;
    int kampanaBoslukY = 30;

    int valfBoslukX = 50;
    int valfBoslukYArka = 50;
    int valfBoslukYOn = 50;

    int kilitliBlokAraBoslukX = 100;

    int tekHizAraBoslukX = 50;
    int ciftHizAraBoslukX = 50;
    int kompanzasyonTekHizAraBoslukX = 100;

    int sogutmaAraBoslukX = 200;
    int sogutmaAraBoslukYkOn = 100;
    int sogutmaAraBoslukYkArka = 40;

    int kilitMotorKampanaBosluk = 100;
    int kilitMotorMotorBoslukX = 100; //hidros kilit motor kampana ile tank dış ölçüsü ara boşluğu
    int kilitMotorBoslukYOn = 100;
    int kilitMotorBoslukYArka = 40;

    int kayipLitre = 7;
    private HostServices hostServices;

    public void initialize() {
        textFilter();
        defineKabinOlcu();
        comboBoxListener();
        sonucTabloSatir1.setCellValueFactory(new PropertyValueFactory<>("satir1Property"));
        sonucTabloSatir2.setCellValueFactory(new PropertyValueFactory<>("satir2Property"));
    }

    @FXML
    public void hesaplaFunc() {
        initKampana();
        int h = 0; //Yükseklik
        int y = 0; //Derinlik
        int x = 0; //Genişlik
        int hacim = 0; //Hacim
        int[] results;
        if (checkComboBox()) {
            showErrorMessage();
        } else {
            enableSonucSection();
            results = calcDimensions(x, y, h, kampanaDegerleri);
            x = results[0];
            y = results[1];
            h = results[2];
            hacim = results[3];
            genislikSonucText.setText("X: " + x + " mm");
            derinlikSonucText.setText("Y: " + y + " mm");
            yukseklikSonucText.setText("h: " + h + " mm");
            hacimText.setText("Tank : " + hacim + "L");

            tabloGuncelle();
            Image image = new Image(Launcher.class.getResourceAsStream("/icons/test.png"));
            sonucKapakImage.setImage(image);
            testOlcu.setVisible(true);
        }
    }

    int[] calcDimensions(int x, int y, int h, int[] kampanaDegerleri) {
        int eskiX=0, eskiY=0, eskiH=0;

        //hesaplama kısmı:
        int[] finalValues = new int[4];
        int yV = 0;
        int yK = 0;
        System.out.println("--------Hesaplama Başladı--------Ø");
        secilenKampana = kampanaDegerleri[motorComboBox.getSelectionModel().getSelectedIndex()];
        String[] secPmp = secilenPompa.split(" cc");
        x += kampanaDegerleri[motorComboBox.getSelectionModel().getSelectedIndex()] + kampanaBoslukX;
        yK += kampanaDegerleri[motorComboBox.getSelectionModel().getSelectedIndex()] + kampanaBoslukY + kampanaBoslukY;
        System.out.println("Motor + Kampana için:");
        System.out.println("X += " + kampanaDegerleri[motorComboBox.getSelectionModel().getSelectedIndex()] + " (Kampana) " + kampanaBoslukX + " (Kampana Boşluk)");
        System.out.println("yK += " + kampanaDegerleri[motorComboBox.getSelectionModel().getSelectedIndex()] + " (Kampana) + " + kampanaBoslukY + " (Kampana Boşluk) + " + kampanaBoslukY + " (Kampana Boşluk)");

        float secilenPompaVal = Float.parseFloat(secPmp[0]);
        //hidrolik kilit seçiliyse: valf tipi = kilitli blok olarak gelicek
        //kilitli blok ölçüsü olarak: X'e +100 olacak
        if(Objects.equals(secilenHidrolikKilitDurumu, "Var") && Objects.equals(secilenValfTipi, "Kilitli Blok || Çift Hız")) {
            x += 120 + kilitliBlokAraBoslukX + valfBoslukX;
            yV += 190 + valfBoslukYArka + valfBoslukYOn;
            System.out.println("Kilitli Blok için:");
            System.out.println("X += " + kilitliBlokAraBoslukX + " (Ara Boşluk) + " + valfBoslukX + " (Valf Boşluk)");
            System.out.println("yV += " + valfBoslukYArka + " (Valf Boşluk Arka) + " + valfBoslukYOn + " (Valf Boşluk Ön)");
        }
        //hidrolik kilit olmadığı durumlarda valf tipleri için
        if(Objects.equals(secilenHidrolikKilitDurumu, "Yok")) {
            if(Objects.equals(secilenValfTipi, "İnişte Tek Hız")) {
                // X yönünde +120 olacak Y yönünde 180 mm eklenecek
                x += 70 + valfBoslukX + tekHizAraBoslukX;
                yV += 180 + valfBoslukYOn + valfBoslukYArka;
                System.out.println("İnişte Tek Hız İçin: (Hidrolik Kilit Yok)");
                System.out.println("X += " + valfBoslukX + " (Valf Boşluk) + " + tekHizAraBoslukX + " (Tek Hız Boşluk)");
                System.out.println("yV += " + valfBoslukYOn + " (Valf Boşluk Ön) + " + valfBoslukYArka + " (Valf Boşluk Arka)");
            } else if(Objects.equals(secilenValfTipi, "İnişte Çift Hız")) {
                //X yönünde 190 Y yönünde 90
                x += 140 + ciftHizAraBoslukX + valfBoslukX;
                yV += 90 + valfBoslukYOn + valfBoslukYArka;
                System.out.println("İnişte Çift Hız İçin: (Hidrolik Kilit Yok)");
                System.out.println("X += " + valfBoslukX + " (Valf Boşluk) + " + ciftHizAraBoslukX + " (Tek Hız Boşluk)");
                System.out.println("yV += " + valfBoslukYOn + " (Valf Boşluk Ön) + " + valfBoslukYArka + " (Valf Boşluk Arka)");
            } else {
                //kompanzasyon seçilmişse:
                //kilit yoksa: X'e 190 Y'ye 180
                if(secilenHidrolikKilitDurumu.equals("Yok") && Objects.equals(secilenValfTipi, "Kompanzasyon + İnişte Tek Hız")) {
                    x += 140 + kompanzasyonTekHizAraBoslukX;
                    yV += 180 + valfBoslukYOn + valfBoslukYArka;
                    System.out.println("Kompanzasyon + Tek Hız İçin: (Hidrolik Kilit Yok)");
                    System.out.println("X += " + kompanzasyonTekHizAraBoslukX + " (Kompanzasyon Ara Boşluk)");
                    System.out.println("yV += " + valfBoslukYOn + " (Valf Boşluk Ön) + " + valfBoslukYArka + " (Valf Boşluk Arka)");
                }
            }
        } else {
            if(secilenPompaVal > 28.1) {
                String[] secKilitMotor = secilenKilitMotor.split(" kW");
                float secilenKilitMotorVal = Float.parseFloat(secKilitMotor[0]);
                String[] secKilitPompa = secilenKilitPompa.split(" cc");
                float secilenKilitPompaVal = Float.parseFloat(secKilitPompa[0]);

                if(Objects.equals(secilenValfTipi, "Kompanzasyon + İnişte Tek Hız")) {
                    yV += 180 + valfBoslukYArka + valfBoslukYOn;
                    System.out.println("Kompanzasyon + İnişte Tek Hız (Kilitli Blok) (Pompa > 28.1) için:");
                    System.out.println("yV += " + valfBoslukYOn + " (Valf Boşluk Ön) + " + valfBoslukYArka + " (Valf Boşluk Arka)");
                } else if(Objects.equals(secilenValfTipi, "İnişte Çift Hız")) {
                    System.out.println("İnişte Çift Hız (Kilitli Blok) için:");
                    if(secilenPompaVal > 28.1) {
                        yV += 90 + valfBoslukYOn;
                        System.out.println("(Pompa > 28.1) için:");
                        System.out.println("yV += " + valfBoslukYOn + " (Valf Boşluk Ön)");
                    } else {
                        yV += 90 + valfBoslukYOn + valfBoslukYArka;
                        System.out.println("(Pompa <= 28.1) için:");
                        System.out.println("yV += " + valfBoslukYOn + " (Valf Boşluk Ön) + " + valfBoslukYArka + " (Valf Boşluk Arka)");
                    }
                } else if(Objects.equals(secilenValfTipi, "İnişte Tek Hız")) {
                    yV += 180 + valfBoslukYOn + valfBoslukYArka;
                    System.out.println("İnişte Tek Hız (Kilitli Blok) için:");
                    System.out.println("yV += " + valfBoslukYOn + " (Valf Boşluk Ön) + " + valfBoslukYArka + " (Valf Boşluk Arka)");
                }

                if(secilenKilitMotorVal != 0) {
                    x += 200 + kilitMotorKampanaBosluk + kilitMotorMotorBoslukX;
                    yV += 200 + kilitMotorBoslukYOn + kilitMotorBoslukYArka;
                    System.out.println("Kilit Motor için:");
                    System.out.println("X += " + kilitMotorKampanaBosluk + " (Kampana Boşluk) + " + kilitMotorMotorBoslukX + " (Kilit Mootr Boşluk)");
                    System.out.println("yV += " + kilitMotorBoslukYOn + " (Kilit Motor Ön) + " + kilitMotorBoslukYArka + " (Kilit Motor Arka)");
                }
            }
        }
        if(Objects.equals(secilenSogutmaDurumu, "Var")) {
            x += 350 + sogutmaAraBoslukX;
            yK += 152 + sogutmaAraBoslukYkOn + sogutmaAraBoslukYkArka;
            System.out.println("Soğutma için:");
            System.out.println("X += " + sogutmaAraBoslukX + " (Ara Boşluk)");
            System.out.println("yK += " + sogutmaAraBoslukYkOn + " (Ara Boşluk Ön) + " + sogutmaAraBoslukYkArka + " (Ara Boşluk Arka)");
        }
        y = Math.max(yV, yK);
        if(y <= 350) {
            y = 350;
        }
        if(x <= 550) {
            x = 550;
        }
        h = 300;

        hesaplananHacim = ((x*h*y) / 1000000) - kayipLitre;
        eskiX = x;
        eskiY = y;
        eskiH = h;

        int enKucukLitreFarki = Integer.MAX_VALUE;
        int[] enKucukLitreOlculer = null;
        int mapVal = 0;
        for (int[] olculer : kabinOlculeri.values()) {
            int litre = olculer[3];
            int tempX = olculer[0];
            int tempY = olculer[1];

            if(hesaplananHacim > girilenTankKapasitesiMiktari) {
                if(x <= tempX && y <= tempY) {
                    //enKucukLitreFarki = litre - girilenTankKapasitesiMiktari;
                    enKucukLitreOlculer = olculer;
                }
            } else {
                if (litre >= girilenTankKapasitesiMiktari && litre - girilenTankKapasitesiMiktari < enKucukLitreFarki) {
                    if(hesaplananHacim != litre && hesaplananHacim < litre) {
                        if(x < tempX && y < tempY) {
                            enKucukLitreFarki = litre - girilenTankKapasitesiMiktari;
                            enKucukLitreOlculer = olculer;
                        }
                    }
                }
            }
            mapVal++;
        }

        if (enKucukLitreOlculer != null) {
            x = enKucukLitreOlculer[0];
            y = enKucukLitreOlculer[1];
            h = enKucukLitreOlculer[2];
            hesaplananHacim = enKucukLitreOlculer[3];
        }

        String atananHT = getKeyByValue(kabinOlculeri, enKucukLitreOlculer).toString();
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

        kullanilacakKabin.setText("Kullanmanız Gereken Kabin: " + atananKabin + " Geçiş Ölçüleri: " + gecisOlculeri + " (x, y, h)");
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
        System.out.println("Atanan Hacim: " + hesaplananHacim);
        System.out.println("Kullanmanız Gereken Kabin: " + atananKabin);
        System.out.println("Geçiş Ölçüleri: " + gecisOlculeri);
        System.out.println("-------------------------------");

        finalValues[0] = x;
        finalValues[1] = y;
        finalValues[2] = h;
        finalValues[3] = hesaplananHacim;
        return finalValues;
    }

    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
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
    public void uniteTipiPressed() {
        if(uniteTipiComboBox.getValue() != null) {
            if(uniteTipiComboBox.getSelectionModel().getSelectedItem().matches("Klasik")) {
                motorComboBox.setDisable(false);
            } else {
                disableAllSections();
            }
        }
    }

    @FXML
    public void motorPressed() {
        if(motorComboBox.getValue() != null) {
            secilenMotor = motorComboBox.getValue();
            secilenKampana = kampanaDegerleri[motorComboBox.getSelectionModel().getSelectedIndex()];
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
                initValf(2);
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
            kilitPompaComboBox.getItems().addAll("4.2 cc", "4.8 cc", "5.8 cc");
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
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("popup.fxml"));
            VBox root = fxmlLoader.load();
            PopupController popupController = fxmlLoader.getController();
            popupController.setValues(kampanaBoslukX, kampanaBoslukY,
                    valfBoslukX, valfBoslukYArka, valfBoslukYOn,
                    kilitliBlokAraBoslukX, tekHizAraBoslukX, ciftHizAraBoslukX,
                    kompanzasyonTekHizAraBoslukX, sogutmaAraBoslukX, sogutmaAraBoslukYkOn,
                    sogutmaAraBoslukYkArka, kilitMotorKampanaBosluk, kilitMotorMotorBoslukX,
                    kilitMotorBoslukYOn, kilitMotorBoslukYArka, kayipLitre);
            popupController.showValues();
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setScene(new Scene(root));
            popupStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void temizlemeIslemi() {
        verileriSifirla();
    }

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    private boolean checkComboBox() {
        if(siparisNumarasi.getText().isEmpty() || uniteTipiComboBox.getSelectionModel().isEmpty() || motorComboBox.getSelectionModel().isEmpty() || pompaComboBox.getSelectionModel().isEmpty() || valfTipiComboBox.getSelectionModel().isEmpty() || hidrolikKilitComboBox.getSelectionModel().isEmpty() || sogutmaComboBox.getSelectionModel().isEmpty()) {
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

    private void initUniteTipi() {
        uniteTipiComboBox.setDisable(false);
        uniteTipiComboBox.getItems().clear();
        uniteTipiComboBox.getItems().addAll("Hidros", "Klasik");
    }

    private void initMotor() {
        motorComboBox.getItems().clear();
        motorComboBox.getItems().addAll("4 kW", "5.5 kW", "5.5 kW (Kompakt)", "7.5 kW (Kompakt)", "11 kW", "11 kW (Kompakt)", "15 kW", "18.5 kW", "22 kW", "37 kW");
    }

    private void initKampana() {
        kampanaVerileri.add("250 mm");
        kampanaVerileri.add("300 mm");
        kampanaVerileri.add("250 mm");
        kampanaVerileri.add("300 mm");
        kampanaVerileri.add("350 mm");
        kampanaVerileri.add("300 mm");
        kampanaVerileri.add("350 mm");
        kampanaVerileri.add("350 mm");
        kampanaVerileri.add("350 mm");
        kampanaVerileri.add("400 mm");
    }

    private void initMotorYukseklik() {
        motorYukseklikVerileri.add("345 mm");
        motorYukseklikVerileri.add("375 mm");
        motorYukseklikVerileri.add("365 mm");
        motorYukseklikVerileri.add("410 mm");
        motorYukseklikVerileri.add("500 mm");
        motorYukseklikVerileri.add("470 mm");
        motorYukseklikVerileri.add("540 mm");
        motorYukseklikVerileri.add("565 mm");
        motorYukseklikVerileri.add("565 mm");
        motorYukseklikVerileri.add("600 mm");
    }

    private void initKabinOlculeri(int x, int y, int h, int litre, String key) {
        int[] kabinOlcu = new int[4];
        kabinOlcu[0] = x;
        kabinOlcu[1] = y;
        kabinOlcu[2] = h;
        kabinOlcu[3] = litre;
        kabinOlculeri.put(key, kabinOlcu);
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
        if(Objects.equals(uniteTipiComboBox.getValue(), "Hidros")) {
            pompaComboBox.getItems().addAll("1.1 cc", "1.6 cc", "2.1 cc", "2.7 cc", "3.2 cc", "3.7 cc", "4.2 cc", "4.8 cc", "5.8 cc", "7 cc", "8 cc", "9 cc");
        } else if(Objects.equals(uniteTipiComboBox.getValue(), "Klasik")) {
            pompaComboBox.getItems().addAll("9.5 cc", "11.9 cc", "14 cc", "14.6 cc", "16.8 cc", "19.2 cc", "22.9 cc", "28.1 cc", "28.8 cc", "33.3 cc", "37.9 cc", "42.6 cc", "45.5 cc", "49.4 cc", "56.1 cc");
        } else {
            pompaComboBox.getItems().addAll("1.1 cc", "1.6 cc", "2.1 cc", "2.7 cc", "3.2 cc", "3.7 cc", "4.2 cc", "4.8 cc", "5.8 cc", "7 cc", "8 cc", "9 cc", "9.5 cc", "11.9 cc", "14 cc", "14.6 cc", "16.8 cc", "19.2 cc", "22.9 cc", "28.1 cc", "28.8 cc", "33.3 cc", "37.9 cc", "42.6 cc", "45.5 cc", "49.4 cc", "56.1 cc");
        }
    }

    private void initValf(int stat) {
        valfTipiComboBox.getItems().clear();
        valfTipiComboBox.setDisable(false);
        if(stat == 1) {
            valfTipiComboBox.getItems().addAll("Kilitli Blok || Çift Hız");
        } else {
            valfTipiComboBox.getItems().addAll("İnişte Tek Hız", "İnişte Çift Hız", "Kompanzasyon + İnişte Tek Hız");
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
        kilitMotorComboBox.getItems().addAll("1.5 kW", "2.2 kW");
    }

    private void initKilitPompa() {
        kilitPompaComboBox.setDisable(false);
        kilitPompaComboBox.getItems().clear();
        kilitPompaText.setVisible(true);
        kilitPompaComboBox.setVisible(true);
        kilitPompaComboBox.getItems().addAll("4.2 cc", "4.8 cc", "5.8 cc");
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
        if(hesaplananHacim == 0) {
            hacimText.setVisible(true);
        }
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
            initUniteTipi();
            if(girilenSiparisNumarasi != null) {
                tabloGuncelle();
            }
        });

        uniteTipiComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            secilenUniteTipi = newValue;
            initMotor();
            if(secilenUniteTipi != null) {
                tabloGuncelle();
            }
        });

        motorComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if(!motorComboBox.getItems().isEmpty()) {
                secilenMotor = newValue;
                secilenKampana = kampanaDegerleri[motorComboBox.getSelectionModel().getSelectedIndex()];
                initPompa();
                if(secilenMotor != null) {
                    tabloGuncelle();
                }
            }
        });

        pompaComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            secilenPompa = newValue;
            if(oldValue != null) {
                String[] oldSecPmp = oldValue.split(" cc");
                float oldSecilenPompaVal = Float.parseFloat(oldSecPmp[0]);
                String[] secPmp = secilenPompa.split(" cc");
                float secilenPompaVal = Float.parseFloat(secPmp[0]);
                if(oldSecilenPompaVal > 28.1 && secilenPompaVal < 28.1) {
                    disableMotorPompa(1);
                } else if(oldSecilenPompaVal < 28.1 && secilenPompaVal > 28.1) {
                    disableMotorPompa(2);
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
                }
            }
            if(secilenPompa != null) {
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
        uniteTipiComboBox.getSelectionModel().clearSelection();
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
        secilenSogutmaDurumu = null;

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

        data = new TableData("Hidrolik Ünitesi Tipi:", uniteTipiComboBox.getValue());
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

        if(secilenKilitMotor != null) {
            data = new TableData("Kilit Motoru:", secilenKilitMotor);
        } else {
            data = new TableData("Kilit Motoru:", "Yok");
        }
        sonucTablo.getItems().add(data);

        if(secilenKilitPompa != null) {
            data = new TableData("Kilit Pompa:", secilenKilitPompa);
        } else {
            data = new TableData("Kilit Pompa:", "Yok");
        }
        sonucTablo.getItems().add(data);

        data = new TableData("Soğutma Durumu:", secilenSogutmaDurumu);
        sonucTablo.getItems().add(data);
    }

    private void showErrorMessage() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Hata");
        alert.setHeaderText(null);
        alert.setContentText("Lütfen tüm girdileri kontrol edin.");
        alert.showAndWait();
    }
}