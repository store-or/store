package com.core.param;

import com.core.annotation.Param;
import com.core.system.Initialize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * Created by laizy on 2017/6/7.
 */
public abstract class ParamManage<T> implements Initialize {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected abstract boolean modify(T key,String value);

    @SuppressWarnings("unchecked")
    protected final void invokeModify(Field field, String value, Object obj, Param param) throws Exception{
        field.setAccessible(true);
        Class<?> clazz = field.getType();
        Object tmp = value;
        if (String.class == clazz) {
            //
        } else if (Boolean.class == clazz || Boolean.TYPE == clazz) {
            tmp = Boolean.valueOf(value);
        } else if (Long.class == clazz || Long.TYPE == clazz) {
            tmp = Long.valueOf(value);
        } else if (Double.class == clazz || Double.TYPE == clazz) {
            tmp = Double.valueOf(value);
        } else if (Integer.class == clazz || Integer.TYPE == clazz) {
            tmp = Integer.valueOf(value);
        } else if (Short.class == clazz || Short.TYPE == clazz) {
            tmp = Short.valueOf(value);
        } else if (Float.class == clazz || Float.TYPE == clazz) {
            tmp = Float.valueOf(value);
        } else {
            throw new UnsupportedOperationException("not support type." + clazz.getName());
        }
        field.set(obj,tmp);
        Class<? extends ParamListener> modify =  param.listener();
        if (!modify.isInterface()) {
            ParamListener paramListener = modify.newInstance();
            paramListener.invoke(obj, tmp);
        }
    }

}