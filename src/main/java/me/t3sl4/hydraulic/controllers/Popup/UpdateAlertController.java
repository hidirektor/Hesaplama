package me.t3sl4.hydraulic.controllers.Popup;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import me.t3sl4.hydraulic.utils.Utils;
import me.t3sl4.hydraulic.utils.general.SceneUtil;
import me.t3sl4.hydraulic.utils.general.SystemVariables;

import java.io.File;
import java.io.IOException;

public class UpdateAlertController {

    @FXML
    private ProgressBar downloadBar;

    @FXML
    private Label versionLabel;
    @FXML
    private Label updateDetails;

    @FXML
    private ImageView scissorLiftView;

    @FXML
    private ImageView closeButton;

    private Stage currentStage;

    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }

    public void setUpdateDetails(String version, String details) {
        versionLabel.setText("Yeni Sürüm: " + version);
        updateDetails.setText(details);
    }

    @FXML
    private void handleUpdate() {
        Utils.openURL(SystemVariables.NEW_VERSION_URL);
        Platform.exit();
        System.exit(0); // Programı kapat
    }

    @FXML
    private void handleDownload() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("İndirme Konumunu Seçin");
        File selectedDirectory = directoryChooser.showDialog(currentStage);

        if (selectedDirectory != null) {
            File[] matchingFiles = selectedDirectory.listFiles(file -> file.getName().startsWith("windows_Hydraulic"));
            if (matchingFiles != null && matchingFiles.length > 0) {
                Utils.showErrorMessage("Seçilen dizinde 'windows_Hydraulic' ile başlayan bir dosya zaten var. Lütfen farklı bir konum seçin.", SceneUtil.getScreenOfNode(versionLabel), (Stage)versionLabel.getScene().getWindow());
                return;
            }

            downloadBar.setVisible(true);
            scissorLiftView.setVisible(true);

            Task<Void> downloadTask = new Task<>() {
                @Override
                protected Void call() {
                    try {
                        Utils.downloadLatestVersion(selectedDirectory, this::updateProgress);
                        closeButton.setVisible(true);
                        downloadBar.setVisible(false);
                        scissorLiftView.setVisible(false);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            };

            downloadBar.progressProperty().bind(downloadTask.progressProperty());

            Thread downloadThread = new Thread(downloadTask);
            downloadThread.setDaemon(true);
            downloadThread.start();
        } else {
            System.out.println("İndirme işlemi iptal edildi.");
        }
    }

    @FXML
    public void ekraniKapat() {
        System.exit(1);
    }
}
