package com.kinikun.drafter.loldrafterapi.controller;

import java.util.Optional;
import java.util.UUID;

import com.kinikun.drafter.loldrafterapi.entity.PlayerEntity;
import com.kinikun.drafter.loldrafterapi.repository.PlayerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlayerController {

    @Autowired
    private PlayerRepository playerRepository;

    @RequestMapping("/players")
    public Iterable<PlayerEntity> findAllPlayers() {
        return this.playerRepository.findAll();
    }

    @RequestMapping("/players/{id}")
    public ResponseEntity<PlayerEntity> findPlayer(@PathVariable String id) {
        try {
            UUID uuid = UUID.fromString(id);
            Optional<PlayerEntity> player = this.playerRepository.findById(uuid);

            if (player.isPresent()) {
                return new ResponseEntity<>(player.get(), HttpStatus.FOUND);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
