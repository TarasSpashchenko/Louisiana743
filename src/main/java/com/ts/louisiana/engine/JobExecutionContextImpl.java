package com.ts.louisiana.engine;

import com.ts.louisiana.engine.api.JobExecutionContext;
import com.ts.louisiana.types.EntityObject;
import io.vertx.core.json.JsonObject;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class JobExecutionContextImpl<T> implements JobExecutionContext<T> {
    private final EntityObject<T> sourceEntityObject;
    private final ConcurrentMap<String, EntityObject<T>> contextStorage = new ConcurrentHashMap<>();

    private volatile EntityObject<T> lastBoundEntityObject;

    public JobExecutionContextImpl(EntityObject<T> sourceEntityObject) {
        super();
        this.sourceEntityObject = sourceEntityObject;
    }

    @Override
    public String getSourceEntityType() {
        return sourceEntityObject.getEntityType();
    }

    @Override
    public EntityObject<T> getSourceEntity() {
        return sourceEntityObject;
    }

    @Override
    public void bindEntityObjectToContext(EntityObject<T> entityObject) {
        contextStorage.put(entityObject.getEntityType(), entityObject);
        lastBoundEntityObject = entityObject;
    }

    @Override
    public EntityObject<T> getBoundEntityObject(String entityType) {
        return contextStorage.get(entityType);
    }

    @Override
    public EntityObject<T> getLastBoundEntityObject() {
        return lastBoundEntityObject;
    }

}
