package com.core.job;

/**
 * Created by laizy on 2017/6/8.
 */
public final class JobPool extends TimeoutPool<TimeoutJob>{
    private JobPool(){}
    private static JobPool instance = null;
    public static JobPool getInstance() {
        if (instance == null) {
            synchronized (JobPool.class) {
                if (instance == null) {
                    JobPool jobPool = new JobPool();
                    jobPool.start();
                    instance = jobPool;
                }
            }
        }
        return instance;
    }

}
