package com.ts.louisiana.metadata;

import com.ts.louisiana.engine.api.ProfileVisitor;

import java.util.List;

public interface Node {
    List<Node> getChildren();
    <T extends Node> void add(T child);
    <T extends Node> void remove(T child);
    <T extends Node> T getChild(int i);

    String getName();

    void accept(ProfileVisitor visitor);
}
