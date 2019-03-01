package com.ts.louisiana.engine.toolkit;

import com.ts.louisiana.engine.api.ActionHandlerSet;
import com.ts.louisiana.engine.api.JobExecutionContext;
import com.ts.louisiana.metadata.Action;
import com.ts.louisiana.types.HoldingsEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Qualifier("holdingsActionHandlerSet")
@Slf4j
public class HoldingsActionHandlerSetImpl extends AbstractActionHandlerSet<HoldingsEntity> implements ActionHandlerSet {

    private final Map<String, String> taskNames = new HashMap<>();
    {
        taskNames.put(CREATE_TASK_NAME_KEY, "Create Holdings Entity");
        taskNames.put(MAP_TASK_NAME_KEY, "Map data into Holdings Entity");
        taskNames.put(STORE_TASK_NAME_KEY, "Store Holdings Entity");
        taskNames.put(BIND_TASK_NAME_KEY, "Bind Holdings Entity to Context");
    }

    @Override
    protected HoldingsEntity createNewEntity() {
        return getEntityRepository().createHoldingsEntity();
    }

    @Override
    protected HoldingsEntity storeEntity(HoldingsEntity entity) {
        return getEntityRepository().storeHoldingsEntity(entity);
    }

    @Override
    protected void bindEntityToContext(JobExecutionContext jobExecutionContext, HoldingsEntity entity) {
        bindHoldingsEntityToContext(jobExecutionContext, entity);
    }

    @Override
    protected String getTaskName(String nameKey, Action action) {
        return taskNames.get(nameKey);
    }

    private void bindHoldingsEntityToContext(JobExecutionContext jobExecutionContext, HoldingsEntity holdingsEntity) {
        jobExecutionContext.bindHoldingsEntityToContext(holdingsEntity);
        //TODO: implement this
        log.info("Holdings has been bound to the Context");
    }
//    @Override
//    public Task<Void> createActionTask(Action action, JobExecutionContext jobExecutionContext) {
//        EntityRepository entityRepository = getEntityRepository();
//        Task<HoldingsEntity> createAndMapHoldingsEntityTask = entityRepository
//                .createHoldingsEntity()
//                .map("Map data to Holdings",
//                        holdingsEntity -> holdingsEntityMapper.mapData(action.getMapping(),
//                                jobExecutionContext.getSourceEntity(),
//                                holdingsEntity,
//                                jobExecutionContext));
//
//        return entityRepository.storeHoldingsEntity(createAndMapHoldingsEntityTask)
//                .map("Bind created Holdings to the Context", holdingsEntity -> {
//                    bindHoldingsEntityToContext(jobExecutionContext, holdingsEntity);
//                    return null;
//                });
//    }
//
//

}