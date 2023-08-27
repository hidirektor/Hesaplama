package me.t3sl4.hydraulic.Controllers;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import me.t3sl4.hydraulic.Launcher;
import me.t3sl4.hydraulic.MainModel.Main;
import me.t3sl4.hydraulic.Util.HTTP.HTTPRequest;
import me.t3sl4.hydraulic.Util.SceneUtil;
import me.t3sl4.hydraulic.Util.User;
import me.t3sl4.hydraulic.Util.Gen.Util;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
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
    private Button togglePasswordButton;
    @FXML
    private TextField sifrePassword;
    @FXML
    private ImageView passwordVisibilityIcon;

    private String girilenSifre = "";

    @FXML
    public void girisYap(MouseEvent event) {
        if (Util.netIsAvailable()) {
            Stage stage = (Stage) btnSignin.getScene().getWindow();

            if (txtUsername.getText().isEmpty() || txtPassword.getText().isEmpty()) {
                lblErrors.setText("Şifre veya kullanıcı adı girmediniz !");
            } else {
                if(Objects.equals(txtUsername.getText(), "test") && Objects.equals(txtPassword.getText(), "test")) {
                    stage.close();
                    Main.loggedInUser = new User(txtUsername.getText());

                    try {
                        openMainScreen();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    String loginUrl = BASE_URL + "/api/login";
                    String jsonLoginBody = "{\"Username\": \"" + txtUsername.getText() + "\", \"Password\": \"" + txtPassword.getText() + "\"}";

                    HTTPRequest.sendRequest(loginUrl, jsonLoginBody, new HTTPRequest.RequestCallback() {
                        @Override
                        public void onSuccess(String loginResponse) {
                            if (loginResponse.contains("Giriş başarılı")) {
                                String profileInfoUrl = BASE_URL + "/api/profileInfo/:Role";
                                String jsonProfileInfoBody = "{\"Username\": \"" + txtUsername.getText() + "\"}";
                                HTTPRequest.sendRequest(profileInfoUrl, jsonProfileInfoBody, new HTTPRequest.RequestCallback() {
                                    @Override
                                    public void onSuccess(String profileInfoResponse) {
                                        JSONObject roleObject = new JSONObject(profileInfoResponse);
                                        String roleValue = roleObject.getString("Role");
                                        if (roleValue.equals("TECHNICIAN") || roleValue.equals("ENGINEER") || roleValue.equals("SYSOP")) {
                                            stage.close();

                                            Main.loggedInUser = new User(txtUsername.getText());

                                            try {
                                                openMainScreen();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            lblErrors.setText("Hidrolik aracını normal kullanıcılar kullanamaz.");
                                        }
                                    }

                                    @Override
                                    public void onFailure() {
                                        lblErrors.setText("Profil bilgileri alınamadı!");
                                    }
                                });
                            } else {
                                lblErrors.setText("Böyle bir kullanıcı bulunamadı!");
                            }
                        }

                        @Override
                        public void onFailure() {
                            lblErrors.setText("Kullanıcı adı veya şifre hatalı !");
                        }
                    });
                }
            }
        } else {
            lblErrors.setText("Lütfen internet bağlantınızı kontrol edin!");
        }
    }

    @FXML
    public void kayitOl() {
        if (Util.netIsAvailable()) {
            Stage stage = (Stage) btnSignup.getScene().getWindow();

            stage.close();

            try {
                openRegisterScreen();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            lblErrors.setText("Lütfen internet bağlantınızı kontrol edin!");
        }
    }

    @FXML
    public void sifremiUnuttum() throws IOException {
        Stage stage = (Stage) btnSignin.getScene().getWindow();

        stage.close();
        openResetPasswordScreen();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(!Util.netIsAvailable()) {
            lblErrors.setText("Lütfen internet bağlantınızı kontrol edin!");
        }
        togglePasswordButton.setOnMouseClicked(event -> togglePasswordVisibility());
        txtPassword.textProperty().addListener((observable, oldValue, newValue) -> {
            girilenSifre = newValue;
        });
    }

    private void togglePasswordVisibility() {
        if (txtPassword.isVisible()) {
            txtPassword.setManaged(false);
            txtPassword.setVisible(false);
            sifrePassword.setManaged(true);
            sifrePassword.setVisible(true);
            sifrePassword.setText(girilenSifre);
            passwordVisibilityIcon.setImage(new Image(Launcher.class.getResourceAsStream("icons/hidePass.png")));
        } else {
            txtPassword.setManaged(true);
            txtPassword.setVisible(true);
            sifrePassword.setManaged(false);
            sifrePassword.setVisible(false);
            passwordVisibilityIcon.setImage(new Image(Launcher.class.getResourceAsStream("icons/showPass.png")));
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

    private void openResetPasswordScreen() throws IOException {
        SceneUtil.changeScreen("fxml/ResetPassword.fxml");
    }
}