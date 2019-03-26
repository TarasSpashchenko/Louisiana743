package com.ts.louisiana.engine;

import com.ts.louisiana.engine.api.JobExecutionContext;
import com.ts.louisiana.types.EntityObject;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
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
    public EntityObject<T> bindEntityObjectToContext(EntityObject<T> entityObject) {
        lastBoundEntityObject = entityObject;
        if (Objects.nonNull(lastBoundEntityObject)) {
            contextStorage.put(entityObject.getEntityType(), entityObject);
            log.info("\n\n{} has been bound to the context.\n", entityObject.getEntityType());
        } else {
            log.info("\n\nEntity is null and has not been bound to the context.\n");
        }

        return lastBoundEntityObject;
    }

    @Override
    public EntityObject<T> getBoundEntityObject(String entityType) {
        EntityObject<T> entityObject = contextStorage.get(entityType);
        log.info("\n\nGet bound {}. Entity {} been found in the context.\n", entityType, Objects.nonNull(entityObject) ? "has" : "has not");
        return entityObject;
    }

    @Override
    public EntityObject<T> getLastBoundEntityObject() {
        log.info("\n\nGet the last bound entity. Entity is {}.\n", Objects.nonNull(lastBoundEntityObject) ? lastBoundEntityObject.getEntityType() : "null");
        return lastBoundEntityObject;
    }

}
