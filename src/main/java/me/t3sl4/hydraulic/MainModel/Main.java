package me.t3sl4.hydraulic.MainModel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import me.t3sl4.hydraulic.Launcher;
import me.t3sl4.hydraulic.Util.Util;
import me.t3sl4.hydraulic.Util.Data.User.User;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public class Main extends Application {
    private double x, y;
    public static User loggedInUser;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Util.filePath();
        fileCopy();

        Util.excelDataRead();

        Parent root = FXMLLoader.load(Objects.requireNonNull(Launcher.class.getResource("fxml/Login.fxml")));
        primaryStage.setScene(new Scene(root));
        primaryStage.initStyle(StageStyle.UNDECORATED);

        Image icon = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("icons/logo.png")));
        primaryStage.getIcons().add(icon);

        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {

            primaryStage.setX(event.getScreenX() - x);
            primaryStage.setY(event.getScreenY() - y);

        });
        primaryStage.show();
    }

    public void fileCopy() {
        Path targetPath = Paths.get(Launcher.excelDBPath);
        System.out.println("Target path: " + targetPath.toString());

        try (InputStream inputStream = getClass().getResourceAsStream("/data/Hidrolik.xlsx")) {
            if (inputStream != null) {
                Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Hidrolik.xlsx kopyalandı: " + targetPath);
            } else {
                System.err.println("Hidrolik.xlsx dosyası bulunamadı.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}