package com.store.controller;

import com.param.CompanyConfigParam;
import com.param.PortalConfigParam;
import com.param.PortalParam;
import com.param.PortalParamManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * Created by laizhiyang on 2017/6/17.
 */
@Controller
@RequestMapping("/contact")
public class ContactUsController extends BaseController {

    @Autowired
    private PortalParamManage portalParamManage;

    @RequestMapping("/index")
    public String contact(Model model) {
        Map<String, PortalParam> paramMap = portalParamManage.mapByKey(CompanyConfigParam.class);
        model.addAttribute("portalParam", paramMap.get(CompanyConfigParam.CONTACTUSCONTENT_PARAM_KEY));
        return "/store/contact/index";
    }
}
