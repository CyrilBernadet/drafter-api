package com.kinikun.drafter.loldrafterapi.batch.writer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.kinikun.drafter.loldrafterapi.dto.MatchDto;

import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.batch.item.ItemWriter;

public class MatchWriter implements ItemWriter<List<MatchDto>> {

    private RestHighLevelClient esClient;

    private static final String MATCH_INDEX = "matchs";

    public MatchWriter(RestHighLevelClient esClient) {
        this.esClient = esClient;
    }

    @Override
    public void write(List<? extends List<MatchDto>> items) throws Exception {
        // Les matchs arrivent sous forme d'une liste de liste (une liste de match par
        // joueur), il faut donc aplatir cette liste
        List<MatchDto> matchs = items.stream().flatMap(List::stream).collect(Collectors.toList());

        BulkRequest request = new BulkRequest(MATCH_INDEX);
        for (MatchDto matchDto : matchs) {

            Map<String, Object> matchMap = new HashMap<>();
            matchMap.put("gameId", matchDto.getGameId());
            matchMap.put("timestamp", matchDto.getTimestamp());
            matchMap.put("winningTeam", matchDto.getWinningTeam());
            matchMap.put("blueTeamBans", matchDto.getBlueTeamBans());
            matchMap.put("redTeamBans", matchDto.getRedTeamBans());
            matchMap.put("blueTeamPicks", matchDto.getBlueTeamPicks());
            matchMap.put("redTeamPicks", matchDto.getRedTeamPicks());

            request.add(new IndexRequest(MATCH_INDEX).source(matchMap, XContentType.JSON));
        }

        this.esClient.bulk(request, RequestOptions.DEFAULT);
    }

}
