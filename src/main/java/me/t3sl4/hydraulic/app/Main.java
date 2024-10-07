package me.t3sl4.hydraulic.app;

import javafx.application.Application;
import javafx.stage.Stage;
import me.t3sl4.hydraulic.utils.database.File.FileUtil;
import me.t3sl4.hydraulic.utils.general.SceneUtil;
import me.t3sl4.hydraulic.utils.service.UserDataService.User;

import java.io.IOException;

public class Main extends Application {
    private double x, y;
    public static User loggedInUser;

    @Override
    public void start(Stage primaryStage) throws IOException {
        SceneUtil.openMainScreen();

        FileUtil.setupFileSystemInBackground();
    }

    public static void main(String[] args) {
        launch(args);
    }
}