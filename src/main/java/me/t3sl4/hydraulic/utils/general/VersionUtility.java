package me.t3sl4.hydraulic.utils.general;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class VersionUtility {

    private static final String CURRENT_VERSION = SystemVariables.getVersion();

    /**
     * GitHub releases/latest URL'inden en son sürümün tag'ini alır.
     * @return Son sürüm mevcutsa true, değilse false.
     */
    public static boolean isUpdateAvailable() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SystemVariables.RELEASE_URL))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            int statusCode = response.statusCode();
            if (statusCode != 200) {
                System.out.println("Error: Unexpected status code " + statusCode);
                return false;
            }

            String responseBody = response.body();

            String latestVersion = extractTagFromHTML(responseBody);
            if (latestVersion == null) {
                System.out.println("Error: Could not extract version tag from HTML");
                return false;
            }

            System.out.println("Latest version found: " + latestVersion);

            if (!CURRENT_VERSION.equals(latestVersion)) {
                System.out.println("Güncelleme mevcut: " + latestVersion);
                return true;
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Program güncel.");
        return false;
    }

    /**
     * HTML yanıtında "releases/tag/{version}" bilgisini bulur.
     * @param html HTML yanıtı
     * @return Tag değeri (örneğin, "v1.2.3"), ya da null
     */
    private static String extractTagFromHTML(String html) {
        String tagPrefix = "/releases/tag/";
        int tagIndex = html.indexOf(tagPrefix);
        if (tagIndex == -1) {
            return null;
        }

        int startIndex = tagIndex + tagPrefix.length();
        int endIndex = html.indexOf("\"", startIndex);
        if (endIndex == -1) {
            return null;
        }

        return html.substring(startIndex, endIndex);
    }
}