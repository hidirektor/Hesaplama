package me.t3sl4.hydraulic.app;

import javafx.application.Application;
import javafx.stage.Screen;
import javafx.stage.Stage;
import me.t3sl4.hydraulic.utils.Utils;
import me.t3sl4.hydraulic.utils.database.File.FileUtil;
import me.t3sl4.hydraulic.utils.general.SceneUtil;

import java.io.IOException;
import java.util.List;
import java.util.prefs.Preferences;

public class Main extends Application {
    List<Screen> screens = Screen.getScreens();

    public static Screen defaultScreen;

    @lombok.SneakyThrows
    @Override
    public void start(Stage primaryStage) {
        Utils.prefs = Preferences.userRoot().node(this.getClass().getName());
        String defaultMonitor = Utils.checkDefaultMonitor();

        defaultScreen = screens.get(0);

        if (defaultMonitor == null) {
            if (screens.size() > 1) {
                Utils.showMonitorSelectionScreen(screens, null, true);
            } else {
                SceneUtil.openMainScreen(screens.get(0));
            }
        } else {
            if(screens.size() > 1) {
                int monitorIndex = Integer.parseInt(defaultMonitor.split(" ")[1]) - 1;
                defaultScreen = screens.get(monitorIndex);
                SceneUtil.openMainScreen(screens.get(monitorIndex));
            } else {
                SceneUtil.openMainScreen(screens.get(0));
            }
        }

        createLicenseFile();

        Thread systemThread = new Thread(FileUtil::setupFileSystem);
        systemThread.start();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void createLicenseFile() throws IOException {
        String userHome = System.getProperty("user.name");
        String os = System.getProperty("os.name").toLowerCase();
        String basePath;

        if (os.contains("win")) {
            basePath = "C:/Users/" + userHome + "/";
        } else {
            basePath = "/Users/" + userHome + "/";
        }

        String licensePath = basePath + "/OnderGrup/userData/license.txt";
        FileUtil.createFile(licensePath);
    }
}
