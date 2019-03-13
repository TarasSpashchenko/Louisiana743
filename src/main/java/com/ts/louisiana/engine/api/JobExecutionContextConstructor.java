package com.ts.louisiana.engine.api;

import com.ts.louisiana.types.EntityObject;

public interface JobExecutionContextConstructor {
    <T> JobExecutionContext<T> createJobExecutionContext(EntityObject<T> sourceRecordEntity);
}
