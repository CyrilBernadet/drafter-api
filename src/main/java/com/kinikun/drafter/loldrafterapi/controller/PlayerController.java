package com.kinikun.drafter.loldrafterapi.controller;

import java.util.Optional;
import java.util.UUID;

import com.kinikun.drafter.loldrafterapi.dto.PlayerDto;
import com.kinikun.drafter.loldrafterapi.entity.PlayerEntity;
import com.kinikun.drafter.loldrafterapi.mapper.PlayerMapper;
import com.kinikun.drafter.loldrafterapi.repository.PlayerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlayerController {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerMapper playerMapper;

    @GetMapping("/players")
    public Iterable<PlayerEntity> findAllPlayers() {
        return this.playerRepository.findAll();
    }

    @GetMapping("/players/{id}")
    public ResponseEntity<PlayerDto> findPlayer(@PathVariable String id) {
        try {
            UUID uuid = UUID.fromString(id);
            Optional<PlayerEntity> player = this.playerRepository.findById(uuid);

            if (player.isPresent()) {
                return new ResponseEntity<>(playerMapper.entityToDto(player.get()), HttpStatus.FOUND);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/players")
    public ResponseEntity<Void> createPlayer(@RequestBody PlayerDto playerDto) {
        try {
            playerRepository.save(playerMapper.dtoToEntity(playerDto));

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
