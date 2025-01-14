package me.t3sl4.hydraulic.controllers.Password;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import me.t3sl4.hydraulic.utils.Utils;
import me.t3sl4.hydraulic.utils.general.SceneUtil;
import me.t3sl4.hydraulic.utils.general.SystemVariables;
import me.t3sl4.hydraulic.utils.service.HTTP.HTTPMethod;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static me.t3sl4.hydraulic.utils.general.SystemVariables.BASE_URL;
import static me.t3sl4.hydraulic.utils.general.SystemVariables.verifyOTPURLPrefix;

public class ResetPasswordEnterOTPController implements Initializable {

    @FXML
    private Button btnCheckOtp;

    @FXML
    private Label lblErrors;

    @FXML
    private TextField txtOtp;

    @FXML
    private Label lblTimer;

    String girilenOTP;

    private Timeline timer;
    private int remainingTime = 60;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        timer = new Timeline(new KeyFrame(javafx.util.Duration.seconds(1), event -> {
            remainingTime--;
            updateTimerLabel();

            if (remainingTime <= 0) {
                timer.stop();
                try {
                    backMainScreen();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (remainingTime <= 20) {
                lblTimer.setTextFill(Color.valueOf("#D0342C"));
            }
        }));
        timer.setCycleCount(Animation.INDEFINITE);

        timer.play();
    }

    private void updateTimerLabel() {
        int minutes = remainingTime / 60;
        int seconds = remainingTime % 60;

        lblTimer.setText(String.format("%02d:%02d", minutes, seconds));
    }

    @FXML
    public void programiKapat() {
        Utils.systemShutdown();
    }

    @FXML
    public void goBackAction() throws IOException {
        Stage stage = (Stage) btnCheckOtp.getScene().getWindow();
        Screen currentScreen = SceneUtil.getScreenOfNode(lblErrors);

        stage.close();

        SceneUtil.changeScreen("fxml/Login.fxml", currentScreen);
    }

    @FXML
    public void onderWeb() {
        Utils.openURL("https://ondergrup.com");
    }

    @FXML
    public void koduDogrula() throws IOException {
        if(!txtOtp.getText().isEmpty()) {
            girilenOTP = txtOtp.getText();

            String otpVerifyUrl = BASE_URL + verifyOTPURLPrefix;
            String jsonOTPVerifyBody = "{\"userName\": \"" + ResetPasswordController.enteredUserName + "\", " +
                    "\"otpCode\": \"" + girilenOTP + "\", " +
                    "\"otpSentTime\": \"" + SystemVariables.otpSentTime + "\"}";

            HTTPMethod.sendJsonRequest(otpVerifyUrl, "POST", jsonOTPVerifyBody, new HTTPMethod.RequestCallback() {
                @Override
                public void onSuccess(String otpResponse) throws IOException {
                    changeOTPScreen();
                }

                @Override
                public void onFailure() {
                    Utils.showErrorOnLabel(lblErrors, "Girdiğin OTP kodu hatalı !");
                }
            });
        } else {
            Utils.showErrorOnLabel(lblErrors, "OTP kodunu girmedin !");
        }
    }

    private void changeOTPScreen() throws IOException {
        Stage stage = (Stage) btnCheckOtp.getScene().getWindow();
        Screen currentScreen = SceneUtil.getScreenOfNode(lblErrors);

        stage.close();

        SceneUtil.changeScreen("fxml/ResetPasswordEnterNewPassword.fxml", currentScreen);
    }

    private void backMainScreen() throws IOException {
        Stage stage = (Stage) btnCheckOtp.getScene().getWindow();
        Screen currentScreen = SceneUtil.getScreenOfNode(lblErrors);

        stage.close();

        SceneUtil.changeScreen("fxml/Login.fxml", currentScreen);
    }
}