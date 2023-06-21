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

    private String secilenMotor;
    private String secilenPompa;
    private int girilenTankKapasitesiMiktari;
    private String secilenValfTipi;
    private String secilenHidrolikKilitDurumu;
    private String secilenSogutmaDurumu;
    boolean hidrolikKilitStat = false;
    boolean sogutmaStat = false;
    private String secilenKilitMotor;
    private String secilenKilitPompa;

    //Excel2List
    List<String> kampanaVerileri = new ArrayList<>();

    private HostServices hostServices;

    public void initialize() {
        initUniteTipi();
        initMotor();
        initPompa();
        initValf(1);
        initHidrolikKilit();
        initSogutma();

        //readExcelData();
    }

    @FXML
    public void hesaplaFunc() {
        initKampana();
        int h = 0; //Yükseklik
        int y = 0; //Derinlik
        int x = 0; //Genişlik
        int[] kampanaDegerleri = {250, 250, 300, 300, 350, 350, 350, 400};
        int[] results;
        if (checkComboBox()) {
            showErrorMessage("Hata", "Lütfen tüm girdileri kontrol edin.");
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
                            "Kampana: " + "NaN\n" +
                            "Seçilen Pompa: " + secilenPompa + "\n" +
                            "Tank Kapasitesi: " + girilenTankKapasitesiMiktari + "\n" +
                            "Seçilen Valf Tipi: " + secilenValfTipi + "\n" +
                            "Hidrolik Kilit Durumu: " + secilenHidrolikKilitDurumu + "\n" +
                            "Soğutma Durumu: " + secilenSogutmaDurumu + "\n";
            hidrolikUnitesiTextArea.setText(secim);
        }
    }

    int[] calcDimensions(int x, int y, int h, int[] kampanaDegerleri) {
        int[] finalValues = new int[3];
        int yV = 0;
        int yK = 0;
        String[] secPmp = secilenPompa.split(" cc");
        x += kampanaDegerleri[motorComboBox.getSelectionModel().getSelectedIndex()];
        yK += kampanaDegerleri[motorComboBox.getSelectionModel().getSelectedIndex()] + 140;
        System.out.println("Motor + Kampana X: " + x);

        float secilenPompaVal = Float.parseFloat(secPmp[0]);
        if(secilenPompaVal > 28.1) {
            //silinecek
        } else {
            //hidrolik kilit seçiliyse: valf tipi = kilitli blok olarak gelicek
            //kilitli blok ölçüsü olarak: X'e +100 olacak
            if(secilenHidrolikKilitDurumu == "Var" && secilenValfTipi == "Kilitli Blok || Çift Hız") {
                x += 300;
                yV += 280;
                System.out.println("Pompa <= 28.1 && Hidorlik Kilit + Kilitli Blok Aktif X: " + x);
            }
        }
        //hidrolik kilit olmadığı durumlarda valf tipleri için
        if(secilenHidrolikKilitDurumu == "Yok") {
            if(secilenValfTipi == "İnişte Tek Hız") {
                // X yönünde +120 olacak Y yönünde 180 mm eklenecek
                x += 120;
                yV += 180 + 80;
                System.out.println("Hidrolik Kilit Yok + İnişte Tek Hız X: " + x + " Y: " + y);
            } else if(secilenValfTipi == "İnişte Çift Hız") {
                //X yönünde 190 Y yönünde 90
                x += 190;
                yV += 90 + 80;
                System.out.println("Hidrolik Kilit Yok + İnişte Çift Hız X: " + x + " Y: " + y);
            } else {
                //kompanzasyon seçilmişse:
                //kilit yoksa: X'e 190 Y'ye 180
                if(secilenHidrolikKilitDurumu == "Yok" && secilenValfTipi == "Kompanzasyon + İnişte Tek Hız") {
                    x += 190;
                    yV += 180 + 80;
                    System.out.println("Hidrolik Kilit Yok + (Kompanzasyon + İnişte Tek Hız) X: " + x + " Y: " + y);
                }
            }
        } else {
            if(secilenPompaVal > 28.1) {
                String[] secKilitMotor = secilenKilitMotor.split(" kW");
                float secilenKilitMotorVal = Float.parseFloat(secKilitMotor[0]);
                String[] secKilitPompa = secilenKilitPompa.split(" cc");
                float secilenKilitPompaVal = Float.parseFloat(secKilitPompa[0]);

                if(secilenValfTipi == "Kompanzasyon + İnişte Tek Hız") {
                    yV += 180 + 80;
                    System.out.println("Hidrolik Kilit Var + (Kompanzasyon + İnişte Tek Hız) X: " + x + " Y: " + y);
                } else if(secilenValfTipi == "İnişte Çift Hız") {
                    yV += 90 + 80;
                    System.out.println("Hidrolik Kilit Var + İnişte Çift Hız X: " + x + " Y: " + y);
                } else if(secilenValfTipi == "İnişte Tek Hız") {
                    yV += 180 + 80;
                    System.out.println("Hidrolik Kilit Var + İnişte Tek Hız X: " + x + " Y: " + y);
                }

                if(secilenKilitMotorVal != 0) {
                    yV += 300;
                    x += 360;
                }
            }
        }
        if(secilenSogutmaDurumu == "Var") {
            x += 550;
            y += 250;
            System.out.println("Soğutma Var X: " + x + " Y: " + y);
        }
        if(yV <= yK) {
            y = yK;
        } else {
            y = yV;
        }
        h = 280;
        x += 110;
        System.out.println("yK: " + yK + " yV" + yV);
        System.out.println(((x*h*y) / 1000000) + " L");
        System.out.println("Boşluklar sonrası: X: " + x + " Y: " + y + " h: " + h);
        hacimText.setVisible(true);
        hacimText.setText("Hacim: " + ((x*h*y) / 1000000) + "L");
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
        if(uniteTipiComboBox.getSelectionModel().getSelectedItem().matches("Klasik")) {
            motorComboBox.setDisable(false);
        } else {
            disableAllSections();
        }
    }

    @FXML
    public void motorPressed() {
        secilenMotor = motorComboBox.getValue();
        pompaComboBox.setDisable(false);
    }

    @FXML
    public void pompaPressed() {
        secilenPompa = pompaComboBox.getValue();
        tankKapasitesiTextField.setDisable(false);
        tankKapasitesiTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.isEmpty()) {
                valfTipiComboBox.setDisable(true);
                disableKilitAndSogutma();
            }
            System.out.println("Old: " + oldValue + " New: " + newValue);
        });
    }

    @FXML
    public void tankKapasitesiEntered() {
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
        secilenHidrolikKilitDurumu = hidrolikKilitComboBox.getValue();
        valfTipiComboBox.setDisable(false);
        hidrolikKilitStat = true;
        if(secilenHidrolikKilitDurumu == "Yok") {
            initValf(0);
        } else {
            initValf(2);
        }
        hidrolikKilitComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            String[] secPmp = secilenPompa.split(" cc");
            float secilenPompaVal = Float.parseFloat(secPmp[0]);
            if(newValue == "Var") {
                if(secilenPompaVal > 28.1) {
                    initValf(0);
                } else {
                    initValf(1);
                }
            } else {
                initValf(0);
            }
        });
    }

    @FXML
    public void valfTipiPressed() {
        secilenValfTipi = valfTipiComboBox.getValue();
        String[] secPmp = secilenPompa.split(" cc");
        float secilenPompaVal = Float.parseFloat(secPmp[0]);

        if(secilenHidrolikKilitDurumu == "Var" && secilenPompaVal > 28.1) {
            kilitMotorText.setVisible(true);
            kilitPompaText.setVisible(true);
            kilitMotorComboBox.setVisible(true);
            kilitPompaComboBox.setVisible(true);
            kilitPompaComboBox.setDisable(true);
            kilitMotorComboBox.getItems().addAll("1.5 kW", "2.2 kW");
        } else {
            sogutmaComboBox.setDisable(false);
            sogutmaStat = true;
        }
    }

    @FXML
    public void kilitMotorPressed() {
        kilitPompaComboBox.setDisable(false);
        kilitPompaComboBox.getItems().addAll("4.2 cc", "4.8 cc", "5.8 cc");
        secilenKilitMotor = kilitMotorComboBox.getValue();
    }

    @FXML
    public void kilitPompaPressed() {
        sogutmaComboBox.setDisable(false);
        initSogutma();
        secilenKilitPompa = kilitPompaComboBox.getValue();
    }

    @FXML
    public void sogutmaPressed() {
        secilenSogutmaDurumu = sogutmaComboBox.getValue();
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
        } else if(girilenTankKapasitesi < 1 || girilenTankKapasitesi > 500) {
            return true;
        }
        return false;
    }

    private void openGitHubDocumentation(ActionEvent event) {
        String url = "https://github.com/hidirektor/OnderGrup-Hydraulic-Tool";
        hostServices.showDocument(url);
    }

    private void initUniteTipi() {
        uniteTipiComboBox.getItems().addAll("Hidros", "Klasik");
    }

    private void initMotor() {
        motorComboBox.getItems().addAll("4 kW", "5.5 kW (Kompakt)", "7.5 kW (Kompakt)", "11 kW (Kompakt)", "15 kW", "18.5 kW", "22 kW", "37 kW");
    }

    private void initKampana() {
        kampanaVerileri.add("250 mm");
        kampanaVerileri.add("250 mm");
        kampanaVerileri.add("300 mm");
        kampanaVerileri.add("300 mm");
        kampanaVerileri.add("350 mm");
        kampanaVerileri.add("350 mm");
        kampanaVerileri.add("350 mm");
        kampanaVerileri.add("400 mm");
    }

    private void initPompa() {
        if(uniteTipiComboBox.getValue() == "Hidros") {
            pompaComboBox.getItems().addAll("1.1 cc", "1.6 cc", "2.1 cc", "2.7 cc", "3.2 cc", "3.7 cc", "4.2 cc", "4.8 cc", "5.8 cc", "7 cc", "8 cc", "9 cc");
        } else if(uniteTipiComboBox.getValue() == "Klasik") {
            pompaComboBox.getItems().addAll("9.5 cc", "11.9 cc", "14 cc", "14.6 cc", "16.8 cc", "19.2 cc", "22.9 cc", "28.1 cc", "28.8 cc", "33.3 cc", "37.9 cc", "42.6 cc", "45.5 cc", "49.4 cc", "56.1 cc");
        } else {
            pompaComboBox.getItems().addAll("1.1 cc", "1.6 cc", "2.1 cc", "2.7 cc", "3.2 cc", "3.7 cc", "4.2 cc", "4.8 cc", "5.8 cc", "7 cc", "8 cc", "9 cc", "9.5 cc", "11.9 cc", "14 cc", "14.6 cc", "16.8 cc", "19.2 cc", "22.9 cc", "28.1 cc", "28.8 cc", "33.3 cc", "37.9 cc", "42.6 cc", "45.5 cc", "49.4 cc", "56.1 cc");
        }
        uniteTipiComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if(newValue == "Hidros") {
                pompaComboBox.getItems().addAll("1.1 cc", "1.6 cc", "2.1 cc", "2.7 cc", "3.2 cc", "3.7 cc", "4.2 cc", "4.8 cc", "5.8 cc", "7 cc", "8 cc", "9 cc");
            } else {
                pompaComboBox.getItems().addAll("9.5 cc", "11.9 cc", "14 cc", "14.6 cc", "16.8 cc", "19.2 cc", "22.9 cc", "28.1 cc", "28.8 cc", "33.3 cc", "37.9 cc", "42.6 cc", "45.5 cc", "49.4 cc", "56.1 cc");
            }
        });
    }

    private void initValf(int stat) {
        valfTipiComboBox.getItems().clear();
        if(stat == 1) {
            valfTipiComboBox.getItems().addAll("Kilitli Blok || Çift Hız");
        } else {
            valfTipiComboBox.getItems().addAll("İnişte Tek Hız", "İnişte Çift Hız", "Kompanzasyon + İnişte Tek Hız");
        }
    }

    private void initHidrolikKilit() {
        hidrolikKilitComboBox.getItems().addAll("Var", "Yok");
    }

    private void initSogutma() {
        sogutmaComboBox.getItems().clear();
        sogutmaComboBox.getItems().addAll("Var", "Yok");
    }

    /*private void readExcelData2() {
        try {
            FileInputStream file = new FileInputStream("Hidrolik.xlsx");

            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);

            int rowCount = sheet.getLastRowNum();
            for (int i = 1; i <= rowCount; i++) {
                XSSFRow row = sheet.getRow(i);
                if (row != null) {
                    XSSFCell motorCell = row.getCell(0);
                    XSSFCell kampanaCell = row.getCell(1);
                    XSSFCell pompaCell = row.getCell(2);

                    String motor = motorCell.getStringCellValue();
                    String kampana = kampanaCell.getStringCellValue();
                    String pompa = pompaCell.getStringCellValue();

                    motorVerileri.add(motor);
                    kampanaVerileri.add(kampana);
                    pompaVerileri.add(pompa);
                }
            }

            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

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

    private void showErrorMessage(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}