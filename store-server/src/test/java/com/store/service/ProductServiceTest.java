package com.store.service;

import com.core.dao.Page;
import com.core.dao.PropertyFilter;
import com.store.BaseContextTest;
import com.store.domain.ClassifyDO;
import com.store.domain.product.ProductDO;
import com.store.domain.product.ProductDetailDO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by laizy on 2017/6/13.
 */
public class ProductServiceTest extends BaseContextTest {
    @Autowired
    private ProductService productService;

    @Test
    public void testGet() {
        List<ProductDO> products = productService.find();
        for (ProductDO product : products) {
            System.out.println(product.getName());
            for (ClassifyDO classify : product.getClassifies()) {
                System.out.println(classify.getName());
            }
        }
    }

    @Test
    public void list1() {
        Page<ProductDO> page = new Page<ProductDO>();
        productService.listWithClassify(page, new ArrayList<PropertyFilter>());
        for (ProductDO product : page.getResult()) {
            System.out.println(product.getName());
            for (ClassifyDO classify : product.getClassifies()) {
                System.out.println(classify.getName());
            }
        }
    }
}
