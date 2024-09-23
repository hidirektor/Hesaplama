package me.t3sl4.hydraulic.Screens;

import javafx.application.Application;
import javafx.stage.Stage;
import me.t3sl4.hydraulic.Utils.SceneUtil;
import me.t3sl4.hydraulic.Utils.UserDataService.User;
import me.t3sl4.hydraulic.Utils.File.GeneralFileSystem;

public class Main extends Application {
    private double x, y;
    public static User loggedInUser;

    @Override
    public void start(Stage primaryStage) throws Exception {
        SceneUtil.openMainScreen();

        Thread systemThread = new Thread(GeneralFileSystem::systemSetup);

        systemThread.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}