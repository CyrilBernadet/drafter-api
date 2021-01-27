package com.kinikun.drafter.loldrafterapi.repository;

import java.util.UUID;

import com.kinikun.drafter.loldrafterapi.entity.PlayerEntity;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends PagingAndSortingRepository<PlayerEntity, UUID> {

}
