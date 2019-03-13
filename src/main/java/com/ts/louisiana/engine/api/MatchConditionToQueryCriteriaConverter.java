package com.ts.louisiana.engine.api;

import com.ts.louisiana.metadata.api.Match;
import com.ts.louisiana.metadata.api.MatchCriteria;
import com.ts.louisiana.types.EntityObject;


public interface MatchConditionToQueryCriteriaConverter<T> {
    QueryCriteria convertMatchConditionToQueryCriteria(final Match match, final JobExecutionContext<T> jobExecutionContext);
}
