package com.ts.louisiana.engine.api;

import com.ts.louisiana.metadata.api.EntityDefinition;

public interface MetadataManager {
    String CREATE_TASK_NAME_ALIAS = "createTaskNameAlias";
    String RETRIEVE_TASK_NAME_ALIAS = "retrieveTaskNameAlias";
    String CHECK_IN_CONTEXT_TASK_NAME_ALIAS = "checkTaskAlias";
    String RETRIEVE_FROM_CONTEXT_TASK_NAME_ALIAS = "retrieveFromContextTaskNameAlias";
    String RETRIEVE_FROM_REPOSITORY_TASK_NAME_ALIAS = "retrieveFromRepositoryTaskNameAlias";
    String MAP_TASK_NAME_ALIAS = "mapTaskNameAlias";
    String STORE_TASK_NAME_ALIAS = "storeTaskNameAlias";
    String BIND_TASK_NAME_ALIAS = "bindTaskNameAlias";

    String RETRIEVE_MASTER_TASK_NAME_ALIAS = "retrieveMasterTaskAlias";
    String CHECK_MASTER_IN_CONTEXT_TASK_NAME_ALIAS = "checkMasterTaskAlias";
    String RETRIEVE_MASTER_FROM_CONTEXT_TASK_NAME_ALIAS = "retrieveMasterFromContextTaskNameAlias";
    String RETRIEVE_MASTER_FROM_REPOSITORY_TASK_NAME_ALIAS = "retrieveMasterFromRepositoryTaskNameAlias";
    String BIND_MASTER_TASK_NAME_ALIAS = "bindMasterTaskNameAlias";

    String WALK_UP_THE_TREE_TASK_NAME_ALIAS = "walkUpTheTreeTaskNameAlias";

    String MATCH_CRITERIA_TO_QUERY_CRITERIA_TASK_NAME_ALIAS = "matchCriteriaToQueryCriteriaTaskNameAlias";
    String MATCH_FORK_TASK_NAME_ALIAS = "matchForkTaskNameAlias";

    String UNDEFINED_TASK_NAME = "UNDEFINED TASK NAME";

    EntityDefinition getEntityDefinition(String entityType);
}
