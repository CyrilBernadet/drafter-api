package com.kinikun.drafter.loldrafterapi.dto;

import java.sql.Timestamp;

public class PlayerDto {

    private Long id;

    private String summonerId;

    private String accountId;

    private Timestamp lastGameProcessedTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSummonerId() {
        return summonerId;
    }

    public void setSummonerId(String summonerId) {
        this.summonerId = summonerId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Timestamp getLastGameProcessedTime() {
        return lastGameProcessedTime;
    }

    public void setLastGameProcessedTime(Timestamp lastGameProcessedTime) {
        this.lastGameProcessedTime = lastGameProcessedTime;
    }

}
