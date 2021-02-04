package com.kinikun.drafter.loldrafterapi.types;

public enum TeamEnum {
    BLUE(100),
    RED(200);

    private int teamCode;

    private TeamEnum(int teamCode) {
        this.teamCode = teamCode;
    }

    public static final TeamEnum getByCode (int teamCode) {
        TeamEnum result = null;

        for (TeamEnum teamEnum : TeamEnum.values()) {
            if (teamCode == teamEnum.getTeamCode()) {
                result = teamEnum;
            }
        }

        return result;
    }

    public int getTeamCode() {
        return teamCode;
    }
}
