package me.t3sl4.hydraulic.controllers.Editor;

import com.jfoenix.controls.JFXCheckBox;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class EditorController {

    @FXML
    private Button saveButton;

    @FXML
    private JFXCheckBox classicEditor;

    @FXML
    private JFXCheckBox modernEditor;

    @FXML
    private ImageView minimizeImage;

    public void initialize() {
        minimizeImage.toFront();

        classicEditor.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                modernEditor.setSelected(false);
                classicMode();
            }
        });

        modernEditor.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                classicEditor.setSelected(false);
                modernMode();
            }
        });
    }

    public void minimizeProgram() {
        if (saveButton != null) {
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.setIconified(true);
        }
    }

    private void classicMode() {
        System.out.println("Classic Mode seçildi");
    }

    private void modernMode() {
        System.out.println("Modern Mode seçildi");
    }
}
