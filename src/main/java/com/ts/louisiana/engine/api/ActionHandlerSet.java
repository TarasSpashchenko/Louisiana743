package com.ts.louisiana.engine.api;

import com.linkedin.parseq.Task;
import com.ts.louisiana.metadata.api.Action;
import com.ts.louisiana.metadata.api.Node;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public interface ActionHandlerSet<T> {
    Task<Void> actionTask(Action action, JobExecutionContext<T> jobExecutionContext);

    Task<Void> createActionTask(Action action, JobExecutionContext<T> jobExecutionContext);

    Task<Void> updateActionTask(Action action, JobExecutionContext<T> jobExecutionContext);

    default Task<Void> composeChildTasks(Task<Void> ownTask, List<Pair<Node, Task<Void>>> childTasks) {
        return childTasks
                .stream()
                .reduce(Pair.of(null, ownTask), (p1, p2) -> Pair.of(p2.getLeft(), p1.getRight().andThen(p2.getLeft().getName(), p2.getRight())))
                .getValue();
    }

    default Task<Void> composeChildTasks2(Task<Void> ownTask, List<Pair<Node, Task<Void>>> childTasks) {
        return childTasks
                .stream()
                .reduce(Pair.of(null, ownTask), (p1, p2) -> Pair.of(p2.getLeft(), p1.getRight().flatMap(p1.getLeft().getName(), v -> p2.getRight())))
                .getValue();
    }
}
