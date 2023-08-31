package me.t3sl4.hydraulic.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import me.t3sl4.hydraulic.Util.Util;
import me.t3sl4.hydraulic.Util.HTTP.HTTPRequest;
import me.t3sl4.hydraulic.Util.SceneUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static me.t3sl4.hydraulic.Launcher.*;

public class ResetPasswordController implements Initializable {

    @FXML
    private Button btnSignin;

    @FXML
    private TextField txtEmail;

    @FXML
    private Label lblErrors;

    public static String otpCode;

    public static String enteredEmail;

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
        Util.openURL("https://ondergrup.com");
    }

    @FXML
    private void sifreyiSifirla() {
        String email = txtEmail.getText();

        if (email.isEmpty()) {
            lblErrors.setText("E-posta adresi boş olamaz.");
        } else {
            if (isValidEmail(email)) {

                String otpUrl = BASE_URL + otpURLPrefix;
                String jsonOTPBody = "{\"Email\": \"" + email + "\"}";

                HTTPRequest.sendRequest(otpUrl, jsonOTPBody, new HTTPRequest.RequestCallback() {
                    @Override
                    public void onSuccess(String otpResponse) throws IOException {
                        otpCode = parseOTPCodeFromResponse(otpResponse);
                        if (otpCode != null) {
                            System.out.println("OTP Code: " + otpCode);
                            enteredEmail = email;
                            changeOTPScreen();
                        } else {
                            lblErrors.setText("OTP kodu alınamadı.");
                        }
                    }

                    @Override
                    public void onFailure() {
                        lblErrors.setText("Kullanıcı adı veya şifre hatalı !");
                    }
                });

            } else {
                lblErrors.setText("Geçerli bir e-posta adresi girin.");
            }
        }
    }

    private void changeOTPScreen() throws IOException {
        Stage stage = (Stage) btnSignin.getScene().getWindow();

        stage.close();
        SceneUtil.changeScreen("fxml/ResetPasswordEnterOTP.fxml");
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private String parseOTPCodeFromResponse(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            if (jsonResponse.has("message") && jsonResponse.has("otpCode")) {
                String message = jsonResponse.getString("message");
                String otpCode = jsonResponse.getString("otpCode");
                return otpCode;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
