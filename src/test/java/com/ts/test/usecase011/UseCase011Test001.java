package com.ts.test.usecase011;

import com.linkedin.parseq.Engine;
import com.linkedin.parseq.EngineBuilder;
import com.linkedin.parseq.Task;
import com.linkedin.parseq.trace.TraceUtil;
import com.ts.louisiana.engine.api.JobTaskBuilder;
import com.ts.louisiana.metadata.ActionImpl;
import com.ts.louisiana.metadata.JobImpl;
import com.ts.louisiana.metadata.MappingImpl;
import com.ts.louisiana.metadata.MatchCriteriaImpl;
import com.ts.louisiana.metadata.MatchImpl;
import com.ts.louisiana.metadata.api.ActionType;
import com.ts.louisiana.metadata.api.Job;
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
import java.util.function.Supplier;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UseCase011Config.class)
@TestPropertySource(locations = {"classpath:application.properties"})
@Slf4j
public class UseCase011Test001 {
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

    private EntityObject<JsonObject> getSourceRecordStorageMarcEntity() {
        try (InputStream marcRecord = Thread.currentThread().getContextClassLoader().getResourceAsStream(SRS_MARC_FILE_NAME)) {
            JsonObject jsonObject = new JsonObject(IOUtils.toString(marcRecord, StandardCharsets.UTF_8));
            return new EntityObjectyImpl("MARC", jsonObject);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void universalTest(Supplier<Job> jobSupplier) {
        Task<?> jobTask = jobTaskBuilder.buildJobTask(jobSupplier.get(), getSourceRecordStorageMarcEntity());
        jobTask = jobTask.onFailure("Error handler", Throwable::printStackTrace);

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

    private Job createJobForCase011Option01() {
        JobImpl job = new JobImpl("Test Job Profile 011");

        MatchCriteriaImpl matchCriteria = new MatchCriteriaImpl();
        matchCriteria.setCriteriaDetails("false");
        job.add(new MatchImpl("Search for Instance by 035 field", "INSTANCE", matchCriteria));

        MatchImpl topLevelAction = job.getChild(0);
        topLevelAction.addNonMatches(new ActionImpl("Create an Instance from MARC", ActionType.CREATE, "INSTANCE", new MappingImpl()));

        matchCriteria = new MatchCriteriaImpl();
        matchCriteria.setCriteriaDetails("false");
        job.add(new MatchImpl("Search for a Holdings by Location", "HOLDINGS", matchCriteria));

        topLevelAction = job.getChild(1);
        topLevelAction.addNonMatches(new ActionImpl("Create a Holdings from MARC", ActionType.CREATE, "HOLDINGS", new MappingImpl()));

        job.add(new ActionImpl("Create an Item from MARC", ActionType.CREATE, "ITEM", new MappingImpl()));

        log.info(Json.encodePrettily(job));
        return job;
    }

    @Test
    public void testJobOption01() {
        universalTest(this::createJobForCase011Option01);
    }

    private Job createJobForCase011Option02() {
        JobImpl job = new JobImpl("Test Job Profile 011");

        MatchCriteriaImpl matchCriteria = new MatchCriteriaImpl();
        matchCriteria.setCriteriaDetails("true");
        job.add(new MatchImpl("Search for Instance by 035 field", "INSTANCE", matchCriteria));

        MatchImpl topLevelAction = job.getChild(0);
        topLevelAction.addNonMatches(new ActionImpl("Create an Instance from MARC", ActionType.CREATE, "INSTANCE", new MappingImpl()));

        matchCriteria = new MatchCriteriaImpl();
        matchCriteria.setCriteriaDetails("false");
        job.add(new MatchImpl("Search for a Holdings by Location", "HOLDINGS", matchCriteria));

        topLevelAction = job.getChild(1);
        topLevelAction.addNonMatches(new ActionImpl("Create a Holdings from MARC", ActionType.CREATE, "HOLDINGS", new MappingImpl()));

        job.add(new ActionImpl("Create an Item from MARC", ActionType.CREATE, "ITEM", new MappingImpl()));

        log.info(Json.encodePrettily(job));
        return job;
    }

    @Test
    public void testJobOption02() {
        universalTest(this::createJobForCase011Option02);
    }

    private Job createJobForCase011Option03() {
        JobImpl job = new JobImpl("Test Job Profile 011");

        MatchCriteriaImpl matchCriteria = new MatchCriteriaImpl();
        matchCriteria.setCriteriaDetails("true");
        job.add(new MatchImpl("Search for Instance by 035 field", "INSTANCE", matchCriteria));

        MatchImpl topLevelAction = job.getChild(0);
        topLevelAction.addNonMatches(new ActionImpl("Create an Instance from MARC", ActionType.CREATE, "INSTANCE", new MappingImpl()));

        matchCriteria = new MatchCriteriaImpl();
        matchCriteria.setCriteriaDetails("true");
        job.add(new MatchImpl("Search for a Holdings by Location", "HOLDINGS", matchCriteria));

        topLevelAction = job.getChild(1);
        topLevelAction.addNonMatches(new ActionImpl("Create a Holdings from MARC", ActionType.CREATE, "HOLDINGS", new MappingImpl()));

        job.add(new ActionImpl("Create an Item from MARC", ActionType.CREATE, "ITEM", new MappingImpl()));
//        job.add(new ActionImpl("Create an MY_NEW_ENTITY from MARC", ActionType.CREATE, "MY_NEW_ENTITY", new MappingImpl()));
////        "MY_NEW_ENTITY"

        log.info(Json.encodePrettily(job));
        return job;
    }

    @Test
    public void testJobOption03() {
        universalTest(this::createJobForCase011Option03);
    }

}
