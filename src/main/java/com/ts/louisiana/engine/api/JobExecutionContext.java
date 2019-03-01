package com.ts.louisiana.engine.api;

import com.ts.louisiana.metadata.EntityType;
import com.ts.louisiana.types.ContextObject;
import com.ts.louisiana.types.HoldingsEntity;
import com.ts.louisiana.types.InstanceEntity;
import com.ts.louisiana.types.ItemEntity;

public interface JobExecutionContext {
    EntityType getSourceEntityType();

    <T> T getSourceEntity();

    void bindInstanceEntityToContext(InstanceEntity instanceEntity);
    void bindHoldingsEntityToContext(HoldingsEntity holdingsEntity);
    void bindItemEntityToContext(ItemEntity itemEntity);

    InstanceEntity getBoundInstanceEntity();
    HoldingsEntity getBoundHoldingsEntity();
    ItemEntity getBoundItemEntity();

    EntityType getLastBoundEntityType();
    ContextObject<?> getLastBoundEntity();
}
