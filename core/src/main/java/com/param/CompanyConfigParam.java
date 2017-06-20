
package com.param;

import com.core.annotation.ConfigParam;
import com.core.annotation.Param;
import org.springframework.stereotype.Component;

/**
 */
@Component
@ConfigParam(type = "companyConfig", system = 0)
public class CompanyConfigParam {
    public static final String PHONE_PARAM_KEY = "phone";
    public static final String ADDRESS_PARAM_KEY = "address";
    public static final String WECHAT_PARAM_KEY = "wechat";
    public static final String WEIBO_PARAM_KEY = "weibo";
    public static final String TMALL_PARAM_KEY = "tmall";
    public static final String ABOUTUS_PARAM_KEY = "aboutUs";
    public static final String ABOUTUSINTRO_PARAM_KEY = "introduction";
    public static final String CONTACTUS_PARAM_KEY = "contactUs";
    public static final String CONTACTUSCONTENT_PARAM_KEY = "contactUsContent";

    @Param(key = PHONE_PARAM_KEY, description = "电话：")
    public static String phone  = "";

    @Param(key = ADDRESS_PARAM_KEY, description = "地址：")
    public static String address = "";

    @Param(key = WECHAT_PARAM_KEY, description = "微信二维码：")
    public static String wechat = "";

    @Param(key = WEIBO_PARAM_KEY, description = "微博地址：")
    public static String weibo = "";

    @Param(key = TMALL_PARAM_KEY, description = "天猫地址：")
    public static String tmall = "";

    @Param(key = ABOUTUS_PARAM_KEY, description = "首页关于我们的图片：")
    public static String aboutUs = "";

    @Param(key = ABOUTUSINTRO_PARAM_KEY, description = "首页关于我们的简介：")
    public static String introduction = "";

    @Param(key = CONTACTUS_PARAM_KEY, description = "联系我们图片：")
    public static String contactUs = "";

    @Param(key = CONTACTUSCONTENT_PARAM_KEY, description = "联系我们内容：")
    public static String contactUsContent = "";
}
