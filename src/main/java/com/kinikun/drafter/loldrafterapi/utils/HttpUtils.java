package com.kinikun.drafter.loldrafterapi.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public final class HttpUtils {

    @Value("${api.key}")
    private String apiKey;

    @Value("${api.url}")
    private static String apiURL;

    private static String API_KEY_STATIC;

    private static String API_URL_STATIC;

    private HttpUtils() {

    }

    public static final String get(String endpoint) throws IOException {
        URL url = new URL(API_URL_STATIC + endpoint);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("X-Riot-Token", API_KEY_STATIC);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        con.disconnect();
        return content.toString();
    }

    @Value("${api.key}")
    public void setApiKey(String apiKey) {
        HttpUtils.API_KEY_STATIC = apiKey;
    }

    @Value("${api.url}")
    public void setApiURL(String apiURL) {
        HttpUtils.API_URL_STATIC = apiURL;
    }
}
