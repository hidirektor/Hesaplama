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

    public static String parseStringVal(String response, String field) {
        JSONObject jsonObject = new JSONObject(response);
        return jsonObject.getString(field);
    }
}