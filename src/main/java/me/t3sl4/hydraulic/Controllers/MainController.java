package me.t3sl4.hydraulic.Controllers;

import com.fasterxml.jackson.databind.DeserializationFeature;
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
import javafx.scene.layout.HBox;
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
import me.t3sl4.hydraulic.Util.HydraulicInfo;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;

import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.t3sl4.hydraulic.Util.Data.Profile;
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
    private Button btnProfil;

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

    @FXML
    private Label toplamSiparisCount;
    @FXML
    private Label klasikUniteCount;
    @FXML
    private Label hidrosUntiteCount;
    @FXML
    private Label parametreCount;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userInfo();
        hidrolikUnitStats();
        hydraulicUnitInit();
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
                        Util.dataManipulator.kilitMotorBoslukYOn, Util.dataManipulator.kilitMotorBoslukYArka, Util.dataManipulator.kayipLitre, Util.dataManipulator.kilitPlatformMotorBosluk, Util.dataManipulator.valfXBoslukSogutma);
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

    @FXML
    public void profileEditScreen() {
        pnlMenus.setStyle("-fx-background-color : #353a46");
        pnlMenus.toFront();
        try {
            FXMLLoader loader = new FXMLLoader(Launcher.class.getResource("fxml/ProfileEdit.fxml"));
            Pane profilPane = loader.load();
            pnlMenus.getChildren().setAll(profilPane);

            double paneWidth = pnlMenus.getWidth();
            double paneHeight = pnlMenus.getHeight();

            double parametreWidth = profilPane.getPrefWidth();
            double parametreHeight = profilPane.getPrefHeight();

            double centerX = (paneWidth - parametreWidth) / 2;
            double centerY = (paneHeight - parametreHeight) / 2;
            profilPane.setLayoutX(centerX);
            profilPane.setLayoutY(centerY+20);
        } catch(IOException e) {
            e.printStackTrace();
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

    public void hidrolikUnitStats() {
        String profileInfoUrl = BASE_URL + "/api/getStatistics";

        HTTPRequest.sendRequestNormal(profileInfoUrl, new HTTPRequest.RequestCallback() {
            @Override
            public void onSuccess(String hydraulicResponse) {
                JSONObject responseJson = new JSONObject(hydraulicResponse);
                int siparisSayisi = responseJson.getInt("Sipariş Sayısı");
                int klasik = responseJson.getInt("Klasik");
                int hidros = responseJson.getInt("Hidros");

                toplamSiparisCount.setText(String.valueOf(siparisSayisi));
                klasikUniteCount.setText(String.valueOf(klasik));
                hidrosUntiteCount.setText(String.valueOf(hidros));
            }

            @Override
            public void onFailure() {
                System.out.println("Kullanıcı bilgileri alınamadı!");
            }
        });

        excelVoidCount();
    }

    public void hydraulicUnitInit() {
        HTTPRequest.sendRequestNormal(BASE_URL + "/api/getHydraulicInfo", new HTTPRequest.RequestCallback() {
            @Override
            public void onSuccess(String response) {
                List<HydraulicInfo> hydraulicInfos = parseJsonResponse(response);

                for (HydraulicInfo info : hydraulicInfos) {
                    try {
                        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(Launcher.class.getResource("fxml/Item.fxml")));
                        Node node = loader.load();

                        // Bind UI components to the loaded FXML
                        HBox itemC = (HBox) loader.getNamespace().get("itemC");
                        Label orderNumberLabel = (Label) loader.getNamespace().get("orderNumberLabel");
                        Label orderDateLabel = (Label) loader.getNamespace().get("orderDateLabel");
                        Label typeLabel = (Label) loader.getNamespace().get("typeLabel");
                        Label InChargeLabel = (Label) loader.getNamespace().get("InChargeLabel");
                        Button pdfViewButton = (Button) loader.getNamespace().get("pdfViewButton");

                        // Set data from HydraulicInfo object
                        orderNumberLabel.setText(info.getSiparisNumarasi());
                        orderDateLabel.setText(formatDateTime(info.getSiparisTarihi()));
                        typeLabel.setText(info.getUniteTipi());
                        InChargeLabel.setText(info.getCreatedBy());

                        // Open PDF file in browser on button click
                        pdfViewButton.setOnAction(event -> {
                            try {
                                Desktop.getDesktop().browse(new URI(info.getPdfFile()));
                            } catch (IOException | URISyntaxException e) {
                                e.printStackTrace();
                            }
                        });

                        // Mouse hover effects
                        final int j = pnItems.getChildren().size();
                        node.setOnMouseEntered(event -> {
                            itemC.setStyle("-fx-background-color : #0A0E3F");
                        });
                        node.setOnMouseExited(event -> {
                            itemC.setStyle("-fx-background-color : #02030A");
                        });

                        pnItems.getChildren().add(node);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure() {
                // Handle failure case
                System.out.println("API request failed.");
            }
        });
    }

    public String formatDateTime(String dateTimeString) {
        try {
            OffsetDateTime dateTime = OffsetDateTime.parse(dateTimeString);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            return dateTime.format(formatter);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public List<HydraulicInfo> parseJsonResponse(String response) {
        List<HydraulicInfo> hydraulicInfos = new ArrayList<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            HydraulicInfo[] infoArray = objectMapper.readValue(response, HydraulicInfo[].class);
            hydraulicInfos.addAll(Arrays.asList(infoArray));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return hydraulicInfos;
    }

    public int getHydraulicUnitCount() {
        String profileInfoUrl = BASE_URL + "/api/getStatistics";
        final int[] totalCount = {0};

        HTTPRequest.sendRequestNormal(profileInfoUrl, new HTTPRequest.RequestCallback() {
            @Override
            public void onSuccess(String hydraulicResponse) {
                JSONObject responseJson = new JSONObject(hydraulicResponse);
                totalCount[0] = responseJson.getInt("Sipariş Sayısı");
                int klasik = responseJson.getInt("Klasik");
                int hidros = responseJson.getInt("Hidros");
            }

            @Override
            public void onFailure() {
                System.out.println("Kullanıcı bilgileri alınamadı!");
            }
        });

        return totalCount[0];
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
                Profile.downloadAndSetProfilePhoto(Main.loggedInUser.getUsername(), profilePhotoCircle, kullaniciProfilFoto);
            }

            @Override
            public void onFailure() {
                System.out.println("Kullanıcı bilgileri alınamadı!");
            }
        });
    }

    private void excelVoidCount() {
        String excelPath = "/data/Hidrolik.xlsx";
        String sheetName = "Boşluk Değerleri";

        try (InputStream file = Launcher.class.getResourceAsStream(excelPath)) {
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheet(sheetName);

            if (sheet != null) {
                int rowCount = sheet.getPhysicalNumberOfRows();
                int maxColumnCount = 0;

                for (int i = 0; i < rowCount; i++) {
                    Row row = sheet.getRow(i);
                    if (row != null) {
                        int columnCount = row.getPhysicalNumberOfCells();
                        if (columnCount > maxColumnCount) {
                            maxColumnCount = columnCount;
                        }
                    }
                }

                System.out.println("Dolu sütun sayısı: " + maxColumnCount);
                parametreCount.setText(String.valueOf(maxColumnCount));
            } else {
                System.out.println("Sayfa bulunamadı: " + sheetName);
            }

            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}