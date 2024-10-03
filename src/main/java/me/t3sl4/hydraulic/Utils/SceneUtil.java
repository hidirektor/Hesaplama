package me.t3sl4.hydraulic.Utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import me.t3sl4.hydraulic.Launcher;

import java.io.IOException;
import java.util.Objects;

public class SceneUtil {

    public static double x, y;

    public static void changeScreen(String newSceneSource) throws IOException {
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(Launcher.class.getResource(newSceneSource)));
        primaryStage.setScene(new Scene(root));
        primaryStage.initStyle(StageStyle.UNDECORATED);

        Image icon = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/images/general/logo.png")));
        primaryStage.getIcons().add(icon);

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

    public static void openMainScreen() throws IOException {
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(Launcher.class.getResource("fxml/Login.fxml")));
        primaryStage.setScene(new Scene(root));
        primaryStage.initStyle(StageStyle.UNDECORATED);

        Image icon = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/images/general/logo.png")));
        primaryStage.getIcons().add(icon);

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
}
