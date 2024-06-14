package me.t3sl4.hydraulic.Controllers;

import java.io.*;
import java.net.*;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import me.t3sl4.hydraulic.Launcher;
import me.t3sl4.hydraulic.Util.Util;
import me.t3sl4.hydraulic.Util.HTTP.HTTPRequest;
import me.t3sl4.hydraulic.Util.SceneUtil;

import static me.t3sl4.hydraulic.Launcher.*;

public class RegisterController implements Initializable {

    @FXML
    private Label btn_exit;

    @FXML
    private ImageView profilePhotoImageView;

    @FXML
    private TextField isimSoyisimText;
    @FXML
    private TextField ePostaText;
    @FXML
    private TextField telefonText;
    @FXML
    private TextField kullaniciAdiText;
    @FXML
    private TextField sifreText;
    @FXML
    private TextField sirketText;

    @FXML
    private ImageView onderLogo;

    @FXML
    private Circle secilenFoto;

    @FXML
    private Button togglePasswordButton;
    @FXML
    private TextField sifrePassword;
    @FXML
    private ImageView passwordVisibilityIcon;

    @FXML
    private ImageView goBack;

    private double x, y;

    String secilenPhotoPath = "";
    private String girilenSifre = "";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        togglePasswordButton.setOnMouseClicked(event -> togglePasswordVisibility());
        sifreText.textProperty().addListener((observable, oldValue, newValue) -> {
            girilenSifre = newValue;
        });
    }

    @FXML
    private void programiKapat(MouseEvent event) {
        System.exit(0);
    }

    @FXML
    private void goBackAction() throws IOException {
        Stage stage = (Stage) goBack.getScene().getWindow();

        stage.close();
        SceneUtil.changeScreen("fxml/Login.fxml");
    }

    private void togglePasswordVisibility() {
        if (sifreText.isVisible()) {
            sifreText.setManaged(false);
            sifreText.setVisible(false);
            sifrePassword.setManaged(true);
            sifrePassword.setVisible(true);
            sifrePassword.setText(girilenSifre);
            passwordVisibilityIcon.setImage(new Image(Launcher.class.getResourceAsStream("icons/hidePass.png")));
        } else {
            sifreText.setManaged(true);
            sifreText.setVisible(true);
            sifrePassword.setManaged(false);
            sifrePassword.setVisible(false);
            passwordVisibilityIcon.setImage(new Image(Launcher.class.getResourceAsStream("icons/showPass.png")));
        }
    }

    @FXML
    private void uploadProfilePhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Resim Dosyaları", "*.jpg")
        );

        Stage stage = (Stage) profilePhotoImageView.getScene().getWindow();
        java.io.File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            secilenFoto.setFill(new ImagePattern(image));
            secilenFoto.setVisible(true);
            profilePhotoImageView.setImage(image);
            profilePhotoImageView.setVisible(false);
            secilenPhotoPath = selectedFile.getAbsolutePath();
        }
    }

    @FXML
    private void kayitOlma() throws IOException {
        Stage stage = (Stage) kullaniciAdiText.getScene().getWindow();
        String userRole = "NORMAL";
        String userName = kullaniciAdiText.getText();
        String password = sifreText.getText();
        String nameSurname = isimSoyisimText.getText();
        String eMail = ePostaText.getText();
        String companyName = sirketText.getText();
        String phone = telefonText.getText();
        String profilePhotoPath = userName + ".jpg";

        if (checkFields()) {
            if(kullaniciAdiText.getText().contains(" ") || Util.checkUpperCase(kullaniciAdiText.getText())) {
                if(kullaniciAdiText.getText().contains(" ")) {
                    Util.showErrorMessage("Kullanıcı adında boşluk karakteri olamaz !");
                    kullaniciAdiText.clear();
                }

                if(Util.checkUpperCase(kullaniciAdiText.getText())) {
                    Util.showErrorMessage("Kullanıcı adında büyük harf kullanılamaz !");
                    kullaniciAdiText.clear();
                }
            } else {
                String created_at = Util.getCurrentDateTime();

                String registerJsonBody =
                        "{" +
                                "\"Role\":\"" + userRole + "\"," +
                                "\"UserName\":\"" + userName + "\"," +
                                "\"Email\":\"" + eMail + "\"," +
                                "\"Password\":\"" + password + "\"," +
                                "\"NameSurname\":\"" + nameSurname + "\"," +
                                "\"Phone\":\"" + phone + "\"," +
                                "\"Profile_Photo\":\"" + profilePhotoPath + "\"," +
                                "\"CompanyName\":\"" + companyName + "\"," +
                                "\"Created_At\":\"" + created_at + "\"" +
                                "}";

                sendRegisterRequest(registerJsonBody, stage);
            }
        } else {
            Util.showErrorMessage("Lütfen gerekli tüm alanları doldurun !");
        }
    }

    private void sendRegisterRequest(String jsonBody, Stage stage) {
        String registerUrl = BASE_URL + registerURLPrefix;
        HTTPRequest.sendRequest(registerUrl, jsonBody, new HTTPRequest.RequestCallback() {
            @Override
            public void onSuccess(String response) throws IOException {
                uploadProfilePhoto2Server(stage);
            }

            @Override
            public void onFailure() {
                Util.showErrorMessage("Kayıt olurken hata meydana geldi !");
            }
        });
    }

    private void uploadProfilePhoto2Server(Stage stage) throws IOException {
        String uploadUrl = BASE_URL + uploadURLPrefix;
        String username = kullaniciAdiText.getText();

        File profilePhotoFile = new File(secilenPhotoPath);
        if (!profilePhotoFile.exists()) {
            Util.showErrorMessage("Profil fotoğrafı bulunamadı !");
            return;
        }

        HTTPRequest.sendMultipartRequest(uploadUrl, username, profilePhotoFile, new HTTPRequest.RequestCallback() {
            @Override
            public void onSuccess(String response) {
                Platform.runLater(() -> {
                    try {
                        stage.close();
                        openMainScreen();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            public void onFailure() {
                Util.showErrorMessage("Profil fotoğrafı yüklenirken hata meydana geldi !");
            }
        });
    }

    @FXML
    public void onderWeb() {
        Util.openURL("https://ondergrup.com");
    }

    @FXML
    public void kayitBilgiTemizle() {
        secilenFoto.setVisible(false);
        isimSoyisimText.clear();
        ePostaText.clear();
        telefonText.clear();
        kullaniciAdiText.clear();
        sifreText.clear();
        sirketText.clear();
    }

    private void openMainScreen() throws IOException {
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(Launcher.class.getResource("fxml/Login.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.initStyle(StageStyle.UNDECORATED);

        Image icon = new Image(Launcher.class.getResourceAsStream("icons/logo.png"));
        primaryStage.getIcons().add(icon);

        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {

            primaryStage.setX(event.getScreenX() - x);
            primaryStage.setY(event.getScreenY() - y);

        });
        primaryStage.show();
    }

    private boolean checkFields() {
        return !isimSoyisimText.getText().isEmpty() && !ePostaText.getText().isEmpty() && !telefonText.getText().isEmpty() && !kullaniciAdiText.getText().isEmpty() && !sifreText.getText().isEmpty() && !sirketText.getText().isEmpty() && profilePhotoImageView.getImage() != null;
    }

}