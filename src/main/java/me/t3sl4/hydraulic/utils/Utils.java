package me.t3sl4.hydraulic.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
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
import me.t3sl4.hydraulic.controllers.Calculation.Classic.ClassicController;
import me.t3sl4.hydraulic.controllers.Calculation.PowerPack.PowerPackController;
import me.t3sl4.hydraulic.controllers.MainController;
import me.t3sl4.hydraulic.controllers.Popup.CylinderController;
import me.t3sl4.hydraulic.controllers.Popup.UpdateAlertController;
import me.t3sl4.hydraulic.utils.database.File.FileUtility;
import me.t3sl4.hydraulic.utils.database.Model.Kabin.Kabin;
import me.t3sl4.hydraulic.utils.database.Model.Table.HydraulicUnitList.HydraulicInfo;
import me.t3sl4.hydraulic.utils.general.SceneUtil;
import me.t3sl4.hydraulic.utils.general.SystemVariables;
import me.t3sl4.hydraulic.utils.service.UserDataService.User;
import me.t3sl4.util.file.DirectoryUtil;
import me.t3sl4.util.file.FileUtil;
import me.t3sl4.util.os.OSUtil;
import me.t3sl4.util.version.VersionUtil;
import me.t3sl4.util.version.model.ReleaseDetail;
import org.json.JSONObject;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
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
import java.util.zip.GZIPOutputStream;

public class Utils {
    public static final Logger logger = Logger.getLogger(MainController.class.getName());

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

    public static void showPopup(Screen currentScreen, String fxmlPath, String title, Modality popupModality, StageStyle popupStyle) {
        Image icon = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/images/general/logo.png")));

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

            popupStage.initModality(popupModality);
            if(popupStyle != null) {
                popupStage.initStyle(popupStyle);
            }
            popupStage.setTitle(title);
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
        Image icon = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/images/general/logo.png")));

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
            OSUtil.updatePrefData(SystemVariables.PREF_NODE_NAME, SystemVariables.DISPLAY_PREF_KEY, selectedMonitor);

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
        selectionStage.setTitle("Hydraulic Tool || Monitör Seçimi");
        selectionStage.getIcons().add(icon);
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
        FileUtil.deleteFile(SystemVariables.tokenPath);

        DirectoryUtil.deleteDirectory(SystemVariables.dataFileLocalPath);

        SystemVariables.loggedInUser = null;

        FileUtility.criticalFileSystem();
        Thread systemThread = new Thread(FileUtility::setupLocalData);
        systemThread.start();
    }

