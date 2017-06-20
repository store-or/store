package com.param;

import com.core.dao.TransactionBaseService;
import com.google.common.collect.Maps;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 */
@Service
public class PortalParamService extends TransactionBaseService<PortalParam,Long> {

    @Override
    @Transactional
    public List<PortalParam> find(Criterion... criterions) {
        Criteria criteria = baseDao.createCriteria(criterions);
        criteria.addOrder(Order.asc("name"));
        //noinspection unchecked
        return criteria.list();
    }

    @Transactional
    public <T> Map<String, PortalParam> mapByKeyForType(String type) {
        List<PortalParam> params = find(Restrictions.eq("type", type));
        Map<String, PortalParam> paramMap = Maps.newHashMap();
        for (PortalParam param : params) {
            paramMap.put(param.getName(), param);
        }
        return paramMap;
    }
}
