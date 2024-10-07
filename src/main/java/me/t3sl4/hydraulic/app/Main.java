package me.t3sl4.hydraulic.app;

import javafx.application.Application;
import javafx.stage.Stage;
import me.t3sl4.hydraulic.utils.database.File.FileUtil;
import me.t3sl4.hydraulic.utils.general.SceneUtil;
import me.t3sl4.hydraulic.utils.service.UserDataService.User;

public class Main extends Application {
    private double x, y;
    public static User loggedInUser;

    @lombok.SneakyThrows
    @Override
    public void start(Stage primaryStage) {
        SceneUtil.openMainScreen();

        Thread systemThread = new Thread(FileUtil::setupFileSystem);

        systemThread.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}