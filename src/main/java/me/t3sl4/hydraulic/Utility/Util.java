package me.t3sl4.hydraulic.Utility;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import me.t3sl4.hydraulic.Launcher;

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

public class Util {


    private static final Logger logger = Logger.getLogger(Util.class.getName());
    
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

    public static void filePath() {
        createDirectory(Launcher.profilePhotoLocalPath);
        createDirectory(Launcher.pdfFileLocalPath);
        createDirectory(Launcher.excelFileLocalPath);
        createDirectory(Launcher.dataFileLocalPath);
        createDirectory(Launcher.loginFilePath);
    }

    private static void createDirectory(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static void openURL(String url) {
        try {
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
        } catch (java.io.IOException e) {
            System.out.println(e.getMessage());
        }
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

    public static boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    public static String getOperatingSystem() {
        return System.getProperty("os.name");
    }

    public static void createMainDirectory() {
        File mainDir = new File(Launcher.mainPath);
        if (!mainDir.exists()) {
            if (mainDir.mkdirs()) {
                System.out.println("Main directory created: " + Launcher.mainPath);
            } else {
                System.err.println("Failed to create main directory: " + Launcher.mainPath);
                System.err.println("Absolute path: " + mainDir.getAbsolutePath());
                System.err.println("Writable: " + mainDir.canWrite());
                System.err.println("Readable: " + mainDir.canRead());
                System.err.println("Executable: " + mainDir.canExecute());
            }
        }
    }

    public static void changeDataStoragePath() {
        if (getOperatingSystem().contains("Windows")) {
            Launcher.mainPath = "C:/Users/" + System.getProperty("user.name") + "/OnderGrup/";
        } else {
            Launcher.mainPath = "/Users/" + System.getProperty("user.name") + "/OnderGrup/";
        }

        Launcher.profilePhotoLocalPath = Launcher.mainPath + "profilePhoto/";
        Launcher.pdfFileLocalPath = Launcher.mainPath + "hydraulicUnits/";
        Launcher.excelFileLocalPath = Launcher.mainPath + "partList/";
        Launcher.dataFileLocalPath = Launcher.mainPath + "data/";
        Launcher.loginFilePath = Launcher.mainPath + "login/loginInfo.txt";
        Launcher.excelDBPath = Launcher.mainPath + "data/Hidrolik.xlsx";
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
        HashMap<String, String> innerMap = new HashMap<>();

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

    public static boolean checkUpperCase(String controlText) {
        for(int i=0; i<controlText.length(); i++) {
            char controlChar = controlText.charAt(i);
            if(Character.isUpperCase(controlChar)) {
                return true;
            }
        }
        return false;
    }

    public static double string2Double(String inputVal) {
        String[] secPmp = inputVal.split(" cc");

        return Double.parseDouble(secPmp[0]);
    }
}