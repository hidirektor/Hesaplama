package me.t3sl4.hydraulic.app;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Screen;
import javafx.stage.Stage;
import me.t3sl4.hydraulic.utils.Utils;
import me.t3sl4.hydraulic.utils.Version.UpdateChecker;
import me.t3sl4.hydraulic.utils.database.File.FileUtil;
import me.t3sl4.hydraulic.utils.database.Model.Replay.ClassicData;
import me.t3sl4.hydraulic.utils.database.Model.Replay.PowerPackData;
import me.t3sl4.hydraulic.utils.general.SceneUtil;
import me.t3sl4.hydraulic.utils.general.SystemVariables;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
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

        if (!checkSingleInstance()) {
            System.out.println("Program zaten çalışıyor. Odaklanıyor...");
            focusApp("Hydraulic Tool " + SystemVariables.getVersion());
            Platform.exit();
            return;
        }

        UpdateChecker updateChecker = new UpdateChecker();
        updateChecker.start();

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
        Utils.prefs = Preferences.userRoot().node("onderGrupUpdater");

        String launcherVersionKey = "launcher_version";
        String hydraulicVersionKey = "hydraulic_version";

        String currentVersion = SystemVariables.CURRENT_VERSION;

        String savedLauncherVersion = Utils.prefs.get(launcherVersionKey, null);
        String savedHydraulicVersion = Utils.prefs.get(hydraulicVersionKey, "unknown");

        if (savedHydraulicVersion == null || !savedHydraulicVersion.equals(currentVersion)) {
            Utils.prefs.put(hydraulicVersionKey, currentVersion);
            savedHydraulicVersion = Utils.prefs.get(hydraulicVersionKey, "unknown");
        }

        // HydraulicTool sürümünü logla
        System.out.println("HydraulicTool sürümü: " + savedHydraulicVersion);
    }

    private static boolean checkSingleInstance() {
        String pid = java.lang.management.ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
        File lockFile = new File(System.getProperty("user.home"), ".onder_grup_hydraulic.pid");

        try {
            if (lockFile.exists()) {
                List<String> lines = Files.readAllLines(lockFile.toPath());
                if (!lines.isEmpty()) {
                    String existingPid = lines.get(0);
                    if (isProcessRunning(existingPid)) {
                        return false;
                    }
                }
            }

            Files.write(lockFile.toPath(), pid.getBytes());
            lockFile.deleteOnExit();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static boolean isProcessRunning(String pid) {
        try {
            Process process = Runtime.getRuntime().exec("tasklist");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(pid)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void focusApp(String windowTitle) {
        Platform.runLater(() -> {
            User32 user32 = User32.INSTANCE;
            WinDef.HWND hwnd = user32.FindWindow(null, windowTitle);
            if (hwnd != null) {
                user32.ShowWindow(hwnd, WinUser.SW_RESTORE);
                user32.SetForegroundWindow(hwnd);
            } else {
                System.out.println("Pencere bulunamadı.");
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
