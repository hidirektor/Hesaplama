package me.t3sl4.hesaplama.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import me.t3sl4.hesaplama.Launcher;

import java.io.IOException;
import java.util.Objects;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 620);
        stage.setTitle("ÖnderLift -- Hidrolik Ünitesi Hesaplama Aracı");
        setUserAgentStylesheet(STYLESHEET_CASPIAN);
        stage.setScene(scene);

        stage.setResizable(false);

        Image icon = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/icons/logo.png")));
        stage.getIcons().add(icon);

        /*scene.setCursor(javafx.scene.Cursor.NONE);
        scene.setCursor(new ImageCursor(icon,
                icon.getWidth() / 6,
                icon.getHeight() / 6));*/

        stage.show();

        MainController controller = fxmlLoader.getController();
        controller.setHostServices(getHostServices());
    }

    public static void main(String[] args) {
        launch();
    }
}