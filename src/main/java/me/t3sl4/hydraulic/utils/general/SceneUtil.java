package me.t3sl4.hydraulic.utils.general;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import me.t3sl4.hydraulic.Launcher;

import java.io.IOException;
import java.util.Objects;

public class SceneUtil {

    public static double x, y;

    public static void changeScreen(String newSceneSource, Screen currentScreen) throws IOException {
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(Launcher.class.getResource(newSceneSource)));

        primaryStage.setScene(new Scene(root));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("Hydraulic Tool " + SystemVariables.getVersion());

        Image icon = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/images/general/logo.png")));
        primaryStage.getIcons().add(icon);

        Rectangle2D bounds = currentScreen.getVisualBounds();

        primaryStage.setOnShown(event -> {
            double stageWidth = primaryStage.getWidth();
            double stageHeight = primaryStage.getHeight();

            double centerX = bounds.getMinX() + (bounds.getWidth() - stageWidth) / 2;
            double centerY = bounds.getMinY() + (bounds.getHeight() - stageHeight) / 2;

            primaryStage.setX(centerX);
            primaryStage.setY(centerY);
        });

        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {

            primaryStage.setX(event.getScreenX() - x);
            primaryStage.setY(event.getScreenY() - y);

        });
        primaryStage.show();
    }

    public static void openMainScreen(Screen currentScreen) throws IOException {
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(Launcher.class.getResource("fxml/Home.fxml")));
        primaryStage.setScene(new Scene(root));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("Hydraulic Tool " + SystemVariables.getVersion());

        Image icon = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/images/general/logo.png")));
        primaryStage.getIcons().add(icon);

        Rectangle2D bounds = currentScreen.getVisualBounds();
        primaryStage.setOnShown(event -> {
            double stageWidth = primaryStage.getWidth();
            double stageHeight = primaryStage.getHeight();

            double centerX = bounds.getMinX() + (bounds.getWidth() - stageWidth) / 2;
            double centerY = bounds.getMinY() + (bounds.getHeight() - stageHeight) / 2;

            primaryStage.setX(centerX);
            primaryStage.setY(centerY);
        });

        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {

            primaryStage.setX(event.getScreenX() - x);
            primaryStage.setY(event.getScreenY() - y);

        });
        primaryStage.show();
    }

    public static Screen getScreenOfNode(Node node) {
        Window window = node.getScene().getWindow();

        double windowX = window.getX();
        double windowY = window.getY();

        for (Screen screen : Screen.getScreens()) {
            Rectangle2D bounds = screen.getVisualBounds();
            if (bounds.contains(windowX, windowY)) {
                return screen;
            }
        }

        return Screen.getPrimary();
    }
}
