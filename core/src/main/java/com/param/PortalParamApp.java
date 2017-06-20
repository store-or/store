package com.param;

import com.core.dao.TransactionBaseService;
import com.core.system.App;

import java.util.Map;

/**
 * Created by laizy on 2016/6/30.
 */
public class PortalParamApp extends App {
    @Override
    public void contextInitialize() throws Throwable {
        Map<String, TransactionBaseService> portalBaseServices = getBeansOfType(TransactionBaseService.class);
        for (TransactionBaseService baseService : portalBaseServices.values()) {
            baseService.setBaseDao();
        }

        getBean(PortalParamManage.class).init();
    }
}
