package com.ts.louisiana.engine.api;

import com.linkedin.parseq.Task;
import com.ts.louisiana.metadata.api.Action;
import com.ts.louisiana.metadata.api.Node;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public interface ActionHandlerSet<T> {
    String CREATE_TASK_NAME_ALIAS = "createTaskNameAlias";
    String RETRIEVE_TASK_NAME_ALIAS = "retrieveTaskNameAlias";
    String CHECK_IN_CONTEXT_TASK_NAME_ALIAS = "checkTaskAlias";
    String RETRIEVE_FROM_CONTEXT_TASK_NAME_ALIAS = "retrieveFromContextTaskNameAlias";
    String MAP_TASK_NAME_ALIAS = "mapTaskNameAlias";
    String STORE_TASK_NAME_ALIAS = "storeTaskNameAlias";
    String BIND_TASK_NAME_ALIAS = "bindTaskNameAlias";

    String RETRIEVE_MASTER_TASK_NAME_ALIAS = "retrieveMasterTaskAlias";
    String CHECK_MASTER_IN_CONTEXT_TASK_NAME_ALIAS = "checkMasterTaskAlias";
    String RETRIEVE_MASTER_FROM_CONTEXT_TASK_NAME_ALIAS = "retrieveMasterFromContextTaskNameAlias";
    String RETRIEVE_MASTER_FROM_REPOSITORY_TASK_NAME_ALIAS = "retrieveMasterFromRepositoryTaskNameAlias";

    String WALK_UP_THE_TREE_TASK_NAME_ALIAS = "walkUpTheTreeTaskNameAlias";

    String UNDEFINED_TASK_NAME = "UNDEFINED TASK NAME";

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
