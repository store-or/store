package com.store.controller;

import com.core.controller.AbstractController;
import com.core.web.freemarker.FreemarkerEnumLoader;
import com.param.CompanyConfigParam;
import com.param.PortalConfigParam;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Map;

/**
 * Created by laizy on 2017/6/15.
 */
public abstract class BaseController extends AbstractController {
    @Autowired
    private FreemarkerEnumLoader freemarkerEnumLoader;

    @ModelAttribute(value="uploadFileContext")
    public String uploadFileContext(){
        return PortalConfigParam.fileDownLoadUrl;
    }

    @ModelAttribute
    public void addStaticsToFreemarker(Model model) {
        freemarkerEnumLoader.registerToModel(model);
        try {
            TemplateHashModel staticsModel = BeansWrapper.getDefaultInstance().getStaticModels();
            model.addAttribute("CompanyConfig", staticsModel.get(CompanyConfigParam.class.getName()));
            model.addAttribute("SystemConfig", staticsModel.get(PortalConfigParam.class.getName()));
        } catch (TemplateModelException e) {
            logger.error("ADD_CompanyConfigParam_failed", e);
        }
    }
}