    public static boolean checkUpdateAndCancelEvent(Stage currentStage) {
        String currentVersion = SystemVariables.getVersion();
        String latestVersion = VersionUtil.getLatestVersion(SystemVariables.REPO_OWNER, SystemVariables.HYDRAULIC_REPO_NAME);

        if(currentVersion.equals(latestVersion)) {
            return false;
        } else {
            ReleaseDetail versionDetail = VersionUtil.getReleaseDetail(SystemVariables.REPO_OWNER, SystemVariables.HYDRAULIC_REPO_NAME, latestVersion);

            if (versionDetail != null) {
                showUpdateAlert(currentStage, versionDetail.getTitle(), versionDetail.getDescription());
            }
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
                                           String pdfPath, String excelPath, String isOffline, String createdBy, JSONObject unitParameters) {
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

            if (unitParameters != null) {
                newEntry.put("unit_parameters", compress(unitParameters.toString()));
            }

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
            hydraulicInfo.setUnitParameters((String) unitData.get("unit_parameters"));
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

    // JSON sıkıştırma
    public static String compress(String jsonString) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream)) {
            gzipOutputStream.write(jsonString.getBytes());
            gzipOutputStream.close();
            return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("JSON sıkıştırma sırasında bir hata oluştu", e);
        }
    }

    // JSON açma
    public static String decompress(String compressedString) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            byte[] compressedBytes = Base64.getDecoder().decode(compressedString);
            try (java.util.zip.GZIPInputStream gzipInputStream = new java.util.zip.GZIPInputStream(new java.io.ByteArrayInputStream(compressedBytes))) {
                byte[] buffer = new byte[1024];
                int len;
                while ((len = gzipInputStream.read(buffer)) > 0) {
                    byteArrayOutputStream.write(buffer, 0, len);
                }
                return byteArrayOutputStream.toString();
            }
        } catch (IOException e) {
            throw new RuntimeException("JSON açma sırasında bir hata oluştu", e);
        }
    }

    public static String getValidString(JSONObject jsonObject, String key) {
        if (jsonObject.has(key)) {
            String value = jsonObject.getString(key);
            return value.equals("null") ? null : value;
        }
        return null;
    }

    public static String getCurrentUnixTime() {
        long unixTime = Instant.now().getEpochSecond();
        return String.valueOf(unixTime);
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
    
    public static void clearOldCalculationData(String unitType) {
        if(unitType.equals("hidros")) {
            PowerPackController.secilenUniteTipi = "PowerPack";
            PowerPackController.girilenSiparisNumarasi = null;
            PowerPackController.secilenMotorTipi = null;
            PowerPackController.uniteTipiDurumu = null;
            PowerPackController.secilenMotorGucu = null;
            PowerPackController.secilenPompa = null;
            PowerPackController.secilenTankTipi = null;
            PowerPackController.secilenTankKapasitesi = null;
            PowerPackController.secilenOzelTankGenislik = null;
            PowerPackController.secilenOzelTankDerinlik = null;
            PowerPackController.secilenOzelTankYukseklik = null;
            PowerPackController.secilenPlatformTipi = null;
            PowerPackController.secilenBirinciValf = null;
            PowerPackController.secilenInisTipi = null;
            PowerPackController.secilenIkinciValf = null;
            PowerPackController.hesaplamaBitti = false;
        } else if(unitType.equals("klasik")) {
            ClassicController.secilenUniteTipi = "Klasik";
            ClassicController.girilenSiparisNumarasi = null;
            ClassicController.secilenMotor = null;
            ClassicController.secilenSogutmaDurumu = null;
            ClassicController.secilenHidrolikKilitDurumu = null;
            ClassicController.secilenPompa = null;
            ClassicController.girilenTankKapasitesiMiktari = 0;
            ClassicController.kompanzasyonDurumu = null;
            ClassicController.secilenValfTipi = null;
            ClassicController.secilenKilitMotor = null;
            ClassicController.secilenKilitPompa = null;
            ClassicController.hesaplamaBitti = false;
        }
    }

    public static void selectReplayedComboItem(ComboBox<String> currentComboBox, String currentData) {
        for (String item : currentComboBox.getItems()) {
            if (item.equals(currentData)) {
                currentComboBox.getSelectionModel().select(item);
                break;
            }
        }
        currentComboBox.setDisable(false);
    }

    public static void collapsableListener(
            AnchorPane menuPane,
            AnchorPane firstItem, AnchorPane secondItem, AnchorPane thirthItem, AnchorPane fourthItem,
            TitledPane firstPane, TitledPane secondPane, TitledPane thirthPane, TitledPane fourthPane) {

        // Varsayılan boyutlar
        int defaultMenuNameSize = 30; // TitledPane başlığı için
        int defaultItemSize = 40;    // Her bir öğenin boyutu
        int[] itemCounts = {3, 2, 4, 3}; // Her TitledPane'in altındaki öğe sayısı

        ChangeListener<Boolean> titledPaneListener = (observable, oldValue, newValue) -> {
            double newHeight = defaultMenuNameSize;
            double currentY = defaultMenuNameSize;

            TitledPane[] panes = {firstPane, secondPane, thirthPane, fourthPane};
            AnchorPane[] items = {firstItem, secondItem, thirthItem, fourthItem};

            for (int i = 0; i < panes.length; i++) {
                if (panes[i].isExpanded()) {
                    newHeight += defaultItemSize * itemCounts[i];
                }
                newHeight += defaultMenuNameSize; // Başlık boyutunu ekle

                if (i > 0) {
                    currentY += panes[i - 1].isExpanded()
                            ? (itemCounts[i - 1] * defaultItemSize + defaultMenuNameSize)
                            : defaultMenuNameSize;
                }
                items[i].setLayoutY(currentY);
            }

            menuPane.setPrefHeight(newHeight);
        };

        firstPane.expandedProperty().addListener(titledPaneListener);
        secondPane.expandedProperty().addListener(titledPaneListener);
        thirthPane.expandedProperty().addListener(titledPaneListener);
        fourthPane.expandedProperty().addListener(titledPaneListener);
    }

    public static void systemShutdown() {
        if(!System.getProperty("os.name").toLowerCase().contains("win")) {
            try {
                Path lockFilePath = Path.of(System.getProperty("user.home"), ".onder_grup_hydraulic.pid");
                Files.deleteIfExists(lockFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Platform.exit();
        System.exit(0);
    }

    public static boolean checkSingleInstance() {
        String pid = java.lang.management.ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
        File lockFile = new File(System.getProperty("user.home"), ".onder_grup_hydraulic.pid");

        try {
            if (lockFile.exists()) {
                List<String> lines = Files.readAllLines(lockFile.toPath());
                if (!lines.isEmpty()) {
                    String existingPid = lines.get(0);
                    if (isProcessRunning(existingPid)) {
                        return false;
                    }
                }
            }

            Files.write(lockFile.toPath(), pid.getBytes());
            lockFile.deleteOnExit();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean isProcessRunning(String pid) {
        try {
            Process process = Runtime.getRuntime().exec("tasklist");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(pid)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void focusApp(String windowTitle) {
        Platform.runLater(() -> {
            User32 user32 = User32.INSTANCE;
            WinDef.HWND hwnd = user32.FindWindow(null, windowTitle);
            if (hwnd != null) {
                user32.ShowWindow(hwnd, WinUser.SW_RESTORE);
                user32.SetForegroundWindow(hwnd);
            } else {
                System.out.println("Pencere bulunamadı.");
            }
        });
    }
}