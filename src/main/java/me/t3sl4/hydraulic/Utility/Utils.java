package me.t3sl4.hydraulic.Utility;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import me.t3sl4.hydraulic.Launcher;
import me.t3sl4.hydraulic.Screens.Main;
import me.t3sl4.hydraulic.Screens.SceneUtil;
import me.t3sl4.hydraulic.Utility.Data.Tank.Tank;
import me.t3sl4.hydraulic.Utility.File.ExcelUtil;
import me.t3sl4.hydraulic.Utility.File.SystemUtil;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.UnaryOperator;
import java.util.logging.Logger;

public class Utils {


    private static final Logger logger = Logger.getLogger(Utils.class.getName());
    
    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static void showErrorMessage(String hataMesaji) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setTitle("Hata");
        alert.setHeaderText(null);
        alert.setContentText(hataMesaji);
        alert.showAndWait();
    }

    public static void showSuccessMessage(String basariMesaji) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setTitle("Başarılı !");
        alert.setHeaderText(null);
        alert.setContentText(basariMesaji);
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

    public static String getStockCodeFromDoubleHashMap(HashMap<String, HashMap<String, String>> inputHash, String searchKey) {
        String stockCode = null;
        HashMap<String, String> innerMap = new HashMap<>();

        for (Map.Entry<String, HashMap<String, String>> entry : inputHash.entrySet()) {
            String key = entry.getKey().trim();
            if(searchKey.trim().equals(key)) {
                innerMap = entry.getValue();

                stockCode = innerMap.get("B");
                break;
            }
        }

        return stockCode;
    }

    public static String getMaterialFromDoubleHashMap(HashMap<String, HashMap<String, String>> inputHash, String searchKey) {
        for (Map.Entry<String, HashMap<String, String>> entry : inputHash.entrySet()) {
            String key = entry.getKey().trim();
            if(searchKey.trim().equals(key)) {
                return key;
            }
        }
        return null;
    }

    public static String getAmountFromDoubleHashMap(HashMap<String, HashMap<String, String>> inputHash, String searchKey) {
        String stockCode = null;
        HashMap<String, String> innerMap;

        for (Map.Entry<String, HashMap<String, String>> entry : inputHash.entrySet()) {
            String key = entry.getKey().trim();
            if(searchKey.trim().equals(key)) {
                innerMap = entry.getValue();

                stockCode = innerMap.get("C");
                break;
            }
        }

        return stockCode;
    }

    public static String float2String(String inputStr) {
        float floatAdet = Float.parseFloat(inputStr);
        int tamSayi = (int) floatAdet;

        return String.valueOf(tamSayi);
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
        stage.close();
        SceneUtil.changeScreen("fxml/Home.fxml");
    }

    public static void openRegisterScreen() throws IOException {
        SceneUtil.changeScreen("fxml/Register.fxml");
    }

    public static void openResetPasswordScreen() throws IOException {
        SceneUtil.changeScreen("fxml/ResetPassword.fxml");
    }

    public static void offlineMod(Label lblErrors, Runnable onComplete) {
        Utils.showErrorOnLabel(lblErrors, "Standart kullanıcı olarak giriş yapılıyor !");

        Timeline timeline = new Timeline();
        timeline.setCycleCount(4);

        final int[] countdown = {3};
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

    public static Tank findTankByTankName(String tankName) {
        for (Tank tank : ExcelUtil.dataManipulator.inputTanks) {
            if (tank.getTankName().equals(tankName)) {
                return tank;
            }
        }
        return null;
    }

    public static Tank findTankByKabinName(String kabinName) {
        for (Tank tank : ExcelUtil.dataManipulator.inputTanks) {
            if (tank.getKabinName().equals(kabinName)) {
                return tank;
            }
        }
        return null;
    }

    public static void deleteLocalData() {
        deleteFile(Launcher.tokenPath);

        deleteDirectory(new File(Launcher.profilePhotoLocalPath));

        deleteDirectory(new File(Launcher.pdfFileLocalPath));

        deleteDirectory(new File(Launcher.excelFileLocalPath));

        deleteDirectory(new File(Launcher.dataFileLocalPath));

        Main.loggedInUser = null;
        Launcher.accessToken = null;
        Launcher.refreshToken = null;
        Launcher.userID = null;
        Launcher.userName = null;

        SystemUtil.firstLaunch();
        SystemUtil.systemSetup();
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