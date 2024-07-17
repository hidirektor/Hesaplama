package me.t3sl4.hydraulic.Utility;

import javafx.scene.control.Label;
import javafx.stage.Stage;
import me.t3sl4.hydraulic.Launcher;
import me.t3sl4.hydraulic.Utility.Data.User.User;
import me.t3sl4.hydraulic.Utility.HTTP.HTTPRequest;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static me.t3sl4.hydraulic.Launcher.*;
import static me.t3sl4.hydraulic.Screens.Main.loggedInUser;

public class ReqUtil {

    public static void loginReq(String loginUrl, String jsonLoginBody, Stage stage, String userName, String password, Label lblErrors) throws IOException {
        HTTPRequest.sendRequest(loginUrl, jsonLoginBody, new HTTPRequest.RequestCallback() {
            @Override
            public void onSuccess(String loginResponse) throws IOException {
                JSONObject loginObject = new JSONObject(loginResponse);
                String userID = loginObject.getString("userID");
                String accessToken = loginObject.getString("accessToken");
                String refreshToken = loginObject.getString("refreshToken");
                String tokenFilePath = Launcher.tokenPath;
                FileWriter writer = new FileWriter(tokenFilePath);
                writer.write("userID: " + userID + "\n");
                writer.write("AccessToken: " + accessToken + "\n");
                writer.write("RefreshToken: " + refreshToken + "\n");
                writer.close();

                String profileInfoUrl = BASE_URL + profileInfoURLPrefix;
                String jsonProfileInfoBody = "{\"userID\": \"" + userID + "\"}";
                HTTPRequest.sendRequest(profileInfoUrl, jsonProfileInfoBody, new HTTPRequest.RequestCallback() {
                    @Override
                    public void onSuccess(String profileInfoResponse) {
                        JSONObject mainObject = new JSONObject(profileInfoResponse);

                        JSONObject userObject = new JSONObject(mainObject.getJSONObject("user"));
                        JSONObject userPreferencesObject = new JSONObject(mainObject.getJSONObject("userPreferences"));

                        String roleValue = userObject.getString("userType");
                        if (roleValue.equals("TECHNICIAN") || roleValue.equals("ENGINEER") || roleValue.equals("SYSOP")) {
                            loggedInUser = new User(userName);

                            updateUserAndOpenMainScreen(stage, lblErrors);
                        } else {
                            Utils.showErrorOnLabel(lblErrors, "Hidrolik aracını normal kullanıcılar kullanamaz.");
                        }
                    }

                    @Override
                    public void onFailure() {
                        Utils.showErrorOnLabel(lblErrors, "Profil bilgileri alınamadı!");
                    }
                });
            }

            @Override
            public void onFailure() {
                Utils.showErrorOnLabel(lblErrors, "Kullanıcı adı veya şifre hatalı !");
            }
        });
    }

    public static void updateUserReq(Runnable onUserUpdateComplete) {
        String profileInfoUrl = BASE_URL + profileInfoURLPrefix;
        String profileInfoBody = "{\"userID\": \"" + Launcher.userID + "\"}";

        HTTPRequest.sendRequest(profileInfoUrl, profileInfoBody, new HTTPRequest.RequestCallback() {
            @Override
            public void onSuccess(String profileInfoResponse) {
                JSONObject responseJson = new JSONObject(profileInfoResponse);
                JSONObject userInfoObject = responseJson.getJSONObject("user");

                String parsedRole = responseJson.getString("userType");
                String parsedFullName = responseJson.getString("nameSurname");
                String parsedEmail = responseJson.getString("eMail");
                String parsedPhone = responseJson.getString("phoneNumber");
                String parsedCompanyName = responseJson.getString("companyName");

                loggedInUser.setRole(parsedRole);
                loggedInUser.setFullName(parsedFullName);
                loggedInUser.setEmail(parsedEmail);
                loggedInUser.setPhone(parsedPhone);
                loggedInUser.setCompanyName(parsedCompanyName);
                onUserUpdateComplete.run();
            }

            @Override
            public void onFailure() {
                System.out.println("Kullanıcı bilgileri alınamadı!");
            }
        });
    }

    public static void updateUserAndOpenMainScreen(Stage stage, Label inLabel) {
        ReqUtil.updateUserReq(() -> {
            try {
                Utils.openMainScreen(inLabel);
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.close();
        });
    }

    public static boolean createHydraulicUnit(String userName, String orderID, String hydraulicType, String partListPath, String schematicPath) {
        String creationURL = BASE_URL + createHydraulicURLPrefix;
        final boolean[] createSucc = {false};

        File partListFile = new File(partListPath);
        File schematicFile = new File(schematicPath);

        if(!partListFile.exists() || !schematicFile.exists()) {
            Utils.showErrorMessage(".xlsx ya da .pdf dosyası bulunamadı !");
            return createSucc[0];
        }

        HTTPRequest.sendMultipartRequestMultiple(creationURL, userName, orderID, hydraulicType, partListFile, schematicFile, new HTTPRequest.RequestCallback() {
            @Override
            public void onSuccess(String response) {
                createSucc[0] = true;
            }

            @Override
            public void onFailure() {
                Utils.showErrorMessage("Ünite dosyaları yüklenirken hata meydana geldi !");
            }
        });

        return createSucc[0];
    }
}
