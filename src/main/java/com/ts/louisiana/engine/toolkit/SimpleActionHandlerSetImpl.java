package com.ts.louisiana.engine.toolkit;

import com.linkedin.parseq.Task;
import com.ts.louisiana.engine.api.ActionHandlerSet;
import com.ts.louisiana.engine.api.EntityRepository;
import com.ts.louisiana.engine.api.JobExecutionContext;
import com.ts.louisiana.engine.api.Mapper;
import com.ts.louisiana.engine.api.MapperFactory;
import com.ts.louisiana.engine.api.MetadataManager;
import com.ts.louisiana.metadata.api.Action;
import com.ts.louisiana.metadata.api.ActionType;
import com.ts.louisiana.metadata.api.EntityDefinition;
import com.ts.louisiana.types.EntityObject;
import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.ts.louisiana.engine.api.MetadataManager.BIND_MASTER_TASK_NAME_ALIAS;
import static com.ts.louisiana.engine.api.MetadataManager.BIND_TASK_NAME_ALIAS;
import static com.ts.louisiana.engine.api.MetadataManager.CHECK_IN_CONTEXT_TASK_NAME_ALIAS;
import static com.ts.louisiana.engine.api.MetadataManager.CHECK_MASTER_IN_CONTEXT_TASK_NAME_ALIAS;
import static com.ts.louisiana.engine.api.MetadataManager.CREATE_TASK_NAME_ALIAS;
import static com.ts.louisiana.engine.api.MetadataManager.MAP_TASK_NAME_ALIAS;
import static com.ts.louisiana.engine.api.MetadataManager.RETRIEVE_FROM_CONTEXT_TASK_NAME_ALIAS;
import static com.ts.louisiana.engine.api.MetadataManager.RETRIEVE_MASTER_FROM_CONTEXT_TASK_NAME_ALIAS;
import static com.ts.louisiana.engine.api.MetadataManager.RETRIEVE_MASTER_FROM_REPOSITORY_TASK_NAME_ALIAS;
import static com.ts.louisiana.engine.api.MetadataManager.RETRIEVE_MASTER_TASK_NAME_ALIAS;
import static com.ts.louisiana.engine.api.MetadataManager.RETRIEVE_TASK_NAME_ALIAS;
import static com.ts.louisiana.engine.api.MetadataManager.STORE_TASK_NAME_ALIAS;
import static com.ts.louisiana.engine.api.MetadataManager.WALK_UP_THE_TREE_TASK_NAME_ALIAS;

@Component
@Slf4j
public class SimpleActionHandlerSetImpl implements ActionHandlerSet<JsonObject> {

    private EntityRepository<JsonObject> entityRepository;

    private MapperFactory<JsonObject> mapperFactory;

    private MetadataManager metadataManager;


    @Override
    public Task<Void> actionTask(Action action, JobExecutionContext<JsonObject> jobExecutionContext) {
        ActionType actionType = action.getActionType();
        switch (actionType) {
            case CREATE:
                return createActionTask(action, jobExecutionContext);
            case UPDATE:
                return updateActionTask(action, jobExecutionContext);
            default:
                throw new RuntimeException("Unknown ActionType: " + actionType);
        }
    }

    @Override
    public Task<Void> createActionTask(Action action, JobExecutionContext<JsonObject> jobExecutionContext) {
        String entityType = action.getEntityType();
        EntityDefinition entityDefinition = metadataManager.getEntityDefinition(entityType);

        return Task.callable(entityDefinition.getTaskName(CREATE_TASK_NAME_ALIAS), () -> entityRepository.createEntityObject(entityType))
                .map(entityDefinition.getTaskName(MAP_TASK_NAME_ALIAS),
                        entityObject -> {
                            EntityObject<JsonObject> sourceEntity = jobExecutionContext.getSourceEntity();
                            Mapper<JsonObject> mapper = mapperFactory.getMapper(sourceEntity.getEntityType(), entityType);
                            return mapper.mapData(action.getMapping(), sourceEntity, entityObject, jobExecutionContext);
                        })
                .map(entityDefinition.getTaskName(STORE_TASK_NAME_ALIAS), entityRepository::storeEntity)
                .map(entityDefinition.getTaskName(BIND_TASK_NAME_ALIAS), entityObject -> {
                    jobExecutionContext.bindEntityObjectToContext(entityObject);
                    return null;
                });

    }

