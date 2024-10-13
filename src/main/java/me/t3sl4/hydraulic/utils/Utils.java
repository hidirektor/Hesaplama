package me.t3sl4.hydraulic.utils;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.*;
import javafx.util.Duration;
import me.t3sl4.hydraulic.Launcher;
import me.t3sl4.hydraulic.app.Main;
import me.t3sl4.hydraulic.controllers.Calculation.Klasik.PartList.KlasikParcaController;
import me.t3sl4.hydraulic.controllers.Popup.CylinderController;
import me.t3sl4.hydraulic.utils.database.File.FileUtil;
import me.t3sl4.hydraulic.utils.database.Model.Kabin.Kabin;
import me.t3sl4.hydraulic.utils.general.SceneUtil;
import me.t3sl4.hydraulic.utils.general.SystemVariables;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.UnaryOperator;

public class Utils {

    public static void showErrorMessage(String hataMesaji, Screen targetScreen, Stage currentStage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(currentStage);
        alert.setTitle("Hata");
        alert.setHeaderText(null);
        alert.setContentText(hataMesaji);

        centerAlertOnScreen(alert, targetScreen);
        alert.showAndWait();
    }

    public static void showSuccessMessage(String basariMesaji, Screen targetScreen, Stage currentStage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(currentStage);
        alert.setTitle("Başarılı !");
        alert.setHeaderText(null);
        alert.setContentText(basariMesaji);

        centerAlertOnScreen(alert, targetScreen);
        alert.showAndWait();
    }

    public static void showErrorOnLabel(Label mainLabel, String message) {
        mainLabel.setText(message);

        Duration delay = Duration.seconds(1.5);

        KeyFrame keyFrame = new KeyFrame(delay, event -> {
            mainLabel.setText(null);
        });

        Timeline timeline = new Timeline(keyFrame);
        timeline.play();
    }

    private static void centerAlertOnScreen(Alert alert, Screen targetScreen) {
        alert.setOnShown(event -> {
            Window alertWindow = alert.getDialogPane().getScene().getWindow();

            Rectangle2D bounds = targetScreen.getVisualBounds();
            double stageWidth = alertWindow.getWidth();
            double stageHeight = alertWindow.getHeight();

            double centerX = bounds.getMinX() + (bounds.getWidth() - stageWidth) / 2;
            double centerY = bounds.getMinY() + (bounds.getHeight() - stageHeight) / 2;

            alertWindow.setX(centerX);
            alertWindow.setY(centerY);
        });
    }

    public static boolean netIsAvailable() {
        try {
            final URL url = new URL("http://www.google.com");
            final URLConnection conn = url.openConnection();
            conn.connect();
            conn.getInputStream().close();
            return true;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            return false;
        }
    }

    public static void openURL(String url) {
        try {
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
        } catch (java.io.IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static double string2Double(String inputVal) {
        String[] secPmp = inputVal.split(" cc");

        return Double.parseDouble(secPmp[0]);
    }

    public static boolean checkUpperCase(String controlText) {
        for(int i=0; i<controlText.length(); i++) {
            char controlChar = controlText.charAt(i);
            if(Character.isUpperCase(controlChar)) {
                return true;
            }
        }
        return false;
    }

    public static void textFilter(javafx.scene.control.TextField filteredField) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        };
        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        filteredField.setTextFormatter(textFormatter);
    }

    public static void openMainScreen(Label sceneLabel) throws IOException {
        Stage stage = (Stage) sceneLabel.getScene().getWindow();
        Screen currentScreen = SceneUtil.getScreenOfNode(sceneLabel);
        stage.close();

        SceneUtil.changeScreen("fxml/Home.fxml", currentScreen);
    }

    public static void openRegisterScreen(Label sceneLabel) throws IOException {
        Screen currentScreen = SceneUtil.getScreenOfNode(sceneLabel);

        SceneUtil.changeScreen("fxml/Register.fxml", currentScreen);
    }

    public static void openLoginScreen(Label sceneLabel) throws IOException {
        Screen currentScreen = SceneUtil.getScreenOfNode(sceneLabel);

        SceneUtil.changeScreen("fxml/Login.fxml", currentScreen);
    }

    public static void openResetPasswordScreen(Label sceneLabel) throws IOException {
        Screen currentScreen = SceneUtil.getScreenOfNode(sceneLabel);

        SceneUtil.changeScreen("fxml/ResetPassword.fxml", currentScreen);
    }

