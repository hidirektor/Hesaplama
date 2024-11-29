package me.t3sl4.hydraulic.controllers;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import me.t3sl4.hydraulic.Launcher;
import me.t3sl4.hydraulic.app.Main;
import me.t3sl4.hydraulic.controllers.Calculation.Classic.ClassicController;
import me.t3sl4.hydraulic.controllers.Calculation.PowerPack.PowerPackController;
import me.t3sl4.hydraulic.controllers.Popup.PopupController;
import me.t3sl4.hydraulic.utils.Utils;
import me.t3sl4.hydraulic.utils.component.FilterSwitch;
import me.t3sl4.hydraulic.utils.component.ThreeStateSwitch;
import me.t3sl4.hydraulic.utils.database.Model.Table.HydraulicUnitList.HydraulicInfo;
import me.t3sl4.hydraulic.utils.general.SceneUtil;
import me.t3sl4.hydraulic.utils.general.SystemVariables;
import me.t3sl4.hydraulic.utils.service.HTTP.HTTPMethod;
import me.t3sl4.hydraulic.utils.service.HTTP.Request.HydraulicUnit.HydraulicService;
import me.t3sl4.hydraulic.utils.service.HTTP.Request.License.LicenseService;
import me.t3sl4.hydraulic.utils.service.UserDataService.Profile;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;

import static me.t3sl4.hydraulic.utils.Utils.openURL;
import static me.t3sl4.hydraulic.utils.general.SystemVariables.*;

public class MainController implements Initializable {

    @FXML
    public VBox buttonsVBox;

    @FXML
    public ImageView leftSubLogo;

    @FXML
    private VBox pnItems = null;
    @FXML
    private Button btnHome;

    @FXML
    private Button btnOnlineMode;

    @FXML
    private Button btnMonitorAdapter;

    @FXML
    private Button btnKlasik;

    @FXML
    private Button btnHidros;

    @FXML
    public Button btnProfil;

    @FXML
    public Button btnSignout;

    @FXML
    public Button btnCloseProgram;

    @FXML
    private Button btnReportBug;

    @FXML
    private Button btnDebugMode;

    @FXML
    private Button btnParametreler;

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

    @FXML
    public Label versionCode;

    @FXML
    private TextField unitSearchBar;

    private List<HydraulicInfo> finalHydraulicUnitList = new ArrayList<>();

    int siparisSayisi;
    int klasik;
    int hidros;

    private final BooleanProperty isKlasik = new SimpleBooleanProperty();
    private final BooleanProperty isHidros = new SimpleBooleanProperty();
    private FilterSwitch klasikSwitch;
    private FilterSwitch hidrosSwitch;
    @FXML
    private VBox klasikSwitchVBox;
    @FXML
    private VBox hidrosSwitchVBox;

    private ThreeStateSwitch threeStateSwitch = new ThreeStateSwitch();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userInfo();
        initializeSwitchs();
        initializeThreeState();
        versionCode.setText(SystemVariables.getVersion());

        initializeHydraulicTable();

