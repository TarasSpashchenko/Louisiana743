package com.ts.louisiana.engine.dummytoolkit;

import com.linkedin.parseq.Task;
import com.ts.louisiana.engine.api.JobExecutionContext;
import com.ts.louisiana.engine.api.Mapper;
import com.ts.louisiana.metadata.Mapping;
import com.ts.louisiana.types.ContextObject;
import com.ts.louisiana.types.InstanceEntity;
import com.ts.louisiana.types.MarcEntity;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
@Slf4j
public class InstanceMapperImpl implements Mapper<MarcEntity, InstanceEntity> {
    @Override
    public InstanceEntity mapData(
            final Mapping mapping,
            final MarcEntity source,
            final InstanceEntity destination,
            final JobExecutionContext jobExecutionContext) {

        return  doMapping(mapping, source, destination, jobExecutionContext);
    }

    private InstanceEntity doMapping(final Mapping mapping,
                                     final MarcEntity source,
                                     final InstanceEntity destination,
                                     final JobExecutionContext jobExecutionContext) {
        log.info("Mapping from MarcEntity to InstanceEntity using jobExecutionContext has been performed.");
        return destination;
    }
}
