package me.t3sl4.hydraulic.utils.general;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class VersionUtility {

    private static final String CURRENT_VERSION = SystemVariables.getVersion();

    /**
     * GitHub API'den son sürümü kontrol eder.
     * @return Son sürüm mevcutsa true, değilse false.
     */
    public static boolean isUpdateAvailable() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SystemVariables.RELEASE_URL))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JSONArray releases = new JSONArray(response.body());
            if (releases.length() > 0) {
                JSONObject latestRelease = releases.getJSONObject(0);
                String latestVersion = latestRelease.getString("tag_name");

                if (!CURRENT_VERSION.equals(latestVersion)) {
                    System.out.println("Güncelleme mevcut: " + latestVersion);
                    return true;
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Program güncel.");
        return false;
    }
}
