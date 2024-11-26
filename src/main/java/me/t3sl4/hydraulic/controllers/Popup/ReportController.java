package me.t3sl4.hydraulic.controllers.Popup;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ReportController {

    @FXML
    private Button sendReportButton;

    @FXML
    public void initialize() {
        //TODO
    }

    public void popupKapat() {
        Stage stage = (Stage) sendReportButton.getScene().getWindow();

        stage.close();
    }

}
