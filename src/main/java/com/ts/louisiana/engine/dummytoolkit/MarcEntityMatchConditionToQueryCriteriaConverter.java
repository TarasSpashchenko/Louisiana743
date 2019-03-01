package com.ts.louisiana.engine.dummytoolkit;

import com.ts.louisiana.engine.api.MatchConditionToQueryCriteriaConverter;
import com.ts.louisiana.engine.api.QueryCriteria;
import com.ts.louisiana.metadata.MatchCriteria;
import com.ts.louisiana.types.MarcEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
@Slf4j
public class MarcEntityMatchConditionToQueryCriteriaConverter implements MatchConditionToQueryCriteriaConverter<MarcEntity> {
    @Override
    public QueryCriteria convertMatchConditionToQueryCriteria(final MatchCriteria matchCriteria, final MarcEntity contextObject) {
        return new QueryCriteria() {
        };
    }
}
