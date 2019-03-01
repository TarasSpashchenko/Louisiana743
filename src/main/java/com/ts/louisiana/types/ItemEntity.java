package com.ts.louisiana.types;

import com.ts.louisiana.metadata.EntityType;
import io.vertx.core.json.JsonObject;

public interface ItemEntity extends ContextObject<JsonObject> {
    default EntityType getEntityType() {
        return EntityType.ITEM;
    }

}
