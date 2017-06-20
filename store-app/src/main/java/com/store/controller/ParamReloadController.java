package com.store.controller;

import com.core.json.JsonResponse;
import com.param.PortalParamManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by laizy on 2017/6/20.
 */
@Controller()
@RequestMapping("/param")
public class ParamReloadController {
    @Autowired
    private PortalParamManage manage;

    @RequestMapping("/reload")
    @ResponseBody
    public String reload() {
        manage.init();
        return JsonResponse.MSG_SUCCESS;
    }
}
