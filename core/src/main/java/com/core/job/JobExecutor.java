package com.core.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Created by laizy on 2017/6/8.
 */
public class JobExecutor {
    private static final ExecutorService executor = ThreadPoolUtil.newCachedExecutor("cache-job");
    private static Logger logger = LoggerFactory.getLogger(JobExecutor.class);

    public static void execute(final TimeoutJob timeoutJob) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    timeoutJob.run();
                } catch (JobException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        });
    }

    public static <T> void executeInCurrentThread(CurrentThreadJob<T> currentThreadJob) throws JobException {
        if (JobPool.getInstance().add(currentThreadJob)) {
            try {
                currentThreadJob.run();
            } finally {
                JobPool.getInstance().remove(currentThreadJob);
            }
        } else {
            //not occur
            logger.warn("currentThread is executing the job, not occur");
        }
    }

    public static <X> Future<X> execute(Callable<X> callable) {
        return executor.submit(callable);
    }
}
