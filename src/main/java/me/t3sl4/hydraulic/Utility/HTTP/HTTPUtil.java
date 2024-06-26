package me.t3sl4.hydraulic.Utility.HTTP;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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