package com.ts.louisiana.metadata.api;

public interface Action extends Node {
    ActionType getActionType();
    String getEntityType();
    Mapping getMapping();
}
