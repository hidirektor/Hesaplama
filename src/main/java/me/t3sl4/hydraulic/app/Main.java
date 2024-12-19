package me.t3sl4.hydraulic.app;

import javafx.application.Application;
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
        Utils.prefs = Preferences.userRoot().node(this.getClass().getName());
        String defaultMonitor = Utils.checkDefaultMonitor();
        FileUtil.criticalFileSystem();

        checkVersionFromPrefs();

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

    private void checkVersionFromPrefs() {
        Utils.prefs = Preferences.userRoot().node(this.getClass().getName());

        String versionKey = "onderGrup_hydraulic_versionNumber";
        String currentVersion = SystemVariables.CURRENT_VERSION;
        String savedVersion = Utils.prefs.get(versionKey, null);

        if (savedVersion == null || !savedVersion.equals(currentVersion)) {
            Utils.prefs.put(versionKey, currentVersion);
            System.out.println("Version updated in preferences: " + currentVersion);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
