package com.ts.louisiana.metadata.api;

public interface EntityDefinition {
    String getEntityType();

    String getMasterEntityType();

    String getTaskName(String taskNameAlias);
}
