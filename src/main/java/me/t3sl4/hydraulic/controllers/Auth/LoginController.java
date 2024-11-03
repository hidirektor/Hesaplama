package me.t3sl4.hydraulic.controllers.Auth;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import me.t3sl4.hydraulic.Launcher;
import me.t3sl4.hydraulic.controllers.MainController;
import me.t3sl4.hydraulic.utils.Utils;
import me.t3sl4.hydraulic.utils.general.InternetService;
import me.t3sl4.hydraulic.utils.general.SystemVariables;
import me.t3sl4.hydraulic.utils.service.HTTP.Request.License.LicenseService;
import me.t3sl4.hydraulic.utils.service.HTTP.Request.User.UserService;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static me.t3sl4.hydraulic.utils.general.SystemVariables.*;
import static me.t3sl4.hydraulic.utils.service.HTTP.Request.User.UserService.updateUserAndOpenMainScreen;

public class LoginController implements Initializable {

    @FXML
    private Label lblErrors;

    @FXML
    private Label codedBy;

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

    @FXML
    private Pane loginPane;

    @FXML
    private Button onlineMod;

    @FXML
    private Button offlineMod;

    @FXML
    private Button offlineMod2;

    @FXML
    private Label licenseDescriptionLabel;

    private String girilenSifre = "";

    private static final Logger logger = Logger.getLogger(MainController.class.getName());

    @FXML
    public void girisYap(MouseEvent event) throws IOException {
        Stage stage = (Stage) btnSignin.getScene().getWindow();
        String loginUrl = BASE_URL + loginURLPrefix;
        String jsonLoginBody = "";

        if (!txtUsername.getText().isEmpty() || !txtPassword.getText().isEmpty()) {
            jsonLoginBody = "{\"userName\": \"" + txtUsername.getText() + "\", \"password\": \"" + txtPassword.getText() + "\"}";
            UserService.loginReq(loginUrl, jsonLoginBody, stage, txtUsername.getText(), txtPassword.getText(), lblErrors);
        }
    }

    @FXML
    public void kayitOl() {
        Stage stage = (Stage) btnSignup.getScene().getWindow();

        try {
            Utils.openRegisterScreen(lblErrors);
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    @FXML
    public void onlineMod() {
        Stage stage = (Stage) btnSignin.getScene().getWindow();
        if(!Utils.checkUpdateAndCancelEvent((Stage)offlineMod.getScene().getWindow())) {
            Utils.checkLocalUserData(() -> {});
            if(SystemVariables.loggedInUser != null) {
                if(SystemVariables.loggedInUser.getUserID() != null || SystemVariables.loggedInUser.getAccessToken() != null || SystemVariables.loggedInUser.getRefreshToken() != null) {
                    updateUserAndOpenMainScreen(stage, lblErrors, () -> {
                        try {
                            Utils.deleteLocalData();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        loginPane.setVisible(true);
                        offlineMod.setVisible(false);
                        onlineMod.setVisible(false);
                    });
                } else {
                    loginPane.setVisible(true);
                    offlineMod.setVisible(false);
                    onlineMod.setVisible(false);
                }
            } else {
                loginPane.setVisible(true);
                offlineMod.setVisible(false);
                onlineMod.setVisible(false);
            }
        }
    }

    @FXML
    public void offlineMod() {
        if(!Utils.checkUpdateAndCancelEvent((Stage)offlineMod.getScene().getWindow())) {
            offlineMod.setDisable(true);

            SystemVariables.offlineMode = true;
            loginPane.setVisible(false);
            offlineMod.setVisible(true);
            onlineMod.setVisible(true);

            Utils.offlineMod(lblErrors, () -> {
                offlineMod.setDisable(false);
            });
        }
    }

    @FXML
    public void sifremiUnuttum() throws IOException {
        Stage stage = (Stage) btnSignin.getScene().getWindow();
        stage.close();

        Utils.openResetPasswordScreen(lblErrors);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        codedBy.setText(SystemVariables.developedBy);

        togglePasswordButton.setOnMouseClicked(event -> togglePasswordVisibility());
        txtPassword.textProperty().addListener((observable, oldValue, newValue) -> {
            girilenSifre = newValue;
        });

        checkInternet();

        checkLicense();
    }

    private void togglePasswordVisibility() {
        if (txtPassword.isVisible()) {
            txtPassword.setManaged(false);
            txtPassword.setVisible(false);
            sifrePassword.setManaged(true);
            sifrePassword.setVisible(true);
            sifrePassword.setText(girilenSifre);
            passwordVisibilityIcon.setImage(new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/images/icons/ikon_hide_pass.png"))));
        } else {
            txtPassword.setManaged(true);
            txtPassword.setVisible(true);
            sifrePassword.setManaged(false);
            sifrePassword.setVisible(false);
            passwordVisibilityIcon.setImage(new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/images/icons/ikon_show_pass.png"))));
        }
    }

    @FXML
    public void openGithub() {
        Utils.openURL("https://github.com/hidirektor");
    }

    @FXML
    public void openOnder() {
        Utils.openURL(SystemVariables.WEB_URL);
    }

    @FXML
    public void ekraniKapat() {
        Stage stage = (Stage) btnSignin.getScene().getWindow();

        stage.close();
    }

    private void checkLicense() {
        if(Utils.checkLicenseKey() == null) {
            Utils.showErrorOnLabel(lblErrors, "Lütfen giriş yaparak lisans anahtarını aktifleştirin.");
            offlineMod.setDisable(true);
            offlineMod2.setDisable(true);
            Utils.licenseStatus(licenseDescriptionLabel, false);
        } else {
            String currentLicenseKey = Utils.checkLicenseKey();
            String licenseCheckURL = BASE_URL + checkLicenseUrlPrefix;

            try {
                LicenseService.checkLicense(licenseCheckURL, currentLicenseKey, () -> {
                    offlineMod.setDisable(false);
                    offlineMod2.setDisable(false);
                    Utils.licenseStatus(licenseDescriptionLabel, true);
                }, () -> {
                    offlineMod.setDisable(true);
                    offlineMod2.setDisable(true);
                    Utils.showErrorOnLabel(lblErrors, "Lütfen giriş yaparak yeni bir lisans aktifleştirin.");
                    Utils.licenseStatus(licenseDescriptionLabel, false);
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void checkInternet() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Lütfen internet bağlantınızı kontrol edin!");
        alert.setTitle("Internet Bağlantısı Kontrolü");
        alert.setHeaderText(null);
        alert.show();

        InternetService internetCheckService = new InternetService();

        internetCheckService.setOnSucceeded(event -> {
            if (internetCheckService.getValue()) {
                alert.close();
                SystemVariables.offlineMode = true;
            } else {
                Utils.showErrorOnLabel(lblErrors, "Lütfen internet bağlantınızı kontrol edin!");
            }
        });

        internetCheckService.setOnFailed(event -> {
            alert.close();
            Utils.showErrorOnLabel(lblErrors, "Bir hata oluştu, internet bağlantınızı kontrol edin!");
        });

        internetCheckService.start();

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(5000);
                    Platform.runLater(() -> internetCheckService.restart());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }).start();
    }
}