package com.store.controller;

import com.core.dao.PropertyFilter;
import com.core.json.JsonMapper;
import com.core.json.JsonResponse;
import com.core.util.ValidatorUtil;
import com.core.web.freemarker.FreemarkerParseException;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.store.domain.RecommendDO;
import com.store.domain.product.ProductDO;
import com.store.domain.product.Status;
import com.store.service.RecommendService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by laizy on 2017/6/14.
 */
@RequestMapping("recommend")
@Controller
public class RecommendController extends BaseController {
    @Autowired
    private RecommendService recommendService;

    @RequestMapping("/list")
    public String list(Model model) {
        listModel(model);
        return "/store/recommend/list";
    }

    @RequestMapping("/ajaxList")
    @ResponseBody
    public String ajaxList(Model model) throws FreemarkerParseException {
        listModel(model);
        return jsonResponseView("/store/recommend/recommends", model);
    }

    private void listModel(Model model) {
        List<RecommendDO> recommends = recommendService.listByIndexAsc();
        model.addAttribute("recommends", recommends);
        List<PropertyFilter> propertyFilters = Lists.newArrayList();
        propertyFilters.add(new PropertyFilter("status", PropertyFilter.MatchType.EQ, Status.ON.getValue(), false, PropertyFilter.INTEGER_TYPE));
        if (CollectionUtils.isNotEmpty(recommends)) {
            Set<Long> ids = Sets.newHashSet();
            for (RecommendDO recommend : recommends) {
                ids.add(recommend.getProduct().getId());
            }
            propertyFilters.add(new PropertyFilter("id", PropertyFilter.MatchType.IN, ids, true, PropertyFilter.LONG_TYPE));
        }
        model.addAttribute("condition", JsonMapper.getDefault().toJson(propertyFilters));
    }

    @RequestMapping("/save")
    @ResponseBody
    public String save(@Valid RecommendDO recommendDO, BindingResult result) {
        validUnique(recommendDO, null, result);
        if (result.hasErrors()) {
            return ValidatorUtil.errorToJson(JsonResponse.CODE_FAILURE, JsonResponse.MSG_ERROR, result).toString();
        }
        setDefault(recommendDO);
        recommendService.add(recommendDO);
        return JsonResponse.JSON_SUCCESS;
    }

    private void validUnique(RecommendDO current, RecommendDO db, BindingResult result) {
        if (!result.hasFieldErrors("product") && !recommendService.isPropertyUnique("product.id", current.getProduct().getId(), db == null ? null : db.getProduct().getId())) {
            result.addError(new FieldError("recommend", "product", "产品不可重复添加推荐"));
        }
    }

    @RequestMapping("/batchSave")
    @ResponseBody
    public String batchSave(Long[] productIds) {
        if (ArrayUtils.isEmpty(productIds)) {
            return new JsonResponse(JsonResponse.CODE_FAILURE, "请选择产品").toString();
        }
        Collection<Long> pIds = Sets.newLinkedHashSet();
        pIds.addAll(Lists.newArrayList(productIds));
        pIds = recommendService.truncateExists(pIds);
        if (CollectionUtils.isEmpty(pIds)) {
            return JsonResponse.JSON_SUCCESS;
        }
        List<RecommendDO> recommends = Lists.newArrayList();
        for (Long pId : pIds) {
            RecommendDO recommendDO = new RecommendDO();
            ProductDO productDO = new ProductDO();
            productDO.setId(pId);
            recommendDO.setProduct(productDO);
            setDefault(recommendDO);
            recommends.add(recommendDO);
        }
        recommendService.batchSave(recommends);
        return JsonResponse.JSON_SUCCESS;
    }

    @RequestMapping("/modifyIndex")
    @ResponseBody
    public String modifyIndex(Long id, Integer index) {
        RecommendDO recommend = recommendService.get(id);
        assertNotNull(recommend);
        setModify(recommend);
        recommendService.modifyIndex(recommend, index);
        return JsonResponse.JSON_SUCCESS;
    }

    @RequestMapping("/del")
    @ResponseBody
    public String del(Long id) {
        recommendService.delete(id);
        return JsonResponse.JSON_SUCCESS;
    }

    private void assertNotNull (RecommendDO recommendDO) {
        Assert.notNull(recommendDO, "推荐位已不存在");
    }
}
