package com.ts.louisiana.engine.marc;

import com.linkedin.parseq.Task;
import com.ts.louisiana.engine.api.ActionHandlerSet;
import com.ts.louisiana.engine.api.JobExecutionContext;
import com.ts.louisiana.engine.api.ProfileVisitor;
import com.ts.louisiana.engine.api.ToolKit;
import com.ts.louisiana.metadata.Action;
import com.ts.louisiana.metadata.Job;
import com.ts.louisiana.metadata.Match;
import com.ts.louisiana.metadata.Node;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MarcProfileVisitorV2Impl implements ProfileVisitor {
    private final ToolKit toolKit;

    private final JobExecutionContext jobExecutionContext;

    private Task<?> lastTaskInChain;

    public MarcProfileVisitorV2Impl(ToolKit toolKit, JobExecutionContext jobExecutionContext) {
        this.toolKit = toolKit;
        this.jobExecutionContext = jobExecutionContext;
    }

    @Override
    public void visitJob(Job job) {
        log.info("visiting Job: {}", job);
        lastTaskInChain = Task.action("Job Profile init()", ()-> log.info("Job Profile init task competed"));

        for (Node child : job.getChildren()) {
            if (child instanceof Action) {
                child.accept(this);
            }
        }

    }

    @Override
    public void visitMatch(Match match) {

    }

    @Override
    public void visitAction(Action action) {
        log.info("visiting Action: {}", action);
        ActionHandlerSet actionHandlerSet = toolKit.getActionHandlerSet(action.getEntityType());

        lastTaskInChain =
                 lastTaskInChain.flatMap("and then " + action.getName(), o -> actionHandlerSet.actionTask(action, jobExecutionContext));

//        lastTaskInChain =
//                Task.flatten("flatten: on " + action.getName(), lastTaskInChain.map(action.getName(), o -> actionHandlerSet.actionTask(action, jobExecutionContext)));
        for (Node child : action.getChildren()) {
            if (child instanceof Action) {
                child.accept(this);
            }
        }

    }

    @Override
    public Task<?> getProfileTask() {
        return lastTaskInChain;
    }
}
