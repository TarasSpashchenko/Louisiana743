package com.ts.louisiana.engine.dummytoolkit;

import com.ts.louisiana.engine.api.JobExecutionContext;
import com.ts.louisiana.engine.api.Mapper;
import com.ts.louisiana.metadata.Mapping;
import com.ts.louisiana.types.HoldingsEntity;
import com.ts.louisiana.types.MarcEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
@Slf4j
public class HoldingsMapperImpl implements Mapper<MarcEntity, HoldingsEntity> {
    @Override
    public HoldingsEntity mapData(
            final Mapping mapping,
            final MarcEntity source,
            final HoldingsEntity destination,
            final JobExecutionContext jobExecutionContext) {

        return doMapping(mapping, source, destination, jobExecutionContext);
    }

    private HoldingsEntity doMapping(final Mapping mapping,
                                     final MarcEntity source,
                                     final HoldingsEntity destination,
                                     final JobExecutionContext jobExecutionContext) {
        log.info("Mapping from MarcEntity to HoldingsEntity using jobExecutionContext has been performed.");
        return destination;
    }
}
