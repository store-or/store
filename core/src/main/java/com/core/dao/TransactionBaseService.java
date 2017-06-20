package com.core.dao;

import com.core.dao.Page;
import com.core.dao.PropertyFilter;
import com.core.service.BaseService;
import org.apache.commons.lang.ArrayUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: hongxingshi
 * Date: 14-8-9
 * Time: 下午1:37
 * To change this template use File | Settings | File Templates.
 */
public class TransactionBaseService<T, PK extends Serializable> extends BaseService<T, PK> {

    @Resource(name = "transactionManager")
    protected PlatformTransactionManager transactionManager;

    @Override
    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public void list(final Page<T> page, List<PropertyFilter> propertyFilters, Criterion... criterions) {
        Criterion[] criterionArr = baseDao.buildFilterCriterions(propertyFilters);
        if (!ArrayUtils.isEmpty(criterions)) {
            criterionArr = (Criterion[]) ArrayUtils.addAll(criterionArr, criterions);
        }
        baseDao.find(page, criterionArr);
    }

    @SuppressWarnings("unchecked")
    public List<Map> executeSqlQuery(final String sql, final Object... parcms) {
        Query query = getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        for (int i = 0; i < parcms.length; i++) {
            query.setParameter(i, parcms[i]);
        }
        return query.list();
    }

    public boolean executeSql(final String sql, final Object... parcms) {
        Query query = getSession().createSQLQuery(sql);
        for (int i = 0; i < parcms.length; i++) {
            query.setParameter(i, parcms[i]);
        }
        int cnt = query.executeUpdate();
        return cnt > 0;
    }

    public List<Long> addSplit(String ids) {
        String[] idArray = ids.split(",");
        List<Long> data = new ArrayList<Long>();
        for (String s : idArray) {
            data.add(Long.valueOf(s));
        }
        return data;
    }

    @Transactional
    public List<T> list(Order order, Criterion... criterions) {
        return baseDao.createCriteria(criterions).addOrder(order).list();
    }

    @Transactional
    public List<T> list(Order[] orders, Criterion... criterions) {
        Criteria criteria = baseDao.createCriteria(criterions);
        if (orders != null){
            for (Order order : orders) {
                criteria.addOrder(order);
            }
        }
        return criteria.list();
    }

    @Transactional
    public void list(final Page<T> page, Criterion[] criterions, Order... orders) {
        Criteria c = baseDao.createCriteria(criterions);

        if (page.isAutoCount()) {
            long totalCount = baseDao.countCriteriaResult(c);
            page.setTotalCount(totalCount);
            if (totalCount == 0) {
                return;
            }
        }
        c.setFirstResult(page.getFirst() - 1);
        c.setMaxResults(page.getPageSize());
        if (!ArrayUtils.isEmpty(orders)) {
            for (Order order : orders) {
                c.addOrder(order);
            }
        }
        List result = c.list();
        page.setResult(result);
    }

    @Transactional
    public void list(final Page<T> page, List<PropertyFilter> propertyFilters) {
        setFirstByDynamicPage(page);
        baseDao.find(page, propertyFilters);
    }

    protected void setFirstByDynamicPage(Page page) {
        if (!page.isAutoCount() && page.getTotalCount() <= 0 && page.getPageNo() > 1) {
            // 1. 动态分页，未计算总条数(autoCount:false, totalCount <= 0)。pageSize=srcPageSize+1，为了区分有没有下一页
            //    所以first = (pageNo - 1) * (pageSize + 1) + 1 - (pageNo - 1)
            // 2. 动态分页，已计算出总跳数(autoCount:false, totalCount > 0)，则不需要修改pageSize
            page.setFirst(page.getFirst() - page.getPageNo() + 1);
        }
    }

    @Transactional
    public long count(Criterion... criterions) {
        return baseDao.countCriteriaResult(baseDao.createCriteria(criterions));
    }

    @Transactional
    public boolean isUnique(Criterion... criterions) {
        return count(criterions) == 0;
    }
}
