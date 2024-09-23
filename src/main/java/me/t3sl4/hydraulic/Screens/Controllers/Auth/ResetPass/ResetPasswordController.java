package me.t3sl4.hydraulic.Screens.Controllers.Auth.ResetPass;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import me.t3sl4.hydraulic.Launcher;
import me.t3sl4.hydraulic.Screens.SceneUtil;
import me.t3sl4.hydraulic.Utils.HTTP.HTTPRequest;
import me.t3sl4.hydraulic.Utils.Utils;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static me.t3sl4.hydraulic.Launcher.BASE_URL;
import static me.t3sl4.hydraulic.Launcher.otpURLPrefix;

public class ResetPasswordController implements Initializable {

    @FXML
    private Button btnSignin;

    @FXML
    private TextField txtEmail;

    @FXML
    private Label lblErrors;

    public static String enteredUserName;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    public void programiKapat(MouseEvent event) {
        System.exit(0);
    }

    @FXML
    public void goBackAction() throws IOException {
        Stage stage = (Stage) btnSignin.getScene().getWindow();

        stage.close();
        SceneUtil.changeScreen("fxml/Login.fxml");
    }

    @FXML
    public void onderWeb() {
        Utils.openURL("https://ondergrup.com");
    }

    @FXML
    private void sifreyiSifirla() {
        enteredUserName = txtEmail.getText();

        if (enteredUserName.isEmpty()) {
            Utils.showErrorOnLabel(lblErrors, "Kullanıcı adı boş olamaz.");
        } else {
            String otpUrl = BASE_URL + otpURLPrefix;
            String jsonOTPBody = "{\"userName\": \"" + enteredUserName + "\"}";

            HTTPRequest.sendJsonRequest(otpUrl, "POST", jsonOTPBody, new HTTPRequest.RequestCallback() {
                @Override
                public void onSuccess(String otpResponse) throws IOException {
                    JSONObject otpResponseObject = new JSONObject(otpResponse);
                    Launcher.otpSentTime = String.valueOf(otpResponseObject.get("otpSentTime"));

                    if (Launcher.otpSentTime != null) {
                        System.out.println("OTP Sent Time: " + Launcher.otpSentTime);
                        changeOTPScreen();
                    } else {
                        Utils.showErrorOnLabel(lblErrors, "OTP kodu alınamadı.");
                    }
                }

                @Override
                public void onFailure() {
                    Utils.showErrorOnLabel(lblErrors, "Kullanıcı adı veya şifre hatalı !");
                }
            });
        }
    }

    private void changeOTPScreen() throws IOException {
        Stage stage = (Stage) btnSignin.getScene().getWindow();

        stage.close();
        SceneUtil.changeScreen("fxml/ResetPasswordEnterOTP.fxml");
    }
}
