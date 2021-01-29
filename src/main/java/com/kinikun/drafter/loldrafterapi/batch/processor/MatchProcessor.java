package com.kinikun.drafter.loldrafterapi.batch.processor;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kinikun.drafter.loldrafterapi.dto.MatchDto;
import com.kinikun.drafter.loldrafterapi.entity.PlayerEntity;
import com.kinikun.drafter.loldrafterapi.repository.PlayerRepository;
import com.kinikun.drafter.loldrafterapi.utils.HttpUtils;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.batch.item.ItemProcessor;

public class MatchProcessor implements ItemProcessor<PlayerEntity, MatchDto> {

    private final static String API_ENDPOINT = "/lol/match/v4/matches/";

    private final static String API_MATCH_PLAYER_ENDPOINT = "/lol/match/v4/matchlists/by-account/";

    private PlayerRepository playerRepository;

    public MatchProcessor(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public MatchDto process(PlayerEntity player) throws Exception {
        List<JSONObject> result = new ArrayList<>();

        List<String> matchIds = this.getMatchIds(player);

        // for (String matchId : matchIds) {
        // JSONObject match = new JSONObject(HttpUtils.get(API_ENDPOINT + matchId));
        // result.add(match);
        // }

        // TODO: Coder un éventuel traitement des données (voir en fonction des besoin
        // des données à conserver)
        return null;
    }

    private List<String> getMatchIds(PlayerEntity player) throws JSONException, IOException {
        
        StringBuilder url = new StringBuilder();
        url.append(API_MATCH_PLAYER_ENDPOINT);
        url.append(player.getAccountId());

        if (player.getLastGameProcessedTime() != null) {
            url.append("?beginTime=");
            url.append(player.getLastGameProcessedTime().getTime());
        }

        JSONObject matchs = new JSONObject(HttpUtils.get(url.toString()));
        JSONArray matchsArray = matchs.getJSONArray("matches");
        List<String> result = new ArrayList<>();

        for (int i = 0; i < matchsArray.length(); i++) {
            JSONObject match = matchsArray.getJSONObject(i);
            player.setLastGameProcessedTime(new Timestamp(Long.valueOf(match.get("timestamp").toString())));
            result.add(match.getString("gameId"));
        }

        playerRepository.save(player);

        return result;
    }
}
