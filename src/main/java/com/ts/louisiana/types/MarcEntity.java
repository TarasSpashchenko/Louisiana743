package com.ts.louisiana.types;

import com.ts.louisiana.metadata.EntityType;
import io.vertx.core.json.JsonObject;

public interface MarcEntity extends ContextObject<JsonObject> {
    default EntityType getEntityType() {
        return EntityType.MARC;
    }

}
