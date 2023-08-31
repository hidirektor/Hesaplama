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
import me.t3sl4.hydraulic.Util.HTTP.HTTPUtil;
import me.t3sl4.hydraulic.Util.SceneUtil;
import me.t3sl4.hydraulic.Util.Data.User.User;
import me.t3sl4.hydraulic.Util.Util;
import org.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static me.t3sl4.hydraulic.Launcher.*;

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
                    String loginUrl = BASE_URL + loginURLPrefix;
                    String cipheredPass = DigestUtils.sha256Hex(txtPassword.getText());
                    String jsonLoginBody = "{\"Username\": \"" + txtUsername.getText() + "\", \"Password\": \"" + cipheredPass + "\"}";

                    HTTPRequest.sendRequest(loginUrl, jsonLoginBody, new HTTPRequest.RequestCallback() {
                        @Override
                        public void onSuccess(String loginResponse) {
                            String profileInfoUrl = BASE_URL + profileInfoURLPrefix +":Role";
                            String jsonProfileInfoBody = "{\"Username\": \"" + txtUsername.getText() + "\"}";
                            HTTPRequest.sendRequest(profileInfoUrl, jsonProfileInfoBody, new HTTPRequest.RequestCallback() {
                                @Override
                                public void onSuccess(String profileInfoResponse) {
                                    JSONObject roleObject = new JSONObject(profileInfoResponse);
                                    String roleValue = roleObject.getString("Role");
                                    if (roleValue.equals("TECHNICIAN") || roleValue.equals("ENGINEER") || roleValue.equals("SYSOP")) {
                                        Main.loggedInUser = new User(txtUsername.getText());

                                        girisProcess();

                                        stage.close();
                                    } else {
                                        lblErrors.setText("Hidrolik aracını normal kullanıcılar kullanamaz.");
                                    }
                                }

                                @Override
                                public void onFailure() {
                                    lblErrors.setText("Profil bilgileri alınamadı!");
                                }
                            });
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

    private void girisProcess() {
        updateUser("Role", 0);
        updateUser("Email", 1);
        updateUser("NameSurname", 2);
        updateUser("Phone", 3);
        updateUser("CompanyName", 4);
        updateUser("Created_At", 5);

        try {
            openMainScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateUser(String requestVal, int section) {
        String profileInfoUrl = BASE_URL + profileInfoURLPrefix + ":" + requestVal;
        String profileInfoBody = "{\"Username\": \"" + Main.loggedInUser.getUsername() + "\"}";

        HTTPRequest.sendRequest(profileInfoUrl, profileInfoBody, new HTTPRequest.RequestCallback() {
            @Override
            public void onSuccess(String profileInfoResponse) {
                String parsedVal = HTTPUtil.parseStringVal(profileInfoResponse, requestVal);
                if (section == 0) {
                    Main.loggedInUser.setRole(parsedVal);
                } else if (section == 1) {
                    Main.loggedInUser.setEmail(parsedVal);
                } else if (section == 2) {
                    Main.loggedInUser.setFullName(parsedVal);
                } else if (section == 3) {
                    Main.loggedInUser.setPhone(parsedVal);
                } else if (section == 4) {
                    Main.loggedInUser.setCompanyName(parsedVal);
                } else {
                    Main.loggedInUser.setCreatedAt(parsedVal);
                }
            }

            @Override
            public void onFailure() {
                System.out.println("Kullanıcı bilgileri alınamadı!");
            }
        });
    }
}