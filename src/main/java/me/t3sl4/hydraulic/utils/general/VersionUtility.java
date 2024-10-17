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
     * @return Son sürüm mevcutsa bir String dizisi (0: versiyon, 1: detay), değilse null.
     */
    public static String[] checkForUpdate() {
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
                return null;
            }

            String responseBody = response.body();

            String latestVersion = extractTagFromHTML(responseBody);
            String releaseDetails = extractReleaseDetailsFromHTML(responseBody); // New method to extract details

            if (latestVersion == null) {
                System.out.println("Error: Could not extract version tag from HTML");
                return null;
            }

            if (!CURRENT_VERSION.equals(latestVersion)) {
                System.out.println("Güncelleme mevcut: " + latestVersion);
                return new String[]{latestVersion, releaseDetails};
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
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

    /**
     * HTML yanıtından sürüm detaylarını çıkarır.
     * @param html HTML yanıtı
     * @return Sürüm detayları, ya da boş bir string
     */
    private static String extractReleaseDetailsFromHTML(String html) {
        String selectorPrefix = "data-test-selector=\"body-content\"";
        int selectorIndex = html.indexOf(selectorPrefix);

        if (selectorIndex == -1) {
            return ""; // Return empty string if the selector is not found
        }

        // Move to the start of the containing <div>
        int divStartIndex = html.lastIndexOf("<div", selectorIndex);
        if (divStartIndex == -1) {
            return ""; // Return empty string if the <div> is not found
        }

        // Look for the closing </div> tag for this section
        int divEndIndex = html.indexOf("</div>", selectorIndex);
        if (divEndIndex == -1) {
            return ""; // Return empty string if the closing </div> is not found
        }

        // Extract the inner HTML of the <div>
        String detailsHtml = html.substring(divStartIndex, divEndIndex);
        StringBuilder detailsBuilder = new StringBuilder();

        // Extract list items from the detailsHtml
        String liPrefix = "<li>";
        int liIndex = detailsHtml.indexOf(liPrefix);

        while (liIndex != -1) {
            int liStartIndex = liIndex + liPrefix.length();
            int liEndIndex = detailsHtml.indexOf("</li>", liStartIndex);
            if (liEndIndex == -1) {
                break; // Break if there is no closing </li>
            }

            String detail = detailsHtml.substring(liStartIndex, liEndIndex).trim();
            detailsBuilder.append("- ").append(detail).append("\n");

            liIndex = detailsHtml.indexOf(liPrefix, liEndIndex);
        }

        return detailsBuilder.toString().trim(); // Return formatted release details
    }
}
