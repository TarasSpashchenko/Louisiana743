package com.ts.louisiana.engine.api;

import com.linkedin.parseq.Task;
import com.ts.louisiana.metadata.MatchCriteria;

public interface MatchHandler<P, R> {
    Task<R> match(final MatchCriteria matchCriteria, final P contextObject);
}
