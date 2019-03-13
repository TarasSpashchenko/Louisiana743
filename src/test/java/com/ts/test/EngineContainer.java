package com.ts.test;

import com.linkedin.parseq.Engine;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

public interface EngineContainer {
    Engine getParseqEngine();

    ExecutorService getAssignedTaskScheduler();

    ScheduledExecutorService getAssignedTimerScheduler();
}
