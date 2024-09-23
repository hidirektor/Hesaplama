package me.t3sl4.hydraulic.Screens.Controllers.Calculation.Klasik;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class CyclinderController {
    @FXML
    private ComboBox<String> silindirSayisiComboBox;


    @FXML
    public void popupKapat() {
        Stage stage = (Stage) silindirSayisiComboBox.getScene().getWindow();

        stage.close();
    }

    @FXML
    public void silindirSayisiPressed() {
        silindirSayisiComboBox.getItems().clear();
        silindirSayisiComboBox.getItems().addAll("1 Silindir", "2 Silindir", "3 Silindir", "4 Silindir");

        //Seçilen sayıyı parça ve ana controller'a atama yap
    }
}
