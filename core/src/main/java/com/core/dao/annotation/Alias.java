package com.core.dao.annotation;

import org.hibernate.Criteria;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by laizy on 2017/6/7.
 */
@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Alias {

    String value();

    int joinType() default Criteria.INNER_JOIN;
}
