package me.t3sl4.hydraulic.Utility.HTTPUtil;

import javafx.application.Platform;
import javafx.concurrent.Task;
import okhttp3.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class HTTPRequest {

    public interface RequestCallback {
        void onSuccess(String response) throws IOException;

        void onFailure();
    }

    public static void sendRequest(String url, String jsonBody, RequestCallback callback) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    URL urlObj = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setDoOutput(true);

                    try (OutputStream os = conn.getOutputStream()) {
                        byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                        os.write(input, 0, input.length);
                    }

                    int responseCode = conn.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        String response = HTTPUtil.readResponse(conn.getInputStream());
                        Platform.runLater(() -> {
                            try {
                                callback.onSuccess(response);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    } else {
                        Platform.runLater(callback::onFailure);
                    }
                } catch (IOException e) {
                    Platform.runLater(callback::onFailure);
                }

                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.start();
    }

    public static void sendRequestNormal(String url, RequestCallback callback) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    URL urlObj = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);

                    int responseCode = conn.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        String response = HTTPUtil.readResponse(conn.getInputStream());
                        Platform.runLater(() -> {
                            try {
                                callback.onSuccess(response);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    } else {
                        Platform.runLater(callback::onFailure);
                    }
                } catch (IOException e) {
                    Platform.runLater(callback::onFailure);
                }

                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.start();
    }

    public static void sendRequest4File(String url, String jsonBody, String localFilePath, RequestCallback callback) {
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
                        callback.onSuccess("Photo saved to " + localFilePath);
                    } else {
                        System.out.println("Response body is empty.");
                        callback.onFailure();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                callback.onFailure();
            }
        });

        thread.start();
    }

    public static void sendMultipartRequest(String url, String username, File file, RequestCallback callback) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    URL urlObj = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);

                    String boundary = "Boundary-" + System.currentTimeMillis();
                    conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

                    try (OutputStream os = conn.getOutputStream()) {
                        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8), true)) {
                            writer.append("--" + boundary).append("\r\n");
                            writer.append("Content-Disposition: form-data; name=\"username\"").append("\r\n");
                            writer.append("\r\n").append(username).append("\r\n");

                            writer.append("--" + boundary).append("\r\n");
                            writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"").append("\r\n");
                            writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(file.getName())).append("\r\n");
                            writer.append("Content-Transfer-Encoding: binary").append("\r\n");
                            writer.append("\r\n");
                            writer.flush();

                            Files.copy(file.toPath(), os);
                            os.flush();

                            writer.append("\r\n");
                            writer.append("--" + boundary + "--").append("\r\n");
                        }
                    }

                    int responseCode = conn.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        String response = HTTPUtil.readResponse(conn.getInputStream());
                        Platform.runLater(() -> {
                            try {
                                callback.onSuccess(response);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    } else {
                        Platform.runLater(callback::onFailure);
                    }
                } catch (IOException e) {
                    Platform.runLater(callback::onFailure);
                }

                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.start();
    }
}
