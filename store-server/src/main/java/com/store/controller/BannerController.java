package com.store.controller;

import com.core.json.JsonResponse;
import com.core.util.ValidatorUtil;
import com.core.web.freemarker.FreemarkerParseException;
import com.store.domain.banner.BannerDO;
import com.store.service.BannerService;
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
@RequestMapping("/banner")
public class BannerController extends BaseController {

    @Autowired
    private BannerService bannerService;

    @RequestMapping("/list")
    public String list(Model model) {
        listModel(model);
        return "/store/banner/list";
    }

    @RequestMapping("/ajaxList")
    @ResponseBody
    public String ajaxList(Model model) throws FreemarkerParseException {
        listModel(model);
        return jsonResponseView("/store/banner/banners", model);
    }

    private void listModel(Model model) {
        List<BannerDO> bannerDOs = bannerService.listByIndexAsc();
        model.addAttribute("banners", bannerDOs);
    }

    @RequestMapping("/toAdd")
    @ResponseBody
    public String toAdd(Model model) throws FreemarkerParseException {
        return renderForm(new BannerDO(), model);
    }

    @RequestMapping("/toModify")
    @ResponseBody
    public String toModify(Long id, Model model) throws FreemarkerParseException {
        BannerDO classifyDO = bannerService.get(id);
        assertNotNull(classifyDO);
        return renderForm(classifyDO, model);
    }

    private String renderForm(BannerDO bannerDO, Model model) throws FreemarkerParseException {
        model.addAttribute("banner", bannerDO);
        return jsonResponseView("/store/banner/form", model);
    }

    @RequestMapping("/save")
    @ResponseBody
    public String save(@Valid BannerDO bannerDO, BindingResult result) {
        validUnique(bannerDO, null, result);
        if (result.hasErrors()) {
            return ValidatorUtil.errorToJson(JsonResponse.CODE_FAILURE, JsonResponse.MSG_ERROR, result).toString();
        }
        setCreate(bannerDO);
        bannerService.add(bannerDO);
        return JsonResponse.JSON_SUCCESS;
    }

    @RequestMapping("/modify")
    @ResponseBody
    public String modify(@Valid BannerDO bannerDO, BindingResult result) {
        BannerDO dbBanner = bannerService.get(bannerDO.getId());
        assertNotNull(dbBanner);
        validUnique(bannerDO, dbBanner, result);
        if (result.hasErrors()) {
            return ValidatorUtil.errorToJson(JsonResponse.CODE_FAILURE, JsonResponse.MSG_ERROR, result).toString();
        }
        setModify(bannerDO);
        bannerService.modify(dbBanner, bannerDO);
        return JsonResponse.JSON_SUCCESS;
    }

    private void validUnique(BannerDO current, BannerDO db, BindingResult result) {
        if (!result.hasFieldErrors("name") && !bannerService.isPropertyUnique("name", current.getName(), db == null ? null : db.getName())) {
            result.addError(new FieldError("bannerDO", "name", "名称不能重复"));
        }
    }

    @RequestMapping("/modifyIndex")
    @ResponseBody
    public String modifyIndex(Long id, Integer index) {
        BannerDO bannerDO = bannerService.get(id);
        assertNotNull(bannerDO);
        setModify(bannerDO);
        bannerService.modifyIndex(bannerDO, index);
        return JsonResponse.JSON_SUCCESS;
    }

    @RequestMapping("/del")
    @ResponseBody
    public String del(Long id) {
        bannerService.delete(id);
        return JsonResponse.JSON_SUCCESS;
    }

    private void assertNotNull (BannerDO bannerDO) {
        Assert.notNull(bannerDO, "banner已不存在");
    }
}
