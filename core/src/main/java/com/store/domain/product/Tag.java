package com.store.domain.product;

import com.core.web.freemarker.FreemarkerEnum;

/**
 * Created by laizy on 2017/6/13.
 */
@FreemarkerEnum(name = "ProductTag")
public enum Tag {
    NEW("新品"),
    HOT("热卖"),
    RECOMMEND("推荐"),
    FACIAL("招牌");

    private String alias;

    Tag(String alias) {
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }
}
