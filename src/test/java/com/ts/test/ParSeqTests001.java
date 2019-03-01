package com.ts.test;

import com.linkedin.parseq.Engine;
import com.linkedin.parseq.EngineBuilder;
import com.linkedin.parseq.Task;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ParSeqTests001 {

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



    @Test
    public void initShutdownTest() {
        EngineContainer engineContainer = initParSeqEngine();

        shutdownParSeqEngine(engineContainer);
    }

    @Test
    public void firstSimpleTaskTest() {
        EngineContainer engineContainer = initParSeqEngine();
        try {
            Task<String> task = Task.callable("current time", () -> new Date().toString()); //.andThen("print it", System.out::println);
            task = task.map("upper case", String::toUpperCase);

            task = task.andThen("print it", System.out::println);
            Engine parseqEngine = engineContainer.getParseqEngine();
            parseqEngine.run(task);


        }
        finally {
            shutdownParSeqEngine(engineContainer);
        }
    }

    @Test
    public void testAA001() {
        int x;
        int y;

        x = y = 7;

        System.out.println("x:" + x + " y: " + y);
    }

}
