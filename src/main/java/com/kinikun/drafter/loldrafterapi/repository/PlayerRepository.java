package com.kinikun.drafter.loldrafterapi.repository;

import java.util.UUID;

import com.kinikun.drafter.loldrafterapi.entity.PlayerEntity;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends PagingAndSortingRepository<PlayerEntity, UUID> {

    @Query("SELECT case when count(p)> 0 then true else false end FROM PlayerEntity p WHERE p.summonerId = (:summonerId)")
    public boolean existsBySummonerId(@Param("summonerId") String summonerId);
}
