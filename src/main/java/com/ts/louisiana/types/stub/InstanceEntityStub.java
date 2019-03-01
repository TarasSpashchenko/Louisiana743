package com.ts.louisiana.types.stub;

import com.ts.louisiana.types.InstanceEntity;
import io.vertx.core.json.JsonObject;

public class InstanceEntityStub extends ContextObjectStub implements InstanceEntity {
    public InstanceEntityStub() {
    }

    public InstanceEntityStub(JsonObject entity) {
        super(entity);
    }
}
