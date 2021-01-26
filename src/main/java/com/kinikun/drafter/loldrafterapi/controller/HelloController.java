package com.kinikun.drafter.loldrafterapi.controller;

import java.util.UUID;

import com.kinikun.drafter.loldrafterapi.batch.entity.PlayerEntity;
import com.kinikun.drafter.loldrafterapi.batch.repository.PlayerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    private PlayerRepository playerRepository;

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @RequestMapping("/player")
    public Iterable<PlayerEntity> findAllPlayers() {
        return this.playerRepository.findAll();
    }
}
