package com.ts.test.usecase003;

import com.linkedin.parseq.Engine;
import com.linkedin.parseq.EngineBuilder;
import com.linkedin.parseq.Task;
import com.linkedin.parseq.trace.TraceUtil;
import com.ts.louisiana.engine.api.JobTaskBuilder;
import com.ts.louisiana.metadata.ActionImpl;
import com.ts.louisiana.metadata.MappingImpl;
import com.ts.louisiana.metadata.api.ActionType;
import com.ts.louisiana.metadata.api.Job;
import com.ts.louisiana.metadata.JobImpl;
import com.ts.louisiana.metadata.api.Mapping;
import com.ts.louisiana.metadata.api.Node;
import com.ts.louisiana.types.EntityObject;
import com.ts.louisiana.types.stub.EntityObjectyImpl;
import com.ts.test.EngineContainer;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UseCase003Config.class)
@TestPropertySource(locations = {"classpath:application.properties"})
@Slf4j
public class UseCase003Test001 {
    private static final String SRS_MARC_FILE_NAME = "1b74ab75-9f41-4837-8662-a1d99118008d.json";

    @Autowired
    private JobTaskBuilder<JsonObject> jobTaskBuilder;

    private Engine parseqEngine;
    private ExecutorService taskScheduler;
    private ScheduledExecutorService timerScheduler;


    private EngineContainer initParSeqEngine() {
        final int numCores = Runtime.getRuntime().availableProcessors();
        taskScheduler = Executors.newFixedThreadPool(numCores + 1);
        timerScheduler = Executors.newSingleThreadScheduledExecutor();

        parseqEngine = new EngineBuilder()
                .setTaskExecutor(taskScheduler)
                .setTimerScheduler(timerScheduler)
                .build();

        return new EngineContainer() {

            @Override
            public Engine getParseqEngine() {
                return parseqEngine;
            }

            @Override
            public ExecutorService getAssignedTaskScheduler() {
                return taskScheduler;
            }

            @Override
            public ScheduledExecutorService getAssignedTimerScheduler() {
                return timerScheduler;
            }
        };
    }

    private void shutdownParSeqEngine(EngineContainer parseqEngineContainer) {

        Engine localParseqEngine = parseqEngineContainer.getParseqEngine();
        localParseqEngine.shutdown();
        try {
            localParseqEngine.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }

        parseqEngineContainer.getAssignedTaskScheduler().shutdown();
        parseqEngineContainer.getAssignedTimerScheduler().shutdown();
    }

//    @Test
//    public void firstSimpleTaskTest() {
//
//        MarcEntity marcEntity = getSourceRecordStorageMarcEntity();
//        Job job = getJob();
//        Match match1 = job.getChild(0);
//
//        Task<ItemEntity> match = marcEntityItemEntityMatchProcessor.match(match1.getMatchCriteria(), marcEntity);
//
//
//
////        match.map("Match Condition", itemEntity -> {
////            if
////        })
//
////        match.transform("asdf", itemEntityTry -> itemEntityTry)
//
//
//        EngineContainer engineContainer = initParSeqEngine();
//        try {
//            parseqEngine.run(match);
//            match.await();
//
//            log.info("ItemEntity: {}", match.get());
//            log.info("\n{}\n", TraceUtil.getJsonTrace(match));
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//            Thread.currentThread().interrupt();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } finally {
//            shutdownParSeqEngine(engineContainer);
//        }
//
//    }


    private Job getEmptyJob() {
        JobImpl job = new JobImpl();
        return job;
    }

    @Test
    public void simpleTest001() {
        Task<?> jobTask = jobTaskBuilder.buildJobTask(getEmptyJob(), getSourceRecordStorageMarcEntity());

        EngineContainer engineContainer = initParSeqEngine();
        try {
            parseqEngine.run(jobTask);
            jobTask.await();

        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } finally {
            shutdownParSeqEngine(engineContainer);
        }

    }


    private Job createJobWithCreateActions() {
        JobImpl job = new JobImpl();

        job.add(new ActionImpl(ActionType.CREATE, "INSTANCE", new MappingImpl()));

        Node topLevelAction = job.getChild(0);
        topLevelAction.add(new ActionImpl(ActionType.CREATE, "HOLDINGS", new MappingImpl()));
        topLevelAction.add(new ActionImpl(ActionType.CREATE, "ITEM", new MappingImpl()));
        topLevelAction.getChild(0).add(new ActionImpl(ActionType.CREATE, "ITEM", new MappingImpl()));

        job.add(new ActionImpl(ActionType.CREATE, "HOLDINGS", new MappingImpl()));
        job.add(new ActionImpl(ActionType.CREATE, "ITEM", new MappingImpl()));
        job.add(new ActionImpl(ActionType.CREATE, "MARCCAT", new MappingImpl()));

        return job;
    }

    private Job createJobWithCreateInstanceActions() {
        JobImpl job = new JobImpl("Test Job Profile 002");

        job.add(new ActionImpl("Create Instance 1", ActionType.CREATE, "INSTANCE", new MappingImpl()));
        Node topLevelAction = job.getChild(0);
        topLevelAction.add(new ActionImpl("Create Instance 1.1", ActionType.CREATE, "INSTANCE", new MappingImpl()));
        topLevelAction.add(new ActionImpl("Create Holdings 1.1", ActionType.CREATE, "HOLDINGS", new MappingImpl()));

        job.add(new ActionImpl("Create Instance 2", ActionType.CREATE, "INSTANCE", new MappingImpl()));
        topLevelAction = job.getChild(1);
        topLevelAction.add(new ActionImpl("Create Instance 2.1", ActionType.CREATE, "INSTANCE", new MappingImpl()));
        topLevelAction.add(new ActionImpl("Create Holdings 2.1", ActionType.CREATE, "HOLDINGS", new MappingImpl()));


        job.add(new ActionImpl("Create Instance 3", ActionType.CREATE, "INSTANCE", new MappingImpl()));
        job.add(new ActionImpl("Create Holdings 1", ActionType.CREATE, "HOLDINGS", new MappingImpl()));

        log.info(Json.encodePrettily(job));

        return job;
    }

