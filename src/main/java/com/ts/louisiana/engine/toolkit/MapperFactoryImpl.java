package com.ts.louisiana.engine.toolkit;

import com.ts.louisiana.engine.api.Mapper;
import com.ts.louisiana.engine.api.MapperFactory;
import com.ts.louisiana.metadata.EntityType;
import com.ts.louisiana.types.HoldingsEntity;
import com.ts.louisiana.types.InstanceEntity;
import com.ts.louisiana.types.MarcEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;


@Component
@Slf4j
public class MapperFactoryImpl implements MapperFactory {
    private final Map<Pair<EntityType, EntityType>, Mapper<?, ?>> mappersContainer = new HashMap<>();

    @Autowired
    private Mapper<MarcEntity, HoldingsEntity> holdingsEntityMapper;

    @Autowired
    private Mapper<MarcEntity, InstanceEntity> instanceEntityMapper;

    @Override
    public <P, R> Mapper<P, R> getMapper(EntityType from, EntityType to) {
        return (Mapper<P, R>) mappersContainer.get(Pair.of(from, to));
    }

    @PostConstruct
    private void init() {
        mappersContainer.put(Pair.of(EntityType.MARC, EntityType.INSTANCE), instanceEntityMapper);
        mappersContainer.put(Pair.of(EntityType.MARC, EntityType.HOLDINGS), holdingsEntityMapper);
    }
}
