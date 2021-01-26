package com.kinikun.drafter.loldrafterapi.batch.writer;

import java.util.List;

import com.kinikun.drafter.loldrafterapi.dto.MatchDto;

import org.springframework.batch.item.ItemWriter;

public class MatchWriter implements ItemWriter<MatchDto> {

    @Override
    public void write(List<? extends MatchDto> matchs) throws Exception {
        // TODO: Enregistrer les données dans ElasticSearch
    }

}
