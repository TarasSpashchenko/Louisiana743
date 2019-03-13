package com.ts.louisiana.metadata;

import com.ts.louisiana.engine.api.ProfileVisitor;
import com.ts.louisiana.metadata.api.Action;
import com.ts.louisiana.metadata.api.ActionType;
import com.ts.louisiana.metadata.api.Mapping;

public class ActionImpl extends NodeImpl implements Action {
    private final ActionType actionType;
    private final String entityType;
    private final Mapping mapping;

    public ActionImpl(ActionType actionType, String entityType, Mapping mapping) {
        super();

        this.actionType = actionType;
        this.entityType = entityType;
        this.mapping = mapping;
    }

    public ActionImpl(String name, ActionType actionType, String entityType, Mapping mapping) {
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
    public String getEntityType() {
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
