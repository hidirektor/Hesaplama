package me.t3sl4.hydraulic.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Window;
import javafx.stage.*;
import javafx.util.Duration;
import me.t3sl4.hydraulic.Launcher;
import me.t3sl4.hydraulic.app.Main;
import me.t3sl4.hydraulic.controllers.MainController;
import me.t3sl4.hydraulic.controllers.Popup.CylinderController;
import me.t3sl4.hydraulic.controllers.Popup.UpdateAlertController;
import me.t3sl4.hydraulic.utils.database.File.FileUtil;
import me.t3sl4.hydraulic.utils.database.Model.Kabin.Kabin;
import me.t3sl4.hydraulic.utils.database.Model.Table.HydraulicUnitList.HydraulicInfo;
import me.t3sl4.hydraulic.utils.general.SceneUtil;
import me.t3sl4.hydraulic.utils.general.SystemVariables;
import me.t3sl4.hydraulic.utils.general.VersionUtility;
import me.t3sl4.hydraulic.utils.service.UserDataService.User;
import org.json.JSONArray;
import org.json.JSONObject;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

public class Utils {

    public static final Logger logger = Logger.getLogger(MainController.class.getName());

    private static final String REGISTRY_PATH = "SOFTWARE\\OnderGrup\\HydraulicCalculation";
    private static final String LICENSE_KEY_NAME = "LicenseKey";

    public static final String PREFERENCE_KEY = "defaultMonitor";
    public static Preferences prefs;

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

        alert.getButtonTypes().setAll(ButtonType.OK);

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

    public static void checkLocalUserData(Runnable onFailure) {
        String userHome = System.getProperty("user.name");
        String os = System.getProperty("os.name").toLowerCase();
        String basePath;

        if (os.contains("win")) {
            basePath = "C:/Users/" + userHome + "/";
        } else {
            basePath = "/Users/" + userHome + "/";
        }
        String tokenPath = basePath + "/OnderGrup/" + "userData/auth.txt";
        if(new File(tokenPath).exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(tokenPath));
                if(reader != null) {
                    String line;
                    String userName = null, userID = null, accessToken = null, refreshToken = null;

                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("userName: ")) {
                            userName = line.substring("userName: ".length());
                        } else if (line.startsWith("userID: ")) {
                            userID = line.substring("userID: ".length());
                        } else if (line.startsWith("AccessToken: ")) {
                            accessToken = line.substring("AccessToken: ".length());
                        } else if (line.startsWith("RefreshToken: ")) {
                            refreshToken = line.substring("RefreshToken: ".length());
                        }
                    }
                    reader.close();

