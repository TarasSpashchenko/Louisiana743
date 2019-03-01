package com.ts.louisiana.metadata;

public interface Action extends Node {
    ActionType getActionType();
    EntityType getEntityType();
    Mapping getMapping();
}
