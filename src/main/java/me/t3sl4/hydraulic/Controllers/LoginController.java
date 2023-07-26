package me.t3sl4.hydraulic.Controllers;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import me.t3sl4.hydraulic.Launcher;
import me.t3sl4.hydraulic.Util.HTTP.HTTPUtil;
import me.t3sl4.hydraulic.Util.User;
import me.t3sl4.hydraulic.Util.Util;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;
import java.util.ResourceBundle;

import static me.t3sl4.hydraulic.Util.Util.BASE_URL;

public class LoginController implements Initializable {

    @FXML
    private Label lblErrors;

    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtPassword;

    @FXML
    private Button btnSignin;

    @FXML
    private Button btnSignup;
    private double x, y;

    public static User loggedInUser;

    @FXML
    public void girisYap(MouseEvent event) throws IOException {
        Image icon = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("icons/logo.png")));

        Stage stage = (Stage) btnSignin.getScene().getWindow();

        if(txtUsername.getText().isEmpty() || txtPassword.getText().isEmpty()) {
            lblErrors.setText("Şifre veya kullanıcı adı girmediniz !");
        } else {
            if (logIn(txtUsername.getText(), txtPassword.getText()).equals("Success")) {
                stage.close();

                String url = BASE_URL + "/api/profileInfo/:NameSurname";
                String jsonBody = "{\"Username\": \"" + txtUsername.getText() + "\"}";
                loggedInUser = new User(txtUsername.getText(), HTTPUtil.parseNameSurname(HTTPUtil.sendPostRequest(url, jsonBody), "NameSurname"));
                System.out.println();
                openMainScreen();
            } else {
                lblErrors.setText("Böyle bir kullanıcı bulunamadı !!");
            }
        }
    }

    @FXML
    public void kayitOl() throws IOException {
        Stage stage = (Stage) btnSignup.getScene().getWindow();

        stage.close();
        openRegisterScreen();
    }

    @FXML
    public void sifremiUnuttum() {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    private String logIn(String username, String password) throws IOException {
        URL url = new URL(BASE_URL + "/api/login");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String jsonInputString = "{ \"Username\": \"" + username + "\", \"Password\": \"" + password + "\" }";

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            return "Success";
        } else {
            return "Failure";
        }
    }

    @FXML
    public void openGithub() {
        Util.openURL("https://github.com/hidirektor");
    }

    @FXML
    public void openOnder() {
        Util.openURL("https://ondergrup.com");
    }

    @FXML
    public void ekraniKapat() {
        Stage stage = (Stage) btnSignin.getScene().getWindow();

        stage.close();
    }

    private void setLblError(Color color, String text) {
        lblErrors.setTextFill(color);
        lblErrors.setText(text);
        System.out.println(text);
    }

    private void openMainScreen() throws IOException {
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(Launcher.class.getResource("fxml/Home.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.initStyle(StageStyle.UNDECORATED);

        Image icon = new Image(Launcher.class.getResourceAsStream("icons/logo.png"));
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

    private void openRegisterScreen() throws IOException {
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(Launcher.class.getResource("fxml/Register.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.initStyle(StageStyle.UNDECORATED);

        Image icon = new Image(Launcher.class.getResourceAsStream("icons/logo.png"));
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