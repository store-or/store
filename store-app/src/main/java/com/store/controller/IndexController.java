package com.store.controller;

import com.core.controller.AbstractController;
import com.store.domain.banner.BannerType;
import com.store.service.BannerService;
import com.store.service.RecommendService;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 */
@Controller
public class IndexController extends BaseController {

    @Autowired
    private BannerService bannerService;
    @Autowired
    private RecommendService recommendService;

    @RequestMapping("/")
    public String start() {
        return "forward:/index";
    }

    @RequestMapping("/index")
    public String index(Model model) {
        model.addAttribute("banners", bannerService.listByType(BannerType.HOME));
        model.addAttribute("recommends", recommendService.listByIndexAsc());
        return "/store/index";
    }
}
