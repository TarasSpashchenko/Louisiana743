package com.ts.louisiana.engine.dummytoolkit;

import com.ts.louisiana.engine.api.EntityRepository;
import com.ts.louisiana.engine.api.QueryCriteria;
import com.ts.louisiana.types.EntityObject;
import com.ts.louisiana.types.stub.EntityObjectyImpl;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Repository
@Profile("test")
@Slf4j
public class EntityRepositoryStubImpl implements EntityRepository<JsonObject> {
    private final Map<Object, EntityObject<?>> dummyCache = new HashMap<>();

    @Override
    public EntityObject<JsonObject> findByQueryCriteria(String requiredEntityType, QueryCriteria queryCriteria) {
        if (Boolean.TRUE.equals(Boolean.valueOf(queryCriteria.getFallbackResolution()))) {
            log.info("\n\n{} has been retrieved.\n", requiredEntityType);
            return new EntityObjectyImpl(requiredEntityType);
        }
        log.info("\n\n{} has not been found.\n", requiredEntityType);
        return null;
    }

    @Override
    public EntityObject<JsonObject> findByEntityObject(String requiredEntityType, EntityObject<JsonObject> byEntityObject) {
        log.info("\n\n{} has been retrieved by {}.\n", requiredEntityType, byEntityObject.getEntityType());
        return new EntityObjectyImpl(requiredEntityType);
    }

    @Override
    public EntityObject<JsonObject> createEntityObject(String entityType) {
        log.info("\n\n{} has been created.\n", entityType);
        return new EntityObjectyImpl(entityType);
    }

    @Override
    public EntityObject<JsonObject> createEntityObject(String entityType, JsonObject entity) {
        log.info("\n\n{} has been created.\n", entityType);
        return new EntityObjectyImpl(entityType, entity);
    }

    @Override
    public EntityObject<JsonObject> storeEntity(EntityObject<JsonObject> entityObject) {
        log.info("\n\n{} has been stored.\n", entityObject.getEntityType());
        return entityObject;
    }
}
