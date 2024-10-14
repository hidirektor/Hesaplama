package me.t3sl4.hydraulic.utils.service;

import javafx.scene.control.Label;
import javafx.stage.Stage;
import me.t3sl4.hydraulic.utils.Utils;
import me.t3sl4.hydraulic.utils.general.SystemVariables;
import me.t3sl4.hydraulic.utils.service.UserDataService.User;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

import static me.t3sl4.hydraulic.utils.general.SystemVariables.*;

public class RequestService {

    public static void loginReq(String loginUrl, String jsonLoginBody, Stage stage, String userName, String password, Label lblErrors) throws IOException {
        HTTPRequest.sendJsonRequest(loginUrl, "POST", jsonLoginBody, new HTTPRequest.RequestCallback() {
            @Override
            public void onSuccess(String loginResponse) throws IOException {
                JSONObject mainObject = new JSONObject(loginResponse);
                JSONObject loginObject = mainObject.getJSONObject("payload");
                String userID = loginObject.getString("userID");
                String accessToken = loginObject.getString("accessToken");
                String refreshToken = loginObject.getString("refreshToken");
                String tokenFilePath = tokenPath;
                FileWriter writer = new FileWriter(tokenFilePath);
                writer.write("userName: " + userName + "\n");
                writer.write("userID: " + userID + "\n");
                writer.write("AccessToken: " + accessToken + "\n");
                writer.write("RefreshToken: " + refreshToken + "\n");
                writer.close();

                String profileInfoUrl = BASE_URL + profileInfoURLPrefix;
                String jsonProfileInfoBody = "{\"userID\": \"" + userID + "\"}";
                HTTPRequest.sendAuthorizedJsonRequest(profileInfoUrl, "POST", jsonProfileInfoBody, accessToken, new HTTPRequest.RequestCallback() {
                    @Override
                    public void onSuccess(String profileInfoResponse) {
                        JSONObject defaultObject = new JSONObject(profileInfoResponse);
                        JSONObject mainObject = defaultObject.getJSONObject("payload");

                        JSONObject userObject = mainObject.getJSONObject("user");
                        JSONObject userPreferencesObject = mainObject.getJSONObject("userPreferences");

                        String roleValue = userObject.getString("userType");
                        if (roleValue.equals("TECHNICIAN") || roleValue.equals("ENGINEER") || roleValue.equals("SYSOP")) {
                            loggedInUser = new User(userName);

                            SystemVariables.loggedInUser.setUserID(userID);
                            SystemVariables.loggedInUser.setAccessToken(accessToken);
                            SystemVariables.loggedInUser.setRefreshToken(refreshToken);

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

    public static void updateUserReq(Runnable onUserUpdateComplete, Runnable onFailure) {
        String profileInfoUrl = BASE_URL + profileInfoURLPrefix;
        String profileInfoBody = "{\"userID\": \"" + loggedInUser.getUserID() + "\"}";

        HTTPRequest.sendAuthorizedJsonRequest(profileInfoUrl, "POST", profileInfoBody, SystemVariables.loggedInUser.getAccessToken(), new HTTPRequest.RequestCallback() {
            @Override
            public void onSuccess(String profileInfoResponse) {
                JSONObject responseJson = new JSONObject(profileInfoResponse);
                JSONObject defaultInfoObject = responseJson.getJSONObject("payload");
                JSONObject userInfoObject = defaultInfoObject.getJSONObject("user");

                String parsedRole = userInfoObject.getString("userType");
                String parsedFullName = userInfoObject.getString("nameSurname");
                String parsedEmail = userInfoObject.getString("eMail");
                String parsedPhone = userInfoObject.getString("phoneNumber");
                String parsedCompanyName = userInfoObject.getString("companyName");

                loggedInUser.setRole(parsedRole);
                loggedInUser.setFullName(parsedFullName);
                loggedInUser.setEmail(parsedEmail);
                loggedInUser.setPhone(parsedPhone);
                loggedInUser.setCompanyName(parsedCompanyName);
                onUserUpdateComplete.run();
            }

            @Override
            public void onFailure() {
                if(onFailure != null) {
                    onFailure.run();
                }
                System.out.println("Kullanıcı bilgileri alınamadı!");
            }
        });
    }

    public static void updateUserAndOpenMainScreen(Stage stage, Label inLabel) {
        RequestService.updateUserReq(() -> {
            try {
                Utils.openMainScreen(inLabel);
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.close();
        }, () -> {
            /*try {
                Utils.deleteLocalData();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }*/
        });
    }
}
