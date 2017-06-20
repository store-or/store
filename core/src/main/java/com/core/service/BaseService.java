package com.core.service;

import com.core.dao.HibernateDao;
import com.core.dao.Page;
import com.core.dao.PropertyFilter;
import com.core.util.ReflectionUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.transform.ResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by laizy on 2017/6/7.
 */
public abstract class BaseService<T, PK extends Serializable> {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected HibernateDao<T, PK> baseDao;
    protected SessionFactory sessionFactory;
    protected Class<T> entityClass;

    @Transactional
    public T get(PK id) {
        return baseDao.get(id);
    }


    @Transactional
    public void saveOrUpdate(T t) {
        baseDao.saveOrUpdate(t);
    }

    @Transactional
    public void delete(PK id) {
        baseDao.delete(id);
    }

    @Transactional
    public void delete(T t) {
        baseDao.delete(t);
    }

    @Transactional
    public void merge(T t) {
        baseDao.merge(t);
    }

    @Transactional
    public void persist(T t) {
        baseDao.persist(t);
    }

    @Transactional
    public void list(final Page<T> page, List<PropertyFilter> propertyFilters) {
        baseDao.find(page, propertyFilters);
    }

    @Transactional
    public Session getSession() {
        return baseDao.getSession();
    }

    @Transactional
    public List<T> find(Criterion... criterions) {
        return baseDao.find(criterions);
    }

    @Transactional
    public List<T> find(ResultTransformer resultTransformer, Criterion... criterions) {
        return baseDao.find(resultTransformer, criterions);
    }

    @Transactional
    public T findUnique(Criterion... criterions) {
        return baseDao.findUnique(criterions);
    }

    @Transactional
    public void batchSave(Collection<T> ts) {
        baseDao.batchSave(ts);
    }

    /**
     * superClass的第一个泛型参数是 实体类的，如非这样，需自己重写
     */
    public void setBaseDao() {
        entityClass = ReflectionUtils.getSuperClassGenricType(getClass());
        baseDao = new HibernateDao<T, PK>(sessionFactory, entityClass);
    }

    public HibernateDao<T, PK> getBaseDao() {
        return baseDao;
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }

    @Transactional
    public boolean isPropertyUnique(String propertyName, Object newValue, Object oldValue) {
        return baseDao.isPropertyUnique(propertyName, newValue, oldValue);
    }

    @Transactional
    public void executeHql(final String hql, final Object... values) {
        baseDao.createQuery(hql, values).executeUpdate();
    }

    @Transactional
    public void executeHql(final String hql, Map<String, Object> values) {
        baseDao.createQuery(hql, values).executeUpdate();
    }
    @Transactional
    public List<T> find(final String hql, List<PropertyFilter> filterList,final Object... values) {
        return baseDao.find(hql,filterList,values);
    }
    @Transactional
    public Page<T> find(final Page<T> page, final String hql,List<PropertyFilter> filterList, final Object... values) {
        return baseDao.find(page,hql,filterList,values);
    }
    public abstract void setSessionFactory(SessionFactory sessionFactory);
}

