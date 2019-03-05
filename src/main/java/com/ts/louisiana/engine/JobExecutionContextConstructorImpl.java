package com.ts.louisiana.engine;

import com.ts.louisiana.engine.api.JobExecutionContext;
import com.ts.louisiana.engine.api.JobExecutionContextConstructor;
import com.ts.louisiana.types.EntityObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JobExecutionContextConstructorImpl implements JobExecutionContextConstructor {
    @Override
    public <T> JobExecutionContext<T> createJobExecutionContext(EntityObject<T> sourceRecordEntity) {
        return new JobExecutionContextImpl<>(sourceRecordEntity);
    }
}
