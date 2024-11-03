package me.t3sl4.hydraulic.utils.service.HTTP.Request.License;

import me.t3sl4.hydraulic.app.Main;
import me.t3sl4.hydraulic.utils.Utils;
import me.t3sl4.hydraulic.utils.service.HTTP.HTTPMethod;
import org.json.JSONObject;

import java.io.IOException;

public class LicenseService {
    public static void activateLicense(String licenseUrl, String licenseKey, String activatedBy, String accessToken, Runnable onSuccess, Runnable onFailure) throws IOException {
        JSONObject licenseJson = new JSONObject();
        licenseJson.put("licenseKey", licenseKey);
        licenseJson.put("activatedBy", activatedBy);
        licenseJson.put("deviceInfo", Utils.getDeviceInfoAsJson().toString());

        HTTPMethod.sendAuthorizedJsonRequest(licenseUrl, "POST", licenseJson.toString(), accessToken, new HTTPMethod.RequestCallback() {
            @Override
            public void onSuccess(String licenseResponse) {
                if(onSuccess != null) {
                    onSuccess.run();
                }
            }

            @Override
            public void onFailure() {
                if(onFailure != null) {
                    onFailure.run();
                }
            }
        });
    }

    public static void checkLicense(String licenseUrl, String licenseKey, Runnable onSuccess, Runnable onFailure) throws IOException {
        JSONObject licenseJson = new JSONObject();
        licenseJson.put("licenseKey", licenseKey);

        HTTPMethod.sendJsonRequest(licenseUrl, "POST", licenseJson.toString(), new HTTPMethod.RequestCallback() {
            @Override
            public void onSuccess(String licenseResponse) {
                Main.license = licenseResponse;
                if(onSuccess != null) {
                    onSuccess.run();
                }
            }

            @Override
            public void onFailure() {
                if(onFailure != null) {
                    onFailure.run();
                }
            }
        });
    }
}
