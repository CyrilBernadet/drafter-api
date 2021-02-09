package com.kinikun.drafter.loldrafterapi.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.stereotype.Component;

@Component
public final class HttpUtils {

    private HttpUtils() {

    }

    public static final String get(String endpoint, String apiURL, String apiKey) {
        try {
            URL url = new URL(apiURL + endpoint);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Riot-Token", apiKey);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            con.disconnect();
            return content.toString();
        } catch (IOException e) {
            return null;
        }
    }
}
