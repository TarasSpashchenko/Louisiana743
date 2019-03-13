package com.ts.louisiana.engine.toolkit;

import com.linkedin.parseq.Task;
import com.ts.louisiana.engine.api.EntityRepository;
import com.ts.louisiana.engine.api.JobExecutionContext;
import com.ts.louisiana.engine.api.MatchConditionToQueryCriteriaConverter;
import com.ts.louisiana.engine.api.MatchHandler;
import com.ts.louisiana.engine.api.MetadataManager;
import com.ts.louisiana.metadata.api.EntityDefinition;
import com.ts.louisiana.metadata.api.Match;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.ts.louisiana.engine.api.MetadataManager.BIND_TASK_NAME_ALIAS;
import static com.ts.louisiana.engine.api.MetadataManager.MATCH_CRITERIA_TO_QUERY_CRITERIA_TASK_NAME_ALIAS;
import static com.ts.louisiana.engine.api.MetadataManager.MATCH_FORK_TASK_NAME_ALIAS;
import static com.ts.louisiana.engine.api.MetadataManager.RETRIEVE_FROM_REPOSITORY_TASK_NAME_ALIAS;

@Component
@Slf4j
public class SimpleMatchHandlerImpl<T> implements MatchHandler<JsonObject> {

    private MatchConditionToQueryCriteriaConverter<JsonObject> matchConditionToQueryCriteriaConverter;

    private MetadataManager metadataManager;

    private EntityRepository<JsonObject> entityRepository;

//    private Task<Boolean> processMatch(Match match, JobExecutionContext<JsonObject> jobExecutionContext) {
//        String matchEntityType = match.getMatchEntityType();
//        EntityDefinition entityDefinition = metadataManager.getEntityDefinition(matchEntityType);
//
//        return Task.callable(entityDefinition.getTaskName(MATCH_CRITERIA_TO_QUERY_CRITERIA_TASK_NAME_ALIAS),
//                () -> matchConditionToQueryCriteriaConverter.convertMatchConditionToQueryCriteria(match, jobExecutionContext))
//                .map(entityDefinition.getTaskName(RETRIEVE_FROM_REPOSITORY_TASK_NAME_ALIAS), queryCriteria -> entityRepository.findByQueryCriteria(matchEntityType, queryCriteria))
//                .map(entityDefinition.getTaskName(BIND_TASK_NAME_ALIAS), entityObject -> Objects.nonNull(jobExecutionContext.bindEntityObjectToContext(entityObject)));
//    }

    @Override
    public Task<Void> match(Match match, JobExecutionContext<JsonObject> jobExecutionContext, Task<Void> onMatchActionSequence, Task<Void> onNotMatchActionSequence) {
        String matchEntityType = match.getMatchEntityType();
        EntityDefinition entityDefinition = metadataManager.getEntityDefinition(matchEntityType);

        Task<Boolean> matchTask = Task.callable(entityDefinition.getTaskName(MATCH_CRITERIA_TO_QUERY_CRITERIA_TASK_NAME_ALIAS),
                () -> matchConditionToQueryCriteriaConverter.convertMatchConditionToQueryCriteria(match, jobExecutionContext))
                .map(entityDefinition.getTaskName(RETRIEVE_FROM_REPOSITORY_TASK_NAME_ALIAS), queryCriteria -> entityRepository.findByQueryCriteria(matchEntityType, queryCriteria))
                .map(entityDefinition.getTaskName(BIND_TASK_NAME_ALIAS), entityObject -> Objects.nonNull(jobExecutionContext.bindEntityObjectToContext(entityObject)));

        return matchTask.flatMap(entityDefinition.getTaskName(MATCH_FORK_TASK_NAME_ALIAS), matchFound -> Boolean.TRUE.equals(matchFound) ? onMatchActionSequence : onNotMatchActionSequence);
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
