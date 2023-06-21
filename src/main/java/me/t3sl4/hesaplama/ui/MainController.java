package me.t3sl4.hesaplama.ui;

import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Box;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    private TextArea hidrolikUnitesiTextArea;

    @FXML
    private TextArea parcaListesiTextArea;

    @FXML
    private Text hidrolikUnitesiText;

    @FXML
    private Text parcaListesiText;

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

    private String secilenMotor = null;
    private int secilenKampana = 0;
    private String secilenPompa = null;
    private int girilenTankKapasitesiMiktari = 0;
    private String secilenValfTipi = null;
    private String secilenHidrolikKilitDurumu = null;
    private String secilenSogutmaDurumu = null;
    boolean hidrolikKilitStat = false;
    boolean sogutmaStat = false;
    private String secilenKilitMotor = null;
    private String secilenKilitPompa = null;
    List<String> kampanaVerileri = new ArrayList<>();

    private HostServices hostServices;

    public void initialize() {
        textFilter();
        initUniteTipi();
        comboBoxListener();
    }

    @FXML
    public void hesaplaFunc() {
        initKampana();
        int h = 0; //Yükseklik
        int y = 0; //Derinlik
        int x = 0; //Genişlik
        int[] kampanaDegerleri = {250, 300, 250, 300, 350, 300, 350, 350, 350, 400};
        int[] results;
        if (checkComboBox()) {
            showErrorMessage();
        } else {
            enableSonucSection();
            results = calcDimensions(x, y, h, kampanaDegerleri);
            x = results[0];
            y = results[1];
            h = results[2];
            genislikSonucText.setText("X: " + x + " mm");
            derinlikSonucText.setText("Y: " + y + " mm");
            yukseklikSonucText.setText("Yükseklik: " + h + " mm");
            String secim =
                    "Hidrolik Ünitesi Tipi: " + uniteTipiComboBox.getValue() + "\n" +
                            "Seçilen Motor: " + secilenMotor + "\n" +
                            "Kampana: " + secilenKampana + "\n" +
                            "Seçilen Pompa: " + secilenPompa + "\n" +
                            "Tank Kapasitesi: " + girilenTankKapasitesiMiktari + "\n" +
                            "Seçilen Valf Tipi: " + secilenValfTipi + "\n" +
                            "Hidrolik Kilit Durumu: " + secilenHidrolikKilitDurumu + "\n" +
                            "Soğutma Durumu: " + secilenSogutmaDurumu + "\n";
            hidrolikUnitesiTextArea.setText(secim);
            //kilit motoru değeri
        }
    }

    int[] calcDimensions(int x, int y, int h, int[] kampanaDegerleri) {
        //boşluklar:
        int kampanaBoslukX = 30;
        int kampanaBoslukY = 30;

        int valfBoslukX = 50;
        int valfBoslukYArka = 50;
        int valfBoslukYOn = 50;

        int kilitliBlokAraBoslukX = 100;

        int tekHizAraBoslukX = 50;
        int ciftHizAraBoslukX = 50;
        int kompanzasyonCiftHizAraBoslukX = 100;

        int sogutmaAraBoslukX = 200;
        int sogutmaAraBoslukYkOn = 100;
        int sogutmaAraBoslukYkArka = 40;

        int kilitMotorKampanaBosluk = 100;
        int kilitMotorMotorBoslukX = 100; //kampana ile tank dış ölçüsü ara boşluğu
        int kilitMotorBoslukYOn = 100;
        int kilitMotorBoslukYArka = 40;

        int kayipLitre = 7;
        //hesaplama kısmı:
        int[] finalValues = new int[3];
        int yV = 0;
        int yK = 0;
        System.out.println("--------Hesaplama Başladı--------");
        secilenKampana = kampanaDegerleri[motorComboBox.getSelectionModel().getSelectedIndex()];
        String[] secPmp = secilenPompa.split(" cc");
        x += kampanaDegerleri[motorComboBox.getSelectionModel().getSelectedIndex()] + kampanaBoslukX;
        yK += kampanaDegerleri[motorComboBox.getSelectionModel().getSelectedIndex()] + kampanaBoslukY + kampanaBoslukY;
        System.out.println("Kampana Boşluk X: " + kampanaBoslukX + " Kampana Boşluk Y: " + kampanaBoslukY);
        System.out.println("Motor + Kampana için (X): " + x + " yK: " + yK);

        float secilenPompaVal = Float.parseFloat(secPmp[0]);
        //hidrolik kilit seçiliyse: valf tipi = kilitli blok olarak gelicek
        //kilitli blok ölçüsü olarak: X'e +100 olacak
        if(Objects.equals(secilenHidrolikKilitDurumu, "Var") && Objects.equals(secilenValfTipi, "Kilitli Blok || Çift Hız")) {
            x += 120 + kilitliBlokAraBoslukX + valfBoslukX;
            yV += 190 + valfBoslukYArka + valfBoslukYOn;
            System.out.println("Kilitli Blok Ara Boşluk X: " + kilitliBlokAraBoslukX + " Valf Boşluk X: " + valfBoslukX);
            System.out.println("Valf Boşluk Y Arka: " + valfBoslukYArka + " Valf Boşluk Y Ön: " + valfBoslukYOn);
            System.out.println("Hidorlik Kilit + Kilitli Blok Aktif X: " + x + " yV: " + yV);
        }
        //hidrolik kilit olmadığı durumlarda valf tipleri için
        if(Objects.equals(secilenHidrolikKilitDurumu, "Yok")) {
            if(Objects.equals(secilenValfTipi, "İnişte Tek Hız")) {
                // X yönünde +120 olacak Y yönünde 180 mm eklenecek
                x += 70 + valfBoslukX + tekHizAraBoslukX;
                yV += 180 + valfBoslukYOn + valfBoslukYArka;
                System.out.println("Valf Boşluk X: " + valfBoslukX + " Tek Hız Boşluk: " + tekHizAraBoslukX);
                System.out.println("Valf Boşluk Y Ön: " + valfBoslukYOn + " Valf Boşluk Y Arka: " + valfBoslukYArka);
                System.out.println("Hidrolik Kilit Yok + İnişte Tek Hız X: " + x + " yV: " + yV);
            } else if(Objects.equals(secilenValfTipi, "İnişte Çift Hız")) {
                //X yönünde 190 Y yönünde 90
                x += 140 + ciftHizAraBoslukX + valfBoslukX;
                yV += 90 + valfBoslukYOn + valfBoslukYArka;
                System.out.println("Valf Boşluk X: " + valfBoslukX + " Çift Hız Boşluk: " + ciftHizAraBoslukX);
                System.out.println("Valf Boşluk Y Ön: " + valfBoslukYOn + " Valf Boşluk Y Arka: " + valfBoslukYArka);
                System.out.println("Hidrolik Kilit Yok + İnişte Çift Hız X: " + x + " yV: " + yV);
            } else {
                //kompanzasyon seçilmişse:
                //kilit yoksa: X'e 190 Y'ye 180
                if(secilenHidrolikKilitDurumu.equals("Yok") && Objects.equals(secilenValfTipi, "Kompanzasyon + İnişte Tek Hız")) {
                    x += 140 + kompanzasyonCiftHizAraBoslukX;
                    yV += 180 + valfBoslukYOn + valfBoslukYArka;
                    System.out.println("Kompanzasyon Ara Boşluk: " + kompanzasyonCiftHizAraBoslukX);
                    System.out.println("Valf Boşluk Y Ön: " + valfBoslukYOn + " Valf Boşluk Y Arka: " + valfBoslukYArka);
                    System.out.println("Hidrolik Kilit Yok + (Kompanzasyon + İnişte Tek Hız) X: " + x + " yV: " + yV);
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
                    System.out.println("Valf Boşluk Y Ön: " + valfBoslukYOn + " Valf Boşluk Y Arka: " + valfBoslukYArka);
                    System.out.println("Hidrolik Kilit Var + (Kompanzasyon + İnişte Tek Hız) X: " + x + " yV: " + yV);
                } else if(Objects.equals(secilenValfTipi, "İnişte Çift Hız")) {
                    yV += 90 + valfBoslukYOn + valfBoslukYArka;
                    System.out.println("Valf Boşluk Y Ön: " + valfBoslukYOn + " Valf Boşluk Y Arka: " + valfBoslukYArka);
                    System.out.println("Hidrolik Kilit Var + İnişte Çift Hız X: " + x + " yV: " + yV);
                } else if(Objects.equals(secilenValfTipi, "İnişte Tek Hız")) {
                    yV += 180 + valfBoslukYOn + valfBoslukYArka;
                    System.out.println("Valf Boşluk Y Ön: " + valfBoslukYOn + " Valf Boşluk Y Arka: " + valfBoslukYArka);
                    System.out.println("Hidrolik Kilit Var + İnişte Tek Hız X: " + x + " yV: " + yV);
                }

                if(secilenKilitMotorVal != 0) {
                    x += 200 + kilitMotorKampanaBosluk + kilitMotorMotorBoslukX;
                    yV += 200 + kilitMotorBoslukYOn + kilitMotorBoslukYArka;
                    System.out.println("Kilit Motor Kampana Boşluk X: " + kilitMotorKampanaBosluk + " Kilit Motor Boşluk X: " + kilitMotorMotorBoslukX);
                    System.out.println("Kilit Motor Boşluk Y: " + kilitMotorBoslukYOn + " Kilit Motor Boşluk Y Arka: " + kilitMotorBoslukYArka);
                    System.out.println("Kilit Motor İçin X: " + x + " yV: " + yV);
                }
            }
        }
        if(Objects.equals(secilenSogutmaDurumu, "Var")) {
            x += 350 + sogutmaAraBoslukX;
            yK += 152 + sogutmaAraBoslukYkOn + sogutmaAraBoslukYkArka;
            System.out.println("Soğutma Ara Boşluk X: " + sogutmaAraBoslukX);
            System.out.println("Soğutma Ara Boşluk Y Ön: " + sogutmaAraBoslukYkOn + " Soğutma Ara Boşluk  Y Arka: " + sogutmaAraBoslukYkArka);
            System.out.println("Soğutma Var X: " + x + " Y: " + y);
        }
        System.out.println("yV ve yK için en büyüğü: yV: " + yV + " yK: " + yK + " Y: " + y);
        y = Math.max(yV, yK);
        if(y <= 350) {
            y = 350;
        }
        if(x <= 550) {
            x = 550;
        }
        h = 300;

        int hesaplananHacim = ((x*h*y) / 1000000) - kayipLitre;
        System.out.println("yK: " + yK + " yV" + yV);
        System.out.println(hesaplananHacim + " L");
        System.out.println("Boşluklar sonrası: X: " + x + " Y: " + y + " h: " + h);
        hacimText.setVisible(true);
        hacimText.setText("Hacim: " + hesaplananHacim + "L");
        System.out.println("--------Hesaplama Bitti--------");
        finalValues[0] = x;
        finalValues[1] = y;
        finalValues[2] = h;
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
    public void temizlemeIslemi() {
        verileriSifirla();
    }

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    private boolean checkComboBox() {
        if(uniteTipiComboBox.getSelectionModel().isEmpty() || motorComboBox.getSelectionModel().isEmpty() || pompaComboBox.getSelectionModel().isEmpty() || valfTipiComboBox.getSelectionModel().isEmpty() || hidrolikKilitComboBox.getSelectionModel().isEmpty() || sogutmaComboBox.getSelectionModel().isEmpty()) {
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

    private void initPompa() {
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

    private void enableSonucSection() {
        parcaListesiTextArea.setVisible(true);
        hidrolikUnitesiTextArea.setVisible(true);
        parcaListesiText.setVisible(true);
        hidrolikUnitesiText.setVisible(true);
        genislikSonucText.setVisible(true);
        yukseklikSonucText.setVisible(true);
        derinlikSonucText.setVisible(true);
        hydraulicUnitShape.setVisible(true);
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
        uniteTipiComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            initMotor();
        });

        motorComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            secilenMotor = newValue;
            initPompa();
        });

        pompaComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            secilenPompa = newValue;
        });

        tankKapasitesiTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!tankKapasitesiTextField.getText().isEmpty()) {
                girilenTankKapasitesiMiktari = Integer.parseInt(newValue);
            }
            initHidrolikKilit();
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
        });

        kilitMotorComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            secilenKilitMotor = kilitMotorComboBox.getValue();
            initKilitPompa();
        });

        kilitPompaComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            secilenKilitPompa = kilitPompaComboBox.getValue();
            initSogutma();
        });

        sogutmaComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            secilenSogutmaDurumu = sogutmaComboBox.getValue();
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
    }

    private void sonucEkraniTemizle() {
        hidrolikUnitesiText.setVisible(false);
        hidrolikUnitesiTextArea.clear();
        hidrolikUnitesiTextArea.setVisible(false);
        parcaListesiText.setVisible(false);
        parcaListesiTextArea.clear();
        parcaListesiTextArea.setVisible(false);
        yukseklikSonucText.setVisible(false);
        genislikSonucText.setVisible(false);
        derinlikSonucText.setVisible(false);
        hacimText.setVisible(false);
        hydraulicUnitShape.setVisible(false);
    }

    private void showErrorMessage() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Hata");
        alert.setHeaderText(null);
        alert.setContentText("Lütfen tüm girdileri kontrol edin.");
        alert.showAndWait();
    }
}