package com.store.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.store.domain.ClassifyDO;
import com.store.domain.product.ProductDO;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.Order;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by laizhiyang on 2017/6/11.
 */
@Service
public class ClassifyService extends BaseIndexService<ClassifyDO> {
    @Autowired
    private ProductService productService;

    @Override
    @Transactional
    public void delete(Long id) {
        productService.deleteByClassifyId(id);
        super.delete(id);
    }

    @Transactional
    public List<ClassifyDO> listForApp() {
        List<ClassifyDO> classifies = listByIndexAsc();
        if (CollectionUtils.isEmpty(classifies)) {
            return classifies;
        }
        Map<Long, ClassifyDO> classifyDOMap = Maps.newHashMap();
        for (ClassifyDO classify : classifies) {
            classify.setProducts(new ArrayList<ProductDO>());
            classifyDOMap.put(classify.getId(), classify);
        }
        List<ProductDO> productDOs = productService.listByClassifyForApp(classifyDOMap.keySet());
        for (ProductDO productDO : productDOs) {
            for (ClassifyDO classifyDO : productDO.getClassifies()) {
                classifyDOMap.get(classifyDO.getId()).getProducts().add(productDO);
            }
        }

        return classifies;
    }
}