        Platform.runLater(() -> checkLicense());
    }

    private void initializeSwitchs() {
        klasikSwitch = new FilterSwitch();
        hidrosSwitch = new FilterSwitch();

        klasikSwitchVBox.getChildren().addAll(klasikSwitch);
        hidrosSwitchVBox.getChildren().addAll(hidrosSwitch);

        klasikSwitch.isToggledProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue) {
                if(hidrosSwitch.isIsToggled()) {
                    hidrosSwitch.setIsToggled(false);
                }
            }
            initializeHydraulicTable();
        });

        hidrosSwitch.isToggledProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue) {
                if(klasikSwitch.isIsToggled()) {
                    klasikSwitch.setIsToggled(false);
                }
            }
            initializeHydraulicTable();
        });

        klasikSwitch.isToggledProperty().bindBidirectional(isKlasik);
        hidrosSwitch.isToggledProperty().bindBidirectional(isHidros);
    }

    private void initializeThreeState() {
        threeStateSwitch.setLayoutX(770.0);
        threeStateSwitch.setLayoutY(110.0);

        threeStateSwitch.setDefaultState(ThreeStateSwitch.SwitchState.LOCAL);

        threeStateSwitch.setStateChangeListener(() -> {
            initializeHydraulicTable();
        });

        pnlOverview.getChildren().add(threeStateSwitch);
    }

    private void initializeHydraulicTable() {
        finalHydraulicUnitList.clear();
        pnItems.getChildren().clear();

        if(loggedInUser != null) {
            if(threeStateSwitch.getState().equals(ThreeStateSwitch.SwitchState.LOCAL)) {
                if(isKlasik.getValue()) {
                    Utils.readLocalHydraulicUnits(localHydraulicStatsPath, finalHydraulicUnitList, "Klasik");
                } else if(isHidros.getValue()) {
                    Utils.readLocalHydraulicUnits(localHydraulicStatsPath, finalHydraulicUnitList, "PowerPack");
                } else {
                    Utils.readLocalHydraulicUnits(localHydraulicStatsPath, finalHydraulicUnitList, null);
                }
            } else if(threeStateSwitch.getState().equals(ThreeStateSwitch.SwitchState.LOCALWEB)) {
                if(isKlasik.getValue()) {
                    Utils.readLocalHydraulicUnits(localHydraulicStatsPath, finalHydraulicUnitList, "Klasik");
                    hydraulicUnitInit(2, finalHydraulicUnitList);
                } else if(isHidros.getValue()) {
                    Utils.readLocalHydraulicUnits(localHydraulicStatsPath, finalHydraulicUnitList, "PowerPack");
                    hydraulicUnitInit(3, finalHydraulicUnitList);
                } else {
                    Utils.readLocalHydraulicUnits(localHydraulicStatsPath, finalHydraulicUnitList, null);
                    hydraulicUnitInit(1, finalHydraulicUnitList);
                }
            } else if(threeStateSwitch.getState().equals(ThreeStateSwitch.SwitchState.WEB)) {
                if(isKlasik.getValue()) {
                    hydraulicUnitInit(2, finalHydraulicUnitList);
                } else if(isHidros.getValue()) {
                    hydraulicUnitInit(3, finalHydraulicUnitList);
                } else {
                    hydraulicUnitInit(1, finalHydraulicUnitList);
                }
            }
        } else {
            threeStateSwitch.setCancel(true);

            if(isKlasik.getValue()) {
                Utils.readLocalHydraulicUnits(localHydraulicStatsPath, finalHydraulicUnitList, "Klasik");
            } else if(isHidros.getValue()) {
                Utils.readLocalHydraulicUnits(localHydraulicStatsPath, finalHydraulicUnitList, "PowerPack");
            } else {
                Utils.readLocalHydraulicUnits(localHydraulicStatsPath, finalHydraulicUnitList, null);
            }
        }

        int classicUnitCount = 0;
        int powerPackUnitCount = 0;

        for(HydraulicInfo currentUnit : finalHydraulicUnitList) {
            if(currentUnit.getHydraulicType().equals("Klasik")) {
                classicUnitCount++;
            } else if(currentUnit.getHydraulicType().equals("PowerPack")) {
                powerPackUnitCount++;
            }
        }

        toplamSiparisCount.setText(String.valueOf(finalHydraulicUnitList.size()));
        klasikUniteCount.setText(String.valueOf(classicUnitCount));
        hidrosUntiteCount.setText(String.valueOf(powerPackUnitCount));

        populateUIWithCachedData(finalHydraulicUnitList);
    }

    @FXML
    public void anaEkranaDon() throws IOException {
        Stage stage = (Stage) parametreCount.getScene().getWindow();
        Screen currentScreen = SceneUtil.getScreenOfNode(kullaniciAdiIsimText);

        if(loggedInUser != null) {
            loggedInUser = null;
        }

        Path authFilePath = Paths.get(SystemVariables.tokenPath);
        try {
            Files.deleteIfExists(authFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage.close();
        SceneUtil.changeScreen("fxml/Login.fxml", currentScreen);
    }

    @FXML
    public void onderGrupSiteOpen() {
        Utils.openURL(SystemVariables.WEB_URL);
    }

    public void handleClicks(ActionEvent actionEvent) throws IOException {
        if(actionEvent.getSource()==btnKlasik) {
            Utils.clearOldCalculationData("hidros");
            Utils.clearOldCalculationData("klasik");
            paneSwitch(1);
        } else if (actionEvent.getSource() == btnHidros) {
            Utils.clearOldCalculationData("hidros");
            Utils.clearOldCalculationData("klasik");
            paneSwitch(2);
        } else if (actionEvent.getSource() == btnParametreler) {
            paneSwitch(4);
        } else if (actionEvent.getSource() == btnHome) {
            pnlOverview.setStyle("-fx-background-color : #02030A");
            pnlOverview.toFront();
            updateHydraulicText();
            initializeHydraulicTable();
        } else if(actionEvent.getSource() == btnOnlineMode) {
            Stage currentStage = (Stage) btnOnlineMode.getScene().getWindow();
            currentStage.close();
            Utils.openLoginScreen(kullaniciAdiIsimText);
        } else if(actionEvent.getSource() == btnMonitorAdapter) {
            Utils.showMonitorSelectionScreen(Screen.getScreens(), SceneUtil.getScreenOfNode(kullaniciAdiIsimText), false);
        } else if (actionEvent.getSource() == btnReportBug) {
            javafx.scene.image.Image icon = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/images/general/logo.png")));
            Utils.showReportPopup(icon, SceneUtil.getScreenOfNode(kullaniciAdiIsimText), "fxml/ReportBug.fxml");
        } else if (actionEvent.getSource() == btnDebugMode) {
            javafx.scene.image.Image icon = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/assets/images/general/logo.png")));
            Utils.showConsolePopup(icon, SceneUtil.getScreenOfNode(kullaniciAdiIsimText), "fxml/Console.fxml");
        }
    }

    @FXML
    public void profileEditScreen() {
        if(loggedInUser != null) {
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
                Utils.logger.log(Level.SEVERE, e.getMessage(), e);
            }
        }
    }

    @FXML
    public void programiKapat() {
        System.exit(0);
    }

    @FXML
    public void siparisNumarasiEntered() {
        if(unitSearchBar.getText() != null) {
            if(!unitSearchBar.getText().isEmpty()) {
                List<HydraulicInfo> searchList = new ArrayList<>();
                for(HydraulicInfo currentInfo : finalHydraulicUnitList) {
                    if(currentInfo.getOrderID().equals(unitSearchBar.getText())) {
                        searchList.add(currentInfo);
                        populateUIWithCachedData(searchList);
                        break;
                    }
                }
            } else {
                initializeHydraulicTable();
            }
        }
    }

    @FXML
    public void siparisNumarasiBackSpacePressed(KeyEvent event) throws AWTException {
        if(event.getCode() == KeyCode.BACK_SPACE || event.getCode() == KeyCode.DELETE) {
            unitSearchBar.clear();
            Robot robot = new Robot();

            robot.keyPress(java.awt.event.KeyEvent.VK_TAB);
            robot.keyRelease(java.awt.event.KeyEvent.VK_TAB);
        }
    }

    public void userInfo() {
        if(loggedInUser != null) {
            kullaniciAdiIsimText.setText(loggedInUser.getUsername() + "\n" + loggedInUser.getFullName() + "\n" + loggedInUser.getCompanyName() + "\n ");
            Profile.downloadAndSetProfilePhoto(loggedInUser.getUsername(), profilePhotoCircle, kullaniciProfilFoto);
            updateHydraulicText();
            buttonsVBox.getChildren().remove(btnOnlineMode);
        } else {
            kullaniciAdiIsimText.setText("Standart Kullanıcı");
            updateHydraulicText();

            buttonsVBox.getChildren().remove(btnProfil);
            buttonsVBox.getChildren().remove(btnSignout);
            buttonsVBox.toFront();
        }
    }

    public void updateHydraulicText() {
        if(loggedInUser != null) {
            hidrolikUnitStats(() -> {
                toplamSiparisCount.setText(String.valueOf(siparisSayisi));
                klasikUniteCount.setText(String.valueOf(klasik));
                hidrosUntiteCount.setText(String.valueOf(hidros));
            });
        }

        excelVoidCount();
    }

    public void hidrolikUnitStats(Runnable updateHydraulicText) {
        String profileInfoUrl = BASE_URL + hydraulicGetStatsURLPrefix;

        HTTPMethod.sendJsonlessRequest(profileInfoUrl, "GET", new HTTPMethod.RequestCallback() {
            @Override
            public void onSuccess(String hydraulicResponse) {
                JSONObject responseJson = new JSONObject(hydraulicResponse);
                JSONObject mainObject = responseJson.getJSONObject("payload").getJSONObject("statistics");

                siparisSayisi = mainObject.getInt("Sipariş Sayısı");
                klasik = mainObject.getInt("Klasik");
                hidros = mainObject.getInt("PowerPack");
                updateHydraulicText.run();
            }

            @Override
            public void onFailure() {
                System.out.println("hydraulicGetStatsURLPrefix Error!");
            }
        });
    }

    public void hydraulicUnitInit(int type, List<HydraulicInfo> finalHydraulicUnitList) {
        String reqURL = BASE_URL + hydraulicGetDetailsURLPrefix;
        String jsonHydraulicBody;

        if(type == 1) {
            //Tümü
            jsonHydraulicBody = "{\"UnitType\": \"" + "" + "\"}";
            HTTPMethod.sendJsonRequest(reqURL, "POST", jsonHydraulicBody, new HTTPMethod.RequestCallback() {
                @Override
                public void onSuccess(String response) {
                    JSONArray responseJson = new JSONObject(response).getJSONObject("payload").getJSONArray("hydraulicInfoResult");
                    Utils.parseHydraulicJsonResponse(String.valueOf(responseJson), finalHydraulicUnitList);
                    populateUIWithCachedData(finalHydraulicUnitList);
                }

                @Override
                public void onFailure() {
                    System.out.println(jsonHydraulicBody);
                    System.out.println("hydraulicGetDetailsURLPrefix All Units Error.");
                }
            });
        } else if(type == 2) {
            //Klasik
            jsonHydraulicBody = "{\"UnitType\": \"" + "Klasik" + "\"}";
            HTTPMethod.sendJsonRequest(reqURL, "POST", jsonHydraulicBody, new HTTPMethod.RequestCallback() {
                @Override
                public void onSuccess(String response) {
                    JSONArray responseJson = new JSONObject(response).getJSONObject("payload").getJSONArray("hydraulicInfoResult");
                    Utils.parseHydraulicJsonResponse(String.valueOf(responseJson), finalHydraulicUnitList);
                    populateUIWithCachedData(finalHydraulicUnitList);
                }

                @Override
                public void onFailure() {
                    System.out.println("hydraulicGetDetailsURLPrefix Klasik Error.");
                }
            });
        } else if(type == 3) {
            //Hidros
            jsonHydraulicBody = "{\"UnitType\": \"" + "PowerPack" + "\"}";
            HTTPMethod.sendJsonRequest(reqURL, "POST", jsonHydraulicBody, new HTTPMethod.RequestCallback() {
                @Override
                public void onSuccess(String response) {
                    JSONArray responseJson = new JSONObject(response).getJSONObject("payload").getJSONArray("hydraulicInfoResult");
                    Utils.parseHydraulicJsonResponse(String.valueOf(responseJson), finalHydraulicUnitList);
                    populateUIWithCachedData(finalHydraulicUnitList);
                }

                @Override
                public void onFailure() {
                    System.out.println("hydraulicGetDetailsURLPrefix PowerPack Error.");
                }
            });
        }
    }

    private void populateUIWithCachedData(List<HydraulicInfo> activeHydraulicList) {
        pnItems.getChildren().clear();

        for (HydraulicInfo info : activeHydraulicList) {
            try {
                FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(Launcher.class.getResource("fxml/Item.fxml")));
                Node node = loader.load();

                HBox itemC = (HBox) loader.getNamespace().get("itemC");
                Label orderNumberLabel = (Label) loader.getNamespace().get("orderNumberLabel");
                Label orderDateLabel = (Label) loader.getNamespace().get("orderDateLabel");
                Label typeLabel = (Label) loader.getNamespace().get("typeLabel");
                Label InChargeLabel = (Label) loader.getNamespace().get("InChargeLabel");
                Button pdfViewButton = (Button) loader.getNamespace().get("pdfViewButton");
                ImageView excelViewButton = (ImageView) loader.getNamespace().get("excelPart");
                ImageView deleteButton = (ImageView) loader.getNamespace().get("deleteIcon");
                ImageView replayButton = (ImageView) loader.getNamespace().get("replayIcon");

                if(info.isLocal()) {
                    pdfViewButton.setText("Aç");
                    itemC.setStyle("-fx-background-radius: 5; -fx-background-insets: 0;");
                }

                orderNumberLabel.setText(info.getOrderID());
                orderDateLabel.setText(Utils.formatDateTimeMultiLine(String.valueOf(info.getCreatedDate())));
                typeLabel.setText(info.getHydraulicType());
                InChargeLabel.setText(info.getUserName());

                pdfViewButton.setOnAction(event -> {
                    if(info.isLocal()) {
                        Utils.openFile(info.getSchematicID());
                    } else {
                        openURL(BASE_URL + getSchematicURLPrefix + info.getOrderID());
                    }
                });

                excelViewButton.setOnMouseClicked(event -> {
                    if(info.isLocal()) {
                        Utils.openFile(info.getPartListID());
                    } else {
                        openURL(BASE_URL + getPartListURLPrefix + info.getOrderID());
                    }
                });

                deleteButton.setOnMouseClicked(event -> {
                    if(info.isLocal()) {
                        Utils.deleteLocalUnitData(localHydraulicStatsPath, info.getOrderID());
                    } else {
                        String jsonDeleteBody = "{\"orderID\": \"" + info.getOrderID() + "\"}";
                        try {
                            HydraulicService.deleteHydraulicUnit(jsonDeleteBody, kullaniciAdiIsimText, loggedInUser.getAccessToken());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    initializeHydraulicTable();
                });

                replayButton.setOnMouseClicked(event -> {
                    try {
                        String correctedJsonData = new String(Utils.decompress(info.getUnitParameters()).getBytes(), StandardCharsets.UTF_8);
                        JSONObject currentHydraulicUnit = new JSONObject(correctedJsonData);

                        if (currentHydraulicUnit.has("Ünite Tipi")) {
                            String uniteTipi = currentHydraulicUnit.getString("Ünite Tipi");
                            if(uniteTipi.equals("Klasik")) {
                                Main.classicReplayData.setSecilenUniteTipi(Utils.getValidString(currentHydraulicUnit, "Ünite Tipi"));
                                Main.classicReplayData.setGirilenSiparisNumarasi(Utils.getValidString(currentHydraulicUnit, "Sipariş Numarası"));
                                Main.classicReplayData.setSecilenMotor(Utils.getValidString(currentHydraulicUnit, "Motor"));
                                Main.classicReplayData.setSecilenSogutmaDurumu(Utils.getValidString(currentHydraulicUnit, "Soğutma"));
                                Main.classicReplayData.setSecilenHidrolikKilitDurumu(Utils.getValidString(currentHydraulicUnit, "Hidrolik Kilit"));
                                Main.classicReplayData.setSecilenPompa(Utils.getValidString(currentHydraulicUnit, "Pompa"));
                                Main.classicReplayData.setGirilenTankKapasitesiMiktari(currentHydraulicUnit.getInt("Gerekli Yağ Miktarı"));
                                Main.classicReplayData.setKompanzasyonDurumu(Utils.getValidString(currentHydraulicUnit, "Kompanzasyon"));
                                Main.classicReplayData.setSecilenValfTipi(Utils.getValidString(currentHydraulicUnit, "Valf Tipi"));
                                Main.classicReplayData.setSecilenKilitMotor(Utils.getValidString(currentHydraulicUnit, "Kilit Motor"));
                                Main.classicReplayData.setSecilenKilitPompa(Utils.getValidString(currentHydraulicUnit, "Kilit Pompa"));
                                Main.classicReplayData.setSecilenKampana(currentHydraulicUnit.getInt("Seçilen Kampana"));
                                Main.classicReplayData.setSecilenPompaVal(currentHydraulicUnit.getDouble("Seçilen Pompa Val"));

                                Utils.clearOldCalculationData("klasik");
                                paneSwitch(1);
                            } else if(uniteTipi.equals("PowerPack")) {
                                Main.powerPackReplayData.setGirilenSiparisNumarasi(Utils.getValidString(currentHydraulicUnit, "Sipariş Numarası"));
                                Main.powerPackReplayData.setSecilenMotorTipi(Utils.getValidString(currentHydraulicUnit, "Motor Voltaj"));
                                Main.powerPackReplayData.setUniteTipiDurumu(Utils.getValidString(currentHydraulicUnit, "Ünite Durumu"));
                                Main.powerPackReplayData.setSecilenMotorGucu(Utils.getValidString(currentHydraulicUnit, "Motor Gücü"));
                                Main.powerPackReplayData.setSecilenPompa(Utils.getValidString(currentHydraulicUnit, "Pompa"));
                                Main.powerPackReplayData.setSecilenTankTipi(Utils.getValidString(currentHydraulicUnit, "Tank Tipi"));
                                Main.powerPackReplayData.setSecilenTankKapasitesi(Utils.getValidString(currentHydraulicUnit, "Tank Kapasitesi"));
                                Main.powerPackReplayData.setSecilenPlatformTipi(Utils.getValidString(currentHydraulicUnit, "Platform Tipi"));
                                Main.powerPackReplayData.setSecilenBirinciValf(Utils.getValidString(currentHydraulicUnit, "1. Valf Tipi"));
                                Main.powerPackReplayData.setSecilenInisTipi(Utils.getValidString(currentHydraulicUnit, "İniş Metodu"));
                                Main.powerPackReplayData.setSecilenIkinciValf(Utils.getValidString(currentHydraulicUnit, "2. Valf Tipi"));

                                String ozelTankOlculeri = Utils.getValidString(currentHydraulicUnit, "Özel Tank Ölçüleri (GxDxY)");
                                if (ozelTankOlculeri != null && ozelTankOlculeri.contains("x")) {
                                    String[] tankOlculeri = ozelTankOlculeri.split("x");
                                    if (tankOlculeri.length == 3) {
                                        Main.powerPackReplayData.setSecilenOzelTankGenislik("null".equals(tankOlculeri[0]) ? null : tankOlculeri[0]);
                                        Main.powerPackReplayData.setSecilenOzelTankDerinlik("null".equals(tankOlculeri[1]) ? null : tankOlculeri[1]);
                                        Main.powerPackReplayData.setSecilenOzelTankYukseklik("null".equals(tankOlculeri[2]) ? null : tankOlculeri[2]);
                                    } else {
                                        System.err.println("Hata: 'Özel Tank Ölçüleri (GxDxY)' formatı beklenenden farklı.");
                                    }
                                } else {
                                    System.err.println("Hata: 'Özel Tank Ölçüleri (GxDxY)' değeri null veya geçersiz formatta.");
                                }

                                Utils.clearOldCalculationData("hidros");
                                paneSwitch(2);
                            } else {
                                System.out.println(uniteTipi);
                                System.err.println("Hata: 'Ünite Tipi' geçersiz.");
                            }
                        } else {
                            System.err.println("Hata: 'Ünite Tipi' anahtarı JSON objesinde bulunamadı.");
                        }
                    } catch (JSONException e) {
                        System.err.println("Hata: JSON formatı hatalı veya geçersiz.\nHata Mesajı: " + e.getMessage());
                    } catch (NullPointerException e) {
                        System.err.println("Hata: JSON verisi null.\nHata Mesajı: " + e.getMessage());
                    } catch (Exception e) {
                        System.err.println("Hata: Beklenmeyen bir hata oluştu - " + e.getMessage());
                    }
                });

                if (info.isLocal()) {
                    node.setOnMouseEntered(event -> itemC.setStyle("-fx-background-color : #FF6961"));
                } else {
                    node.setOnMouseEntered(event -> itemC.setStyle("-fx-background-color : #BCECE6"));
                }
                node.setOnMouseExited(event -> itemC.setStyle("-fx-background-color : #02030A"));

                pnItems.getChildren().add(node);
            } catch (IOException e) {
                Utils.logger.log(Level.SEVERE, e.getMessage(), e);
            }
        }
    }

    private void excelVoidCount() {
        try {
            FileReader reader = new FileReader(SystemVariables.generalDBPath);
            StringBuilder sb = new StringBuilder();
            int i;
            while ((i = reader.read()) != -1) {
                sb.append((char) i);
            }
            reader.close();

            JSONObject jsonObject = new JSONObject(sb.toString());
            JSONObject voidValues = jsonObject.getJSONObject("void_values");

            parametreCount.setText(String.valueOf(voidValues.length()));
        } catch (IOException e) {
            Utils.logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private void paneSwitch(int state) {
        if(state == 1) {
            pnlMenus.setStyle("-fx-background-color : #353a46");
            pnlMenus.toFront();
            try {
                FXMLLoader loader = new FXMLLoader(Launcher.class.getResource("fxml/Classic.fxml"));
                Pane parametrePane = loader.load();
                pnlMenus.getChildren().setAll(parametrePane);
            } catch (IOException e) {
                Utils.logger.log(Level.SEVERE, e.getMessage(), e);
            }
        } else if(state == 2) {
            pnlMenus.setStyle("-fx-background-color : #353a46");
            pnlMenus.toFront();
            try {
                FXMLLoader loader = new FXMLLoader(Launcher.class.getResource("fxml/PowerPack.fxml"));
                Pane parametrePane = loader.load();
                pnlMenus.getChildren().setAll(parametrePane);
            } catch (IOException e) {
                Utils.logger.log(Level.SEVERE, e.getMessage(), e);
            }
        } else if(state == 4) {
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
                popupController.setValues(
                        getLocalHydraulicData().kampanaTankArasiBoslukX, getLocalHydraulicData().kampanaTankArasiBoslukY, getLocalHydraulicData().kampanaBoslukYOn,
                        getLocalHydraulicData().tekHizTankArasiBoslukX, getLocalHydraulicData().tekHizTankArasiBoslukY, getLocalHydraulicData().tekHizAraBoslukX,
                        getLocalHydraulicData().tekHizYOn, getLocalHydraulicData().tekHizBlokX, getLocalHydraulicData().tekHizBlokY,
                        getLocalHydraulicData().ciftHizTankArasiBoslukX, getLocalHydraulicData().ciftHizTankArasiBoslukY, getLocalHydraulicData().ciftHizAraBoslukX,
                        getLocalHydraulicData().ciftHizYOn, getLocalHydraulicData().ciftHizBlokX, getLocalHydraulicData().ciftHizBlokY,
                        getLocalHydraulicData().kilitliBlokTankArasiBoslukX, getLocalHydraulicData().kilitliBlokTankArasiBoslukY, getLocalHydraulicData().kilitliBlokAraBoslukX,
                        getLocalHydraulicData().kilitliBlokYOn, getLocalHydraulicData().kilitliBlokX, getLocalHydraulicData().kilitliBlokY,
                        getLocalHydraulicData().kilitMotorTankArasiBoslukX, getLocalHydraulicData().kilitMotorTankArasiBoslukY, getLocalHydraulicData().kilitMotorAraBoslukX,
                        getLocalHydraulicData().kilitMotorYOn, getLocalHydraulicData().tekHizKilitAyriY, getLocalHydraulicData().tekHizKilitAyriYOn,
                        getLocalHydraulicData().ciftHizKilitAyriY, getLocalHydraulicData().ciftHizKilitAyriYOn, getLocalHydraulicData().kilitMotorX,
                        getLocalHydraulicData().kilitMotorY, getLocalHydraulicData().kayipLitre, getLocalHydraulicData().defaultHeight
                );
                popupController.showValues();
            } catch (IOException e) {
                Utils.logger.log(Level.SEVERE, e.getMessage(), e);
            }
        }
    }

    public void minimizeProgram() {
        if (pnlMenus != null) {
            Stage stage = (Stage) pnlMenus.getScene().getWindow();
            stage.setIconified(true);
        }
    }

    public void checkLicense() {
        String licenseKey = Utils.checkLicenseKey();
        Screen currentScreen = SceneUtil.getScreenOfNode(versionCode);
        Stage currentStage = (Stage) versionCode.getScene().getWindow();
        if (licenseKey == null) {
            Utils.showLicensePopup(currentScreen, currentStage);
        } else {
            String licenseCheckURL = BASE_URL + checkLicenseUrlPrefix;

            try {
                LicenseService.checkLicense(licenseCheckURL, licenseKey, null, () -> {
                    Utils.showLicensePopup(currentScreen, currentStage);
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}