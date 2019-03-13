package com.ts.louisiana.engine.api;

import com.ts.louisiana.types.EntityObject;
import io.vertx.core.json.JsonObject;

public interface EntityRepository<T> {
    EntityObject<T> findByQueryCriteria(String requiredEntityType, QueryCriteria queryCriteria);
    EntityObject<T> findByEntityObject(String requiredEntityType, EntityObject<T> byEntityObject);

    EntityObject<T> createEntityObject(String entityType);
    EntityObject<T> createEntityObject(String entityType, T entity);

    EntityObject<T> storeEntity(EntityObject<T> entityObject);
}
