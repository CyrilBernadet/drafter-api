package com.kinikun.drafter.loldrafterapi.batch.reader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.kinikun.drafter.loldrafterapi.dto.MatchDto;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Value;

public class MatchReader implements ItemReader<List<String>> {

    private final static String ENCRYPTED_ACCOUNT_ID = "RTnb5fk3udlHF-VBNQ_g61CWvy0bAbxJYnfO1Rz9A9qI8NQ";

    @Value("${api.key}")
    String apiKey;

    @Value("${api.url}")
    String apiURL;

    @Override
    public List<String> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        URL url = new URL(apiURL + ENCRYPTED_ACCOUNT_ID);

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
        
        JSONObject matchs = new JSONObject(content.toString());
        JSONArray matchsArray = matchs.getJSONArray("matches");

        List<String> result = new ArrayList<>();

        for (int i = 0; i< matchsArray.length(); i++) {
            JSONObject match = matchsArray.getJSONObject(i);
            result.add(match.getString("gameId"));
        }

        return result;
    }

}
