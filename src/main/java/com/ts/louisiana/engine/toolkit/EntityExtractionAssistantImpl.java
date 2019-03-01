package com.ts.louisiana.engine.toolkit;

import com.ts.louisiana.engine.api.EntityRepository;
import com.ts.louisiana.engine.api.JobExecutionContext;
import com.ts.louisiana.metadata.EntityType;
import com.ts.louisiana.types.ContextObject;
import com.ts.louisiana.types.HoldingsEntity;
import com.ts.louisiana.types.InstanceEntity;
import com.ts.louisiana.types.ItemEntity;
import com.ts.louisiana.types.OrderLineEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class EntityExtractionAssistantImpl {
    @Autowired
    private EntityRepository entityRepository;

    private <T, B> T retrieveEntityInternal(EntityType lastBoundEntityType,
                                            ContextObject<?> lastBoundEntity,
                                            Class<T> entityClass,
                                            Class<B> byEntityClass,
                                            Supplier<T> boundEntitySupplier,
                                            Function<B, T> fromRepository
    ) {
        if (entityClass.isInstance(lastBoundEntity)) {
            return entityClass.cast(lastBoundEntity);
        }

        T boundEntity = boundEntitySupplier.get();
        if (Objects.nonNull(boundEntity)) {
            return boundEntity;
        }

        if (byEntityClass.isInstance(lastBoundEntity)) {
            return fromRepository.apply(byEntityClass.cast(lastBoundEntity));
        }
        throw new RuntimeException("Can't retrieve " + entityClass.getName() + " by " + lastBoundEntityType + ": " + lastBoundEntity);
    }

    private ItemEntity retrieveItemEntity(EntityType lastBoundEntityType, ContextObject<?> lastBoundEntity, JobExecutionContext jobExecutionContext) {
        return retrieveEntityInternal(lastBoundEntityType,
                lastBoundEntity,
                ItemEntity.class,
                OrderLineEntity.class,
                jobExecutionContext::getBoundItemEntity,
                entityRepository::findItemByOrderLine);
    }

    private HoldingsEntity retrieveHoldingsEntity(EntityType lastBoundEntityType, ContextObject<?> lastBoundEntity, JobExecutionContext jobExecutionContext) {
        return retrieveEntityInternal(lastBoundEntityType,
                lastBoundEntity,
                HoldingsEntity.class,
                ItemEntity.class,
                jobExecutionContext::getBoundHoldingsEntity,
                entityRepository::findHoldingsByItem);
    }

    private InstanceEntity retrieveInstanceEntity(EntityType lastBoundEntityType, ContextObject<?> lastBoundEntity, JobExecutionContext jobExecutionContext) {
        return retrieveEntityInternal(lastBoundEntityType,
                lastBoundEntity,
                InstanceEntity.class,
                HoldingsEntity.class,
                jobExecutionContext::getBoundInstanceEntity,
                entityRepository::findInstanceByHoldings);
    }

    public <T> T retrieveEntity(EntityType entityType, JobExecutionContext jobExecutionContext) {

    }
}
