package com.ts.louisiana.engine.marc;

import com.linkedin.parseq.Task;
import com.ts.louisiana.engine.api.ActionHandlerSet;
import com.ts.louisiana.engine.api.JobExecutionContext;
import com.ts.louisiana.engine.api.MatchHandler;
import com.ts.louisiana.engine.api.ProfileVisitor;
import com.ts.louisiana.engine.api.ToolKit;
import com.ts.louisiana.metadata.api.Action;
import com.ts.louisiana.metadata.api.Job;
import com.ts.louisiana.metadata.api.Match;
import com.ts.louisiana.metadata.api.Node;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class MarcProfileVisitorV2Impl implements ProfileVisitor {
    private final ToolKit<JsonObject> toolKit;

    private final JobExecutionContext<JsonObject> jobExecutionContext;

    private Task<Void> lastTaskInChain;

    public MarcProfileVisitorV2Impl(ToolKit<JsonObject> toolKit, JobExecutionContext<JsonObject> jobExecutionContext) {
        this.toolKit = toolKit;
        this.jobExecutionContext = jobExecutionContext;
    }

    @Override
    public void visitJob(Job job) {
        log.info("visiting Job: {}", job);
        lastTaskInChain = Task.action("Job Profile init()", () -> log.info("\n\nJob Profile init task competed\n"));

        for (Node child : job.getChildren()) {
            child.accept(this);
        }

    }

    @Override
    public void visitMatch(Match match) {
        log.info("visiting Match: {}", match);

        Task<Void> myLastTaskIChain = lastTaskInChain;
        lastTaskInChain = Task.action("On Match branch init()", () -> log.info("\n\nOn Match branch init task competed.\n"));

        for (Node child : match.getMatchesChildren()) {
            child.accept(this);
        }

        Task<Void> onMatchTask = lastTaskInChain;
        lastTaskInChain = Task.action("On Non Match branch init()", () -> log.info("\n\nOn Non Match branch init task competed.\n"));

        for (Node child : match.getNonMatchesChildren()) {
            child.accept(this);
        }

        Task<Void> onNonMatchTask = lastTaskInChain;

        MatchHandler<JsonObject> matchHandler = toolKit.getMatchHandler(match.getMatchEntityType());
//        lastTaskInChain = Objects.nonNull(myLastTaskIChain) ?
//                myLastTaskIChain.flatMap("and then " + match.getName(), o -> matchHandler.match(match, jobExecutionContext, onMatchTask, onNonMatchTask)):
//                matchHandler.match(match, jobExecutionContext, onMatchTask, onNonMatchTask);
        lastTaskInChain = Objects.nonNull(myLastTaskIChain) ?
                myLastTaskIChain.flatMap("and then " + match.getName(), o -> matchHandler.match(match, jobExecutionContext, onMatchTask, onNonMatchTask)) :
                matchHandler.match(match, jobExecutionContext, onMatchTask, onNonMatchTask);
    }

    @Override
    public void visitAction(Action action) {
        log.info("visiting Action: {}", action);
        ActionHandlerSet<JsonObject> actionHandlerSet = toolKit.getActionHandlerSet(action.getEntityType());

//        lastTaskInChain = Objects.nonNull(lastTaskInChain) ?
//                lastTaskInChain.flatMap("and then " + action.getName(), o -> actionHandlerSet.actionTask(action, jobExecutionContext)) :
//                actionHandlerSet.actionTask(action, jobExecutionContext);

        lastTaskInChain = lastTaskInChain.flatMap("and then " + action.getName(), o -> actionHandlerSet.actionTask(action, jobExecutionContext));

        for (Node child : action.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public Task<Void> getProfileTask() {
        return lastTaskInChain;
    }
}
