package com.kinikun.drafter.loldrafterapi.batch.processor;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.kinikun.drafter.loldrafterapi.dto.MatchDto;
import com.kinikun.drafter.loldrafterapi.entity.PlayerEntity;
import com.kinikun.drafter.loldrafterapi.repository.PlayerRepository;
import com.kinikun.drafter.loldrafterapi.types.TeamEnum;
import com.kinikun.drafter.loldrafterapi.utils.HttpUtils;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.batch.item.ItemProcessor;

public class MatchProcessor implements ItemProcessor<PlayerEntity, List<MatchDto>> {

    private String apiKey;

    private String apiURL;

    private static final String API_ENDPOINT = "/lol/match/v4/matches/";

    private static final String API_MATCH_PLAYER_ENDPOINT = "/lol/match/v4/matchlists/by-account/";

    private PlayerRepository playerRepository;

    public MatchProcessor(PlayerRepository playerRepository, String apiURL, String apiKey) {
        this.playerRepository = playerRepository;
        this.apiURL = apiURL;
        this.apiKey = apiKey;
    }

    @Override
    public List<MatchDto> process(PlayerEntity player) throws Exception {
        List<String> matchIds = this.getMatchIds(player);
        List<MatchDto> matchDtos = new ArrayList<>();

        // Pour le moment (tests) on limite le nombre de matchs récupérés à 10
        int counter = 0;

        for (int i = 0; i < matchIds.size() && counter < 10; i++, counter++) {
            String stringObject = HttpUtils.get(API_ENDPOINT + matchIds.get(i), apiURL, apiKey);

            if (stringObject != null) {
                JSONObject match = new JSONObject(stringObject);
                long matchTimestamp = match.getLong("gameCreation");

                matchDtos.add(createMatchDto(match));

                // On ajoute les participants qui n'existent pas déjà
                this.addPlayers(match.getJSONArray("participantIdentities"), matchTimestamp);
            }
        }

        return matchDtos;
    }

    private List<String> getMatchIds(PlayerEntity player) throws JSONException, IOException {

        StringBuilder url = new StringBuilder();
        url.append(API_MATCH_PLAYER_ENDPOINT);
        url.append(player.getAccountId());

        if (player.getLastGameProcessedTime() != null) {
            url.append("?beginTime=");
            url.append(player.getLastGameProcessedTime().getTime());
        }

        String stringObject = HttpUtils.get(url.toString(), apiURL, apiKey);
        List<String> result = new ArrayList<>();

        if (stringObject != null) {
            JSONObject matchs = new JSONObject(stringObject);
            JSONArray matchsArray = matchs.getJSONArray("matches");

            for (int i = 0; i < matchsArray.length(); i++) {
                JSONObject match = matchsArray.getJSONObject(i);
                player.setLastGameProcessedTime(new Timestamp(Long.valueOf(match.getString("timestamp"))));
                result.add(match.getString("gameId"));
            }

            playerRepository.save(player);
        }

        return result;
    }

    private List<String> getBans(JSONObject team) throws JSONException {

        JSONArray bans = team.getJSONArray("bans");
        List<String> bansList = new ArrayList<>();

        for (int banIndex = 0; banIndex < bans.length(); banIndex++) {
            JSONObject ban = bans.getJSONObject(banIndex);
            bansList.add(ban.getString("championId"));
        }

        return bansList;
    }

    private List<String> getPicks(JSONArray participants, int teamId) throws JSONException {

        List<String> picksList = new ArrayList<>();

        for (int participantsIndex = 0; participantsIndex < participants.length(); participantsIndex++) {
            JSONObject participant = participants.getJSONObject(participantsIndex);

            if (participant.getInt("teamId") == teamId) {
                picksList.add(participant.getString("championId"));
            }
        }

        return picksList;
    }

    private void addPlayers(JSONArray participants, long matchTimestamp) throws JSONException {

        for (int participantsIndex = 0; participantsIndex < participants.length(); participantsIndex++) {
            JSONObject participant = participants.getJSONObject(participantsIndex).getJSONObject("player");

            if (!this.playerRepository.existsBySummonerId(participant.getString("summonerId"))) {
                PlayerEntity playerEntity = new PlayerEntity();
                playerEntity.setAccountId(participant.getString("accountId"));
                playerEntity.setSummonerId(participant.getString("summonerId"));
                playerEntity.setLastGameProcessedTime(new Timestamp(matchTimestamp));

                playerRepository.save(playerEntity);
            }
        }
    }

    private MatchDto createMatchDto(JSONObject match) throws JSONException {
        long matchTimestamp = match.getLong("gameCreation");

        MatchDto matchDto = new MatchDto();
        matchDto.setGameId(match.getLong("gameId"));
        matchDto.setTimestamp(matchTimestamp);

        JSONArray teams = match.getJSONArray("teams");

        for (int teamIndex = 0; teamIndex < teams.length(); teamIndex++) {
            JSONObject team = teams.getJSONObject(teamIndex);
            int teamId = team.getInt("teamId");

            if ("Win".equals(team.get("win"))) {
                matchDto.setWinningTeam(TeamEnum.getByCode(teamId));
            }

            List<String> bansList = this.getBans(team);
            List<String> picksList = this.getPicks(match.getJSONArray("participants"), teamId);

            if (TeamEnum.BLUE.getTeamCode() == teamId) {
                matchDto.setBlueTeamBans(bansList);
                matchDto.setBlueTeamPicks(picksList);
            } else {
                matchDto.setRedTeamBans(bansList);
                matchDto.setRedTeamPicks(picksList);
            }
        }

        return matchDto;
    }
}
