package com.store.system;

import com.core.dao.TransactionBaseService;
import com.core.system.App;

import java.util.Map;

/**
 */
public class StoreApp extends App {

    @Override
    public void contextInitialize() throws Throwable {
        // 加载crudService的dao
        Map<String, TransactionBaseService> crudMap = getBeansOfType(TransactionBaseService.class);
        for (TransactionBaseService baseService : crudMap.values()) {
            baseService.setBaseDao();
        }
        // 先加载配置信息
        getBean(StoreConfigInit.class).init();
        // 初始化httpClient配置池信息
        getBean(HttpJobInit.class).init();
    }
}