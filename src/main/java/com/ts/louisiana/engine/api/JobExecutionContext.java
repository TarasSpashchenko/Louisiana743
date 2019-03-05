package com.ts.louisiana.engine.api;

import com.ts.louisiana.types.EntityObject;

public interface JobExecutionContext<T> {
    String getSourceEntityType();

    EntityObject<T>  getSourceEntity();

    void bindEntityObjectToContext(EntityObject<T> entityObject);

    EntityObject<T> getBoundEntityObject(String entityType);

    EntityObject<T> getLastBoundEntityObject();
}
