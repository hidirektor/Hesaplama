package me.t3sl4.hydraulic.controllers.Auth;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import me.t3sl4.hydraulic.Launcher;
import me.t3sl4.hydraulic.utils.Utils;
import me.t3sl4.hydraulic.utils.general.SceneUtil;
import me.t3sl4.hydraulic.utils.service.HTTPRequest;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static me.t3sl4.hydraulic.utils.general.SystemVariables.*;

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
        Screen currentScreen = SceneUtil.getScreenOfNode(btn_exit);

        stage.close();

        SceneUtil.changeScreen("fxml/Login.fxml", currentScreen);
    }

    private void togglePasswordVisibility() {
        if (sifreText.isVisible()) {
            sifreText.setManaged(false);
            sifreText.setVisible(false);
            sifrePassword.setManaged(true);
            sifrePassword.setVisible(true);
            sifrePassword.setText(girilenSifre);
            passwordVisibilityIcon.setImage(new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/images/icons/ikon_hide_pass.png"))));
        } else {
            sifreText.setManaged(true);
            sifreText.setVisible(true);
            sifrePassword.setManaged(false);
            sifrePassword.setVisible(false);
            passwordVisibilityIcon.setImage(new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/images/icons/ikon_show_pass.png"))));
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
    private void kayitOlma() {
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
            if(kullaniciAdiText.getText().contains(" ") || Utils.checkUpperCase(kullaniciAdiText.getText())) {
                if(kullaniciAdiText.getText().contains(" ")) {
                    Utils.showErrorMessage("Kullanıcı adında boşluk karakteri olamaz !", SceneUtil.getScreenOfNode(btn_exit), (Stage)btn_exit.getScene().getWindow());
                    kullaniciAdiText.clear();
                }

                if(Utils.checkUpperCase(kullaniciAdiText.getText())) {
                    Utils.showErrorMessage("Kullanıcı adında büyük harf kullanılamaz !", SceneUtil.getScreenOfNode(btn_exit), (Stage)btn_exit.getScene().getWindow());
                    kullaniciAdiText.clear();
                }
            } else {
                String registerJsonBody =
                        "{" +
                                "\"userName\":\"" + userName + "\"," +
                                "\"userType\":\"" + userRole + "\"," +
                                "\"nameSurname\":\"" + nameSurname + "\"," +
                                "\"eMail\":\"" + eMail + "\"," +
                                "\"phoneNumber\":\"" + phone + "\"," +
                                "\"companyName\":\"" + companyName + "\"," +
                                "\"password\":\"" + password + "\"" +
                                "}";

                sendRegisterRequest(registerJsonBody, stage);
            }
        } else {
            Utils.showErrorMessage("Lütfen gerekli tüm alanları doldurun !", SceneUtil.getScreenOfNode(btn_exit), (Stage)btn_exit.getScene().getWindow());
        }
    }

    private void sendRegisterRequest(String jsonBody, Stage stage) {
        String registerUrl = BASE_URL + registerURLPrefix;
        HTTPRequest.sendJsonRequest(registerUrl, "POST", jsonBody, new HTTPRequest.RequestCallback() {
            @Override
            public void onSuccess(String response) throws IOException {
                uploadProfilePhoto2Server(stage);
            }

            @Override
            public void onFailure() {
                Utils.showErrorMessage("Kayıt olurken hata meydana geldi !", SceneUtil.getScreenOfNode(btn_exit), (Stage)btn_exit.getScene().getWindow());
            }
        });
    }

    private void uploadProfilePhoto2Server(Stage stage) throws IOException {
        String uploadUrl = BASE_URL + uploadProfilePhotoURLPrefix;
        String username = kullaniciAdiText.getText();

        System.out.println(uploadUrl + username);

        File profilePhotoFile = new File(secilenPhotoPath);
        if (!profilePhotoFile.exists()) {
            Utils.showErrorMessage("Profil fotoğrafı bulunamadı !", SceneUtil.getScreenOfNode(btn_exit), (Stage)btn_exit.getScene().getWindow());
            return;
        }

        HTTPRequest.uploadFile(uploadUrl, "POST", profilePhotoFile, username, new HTTPRequest.RequestCallback() {
            @Override
            public void onSuccess(String response) {
                Platform.runLater(() -> {
                    try {
                        stage.close();
                        SceneUtil.openMainScreen();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            public void onFailure() {
                Utils.showErrorMessage("Profil fotoğrafı yüklenirken hata meydana geldi !", SceneUtil.getScreenOfNode(btn_exit), (Stage)btn_exit.getScene().getWindow());
            }
        });
    }

    @FXML
    public void onderWeb() {
        Utils.openURL("https://ondergrup.com");
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

    private boolean checkFields() {
        return !isimSoyisimText.getText().isEmpty() && !ePostaText.getText().isEmpty() && !telefonText.getText().isEmpty() && !kullaniciAdiText.getText().isEmpty() && !sifreText.getText().isEmpty() && !sirketText.getText().isEmpty() && profilePhotoImageView.getImage() != null;
    }

}