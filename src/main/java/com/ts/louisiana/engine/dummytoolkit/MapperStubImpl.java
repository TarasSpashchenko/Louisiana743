package com.ts.louisiana.engine.dummytoolkit;

import com.ts.louisiana.engine.api.JobExecutionContext;
import com.ts.louisiana.engine.api.Mapper;
import com.ts.louisiana.metadata.api.Mapping;
import com.ts.louisiana.types.EntityObject;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
@Slf4j
public class MapperStubImpl implements Mapper<JsonObject> {
    @Override
    public EntityObject<JsonObject> mapData(
            Mapping mapping,
            EntityObject<JsonObject> source,
            EntityObject<JsonObject> destination,
            JobExecutionContext<JsonObject> jobExecutionContext) {
        return destination;
    }
}
