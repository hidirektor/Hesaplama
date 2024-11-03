package me.t3sl4.hydraulic.app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
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

        Task<Void> setupTask = new Task<>() {
            @Override
            protected Void call() {
                FileUtil.criticalFileSystem();
                return null;
            }

            @Override
            protected void succeeded() {
                openMainScreen(defaultMonitor);

                loadData();
            }

            @Override
            protected void failed() {
                System.err.println("Dosya sistemi kurulumu başarısız.");
            }
        };

        Thread setupThread = new Thread(setupTask);
        setupThread.setDaemon(true);
        setupThread.start();
    }

    private void openMainScreen(String defaultMonitor) {
        Platform.runLater(() -> {
            if (defaultMonitor == null) {
                if (screens.size() > 1) {
                    Utils.showMonitorSelectionScreen(screens, null, true);
                } else {
                    try {
                        SceneUtil.openMainScreen(screens.get(0));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else {
                if (screens.size() > 1) {
                    int monitorIndex = Integer.parseInt(defaultMonitor.split(" ")[1]) - 1;
                    defaultScreen = screens.get(monitorIndex);
                    try {
                        SceneUtil.openMainScreen(screens.get(monitorIndex));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    try {
                        SceneUtil.openMainScreen(screens.get(0));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    private void loadData() {
        Task<Void> dataLoadTask = new Task<>() {
            @Override
            protected Void call() {
                FileUtil.setupLocalData();
                return null;
            }

            @Override
            protected void succeeded() {
                System.out.println("Veriler başarıyla yüklendi.");
            }

            @Override
            protected void failed() {
                System.err.println("Veri yükleme başarısız.");
            }
        };

        Thread dataLoadThread = new Thread(dataLoadTask);
        dataLoadThread.setDaemon(true);
        dataLoadThread.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}