package com.ts.louisiana.engine.api;

public interface MapperFactory<T> {
     Mapper<T> getMapper(String fromEntityType, String toEntityType);
}
