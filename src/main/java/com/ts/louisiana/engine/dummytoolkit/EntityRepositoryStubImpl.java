package com.ts.louisiana.engine.dummytoolkit;

import com.linkedin.parseq.Task;
import com.ts.louisiana.engine.api.EntityRepository;
import com.ts.louisiana.engine.api.QueryCriteria;
import com.ts.louisiana.types.ContextObject;
import com.ts.louisiana.types.HoldingsEntity;
import com.ts.louisiana.types.InstanceEntity;
import com.ts.louisiana.types.ItemEntity;
import com.ts.louisiana.types.OrderLineEntity;
import com.ts.louisiana.types.stub.HoldingsEntityStub;
import com.ts.louisiana.types.stub.InstanceEntityStub;
import com.ts.louisiana.types.stub.ItemEntityStub;
import com.ts.louisiana.types.stub.OrderLineEntityStub;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Repository
@Profile("test")
@Slf4j
public class EntityRepositoryStubImpl implements EntityRepository {
    private final Map<Object, ContextObject<?>> dummyCache = new HashMap<>();

    @Override
    public ItemEntity findItemBy(QueryCriteria queryCriteria) {
        ItemEntity itemEntityFromCache = getItemEntityFromCache(queryCriteria);
        return Objects.isNull(itemEntityFromCache) ? getItemEntityFromDataSource(queryCriteria) : itemEntityFromCache;
    }

    @Override
    public InstanceEntity findInstanceByHoldings(HoldingsEntity holdingsEntity) {
        return null;
    }

    @Override
    public HoldingsEntity findHoldingsByItem(ItemEntity itemEntity) {
        return null;
    }

    @Override
    public ItemEntity findItemByOrderLine(OrderLineEntity orderLineEntity) {
        return null;
    }

    @Override
    public ItemEntity createItemEntity() {
        return new ItemEntityStub();
    }

    @Override
    public HoldingsEntity createHoldingsEntity() {
        return new HoldingsEntityStub();
    }

    @Override
    public InstanceEntity createInstanceEntity() {
        return new InstanceEntityStub();
    }

    @Override
    public OrderLineEntity createOrderLineEntity() {
        return new OrderLineEntityStub();
    }

    @Override
    public InstanceEntity storeInstanceEntity(InstanceEntity instanceEntity) {
        log.info("instanceEntity has been stored: {}", instanceEntity);
        return instanceEntity;
    }

    @Override
    public HoldingsEntity storeHoldingsEntity(HoldingsEntity holdingsEntity) {
        log.info("holdingsEntity has been stored: {}", holdingsEntity);
        return holdingsEntity;
    }

    @Override
    public ItemEntity storeItemEntity(ItemEntity itemEntity) {
        log.info("itemEntity has been stored: {}", itemEntity);
        return itemEntity;
    }

    @Override
    public OrderLineEntity storeOrderLineEntity(OrderLineEntity orderLineEntity) {
        log.info("orderLineEntity has been stored: {}", orderLineEntity);
        return orderLineEntity;
    }


    protected ItemEntity getItemEntityFromCache(QueryCriteria queryCriteria) {
        return (ItemEntity) dummyCache.get(queryCriteria);

    }

    protected ItemEntity getItemEntityFromDataSource(QueryCriteria queryCriteria) {
        ItemEntityStub object = new ItemEntityStub();
        dummyCache.put(queryCriteria, object);
        return object;
    }
}
