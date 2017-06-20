package com.core.job;

/**
 * Created by laizy on 2017/6/7.
 */
public interface TimeoutJob {
    /**
     * @return  -1 means not yet start
     */
    public long startTime();

    public void run() throws JobException;

    /**
     * @param executeThread : executeThread.interrupt()
     * @throws JobException
     */
    public void about(Thread executeThread) throws JobException;

    public boolean checkTimeout();
}
