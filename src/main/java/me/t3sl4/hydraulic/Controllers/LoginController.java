package me.t3sl4.hydraulic.Controllers;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import me.t3sl4.hydraulic.Main;
import me.t3sl4.hydraulic.Util.HTTP.HTTPRequest;
import me.t3sl4.hydraulic.Util.HTTP.HTTPUtil;
import me.t3sl4.hydraulic.Util.SceneUtil;
import me.t3sl4.hydraulic.Util.User;
import me.t3sl4.hydraulic.Util.Gen.Util;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

import static me.t3sl4.hydraulic.Util.Gen.Util.BASE_URL;

public class LoginController implements Initializable {

    @FXML
    private Label lblErrors;

    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtPassword;

    @FXML
    private Button btnSignin;

    @FXML
    private Button btnSignup;

    @FXML
    public void girisYap(MouseEvent event) {
        if (Util.netIsAvailable()) {
            Stage stage = (Stage) btnSignin.getScene().getWindow();

            if (txtUsername.getText().isEmpty() || txtPassword.getText().isEmpty()) {
                lblErrors.setText("Şifre veya kullanıcı adı girmediniz !");
            } else {
                String url = BASE_URL + "/api/login";
                String jsonBody = "{\"Username\": \"" + txtUsername.getText() + "\", \"Password\": \"" + txtPassword.getText() + "\"}";

                HTTPRequest.sendRequest(url, jsonBody, new HTTPRequest.RequestCallback() {
                    @Override
                    public void onSuccess(String response) {
                        if (response.contains("Giriş başarılı")) {
                            stage.close();

                            Main.loggedInUser = new User(txtUsername.getText());

                            try {
                                openMainScreen();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        } else {
                            lblErrors.setText("Böyle bir kullanıcı bulunamadı!");
                        }
                    }

                    //TODO
                    //response kodlarına göre hata mesjaını revize et ()
                    @Override
                    public void onFailure() {
                        lblErrors.setText("Kullanıcı adı veya şifre hatalı !");
                    }
                });
            }
        } else {
            lblErrors.setText("Lütfen internet bağlantınızı kontrol edin!");
        }
    }

    @FXML
    public void kayitOl() throws IOException {
        Stage stage = (Stage) btnSignup.getScene().getWindow();

        if(Util.netIsAvailable()) {
            stage.close();
            openRegisterScreen();
        } else {
            lblErrors.setText("Lütfen internet bağlantınızı kontrol edin!");
        }
    }

    @FXML
    public void sifremiUnuttum() {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(!Util.netIsAvailable()) {
            lblErrors.setText("Lütfen internet bağlantınızı kontrol edin!");
        }
    }

    @FXML
    public void openGithub() {
        Util.openURL("https://github.com/hidirektor");
    }

    @FXML
    public void openOnder() {
        Util.openURL("https://ondergrup.com");
    }

    @FXML
    public void ekraniKapat() {
        Stage stage = (Stage) btnSignin.getScene().getWindow();

        stage.close();
    }

    private void openMainScreen() throws IOException {
        SceneUtil.changeScreen("fxml/Home.fxml");
    }

    private void openRegisterScreen() throws IOException {
        SceneUtil.changeScreen("fxml/Register.fxml");
    }
}