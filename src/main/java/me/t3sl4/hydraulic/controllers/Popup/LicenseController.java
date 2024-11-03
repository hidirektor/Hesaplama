package me.t3sl4.hydraulic.controllers.Popup;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import me.t3sl4.hydraulic.utils.Utils;
import me.t3sl4.hydraulic.utils.general.SceneUtil;
import me.t3sl4.hydraulic.utils.service.HTTP.Request.License.LicenseService;

import java.io.IOException;

import static me.t3sl4.hydraulic.utils.general.SystemVariables.*;

public class LicenseController {

    @FXML
    private TextField licenseKeyField;

    @FXML
    private Button licenseButton;

    @FXML
    public void initialize() {
        //TODO
    }

    public void popupKapat() {
        Stage stage = (Stage) licenseKeyField.getScene().getWindow();

        stage.close();
    }

    @FXML
    public void activateProgram() throws IOException {
        String activateLicenseUrl = BASE_URL + activateLicenseUrlPrefix;

        if(licenseKeyField.getText() != null || !licenseKeyField.getText().isEmpty()) {
            String enteredLicenseKey = licenseKeyField.getText();
            LicenseService.activateLicense(activateLicenseUrl, enteredLicenseKey, loggedInUser.getEmail(), loggedInUser.getAccessToken(), () -> {
                try {
                    Utils.showSuccessMessage("Lisans anahtarı aktifleştirildi.", SceneUtil.getScreenOfNode(licenseButton), (Stage) licenseKeyField.getScene().getWindow());

                    Thread.sleep(2000);

                    Utils.saveLicenseKey(enteredLicenseKey);
                    popupKapat();
                } catch (InterruptedException e) {
                    System.err.println("Bekleme sırasında hata: " + e.getMessage());
                }
            }, () -> {
                Utils.showErrorMessage("Girdiğiniz lisans anahtarı hatalıdır.", SceneUtil.getScreenOfNode(licenseButton), (Stage) licenseKeyField.getScene().getWindow());
            });
        }
    }
}
