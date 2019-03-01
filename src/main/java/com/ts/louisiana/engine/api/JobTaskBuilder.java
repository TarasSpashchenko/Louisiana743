package com.ts.louisiana.engine.api;

import com.linkedin.parseq.Task;
import com.ts.louisiana.metadata.Job;


public interface JobTaskBuilder<T> {

    Task<?> buildJobTask(Job job, T sourceRecordEntity);

}
