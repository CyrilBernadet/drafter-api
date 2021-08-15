package com.kinikun.drafter.loldrafterapi.mapper;

import com.kinikun.drafter.loldrafterapi.dto.PlayerDto;
import com.kinikun.drafter.loldrafterapi.entity.PlayerEntity;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PlayerMapper {
    
    @Mapping(source = "id", target = "id")
    @Mapping(source = "summonerId", target = "summonerId")
    @Mapping(source = "accountId", target = "accountId")
    @Mapping(source = "lastGameProcessedTime", target = "lastGameProcessedTime")
    PlayerDto entityToDto(PlayerEntity entity);

    @InheritInverseConfiguration
    PlayerEntity dtoToEntity(PlayerDto dto);
}
