package com.core.job;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by laizy on 2017/6/7.
 */
public class SimpleThreadFactory implements ThreadFactory {
    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;

    public SimpleThreadFactory(String prefix) {
        group = Thread.currentThread().getThreadGroup();
        namePrefix = "pool-"+prefix+"-thread-";
    }

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(group, r, namePrefix + threadNumber.getAndIncrement());
    }
}
