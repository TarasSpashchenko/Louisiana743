package com.ts.louisiana.metadata;

import java.util.List;

public interface Match extends Node {
    EntityType getMatchEntityType();

    MatchCriteria getMatchCriteria();

    List<Node> getNonMatchesChildren();

    <T extends Node> void addNonMatches(T child);

    <T extends Node> void removeNonMatches(T child);

    <T extends Node> T getNonMatchesChild(int i);

    default List<Node> getMatchesChildren() {
        return getChildren();
    }

    default <T extends Node> void addMatches(T child) {
        add(child);
    }

    default <T extends Node> void removeMatches(T child) {
        remove(child);
    }

    default <T extends Node> T getMatchesChild(int i) {
        return getChild(i);
    }

}
