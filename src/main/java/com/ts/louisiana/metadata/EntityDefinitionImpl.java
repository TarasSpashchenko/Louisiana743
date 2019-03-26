package com.ts.louisiana.metadata;


import com.ts.louisiana.metadata.api.EntityDefinition;
import com.ts.louisiana.metadata.api.EntityOperation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.ts.louisiana.engine.api.MetadataManager.UNDEFINED_TASK_NAME;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntityDefinitionImpl implements EntityDefinition {
    private String entityType;

    private String masterEntityType;

    private String referenceToMasterFieldName;

    private List<String> detailEntityTypes;

    private Map<String, String> taskNames;

    private Map<String, EntityOperation> entityOperations;

    @Override
    public String getTaskName(String taskNameAlias) {
        if (Objects.isNull(taskNames)) {
            return UNDEFINED_TASK_NAME;
        }
        String name = taskNames.get(taskNameAlias);
        return StringUtils.isNotBlank(name) ? name : UNDEFINED_TASK_NAME;
    }

    @Override
    public EntityOperation getEntityOperation(String operationType) {
        if (Objects.isNull(entityOperations)) {
            return null;
        }
        return entityOperations.get(operationType);
    }
}
