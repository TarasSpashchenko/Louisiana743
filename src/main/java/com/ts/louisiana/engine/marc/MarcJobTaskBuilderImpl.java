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
import com.ts.louisiana.metadata.EntityType;
import com.ts.louisiana.metadata.Job;
import com.ts.louisiana.types.ContextObject;
import com.ts.louisiana.types.MarcEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class MarcJobTaskBuilderImpl implements JobTaskBuilder<MarcEntity>, ToolKit {

    private Map<Pair<EntityType, EntityType>, MatchHandler<ContextObject<?>, ContextObject<?>>>
            matchProcessorsByEntityType = new HashMap<>();

    private Map<EntityType, ActionHandlerSet> actionHandlerSetByEntityType = new HashMap<>();

    @Autowired
    private JobExecutionContextConstructor jobExecutionContextConstructor;

    @Autowired
    private JobHandler jobHandler;

    @Autowired
    @Qualifier("instanceActionHandlerSet")
    private ActionHandlerSet instanceActionHandlerSet;

    @Autowired
    @Qualifier("holdingsActionHandlerSet")
    private ActionHandlerSet holdingsActionHandlerSet;

    @Override
    public Task<?> buildJobTask(Job job, MarcEntity sourceRecordEntity) {
        //TODO: JobExecutionContext should be used here
        JobExecutionContext jobExecutionContext =
                jobExecutionContextConstructor.createJobExecutionContext(EntityType.MARC, sourceRecordEntity);

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
    public <P, R> MatchHandler<P, R> getMatchHandler(EntityType from, EntityType to) {
        MatchHandler<P, R> matchHandler = (MatchHandler<P, R>) matchProcessorsByEntityType.get(Pair.of(from, to));

        return matchHandler;
    }

    @Override
    public ActionHandlerSet getActionHandlerSet(EntityType entityType) {
        return actionHandlerSetByEntityType.get(entityType);
    }

    @PostConstruct
    private void init() {
//
        // TODO: fill matchProcessorsByEntityType here
//
        actionHandlerSetByEntityType.put(EntityType.INSTANCE, instanceActionHandlerSet);
        actionHandlerSetByEntityType.put(EntityType.HOLDINGS, holdingsActionHandlerSet);


    }
}
