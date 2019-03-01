package com.ts.louisiana.engine.api;

import com.ts.louisiana.types.HoldingsEntity;
import com.ts.louisiana.types.InstanceEntity;
import com.ts.louisiana.types.ItemEntity;
import com.ts.louisiana.types.OrderLineEntity;

public interface EntityRepository {
    ItemEntity findItemBy(QueryCriteria queryCriteria);

    InstanceEntity findInstanceByHoldings(HoldingsEntity holdingsEntity);
    HoldingsEntity findHoldingsByItem(ItemEntity itemEntity);
    ItemEntity findItemByOrderLine(OrderLineEntity orderLineEntity);

    ItemEntity createItemEntity();
    HoldingsEntity createHoldingsEntity();
    InstanceEntity createInstanceEntity();
    OrderLineEntity createOrderLineEntity();

    InstanceEntity storeInstanceEntity(InstanceEntity instanceEntity);
    HoldingsEntity storeHoldingsEntity(HoldingsEntity holdingsEntity);
    ItemEntity storeItemEntity(ItemEntity itemEntity);
    OrderLineEntity storeOrderLineEntity(OrderLineEntity orderLineEntity);

}
