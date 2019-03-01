package com.ts.louisiana.engine.toolkit;

import com.linkedin.parseq.Task;
import com.ts.louisiana.engine.api.ActionHandlerSet;
import com.ts.louisiana.engine.api.EntityRepository;
import com.ts.louisiana.engine.api.JobExecutionContext;
import com.ts.louisiana.engine.api.Mapper;
import com.ts.louisiana.engine.api.MapperFactory;
import com.ts.louisiana.metadata.Action;
import com.ts.louisiana.metadata.ActionType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public abstract class AbstractActionHandlerSet<T> implements ActionHandlerSet {
    public static final String CREATE_TASK_NAME_KEY = "createTaskNameKey";
    public static final String RETRIEVE_TASK_NAME_KEY = "retrieveTaskNameKey";
    public static final String MAP_TASK_NAME_KEY = "mapTaskNameKey";
    public static final String STORE_TASK_NAME_KEY = "storeTaskNameKey";
    public static final String BIND_TASK_NAME_KEY = "binTaskNameKey";

    public static final String UNDEFINED_TASK_NAME = "UNDEFINED TASK NAME";

    @Autowired
    private EntityRepository entityRepository;

    @Autowired
    private MapperFactory mapperFactory;

    @Override
    public Task<Void> actionTask(Action action, JobExecutionContext jobExecutionContext) {
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
    public Task<Void> createActionTask(Action action, JobExecutionContext jobExecutionContext) {
        return Task.callable(Optional.ofNullable(getTaskName(CREATE_TASK_NAME_KEY, action)).orElse(UNDEFINED_TASK_NAME), this::createNewEntity)
                .map(Optional.ofNullable(getTaskName(MAP_TASK_NAME_KEY, action)).orElse(UNDEFINED_TASK_NAME), entity -> {
                            Mapper<?, T> mapper = mapperFactory.getMapper(jobExecutionContext.getSourceEntityType(), action.getEntityType());
                            return mapper.mapData(action.getMapping(), jobExecutionContext.getSourceEntity(), entity, jobExecutionContext);
                        }
                )
                .map(Optional.ofNullable(getTaskName(STORE_TASK_NAME_KEY, action)).orElse(UNDEFINED_TASK_NAME), this::storeEntity)
                .map(Optional.ofNullable(getTaskName(BIND_TASK_NAME_KEY, action)).orElse(UNDEFINED_TASK_NAME), entity -> {
                    bindEntityToContext(jobExecutionContext, entity);
                    return null;
                });

    }

    @Override
    public Task<Void> updateActionTask(Action action, JobExecutionContext jobExecutionContext) {
        return retrieveEntityTask(action, jobExecutionContext)
                .map(Optional.ofNullable(getTaskName(MAP_TASK_NAME_KEY, action)).orElse(UNDEFINED_TASK_NAME), entity -> {
                            Mapper<?, T> mapper = mapperFactory.getMapper(jobExecutionContext.getSourceEntityType(), action.getEntityType());
                            return mapper.mapData(action.getMapping(), jobExecutionContext.getSourceEntity(), entity, jobExecutionContext);
                        }
                )
                .map(Optional.ofNullable(getTaskName(STORE_TASK_NAME_KEY, action)).orElse(UNDEFINED_TASK_NAME), this::storeEntity)
                .map(Optional.ofNullable(getTaskName(BIND_TASK_NAME_KEY, action)).orElse(UNDEFINED_TASK_NAME), entity -> {
                    bindEntityToContext(jobExecutionContext, entity);
                    return null;
                });

    }

    protected abstract Task<T> retrieveEntityTask(Action action, JobExecutionContext jobExecutionContext);

    protected abstract T createNewEntity();

    protected abstract T storeEntity(T entity);

    protected abstract void bindEntityToContext(JobExecutionContext jobExecutionContext, T entity);

    protected abstract String getTaskName(String nameKey, Action action);

    protected final EntityRepository getEntityRepository() {
        return entityRepository;
    }

}
