package com.core.web.freemarker;

import java.lang.annotation.*;

/**
 * Created by laizy on 2017/4/14.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FreemarkerEnum {
    String name();
}
