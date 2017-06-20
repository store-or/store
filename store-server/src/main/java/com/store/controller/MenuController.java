package com.store.controller;


import com.core.json.JsonResponse;
import com.core.util.ValidatorUtil;
import com.core.web.freemarker.FreemarkerParseException;
import com.google.common.collect.Lists;
import com.store.domain.MenuDO;
import com.store.service.MenuService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

/**
 * Created by wangmj on 2017/6/15.
 */
@Controller
@RequestMapping("/menu")
public class MenuController extends BaseController {

    @Autowired
    private MenuService menuService;

    @RequestMapping("/list")
    public String list(Long menuId, Model model) {
        if (null != menuId){
            model.addAttribute("menuId", menuId);
        }
        model.addAttribute("menus", menuService.listByIndexAsc());
        return "/store/menu/list";
    }

    @RequestMapping("/ajaxContent")
    @ResponseBody
    public String ajaxContent(Long menuId, Model model) throws FreemarkerParseException{
        MenuDO dbMenu = menuService.get(menuId);
        assertNotNull(dbMenu);
        model.addAttribute("menu", dbMenu);
        return jsonResponseView("/store/menu/show", model);
    }

    @RequestMapping("/toAdd")
    @ResponseBody
    public String toAdd(Model model) throws FreemarkerParseException{
        return renderForm(new MenuDO(), model);
    }

    @RequestMapping("/toModify")
    @ResponseBody
    public String toModify(Long menuId, Model model) throws FreemarkerParseException {
        MenuDO menuDO = menuService.get(menuId);
        assertNotNull(menuDO);
        return renderForm(menuDO, model);
    }

    @RequestMapping("/save")
    @ResponseBody
    public String save(@Valid MenuDO menuDO, BindingResult result, String type, Model model) throws FreemarkerParseException{
        if (result.hasErrors()){
            return ValidatorUtil.errorToJson(JsonResponse.CODE_FAILURE, "error", result).toString();
        }
        MenuDO dbMenu;
        if (null != menuDO.getId()){
            dbMenu = menuService.get(menuDO.getId());
            assertNotNull(dbMenu);
            dbMenu.setCurrentContent(menuDO.getCurrentContent());
            setDefault(dbMenu);
        }else {
            dbMenu = menuDO;
            dbMenu.setIndex(menuService.getMaxIndex() + 1);
            setModify(dbMenu);
        }
        dbMenu.setCurrentVersion(generaVersion());
        if (StringUtils.equals(type, "PUBLIC")){
            dbMenu.setPublishVersion(dbMenu.getCurrentVersion());
            dbMenu.setPublishContent(dbMenu.getCurrentContent());
        }
        menuService.saveOrUpdate(dbMenu);
        return new JsonResponse<Long>(JsonResponse.CODE_SUCCESS, JsonResponse.MSG_SUCCESS, dbMenu.getId()).toString();
    }

    @RequestMapping("/publicVersion")
    @ResponseBody
    public String publicVersion(Long menuId){
        MenuDO dbMenu = menuService.get(menuId);
        assertNotNull(dbMenu);
        dbMenu.setPublishVersion(dbMenu.getCurrentVersion());
        dbMenu.setPublishContent(dbMenu.getCurrentContent());
        setModify(dbMenu);
        menuService.saveOrUpdate(dbMenu);
        return JsonResponse.JSON_SUCCESS;
    }

    @RequestMapping("/updateIndex")
    @ResponseBody
    public String updateIndex(Long menuId, String type){
        MenuDO menuDO = menuService.get(menuId);
        assertNotNull(menuDO);
        Integer index = menuDO.getIndex();
        Integer toIndex = -1;
        if (StringUtils.equals(type, "UP")){
            toIndex = index - 1;
        }else if(StringUtils.equals(type, "DOWN")){
            toIndex = index + 1;
        }
        MenuDO toMenu = menuService.findUnique(Restrictions.eq("index", toIndex));
        assertNotNull(toMenu);
        menuDO.setIndex(toIndex);
        toMenu.setIndex(index);
        setModify(menuDO);
        setModify(toMenu);
        menuService.batchSave(Lists.newArrayList(menuDO, toMenu));
        return JsonResponse.JSON_SUCCESS;
    }

    @RequestMapping("/del")
    @ResponseBody
    public String del(Long menuId){
        MenuDO menuDO = menuService.get(menuId);
        assertNotNull(menuDO);
        menuService.delete(menuDO);
        return JsonResponse.JSON_SUCCESS;
    }

    private String renderForm(MenuDO menuDO, Model model) throws FreemarkerParseException {
        model.addAttribute("menu", menuDO);
        return jsonResponseView("/store/menu/edit", model);
    }

    private String generaVersion() {
        return DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmssSSS");
    }

    private void assertNotNull (MenuDO menuDO) {
        Assert.notNull(menuDO, "菜单不存在");
    }

}
