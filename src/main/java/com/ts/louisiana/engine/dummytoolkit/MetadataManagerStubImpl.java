package com.ts.louisiana.engine.dummytoolkit;

import com.ts.louisiana.engine.api.MetadataManager;
import com.ts.louisiana.metadata.EntityDefinitionImpl;
import com.ts.louisiana.metadata.EntityOperationImpl;
import com.ts.louisiana.metadata.api.EntityDefinition;
import com.ts.louisiana.metadata.api.EntityOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;


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
    private static final String INVOICE = "INVOICE";
    //    private static final String ORDER_LINE = "ORDER_LINE";
    private static final String MARC_CAT = "MARC_CAT";

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
        taskNames.put(RETRIEVE_FROM_REPOSITORY_TASK_NAME_ALIAS, "Retrieve an Instance from repository");
        taskNames.put(MAP_TASK_NAME_ALIAS, "Map data to the Instance");
        taskNames.put(STORE_TASK_NAME_ALIAS, "Store the Instance");
        taskNames.put(BIND_TASK_NAME_ALIAS, "Bind the Instance to context");

        taskNames.put(RETRIEVE_MASTER_TASK_NAME_ALIAS, "WARNING!!! Retrieve a master object");
        taskNames.put(CHECK_MASTER_IN_CONTEXT_TASK_NAME_ALIAS, "WARNING!!! Check a master in context");
        taskNames.put(RETRIEVE_MASTER_FROM_CONTEXT_TASK_NAME_ALIAS, "WARNING!!! Retrieve a master from context");
        taskNames.put(RETRIEVE_MASTER_FROM_REPOSITORY_TASK_NAME_ALIAS, "WARNING!!! Retrieve a master from repository");

        taskNames.put(WALK_UP_THE_TREE_TASK_NAME_ALIAS, "Walk up the tree...");
        taskNames.put(MATCH_CRITERIA_TO_QUERY_CRITERIA_TASK_NAME_ALIAS, "Convert MatchCriteria to QueryCriteria");
        taskNames.put(MATCH_FORK_TASK_NAME_ALIAS, "Match Fork");

        Map<String, EntityOperation> entityOperations = new HashMap<>();
        entityOperations.put("RETRIEVE", EntityOperationImpl.builder()
                .operationType("RETRIEVE")
                .verb(EntityOperation.Verb.GET)
                .uri("/inventory/instances/{ID}")
                .build());

        entityOperations.put("CREATE", EntityOperationImpl.builder()
                .operationType("CREATE")
                .verb(EntityOperation.Verb.POST)
                .uri("/inventory/instances")
                .build());

        entityOperations.put("UPDATE", EntityOperationImpl.builder()
                .operationType("UPDATE")
                .verb(EntityOperation.Verb.PUT)
                .uri("/inventory/instances/{ID}")
                .build());

        entityOperations.put("DELETE", EntityOperationImpl.builder()
                .operationType("DELETE")
                .verb(EntityOperation.Verb.DELETE)
                .uri("/inventory/instances/{ID}")
                .build());

        knownEntities.put(INSTANCE, EntityDefinitionImpl.builder().entityType(INSTANCE).taskNames(taskNames).entityOperations(entityOperations).build());

        taskNames = new HashMap<>();
        taskNames.put(CREATE_TASK_NAME_ALIAS, "Create a Holdings");
        taskNames.put(RETRIEVE_TASK_NAME_ALIAS, "Retrieve a Holdings");
        taskNames.put(CHECK_IN_CONTEXT_TASK_NAME_ALIAS, "Check for a Holdings in context");
        taskNames.put(RETRIEVE_FROM_CONTEXT_TASK_NAME_ALIAS, "Retrieve a Holdings from context");
        taskNames.put(RETRIEVE_FROM_REPOSITORY_TASK_NAME_ALIAS, "Retrieve a Holdings from repository");
        taskNames.put(MAP_TASK_NAME_ALIAS, "Map data to the Holdings");
        taskNames.put(STORE_TASK_NAME_ALIAS, "Store the Holdings");
        taskNames.put(BIND_TASK_NAME_ALIAS, "Bind the Holdings to context");

        taskNames.put(RETRIEVE_MASTER_TASK_NAME_ALIAS, "Retrieve an Instance");
        taskNames.put(CHECK_MASTER_IN_CONTEXT_TASK_NAME_ALIAS, "Check for an Instance in context");
        taskNames.put(RETRIEVE_MASTER_FROM_CONTEXT_TASK_NAME_ALIAS, "Retrieve an Instance from context");
        taskNames.put(RETRIEVE_MASTER_FROM_REPOSITORY_TASK_NAME_ALIAS, "Retrieve an Instance from repository");
        taskNames.put(BIND_MASTER_TASK_NAME_ALIAS, "Bind the Instance to context");

        taskNames.put(WALK_UP_THE_TREE_TASK_NAME_ALIAS, "Walk up the tree...");
        taskNames.put(MATCH_CRITERIA_TO_QUERY_CRITERIA_TASK_NAME_ALIAS, "Convert MatchCriteria to QueryCriteria");
        taskNames.put(MATCH_FORK_TASK_NAME_ALIAS, "Match Fork");

        knownEntities.put(HOLDINGS, EntityDefinitionImpl.builder().entityType(HOLDINGS).masterEntityType(INSTANCE).taskNames(taskNames).build());

        taskNames = new HashMap<>();
        taskNames.put(CREATE_TASK_NAME_ALIAS, "Create an Item");
        taskNames.put(RETRIEVE_TASK_NAME_ALIAS, "Retrieve an Item");
        taskNames.put(CHECK_IN_CONTEXT_TASK_NAME_ALIAS, "Check for an Item in context");
        taskNames.put(RETRIEVE_FROM_CONTEXT_TASK_NAME_ALIAS, "Retrieve an Item from context");
        taskNames.put(RETRIEVE_FROM_REPOSITORY_TASK_NAME_ALIAS, "Retrieve an Item from repository");
        taskNames.put(MAP_TASK_NAME_ALIAS, "Map data to the Item");
        taskNames.put(STORE_TASK_NAME_ALIAS, "Store the Item");
        taskNames.put(BIND_TASK_NAME_ALIAS, "Bind the Item to context");

        taskNames.put(RETRIEVE_MASTER_TASK_NAME_ALIAS, "Retrieve a Holdings");
        taskNames.put(CHECK_MASTER_IN_CONTEXT_TASK_NAME_ALIAS, "Check for a Holdings in context");
        taskNames.put(RETRIEVE_MASTER_FROM_CONTEXT_TASK_NAME_ALIAS, "Retrieve a Holdings from context");
        taskNames.put(RETRIEVE_MASTER_FROM_REPOSITORY_TASK_NAME_ALIAS, "Retrieve a Holdings from repository");
        taskNames.put(BIND_MASTER_TASK_NAME_ALIAS, "Bind the Holdings to context");

        taskNames.put(WALK_UP_THE_TREE_TASK_NAME_ALIAS, "Walk up the tree...");
        taskNames.put(MATCH_CRITERIA_TO_QUERY_CRITERIA_TASK_NAME_ALIAS, "Convert MatchCriteria to QueryCriteria");
        taskNames.put(MATCH_FORK_TASK_NAME_ALIAS, "Match Fork");

        knownEntities.put(ITEM, EntityDefinitionImpl.builder().entityType(ITEM).masterEntityType(HOLDINGS).taskNames(taskNames).build());

        knownEntities.put(ORDER, EntityDefinitionImpl.builder().entityType(ORDER).masterEntityType(ITEM).build());

        taskNames = new HashMap<>();
        taskNames.put(CREATE_TASK_NAME_ALIAS, "Create an Order line");
        taskNames.put(RETRIEVE_TASK_NAME_ALIAS, "Retrieve an Order line");
        taskNames.put(CHECK_IN_CONTEXT_TASK_NAME_ALIAS, "Check for an Order line in context");
        taskNames.put(RETRIEVE_FROM_CONTEXT_TASK_NAME_ALIAS, "Retrieve an Order line from context");
        taskNames.put(RETRIEVE_FROM_REPOSITORY_TASK_NAME_ALIAS, "Retrieve an Order line from repository");
        taskNames.put(MAP_TASK_NAME_ALIAS, "Map data to the Order line");
        taskNames.put(STORE_TASK_NAME_ALIAS, "Store the Order line");
        taskNames.put(BIND_TASK_NAME_ALIAS, "Bind the Item to Order line");

        taskNames.put(RETRIEVE_MASTER_TASK_NAME_ALIAS, "Retrieve an Item");
        taskNames.put(CHECK_MASTER_IN_CONTEXT_TASK_NAME_ALIAS, "Check for an Item in context");
        taskNames.put(RETRIEVE_MASTER_FROM_CONTEXT_TASK_NAME_ALIAS, "Retrieve an Item from context");
        taskNames.put(RETRIEVE_MASTER_FROM_REPOSITORY_TASK_NAME_ALIAS, "Retrieve an Item from repository");
        taskNames.put(BIND_MASTER_TASK_NAME_ALIAS, "Bind the Item to context");

        taskNames.put(WALK_UP_THE_TREE_TASK_NAME_ALIAS, "Walk up the tree...");
        taskNames.put(MATCH_CRITERIA_TO_QUERY_CRITERIA_TASK_NAME_ALIAS, "Convert MatchCriteria to QueryCriteria");
        taskNames.put(MATCH_FORK_TASK_NAME_ALIAS, "Match Fork");

        knownEntities.put(ORDER_LINE, EntityDefinitionImpl.builder().entityType(ORDER_LINE).masterEntityType(ITEM).taskNames(taskNames).build());

        taskNames = new HashMap<>();
        taskNames.put(CREATE_TASK_NAME_ALIAS, "Create a MARCCat record");
        taskNames.put(RETRIEVE_TASK_NAME_ALIAS, "Retrieve a MARCCat record");
        taskNames.put(CHECK_IN_CONTEXT_TASK_NAME_ALIAS, "Check for a MARCCat record in context");
        taskNames.put(RETRIEVE_FROM_CONTEXT_TASK_NAME_ALIAS, "Retrieve a MARCCat record from context");
        taskNames.put(RETRIEVE_FROM_REPOSITORY_TASK_NAME_ALIAS, "Retrieve a MARCCat record from repository");
        taskNames.put(MAP_TASK_NAME_ALIAS, "Map data to the MARCCat record");
        taskNames.put(STORE_TASK_NAME_ALIAS, "Store the MARCCat record");
        taskNames.put(BIND_TASK_NAME_ALIAS, "Bind the MARCCat record line");
        knownEntities.put(MARC_CAT, EntityDefinitionImpl.builder().entityType(MARC_CAT).taskNames(taskNames).build());

        taskNames = new HashMap<>();
        taskNames.put(CREATE_TASK_NAME_ALIAS, "Create an Invoice");
        taskNames.put(RETRIEVE_TASK_NAME_ALIAS, "Retrieve an Invoice");
        taskNames.put(CHECK_IN_CONTEXT_TASK_NAME_ALIAS, "Check for an Invoice in context");
        taskNames.put(RETRIEVE_FROM_CONTEXT_TASK_NAME_ALIAS, "Retrieve an Invoice from context");
        taskNames.put(RETRIEVE_FROM_REPOSITORY_TASK_NAME_ALIAS, "Retrieve an Invoice from repository");
        taskNames.put(MAP_TASK_NAME_ALIAS, "Map data to the Invoice");
        taskNames.put(STORE_TASK_NAME_ALIAS, "Store the Invoice");
        taskNames.put(BIND_TASK_NAME_ALIAS, "Bind the Invoice to context");
        knownEntities.put(INVOICE, EntityDefinitionImpl.builder().entityType(INVOICE).taskNames(taskNames).build());

        knownEntities.put(MARC, EntityDefinitionImpl.builder().entityType(MARC).build());

//        knownEntities.put("MY_NEW_ENTITY", EntityDefinitionImpl.builder().entityType("MY_NEW_ENTITY").build());
    }
}
