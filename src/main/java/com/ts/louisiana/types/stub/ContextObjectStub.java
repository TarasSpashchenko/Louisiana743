package com.ts.louisiana.types.stub;

import com.ts.louisiana.types.ContextObject;
import com.ts.louisiana.types.HoldingsEntity;
import com.ts.louisiana.types.InstanceEntity;
import com.ts.louisiana.types.ItemEntity;
import com.ts.louisiana.types.MarcEntity;
import io.vertx.core.json.JsonObject;

@Deprecated
abstract class ContextObjectStub implements ContextObject<JsonObject> {
    private final JsonObject entity;

    ContextObjectStub() {
        this.entity = new JsonObject();
    }

    ContextObjectStub(final JsonObject entity) {
        this.entity = entity;
    }

    @Override
    public JsonObject getEntity() {
        return entity;
    }
}
