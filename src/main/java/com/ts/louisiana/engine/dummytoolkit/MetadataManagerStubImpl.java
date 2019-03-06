package com.ts.louisiana.engine.dummytoolkit;

import com.ts.louisiana.engine.api.MetadataManager;
import com.ts.louisiana.metadata.EntityDefinitionImpl;
import com.ts.louisiana.metadata.api.EntityDefinition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

import static com.ts.louisiana.engine.api.ActionHandlerSet.BIND_MASTER_TASK_NAME_ALIAS;
import static com.ts.louisiana.engine.api.ActionHandlerSet.BIND_TASK_NAME_ALIAS;
import static com.ts.louisiana.engine.api.ActionHandlerSet.CHECK_IN_CONTEXT_TASK_NAME_ALIAS;
import static com.ts.louisiana.engine.api.ActionHandlerSet.CHECK_MASTER_IN_CONTEXT_TASK_NAME_ALIAS;
import static com.ts.louisiana.engine.api.ActionHandlerSet.CREATE_TASK_NAME_ALIAS;
import static com.ts.louisiana.engine.api.ActionHandlerSet.MAP_TASK_NAME_ALIAS;
import static com.ts.louisiana.engine.api.ActionHandlerSet.RETRIEVE_FROM_CONTEXT_TASK_NAME_ALIAS;
import static com.ts.louisiana.engine.api.ActionHandlerSet.RETRIEVE_MASTER_FROM_CONTEXT_TASK_NAME_ALIAS;
import static com.ts.louisiana.engine.api.ActionHandlerSet.RETRIEVE_MASTER_FROM_REPOSITORY_TASK_NAME_ALIAS;
import static com.ts.louisiana.engine.api.ActionHandlerSet.RETRIEVE_MASTER_TASK_NAME_ALIAS;
import static com.ts.louisiana.engine.api.ActionHandlerSet.RETRIEVE_TASK_NAME_ALIAS;
import static com.ts.louisiana.engine.api.ActionHandlerSet.STORE_TASK_NAME_ALIAS;
import static com.ts.louisiana.engine.api.ActionHandlerSet.WALK_UP_THE_TREE_TASK_NAME_ALIAS;


//import org.springframework.beans.factory.DisposableBean;
//import org.springframework.beans.factory.InitializingBean;

@Component
@Profile("test")
@Slf4j
public class MetadataManagerStubImpl implements MetadataManager {

    private static final String MARC = "MARC";
    private static final String INSTANCE = "INSTANCE";
    private static final String HOLDINGS = "HOLDINGS";
    private static final String ITEM = "ITEM";
    private static final String ORDER = "ORDER";
    private static final String ORDER_LINE = "ORDER_LINE";
    private static final String MARC_CAT  = "MARC_CAT";

    private final Map<String, EntityDefinition> knownEntities = new HashMap<>();

    @Override
    public EntityDefinition getEntityDefinition(String entityType) {
        return knownEntities.get(entityType);
    }

