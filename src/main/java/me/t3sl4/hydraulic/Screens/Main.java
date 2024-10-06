package me.t3sl4.hydraulic.Screens;

import javafx.application.Application;
import javafx.stage.Stage;
import me.t3sl4.hydraulic.Utils.Database.File.FileUtil;
import me.t3sl4.hydraulic.Utils.General.SceneUtil;
import me.t3sl4.hydraulic.Utils.System.UserDataService.User;

public class Main extends Application {
    private double x, y;
    public static User loggedInUser;

    @lombok.SneakyThrows
    @Override
    public void start(Stage primaryStage) {
        SceneUtil.openMainScreen();

        FileUtil.setupFileSystemInBackground();
    }

    public static void main(String[] args) {
        launch(args);
    }
}