package com.kinikun.drafter.loldrafterapi.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "player")
public class PlayerEntity {

    @Id
    @Column(updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String summonerId;

    @Column(nullable = false)
    private String accountId;

    @Column(nullable = true)
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