    @PostConstruct
    private void init() {
        //TODO: should be built from some sort of configuration file

        Map<String, String> taskNames = new HashMap<>();
        taskNames.put(CREATE_TASK_NAME_ALIAS, "Create an Instance");
        taskNames.put(RETRIEVE_TASK_NAME_ALIAS, "Retrieve an Instance");
        taskNames.put(CHECK_IN_CONTEXT_TASK_NAME_ALIAS, "Check for an Instance in context");
        taskNames.put(RETRIEVE_FROM_CONTEXT_TASK_NAME_ALIAS, "Retrieve an Instance from context");
        taskNames.put(MAP_TASK_NAME_ALIAS, "Map data to the Instance");
        taskNames.put(STORE_TASK_NAME_ALIAS, "Store the Instance");
        taskNames.put(BIND_TASK_NAME_ALIAS, "Bind the Instance to context");

        taskNames.put(RETRIEVE_MASTER_TASK_NAME_ALIAS, "WARNING!!! Retrieve a master object");
        taskNames.put(CHECK_MASTER_IN_CONTEXT_TASK_NAME_ALIAS, "WARNING!!! Check a master in context");
        taskNames.put(RETRIEVE_MASTER_FROM_CONTEXT_TASK_NAME_ALIAS, "WARNING!!! Retrieve a master from context");
        taskNames.put(RETRIEVE_MASTER_FROM_REPOSITORY_TASK_NAME_ALIAS, "WARNING!!! Retrieve a master from repository");

        taskNames.put(WALK_UP_THE_TREE_TASK_NAME_ALIAS, "Walk up the tree...");

        knownEntities.put(INSTANCE, EntityDefinitionImpl.builder().entityType(INSTANCE).taskNames(taskNames).build());

        taskNames = new HashMap<>();
        taskNames.put(CREATE_TASK_NAME_ALIAS, "Create a Holdings");
        taskNames.put(RETRIEVE_TASK_NAME_ALIAS, "Retrieve a Holdings");
        taskNames.put(CHECK_IN_CONTEXT_TASK_NAME_ALIAS, "Check for a Holdings in context");
        taskNames.put(RETRIEVE_FROM_CONTEXT_TASK_NAME_ALIAS, "Retrieve a Holdings from context");
        taskNames.put(MAP_TASK_NAME_ALIAS, "Map data to the Holdings");
        taskNames.put(STORE_TASK_NAME_ALIAS, "Store the Holdings");
        taskNames.put(BIND_TASK_NAME_ALIAS, "Bind the Holdings to context");

        taskNames.put(RETRIEVE_MASTER_TASK_NAME_ALIAS, "Retrieve an Instance");
        taskNames.put(CHECK_MASTER_IN_CONTEXT_TASK_NAME_ALIAS, "Check for an Instance in context");
        taskNames.put(RETRIEVE_MASTER_FROM_CONTEXT_TASK_NAME_ALIAS, "Retrieve an Instance from context");
        taskNames.put(RETRIEVE_MASTER_FROM_REPOSITORY_TASK_NAME_ALIAS, "Retrieve an Instance from repository");
        taskNames.put(BIND_MASTER_TASK_NAME_ALIAS, "Bind the Instance to context");

        taskNames.put(WALK_UP_THE_TREE_TASK_NAME_ALIAS, "Walk up the tree...");

        knownEntities.put(HOLDINGS, EntityDefinitionImpl.builder().entityType(HOLDINGS).masterEntityType(INSTANCE).taskNames(taskNames).build());

        taskNames = new HashMap<>();
        taskNames.put(CREATE_TASK_NAME_ALIAS, "Create an Item");
        taskNames.put(RETRIEVE_TASK_NAME_ALIAS, "Retrieve an Item");
        taskNames.put(CHECK_IN_CONTEXT_TASK_NAME_ALIAS, "Check for an Item in context");
        taskNames.put(RETRIEVE_FROM_CONTEXT_TASK_NAME_ALIAS, "Retrieve an Item from context");
        taskNames.put(MAP_TASK_NAME_ALIAS, "Map data to the Item");
        taskNames.put(STORE_TASK_NAME_ALIAS, "Store the Item");
        taskNames.put(BIND_TASK_NAME_ALIAS, "Bind the Item to context");

        taskNames.put(RETRIEVE_MASTER_TASK_NAME_ALIAS, "Retrieve a Holdings");
        taskNames.put(CHECK_MASTER_IN_CONTEXT_TASK_NAME_ALIAS, "Check for a Holdings in context");
        taskNames.put(RETRIEVE_MASTER_FROM_CONTEXT_TASK_NAME_ALIAS, "Retrieve a Holdings from context");
        taskNames.put(RETRIEVE_MASTER_FROM_REPOSITORY_TASK_NAME_ALIAS, "Retrieve a Holdings from repository");
        taskNames.put(BIND_MASTER_TASK_NAME_ALIAS, "Bind the Holdings to context");

        taskNames.put(WALK_UP_THE_TREE_TASK_NAME_ALIAS, "Walk up the tree...");

        knownEntities.put(ITEM, EntityDefinitionImpl.builder().entityType(ITEM).masterEntityType(HOLDINGS).taskNames(taskNames).build());

        knownEntities.put(ORDER, EntityDefinitionImpl.builder().entityType(ORDER).masterEntityType(ITEM).build());

        taskNames = new HashMap<>();
        taskNames.put(CREATE_TASK_NAME_ALIAS, "Create an Order line");
        taskNames.put(RETRIEVE_TASK_NAME_ALIAS, "Retrieve an Order line");
        taskNames.put(CHECK_IN_CONTEXT_TASK_NAME_ALIAS, "Check for an Order line in context");
        taskNames.put(RETRIEVE_FROM_CONTEXT_TASK_NAME_ALIAS, "Retrieve an Order line from context");
        taskNames.put(MAP_TASK_NAME_ALIAS, "Map data to the Order line");
        taskNames.put(STORE_TASK_NAME_ALIAS, "Store the Order line");
        taskNames.put(BIND_TASK_NAME_ALIAS, "Bind the Item to Order line");

        taskNames.put(RETRIEVE_MASTER_TASK_NAME_ALIAS, "Retrieve an Item");
        taskNames.put(CHECK_MASTER_IN_CONTEXT_TASK_NAME_ALIAS, "Check for an Item in context");
        taskNames.put(RETRIEVE_MASTER_FROM_CONTEXT_TASK_NAME_ALIAS, "Retrieve an Item from context");
        taskNames.put(RETRIEVE_MASTER_FROM_REPOSITORY_TASK_NAME_ALIAS, "Retrieve an Item from repository");
        taskNames.put(BIND_MASTER_TASK_NAME_ALIAS, "Bind the Item to context");

        taskNames.put(WALK_UP_THE_TREE_TASK_NAME_ALIAS, "Walk up the tree...");

        knownEntities.put(ORDER_LINE, EntityDefinitionImpl.builder().entityType(ORDER_LINE).masterEntityType(ITEM).taskNames(taskNames).build());

        knownEntities.put(MARC_CAT, EntityDefinitionImpl.builder().entityType(MARC_CAT).build());

        knownEntities.put(MARC, EntityDefinitionImpl.builder().entityType(MARC).build());
    }
}
