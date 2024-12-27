package me.t3sl4.hydraulic.app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Screen;
import javafx.stage.Stage;
import me.t3sl4.hydraulic.utils.Utils;
import me.t3sl4.hydraulic.utils.database.File.FileUtil;
import me.t3sl4.hydraulic.utils.database.Model.Replay.ClassicData;
import me.t3sl4.hydraulic.utils.database.Model.Replay.PowerPackData;
import me.t3sl4.hydraulic.utils.general.SceneUtil;
import me.t3sl4.hydraulic.utils.general.SystemVariables;

import java.util.List;
import java.util.prefs.Preferences;

public class Main extends Application {
    List<Screen> screens = Screen.getScreens();

    public static Screen defaultScreen;

    public static String license;

    public static PowerPackData powerPackReplayData = new PowerPackData();
    public static ClassicData classicReplayData = new ClassicData();

    @lombok.SneakyThrows
    @Override
    public void start(Stage primaryStage) {
        Utils.prefs = Preferences.userRoot().node("onderGrupUpdater");
        String defaultMonitor = Utils.checkDefaultMonitor();
        FileUtil.criticalFileSystem();

        if(!System.getProperty("os.name").toLowerCase().contains("win")) {
            if (!Utils.checkSingleInstance()) {
                System.out.println("Program zaten çalışıyor. Odaklanıyor...");
                Utils.focusApp("Hydraulic Tool " + SystemVariables.getVersion());
                Platform.exit();
                return;
            }
        }

        Utils.checkVersionFromPrefs();

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

        Thread systemThread = new Thread(FileUtil::setupLocalData);
        systemThread.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
