package com.ts.louisiana.types.stub;

import com.ts.louisiana.types.EntityObject;
import io.vertx.core.json.JsonObject;


public class EntityObjectyImpl implements EntityObject<JsonObject> /* ClusterSerializable ???*/ {
    private JsonObject entity;
    private String entityType;

    public EntityObjectyImpl() {
        super();
    }

    public EntityObjectyImpl(String entityType) {
        this(entityType, new JsonObject());
    }

    public EntityObjectyImpl(String entityType, JsonObject entity) {
        this.entityType = entityType;;
        this.entity = entity;
    }

    @Override
    public String getEntityType() {
        return entityType;
    }

    @Override
    public JsonObject getEntity() {
        return entity;
    }
}
