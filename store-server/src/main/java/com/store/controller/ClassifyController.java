package com.store.controller;

import com.core.json.JsonResponse;
import com.core.util.ValidatorUtil;
import com.core.web.freemarker.FreemarkerParseException;
import com.store.domain.ClassifyDO;
import com.store.service.ClassifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by laizhiyang on 2017/6/11.
 */
@Controller
@RequestMapping("/classify")
public class ClassifyController extends BaseController {

    @Autowired
    private ClassifyService classifyService;

    @RequestMapping("/list")
    public String list(Model model) {
        listModel(model);
        return "/store/classify/list";
    }

    @RequestMapping("/ajaxList")
    @ResponseBody
    public String ajaxList(Model model) throws FreemarkerParseException {
        listModel(model);
        return jsonResponseView("/store/classify/classifies", model);
    }

    private void listModel(Model model) {
        List<ClassifyDO> classifies = classifyService.listByIndexAsc();
        model.addAttribute("classifies", classifies);
    }

    @RequestMapping("/toAdd")
    @ResponseBody
    public String toAdd(Model model) throws FreemarkerParseException {
        return renderForm(new ClassifyDO(), model);
    }

    @RequestMapping("/toModify")
    @ResponseBody
    public String toModify(Long id, Model model) throws FreemarkerParseException {
        ClassifyDO classifyDO = classifyService.get(id);
        assertNotNull(classifyDO);
        return renderForm(classifyDO, model);
    }

    private String renderForm(ClassifyDO classifyDO, Model model) throws FreemarkerParseException {
        model.addAttribute("classify", classifyDO);
        return jsonResponseView("/store/classify/form", model);
    }

    @RequestMapping("/save")
    @ResponseBody
    public String save(@Valid ClassifyDO classifyDO, BindingResult result) {
        validUnique(classifyDO, null, result);
        if (result.hasErrors()) {
            return ValidatorUtil.errorToJson(JsonResponse.CODE_FAILURE, JsonResponse.MSG_ERROR, result).toString();
        }
        setDefault(classifyDO);
        classifyService.add(classifyDO);
        return JsonResponse.JSON_SUCCESS;
    }

    @RequestMapping("/modify")
    @ResponseBody
    public String modify(@Valid ClassifyDO classifyDO, BindingResult result) {
        ClassifyDO dbClasify = classifyService.get(classifyDO.getId());
        assertNotNull(dbClasify);
        validUnique(classifyDO, dbClasify, result);
        if (result.hasErrors()) {
            return ValidatorUtil.errorToJson(JsonResponse.CODE_FAILURE, JsonResponse.MSG_ERROR, result).toString();
        }
        setModify(classifyDO);
        classifyService.modify(dbClasify, classifyDO);
        return JsonResponse.JSON_SUCCESS;
    }

    private void validUnique(ClassifyDO current, ClassifyDO db, BindingResult result) {
        if (!result.hasFieldErrors("name") && !classifyService.isPropertyUnique("name", current.getName(), db == null ? null : db.getName())) {
            result.addError(new FieldError("classifyDO", "name", "名称不能重复"));
        }
    }

    @RequestMapping("/modifyIndex")
    @ResponseBody
    public String modifyIndex(Long id, Integer index) {
        ClassifyDO classifyDO = classifyService.get(id);
        assertNotNull(classifyDO);
        setModify(classifyDO);
        classifyService.modifyIndex(classifyDO, index);
        return JsonResponse.JSON_SUCCESS;
    }

    @RequestMapping("/del")
    @ResponseBody
    public String del(Long id) {
        classifyService.delete(id);
        return JsonResponse.JSON_SUCCESS;
    }

    private void assertNotNull (ClassifyDO classifyDO) {
        Assert.notNull(classifyDO, "分类不存在");
    }
}
