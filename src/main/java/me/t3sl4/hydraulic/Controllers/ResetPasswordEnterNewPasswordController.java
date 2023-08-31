package me.t3sl4.hydraulic.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import me.t3sl4.hydraulic.Launcher;
import me.t3sl4.hydraulic.Util.Util;
import me.t3sl4.hydraulic.Util.HTTP.HTTPRequest;
import me.t3sl4.hydraulic.Util.SceneUtil;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static me.t3sl4.hydraulic.Util.Util.BASE_URL;

public class ResetPasswordEnterNewPasswordController implements Initializable {

    @FXML
    private Button btnChangePass;

    @FXML
    private TextField sifreText;
    @FXML
    private TextField sifrePassword;
    @FXML
    private ImageView passwordVisibilityIcon;
    @FXML
    private Button togglePasswordButton;

    @FXML
    private Label lblErrors;

    private String girilenSifre = "";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        togglePasswordButton.setOnMouseClicked(event -> togglePasswordVisibility());
        sifreText.textProperty().addListener((observable, oldValue, newValue) -> {
            girilenSifre = newValue;
        });
    }

    @FXML
    public void programiKapat(MouseEvent event) {
        System.exit(0);
    }

    @FXML
    public void goBackAction() throws IOException {
        Stage stage = (Stage) btnChangePass.getScene().getWindow();

        stage.close();
        SceneUtil.changeScreen("fxml/Login.fxml");
    }

    @FXML
    public void onderWeb() {
        Util.openURL("https://ondergrup.com");
    }

    @FXML
    public void sifreDegistir() {
        String yeniSifre = DigestUtils.sha256Hex(sifreText.getText());

        if (yeniSifre.isEmpty()) {
            lblErrors.setText("E-posta adresi boş olamaz.");
        } else {
            String otpUrl = BASE_URL + "/api/updatePass";
            String jsonUpdatePassBody = "{\"Email\": \"" + ResetPasswordController.enteredEmail + "\", \"Password\": \"" + yeniSifre + "\"}";

            HTTPRequest.sendRequest(otpUrl, jsonUpdatePassBody, new HTTPRequest.RequestCallback() {
                @Override
                public void onSuccess(String changeResponse) throws IOException {
                    if (changeResponse.contains("Şifre güncellendi")) {
                        backMainScreen();
                    } else {
                        lblErrors.setText("Şifre güncelleme hatası!");
                    }
                }

                @Override
                public void onFailure() {
                    lblErrors.setText("Eski şifre ile yeni şifre aynı olamaz !");
                }
            });
        }
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

    private void backMainScreen() throws IOException {
        Stage stage = (Stage) btnChangePass.getScene().getWindow();

        stage.close();
        SceneUtil.changeScreen("fxml/Login.fxml");
    }
}
