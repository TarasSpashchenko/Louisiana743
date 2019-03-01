package com.ts.louisiana.engine.toolkit;

import com.linkedin.parseq.Task;
import com.ts.louisiana.engine.api.ActionHandlerSet;
import com.ts.louisiana.engine.api.JobExecutionContext;
import com.ts.louisiana.metadata.Action;
import com.ts.louisiana.metadata.EntityType;
import com.ts.louisiana.types.HoldingsEntity;
import com.ts.louisiana.types.InstanceEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@Qualifier("instanceActionHandlerSet")
@Slf4j
public class InstanceActionHandlerSetImpl extends AbstractActionHandlerSet<InstanceEntity> implements ActionHandlerSet {

    private final Map<String, String> taskNames = new HashMap<>();
    {
        taskNames.put(CREATE_TASK_NAME_KEY, "Create Instance Entity");
        taskNames.put(RETRIEVE_TASK_NAME_KEY, "Retrieve Instance Entity");
        taskNames.put(MAP_TASK_NAME_KEY, "Map data into Instance Entity");
        taskNames.put(STORE_TASK_NAME_KEY, "Store Instance Entity");
        taskNames.put(BIND_TASK_NAME_KEY, "Bind Instance Entity to Context");
    }

    public HoldingsEntity findHoldings
    public InstanceEntity retrieveEntity(Action action, JobExecutionContext jobExecutionContext) {
        InstanceEntity boundInstanceEntity = jobExecutionContext.getBoundInstanceEntity();
        if (Objects.nonNull(boundInstanceEntity)) {
            return boundInstanceEntity;
        }

        EntityType lastBoundEntityType = jobExecutionContext.getLastBoundEntityType();
        switch (lastBoundEntityType) {
            case HOLDINGS: return getEntityRepository().findInstanceByHoldings(jobExecutionContext.getBoundHoldingsEntity());
        }

        InstanceEntity instanceEntity = jobExecutionContext.getBoundInstanceEntity();
        if (Objects.nonNull(instanceEntity)) {
            return instanceEntity;
        }


        return  (Objects.isNull(instanceEntity))
    }


    @Override
    protected Task<InstanceEntity> retrieveEntityTask(Action action, JobExecutionContext jobExecutionContext) {
        return Task.callable("Retrieve Instance", )
        return null;
    }

    @Override
    protected InstanceEntity createNewEntity() {
        return getEntityRepository().createInstanceEntity();
    }


    @Override
    protected InstanceEntity storeEntity(InstanceEntity entity) {
        return getEntityRepository().storeInstanceEntity(entity);
    }

    @Override
    protected void bindEntityToContext(JobExecutionContext jobExecutionContext, InstanceEntity entity) {
        bindInstanceEntityToContext(jobExecutionContext, entity);
    }

    @Override
    protected String getTaskName(String nameKey, Action action) {
        return taskNames.get(nameKey);
    }

    private void bindInstanceEntityToContext(JobExecutionContext jobExecutionContext, InstanceEntity instanceEntity) {
        jobExecutionContext.bindInstanceEntityToContext(instanceEntity);
        //TODO: implement this
        log.info("Instance has been bound to the Context");
    }

//    @Override
//    public Task<Void> createActionTask(Action action, JobExecutionContext jobExecutionContext) {
//        EntityRepository entityRepository = getEntityRepository();
//        Task<InstanceEntity> createAndMapInstanceEntityTask = entityRepository
//                .createInstanceEntity()
//                .map("Map data to Instance",
//                        instanceEntity -> instanceEntityMapper.mapData(action.getMapping(),
//                                jobExecutionContext.getSourceEntity(),
//                                instanceEntity,
//                                jobExecutionContext));
//
//        return entityRepository.storeInstanceEntity(createAndMapInstanceEntityTask)
//                .map("Bind created Instance to the Context", instanceEntity -> {
//                    bindInstanceEntityToContext(jobExecutionContext, instanceEntity);
//                    return null;
//                });
//    }
//
//    @Override
//    public Task<Void> updateActionTask(Action action, JobExecutionContext jobExecutionContext) {
//        return null;
//    }




//    @Override
//    public Task<InstanceEntity> createEntity(Action action) {
//        //TODO: repository should be used
//        return Task.callable("Create a new Instance", ContextObjectStub::new);
//    }
//
//    @Override
//    public Task<Void> storeEntity(Task<InstanceEntity> obtainTask, Task<InstanceEntity> mappingTask) {
//        obtainTask.flatMap(instanceEntity -> mappingTask);
//        return null;
//    }
}
