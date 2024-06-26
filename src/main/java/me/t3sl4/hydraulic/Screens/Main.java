package me.t3sl4.hydraulic.Screens;

import javafx.application.Application;
import javafx.stage.Stage;
import me.t3sl4.hydraulic.Utility.Data.User.User;
import me.t3sl4.hydraulic.Utility.File.SystemUtil;

public class Main extends Application {
    private double x, y;
    public static User loggedInUser;

    @Override
    public void start(Stage primaryStage) throws Exception {
        SceneUtil.openMainScreen();

        Thread systemThread = new Thread(SystemUtil::systemSetup);

        systemThread.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}