    @Test
    public void simpleTest002() {
        Task<?> jobTask = jobTaskBuilder.buildJobTask(createJobWithCreateInstanceActions(), getSourceRecordStorageMarcEntity());
//        jobTask = jobTask.onFailure("Error ", Throwable::printStackTrace);


        EngineContainer engineContainer = initParSeqEngine();
        try {
            parseqEngine.run(jobTask);
            jobTask.await();
            log.info("\n{}\n", TraceUtil.getJsonTrace(jobTask));

        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            shutdownParSeqEngine(engineContainer);
        }

    }

    private Job createJobWithUpdateInstanceActions() {
        JobImpl job = new JobImpl("Test Job Profile 003");
        job.add(new ActionImpl("Create Item", ActionType.CREATE, "ITEM", new MappingImpl()));
        job.add(new ActionImpl("Create OrderLine", ActionType.CREATE, "ORDER_LINE", new MappingImpl()));
        job.add(new ActionImpl("Update Instance", ActionType.UPDATE, "INSTANCE", new MappingImpl()));
        job.add(new ActionImpl("Update Holdings", ActionType.UPDATE, "HOLDINGS", new MappingImpl()));
        job.add(new ActionImpl("Update Item", ActionType.UPDATE, "ITEM", new MappingImpl()));

        log.info(Json.encodePrettily(job));
        return job;
    }

    @Test
    public void simpleTest003() {
        Task<?> jobTask = jobTaskBuilder.buildJobTask(createJobWithUpdateInstanceActions(), getSourceRecordStorageMarcEntity());
//        jobTask = jobTask.onFailure("Error ", Throwable::printStackTrace);


        EngineContainer engineContainer = initParSeqEngine();
        try {
            parseqEngine.run(jobTask);
            jobTask.await();
            log.info("\n{}\n", TraceUtil.getJsonTrace(jobTask));

        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            shutdownParSeqEngine(engineContainer);
        }

    }

    private Job createJobWithCreateUpdateInstanceActions() {
        JobImpl job = new JobImpl("Test Job Profile 003");
        job.add(new ActionImpl("Create Instance", ActionType.CREATE, "INSTANCE", new MappingImpl()));
        job.add(new ActionImpl("Update Instance", ActionType.UPDATE, "INSTANCE", new MappingImpl()));
//        job.add(new ActionImpl("Update Holdings", ActionType.UPDATE, "HOLDINGS", new MappingImpl()));
//        job.add(new ActionImpl("Update Item", ActionType.UPDATE, "ITEM", new MappingImpl()));

        log.info(Json.encodePrettily(job));
        return job;
    }

    @Test
    public void simpleTest004() {
        Task<?> jobTask = jobTaskBuilder.buildJobTask(createJobWithCreateUpdateInstanceActions(), getSourceRecordStorageMarcEntity());
//        jobTask = jobTask.onFailure("Error ", Throwable::printStackTrace);


        EngineContainer engineContainer = initParSeqEngine();
        try {
            parseqEngine.run(jobTask);
            jobTask.await();
            log.info("\n{}\n", TraceUtil.getJsonTrace(jobTask));

        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            shutdownParSeqEngine(engineContainer);
        }

    }


    private EntityObject<JsonObject> getSourceRecordStorageMarcEntity() {
        try (InputStream marcRecord = Thread.currentThread().getContextClassLoader().getResourceAsStream(SRS_MARC_FILE_NAME)) {
            JsonObject jsonObject = new JsonObject(IOUtils.toString(marcRecord, StandardCharsets.UTF_8));
            return new EntityObjectyImpl("MARC", jsonObject);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Task<Void> matchFoundActionTask(Boolean b) {
        return Task.action("Match found action sequence", () -> System.out.println("Match found action sequence performed " + b));
    }

    private Task<Void> matchNotFoundActionTask() {
        return Task.action("Match not found action sequence", () -> System.out.println("Match not found action sequence performed"));
    }

    private Task<Void> forkTask(Boolean b) {
        return Boolean.TRUE.equals(b) ? matchFoundActionTask(b) : matchNotFoundActionTask();
    }

    private void testTaskCondition(Boolean matchResult) {
//        Task<Void> matchFoundAction = Task.action("Match found action sequence", () -> System.out.println("Match found action sequence performed"));
//        Task<Void> matchNotFoundAction = Task.action("Match not found action sequence", () -> System.out.println("Match not found action sequence performed"));

        Task<Boolean> match = Task.callable("Match", () -> matchResult);

        Task<Void> matchFork = match.flatMap("Match fork", this::forkTask);

        EngineContainer engineContainer = initParSeqEngine();
        try {
            parseqEngine.run(matchFork);
            matchFork.await();
            log.info("\n{}\n", TraceUtil.getJsonTrace(matchFork));

        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            shutdownParSeqEngine(engineContainer);
        }

    }


    @Test
    public void testTaskCondition() {
        testTaskCondition(Boolean.TRUE);
        testTaskCondition(Boolean.FALSE);
    }

}
