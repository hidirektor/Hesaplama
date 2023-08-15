package me.t3sl4.hydraulic.Util.HTTP;

import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

public class HTTPUtil {

    public static String readResponse(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

    public static String sendPostRequest(String url, String jsonBody) throws IOException {
        HttpURLConnection connection = null;
        try {
            URL urlObj = new URL(url);
            connection = (HttpURLConnection) urlObj.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonBody.getBytes("UTF-8");
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return readResponse(connection.getInputStream());
            } else {
                return null; // veya hata mesajı
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static String parseStringVal(String response, String field) {
        JSONObject jsonObject = new JSONObject(response);
        return jsonObject.getString(field);
    }
}