package com.store.controller;

import com.core.dao.HibernateUtil;
import com.core.dao.Page;
import com.core.dao.PropertyFilter;
import com.core.json.JsonResponse;
import com.core.util.ValidatorUtil;
import com.core.web.freemarker.FreemarkerParseException;
import com.google.common.collect.Lists;
import com.store.domain.ClassifyDO;
import com.store.domain.product.ProductDO;
import com.store.domain.product.Status;
import com.store.service.ClassifyService;
import com.store.service.ProductService;
import org.apache.commons.lang.StringUtils;
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
 * Created by laizy on 2017/6/13.
 */
@Controller
@RequestMapping("/product")
public class ProductController extends BaseController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ClassifyService classifyService;

    @RequestMapping("/list")
    public String list(Model model) {
        listModel(new Page<ProductDO>(), null, model);
        model.addAttribute("classifies", classifyService.listByIndexAsc());
        return "/store/product/list";
    }

    @ResponseBody
    @RequestMapping("/ajaxList")
    public String ajaxList(Page<ProductDO> page, String search, Model model) throws FreemarkerParseException {
        listModel(page, search, model);
        return jsonResponseView("/store/product/products", model);
    }

    private void listModel(Page<ProductDO> page, String search, Model model) {
        if (StringUtils.isBlank(page.getOrderBy())) {
            // 默认按置顶时间的降序
            page.setOrderBy("topTime,createTime");
            page.setOrder(Page.DESC + "," + Page.DESC);
        }
        productService.listWithClassify(page, HibernateUtil.buildPropertyFilters(search));
        model.addAttribute("page", page);
    }

    @RequestMapping("/toAdd")
    public String toAdd(Model model) {
        return renderForm(new ProductDO(), model);
    }

    @RequestMapping("/toModify")
    public String toModify(Long id, Model model) {
        ProductDO productDO = productService.get(id);
        assertProductExists(productDO);
        productDO.parse();
        return renderForm(productDO, model);
    }

    private String renderForm(ProductDO productDO, Model model) {
        model.addAttribute("classifies", classifyService.listByIndexAsc());
        model.addAttribute("product", productDO);
        return "/store/product/form";
    }

    @RequestMapping("/save")
    @ResponseBody
    public String save(@Valid ProductDO productDO, BindingResult result) {
        validUnique(productDO, null, result);
        if (result.hasErrors()) {
            return jsonValidate(result);
        }
        productDO.setStatus(Status.ON.getValue());
        setDefault(productDO);
        transform(productDO);
        productService.saveOrUpdate(productDO);
        return JsonResponse.JSON_SUCCESS;
    }

    @RequestMapping("/modify")
    @ResponseBody
    public String modify(@Valid ProductDO productDO, BindingResult result) {
        ProductDO db = productService.get(productDO.getId());
        assertProductExists(db);
        validUnique(productDO, db, result);
        if (result.hasErrors()) {
            return jsonValidate(result);
        }
        setModify(productDO);
        transform(productDO);
        productService.saveOrUpdate(productDO);
        return JsonResponse.JSON_SUCCESS;
    }

    private void transform(ProductDO productDO) {
        List<ClassifyDO> classifies = Lists.newArrayList();
        for (String classifyId : StringUtils.split(productDO.getClassifyIds(), ProductDO.CLASSIFY_ID_SPLIT)) {
            ClassifyDO classifyDO = new ClassifyDO();
            classifyDO.setId(Long.valueOf(classifyId));
            classifies.add(classifyDO);
        }
        productDO.setClassifies(classifies);
        productDO.transform();
    }

    private void validUnique(ProductDO current, ProductDO db, BindingResult result) {
        if (!result.hasFieldErrors("name") && !productService.isPropertyUnique("name", current.getName(), db == null ? null : db.getName())) {
            result.addError(new FieldError("productDO", "name", "名称不能重复"));
        }
    }

    @RequestMapping("/putOn")
    @ResponseBody
    public String putOn(ProductDO productDO) {
        productDO.setStatus(Status.ON.getValue());
        setModify(productDO);
        productService.modifyStatus(productDO);
        return JsonResponse.JSON_SUCCESS;
    }

    @RequestMapping("/putOff")
    @ResponseBody
    public String putOff(ProductDO productDO) {
        productDO.setStatus(Status.OFF.getValue());
        setModify(productDO);
        productService.modifyStatus(productDO);
        return JsonResponse.JSON_SUCCESS;
    }

    @RequestMapping("/setTop")
    @ResponseBody
    public String setTop(ProductDO productDO) {
        productDO.setTopTime(System.currentTimeMillis());
        setModify(productDO);
        productService.modifyTopTime(productDO);
        return JsonResponse.JSON_SUCCESS;
    }

    @RequestMapping("/unSetTop")
    @ResponseBody
    public String unSetTop(ProductDO productDO) {
        productDO.setTopTime(null);
        setModify(productDO);
        productService.modifyTopTime(productDO);
        return JsonResponse.JSON_SUCCESS;
    }

    @RequestMapping("/del")
    @ResponseBody
    public String del(Long id) {
        productService.delete(id);
        return JsonResponse.JSON_SUCCESS;
    }

    private void assertProductExists(ProductDO productDO) {
        Assert.notNull(productDO, "产品已失效，请刷新");
    }

    @ResponseBody
    @RequestMapping("/toChoose")
    public String toChoose(String condition, Model model) throws FreemarkerParseException {
        listChooseModel(new Page<ProductDO>(1, "chooseProductPage"), condition, null, model);
        model.addAttribute("classifies", classifyService.listByIndexAsc());
        return jsonResponseView("/store/product/choose/listForm", model);
    }

    @ResponseBody
    @RequestMapping("/ajaxChooseProducts")
    public String ajaxChooseProducts(Page<ProductDO> page, String condition, String search, Model model) throws FreemarkerParseException {
        listChooseModel(page, condition, search, model);
        return jsonResponseView("/store/product/choose/products", model);
    }

    private void listChooseModel(Page<ProductDO> page, String condition, String search, Model model) {
        if (StringUtils.isBlank(page.getOrderBy())) {
            // 默认按置顶时间的降序
            page.setOrderBy("topTime,createTime");
            page.setOrder(Page.DESC + "," + Page.DESC);
        }
        List<PropertyFilter> propertyFilters = HibernateUtil.buildPropertyFilters(search);
        propertyFilters.addAll(HibernateUtil.buildPropertyFilters(condition));
        productService.listWithClassify(page, propertyFilters);
        model.addAttribute("page", page);
    }
}
