package com.ts.louisiana.engine.toolkit;

import com.ts.louisiana.engine.api.Mapper;
import com.ts.louisiana.engine.api.MapperFactory;
import io.vertx.core.json.JsonObject;
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

    private Mapper<JsonObject> jsonObjectMapper;


    @Override
    public Mapper getMapper(String fromEntityType, String toEntityType) {
        return jsonObjectMapper;
    }

    @Autowired
    public void setJsonObjectMapper(Mapper<JsonObject> jsonObjectMapper) {
        this.jsonObjectMapper = jsonObjectMapper;
    }

}
