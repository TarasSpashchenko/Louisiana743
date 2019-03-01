package com.ts.louisiana.engine.api;

import com.ts.louisiana.metadata.EntityType;

public interface MapperFactory {
    <P, R> Mapper<P, R> getMapper(EntityType from, EntityType to);
}
