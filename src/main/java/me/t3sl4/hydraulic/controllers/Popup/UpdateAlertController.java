package me.t3sl4.hydraulic.controllers.Popup;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import me.t3sl4.hydraulic.utils.Utils;
import me.t3sl4.hydraulic.utils.general.SystemVariables;

import java.io.IOException;

public class UpdateAlertController {

    @FXML
    private Label versionLabel;
    @FXML
    private Label updateDetails;

    private String versionCode;

    private Stage currentStage;

    public void setUpdateDetails(String version, String details) {
        versionCode = version;
        versionLabel.setText("Yeni Sürüm: " + version);
        updateDetails.setText(details);
    }

    @FXML
    private void handleUpdate() throws IOException {
        String launcherPath = SystemVariables.mainPath + "windows_Launcher.exe";

        Process process = new ProcessBuilder(launcherPath).start();
        Utils.systemShutdown();
    }

    @FXML
    public void programiKapat() {
        Utils.systemShutdown();
    }

    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }
}