                    if (userName != null && userID != null && accessToken != null && refreshToken != null) {
                        SystemVariables.loggedInUser = new User(userName);

                        SystemVariables.loggedInUser.setUserID(userID);
                        SystemVariables.loggedInUser.setAccessToken(accessToken);
                        SystemVariables.loggedInUser.setRefreshToken(refreshToken);
                    } else {
                        if(onFailure != null) {
                            onFailure.run();
                        }
                    }
                } else {
                    if(onFailure != null) {
                        onFailure.run();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
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

    public static void openMainScreenOnTargetScreen(Screen currentScreen) throws IOException {
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

    public static String showCyclinderPopup(Screen currentScreen, Stage currentStage, String platformTipi, String birinciValf) {
        Image icon = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/images/general/logo.png")));
        try {
            FXMLLoader loader = new FXMLLoader(Launcher.class.getResource("fxml/CylinderCount.fxml"));
            Parent root = loader.load();

            CylinderController controller = loader.getController();

            if(platformTipi != null && birinciValf != null) {
                controller.setPlatformTipi(platformTipi);
                controller.setBirinciValf(birinciValf);
            }

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

    public static void showLicensePopup(Screen currentScreen, Stage currentStage) {
        Image icon = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/images/general/logo.png")));
        try {
            FXMLLoader loader = new FXMLLoader(Launcher.class.getResource("fxml/LicensePopup.fxml"));
            Parent root = loader.load();

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

        } catch (IOException e) {
            e.printStackTrace();
            Utils.showErrorMessage("Lisans anahtarı girilirken hata meydana geldi.", currentScreen, currentStage);
        }
    }

    public static void showParcaListesiPopup(Image icon, Screen currentScreen, String fxmlPath) {
        AtomicReference<Double> screenX = new AtomicReference<>((double) 0);
        AtomicReference<Double> screenY = new AtomicReference<>((double) 0);

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(fxmlPath));
            VBox root = fxmlLoader.load();

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

    public static void showMonitorSelectionScreen(List<Screen> screens, Screen currentScreen, boolean redirectStatus) {
        Stage selectionStage = new Stage();
        VBox layout = new VBox(10);
        layout.setStyle("-fx-background-color: #353a46;");
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 300, 200);

        Label label = new Label("Lütfen programın standart olarak\naçılmasını istediğiniz monitörünüzü seçin :))");
        label.setTextAlignment(TextAlignment.CENTER);
        label.setStyle("-fx-text-fill: white;");

        ObservableList<String> monitorOptions = FXCollections.observableArrayList();
        for (int i = 0; i < screens.size(); i++) {
            String deviceName = screens.get(i).getBounds().getWidth() + "x" + screens.get(i).getBounds().getHeight();
            monitorOptions.add("Monitor " + (i + 1) + " - " + deviceName + " - " + getMonitorBrand(i));
        }

        ComboBox<String> monitorComboBox = new ComboBox<>(monitorOptions);
        monitorComboBox.getSelectionModel().selectFirst();

        Button selectButton = new Button("Seç");
        selectButton.setStyle("-fx-background-color: #1761ab; -fx-text-fill: white;");
        selectButton.setOnAction(event -> {
            String selectedMonitor = monitorComboBox.getSelectionModel().getSelectedItem();
            saveSelectedMonitor(selectedMonitor);

            try {
                int monitorIndex = Integer.parseInt(selectedMonitor.split(" ")[1]) - 1;
                if(redirectStatus) {
                    SceneUtil.openMainScreen(screens.get(monitorIndex));
                }
                selectionStage.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        layout.getChildren().addAll(label, monitorComboBox, selectButton);

        selectionStage.setScene(scene);
        selectionStage.initStyle(StageStyle.TRANSPARENT);
        selectionStage.initStyle(StageStyle.UNDECORATED);
        selectionStage.setResizable(false);
        selectionStage.centerOnScreen();

        if(currentScreen != null) {
            Rectangle2D bounds = currentScreen.getVisualBounds();
            selectionStage.setOnShown(event -> {
                double stageWidth = selectionStage.getWidth();
                double stageHeight = selectionStage.getHeight();

                // Calculate the center position
                double centerX = bounds.getMinX() + (bounds.getWidth() - stageWidth) / 2;
                double centerY = bounds.getMinY() + (bounds.getHeight() - stageHeight) / 2;

                // Set the stage position
                selectionStage.setX(centerX);
                selectionStage.setY(centerY);
            });
        }

        selectionStage.show();
    }

    private static String getMonitorBrand(int index) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = ge.getScreenDevices();

        if (index < devices.length) {
            return devices[index].getIDstring();
        }

        return "Unknown Monitor";
    }

    private static void saveSelectedMonitor(String monitor) {
        prefs.put(PREFERENCE_KEY, monitor);
    }

    public static String checkDefaultMonitor() {
        return Utils.prefs.get(PREFERENCE_KEY, null);
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

    public static Kabin findClassicTankByKabinName(String kabinName) {
        for (Kabin tank : SystemVariables.getLocalHydraulicData().classicCabins) {
            if (tank.getKabinName().equals(kabinName)) {
                return tank;
            }
        }
        return null;
    }

    public static Kabin findPowerPackTankByKabinName(String kabinName) {
        for (Kabin tank : SystemVariables.getLocalHydraulicData().powerPackCabins) {
            if (tank.getKabinName().equals(kabinName)) {
                return tank;
            }
        }
        return null;
    }

    public static void deleteLocalData() throws IOException {
        deleteFile(SystemVariables.tokenPath);

        deleteDirectory(new File(SystemVariables.dataFileLocalPath));

        SystemVariables.loggedInUser = null;

        FileUtil.criticalFileSystem();
        Thread systemThread = new Thread(FileUtil::setupLocalData);
        systemThread.start();
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

    public static boolean checkUpdateAndCancelEvent(Stage currentStage) {
        String[] updateInfo = VersionUtility.checkForUpdate();
        if (updateInfo != null) {
            showUpdateAlert(currentStage, updateInfo[0], updateInfo[1]); // Version and details
        }
        return false;
    }

    private static void showUpdateAlert(Stage currentStage, String version, String details) {
        try {
            FXMLLoader loader = new FXMLLoader(Launcher.class.getResource("fxml/UpdateAlert.fxml"));
            AnchorPane root = loader.load();

            UpdateAlertController controller = loader.getController();
            controller.setCurrentStage(currentStage);
            controller.setUpdateDetails(version, details);

            Stage alertStage = new Stage();
            alertStage.setTitle("Güncelleme Mevcut");
            alertStage.initModality(Modality.APPLICATION_MODAL);
            alertStage.initStyle(StageStyle.UNDECORATED);
            alertStage.initOwner(currentStage);

            Rectangle2D bounds = Main.defaultScreen.getVisualBounds();
            alertStage.setOnShown(event -> {
                double stageWidth = alertStage.getWidth();
                double stageHeight = alertStage.getHeight();

                // Calculate the center position
                double centerX = bounds.getMinX() + (bounds.getWidth() - stageWidth) / 2;
                double centerY = bounds.getMinY() + (bounds.getHeight() - stageHeight) / 2;

                // Set the stage position
                alertStage.setX(centerX);
                alertStage.setY(centerY);
            });

            Scene scene = new Scene(root);
            alertStage.setScene(scene);
            alertStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void downloadLatestVersion(File selectedDirectory, ProgressUpdater progressUpdater) throws IOException {
        String os = System.getProperty("os.name").toLowerCase();
        String downloadURL = getDownloadURLForOS(os);

        if (downloadURL == null) {
            System.out.println("Uygun sürüm bulunamadı.");
            return;
        }

        File downloadFile = new File(selectedDirectory.getAbsolutePath() + "/" + getFileNameFromURL(downloadURL));
        try (BufferedInputStream in = new BufferedInputStream(new URL(downloadURL).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(downloadFile)) {

            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            long totalBytesRead = 0;
            long fileSize = new URL(downloadURL).openConnection().getContentLengthLong();

            while ((bytesRead = in.read(dataBuffer, 0, dataBuffer.length)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
                totalBytesRead += bytesRead;

                // İlerlemeyi güncelle
                progressUpdater.updateProgress(totalBytesRead, fileSize);
            }

            System.out.println("Dosya başarıyla indirildi: " + downloadFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private static String getDownloadURLForOS(String os) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(SystemVariables.ASSET_URL).openConnection();
        connection.setRequestProperty("Accept", "application/vnd.github.v3+json");

        if (connection.getResponseCode() == 200) {
            String jsonResponse = new Scanner(connection.getInputStream(), "UTF-8").useDelimiter("\\A").next();
            JSONObject releaseData = new JSONObject(jsonResponse);
            JSONArray assets = releaseData.getJSONArray("assets");

            for (int i = 0; i < assets.length(); i++) {
                JSONObject asset = assets.getJSONObject(i);
                String assetName = asset.getString("name");

                if (os.contains("win") && assetName.contains("windows")) {
                    return asset.getString("browser_download_url");
                } else if (os.contains("mac") && assetName.contains("macOS")) {
                    return asset.getString("browser_download_url");
                } else if ((os.contains("nix") || os.contains("nux")) && assetName.contains("linux")) {
                    return asset.getString("browser_download_url");
                }
            }
        } else {
            System.out.println("GitHub API'ye erişilemedi: " + connection.getResponseMessage());
        }

        return null;
    }

    private static String getFileNameFromURL(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }

    public static String formatDateTime(String unixTimestamp) {
        try {
            Instant instant = Instant.ofEpochSecond(Long.valueOf(unixTimestamp));
            OffsetDateTime dateTime = instant.atOffset(ZoneId.of("Europe/Istanbul").getRules().getOffset(instant));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            return dateTime.format(formatter);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return "";
        }
    }

    public static String calculateTimeRemaining(String unixTimestampString) {
        long unixTimestamp = Long.parseLong(unixTimestampString);

        Instant instant = Instant.ofEpochSecond(unixTimestamp);
        LocalDateTime targetDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        LocalDateTime now = LocalDateTime.now();

        long years = ChronoUnit.YEARS.between(now, targetDateTime);
        now = now.plusYears(years);

        long months = ChronoUnit.MONTHS.between(now, targetDateTime);
        now = now.plusMonths(months);

        long days = ChronoUnit.DAYS.between(now, targetDateTime);
        now = now.plusDays(days);

        long hours = ChronoUnit.HOURS.between(now, targetDateTime);
        now = now.plusHours(hours);

        long minutes = ChronoUnit.MINUTES.between(now, targetDateTime);

        return years + " yıl, " + months + " ay, " + days + " gün, " + hours + " saat, " + minutes + " dk";
    }

    public static String formatDateTimeMultiLine(String unixTimestamp) {
        try {
            Instant instant = Instant.ofEpochSecond(Long.valueOf(unixTimestamp));

            OffsetDateTime dateTime = instant.atOffset(ZoneId.of("Europe/Istanbul").getRules().getOffset(instant));

            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            String formattedTime = dateTime.format(timeFormatter);
            String formattedDate = dateTime.format(dateFormatter);

            return formattedTime + "\n" + formattedDate;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return "";
        }
    }

    @FunctionalInterface
    public interface ProgressUpdater {
        void updateProgress(long bytesRead, long totalBytes);
    }

    @SuppressWarnings("unchecked")
    public static void createLocalUnitData(String yamlFilePath, String orderNumber, String createdDate, String unitType,
                                           String pdfPath, String excelPath, String isOffline, String createdBy) {
        LoaderOptions loaderOptions = new LoaderOptions();
        Yaml yaml = new Yaml(loaderOptions);
        Map<String, Object> data;

        File yamlFile = new File(yamlFilePath);
        if (!yamlFile.exists()) {
            data = new HashMap<>();
            data.put("local_units", new HashMap<>());
        } else {
            try (FileReader reader = new FileReader(yamlFile)) {
                data = yaml.load(reader);
                if (data == null) {
                    data = new HashMap<>();
                    data.put("local_units", new HashMap<>());
                }
            } catch (IOException e) {
                throw new RuntimeException("YAML dosyası okunurken bir hata oluştu", e);
            }
        }

        Map<String, Map<String, Object>> localUnits = (Map<String, Map<String, Object>>) data.get("local_units");

        boolean found = false;
        for (Map.Entry<String, Map<String, Object>> entry : localUnits.entrySet()) {
            if (entry.getValue().get("order_number").equals(orderNumber)) {
                if (pdfPath != null) entry.getValue().put("pdf_path", pdfPath);
                if (excelPath != null) entry.getValue().put("excel_path", excelPath);
                found = true;
                break;
            }
        }

        if (!found) {
            int nextIndex = localUnits.size();
            Map<String, Object> newEntry = new HashMap<>();
            newEntry.put("order_number", orderNumber);
            newEntry.put("created_date", createdDate);
            newEntry.put("unit_type", unitType);
            newEntry.put("pdf_path", pdfPath == null ? "" : pdfPath);
            newEntry.put("excel_path", excelPath == null ? "" : excelPath);

            Map<String, String> creationData = new HashMap<>();
            creationData.put("isOffline", isOffline);
            creationData.put("created_by", createdBy);

            newEntry.put("creation_data", creationData);

            localUnits.put(String.valueOf(nextIndex), newEntry);
        }

        try (FileWriter writer = new FileWriter(yamlFile)) {
            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            options.setDefaultScalarStyle(DumperOptions.ScalarStyle.DOUBLE_QUOTED);
            yaml = new Yaml(options);
            yaml.dump(data, writer);
        } catch (IOException e) {
            throw new RuntimeException("YAML dosyasına yazılırken bir hata oluştu", e);
        }
    }

    @SuppressWarnings("unchecked")
    public static void readLocalHydraulicUnits(String yamlFilePath, List<HydraulicInfo> localHydraulicInfos, String unitType) {
        File yamlFile = new File(yamlFilePath);
        if (!yamlFile.exists()) {
            System.out.println("YAML dosyası bulunamadı.");
            return;
        }

        LoaderOptions loaderOptions = new LoaderOptions();
        Yaml yaml = new Yaml(loaderOptions);
        Map<String, Object> data;

        try (FileReader reader = new FileReader(yamlFile)) {
            data = yaml.load(reader);
            if (data == null) {
                System.out.println("YAML dosyası boş.");
                return;
            }
        } catch (IOException e) {
            throw new RuntimeException("YAML dosyası okunurken bir hata oluştu", e);
        }

        Map<String, Map<String, Object>> localUnits = (Map<String, Map<String, Object>>) data.get("local_units");
        if (localUnits == null || localUnits.isEmpty()) {
            System.out.println("Hydraulic unit bulunamadı.");
            return;
        }

        int totalUnits = localUnits.size();
        //System.out.println("Toplam Hydraulic Unit Sayısı: " + totalUnits);

        int hidrosCount = 0;
        int klasikCount = 0;
        for (Map<String, Object> unit : localUnits.values()) {
            String unitTypeFromData = (String) unit.get("unit_type");
            if ("PowerPack".equalsIgnoreCase(unitTypeFromData)) {
                hidrosCount++;
            } else if ("Klasik".equalsIgnoreCase(unitTypeFromData)) {
                klasikCount++;
            }
        }

        //System.out.println("Hidros Unit Sayısı: " + hidrosCount);
        //System.out.println("Klasik Unit Sayısı: " + klasikCount);

        for (Map.Entry<String, Map<String, Object>> entry : localUnits.entrySet()) {
            Map<String, Object> unitData = entry.getValue();
            String currentUnitType = (String) unitData.get("unit_type");

            if (unitType != null && !unitType.equalsIgnoreCase(currentUnitType)) {
                continue;
            }

            HydraulicInfo hydraulicInfo = new HydraulicInfo();
            hydraulicInfo.setId(Integer.parseInt(entry.getKey()));
            hydraulicInfo.setLocal(true);
            hydraulicInfo.setOrderID((String) unitData.get("order_number"));
            hydraulicInfo.setHydraulicType(currentUnitType);
            hydraulicInfo.setSchematicID((String) unitData.get("pdf_path"));
            hydraulicInfo.setPartListID((String) unitData.get("excel_path"));
            hydraulicInfo.setCreatedDate(Long.parseLong((String) unitData.get("created_date")));

            Map<String, String> creationData = (Map<String, String>) unitData.get("creation_data");
            if (creationData != null) {
                hydraulicInfo.setUserID(creationData.get("created_by"));
                hydraulicInfo.setUserName(creationData.get("created_by"));
            }

            localHydraulicInfos.add(hydraulicInfo);
        }

        /*System.out.println("\nYAML'deki filtrelenmiş datalar listeye eklendi:");
        for (HydraulicInfo info : localHydraulicInfos) {
            System.out.println("ID: " + info.getId());
            System.out.println("Order ID: " + info.getOrderID());
            System.out.println("Hydraulic Type: " + info.getHydraulicType());
            System.out.println("Schematic ID (PDF Path): " + info.getSchematicID());
            System.out.println("Created Date: " + info.getCreatedDate());
            System.out.println("User ID: " + info.getUserID());
            System.out.println("User Name: " + info.getUserName());
            System.out.println("-----------------------");
        }*/
    }

    public static String getCurrentUnixTime() {
        long unixTime = Instant.now().getEpochSecond();
        return String.valueOf(unixTime);
    }

    public static void openFile(String filePath) {
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("Dosya bulunamadı: " + filePath);
            return;
        }

        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.open(file);
                System.out.println("Dosya başarıyla açıldı: " + filePath);
            } catch (IOException e) {
                System.out.println("Dosya açılamadı: " + e.getMessage());
            }
        } else {
            System.out.println("Bu platform masaüstü fonksiyonlarını desteklemiyor.");
        }
    }

    @SuppressWarnings("unchecked")
    public static void deleteLocalUnitData(String yamlFilePath, String orderID) {
        LoaderOptions loaderOptions = new LoaderOptions();
        Yaml yaml = new Yaml(loaderOptions);
        Map<String, Object> data;

        File yamlFile = new File(yamlFilePath);
        if (!yamlFile.exists()) {
            throw new RuntimeException("YAML dosyası bulunamadı");
        }

        try (FileReader reader = new FileReader(yamlFile)) {
            data = yaml.load(reader);
            if (data == null || !data.containsKey("local_units")) {
                throw new RuntimeException("YAML dosyasında geçerli bir 'local_units' alanı bulunamadı");
            }
        } catch (IOException e) {
            throw new RuntimeException("YAML dosyası okunurken bir hata oluştu", e);
        }

        Map<String, Map<String, Object>> localUnits = (Map<String, Map<String, Object>>) data.get("local_units");
        String targetKey = null;

        // Find the entry to delete by matching order_number
        for (Map.Entry<String, Map<String, Object>> entry : localUnits.entrySet()) {
            Map<String, Object> unitData = entry.getValue();
            if (orderID.equals(unitData.get("order_number"))) {
                targetKey = entry.getKey();

                // Delete associated files if paths are specified
                String pdfPath = (String) unitData.get("pdf_path");
                String excelPath = (String) unitData.get("excel_path");

                if (pdfPath != null && !pdfPath.isEmpty()) {
                    new File(pdfPath).delete();
                }
                if (excelPath != null && !excelPath.isEmpty()) {
                    new File(excelPath).delete();
                }
                break;
            }
        }

        if (targetKey == null) {
            throw new RuntimeException("Verilen orderID ile eşleşen bir kayıt bulunamadı.");
        }

        // Remove the target entry
        localUnits.remove(targetKey);

        // Re-index remaining entries
        Map<String, Map<String, Object>> updatedUnits = new LinkedHashMap<>();
        int index = 0;
        for (Map.Entry<String, Map<String, Object>> entry : localUnits.entrySet()) {
            updatedUnits.put(String.valueOf(index++), entry.getValue());
        }

        data.put("local_units", updatedUnits);

        // Write the updated data back to the YAML file
        try (FileWriter writer = new FileWriter(yamlFile)) {
            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            options.setDefaultScalarStyle(DumperOptions.ScalarStyle.DOUBLE_QUOTED);
            yaml = new Yaml(options);
            yaml.dump(data, writer);
        } catch (IOException e) {
            throw new RuntimeException("YAML dosyasına yazılırken bir hata oluştu", e);
        }
    }

    public static void parseHydraulicJsonResponse(String response, List<HydraulicInfo> finalHydraulicUnitList) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            HydraulicInfo[] infoArray = objectMapper.readValue(response, HydraulicInfo[].class);
            finalHydraulicUnitList.addAll(Arrays.asList(infoArray));
        } catch (IOException e) {
            Utils.logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static String getDeviceInfoAsJson() {
        try {
            String osName = System.getProperty("os.name");
            String osVersion = System.getProperty("os.version");
            String osArch = System.getProperty("os.arch");
            int availableProcessors = Runtime.getRuntime().availableProcessors();

            long maxMemory = Runtime.getRuntime().maxMemory();
            long totalMemory = Runtime.getRuntime().totalMemory();

            String ipAddress = getIpAddress();
            String externalIpAddress = getExternalIpAddress();
            String hwid = getHardwareId();

            JSONObject deviceInfoJson = new JSONObject();
            deviceInfoJson.put("osName", osName);
            deviceInfoJson.put("osVersion", osVersion);
            deviceInfoJson.put("osArch", osArch);
            deviceInfoJson.put("availableProcessors", availableProcessors);
            deviceInfoJson.put("maxMemory", maxMemory);
            deviceInfoJson.put("totalMemory", totalMemory);
            deviceInfoJson.put("ipAddress", ipAddress);
            deviceInfoJson.put("externalIpAddress", externalIpAddress);
            deviceInfoJson.put("hwid", hwid);

            return deviceInfoJson.toString(4);
        } catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }
    }

    private static String getIpAddress() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();

                if (networkInterface.isUp() && !networkInterface.isLoopback()) {
                    Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                    while (inetAddresses.hasMoreElements()) {
                        InetAddress inetAddress = inetAddresses.nextElement();
                        if (inetAddress instanceof Inet4Address) {
                            return inetAddress.getHostAddress();
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "Unknown";
    }

    private static String getExternalIpAddress() {
        String ipAddress = "Unknown";
        try {
            URL url = new URL("http://api.ipify.org");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            ipAddress = reader.readLine();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ipAddress;
    }

    private static String getHardwareId() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                if (networkInterface != null && !networkInterface.isLoopback() && networkInterface.getHardwareAddress() != null) {
                    byte[] macBytes = networkInterface.getHardwareAddress();
                    StringBuilder macAddress = new StringBuilder();
                    for (byte b : macBytes) {
                        macAddress.append(String.format("%02X", b));
                    }
                    return macAddress.toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Unknown";
    }

    public static void saveLicenseKey(String licenseKey) {
        try {
            Files.write(Paths.get(SystemVariables.licensePath),
                    licenseKey.getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("Lisans anahtarı başarıyla kaydedildi.");
        } catch (IOException e) {
            System.err.println("Lisans anahtarı kaydedilirken hata: " + e.getMessage());
        }
    }

    public static String checkLicenseKey() {
        String userHome = System.getProperty("user.name");
        String os = System.getProperty("os.name").toLowerCase();
        String basePath;

        if (os.contains("win")) {
            basePath = "C:/Users/" + userHome + "/";
        } else {
            basePath = "/Users/" + userHome + "/";
        }

        String licensePath = basePath + "/OnderGrup/userData/license.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(licensePath))) {
            String licenseKey = null;

            String line;
            while ((line = reader.readLine()) != null) {
                licenseKey = line;
                break;
            }

            if (licenseKey != null && !licenseKey.isEmpty()) {
                //System.out.println("Lisans anahtarı: " + licenseKey);
                return licenseKey;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void licenseStatus(Label licenseLabel, boolean licenseStatus) {
        if(licenseStatus) {
            String licenseKey = Utils.checkLicenseKey();
            String displayKey = (licenseKey != null && licenseKey.length() > 10) ? licenseKey.substring(0, 10) : licenseKey;

            JSONObject jsonObj = new JSONObject(Main.license);
            JSONObject payload = jsonObj.getJSONObject("payload");

            String licenseOwner = payload.getString("licenseOwner");
            String licenseExpiry = payload.getString("licenseExpiry");
            String licenseDeviceCount = payload.getString("licenseDeviceCount");

            String displayText;

            if (licenseExpiry.equals("endless")) {
                displayText = "Lisans Sahibi : " + licenseOwner +
                        "\nLisans Bitiş : Sınırsız" +
                        "\nKalan Süre : Sınırsız" +
                        "\nCihaz Sayısı : " + licenseDeviceCount +
                        "\nLisans Anahtarı : " + displayKey + " ...";
            } else {
                String bitisTarihi = Utils.formatDateTime(licenseExpiry);
                String kalanSure = calculateTimeRemaining(licenseExpiry);

                displayText = "Lisans Sahibi : " + licenseOwner +
                        "\nLisans Bitiş : " + bitisTarihi +
                        "\nKalan Süre : " + kalanSure +
                        "\nCihaz Sayısı : " + licenseDeviceCount +
                        "\nLisans Anahtarı : " + displayKey + " ...";
            }

            licenseLabel.setText(displayText);
        } else {
            licenseLabel.setText("Lisans Durumu : Geçersiz Lisans\nLütfen giriş yaparak programı aktifleştirin.");
        }
    }
}