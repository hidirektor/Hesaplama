package me.t3sl4.hydraulic.Controllers;

import javafx.concurrent.Task;
import me.t3sl4.hydraulic.Util.HTTP.HTTPRequest;
import okhttp3.*;

import java.io.FileOutputStream;
import java.io.IOException;

public class Test {
    public static void main(String[] args) {
        String url = "http://localhost:3000/api/fileSystem/downloadPhoto";
        String username = "asd";
        String jsonBody = "{\"username\":\"" + username + "\"} ";

        String localFilePath = "C:/Users/" + System.getProperty("user.name") + "/OnderGrup/profilePhoto/";
        String localFileFinalPath = localFilePath + username + ".jpg";

        HTTPRequest.sendRequest4File(url, jsonBody, localFileFinalPath, new HTTPRequest.RequestCallback() {
            @Override
            public void onSuccess(String response) {
                System.out.println("Başarılı");
            }

            @Override
            public void onFailure() {
                System.out.println("Profil fotoğrafı indirilemedi.");
            }
        });
    }

    public static void sendRequest4File(String url, String jsonBody, String localFilePath) {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, jsonBody);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        Thread thread = new Thread(() -> {
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                try (ResponseBody responseBody = response.body()) {
                    if (responseBody != null) {

                        try (FileOutputStream fos = new FileOutputStream(localFilePath)) {
                            fos.write(responseBody.bytes());
                        }

                        System.out.println("Photo saved to " + localFilePath);

                    } else {
                        System.out.println("Response body is empty.");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        thread.start();
    }
}