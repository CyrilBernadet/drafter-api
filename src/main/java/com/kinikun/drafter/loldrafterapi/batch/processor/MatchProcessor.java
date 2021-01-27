package com.kinikun.drafter.loldrafterapi.batch.processor;

import java.util.List;

import com.kinikun.drafter.loldrafterapi.dto.MatchDto;

import org.springframework.batch.item.ItemProcessor;

public class MatchProcessor implements ItemProcessor<List<String>, MatchDto> {

    @Override
    public MatchDto process(List<String> item) throws Exception {
        // TODO: Coder un éventuel traitement des données (voir en fonction des besoin
        // des données à conserver)
        return null;
    }
}
