package me.t3sl4.hesaplama.ui;

import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

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

    private String secilenMotor;
    private String secilenPompa;
    private int girilenTankKapasitesiMiktari;
    private String secilenValfTipi;
    private String secilenHidrolikKilitDurumu;
    private String secilenSogutmaDurumu;

    private HostServices hostServices;

    public void initialize() {
        initUniteTipi();
        initMotor();
        initPompa();
        initValf();
        initHidrolikKilit();
        initSogutma();
    }

    @FXML
    public void hesaplaFunc() {
        if (checkComboBox()) {
            showErrorMessage("Hata", "Lütfen tüm girdileri kontrol edin.");
        } else {
            // Hesaplama işlemini burada gerçekleştir
            // Sonucu sonucArea'ya yazdır
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
    }

    @FXML
    public void pompaPressed() {
        if(secilenMotor == "4 kW") {
            //4 kW motor seçilirse seçilenleri filtrele
        }
        secilenPompa = pompaComboBox.getValue();
        tankKapasitesiTextField.setDisable(false);
        tankKapasitesiTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.isEmpty()) {
                valfTipiComboBox.setDisable(true);
            }
            System.out.println("textfield changed from " + oldValue + " to " + newValue);
        });
    }

    @FXML
    public void tankKapasitesiEntered() {
        if(!tankKapasitesiTextField.getText().isEmpty()) {
            girilenTankKapasitesiMiktari = Integer.parseInt(tankKapasitesiTextField.getText());
            if(girilenTankKapasitesiMiktari < 1 || girilenTankKapasitesiMiktari > 500) {
                valfTipiComboBox.setDisable(true);
            } else {
                valfTipiComboBox.setDisable(false);
            }
        } else {
            valfTipiComboBox.setDisable(true);
        }
    }

    @FXML
    public void tankKapasitesiBackSpacePressed(KeyEvent event) {
        if(event.getCode() == KeyCode.BACK_SPACE || event.getCode() == KeyCode.DELETE) {
            tankKapasitesiTextField.clear();
            valfTipiComboBox.setDisable(true);
        }
    }

    @FXML
    public void valfTipiPressed() {
        secilenValfTipi = valfTipiComboBox.getValue();
        hidrolikKilitComboBox.setDisable(false);
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

    private void initValf() {
        valfTipiComboBox.getItems().addAll("İnişte Tek Hız", "İnişte Çift Hız", "Kompanzasyon + İnişte Tek Hız", "Kilitli Blok || Çift Hız");
    }

    private void initHidrolikKilit() {
        hidrolikKilitComboBox.getItems().addAll("Var", "Yok");
    }

    private void initSogutma() {
        sogutmaComboBox.getItems().addAll("Var", "Yok");
    }

    private void disableAllSections() {
        motorComboBox.setDisable(true);
        pompaComboBox.setDisable(true);
        tankKapasitesiTextField.setDisable(true);
        valfTipiComboBox.setDisable(true);
        hidrolikKilitComboBox.setDisable(true);
        sogutmaComboBox.setDisable(true);
    }

    private void showErrorMessage(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}