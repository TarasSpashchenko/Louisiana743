package com.ts.louisiana.engine;

import com.ts.louisiana.engine.api.JobExecutionContext;
import com.ts.louisiana.metadata.EntityType;
import com.ts.louisiana.types.ContextObject;
import com.ts.louisiana.types.HoldingsEntity;
import com.ts.louisiana.types.InstanceEntity;
import com.ts.louisiana.types.ItemEntity;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class JobExecutionContextImpl implements JobExecutionContext {
    //???
    private final ConcurrentMap<Object, Object> contextStorage = new ConcurrentHashMap<>();

    private final Object sourceEntity;
    private final EntityType sourceEntityType;

    private volatile InstanceEntity instanceEntity;
    private volatile HoldingsEntity holdingsEntity;
    private volatile ItemEntity itemEntity;

    private volatile EntityType lastBoundEntityType;
    private volatile ContextObject<?> lastBoundEntity;



    public <T> JobExecutionContextImpl(EntityType sourceEntityType, T sourceEntity) {
        this.sourceEntityType = sourceEntityType;
        this.sourceEntity = sourceEntity;
    }

    @Override
    public EntityType getSourceEntityType() {
        return sourceEntityType;
    }

    @Override
    public <T> T getSourceEntity() {
        return (T) sourceEntity;
    }

    @Override
    public void bindInstanceEntityToContext(InstanceEntity instanceEntity) {
        lastBoundEntity = this.instanceEntity = instanceEntity;
        lastBoundEntityType = EntityType.INSTANCE;
    }

    @Override
    public void bindHoldingsEntityToContext(HoldingsEntity holdingsEntity) {
        lastBoundEntity = this.holdingsEntity = holdingsEntity;
        lastBoundEntityType = EntityType.HOLDINGS;
    }

    @Override
    public void bindItemEntityToContext(ItemEntity itemEntity) {
        lastBoundEntity = this.itemEntity = itemEntity;
        lastBoundEntityType = EntityType.ITEM;
    }

    @Override
    public InstanceEntity getBoundInstanceEntity() {
        return instanceEntity;
    }

    @Override
    public HoldingsEntity getBoundHoldingsEntity() {
        return holdingsEntity;
    }

    @Override
    public ItemEntity getBoundItemEntity() {
        return itemEntity;
    }

    @Override
    public EntityType getLastBoundEntityType() {
        return lastBoundEntityType;
    }

    @Override
    public ContextObject<?> getLastBoundEntity() {
        return lastBoundEntity;
    }
}
