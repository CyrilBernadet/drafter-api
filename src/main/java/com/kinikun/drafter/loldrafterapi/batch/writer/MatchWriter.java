package com.kinikun.drafter.loldrafterapi.batch.writer;

import java.util.List;
import java.util.stream.Collectors;

import com.kinikun.drafter.loldrafterapi.dto.MatchDto;

import org.springframework.batch.item.ItemWriter;

public class MatchWriter implements ItemWriter<List<MatchDto>> {

    @Override
    public void write(List<? extends List<MatchDto>> items) throws Exception {
        // Les matchs arrivent sous forme d'une liste de liste (une liste de match par
        // joueur), il faut donc aplatir cette liste
        List<MatchDto> matchs = items.stream().flatMap(List::stream).collect(Collectors.toList());
        // TODO: Enregistrer les données dans ElasticSearch
    }

}
