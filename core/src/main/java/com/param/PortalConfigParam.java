
package com.param;

import com.core.annotation.ConfigParam;
import com.core.annotation.Param;
import org.springframework.stereotype.Component;

/**
 */
@Component
@ConfigParam(type = "systemConfig")
public class PortalConfigParam {
    @Param(key = "httpFileDir", description = "httpd的文件目录")
    public static String fileOptDir  = "";

    @Param(key = "fileDownLoadUrl", description = "文件下载地址")
    public static String fileDownLoadUrl = "http://172.16.0.101:10000";

}
