package com.ts.louisiana.metadata;

import com.ts.louisiana.metadata.api.EntityOperation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntityOperationImpl implements EntityOperation {
    private String operationType;
    private Verb verb;
    private String uri;

    @Override
    public String getOperationType() {
        return operationType;
    }

    @Override
    public Verb getVerb() {
        return verb;
    }

    @Override
    public String getUri() {
        return uri;
    }
}
