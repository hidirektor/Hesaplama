package me.t3sl4.hydraulic.controllers.Popup;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import lombok.Setter;
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

    @Setter
    private Stage currentStage;

    private String versionCode;

    public void setUpdateDetails(String version, String details) {
        versionCode = version;
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
                File newDirectory = new File(selectedDirectory, "Hidrolik Yeni Sürüm v" + versionCode);
                if (newDirectory.exists()) {
                    deleteDirectoryRecursively(newDirectory);
                    System.out.println("'Hidrolik Yeni Sürüm v'" + versionCode + "' klasörü silindi ve yeniden oluşturulacak.");
                }
                boolean created = newDirectory.mkdirs();
                if (created) {
                    System.out.println("Yeni klasör 'Hidrolik Yeni Sürüm v" + versionCode + "' oluşturuldu.");
                } else {
                    System.out.println("Yeni klasör oluşturulamadı.");
                    Utils.showErrorMessage("Yeni klasör oluşturulamadı. Lütfen farklı bir konum seçin.", SceneUtil.getScreenOfNode(versionLabel), (Stage) versionLabel.getScene().getWindow());
                    return;
                }

                selectedDirectory = newDirectory;
                Utils.showErrorMessage("Seçilen dizinde 'windows_Hydraulic' ile başlayan bir dosya zaten var. 'Hidrolik Yeni Sürüm' klasörü oluşturuldu.",
                        SceneUtil.getScreenOfNode(versionLabel), (Stage) versionLabel.getScene().getWindow());
            } else {
                selectedDirectory = directoryChooser.showDialog(currentStage);
            }

            downloadBar.setVisible(true);
            scissorLiftView.setVisible(true);

            File finalSelectedDirectory = selectedDirectory;
            Task<Void> downloadTask = new Task<>() {
                @Override
                protected Void call() {
                    try {
                        Utils.downloadLatestVersion(finalSelectedDirectory, this::updateProgress);
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
    public void programiKapat() {
        Utils.systemShutdown();
    }

    private void deleteDirectoryRecursively(File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteDirectoryRecursively(file);
                }
            }
        }
        directory.delete();
    }
}
