package com.store.service;

import com.core.dao.TransactionBaseService;
import com.google.common.collect.Lists;
import com.store.domain.BaseIndexDO;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by laizhiyang on 2017/6/11.
 */
public abstract class BaseIndexService<T extends BaseIndexDO> extends TransactionBaseService<T, Long> {

    @Transactional
    public void add(T t) {
        Integer max = getMaxIndex();
        t.setIndex(max == null ? 1 : (max.intValue() + 1));
        super.saveOrUpdate(t);
    }

    protected Integer getMaxIndex() {
        return (Integer) baseDao.createCriteria().setProjection(Projections.max("index")).uniqueResult();
    }

    protected void modifyIndex(List<T> list, T t, int index) {
        if (t.getIndex().equals(index)) {
            return;
        }
        List<T> changes = Lists.newArrayList();
        int from = 1;
        for (T tmp : list) {
            if (from == index) {
                from++;
            }
            if (tmp.getId().equals(t.getId())) {
                tmp.setIndex(index);
                tmp.copyModify(t);
                changes.add(tmp);
            } else {
                if (!tmp.getIndex().equals(from)) {
                    tmp.copyModify(t);
                    tmp.setIndex(from);
                    changes.add(tmp);
                }
                from++;
            }
        }
        super.batchSave(changes);
    }

    @Transactional
    public List<T> listByIndexAsc(Criterion... criterions) {
        return list(Order.asc("index"), criterions);
    }

    @Transactional
    public void modifyIndex(T t, int index) {
        modifyIndex(listByIndexAsc(), t, index);
    }

    @Transactional
    public void modify(T src, T to) {
        BeanUtils.copyProperties(to, src, new String[]{"id", "index"});
        saveOrUpdate(src);
    }

}
