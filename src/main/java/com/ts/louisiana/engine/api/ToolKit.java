package com.ts.louisiana.engine.api;

public interface ToolKit<T> {
    JobHandler getJobHandler();

    MatchHandler<T> getMatchHandler(String entityType);

    ActionHandlerSet<T> getActionHandlerSet(String entityType);

}
