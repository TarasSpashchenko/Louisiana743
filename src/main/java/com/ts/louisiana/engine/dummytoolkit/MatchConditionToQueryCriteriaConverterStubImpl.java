package com.ts.louisiana.engine.dummytoolkit;

import com.ts.louisiana.engine.api.JobExecutionContext;
import com.ts.louisiana.engine.api.MatchConditionToQueryCriteriaConverter;
import com.ts.louisiana.engine.api.QueryCriteria;
import com.ts.louisiana.metadata.api.Match;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
@Slf4j
public class MatchConditionToQueryCriteriaConverterStubImpl implements MatchConditionToQueryCriteriaConverter<JsonObject> {
    @Override
    public QueryCriteria convertMatchConditionToQueryCriteria(Match match, JobExecutionContext<JsonObject> jobExecutionContext) {
        return new QueryCriteria() {};
    }
}
