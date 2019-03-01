package com.ts.louisiana.engine.api;

import com.linkedin.parseq.Task;
import com.ts.louisiana.metadata.Action;
import com.ts.louisiana.metadata.Job;
import com.ts.louisiana.metadata.Match;

public interface ProfileVisitor {
    void visitJob(Job job);
    void visitMatch(Match match);
    void visitAction(Action action);

    Task<?> getProfileTask();
}
