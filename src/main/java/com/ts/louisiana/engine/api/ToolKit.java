package com.ts.louisiana.engine.api;

import com.ts.louisiana.metadata.EntityType;

public interface ToolKit {
    JobHandler getJobHandler();

    <P, R> MatchHandler<P, R> getMatchHandler(EntityType from, EntityType to);

    ActionHandlerSet getActionHandlerSet(EntityType entityType);

}
