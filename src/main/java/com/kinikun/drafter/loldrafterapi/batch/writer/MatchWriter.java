package com.kinikun.drafter.loldrafterapi.batch.writer;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.kinikun.drafter.loldrafterapi.dto.MatchDto;

import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.springframework.batch.item.ItemWriter;
import org.springframework.util.CollectionUtils;

public class MatchWriter implements ItemWriter<List<MatchDto>> {

    private RestHighLevelClient esClient;

    private static final String MATCH_INDEX = "matchs";

    public MatchWriter(RestHighLevelClient esClient) {
        this.esClient = esClient;
    }

    @Override
    public void write(List<? extends List<MatchDto>> items) throws Exception {

        this.purgeMatchs();

        // Les matchs arrivent sous forme d'une liste de listes (une liste de match par
        // joueur), il faut donc aplatir cette liste
        List<MatchDto> matchs = items.stream().flatMap(List::stream).collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(matchs)) {
            BulkRequest request = new BulkRequest(MATCH_INDEX);
            boolean hasMatchBeenAdded = false;
            for (MatchDto matchDto : matchs) {
                if (!checkIfMatchAlreadyExists(matchDto.getGameId())) {
                    Map<String, Object> matchMap = new HashMap<>();
                    matchMap.put("gameId", matchDto.getGameId());
                    matchMap.put("timestamp", matchDto.getTimestamp());
                    matchMap.put("winningTeam", matchDto.getWinningTeam());
                    matchMap.put("blueTeamBans", matchDto.getBlueTeamBans());
                    matchMap.put("redTeamBans", matchDto.getRedTeamBans());
                    matchMap.put("blueTeamPicks", matchDto.getBlueTeamPicks());
                    matchMap.put("redTeamPicks", matchDto.getRedTeamPicks());

                    request.add(new IndexRequest(MATCH_INDEX).source(matchMap, XContentType.JSON));
                    request.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
                    hasMatchBeenAdded = true;
                }
            }

            if (hasMatchBeenAdded) {
                this.esClient.bulk(request, RequestOptions.DEFAULT);
            }
        }
    }

    private void purgeMatchs() throws IOException {
        // Suppression de tous les matchs ayant plus d'un mois
        DeleteByQueryRequest request = new DeleteByQueryRequest(MATCH_INDEX);
        RangeQueryBuilder queryBuilder = new RangeQueryBuilder("timestamp");

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);

        queryBuilder.to(cal.getTimeInMillis());
        request.setQuery(queryBuilder);

        this.esClient.deleteByQuery(request, RequestOptions.DEFAULT);
    }

    private boolean checkIfMatchAlreadyExists(long gameId) throws IOException {
        GetIndexRequest getIndexRequest = new GetIndexRequest(MATCH_INDEX);
        boolean exists = this.esClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);

        if (!exists) {
            return false;
        }

        CountRequest countRequest = new CountRequest(MATCH_INDEX);
        countRequest.query(QueryBuilders.matchQuery("gameId", gameId));
        CountResponse countResponse = this.esClient.count(countRequest, RequestOptions.DEFAULT);
        return countResponse.getCount() != 0;
    }

}
