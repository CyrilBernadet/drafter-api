package com.kinikun.drafter.loldrafterapi.dto;

import java.util.List;

import com.kinikun.drafter.loldrafterapi.types.TeamEnum;

public class MatchDto {

    private long gameId;

    private long timestamp;

    private TeamEnum winningTeam;

    private List<String> blueTeamBans;

    private List<String> redTeamBans;

    private List<String> blueTeamPicks;

    private List<String> redTeamPicks;

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public TeamEnum getWinningTeam() {
        return winningTeam;
    }

    public void setWinningTeam(TeamEnum winningTeam) {
        this.winningTeam = winningTeam;
    }

    public List<String> getBlueTeamBans() {
        return blueTeamBans;
    }

    public void setBlueTeamBans(List<String> blueTeamBans) {
        this.blueTeamBans = blueTeamBans;
    }

    public List<String> getRedTeamBans() {
        return redTeamBans;
    }

    public void setRedTeamBans(List<String> redTeamBans) {
        this.redTeamBans = redTeamBans;
    }

    public List<String> getBlueTeamPicks() {
        return blueTeamPicks;
    }

    public void setBlueTeamPicks(List<String> blueTeamPicks) {
        this.blueTeamPicks = blueTeamPicks;
    }

    public List<String> getRedTeamPicks() {
        return redTeamPicks;
    }

    public void setRedTeamPicks(List<String> redTeamPicks) {
        this.redTeamPicks = redTeamPicks;
    }

    
}
