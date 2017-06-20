package com.core.param;

/**
 * Created by laizy on 2017/6/7.
 */
public interface ParamListener<T,V> {
    void invoke(T t, V v);

    public static final EmptyParamListener empty = new EmptyParamListener();

    public static final class EmptyParamListener implements ParamListener{
        @Override
        public void invoke(Object o, Object o1) {
            //null;
        }
    }
}
