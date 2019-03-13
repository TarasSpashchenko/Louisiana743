package com.ts.louisiana.metadata;


import com.ts.louisiana.metadata.api.EntityDefinition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

import static com.ts.louisiana.engine.api.MetadataManager.UNDEFINED_TASK_NAME;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntityDefinitionImpl implements EntityDefinition {
    private String entityType;

    private String masterEntityType;

    private List<String> detailEntityTypes;

    private Map<String, String> taskNames;

    @Override
    public String getTaskName(String taskNameAlias) {
        String name = taskNames.get(taskNameAlias);
        return StringUtils.isNotBlank(name) ? name : UNDEFINED_TASK_NAME;

    }
}
