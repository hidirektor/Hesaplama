package me.t3sl4.hydraulic.Screens;

import javafx.application.Application;
import javafx.stage.Stage;
import me.t3sl4.hydraulic.Launcher;
import me.t3sl4.hydraulic.Utility.DataUtil.User.User;
import me.t3sl4.hydraulic.Utility.FileUtil.ExcelUtil;
import me.t3sl4.hydraulic.Utility.Util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Main extends Application {
    private double x, y;
    public static User loggedInUser;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Util.filePath();
        fileCopy();

        ExcelUtil.excelDataRead();

        SceneUtil.openMainScreen();
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