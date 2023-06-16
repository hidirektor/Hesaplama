package me.t3sl4.hesaplama.ui;

import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javafx.stage.Stage;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;

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
    private Box hydraulicUnitShape;

    private String secilenMotor;
    private String secilenPompa;
    private int girilenTankKapasitesiMiktari;
    private String secilenValfTipi;
    private String secilenHidrolikKilitDurumu;
    private String secilenSogutmaDurumu;

    boolean valfTipiStat = false;
    boolean hidrolikKilitStat = false;
    boolean sogutmaStat = false;

    //Excel2List
    List<String> istenenMakineBilgiler = new ArrayList<String>();

    List<String> motorVerileri = new ArrayList<>();
    List<String> kampanaVerileri = new ArrayList<>();
    List<String> pompaVerileri = new ArrayList<>();
    List<String> tankKapasiteVerileri = new ArrayList<>();
    List<String> valfVerileri = new ArrayList<>();
    List<String> hidrolikKilitVerileri = new ArrayList<>();
    List<String> sogutmaVerileri = new ArrayList<>();

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
        int yukseklik = 380;
        int derinlik =100;
        int genislik = 250;
        if (checkComboBox()) {
            showErrorMessage("Hata", "Lütfen tüm girdileri kontrol edin.");
        } else {
            enableSonucSection();
            if(secilenHidrolikKilitDurumu == "Var" || secilenValfTipi == "Kilitli Blok || Çift Hız") {
                genislik += 100;
            } else {
                derinlik = 100;
                yukseklik = 380;
            }
            derinlik += 50;
            yukseklik += 50;
            genislik += 50;

            genislikSonucText.setText("Genişlik: " + genislik + " mm");
            derinlikSonucText.setText("Derinlik: " + derinlik + " mm");
            yukseklikSonucText.setText("Yükseklik: " + yukseklik + " mm");
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
        if(secilenMotor == "4 kW") {
            pompaComboBox.getItems().clear();
            pompaComboBox.getItems().addAll("1.1 cc", "1.6 cc", "4.8 cc");
        }
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
            System.out.println("textfield changed from " + oldValue + " to " + newValue);
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
        }
        hidrolikKilitComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if(newValue == "Var") {
                initValf(1);
            } else {
                initValf(0);
            }
        });
    }

    @FXML
    public void valfTipiPressed() {
        secilenValfTipi = valfTipiComboBox.getValue();
        sogutmaComboBox.setDisable(false);
        sogutmaStat = true;
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
        System.out.println(girilenTankKapasitesi);

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
        motorComboBox.getItems().addAll("4 kW", "5.5 kW", "7.5 kW", "11 kW", "15 kW", "18.5 kW", "22 kW", "37 kW");
    }

    private void initKampana() {
        //motorComboBox 2 kampana
        motorComboBox.getItems().addAll("250 mm", "250 mm", "300 mm", "300 mm", "350 mm", "350 mm", "350 mm", "400 mm");
    }

    private void initPompa() {
        if(uniteTipiComboBox.getValue() == "Hidros") {
            pompaComboBox.getItems().addAll("1.1 cc", "1.6 cc", "2.1 cc", "2.7 cc", "3.2 cc", "3.7 cc", "4.2 cc", "4.8 cc", "5.8 cc", "7 cc", "8 cc", "9 cc");
        } else if(uniteTipiComboBox.getValue() == "Klasik") {
            pompaComboBox.getItems().addAll("9.5 cc", "11.9 cc", "14 cc", "14.6 cc", "16.8 cc", "19.2 cc", "22.9 cc", "28.1 cc", "28.8 cc", "33.3 cc", "37.9 cc", "42.6 cc", "45.5 cc", "49.4 cc", "56.1 cc");
        } else {
            pompaComboBox.getItems().addAll("1.1 cc", "1.6 cc", "2.1 cc", "2.7 cc", "3.2 cc", "3.7 cc", "4.2 cc", "4.8 cc", "5.8 cc", "7 cc", "8 cc", "9 cc", "9.5 cc", "11.9 cc", "14 cc", "14.6 cc", "16.8 cc", "19.2 cc", "22.9 cc", "28.1 cc", "28.8 cc", "33.3 cc", "37.9 cc", "42.6 cc", "45.5 cc", "49.4 cc", "56.1 cc");
        }
    }

    private void initValf(int stat) {
        valfTipiComboBox.getItems().clear();
        if(stat == 1) {
            valfTipiComboBox.getItems().addAll("İnişte Tek Hız", "İnişte Çift Hız", "Kompanzasyon + İnişte Tek Hız", "Kilitli Blok || Çift Hız");
        } else {
            valfTipiComboBox.getItems().addAll("İnişte Tek Hız", "İnişte Çift Hız", "Kompanzasyon + İnişte Tek Hız");
        }
    }

    private void initHidrolikKilit() {
        hidrolikKilitComboBox.getItems().addAll("Var", "Yok");
    }

    private void initSogutma() {
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