package com.core.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Created by laizy on 2017/6/7.
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
@Component
@Inherited
public @interface Env {
    String name();
    Class<?> superClass();
}
