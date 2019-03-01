package com.ts.louisiana.types;

import com.ts.louisiana.metadata.EntityType;

public interface ContextObject<T> {
    EntityType getEntityType();
    T getEntity();
}
