package com.ts.louisiana.engine;

import com.ts.louisiana.engine.api.JobExecutionContext;
import com.ts.louisiana.engine.api.JobExecutionContextConstructor;
import com.ts.louisiana.metadata.EntityType;
import com.ts.louisiana.types.MarcEntity;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JobExecutionContextConstructorImpl implements JobExecutionContextConstructor {
    @Override
    public <T> JobExecutionContext createJobExecutionContext(EntityType sourceEntityType, T sourceRecordEntity) {
        return new JobExecutionContextImpl(sourceEntityType, sourceRecordEntity);
    }
}
