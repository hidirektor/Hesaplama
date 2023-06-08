package me.t3sl4.hesaplama;

import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class MainController {
    @FXML
    private ComboBox<String> firstOption;

    @FXML
    private ComboBox<String> secondOption;

    @FXML
    private ComboBox<String> thirthOption;

    @FXML
    private ComboBox<String> fourthOption;

    @FXML
    private TextArea sonucArea;

    private HostServices hostServices;

    public void initialize() {
        // ComboBox'lara Lorem Ipsum değerlerini ekle
        addLoremIpsumItems(firstOption);
        addLoremIpsumItems(secondOption);
        addLoremIpsumItems(thirthOption);
        addLoremIpsumItems(fourthOption);
    }

    @FXML
    public void hesaplaFunc() {
        // ComboBox'ların seçili değerlerini kontrol et
        if (firstOption.getSelectionModel().isEmpty() || secondOption.getSelectionModel().isEmpty() ||
                thirthOption.getSelectionModel().isEmpty() || fourthOption.getSelectionModel().isEmpty()) {
            showErrorMessage("Hata", "Lütfen tüm seçenekleri belirleyin.");
        } else {
            // Hesaplama işlemini burada gerçekleştir
            // Sonucu sonucArea'ya yazdır
        }
    }

    @FXML
    public void yardimFunc() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Yardım");
        alert.setHeaderText("Yardım İçin GitHub Sayfasına Yönlendiriliyorsunuz");

        Text text = new Text("Yardım dokümantasyonuna ulaşmak için aşağıdaki linke tıklayınız:");
        Hyperlink link = new Hyperlink("GitHub Dokümantasyonu");
        link.setOnAction(this::openGitHubDocumentation);

        VBox vbox = new VBox(text, link);
        alert.getDialogPane().setContent(vbox);
        alert.showAndWait();
    }

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    private void openGitHubDocumentation(ActionEvent event) {
        String url = "https://github.com/hidirektor";
        hostServices.showDocument(url);
    }

    private void addLoremIpsumItems(ComboBox<String> comboBox) {
        comboBox.getItems().addAll("Lorem Ipsum 1", "Lorem Ipsum 2", "Lorem Ipsum 3");
    }

    private void showErrorMessage(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}