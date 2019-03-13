package com.ts.test.usecase003;

import com.linkedin.parseq.Engine;
import com.linkedin.parseq.EngineBuilder;
import com.linkedin.parseq.Task;
import com.linkedin.parseq.trace.TraceUtil;
import com.ts.louisiana.engine.api.JobTaskBuilder;
import com.ts.louisiana.metadata.ActionImpl;
import com.ts.louisiana.metadata.EntityDefinitionImpl;
import com.ts.louisiana.metadata.MappingImpl;
import com.ts.louisiana.metadata.MatchCriteriaImpl;
import com.ts.louisiana.metadata.MatchImpl;
import com.ts.louisiana.metadata.api.ActionType;
import com.ts.louisiana.metadata.api.EntityDefinition;
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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UseCase003Config.class)
@TestPropertySource(locations = {"classpath:application.properties"})
@Slf4j
public class UseCase003Test001 {
    private static final String SRS_MARC_FILE_NAME = "1b74ab75-9f41-4837-8662-a1d99118008d.json";

//    private static final String CREATE_TASK_NAME_ALIAS = "createTaskNameAlias";
//    private static final String RETRIEVE_TASK_NAME_ALIAS = "retrieveTaskNameAlias";
//    private static final String CHECK_IN_CONTEXT_TASK_NAME_ALIAS = "checkTaskAlias";
//    private static final String RETRIEVE_FROM_CONTEXT_TASK_NAME_ALIAS = "retrieveFromContextTaskNameAlias";
//    private static final String RETRIEVE_FROM_REPOSITORY_TASK_NAME_ALIAS = "retrieveFromRepositoryTaskNameAlias";
//    private static final String MAP_TASK_NAME_ALIAS = "mapTaskNameAlias";
//    private static final String STORE_TASK_NAME_ALIAS = "storeTaskNameAlias";
//    private static final String BIND_TASK_NAME_ALIAS = "bindTaskNameAlias";
//
//    private static final String RETRIEVE_MASTER_TASK_NAME_ALIAS = "retrieveMasterTaskAlias";
//    private static final String CHECK_MASTER_IN_CONTEXT_TASK_NAME_ALIAS = "checkMasterTaskAlias";
//    private static final String RETRIEVE_MASTER_FROM_CONTEXT_TASK_NAME_ALIAS = "retrieveMasterFromContextTaskNameAlias";
//    private static final String RETRIEVE_MASTER_FROM_REPOSITORY_TASK_NAME_ALIAS = "retrieveMasterFromRepositoryTaskNameAlias";
//    private static final String BIND_MASTER_TASK_NAME_ALIAS = "bindMasterTaskNameAlias";
//
//    private static final String WALK_UP_THE_TREE_TASK_NAME_ALIAS = "walkUpTheTreeTaskNameAlias";
//
//    private static final String MATCH_CRITERIA_TO_QUERY_CRITERIA_TASK_NAME_ALIAS = "matchCriteriaToQueryCriteriaTaskNameAlias";
//    private static final String MATCH_FORK_TASK_NAME_ALIAS = "matchForkTaskNameAlias";
//
//    String UNDEFINED_TASK_NAME = "UNDEFINED TASK NAME";

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


    private Job createJobWithSingleMatch() {
        JobImpl job = new JobImpl("Test Job Profile 003");
        job.add(new MatchImpl("Match Order line", "ORDER_LINE", new MatchCriteriaImpl()));
        Node topLevelAction = job.getChild(0);
        topLevelAction.add(new ActionImpl("Update Instance", ActionType.UPDATE, "INSTANCE", new MappingImpl()));
        topLevelAction.add(new ActionImpl("Update Holdings", ActionType.UPDATE, "HOLDINGS", new MappingImpl()));
        topLevelAction.add(new ActionImpl("Update Item", ActionType.UPDATE, "ITEM", new MappingImpl()));



//        job.add(new ActionImpl("Update Instance", ActionType.UPDATE, "INSTANCE", new MappingImpl()));
//        job.add(new ActionImpl("Update Holdings", ActionType.UPDATE, "HOLDINGS", new MappingImpl()));
//        job.add(new ActionImpl("Update Item", ActionType.UPDATE, "ITEM", new MappingImpl()));


        log.info(Json.encodePrettily(job));
        return job;
    }

