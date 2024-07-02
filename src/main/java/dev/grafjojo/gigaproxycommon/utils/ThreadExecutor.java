package dev.grafjojo.gigaproxycommon.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadExecutor {

    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();

    public static void execute(Runnable runnable) {
        EXECUTOR.execute(runnable);
    }

    public static ExecutorService getExecutor() {
        return EXECUTOR;
    }

    public static void shutdown() {
        EXECUTOR.shutdown();
    }
}
