package com.ts.louisiana.engine.api;

import com.ts.louisiana.metadata.api.Mapping;
import com.ts.louisiana.types.EntityObject;

public interface Mapper<T> {
    EntityObject<T> mapData(final Mapping mapping, final EntityObject<T> source, final EntityObject<T> destination, final JobExecutionContext<T> jobExecutionContext);
}
