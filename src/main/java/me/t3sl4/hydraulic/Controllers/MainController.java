package me.t3sl4.hydraulic.Controllers;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import me.t3sl4.hydraulic.Launcher;
import me.t3sl4.hydraulic.Main;
import me.t3sl4.hydraulic.Util.Gen.Util;
import me.t3sl4.hydraulic.Util.HTTP.HTTPRequest;
import me.t3sl4.hydraulic.Util.HTTP.HTTPUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Objects;
import java.util.ResourceBundle;

import static me.t3sl4.hydraulic.Util.Gen.Util.BASE_URL;

public class MainController implements Initializable {

    @FXML
    private VBox pnItems = null;
    @FXML
    private Button btnOverview;

    @FXML
    private Button btnOrders;

    @FXML
    private Button btnCustomers;

    @FXML
    private Button btnMenus;

    @FXML
    private Button btnSignout;

    @FXML
    private Pane pnlCustomer;

    @FXML
    private Pane pnlOverview;

    @FXML
    private Pane pnlMenus;
    @FXML
    private Label kullaniciAdiIsimText;
    @FXML
    private ImageView kullaniciProfilFoto;

    @FXML
    private Circle profilePhotoCircle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userInfo();
        Node[] nodes = new Node[10];
        for (int i = 0; i < nodes.length; i++) {
            try {

                final int j = i;
                nodes[i] = FXMLLoader.load(Objects.requireNonNull(Launcher.class.getResource("fxml/Item.fxml")));

                nodes[i].setOnMouseEntered(event -> {
                    nodes[j].setStyle("-fx-background-color : #0A0E3F");
                });
                nodes[i].setOnMouseExited(event -> {
                    nodes[j].setStyle("-fx-background-color : #02030A");
                });
                pnItems.getChildren().add(nodes[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void programiKapat() {
        Stage stage = (Stage) btnSignout.getScene().getWindow();

        stage.close();
    }

    @FXML
    public void onderGrupSiteOpen() {
        Util.openURL("https://ondergrup.com");
    }

    public void handleClicks(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnCustomers) {
            pnlCustomer.setStyle("-fx-background-color : #02030A");
            pnlCustomer.toFront();
        }
        if (actionEvent.getSource() == btnMenus) {
            pnlMenus.setStyle("-fx-background-color : #02030A");
            pnlMenus.toFront();
            try {
                FXMLLoader loader = new FXMLLoader(Launcher.class.getResource("fxml/Parametre.fxml"));
                Pane parametrePane = loader.load();
                pnlMenus.getChildren().setAll(parametrePane);

                double paneWidth = pnlMenus.getWidth();
                double paneHeight = pnlMenus.getHeight();

                double parametreWidth = parametrePane.getPrefWidth();
                double parametreHeight = parametrePane.getPrefHeight();

                double centerX = (paneWidth - parametreWidth) / 2;
                double centerY = (paneHeight - parametreHeight) / 2;
                parametrePane.setLayoutX(centerX);
                parametrePane.setLayoutY(centerY);

                PopupController popupController = loader.getController();
                popupController.setValues(Util.dataManipulator.kampanaBoslukX, Util.dataManipulator.kampanaBoslukY,
                        Util.dataManipulator.valfBoslukX, Util.dataManipulator.valfBoslukYArka, Util.dataManipulator.valfBoslukYOn,
                        Util.dataManipulator.kilitliBlokAraBoslukX, Util.dataManipulator.tekHizAraBoslukX, Util.dataManipulator.ciftHizAraBoslukX,
                        Util.dataManipulator.kompanzasyonTekHizAraBoslukX, Util.dataManipulator.sogutmaAraBoslukX, Util.dataManipulator.sogutmaAraBoslukYkOn,
                        Util.dataManipulator.sogutmaAraBoslukYkArka, Util.dataManipulator.kilitMotorKampanaBosluk, Util.dataManipulator.kilitMotorMotorBoslukX,
                        Util.dataManipulator.kilitMotorBoslukYOn, Util.dataManipulator.kilitMotorBoslukYArka, Util.dataManipulator.kayipLitre);
                popupController.showValues();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (actionEvent.getSource() == btnOverview) {
            pnlOverview.setStyle("-fx-background-color : #02030A");
            pnlOverview.toFront();
        }
        if(actionEvent.getSource()==btnOrders) {
            pnlMenus.setStyle("-fx-background-color : #353a46");
            pnlMenus.toFront();
            try {
                FXMLLoader loader = new FXMLLoader(Launcher.class.getResource("fxml/Klasik.fxml"));
                Pane parametrePane = loader.load();
                pnlMenus.getChildren().setAll(parametrePane);

                double paneWidth = pnlMenus.getWidth();
                double paneHeight = pnlMenus.getHeight();

                double parametreWidth = parametrePane.getPrefWidth();
                double parametreHeight = parametrePane.getPrefHeight();

                double centerX = (paneWidth - parametreWidth) / 2;
                double centerY = (paneHeight - parametreHeight) / 2;
                parametrePane.setLayoutX(centerX-100);
                parametrePane.setLayoutY(centerY+20);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void userInfo() {
        updateUser("Role", 0);
        updateUser("Email", 1);
        updateUser("NameSurname", 2);
        updateUser("Phone", 3);
        updateUser("CompanyName", 4);
        updateUser("Created_At", 5);
    }

    public void updateUser(String requestVal, int section) {
        String profileInfoUrl = BASE_URL + "/api/profileInfo/:" + requestVal;
        String profileInfoBody = "{\"Username\": \"" + Main.loggedInUser.getUsername() + "\"}";

        HTTPRequest.sendRequest(profileInfoUrl, profileInfoBody, new HTTPRequest.RequestCallback() {
            @Override
            public void onSuccess(String profileInfoResponse) {
                String parsedVal = HTTPUtil.parseStringVal(profileInfoResponse, requestVal);
                if (section == 0) {
                    Main.loggedInUser.setRole(parsedVal);
                } else if (section == 1) {
                    Main.loggedInUser.setEmail(parsedVal);
                } else if (section == 2) {
                    Main.loggedInUser.setFullName(parsedVal);
                } else if (section == 3) {
                    Main.loggedInUser.setPhone(parsedVal);
                } else if (section == 4) {
                    Main.loggedInUser.setCompanyName(parsedVal);
                } else {
                    Main.loggedInUser.setCreatedAt(parsedVal);
                }

                kullaniciAdiIsimText.setText(Main.loggedInUser.getUsername() + "\n" + Main.loggedInUser.getFullName());
                downloadAndSetProfilePhoto(Main.loggedInUser.getUsername());
            }

            @Override
            public void onFailure() {
                System.out.println("Kullanıcı bilgileri alınamadı!");
            }
        });
    }

    private void downloadAndSetProfilePhoto(String username) {
        String localFilePath = "C:/Users/" + System.getProperty("user.name") + "/OnderGrup/profilePhoto/";
        String localFileFinalPath = localFilePath + username + ".jpg";

        File localFile = new File(localFileFinalPath);
        if (localFile.exists()) {
            setProfilePhoto(username);
        } else {
            String photoUrl = BASE_URL + "/api/fileSystem/downloadPhoto";
            String jsonBody = "{\"username\":\"" + username + "\"} ";

            HTTPRequest.sendRequest4File(photoUrl, jsonBody, localFileFinalPath, new HTTPRequest.RequestCallback() {
                @Override
                public void onSuccess(String response) {
                    setProfilePhoto(username);
                }

                @Override
                public void onFailure() {
                    System.out.println("Profil fotoğrafı indirilemedi.");
                }
            });
        }
    }

    private void setProfilePhoto(String username) {
        String photoPath = "C:\\Users\\" + System.getProperty("user.name") + "\\OnderGrup\\profilePhoto\\" + username + ".jpg";
        File photoFile = new File(photoPath);

        if (photoFile.exists()) {
            Image image = new Image(photoFile.toURI().toString());
            profilePhotoCircle.setFill(new ImagePattern(image));
            profilePhotoCircle.setEffect(new DropShadow(+25d, 0d, +2d, Color.valueOf("#05071F")));
            kullaniciProfilFoto.setImage(image);
            kullaniciProfilFoto.setVisible(false);
        }
    }
}