    private Task<EntityObject<JsonObject>> walkUpTheTree(Composite317 composite) {
        EntityDefinition entityDefinition = metadataManager.getEntityDefinition(composite.currentEntityObject.getEntityType());
        String masterEntityType = entityDefinition.getMasterEntityType();
        JobExecutionContext<JsonObject> localJobExecutionContext = composite.jobExecutionContext;
        String localRequiredEntityType = composite.requiredEntityType;

        return localRequiredEntityType.equals(composite.currentEntityObject.getEntityType()) ?
                Task.callable(entityDefinition.getTaskName(RETRIEVE_FROM_CONTEXT_TASK_NAME_ALIAS), () -> composite.currentEntityObject) :
                Task.callable(entityDefinition.getTaskName(CHECK_MASTER_IN_CONTEXT_TASK_NAME_ALIAS), () -> localJobExecutionContext.getBoundEntityObject(masterEntityType))
                        .flatMap(entityDefinition.getTaskName(RETRIEVE_MASTER_TASK_NAME_ALIAS),
                                entityObject -> Objects.nonNull(entityObject) ?
                                        Task.callable(entityDefinition.getTaskName(RETRIEVE_MASTER_FROM_CONTEXT_TASK_NAME_ALIAS), () -> entityObject) :
                                        Task.callable(entityDefinition.getTaskName(RETRIEVE_MASTER_FROM_REPOSITORY_TASK_NAME_ALIAS), () -> entityRepository.findByEntityObject(masterEntityType, composite.currentEntityObject))
                                                .map(entityDefinition.getTaskName(BIND_MASTER_TASK_NAME_ALIAS), masterEntityObject -> {
                                                    localJobExecutionContext.bindEntityObjectToContext(masterEntityObject);
                                                    return masterEntityObject;
                                                }))
                        .flatMap(entityDefinition.getTaskName(WALK_UP_THE_TREE_TASK_NAME_ALIAS), nextEntityObject ->
                                walkUpTheTree(new Composite317(localRequiredEntityType, nextEntityObject, localJobExecutionContext)));
    }


    private Task<EntityObject<JsonObject>> retrieveEntity(String entityType, JobExecutionContext<JsonObject> jobExecutionContext) {
        EntityDefinition entityDefinition = metadataManager.getEntityDefinition(entityType);

        Task<EntityObject<JsonObject>> checkEntityObject = Task.callable(entityDefinition.getTaskName(CHECK_IN_CONTEXT_TASK_NAME_ALIAS),
                () -> jobExecutionContext.getBoundEntityObject(entityType));

        return checkEntityObject.flatMap(entityDefinition.getTaskName(RETRIEVE_TASK_NAME_ALIAS),
                entityObject -> Objects.nonNull(entityObject) ?
                        Task.callable(entityDefinition.getTaskName(RETRIEVE_FROM_CONTEXT_TASK_NAME_ALIAS), () -> entityObject) :
                        walkUpTheTree(new Composite317(entityType, jobExecutionContext.getLastBoundEntityObject(), jobExecutionContext))
        );
    }

    @Override
    public Task<Void> updateActionTask(Action action, JobExecutionContext<JsonObject> jobExecutionContext) {
        String entityType = action.getEntityType();
        EntityDefinition entityDefinition = metadataManager.getEntityDefinition(entityType);
        return retrieveEntity(entityType, jobExecutionContext)
                .map(entityDefinition.getTaskName(MAP_TASK_NAME_ALIAS),
                        entityObject -> {
                            EntityObject<JsonObject> sourceEntity = jobExecutionContext.getSourceEntity();
                            Mapper<JsonObject> mapper = mapperFactory.getMapper(sourceEntity.getEntityType(), entityType);
                            return mapper.mapData(action.getMapping(), sourceEntity, entityObject, jobExecutionContext);
                        })
                .map(entityDefinition.getTaskName(STORE_TASK_NAME_ALIAS), entityRepository::storeEntity)
                .map(entityDefinition.getTaskName(BIND_TASK_NAME_ALIAS), entityObject -> {
                    jobExecutionContext.bindEntityObjectToContext(entityObject);
                    return null;
                });
    }

    @Autowired
    public void setEntityRepository(EntityRepository<JsonObject> entityRepository) {
        this.entityRepository = entityRepository;
    }

    @Autowired
    public void setMapperFactory(MapperFactory<JsonObject> mapperFactory) {
        this.mapperFactory = mapperFactory;
    }

    @Autowired
    public void setMetadataManager(MetadataManager metadataManager) {
        this.metadataManager = metadataManager;
    }

    @Value
//    @NoArgsConstructor
    @AllArgsConstructor
    private static class Composite317 {
        private String requiredEntityType;
        private EntityObject<JsonObject> currentEntityObject;
        private JobExecutionContext<JsonObject> jobExecutionContext;
    }
}
