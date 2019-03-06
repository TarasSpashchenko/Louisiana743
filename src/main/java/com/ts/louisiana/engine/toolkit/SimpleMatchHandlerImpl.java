package com.ts.louisiana.engine.toolkit;

import com.linkedin.parseq.Task;
import com.ts.louisiana.engine.api.EntityRepository;
import com.ts.louisiana.engine.api.JobExecutionContext;
import com.ts.louisiana.engine.api.MatchConditionToQueryCriteriaConverter;
import com.ts.louisiana.engine.api.MatchHandler;
import com.ts.louisiana.engine.api.MetadataManager;
import com.ts.louisiana.metadata.api.Match;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class SimpleMatchHandlerImpl<T> implements MatchHandler<JsonObject> {

    private MatchConditionToQueryCriteriaConverter<JsonObject> matchConditionToQueryCriteriaConverter;

    private MetadataManager metadataManager;

    private EntityRepository<JsonObject> entityRepository;

    private Task<Boolean> processMatch(Match match, JobExecutionContext<JsonObject> jobExecutionContext) {
        return Task.callable("TODO: Convert MatchCriteria to QueryCriteria", () -> matchConditionToQueryCriteriaConverter.convertMatchConditionToQueryCriteria(match, jobExecutionContext))
                .map("TODO: Retrieve Entity from Repository", queryCriteria -> entityRepository.findByQueryCriteria(match.getMatchEntityType(), queryCriteria))
                .map("TODO: Bind Entity to Context", entityObject -> Objects.nonNull(jobExecutionContext.bindEntityObjectToContext(entityObject)));
    }


    @Override
    public Task<Void> match(Match match, JobExecutionContext<JsonObject> jobExecutionContext, Task<Void> onMatchActionSequence, Task<Void> onNotMatchActionSequence) {
        return  processMatch(match, jobExecutionContext)
                .flatMap("TODO: Match Fork", matchFound -> Boolean.TRUE.equals(matchFound) ? onMatchActionSequence : onNotMatchActionSequence);
    }

    @Autowired
    public void setMatchConditionToQueryCriteriaConverter(MatchConditionToQueryCriteriaConverter<JsonObject> matchConditionToQueryCriteriaConverter) {
        this.matchConditionToQueryCriteriaConverter = matchConditionToQueryCriteriaConverter;
    }

    @Autowired
    public void setMetadataManager(MetadataManager metadataManager) {
        this.metadataManager = metadataManager;
    }

    @Autowired
    public void setEntityRepository(EntityRepository<JsonObject> entityRepository) {
        this.entityRepository = entityRepository;
    }


}
