package com.privilege.system;

import com.core.dao.TransactionBaseService;
import com.core.system.App;
import com.privilege.security.ResourceCache;

import java.util.Map;

/**
 * Created by laizy on 2016/6/30.
 */
public class PrivilegeApp extends App {
    @Override
    public void contextInitialize() throws Throwable {
        // 加载privilegeService的dao
        Map<String, TransactionBaseService> privilegeMap = getBeansOfType(TransactionBaseService.class);
        for (TransactionBaseService baseService : privilegeMap.values()) {
            baseService.setBaseDao();
        }
        // 加载資源信息
        App.getBean(ResourceCache.class).init();
    }
}
