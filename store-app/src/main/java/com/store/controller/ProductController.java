package com.store.controller;

import com.core.web.WebContext;
import com.store.domain.banner.BannerType;
import com.store.domain.product.ProductDO;
import com.store.domain.product.Status;
import com.store.exception.StoreException;
import com.store.service.BannerService;
import com.store.service.ClassifyService;
import com.store.service.ProductService;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by laizhiyang on 2017/6/17.
 */
@Controller
@RequestMapping("/product")
public class ProductController extends BaseController {
    @Autowired
    private ClassifyService classifyService;
    @Autowired
    private ProductService productService;
    @Autowired
    private BannerService bannerService;

    @RequestMapping("/")
    public String list(Model model) {
        return "forward:/index";
    }

    @RequestMapping("/index")
    public String index(Model model) {
        model.addAttribute("banners", bannerService.listByType(BannerType.PRODUCT));
        model.addAttribute("classifies", classifyService.listForApp());
        return "/store/product/list";
    }

    @RequestMapping("/{id}")
    public String detail(@PathVariable long id, Model model) throws IOException, StoreException {
        ProductDO productDO = productService.get(id);
        if (productDO == null) {
            WebContext.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND, "产品不存在");
            return "";
        }
        if (!Status.ON.getValue().equals(productDO.getStatus())) {
            throw new StoreException("产品已下架");
        }
        productDO.parse();
        if (productDO.getDetailDO() != null) {
            productDO.getDetailDO().parse();
        }
        model.addAttribute("product", productDO);
        return "/store/product/detail";
    }

}
