package com.ts.louisiana.engine.api;

import com.linkedin.parseq.Task;
import com.ts.louisiana.metadata.Node;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public interface JobHandler {
    Task<Void> composeChildTasks(List<Pair<Node, Task<Void>>> childTasks);
}
