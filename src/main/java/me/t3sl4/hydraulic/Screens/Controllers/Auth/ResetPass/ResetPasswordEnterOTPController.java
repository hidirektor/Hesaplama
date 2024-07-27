package me.t3sl4.hydraulic.Screens.Controllers.Auth.ResetPass;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import me.t3sl4.hydraulic.Launcher;
import me.t3sl4.hydraulic.Screens.SceneUtil;
import me.t3sl4.hydraulic.Utility.HTTP.HTTPRequest;
import me.t3sl4.hydraulic.Utility.Utils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static me.t3sl4.hydraulic.Launcher.BASE_URL;
import static me.t3sl4.hydraulic.Launcher.verifyOTPURLPrefix;

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
    public void programiKapat(MouseEvent event) {
        System.exit(0);
    }

    @FXML
    public void goBackAction() throws IOException {
        Stage stage = (Stage) btnCheckOtp.getScene().getWindow();

        stage.close();
        SceneUtil.changeScreen("fxml/Login.fxml");
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
                    "\"otpSentTime\": \"" + Launcher.otpSentTime + "\"}";

            HTTPRequest.sendJsonRequest(otpVerifyUrl, "POST", jsonOTPVerifyBody, new HTTPRequest.RequestCallback() {
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

        stage.close();
        SceneUtil.changeScreen("fxml/ResetPasswordEnterNewPassword.fxml");
    }

    private void backMainScreen() throws IOException {
        Stage stage = (Stage) btnCheckOtp.getScene().getWindow();

        stage.close();
        SceneUtil.changeScreen("fxml/Login.fxml");
    }
}