package com.store.service;

import com.core.dao.SQLCriterion;
import com.core.dao.TransactionBaseService;
import com.core.dao.Page;
import com.core.dao.PropertyFilter;
import com.store.domain.ClassifyDO;
import com.store.domain.product.ProductDO;
import com.store.domain.product.Status;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * Created by laizy on 2017/6/13.
 */
@Service
public class ProductService extends TransactionBaseService<ProductDO, Long> {
    @Autowired
    private RecommendService recommendService;

    @Transactional
    public void modifyTopTime(ProductDO productDO) {
        if (productDO.getTopTime() == null) {
            String hql = "update ProductDO set topTime=null,modifyTime=?,modifyUser=? where id=?";
            executeHql(hql, productDO.getModifyTime(), productDO.getModifyUser(), productDO.getId());
            return;
        }
        String hql = "update ProductDO set topTime=?,modifyTime=?,modifyUser=? where id=?";
        executeHql(hql, productDO.getTopTime(), productDO.getModifyTime(), productDO.getModifyUser(), productDO.getId());
    }

    @Transactional
    public void modifyStatus(ProductDO productDO) {
        if (Status.OFF.getValue().equals(productDO.getStatus())) {
            // 下架的产品，删除对应的推荐位
            recommendService.deleteByProductId(productDO.getId());
        }
        String hql = "update ProductDO set status=?,modifyTime=?,modifyUser=? where id=?";
        executeHql(hql, productDO.getStatus(), productDO.getModifyTime(), productDO.getModifyUser(), productDO.getId());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        recommendService.deleteByProductId(id);
        super.delete(id);
    }

    @Override
    @Transactional
    public ProductDO get(Long id) {
        ProductDO productDO = super.get(id);
        if (productDO == null) {
            return productDO;
        }
        Hibernate.initialize(productDO.getClassifies());
        Hibernate.initialize(productDO.getDetailDO());
        return productDO;
    }

    @Transactional
    public void listWithClassify(final Page<ProductDO> page, List<PropertyFilter> propertyFilters) {
        super.list(page, propertyFilters);
        if (CollectionUtils.isEmpty(page.getResult())) {
            return;
        }
        for (ProductDO productDO : page.getResult()) {
            Hibernate.initialize(productDO.getClassifies());
        }
    }

    @Transactional
    public void deleteByClassifyId(Long classifyId) {
        // 删除产品与分类关系表
        String sql = "delete from product_classify where classify_id=?";
        executeSql(sql, classifyId);
        String classify = ProductDO.CLASSIFY_ID_SPLIT + classifyId + ProductDO.CLASSIFY_ID_SPLIT;
        // 删除精品推荐
        sql = "delete from recommend r where exists(select id from product where id=r.product_id and classify_ids ~ ?)";
        executeSql(sql, classify);
        // 删除产品
        sql = "delete from product where classify_ids ~ ?";
        executeSql(sql, classify);
    }

    @Transactional
    public List<ProductDO> listByClassifyForApp(Collection<Long> classifyIds) {
        String condition = "'," + StringUtils.join(classifyIds, ",|,") + ",'";
        return super.list(new Order[]{Order.desc("topTime"), Order.desc("createTime")}, new SQLCriterion("classifyIds ~ " + condition, "classifyIds"), Restrictions.eq("status", Status.ON.getValue()));
    }
}
