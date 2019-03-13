package com.ts.louisiana.engine.api;

import com.linkedin.parseq.Task;
import com.ts.louisiana.metadata.api.Job;
import com.ts.louisiana.types.EntityObject;


public interface JobTaskBuilder<T> {

    Task<?> buildJobTask(Job job, EntityObject<T> sourceRecordEntity);

}
