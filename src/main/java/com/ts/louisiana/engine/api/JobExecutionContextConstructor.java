package com.ts.louisiana.engine.api;

import com.ts.louisiana.metadata.EntityType;

public interface JobExecutionContextConstructor {
    <T> JobExecutionContext createJobExecutionContext(EntityType sourceEntityType, T sourceRecordEntity);

}
