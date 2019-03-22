package com.ts.louisiana.metadata.api;

public interface EntityDefinition {
    String getEntityType();

    String getMasterEntityType();

    String getReferenceToMasterFieldName();

    String getTaskName(String taskNameAlias);

    EntityOperation getEntityOperation(String operationType);
}
