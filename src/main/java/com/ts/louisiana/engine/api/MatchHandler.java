package com.ts.louisiana.engine.api;

import com.linkedin.parseq.Task;
import com.ts.louisiana.metadata.api.Match;

public interface MatchHandler<T> {
    Task<Void> match(final Match match,
                     final JobExecutionContext<T> jobExecutionContext,
                     Task<Void> onMatchActionSequence,
                     Task<Void> onNotMatchActionSequence);
}
