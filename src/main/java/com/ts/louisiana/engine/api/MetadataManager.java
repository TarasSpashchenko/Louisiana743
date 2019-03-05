package com.ts.louisiana.engine.api;

import com.ts.louisiana.metadata.api.EntityDefinition;

public interface MetadataManager {
    EntityDefinition getEntityDefinition(String entityType);
}
