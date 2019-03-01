package com.ts.louisiana.metadata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

abstract class NodeImpl implements Node {
    private final List<Node> nodeContent = new ArrayList<>();

    private final String name;

    public NodeImpl() {
        this("Undefined");
    }

    public NodeImpl(String name) {
        this.name = name;
    }

    @Override
    public List<Node> getChildren() {
        return Collections.unmodifiableList(nodeContent);
    }

    @Override
    public <T extends Node> void add(T child) {
        nodeContent.add(child);
    }

    @Override
    public <T extends Node> void remove(T child) {
        if (Objects.nonNull(child)) {
            nodeContent.remove(child);
        }
    }

    @Override
    public <T extends Node> T getChild(int i) {
        return (T) nodeContent.get(i);
    }

    @Override
    public String getName() {
        return name;
    }
}