    public static String showCyclinderPopup(Screen currentScreen, Stage currentStage) {
        Image icon = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/images/general/logo.png")));
        try {
            FXMLLoader loader = new FXMLLoader(Launcher.class.getResource("fxml/CylinderCount.fxml"));
            Parent root = loader.load();

            CylinderController controller = loader.getController();

            Stage stage = new Stage();

            Rectangle2D bounds = currentScreen.getVisualBounds();
            stage.setOnShown(event -> {
                double stageWidth = stage.getWidth();
                double stageHeight = stage.getHeight();

                // Calculate the center position
                double centerX = bounds.getMinX() + (bounds.getWidth() - stageWidth) / 2;
                double centerY = bounds.getMinY() + (bounds.getHeight() - stageHeight) / 2;

                // Set the stage position
                stage.setX(centerX);
                stage.setY(centerY);
            });

            stage.setTitle("Silindir Seçimi");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.getIcons().add(icon);
            stage.showAndWait();

            if (controller.isConfirmed()) {
                return controller.getFinalResult();
            } else {
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
            Utils.showErrorMessage("Silindir seçimi sırasında bir hata oluştu.", currentScreen, currentStage);
            return null;
        }
    }

    public static void showParcaListesiPopup(Image icon, Screen currentScreen, String fxmlPath) {
        AtomicReference<Double> screenX = new AtomicReference<>((double) 0);
        AtomicReference<Double> screenY = new AtomicReference<>((double) 0);

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(fxmlPath));
            VBox root = fxmlLoader.load();
            KlasikParcaController parcaController = fxmlLoader.getController();

            Stage popupStage = new Stage();
            Rectangle2D bounds = currentScreen.getVisualBounds();
            popupStage.setOnShown(event -> {
                double stageWidth = popupStage.getWidth();
                double stageHeight = popupStage.getHeight();

                double centerX = bounds.getMinX() + (bounds.getWidth() - stageWidth) / 2;
                double centerY = bounds.getMinY() + (bounds.getHeight() - stageHeight) / 2;

                popupStage.setX(centerX);
                popupStage.setY(centerY);
            });

            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initStyle(StageStyle.UNDECORATED);
            popupStage.setScene(new Scene(root));
            popupStage.getIcons().add(icon);

            root.setOnMousePressed(event -> {
                screenX.set(event.getSceneX());
                screenY.set(event.getSceneY());
            });
            root.setOnMouseDragged(event -> {

                popupStage.setX(event.getScreenX() - screenX.get());
                popupStage.setY(event.getScreenY() - screenY.get());

            });
            popupStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void offlineMod(Label lblErrors, Runnable onComplete) {
        Utils.showErrorOnLabel(lblErrors, "Standart kullanıcı olarak giriş yapılıyor !");

        Timeline timeline = new Timeline();
        timeline.setCycleCount(3);

        final int[] countdown = {2};
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), event1 -> {
            if (countdown[0] > 0) {
                Utils.showErrorOnLabel(lblErrors, "Aktarıma Son: " + countdown[0]);
                countdown[0]--;
            } else {
                timeline.stop();
                try {
                    Utils.openMainScreen(lblErrors);
                    if (onComplete != null) {
                        onComplete.run();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        timeline.getKeyFrames().add(keyFrame);
        timeline.playFromStart();
    }

    public static Kabin findTankByKabinName(String kabinName) {
        for (Kabin tank : SystemVariables.getLocalHydraulicData().inputTanks) {
            if (tank.getKabinName().equals(kabinName)) {
                return tank;
            }
        }
        return null;
    }

    public static void deleteLocalData() throws IOException {
        deleteFile(SystemVariables.tokenPath);

        deleteDirectory(new File(SystemVariables.profilePhotoLocalPath));

        deleteDirectory(new File(SystemVariables.pdfFileLocalPath));

        deleteDirectory(new File(SystemVariables.excelFileLocalPath));

        deleteDirectory(new File(SystemVariables.dataFileLocalPath));

        Main.loggedInUser = null;
        SystemVariables.accessToken = null;
        SystemVariables.refreshToken = null;
        SystemVariables.userID = null;
        SystemVariables.userName = null;

        FileUtil.setupFileSystem();
    }

    private static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("Dosya silindi: " + filePath);
            } else {
                System.err.println("Dosya silinemedi: " + filePath);
            }
        } else {
            System.out.println("Dosya bulunamadı: " + filePath);
        }
    }

    private static void deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        deleteFile(file.getAbsolutePath());
                    }
                }
            }
            if (directory.delete()) {
                System.out.println("Dizin silindi: " + directory.getAbsolutePath());
            } else {
                System.err.println("Dizin silinemedi: " + directory.getAbsolutePath());
            }
        } else {
            System.out.println("Dizin bulunamadı: " + directory.getAbsolutePath());
        }
    }
}