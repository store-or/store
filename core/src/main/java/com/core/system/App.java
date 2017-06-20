package com.core.system;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * Created by laizy on 2017/6/7.
 */
public abstract class App {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected static ApplicationContext context;

    static Table<String, Class<?>, Object> table = HashBasedTable.create();

    static Reflections reflections = null;
    String basePackage;

    /**
     * must initialize ConfigParam
     * @throws Throwable : 初始化失败
     */
    public abstract void contextInitialize() throws Throwable;

    public static <T> T getBean(String name,Class<T> t) {
        Object bean = context.getBean(name);
        if (t.isAssignableFrom(bean.getClass())) {
            return t.cast(bean);
        } else {
            return null;
        }
    }

    public static <T> T getBean(Class<T> t) {
        return context.getBean(t);
    }

    public static <T>Map<String,T> getBeansOfType(Class<T> t) {
        return context.getBeansOfType(t);
    }

    public static Map<String,Object> getBeansWithAnnotation(Class<? extends Annotation> clazz) {
        return context.getBeansWithAnnotation(clazz);
    }

    @SuppressWarnings("unchecked")
    public static <X> X getEnvX(String name, Class<? super X> clazz) {
        Object obj = table.get(name, clazz);
        if (obj != null && clazz.isInstance(obj)) {
            return (X) obj;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <X> Class<X> getEnvXClass(String name, Class<? super X> clazz) {
        Object obj = table.get(name, clazz);
        if (obj != null && clazz.isInstance(obj)) {
            return (Class<X>) obj.getClass();
        }
        return null;
    }

    public static Map<String,Object> columns(Class<?> clazz) {
        Map<Class<?>, Map<String, Object>> classMapMap = table.columnMap();
        return classMapMap.get(clazz);
    }

    public static <T> Set<Class<? extends T>> getSubTypesOf(Class<T> superClass) {
        if (reflections != null) {
            return reflections.getSubTypesOf(superClass);
        } else {
            return Collections.emptySet();
        }
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }
}
