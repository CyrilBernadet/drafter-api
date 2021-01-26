package com.kinikun.drafter.loldrafterapi.batch.repository;

import java.util.UUID;

import com.kinikun.drafter.loldrafterapi.batch.entity.PlayerEntity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends CrudRepository<PlayerEntity, UUID>{
    
}
