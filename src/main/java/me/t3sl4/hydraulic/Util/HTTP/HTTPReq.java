package me.t3sl4.hydraulic.Util.HTTP;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HTTPReq {
    private String reqURL;
    private String reqParameters;

    public HTTPReq(String reqURL, String reqParameters, String userTypeEndpoint, String authenticationEndPoint) {
        this.reqURL = reqURL;
        this.reqParameters = reqParameters;
    }

    public String sendGET(String adres, String urlParams) {
        try {
            URL url;
            if (urlParams != null && !urlParams.isEmpty()) {
                url = new URL(adres + "?" + urlParams);
            } else {
                url = new URL(adres);
            }

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                return response.toString();
            } else {
                System.out.println("HTTP isteği başarısız oldu. Hata kodu: " + responseCode);
                return null;
            }
        } catch (Exception e) {
            System.out.println("HTTP isteği sırasında bir hata oluştu: " + e.getMessage());
            return null;
        }
    }

    public String sendPOST(String adres, String urlParams) {
        try {
            URL url = new URL(adres);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            byte[] postData = urlParams.getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;
            connection.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                wr.write(postData);
            }

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                return response.toString();
            } else {
                System.out.println("HTTP isteği başarısız oldu. Hata kodu: " + responseCode);
                return null;
            }
        } catch (Exception e) {
            System.out.println("HTTP isteği sırasında bir hata oluştu: " + e.getMessage());
            return null;
        }
    }
}
