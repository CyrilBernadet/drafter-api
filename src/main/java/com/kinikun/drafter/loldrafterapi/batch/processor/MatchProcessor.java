package com.kinikun.drafter.loldrafterapi.batch.processor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.kinikun.drafter.loldrafterapi.dto.MatchDto;
import com.kinikun.drafter.loldrafterapi.entity.PlayerEntity;
import com.kinikun.drafter.loldrafterapi.utils.HttpUtils;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.batch.item.ItemProcessor;

public class MatchProcessor implements ItemProcessor<PlayerEntity, MatchDto> {

    private final static String API_ENDPOINT = "/lol/match/v4/matches/";

    private final static String API_MATCH_PLAYER_ENDPOINT = "/lol/match/v4/matchlists/by-account/";

    @Override
    public MatchDto process(PlayerEntity player) throws Exception {

        List<JSONObject> result = new ArrayList<>();

        List<String> matchIds = this.getMatchIds(player.getAccountId());

        for (String matchId : matchIds) {
            JSONObject match = new JSONObject(HttpUtils.get(API_ENDPOINT + matchId));
            result.add(match);
        }

        // TODO: Coder un éventuel traitement des données (voir en fonction des besoin
        // des données à conserver)
        return null;
    }

    private List<String> getMatchIds(String encryptedAccountId) throws JSONException, IOException {
        JSONObject matchs = new JSONObject(HttpUtils.get(API_MATCH_PLAYER_ENDPOINT + encryptedAccountId));
        JSONArray matchsArray = matchs.getJSONArray("matches");
        List<String> result = new ArrayList<>();

        for (int i = 0; i < matchsArray.length(); i++) {
            JSONObject match = matchsArray.getJSONObject(i);
            result.add(match.getString("gameId"));
        }

        return result;
    }
}
