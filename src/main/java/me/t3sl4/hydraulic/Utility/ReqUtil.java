package me.t3sl4.hydraulic.Utility;

import javafx.scene.control.Label;
import javafx.stage.Stage;
import me.t3sl4.hydraulic.Utility.Data.User.User;
import me.t3sl4.hydraulic.Utility.HTTP.HTTPRequest;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import static me.t3sl4.hydraulic.Launcher.*;
import static me.t3sl4.hydraulic.Screens.Main.loggedInUser;

public class ReqUtil {

    public static void loginReq(String loginUrl, String jsonLoginBody, Stage stage, String userName, String password, Label lblErrors) throws IOException {
        HTTPRequest.sendRequest(loginUrl, jsonLoginBody, new HTTPRequest.RequestCallback() {
            @Override
            public void onSuccess(String loginResponse) {
                String profileInfoUrl = BASE_URL + profileInfoURLPrefix +":Role";
                String jsonProfileInfoBody = "{\"Username\": \"" + userName + "\"}";
                HTTPRequest.sendRequest(profileInfoUrl, jsonProfileInfoBody, new HTTPRequest.RequestCallback() {
                    @Override
                    public void onSuccess(String profileInfoResponse) {
                        JSONObject roleObject = new JSONObject(profileInfoResponse);
                        String roleValue = roleObject.getString("Role");
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
        String profileInfoUrl = BASE_URL + wholeProfileURLPrefix;
        String profileInfoBody = "{\"username\": \"" + loggedInUser.getUsername() + "\"}";

        HTTPRequest.sendRequest(profileInfoUrl, profileInfoBody, new HTTPRequest.RequestCallback() {
            @Override
            public void onSuccess(String profileInfoResponse) {
                JSONObject responseJson = new JSONObject(profileInfoResponse);
                String parsedRole = responseJson.getString("Role");
                String parsedFullName = responseJson.getString("NameSurname");
                String parsedEmail = responseJson.getString("Email");
                String parsedPhone = responseJson.getString("Phone");
                String parsedCompanyName = responseJson.getString("CompanyName");

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

    public static boolean uploadPDFFile2Server(String filePath, String girilenSiparisNumarasi) {
        String uploadUrl = BASE_URL + uploadPDFURLPrefix;
        final boolean[] pdfSucc = {false};

        File pdfFile = new File(filePath);
        if (!pdfFile.exists()) {
            Utils.showErrorMessage("PDF dosyası bulunamadı !");
            return pdfSucc[0];
        }

        HTTPRequest.sendMultipartRequest(uploadUrl,girilenSiparisNumarasi, pdfFile, new HTTPRequest.RequestCallback() {
            @Override
            public void onSuccess(String response) {
                pdfSucc[0] = true;
            }

            @Override
            public void onFailure() {
                Utils.showErrorMessage("Ünite dosyaları yüklenirken hata meydana geldi !");
            }
        });

        return pdfSucc[0];
    }

    public static boolean uploadExcelFile2Server(String filePath, String girilenSiparisNumarasi) {
        String uploadUrl = BASE_URL + uploadExcelURLPrefix;
        final boolean[] excelSucc = {false};

        File excelFile = new File(filePath);
        if (!excelFile.exists()) {
            Utils.showErrorMessage("Excel dosyası bulunamadı !");
            return excelSucc[0];
        }

        HTTPRequest.sendMultipartRequest(uploadUrl, girilenSiparisNumarasi, excelFile, new HTTPRequest.RequestCallback() {
            @Override
            public void onSuccess(String response) {
                excelSucc[0] = true;
            }

            @Override
            public void onFailure() {
                Utils.showErrorMessage("Ünite dosyaları yüklenirken hata meydana geldi !");
            }
        });

        return excelSucc[0];
    }
}
