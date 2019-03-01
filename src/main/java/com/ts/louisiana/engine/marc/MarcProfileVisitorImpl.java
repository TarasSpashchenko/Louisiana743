package com.ts.louisiana.engine.marc;

import com.linkedin.parseq.Task;
import com.ts.louisiana.engine.api.ActionHandlerSet;
import com.ts.louisiana.engine.api.JobExecutionContext;
import com.ts.louisiana.engine.api.ProfileVisitor;
import com.ts.louisiana.engine.api.ToolKit;
import com.ts.louisiana.metadata.Action;
import com.ts.louisiana.metadata.EntityType;
import com.ts.louisiana.metadata.Job;
import com.ts.louisiana.metadata.Mapping;
import com.ts.louisiana.metadata.Match;
import com.ts.louisiana.metadata.Node;
import com.ts.louisiana.types.ContextObject;
import com.ts.louisiana.types.MarcEntity;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
class MarcProfileVisitorImpl implements ProfileVisitor {
    private final ToolKit toolKit;

    private final JobExecutionContext jobExecutionContext;

    private final Map<Integer, List<Pair<Node, Task<Void>>>> interimTaskContainers = new HashMap<>();

    private int iterationLevel = 0;

    private Task<Void> topLevelTask;

    public MarcProfileVisitorImpl(ToolKit toolKit, JobExecutionContext jobExecutionContext) {
        this.toolKit = toolKit;
        this.jobExecutionContext = jobExecutionContext;
    }

    @Override
    public void visitJob(Job job) {
        log.info("visiting Job: {}", job);

        List<Pair<Node, Task<Void>>> childTasks = new ArrayList<>();
        iterationLevel++;
        try {
            interimTaskContainers.put(iterationLevel, childTasks);

            for (Node child : job.getChildren()) {
                if (child instanceof Action) {
                    child.accept(this);
                }
            }
        } finally {
            interimTaskContainers.remove(iterationLevel--);
        }

        topLevelTask = toolKit.getJobHandler().composeChildTasks(childTasks);

    }

    @Override
    public void visitMatch(Match match) {
        iterationLevel++;
        try {

        } finally {
            iterationLevel--;
        }

    }

    @Override
    public void visitAction(Action action) {
        log.info("visiting Action: {}", action);

        ActionHandlerSet actionHandlerSet = toolKit.getActionHandlerSet(action.getEntityType());
        List<Pair<Node, Task<Void>>> destinationTaskList = interimTaskContainers.get(iterationLevel);

        Task<Void> task = actionHandlerSet.actionTask(action, jobExecutionContext);

        List<Pair<Node, Task<Void>>> childTasks = new ArrayList<>();
        iterationLevel++;
        try {
            interimTaskContainers.put(iterationLevel, childTasks);

            for (Node child : action.getChildren()) {
                if (child instanceof Action) {
                    child.accept(this);
                }
            }
        } finally {
            interimTaskContainers.remove(iterationLevel--);
        }

        destinationTaskList.add(Pair.of(action, actionHandlerSet.composeChildTasks(task, childTasks)));
    }

//    @Override
//    public void visitMapping(Mapping mapping) {
//        iterationLevel++;
//        try {
//            log.info("visiting mapping: {}", mapping);
////        return Task.action("Mapping " + mapping + " task", () -> log.info("Mapping task in progress... working on jobExecutionContext {}", jobExecutionContext));    }
//        } finally {
//            iterationLevel--;
//        }
//    }

    @Override
    public Task<Void> getProfileTask() {
        return topLevelTask;
    }

    //TODO: some sort of result should be here
}
