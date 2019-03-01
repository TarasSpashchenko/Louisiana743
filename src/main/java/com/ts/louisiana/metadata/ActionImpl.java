package com.ts.louisiana.metadata;

import com.ts.louisiana.engine.api.ProfileVisitor;

public class ActionImpl extends NodeImpl implements Action {
    private final ActionType actionType;
    private final EntityType entityType;
    private final Mapping mapping;

    public ActionImpl(ActionType actionType, EntityType entityType, Mapping mapping) {
        super();

        this.actionType = actionType;
        this.entityType = entityType;
        this.mapping = mapping;
    }

    public ActionImpl(String name, ActionType actionType, EntityType entityType, Mapping mapping) {
        super(name);

        this.actionType = actionType;
        this.entityType = entityType;
        this.mapping = mapping;
    }

    @Override
    public ActionType getActionType() {
        return actionType;
    }

    @Override
    public EntityType getEntityType() {
        return entityType;
    }

    @Override
    public Mapping getMapping() {
        return mapping;
    }

    @Override
    public void accept(ProfileVisitor visitor) {
        visitor.visitAction(this);
    }
}
