package com.core.job;

/**
 * Created by laizy on 2017/6/7.
 */
public abstract class CurrentThreadJob<T> implements TimeoutJob {

    public abstract T getResult();

}
