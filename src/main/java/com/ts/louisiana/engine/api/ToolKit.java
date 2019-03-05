package com.ts.louisiana.engine.api;

public interface ToolKit {
    JobHandler getJobHandler();

//    <P, R> MatchHandler<P, R> getMatchHandler(EntityType from, EntityType to);

    ActionHandlerSet getActionHandlerSet(String entityType);

}
