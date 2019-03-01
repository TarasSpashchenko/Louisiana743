package com.ts.louisiana.types.stub;

import com.ts.louisiana.types.ItemEntity;
import io.vertx.core.json.JsonObject;

public class ItemEntityStub extends ContextObjectStub implements ItemEntity {
    public ItemEntityStub() {
    }

    public ItemEntityStub(JsonObject entity) {
        super(entity);
    }
}
