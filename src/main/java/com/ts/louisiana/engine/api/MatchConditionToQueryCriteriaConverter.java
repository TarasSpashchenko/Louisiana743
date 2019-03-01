package com.ts.louisiana.engine.api;

import com.ts.louisiana.metadata.MatchCriteria;

//???????
public interface MatchConditionToQueryCriteriaConverter<T> {
    QueryCriteria convertMatchConditionToQueryCriteria(final MatchCriteria matchCriteria, final T contextObject);
}
