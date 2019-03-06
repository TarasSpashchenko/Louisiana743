package com.ts.louisiana.engine.marc;

import com.linkedin.parseq.Task;
import com.ts.louisiana.engine.api.ActionHandlerSet;
import com.ts.louisiana.engine.api.JobExecutionContext;
import com.ts.louisiana.engine.api.JobExecutionContextConstructor;
import com.ts.louisiana.engine.api.JobHandler;
import com.ts.louisiana.engine.api.JobTaskBuilder;
import com.ts.louisiana.engine.api.MatchHandler;
import com.ts.louisiana.engine.api.ProfileVisitor;
import com.ts.louisiana.engine.api.ToolKit;
import com.ts.louisiana.metadata.api.Job;
import com.ts.louisiana.types.EntityObject;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MarcJobTaskBuilderImpl implements JobTaskBuilder<JsonObject>, ToolKit<JsonObject> {

    private JobExecutionContextConstructor jobExecutionContextConstructor;

    private JobHandler jobHandler;

    private ActionHandlerSet<JsonObject> instanceActionHandlerSet;

    private MatchHandler<JsonObject> matchHandler;

    @Override
    public Task<?> buildJobTask(Job job, EntityObject<JsonObject> sourceRecordEntity) {
        JobExecutionContext<JsonObject> jobExecutionContext =
                jobExecutionContextConstructor.createJobExecutionContext(sourceRecordEntity);

        ProfileVisitor profileVisitor = new MarcProfileVisitorV2Impl(this, jobExecutionContext);
        job.accept(profileVisitor);

        //TODO: get visitor result somehow
        return profileVisitor.getProfileTask();
    }

    @Override
    public JobHandler getJobHandler() {
        return jobHandler;
    }

    @Override
    public MatchHandler<JsonObject> getMatchHandler(String entityType) {
        return matchHandler;
    }

    @Override
    public ActionHandlerSet<JsonObject> getActionHandlerSet(String entityType) {
        return instanceActionHandlerSet;
    }

    @Autowired
    public void setJobExecutionContextConstructor(JobExecutionContextConstructor jobExecutionContextConstructor) {
        this.jobExecutionContextConstructor = jobExecutionContextConstructor;
    }

    @Autowired
    public void setJobHandler(JobHandler jobHandler) {
        this.jobHandler = jobHandler;
    }

    @Autowired
    public void setInstanceActionHandlerSet(ActionHandlerSet<JsonObject> instanceActionHandlerSet) {
        this.instanceActionHandlerSet = instanceActionHandlerSet;
    }

    @Autowired
    public void setMatchHandler(MatchHandler<JsonObject> matchHandler) {
        this.matchHandler = matchHandler;
    }

}
