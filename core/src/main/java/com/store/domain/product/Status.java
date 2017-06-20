package com.store.domain.product;

import com.core.web.freemarker.FreemarkerEnum;

/**
 * Created by laizy on 2017/6/13.
 */
@FreemarkerEnum(name = "ProductStatus")
public enum Status {
    ON("上架", 1),
    OFF("下架", 0);

    private String alias;
    private Integer value;

    Status(String alias, Integer value) {
        this.alias = alias;
        this.value = value;
    }

    public String getAlias() {
        return alias;
    }

    public Integer getValue() {
        return value;
    }
}
