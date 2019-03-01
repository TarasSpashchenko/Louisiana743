package com.ts.louisiana.metadata;

import com.ts.louisiana.engine.api.ProfileVisitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MatchImpl extends NodeImpl implements Match {
    private final List<Node> nonMatchesContent = new ArrayList<>();

    private final MatchCriteria matchCriteria;

    private final  EntityType matchEntityType;

    public MatchImpl(EntityType matchEntityType, MatchCriteria matchCriteria) {
        super();
        this.matchEntityType = matchEntityType;
        this.matchCriteria = matchCriteria;
    }

    public MatchImpl(String name, EntityType matchEntityType, MatchCriteria matchCriteria) {
        super(name);
        this.matchEntityType = matchEntityType;
        this.matchCriteria = matchCriteria;
    }

    @Override
    public EntityType getMatchEntityType() {
        return null;
    }

    @Override
    public MatchCriteria getMatchCriteria() {
        return matchCriteria;
    }

    @Override
    public List<Node> getNonMatchesChildren() {
        return Collections.unmodifiableList(nonMatchesContent);
    }

    @Override
    public <T extends Node> void addNonMatches(T child) {
        nonMatchesContent.add(child);
    }

    @Override
    public <T extends Node> void removeNonMatches(T child) {
        nonMatchesContent.remove(child);
    }

    @Override
    public <T extends Node> T getNonMatchesChild(int i) {
        return (T) nonMatchesContent.get(i);
    }

    @Override
    public void accept(ProfileVisitor visitor) {
        visitor.visitMatch(this);
    }
}
