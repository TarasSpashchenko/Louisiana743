package com.ts.louisiana.engine.api;

import com.ts.louisiana.metadata.Mapping;

public interface Mapper<P, R> {
    R mapData(final Mapping mapping, final P source, final R destination, final JobExecutionContext jobExecutionContext);
}
