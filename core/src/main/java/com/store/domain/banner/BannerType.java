package com.store.domain.banner;

import com.core.web.freemarker.FreemarkerEnum;

/**
 * Created by laizy on 2017/6/14.
 */
@FreemarkerEnum(name = "BannerType")
public enum BannerType {
    PRODUCT("产品页"),
    HOME("首页");

    private String alias;

    BannerType(String alias) {
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }
}
