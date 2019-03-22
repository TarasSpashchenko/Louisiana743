package com.ts.louisiana.metadata.api;

public interface EntityOperation {
    enum Verb {
        GET,
        POST,
        PUT,
        DELETE
    }

    String getOperationType();

    Verb getVerb();

    String getUri();
}
