package com.core.annotation;


import com.core.param.ParamListener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * must public <br/>
 * only support <br/>
 * Boolean,Long,Integer,Float,Double,Short,String  <br/>
 * primitive type: boolean,long,int,float,double,short
 * @author laizy
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface Param {
    String key();
    String description() default "";
    Class<? extends ParamListener> listener() default ParamListener.class;
}
