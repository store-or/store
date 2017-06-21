package com.store.controller;

import com.core.web.freemarker.FreemarkerParseException;
import com.store.domain.MenuDO;
import com.store.service.MenuService;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by laizhiyang on 2017/6/17.
 */
@Controller
@RequestMapping("/about")
public class AboutUsController extends BaseController {

    @Autowired
    private MenuService menuService;

    @RequestMapping("/index")
    public String about(Model model) {
        List<MenuDO> menuDOList = menuService.listByIndexAsc(Restrictions.isNotNull("publishContent"));
        if (CollectionUtils.isNotEmpty(menuDOList)){
            model.addAttribute("menuList", menuDOList);
        }
        return "/store/about/index";
    }


    @RequestMapping("/ajaxContent")
    @ResponseBody
    public String ajaxContent(Long menuId, Model model) throws FreemarkerParseException {
        MenuDO dbMenu = menuService.get(menuId);
        model.addAttribute("menu", dbMenu);
        return jsonResponseView("/store/about/show", model);
    }

}