    @Test
    public void testJobWithSingleMatch() {
        universalTest(this::createJobWithSingleMatch);
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


//    @Test
//    public void dumpEntityObjects() {
//        final Map<String, EntityDefinition> knownEntities = new HashMap<>();
//        final String MARC = "MARC";
//        final String INSTANCE = "INSTANCE";
//        final String HOLDINGS = "HOLDINGS";
//        final String ITEM = "ITEM";
//        final String ORDER = "ORDER";
//        final String ORDER_LINE = "ORDER_LINE";
//        final String MARC_CAT  = "MARC_CAT";
//
//
//        Map<String, String> taskNames = new HashMap<>();
//        taskNames.put(CREATE_TASK_NAME_ALIAS, "Create an Instance");
//        taskNames.put(RETRIEVE_TASK_NAME_ALIAS, "Retrieve an Instance");
//        taskNames.put(CHECK_IN_CONTEXT_TASK_NAME_ALIAS, "Check for an Instance in context");
//        taskNames.put(RETRIEVE_FROM_CONTEXT_TASK_NAME_ALIAS, "Retrieve an Instance from context");
//        taskNames.put(RETRIEVE_FROM_REPOSITORY_TASK_NAME_ALIAS, "Retrieve an Instance from repository");
//        taskNames.put(MAP_TASK_NAME_ALIAS, "Map data to the Instance");
//        taskNames.put(STORE_TASK_NAME_ALIAS, "Store the Instance");
//        taskNames.put(BIND_TASK_NAME_ALIAS, "Bind the Instance to context");
//
//        taskNames.put(RETRIEVE_MASTER_TASK_NAME_ALIAS, "WARNING!!! Retrieve a master object");
//        taskNames.put(CHECK_MASTER_IN_CONTEXT_TASK_NAME_ALIAS, "WARNING!!! Check a master in context");
//        taskNames.put(RETRIEVE_MASTER_FROM_CONTEXT_TASK_NAME_ALIAS, "WARNING!!! Retrieve a master from context");
//        taskNames.put(RETRIEVE_MASTER_FROM_REPOSITORY_TASK_NAME_ALIAS, "WARNING!!! Retrieve a master from repository");
//
//        taskNames.put(WALK_UP_THE_TREE_TASK_NAME_ALIAS, "Walk up the tree...");
//        taskNames.put(MATCH_CRITERIA_TO_QUERY_CRITERIA_TASK_NAME_ALIAS, "Convert MatchCriteria to QueryCriteria");
//        taskNames.put(MATCH_FORK_TASK_NAME_ALIAS, "Match Fork");
//
//        knownEntities.put(INSTANCE, EntityDefinitionImpl.builder().entityType(INSTANCE).taskNames(taskNames).build());
//
//        taskNames = new HashMap<>();
//        taskNames.put(CREATE_TASK_NAME_ALIAS, "Create a Holdings");
//        taskNames.put(RETRIEVE_TASK_NAME_ALIAS, "Retrieve a Holdings");
//        taskNames.put(CHECK_IN_CONTEXT_TASK_NAME_ALIAS, "Check for a Holdings in context");
//        taskNames.put(RETRIEVE_FROM_CONTEXT_TASK_NAME_ALIAS, "Retrieve a Holdings from context");
//        taskNames.put(RETRIEVE_FROM_REPOSITORY_TASK_NAME_ALIAS, "Retrieve a Holdings from repository");
//        taskNames.put(MAP_TASK_NAME_ALIAS, "Map data to the Holdings");
//        taskNames.put(STORE_TASK_NAME_ALIAS, "Store the Holdings");
//        taskNames.put(BIND_TASK_NAME_ALIAS, "Bind the Holdings to context");
//
//        taskNames.put(RETRIEVE_MASTER_TASK_NAME_ALIAS, "Retrieve an Instance");
//        taskNames.put(CHECK_MASTER_IN_CONTEXT_TASK_NAME_ALIAS, "Check for an Instance in context");
//        taskNames.put(RETRIEVE_MASTER_FROM_CONTEXT_TASK_NAME_ALIAS, "Retrieve an Instance from context");
//        taskNames.put(RETRIEVE_MASTER_FROM_REPOSITORY_TASK_NAME_ALIAS, "Retrieve an Instance from repository");
//        taskNames.put(BIND_MASTER_TASK_NAME_ALIAS, "Bind the Instance to context");
//
//        taskNames.put(WALK_UP_THE_TREE_TASK_NAME_ALIAS, "Walk up the tree...");
//        taskNames.put(MATCH_CRITERIA_TO_QUERY_CRITERIA_TASK_NAME_ALIAS, "Convert MatchCriteria to QueryCriteria");
//        taskNames.put(MATCH_FORK_TASK_NAME_ALIAS, "Match Fork");
//
//        knownEntities.put(HOLDINGS, EntityDefinitionImpl.builder().entityType(HOLDINGS).masterEntityType(INSTANCE).taskNames(taskNames).build());
//
//        taskNames = new HashMap<>();
//        taskNames.put(CREATE_TASK_NAME_ALIAS, "Create an Item");
//        taskNames.put(RETRIEVE_TASK_NAME_ALIAS, "Retrieve an Item");
//        taskNames.put(CHECK_IN_CONTEXT_TASK_NAME_ALIAS, "Check for an Item in context");
//        taskNames.put(RETRIEVE_FROM_CONTEXT_TASK_NAME_ALIAS, "Retrieve an Item from context");
//        taskNames.put(RETRIEVE_FROM_REPOSITORY_TASK_NAME_ALIAS, "Retrieve an Item from repository");
//        taskNames.put(MAP_TASK_NAME_ALIAS, "Map data to the Item");
//        taskNames.put(STORE_TASK_NAME_ALIAS, "Store the Item");
//        taskNames.put(BIND_TASK_NAME_ALIAS, "Bind the Item to context");
//
//        taskNames.put(RETRIEVE_MASTER_TASK_NAME_ALIAS, "Retrieve a Holdings");
//        taskNames.put(CHECK_MASTER_IN_CONTEXT_TASK_NAME_ALIAS, "Check for a Holdings in context");
//        taskNames.put(RETRIEVE_MASTER_FROM_CONTEXT_TASK_NAME_ALIAS, "Retrieve a Holdings from context");
//        taskNames.put(RETRIEVE_MASTER_FROM_REPOSITORY_TASK_NAME_ALIAS, "Retrieve a Holdings from repository");
//        taskNames.put(BIND_MASTER_TASK_NAME_ALIAS, "Bind the Holdings to context");
//
//        taskNames.put(WALK_UP_THE_TREE_TASK_NAME_ALIAS, "Walk up the tree...");
//        taskNames.put(MATCH_CRITERIA_TO_QUERY_CRITERIA_TASK_NAME_ALIAS, "Convert MatchCriteria to QueryCriteria");
//        taskNames.put(MATCH_FORK_TASK_NAME_ALIAS, "Match Fork");
//
//        knownEntities.put(ITEM, EntityDefinitionImpl.builder().entityType(ITEM).masterEntityType(HOLDINGS).taskNames(taskNames).build());
//
//        knownEntities.put(ORDER, EntityDefinitionImpl.builder().entityType(ORDER).masterEntityType(ITEM).build());
//
//        taskNames = new HashMap<>();
//        taskNames.put(CREATE_TASK_NAME_ALIAS, "Create an Order line");
//        taskNames.put(RETRIEVE_TASK_NAME_ALIAS, "Retrieve an Order line");
//        taskNames.put(CHECK_IN_CONTEXT_TASK_NAME_ALIAS, "Check for an Order line in context");
//        taskNames.put(RETRIEVE_FROM_CONTEXT_TASK_NAME_ALIAS, "Retrieve an Order line from context");
//        taskNames.put(RETRIEVE_FROM_REPOSITORY_TASK_NAME_ALIAS, "Retrieve an Order line from repository");
//        taskNames.put(MAP_TASK_NAME_ALIAS, "Map data to the Order line");
//        taskNames.put(STORE_TASK_NAME_ALIAS, "Store the Order line");
//        taskNames.put(BIND_TASK_NAME_ALIAS, "Bind the Item to Order line");
//
//        taskNames.put(RETRIEVE_MASTER_TASK_NAME_ALIAS, "Retrieve an Item");
//        taskNames.put(CHECK_MASTER_IN_CONTEXT_TASK_NAME_ALIAS, "Check for an Item in context");
//        taskNames.put(RETRIEVE_MASTER_FROM_CONTEXT_TASK_NAME_ALIAS, "Retrieve an Item from context");
//        taskNames.put(RETRIEVE_MASTER_FROM_REPOSITORY_TASK_NAME_ALIAS, "Retrieve an Item from repository");
//        taskNames.put(BIND_MASTER_TASK_NAME_ALIAS, "Bind the Item to context");
//
//        taskNames.put(WALK_UP_THE_TREE_TASK_NAME_ALIAS, "Walk up the tree...");
//        taskNames.put(MATCH_CRITERIA_TO_QUERY_CRITERIA_TASK_NAME_ALIAS, "Convert MatchCriteria to QueryCriteria");
//        taskNames.put(MATCH_FORK_TASK_NAME_ALIAS, "Match Fork");
//
//        knownEntities.put(ORDER_LINE, EntityDefinitionImpl.builder().entityType(ORDER_LINE).masterEntityType(ITEM).taskNames(taskNames).build());
//
//        knownEntities.put(MARC_CAT, EntityDefinitionImpl.builder().entityType(MARC_CAT).build());
//
//        knownEntities.put(MARC, EntityDefinitionImpl.builder().entityType(MARC).build());
//
//        log.info(Json.encodePrettily(knownEntities));
//
//    }
}
